package org.jax.mgi.mtb.gui.table;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.jax.mgi.mtb.gui.MXTable;


/**
 * Table header listener for </code>MXTable</code>.
 *
 * @author moose@jawsoft.us
 * @version 1.0
 */
public class MXHeaderListener
    extends MouseAdapter {
        
    private final boolean DEBUG_MODE = true;
    
    /**
     * Capture when the mouse is released while in the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseReleased(MouseEvent event) {
        // not implemented
    }
    
    /**
     * Capture when the mouse is pressed while in the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mousePressed(MouseEvent event) {
        // not implemented
    }
    
    /**
     * Capture when the mouse has entered the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseEntered(MouseEvent event) {
        // not implemented
    }

    /**
     * Capture when the mouse hass exited the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseExited(MouseEvent event) {
        // not implemented
    }

    /**
     * Capture when the mouse is clicked while in the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseClicked(MouseEvent event) {
        MXTable table = 
            (MXTable)((JTableHeader)event.getSource()).getTable();
        TableColumnModel colModel = table.getColumnModel();
        
        debug("################################################ START");
        debug("when="+event.getWhen());
        
        // The index of the column whose header was clicked
        int index = colModel.getColumnIndexAtX(event.getX());
        int modelIndex = colModel.getColumn(index).getModelIndex();
        debug("Mouse clicked at index=" + index + ",modelIndex=" + modelIndex);
        debug("component=" + event.getComponent());
        debug("paramString="+event.paramString());
        debug("click count = " + event.getClickCount());
        debug("source = " + event.getSource());
        
        // return if not clicked on any column header
        if (index == -1) {
            debug("Did not click on column header");
            return;
        }
        
        // determine if mouse was pressed between column heads
        Rectangle headerRect = table.getTableHeader().getHeaderRect(index);
        
        if (index == 0) {
            headerRect.width -= 3;
        } else {
            headerRect.grow(-3, 0);
        }
        
        if (!headerRect.contains(event.getX(), event.getY())) {
            debug("Mouse was clicked between column heads.");
            return;
        }

        MXTableModelInterface model = (MXTableModelInterface)table.getModel();
        boolean sortAscending = true;
        
        if (model.isSortable(modelIndex)) {
            debug("Sortable.");
            // toggle ascension, if already sorted
            if (table.getSortedColumnIndex() == index) {
                debug("table.getSortedColumnIndex()="+table.getSortedColumnIndex());
                debug("index="+index);
                debug("Column already sorted, so toggling.");
                
                sortAscending = !table.getSortedColumnAscending();
                table.setSortedColumnAscending(sortAscending);
            }
            table.setSortedColumnIndex(index);
        
            debug("sorting="+index +" to be " + (sortAscending ? "ascending" : "decending"));
            model.sortColumn(modelIndex, sortAscending);
        }
        
        debug("################################################ DONE");
    }

    //------------------------------------------------------- Protected Methods
    // none
    
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
