/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlCheckboxMenuItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.menu;

import javax.swing.JCheckBoxMenuItem;

/**
 * Menu item with HTML rendering capabilities.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:04 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlCheckboxMenuItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 */
public final class MXHtmlCheckboxMenuItem extends JCheckBoxMenuItem {

    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables

    /**
     * The inner HTML item.
     */
    private MXHtmlItem htmlItem;
    
    
    // ----------------------------------------------------------- Constructors
    
    /**
     * Simple constructor.
     *
     * @param text The inner HTML text.
     */
    public MXHtmlCheckboxMenuItem(String text) {
        super(text);
        this.htmlItem = new MXHtmlItem(text);
        super.setText(this.htmlItem.getFullText());
        this.setOpaque(false);
    }
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Set the text.
     * 
     * @param text The text
     * @see javax.swing.AbstractButton#setText(java.lang.String)
     */
    public void setText(String text) {
        this.htmlItem = new MXHtmlItem(text);
        super.setText(this.htmlItem.getFullText());
    }
    
    
    /**
     * Signals when the menu selection has changed.
     *
     * @param isIncluded <code>true</code to be included, <code>false</code>
     * otherwise 
     * @see javax.swing.MenuElement#menuSelectionChanged(boolean)
     */
    public final void menuSelectionChanged(boolean isIncluded) {
        super.menuSelectionChanged(isIncluded);
        this.htmlItem.statusChanged(isIncluded, this.isEnabled());
        super.setText(this.htmlItem.getFullText());
    }
    
    /*
     * Sets this to be enabled or disabled.
     *
     * @param isEnabled <code>true</code> to enable, <code>false</code>
     * otherwise
     * @see java.awt.Component#setEnabled(boolean)
     */
    public final void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.htmlItem.setEnabled(isEnabled);
        super.setText(this.htmlItem.getFullText());
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}