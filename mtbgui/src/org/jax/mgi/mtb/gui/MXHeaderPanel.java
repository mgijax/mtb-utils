/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXHeaderPanel.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

/**
 * A <code>JPanel</code> which can be used as a header title panel.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:23:19 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXHeaderPanel.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 */
public class MXHeaderPanel extends JPanel {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * Holds the left icon if any is specified.
     */
    private JLabel lblImageLeft;

    /**
     * Holds the right icon if any is specified.
     */
    private JLabel lblImageRight;

    /**
     * Holds the title.
     */
    private JLabel lblTitle;

    /**
     * The starting color.
     */
    private Color colorStart;

    /**
     * The ending color.
     */
    private Color colorFinish;

    /**
     * The position of which to start the gradient painting.
     */
    private int nPosition = -1;

    /**
     * A separator.
     */
    private JSeparator jseparator;

    /**
     * Specifies whether or not to draw the separator.
     */
    private boolean drawSeparatorUnderneath = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Default constuctor.
     */
    public MXHeaderPanel() {
        this(null);
    }

    /**
     * Constructor which set the title of the header panel.
     *
     * @param title the title
     */
    public MXHeaderPanel(String title) {
        this(title, null);
    }

    /**
     * Constructor which set the title and icon of the header panel.
     *
     * @param title the title
     * @param icon the icon
     */
    public MXHeaderPanel(String strTitle, Icon icon) {
        super();
        initGUI();
        lblTitle.setText(strTitle);
        lblTitle.setIcon(icon);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Set the draw separator underneath property.
     *
     * @param draw specify <code>true</code> to draw the separator,
     *        <code>false</code> otherwise
     */
    public void setDrawSeparatorUnderneath(boolean draw) {
        this.drawSeparatorUnderneath = draw;
    }

    /**
     * Get the draw separator underneath property.
     *
     * @return <code>true</code> if the separator is to be drawn, 
     *        <code>false</code> otherwise
     */
    public boolean isDrawSeparatorUnderneath() {
        return this.drawSeparatorUnderneath;
    }

    /**
     * Set the foreground color.
     *
     * @param fg the color
     */
    public void setForeground(Color fg) {
        Color old = getForeground();
        if (lblTitle != null) {
            lblTitle.setForeground(fg);
        }
        repaint();
        firePropertyChange("foreground", old, getForeground());
    }

    /**
     * Get the foreground color.
     *
     * @return the foreground color
     */
    public Color getForeground() {
        return (lblTitle == null ? null : lblTitle.getForeground());
    }

    /**
     * Set the font.
     *
     * @param font the font to use
     */
    public void setFont(Font font) {
        Font old = getFont();
        if (lblTitle != null) {
            lblTitle.setFont(font);
        }
        repaint();
        firePropertyChange("font", old, getFont());
    }

    /**
     * Get the font.
     *
     * @return the font to use
     */
    public Font getFont() {
        return (lblTitle == null ? null : lblTitle.getFont());
    }

    /**
     * Set the text of the title.
     *
     * @param title the text of the title
     */
    public void setText(String title) {
        String old = getText();
        if (lblTitle != null) {
            lblTitle.setText(title);
        }
        repaint();
        firePropertyChange("text", old, getText());
    }

    /**
     * Get the text of the title.
     *
     * @return the text of the title
     */
    public String getText() {
        return (lblTitle == null ? null : lblTitle.getText());
    }

    /**
     * Set the finishing color to use.
     *
     * @param color The <code>Color</code> to use
     */
    public void setStartColor(Color color) {
        Color old = colorStart;
        colorStart = color;
        repaint();
        firePropertyChange("startColor", old, getStartColor());
    }

    /**
     * Get the start color.
     * 
     * @return The start color
     */
    public Color getStartColor() {
        return colorStart;
    }

    /**
     * Set the finishing color.
     *
     * @param color The <code>Color</code> to use
     */
    public void setFinishColor(Color color) {
        Color old = colorFinish;
        colorFinish = color;
        repaint();
        firePropertyChange("startColor", old, getStartColor());
    }

    /**
     * Get the finish color.
     *
     * @return The finish color
     */
    public Color getFinishColor() {
        return colorFinish;
    }

    /**
     * Set the position in a percentage of where the gradient should start.
     *
     * @param pos the position
     */
    public void setPosition(int pos) {
        int old = nPosition;
        nPosition = pos;
        repaint();
        firePropertyChange("postition", old, getPosition());
    }

    /**
     * Get the position in a percentage of where the gradient should start.
     *
     * @return the position
     */
    public int getPosition() {
        return this.nPosition;
    }

    /**
     * Set the icon to use.  Defaults to the left side icon.
     *
     * @param icon the icon to use
     */
    public void setIcon(Icon icon) {
        setIconLeft(icon);
    }

    /**
     * Get the icon that is being used.  Defaults to the left side icon.
     *
     * @return the icon being used
     */
    public Icon getIcon() {
        return getIconLeft();
    }

    /**
     * Set the icon to use on the left side.
     *
     * @param icon the icon to use
     */
    public void setIconLeft(Icon icon) {
        Icon old = getIconLeft();
        lblImageLeft.setIcon(icon);
        firePropertyChange("icon", old, getIconLeft());
    }

    /**
     * Get the icon that is being used on the left side.
     *
     * @return the icon being used
     */
    public Icon getIconLeft() {
        return lblImageLeft.getIcon();
    }

    /**
     * Set the icon to use on the right side.
     *
     * @param icon the icon to use
     */
    public void setIconRight(Icon icon) {
        Icon old = getIconRight();
        lblImageRight.setIcon(icon);
        firePropertyChange("icon", old, getIconRight());
    }

    /**
     * Get the icon that is being used on the right side.
     *
     * @return the icon being used
     */
    public Icon getIconRight() {
        return lblImageRight.getIcon();
    }

    /**
     * Paint the component.
     *
     * @param g the <code>Graphics</code> context
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        if (colorStart == null) {
            colorStart = getParentBackgroundColor();
        }

        if (colorFinish == null) {
            colorFinish = getParentBackgroundColor();
        }

        if ((colorStart != null) && (colorFinish != null)) {
            Dimension dim = getSize();
            Graphics2D g2 = (Graphics2D) g;
            Insets inset = getInsets();
            int wide = dim.width - (inset.left + inset.right);
            int high = dim.height - (inset.top + inset.bottom);

            if (nPosition > 0) {
                int cutoff = (int) ((float) wide * ((float) nPosition / 100));

                g2.setColor(colorStart);
                g2.fillRect(inset.left, inset.top, wide, high);
                GradientPaint p =
                    new GradientPaint(cutoff, 0, colorStart, wide,
                                      0, colorFinish, false);

                g2.setPaint(p);
                g2.fillRect(cutoff, inset.top, wide - cutoff, high);
            } else {
                g2.setPaint(
                        new GradientPaint(inset.left, inset.top, colorStart,
                        wide, high, colorFinish, true));
                g2.fillRect(inset.left, inset.top, wide, high);
            }
        }

        // draw the bottom
        if (drawSeparatorUnderneath) {
            g.setColor( jseparator.getForeground() );
            g.drawLine( 0, getHeight() - 2, getWidth(), getHeight() - 2 );
            g.setColor( jseparator.getBackground() );
            g.drawLine( 0, getHeight() - 1, getWidth(), getHeight() - 1);
        }

        super.paintComponent(g);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Initialize the components and lay them out.
     */
    private void initGUI() {
        setLayout(new BorderLayout());
        setOpaque(false);

        Font font = UIManager.getDefaults().getFont("Label.font");
        font = font.deriveFont((float)font.getSize() + 1);
        colorStart = UIManager.getDefaults().getColor("Desktop.background");

        lblTitle = new JLabel("MXHeaderPanel");
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lblTitle.setForeground(UIManager.getDefaults().getColor(
               "InternalFrame.activeTitleForeground"));
        lblTitle.setFont(font);
        add(lblTitle, BorderLayout.CENTER);

        lblImageLeft = new JLabel();
        add(lblImageLeft, BorderLayout.WEST);

        lblImageRight = new JLabel();
        add(lblImageRight, BorderLayout.EAST);


        jseparator = new JSeparator();
    }

    /**
     * Get the background color of the parent component.
     *
     * @return the background color of the parent component or null if there is
     *         no parent component
     */
    private Color getParentBackgroundColor() {
        Container c = this.getParent();
        Color pbg = null;

        if (c != null) {
            pbg = c.getBackground();
        }

        return pbg;
    }
}
