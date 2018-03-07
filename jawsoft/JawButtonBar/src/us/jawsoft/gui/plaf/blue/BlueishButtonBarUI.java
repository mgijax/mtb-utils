/*
 * BlueisButtonBarUI.java
 *
 * Created on October 27, 2005, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf.blue;

import us.jawsoft.gui.plaf.ButtonBarButtonUI;
import us.jawsoft.gui.plaf.basic.BasicButtonBarUI;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

/**
 * BlueishButtonBarUI. <br>
 *
 */
public class BlueishButtonBarUI extends BasicButtonBarUI {
    private static boolean doToggle = true;
    
    public BlueishButtonBarUI() {
        super();
    }
    
    public BlueishButtonBarUI(boolean toggle) {
        super();
        this.doToggle = toggle;
    }
    
    public static ComponentUI createUI(JComponent c) {
        return new BlueishButtonBarUI(doToggle);
    }
    
    protected void installDefaults() {
        Border b = bar.getBorder();
        if (b == null || b instanceof UIResource) {
            bar.setBorder(
                    new BorderUIResource(
                    new CompoundBorder(
                    BorderFactory.createLineBorder(
                    UIManager.getColor("controlDkShadow")),
                    BorderFactory.createEmptyBorder(1, 1, 1, 1))));
        }
        
        Color color = bar.getBackground();
        if (color == null || color instanceof ColorUIResource) {
            bar.setOpaque(true);
            bar.setBackground(new ColorUIResource(Color.white));
        }
    }
    
    public void installButtonBarUI(AbstractButton button) {
        button.setUI(new BlueishButtonBarButtonUI(doToggle));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setOpaque(false);
    }
    
    static class BlueishButtonBarButtonUI
            extends BlueishButtonUI implements ButtonBarButtonUI {
        
        BlueishButtonBarButtonUI(boolean toggle) {
            super(toggle);
        }
    }
    
}
