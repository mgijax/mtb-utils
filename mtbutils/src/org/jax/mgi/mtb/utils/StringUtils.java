/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/StringUtils.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The <code>StringUtils</code> class is a static class the encapsulates many
 * common <code>String</code> utilities.
 *
 * @author mjv
 * @date 2007/04/30 15:52:18
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/StringUtils.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 */
public class StringUtils {

    // -------------------------------------------------------------- Constants
    public static final String EOL = Utils.EOL;
    public static final Format formatter = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss:S ");

    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Wrap <code>text</code> with HTML and BODY open and close tags.
     *
     * @param value The text wrap
     *
     * @return "&gt;html&lt;&gt;body&lt;" + value + "&gt;/html&lt;&gt/body&lt;"
     */
    public static String toHTML(Object value) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><body>");

        if (value != null) {
            sb.append(value.toString());
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * Wrap <code>text</code> with HTML and BODY open and close tags.
     *
     * @param value The text wrap
     *
     * @return "&gt;html&lt;&gt;body&lt;" + value + "&gt;/html&lt;&gt/body&lt;"
     */
    public static String toHTML(int text) {
        return toHTML(new Integer(text));
    }

    /**
     * Wrap <code>text</code> with HTML and BODY open and close tags.
     *
     * @param value The text wrap
     *
     * @return "&gt;html&lt;&gt;body&lt;" + value + "&gt;/html&lt;&gt/body&lt;"
     */
    public static String toHTML(long text) {
        return toHTML(new Long(text));
    }

    /**
     * Wrap <code>text</code> with HTML and BODY open and close tags.
     *
     * @param value The text wrap
     *
     * @return "&gt;html&lt;&gt;body&lt;" + value + "&gt;/html&lt;&gt/body&lt;"
     */
    public static String toHTML(double text) {
        return toHTML(new Double(text));
    }

    /**
     * Wrap <code>text</code> with HTML and BODY open and close tags.
     *
     * @param value The text wrap
     *
     * @return "&gt;html&lt;&gt;body&lt;" + value + "&gt;/html&lt;&gt/body&lt;"
     */
    public static String toHTML(float text) {
        return toHTML(new Float(text));
    }


    /**
     * Check value for nullness and return defaultValue if it is null.
     * <p>
     * Based off of Oracle's nvl SQL function.
     *
     * @param value The value to check for null
     * @param defaultValue The defaultValue
     *
     * @return A String whose value is value if it is not null and defaultValue
     *         otherwise
     */
    public static String nvl(String value, String defaultValue) {
        if (value != null) {
            return value;
        }

        return defaultValue;
    }

    public static String trim(String str) {
        if (hasValue(str)) {
            return str.trim();
        }

        return null;
    }

    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     * @param alphaFront True to make sure the first character is alphanumeric
     *
     * @return An int value same as String.compareTo()
     */
    public static int compare(String a, String b, boolean alphaFront, boolean dashFirst) {
        int ret = 0;
        char dash = '-';
/*
        if (a == null && b != null) {
            ret = -1;
        } else if (a == null && b == null) {
            ret = 0;
        } else if (a != null && b == null) {
            ret = 1;
        } else {
            if (alphaFront) {
                char a1 = a.charAt(0);
                char b1 = b.charAt(0);

                if (Character.isLetterOrDigit(a1) &&
                    Character.isLetterOrDigit(b1)) {
                    ret = a.compareTo(b);
                } else if (!Character.isLetterOrDigit(a1) &&
                           Character.isLetterOrDigit(b1)) {
                    ret = 1;
                } else if (Character.isLetterOrDigit(a1) &&
                           !Character.isLetterOrDigit(b1)) {
                    ret = -1;
                } else {
                    ret = a.compareTo(b);
                }
            } else {
                ret = a.compareTo(b);
            }
        }
*/

        if (a == null && b != null) {
            ret = -1;
        } else if (a == null && b == null) {
            ret = 0;
        } else if (a != null && b == null) {
            ret = 1;
        } else {
            if (alphaFront && dashFirst) {
                char a1 = a.charAt(0);
                char b1 = b.charAt(0);

                if ((a1 == dash) && (b1 == dash)) {
                    ret = 0;
                } else if ((a1 != dash) && (b1 == dash)) {
                    ret = 1;
                } else if ((a1 == dash) && (b1 != dash)) {
                    ret = -1;
                } else {
                    if (Character.isLetterOrDigit(a1) && Character.isLetterOrDigit(b1)) {
                        ret = a.compareTo(b);
                    } else if (!Character.isLetterOrDigit(a1) && Character.isLetterOrDigit(b1)) {
                        ret = 1;
                    } else if (Character.isLetterOrDigit(a1) && !Character.isLetterOrDigit(b1)) {
                        ret = -1;
                    } else {
                        ret = a.compareTo(b);
                    }
                }
            } else if (alphaFront && !dashFirst) {
                char a1 = a.charAt(0);
                char b1 = b.charAt(0);

                if (Character.isLetterOrDigit(a1) && Character.isLetterOrDigit(b1)) {
                    ret = a.compareTo(b);
                } else if (!Character.isLetterOrDigit(a1) && Character.isLetterOrDigit(b1)) {
                    ret = 1;
                } else if (Character.isLetterOrDigit(a1) && !Character.isLetterOrDigit(b1)) {
                    ret = -1;
                } else {
                    ret = a.compareTo(b);
                }
            } else if (!alphaFront && dashFirst) {
                char a1 = a.charAt(0);
                char b1 = b.charAt(0);

                if ((a1 == dash) && (b1 == dash)) {
                    ret = 0;
                } else if ((a1 != dash) && (b1 == dash)) {
                    ret = 1;
                } else if ((a1 == dash) && (b1 != dash)) {
                    ret = -1;
                } else {
                    ret = a.compareTo(b);
                }
            } else {
                ret = a.compareTo(b);
            }
        }





        return ret;
    }


    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     * @param alphaFront True to make sure the first character is alphanumeric
     *
     * @return An int value same as String.compareTo()
     */
    public static int compare(String a, String b, boolean alphaFront) {
        return compare(a, b, alphaFront, false);
    }

    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     *
     * @return An int value same as String.compareTo()
     */
    public static int compare(String a, String b) {
        return compare(a, b, false);
    }

    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     * @param alphaFront True to make sure the first character is alphanumeric
     *
     * @return An int value same as String.compareTo()
     */
    public static int compareIgnoreCase(String a, String b, boolean alphaFront, boolean dashFirst) {
        if (a != null) {
            a = a.toLowerCase();
        }

        if (b != null) {
            b = b.toLowerCase();
        }

        return compare(a, b, alphaFront, dashFirst);
    }

    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     * @param alphaFront True to make sure the first character is alphanumeric
     *
     * @return An int value same as String.compareTo()
     */
    public static int compareIgnoreCase(String a, String b, boolean alphaFront) {
        return compareIgnoreCase(a, b, alphaFront, false);
    }

    /**
     * Compare Strings a and b.
     *
     * @param a The first value
     * @param b The second value
     *
     * @return An int value same as String.compareTo()
     */
    public static int compareIgnoreCase(String a, String b) {
        return compareIgnoreCase(a, b, false);
    }

    /**
     * Evaluate a and b to see if they are equal, handling nulls correctly.
     *
     * @param a The first String to compare
     * @param b The second String to compare
     *
     * @return True if both are null or if both are not null equal Strings,
     *         false otherwise.
     */
    public static boolean equals(String s, String t) {
        if (s == null) {
            if (t == null) {
                return true;
            }
            return false;
        } else {
            if (t == null) {
                return false;
            }
        }
        return s.equals(t);
    }

    /**
     * Makes sure that str is not null and has a value (length > 0).
     *
     * @param str The value to check
     *
     * @return True if str has a value, false otherwise
     */
    public static boolean hasValue(String str) {
        if (str == null) {
            return false;
        }

        if (str.trim().length() == 0) {
            return false;
        }

        return true;
    }

     /**
     * Prints the stacktrace to a buffer and returns the buffer as a String.
     *
     * @param t The Throwable you wnat to generate a stacktrace for.
     *
     * @return The stacktrace of the supplied Throwable.
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

    /**
     * Initialize the first letter in a word.
     *
     * @param str The String to alter
     *
     * @return The altered String
     */
    public static String initCap(String str) {
        if (!hasValue(str)) {
            return str;
        }

        char arr[] = str.toCharArray();
        boolean inWord = false;

        try {
            for (int i = 0; i < arr.length; i++) {
                if (Character.isWhitespace(arr[i])) {
                    inWord = false;
                } else {
                    if (inWord) {
                        ;// do nothing
                    } else {
                        arr[i] = Character.toUpperCase(arr[i]);
                    }
                    inWord = true;
                }
            }
        } catch (Exception ignore) {}

        return new String(arr);
    }

    /**
     * Make a string into Ascii art.
     *
     * @param font The Font to use (sraw with)
     * @param text The text to draw
     *
     * @return The text as 'Ascii Art' in the given font using a '#' character
     *         for each pixel (the text itself if any runtime exception occurs)
     */
    public static String getAsciiArt(Font font, String text) {
        return getAsciiArt(font, text, new Character('#'));
    }

    /**
     * Make a string into Ascii art.
     *
     * @param font The Font to use (sraw with)
     * @param text The text to draw
     * @param chr  The Character to use
     *
     * @return The text as 'Ascii Art' in the given font using a '#' character
     *         for each pixel (the text itself if any runtime exception occurs)
     */
    public static String getAsciiArt(Font font, String text, Character chr) {
        try {
            if (font == null) {
                font = new Font("SansSerif", Font.PLAIN, 14);
            }

            if (text == null) {
                text = "(null)";
            }

            int tabWidth = 4;

            text = text.replaceAll("\\t", repeat(' ', tabWidth));
            FontRenderContext
                 fontRenderContext = new FontRenderContext(null, false, false);
            StringBuffer buffer = new StringBuffer();
            String[] lines = text.split("\n|\r\n", 0);

            for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
                // TextLayout does not handle white space like I want to, and
                // GlyphVector has no convenient method to calculate overall
                // ascent/descent.  So just use both...

                String line = lines[lineIndex];
                GlyphVector glyphVector =
                        font.createGlyphVector(fontRenderContext, line);
                Rectangle2D bounds = glyphVector.getLogicalBounds();
                int width = (int) bounds.getWidth();
                int height = (int) bounds.getHeight();

                if (line.length() == 0) {
                    buffer.append(repeat('\n', height));
                    continue;
                }

                TextLayout textLayout =
                        new TextLayout(line, font, fontRenderContext);
                float ascent = textLayout.getAscent();
                BufferedImage image =
                        new BufferedImage(width, height,
                                          BufferedImage.TYPE_BYTE_BINARY);
                Graphics2D graphics = image.createGraphics();

                try {
                    graphics.drawGlyphVector(glyphVector, 0, ascent);

                    Raster raster = image.getRaster();
                    int[] ints = new int[1];

                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            raster.getPixel(x, y, ints);
                            int pixelValue = ints[0];

                            buffer.append(pixelValue > 0 ? chr.charValue() : ' ');
                        }
                        buffer.append('\n');
                    }
                }
                finally {
                    graphics.dispose();
                }

            }

            return new String(buffer);
        } catch (NoClassDefFoundError ncdf) {
            // Don't want anything silly to happen only because using
            // this fun method.
            // Revert to plain output ;-(
            return text;
        } catch (RuntimeException re) {
            return text;
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * Convert a Collection to a String with the specified delimiter and
     * wrapper.
     *
     * @param c The Collection to convert
     * @param delim The text delimiter
     * @param surround The text to surround each element in the collection
     *
     * @return The generate String
     */
    public static String collectionToString(Collection c,
                                            String delim,
                                            String surround) {
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



    /**
     * Generate a String consisting of the character c repeated count times.
     *
     * @param c The character to repeat
     * @param count The number of times to repeat c
     *
     * @return A String consisting of the character c repeated count times.
     */
    public static String repeat(char c, int count) {
        char[] fill = new char[count];

        Arrays.fill(fill, c);
        return new String(fill);
    }


    /**
     * Check to see if a String contains whitespace.
     *
     * @param s The string to check
     *
     * @return True if the String contains whitespace, false otherwise
     */
    public static boolean containsWhiteSpace(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a string from an integer value using the specified base and
     * pads it with zeros at the start to fill the supplied length. For
     * example, padZeros (5, 1, 10) returns "00001". If the value is negative
     * then the minus sign is included in the length. For example,
     * padZeros (5, -1, 10) returns "-0001".
     *
     * @param length The length of the returned string after padding with zeros
     * @param value The value to be padded with zeros.
     * @param base The base to use for the number when represented as a string
     *
     * @return The generated String
     */
    public static String padZeros(int length, int value, int base) {
        if (value < 0) {
            return "-" + padZeros(length - 1, Math.abs(value), base);
        }

        String s = Integer.toString(value, base);
        int padCount = length - s.length();
        StringBuffer buffer = new StringBuffer(length);

        for (int i = 0; i < padCount; i++) {
            buffer.append('0');
        }

        buffer.append(s);

        return buffer.toString();
    }

    /**
     * Creates a string from an integer value and pads it with zeros at the
     * start to fill the supplied length.  For example, padZeros (5, 1) returns
     * "00001". If the value is negative then the minus sign is included in the
     * length. For example, padZeros (5, -1) returns "-0001".
     * <p>
     * The base used for the returned string is base 10.
     *
     * @param length The length of the returned string after padding with zeros
     * @param value The value to be padded with zeros.
     *
     * @return The generated String
     */
    public static String padZeros(int length, int value) {
        return padZeros(length, value, 10);
    }

    /**
     * Separates the supplied String into separate Strings which are
     * separated by the separator. The separated strings can be automatically
     * trimmed (remove white space from start and end.  For example,
     * separateString (" 1 , 2, 3,    4", ",", true) returns {"1","2","3","4"}.
     *
     * @param string The string to be separated
     * @param separator The string used as the separator (e.g. ",")
     * @param trim True if white space should be trimmed from the separated
     *        strings or false if they shouldn't
     *
     * @return An array of Strings that were seperated
     */
    public static String[] separateString(String string,
                                          String separator,
                                          boolean trim) {
        int nArraySize = 1;
        int i = 0;
        int nSeparatorLength = separator.length();

        // Calculate size of array to allocate

        while (i < string.length()) {
            int index = string.indexOf(separator, i);

            if (index == -1) {
                break;
            }

            i = index + nSeparatorLength;
            nArraySize++;
        }

        // Create and fill array with separated strings
        String[] result = new String[nArraySize];
        int nPos = 0;

        i = 0;
        String sValue = null;

        while (i < string.length()) {
            int index = string.indexOf(separator, i);

            if (index == -1) {
                sValue = string.substring(i);
            } else {
                sValue = string.substring(i, index);
            }

            if (trim) {
                sValue = sValue.trim();
            }

            result[nPos++] = sValue;

            if (index == -1) {
                break;
            }

            i = index + nSeparatorLength;
        }

        if (nPos < nArraySize) {
            result[nPos] = "";
        }

        return result;
    }

    /**
     * Separates the string using the supplied separator without trimming the
     * separted strings.
     *
     * @param string The string to be separated
     * @param separator The string used as the separator (e.g. ",")
     *
     * @return An array of Strings that were seperated
     */
    public static String[] separateString(String string, String separator) {
        return separateString(string, separator, false);
    }

    /**
     * Seapartes the string using a comma with optional trimming of the
     * separted strings.
     *
     * @param string The string to be separated
     * @param trim True if white space should be trimmed from the separated
     *        strings or false if they shouldn't
     *
     * @return An array of Strings that were seperated
     */
    public static String[] separateString(String string, boolean trim) {
        return separateString(string, ",", trim);
    }

    /**
     * Seapartes the string using a comma without trimming of the separted
     * strings.
     *
     * @param string The string to be separated
     *
     * @return An array of Strings that were seperated
     */
    public static String[] separateString(String string) {
        return separateString(string, false);
    }

    /**
     * Does the opposite of spearateString and creates a String which is
     * composed of the supplied string separated with seaparator.
     *
     * @param strings An array of Strings
     * @param separator The separtor to use
     *
     * @return A concatenated String
     */
    public static String makeString(String[] strings, String separator) {
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < strings.length; i++) {
            b.append(strings[i]);

            if (i < strings.length - 1) {
                b.append(separator);
            }
        }

        return b.toString();
    }

    /**
     * Determines if a String matches a pattern with or without case
     * sensitivity.
     *
     * @param sString The string to match against the pattern.
     * @param sPattern The pattern to check against.
     * @param bCaseSensitive True if case sensitivity should be used when
     *                       matching the pattern or false otherwise.
     *
     * @return If the string matches the expression, true, else false.
     */
    public static boolean matchesPattern(String sString, String sPattern,
                                         boolean bCaseSensitive) {
        return matchesPattern(sString, sPattern, 0, 0, bCaseSensitive);
    }

    /**
     * Determines if a string matches a pattern with case sensitivity.
     *
     * @param sString The string to match against the pattern.
     * @param sPattern The pattern to check against.
     * @return If the string matches the expression, true, else false.
     */
    public static boolean matchesPattern(String sString, String sPattern) {
        return matchesPattern(sString, sPattern, true);
    }

    /**
     * An internal routine to implement expression matching.
     * <p>
     * This routine is based on a self-recursive algorithm.
     *
     * @param sString The string to check
     * @param sPattern The pattern to check
     * @param nStringIndex The index of the String to start looking
     * @param nPatternIndex The index of the pattern to start matching
     * @param bCaseSensitive True for case sensitivity, false otherwise
     *
     * @return True if String matched pattern, else false.
     */
    public static boolean matchesPattern(String sString,
            String sPattern,
            int nStringIndex,
            int nPatternIndex,
            boolean bCaseSensitive) {
        int   nPatternLength = sPattern.length();
        int   nStringLength = sString.length();

        while (true) {
            if (nPatternIndex >= nPatternLength) {
                return (nStringIndex >= nStringLength);
            }

            if (nStringIndex >= nStringLength &&
                sPattern.charAt(nPatternIndex) != '*') {
                return false;
            }

            // Check for a '*' as the next sPattern char.
            // This is handled by a recursive call for
            // each postfix of the name.

            if (sPattern.charAt(nPatternIndex) == '*') {
                if (++nPatternIndex >= nPatternLength) {
                    return true;
                }

                while (true) {
                    if (matchesPattern(sString, sPattern, nStringIndex,
                                       nPatternIndex, bCaseSensitive)) {
                        return true;
                    }

                    if (nStringIndex >= nStringLength) {
                        return false;
                    }

                    ++nStringIndex;
                }
            }

            // Check for '?' as the next sPattern char.
            // This matches the current character.

            if (sPattern.charAt(nPatternIndex) == '?') {
                ++nPatternIndex;
                ++nStringIndex;
                continue;
            }

            // Check for '[' as the next sPattern char.
            // This is a list of acceptable characters,
            // which can include character ranges.

            if (sPattern.charAt(nPatternIndex) == '[') {
                for (++nPatternIndex;; ++nPatternIndex) {
                    if (nPatternIndex >= nPatternLength ||
                        sPattern.charAt(nPatternIndex) == ']') {
                        return false;
                    }

                    if (areCharsEqual(sPattern.charAt(nPatternIndex),
                                      sString.charAt(nStringIndex),
                                      bCaseSensitive)) {
                        break;
                    }

                    if (nPatternIndex < (nPatternLength - 1) &&
                        sPattern.charAt(nPatternIndex + 1) == '-') {
                        if (nPatternIndex >= (nPatternLength - 2)) {
                            return false;
                        }

                        char c = sString.charAt(nStringIndex);
                        char cRangeStart = sPattern.charAt(nPatternIndex);
                        char cRangeEnd = sPattern.charAt(nPatternIndex + 2);

                        if (isCharInRange(c, cRangeStart,
                                          cRangeEnd, bCaseSensitive)) {
                            break;
                        }

                        nPatternIndex += 2;
                    }
                }

                for (;sPattern.charAt(nPatternIndex) != ']';++nPatternIndex) {
                    if (nPatternIndex >= nPatternLength) {
                        --nPatternIndex;
                        break;
                    }
                }

                ++nPatternIndex;
                ++nStringIndex;
                continue;
            }

            // Check for backslash escapes
            // We just skip over them to match the next char.

            if (sPattern.charAt(nPatternIndex) == '\\') {
                if (++nPatternIndex >= nPatternLength) {
                    return false;
                }
            }

            if (nPatternIndex < nPatternLength &&
                nStringIndex < nStringLength) {
                if (!areCharsEqual(sPattern.charAt(nPatternIndex),
                                   sString.charAt(nStringIndex),
                                   bCaseSensitive)) {
                    return false;
                }
            }

            ++nPatternIndex;
            ++nStringIndex;
        }
    }

    /**
     * Expands the the suplied String and returns a new String where tabs have
     * been expanded to spaces.
     *
     * @param sString The string
     * @param nTabSize The number of spaces to use instead of the tab character
     *
     * @return The expanded String
     */
    public static String expandTabs(String sString, int nTabSize) {
        boolean bHasTabs = false;

        for (int i = 0; i < sString.length(); i++) {
            if (sString.charAt(i) == '\t') {
                bHasTabs = true;
                break;
            }
        }

        if (!bHasTabs) {
            return sString;
        }

        StringBuffer buffer = new StringBuffer(sString.length() + 20);

        for (int nChar = 0; nChar < sString.length(); nChar++) {
            char c = sString.charAt(nChar);

            if (c == '\t') {
                int nNumSpaces = (buffer.length() / nTabSize + 1) * nTabSize -
                                 buffer.length();

                for (int j = 0; j < nNumSpaces; j++) {
                    buffer.append(' ');
                }
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * In String string, replace all occurances of s1 with s2. The match is
     * case sensitive, but s1 and s1 need not be of the same length.
     *
     * @param string The String to perform the replace on
     * @param s1 The String to find
     * @param s3 The String to replace with
     *
     * @return The altered String
     */
    public static String replace(String string, String s1, String s2) {
        if (!hasValue(string)) {
            return string;
        }

        while(string.indexOf(s1) != -1) {

          String pre = string.substring(0, string.indexOf(s1));
          String suf = string.substring(string.indexOf(s1) + s1.length());

          string = pre + s2 + suf;
      }

        return string;
    }
    /**
     * Determin if the characters are equal.
     *
     * @param c1 The first character
     * @param c2 The second character
     * @param bCaseSensitive Apply case sensitivity
     *
     * @return True if the chracters are equal, false otherwise
     */
    public static boolean areCharsEqual(char c1, char c2,
                                        boolean bCaseSensitive) {
        if (bCaseSensitive) {
            return c1 == c2;
        }

        return Character.toUpperCase(c1) == Character.toUpperCase(c2);
    }

    /**
     * Check to see if a specific character is between the specified range.
     *
     * @param c The character to check
     * @param cRangeStart The start range
     * @param cRangeEnd The end range
     * @param bCaseSensitive Apply case sensitivity
     *
     * @return True if c is between cRangeStart and cRangeEnd, false otherwise
     */
    public static boolean isCharInRange(char c, char cRangeStart,
                                        char cRangeEnd,
                                        boolean bCaseSensitive) {
        if (!bCaseSensitive) {
            c = Character.toUpperCase(c);
            cRangeStart = Character.toUpperCase(cRangeStart);
            cRangeEnd = Character.toUpperCase(cRangeEnd);
        }

        if (cRangeEnd < cRangeStart) {
            char cTemp = cRangeStart;

            cRangeStart = cRangeEnd;
            cRangeEnd = cTemp;
        }

        return (c >= cRangeStart && c <= cRangeEnd);
    }

    /**
     * Retrieve the text between the start and end Strings.
     *
     * @param str The string to check
     * @param start The start String
     * @param end The end String
     *
     * @return The String inbetween start and end
     */
    public static String getBetween(String str, String start, String end) {
        try {
            int s = str.indexOf(start);
            int e = str.lastIndexOf(end);

            return str.substring(s, e); // + end.length());
        } catch (StringIndexOutOfBoundsException soob) {// so nothing
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Print to System.out.
     *
     * @param str The String to print
     */
    public static void out(String str) {
        System.out.println(str);
    }

    /**
     * Print to System.out with date and time.
     *
     * @param str The String to print
     */
    public static void outd(String str) {
        System.out.print(formatter.format(new Date()));
        out(str);
    }

    /**
     *Print to System.out.
     *
     * @param strb The StringBuffer to print
     */
    public static void out(StringBuffer strb) {
        out(strb.toString());
    }

    /**
     * Print to System.out with date and time.
     *
     * @param str The StringBuffer to print
     */
    public static void outd(StringBuffer strb) {
        System.out.print(formatter.format(new Date()));
        out(strb);
    }
    /**
     * Retrieve the first String contained in array
     *
     * @param array An array of Strings
     *
     * @return The first String contained in array, or null if array is empty
     */
    public static String getFirst(String[] array) {
        if ((array == null) || (array.length == 0)) {
            return null;
        }
        return array[0];
    }

    /**
     * Join the objects in <tt>v</tt> into one String.
     *
     * @param v An array of <tt>Object</tt>s which are to have their
     *          <tt>String</tt> representations joined together
     *
     * @return <tt>String</tt> which is the concatenation of the string
     *         representations of the objects in <tt>v</tt>
     */
    public static String join(Object[] v) {
        return join(v, ""); // no delimiter between strings
    }

    /**
     * Join the objects in <tt>v</tt> into one String, using the given
     * <tt>delim</tt> delimiter between them.
     *
     * @param v An array of <tt>Object</tt>s which are to have their
     *          <tt>String</tt> representations joined together
     * @param delim The <tt>String</tt> to insert between the strings we are
     *              joining together
     *
     * @return <tt>String</tt> which is the concatenation of the string
     * representations of the objects in <tt>v</tt>, using <tt>delim</tt> as
     * the delimiter between them
     */
    public static String join(Object[] v, String delim) {
        StringBuffer buf = new StringBuffer();

        if (v.length > 0) {
            buf.append(v[0].toString());
            for (int i = 1; i < v.length; i++) {
                buf.append(delim);
                buf.append(v[i].toString());
            }
        }
        return buf.toString();
    }

    /**
     * Join the objects in <tt>v</tt> into one String.
     *
     * @param v A <tt>Collection</tt> of <tt>Object</tt>s which are to have
     *          their <tt>String</tt> representations joined together
     *
     * @return <tt>String</tt> which is the concatenation of the string
     *         representations of the objects in <tt>v</tt>
     */
    public static String join(Collection v) {
        // no delimiter between strings
        return join(v, "");
    }

    /**
     * Join the objects in <tt>v</tt> into one String, using the given
     * <tt>delim</tt> delimiter between them.
     *
     * @param v A <tt>Collection</tt> of <tt>Object</tt>s which are to have
     *          their <tt>String</tt> representations joined together
     * @param delim The <tt>String</tt> to insert between the strings we are
     *              joining together
     *
     * @return <tt>String</tt> which is the concatenation of the string
     *         representations of the objects in <tt>v</tt>, using
     *         <tt>delim</tt> as the delimiter between them
     */
    public static String join(Collection v, String delim) {
        String s = null; // steps through v
        boolean isFirst = true; // first String in v?
        StringBuffer buf = new StringBuffer(); // what we're building

        Iterator it = v.iterator();

        while (it.hasNext()) {
            s = it.next().toString();
            if (isFirst) {
                buf.append(s);
                isFirst = false;
            } else {
                buf.append(delim);
                buf.append(s);
            }
        }
        return buf.toString();
    }

    /**
     * Split the given <tt>String s</tt> into an <tt>ArrayList</tt> of
     * <tt>String</tt>s, splitting whenever one of the delimiter characters in
     * <tt>delims</tt> is found.
     *
     * @param s the <tt>String</tt> to be split
     * @param delims contains the characters which are delimiters
     *
     * @return <tt>ArrayList</tt> of <tt>String</tt>s, when <tt>s</tt> is
     *         split at each character in the set of delimiters
     */
    public static List<String> split(String s, String delims) {
        List<String> vec = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(s, delims);

        while (tokenizer.hasMoreTokens()) {
            vec.add(tokenizer.nextToken());
        }
        return vec;
    }

    /**
     * Split the given <tt>String s</tt> into a <tt>ArrayList</tt> of
     * <tt>String</tt>s, splitting using the standard set of delimiter
     * characters -- space, tab, newline, carriage return, form feed.
     *
     * @return <tt>ArrayList</tt> of <tt>String</tt>s, when <tt>s</tt> is
     *         split at each whitespace
     */
    public static List<String> split(String s) {
        return split(s, " \t\n\r\f");
    }

    /**
     * Create a String with the contents of <tt>s</tt> right-justified in
     * <tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is a wrapper over
     * <tt>rjust()</tt>.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String justifyRight(String s, int width) {
        return rjust(s, width);
    }

    /**
     * Create a String with the contents of <tt>s</tt> left-justified in
     * <tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is a wrapper over
     * <tt>ljust()</tt>.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String justifyLeft(String s, int width) {
        return ljust(s, width);
    }

    /**
     * Create a String with the contents of <tt>s</tt> centered in
     * <tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is a wrapper over
     * <tt>center()</tt>.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String justifyCenter(String s, int width) {
        return center(s, width);
    }

    /** Create a String with the contents of <tt>s</tt> at the right-justified
     * in<tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is named for its
     * equivalent function in Python's string module.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String rjust(String s, int width) {
        return padLeft(s, width, ' ');
    }

    /**
     * Create a String with the contents of <tt>s</tt> left-justified in
     * <tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is named for its
     * equivalent function in Python's string module.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String ljust(String s, int width) {
        return padRight(s, width, ' ');
    }

    /**
     * Create a String with the contents of <tt>s</tt> centered in
     * <tt>width</tt> characters.  If <tt>s.length() >= width</tt>, then it
     * just returns <tt>s</tt> as-is.  This function is named for its
     * equivalent function in Python's string module.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String center(String s, int width) {
        return padCenter(s, width, ' ');
    }

    /**
     * Pad and return a String with the contents of <tt>s</tt> at the right,
     * padded out to the given <tt>width</tt> by adding copies of the
     * <tt>pad</tt> character to the <I>left</I> side
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String padLeft(String s, int width, char pad) {
        int padSize = width - s.length();

        if (padSize <= 0) {
            return s;
        }

        StringBuffer sb = new StringBuffer(width);

        for (int i = 0; i < padSize; i++) {
            sb.append(pad);
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Pad and return a String with the contents of <tt>s</tt> at the left,
     * padded out to the given <tt>width</tt> by adding copies of the
     * <tt>pad</tt> character to the <I>right</I> side
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String padRight(String s, int width, char pad) {
        int padSize = width - s.length();

        if (padSize <= 0) {
            return s;
        }

        StringBuffer sb = new StringBuffer(width);

        sb.append(s);
        for (int i = 0; i < padSize; i++) {
            sb.append(pad);
        }
        return sb.toString();
    }

    /**
     * Pad and return a String with the contents of <tt>s</tt> centered in
     * <tt>width</tt> characters.  To do the centering, copies of
     * the <tt>pad</tt> character are added to the left and right sides as
     * needed.  If <tt>s.length() >= width</tt>,  then it just returns
     * <tt>s</tt> as-is.
     * <p>
     * Note that for a <tt>width</tt> of odd size, the extra <tt>pad</tt>
     * goes to the right.
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public static String padCenter(String s, int width, char pad) {
        int padSize = width - s.length();

        if (padSize <= 0) {
            return s;
        }

        int halfPad = padSize / 2;
        StringBuffer sb = new StringBuffer(width);

        for (int i = 0; i < halfPad; i++) {
            sb.append(pad);
        }
        sb.append(s);
        for (int i = halfPad; i < padSize; i++) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static String xmlEncodeString(String orig) {
        if (orig == null) {
            return "";
        }

        char[] chars = orig.toCharArray();

        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                //case '&' :
                  //  strBuf.append("&amp;");
                  //  break;
                case '\"' :
                    strBuf.append("&quot;");
                    break;
                case '\'' :
                    strBuf.append("&apos;");
                    break;
                case '<' :
                    strBuf.append("&lt;");
                    break;
                case '>' :
                    strBuf.append("&gt;");
                    break;
                case '\n' : // Line Feed is OK
                case '\r' : // Carriage Return is OK
                case '\t' : // Tab is OK
                    // These characters are specifically OK, as exceptions to
                    // the general rule below:
                    strBuf.append(chars[i]);
                    break;
                default :
                    if (((int)chars[i]) > 127) {
                        strBuf.append("&#");
                        strBuf.append((int)chars[i]);
                        strBuf.append(";");
                    } else {
                            strBuf.append(chars[i]);
                    }
                }
        }

        return strBuf.toString();
    }

    /**
     * Convert all "<" and ">" pairs in 's' to be HTML superscript tags.
     *
     * @param s the source String
     * @return String as 's', but with the noted replacement made. returns
     *         null if 's' is null.
     */
    public static String superscript(String s) {
        return superscript(s, "<", ">");
    }

    /**
     * Convert all 'start' and 'stop' pair in 's' to be HTML superscript tags.
     *
     * @param s the source String
     * @param start the String which indicates the position for the HTML
     *              superscript start tag "<sup>"
     * @param stop the String which indicates the position for the HTML
     *             superscript stop tag "</sup>"
     * @return String as 's', but with the noted replacement made. returns
     *         null if 's' is null. returns 's' if either 'start' or 'stop' is
     *         null
     */
    public static String superscript(String s, String start, String stop) {
        if (s == null) {
            return null; // no source string
        }

        if ((start == null) || (stop == null)) {
            return s; // no start/stop string
        }

        // Otherwise, find the first instance of 'start' and 'stop' in 's'.
        // If either does not appear, then short-circuit and just return 's'
        // as-is.

        int startPos = s.indexOf(start);
        if (startPos == -1) {
            return s;
        }

        int stopPos = s.indexOf(stop);
        if (stopPos == -1) {
            return s;
        }

        String supStart = "<sup>";
        String supEnd = "</sup>";

        int startLen = start.length(); // how many chars to cut out for start
        int stopLen = stop.length(); // how many chars to cut out for stop
        int sectionStart = 0; // position of char starting section

        StringBuffer sb = new StringBuffer();

        while ((startPos != -1) && (stopPos != -1)) {
            sb.append(s.substring(sectionStart, startPos));
            sb.append(supStart);
            sb.append(s.substring(startPos + startLen, stopPos));
            sb.append(supEnd);

            sectionStart = stopPos + stopLen;
            startPos = s.indexOf(start, sectionStart);
            stopPos = s.indexOf(stop, sectionStart);
        }
        sb.append(s.substring(sectionStart));

        return sb.toString();
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

}
