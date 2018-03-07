/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/compression/CompressionResponseStream.java,v 1.1 2008/07/17 17:03:29 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.webutils.filters.compression;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of <b>ServletOutputStream</b> that works with
 * the CompressionServletResponseWrapper implementation.
 */

public class CompressionResponseStream
    extends ServletOutputStream {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Response.
     *
     * @param response The associated response
     */
    public CompressionResponseStream(HttpServletResponse response) 
        throws IOException{

        super();
        bClosed = false;
        this.response = response;
        this.output = response.getOutputStream();

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The threshold number which decides to compress or not.
     * Users can configure in web.xml to set it to fit their needs.
     */
    protected int nCompressionThreshold = 0;

    /**
     * Debug level
     */
    private int nDebug = 0;

    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] arrBuffer = null;

    /**
     * The number of data bytes currently in the buffer.
     */
    protected int nBufferCount = 0;

    /**
     * The underlying gzip output stream to which we should write data.
     */
    protected GZIPOutputStream gzipStream = null;

    /**
     * Has this stream been closed?
     */
    protected boolean bClosed = false;

    /**
     * The content length past which we will not write, or -1 if there is
     * no defined content length.
     */
    protected int nLength = -1;

    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;

    /**
     * The underlying servket output stream to which we should write data.
     */
    protected ServletOutputStream output = null;


    // --------------------------------------------------------- Public Methods

    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.nDebug = debug;
    }


    /**
     * Set the compressionThreshold number and create buffer for this size
     */
    protected void setBuffer(int threshold) {
        nCompressionThreshold = threshold;
        arrBuffer = new byte[nCompressionThreshold];
        
        if (nDebug > 1) {
            System.out.println("buffer is set to "+nCompressionThreshold);
        }
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {
        if (nDebug > 1) {
            System.out.println("close() @ CompressionResponseStream");
        }
        
        if (bClosed) {
            throw new IOException("This output stream has been closed");
        }

        if (gzipStream != null) {
            flushToGZip();
            gzipStream.close();
            gzipStream = null;
        } else {
            if (nBufferCount > 0) {
                if (nDebug > 2) {
                    System.out.print("output.write(");
                    System.out.write(arrBuffer, 0, nBufferCount);
                    System.out.println(")");
                }
                output.write(arrBuffer, 0, nBufferCount);
                nBufferCount = 0;
            }
        }

        output.close();
        bClosed = true;

    }


    /**
     * Flush any buffered data for this output stream, which also causes the
     * response to be committed.
     */
    public void flush() throws IOException {

        if (nDebug > 1) {
            System.out.println("flush() @ CompressionResponseStream");
        }
        if (bClosed) {
            throw new IOException("Cannot flush a closed output stream");
        }

        if (gzipStream != null) {
            gzipStream.flush();
        }

    }

    public void flushToGZip() throws IOException {

        if (nDebug > 1) {
            System.out.println("flushToGZip() @ CompressionResponseStream");
        }
        if (nBufferCount > 0) {
            if (nDebug > 1) {
                System.out.println("flushing out to GZipStream, bufferCount = "
                                   + nBufferCount);
            }
            writeToGZip(arrBuffer, 0, nBufferCount);
            nBufferCount = 0;
        }

    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {

        if (nDebug > 1) {
            System.out.println("write "+b+" in CompressionResponseStream ");
        }
        if (bClosed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        if (nBufferCount >= arrBuffer.length) {
            flushToGZip();
        }

        arrBuffer[nBufferCount++] = (byte) b;

    }


    /**
     * Write b.length bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {

        write(b, 0, b.length);

    }


    /**
     * Write len bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {

        if (nDebug > 1) {
            System.out.println("write, bufferCount = " + nBufferCount + 
                               " len = " + len + " off = " + off);
        }
        if (nDebug > 2) {
            System.out.print("write(");
            System.out.write(b, off, len);
            System.out.println(")");
        }

        if (bClosed) {
            throw new IOException("Cannot write to a closed output stream");
        }

        if (len == 0) {
            return;
        }

        // Can we write into buffer ?
        if (len <= (arrBuffer.length - nBufferCount)) {
            System.arraycopy(b, off, arrBuffer, nBufferCount, len);
            nBufferCount += len;
            return;
        }

        // There is not enough space in buffer. Flush it ...
        flushToGZip();

        // ... and try again. Note, that bufferCount = 0 here !
        if (len <= (arrBuffer.length - nBufferCount)) {
            System.arraycopy(b, off, arrBuffer, nBufferCount, len);
            nBufferCount += len;
            return;
        }

        // write direct to gzip
        writeToGZip(b, off, len);
    }

    public void writeToGZip(byte b[], int off, int len) throws IOException {

        if (nDebug > 1) {
            System.out.println("writeToGZip, len = " + len);
        }
        if (nDebug > 2) {
            System.out.print("writeToGZip(");
            System.out.write(b, off, len);
            System.out.println(")");
        }
        if (gzipStream == null) {
            if (nDebug > 1) {
                System.out.println("new GZIPOutputStream");
            }
            response.addHeader("Content-Encoding", "gzip");
            gzipStream = new GZIPOutputStream(output);
        }
        gzipStream.write(b, off, len);

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Has this response stream been closed?
     */
    public boolean closed() {
        return this.bClosed;
    }
}
