package org.jax.mgi.mtb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/** The StreamGobbler is used by the TextSearchHandler to grab standard out
 * and standard error when the search engine program is exec'd.
 * @is a thread that grabs info from a stream.
 * @has knowledge of grabbing data from atream..
 * @does grabs data from an input stream and puts it in a StringBuffer.
 */
public class StreamGobbler extends Thread {
    private List<String> lines;
    private InputStream is;
    private String type;
    private OutputStream os;
    private boolean debug = false;
    static String newline = System.getProperty("line.separator");
    
    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }
    
    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.lines = new ArrayList<String>();
    }
    
    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }
            
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }
                
                lines.add(line);
                
                if (debug) {
                    System.out.println(type + ">" + line);
                }
            }
            if (pw != null) {
                pw.flush();
            }
            isr.close();
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /** return content of a stream as a StringBuffer
     * @return StringBuffer representing content of stream
     * @assumes that the stream is a finite string of information..
     * @effects nothing
     */
    public StringBuffer getData() {
        StringBuffer sb = new StringBuffer();
        List<String> l = getLines();
        
        for (int i = 0; i < l.size(); i++) {
            sb.append(l.get(i));
            
            if (i != (l.size() - 1)) {
                sb.append(newline);
            }
        }
        
        return sb;
    }
    
    public List<String> getLines() {
        return this.lines;
    }
}