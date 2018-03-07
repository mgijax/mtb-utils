package us.jawsoft.web.CacheFilter;

import java.io.Serializable;

/**
 * A CacheItemMemory extends <code>CacheItem</code> for specific use of
 * storing a <code>CacheItem</code> in memory.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheItemMemory
    extends CacheItem
    implements Serializable {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new CacheItem using the key provided.
     *
     * @param key The key for this CacheItem
     */
    public CacheItemMemory(String key) {
        super(key);
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Get the cached content from this CacheItem.
     *
     * @return The content of this CacheItem.
     */
    public Object getContent() {
        return this.content;
    }

    /**
     * Sets the content that is being cached.
     *
     * @param content The content to store in this CacheItem.
     */
    public synchronized void setContent(Object content) {
        this.content = content;
        touch();
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
