package org.jax.mgi.mtb.gui.table;

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
public class MXTableHeader extends JTableHeader {

    //--------------------------------------------------------------- Constants
    // none

    //------------------------------------------------------ Instance Variables

    /**
     * The sorted column index.
     */
    protected int sortedColumnIndex = -1;

    /**
     * Specifies whether the sort order is ascending or not.
     */
    protected boolean sortedColumnAscending = true;

    private Icon ascendingIcon = null;
    private Icon decendingIcon = null;

    //------------------------------------------------------------ Constructors

    /**
     * Default constructor.
     */
    public MXTableHeader() {
        super();
        initRenderer();
    }

    //---------------------------------------------------------- Public Methods

    public void initRenderer() {
        this.setDefaultRenderer(new MXHeaderRenderer(ascendingIcon, decendingIcon));
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
    // none

    //--------------------------------------------------------- Private Methods
    // none
}

