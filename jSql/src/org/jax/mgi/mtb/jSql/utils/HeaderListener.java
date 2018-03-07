/*
 * HeaderListener.java
 *
 * Created on December 1, 2003, 11:55 AM
 */

package org.jax.mgi.mtb.jSql.utils;

import java.io.*;
import java.sql.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

/**
 *
 * @author  mjv
 */
public class HeaderListener
    extends MouseAdapter {
    JTableHeader header;
    SortButtonRenderer renderer;

    public HeaderListener(JTableHeader header, SortButtonRenderer renderer) {
        this.header = header;
        this.renderer = renderer;
    }
    
    public void mouseClicked(MouseEvent evt) {
        JTable table = ((JTableHeader)evt.getSource()).getTable();
        TableColumnModel colModel = table.getColumnModel();
        
        // The index of the column whose header was clicked
        int vColIndex = colModel.getColumnIndexAtX(evt.getX());
        int mColIndex = table.convertColumnIndexToModel(vColIndex);
        
        // return if not clicked on any column header
        if (vColIndex == -1) {
            return;
        }
        
        // determine if mouse was pressed between column heads
        Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
        
        if (vColIndex == 0) {
            headerRect.width -= 3;
        } else {
            headerRect.grow(-3, 0);
        }
        
        if (!headerRect.contains(evt.getX(), evt.getY())) {
            // mouse was clicked between column heads
            return;
        }
        
        // if we reach here, mouse was clicked on a colummn head
        renderer.setPressedColumn(vColIndex);
        renderer.setSelectedColumn(vColIndex);
        header.repaint();

        if (header.getTable().isEditing()) {
            header.getTable().getCellEditor().stopCellEditing();
        }

        boolean isAscent;
        if (SortButtonRenderer.DOWN == renderer.getState(vColIndex)) {
            isAscent = true;
        } else {
            isAscent = false;
        }
        ((TableSorter) header.getTable().getModel()).sortByColumn(mColIndex, isAscent);                
        
        renderer.setPressedColumn( -1); // clear
        header.repaint();
    }
}
