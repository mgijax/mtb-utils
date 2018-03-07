package us.jawsoft.web.CacheFilter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Locale;
import javax.servlet.ServletResponse;




/**
 * Holds the servlet response in a byte array so that it can be held
 * in the cache (and, since this class is serializable, optionally
 * persisted to disk).
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheContent implements Serializable {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables
    
    /**
     * A byte stream.
     */
    private transient ByteArrayOutputStream bout 
                             = new ByteArrayOutputStream(1000);
    
    /**
     * The locale.
     */
    private Locale locale = null;
    
    /**
     * The content type.
     */
    private String contentType = null;
    
    /**
     * The actual content stored as an array.
     */
    private byte[] content = null;
    
    
    // ----------------------------------------------------------- Constructors
    // none
    
    
    // --------------------------------------------------------- Public Methods

    /**
     * Set the content type. We capture this so that when we serve this
     * data from cache, we can set the correct content type on the response.
     *
     * @param contentType The content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Set the Locale. We capture this so that when we serve this data from
     * cache, we can set the correct locale on the response.
     *
     * @param locale The locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Get an output stream. This is used by the 
     * {@link CacheServletOutputStream} to capture the original (uncached) 
     * response into a byte array.
     *
     * @return The stream
     */
    public OutputStream getOutputStream() {
        return bout;
    }

    /**
     * Gets the size of this cached content.
     *
     * @return The size of the content, in bytes. If no content
     * exists, this method returns <code>-1</code>.
     */
    public int getSize() {
        return (content == null) ? -1 : content.length;
    }

    /**
     * Called once the response has been written in its entirety. This
     * method commits the response output stream by converting the output
     * stream into a byte array.
     */
    public void commit() {
        content = bout.toByteArray();
    }

    /**
     * Writes this cached data out to the supplied 
     * <code>ServletResponse</code>.
     *
     * @param response The servlet response to output the cached content to.
     *
     * @throws IOException
     */
    public void writeTo(ServletResponse response) throws IOException {
        //Send the content type and data to this response
        if (contentType != null) {
            response.setContentType(contentType);
        }

        response.setContentLength(content.length);

        if (locale != null) {
            response.setLocale(locale);
        }

        OutputStream out = 
            new BufferedOutputStream(response.getOutputStream());
        
        out.write(content);
        out.flush();
    }
    
    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
    
}
