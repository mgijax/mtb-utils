/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/URLDownload.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
 
/**
 * Essentially a JavaBean for the UrlDownloader class to use.
 *
 * @author mjv
 * @date 2007/04/30 15:52:19
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/URLDownload.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * @see java.lang.Thread
 */
public class URLDownload {
    //--------------------------------------------------------------- Constants

    /**
     * The name of the class.
     */
    private static final String className = URLDownload.class.getName();

    /**
     * For out.println() instead of System.out.println()
     */
    private static final PrintStream out = System.out;
  
    //------------------------------------------------------ Instance Variables

    /**
     * <code>TRUE</code> for debug mode, <code>false</code> otherwise
     */
    private static boolean debugMode = false;
    
    /**
     * <code>TRUE</code> to show output, <code>false</code> otherwise
     */
    private static boolean showOutput = false;
    
    /**
     * Name of the file to save the url contents to.
     */
    private static String fileName;

    /**
     * The content of the requested URL.
     */
    private byte[] content;

    /**
     * The requested URL.
     */
    private String url; 
    
    /**
     * The content type of the requested URL.
     */
    private String contentType;

    /**
     * The specified content length of the requested URL.
     */
    private long contentLength;
    
    /**
     * The size in bytes of what was downloaded.
     */
    private long size;
    
    /**
     * The response message from the server.
     */
    private String responseMessage;

    /**
     * The response code from the server.
     */
    private int responseCode;
    
    //------------------------------------------------------------ Constructors

    /**
     * Constructor.
     */
    public URLDownload() {
        setSize(-1);
        setContentLength(-1);
        setResponseCode(-1);
        setResponseMessage("");
        setContentType("");
    }
    
    //---------------------------------------------------------- Public Methods

    /**
     * Get the URL
     *
     * @return A <code>String</code> representation of the url.
     */
    public String getURL() {
        return this.url;
    }
    
    /**
     * Get the server specified content type.
     *
     * @return The content type
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Get the server specified content length.
     *
     * @return The content length
     */
    public long getContentLength() {
        return this.contentLength;
    }

    /**
     * Get the number of bytes that were downloaded.
     *
     * @return The number of bytes
     */
    public long getSize() {
        return this.size;
    }
    
    /**
     * Get the server response code.
     *
     * @return The response code from the server
     */
    public int getResponseCode() {
        return this.responseCode;
    }
    
    /**
     * Get the server response message.
     *
     * @return The response message from the server
     */
    public String getResponseMessage() {
        return this.responseMessage;
    }

    /**
     * Get the content
     *
     * @return The content
     */
    public byte[] getContent() {
        return this.content;
    }

    /**
     * Download the specified url and return the contents.
     *
     * @param url The url to download
     * 
     * @return The contents of the url
     */
    public byte[] downloadUrl(String url) {
        setURL(url);
        
        try {
            URLConnection urlC = (new URL(url).openConnection());
 
            if (urlC instanceof HttpURLConnection) {
                readHttpURL((HttpURLConnection) urlC);
            } else {
                readURL(urlC);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return getContent();
    } 

    /**
     * The main method to run as an application.
     *
     * @param args The command line arguments
     */
    public static void main(String args[]) throws IOException {
        URLDownload app = new URLDownload();
        
        // by default on command line set output to true
        showOutput = true;

        // parse the command line arguments    
        int i = 0;
        try {
            while (args[i].charAt(0) == '-') {
                String argument = args[i].substring(1);
                
                if (argument.equals("d")) {
                    debugMode = true;
                } else if (argument.equals("s")) {
                    showOutput = false;
                } else if (argument.equals("f")) {
                    fileName = args[++i];
                }

                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            showUsage();
        }
    
        try {
            // get the url
            byte[] data = app.downloadUrl(args[i]);
            
            if (showOutput) {
                out.write(data, 0, data.length);
            }
            
            if ((fileName != null) && (fileName.length() > 0)) {
                app.writeFile(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Show the main application usage.
     */
    public static void showUsage() {
        System.err.println("USAGE: " + className + 
                           " [-d] [-s] [-f file] <url>");
        
        System.err.println("\t-d  : debug all action.");
        System.err.println("\t-s : silent mode - show no output");
        System.err.print("\t-f  : followed by the name of the file to " +
                         "save the output to");
        
        System.exit(1);
    } 

    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods

    /**
     * Set the URL.
     *
     * @param url A <code>String</code> representation of the url.
     */
    private void setURL(String url) {
        this.url = url;
    }
    
    /**
     * Set the server specified content type.
     *
     * @param contentType The content type
     */
    private void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * Set the server specified content length.
     *
     * @param contentLength The content length
     */
    private void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    } 

    /**
     * Set number of bytes downloaded.
     *
     * @param size The number of bytes
     */
    private void setSize(long size) {
        this.size = size;
    } 
    
    /**
     * Set the server specified response code.
     *
     * @param responseCode The response code
     */
    private void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    
    /**
     * Set the server specified response message.
     *
     * @param responseMessage The response message
     */
    private void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
 
    /**
     * Download the contents.
     *
     * @param url A <code>URLConnection</code> object
     */
    private void readURL(URLConnection url) {
        try {
            BufferedInputStream in = 
                new BufferedInputStream(url.getInputStream(), 1024);
                
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);                
                
            byte buf[] = new byte[1024];
            int nRead = 0; 
            int nWrote = 0;
                        
            // write to file until read return -1(have no data to read)
            while ((nRead = in.read(buf, 0, 1024)) >= 0) {
                baos.write(buf, 0, nRead);
                nWrote+=nRead;
            }
            
            size = nWrote;
            content = baos.toByteArray();
            
            in.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Download the contents.
     *
     * @param url A <code>HttpURLConnection</code> object
     */
    private void readHttpURL(HttpURLConnection url) 
        throws IOException {
 
        long before;
        long after;
 
        url.setAllowUserInteraction(true);
        debug("Contacting the URL ...");
        url.connect();
        
        debug("Connected. Waiting for reply ...");
        before = System.currentTimeMillis();

        BufferedInputStream in = 
            new BufferedInputStream(url.getInputStream(), 1024);
            
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);                
          
        byte buf[] = new byte[1024];
        int nRead=0, nWrote=0;

        after = System.currentTimeMillis();
        debug("The reply takes " + ((int)(after-before)/1000) + " seconds");
 
        before = System.currentTimeMillis();
 
        try {
            if (url.getResponseCode() != HttpURLConnection.HTTP_OK) {
                out.println(url.getResponseMessage());
            } else {
                getHeaderInfo(url);
                
                // write to file until read return -1(have no data to read)
                while ((nRead = in.read(buf, 0, 1024)) >= 0) {
                    baos.write(buf, 0, nRead);
                    nWrote+=nRead;
                }
                size = nWrote;
                
                after = System.currentTimeMillis();
                int milliSeconds = (int) (after-before);
            
                setResponseCode(url.getResponseCode());
                setResponseMessage(url.getResponseMessage());
            
                debug("Read " + getSize() + " bytes from " + getURL());
                debug(getResponseCode() + " " + getResponseMessage());
                
                url.disconnect();
                
                debug("It took " + milliSeconds +  " ms");
      
                if (url.usingProxy()) {
                    debug("This URL uses a proxy");
                }
                
                in.close();
                content = baos.toByteArray();
            }
        } catch (Exception e) {
            if (showOutput) {
                out.println(e + ": " + e.getMessage());
            }
            debug("I/O Error : Read " + getSize() + " bytes from " + getURL());
            if (showOutput) {
                out.println("I/O Error " + url.getResponseMessage());
            }
        }
    }

    /**
     * Store header information.
     *
     * @param url A <code>URLConnection</code> object  
     */
    private void getHeaderInfo(URLConnection url) {
        setContentType(url.getContentType());
        setContentLength(url.getContentLength());
        debug("Content-Length   : " + getContentLength());
        debug("Content-Type     : " + getContentType());
        
        if (url.getContentEncoding() != null) {
            debug("Content-Encoding : " + url.getContentEncoding());
        }
    }
    
    /**
     * Write the contents to a file.
     *
     * @param fileName The name of the file
     */
    private void writeFile(String fileName) {
        try {
            // This is a fast and easy way to read the file into memory
            File f = new File(fileName);
            RandomAccessFile raFile = new RandomAccessFile(f, "rw");
            raFile.write(content, 0, content.length);
            raFile.close();        
        } catch (FileNotFoundException fnfe) {
            if (showOutput) {
                out.println(fnfe + ": " + fnfe.getMessage());
            }
        } catch (IOException ioe) {
            if (showOutput) {
                out.println(ioe + ": " + ioe.getMessage());
            }
        }
        
    }
    
    /**
     * Show debug information.
     * 
     * @param text The text to display.
     */
    private void debug(String text) {
        if (debugMode) {
            out.println(text);
        }
    }
}
      