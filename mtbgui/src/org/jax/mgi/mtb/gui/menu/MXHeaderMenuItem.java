/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHeaderMenuItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JMenuItem;
import org.jax.mgi.mtb.gui.util.ImageCreator;

/**
 * Header menu item.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:04 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHeaderMenuItem.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 */
public final class MXHeaderMenuItem extends JMenuItem {
    
    // -------------------------------------------------------------- Constants
    
    private static final Font HEADER_FONT = new Font("Tahoma", Font.BOLD, 14);
    
    // ----------------------------------------------------- Instance Variables
    // none
    
    // ----------------------------------------------------------- Constructors
    
    /**
     * Simple constructor.
     *
     * @param text The inner HTML text.
     */
    public MXHeaderMenuItem(String text) {
        super(text);
        this.setEnabled(false);
    }
    
    // --------------------------------------------------------- Public Methods
    // none
    
    // ------------------------------------------------------ Protected Methods
    
    /**
     * Paint the component.
     * 
     * @param g The <code>Graphics</code> object
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected final void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // paint background image
        final BufferedImage image = 
            ImageCreator.getGradientCubesImage(this.getWidth(),
                                               this.getHeight(), 
                                               ImageCreator.mainMidColor,
                                               ImageCreator.mainUltraDarkColor, 
                                               (int) (0.7 * this.getWidth()),
                                               (int) (0.9 * this.getWidth()));
        g2d.drawImage(image, 0, 0, null);
        g2d.setFont(HEADER_FONT);
        
        // place the text slightly to the left of the center
        int x = (this.getWidth() - 
                         g2d.getFontMetrics().stringWidth(this.getText())) / 4;
        
        int y = (int)(g2d.getFontMetrics().getLineMetrics(this.getText(), g2d).
                getHeight());
        
        // paint the text with black shadow
        g2d.setColor(Color.black);
        g2d.drawString(this.getText(), x + 1, y + 1);
        g2d.setColor(Color.white);
        g2d.drawString(this.getText(), x, y);
    }
    
    // -------------------------------------------------------- Private Methods
    // none
}
