/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/hyperlink/ui/MXHyperLinkUI.java,v 1.1 2008/08/01 12:23:21 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.hyperlink.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;
import org.jax.mgi.mtb.gui.MXHyperlink;

/**
 * UI for <code>MXHyperlink</code>.
 *
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @see javax.swing.plaf.metal.MetalButtonUI
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/hyperlink/ui/MXHyperLinkUI.java,v 1.1 2008/08/01 12:23:21 sbn Exp $
 * @date $Date: 2008/08/01 12:23:21 $
 */
 public class MXHyperLinkUI extends MetalButtonUI {

    // -------------------------------------------------------------- Constants

    private static final MXHyperLinkUI ui = new MXHyperLinkUI();


    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     *
     * @param jcomponent
     * @return
     */
    public static ComponentUI createUI(JComponent jcomponent) {
        return ui;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Paint the "hyperlink" styled text.
     *
     * @param g the graphics context
     * @param com the component
     * @param rect the rectangle
     * @param s the text to draw
     */
    protected void paintText(Graphics g, JComponent com, Rectangle rect,
                             String s) {

        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        MXHyperlink bn = (MXHyperlink) com;
        ButtonModel bnModel = bn.getModel();

        if (bnModel.isEnabled()) {
            if (bnModel.isPressed()) {
                bn.setForeground(bn.getActiveLinkColor());
            } else if (bn.isLinkVisited()) {
                bn.setForeground(bn.getVisitedLinkColor());
            } else {
                bn.setForeground(bn.getLinkColor());
            }
        } else {
            if (bn.getDisabledLinkColor() != null) {
                bn.setForeground(bn.getDisabledLinkColor());
            }
        }
        super.paintText(g2d, com, rect, s);
        int behaviour = bn.getLinkBehavior();
        boolean drawLine = false;

        if (behaviour == MXHyperlink.HOVER_UNDERLINE) {
            if (bnModel.isRollover()) {
                drawLine = true;
            }
        } else if (behaviour == MXHyperlink.ALWAYS_UNDERLINE ||
                   behaviour == MXHyperlink.SYSTEM_DEFAULT) {
            drawLine = true;
        }

        if (!drawLine) {
            return;
        }

        FontMetrics fm = g2d.getFontMetrics();
        int x = rect.x + getTextShiftOffset();
        int y = rect.y + fm.getAscent() + fm.getDescent() +
                getTextShiftOffset() - 1;

        if (bnModel.isEnabled()) {
            g2d.setColor(bn.getForeground());
            g2d.drawLine(x, y, (x + rect.width) - 1, y);
        } else {
            g2d.setColor(bn.getBackground().brighter());
            g2d.drawLine(x, y, (x + rect.width) - 1, y);
        }
    }

    // -------------------------------------------------------- Private Methods
    // none
}
