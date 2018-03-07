package org.jax.mgi.mtb.gui.completer;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractListModel;

/**
 * Class to hold the remaining objects that still match the users input.
 */
public class MXFilterListModel extends AbstractListModel {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private Object[] arrFullList;
    private ArrayList listFiltered;
    private String strFilter;
    private boolean bCaseSensitive = false;

    // ----------------------------------------------------------- Constructors

    public MXFilterListModel(Object[] unfilteredList) {
        arrFullList = unfilteredList;
        listFiltered = new ArrayList(Arrays.asList(unfilteredList));
    }

    // --------------------------------------------------------- Public Methods

    public int getSize() {
        return listFiltered.size();
    }

    public Object getElementAt(int index) {
        return listFiltered.get(index);
    }

    public String getFilter() {
        return strFilter;
    }

    public void setFilter(String filter) {
        listFiltered.clear();
        for(int i = 0; i < arrFullList.length; i++) {
            Object obj = arrFullList[i];
            if (obj.toString().length() < filter.length()) {
                continue;
            }

            if (bCaseSensitive) {
                if (obj.toString().startsWith(filter)) {
                    listFiltered.add(obj);
                }
            } else {
                if (obj.toString().substring(0, filter.length()).compareToIgnoreCase(filter) == 0) {
                    listFiltered.add(obj);
                }
            }
        }
        fireContentsChanged(this, 0, listFiltered.size());
    }

    public void clearFilter() {
        strFilter = null;
        listFiltered = new ArrayList(Arrays.asList(arrFullList));
    }

    public boolean getCaseSensitive() {
        return bCaseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        bCaseSensitive = caseSensitive;
        clearFilter();
    }

    public void setCompleterMatches(Object[] objectsToMatch) {
        arrFullList = objectsToMatch;
        clearFilter();
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
