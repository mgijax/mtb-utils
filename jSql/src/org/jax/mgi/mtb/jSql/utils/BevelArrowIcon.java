package org.jax.mgi.mtb.jSql.utils;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.UIManager;


/**
 * 
 */
public class BevelArrowIcon
    implements Icon {

    // -------------------------------------------------------------- Constants

    public static final int UP = 0; 
    public static final int DOWN = 1;
    private static final int DEFAULT_SIZE = 11;

    // ----------------------------------------------------- Instance Variables

    /**
     * The color of the first edge.
     */
    private Color edge1;

    /**
     * The color of the second edge.
     */
    private Color edge2;

    /**
     * The color of the fill.
     */
    private Color fill;

    /**
     * The size of the arrow..
     */
    private int size;

    /**
     * The direction the arrow is facing.
     */
    private int direction;

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new beveled arrow icon.
     * 
     * @param direction The direction the arrow should face
     * @param isRaisedView True if the icon should have a raised view
     * @param isPressedView True if the icon should have a pressed view
     */
    public BevelArrowIcon(int direction, boolean isRaisedView,
            boolean isPressedView) {
        if (isRaisedView) {
            if (isPressedView) {
                init(UIManager.getColor("controlLtHighlight"),
                        UIManager.getColor("controlDkShadow"),
                        UIManager.getColor("controlShadow"), DEFAULT_SIZE,
                        direction);
            } else {
                init(UIManager.getColor("controlHighlight"),
                        UIManager.getColor("controlShadow"),
                        UIManager.getColor("control"), DEFAULT_SIZE, direction);
            }
        } else {
            if (isPressedView) {
                init(UIManager.getColor("controlDkShadow"),
                        UIManager.getColor("controlLtHighlight"),
                        UIManager.getColor("controlShadow"), DEFAULT_SIZE,
                        direction);
            } else {
                init(UIManager.getColor("controlShadow"),
                        UIManager.getColor("controlHighlight"),
                        UIManager.getColor("control"), DEFAULT_SIZE, direction);
            }
        }
    }

    /**
     * Construct a new beveled arrow icon.
     * 
     * @param edge1 The Color of the first edge
     * @param edge2 The Color of the second edge
     * @param fill The Color to fill the arrow in
     * @param size The size of the arrow
     * @param direction The direction the arrow will face (up or down)
     */
    public BevelArrowIcon(Color edge1, Color edge2, Color fill,
            int size, int direction) {
        init(edge1, edge2, fill, size, direction);
    }

    // --------------------------------------------------------- Public Methods

    /**
     *  Paint the icon.
     *  
     *  @param c The component to draw on
     *  @param g The graphics contect
     *  @param x The x coordinate
     *  @param y The y coordinate
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        switch (direction) {
        case DOWN:
            drawDownArrow(g, x, y);
            break;

        case UP:
            drawUpArrow(g, x, y);
            break;
        }
    }

    /**
     * Get the width of the icon.
     * 
     * @return The width of the icon.
     */
    public int getIconWidth() {
        return size;
    }

    /**
     * Get the height of the icon.
     * 
     * @return The height of the icon.
     */
    public int getIconHeight() {
        return size;
    }

    // -------------------------------------------------------- Private Methods
    
    /**
     * Initialize the arrow attributes.
     * 
     * @param edge1 The Color of the first edge
     * @param edge2 The Color of the second edge
     * @param fill The Color to fill the arrow in
     * @param size The size of the arrow
     * @param direction The direction the arrow will face (up or down)
     */
    private void init(Color edge1, Color edge2, Color fill,
            int size, int direction) {
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.fill = fill;
        this.size = size;
        this.direction = direction;
    }

    /**
     * Draw an arrow pointed down.
     * 
     * @param g The graphics contect
     * @param xo The x coordinate
     * @param yo the y coordinate
     */
    private void drawDownArrow(Graphics g, int xo, int yo) {
        g.setColor(edge1);
        g.drawLine(xo, yo, xo + size - 1, yo);
        g.drawLine(xo, yo + 1, xo + size - 3, yo + 1);
        g.setColor(edge2);
        g.drawLine(xo + size - 2, yo + 1, xo + size - 1, yo + 1);
        int x = xo + 1;
        int y = yo + 2;
        int dx = size - 6;

        while (y + 1 < yo + size) {
            g.setColor(edge1);
            g.drawLine(x, y, x + 1, y);
            g.drawLine(x, y + 1, x + 1, y + 1);
            if (0 < dx) {
                g.setColor(fill);
                g.drawLine(x + 2, y, x + 1 + dx, y);
                g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
            }
            g.setColor(edge2);
            g.drawLine(x + dx + 2, y, x + dx + 3, y);
            g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
            x += 1;
            y += 2;
            dx -= 2;
        }
        g.setColor(edge1);
        g.drawLine(xo + (size / 2), yo + size - 1, xo + (size / 2),
                yo + size - 1);
    }

    /**
     * Draw an arrow pointed up.
     * 
     * @param g The graphics contect
     * @param xo The x coordinate
     * @param yo the y coordinate
     */
    private void drawUpArrow(Graphics g, int xo, int yo) {
        g.setColor(edge1);
        int x = xo + (size / 2);

        g.drawLine(x, yo, x, yo);
        x--;
        int y = yo + 1;
        int dx = 0;

        while (y + 3 < yo + size) {
            g.setColor(edge1);
            g.drawLine(x, y, x + 1, y);
            g.drawLine(x, y + 1, x + 1, y + 1);
            if (0 < dx) {
                g.setColor(fill);
                g.drawLine(x + 2, y, x + 1 + dx, y);
                g.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
            }
            g.setColor(edge2);
            g.drawLine(x + dx + 2, y, x + dx + 3, y);
            g.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
            x -= 1;
            y += 2;
            dx += 2;
        }
        g.setColor(edge1);
        g.drawLine(xo, yo + size - 3, xo + 1, yo + size - 3);
        g.setColor(edge2);
        g.drawLine(xo + 2, yo + size - 2, xo + size - 1, yo + size - 2);
        g.drawLine(xo, yo + size - 1, xo + size, yo + size - 1);
    }
}
