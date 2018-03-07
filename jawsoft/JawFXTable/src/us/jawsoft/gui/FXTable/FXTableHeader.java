package us.jawsoft.gui.FXTable;

import java.awt.Font;
import javax.swing.Icon;
import javax.swing.table.JTableHeader;

/**
 * The <code>ColumnComparator</code> class implements the 
 * <code>Comparator</code> interface.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class FXTableHeader extends JTableHeader {
        
    //--------------------------------------------------------------- Constants
    
    /**
     * Specifies if debugging information should be displayed.
     */
    private final boolean DEBUG_MODE = false;

    //------------------------------------------------------ Instance Variables
    
    /**
     * The sorted column index.
     */ 
    protected int sortedColumnIndex = -1;

    /**
     * Specifies whether the sort order is ascending or not.
     */ 
    protected boolean sortedColumnAscending = true;

    private Font headerFont = null;
    private Icon ascendingIcon = null;
    private Icon decendingIcon = null;
    
    //------------------------------------------------------------ Constructors
    
    /**
     * Default constructor.
     */
    public FXTableHeader() {
        super();
        initRenderer();
    }
    

    //---------------------------------------------------------- Public Methods
    public void initRenderer() {
        this.setDefaultRenderer(new FXHeaderRenderer(ascendingIcon, decendingIcon));
    }
    
    public void setAscendingIcon(Icon icon) {
        this.ascendingIcon = icon;
        initRenderer();
    }
    
    public void setDecendingIcon(Icon icon) {
        this.decendingIcon = icon;
        initRenderer();
    }
    
    
    //------------------------------------------------------- Protected Methods


    //--------------------------------------------------------- Private Methods

    /**
     * Output debug information.
     */
    private void debug(String text) {
        if (DEBUG_MODE) {
            System.out.println(text);
        }
    }
}

