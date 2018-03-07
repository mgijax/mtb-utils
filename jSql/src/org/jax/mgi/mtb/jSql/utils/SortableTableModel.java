package org.jax.mgi.mtb.jSql.utils;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


public class SortableTableModel extends AbstractTableModel {
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String[] columnNames = {};
    Vector rows = new Vector();
    ResultSetMetaData metaData;
    int maxRows = 50000;
    private String messageText = null;

    public SortableTableModel(Connection conn) {
        try {
            connection = conn;
        } catch (Exception e) {}
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getMaxRows() {
        return maxRows;
    }
    
private ResultSet processStatement(Statement stmnt,String queryString)  throws SQLException, Exception 
  { 
      // Execute the statement. The boolean return parameter 
      // denotes the kind of result obtained from execution. 
        int rsIndex = 0; 
        ResultSet rsCurrent = null; 
        ResultSet rsNext = null; 
            boolean returnedResultSet = stmnt.execute(queryString); 
             
            while(true) 
            { 
                // Did we get a ResultSet? 
                if(! returnedResultSet ) 
                { 
                     // This might be an update count  
                     int updateCount = stmnt.getUpdateCount(); 

                     if(updateCount == -1) 
                     { 
                        //System.out.println("\nDone handling all results."); 
                        break; 
                     } 

                     // This IS an update count 
                     messageText = "Updated database."; 
                } 
                else 
                { 
                     // This should be a ResultSet 
                     rsCurrent = stmnt.getResultSet(); 
                     //ResultSetMetaData rsmd = rs.getMetaData(); 

                     // Log ResultSet geometry here, so that we may  
                     // see  the number of columns in the ResultSet 
                    // System.out.println("Got ResultSet[" + (++rsIndex) + "]: "  
                                         //     + rsCurrent.getMetaData().getColumnCount() + " columns."); 
//
                     // Do not forget to close the ResultSet and  
                     // release the database resources owned by it! 
                     //  
                     // Since we use the getMoreResults() method to 
                     // obtain the next ResultSet or updateCount, the 
                     // explicit close is not required. However, I do 
                     // recommend closing explicitly at all times to 
                     // eliminate bad driver behavior/implementation. 
                     //  
                     // Check, for instance, the JDBC/ODBC bridge driver here.... 
                    // rs.close(); 
                       //System.out.println("ResultSet Found"); 
                       returnedResultSet = true;     
                } 

                // Get the next ResultSet returned from the Statement. 
                // If there are no more Results, the getMoreResults method 
                // will return false. 
                try {
                
                    if(!stmnt.getMoreResults(Statement.KEEP_CURRENT_RESULT) )//&& )  
                    {                 
                        /*if(stmnt.getUpdateCount() == -1) 
                        { 
                            System.out.println("No More results are there."); 
                            break; 
                        } 
                        else                 
                        */ 
                            returnedResultSet = false;                 
                    } 
                } catch (Exception e) {
                    //System.out.println("WARNING: " + e.toString());
                    break;
                }
            } 
      return rsCurrent; 
  }

    public String getMessage() {
        return messageText;
    }

    public boolean executeQuery(ResultSet rs) throws SQLException, Exception {
        messageText = null;
        if (connection == null) {
            messageText = "There is no active connections.";
            return false;
        }

        try {
            resultSet = rs;
            metaData = resultSet.getMetaData();

            int numberOfColumns = metaData.getColumnCount();

            columnNames = new String[numberOfColumns];
            // Get the column names and cache them.
            // Then we can close the connection.
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames[column] = metaData.getColumnLabel(column + 1);
            }

            // Get all rows.
            rows = new Vector();
            while (resultSet.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= getColumnCount(); i++) {
                    //System.out.println(resultSet.getObject(i));
                    newRow.addElement(resultSet.getObject(i));
                }
                rows.addElement(newRow);
            }
            // close(); Need to copy the metaData, bug in jdbc:odbc driver.
            fireTableChanged(null); // Tell the listeners a new table has arrived.
        } catch (SQLException ex) {
            
            //ex.printStackTrace();
            throw ex;
        } catch (Exception exc) {
            throw exc;
        }

        return true;
    }

    public boolean executeQuery(String query) throws SQLException, Exception {
        //System.out.println("QUERY="+query);
            String dbname = "";
            
        try {
            String temp = connection.getMetaData().getDatabaseProductName();
            
            if (temp != null) {
                dbname = temp;
            }
        } catch (Exception tempE) {
            
        }
            
            //System.out.println("dbName="+dbname);

        try {
            if (dbname.toLowerCase().indexOf("oracle") > 0) {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
            } else {
                statement = connection.createStatement();
            }
            // set the maximum number of rows
            statement.setMaxRows(maxRows);

            
            ResultSet rs = null;

            if (dbname.toLowerCase().indexOf("oracle") > 0) {
                rs = processStatement(statement, query);
            }
            else if((dbname.toLowerCase().equals("mysql")) 
               &&(query.trim().startsWith("update")
                  || query.trim().startsWith("insert")
                  || query.trim().startsWith("delete")
                  || query.trim().startsWith("create")))
            {
            
                statement.executeUpdate(query); 
            
                      
            } else {
                rs = statement.executeQuery(query);
            }

            if(rs == null) {
                //System.out.println("RESULT SET IS NULL");
                return true;
            }
            
            executeQuery(rs);
            
        } catch (SQLException ex) {
            
            //ex.printStackTrace();
            throw ex;
        } catch (Exception exc) {
            throw exc;
        }

        return true;
    }

    public void close() throws SQLException {
        // System.out.println("Closing db connection");
        resultSet.close();
        statement.close();
        //connection.close();
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // Implementation of the TableModel Interface
    //
    // ////////////////////////////////////////////////////////////////////////

    // MetaData

    public String getColumnName(int column) {
        if (columnNames[column] != null) {
            return columnNames[column];
        } else {
            return "";
        }
    }

    public Class getColumnClass(int column) {
        int type;

        try {
            type = metaData.getColumnType(column + 1);
        } catch (SQLException e) {
            return super.getColumnClass(column);
        }

        switch (type) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return String.class;

        case Types.BIT:
            return Boolean.class;

        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.NUMERIC:
            return Integer.class;

        case Types.BIGINT:
            return Long.class;

        case Types.FLOAT:
        case Types.DOUBLE:
            return Double.class;

        case Types.DATE:
            return java.sql.Date.class;

        default:
            return Object.class;
        }
    }

    public boolean isCellEditable(int row, int column) {
        try {
            return metaData.isWritable(column + 1);
        } catch (SQLException e) {
            return false;
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    // Data methods

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int aRow, int aColumn) {
        Vector row = (Vector) rows.elementAt(aRow);

        return row.elementAt(aColumn);
    }

    public String dbRepresentation(int column, Object value) {
        int type;

        if (value == null) {
            return "null";
        }

        try {
            type = metaData.getColumnType(column + 1);
        } catch (SQLException e) {
            return value.toString();
        }

        switch (type) {
        case Types.INTEGER:
        case Types.DOUBLE:
        case Types.FLOAT:
            return value.toString();

        case Types.BIT:
            return ((Boolean) value).booleanValue() ? "1" : "0";

        case Types.DATE:
            return value.toString(); // This will need some conversion.

        default:
            return "\"" + value.toString() + "\"";
        }

    }

    public void setValueAt(Object value, int row, int column) {
        try {
            String tableName = metaData.getTableName(column + 1);

            // Some of the drivers seem buggy, tableName should not be null.
            if (tableName == null) {// System.out.println("Table name returned null.");
            }
            String columnName = getColumnName(column);
            String query = "update " + tableName + " set " + columnName + " = "
                    + dbRepresentation(column, value) + " where ";

            // We don't have a model of the schema so we don't know the
            // primary keys or which columns to lock on. To demonstrate
            // that editing is possible, we'll just lock on everything.
            for (int col = 0; col < getColumnCount(); col++) {
                String colName = getColumnName(col);

                if (colName.equals("")) {
                    continue;
                }
                if (col != 0) {
                    query = query + " and ";
                }
                query = query + colName + " = "
                        + dbRepresentation(col, getValueAt(row, col));
            }
            // System.out.println(query);
            // System.out.println("Not sending update to database");
            // statement.executeQuery(query);
        } catch (SQLException e) {// e.printStackTrace();
            // System.err.println("Update failed");
        }
        Vector dataRow = (Vector) rows.elementAt(row);

        dataRow.setElementAt(value, column);

    }
}
