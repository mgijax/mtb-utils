/*
 * BasicButtonBarUI.java
 *
 * Created on October 27, 2005, 3:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf.basic;

import us.jawsoft.gui.JawButtonBar;
import us.jawsoft.gui.PercentLayout;
import us.jawsoft.gui.plaf.ButtonBarUI;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * BasicButtonBarUI. <br>
 *
 */
public class BasicButtonBarUI extends ButtonBarUI {
    
    protected JawButtonBar bar;
    protected PropertyChangeListener propertyListener;
    
    public static ComponentUI createUI(JComponent c) {
        return new BasicButtonBarUI();
    }
    
    public void installUI(JComponent c) {
        super.installUI(c);
        
        bar = (JawButtonBar)c;
        
        installDefaults();
        installListeners();
        
        updateLayout();
    }
    
    public void uninstallUI(JComponent c) {
        uninstallDefaults();
        uninstallListeners();
        super.uninstallUI(c);
    }
    
    protected void installDefaults() {
    }
    
    protected void uninstallDefaults() {
    }
    
    protected void installListeners() {
        propertyListener = createPropertyChangeListener();
        bar.addPropertyChangeListener(propertyListener);
    }
    
    protected void uninstallListeners() {
        bar.removePropertyChangeListener(propertyListener);
    }
    
    protected PropertyChangeListener createPropertyChangeListener() {
        return new ChangeListener();
    }
    
    protected void updateLayout() {
        if (bar.getOrientation() == JawButtonBar.HORIZONTAL) {
            bar.setLayout(new PercentLayout(PercentLayout.HORIZONTAL, 2));
        } else {
            bar.setLayout(new PercentLayout(PercentLayout.VERTICAL, 2));
        }
    }
    
    public Dimension getPreferredSize(JComponent c) {
        JawButtonBar b = (JawButtonBar)c;
        Dimension preferred = b.getLayout().preferredLayoutSize(c);
        if (b.getOrientation() == JawButtonBar.HORIZONTAL) {
            return new Dimension(preferred.width, 53);
        } else {
            return new Dimension(74, preferred.height);
        }
    }
    
    
    private class ChangeListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(JawButtonBar.ORIENTATION_CHANGED_KEY)) {
                updateLayout();
                bar.revalidate();
                bar.repaint();
            }
        }
    }
    
}
