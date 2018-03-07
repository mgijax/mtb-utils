
/**
 * Copyright Neil Cochrane 2006
 * @author neilcochrane
 */

package org.jax.mgi.mtb.gui.completer;

import javax.swing.JTextField;

/**
 * A filter that will attempt to autocomplete enties into a textfield with the 
 * string representations of objects in a given array.
 *
 * Add this filter class to the Document of the text field.
 *
 * The first match in the array is the one used to autocomplete. So sort your 
 * array by most important objects first.
 */
public class MXCompleterFilter extends MXAbstractCompleterFilter {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    protected JTextField jTextField;
    protected Object[] objectList;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new instance of MXCompleterFilter
     * 
     * @param completerObjs an array of objects used to attempt completion
     * @param textField the text component to receive the completion
     */
    public MXCompleterFilter(Object[] completerObjs, JTextField textField) {
        objectList = completerObjs;
        jTextField = textField;
    }

    // --------------------------------------------------------- Public Methods

    public int getCompleterListSize() {
        return objectList.length;
    }

    public Object getCompleterObjectAt(int i) {
        return objectList[i];
    }

    public JTextField getTextField() {
        return jTextField;
    }

    /**
     * Set the list of objects to match against.
     * @param objectsToMatch
     */
    public void setCompleterMatches(Object[] objectsToMatch) {
        objectList = objectsToMatch;
        firstSelectedIndex = -1;
    }
    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

}
