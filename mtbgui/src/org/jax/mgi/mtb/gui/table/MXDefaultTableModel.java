package org.jax.mgi.mtb.gui.table;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * The <code>MXColumnComparator</code> extends the 
 * <code>DefaultTableModel</code> class and implements the 
 * <code>FXTableModel</code> interface.  This is a sortable table model.
 * 
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class MXDefaultTableModel
    extends DefaultTableModel
    implements MXTableModelInterface {
    
    //--------------------------------------------------------------- Constants
    // none

    //------------------------------------------------------ Instance Variables
    // none

    //------------------------------------------------------------ Constructors

    /**
     * Default constructor.
     */
    public MXDefaultTableModel() {
    }
    
    /**
     * Constructor specifying the number of rows and columns.
     *
     * @param rows The number of rows.
     * @param columns The number of columns.
     */
    public MXDefaultTableModel(int rows, int columns) {
        super(rows, columns);
    }
    
    /**
     * Constructor specifying the data and column names.
     *
     * @param data Multidimensional array containing the model's data elements.
     * @param names An array of column names.
     */
    public MXDefaultTableModel(Object[][] data, Object[] names) {
        super(data, names);
    }
    
    /**
     * Constructor specifying the column names and number of rows.
     *
     * @param names An array of column names.
     * @param rows The number of rows.
     */
    public MXDefaultTableModel(Object[] names, int rows) {
        super(names, rows);
    }
    
    /**
     * Constructor specifying the column names and number of rows.
     *
     * @param names A <code>Vector</code> of column names.
     * @param rows The number of rows.
     */
    public MXDefaultTableModel(Vector names, int rows) {
        super(names, rows);
    }
    
    /**
     * Constructor specifying the data and column names.
     *
     * @param data A <code>Vector</code> containing the model's data elements.
     * @param names A <code>Vector</code> of column names.
     */
    public MXDefaultTableModel(Vector data, Vector names) {
        super(data, names);
    }
    
    /**
     * Constructor specifying the column names and number of rows.
     *
     * @param names An <code>ArrayList</code> of column names.
     * @param rows The number of rows.
     */
    public MXDefaultTableModel(List names, int rows) {
        // convert the ArrayList to a Vector
        super(convert(names), rows);    
    }

    /**
     * Constructor specifying the data and column names.
     *
     * @param data An <code>ArrayList</code> containing the model's data 
     * elements.
     * @param names An <code>ArrayList</code> of column names.
     */
    public MXDefaultTableModel(List data, List names) {
        // convert the ArrayLists to Vectors
        this(convert(data), new Vector(names));
    }
    
    //---------------------------------------------------------- Public Methods

    /**
     * Indicates whether the specified column is sortable.
     *
     * @param col The column index
     *
     * @return <code>true</code> if the column is sortable, <code>false</code>
     * otherwise
     */
    public boolean isSortable(int col) {
        return true;
    }
    
    /**
     * Sort the specified column in the specified order.
     *
     * @param col The column index
     * @param ascending Set to <code>true</code> if the column should be sorted
     * in ascending order, <code>false</code> otherwise
     */
    public void sortColumn(int col, boolean ascending) {
        Collections.sort(getDataVector(),
            new MXColumnComparator(col, ascending));
    }
    
    public void insertRowNumbers() {
        
    }
    
    public void removeRowNumbers() {
    }
    
    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods

    /**
     * Static method to convert an ArrayList of ArrayLists to a Vector of 
     * Vectors!
     *
     * @param a The <code<ArrayList</code> to convert.
     *
     * @return A <code>Vector</code>
     */
    private static Vector convert(List  a) {
        Vector v = new Vector();
        
        for (Object o : a) {
            Vector temp = new Vector((Collection)o);
            v.add(temp);
        }
        
        return v;
    }
}

