/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/DAOManager.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.sql.DataSource;

/**
 * The DAOManager provides connections and manages transactions transparently.
 * <br>
 * It is a singleton, you get its instance with the getInstance() method.
 * All of the XxxxManager classes use the Manager to get database connections.
 * Before doing any operation, you must pass either a
 * datasource or a jdbc driver/url/username/password.
 * You may extend it and use setInstance() method to make sure your
 * implementation is used as a singleton.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:39 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/DAOManager.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 */
public abstract class DAOManager {

    // -------------------------------------------------------------- Constants

    private final static org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(DAOManager.class.getName());

    // ----------------------------------------------------- Instance Variables

    protected DataSource ds = null;
    protected String jdbc_driver = null;
    protected String jdbc_url = null;
    protected String jdbc_username = null;
    protected String jdbc_password = null;

    // ----------------------------------------------------------- Constructors

    // --------------------------------------------------------- Public Methods

    /**
     * Sets the datasource to be used by the manager.
     * <br>
     * A good datasource manages a pool of connections.
     *
     * @param ds the data source
     */
    public void setDataSource(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Loads the passed jdbc driver.
     * <br>
     * Only needed if the datasource is not set.
     */
    public void setJdbcDriver(String jdbc_driver)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.jdbc_driver = jdbc_driver;
        Class.forName(jdbc_driver).newInstance();
    }

    /**
     * Sets the jdbc url.
     * <br>
     * Only needed if the datasource is not set.
     */
    public void setJdbcUrl(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    /**
     * Sets the username used to access the database.
     * <br>
     * Only needed if the datasource is not set.
     */
    public void setJdbcUsername(String jdbc_username) {
        this.jdbc_username = jdbc_username;
    }

    /**
     * Sets the password used to access the database.
     * <br>
     * Only needed if the datasource is not set.
     */
    public void setJdbcPassword(String jdbc_password) {
        this.jdbc_password = jdbc_password;
    }
    /**
     * Gets an auto commit connection.
     * <br>
     * Normally you do not need this method that much ;-)
     *
     * @return an auto commit connection
     */
    public abstract Connection getConnection() throws SQLException;

    /**
     * Releases the database connection.
     * <br>
     * Normally you should not need this method ;-)
     */
    public abstract void releaseConnection(Connection c);

    /**
     * Initiates a database transaction.
     * <br>
     * When working within a transaction, you should invoke this method first.
     * The connection is returned just in case you need to set the isolation level.
     *
     * @return a non-auto commit connection with the default transaction isolation level
     */
    public abstract Connection beginTransaction() throws SQLException;

    /**
     * Releases connection used for the transaction and performs a commit or rollback.
     *
     * @param commit tells whether this connection should be committed
     *        true for commit(), false for rollback()
     */
    public abstract void endTransaction(boolean commit) throws SQLException;

    /**
     * Closes the passed Statement.
     */
    public void close(Statement s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (SQLException x) {
            log.warn("Could not close statement!: " + x.toString());
        }
    }
 
    /**
     * Closes the passed ResultSet.
     */
    public void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException x) {
            log.warn("Could not close result set!: " + x.toString());
        }
    }

    /**
     * Closes the passed Statement and ResultSet.
     */
    public void close(Statement s, ResultSet rs) {
        close(rs);
        close(s);
    }

    /**
     * Retrieves an int value from the passed result set as an Integer object.
     */
    public static Integer getInteger(ResultSet rs, int pos) throws SQLException {
        int i = rs.getInt(pos);
        return rs.wasNull() ? (Integer)null : new Integer(i);
    }

    /**
     * Set an Integer object to the passed prepared statement as an int or as null.
     */
    public static void setInteger(PreparedStatement ps, int pos, Integer i) throws SQLException {
        if (i==null) {
            ps.setNull(pos, Types.INTEGER);
        } else {
            ps.setInt(pos, i.intValue());
        }
    }

    /**
     * Retrieves a float value from the passed result set as a Float object.
     */
    public static Float getFloat(ResultSet rs, int pos) throws SQLException {
        float f = rs.getFloat(pos);
        return rs.wasNull() ? (Float)null : new Float(f);
    }

    /**
     * Set a Float object to the passed prepared statement as a float or as null.
     */
    public static void setFloat(PreparedStatement ps, int pos, Float f) throws SQLException {
        if (f==null) {
            ps.setNull(pos, Types.FLOAT);
        } else {
            ps.setFloat(pos, f.floatValue());
        }
    }

    /**
     * Retrieves a double value from the passed result set as a Double object.
     */
    public static Double getDouble(ResultSet rs, int pos) throws SQLException {
        double d = rs.getDouble(pos);
        return rs.wasNull() ? (Double)null : new Double(d);
    }

    /**
     * Set a Double object to the passed prepared statement as a double or as null.
     */
    public static void setDouble(PreparedStatement ps, int pos, Double d) throws SQLException {
        if (d==null) {
            ps.setNull(pos, Types.DOUBLE);
        } else {
            ps.setDouble(pos, d.doubleValue());
        }
    }

    /**
     * Retrieves a long value from the passed result set as a Long object.
     */
    public static Long getLong(ResultSet rs, int pos) throws SQLException {
        long l = rs.getLong(pos);
        return rs.wasNull() ? (Long)null : new Long(l);
    }

    /**
     * Set a Long object to the passed prepared statement as a long or as null.
     */
    public static void setLong(PreparedStatement ps, int pos, Long l) throws SQLException {
        if (l==null) {
            ps.setNull(pos, Types.BIGINT);
        } else {
            ps.setLong(pos, l.longValue());
        }
    }

    /**
     * Retrieves a boolean value from the passed result set as a Boolean object.
     */
    public static Boolean getBoolean(ResultSet rs, int pos) throws SQLException {
        boolean b = rs.getBoolean(pos);
        return rs.wasNull() ? (Boolean)null : Boolean.valueOf(b);
    }
    /**
     * Set a Boolean object to the passed prepared statement as a boolean or as null.
     */
    public static void setBoolean(PreparedStatement ps, int pos, Boolean b) throws SQLException {
        if (b==null) {
            ps.setNull(pos, Types.BOOLEAN);
        } else {
            ps.setBoolean(pos, b.booleanValue());
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
