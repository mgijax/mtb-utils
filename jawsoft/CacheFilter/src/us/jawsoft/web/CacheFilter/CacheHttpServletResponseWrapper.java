package us.jawsoft.web.CacheFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * CacheServletResponse is a serialized representation of a response.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheHttpServletResponseWrapper 
    extends HttpServletResponseWrapper {

    // -------------------------------------------------------------- Constants
    // none
    

    // ----------------------------------------------------- Instance Variables

    /**
     * We cache the printWriter so we can maintain a single instance
     * of it no matter how many times it is requested.
     */
    private PrintWriter cachedWriter;
    private CacheContent result = null;
    private CacheServletOutputStream cacheOut = null;
    private int status = SC_OK;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor
     *
     * @param response The servlet response
     */
    public CacheHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        result = new CacheContent();
    }


    // --------------------------------------------------------- Public Methods
    
    /**
     * Get a response content
     *
     * @return The content
     */
    public CacheContent getContent() {
        //Create the byte array
        result.commit();

        //Return the result from this response
        return result;
    }

    /**
     * Set the content type
     *
     * @param value The content type
     */
    public void setContentType(String value) {
        super.setContentType(value);
        result.setContentType(value);
    }

    /**
     * Set the date of a header
     *
     * @param name The header name
     * @param value The date
     */
    public void setDateHeader(String name, long value) {
        super.setDateHeader(name, value);
    }

    /**
     * We override this so we can catch the response status. Only
     * responses with a status of 200 (<code>SC_OK</code>) will
     * be cached.
     */
    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only
     * responses with a status of 200 (<code>SC_OK</code>) will
     * be cached.
     */
    public void sendError(int status, String string) throws IOException {
        super.sendError(status, string);
        this.status = status;
    }

    /**
     * We override this so we can catch the response status. Only
     * responses with a status of 200 (<code>SC_OK</code>) will
     * be cached.
     */
    public void sendError(int status) throws IOException {
        super.sendError(status);
        this.status = status;
    }

    /**
     * Sends a temporary redirect response to the client using the 
     * specified redirect location URL.
     *
     * @parem location the location
     */
    public void sendRedirect(String location) throws IOException {
        this.status = SC_MOVED_TEMPORARILY;
        super.sendRedirect(location);
    }

    /**
     * Retrieves the captured HttpResponse status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the locale
     *
     * @param value The locale
     */
    public void setLocale(Locale value) {
        super.setLocale(value);
        result.setLocale(value);
    }

    /**
     * Get an output stream
     *
     * @throws IOException
     */
    public ServletOutputStream getOutputStream() throws IOException {
        // Pass this faked servlet output stream that captures what is sent
        if (cacheOut == null) {
            cacheOut = new CacheServletOutputStream(result.getOutputStream(), 
                                                    super.getOutputStream());
        }

        return cacheOut;
    }

    /**
     * Get a print writer
     *
     * @throws IOException
     */
    public PrintWriter getWriter() throws IOException {
        if (cachedWriter == null) {
            cachedWriter = new PrintWriter(getOutputStream());
        }

        return cachedWriter;
    }

    public void flushBuffer() throws IOException {
        super.flushBuffer();

        if (cacheOut != null) {
            cacheOut.flush();
        }

        if (cachedWriter != null) {
            cachedWriter.flush();
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

}
