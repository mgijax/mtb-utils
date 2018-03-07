/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueBeanComparator.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.util.Comparator;

/**
 * Subclass of <code>Comparator</code> to compare <code>LabelValueBean</code> 
 * objects.
 *
 * @author mjv
 * @date 2007/04/30 15:52:18
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueBeanComparator.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * @see java.util.Comparator
 */
public class LabelValueBeanComparator<T extends LabelValueBean> implements Comparator<T> {
    
    // -------------------------------------------------------------- Constants
    public static final int TYPE_LABEL = 0;
    public static final int TYPE_VALUE = 1;
    
    // ----------------------------------------------------- Instance Variables
    
    /**
     * Holds the field on which the comparison is performed.
     */
    private int iType;
    
    /**
     * Value that will contain the information about the order of the sort: 
     * normal or reversal.
     */
    private boolean bReverse;

    // ----------------------------------------------------------- Constructors
    
    /**
     * Constructor class for LabelValueBeanComparator.
     * <br>
     * Example:
     * <br>
     * <code>Arrays.sort(pArray, 
     *     new LabelValueBeanComparator(LabelValueBeanComparator.TYPE_LABEL));
     * </code>
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>LabelValueBeanComparator.TYPE_LABEL
     *   <li>LabelValueBeanComparator.TYPE_VALUE
     * </ul>
     */
    public LabelValueBeanComparator(int iType) {
        this(iType, false);
    }

    /**
     * Constructor class for MTBStrainComparator.
     * <br>
     *
     * @param iType the field from which you want to sort.
     * <br>
     * Possible values are:
     * <ul>
     *   <li>LabelValueBeanComparator.TYPE_LABEL
     *   <li>LabelValueBeanComparator.TYPE_VALUE
     * </ul>
     * @param bReverse set this value to true, if you want to reverse the 
     *        sorting results
     */
    public LabelValueBeanComparator(int iType, boolean bReverse) {
        this.iType = iType;
        this.bReverse = bReverse;
    }

    // --------------------------------------------------------- Public Methods
    
    /**
     * Implementation of the compare method.
     *
     * @param obj1 The first object to compare.
     * @param obj2 The second object to compare.
     * @return An integer which value is one of the following:<br>
     *         <code>-1</code> if obj1 < obj2<br>
     *         <code>0</code> if obj1 = obj2<br>
     *         <code>1</code> if obj1 > obj2
     */
    public int compare(T obj1, T obj2) {
        int iReturn = 0;
        switch(iType) {
            case TYPE_LABEL:
                iReturn = obj1.compareLabel(obj2);
                break;
            case TYPE_VALUE:
                iReturn = obj1.compareValue(obj2);
                break;
            default: 
                String err = "Type passed for the field is not supported";
                throw new IllegalArgumentException(err);
        }

        return bReverse ? (-1 * iReturn) : iReturn;
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}
