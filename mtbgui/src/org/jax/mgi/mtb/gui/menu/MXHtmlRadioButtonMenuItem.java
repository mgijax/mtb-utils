/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlRadioButtonMenuItem.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.menu;

import javax.swing.JRadioButtonMenuItem;

/**
 * Menu item with HTML rendering capabilities.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:05 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlRadioButtonMenuItem.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 */
public final class MXHtmlRadioButtonMenuItem extends JRadioButtonMenuItem {

    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables

    /**
     * The inner HTML item.
     */
    private final MXHtmlItem htmlItem;

    
    // ----------------------------------------------------------- Constructors
    
    /**
     * Simple constructor.
     *
     * @param text The inner HTML text.
     */
    public MXHtmlRadioButtonMenuItem(String text) {
        super(text);
        this.htmlItem = new MXHtmlItem(text);
        super.setText(this.htmlItem.getFullText());
        this.setOpaque(false);
    }

    // --------------------------------------------------------- Public Methods
    
    /**
     * Overrides menuSelectionChanged in javax.swing.MenuElement.
     *
     * @see javax.swing.MenuElement#menuSelectionChanged(boolean)
     */
    public final void menuSelectionChanged(boolean isIncluded) {
        super.menuSelectionChanged(isIncluded);
        this.htmlItem.statusChanged(isIncluded, this.isEnabled());
        super.setText(this.htmlItem.getFullText());
    }

    /**
     * Overrides setEnabled in java.awt.Component.
     *
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