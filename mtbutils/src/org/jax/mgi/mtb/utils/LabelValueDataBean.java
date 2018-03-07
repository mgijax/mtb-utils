/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueDataBean.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.io.Serializable;

/**
 * Simple Java bean to hold a label and associated value and data element.
 *
 * @author mjv
 * @date 2007/04/30 15:52:18
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueDataBean.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * @see LabelValueBean
 */
public class LabelValueDataBean<L,V,D>
    extends LabelValueBean<L,V>
    implements Serializable {

    // -------------------------------------------------------------- Constants
    private static final long serialVersionUID = 7482368468528737325L;

    // ----------------------------------------------------- Instance Variables

    private D data = null;

    // ----------------------------------------------------------- Constructors
    
    /**
     * Default constructor.
     */
    public LabelValueDataBean() {
        this(null, null, null);
    }

    /**
     * Constructor.
     *
     * @param label The label.
     * @param value The value of the associated label.
     * @param data The data element of the associated label.
     */
    public LabelValueDataBean(L label, V value, D data) {
        super(label, value);
        setData(data);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Ge the data.
     *
     * @return The data
     */
    public D getData() {
        return data;
    }

    /**
     * Set the data.
     *
     * @param data The data.
     */
    public void setData(D data) {
        this.data = data;
    }
    
    public int compareData(LabelValueDataBean other) {
        int iRet = 0;
        
        if (other == null) {
            iRet = -1;
        } else {
            if ((getData() == null) && (other.getData() == null)) {
                iRet = 0;
            } else if ((getData() != null) && (other.getData() == null)) {
                iRet = -1;
            } else if ((getData() == null) && (other.getValue() != null)) {
                iRet = 1;
            } else {
                iRet = StringUtils.compare(getData().toString(), 
                                           other.getData().toString(),
                                           true, true);
            }
        }

        return iRet;
    }
    

    /**
     * Return a <code>String</code> representing the values of the bean. 
     *
     * @return A <code>String</code> representation of the bean.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(LabelValueDataBean.class.getName());
        sb.append('[');
        sb.append(getLabel());
        sb.append(',');
        sb.append(getValue());
        sb.append(',');
        sb.append(getData());
        sb.append(']');
        return sb.toString();
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}
