/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/Utils.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The <code>Utils</code> class is a static class the encapsulates many common
 * utilities used in the WI.
 *
 * @author mjv
 * @date 2007/04/30 15:52:19
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/Utils.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 */
public class Utils {
    
    // -------------------------------------------------------------- Constants
    public static final String EOL =  System.getProperty("line.separator");
    
    // ----------------------------------------------------- Instance Variables
    // none
    
    // ----------------------------------------------------------- Constructors
    // none 
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Prints the stacktrace to a buffer and returns the buffer as a String.
     *
     * @param t the Throwable you wnat to generate a stacktrace for.
     * @return the stacktrace of the supplied Throwable.
     */
    public static String getStackTrace(Throwable t) {
        try {
            StringWriter sw = new StringWriter();

            t.printStackTrace(new PrintWriter(sw));
            sw.close();
            return sw.toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
    
}
