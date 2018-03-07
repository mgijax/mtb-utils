package org.jax.mgi.mtb.jSql.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 */
public class BlankIcon
    implements Icon {
    private Color fillColor;
    private int size;

    public BlankIcon() {
        this(null, 11);
    }

    /**
     * @param color
     * @param size
     */    
    public BlankIcon(Color color, int size) {
        this.fillColor = color;
        this.size = size;
    }

    /**
     * @param c
     * @param g
     * @param x
     * @param y
     */    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (fillColor != null) {
            g.setColor(fillColor);
            g.drawRect(x, y, size - 1, size - 1);
        }
    }

    /**
     * @return
     */    
    public int getIconWidth() {
        return size;
    }

    /**
     * @return
     */    
    public int getIconHeight() {
        return size;
    }
}
