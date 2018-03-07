package us.jawsoft.web.CacheFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * <p>A <code>Cache</code> class is essentially a wrapper around the
 * <code>java.util.Hashtable</code> class that stores any number
 * <code>CacheItem</code> objects.</p>
 *
 * <p>The <code>Cache</code> class contains various methods for manipulating
 * the cache.</p>
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class Cache
    implements Serializable {

    // -------------------------------------------------------------- Constants

    /**
     * Represents a status code for a missing item in the cache.
     */
    private final int CACHE_ITEM_NOT_FOUND = -1;

    /**
     * Represents a status code for a found item in the cache.
     */
    private final int CACHE_ITEM_FOUND = 0;

    /**
     * Represents a status code for an item in the cache whose content needs
     * to be refreshed.
     */
    private final int CACHE_ITEM_NEEDS_REFRESH = 1;


    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>CacheManager</code> for cache utility functions.
     */
    protected CacheManager mgr = null;

    /**
     * The actual cache hashtable. This is where the cached objects are held.
     */
    private Map<String, CacheItem> cache = null;

    /**
     * Date of last complete cache flush.
     */
    private Date flushDateTime = null;

    /**
     * A set that holds keys of cache entries that are currently being built.
     * The cache checks against this map when a stale entry is requested.
     * If the requested key is in here, we know the entry is currently being
     * built by another thread and hence we can either block and wait or serve
     * the stale entry (depending on whether cache blocking is enabled or not).
     * <p>
     * We need to isolate these here since the actual CacheItem
     * objects may not normally be held in memory at all (eg, if no
     * memory cache is configured).
     */
    private Map<String, EntryUpdateState> updateStates = new HashMap<String, EntryUpdateState>();

    /**
     * Indicates whether the cache blocks requests until new content has
     * been generated or just serves stale content instead.
     */
    private boolean blocking = false;


    // ----------------------------------------------------------- Constructors

    /**
     * Create a new Cache.
     *
     * @param mgr A CacheManager for access to utility functions.
     */
    public Cache(CacheManager mgr) {
        cache = new Hashtable<String, CacheItem>();
        this.mgr = mgr;
        this.blocking = false;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Checks if the cache was flushed more recently than the CacheItem provided.
     * Used to determine whether to refresh the particular CacheItem.
     *
     * @param cacheItem The cache entry which we're seeing whether to refresh.
     *
     * @return Whether or not the cache has been flushed more recently than
     * this cache entry was updated.
     */
    public boolean isFlushed(CacheItem cacheItem) {
        boolean bRet = false;

        if (flushDateTime != null) {
            long lastUpdate = cacheItem.getLastUpdateTime();
            bRet = (flushDateTime.getTime() >= lastUpdate);
        }

        return bRet;
    }

    /**
     * Get an object from the cache specifying its key.
     *
     * @param key The key of the object in the cache.
     * @param refreshPeriod  How long before the object needs refresh. To
     * allow the object to stay in the cache indefinitely.
     *
     * @return The object from cache
     *
     * @throws NeedsRefreshException Thrown when the object either
     * doesn't exist, or exists but is stale. When this exception occurs,
     * the CacheItem corresponding to the supplied key will be locked
     * and other threads requesting this entry will potentially be blocked
     * until the caller repopulates the cache. If the caller choses not
     * to repopulate the cache, they <em>must</em> instead call
     * {@link #cancelUpdate(String)}.
     */
    public Object getFromCache(String key, int refreshPeriod)
        throws NeedsRefreshException {

        CacheItem cacheEntry = getCacheItem(key);
        Object content = cacheEntry.getContent();
        boolean reload = false;
        int accessEventType = CACHE_ITEM_FOUND;

        // Check if this entry has expired or has not yet been added to the
        // cache. If so, we need to decide whether to block, serve stale
        // content or throw a NeedsRefreshException
        if (isStale(cacheEntry, refreshPeriod)) {
            EntryUpdateState updateState = getUpdateState(key);

            synchronized (updateState) {
                if ((updateState.isAwaitingUpdate()) ||
                    (updateState.isCancelled())) {
                    // No one else is currently updating this entry so grab
                    // ownership
                    updateState.startUpdate();

                    if (cacheEntry.isNew()) {
                        accessEventType = CACHE_ITEM_NOT_FOUND;
                    } else {
                        accessEventType = CACHE_ITEM_NEEDS_REFRESH;
                    }
                } else if (updateState.isUpdating()) {
                    // Another thread is already updating the cache. We block
                    // if this is a new entry, or blocking mode is enabled.
                    // Either putInCache() or cancelUpdate() can cause this
                    // thread to resume.
                    if (cacheEntry.isNew() || blocking) {
                        do {
                            try {
                                updateState.wait();
                            } catch (InterruptedException e) {
                            }
                        } while (updateState.isUpdating());

                        if (updateState.isCancelled()) {
                            // The updating thread cancelled the update, let
                            // this one have a go.
                            updateState.startUpdate();

                            // We put the updateState object back into the
                            // updateStates hash so any remaining threads
                            // waiting on this cache entry will be notified
                            // once this thread has done its thing (either
                            // updated the cache or cancelled the update).
                            // Without this code they'll get left hanging...
                            synchronized (updateStates) {
                                updateStates.put(key, updateState);
                            }

                            if (cacheEntry.isNew()) {
                                accessEventType = CACHE_ITEM_NOT_FOUND;
                            } else {
                                accessEventType = CACHE_ITEM_NEEDS_REFRESH;
                            }
                        } else if (updateState.isComplete()) {
                            reload = true;
                        } 
                    }
                } else {
                    reload = true;
                }
            }
        }

        // If reload is true then another thread must have successfully rebuilt
        // the cache entry
        if (reload) {
            cacheEntry = (CacheItem) cache.get(key);

            if (cacheEntry != null) {
                content = cacheEntry.getContent();
            }
            // else {
                // Could not reload cache entry after waiting for it to be
                // rebuilt
            // }
        }


        // If we didn't end up getting a hit then we need to throw a NRE
        if (accessEventType != CACHE_ITEM_FOUND) {
            throw new NeedsRefreshException(content);
        }

        return content;
    }


    /**
     * Cancels any pending update for this cache entry. This should
     * <em>only</em> be called by the thread that is responsible for performing
     * the update ie the thread that received the original
     * {@link NeedsRefreshException}.
     * <p/>
     * If a cache entry is not updated (via {@link #putInCache} and this method
     * is not called to let the cache know the update will not be forthcoming,
     * subsequent requests for this cache entry will either block indefinitely
     * (if this is a new cache entry or cache.blocking=true), or forever get
     * served stale content. Notehowever that there is no harm in cancelling an
     * update on a key that either does not exist or is not currently being
     * updated.
     *
     * @param key The key for the cache entry in question.
     */
    public void cancelUpdate(String key) {
        EntryUpdateState state;

        if (key != null) {
            synchronized (updateStates) {
                state = (EntryUpdateState) updateStates.remove(key);

                if (state != null) {
                    synchronized (state) {
                        state.cancelUpdate();
                        state.notify();
                    }
                }
            }
        }
    }

    /**
     * Flush all entries in the cache now.
     *
     */
    public void flushAll() {
        flushAll(new Date());
    }

    /**
     * Flush all entries in the cache on the given date/time.
     *
     * @param date The date at which all cache entries will be flushed.
     */
    public void flushAll(Date date) {
        flushDateTime = date;
    }

    /**
     * Flush the cache entry (if any) that corresponds to the cache key
     * supplied. This call will flush the entry from the cache and remove the
     * references to it from any cache groups that it is a member of.
     *
     * @param key The key of the entry to flush.
     */
    public void flushEntry(String key) {
        flushEntry(getCacheItem(key));
    }

    /**
     * Put an object in the cache specifying the key to use.
     *
     * @param key Key of the object in the cache.
     * @param content The object to cache.
     */
    public void putInCache(String key, Object content) {
        putInCache(key, content, null);
    }

    /**
     * Put an object in the cache specifying the key to use.
     *
     * @param key Key of the object in the cache.
     * @param content The object to cache.
     * @param id An optional identifier
     */
    public void putInCache(String key, Object content, String id) {
        CacheItem cacheItem = getCacheItem(key);
        cacheItem.setContent(content);
        cacheItem.setId(id);
        cache.put(key, cacheItem);

        // Signal to any threads waiting on this update that it's now ready for
        // them in the cache!
        completeUpdate(key);
    }

    /**
     * Get the elements in the cache.
     *
     * @return AN enumeration of CacheItems
     */
    public List<CacheItem> getCacheItems() {
        return new ArrayList<CacheItem>(cache.values());
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Get an entry from this cache or create one if it doesn't exist.
     *
     * @param key The key of the cache entry
     *
     * @return CacheItem for the specified key.
     */
    protected CacheItem getCacheItem(String key) {
        // Verify that the key is valid
        if ((key == null) || (key.length() == 0)) {
            throw new IllegalArgumentException("getCacheItem called with " +
                                               "an empty or null key");
        }

        CacheItem cacheEntry = (CacheItem)cache.get(key);

        // if the cache entry does not exist, create a new one
        if (cacheEntry == null) {

            // Determine which type of cache this is, in-memory or on-disk
            if (mgr.useMemoryCaching()) {
                cacheEntry = new CacheItemMemory(key);
            } else {
                cacheEntry = new CacheItemDisk(key, mgr.getCachePath());
            }
        }

        return cacheEntry;
    }

    /**
     * Indicates whether or not the cache entry is stale.
     *
     * @param cacheItem The cache entry to test the freshness of.
     * @param refreshPeriod The maximum allowable age of the entry, in seconds.
     *
     * @return <code>true</code> if the entry is stale, <code>false</code>
     * otherwise.
     */
    protected boolean isStale(CacheItem cacheItem, int refreshPeriod) {
        boolean result = cacheItem.needsRefresh(refreshPeriod) ||
                         isFlushed(cacheItem);
        return result;
    }

    /**
     * Get the updating cache entry from the update map. If one is not found,
     * create a new one (with state {@link EntryUpdateState#NOT_YET_UPDATING})
     * and add it to the map.
     *
     * @param key The cache key for this entry.
     *
     * @return the CacheItem that was found (or added to) the updatingEntries
     * map.
     */
    protected EntryUpdateState getUpdateState(String key) {
        EntryUpdateState updateState;

        synchronized (updateStates) {
            // Try to find the matching state object in the updating entry map.
            updateState = (EntryUpdateState) updateStates.get(key);

            if (updateState == null) {
                // It's not there so add it.
                updateState = new EntryUpdateState();
                updateStates.put(key, updateState);
            }
        }

        return updateState;
    }

    /**
     * Completely clears the cache.
     */
    protected void clear() {
        cache.clear();
    }

    /**
     * Removes the update state for the specified key and notifies any other
     * threads that are waiting on this object. This is called automatically
     * by the {@link #putInCache} method.
     *
     * @param key The cache key that is no longer being updated.
     */
    protected void completeUpdate(String key) {
        EntryUpdateState state;

        synchronized (updateStates) {
            state = (EntryUpdateState) updateStates.remove(key);

            if (state != null) {
                synchronized (state) {
                    state.completeUpdate();
                    state.notifyAll();
                }
            }
        }
    }

    /**
     * Completely removes a cache entry from the cache and its associated cache
     * groups.
     *
     * @param key The key of the entry to remove.
     */
    protected void removeEntry(String key) {
        cache.remove(key);
    }



    // -------------------------------------------------------- Private Methods

    /**
     * Flush a cache entry.
     *
     * @param entry The entry to flush
     */
    private void flushEntry(CacheItem entry) {
        String key = entry.getKey();

        // Flush the object itself
        entry.flush();

        if (!entry.isNew()) {
            // Update the entry's state in the map
            cache.put(key, entry);
        }
    }
}
