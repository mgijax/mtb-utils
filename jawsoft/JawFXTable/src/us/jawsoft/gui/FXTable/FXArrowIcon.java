package us.jawsoft.gui.FXTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * A <code>Icon</code> that is just an arrow indicating the direction of the
 * sort order.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class FXArrowIcon
    implements Icon {
        
    //--------------------------------------------------------------- Constants
    
    /**
     * No sort order specified.
     */
    public static final int NONE = 0;

    /**
     * Sort in decending order.
     */
    public static final int DECENDING = 1;

    /**
     * Sort in ascending order.
     */
    public static final int ASCENDING = 2;

    //------------------------------------------------------ Instance Variables

    /**
     * The sort order direction.
     */
    protected int direction;
    
    /**
     * The width of the icon.
     */
    protected int width = 8;

    /**
     * The height of the icon.
     */
    protected int height = 8;
    
    //------------------------------------------------------------ Constructors

    /**
     * Constructor specifying the sort order.
     *
     * @param direction One of three possible values: <code>NONE</code>,
     * <code>DECENDING</code>, or <code>ASCENDING</code>
     */
    public FXArrowIcon(int direction) {
        this.direction = direction;
    }
    
    //---------------------------------------------------------- Public Methods
    
    /**
     * Get the width of the icon.
     *
     * @return The width in pixels.
     */
    public int getIconWidth() {
        return this.width;
    }
    
    /**
     * Get the height of the icon.
     *
     * @return The height in pixels.
     */
    public int getIconHeight() {
        return this.height;
    }
    
    /**
     * Paint the icon.
     *
     * @param c <code>Component</code> to paint on top of.
     * @param g <code>Graphics</code> to paint on.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color bg = c.getBackground();
        Color light = bg.brighter();
        Color shade = bg.darker();
        
        int w = width;
        int h = height;
        int m = w / 2;
        if (direction == ASCENDING) {
            g.setColor(shade);
            g.drawLine(x, y, x + w, y);
            g.drawLine(x, y, x + m, y + h);
            g.setColor(light);
            g.drawLine(x + w, y, x + m, y + h);
        } else if (direction == DECENDING) {
            g.setColor(shade);
            g.drawLine(x + m, y, x, y + h);
            g.setColor(light);
            g.drawLine(x, y + h, x + w, y + h);
            g.drawLine(x + m, y, x + w, y + h);
        } else {
            // do nothing
        }
    }

    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods
    // none
}

