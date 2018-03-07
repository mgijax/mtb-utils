package org.jax.mgi.mtb.gui.completer;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 */
public class MXCompleterTextField extends JTextField {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private MXCompleterFilter completerFilter;

    // ----------------------------------------------------------- Constructors

    /**
     * default constructor shows the completer window when offering matches.
     * @param completeMatches
     */
    public MXCompleterTextField(Object[] completeMatches) {
        this(completeMatches, true);
    }

    /**
     * useWindow - true will popup the completer window to help with matches,
     * false will just complete in the textfield with no window.
     */
    public MXCompleterTextField(Object[] completeMatches, boolean useWindow) {
        super();
        if (useWindow) {
            completerFilter = new MXCompleterFilterWithWindow(completeMatches, this);
        } else {
            completerFilter = new MXCompleterFilter(completeMatches, this);
        }

        PlainDocument pd = new PlainDocument();
        pd.setDocumentFilter(completerFilter);
        setDocument(pd);
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Warning: Calling setDocument on a completerTextField will remove the 
     * completion mecanhism for this text field if the document is not derived 
     * from AbstractDocument.
     *
     * Only AbstractDocuments support the required DocumentFilter API for 
     * completion.
     */
    public void setDocument(Document doc) {
        super.setDocument(doc);

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument)doc).setDocumentFilter(completerFilter);
        }
    }

    public boolean isCaseSensitive() {
        return completerFilter.isCaseSensitive();
    }

    public boolean isCorrectingCase() {
        return completerFilter.isCorrectingCase();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        completerFilter.setCaseSensitive(caseSensitive);
    }

    /**
     * Will change the user entered part of the string to match the case of the matched item.
     *
     * e.g.
     * "europe/lONdon" would be corrected to "Europe/London"
     *
     * This option only makes sense if case sensitive is turned off
     */
    public void setCorrectCase(boolean correctCase) {
        completerFilter.setCorrectCase(correctCase);
    }

    /**
     * Set the list of objects to match against.
     * @param completeMatches
     */
    public void setCompleterMatches(Object[] completeMatches) {
        completerFilter.setCompleterMatches(completeMatches);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
