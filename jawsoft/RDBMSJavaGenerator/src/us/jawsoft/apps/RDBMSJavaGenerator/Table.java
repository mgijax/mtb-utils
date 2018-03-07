package us.jawsoft.apps.RDBMSJavaGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Table {
    
    private Map colHash = new HashMap();
    private Map manyToManyHash = new HashMap();
    private List cols = new ArrayList();
    private List priKey = new ArrayList();
    private List impKey = new ArrayList();
    private String catalog;
    private String schema;
    private String name;
    private String type;
    private String remarks;
    
    public boolean isRelationTable() {
        return impKey.size() > 1;
    }
    
    /**
     * Tells whether if one of this table's columns (imported key)
     * points to one of the otherTable's pk.
     */
    public boolean relationConnectsTo(Table otherTable) {
        if (this.equals(otherTable)) {
            return false;
        }
        
        for (int i = 0; i < impKey.size(); i++) {
            Column c = (Column) impKey.get(i);
            if (c.getTableName().equals(otherTable.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return, beside the passed table, the tables this table points to.
     */
    public Table[] linkedTables(Database pDatabase, Table pTable) {
        List pVector = new ArrayList();
        
        for (int iIndex = 0; iIndex < impKey.size(); iIndex++) {
            Column pColumn = (Column) impKey.get(iIndex);
            if (!pColumn.getTableName().equals(pTable.getName())) {
                Table pTableToAdd = pDatabase.getTable(pColumn.getTableName());
                if (!pVector.contains(pTableToAdd)) {
                    pVector.add(pTableToAdd);
                }
            }
        }
        Table pReturn[] = new Table[pVector.size()];
        for (int i = 0; i < pReturn.length; i++) {
            pReturn[i] = (Table)pVector.get(i);
        }
        return pReturn;
    }
    
    /**
     * Return the imported key pointing to the passed table.
     */
    public Column getForeignKeyFor(Table pTable) {
        for (int iIndex = 0; iIndex < impKey.size(); iIndex++) {
            Column pColumn = (Column) impKey.get(iIndex);
            if (pColumn.getTableName().equals(pTable.getName())) {
                return pColumn;
            }
        }
        return null;
    }
    
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    public void setSchema(String schema) {
        this.schema = schema;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getCatalog() {
        return catalog;
    }
    public String getSchema() {
        return schema;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getRemarks() {
        return remarks;
    }
    
    public Column[] getColumns() {
        Column list[] = new Column[cols.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = (Column)cols.get(i);
        }
        return list;
    }
    
    public Column getColumn(String name) {
        return (Column) colHash.get(name.toLowerCase());
    }
    
    public void addColumn(Column column) {
        colHash.put(column.getName().toLowerCase(), column);
        cols.add(column);
    }
    
    public void removeColumn(Column column) {
        cols.remove(column);
        colHash.remove(column.getName().toLowerCase());
    }
    
    public Column[] getPrimaryKeys() {
        Column list[] = new Column[priKey.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = (Column)priKey.get(i);
        }
        return list;
    }
    
    public void addPrimaryKey(Column column) {
        priKey.add(column);
        column.isPrimaryKey(true);
    }
    
    public Column[] getImportedKeys() {
        Column list[] = new Column[impKey.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = (Column)impKey.get(i);
        }
        return list;
    }
    
    public void addImportedKey(Column column) {
        impKey.add(column);
        Column myColumn = getColumn(column.getForeignKeyColName());
        myColumn.setPointsTo(column);
    }
    
    /**
     * Returns a 2-D array of the keys in this table that form a many
     * to many relationship.
     * <br>
     * The size of the first dimension is based on the number of
     * unique tables that are being managed. The second dimension
     * is always 2 elements. The first element is the column in the
     * relationship table itself. The second column is the primary
     * key column in the target table.
     */
    public Column[][] getManyToManyKeys() {
        //         // it matters only if we have 2 entries.
        //         if (manyToManyHash.size()<=1) {
        //             return new Column[0][0];
        //         }
        
        Column list[][] = new Column[manyToManyHash.size()][2];
        int i = 0;
        for (Iterator it = manyToManyHash.keySet().iterator(); it.hasNext(); i++) {
            Column fk = (Column) it.next();
            Column pk = (Column) manyToManyHash.get(fk);
            list[i][0] = fk;
            list[i][1] = pk;
        }
        return list;
    }
    
    
    public void addManyToManyKey(Column fk, Column pk) {
        manyToManyHash.put(fk, pk);
    }
}