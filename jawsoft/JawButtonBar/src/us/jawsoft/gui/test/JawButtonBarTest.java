/*
 * JawButtonBarTest.java
 *
 * Created on October 27, 2005, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.test;

/**
 *
 * @author mjv
 */

import us.jawsoft.gui.JawButtonBar;
import us.jawsoft.gui.plaf.blue.BlueishButtonBarUI;
import us.jawsoft.gui.plaf.misc.IconPackagerButtonBarUI;
import us.jawsoft.gui.util.ResourceManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * Demo of the JawButtonBar. <br>
 *
 */
public class JawButtonBarTest extends JPanel {
    
    static ResourceManager RESOURCE = ResourceManager.get(JawButtonBarTest.class);
    
    public JawButtonBarTest() {
        setLayout(new BorderLayout());
        
        //JTabbedPane tabs = new JTabbedPane();
        //add("Center", tabs);
        
        //{ // with the mozilla L&F
            JawButtonBar toolbar = new JawButtonBar(JawButtonBar.VERTICAL);
            toolbar.setUI(new BlueishButtonBarUI(true));
            add("Center", new ButtonBarPanel(toolbar));
        //    tabs.addTab("Mozilla L&F", new ButtonBarPanel(toolbar));
        //}
        /*
        { // with the icon packager L&F
            JawButtonBar toolbar = new JawButtonBar(JawButtonBar.VERTICAL);
            toolbar.setUI(new IconPackagerButtonBarUI());
            tabs.addTab("Icon Packager L&F", new ButtonBarPanel(toolbar));
        }
         */
        
    }
    
    static class ButtonBarPanel extends JPanel {
        
        private Component currentComponent;
        
        public ButtonBarPanel(JawButtonBar toolbar) {
            setLayout(new BorderLayout());
            
            add("West", toolbar);
            
            ButtonGroup group = new ButtonGroup();
            
            addButton(RESOURCE.getString("Main.welcome"), "icons/welcome32x32.png", makePanel(RESOURCE.getString("Main.welcome")), toolbar, group);
            addButton(RESOURCE.getString("Main.settings"), "icons/propertysheet32x32.png", makePanel(RESOURCE.getString("Main.settings")), toolbar, group);
            addButton(RESOURCE.getString("Main.sounds"), "icons/fonts32x32.png", makePanel(RESOURCE.getString("Main.sounds")), toolbar, group);
            addButton(RESOURCE.getString("Main.stats"), "icons/folder32x32.png", makePanel(RESOURCE.getString("Main.stats")), toolbar, group);
        }
        
        private JPanel makePanel(String title) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel top = new JLabel(title);
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(panel.getBackground().brighter());
            panel.add("North", top);
            panel.setPreferredSize(new Dimension(400, 300));
            panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            return panel;
        }
        
        private void show(Component component) {
            if (currentComponent != null) {
                remove(currentComponent);
            }
            add("Center", currentComponent = component);
            revalidate();
            repaint();
        }
        
        private void addButton(String title, String iconUrl,
                final Component component, JawButtonBar bar, ButtonGroup group) {
            Action action = new AbstractAction(title, new ImageIcon(
                    JawButtonBarTest.class.getResource(iconUrl))) {
                public void actionPerformed(ActionEvent e) {
                    show(component);
                    System.out.println(e.toString());
                }
            };
            
            JToggleButton button = new JToggleButton(action);
            
            bar.add(button);
            
            group.add(button);
            
            if (group.getSelection() == null) {
                button.setSelected(false);
                show(component);
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("ButtonBar");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", new JawButtonBarTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }
    
}