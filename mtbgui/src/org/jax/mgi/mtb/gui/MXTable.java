  /**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTable.java,v 1.2 2008/11/04 13:18:02 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jax.mgi.mtb.gui.table.MXDefaultTableModel;
import org.jax.mgi.mtb.gui.table.MXHeaderRenderer;
import org.jax.mgi.mtb.gui.table.MXTableModelInterface;
/**
 * The <code>ColumnComparator</code> class implements the
 * <code>Comparator</code> interface.
 *
 * @author $Author: sbn $
 * @version $Revision: 1.2 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTable.java,v 1.2 2008/11/04 13:18:02 sbn Exp $
 * @date $Date: 2008/11/04 13:18:02 $
 */
public class MXTable extends JTable
        implements MouseListener {

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
    protected boolean sortedColumnAscending = false;
    protected int[] columnSizes;

    /**
     * The color of the hilite row.
     */
    protected Color rowHiliteColor = null;
    protected String toolTipFGColor = null;
    protected String toolTipBGColor = null;


    private HashMap disabledToolTipColumns = new HashMap();
    private boolean toolTipsEnabled = true;
    private boolean editable = true;
    private boolean highlight = false;
    private int highlightRows = 1;
    private int startHighlightRow = 1;
    private Icon ascendingIcon = null;
    private Icon decendingIcon = null;
    private TableCellRenderer tableCellRenderer = null;
    
    private boolean doHighlights = true;

    //------------------------------------------------------------ Constructors

    /**
     * Default constructor.
     */
    public MXTable() {
        this(new MXDefaultTableModel());
    }

    /**
     * Construct a JTable with the specified number of rows and columns.
     *
     * @param rows The numer of rows.
     * @param cols The numer of columns.
     */
    public MXTable(int rows, int cols) {
        this(new MXDefaultTableModel(rows, cols));
    }

    /**
     * Constructor specifying the data and column names.
     *
     * @param data Multidimensional array containing the model's data elements.
     * @param names An array of column names.
     */
    public MXTable(Object[][] data, Object[] names) {
        this(new MXDefaultTableModel(data, names));
    }

    /**
     * Constructor specifying the data and column names.
     *
     * @param data A <code>Vector</code> containing the model's data elements.
     * @param names A <code>Vector</code> of column names.
     */
    public MXTable(Vector data, Vector names) {
        this(new MXDefaultTableModel(data, names));
    }

    /**
     * Constructor specifying the data and column names.
     *
     * @param data An <code>ArrayList</code> containing the model's data
     * elements.
     * @param names An <code>ArrayList</code> of column names.
     */
    public MXTable(List data, List names) {
        this(new MXDefaultTableModel(data, names));
    }

    /**
     * Constructor specifying the <code>FXTableModel</code>.
     *
     * @param model The <code>FXTableModel</code>.
     */
    public MXTable(MXTableModelInterface model) {
        super(model);
        initColumnSizes();
        initMXHeader();
    }

    /**
     * Constructor specifying the <code>FXTableModel</code> and
     * <code>TableColumnModel</code>.
     *
     * @param model The <code>FXTableModel</code>.
     * @param colModel The <code>TableColumnModel</code>.
     */
    public MXTable(MXTableModelInterface model,
            TableColumnModel colModel) {
        super(model, colModel);
        initColumnSizes();
        initMXHeader();
    }

    /**
     * Constructor specifying the <code>FXTableModel</code>.
     *
     * @param model The <code>FXTableModel</code>.
     * @param colModel The <code>TableColumnModel</code>.
     * @param selModel The <code>ListSelectionModel</code>.
     */
    public MXTable(MXTableModelInterface model,
            TableColumnModel colModel,
            ListSelectionModel selModel) {
        super(model, colModel, selModel);
        initColumnSizes();
        initMXHeader();
    }

    //---------------------------------------------------------- Public Methods

    private void initColumnSizes() {
        int numColumns = getModel().getColumnCount();
        columnSizes = new int[numColumns];

        for (int i = 0; i < numColumns; i++) {
            columnSizes[i] = 0;
        }
    }


    ///////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////

    public void setColumnSizes(int[] columnSizes) {
        this.columnSizes=columnSizes;
    }

    public void columnMoved(TableColumnModelEvent evt) {
        int moveFrom=evt.getFromIndex();
        int moveTo=evt.getToIndex();

        if (moveTo>moveFrom) {
            int movedWidth=this.columnSizes[moveFrom];
            for (int i=moveFrom; i<moveTo; i++) {
                this.columnSizes[i]=this.columnSizes[i+1];
            }
            this.columnSizes[moveTo]=movedWidth;
        } else {
            int movedWidth=this.columnSizes[moveFrom];
            for (int i=moveFrom; i>moveTo; i--) {
                this.columnSizes[i]=this.columnSizes[i-1];
            }
            this.columnSizes[moveTo]=movedWidth;
        }

        super.columnMoved(evt);
    }

    public void doLayout() {
        // Get the column that is resized
        TableColumn resizingColumn=(tableHeader==null)?null:tableHeader.getResizingColumn();

        // If user is resizing a column, we first need to check if the column should be "fixated", that is
        // in my idea of this resizing once the non-fixed column is resized by user, it becomes "fixed"
        if (resizingColumn!=null) {
            int columnIndex=viewIndexForColumn(resizingColumn);

            this.columnSizes[columnIndex]=resizingColumn.getWidth();
        }

        // Now layout the columns

        //int wholeTableWidth=getPreferredScrollableViewportSize().width;
        int wholeTableWidth=getWidth()+1;
        int nonFixedColumnCount=0;
        for (int i=0; i<this.columnSizes.length; i++) {
            if (this.columnSizes[i]==0) {
                nonFixedColumnCount++;
            }
        }

        if (nonFixedColumnCount>0) {
            int widthForFixed=0;
            for (int i=0; i<columnModel.getColumnCount(); i++) {
                TableColumn tc=columnModel.getColumn(i);
                if (this.columnSizes[i]!=0) {
                    tc.setWidth(this.columnSizes[i]);
                    widthForFixed+=this.columnSizes[i];
                }
            }

            int remainingWidth=wholeTableWidth-widthForFixed;
            for (int i=0; i<columnModel.getColumnCount(); i++) {
                TableColumn tc=columnModel.getColumn(i);
                if (this.columnSizes[i]==0) {
                    tc.setWidth(remainingWidth/nonFixedColumnCount);
                }
            }
        } else {
            int widthForFixed=0;
            for (int i=0; i<columnModel.getColumnCount(); i++) {
                TableColumn tc=columnModel.getColumn(i);

                int prefWidth=this.columnSizes[i];
                tc.setWidth(prefWidth);
                widthForFixed+=prefWidth;
            }

            int remainingWidth=wholeTableWidth-widthForFixed;
            for (int i=0; i<columnModel.getColumnCount(); i++) {
                TableColumn tc=columnModel.getColumn(i);
                tc.setWidth(tc.getWidth()+(remainingWidth/columnModel.getColumnCount()));
            }
        }
    }

    private int viewIndexForColumn(TableColumn aColumn) {
        TableColumnModel cm=getColumnModel();
        for (int column=0; column<cm.getColumnCount(); column++) {
            if (cm.getColumn(column)==aColumn) {
                return column;
            }
        }
        return -1;
    }
    ///////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////

    /**
     * Get the hilite color for alternate rows.
     *
     * @return The <code>Color</code> used in row hiliting.
     */
    public Color getRowHiliteColor() {
        return this.rowHiliteColor;
    }

    /**
     * Set the hilite color for the alternate rows.
     *
     * @param c The <code>Color</code> used in row hiliting.
     */
    public void setRowHiliteColor(Color c) {
        this.rowHiliteColor = c;
    }

    /**
     * Get the sorted column index.
     *
     * @return The index of the column being sorted.
     */
    public int getSortedColumnIndex() {
        return this.sortedColumnIndex;
    }

    /**
     * Set the sorted column index.
     *
     * @param sortedColumnIndex The sorted column index.
     */
    public void setSortedColumnIndex(int sortedColumnIndex) {
        this.sortedColumnIndex = sortedColumnIndex;
    }

    /**
     * Specifies whether the sort order is ascending or not.
     *
     * @return <code>true</code> if the sort order is ascending,
     * <code>false</code> otherwise
     */
    public boolean getSortedColumnAscending() {
        return this.sortedColumnAscending;
    }

    /**
     * Specifies whether the sort order is ascending or not.
     *
     * @param sortedColumnAscending <code>true</code> if the sort order is
     * ascending, <code>false</code> otherwise
     */
    public void setSortedColumnAscending(boolean sortedColumnAscending) {
        this.sortedColumnAscending = sortedColumnAscending;
    }

    /**
     * Capture when the mouse is released while in the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseReleased(MouseEvent event) {
        TableColumnModel colModel = getColumnModel();
        // The index of the column whose header was clicked
        int index = colModel.getColumnIndexAtX(event.getX());
        int modelIndex = colModel.getColumn(index).getModelIndex();

        // return if not clicked on any column header
        if (index == -1) {
            return;
        }

        // determine if mouse was pressed between column heads
        Rectangle headerRect = this.getTableHeader().getHeaderRect(index);

        if (index == 0) {
            headerRect.width -= 3;
        } else {
            headerRect.grow(-10,0);
        }

        if (!headerRect.contains(event.getX(), event.getY())) {
            return;
        }

        MXTableModelInterface model = (MXTableModelInterface)getModel();
        if (model.isSortable(modelIndex)) {
            // toggle ascension, if already sorted
            if (sortedColumnIndex == index) {
                sortedColumnAscending = !sortedColumnAscending;
            }

            sortedColumnIndex = index;

            model.sortColumn(modelIndex, sortedColumnAscending);
        }
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
     * Capture when the mouse is clicked while in the <code>JTable</code>.
     *
     * @param event The <code>MouseEvent</code>
     */
    public void mouseClicked(MouseEvent event) {
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
     * Make all cells <b>un</b>editable.
     */
    public void makeUneditable() {
        this.editable = false;
    }

    /**
     * Make all cells editable.
     */
    public void makeEditable() {
        this.editable = true;
    }

    /**
     * Return true if the cell at row, coilumn is editable, false otherwise.
     *
     * @param row The row
     * @param column The column
     */
    public boolean isCellEditable(int row, int column) {
        if (this.editable) {
            return super.isCellEditable(row, column);
        } else {
            return this.editable;
        }
    }

    /**
     * Enable or disbale tooltips for the table.
     *
     * @param enable True to enable tooltips, false to disable tooltips
     */
    public void enableToolTips(boolean enable) {
        this.toolTipsEnabled = enable;
    }

    /**
     * Enable or disbale tooltips for a column.
     *
     * @param col The column, -1 for all columns
     * @param enable True to enable tooltips, false to disable tooltips
     */
    public void enableToolTip(int col, boolean enable) {
        try {

            if (col == -1) {
                disabledToolTipColumns = new HashMap();
                return;
            }

            String name = this.getColumnName(col);

            if (enable) {
                disabledToolTipColumns.remove(name);
            } else {
                disabledToolTipColumns.put(name, name);
            }
        } catch (Exception e) {

        }
    }

    public void setToolTipFGColor(String s) {
        this.toolTipFGColor = s;
    }

    public String getToolTipFGColor() {
        return this.toolTipFGColor;
    }

    public void setToolTipBGColor(String s) {
        this.toolTipBGColor = s;
    }

    public String getToolTipBGColor() {
        return this.toolTipBGColor;
    }

    /**
     * Get the text to display as a ToolTip if the data does not fit in the
     * cell.
     *
     * @param event The <code>MouseEvent</code>
     * @return The text to display.
     */
    public String getToolTipText(MouseEvent event) {
        if (!toolTipsEnabled) {
            return null;
        }

        int row = rowAtPoint(event.getPoint());
        int col = columnAtPoint(event.getPoint());
        Object o = null;

        try {
            o = getValueAt(row,col);
        } catch (Exception e) {
        }

        if (o == null) {
            return null;
        }

        if (o.toString().equals("")) {
            return null;
        }

        String name = this.getColumnName(col);

        if (disabledToolTipColumns.containsKey(name)) {
            return null;
        }

        StringBuffer sb = new StringBuffer(500);
        sb.append("<html><body bgcolor=\"");

        if (toolTipFGColor == null) {
            toolTipFGColor = "black";
        }

        if (toolTipBGColor == null) {
            toolTipBGColor = "white";
        }

        sb.append(toolTipBGColor);
        sb.append("\" color=\"");
        sb.append(toolTipFGColor);
        sb.append("\"><table border=\"0\" width=\"200\">");
        sb.append(o.toString());
        sb.append("</table></body></html>");

        return sb.toString();
    }

    /**
     * Get the location of the ToolTip text to display.
     *
     * @param event The <code>MouseEvent</code>
     * @return The location as a </code>Point</code>.
     */
    public Point getToolTipLocation(MouseEvent event) {
        int row = rowAtPoint( event.getPoint() );
        int col = columnAtPoint( event.getPoint() );
        Object o = null;

        try {
            o = getValueAt(row,col);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        if (o == null) {
            return null;
        }

        if (o.toString().equals("")) {
            return null;
        }

        Point pt = getCellRect(row, col, true).getLocation();
        pt.translate(-1,-2);
        return pt;
    }

    /**
     * Specifically added so someonce could extend the MXDefaultTableModel and
     * provide a custom renderer from there.
     */
    public void setDefaultRenderer(Class columnClass, TableCellRenderer renderer) {
        this.tableCellRenderer = renderer;
    }


    /**
     * Prepare the renderer for each column.  Used currently for row hiliting.
     *
     * @param renderer The <code>TableCellRenderer</code>
     * @param rowIndex The row index.
     * @param vColIndex The column index.
     * @return The component renderer.
     */
    public Component prepareRenderer(TableCellRenderer renderer,
            int rowIndex, int vColIndex) {

        if (this.tableCellRenderer != null) {
            return super.prepareRenderer(this.tableCellRenderer, rowIndex, vColIndex);
        }
       

        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);

        int row = rowIndex;
        
        if(doHighlights){

          if(getAlternateRowHighlight()) {
              if (getRowHiliteColor() == null) {
                  c.setBackground(getBackground());
              } else {
                  if (rowIndex < (getStartHighlightRow())) {
                      c.setBackground(getBackground());
                  } else {
                      row = rowIndex - (getStartHighlightRow());

                      if (row % getAlternateRowHighlightCount() == 0) {
                          c.setBackground(getRowHiliteColor());
                      } else {
                          c.setBackground(getBackground());
                      }
                  }
              }
          } else {
              c.setBackground(getBackground());
          }
        }
        if (isCellSelected(rowIndex, vColIndex)) {
            c.setBackground(getSelectionBackground());
        }

        return c;
    }

    public void setAscendingIcon(Icon icon) {
        //FXTableHeader MXHeader = (MXTableHeader)this.getTableHeader();
        //fxHeader.setAscendingIcon(icon);
        this.ascendingIcon = icon;

        JTableHeader mxHeader = this.getTableHeader();
        MXHeaderRenderer mxRenderer = (MXHeaderRenderer)mxHeader.getDefaultRenderer();
        mxRenderer.setAscendingIcon(icon);
    }

    public void setDecendingIcon(Icon icon) {
        //FXTableHeader MXHeader = (MXTableHeader)this.getTableHeader();
        //fxHeader.setDecendingIcon(icon);
        this.decendingIcon = icon;
        JTableHeader mxHeader = this.getTableHeader();
        MXHeaderRenderer mxRenderer = (MXHeaderRenderer)mxHeader.getDefaultRenderer();
        mxRenderer.setDecendingIcon(icon);
    }

    // allow individual column renderers to handle hightlighting
    public void setDoHightlights(boolean in){
      this.doHighlights = in;
    }
    
    public void setAlternateRowHighlight(boolean on) {
        this.highlight = on;
    }

    public boolean getAlternateRowHighlight() {
        return this.highlight;
    }


    public void setAlternateRowHighlightCount(int rows) {
        if (rows < 0) {
            this.highlightRows = 2;
        } else {
            this.highlightRows = rows;
        }
    }

    public int getAlternateRowHighlightCount() {
        return this.highlightRows;
    }

    // count starts at 0, so the first row is really row #0
    public void setStartHighlightRow(int startHighlightRow) {
        if (startHighlightRow < 0) {
            this.startHighlightRow = 0;
        } else {
            this.startHighlightRow = startHighlightRow;
        }
    }

    public int getStartHighlightRow() {
        return this.startHighlightRow;
    }


    public boolean getScrollableTracksViewportHeight() {
        return getPreferredSize().height < getParent().getHeight();
    }

    public void setModel(MXTableModelInterface model) {
        super.setModel(model);
    }

    public void setHeaderFont(Font headerFont) {
        JTableHeader mxHeader = this.getTableHeader();
        mxHeader.setFont(headerFont);
    }

    public Font getHeaderFont() {
        return this.getTableHeader().getFont();
    }


    public void displayRowNumbers() {
        //MXDefaultTableModel model = (MXDefaultTableModel)this.getModel();
    }

    //------------------------------------------------------- Protected Methods

    /**
     * Initialize the table header.
     */
    protected void initMXHeader() {
        JTableHeader mxHeader = this.getTableHeader();
        mxHeader.setDefaultRenderer(new MXHeaderRenderer(ascendingIcon, decendingIcon));
        mxHeader.addMouseListener(this);
    }

    //--------------------------------------------------------- Private Methods
    //none
}

