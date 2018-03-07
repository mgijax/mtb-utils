/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/DataBean.java,v 1.1 2007/04/30 15:52:17 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * A SimpleDTO, useful for transferring generic data.  <i>Derived from MGI's
 * DTO Object.</i>
 *
 * @author mjv
 * @date 2007/04/30 15:52:17
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/DataBean.java,v 1.1 2007/04/30 15:52:17 mjv Exp
 */
public class DataBean implements Map {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    // the mapping of fieldnames to values in this SimpleDTO
    private Map data = null;

    // a simple, one-item String array used to specify an array type for the
    // Set.toArray() method. We define it once as a class variable to avoid
    // redefining it each time the getFields() method is called.
    private static String[] STRING_ARRAY = new String[1];


    // ----------------------------------------------------------- Constructors

    /**
     *
     */
    public DataBean() {
        this.data = new HashMap();
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Clear the data from this collection.
     *
     * @see java.util.Map.clear()
     */
    public void clear() {
        this.data.clear();
    }

    /**
     * Return a clone of this object.
     *
     * @return a cloned <code>SimpleDTO</code>
     * @see java.util.Map.clone()
     */
    public Object clone() {
        DataBean dto = new DataBean();
        dto.putAll(this);
        return dto;
    }

    /**
     * Check to see if this collection contains the specified <code>key</code>.
     *
     * @return <code>true</code> if the <code>key</code> is in this colection,
     *         <code>false</code> otherwise
     * @see java.util.Map.containsKey (Object )
     */
    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    /**
     * Check to see if this collection contains the specified
     * <code>value</code>.
     *
     * @return <code>true</code> if the <code>value</code> is in this
     *         colection, <code>false</code> otherwise
     * @see java.util.Map.containsValue (Object )
     */
    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    /**
     * Get the <code>Set</code> of entry elements.
     *
     * @return the <code>Set</code> of entry elements
     * @see java.util.Map.entrySet()
     */
    public Set entrySet() {
        return this.data.entrySet();
    }

    /**
     * Get the <code>Object</code> at the specified <code>key</code>.
     *
     * @return the <code>Object</code> at the specified <code>key</code>
     * @see java.util.Map.get (Object)
     */
    public Object get(Object key) {
        return this.data.get(key);
    }

    /**
     * Return <code>true</code> if this collection is empty, <code>false</code>
     * otherwise.
     *
     * @return <code>true</code> if this collection is empty,
     *         <code>false</code> otherwise
     * @see java.util.Map.isEmpty()
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
     * Get the <code>Set</code> of key elements.
     *
     * @return the <code>Set</code> of key elements
     * @see java.util.Map.keySet()
     */
    public Set keySet() {
        return this.data.keySet();
    }

    /**
     * Put an elemnt into this collection.
     *
     * @param key the key value
     * @param value the object value
     * @see java.util.Map.put (Object key, Object value)
     */
    public Object put(Object key, Object value) {
        return this.data.put(key, value);
    }

    /**
     * Put all the elements in the <code>Map</code> into this.
     *
     * @param m the <code>Map</code>
     * @see java.util.Map.putAll (Map)
     */
    public void putAll(Map m) throws NullPointerException {
        this.data.putAll(m);
    }

    /**
     * Remove <code>key</code> from the </code>Collection</code>.
     *
     * @return the removed <code>Object</code>
     * @see java.util.Map.remove (Object)
     */
    public Object remove(Object key) {
        return this.data.remove(key);
    }

    /**
     * Get the number of data elements.
     *
     * @return the number of data elements
     * @see java.util.Map.size()
     */
    public int size() {
        return this.data.size();
    }

    /**
     * Get the values.
     *
     * @return a <code>Collection</code> of values
     *
     * @see java.util.Map.values()
     */
    public Collection values() {
        return this.data.values();
    }

    /**
     * Get all the fieldnames defined in this <code>SimpleDTO</code>.
     *
     * @return an array of Strings, each of which is the name of a field
     *         defined in this <code>SimpleDTO</code>. If the
     *         <code>SimpleDTO</code> has no fields defined, then a zero-length
     *         String array is returned
     */
    public String[] getFields() {
        Set keySet = this.data.keySet();
        if (keySet.isEmpty()) {
            return new String[0];
        }
        return (String[]) keySet.toArray(STRING_ARRAY);
    }

    /**
     * Removes any fieldnames and values currently defined in this
     * <code>SimpleDTO</code>.
     */
    public void reset() {
        this.data.clear();
    }

    /**
     * Merge the 'other' <code>SimpleDTO</code> into this one. Any fields
     * defined in 'this' and in 'dtoOther' will be set to the value from
     * 'dtoOther'.
     *
     * @param dtoOther the SimpleDTO to be merged into this one
     */
    public void merge(DataBean dtoOther) {
        this.merge(dtoOther, true);
    }

    /**
     * Merge the 'other' <code>SimpleDTO</code<> into this one.
     *
     * @param dtoOther the <code>SimpleDTO</code> to be merged into this one
     * @param overwrite determines whether values from 'other' should replace
     * values in this SimpleDTO for any shared fieldnames. (true means replace
     * them, false means to keep those currently in this SimpleDTO)
     */
    public void merge(DataBean dtoOther, boolean bOverwrite) {
        String[] fields = dtoOther.getFields();
        String fieldname = null;

        // for the sake of efficiency, we only check bOverwrite once, then
        // go to the appropriate loop (rather than having one loop and
        // checking 'bOverwrite' inside it)

        if (bOverwrite) {
            // Since we are overwriting, we do not need to see if any of the
            // fields are already defined in 'this' object; we just go ahead
            // and copy all the definitions from 'other' to 'this'.

            for (int i = 0; i < fields.length; i++) {
                fieldname = fields[i];
                this.data.put(fieldname, dtoOther.get(fieldname));
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                fieldname = fields[i];
                if (!this.data.containsKey(fieldname)) {
                    this.data.put(fieldname, dtoOther.get(fieldname));
                }
            }
        }
    }

    /**
     * Returns the object string representation.
     *
     * @return the object as a string
     */
    public String toString() {
        return FieldPrinter.getFieldsAsString(this);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}


