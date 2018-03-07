/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/custom/SearchResults.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.dao.custom;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:02:39 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/dao/src/org/jax/mgi/mtb/dao/custom/SearchResults.java,v 1.1 2008/07/17 17:02:39 sbn Exp $
 */
public class SearchResults<T> {

    // -------------------------------------------------------------- Constants

    private final String LINE_SEP = "line.separator";
    protected final String EOL =  System.getProperty(LINE_SEP);

    // ----------------------------------------------------- Instance Variables
    // none

    private List<T> l = null;
    private long total = 0;
    private long ancillaryTotal = 0;
    private long ancillarySize = 0;

    // ----------------------------------------------------------- Constructors

    public SearchResults() {
        this(null, 0);
    }

    public SearchResults(List<T> l,
                         long total) {
        setList(l);
        setTotal(total);
    }

    public List<T> getList() {
        return this.l;
    }

    public void setList(List<T> l){
        this.l = l;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAncillarySize() {
        return this.ancillarySize;
    }

    public void setAncillarySize(long ancillarySize) {
        this.ancillarySize = ancillarySize;
    }

    public long getAncillaryTotal() {
        return this.ancillaryTotal;
    }

    public void setAncillaryTotal(long ancillaryTotal) {
        this.ancillaryTotal = ancillaryTotal;
    }

    public long getSize() {
        long size = 0;

        if (l != null) {
            size = l.size();
        }

        return size;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
