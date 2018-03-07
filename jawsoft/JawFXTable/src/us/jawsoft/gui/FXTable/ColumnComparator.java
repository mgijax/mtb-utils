package us.jawsoft.gui.FXTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

/**
 * The <code>ColumnComparator</code> class implements the 
 * <code>Comparator</code> interface.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class ColumnComparator
    implements Comparator {

    //--------------------------------------------------------------- Constants
    // none

    //------------------------------------------------------ Instance Variables

    /**
     * The column index.
     */
    protected int index;
    
    /**
     * <code>true</code> if the column should be sorted in ascending order, 
     * <code>false</code> otherwise.
     */
    protected boolean ascending = false;
  
  
    //------------------------------------------------------------ Constructors

    /**
     * Constructor for comparing columns.
     *
     * @param index The column index
     *
     * @param ascending Set to <code>true</code> if the column should be sorted
     * in ascending order, <code>false</code> otherwise
     */
    public ColumnComparator(int index, boolean ascending) {
        this.index = index;
        this.ascending = ascending;
    }
  

    //---------------------------------------------------------- Public Methods

    /**
     * Compare the two objects.
     * 
     * @param one First object to compare
     *
     * @param two Second object to compare
     *
     * @return An integer value specifying how these items compare to one 
     * another
     * <ul>
     *     <li><code>-1</code> - <code>one</code> &lt; <code>two</code></li>
     *     <li><code>0</code> - <code>one</code> = <code>two</code></li>
     *     <li><code>1</code> - <code>one</code> &gt; <code>two</code></li>
     * </ul>
     */
    public int compare(Object one, Object two) {
        int iReturn = 0;
        
        if (one == null && two != null) {
            iReturn = -1;
        } else if (one == null && two == null) {
            iReturn = 0;
        } else if (one != null && two == null) {
            iReturn = 1;
        } else {
            if ((one instanceof ArrayList) && (two instanceof ArrayList)) {
                ArrayList aOne = (ArrayList)one;
                ArrayList aTwo = (ArrayList)two;
                one = aOne.get(index);
                two = aTwo.get(index);
            } else if ((one instanceof Vector) && (two instanceof Vector)) {
                Vector vOne = (Vector)one;
                Vector vTwo = (Vector)two;
                one = vOne.elementAt(index);
                two = vTwo.elementAt(index);
            } 
            
            if (one == null && two != null) {
                iReturn = -1;
            } else if (one == null && two == null) {
                iReturn = 0;
            } else if (one != null && two == null) {
                iReturn = 1;
            } else {
                if ((one instanceof Comparable) && (two instanceof Comparable)) {
                    Comparable cOne = (Comparable)one;
                    Comparable cTwo = (Comparable)two;
                    iReturn = cOne.compareTo(cTwo);
                } 
            }
        }
        
        return ascending ? iReturn : (-1 * iReturn);
    }
    
    
    //------------------------------------------------------- Protected Methods
    // none
    
    //--------------------------------------------------------- Private Methods
    // none
}

