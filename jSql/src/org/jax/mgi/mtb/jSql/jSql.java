 /*
 * NewJInternalFrame.java
 *
 * Created on July 25, 2006, 4:05 PM
 */

package org.jax.mgi.mtb.jSql;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.jax.mgi.mtb.jSql.dialogs.AboutDialog;
import org.jax.mgi.mtb.jSql.dialogs.ConnectDialog;
import org.jax.mgi.mtb.jSql.dialogs.DisconnectDialog;
import org.jax.mgi.mtb.jSql.windows.DatabaseBrowserFrame;
import org.jax.mgi.mtb.jSql.windows.SqlQueryFrame;
import org.jax.mgi.mtb.rdbms.RDBMSConnectionPoolManager;


/**
 *
 * @author  mjv
 */
public class jSql extends javax.swing.JInternalFrame {
    
    /** Creates new form NewJInternalFrame */
    public jSql() {
        this(true);
    }
    
    public jSql(boolean standalone) {
        connectionManager = new RDBMSConnectionPoolManager();
        initComponents();
        this.setTitle("MTB SQL Editor and Browser");
        
    }
    
    /**
     * If a string is on the system clipboard, this method returns it, 
     * otherwise it returns null.
     *
     * @return A String with the contents of the systems clipboard
     */
    public String getClipboard() {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
    
        try {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                return text;
            }
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
        return null;
    }
    
    /**
     * This method writes a string to the system clipboard.
     *
     * @param str A String to write to the system clipboard
     */
    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    /** 
     * Exit the Application 
     *
     * @param evt A WindowEvent object
     */    
    
    private void showConnectionDialog() {
        ConnectDialog dlg = new ConnectDialog(null, this, true);
        dlg.setSize(dlg.getSize().width+100, dlg.getSize().height);
        dlg.setProperties(properties);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = dlg.getSize();
        
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        dlg.setLocation( (screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        
        dlg.show();
    }       
    
    /**
     * The main method to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        boolean packFrame = false;
        
        jSql app = new jSql();
        
        try {
            app.loadProperties();
            app.loadQueries("recentQuery_");
        } catch (Exception e) {
            app.handleException(e);
            System.exit(-1);
        }

        if (packFrame) {
            app.pack();
        } else {
            app.validate();
        }
        
        JFrame theFrame = new JFrame("jSQL");

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        app.setSize(screenSize.width - 200, screenSize.height - 200);
        Dimension frameSize = app.getSize();
        
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        app.setLocation( (screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        app.setVisible(true);

        app.showConnectionDialog();
        
        /*      
        LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        
        for (int i = 0; i < info.length; i++) {
            System.out.println(info[i].getName());
        }
        */        
    }
    
    /**
     * Get the recent queries that were successfully executed.
     *
     * @return An ArrayList of the recent queries
     */
    public ArrayList getRecentQueries() {
        return recentQueries;
    }
    
    /**
     * Set the active connection id, which is a String identifier.
     *
     * @param id The unique id of the active connection
     */
    public void setActiveConnectionId(String id) {
        activeConnectionId = id;
    }
    
    public String getActiveConnectionId() {
        return activeConnectionId;
    }
    
    /**
     * Create a new connection.
     *
     * @param name The name of the connection
     * @param driver The JDBC driver for the connection
     * @param url The JDBC URL of the connection
     * @param user The user name to log in
     * @param password The password of the user
     *
     * @throws Exception An Exception if an error occurs
     */
    public void addNewConnection(String name, String driver, String url,
                                 String user, String password) 
        throws Exception {
        try {                                     
            connectionManager.createPool(name, driver, url, user, password);
            setActiveConnectionId(name);
            
            // remove all the connection properties
            int i = -1;
            int foundIndex = -1;
            while (properties.containsKey("name_" + ++i)) {
                if (name.equalsIgnoreCase((String)properties.get("name_" + i))) {
                    foundIndex = i;
                }
            }
            
            if (foundIndex == -1) {
                foundIndex = i;
            }
            
            properties.setProperty("name_" + foundIndex, name);
            properties.setProperty("driver_" + foundIndex, driver);
            properties.setProperty("url_" + foundIndex, url);
            properties.setProperty("userid_" + foundIndex, user);
            properties.setProperty("password_" + foundIndex, password);
            
            addNewSqlQueryFrame();

            // write properties file
            try {        
                String file = USER_HOME + FILE_SEPARATOR + PROPERTIES_FILE;
                properties.store(new FileOutputStream(file), null);
            } catch (IOException ioe) {
                handleException(ioe);
            }            
            
        } catch (IOException ioe) {
            throw ioe;
        }
    }
    
    public void addNewSqlQueryFrame() {
        debug("New Sql Query Frame - " + getActiveConnectionId());
        SqlQueryFrame s = new SqlQueryFrame(this, getActiveConnectionId(), true, true, true, true);
        
        Dimension screenSize = this.getSize();
        s.setSize(screenSize.width - 200, screenSize.height - 200);
        Dimension frameSize = s.getSize();
        
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        s.setLocation( (screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        this.desktopPane.add(s);
        s.show();
    }
    
    public void addNewDatabaseBrowserFrame(String id) {
        debug("New Database Browser Frame - " + getActiveConnectionId());
        DatabaseBrowserFrame s = new DatabaseBrowserFrame(this, getActiveConnectionId(), true, true, true, true);
        Dimension screenSize = this.getSize();
        s.setSize(screenSize.width - 200, screenSize.height - 200);
        Dimension frameSize = s.getSize();
        
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        s.setLocation( (screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        this.desktopPane.add(s);
        s.show();
    }

    public Connection getConnection(String id) {
        return connectionManager.getConnection(id);
    }
    
    public void returnConnection(String id, Connection conn) {
        connectionManager.returnConnection(id, conn);
    }

    void loadQueries(String pre) {
        try {
            String file = USER_HOME + FILE_SEPARATOR + PROPERTIES_FILE;

            properties.load(new FileInputStream(file));

            boolean done = false;
            int savedQuery = 0;

            while (!done) {
                String query = properties.getProperty(pre + savedQuery);

                if ((query == null) || (query.equals(""))) {
                    done = true;
                } else {
                    // debug(pre + savedQuery + " = " + query);
                    if (pre.compareToIgnoreCase("savedQuery_") == 0) {
                        savedQueries.add(query);
                    } else {
                        recentQueries.add(query);
                    }
                    savedQuery++;
                }
            }
        } catch (IOException e) {
            return;
        }
    }

    public void loadProperties() throws Exception {
        String file = USER_HOME + FILE_SEPARATOR + PROPERTIES_FILE;
        
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new Exception("Unable to load properties file. (" + 
                file + ")");
        }

        String num = properties.getProperty("numberRecentQueries");

        try {
            numberRecentQueries = Integer.parseInt(num);
        } catch (Exception exc) {
            numberRecentQueries = 20;
        }
        
    }

    public void saveConnections(Hashtable connections) {
        boolean found = false;
        int connection = 0;

        // remove all the connection properties
        int i = -1;
        while (properties.containsKey("name_" + ++i)) {
            properties.remove("name_" + i);
            properties.remove("driver_" + i);
            properties.remove("url_" + i);
            properties.remove("userid_" + i);
            properties.remove("password_" + i);
        }

        i = 0;

        // loop through the connections and save them to props
        Enumeration e = connections.keys();
        
        while (e.hasMoreElements()) {
            Hashtable temp = (Hashtable)connections.get(e.nextElement());

            String nameT = (String)temp.get("name");
            String driverT = (String)temp.get("driver");
            String urlT = (String)temp.get("url");
            String useridT = (String)temp.get("userid");
            String passwordT = (String)temp.get("password");
            
            properties.setProperty("name_" + i, nameT);
            properties.setProperty("driver_" + i, driverT);
            properties.setProperty("url_" + i, urlT);
            properties.setProperty("userid_" + i, useridT);
            properties.setProperty("password_" + i, passwordT);
            
            i++;
        }
        
        // Write properties file.
        try {        
            String file = USER_HOME + FILE_SEPARATOR + PROPERTIES_FILE;
            properties.store(new FileOutputStream(file), null);
        } catch (IOException ioe) {
            handleException(ioe);
        }
    }

    public void debug(String text) {
        if (DEBUG_MODE) {
            System.out.println(text);
        }
    }    
    
    public void handleException(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        //e.printStackTrace();
    }
    
    private RDBMSConnectionPoolManager connectionManager = null;
    private String activeConnectionId = null;
    private ArrayList recentQueries = new ArrayList();
    private ArrayList savedQueries = new ArrayList();
    private Properties properties = new Properties();    
    private int numberRecentQueries = 20;
    private int totalConnections = 0;
    
    // -------------------------------------------------------------- Constants
    
    private final boolean DEBUG_MODE = false;
    private final String FILE_SEPARATOR = System.getProperty("file.separator");
    private final String PROPERTIES_FILE = ".jSql_properties";
    private final String USER_HOME = System.getProperty("user.home");
    private final int TILE = 1;
    private final int TILE_HORIZONTALLY = 2;
    private final int TILE_VERTICALLY = 3;
    
    /**
     * 
     */    
    public void cascade(Container container, int xOffset, int yOffset, 
                        int xSize, int ySize) {
        Component components[] = container.getComponents();
        
        if(components != null && components.length > 0) {
        
            for(int k = 0; k < components.length; k++) {
                if((components[k] instanceof JInternalFrame) && !((JInternalFrame)components[k]).isIcon()) {
                    components[k].setLocation(xOffset * k, yOffset * k);
                    components[k].setSize(xSize, ySize);
                    try {
                        ((JInternalFrame)components[k]).setSelected(true);
                    }
                    catch(PropertyVetoException pve) {
                        pve.printStackTrace();
                    }
                }
            }
        }
        container.validate();
        container.repaint(1000L);
    }

    public void cascade(Container container)
    {
        cascade(container, 30, 30, container.getWidth() - 200, container.getHeight() - 200);
    }

    public void tileHorizontally(Container container)
    {
        Component components[] = container.getComponents();
        Rectangle rectangle = container.getBounds();
        double height = rectangle.getHeight();
        double width = rectangle.getWidth();
        double x = rectangle.getX();
        double y = rectangle.getY();
        int nonIconifiedComponents = 0;
        if(components != null && components.length > 0) {
            for(int k = 0; k < components.length; k++)
                if((components[k] instanceof JInternalFrame) && !((JInternalFrame)components[k]).isIcon())
                    nonIconifiedComponents++;

        }
        int heightOfEachComponent = (int)height;
        int widthOfEachComponent = (int)width / nonIconifiedComponents;
        Rectangle tempRect = null;
        if(components != null && components.length > 0)
        {
            int k = 0;
            int counter = 0;
            for(; k < components.length; k++)
                if((components[k] instanceof JInternalFrame) && !((JInternalFrame)components[k]).isIcon())
                {
                    tempRect = new Rectangle(widthOfEachComponent * counter++, (int)y, widthOfEachComponent, heightOfEachComponent);
                    components[k].setBounds(tempRect);
                }

        }
        container.validate();
        container.repaint(1000L);
    }

    public void tile(Container container, int alignment)
    {
        Component components[] = container.getComponents();
        Rectangle rectangle = container.getBounds();
        double height = rectangle.getHeight();
        double width = rectangle.getWidth();
        double x = rectangle.getX();
        double y = rectangle.getY();
        int nonIconifiedComponents = 0;
        if(components != null && components.length > 0)
        {
            for(int k = 0; k < components.length; k++)
                if((components[k] instanceof JInternalFrame) && !((JInternalFrame)components[k]).isIcon())
                    nonIconifiedComponents++;

        }
        int heightOfEachComponent = 0;
        int widthOfEachComponent = 0;
        int noOfComponentsInX = (int)Math.sqrt(nonIconifiedComponents);
        if(alignment == 1)
        {
            heightOfEachComponent = (int)height / (nonIconifiedComponents / noOfComponentsInX);
            widthOfEachComponent = (int)width / noOfComponentsInX;
        } else
        if(alignment == 3)
        {
            heightOfEachComponent = (int)height;
            widthOfEachComponent = (int)width / nonIconifiedComponents;
        } else
        if(alignment == 2)
        {
            heightOfEachComponent = (int)height / nonIconifiedComponents;
            widthOfEachComponent = (int)width;
        }
        Rectangle tempRect = null;
        if(components != null && components.length > 0)
        {
            int k = 0;
            int counter = 0;
            int yLocation = 0;
            int xLocation = 0;
            for(; k < components.length; k++)
                if((components[k] instanceof JInternalFrame) && !((JInternalFrame)components[k]).isIcon())
                {
                    if(alignment == 1)
                    {
                        if(k != 0 && k % noOfComponentsInX == 0)
                        {
                            yLocation++;
                            xLocation = 0;
                        }
                        tempRect = new Rectangle(widthOfEachComponent * xLocation++, heightOfEachComponent * yLocation, widthOfEachComponent, heightOfEachComponent - (int)y);
                    } else
                    if(alignment == 3)
                        tempRect = new Rectangle(widthOfEachComponent * counter++, 0, widthOfEachComponent, heightOfEachComponent - (int)y);
                    else
                    if(alignment == 2)
                        tempRect = new Rectangle((int)x, heightOfEachComponent * counter++, widthOfEachComponent, heightOfEachComponent);
                    components[k].setBounds(tempRect);
                }

        }
        container.validate();
        container.repaint(1000L);
    }
    
    public void disconnect(String connectionId) {
        closeAllWindowsForConnection(desktopPane, connectionId);
        connectionManager.removePool(connectionId);
    }
    
    public void closeAllWindowsForConnection(Container container, String connectionId) {
        Component components[] = container.getComponents();
        if(components != null && components.length > 0) {
            for(int k = 0; k < components.length; k++) {
                if(components[k] instanceof JInternalFrame) {
                    JInternalFrame f = (JInternalFrame)components[k];
                    if(f.getTitle().indexOf(connectionId) > 0) {
                        container.remove(components[k]);
                        try {
                            ((JInternalFrame)components[k]).setClosed(true);
                        } catch(PropertyVetoException pve) {
                            pve.printStackTrace();
                        }  
                    }
                }
            }
        }
        container.validate();
        container.repaint(1000L);
    }

    public void closeAll(Container container)
    {
        Component components[] = container.getComponents();
        if(components != null && components.length > 0)
        {
            for(int k = 0; k < components.length; k++)
                if(components[k] instanceof JInternalFrame)
                {
                    container.remove(components[k]);
                    try
                    {
                        ((JInternalFrame)components[k]).setClosed(true);
                    }
                    catch(PropertyVetoException pve)
                    {
                        pve.printStackTrace();
                    }
                }

        }
        container.removeAll();
        container.validate();
        container.repaint(1000L);
    }

    /**
     * Save a recent query.
     *
     * @param query The query to save
     */
    public void saveRecentQuery(String query) {
        int qryIndex = recentQueries.indexOf(query);

        if (qryIndex == -1) {
            recentQueries.add(query);
        } else {
            recentQueries.remove(qryIndex);
            recentQueries.add(query);
        }

        if (recentQueries.size() > numberRecentQueries) {
            recentQueries.remove(0);
        }
        saveRecentQueries();
    }    
    
    /**
     * Save all of the recent queries to disk.
     */
    void saveRecentQueries() {
        debug("# RECENT QUERIES=" + recentQueries.size());

        int i = -1;

        while (properties.containsKey("recentQuery_" + ++i)) {
            properties.remove("recentQuery_" + i);
        }

        for (i = 0; i < recentQueries.size(); i++) {
            properties.setProperty("recentQuery_" + i, 
                    (String) recentQueries.get(i));
        }

        // write properties file
        try {
            String file = USER_HOME + FILE_SEPARATOR + PROPERTIES_FILE;

            properties.store(new FileOutputStream(file), null);
        } catch (IOException ioe) {
            handleException(ioe);
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    desktopPane = new javax.swing.JDesktopPane();
    menuBar = new javax.swing.JMenuBar();
    connectionMenu = new javax.swing.JMenu();
    connectMenuItem = new javax.swing.JMenuItem();
    disconnectMenuItem = new javax.swing.JMenuItem();
    databaseBrowserMenu = new javax.swing.JMenu();
    dbBrowserNewMenuItem = new javax.swing.JMenuItem();
    dbBrowserCloseMenuItem = new javax.swing.JMenuItem();
    sqlWindowMenu = new javax.swing.JMenu();
    sqlWindowNewMenuItem = new javax.swing.JMenuItem();
    sqlWindowOpenMenuItem = new javax.swing.JMenuItem();
    sqlWindowSaveMenuItem = new javax.swing.JMenuItem();
    sqlWindowExportMenuItem = new javax.swing.JMenuItem();
    sqlWindowCloseMenuItem = new javax.swing.JMenuItem();
    windowsMenu = new javax.swing.JMenu();
    cascadeWindowsMenuItem = new javax.swing.JMenuItem();
    tileWindowsHorizMenuItem = new javax.swing.JMenuItem();
    tileWindowsVertMenuItem = new javax.swing.JMenuItem();
    closeAllWindowsMenuItem = new javax.swing.JMenuItem();
    helpMenu = new javax.swing.JMenu();
    contentMenuItem = new javax.swing.JMenuItem();
    aboutMenuItem = new javax.swing.JMenuItem();

    setClosable(true);
    setIconifiable(true);
    setMaximizable(true);
    setResizable(true);
    setTitle("jSQL");

    desktopPane.setBackground(new java.awt.Color(204, 204, 204));

    connectionMenu.setText("Connection");

    connectMenuItem.setText("Connect");
    connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        connectMenuItemActionPerformed(evt);
      }
    });
    connectionMenu.add(connectMenuItem);

    disconnectMenuItem.setText("Disconnect");
    disconnectMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        disconnectMenuItemActionPerformed(evt);
      }
    });
    connectionMenu.add(disconnectMenuItem);

    menuBar.add(connectionMenu);

    databaseBrowserMenu.setText("Database Browser");

    dbBrowserNewMenuItem.setText("New");
    dbBrowserNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dbBrowserNewMenuItemActionPerformed(evt);
      }
    });
    databaseBrowserMenu.add(dbBrowserNewMenuItem);

    dbBrowserCloseMenuItem.setText("Close");
    dbBrowserCloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dbBrowserCloseMenuItemActionPerformed(evt);
      }
    });
    databaseBrowserMenu.add(dbBrowserCloseMenuItem);

    menuBar.add(databaseBrowserMenu);

    sqlWindowMenu.setText("Sql Editor");

    sqlWindowNewMenuItem.setText("New");
    sqlWindowNewMenuItem.setToolTipText("Create a new Sql Editor");
    sqlWindowNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sqlWindowNewMenuItemActionPerformed(evt);
      }
    });
    sqlWindowMenu.add(sqlWindowNewMenuItem);

    sqlWindowOpenMenuItem.setText("Open");
    sqlWindowOpenMenuItem.setToolTipText("Open a Sql file.");
    sqlWindowOpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sqlWindowOpenMenuItemActionPerformed(evt);
      }
    });
    sqlWindowMenu.add(sqlWindowOpenMenuItem);

    sqlWindowSaveMenuItem.setText("Save");
    sqlWindowSaveMenuItem.setToolTipText("Save the Sql to a file.");
    sqlWindowSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sqlWindowSaveMenuItemActionPerformed(evt);
      }
    });
    sqlWindowMenu.add(sqlWindowSaveMenuItem);

    sqlWindowExportMenuItem.setText("Export");
    sqlWindowExportMenuItem.setToolTipText("Export the Sql query results.");
    sqlWindowExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sqlWindowExportMenuItemActionPerformed(evt);
      }
    });
    sqlWindowMenu.add(sqlWindowExportMenuItem);

    sqlWindowCloseMenuItem.setText("Close");
    sqlWindowCloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sqlWindowCloseMenuItemActionPerformed(evt);
      }
    });
    sqlWindowMenu.add(sqlWindowCloseMenuItem);

    menuBar.add(sqlWindowMenu);

    windowsMenu.setText("Window");

    cascadeWindowsMenuItem.setText("Cascade");
    cascadeWindowsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cascadeWindowsMenuItemActionPerformed(evt);
      }
    });
    windowsMenu.add(cascadeWindowsMenuItem);

    tileWindowsHorizMenuItem.setText("Tile Horizontally");
    tileWindowsHorizMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        tileWindowsHorizMenuItemActionPerformed(evt);
      }
    });
    windowsMenu.add(tileWindowsHorizMenuItem);

    tileWindowsVertMenuItem.setText("Tile Vertically");
    tileWindowsVertMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        tileWindowsVertMenuItemActionPerformed(evt);
      }
    });
    windowsMenu.add(tileWindowsVertMenuItem);

    closeAllWindowsMenuItem.setText("Close All");
    closeAllWindowsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeAllWindowsMenuItemActionPerformed(evt);
      }
    });
    windowsMenu.add(closeAllWindowsMenuItem);

    menuBar.add(windowsMenu);

    helpMenu.setText("Help");

    contentMenuItem.setText("Contents");
    contentMenuItem.setEnabled(false);
    helpMenu.add(contentMenuItem);

    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(desktopPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(desktopPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutDialog dlg = new AboutDialog(null, true);
        dlg.setLocationRelativeTo(this);
        dlg.show();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void closeAllWindowsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAllWindowsMenuItemActionPerformed
        closeAll(desktopPane);
    }//GEN-LAST:event_closeAllWindowsMenuItemActionPerformed

    private void tileWindowsVertMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tileWindowsVertMenuItemActionPerformed
        tile(desktopPane, TILE_VERTICALLY);
    }//GEN-LAST:event_tileWindowsVertMenuItemActionPerformed

    private void tileWindowsHorizMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tileWindowsHorizMenuItemActionPerformed
        tile(desktopPane, TILE_HORIZONTALLY);
    }//GEN-LAST:event_tileWindowsHorizMenuItemActionPerformed

    private void cascadeWindowsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cascadeWindowsMenuItemActionPerformed
        cascade(desktopPane);
    }//GEN-LAST:event_cascadeWindowsMenuItemActionPerformed

    private void sqlWindowCloseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlWindowCloseMenuItemActionPerformed
        desktopPane.getSelectedFrame().dispose();
    }//GEN-LAST:event_sqlWindowCloseMenuItemActionPerformed

    private void sqlWindowExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlWindowExportMenuItemActionPerformed
        if (desktopPane.getSelectedFrame() instanceof SqlQueryFrame) {
            SqlQueryFrame f = (SqlQueryFrame)desktopPane.getSelectedFrame();
            f.exportResults();
        }
    }//GEN-LAST:event_sqlWindowExportMenuItemActionPerformed

    private void sqlWindowSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlWindowSaveMenuItemActionPerformed
        if (desktopPane.getSelectedFrame() instanceof SqlQueryFrame) {
            SqlQueryFrame f = (SqlQueryFrame)desktopPane.getSelectedFrame();
            f.saveQuery();
        }
    }//GEN-LAST:event_sqlWindowSaveMenuItemActionPerformed

    private void sqlWindowOpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlWindowOpenMenuItemActionPerformed
        if (desktopPane.getSelectedFrame() instanceof SqlQueryFrame) {
            SqlQueryFrame f = (SqlQueryFrame)desktopPane.getSelectedFrame();
            f.loadQuery();
        }
    }//GEN-LAST:event_sqlWindowOpenMenuItemActionPerformed

    private void sqlWindowNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlWindowNewMenuItemActionPerformed
        addNewSqlQueryFrame();
    }//GEN-LAST:event_sqlWindowNewMenuItemActionPerformed

    private void dbBrowserCloseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbBrowserCloseMenuItemActionPerformed
        desktopPane.getSelectedFrame().dispose();
    }//GEN-LAST:event_dbBrowserCloseMenuItemActionPerformed

    private void dbBrowserNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbBrowserNewMenuItemActionPerformed
        addNewDatabaseBrowserFrame(getActiveConnectionId());
    }//GEN-LAST:event_dbBrowserNewMenuItemActionPerformed

    private void disconnectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectMenuItemActionPerformed

        if (connectionManager.getConnectionPoolCount() > 0) {
            DisconnectDialog dlg = new DisconnectDialog(null, this, connectionManager.getNames(), true);
            dlg.setSize(400, 400);
            dlg.setLocationRelativeTo(this);
            dlg.show();
        }
 
    }//GEN-LAST:event_disconnectMenuItemActionPerformed

    private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectMenuItemActionPerformed
        showConnectionDialog();
    }//GEN-LAST:event_connectMenuItemActionPerformed
    
    
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem aboutMenuItem;
  private javax.swing.JMenuItem cascadeWindowsMenuItem;
  private javax.swing.JMenuItem closeAllWindowsMenuItem;
  private javax.swing.JMenuItem connectMenuItem;
  private javax.swing.JMenu connectionMenu;
  private javax.swing.JMenuItem contentMenuItem;
  private javax.swing.JMenu databaseBrowserMenu;
  private javax.swing.JMenuItem dbBrowserCloseMenuItem;
  private javax.swing.JMenuItem dbBrowserNewMenuItem;
  private javax.swing.JDesktopPane desktopPane;
  private javax.swing.JMenuItem disconnectMenuItem;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem sqlWindowCloseMenuItem;
  private javax.swing.JMenuItem sqlWindowExportMenuItem;
  private javax.swing.JMenu sqlWindowMenu;
  private javax.swing.JMenuItem sqlWindowNewMenuItem;
  private javax.swing.JMenuItem sqlWindowOpenMenuItem;
  private javax.swing.JMenuItem sqlWindowSaveMenuItem;
  private javax.swing.JMenuItem tileWindowsHorizMenuItem;
  private javax.swing.JMenuItem tileWindowsVertMenuItem;
  private javax.swing.JMenu windowsMenu;
  // End of variables declaration//GEN-END:variables
    
}
