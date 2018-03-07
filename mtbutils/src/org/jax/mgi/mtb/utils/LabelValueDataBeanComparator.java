/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueDataBeanComparator.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.util.Comparator;

/**
 * Subclass of <code>Comparator</code> to compare 
 * <code>LabelValueDataBean</code> objects.
 *
 * @author mjv
 * @date 2007/04/30 15:52:18
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueDataBeanComparator.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * @see java.util.Comparator
 */
public class LabelValueDataBeanComparator<T extends LabelValueDataBean> 
       implements Comparator<T> {
    
    // -------------------------------------------------------------- Constants
    public static final int TYPE_LABEL = 0;
    public static final int TYPE_VALUE = 1;
    public static final int TYPE_DATA = 2;
    
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
     * Constructor class for LabelValueDataBeanComparator.
     *
     * @param iType the field from which you want to sort
     * <br>
     * Possible values are:
     * <ul>
     *   <li>LabelValueDataBeanComparator.TYPE_LABEL
     *   <li>LabelValueDataBeanComparator.TYPE_VALUE
     *   <li>LabelValueDataBeanComparator.TYPE_DATA
     * </ul>
     */
    public LabelValueDataBeanComparator(int iType) {
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
     *   <li>LabelValueBeanComparator.TYPE_DATA
     * </ul>
     * @param bReverse set this value to true, if you want to reverse the 
     *        sorting results
     */
    public LabelValueDataBeanComparator(int iType, boolean bReverse) {
        this.iType = iType;
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
        int iReturn = 0;
        switch(iType) {
            case TYPE_LABEL:
                iReturn = obj1.compareLabel(obj2);
                break;
            case TYPE_VALUE:
                iReturn = obj1.compareValue(obj2);
                break;
            case TYPE_DATA:
                iReturn = obj1.compareData(obj2);
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
