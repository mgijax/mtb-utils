package us.jawsoft.gui.FXTable;

import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import us.jawsoft.gui.JawFXTable;

/**
 * Table header renderer for </code>JawFXTable</code> showing the direction of
 * the sort order.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class FXHeaderRenderer
    extends DefaultTableCellRenderer {

    //--------------------------------------------------------------- Constants

    /**
     * Specifies if debugging information should be displayed.
     */
    private final boolean DEBUG_MODE = false;

    //------------------------------------------------------ Instance Variables
    
    /**
     * Unsorted icon.
     */
    public static Icon NONSORTED = new FXArrowIcon(FXArrowIcon.NONE);

    /**
     * Ascending icon.
     */
    public static Icon ASCENDING = new FXArrowIcon(FXArrowIcon.ASCENDING);
    //public static Icon ASCENDING = new ImageIcon(loadIcon("us/jawsoft/gui/FXTable/up.png").getImage());

    /**
     * Decending icon.
     */
    public static Icon DECENDING = new FXArrowIcon(FXArrowIcon.DECENDING);
    //public static Icon DECENDING = new ImageIcon(loadIcon("us/jawsoft/gui/FXTable/down.png").getImage());
    
    private Font font = null;


    //------------------------------------------------------------ Constructors
    
    /**
     * Default constructor.
     */
    public FXHeaderRenderer() {
        setHorizontalTextPosition(LEFT);
        setHorizontalAlignment(CENTER);
    }

    public FXHeaderRenderer(Icon ascending, Icon decending) {
        setHorizontalTextPosition(LEFT);
        setHorizontalAlignment(CENTER);
        
        
    }
    public FXHeaderRenderer(Font font, Icon ascending, Icon decending) {
        setHorizontalTextPosition(LEFT);
        setHorizontalAlignment(CENTER);
        
        this.font = font;
        
        if (ascending != null) {
            this.ASCENDING = ascending;
        }
        
        if (decending != null) {
            this.DECENDING = decending;
        }
    }

    //---------------------------------------------------------- Public Methods
    
    
    public void setAscendingIcon(Icon icon) {
        if (icon != null) {
            this.ASCENDING = icon;
        }
        
    }
    
    public void setDecendingIcon(Icon icon) {
        if (icon != null) {
            this.DECENDING = icon;
        }
    }

    /**
     * Get the table cell renderer for the specified cell in the 
     * <code>JTable</code>.
     *
     * @param table The <code>JTable</code>
     * @param value The value to assign to the cell at [row, column]
     * @param isSelected <code>true</code> if the cell is selected, 
     * <code>false</code> otherwise
     * @param hasFocus <code>true</code> if the cell has focus, 
     * <code>false</code> otherwise
     * @param row The row of the cell to render.
     * @param col The column of the cell to render.
     *
     * @return The table cell renderer.
     */
    public Component getTableCellRendererComponent(JTable table, 
                                                   Object value, 
                                                   boolean isSelected,
                                                   boolean hasFocus, 
                                                   int row, 
                                                   int col) {

        int index = -1;
        boolean ascending = true;
        
        if (table instanceof JawFXTable) {
            JawFXTable sortTable = (JawFXTable)table;
            index = sortTable.getSortedColumnIndex();
            ascending = sortTable.getSortedColumnAscending();
        }
        
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                setForeground(header.getForeground());
                setBackground(header.getBackground());
                
                //if (font == null) {
                    //debug("FONT==null");
                    setFont(header.getFont());
                //} else {
                    //debug("FONT=" + font.toString());
                  //  setFont(font);
                //}
            }
        }
        
        Icon icon = ascending ? ASCENDING : DECENDING;
        setIcon(col == index ? icon : NONSORTED);
        setText((value == null) ? "" : value.toString());
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        
        return this;
    }
    
    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods
    
    /**     
     * Loads an <code>ImageIcon</code> object from this archive.    
     *<pre>     * Note: This method is compatible under Java Web Start.  
     *</pre>     * @param pathnamethe name of the icon file   
     * @return the icon if loaded, <code>null</code> otherwise   
     */    
    private static ImageIcon loadIcon(String pathname) {     
         URL url = JawFXTable.class.getClassLoader().getResource(pathname);        
         if (url != null)          
             return new ImageIcon(url);		
         System.err.println("Util.loadIcon Error: Could not find icon '" +    
             pathname + "'");        
         return null;  
     }

    /**
     * Output debug information.
     */
    private void debug(String text) {
        if (DEBUG_MODE) {
            System.out.println(text);
        }
    }
}

