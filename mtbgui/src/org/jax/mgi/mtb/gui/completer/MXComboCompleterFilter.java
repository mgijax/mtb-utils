package org.jax.mgi.mtb.gui.completer;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class to add completion mechanism to combo boxes.
 * The class assumes that the look and field uses a JTextField as the combo
 * box editor. A check should be done to ensure this is true before
 * adding this class to a ComboBox.
 * 
 * To add to a combo, call the static method addCompletionMechanism(yourCombo),
 * or do the following:
 * 
 *   if (!(myCombo.getEditor().getEditorComponent() instanceof JTextField))
 *     return;
 * 
 *   JTextField tf = (JTextField)myCombo.getEditor().getEditorComponent();
 *   PlainDocument pd = new PlainDocument();
 *   _filter = new MXComboCompleterFilter(myCombo);
 *   pd.setDocumentFilter(_filter);
 *   tf.setDocument(pd);
 * 
 * @author ncochran
 */
public class MXComboCompleterFilter extends MXAbstractCompleterFilter {
    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private JComboBox jComboBox;

    // ----------------------------------------------------------- Constructors

    public MXComboCompleterFilter(JComboBox combo) {
        jComboBox = combo;
    }

    // --------------------------------------------------------- Public Methods

    public int getCompleterListSize() {
        return jComboBox.getModel().getSize();
    }

    public Object getCompleterObjectAt(int i) {
        return jComboBox.getItemAt(i);
    }

    public JTextField getTextField() {
        return (JTextField)jComboBox.getEditor().getEditorComponent();
    }

    /**
     * Helper method to add auto-completion to a jcombobox or derivation.
     *
     * The look and feel must use a JTextField as the combo box editor (or null
     * will be returned).
     *
     * The JTextField will have it's document set to a new PlainDocument and 
     * the returned filter will be set to autocomplete the contents.
     *
     * Use the returned filter to set options such as case-sensitivity.
     *
     * @param combo
     */
    static public MXComboCompleterFilter addCompletionMechanism(JComboBox combo) {
        if (!(combo.getEditor().getEditorComponent() instanceof JTextField)) {
            return null;
        }

        JTextField tf = (JTextField)combo.getEditor().getEditorComponent();
        PlainDocument pd = new PlainDocument();
        MXComboCompleterFilter  filter = new MXComboCompleterFilter(combo);
        pd.setDocumentFilter(filter);
        tf.setDocument(pd);

        return filter;
    }

    public void replace(FilterBypass filterBypass, int offset, int length, 
                        String string, AttributeSet attributeSet) 
        throws BadLocationException {

        super.replace(filterBypass, offset, length, string, attributeSet);

        // Try to select the item in the combo list
        if (firstSelectedIndex != -1) {
            JTextField tf = 
                    (JTextField)jComboBox.getEditor().getEditorComponent();
            int preTextLen = preText.length();
            String text = tf.getText();

            jComboBox.setSelectedIndex(firstSelectedIndex);
            filterBypass.replace(0, tf.getDocument().getLength(), text, 
                                 attributeSet);

            getTextField().select(preTextLen, tf.getDocument().getLength());
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
}
