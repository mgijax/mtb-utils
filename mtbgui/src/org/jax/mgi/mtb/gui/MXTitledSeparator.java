/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTitledSeparator.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * <p>A simple horizontal separator that contains a title.<br/>
 * <img src="http://www.jgui.com/SwingLabs/SwingX/MXTitledSeparator/MXTitledSeparator.png"/></p>
 *
 * <p>MXTitledSeparator allows you to specify the title via the #setTitle method.
 * The title alignment may be specified by using the #setHorizontalAlignment
 * method, and accepts all the same arguments as the JLabel#setHorizontalAlignment
 * method.</p>
 *
 * <p>In addition, you may specify an Icon to use with this separator. The icon
 * will appear "leading" the title (on the left in left-to-right languages,
 * on the right in right-to-left languages). To change the position of the
 * title with respect to the icon, call #setHorizontalTextPosition.</p>
 *
 * <p>The default font and color of the title comes from the LookAndFeel, mimicking
 * the font and color of the TitledBorder</p>
 *
 * <p>Here are a few example code snippets:
 * <pre><code>
 *  //create a plain separator
 *  MXTitledSeparator sep = new MXTitledSeparator();
 *  sep.setText("Customer Info");
 *
 *  //create a separator with an icon
 *  sep = new MXTitledSeparator();
 *  sep.setText("Customer Info");
 *  sep.setIcon(new ImageIcon("myimage.png"));
 *
 *  //create a separator with an icon to the right of the title,
 *  //center justified
 *  sep = new MXTitledSeparator();
 *  sep.setText("Customer Info");
 *  sep.setIcon(new ImageIcon("myimage.png"));
 *  sep.setHorizontalAlignment(SwingConstants.CENTER);
 *  sep.setHorizontalTextPosition(SwingConstants.TRAILING);
 * </code></pre>
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:23:19 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTitledSeparator.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 */
public class MXTitledSeparator extends JPanel {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * Implementation detail: the label used to display the title
     */
    private final JLabel label;
    /**
     * Implementation detail: a separator to use on the left of the
     * title if alignment is centered or right justified
     */
    private final JSeparator leftSeparator;
    /**
     * Implementation detail: a separator to use on the right of the
     * title if alignment is centered or left justified
     */
    private final JSeparator rightSeparator;


    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new instance of JXSeparator. The default title is simply
     * and empty string. Default justification is LEADING, and the default
     * horizontal text position is TRAILING (title follows icon)
     */
    public MXTitledSeparator() {
        setLayout(new GridBagLayout());

        label = new JLabel("MXTitledSeparator");
        leftSeparator = new JSeparator();
        rightSeparator = new JSeparator();

        initGUI();

        Dimension prefSize = getPreferredSize();
        prefSize.width = Math.max(prefSize.width, 50);
        setPreferredSize(prefSize);

        label.setForeground(UIManager.getColor("TitledBorder.titleColor"));
        label.setFont(UIManager.getFont("TitledBorder.font"));
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Sets the title for the separator. This may be simple html, or plain
     * text.
     *
     * @param title the new title. Any string input is acceptable
     */
    public void setTitle(String title) {
        String old = getTitle();
        label.setText(title);
        firePropertyChange("title", old, getTitle());
    }

    /**
     * Return the text.
     *
     * @return the title being used. This will be the raw title text, and so 
     *         may include html tags etc if they were so specified in 
     *         #setTitle.
     */
    public String getTitle() {
        return label.getText();
    }

    /**
     * Sets the alignment of the title along the X axis. If left/leading, then
     * the title will lead the separator (in left-to-right languages,
     * the title will be to the left and the separator to the right). If centered,
     * then a separator will be to the left, followed by the titled (centered),
     * followed by a separator to the right. Right/trailing will have the title
     * on the right with a separator to its left.
     *
     * @param alignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> (the default) or
     *           <code>TRAILING</code>.
     *
     * @see SwingConstants
     * @see #getHorizontalAlignment
     */
    public void setHorizontalAlignment(int alignment) {
        int old = getHorizontalAlignment();
        label.setHorizontalAlignment(alignment);
        if (old != getHorizontalAlignment()) {
            initGUI();
        }
        firePropertyChange("horizontalAlignment", old, 
                getHorizontalAlignment());
    }

    /**
     * Returns the alignment of the title contents along the X axis.
     *
     * @return   The value of the horizontalAlignment property, one of the
     *           following constants defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     *
     * @see #setHorizontalAlignment
     * @see SwingConstants
     */
    public int getHorizontalAlignment() {
        return label.getHorizontalAlignment();
    }

    /**
     * Sets the horizontal position of the title's text,
     * relative to the icon.
     *
     * @param position  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code>, or
     *           <code>TRAILING</code> (the default).
     * @exception IllegalArgumentException
     */
    public void setHorizontalTextPosition(int position) {
        int old = getHorizontalTextPosition();
        label.setHorizontalTextPosition(position);
        firePropertyChange("horizontalTextPosition", old, 
                getHorizontalTextPosition());
    }

    /**
     * Returns the horizontal position of the title's text,
     * relative to the icon.
     *
     * @return   One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     *
     * @see SwingConstants
     */
    public int getHorizontalTextPosition() {
        return label.getHorizontalTextPosition();
    }

    /**
     * Defines the icon this component will display.  If
     * the value of icon is null, nothing is displayed.
     * <p>
     * The default value of this property is null.
     *
     * @see #setHorizontalTextPosition
     * @see #getIcon
     */
    public void setIcon(Icon icon) {
        Icon old = getIcon();
        label.setIcon(icon);
        firePropertyChange("icon", old, getIcon());
    }

    /**
     * Returns the graphic image (glyph, icon) that the MXTitledSeparator 
     * displays.
     *
     * @return an Icon
     * @see #setIcon
     */
    public Icon getIcon() {
        return label.getIcon();
    }

    /**
     * @inheritDoc
     * Passes the foreground on to the label
     */
    @Override
    public void setForeground(Color foreground) {
        if (label != null) {
            label.setForeground(foreground);
        }
        super.setForeground(foreground);

    }
    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Implementation detail. lays out this component, showing/hiding 
     * components as necessary. Actually changes the containment (removes and 
     * adds components). MXTitledSeparator is treated as a single component 
     * rather than a container.
     */
    private void initGUI() {
        removeAll();

        switch (label.getHorizontalAlignment()) {
            case SwingConstants.LEFT:
            case SwingConstants.LEADING:
            case SwingConstants.WEST:
                add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(rightSeparator, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
                break;
            case SwingConstants.RIGHT:
            case SwingConstants.TRAILING:
            case SwingConstants.EAST:
                add(rightSeparator, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
                add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(label, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                break;
            case SwingConstants.CENTER:
            default:
                add(leftSeparator, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
                add(Box.createHorizontalStrut(3), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(label, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(Box.createHorizontalStrut(3), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0));
                add(rightSeparator, new GridBagConstraints(4, 0, 1, 1, 0.5, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
        }
    }
}
