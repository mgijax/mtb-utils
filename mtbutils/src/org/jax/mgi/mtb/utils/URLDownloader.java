/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/URLDownloader.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

/**
 * The <code>URLDownloader</code> class is a threaded class that encapsulates
 * the download of a URL.
 *
 * @author mjv
 * @date 2007/04/30 15:52:19
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/URLDownloader.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * @see java.lang.Thread
 */
public class URLDownloader 
        extends Thread {
    
    // -------------------------------------------------------------- Constants
    public static final String USER_AGENT = URLDownloader.class.getName() + 
                        "/1.0 (Java Based URL Grabber for MTB) MTB/1.0";
    
    // ----------------------------------------------------- Instance Variables
    private String s = null;
    private String url = null;
    private Timer timer = null;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor which takes a URL in the following format:
     * <p>
     * <code>http://&lt;server&gt;/&lt;pathtofile&gt;</code>
     * <p>
     * where
     * <p>
     * <code>&lt;server&gt;</code> is the name of the server
     * <code>&lt;pathtofile&gt;</code> is the path to the file (URI)
     *
     * @param url The URL to download.
     */
    public URLDownloader(String url) {
        super();
        this.url = url;
        this.timer = new Timer();
    }
    
    // --------------------------------------------------------- Public Methods

    /**
     * Get the downloaded data as a String.
     *
     * @return A String containing the data that was downloaded.
     */
    public String getData() {
        return s;
    }

    /**
     * The run method performs on Thread.start()
     */
    public void run() {
        timer.restart();
        try {
            URLDownload d = new URLDownload();
            s = new String(d.downloadUrl(url));
        } catch (Exception e) {
             System.err.println("Unknown Host");
            return;
        } finally {
            timer.stop();
            //System.out.println(url + " is done!");
        }
    }
    
    /**
     * Get the time it took to download the data.
     *
     * @return The time to download the data
     */
    public String getDownloadTime() {
        return timer.toString();
    }
    
    /**
     * Get the time elasped since the download started.
     *
     * @return The time since the download started
     */
    public String getElaspedTime() {
        return timer.toString();
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}
