/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/FieldComparator.java,v 1.1 2007/04/30 15:52:17 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Subclass of <code>Comparator</code> to compare <code>Field</code>
 * objects.
 *
 * @author mjv
 * @date 2007/04/30 15:52:17
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/FieldComparator.java,v 1.1 2007/04/30 15:52:17 mjv Exp
 * @see java.util.Comparator
 */
public class FieldComparator<T extends Field> implements Comparator<T> {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * Value that will contain the information about the order of the sort:
     * normal or reversal.
     */
    private boolean bReverse;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor class for FieldComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, new FieldComparator());
     * </code>
     */
    public FieldComparator() {
        this(false);
    }

    /**
     * Constructor class for FieldComparator.
     * <br>
     *
     * @param bReverse set this value to true, if you want to reverse the
     *        sorting results
     */
    public FieldComparator(boolean bReverse) {
        this.bReverse = bReverse;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Implementation of the compare method.
     *
     * @param pObj1 The first object to compare.
     * @param pObj2 The second object to compare.
     * @return An integer which value is one of the following:<br>
     *         <code>-1</code> if pObj1 < pObj2<br>
     *         <code>0</code> if pObj1 = pObj2<br>
     *         <code>1</code> if pObj1 > pObj2
     */
    public int compare(T obj1, T obj2) {
        int iReturn = StringUtils.compare(obj1.getName(), obj2.getName(), true, true);
        

        return bReverse ? (-1 * iReturn) : iReturn;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
