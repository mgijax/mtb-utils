package us.jawsoft.web.CacheFilter;

/**
 * This exception is thrown when retrieving an item from cache and it is
 * expired.
 * <p>
 * Note that for fault tolerance purposes, it is possible to retrieve the
 * current cached object from the exception.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public final class NeedsRefreshException 
    extends Exception {

    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables
    
    /**
     * Current object in the cache
     */
    private Object cacheContent = null;


    // ----------------------------------------------------------- Constructors 
    
    /**
     * Create a NeedsRefreshException
     */
    public NeedsRefreshException(Object cacheContent) {
        super();
        this.cacheContent = cacheContent;
    }

    // --------------------------------------------------------- Public Methods 

    /**
     * Retrieve current object in the cache.
     *
     * @return The content
     */
    public Object getCacheContent() {
        return cacheContent;
    }
    
    // ------------------------------------------------------ Protected Methods 
    // none
    
    // -------------------------------------------------------- Private Methods 
    // none
}
