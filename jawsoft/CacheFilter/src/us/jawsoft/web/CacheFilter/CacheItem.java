package us.jawsoft.web.CacheFilter;

/**
 * A CacheItem is an object that is stored in the <code>Cache</code>.  It
 * contains information regarding the key for the <code>Cache</code>, the
 * contents, the creation time, last time this <code>CacheItem</code> was
 * last updated, etc.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheItem {

    // -------------------------------------------------------------- Constants

    /**
     * Value used for default initialization of the creation time and the last
     * update.  Basically a flag to signal that the value has not been set yet.
     */
    protected static final byte NOT_YET = -1;

    /**
     * A flag that ensures an entry does not become stale until it is either
     * explicitly flushed or expires.
     */
    public static final int NO_EXPIRATION = -1;

    // ----------------------------------------------------- Instance Variables

    /**
     *  The unique cache key for this entry.
     */
    protected String key;

    /**
     *  An optional identifier.
     */
    protected String id;

    /**
     * The actual content that is being cached.
     */
    protected Object content = null;

    /**
     * <code>true</code> if this entry was flushed
     */
    protected boolean wasFlushed = false;

    /**
     * The time this entry was created.
     */
    protected long createTime = NOT_YET;

    /**
     * The time this emtry was last updated.
     */
    protected long lastUpdateTime = NOT_YET;


    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new CacheItem using the key provided.
     *
     * @param key The key for this CacheItem
     */
    public CacheItem(String key) {
        setKey(key);
        setCreateTime(System.currentTimeMillis());
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Get the key of this CacheItem
     *
     * @return The key of this CacheItem
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Get the optional identifier for this CacheItem.
     *
     * @return The optional identifier.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets an optional id for this CacheItem.
     *
     * @param id An id to store for this CacheItem.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the cached content from this CacheItem.
     *
     * @return The content of this CacheItem.
     */
    public Object getContent() {
        return null;
    }

    /**
     * Sets the content that is being cached.
     *
     * @param content The content to store in this CacheItem.
     */
    public synchronized void setContent(Object content) {
        // nothing
    }

    /**
     * Get the date this CacheItem was created.
     *
     * @return The date this CacheItem was created in milliseconds.
     */
    public long getCreateTime() {
        return this.createTime;
    }

    /**
     * Get the date this CacheItem was last updated.
     *
     * @return The date this CacheItem was last updated in milliseconds.
     */
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**
     * Similar to a Unix touch command.  Refreshes the timestamp.
     */
    public void touch() {
        setLastUpdateTime(System.currentTimeMillis());
    }

    /**
     * Indicates whether this CacheItem is a freshly created one and
     * has not yet been assigned content or placed in a cache.
     *
     * @return <code>true</code> if this entry is newly created
     */
    public boolean isNew() {
        return this.lastUpdateTime == NOT_YET;
    }

    /**
     * Get the size of the cache entry in bytes (roughly).<p>
     *
     * @return The approximate size of the entry in bytes, or -1 if the
     * size could not be estimated.
     */
    public long getSize() {
        // a char is two bytes
        int size = (key.length() * 2) + 4;

        if (id != null) {
            size += ((id.length() * 2) + 4);
        }

        if (content != null) {
            if (content.getClass() == String.class) {
                size += ((content.toString().length() * 2) + 4);
            } else if (content instanceof CacheContent) {
                size += ((CacheContent) content).getSize();
            } else {
                return -1;
            }

            //add created, lastUpdate, and wasFlushed field sizes (1, 8, and 8)
            return size + 17;
        } else {
            return -1;
        }
    }

    /**
     * Flush the entry from cache.
     * <p/>
     * Note that flushing the cache doesn't actually remove the cache contents
     * it just tells the CacheEntry that it needs a refresh next time it is
     * asked this is so that the content is still there for a cache in use.
     */
    public void flush() {
        wasFlushed = true;
    }

    /**
     * Check if this CacheItem needs to be refreshed.
     *
     * @param refreshPeriod The period of refresh (in seconds).  A value of
     * {@link #NO_EXPIRATION} will result in the content never becoming
     * stale unless it is explicitly flushed. Passing in 0 will always result
     * in a refresh being required.
     *
     * @return <code>true</code>if this CacheItem needs refreshing,
     * <code>false</code> otherwise.
     */
    public boolean needsRefresh(int refreshPeriod) {
        boolean needsRefresh;

        // needs a refresh if it has never been updated
        if (lastUpdateTime == NOT_YET) {
            needsRefresh = true;
        } else if (wasFlushed) {
            // Was it flushed from cache?
            needsRefresh = true;
            wasFlushed = false;
        } else if (refreshPeriod == 0) {
            needsRefresh = true;
        } else if ((refreshPeriod >= 0) &&
                   (System.currentTimeMillis() >=
                              (lastUpdateTime + (refreshPeriod * 1000L)))) {
            // check if the last update + update period is in the past
            needsRefresh = true;
        } else {
            needsRefresh = false;
        }

        return needsRefresh;
    }


    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Sets the unique key for this CacheItem.
     *
     * @param key The key to store for this CacheItem.
     */
    private void setKey(String key) {
        this.key = key;
    }

    /**
     * Set the date this CacheItem was created.
     *
     * @param createTime The time (in milliseconds) this CacheItem was created.
     */
    private void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * Set the date this CacheItem was last updated.
     *
     * @param lastUpdateTime The time (in milliseconds) this CacheItem was
     * last updated.
     */
    private void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
