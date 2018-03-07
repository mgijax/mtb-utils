/*
 * BlueishButtonUI.java
 *
 * Created on October 27, 2005, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf.blue;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * BlueishButtonUI. <br>
 *
 */
public class BlueishButtonUI
        extends BasicButtonUI {
    
    private boolean doToggle = true;
    
    private static Color blueishBackgroundOver = new Color(224, 232, 246);
    private static Color blueishBorderOver = new Color(152, 180, 226);
    
    private static Color blueishBackgroundSelected = new Color(193, 210, 238);
    private static Color blueishBorderSelected = new Color(49, 106, 197);
    
    public BlueishButtonUI() {
        super();
    }
    
    public BlueishButtonUI(boolean toggle) {
        super();
        this.doToggle = toggle;
    }
    
    public void installUI(JComponent c) {
        super.installUI(c);
        
        AbstractButton button = (AbstractButton)c;
        button.setRolloverEnabled(true);
        button.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }
    
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton)c;
        
        if (doToggle) {
            if (button.getModel().isRollover() || button.getModel().isArmed() || button.getModel().isSelected()) {
                Color oldColor = g.getColor();

                // paint bg
                if (button.getModel().isSelected()) {
                    g.setColor(Color.WHITE);
                    g.setColor(blueishBackgroundOver);
                    g.setColor(blueishBackgroundSelected);
                } else {
                    g.setColor(blueishBackgroundOver);
                }
                
                g.fillRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

                // paint border
                if (button.getModel().isSelected()) {
                    g.setColor(blueishBorderSelected);
                } else {
                    g.setColor(blueishBorderOver);
                }
                g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

                g.setColor(oldColor);
            }
        } else {
            if (button.getModel().isRollover() || button.getModel().isArmed()) {
                Color oldColor = g.getColor();
                g.setColor(blueishBackgroundOver);
                g.fillRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

                // paint border
                if (button.getModel().isSelected()) {
                    g.setColor(blueishBorderSelected);
                } else {
                    g.setColor(blueishBorderOver);
                }
                g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

                g.setColor(oldColor);
            }
        }
        
        super.paint(g, c);
    }
    
}
