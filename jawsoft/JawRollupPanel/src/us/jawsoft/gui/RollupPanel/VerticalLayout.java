package us.jawsoft.gui.RollupPanel;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Insets;

public class VerticalLayout implements LayoutManager {
            private int vgap;
            public VerticalLayout(int vgap) {
                this.vgap = vgap;
            }

            public void addLayoutComponent(String name, Component comp) {}

            public void removeLayoutComponent(Component comp) {}

            public Dimension preferredLayoutSize(Container parent) {
                Dimension available = parent.getSize();
                
                synchronized (parent.getTreeLock()) {
                    Insets insets = parent.getInsets();
                    int ncomponents = parent.getComponentCount();
                    int w = 0;
                    int h = 0;

                    for (int i = 0; i < ncomponents; i++) {
                        Component comp = parent.getComponent(i);
                        Dimension d = comp.getPreferredSize();

                        if (w < d.width) {
                            w = d.width;
                        }
                        h += d.height;
                    }
                    return new Dimension(Math.max(w, available.width), //insets.left + insets.right + w,
                            insets.top + insets.bottom + h
                            + (ncomponents - 1) * vgap);
                }
            }

            public Dimension minimumLayoutSize(Container parent) {
                return preferredLayoutSize(parent);
            }

            public void layoutContainer(Container parent) {
                synchronized (parent.getTreeLock()) {
                    Insets insets = parent.getInsets();
                    int ncomponents = parent.getComponentCount();
                    int w = parent.getWidth() - (insets.left + insets.right);
                    int x = insets.left;
                    int y = insets.top;

                    for (int i = 0; i < ncomponents; i++) {
                        Component comp = parent.getComponent(i);

                        comp.setBounds(x, y, w, comp.getPreferredSize().height);
                        y += (comp.getPreferredSize().height + vgap);
                    }
                }
            }
        }
