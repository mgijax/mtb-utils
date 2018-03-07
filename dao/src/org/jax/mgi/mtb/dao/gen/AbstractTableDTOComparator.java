/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/gen/AbstractTableDTOComparator.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao.gen;

import java.util.Comparator;

/**
 * Abstract <code>Comparator</code> class for comparing and sorting data.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:39 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/gen/AbstractTableDTOComparator.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 */
public abstract class AbstractTableDTOComparator<T>
        implements Comparator<T> {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * Holds the field on which the comparison is performed.
     */
    protected int nColumn;
    /**
     * Value that will contain the information about the order of the sort:
     * normal or reversal.
     */
    protected boolean bReverse = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor class for AbstractTableDTOTableModelComparator.
     *
     *
     * @param nCol the field from which you want to sort
     */
    public AbstractTableDTOComparator(int nCol) {
        this(nCol, false);
    }

    /**
     * Constructor class for AbstractTableDTOTableModelComparator.
     *
     *
     * @param nCol the field from which you want to sort
     * @param bRev <code>true</code for bReverse order
     */
    public AbstractTableDTOComparator(int nCol, boolean bRev) {
        this.nColumn = nCol;
        this.bReverse = bRev;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Implementation of the compare method.
     *
     * @param pObj1 the first object to compare
     * @param pObj2 the second object to compare
     * @return 0 if the two objects are equal, 1 if the first object is
     *  greater than the second object, -1 if the second object is greater than
     *  the first object
     */
    public abstract int compare(T obj1, T obj2);

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
