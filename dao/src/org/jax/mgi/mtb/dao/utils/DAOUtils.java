/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/utils/DAOUtils.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Static utility methods for DAOs.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:39 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/utils/DAOUtils.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 */
public class DAOUtils {

    // -------------------------------------------------------------- Constants
    // none
    // ----------------------------------------------------- Instance Variables
    // none
    // ----------------------------------------------------------- Constructors
    /**
     * Private constructor.
     */
    private DAOUtils() {
        // no-op'd
    }

   
    
    public static String formatCondition(String column, String condition,
            String value, String delim) {

     
        // todo: value may be a collection (ie comma seperated)

        String delimiter = "";

        if ((delim != null) && (delim.trim().length() > 0)) {
            delimiter = delim;
        }

        if ((column == null) || (condition == null) || (value == null)) {
            return "";
        }

        if ((column.length() == 0) || (condition.length() == 0) || (value.length() == 0)) {
            return "";
        }

        value = escape(value);

        String ret = "";
        String cond = condition.toLowerCase().trim();

        if (cond.equals("=")) {
            ret = "lower(" + column + ") = lower(" + delimiter + value + delimiter + ")";
        } else if (cond.equals("!=")) {
            ret = "lower(" + column + ") != lower(" + delimiter + value + delimiter + ")";
        } else if (cond.equals("equals")) {
            ret = "lower(" + column + ") = lower(" + delimiter + value + delimiter + ")";
        } else if (cond.equals("contains")) {
            ret = column + " ilike " + delimiter + "%" + value + "%" + delimiter;
        } else if ((cond.equals("starts")) || (cond.equals("begins"))) {
            ret = column + " ilike " + delimiter + value + "%" + delimiter;
        } else if (cond.equals("ends")) {
            ret = column + " ilike " + delimiter + "%" + value + delimiter;
        } else if (cond.equals("inList")) {
            ret = "lower(" + column + ")" + parseList(value, delimiter).toLowerCase();
        }
        
        return ret;
    }

    // take a comma seperated list of values and turn it into a
    // comma seperated list of delimited strings to use in an "in" statement
    private static String parseList(String value, String delimiter) {
        StringBuffer sb = new StringBuffer();
        Scanner s = new Scanner(value);
        s.useDelimiter(",");
        sb.append(" in (");
        while (s.hasNext()) {
            sb.append(delimiter);
            sb.append(s.next().trim());
            sb.append(delimiter);
            if (s.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(") ");
        return sb.toString();
    }

    /**
     *
     * @param column
     * @param condition
     * @param value
     * @return
     */
    public static String formatNumericCondition(String column, String condition, Number value) {

        String delimiter = "";

        if ((column == null) || (condition == null) || (value == null)) {
            return "";
        }

        if ((column.length() == 0) || (condition.length() == 0)) {
            return "";
        }

        String ret = "";
        String cond = condition.toLowerCase().trim();

        if (cond.equals("=")) {
            ret = column + " = " + delimiter + value + delimiter;
        } else if (cond.equals("!=")) {
            ret = column + " != " + delimiter + value + delimiter;
        } else if (cond.equals(">")) {
            ret = column + " > " + delimiter + value + delimiter;
        } else if (cond.equals(">=")) {
            ret = column + " >= " + delimiter + value + delimiter;
        } else if (cond.equals("<")) {
            ret = column + " < " + delimiter + value + delimiter;
        } else if (cond.equals("<=")) {
            ret = column + " <= " + delimiter + value + delimiter;
        }

        return ret + " ";
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String nvl(String value, String defaultValue) {
        if (value != null && value.trim().length() > 0) {
            return value;
        }

        return defaultValue;
    }

    /**
     *
     * @param c
     * @param delim
     * @param surround
     * @return
     */
    public static String collectionToString(Collection c, String delim, String surround) {
        StringBuffer str = new StringBuffer("");

        if (c == null) {
            return surround + surround;
        }

        if (c.isEmpty()) {
            return surround + surround;
        } else {
            Iterator it = c.iterator();

            while (it.hasNext()) {

                Object obj = it.next();

                if (obj == null) {
                    str.append(surround);
                    str.append(surround);
                } else {

                    if (obj instanceof String) {
                        str.append(surround);
                        str.append(obj);
                        str.append(surround);
                    } else {
                        str.append(surround);
                        str.append(obj.toString());
                        str.append(surround);
                    }
                }

                if (it.hasNext()) {
                    str.append(delim);
                }
            }
        }

        return str.toString();
    }

    // ------------------------------------------------------ Protected Methods
    // none
    // -------------------------------------------------------- Private Methods
    
    
    //escape characters that will cause problems
    private static String escape(String value) {
    //    System.out.print(value);
        value = value.replaceAll("\"", "\"\"");
        value = value.replaceAll("'", "''");
        value = value.replaceAll("%", "");
        value = value.replaceAll("\\*", "");
      

        return value;

    }
}
