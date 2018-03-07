/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueBean.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.io.Serializable;

/**
 * Simple Java bean to hold a label and associated value.
 *
 * @author mjv
 * @date 2007/04/30 15:52:18
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/LabelValueBean.java,v 1.1 2007/04/30 15:52:18 mjv Exp
 */
public class LabelValueBean<L,V>
    implements Serializable {

    // -------------------------------------------------------------- Constants
    
    private static final long serialVersionUID = 2479854968528737325L;
    
    
    // ----------------------------------------------------- Instance Variables
    private L label = null;
    private V value = null;

    
    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor.
     */
    public LabelValueBean() {
        this(null, null);
    }

    /**
     * Constructor.
     *
     * @param label The label.
     * @param value The value of the associated label.
     */
    public LabelValueBean(L label, V value) {
        setLabel(label);
        setValue(value);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Retrieve the label.
     * <p>
     * <i>Note this is declared as final for optimation purposes.  Java can 
     * inline functions that are static, private, or declared final.</i>
     *
     * @return A <code>L</code> object containing the label.
     */
    public final L getLabel() {
        return label;
    }

    /**
     * Ste the label.
     *
     * @param label The label to set.
     */
    public final void setLabel(L label) {
        this.label = label;
    }

    /**
     * Get the value.
     *
     * @return The value
     */
    public final V getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The value to set
     */
    public final void setValue(V value) {
        this.value = value;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof LabelValueBean) {
            LabelValueBean bean = (LabelValueBean)obj;
            
            if (!(bean.getValue().equals(this.getValue()))) {
                return false;
            }
            
            if (!(bean.getLabel().equals(this.getLabel()))) {
                return false;
            }
            
            
            return true;
        }
        
        return false;
    }
    
    public int compareLabel(LabelValueBean other) {
        int iRet = 0;
        
        if (other == null) {
            iRet = -1;
        } else {
            if ((getLabel() == null) && (other.getLabel() == null)) {
                iRet = 0;
            } else if ((getLabel() != null) && (other.getLabel() == null)) {
                iRet = -1;
            } else if ((getLabel() == null) && (other.getLabel() != null)) {
                iRet = 1;
            } else {
                iRet = StringUtils.compare(getLabel().toString(), 
                                           other.getLabel().toString(),
                                           true, true);
            }
        }

        return iRet;
    }

    public int compareValue(LabelValueBean other) {
        int iRet = 0;
        
        if (other == null) {
            iRet = -1;
        } else {
            if ((getValue() == null) && (other.getValue() == null)) {
                iRet = 0;
            } else if ((getValue() != null) && (other.getValue() == null)) {
                iRet = -1;
            } else if ((getValue() == null) && (other.getValue() != null)) {
                iRet = 1;
            } else {
                iRet = StringUtils.compare(getValue().toString(), 
                                           other.getValue().toString(),
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
        StringBuffer sb = new StringBuffer(LabelValueBean.class.getName());
        sb.append('[');
        sb.append(getLabel());
        sb.append(',');
        sb.append(getValue());
        sb.append(']');
        return sb.toString();
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}
