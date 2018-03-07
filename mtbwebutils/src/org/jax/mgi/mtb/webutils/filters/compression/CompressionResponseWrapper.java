/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/compression/CompressionResponseWrapper.java,v 1.2 2008/12/18 19:48:08 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.webutils.filters.compression;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with
 * the CompressionServletResponseStream implementation..
 */

public class CompressionResponseWrapper extends HttpServletResponseWrapper {

    // ----------------------------------------------------- Constructor

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */

    public CompressionResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
        if (nDebug > 1) {
            System.out.println("CompressiontResponseWrapper constructor");
        }
    }


    // ----------------------------------------------------- Instance Variables

    /**
     * Original response
     */

    protected HttpServletResponse origResponse = null;

    /**
     * Descriptive information about this Response implementation.
     */

    protected static final String info = "CompressiontResponseWrapper";

    /**
     * The ServletOutputStream that has been returned by
     * getOutputStream(), if any.
     */

    protected ServletOutputStream stream = null;


    /**
     * The PrintWriter that has been returned by
     * getWriter(), if any.
     */

    protected PrintWriter writer = null;

    /**
     * The threshold number to compress
     */
    protected int nThreshold = 0;

    /**
     * Debug level
     */
    private int nDebug = 0;

    /**
     * Content type
     */
    protected String strContentType = null;

    // --------------------------------------------------------- Public Methods


    /**
     * Set content type
     */
    public void setContentType(String contentType) {
        if (nDebug > 1) {
            System.out.println("setContentType to "+contentType);
        }
        this.strContentType = contentType;
        origResponse.setContentType(contentType);
    }


    /**
     * Set threshold number
     */
    public void setCompressionThreshold(int threshold) {
        if (nDebug > 1) {
            System.out.println("setCompressionThreshold to " + threshold);
        }
        this.nThreshold = threshold;
    }


    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.nDebug = debug;
    }


    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        if (nDebug > 1) {
            System.out.println("createOutputStream gets called");
        }

        CompressionResponseStream stream = 
                new CompressionResponseStream(origResponse);
        stream.setDebugLevel(nDebug);
        stream.setBuffer(nThreshold);

        return stream;

    }


    /**
     * Finish a response.
     */
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
        }
    }


    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
        if (nDebug > 1) {
            System.out.println("flush buffer @ CompressiontResponseWrapper");
        }
        if(stream != null){
          ((CompressionResponseStream)stream).flush();
        }

    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException if getWriter has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            throw new IllegalStateException("getWriter() already called");
        }

        if (stream == null) {
            stream = createOutputStream();
        }
        
        if (nDebug > 1) {
            System.out.println("stream set to "+stream+" in getOutputStream");
        }

        return stream;

    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if getOutputStream has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (writer != null) {
            return writer;
        }

        if (stream != null) {
           throw new IllegalStateException("getOutputStream() already called");
        }

        stream = createOutputStream();
        if (nDebug > 1) {
            System.out.println("stream is set to "+stream+" in getWriter");
        }
        //String charset = getCharsetFromContentType(contentType);
        String charEnc = origResponse.getCharacterEncoding();
        if (nDebug > 1) {
            System.out.println("character encoding is " + charEnc);
        }
        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
        } else {
            writer = new PrintWriter(stream);
        }
        
        return writer;

    }


    public void setContentLength(int length) {
    }


    /**
     * Returns character from content type. This method was taken from tomcat.
    private static String getCharsetFromContentType(String type) {

        if (type == null) {
            return null;
        }
        int semi = type.indexOf(';');
        if (semi == -1) {
            return null;
        }
        String afterSemi = type.substring(semi + 1);
        int charsetLocation = afterSemi.indexOf("charset=");
        if(charsetLocation == -1) {
            return null;
        } else {
            return afterSemi.substring(charsetLocation + 8).trim();
        }
    }
     */
}
