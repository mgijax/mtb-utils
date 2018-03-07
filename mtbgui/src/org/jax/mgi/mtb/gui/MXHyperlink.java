/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXHyperlink.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import org.jax.mgi.mtb.gui.hyperlink.*;
import java.awt.Color;
import java.awt.Cursor;
import java.net.URL;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import org.jax.mgi.mtb.gui.hyperlink.ui.MXHyperLinkUI;

/**
 * A component used to simulate the look and feel of a hyperlink.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:23:18 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXHyperlink.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * @see javax.swing.JButton
 */
public class MXHyperlink extends JButton {

    // -------------------------------------------------------------- Constants

    public static final int ALWAYS_UNDERLINE = 0;
    public static final int HOVER_UNDERLINE = 1;
    public static final int NEVER_UNDERLINE = 2;
    public static final int SYSTEM_DEFAULT = 3;


    // ----------------------------------------------------- Instance Variables

    private int linkBehavior;
    private Color linkColor;
    private Color colorPressed;
    private Color visitedLinkColor;
    private Color disabledLinkColor;
    private URL buttonURL;
    private Action defaultAction;
    private boolean isLinkVisited;


    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor.
     */
    public MXHyperlink() {
        this(null, null, null);
    }

    /**
     * Constructor.
     *
     * @param action the action to invoke
     */
    public MXHyperlink(Action action) {
        this();
        setAction(action);
    }

    /**
     * Constructor.
     *
     * @param icon the icon for the button
     */
    public MXHyperlink(Icon icon) {
        this(null, icon, null);
    }

    /**
     * Constructor.
     *
     * @param s the button text
     */
    public MXHyperlink(String s) {
        this(s, null, null);
    }

    /**
     * Constructor.
     *
     * @param url the button url
     */
    public MXHyperlink(URL url) {
        this(null, null, url);
    }

    /**
     * Constructor.
     *
     * @param s the button text
     * @param url the button url
     */
    public MXHyperlink(String s, URL url) {
        this(s, null, url);
    }

    /**
     * Constructor.
     *
     * @param icon the icon of the button
     * @param url the url of the button
     */
    public MXHyperlink(Icon icon, URL url) {
        this(null, icon, url);
    }

    /**
     * Constructor.
     *
     * @param text the button text
     * @param icon the icon of the button
     * @param url the url of the button
     */
    public MXHyperlink(String text, Icon icon, URL url) {
        super(text, icon);
        linkBehavior = HOVER_UNDERLINE;
        linkColor = Color.blue;
        colorPressed = Color.red;
        visitedLinkColor = new Color(128, 0, 128);
        if (text == null && url != null) {
            setText(url.toExternalForm());
        }
        buttonURL = url;
        setCursor(Cursor.getPredefinedCursor(12));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setRolloverEnabled(true);
        addActionListener(defaultAction);
        setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        setMargin(new java.awt.Insets(2, 2, 2, 2));
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Construct the UI.
     */
    public void updateUI() {
        setUI(MXHyperLinkUI.createUI(this));
    }

    /**
     * Get the UI class ID.
     *
     * @return the UI class ID
     */
    public String getUIClassID() {
        return "LinkButtonUI";
    }


    /**
     * Set the link behavior of the button.
     *
     * @param bnew the type of behavior
     */
    public void setLinkBehavior(int bnew) {
        checkLinkBehaviour(bnew);
        int old = linkBehavior;
        linkBehavior = bnew;
        firePropertyChange("linkBehavior", old, bnew);
        repaint();
    }


    /**
     * Get the link behavior.
     *
     * @return the type of behavior
     */
    public int getLinkBehavior() {
        return linkBehavior;
    }

    /**
     * Set the color of the link.
     *
     * @param color the color of the link
     */
    public void setLinkColor(Color color) {
        Color colorOld = linkColor;
        linkColor = color;
        firePropertyChange("linkColor", colorOld, color);
        repaint();
    }

    /**
     * Get the link color.
     *
     * @return the color of the link
     */
    public Color getLinkColor() {
        return linkColor;
    }

    /**
     * Set the active link color.
     *
     * @param colorNew the active link color
     */
    public void setActiveLinkColor(Color colorNew) {
        Color colorOld = colorPressed;
        colorPressed = colorNew;
        firePropertyChange("activeLinkColor", colorOld, colorNew);
        repaint();
    }

    /**
     * Get the active link color.
     *
     * @return the active link color
     */
    public Color getActiveLinkColor() {
        return colorPressed;
    }

    /**
     * Set the disabled link color.
     *
     * @param color the disabled link color
     */
    public void setDisabledLinkColor(Color color) {
        Color colorOld = disabledLinkColor;
        disabledLinkColor = color;
        firePropertyChange("disabledLinkColor", colorOld, color);
        if (!isEnabled()) {
            repaint();
        }
    }

    /**
     * Get the disabled link color.
     *
     * @return
     */
    public Color getDisabledLinkColor() {
        return disabledLinkColor;
    }

    /**
     * Set the visited link color.
     *
     * @param colorNew the visited link color
     */
    public void setVisitedLinkColor(Color colorNew) {
        Color colorOld = visitedLinkColor;
        visitedLinkColor = colorNew;
        firePropertyChange("visitedLinkColor", colorOld, colorNew);
        repaint();
    }

    /**
     * Get the visited link color.
     *
     * @return the visited link color
     */
    public Color getVisitedLinkColor() {
        return visitedLinkColor;
    }

    /**
     * Set the url of the button.
     *
     * @param url the url of the button
     */
    public void setLinkURL(URL url) {
        URL urlOld = buttonURL;
        buttonURL = url;
        setupToolTipText();
        firePropertyChange("linkURL", urlOld, url);
        revalidate();
        repaint();
    }

    /**
     * Get the url of the button.
     *
     * @return the url of the button
     */
    public URL getLinkURL() {
        return buttonURL;
    }

    /**
     * Set if the link has been visited or not.
     *
     * @param flagNew whether the link has been visited or not
     */
    public void setLinkVisited(boolean flagNew) {
        boolean flagOld = isLinkVisited;
        isLinkVisited = flagNew;
        firePropertyChange("linkVisited", flagOld, flagNew);
        repaint();
    }

    /**
     * Whether or not the link has been visited.
     *
     * @return whether or not the link has been visited
     */
    public boolean isLinkVisited() {
        return isLinkVisited;
    }

    /**
     * Set the default action to perform.
     *
     * @param actionNew the default action to perform
     */
    public void setDefaultAction(Action actionNew) {
        Action actionOld = defaultAction;
        defaultAction = actionNew;
        firePropertyChange("defaultAction", actionOld, actionNew);
    }

    /**
     * Get the default action to perform.
     *
     * @return the default action to perform
     */
    public Action getDefaultAction() {
        return defaultAction;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Set the text of the tooltip.
     */
    protected void setupToolTipText() {
        String tip = null;
        if (buttonURL != null) {
            tip = buttonURL.toExternalForm();
        }
        setToolTipText(tip);
    }

    /**
     * Return the parameters of the button as a string.
     *
     * @return a string representation of the buttons parameters
     */
    protected String paramString() {
        String str;
        if (linkBehavior == ALWAYS_UNDERLINE) {
            str = "ALWAYS_UNDERLINE";
        } else if (linkBehavior == HOVER_UNDERLINE) {
            str = "HOVER_UNDERLINE";
        } else if (linkBehavior == NEVER_UNDERLINE) {
            str = "NEVER_UNDERLINE";
        } else {
            str = "SYSTEM_DEFAULT";
        }
        String colorStr = linkColor == null ? "" : linkColor.toString();
        String colorPressStr = colorPressed == null ? "" : colorPressed
                .toString();
        String disabledLinkColorStr = disabledLinkColor == null ? ""
                : disabledLinkColor.toString();
        String visitedLinkColorStr = visitedLinkColor == null ? ""
                : visitedLinkColor.toString();
        String buttonURLStr = buttonURL == null ? "" : buttonURL.toString();
        String isLinkVisitedStr = isLinkVisited ? "true" : "false";

        return super.paramString() + ",linkBehavior=" + str + ",linkURL="
                + buttonURLStr + ",linkColor=" + colorStr + ",activeLinkColor="
                + colorPressStr + ",disabledLinkColor=" + disabledLinkColorStr
                + ",visitedLinkColor=" + visitedLinkColorStr
                + ",linkvisitedString=" + isLinkVisitedStr;
    }


    // -------------------------------------------------------- Private Methods

    /**
     * Return the type of behavior for the link.
     *
     * @param beha the type of link behavior
     */
    private void checkLinkBehaviour(int beha) {
        if (beha != ALWAYS_UNDERLINE && beha != HOVER_UNDERLINE
                && beha != NEVER_UNDERLINE && beha != SYSTEM_DEFAULT) {
            throw new IllegalArgumentException("Not a legal LinkBehavior");
        }
    }
}