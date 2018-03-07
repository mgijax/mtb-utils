package us.jawsoft.apps.RDBMSJavaGenerator;
import java.util.ArrayList;

public class PreparedManagerWriter extends CodeWriter {
    protected static final String POST_INSERT_BLOCK = "postinsert";
    protected static final String PRE_INSERT_BLOCK  = "preinsert";
    protected static final String POST_UPDATE_BLOCK = "postupdate";
    protected static final String PRE_UPDATE_BLOCK  = "preupdate";

    public boolean isStringInArrayList(ArrayList pArrayList, String strValue) {
        for(int iIndex = 0; iIndex < pArrayList.size(); iIndex ++) {
            String strP = (String) pArrayList.get(iIndex);
            if (strP.equalsIgnoreCase(strValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A fairly monolithic method that writes out the Manager class for
     * a specific table.
     * <br>
     * The PreparedManagerWriter is a bit more sophisticated than the
     * DynamicManagerWriter because it provides a larger number of load
     * methods (one per exported foreign key) and the ability to pass in
     * a Connection pObject to methods (which enables transactions to
     * span multiple method invocations).
     */
    protected void writeManager() throws Exception {
        // Check if primary key exists for this table
        // If no primary key, then we can't manage it..
        String coreClass = generateCoreClassName();

        // Resolve class and package
        className = generateManagerClassName();
        String beanClass = generateBeanClassName();
        pkg = basePackage; // + "." + convertName(table.getName()).toLowerCase();

        // import keys
        Column impKeys[] = table.getImportedKeys();
        Column pk[] = table.getPrimaryKeys();
        Column cols[] = table.getColumns();
        // Setup the PrintWriter and read in existing Java source file
        initWriter();

        // Start class
        indent(0, "package " + pkg + ";");
        writer.println();

        indent(0, "import java.util.*;");
        indent(0, "import java.sql.*;");
        for(int i = 0; i < arrImports.length; i++) {
            indent(0, "import " + arrImports[i] + ";");
        }
        //indent(0, "import java.util.*;");
        indent(0, userCode.getBlock(this.IMPORT_BLOCK));

        writer.println();
        indent(0, "/**");
        indent(0, " * Handles database calls for the " + table.getName() + " table.");
        if (table.getRemarks() != null && table.getRemarks().length()>0)
            indent(1, " * Remarks: " + table.getRemarks());
        indent(0, " */");
        indent(0, "public class " + className);
        indent(0, userCode.getBlock(this.EXTENDS_BLOCK));
        indent(0, "{");
        indent(0, "");

        // Write the initializer block
        // write out all columns as a separate static int
        indent(0, "");
        indent(0, "    // -------------------------------------------------------------- Constants");
        indent(0, "");

        for(int i = 0; i < cols.length; i++) {
            indent(1, "/**");
            indent(1, " * Column "+  cols[i].getName() + " of type " +
                    cols[i].getJavaTypeAsTypeName()+ " mapped to " + cols[i].getJavaType() + ".");
            indent(1, " */");
            indent(1, "public static final int ID_" + cols[i].getConstName() +
                    " = " + i + ";");
            indent(1, "public static final int TYPE_" + cols[i].getConstName() +
                    " = " +
                    cols[i].getJavaTypeAsTypeName() + ";");
            indent(1, "public static final String NAME_" + cols[i].getConstName() +
                    " = \"" +
                    cols[i].getName() + "\";");
            indent(0, "");
        }
        indent(0, "");

        // name of the table
        indent(1, "private static final String TABLE_NAME = \"" + table.getName() + "\";");
        indent(0, "");

        // write out static array of int -> column name mappings
        indent(1, "/**");
        indent(1, " * Create an array of type string containing all the fields of the " + table.getName() + " table.");
        indent(1, " */");
        indent(1, "private static final String[] FIELD_NAMES = {");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "    " + (i==0 ? "\"" : ",\"") + cols[i].getFullName() + "\"");
        }
        indent(1, "};");
        indent(0, "");

        // write out static array of int -> column type mappings
        indent(1, "/**");
        indent(1, " * Create an array of type containing all the fields of the " + table.getName() + " table.");
        indent(1, " */");
        indent(1, "private static final Object[] FIELD_TYPES = {");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "    " + (i==0 ? "\"" : ",\"") + cols[i].getJavaType() + "\"");
        }
        indent(1, "};");
        indent(0, "");

//         // write out static array of int -> column name mappings
//         indent(1, "/** create an array of string containing all the fields of the " + table.getName() + " table. */");
//         indent(1, "private static final String[] TABLEFIELD_NAMES = ");
//         indent(1, "{");
//         for(int i = 0; i < cols.length; i++)
//         {
//             indent(1, "   " + (i==0 ? "\"" : ",\"") + cols[i].getName() + "\"");
//         }
//         indent(1, "};");
//         indent(1, "");

        // write out all column names as comma separated string
        StringBuffer allFields = new StringBuffer();
        for(int i = 0; i < cols.length; i++) {
            if(i == 0) {
                allFields.append("\"");
            } else {
                allFields.append(CodeWriter.LINE_SEP);
                allFields.append("                            ");
                allFields.append("+ \",");
            }
            allFields.append(cols[i].getFullName());
            allFields.append("\"");
        }
        allFields.append(";");

        indent(1, "/**");
        indent(1, " * Field that contains the comma separated fields of the " + table.getName() + " table.");
        indent(1, " */");
        indent(1, "private static final String ALL_FIELDS = " + allFields);
        indent(0, "");


        indent(0, "");
        indent(0, "    // ----------------------------------------------------- Instance Variables");
        indent(0, "");

        indent(1, "private static " + className +" singleton = new "+className+"();");
        indent(0, "");
        indent(0, "    // --------------------------------------------------------- Public Methods");
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Get the " + generateManagerClassName(table.getName()) + " singleton.");
        indent(1, " *");
        indent(1, " * @return " + generateManagerClassName(table.getName()) + " ");
        indent(1, " */");
        indent(1, "synchronized public static " + generateManagerClassName(table.getName()) + " getInstance() {");
        indent(1, "    return singleton;");
        indent(1, "}");
        indent(0, "");

        indent(1, "/**");
        indent(1, " * Sets your own " + generateManagerClassName(table.getName()) + " instance.");
        indent(1, " <br>");
        indent(1, " * This is optional, by default we provide it for you.");
        indent(1, " */");
        indent(1, "synchronized public static void setInstance("+ generateManagerClassName(table.getName())+" instance) {");
        indent(1, "    singleton = instance;");
        indent(1, "}");
        indent(0, "");

        indent(0, "");
        indent(1, "/**");
        indent(1, " * Creates a new " + beanClass + " instance.");
        indent(1, " *");
        indent(1, " * @return the new " + beanClass + " ");
        indent(1, " */");
        indent(1, "public " + beanClass + " create"+beanClass+"() {");
        indent(1, "    return new "+beanClass+"();");
        indent(1, "}");
        indent(0, "");

        // setup primary key strings
        StringBuffer keys = new StringBuffer();
        for(int i = 0; i < pk.length; i++) {
            if(i != 0) {
                keys.append(", ");
            }

            keys.append(pk[i].getJavaType());
            keys.append(' ');
            keys.append(getVarName(pk[i]));
        }

        String noWhereSelect = "SELECT \" + ALL_FIELDS + \" FROM " + table.getName();
        String baseSelect = noWhereSelect + " WHERE ";
        StringBuffer sql = new StringBuffer(baseSelect);

        if (pk.length != 0) {
            indent(1, "//-------------------------------------------------------------------------");
            indent(1, "// PRIMARY KEY METHODS");
            indent(1, "//-------------------------------------------------------------------------");

            // getInstance
            indent(0, "");
            indent(1, "/**");
            indent(1, " * Loads a " + beanClass + " from the " + table.getName() + " using its key fields.");
            indent(1, " *");
            indent(1, " * @return a unique " + beanClass + " ");
            indent(1, " */");
            for(int i = 0; i < pk.length; i++) {
                if(i > 0) {
                    sql.append(" and ");
                }
                sql.append(pk[i].getFullName());
                sql.append("=?");
            }
            indent(1, "public " + beanClass + " loadByPrimaryKey(" + keys + ") throws SQLException {");
            indent(1, "    Connection c = null;");
            indent(1, "    PreparedStatement ps = null;");
            indent(1, "    try {");
            indent(1, "        c = getConnection();");
            indent(1, "        ps = c.prepareStatement(\"" + sql + "\",");
            indent(1, "                                ResultSet.TYPE_SCROLL_INSENSITIVE,");
            indent(1, "                                ResultSet.CONCUR_READ_ONLY);");
            for(int i = 0; i < pk.length; i++) {
                indent(1, "        " + pk[i].getPreparedStatementMethod(getVarName(pk[i]), i+1));
            }
            indent(1, "        List<" + beanClass + "> pReturn = loadByPreparedStatement(ps);");
            indent(1, "        if (pReturn.size() < 1) {");
            indent(1, "            return null;");
            indent(1, "        } else {");
            indent(1, "            return pReturn.get(0);");
            indent(1, "        }");
            indent(1, "    } finally {");
            indent(1, "        getManager().close(ps);");
            indent(1, "        freeConnection(c);");
            indent(1, "    }");
            indent(1, "}");
            indent(0, "");
        }

        // deleteByKey
        sql.setLength(0);
        sql.append("DELETE from ");
        sql.append(table.getName());
        sql.append(" WHERE ");
        for(int i = 0; i < pk.length; i++) {
            if(i > 0)
                sql.append(" and ");
            sql.append(pk[i].getFullName());
            sql.append("=?");
        }

        if (pk.length != 0) {
            indent(1, "/**");
            indent(1, " * Deletes rows according to its keys.");
            indent(1, " *");
            indent(1, " * @return the number of deleted rows");
            indent(1, " */");
            indent(1, "public int deleteByPrimaryKey(" + keys + ") throws SQLException {");
            indent(1, "    Connection c = null;");
            indent(1, "    PreparedStatement ps = null;");
            indent(1, "    try {");
            indent(1, "        c = getConnection();");
            indent(1, "        ps = c.prepareStatement(\"" + sql + "\",");
            indent(1, "                                ResultSet.TYPE_SCROLL_INSENSITIVE,");
            indent(1, "                                ResultSet.CONCUR_READ_ONLY);");
            for(int i = 0; i < pk.length; i++) {
                indent(1, "        " + pk[i].getPreparedStatementMethod(getVarName(pk[i]), i+1));
            }
            indent(1, "        return ps.executeUpdate();");
            indent(1, "    } finally {");
            indent(1, "        getManager().close(ps);");
            indent(1, "        freeConnection(c);");
            indent(1, "    }");
            indent(1, "}");
            indent(0, "");
        }

        ArrayList pImportedKeys = new ArrayList();
        for(int i = 0; i < impKeys.length; i++) {
            if (i==0) {
                indent(1, "");
                indent(1, "");
                indent(1, "//-------------------------------------------------------------------------");
                indent(1, "// FOREIGN KEY METHODS");
                indent(1, "//-------------------------------------------------------------------------");
            }

            sql.setLength(0);
            sql.append(baseSelect);
            sql.append(impKeys[i].getForeignKeyColName()); // pointer name
            sql.append("=?");

            if (isStringInArrayList(pImportedKeys, impKeys[i].getForeignKeyColName()))
                continue;

            pImportedKeys.add(impKeys[i].getForeignKeyColName());

            String methodName = "loadBy" +   convertName(impKeys[i].getForeignKeyColName());
            String deleteMethodName = "deleteBy" +   convertName(impKeys[i].getForeignKeyColName());

            // loadByKey
            indent(0, "");
            indent(1, "/**");
            indent(1, " * Loads a List of " + beanClass + " objects from the " + table.getName() + " table using its "
                    + impKeys[i].getForeignKeyColName() + " field.");
            indent(1, " *");
            indent(1, " * @return a List of " + beanClass + " objects");
            indent(1, " */");
            indent(1, "public List<" + beanClass + "> " + methodName + "(" + impKeys[i].getJavaType() + " value) ");
            indent(1, "    throws SQLException {");
            indent(1, "    Connection c = null;");
            indent(1, "    PreparedStatement ps = null;");
            indent(1, "    try {");
            indent(1, "        c = getConnection();");
            indent(1, "        ps = c.prepareStatement(\"" + sql + "\",");
            indent(1, "                                ResultSet.TYPE_SCROLL_INSENSITIVE,");
            indent(1, "                                ResultSet.CONCUR_READ_ONLY);");
            indent(1, "        " + impKeys[i].getPreparedStatementMethod("value", 1));
            indent(1, "        return loadByPreparedStatement(ps);");
            indent(1, "    } finally {");
            indent(1, "        getManager().close(ps);");
            indent(1, "        freeConnection(c);");
            indent(1, "    }");
            indent(1, "}");
            indent(0, "");

            // If we can load we can also delete by...
            //
            String delSQL="DELETE FROM " + table.getName() + " WHERE " + impKeys[i].getForeignKeyColName() + "=?";

            indent(0, "");
            indent(1, "/**");
            indent(1, " * Deletes from the " + table.getName() + " table by " + impKeys[i].getForeignKeyColName() + " field.");
            indent(1, " *");
            indent(1, " * @param value the key value to seek");
            indent(1, " * @return the number of rows deleted");
            indent(1, " */");
            indent(1, "public int " + deleteMethodName + "(" + impKeys[i].getJavaType() + " value)");
            indent(1, "    throws SQLException {");
            indent(1, "    Connection c = null;");
            indent(1, "    PreparedStatement ps = null;");
            indent(1, "    try {");
            indent(1, "        c = getConnection();");
            indent(1, "        ps = c.prepareStatement(\"" + delSQL + "\");");
            indent(1, "        " + impKeys[i].getPreparedStatementMethod("value", 1));
            indent(1, "        return ps.executeUpdate();");
            indent(1, "    } finally {");
            indent(1, "        getManager().close(ps);");
            indent(1, "        freeConnection(c);");
            indent(1, "    }");
            indent(1, "}");
            indent(0, "");
        }

        ArrayList pLoadBy = new ArrayList();
        for(int i = 0; i < impKeys.length; i++) {
            if (i==0) {
                indent(0, "");
                indent(1, "//-------------------------------------------------------------------------");
                indent(1, "// GET/SET FOREIGN KEY BEAN METHOD");
                indent(1, "//-------------------------------------------------------------------------");
            }

            String importedClass = generateBeanClassName(impKeys[i].getTableName());
            String importedClassManager = generateManagerClassName(impKeys[i].getTableName());

            if (pLoadBy.contains(importedClass)) {
                continue;
            }
            pLoadBy.add(importedClass);

            System.out.println(impKeys[i].getForeignKeyTabName() + "." + impKeys[i].getForeignKeyColName() + " -> " + impKeys[i].getTableName() + "." + impKeys[i].getName() );
            Column pForeignColumn = impKeys[i].getForeignColumn();

            // get foreign Class
            indent(1, "/**");
            indent(1, " * Retrieves the " + importedClass + " object from the " + table.getName() +
                    "." + impKeys[i].getName() + " field.");
            indent(1, " *");
            indent(1, " * @param pObject the " + beanClass + " ");
            indent(1, " * @return the associated " + importedClass + " pObject");
            indent(1, " */");
            indent(1, "public " + importedClass + " get" + importedClass+ "(" + beanClass + " pObject) ");
            indent(1, "throws SQLException {");
            indent(1, "    " + importedClass + " other = " + importedClassManager +
                    ".getInstance().create"+importedClass+"();");
            indent(1, "    other." + getSetMethod(impKeys[i]) +
                    "(pObject."+getGetMethod(impKeys[i].getForeignColumn()) +"());");
            indent(1, "    return " + generateManagerClassName(impKeys[i].getTableName()) +
                    ".getInstance().loadUniqueUsingTemplate(other);");
            indent(1, "}");
            indent(0, "");

            // set foreign key object
            indent(1, "/**");
            indent(1, " * Associates the " + beanClass + " object to the " + importedClass + " object.");
            indent(1, " *");
            indent(1, " * @param pObject the " + beanClass + " object to use");
            indent(1, " * @param pObjectToBeSet the " + importedClass + " object to associate to the " + beanClass + " ");
            indent(1, " * @return the associated " + importedClass + " pObject");
            indent(1, " */");
            indent(1, "// SET IMPORTED");
            indent(1, "public " + beanClass + " set" + importedClass+ "(" + beanClass + " pObject," + importedClass + " pObjectToBeSet) {");
            indent(1, "    pObject." + getSetMethod(pForeignColumn) + "(pObjectToBeSet." + getGetMethod(impKeys[i]) + "());");
            indent(1, "    return pObject;");
            indent(1, "}");
            indent(0, "");
        }
        pLoadBy.clear();

//         indent(1, "");
//         indent(1, "");
//         indent(1, "//////////////////////////////////////");
//         indent(1, "// GET/SET IMPORTED");
//         indent(1, "//////////////////////////////////////");

//         // write load methods for all many-to-many relationships
//         Column manyToMany[][] = table.getManyToManyKeys();
//         for(int i = 0; i < manyToMany.length; i++) {
//             // This is the key in the cross-ref table
//             Column fk = manyToMany[i][0];

//             // This is the key in our table (probably our primary key)
//             Column mmPk = manyToMany[i][1];

//             // Get the other columns in the cross ref table
//             Table fkTable = db.getTable(fk.getTableName());

//             sql.setLength(0);
//             sql.append(noWhereSelect);
//             sql.append(", ");
//             sql.append(fkTable.getName());
//             sql.append(" WHERE ");
//             sql.append(table.getName() + "." + mmPk.getName() + "=");
//             sql.append(fk.getTableName() + "." + fk.getName());

//             Column fkCol[] = fkTable.getColumns();
//             StringBuffer otherCols = null;
//             StringBuffer argList = null;
//             StringBuffer argNames = null;
//             for(int x = 0; x < fkCol.length; x++) {
//                 if(!fkCol[x].getName().equals(fk.getName())) {
//                     sql.append(" AND ");
//                     sql.append(fkTable.getName() + "." + fkCol[x].getName());
//                     sql.append("=?");

//                     if(argList == null) argList = new StringBuffer();
//                     else                argList.append(", ");
//                     argList.append(getJavaType(fkCol[x]) + " " +
//                                    convertName(fkCol[x].getName()));

//                     if(argNames == null) argNames = new StringBuffer();
//                     else                 argNames.append(", ");
//                     argNames.append(convertName(fkCol[x].getName()));

//                     if(otherCols == null) otherCols = new StringBuffer(fkTable.getName() + "_");
//                     else                  otherCols.append("_and_");
//                     otherCols.append(fkCol[x].getName());
//                 }
//             }

//             String methodName = "loadBy" + convertName(otherCols.toString());

//             indent(1, "//36");
//             indent(1, "public " + beanClass + "[] " + methodName + "(" + argList + ") throws SQLException");
//             indent(1, "{");
//             indent(1, "    Connection c = null;");
//             indent(1, "    PreparedStatement ps = null;");
//             indent(1, "    try ");
//             indent(1, "    {");
//             indent(1, "        c = getConnection();");
//             indent(1, "        ps = c.prepareStatement(\"" + sql + "\",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
//             int z = 1;
//             for(int x = 0; x < fkCol.length; x++) {
//                 if(!fkCol[x].getName().equals(fk.getName())) {
//                     indent(1, "        " + getPreparedStatementMethod(fkCol[x], convertName(fkCol[x].getName()), z));
//                     z++;
//                 }
//             }
//             indent(1, "        return loadByPreparedStatement(ps);");

//             indent(1, "    }");
//             indent(1, "    finally");
//             indent(1, "    {");
//             indent(1, "        getManager().close(ps);");
//             indent(1, "        freeConnection(c);");
//             indent(1, "    }");
//             indent(1, "}");
//             indent(1, "");
//         }

        indent(0, "");
        indent(0, "");
        indent(1, "//-------------------------------------------------------------------------");
        indent(1, "// GET/SET FOREIGN KEY BEAN METHOD");
        indent(1, "//-------------------------------------------------------------------------");

        // loadAll
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Loads all the rows from " + table.getName() + ".");
        indent(1, " *");
        indent(1, " * @return a List of " + className + " objects");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadAll() throws SQLException {");
        indent(1, "    return loadAll(1, -1);");
        indent(1, "}");
        indent(0, "");
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Loads the given number of rows from " + table.getName() + ", given the start row.");
        indent(1, " *");
        indent(1, " * @param startRow the start row to be used (first row = 1, last row = -1)");
        indent(1, " * @param numRows the number of rows to be retrieved (all rows = a negative number)");
        indent(1, " * @return a List of " + className + " objects");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadAll(int startRow, int numRows) throws SQLException {");
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        indent(1, "    try {");
        indent(1, "        c = getConnection();");
        indent(1, "        ps = c.prepareStatement(\"" + noWhereSelect + "\",");
        indent(1, "                                ResultSet.TYPE_SCROLL_INSENSITIVE,");
        indent(1, "                                ResultSet.CONCUR_READ_ONLY);");
        indent(1, "        return loadByPreparedStatement(ps, null, startRow, numRows);");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");
        indent(0, "");

        indent(1, "/**");
        indent(1, " * Retrieves a List of " + beanClass + " objects given a sql 'where' clause.");
        indent(1, " *");
        indent(1, " * @param where the sql 'where' clause");
        indent(1, " * @return the resulting " + beanClass + " table ");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadByWhere(String where) throws SQLException {");
        indent(1, "    return loadByWhere(where, null);");
        indent(1, "}");
        indent(0, "");

        indent(1, "/**");
        indent(1, " * Retrieves a List of " + beanClass + " objects given a sql where clause, and a list of fields.");
        indent(1, " * It is up to you to pass the 'WHERE' in your where clausis.");
        indent(1, " *");
        indent(1, " * @param where the sql 'where' clause");
        indent(1, " * @param fieldList table of the field's associated constants");
        indent(1, " * @return the resulting " + beanClass + " table ");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadByWhere(String where, int[] fieldList) ");
        indent(1, "    throws SQLException {");
        indent(1, "    return loadByWhere(where, null, 1, -1);");
        indent(1, "}");
        indent(0, "");

        indent(1, "/**");
        indent(1, " * Retrieves a List of " + beanClass + " objects given a sql where clause and a list of fields, and startRow and numRows.");
        indent(1, " * It is up to you to pass the 'WHERE' in your where clausis.");
        indent(1, " *");
        indent(1, " * @param where the sql 'where' clause");
        indent(1, " * @param startRow the start row to be used (first row = 1, last row = -1)");
        indent(1, " * @param numRows the number of rows to be retrieved (all rows = a negative number)");
        indent(1, " * @param fieldList table of the field's associated constants");
        indent(1, " * @return the resulting " + beanClass + " table ");
        indent(1, " */");

        String t = "public List<" + beanClass + "> loadByWhere(String where, ";
        indent(1, t);
        int startPos = t.indexOf('(');
        String pad = padLeft(" ", startPos, ' ');
        indent(1, pad + "int[] fieldList, ");
        indent(1, pad + "int startRow, ");
        indent(1, pad + "int numRows) ");
        indent(1, "    throws SQLException {");
        indent(1, "    String sql = null;");
        indent(1, "    if(fieldList == null) {");
        indent(1, "        sql = \"select \" + ALL_FIELDS + \" from " + table.getName() + " \" + where;");
        indent(1, "    } else {");
        indent(1, "        StringBuffer buff = new StringBuffer(128);");
        indent(1, "        buff.append(\"select \");");
        indent(1, "        for(int i = 0; i < fieldList.length; i++) {");
        indent(1, "            if(i != 0) {");
        indent(1, "                buff.append(',');");
        indent(1, "            }");
        indent(1, "            buff.append(FIELD_NAMES[fieldList[i]]);");
        indent(1, "        }");
        indent(1, "        buff.append(\" from " + table.getName() + " \");");
        indent(1, "        buff.append(where);");
        indent(1, "        sql = buff.toString();");
        indent(1, "        buff = null;");
        indent(1, "    }");
        indent(1, "    Connection c = null;");
        indent(1, "    Statement pStatement = null;");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    List<" + beanClass + "> v = new ArrayList<" + beanClass + ">();");
        indent(1, "    try {");
        indent(1, "        c = getConnection();");
        indent(1, "        pStatement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
        indent(1, "        rs =  pStatement.executeQuery(sql);");
        indent(1, "        int count = 0;");
        indent(1, "        if (rs.absolute(startRow) && numRows!=0) {");
        indent(1, "            do  {");
        indent(1, "                if(fieldList == null) {");
        indent(1, "                    v.add(decodeRow(rs));");
        indent(1, "                } else {");
        indent(1, "                    v.add(decodeRow(rs, fieldList));");
        indent(1, "                }");
        indent(1, "                count++;");
        indent(1, "            } while ( (count<numRows||numRows<0) && rs.next() );");
        indent(1, "        }");
        indent(0, "");
        indent(1, "    } finally {");
        //indent(1, "        if (v != null) { v.clear(); }");
        indent(1, "        getManager().close(pStatement, rs);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "    return v;");
        indent(1, "}");
        indent(0, "");

        indent(0, "");
        indent(1, "/**");
        indent(1, " * Deletes all rows from " + table.getName() + " table.");
        indent(1, " * @return the number of deleted rows.");
        indent(1, " */");
        indent(1, "public int deleteAll() throws SQLException {");
        indent(1, "    return deleteByWhere(\"\");");
        indent(1, "}");
        indent(0, "");
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Deletes rows from the " + table.getName() + " table using a 'where' clause.");
        indent(1, " * It is up to you to pass the 'WHERE' in your where clausis.");
        indent(1, " * <br>Attention, if 'WHERE' is omitted it will delete all records. ");
        indent(1, " *");
        indent(1, " * @param where the sql 'where' clause");
        indent(1, " * @return the number of deleted rows");
        indent(1, " */");
        indent(1, "public int deleteByWhere(String where) throws SQLException {");
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        indent(0, "");
        indent(1, "    try {");
        indent(1, "        c = getConnection();");
        indent(1, "        String delByWhereSQL = \"DELETE FROM " + table.getName() + " \" + where;");
        indent(1, "        ps = c.prepareStatement(delByWhereSQL);");
        indent(1, "        return ps.executeUpdate();");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");
        indent(0, "");

        writeSave();

        indent(0, "");
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Saves a List of " + beanClass + " objects into the database.");
        indent(1, " *");
        indent(1, " * @param pObjects the " + beanClass + " pObject table to be saved");
        indent(1, " * @return the saved " + beanClass + " List.");
        // TODO: BATCH UPDATE
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> save(List<" + beanClass + "> pObjects) throws SQLException {");
        indent(1, "    for (" + beanClass + " bean : pObjects) {");
        indent(1, "        save(bean);");
        indent(1, "    }");
        indent(1, "    return pObjects;");
        indent(1, "}");
        indent(0, "");

        indent(0, "");
        indent(0, "");

        // loadObject
        indent(1, "/**");
        indent(1, " * Loads a unique " + beanClass + " object from a template");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " object to look for");
        indent(1, " * @return the pObject matching the template");
        indent(1, " */");
        indent(1, "public " + beanClass + " loadUniqueUsingTemplate(" + beanClass + " pObject) ");
        indent(1, "    throws SQLException {");
        indent(1, "     List<" + beanClass + "> pReturn = loadUsingTemplate(pObject);");
        indent(1, "     if (pReturn.size() == 0) {");
        indent(1, "         return null;");
        indent(1, "     }");
        indent(1, "     if (pReturn.size() > 1) {");
        indent(1, "         throw new SQLException(\"More than one element !!\");");
        indent(1, "     }");
        indent(1, "     return pReturn.get(0);");
        indent(1, " }");
        indent(0, "");

        // loadObjects
        indent(1, "/**");
        indent(1, " * Loads a List of " + beanClass + " objects from a template one.");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " template to look for");
        indent(1, " * @return all the " + beanClass + " objects matching the template");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadUsingTemplate(" + beanClass + " pObject) ");
        indent(1, "    throws SQLException {");
        indent(1, "    return loadUsingTemplate(pObject, 1, -1);");
        indent(1, "}");
        indent(1, "/**");
        indent(1, " * Loads an array of " + beanClass + " from a template one, given the start row and number of rows.");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " template to look for");
        indent(1, " * @param startRow the start row to be used (first row = 1, last row=-1)");
        indent(1, " * @param numRows the number of rows to be retrieved (all rows = a negative number)");
        indent(1, " * @return all the " + beanClass + " matching the template");
        indent(1, " */");
        String t2 = "public List<" + beanClass + "> loadUsingTemplate(" + beanClass + " pObject,";
        indent(1, t2);
        startPos = t2.indexOf('(');
        pad = padLeft(" ", startPos, ' ');
        indent(1, pad + "int startRow,");
        indent(1, pad + "int numRows)");
        indent(1, "    throws SQLException {");
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        //indent(1, "    StringBuffer where = new StringBuffer(\"\");");
        indent(1, "    StringBuffer _sql = new StringBuffer(\"SELECT \" + ALL_FIELDS + \" from " + table.getName() + " WHERE \");");
        indent(1, "    StringBuffer _sqlWhere = new StringBuffer(\"\");");
        indent(1, "    try {");
        indent(1, "        int _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "");
            indent(1, "         if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "             _dirtyCount ++; ");
            indent(1, "             _sqlWhere.append((_sqlWhere.length() == 0) ? \" \" : \" AND \").append(\"" + cols[i].getName() + "= ?\");");
            indent(1, "         }");
        }
        indent(1, "");
        indent(1, "         if (_dirtyCount == 0) {");
        indent(1, "             throw new SQLException (\"The pObject to look for is invalid : not initialized !\");");
        indent(1, "         }");

        indent(1, "         _sql.append(_sqlWhere);");
        indent(1, "         c = getConnection();");
        indent(1, "         ps = c.prepareStatement(_sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
        indent(1, "         _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "");
            indent(1, "         if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "             " + cols[i].getPreparedStatementMethod(
                    "pObject."+getGetMethod(cols[i])+"()", "++_dirtyCount"));
            indent(1, "         }");
        }
        indent(1, "");
        //indent(1, "         ps.executeQuery();");
        indent(1, "         return loadByPreparedStatement(ps, null, startRow, numRows);");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");

        // delete
        indent(1, "/**");
        indent(1, " * Deletes rows using a " + beanClass + " template.");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " object(s) to be deleted");
        indent(1, " * @return the number of deleted objects");
        indent(1, " */");
        indent(1, "public int deleteUsingTemplate(" + beanClass + " pObject) throws SQLException {");
        if (pk.length == 1) {
            indent(1, "    if (pObject." + getInitializedMethod(pk[0])+ "()) {");
            indent(1, "        return deleteByPrimaryKey(pObject." + getGetMethod(pk[0])+ "());");
            indent(1, "    }");
        }
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        indent(1, "    StringBuffer sql = null;");
        indent(1, "");
        indent(1, "    try  {");
        indent(1, "        sql = new StringBuffer(\"DELETE FROM " + table.getName() + " WHERE \");");
        indent(1, "        int _dirtyAnd = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "        if (pObject." + getInitializedMethod(cols[i]) + "()) {");
            indent(1, "            if (_dirtyAnd > 0) {");
            indent(1, "                sql.append(\" AND \");");
            indent(1, "            }");
            indent(1, "            sql.append(\"" + cols[i].getName() + "\").append(\"=?\");");
            indent(1, "            _dirtyAnd ++;");
            indent(1, "        }");
            indent(1, "");
        }
        indent(1, "        c = getConnection();");
        indent(1, "        ps = c.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
        indent(1, "        int _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "");
            indent(1, "        if (pObject." + getInitializedMethod(cols[i]) + "()) {");
            indent(4, cols[i].getPreparedStatementMethod(
                    "pObject."+getGetMethod(cols[i])+"()",
                    "++_dirtyCount"));
            indent(1, "        }");
        }
        indent(1, "");
        indent(1, "        int _rows = ps.executeUpdate();");
        indent(1, "        return _rows;");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");
        indent(0, "");

        // look for N/N linked tables
        Table[] pRelationTables = db.getRelationTable(table);

        for (int iIndex = 0; iIndex < pRelationTables.length; iIndex ++) {
            Table[] pLinkedTables = pRelationTables[iIndex].linkedTables(db, table);
            for (int iLinkedIndex = 0; iLinkedIndex < pLinkedTables.length; iLinkedIndex ++) {
                if (iLinkedIndex == 0 && iIndex == 0) {
                    indent(1, "");
                    indent(1, "");
                    indent(1, "//-------------------------------------------------------------------------");
                    indent(1, "// MANY TO MANY: LOAD OTHER BEAN VIA JUNCTION TABLE ");
                    indent(1, "//-------------------------------------------------------------------------");
                }

                Table pRelationTable = pRelationTables[iIndex];
                Table pLinkedTable = pLinkedTables[iLinkedIndex];

                String strLinkedCore = generateCoreClassName(pLinkedTable.getName());
                String strLinkedBean = generateBeanClassName(pLinkedTable.getName());

                String strRelationCore = generateCoreClassName(pRelationTable.getName());
                String strRelationBean = generateBeanClassName(pRelationTable.getName());

                Column pLocalKey = pRelationTable.getForeignKeyFor(table);
                Column pExternalKey = pRelationTable.getForeignKeyFor(pLinkedTable);

                indent(1, "/**");
                indent(1, " * Retrieves a List of " + strLinkedBean + " objects using the relation table " +
                        strRelationCore + " given a " + beanClass + " object.");
                indent(1, " *");
                indent(1, " * @param pObject the " + beanClass + " pObject to be used");
                indent(1, " * @return a List of " + strLinkedBean + " objects");
                indent(1, " */");
                indent(1, "// MANY TO MANY");
                indent(1, "public List<" + strLinkedBean + "> load" + strLinkedCore + "Via" + strRelationCore + "(" + beanClass + " pObject) ");
                indent(1, "    throws SQLException {");
                indent(1, "    return load" + strLinkedCore + "Via" + strRelationCore + "(pObject, 1, -1);");
                indent(1, "}");
                indent(0, "");

                indent(1, "/**");
                indent(1, " * Retrieves a List of " + strLinkedBean + " objects using the relation table " +
                        strRelationCore + " given a " + beanClass + " object, specifying the start row and the number of rows.");
                indent(1, " *");
                indent(1, " * @param pObject the " + beanClass + " pObject to be used");
                indent(1, " * @param startRow the start row to be used (first row = 1, last row = -1)");
                indent(1, " * @param numRows the number of rows to be retrieved (all rows = a negative number)");
                indent(1, " * @return a List of " + strLinkedBean + " objects");
                indent(1, " */");
                indent(1, "// MANY TO MANY");
                indent(1, "public List<" + strLinkedBean + "> load" + strLinkedCore + "Via" + strRelationCore + "(" + beanClass + " pObject, int startRow, int numRows)");
                indent(1, "    throws SQLException {");
                indent(1, "    Connection c = null;");
                indent(1, "    PreparedStatement ps = null;");
                indent(1, "    StringBuffer strSQL = new StringBuffer();");
                indent(1, "    strSQL.append(\" select *\");");
                indent(1, "    strSQL.append(\"   from " + pLinkedTable.getName() + ", \");");
                indent(1, "    strSQL.append(\"        " + pRelationTable.getName() + "\");");
                indent(1, "    strSQL.append(\"  where " + pLocalKey.getForeignKeyTabName() + "." + pLocalKey.getForeignKeyColName() + " = ?\");");
                indent(1, "    strSQL.append(\"    and " + pExternalKey.getForeignKeyTabName() + "." + pExternalKey.getForeignKeyColName() + " = " + pExternalKey.getTableName() + "." + pExternalKey.getName() +"\");");
                indent(1, "     try {");
                indent(1, "         c = getConnection();");
                indent(1, "         ps = c.prepareStatement(strSQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
                indent(1, "         " + pLocalKey.getPreparedStatementMethod("pObject." + getGetMethod(pLocalKey) + "()", 1));
                indent(1, "         return " + generateManagerClassName(pLinkedTable.getName()) + ".getInstance().loadByPreparedStatement(ps, null, startRow, numRows);");
                indent(1, "     } finally {");
                indent(1, "        getManager().close(ps);");
                indent(1, "        freeConnection(c);");
                indent(1, "     }");
                indent(1, "}");
                indent(0, "");
            }
        }

        // max
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Retrieves the max value for a specified column of the table " + table.getName() + ".");
        indent(1, " *");
        indent(1, " * @return the max value");
        indent(1, " */");
        indent(1, "public Object max(int column) throws SQLException {");
        indent(1, "    String sql = \"select max(\" + FIELD_NAMES[column] +\") as MMAX from " + table.getName() + "\";");
        indent(1, "    Connection c = null;");
        indent(1, "    Statement pStatement = null;");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    try  {");
        indent(1, "        Object iReturn = null;    ");
        indent(1, "        c = getConnection();");
        indent(1, "        pStatement = c.createStatement();");
        indent(1, "        rs =  pStatement.executeQuery(sql);");
        indent(1, "        if (rs.next()) {");
        indent(1, "            switch (column) {");

        for(int i = 0; i < cols.length; i++) {
            indent(1, "                case ID_" + cols[i].getConstName() + ":");
            indent(1, "                    iReturn = " + cols[i].getResultSetMethodObject("1") + ";");
            indent(1, "                    break;");
        }

        indent(1, "                default:");
        indent(1, "                    iReturn = rs.getObject(1);");
        indent(1, "                    break;");
        indent(1, "            }");
        indent(1, "        }");
        indent(1, "        if (iReturn != null) {");
        indent(1, "            return iReturn;");
        indent(1, "        }");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(pStatement, rs);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "    return null;");
        indent(1, "}");
        indent(0, "");

        // min
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Retrieves the min value for a specified column of the table " + table.getName() + ".");
        indent(1, " *");
        indent(1, " * @return the min value");
        indent(1, " */");
        indent(1, "public Object min(int column) throws SQLException {");
        indent(1, "    String sql = \"select min(\" + FIELD_NAMES[column] +\") as MMIN from " + table.getName() + "\";");
        indent(1, "    Connection c = null;");
        indent(1, "    Statement pStatement = null;");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    try  {");
        indent(1, "        Object iReturn = null;    ");
        indent(1, "        c = getConnection();");
        indent(1, "        pStatement = c.createStatement();");
        indent(1, "        rs =  pStatement.executeQuery(sql);");
        indent(1, "        if (rs.next()) {");
        indent(1, "            switch (column) {");

        for(int i = 0; i < cols.length; i++) {
            indent(1, "                case ID_" + cols[i].getConstName() + ":");
            indent(1, "                    iReturn = (" + cols[i].getJavaType() + ")rs.getObject(1);");
            indent(1, "                    break;");
        }

        indent(1, "                default:");
        indent(1, "                    iReturn = rs.getObject(1);");
        indent(1, "                    break;");
        indent(1, "            }");
        indent(1, "        }");
        indent(1, "        if (iReturn != null) {");
        indent(1, "            return iReturn;");
        indent(1, "        }");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(pStatement, rs);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "    throw new SQLException(\"Error in min\");");
        indent(1, "}");
        indent(0, "");

        // count
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Retrieves the number of rows of the table " + table.getName() + ".");
        indent(1, " *");
        indent(1, " * @return the number of rows returned");
        indent(1, " */");
        indent(1, "public int countAll() throws SQLException {");
        indent(1, "    return countWhere(\"\");");
        indent(1, "}");
        indent(0, "");
        indent(0, "");

        // countWhere
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Retrieves the number of rows of the table " + table.getName() + " with a 'where' clause.");
        indent(1, " * It is up to you to pass the 'WHERE' in your where clausis.");
        indent(1, " *");
        indent(1, " * @param where the restriction clause");
        indent(1, " * @return the number of rows returned");
        indent(1, " */");
        indent(1, "public int countWhere(String where) throws SQLException {");
        indent(1, "    String sql = \"select count(*) as MCOUNT from " + table.getName() + " \" + where;");
        indent(1, "    Connection c = null;");
        indent(1, "    Statement pStatement = null;");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    try  {");
        indent(1, "        int iReturn = -1;    ");
        indent(1, "        c = getConnection();");
        indent(1, "        pStatement = c.createStatement();");
        indent(1, "        rs =  pStatement.executeQuery(sql);");
        indent(1, "        if (rs.next()) {");
        indent(1, "            iReturn = rs.getInt(\"MCOUNT\");");
        indent(1, "        }");
        indent(1, "        if (iReturn != -1) {");
        indent(1, "            return iReturn;");
        indent(1, "        }");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(pStatement, rs);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "    throw new SQLException(\"Error in countWhere\");");
        indent(1, "}");
        indent(0, "");

        // countByPreparedStatement
        indent(1, "/**");
        indent(1, " * Retrieves the number of rows of the table " + table.getName() + " with a prepared statement.");
        indent(1, " *");
        indent(1, " * @param ps the PreparedStatement to be used");
        indent(1, " * @return the number of rows returned");
        indent(1, " */");
        indent(1, "int countByPreparedStatement(PreparedStatement ps) throws SQLException {");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    try  {");
        indent(1, "        int iReturn = -1;");
        indent(1, "        rs = ps.executeQuery();");
        indent(1, "        if (rs.next()) {");
        indent(1, "            iReturn = rs.getInt(\"MCOUNT\");");
        indent(1, "        }");
        indent(1, "        if (iReturn != -1) {");
        indent(1, "            return iReturn;");
        indent(1, "        }");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(rs);");
        indent(1, "    }");
        indent(1, "   throw new SQLException(\"Error in countByPreparedStatement\");");
        indent(1, "}");
        indent(0, "");

        // count
        indent(1, "/**");
        indent(1, " * Looks for the number of elements of a specific " + beanClass + " pObject given a c");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " pObject to look for");
        indent(1, " * @return the number of rows returned");
        indent(1, " */");
        indent(1, "public int countUsingTemplate(" + beanClass + " pObject) throws SQLException  {");
        //indent(1, "    StringBuffer where = new StringBuffer(\"\");");
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        indent(1, "    StringBuffer _sql = null;");
        indent(1, "    StringBuffer _sqlWhere = null;");
        indent(1, "");
        indent(1, "    try {");
        indent(1, "            _sql = new StringBuffer(\"SELECT count(*) as MCOUNT  from " + table.getName() + " WHERE \");");
        indent(1, "            _sqlWhere = new StringBuffer(\"\");");
        indent(1, "            int _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "");
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "                _dirtyCount++; ");
            indent(1, "                _sqlWhere.append((_sqlWhere.length() == 0) ? \" \" : \" AND \").append(\"" + cols[i].getName() + "= ?\");");
            indent(1, "            }");
        }
        indent(1, "");
        indent(1, "            if (_dirtyCount == 0) {");
        indent(1, "               throw new SQLException (\"The pObject to look is unvalid : not initialized !\");");
        indent(1, "            }");
        indent(1, "");
        indent(1, "            _sql.append(_sqlWhere);");
        indent(1, "            c = getConnection();");
        indent(1, "            ps = c.prepareStatement(_sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
        indent(1, "");
        indent(1, "            _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "");
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "                " + cols[i].getPreparedStatementMethod("pObject."+getGetMethod(cols[i])+"()","++_dirtyCount"));
            indent(1, "            }");
        }
        indent(1, "");
        indent(1, "            return countByPreparedStatement(ps);");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");
        indent(0, "");

        // write decodeRow()
        indent(1, "/**");
        indent(1, " * Transforms a ResultSet iterating on the " + table.getName() + " on a " + beanClass + " pObject.");
        indent(1, " *");
        indent(1, " * @param rs the ResultSet to be transformed");
        indent(1, " * @return pObject resulting " + beanClass + " pObject");
        indent(1, " */");
        indent(1, "public " + beanClass + " decodeRow(ResultSet rs) throws SQLException {");
        indent(1, "    " + beanClass + " pObject = create" + beanClass + "();");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "    pObject." + getSetMethod(cols[i]) + "(" + cols[i].getResultSetMethodObject(Integer.toString(i+1)) + ");");
        }
        indent(1, "    pObject.isNew(false);");
        indent(1, "    pObject.resetIsModified();");
        indent(1, "    return pObject;");
        indent(1, "}");
        indent(0, "");

        // write decodeRow() that accepts a field list
        indent(1, "/**");
        indent(1, " * Transforms a ResultSet iterating on the " + table.getName() + " table on a " + beanClass + " pObject according to a list of fields.");
        indent(1, " *");
        indent(1, " * @param rs the ResultSet to be transformed");
        indent(1, " * @param fieldList table of the field's associated constants");
        indent(1, " * @return pObject resulting " + beanClass + " pObject");
        indent(1, " */");
        indent(1, "public " + beanClass + " decodeRow(ResultSet rs, int[] fieldList) throws SQLException {");
        indent(1, "    " + beanClass + " pObject = create" + beanClass + "();");
        indent(1, "    int pos = 0;");
        indent(1, "    for(int i = 0; i < fieldList.length; i++) {");
        indent(1, "        switch(fieldList[i]) {");

        for(int i = 0; i < cols.length; i++) {
            indent(1, "            case ID_" + cols[i].getConstName() + ":");
            indent(1, "                ++pos;");
            indent(4,   "    pObject." +getSetMethod(cols[i]) + "(" + cols[i].getResultSetMethodObject("pos") + ");");
            indent(4,     "    break;");
        }
        indent(1, "        }");
        indent(1, "    }");
        indent(1, "    pObject.isNew(false);");
        indent(1, "    pObject.resetIsModified();");
        indent(0, "");
        indent(1, "    return pObject;");
        indent(1, "}");
        indent(0, "");

        indent(0, "");

        // loadByPreparedStatement
        indent(1, "/**");
        indent(1, " * Loads all the elements using a prepared statement.");
        indent(1, " *");
        indent(1, " * @param ps the PreparedStatement to be used");
        indent(1, " * @return a List of " + beanClass + " objects");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadByPreparedStatement(PreparedStatement ps) throws SQLException {");
        indent(1, "    return loadByPreparedStatement(ps, null);");
        indent(1, "}");
        indent(0, "");

        // loadByPreparedStatement with fieldList
        indent(1, "/**");
        indent(1, " * Loads all the elements using a prepared statement specifying a list of fields to be retrieved.");
        indent(1, " *");
        indent(1, " * @param ps the PreparedStatement to be used");
        indent(1, " * @param fieldList table of the field's associated constants");
        indent(1, " * @return a List of " + beanClass + " objects");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadByPreparedStatement(PreparedStatement ps, int[] fieldList) throws SQLException {");
        indent(1, "    return loadByPreparedStatement(ps, fieldList, 1, -1);");
        indent(1, "}");
        indent(0, "");
        indent(1, "/**");
        indent(1, " * Loads all the elements using a prepared statement specifying a list of fields to be retrieved,");
        indent(1, " * and specifying the start row and the number of rows.");
        indent(1, " *");
        indent(1, " * @param ps the PreparedStatement to be used");
        indent(1, " * @param startRow the start row to be used (first row = 1, last row = -1)");
        indent(1, " * @param numRows the number of rows to be retrieved (all rows = a negative number)");
        indent(1, " * @param fieldList table of the field's associated constants");
        indent(1, " * @return a List of " + beanClass + " objects");
        indent(1, " */");
        indent(1, "public List<" + beanClass + "> loadByPreparedStatement(PreparedStatement ps, int[] fieldList, int startRow, int numRows) throws SQLException {");
        indent(1, "    ResultSet rs =  null;");
        indent(1, "    List<" + beanClass + "> v = new ArrayList<" + beanClass + ">();");
        indent(1, "    try {");
        indent(1, "        rs = ps.executeQuery();");
        indent(1, "        int count = 0;");
        indent(1, "        if (rs.absolute(startRow) && numRows!=0) {");
        indent(1, "            do {");
        indent(1, "                if(fieldList == null) {");
        indent(1, "                    v.add(decodeRow(rs));");
        indent(1, "                } else {");
        indent(1, "                    v.add(decodeRow(rs, fieldList));");
        indent(1, "                }");
        indent(1, "                count++;");
        indent(1, "            } while ( (count<numRows||numRows<0) && rs.next() );");
        indent(1, "        }");
        indent(1, "    } finally {");
        //indent(1, "        if (v != null) { v.clear(); v = null;}");
        indent(1, "        getManager().close(rs);");
        indent(1, "    }");
        indent(1, "    return v;");
        indent(1, "}");
        indent(0, "");

        //indent(1, "//-------------------------------------------------------------------------");
        //indent(1, "// LISTENER ");
        //indent(1, "//-------------------------------------------------------------------------");

        // listener
        //indent(1, "private "+generateListenerClassName() + " listener = null;");
        //indent(0, "");
        //indent(1, "/**");
        //indent(1, " * Registers a unique " + generateListenerClassName() + " listener.");
        //indent(1, " */");
        //indent(1, "public void registerListener("+generateListenerClassName() +" listener) {");
        //indent(1, "    this.listener = listener;");
        //indent(1, "}");
        //indent(0, "");

        // beforeInsert
        //indent(1, "/**");
        //indent(1, " * Before the save of the " + beanClass + " pObject.");
        //indent(1, " *");
        //indent(1, " * @param pObject the " + beanClass + " pObject to be saved");
        //indent(1, " */");
        //indent(1, "void beforeInsert(" + beanClass + " pObject) throws SQLException {");
        //indent(1, "    if (listener != null) {");
        //indent(1, "        listener.beforeInsert(pObject);");
        //indent(1, "    }");
        //indent(1, "}");
        //indent(0, "");

        // afterInsert
        //indent(1, "/**");
        //indent(1, " * After the save of the " + beanClass + " pObject.");
        //indent(1, " *");
        //indent(1, " * @param pObject the " + beanClass + " pObject to be saved");
        //indent(1, " */");
        //indent(1, "void afterInsert(" + beanClass + " pObject) throws SQLException {");
        //indent(1, "    if (listener != null) {");
        //indent(1, "        listener.afterInsert(pObject);");
        //indent(1, "    }");
        //indent(1, "}");
        //indent(0, "");

        // beforeUpdate
        //indent(1, "/**");
        //indent(1, " * Before the update of the " + beanClass + " pObject.");
        //indent(1, " *");
        //indent(1, " * @param pObject the " + beanClass + " pObject to be updated");
        //indent(1, " */");
        //indent(1, "void beforeUpdate(" + beanClass + " pObject) throws SQLException {");
        //indent(1, "    if (listener != null) {");
        //indent(1, "        listener.beforeUpdate(pObject);");
        //indent(1, "    }");
        //indent(1, "}");
        //indent(0, "");

        // afterUpdate
        //indent(1, "/**");
        //indent(1, " * After the update of the " + beanClass + " pObject.");
        //indent(1, " *");
        //indent(1, " * @param pObject the " + beanClass + " pObject to be updated");
        //indent(1, " */");
        //indent(1, "void afterUpdate(" + beanClass + " pObject) throws SQLException {");
        //indent(1, "    if (listener != null) {");
        //indent(1, "        listener.afterUpdate(pObject);");
        //indent(1, "    }");
        //indent(1, "}");
        //indent(0, "");

        // TODO: before/after delete
        indent(1, "//-------------------------------------------------------------------------");
        indent(1, "// UTILS  ");
        indent(1, "//-------------------------------------------------------------------------");

        indent(0, "");
        indent(1, "/**");
        indent(1, " * Retrieves the manager object used to get connections.");
        indent(1, " *");
        indent(1, " * @return the manager used");
        indent(1, " */");
        indent(1, "public " + gManagerName + " getManager() {");
        indent(1, "    return " + gManagerName + " .getInstance();");
        indent(1, "}");
        indent(0, "");

        indent(1, "/**");
        indent(1, " * Frees the connection.");
        indent(1, " *");
        indent(1, " * @param c the connection to release");
        indent(1, " */");
        indent(1, "public void freeConnection(Connection c) {");
        indent(1, "    getManager().releaseConnection(c); // back to pool");
        indent(1, "}");

        indent(1, "/**");
        indent(1, " * Gets the connection.");
        indent(1, " */");
        indent(1, "public Connection getConnection() throws SQLException {");
        indent(1, "    return getManager().getConnection();");
        indent(1, "}");
        indent(0, "");

        indent(0, "");
        indent(0, "    // ------------------------------------------------------ Protected Methods");
        indent(0, "    // none");
        indent(0, "");
        indent(0, "    // -------------------------------------------------------- Private Methods");
        indent(0, "    // none");

        // End class
        writeEnd();
    }

    /**
     * Pad and return a String with the contents of <tt>s</tt> at the right,
     * padded out to the given <tt>width</tt> by adding copies of the
     * <tt>pad</tt> character to the <I>left</I> side
     *
     * @return <tt>String</tt> with length >= <tt>width</tt>
     */
    public String padLeft(String s, int width, char pad) {
        int padSize = width - s.length();

        if (padSize <= 0) {
            return s;
        }

        StringBuffer sb = new StringBuffer(width);

        for (int i = 0; i < padSize; i++) {
            sb.append(pad);
        }
        sb.append(s);
        return sb.toString();
    }


    public void writeSave(/*PrintWriter writer, Table table, Database db*/) throws Exception {
        String beanClass = generateBeanClassName();

        // import keys
        Column impKeys[] = table.getImportedKeys();
        Column pk[] = table.getPrimaryKeys();
        Column cols[] = table.getColumns();

        // write out all column names as comma separated string
        StringBuffer allFields = new StringBuffer();
        for(int i = 0; i < cols.length; i++) {
            if(i == 0) {
                allFields.append("\"");
            } else {
                allFields.append(CodeWriter.LINE_SEP);
                allFields.append("                            ");
                allFields.append("+ \",");
            }

            allFields.append(cols[i].getFullName());
            allFields.append("\"");
        }
        allFields.append(";");

        StringBuffer sql = new StringBuffer();

        // save
        indent(1, "/**");
        indent(1, " * Saves the " + beanClass + " pObject into the database.");
        indent(1, " *");
        indent(1, " * @param pObject the " + beanClass + " pObject to be saved");
        indent(1, " */");
        indent(1, "public " + beanClass + " save(" + beanClass + " pObject) ");
        indent(1, "    throws SQLException {");
        indent(1, "    Connection c = null;");
        indent(1, "    PreparedStatement ps = null;");
        indent(1, "    StringBuffer _sql = null;");
        indent(0, "");

        // -------------------------------------------- AUTO GENERATION OF KEYS

        /* EXAMPLE OF GENERATED CODE
        if (pObject.getStrainNotesKey() == null) {
            Object obj = max(ID__STRAINNOTES_KEY);
            Long val = new Long(0);
            if (obj != null) {
               val = (Long)obj;
            }
            long key = val.longValue();
            pObject.setStrainNotesKey(++key);
        }
         */

        for (int i = 0; i < pk.length; i++) {
            if (pk[i].isColumnNumeric()) {
                indent(1, "    if (pObject." + getGetMethod(pk[i]) + "() == null) {");
                indent(1, "        Object obj = max(ID_" + pk[i].getConstName() + ");");
                indent(1, "        " + pk[i].getJavaType() + " val = new " + pk[i].getJavaType() + "(0);");
                indent(1, "        if (obj != null) {");
                indent(1, "            val = (" + pk[i].getJavaType() + ")obj;");
                indent(1, "        }");
                indent(1, "        long key = val.longValue();");
                indent(1, "        pObject." + getSetMethod(pk[i]) + "(++key);");
                indent(1, "    }");
                indent(1, "");
            }
        }

        // --------------------------------------------------------------------

        indent(1, "    try {");
        indent(1, "        c = getConnection();");
        indent(1, "        if (pObject.isNew()) {");
        indent(1, "            // ------------ SAVE ------------ ");
        //-------------------------------------
        writePreInsert(table);
        //-------------------------------------
        //indent(1, "            beforeInsert(pObject); // listener callback");
        indent(1, "            int _dirtyCount = 0;");
        if ( Column.isPresentLock(cols, optimisticLockType, optimisticLockColumn) ) {
            String lockValue = "new " + getLockColumn(cols).getJavaType() + "(String.valueOf(System.currentTimeMillis()))";
            indent(1, "            pObject."+getSetMethod(getLockColumn(cols))+"(" + lockValue + ");");
            indent(1, "");
        }
        indent(1, "            _sql = new StringBuffer(\"INSERT into " + table.getName() + " (\");");
        indent(1, "");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "                if (_dirtyCount>0) {");
            indent(1, "                    _sql.append(',');");
            indent(1, "                }");
            indent(1, "                _sql.append(\"" + cols[i].getName() + "\");");
            indent(1, "                _dirtyCount++;");
            indent(1, "            }");
            indent(0, "");
        }
        indent(1, "            _sql.append(\") values (\");");
        indent(1, "            if(_dirtyCount > 0) {");
        indent(1, "                _sql.append('?');");
        indent(1, "                for(int i = 1; i < _dirtyCount; i++) {");
        indent(1, "                    _sql.append(\",?\");");
        indent(1, "                }");
        indent(1, "            }");
        indent(1, "            _sql.append(\")\");");
        indent(0, "");
        indent(1, "            ps = c.prepareStatement(_sql.toString(), "+preparedStatementArgsAsString()+");");
        indent(1, "            _dirtyCount = 0;");
        indent(0, "");
        for(int i = 0; i < cols.length; i++) {
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "                " + cols[i].getPreparedStatementMethod(
                    "pObject."+getGetMethod(cols[i])+"()",
                    "++_dirtyCount"));
            indent(1, "            }");
            indent(1, "");
        }
        indent(1, "            ps.executeUpdate();");
        //-------------------------------------
        writePostInsert(table);
        //-------------------------------------
        indent(1, "");
        indent(1, "            pObject.isNew(false);");
        indent(1, "            pObject.resetIsModified();");
        //indent(1, "            afterInsert(pObject); // listener callback");
        indent(1, "        } else { ");
        indent(1, "            if (pObject.isOld()) { ");
        indent(1, "                int num = deleteUsingTemplate(pObject);");
        indent(1, "            } else { // UPDATE ");
        // ======= UPDATE ====================================================================================
        if (pk.length == 0) {
            System.out.println("!! WARNING !! " + table.getName() + " does not have any primary key...");
        }
        //indent(1, "            beforeUpdate(pObject); // listener callback");
        if ( Column.isPresentLock(cols, optimisticLockType, optimisticLockColumn) ) {
            indent(1, "            " + getLockColumn(cols).getJavaType() + " oldLockValue = pObject."+getGetMethod(getLockColumn(cols))+"();");
            String newLockValue = "new " + getLockColumn(cols).getJavaType() + "(String.valueOf(System.currentTimeMillis()))";
            indent(1, "            pObject."+getSetMethod(getLockColumn(cols))+"(" + newLockValue + ");");
            indent(1, "");
        }
        indent(1, "            _sql = new StringBuffer(\"UPDATE " + table.getName() + " SET \");");
        indent(1, "            boolean useComma=false;");
        for(int i = 0; i < cols.length; i++) {
            indent(0, "");
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            indent(1, "                if (useComma) {");
            indent(1, "                    _sql.append(',');");
            indent(1, "                } else {");
            indent(1, "                    useComma=true;");
            indent(1, "                }");
            indent(1, "                _sql.append(\"" + cols[i].getName() + "\").append(\"=?\");");
            indent(1, "            }");
        }

        if (pk.length > 0) {
            indent(1, "            _sql.append(\" WHERE \");");
        }
        sql.setLength(0);
        int j = 0;
        for(j = 0; j < pk.length; j++) {
            if(j > 0)
                sql.append(" AND ");
            sql.append(pk[j].getFullName());
            sql.append("=?");
        }
        if (Column.isPresentLock(cols, optimisticLockType, optimisticLockColumn)) {
            if(j > 0) {
                sql.append(" AND ");
            }
            sql.append(getLockColumn(cols).getFullName() + "=?");
        }
        indent(1, "            _sql.append(\"" + sql + "\");");
        indent(1, "            ps = c.prepareStatement(_sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);");
        indent(1, "            int _dirtyCount = 0;");
        for(int i = 0; i < cols.length; i++) {
            indent(0, "");
            indent(1, "            if (pObject." + getModifiedMethod(cols[i]) + "()) {");
            //indent(1, "               if (pObject."+getGetMethod(cols[i])+"() == null)");
            //indent(1, "                  ps.setNull(++_dirtyCount, "+cols[i].getJavaTypeAsTypeName()+");");
            //indent(1, "               else");
            indent(1, "                  " + cols[i].getPreparedStatementMethod(
                    "pObject."+getGetMethod(cols[i])+"()",
                    "++_dirtyCount"));
            indent(1, "            }");
        }
        indent(1, "");
        indent(1, "            if (_dirtyCount == 0) {");
        indent(1, "                 return pObject;");
        indent(1, "            }");
        indent(1, "");
        for(int i = 0; i < pk.length; i++) {
            indent(1, "            " + pk[i].getPreparedStatementMethod("pObject." + getGetMethod(pk[i]) + "Original()", "++_dirtyCount"));
        }
        if (Column.isPresentLock(cols, optimisticLockType, optimisticLockColumn)) {
            indent(1, "                  " + getLockColumn(cols).getPreparedStatementMethod(
                    "oldLockValue",
                    "++_dirtyCount"));
            indent(1, "            if (ps.executeUpdate()==0) {");
            indent(1, "                throw new SQLException(\"sql2java.exception.optimisticlock\");");
            indent(1, "            }");
        } else {
            indent(1, "            ps.executeUpdate();");
        }
        indent(1, "            pObject.resetIsModified();");
        //indent(1, "            afterUpdate(pObject); // listener callback");
        indent(1, "        }");
        indent(1, "    }");
        indent(1, "");
        indent(1, "        return pObject;");
        indent(1, "    } finally {");
        indent(1, "        getManager().close(ps);");
        indent(1, "        freeConnection(c);");
        indent(1, "    }");
        indent(1, "}");
        indent(0, "");
    }

    /**
     * Arguments passed to the prepared statement performing insert.
     * <br>
     * Can be overriden to retrieve auto generated keys.
     */
    protected String preparedStatementArgsAsString() {
        if ("auto".equalsIgnoreCase(Main.getProperty("generatedkey.retrieve", ""))) {
            return "Statement.RETURN_GENERATED_KEYS";
        } else {
            return "ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY";
        }
    }

    /**
     * An empty method that you can override to generate code to be
     * inserted after the save() method inserts a row.
     * <br>
     * Typically this is useful for grabbing the auto-generated primary
     * key and setting the pObject's corresponding property with that
     * value. See the OracleManagerWriter for a sample implementation
     * that maps to a convention I frequently use.
     */
    protected void writePostInsert(Table table) {
        String mode = Main.getProperty("generatedkey.retrieve", "");

        if ("after".equalsIgnoreCase(mode)) {
            String hint = getHint(table);
            Column pk[] = table.getPrimaryKeys();

            if (pk.length==1 && pk[0].isColumnNumeric()) {
                Column pkc = pk[0];
                indent(4, "");
                indent(4, "if (!pObject." + getModifiedMethod(pkc) + "()) {");
                indent(5, "PreparedStatement ps2 = null;");
                indent(5, "ResultSet rs = null;");

                indent(5, "try { ");
                indent(5, "    ps2 = c.prepareStatement(\""+ hint +"\");");
                indent(5, "    rs = ps2.executeQuery();");
                indent(5, "    if(rs.next()) {");
                indent(5, "        pObject." + getSetMethod(pkc) + "(" + pkc.getResultSetMethodObject("1")+ ");");
                indent(5, "    } else {");
                indent(5, "        getManager().log(\"ATTENTION: Could not retrieve generated key!\");");
                indent(5, "    }");
                indent(5, "} finally { ");
                indent(5, "    getManager().close(ps2, rs);");
                indent(5, "}");
                indent(4, "}");
            }
        } else if ("auto".equalsIgnoreCase(mode)) {
            Column pk[] = table.getPrimaryKeys();

            if (pk.length==1 && pk[0].isColumnNumeric()) {
                Column pkc = pk[0];

                indent(4, "if (!pObject." + getModifiedMethod(pkc) + "()) {");
                indent(5, "ResultSet rs = ps.getGeneratedKeys();");
                indent(5, "try { ");
                indent(5, "    if(rs.next())");
                indent(5, "        pObject." + getSetMethod(pkc) + "(" + pkc.getResultSetMethodObject("1")+ ");");
                indent(5, "    else");
                indent(5, "        getManager().log(\"ATTENTION: Could not retrieve auto generated key!\");");
                indent(5, "} finally { ");
                indent(5, "    getManager().close(rs);");
                indent(5, "}");
                indent(4, "}");
            }
        }
    }

    protected void writePreInsert(Table table) {
        String before = Main.getProperty("generatedkey.retrieve", "");

        if (!"before".equalsIgnoreCase(before)) {
            return;
        }

        String hint = getHint(table);

        Column pk[] = table.getPrimaryKeys();

        if (pk.length == 1 && pk[0].isColumnNumeric()) {
            Column pkc = pk[0];
            indent(4, "if (!pObject." + getModifiedMethod(pkc) + "()) {");
            indent(5, "ps = c.prepareStatement(\""+hint+"\");");
            indent(5, "ResultSet rs = null;");
            indent(5, "try {");
            indent(5, "    rs = ps.executeQuery();");
            indent(5, "    if(rs.next())");
            indent(5, "        pObject." + getSetMethod(pkc) + "(" + pkc.getResultSetMethodObject("1") + ");");
            indent(5, "    else");
            indent(5, "        getManager().log(\"ATTENTION: Could not retrieve generated key!\");");
            indent(5, "} finally {");
            indent(5, "    getManager().close(ps, rs);");
            indent(5, "    ps=null;");
            indent(5, "}");
            indent(4, "}");
        }
    }

    private String getHint(Table table) {
        String hint = Main.getProperty("generatedkey.statement", "");

        int index = hint.indexOf("<TABLE>");
        if (index>0) {
            String tmp = hint.substring(0, index) + table.getName();

            if (hint.length() > index+"<TABLE>".length()) {
                tmp = tmp + hint.substring(index+"<TABLE>".length(), hint.length());
            }

            hint = tmp;
        }
        return hint;
    }

    private Column getLockColumn(Column[] cols) {
        Column retVal = null;
        for(int i = 0; i < cols.length; i++) {
            if (cols[i].getName().equalsIgnoreCase(optimisticLockColumn)) {
                retVal = cols[i];
                break;
            }
        }
        return retVal;
    }
}