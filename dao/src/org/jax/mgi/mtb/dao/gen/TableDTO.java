/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/gen/TableDTO.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao.gen;

import org.jax.mgi.mtb.utils.DataBean;
import org.jax.mgi.mtb.utils.FieldPrinter;

/**
 * A generic DTO for other database tables to inherit from.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:39 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/gen/TableDTO.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 */
public class TableDTO {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    protected boolean bIsNew = true;
    protected boolean bIsOld = false;
    protected DataBean dataBean = new DataBean();


    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor.
     */
    public TableDTO() {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Determines if the current object is new.
     *
     * @return <code>true</code> if the current object is new,
     *         <code>false</code> if the object is not new
     */
    public boolean isNew() {
        return bIsNew;
    }

    /**
     * Specifies to the object if it has been set as new.
     *
     * @param isNew the boolean value to be assigned
     */
    public void isNew(boolean isNew) {
        this.bIsNew = isNew;
    }

    /**
     * Determines if the current object is old and needs to be deleted.
     *
     * @return <code>true</code> if the current object is to be deleted,
     *         <code>false</code> if the object is not
     */
    public boolean isOld() {
        return bIsOld;
    }

    /**
     * Specifies to the object if it has been set to be deleted.
     *
     * @param isOld the boolean value to be assigned
     */
    public void isOld(boolean isOld) {
        this.bIsOld = isOld;
    }

    /**
     * Determines if the object has been modified since the last time this
     * method was called.
     * <p>
     * We can also determine if this object has ever been modified since its
     * creation.
     *
     * @return <code>true</code> if the object has been modified,
     *         <code>false</code> if the object has not been modified
     */
    public boolean isModified() {
        return false;
    }

    /**
     * Resets the object modification status to 'not modified'.
     */
    public void resetIsModified() {
        // no-op'd
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param dtoTable the bean to copy into the current bean
     */
    public void copy(TableDTO dtoTable) {
        // no-op'd
    }

    /**
     * Get the associated <code>DataBean</code>.
     *
     * @return the <code>DataBean</code>
     */
    public DataBean getDataBean() {
        return this.dataBean;
    }

    /**
     * Set the <code>DataBean</code> object.
     *
     * @param dtoSimple the <code>DataBean</code>
     */
    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * Returns the object string representation.
     *
     * @return the object as a string
     */
    public String toString() {
        return FieldPrinter.getFieldsAsString(this);
    }

    /**
     * Returns the object string representation in XML formt.
     *
     * @return the object as a string
     */
    public String toXML() {
        return "";
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
