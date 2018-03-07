package org.jax.mgi.mtb.gui.completer;

import java.util.List;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * An editable combo class that will autocomplete the user entered text to the
 * entries in the combo drop down.
 * 
 * You can directly add auto-complete to existing JComboBox derived classes
 * using:
 * MXComboCompleterFilter.addCompletion(yourCombo);
 */
public class MXCompleterComboBox extends JComboBox {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private MXComboCompleterFilter filter;

    // ----------------------------------------------------------- Constructors

    public MXCompleterComboBox() {
        super();
    }

    public MXCompleterComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    public MXCompleterComboBox(Object[] items) {
        super(items);
        _init();
    }

    public MXCompleterComboBox(Vector items) {
        super(items);
    }

    public MXCompleterComboBox(List items) {
        super(new Vector(items));
    }


    // --------------------------------------------------------- Public Methods

    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    public void setCorrectCase(boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    private void _init() {
        setEditable(true);
        filter = MXComboCompleterFilter.addCompletionMechanism(this);
    }
}
