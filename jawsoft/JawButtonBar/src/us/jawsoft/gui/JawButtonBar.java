/*
 * JawButtonBar.java
 *
 * Created on October 27, 2005, 3:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui;

import us.jawsoft.gui.plaf.ButtonBarButtonUI;
import us.jawsoft.gui.plaf.ButtonBarUI;
import us.jawsoft.gui.plaf.JawButtonBarAddon;
import us.jawsoft.gui.plaf.LookAndFeelAddons;

import java.awt.Component;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 * JButtonBar helps organizing buttons together (as seen in Mozilla Firefox
 * or IntelliJ).<br>
 *
 * @javabean.class
 *          name="JButtonBar"
 *          shortDescription="JButtonBar helps organizing buttons together (as seen in Mozilla Firefox or IntelliJ)."
 *          stopClass="java.awt.Component"
 *
 * @javabean.icons
 *          mono16="JButtonBar16-mono.gif"
 *          color16="JButtonBar16.gif"
 *          mono32="JButtonBar32-mono.gif"
 *          color32="JButtonBar32.gif"
 */
public class JawButtonBar extends JComponent implements SwingConstants {
    
    public static final String UI_CLASS_ID = "ButtonBarUI";
    
    // ensure at least the default ui is registered
    static {
        LookAndFeelAddons.contribute(new JawButtonBarAddon());
    }
    
    public static final String ORIENTATION_CHANGED_KEY = "orientation";
    
    private int orientation;
    
    public JawButtonBar() {
        this(HORIZONTAL);
    }
    
    public JawButtonBar(int orientation) {
        this.orientation = orientation;
        updateUI();
        addContainerListener(buttonTracker);
    }
    
    /**
     * Notification from the <code>UIManager</code> that the L&F has changed.
     * Replaces the current UI object with the latest version from the <code>UIManager</code>.
     *
     * @see javax.swing.JComponent#updateUI
     */
    public void updateUI() {
        setUI((ButtonBarUI)LookAndFeelAddons.getUI(this, ButtonBarUI.class));
    }
    
    /**
     * Returns the L&F object that renders this component.
     *
     * @return the ButtonBarUI object
     * @see #setUI
     */
    public ButtonBarUI getUI() {
        return (ButtonBarUI)ui;
    }
    
    /**
     * Sets the L&F object that renders this component.
     *
     * @param ui the <code>ButtonBarUI</code> L&F object
     * @see javax.swing.UIDefaults#getUI
     *
     * @beaninfo bound: true hidden: true description: The UI object that
     * implements the buttonbar's LookAndFeel.
     */
    public void setUI(ButtonBarUI ui) {
        super.setUI(ui);
        Component[] components = getComponents();
        // whenever our UI is changed, make sure UI of our buttons is updated.
        for (int i = 0, c = components.length; i < c; i++) {
            if (components[i] instanceof AbstractButton) {
                ui.installButtonBarUI((AbstractButton)components[i]);
            }
        }
    }
    
    /**
     * Returns the name of the L&F class that renders this component.
     *
     * @return the string {@link #UI_CLASS_ID}
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     */
    public String getUIClassID() {
        return UI_CLASS_ID;
    }
    
    /**
     * Sets the orientation of the bar. The orientation must have either the
     * value <code>HORIZONTAL</code> or <code>VERTICAL</code>. If <code>orientation</code>
     * is an invalid value, an exception will be thrown.
     *
     * @param orientation the new orientation -- either <code>HORIZONTAL</code>
     *          or</code> VERTICAL</code>
     * @exception IllegalArgumentException if orientation is neither <code>
     *              HORIZONTAL</code> nor <code>VERTICAL</code>
     * @see #getOrientation @beaninfo description: The current orientation of the
     *      bar bound: true preferred: true
     */
    public void setOrientation(int orientation) {
        if (!(orientation == HORIZONTAL || orientation == VERTICAL)) {
            throw new IllegalArgumentException(
                    "orientation must be one of: " + "VERTICAL, HORIZONTAL");
        }
        
        if (this.orientation != orientation) {
            int oldOrientation = this.orientation;
            this.orientation = orientation;
            firePropertyChange(
                    ORIENTATION_CHANGED_KEY,
                    oldOrientation,
                    this.orientation);
        }
    }
    
    public int getOrientation() {
        return orientation;
    }
    
    /**
     * This listener ensures the UI of buttons added to the JButtonBar gets reset
     * everytime it is changed to a pluggable UI which does not implement
     * ButtonBarUI.
     */
    private static PropertyChangeListener uiUpdater =
            new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof AbstractButton) {
                AbstractButton button = (AbstractButton)evt.getSource();
                if (button.getParent() instanceof JawButtonBar
                        && !(button.getUI() instanceof ButtonBarButtonUI)) {
                    (
                            (ButtonBarUI)
                            ((JawButtonBar)button.getParent()).ui).installButtonBarUI(
                            button);
                }
            }
        }
    };
    
    /**
     * This listener tracks buttons added to a JButtonBar. When a button is
     * added, it updates its UI and ensures further ui changes will be tracked
     * too. When the button is removed, it is no longer tracked.
     */
    private static ContainerListener buttonTracker = new ContainerAdapter() {
        public void componentAdded(ContainerEvent e) {
            JawButtonBar container = (JawButtonBar)e.getContainer();
            if (e.getChild() instanceof AbstractButton) {
                ((ButtonBarUI)container.ui).installButtonBarUI(
                        (AbstractButton)e.getChild());
                ((AbstractButton)e.getChild()).addPropertyChangeListener(
                        "UI",
                        JawButtonBar.uiUpdater);
            }
        }
        public void componentRemoved(ContainerEvent e) {
            if (e.getChild() instanceof AbstractButton) {
                ((AbstractButton)e.getChild()).removePropertyChangeListener("UI",
                        JawButtonBar.uiUpdater);
            }
        }
    };
    
}