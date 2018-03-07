package us.jawsoft.web.CacheFilter;

import java.io.Serializable;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * An extension of Cache that implements a session binding listener,
 * and deletes it's entries when unbound.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public final class ServletCache
    extends Cache
    implements HttpSessionBindingListener, Serializable {

    // -------------------------------------------------------------- Constants
    // none


    // ----------------------------------------------------- Instance Variables

    /**
     * The scope of that cache.
     */
    private int scope;


    // ----------------------------------------------------------- Constructors

    /**
     * Create a new ServletCache
     *
     * @param mgr The CacheManager to administer this ServletCache.
     * @param scope The scope
     */
    public ServletCache(CacheManager mgr, int scope) {
        super(mgr);
        setScope(scope);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get the cache scope
     *
     * @return The cache scope
     */
    public int getScope() {
        return scope;
    }

    /**
     * When this Cache is bound to the session, do nothing.
     *
     * @param event The SessionBindingEvent.
     */
    public void valueBound(HttpSessionBindingEvent event) {
        // do nothing
    }

    /**
     * When the users's session ends, all listeners are finalized and the
     * session cache directory is deleted from disk.
     *
     * @param event The event that triggered this unbinding.
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        clear();
    }


    // ------------------------------------------------------ Protected Methods

    /**
     * Indicates whether or not the cache entry is stale. This overrides the
     * {@link Cache#isStale(CacheItem, int)} method to take into
     * account any flushing that may have been applied to the scope that this
     * cache belongs to.
     *
     * @param cacheEntry The cache entry to test the freshness of.
     * @param refreshPeriod The maximum allowable age of the entry, in seconds.
     * @param cronExpiry A cron expression that defines fixed expiry dates
     * and/or times for this cache entry.
     *
     * @return <code>true</code> if the entry is stale, <code>false</code>
     * otherwise.
     */
    protected boolean isStale(CacheItem cacheEntry,
                              int refreshPeriod,
                              String cronExpiry) {
        return super.isStale(cacheEntry, refreshPeriod) ||
                   mgr.isScopeFlushed(cacheEntry, scope);
    }


    // -------------------------------------------------------- Private Methods

    /**
     * Set the cache scope
     *
     * @param scope The cache scope
     */
    private void setScope(int scope) {
        this.scope = scope;
    }
}
