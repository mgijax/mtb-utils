package org.jax.mgi.mtb.gui.completer;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

abstract public class MXAbstractCompleterFilter extends DocumentFilter {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * The text in the input field before we started last looking for matches.
     */
    protected String preText;
    protected boolean bCase = false;
    protected boolean bCorrective = true;
    protected int firstSelectedIndex = -1;


    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    abstract public int getCompleterListSize();

    abstract public Object getCompleterObjectAt(int i);

    abstract public JTextField getTextField();

    public void replace(FilterBypass filterBypass, int offset, int length,
                        String string, AttributeSet attributeSet)
        throws BadLocationException {
        super.replace(filterBypass, offset, length, string, attributeSet);
        Document doc = filterBypass.getDocument();
        preText = doc.getText(0,doc.getLength());
        firstSelectedIndex = -1;

        for (int i=0; i< getCompleterListSize(); i++) {
            String objString = getCompleterObjectAt(i).toString();

            if ((bCase)
            ? objString.equals(preText)
            : objString.equalsIgnoreCase(preText)) {
                firstSelectedIndex = i;

                if (bCorrective) {
                    filterBypass.replace(0, preText.length(), objString, attributeSet);
                }
                break;
            }

            if (objString.length() <= preText.length()) {
                continue;
            }

            String objStringStart = objString.substring(0, preText.length());

            if ((bCase)
            ? objStringStart.equals(preText)
            : objStringStart.equalsIgnoreCase(preText)) {
                String objStringEnd = objString.substring(preText.length());
                if (bCorrective) {
                    filterBypass.replace(0, preText.length(), objString, attributeSet);
                } else {
                    filterBypass.insertString(preText.length(), objStringEnd, attributeSet);
                }

                getTextField().select(preText.length(), doc.getLength());
                firstSelectedIndex = i;
                break;
            }
        }
    }

    public void insertString(FilterBypass filterBypass, int offset,
                             String string, AttributeSet attributeSet)
    throws BadLocationException {
        super.insertString(filterBypass, offset, string, attributeSet);
    }

    public void remove(FilterBypass filterBypass, int offset, int length)
    throws BadLocationException {
        super.remove(filterBypass, offset, length);
    }

    public void setCaseSensitive(boolean caseSensitive) {
        bCase = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return bCase;
    }

    /**
     * Will change the user entered part of the string to match the case of 
     * the matched item.
     *
     * e.g.
     * "europe/lONdon" would be corrected to "Europe/London"
     *
     * This option only makes sense if case sensitive is turned off
     */
    public void setCorrectCase(boolean correctCase) {
        bCorrective = correctCase;
    }

    public boolean isCorrectingCase() {
        return bCorrective;
    }

    /**
     *
     * @return the index of the first object in the object array that can match
     * the user entered string (-1 if no object is currently being used as a 
     * match)
     */
    public int getLeadingSelectedIndex() {
        return firstSelectedIndex;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

}
