/*
 * SqlQueryFrame.java
 *
 * Created on November 28, 2003, 9:40 AM
 */
package org.jax.mgi.mtb.jSql.windows;

import java.io.*;
import java.sql.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

import org.jax.mgi.mtb.jSql.utils.*;
import org.jax.mgi.mtb.jSql.*;
import org.jax.mgi.mtb.jSql.dialogs.*;

/**
 *
 * @author  mjv
 */
public class SqlQueryFrame extends javax.swing.JInternalFrame {

  private String connectionId = null;
  private SortableTableModel sortableTableModel = null;
  private TableSorter tableSorter;
  private jSql app = null;
  private EditorKit editorKit = null;
  private String connectionStr = null;

  /** 
   * Creates a <code>JInternalFrame</code> with the specified title,
   * resizability, closability, maximizability, and iconifiability.
   * All <code>JInternalFrame</code> constructors use this one.
   *
   * @param title       the <code>String</code> to display in the title bar
   * @param resizable   if <code>true</code>, the internal frame can be resized
   * @param closable    if <code>true</code>, the internal frame can be closed
   * @param maximizable if <code>true</code>, the internal frame can be maximized
   * @param iconifiable if <code>true</code>, the internal frame can be iconified
   */
  public SqlQueryFrame(jSql app, String connectionId, boolean resizable, boolean closable,
          boolean maximizable, boolean iconifiable) {

    super("Sql Editor - " + connectionId, resizable, closable, maximizable, iconifiable);
    this.app = app;
    setConnectionId(connectionId);
    editorKit = new StyledEditorKit() {

      public Document createDefaultDocument() {
        return new SqlStyleDocument();
      }
    };

    initComponents();

    jTextPaneSqlQuery.setEditorKitForContentType("text/sql", editorKit);
    jTabbedPane1.setTitleAt(0, "Results");
    jTabbedPane1.setTitleAt(1, "Messages");
    jScrollPaneSqlResults.getViewport().remove(tblQueryResults);
  }

  public void setConnectionId(String connectionId) {
    this.connectionId = connectionId;
  }

  public String getSelectedText() {
    return jTextPaneSqlQuery.getSelectedText();
  }

  public String getConnectionId() {
    return this.connectionId;
  }

  public JTable getTable() {
    return this.tblQueryResults;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelSqlQuery = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneSqlQuery = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnExecute = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRecentQueries = new javax.swing.JButton();
        btnSaveQuery = new javax.swing.JButton();
        btnLoadQuery = new javax.swing.JButton();
        btnExportResults = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPaneSqlResults = new javax.swing.JScrollPane();
        tblQueryResults = new javax.swing.JTable();
        jScrollPaneSqlMessages = new javax.swing.JScrollPane();
        jeditSqlMessages = new javax.swing.JTextArea();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                internalFrameActivation(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jPanelSqlQuery.setLayout(new java.awt.BorderLayout());

        jTextPaneSqlQuery.setEditorKit(editorKit);
        jScrollPane1.setViewportView(jTextPaneSqlQuery);

        jPanelSqlQuery.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setAutoscrolls(true);
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/new.gif")));
        btnNew.setText("New");
        btnNew.setToolTipText("Clear the SQL Editor Window");
        btnNew.setMaximumSize(new java.awt.Dimension(105, 28));
        btnNew.setMinimumSize(new java.awt.Dimension(105, 28));
        btnNew.setPreferredSize(new java.awt.Dimension(105, 28));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jToolBar1.add(btnNew);

        btnExecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/play.gif")));
        btnExecute.setText("Execute");
        btnExecute.setToolTipText("Execute the selected or standalone SQL");
        btnExecute.setMaximumSize(new java.awt.Dimension(105, 28));
        btnExecute.setMinimumSize(new java.awt.Dimension(105, 28));
        btnExecute.setPreferredSize(new java.awt.Dimension(105, 28));
        btnExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecuteActionPerformed(evt);
            }
        });

        jToolBar1.add(btnExecute);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/stop.gif")));
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("Not yet implemented");
        btnCancel.setEnabled(false);
        btnCancel.setMaximumSize(new java.awt.Dimension(105, 28));
        btnCancel.setMinimumSize(new java.awt.Dimension(105, 28));
        btnCancel.setPreferredSize(new java.awt.Dimension(105, 28));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jToolBar1.add(btnCancel);

        btnRecentQueries.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/history.gif")));
        btnRecentQueries.setText("History");
        btnRecentQueries.setToolTipText("View the recently executed  queries.");
        btnRecentQueries.setMaximumSize(new java.awt.Dimension(105, 28));
        btnRecentQueries.setMinimumSize(new java.awt.Dimension(105, 28));
        btnRecentQueries.setPreferredSize(new java.awt.Dimension(105, 28));
        btnRecentQueries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecentQueriesActionPerformed(evt);
            }
        });

        jToolBar1.add(btnRecentQueries);

        btnSaveQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/save.gif")));
        btnSaveQuery.setText("Save");
        btnSaveQuery.setToolTipText("Save a query to a file.");
        btnSaveQuery.setMaximumSize(new java.awt.Dimension(105, 28));
        btnSaveQuery.setMinimumSize(new java.awt.Dimension(105, 28));
        btnSaveQuery.setPreferredSize(new java.awt.Dimension(105, 28));
        btnSaveQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveQueryActionPerformed(evt);
            }
        });

        jToolBar1.add(btnSaveQuery);

        btnLoadQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/redo.gif")));
        btnLoadQuery.setText("Open");
        btnLoadQuery.setToolTipText("Load a query from a file.");
        btnLoadQuery.setMaximumSize(new java.awt.Dimension(105, 28));
        btnLoadQuery.setMinimumSize(new java.awt.Dimension(105, 28));
        btnLoadQuery.setPreferredSize(new java.awt.Dimension(105, 28));
        btnLoadQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadQueryActionPerformed(evt);
            }
        });

        jToolBar1.add(btnLoadQuery);

        btnExportResults.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/jSql/resources/export.gif")));
        btnExportResults.setText("Export");
        btnExportResults.setToolTipText("Export the results to a file or to the clipboard.");
        btnExportResults.setMaximumSize(new java.awt.Dimension(105, 28));
        btnExportResults.setMinimumSize(new java.awt.Dimension(105, 28));
        btnExportResults.setPreferredSize(new java.awt.Dimension(105, 28));
        btnExportResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportResultsActionPerformed(evt);
            }
        });

        jToolBar1.add(btnExportResults);

        jPanelSqlQuery.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane1.setLeftComponent(jPanelSqlQuery);

        jScrollPaneSqlResults.setToolTipText("Sql Query Results will appear in this window.");
        jScrollPaneSqlResults.setName("Sql Results");
        tblQueryResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPaneSqlResults.setViewportView(tblQueryResults);

        jTabbedPane1.addTab("tab3", jScrollPaneSqlResults);

        jScrollPaneSqlMessages.setToolTipText("Sql Messages will appear in this window.");
        jScrollPaneSqlMessages.setName("Sql Messages");
        jScrollPaneSqlMessages.setViewportView(jeditSqlMessages);

        jTabbedPane1.addTab("tab3", jScrollPaneSqlMessages);

        jSplitPane1.setRightComponent(jTabbedPane1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExportResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportResultsActionPerformed
      // Add your handling code here:
      exportResults();

    }//GEN-LAST:event_btnExportResultsActionPerformed

  public void exportResults() {
    ExportDialog dlg = new ExportDialog(null, this, null, true);
    dlg.setLocationRelativeTo(app);
    dlg.show();
  }

  public void loadQuery() {
    JFileChooser dlg = new JFileChooser();
    int returnVal = dlg.showDialog(this, "Load Query");

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {

        if (dlg.getSelectedFile() != null) {
          String fileContents = null;
          RandomAccessFile file = new RandomAccessFile(dlg.getSelectedFile().toString(),
                  "r"); // slurping the source file in the memory
          {
            byte[] fileC = new byte[(int) file.length()];

            file.readFully(fileC);
            file.close();
            //fileContents = new String(fileC, 0);
            fileContents = new String(fileC, 0, fileC.length);
          }
          file.close();

          jTextPaneSqlQuery.setText(fileContents);
          jTextPaneSqlQuery.requestFocus();
        }

      } catch (Exception exc) {
        app.handleException(exc);
      }
    }
  }

  public void saveQuery() {
    String text = jTextPaneSqlQuery.getText();

    if ((text == null) || (text.equals(""))) {
      app.handleException(new Exception("Please enter a query to save."));
      return;
    }

    JFileChooser dlg = new JFileChooser();
    int returnVal = dlg.showDialog(this, "Save Query");

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        String fileName = dlg.getSelectedFile().toString();

        if ((fileName != null) && (!fileName.equals(""))) {
          {
            File newOne = new File(fileName);

            if (newOne.exists()) {
              newOne.delete();
            }
          }
          // writing the new file
          RandomAccessFile file = new RandomAccessFile(fileName, "rw");

          file.writeBytes(text);
          file.close();
        }
      } catch (Exception exc) {
        app.handleException(exc);
      }
    }
  }

    private void btnLoadQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadQueryActionPerformed
      // Add your handling code here:
      loadQuery();
    }//GEN-LAST:event_btnLoadQueryActionPerformed

    private void btnSaveQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveQueryActionPerformed
      // Add your handling code here:
      saveQuery();
    }//GEN-LAST:event_btnSaveQueryActionPerformed

  public void setQueryText(String query) {
    jTextPaneSqlQuery.setText(query);
  }

    private void btnRecentQueriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecentQueriesActionPerformed
      // Add your handling code here:
      ArrayList a = app.getRecentQueries();

      RecentQueriesDialog dlg = new RecentQueriesDialog(null, this, a, true);
      dlg.setSize(this.getSize().width - 200, this.getSize().height - 200);
      dlg.setLocationRelativeTo(this);
      dlg.show();

    }//GEN-LAST:event_btnRecentQueriesActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
      // Add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void internalFrameActivation(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_internalFrameActivation
      // Add your handling code here:
    }//GEN-LAST:event_internalFrameActivation

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
      // Add your handling code here:
      executeQuery();
    }//GEN-LAST:event_btnExecuteActionPerformed

  private void executeQuery() {
    // get the connection
    // except the connection manager doesn't retun connections by ID
    // so we need to check to see if we get back the one we last used
    // otherwise any temporary tables won't be there
    // give it three tries
    // why doesn't the manager get fixed? well, there is no source.
    Connection conn = app.getConnection(connectionId);
    if (connectionStr == null) {
      // remember the connection
      connectionStr = conn.toString();
    } else {
      int threeTries = 0;
      // see if we got the same one back, if not try again and again..
      while (!connectionStr.equals(conn.toString()) && threeTries < 2) {
        app.returnConnection(connectionId, conn);
        conn = app.getConnection(connectionId);
        threeTries++;
      }
    }

    sortableTableModel = new SortableTableModel(conn);
    tableSorter = new TableSorter(sortableTableModel);

    String query = jTextPaneSqlQuery.getSelectedText();

    if (query == null) {
      query = jTextPaneSqlQuery.getText();
    }

    query = query.trim();
    //System.out.println("QUERY: " + query);

    if ((query.equals("")) || (query == null)) {
      //System.out.println("RETURNING");
      return;
    }

    try {
      sortableTableModel.executeQuery(query);
      jScrollPaneSqlResults.getViewport().remove(tblQueryResults);
      tblQueryResults = new JTable(tableSorter);
      tblQueryResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      SortButtonRenderer renderer = new SortButtonRenderer();
      TableColumnModel model = tblQueryResults.getColumnModel();
      TableModel tm = tblQueryResults.getModel();
      int n = tm.getColumnCount();
      for (int i = 0; i < n; i++) {
        model.getColumn(i).setHeaderRenderer(renderer);
        FontMetrics fm = tblQueryResults.getFontMetrics(tblQueryResults.getFont());
        TableColumnModel columnModel = tblQueryResults.getColumnModel();
        TableColumn column = columnModel.getColumn(i);
        int width = SwingUtilities.computeStringWidth(fm, tm.getColumnName(i) + "            ");
        //then to set the width of your column:
        column.setPreferredWidth(width);
      }

      JTableHeader header = tblQueryResults.getTableHeader();
      header.addMouseListener(new HeaderListener(header, renderer));
      jeditSqlMessages.setText("");
      jScrollPaneSqlResults.getViewport().add(tblQueryResults, null);
      jTabbedPane1.setSelectedComponent(jScrollPaneSqlResults);
      app.saveRecentQuery(query);

    /*            
    btnSave.setEnabled(true);
    
    if (!recentQueries.contains(query)) {
    recentQueries.add(query);
    saveRecentQueries();
    }
    
    if (tm.getRowCount() > 0) {
    btnExport.setEnabled(true);
    } else {
    btnExport.setEnabled(false);
    }
     **/
    } catch (Exception e32) {
      //        btnExport.setEnabled(false);
      //      btnSave.setEnabled(false);
      //app.handleException(e32);
      //      e32.printStackTrace();
      jScrollPaneSqlResults.getViewport().remove(tblQueryResults);
      tblQueryResults = new JTable(null);
      tblQueryResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      jScrollPaneSqlResults.getViewport().add(tblQueryResults, null);
      jTabbedPane1.setSelectedComponent(jScrollPaneSqlMessages);
      jeditSqlMessages.setText(e32.getMessage());

    } finally {
      app.returnConnection(connectionId, conn);
    }
  }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
      // Add your handling code here:
      jTextPaneSqlQuery.setText("");
    }//GEN-LAST:event_btnNewActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnExportResults;
    private javax.swing.JButton btnLoadQuery;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnRecentQueries;
    private javax.swing.JButton btnSaveQuery;
    private javax.swing.JPanel jPanelSqlQuery;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneSqlMessages;
    private javax.swing.JScrollPane jScrollPaneSqlResults;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPaneSqlQuery;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea jeditSqlMessages;
    private javax.swing.JTable tblQueryResults;
    // End of variables declaration//GEN-END:variables
}
