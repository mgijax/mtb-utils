/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.menu;

/**
 * Item with HTML rendering capabilities. Provides rendering capabilities for
 * enabled, disabled and enabled-selected condition.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:04 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 */
public final class MXHtmlItem {

    // -------------------------------------------------------------- Constants
    
    public static final String ENABLED_SELECTED = "black";
    public static final String ENABLED = "black";
    public static final String DISABLED = "#b0b0b0";
    public static final String HEADER = "white";
    
    // ----------------------------------------------------- Instance Variables
    
    /**
     * Status of <code>this</code> item
     */
    private String status;
    
    /**
     * Inner text of <code>this</code> item (without the main
     * <code>HTML</code> and <code>BODY</code> tags and color information).
     */
    private String innerText = "";
    
    /**
     * The full text of <code>this</code> item.
     */
    private String fullText = "";
    
    
    // ----------------------------------------------------------- Constructors
    
    /**
     * Simple constructor.
     */
    public MXHtmlItem() {
        this.status = ENABLED;
    }
    
    /**
     * Simple constructor.
     *
     * @param text The initial inner text.
     */
    public MXHtmlItem(String text) {
        this.innerText = text;
        this.status = ENABLED;
        this.updateText();
    }
    

    // --------------------------------------------------------- Public Methods
    
    /**
     * Sets whether or not this is a header item.
     */
    public final void setIsHeader() {
        this.status = HEADER;
        this.updateText();
    }
    
    /**
     * Updates the full text of <code>this</code> item.
     */
    public final void updateText() {
        this.fullText = getFullString(this.innerText, this.status);
    }
    
    /**
     * Sets new value for the inner text of <code>this</code> item. The full
     * text is computed accordingly.
     *
     * @param text New value for the inner text of <code>this</code> item.
     */
    public final void setText(String text) {
        this.innerText = text;
        this.updateText();
    }
    
    /**
     * Indicates that the status of <code>this</code> item should be
     * recomputed.
     *
     * @param isIncluded The <code>included</code> status.
     * @param isEnabled The <code>enabled</code> status.
     */
    public final void statusChanged(boolean isIncluded, boolean isEnabled) {
        if (this.status == HEADER) {
            return;
        }
        
        this.status = ENABLED;
        
        if (isIncluded && isEnabled) {
            this.status = ENABLED_SELECTED;
        }
        
        if (!isEnabled) {
            this.status = DISABLED;
        }
        
        this.updateText();
    }
    
    /**
     * Sets the <code>enabled</code> status of <code>this</code> item.
     *
     * @param isEnabled The <code>enabled</code> status of <code>this</code> 
     * item.
     */
    public final void setEnabled(boolean isEnabled) {
        if (this.status == HEADER) {
            return;
        }
        
        this.status = (isEnabled ? ENABLED : DISABLED);
        
        this.updateText();
    }
    
    /**
     * Returns the full text of <code>this</code> item.
     *
     * @return The full text of <code>this</code> item.
     */
    public final String getFullText() {
        return this.fullText;
    }
    
    /**
     * Returns the status of <code>this</code> item.
     *
     * @return The status of <code>this</code> item.
     */
    public final String getStatus() {
        return status;
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    
    /**
     * Returns the formatted full text for the specified status.
     *
     * @param innerText The inner text to format.
     * @param status The status.
     * @return The formatted full text.
     */
    private static String getFullString(String innerText, String status) {
        return "<html><font color=" + status + ">"
                + innerText + "</font></html>";
    }
    
    
    // ------------------------------------------------------------ Inner Class
    
    class ItemStatus {
        /**
         * The associated foreground text color.
         */
        private final String color;
        
        /**
         * Simple constructor.
         *
         * @param color The associated foreground text color.
         */
        ItemStatus(String color) {
            this.color = color;
        }
        
        /**
         * Returns the associated foreground text color.
         *
         * @return The associated foreground text color.
         */
        public String getColor() {
            return this.color;
        }
    }
}