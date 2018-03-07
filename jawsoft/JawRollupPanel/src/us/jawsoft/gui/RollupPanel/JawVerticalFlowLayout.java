package us.jawsoft.gui.RollupPanel;

import java.awt.LayoutManager;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Insets;

/**
 * A vertical flow layout.
 */
public class JawVerticalFlowLayout implements LayoutManager {

    // -------------------------------------------------------------- Constants
    
    private final boolean DEBUG_MODE = false;

    // ----------------------------------------------------- Instance Variables
    
    private int gap;
    private boolean fillWidth = false;
    private boolean fillHeight = false;
    private boolean makeSameSize = false;

    // ----------------------------------------------------------- Constructors

    public JawVerticalFlowLayout() {
        this(2, false, false, false);
    }

    /**
     *
     * @param gap
     */    
    public JawVerticalFlowLayout(int gap) {
        this(gap, false, false, false);
    }
    
    /**
     *
     * @param fill
     */    
    public JawVerticalFlowLayout(boolean fillWidth, boolean fillHeight) {
        this(2, fillWidth, fillHeight, false);
    }

    
    /**
     *
     * @param gap
     * @param fill
     * @param sameSize
     */    
    public JawVerticalFlowLayout(int gap, boolean fillWidth, boolean fillHeight, boolean sameSize) {
        this.gap = gap;
        this.fillWidth = fillWidth;
        this.fillHeight = fillHeight;
        this.makeSameSize = sameSize;
    }

    // --------------------------------------------------------- Public Methods

/*
    public void setMakeSameSize(boolean m) {
        this.makeSameSize = m;
    }
    
    public boolean getMakeSameSize() {
        return this.makeSameSize;
    }
*/    
    public void setFillWidth(boolean f) {
        this.fillWidth = f;
    }
    
    public boolean getFillWidth() {
        return this.fillWidth;
    }
    
    public void setFillHeight(boolean f) {
        this.fillHeight = f;
    }
    
    public boolean getFillHeight() {
        return this.fillHeight;
    }

    public void setVerticalGap(int gap) {
        this.gap = gap;
    }

    public int getVerticalGap() {
        return gap;
    }

    
    
    
    
    // implement LayoutManager
    /**
     *
     * @param name
     * @param comp
     */    
    public void addLayoutComponent(String name, Component comp) {}

    /**
     *
     * @param comp
     */    
    public void removeLayoutComponent(Component comp) {}
    
    /**
     *
     * @param parent
     * @return
     */    
    public Dimension preferredLayoutSize(Container parent) {
        debug("preferredLayoutSize() called");
        return getLayoutSize(parent);
    }

    /**
     *
     * @param parent
     * @return
     */    
    public Dimension minimumLayoutSize(Container parent) {
        debug("minimumLayoutSize() called");
        return getLayoutSize(parent);
    }
    
    /**
     *
     * NOTE: BUG!!!!!
     * scenarios are:
     *
     *  1. fillWidth = false and fillHeight = false
     *  2. fillWidth = true and fillHeight = false
     *  3. fillWidth = false and fillHeight = true
     *  4. fillWidth = true and fillHeight = true
     *
     * Scenarios 1, 2, and 4 work fince.
     *
     * Scenario 3 adjusts the height so the components are actually "taller"
     * than the parent component.
     *
     * @param parent
     */    
    public void layoutContainer(Container parent) {
        Dimension available = parent.getSize();
        Dimension required = preferredLayoutSize(parent);
        Insets    insets = parent.getInsets();
        final int x = insets.left;
        int       y = insets.top;
        final int xsHeight = available.height - required.height;
        int count = parent.getComponentCount();
        
        if (count <= 0) {
            return;
        }
        
        debug("AVAILABLE=" + available.toString());
        debug("REQUIRED=" + required.toString());
        
        int totalWidth = 0;
        int totalHeight = 0;
        
        if (this.fillWidth) {
            totalWidth = Math.max(available.width, required.width);
            totalWidth -= insets.left;
            totalWidth -= insets.right;
        } else {
            totalWidth = Math.min(available.width, required.width);
        }
        
        if (this.fillHeight) {
            totalHeight = Math.max(available.height, required.height);
        } else {
            totalHeight = Math.min(available.height, required.height);
        }
        
        debug("totalHeight = " + totalHeight);
        debug("totalWidth = " + totalWidth);
        
        int addH = 0;
        int modH = 0;
            
        if (xsHeight > 0) {
            addH = xsHeight / count;
            modH = xsHeight % count;
        }
        
        debug("addH = " + addH);
        debug("modH = " + modH);
        
        
        for (int i = 0; i < count; i++) {
            Component c = parent.getComponent(i);

            if (c.isVisible()) {
                int h = c.getPreferredSize().height;
                
                if (this.fillHeight) {
                    h += addH;
    
                    if (i == (count - 1)) {
                        h += modH;
                    }
                }
                
                int w = 0;
                
                if (this.fillWidth) {
                    w = totalWidth;
                } else {
                    w = c.getPreferredSize().width;
                }

                debug("lLacing component at: (x, y, w, h) : (" + x + ", " + y + ", " + w + ", " + h + ")");
                
                c.setBounds(x, y, w, h);
                
                y += (h + getVerticalGap());
                
                debug("y = " + y);
            }
        }
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods

    private Dimension getLayoutSize(Container parent) {
        int count = parent.getComponentCount();
        Dimension size = new Dimension(0, 0);
        Dimension componentSize = new Dimension(0, 0);

        debug("----------------------- START: getLayoutSize() -----------------------");

        for (int i = 0; i < count; i++) {
            Component c = (Component) parent.getComponent(i);
            
            if (this.fillWidth) {
                componentSize = c.getPreferredSize();
            } else {
                componentSize = c.getMinimumSize();
            }

            size.width = Math.max(componentSize.width, size.width);
            size.height += componentSize.height;

            if ((i > 0) && (i < count)) {
                size.height += getVerticalGap();
            }
        }
        
        Insets border = parent.getInsets();

        size.width += border.left + border.right;
        size.height += border.top + border.bottom;
       

        debug("returning: " + size.toString());                  
        debug("----------------------- END: getLayoutSize() -----------------------");
        return size;
    }
    
    private void debug(String text) {
        if (DEBUG_MODE) {
            System.out.println(text);
        }
    }
}
