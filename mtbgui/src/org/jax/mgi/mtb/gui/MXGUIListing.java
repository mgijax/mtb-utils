/*
 * MXListing.java
 *
 * Created on November 7, 2006, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

public class MXGUIListing {
    public static void main(String[] args) throws Exception {
        // Get the native look and feel class name
        String nativeLF = UIManager.getSystemLookAndFeelClassName();

        // Install the look and feel
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame f = new JFrame("Listing");
        JPanel view = new JPanel(new GridBagLayout());
        Container controls = new JToolBar();
        controls.add(Box.createGlue());
        controls.add(getTypesCombo(view));
        controls.add(Box.createGlue());
        Container cp = f.getContentPane();
        cp.add(new JScrollPane(view));
        cp.add(controls, BorderLayout.NORTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    static class ComponentMaker {
        public JComponent make(Object key, Object value) {
            return new JLabel(value == null ? "(null)" : value.toString());
        }
    }

    static ComponentMaker[] makers = {
        new ComponentMaker(),   //Boolean
        new ComponentMaker(){   //Border
            public JComponent make(Object key, Object value) {
                JLabel lbl = new JLabel("        ");
                lbl.setBorder(new BorderAdapter((Border)value, key.toString()));
                return lbl;
            }
        },
        new ComponentMaker(){   //Color
            public JComponent make(Object key, Object value) {
                return new JLabel(new ColorIcon((Color)value));
            }
        },
        new ComponentMaker(){   //Font
            public JComponent make(Object key, Object value) {
                Font font = (Font)value;
                JLabel lbl = new JLabel(key+": "+font.getFontName()+", size="+font.getSize2D());
                lbl.setFont(font);
                return lbl;
            }
        },
        new ComponentMaker(){   //Icon
            public JComponent make(Object key, Object value) {
                return new JLabel(new IconAdapter((Icon)value, key.toString()));
            }
        },
        new ComponentMaker(),   //Number
        new ComponentMaker(),   //String
        new ComponentMaker()    //The Rest
    };

    static JComboBox getTypesCombo(final JPanel view) {
        JComboBox cboTypes = new JComboBox(
                new String[]{"Boolean","Border","Color","Font","Icon", "Number", "String", "The Rest"});
        cboTypes.addActionListener(new ActionListener(){
            Class[] types = {Boolean.class, Border.class, Color.class, Font.class, Icon.class, Number.class, String.class};

            public void actionPerformed(ActionEvent evt) {
                int idx = ((JComboBox) evt.getSource()).getSelectedIndex();
                if (idx != -1) {
                    view.removeAll();
                    List keys = idx<types.length ? findKeys(types[idx]) : findRestKeys(types);
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                    gbc.gridy = 0;
                    for(int i=0, ub=keys.size(); i<ub; ++i) {
                        if (i ==ub-1) {
                            gbc.weighty = 1;
                        }
                        Object key = keys.get(i);
                        gbc.weightx = 0;
                        view.add(new JLabel(key.toString()), gbc);
                        gbc.weightx = 1;
                        view.add(makers[idx].make(key, UIManager.get(key)), gbc);
                        gbc.gridy++;
                    }
                    view.revalidate();
                    Component parent = view.getParent();
                    if (parent instanceof JViewport) {
                        ((JViewport) parent).setViewPosition(new Point(0,0));
                    }
                }
            }
        });
        cboTypes.setSelectedIndex(2);
        return cboTypes;
    }

    static List findKeys(Class cls) {
        List keys = new ArrayList();
        UIDefaults defs = UIManager.getLookAndFeel().getDefaults();
        for(Iterator i=defs.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            if (cls.isInstance(UIManager.get(key))) {
                keys.add(key);
            }
        }
        Collections.sort(keys);
        return keys;
    }

    static List findRestKeys(Class[] cls) {
        List keys = new ArrayList();
        UIDefaults defs = UIManager.getLookAndFeel().getDefaults();
        for(Iterator i=defs.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            Object value = UIManager.get(key);
            int j;
            for(j=0; j<cls.length; ++j) {
                if (cls[j].isInstance(value)) {
                    break;
                }
            }
            if (j==cls.length) {
                keys.add(key);
            }
        }
        //Collections.sort(keys);
        return keys;
    }
}


class ColorIcon implements Icon {
    private Color color;

    public ColorIcon(Color color) {
        this.color = color;
    }

    public int getIconWidth() {
        return 20;
    }

    public int getIconHeight() {
        return 16;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (color != null) {
            g.setColor(color);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            g.setColor(Color.BLACK);
            g.drawRect(x, y, getIconWidth()-1, getIconHeight()-1);
        }
    }
}

class IconAdapter implements Icon {
    private Icon icon;
    private Component component;

    public IconAdapter(Icon icon, String key) {
        this.icon = icon;
        try {
            int dotIndex = key.lastIndexOf('.');
            if (dotIndex != -1) {
                String classname = "javax.swing.J" + key.substring(0, dotIndex);
                component = (Component) Class.forName(classname).newInstance();
            }
        } catch (Exception e) {
        }
    }

    public int getIconWidth() {
        return icon.getIconWidth();
    }

    public int getIconHeight() {
        return icon.getIconHeight();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        try {
            if (component != null) {
                c = component;
            }
            icon.paintIcon(c, g, x, y);
        } catch(Exception e) {
        }
    }
}

class BorderAdapter implements Border {
    private Border border;
    private Component component;

    public BorderAdapter(Border border, String key) {
        this.border = border;
        int dotIndex = key.lastIndexOf('.');
        if (dotIndex != -1) {
            String classname = "javax.swing.J" + key.substring(0, dotIndex);
            try {
                component = (Component) Class.forName(classname).newInstance();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isBorderOpaque() {
        return border.isBorderOpaque();
    }

    public Insets getBorderInsets(Component c) {
        if (component != null) {
            c = component;
        }
        return border.getBorderInsets(c);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (component != null) {
            c = component;
        }
        border.paintBorder(c, g, x, y, width, height);
    }
}
