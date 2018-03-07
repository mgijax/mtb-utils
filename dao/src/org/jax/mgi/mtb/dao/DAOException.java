/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/DAOException.java,v 1.1 2008/07/17 17:02:38 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao;

/**
 * This class is used to encapsulate DAO errors.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:38 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/DAOException.java,v 1.1 2008/07/17 17:02:38 sbn Exp $
 */
public class DAOException extends Exception {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor for a <code>Throwable</code>.
     *
     * @param t the <code>Throwable</code> object
     */
    public DAOException(Throwable t) {
        super(t);
    }

    /**
     * Constructor for a message.
     *
     * @param str the message
     */
    public DAOException(String str) {
        super(str);
    }

    /**
     * Constructor for a <code>Throwable</code> and a message.
     *
     * @param str the message
     * @param t the <code>Throwable</code> object
     */
    public DAOException(String str,
                        Throwable t) {
        super(str, t);
    }

    // --------------------------------------------------------- Public Methods
    // none

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}