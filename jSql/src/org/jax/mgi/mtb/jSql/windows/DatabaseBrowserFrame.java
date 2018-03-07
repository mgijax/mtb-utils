/*
 * DatabaseBrowserFrame.java
 *
 * Created on December 1, 2003, 2:17 PM
 */

package org.jax.mgi.mtb.jSql.windows;

import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;

import org.jax.mgi.mtb.jSql.*;
import org.jax.mgi.mtb.jSql.windows.*;
import org.jax.mgi.mtb.jSql.utils.*;

/**
 *
 * @author  mjv
 */
public class DatabaseBrowserFrame extends javax.swing.JInternalFrame {
    
    private jSql app = null;
    private String connectionId = null;
    CustomItemListener actionListener = null;
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
    public DatabaseBrowserFrame(jSql app, String connectionId, boolean resizable, boolean closable, 
                         boolean maximizable, boolean iconifiable) {
        
        super("Database Browser - " + connectionId, resizable, closable, maximizable, iconifiable);        
        this.app = app;
        setConnectionId(connectionId);
        initComponents();
        // Create and register listener
        actionListener = new CustomItemListener(this);
        jTabbedPane1.removeAll();
        populateAll(null, null);
    }
    
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
    
    public String getConnectionId() {
        return this.connectionId;
    }
    
    public String getCurrentSchema() {
        Object sel = comboSchemas.getSelectedItem();
        
        if (sel != null) {
            return sel.toString();
        }
        
        return null;
    }
    
    public String getCurrentCatalog() {
        Object sel = comboCatalogs.getSelectedItem();
        
        if (sel != null) {
            return sel.toString();
        }
        
        return null;
    }

    public void populateAll(String catalog, String schema) {
        
        Connection conn = app.getConnection(getConnectionId());
        //System.out.println("DatabaseBrowserFrame.getConnectionId()="+getConnectionId());

        try {
            
            // handle catalogs
            DatabaseMetaData dmd = conn.getMetaData();
            ResultSet rs = dmd.getCatalogs();
            int count = 0;

            comboCatalogs.removeItemListener(actionListener);
            comboCatalogs.removeAllItems();
            
            while (rs.next()) {
                comboCatalogs.addItem(rs.getString(1));
                count++;
            }
            
            if (count == 0) {
                pnlHierarchy.remove(pnlCatalogs);
            } else {
                lblCatalogs.setText(dmd.getCatalogTerm().toUpperCase());
                
                if (catalog == null) {
                    catalog = conn.getCatalog();
                }
                
                comboCatalogs.addItemListener(actionListener);
                comboCatalogs.setSelectedItem(catalog);
            }
            
            // handle schemas
            lblSchemas.setText(dmd.getSchemaTerm().toUpperCase());            
            rs = dmd.getSchemas();
            comboSchemas.removeItemListener(actionListener);
            comboSchemas.removeAllItems();
            
            while (rs.next()) {
                comboSchemas.addItem(rs.getString(1).trim());
                count++;
            }
            
            if (schema == null) {
                schema = dmd.getUserName();
            }
            
            comboSchemas.addItemListener(actionListener);
            comboSchemas.setSelectedItem(schema);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.returnConnection(getConnectionId(), conn);
        }
        
        populateObjects(catalog, schema);
    }
    
    public void populateObjects(String catalog, String schema) {
        Connection conn = app.getConnection(getConnectionId());

        jTabbedPane2.removeAll();
        
        try {
            DatabaseMetaData dmd = conn.getMetaData();
            ResultSet rs = null;

            // add objects
            jTabbedPane2.removeAll();
            

            rs = dmd.getTableTypes();
            
            ArrayList temp = new ArrayList(); 
            
            while (rs.next()) {
                temp.add(rs.getString(1));
            }
            
            rs.close();
            
            for (int i = 0; i < temp.size(); i++) {
                String tableTypes[] = new String[1];
                tableTypes[0] = ((String)temp.get(i)).trim();
                //System.out.println("---"+tableTypes[0]+"---");
                rs = dmd.getTables(catalog, schema, null, tableTypes);
                
                BrowserTypeListPanel p = new BrowserTypeListPanel(this, tableTypes[0], rs);
                jTabbedPane2.add(tableTypes[0], p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.returnConnection(getConnectionId(), conn);
        }
    }
    
    public void populateDetail(Object o) {
        jTabbedPane1.removeAll();
        Connection conn = null;
        DatabaseMetaData dmd = null;
        
        try {
            BrowserTypeListPanel p = (BrowserTypeListPanel)jTabbedPane2.getSelectedComponent();
            if (p.getType().toLowerCase().equalsIgnoreCase("table")) {
                conn = app.getConnection(connectionId);
                dmd = conn.getMetaData();
                DetailTableTypePanel columns = new DetailTableTypePanel(app, conn, dmd.getColumns(getCurrentCatalog(), getCurrentSchema(), o.toString(), null));
                app.returnConnection(connectionId, conn);

                conn = app.getConnection(connectionId);
                dmd = conn.getMetaData();
                DetailTableTypePanel primaryKeys = new DetailTableTypePanel(app, conn, dmd.getPrimaryKeys(getCurrentCatalog(), getCurrentSchema(), o.toString()));
                app.returnConnection(connectionId, conn);

                conn = app.getConnection(connectionId);
                dmd = conn.getMetaData();
                DetailTableTypePanel importedKeys = new DetailTableTypePanel(app, conn, dmd.getImportedKeys(getCurrentCatalog(), getCurrentSchema(), o.toString()));
                app.returnConnection(connectionId, conn);

                conn = app.getConnection(connectionId);
                dmd = conn.getMetaData();
                DetailTableTypePanel exportedKeys = new DetailTableTypePanel(app, conn, dmd.getExportedKeys(getCurrentCatalog(), getCurrentSchema(), o.toString()));
                app.returnConnection(connectionId, conn);

    //            conn = app.getConnection(connectionId);
    //            dmd = conn.getMetaData();
    //            DetailTableTypePanel indexInfo = new DetailTableTypePanel(app, connectionId, dmd.getIndexInfo(getCurrentCatalog(), getCurrentSchema(), o.toString(), false, false));
    //            app.returnConnection(connectionId, conn);

                jTabbedPane1.add("COLUMNS", columns);
                jTabbedPane1.add("PRIMARY KEYS", primaryKeys);
    //            jTabbedPane1.add("INDEX INFO", indexInfo);
                jTabbedPane1.add("IMPORTED KEYS", importedKeys);
                jTabbedPane1.add("EXPORTED KEYS", exportedKeys);
            }
            
            conn = app.getConnection(connectionId);
            DetailTableTypePanel data = new DetailTableTypePanel(app, conn, "SELECT * FROM " + o.toString());
            jTabbedPane1.add("DATA", data);
            app.returnConnection(connectionId, conn);

        } catch (Exception e) {
            //System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void showResults(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                //System.out.print(rsmd.getColumnName(i) + "\t");
            }
            
            //System.out.println();
            
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    //System.out.print(rs.getString(i) + "\t");
                }
                //System.out.println();
            }
        } catch (Exception e) {
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnlObjects = new javax.swing.JPanel();
        pnlHierarchy = new javax.swing.JPanel();
        pnlCatalogs = new javax.swing.JPanel();
        lblCatalogs = new javax.swing.JLabel();
        comboCatalogs = new javax.swing.JComboBox();
        pnlSchemas = new javax.swing.JPanel();
        lblSchemas = new javax.swing.JLabel();
        comboSchemas = new javax.swing.JComboBox();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        scrollPaneObjects = new javax.swing.JScrollPane();
        listObjects = new javax.swing.JList();
        pnlDetail = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        dummyPanel = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(400);
        pnlObjects.setLayout(new java.awt.BorderLayout());

        pnlHierarchy.setLayout(new javax.swing.BoxLayout(pnlHierarchy, javax.swing.BoxLayout.X_AXIS));

        pnlHierarchy.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnlHierarchy.setMinimumSize(new java.awt.Dimension(97, 40));
        pnlHierarchy.setPreferredSize(new java.awt.Dimension(97, 40));
        lblCatalogs.setText("Catalogs");
        pnlCatalogs.add(lblCatalogs);

        pnlCatalogs.add(comboCatalogs);

        pnlHierarchy.add(pnlCatalogs);

        lblSchemas.setText("Schemas");
        pnlSchemas.add(lblSchemas);

        pnlSchemas.add(comboSchemas);

        pnlHierarchy.add(pnlSchemas);

        pnlObjects.add(pnlHierarchy, java.awt.BorderLayout.NORTH);

        jTabbedPane2.setPreferredSize(new java.awt.Dimension(53, 400));
        listObjects.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Items go here" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listObjects.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        scrollPaneObjects.setViewportView(listObjects);

        jTabbedPane2.addTab("tab1", scrollPaneObjects);

        pnlObjects.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(pnlObjects);

        pnlDetail.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.addTab("tab1", dummyPanel);

        pnlDetail.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(pnlDetail);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboCatalogs;
    private javax.swing.JComboBox comboSchemas;
    private javax.swing.JPanel dummyPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblCatalogs;
    private javax.swing.JLabel lblSchemas;
    private javax.swing.JList listObjects;
    private javax.swing.JPanel pnlCatalogs;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlHierarchy;
    private javax.swing.JPanel pnlObjects;
    private javax.swing.JPanel pnlSchemas;
    private javax.swing.JScrollPane scrollPaneObjects;
    // End of variables declaration//GEN-END:variables
    
}

class CustomItemListener implements ItemListener {
    DatabaseBrowserFrame f = null;
    
    public CustomItemListener(DatabaseBrowserFrame f) {
        super();
        this.f = f;
    }

    // This method is called only if a new item has been selected.
        public void itemStateChanged(ItemEvent evt) {
            JComboBox cb = (JComboBox)evt.getSource();
    
            // Get the affected item
            Object item = evt.getItem();
    
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                // Item was just selected
                f.populateObjects(f.getCurrentCatalog(), f.getCurrentSchema());
                //System.out.println("f.getCurrentCatalog(), f.getCurrentSchema()="+f.getCurrentCatalog() +", " + f.getCurrentSchema());
            } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                // Item is no longer selected
            }
        }
    }
