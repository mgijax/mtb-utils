package org.jax.mgi.mtb.gui.table;

import javax.swing.table.TableModel;

/**
 * The <code>FXTableModel</code> interface extends the 
 * <code>TableModel</code> interface.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public interface MXTableModelInterface
  extends TableModel {
  
    //--------------------------------------------------------------- Constants
    // none

    //------------------------------------------------------ Instance Variables
    // none

    //------------------------------------------------------------ Constructors
    // none

    //---------------------------------------------------------- Public Methods

    /**
     * Returns <code>true</code> or <code>false</code> depending on whether or
     * not the column is sortable.
     *
     * @param col The column index
     *
     * @return <code>true</code> if the column is sortable, <code>false</code>
     * otherwise
     */
    public boolean isSortable(int col);
  
    /**
     * Sort the specified column in the specified order.
     *
     * @param col The column index
     * @param ascending Set to <code>true</code> if the column should be sorted
     * in ascending order, <code>false</code> otherwise
     */
    public void sortColumn(int col, boolean ascending);
    
    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods
    // none
}

