/*
 * FieldPrinter.java
 *
 * Created on December 28, 2005, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Utility class which provides a static method which can be used
 * to retrieve a String representation of the Field's and
 * their values.
 *
 * System.out.println(FieldPrinter.getFieldsAsString(dto));
 *
 * @author craig (www.codecraig.com)
 * @see #getFieldsAsString(Object)
 */
public final class FieldPrinter {

    private static final String valStart = "        <value>";
    private static final String valEnd = "</value>\n";

    private FieldPrinter() {}

    /**
     * Returns information for each field in the given obj
     *
     * @param obj    the object whose field information is to be returned
     * @return information for each field in the given obj
     * @see Field
     */
    public static String getFieldsAsString(final Object obj) {
        StringBuffer result = null;
        if (obj != null) {
            result = new StringBuffer();
            Field[] fields = obj.getClass().getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            result.append("<class name=\"").append(obj.getClass().getName()).append("\">\n");
            //result.append("    <package>").append(obj.getClass().getPackage()).append("</package>\n");
            // sort the fields by name

            Arrays.sort(fields, new FieldComparator<Field>());

            for (int i = 0; i < fields.length; i++) {
                try {
                    result.append("    <field name=\"").append(fields[i].getName()).append("\"");
                    Type fieldType = fields[i].getGenericType();
                    int mod = fields[i].getModifiers();
                    Object val = fields[i].get(obj);

                    String modifier = "";
                    if (mod != 0) {
                        modifier = Modifier.toString(mod);
                    }

                    result.append(" modifier=\"").append(modifier).append("\"");

                    String typeName = fieldType.toString();

                    if (fieldType instanceof Class) {
                        typeName = getTypeName((Class)fieldType);
                    }

                    result.append(" type=\"").append(typeName).append("\">\n");

                    result.append(appendValue(val));

                    result.append("    </field>\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            result.append("</class>");

        }
        return (result == null ? null : result.toString());
    }

    /**
     * Appends the value(s) in value to the given
     * StringBuffer sb.  If the given
     * value is an array, its contents will be appended in a comma-delimited
     * string.
     *
     * @param sb    the StringBuffer to be appended to
     * @param value    the value to append to sb
     */
    private static String appendValue(final Object value) {
        StringBuffer sb = new StringBuffer();
        if (isArray(value) == false) {
            sb.append(valStart).append(value).append(valEnd);
        }
        else {
            if (value instanceof Object[]) {
                Object[] array = (Object[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof char[]) {
                char[] array = (char[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(String.valueOf(array[i])).append(valEnd);
                }
            } else if (value instanceof int[]) {
                int[] array = (int[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof byte[]) {
                byte[] array = (byte[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof long[]) {
                long[] array = (long[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof boolean[]) {
                boolean[] array = (boolean[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof float[]) {
                float[] array = (float[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            } else if (value instanceof double[]) {
                double[] array = (double[]) value;
                for (int i = 0; i < array.length; i++) {
                    sb.append(valStart).append(array[i]).append(valEnd);
                }
            }
        }

        return sb.toString();
    }

    /**
     * Determines if the given val is some type of
     * array
     *
     * @param val    object to check
     * @return    true if val is an array,
     *             false otherwise
     */
    private static final boolean isArray(final Object val) {
        if (val instanceof Object[] ||
            val instanceof char[] ||
            val instanceof int[] ||
            val instanceof byte[] ||
            val instanceof long[] ||
            val instanceof boolean[] ||
            val instanceof float[] ||
            val instanceof double[]) {
            return true;
        }
        return false;
    }

    /**
     * Returns a String representation of the given modifier
     *
     * @param modifier    the modifier to use
     * @return    a String representation of the given modifier
     * @see Modifier
     */
    private static final String getModifierName(final int modifier) {
        switch (modifier) {
            case Modifier.ABSTRACT:
                return "abstract";
            case Modifier.FINAL:
                return "final";
            case Modifier.INTERFACE:
                return "interface";
            case Modifier.NATIVE:
                return "native";
            case Modifier.PRIVATE:
                return "private";
            case Modifier.PROTECTED:
                return "protected";
            case Modifier.PUBLIC:
                return "public";
            case Modifier.STATIC:
                return "static";
            case Modifier.STRICT:
                return "strict";
            case Modifier.SYNCHRONIZED:
                return "synchronized";
            case Modifier.TRANSIENT:
                return "transient";
            case Modifier.VOLATILE:
                return "volatile";
            default:
                return "";
        }
    }

    private static String getTypeName(Class type) {
        if (type.isArray()) {
            try {
                Class cl = type;
                int dimensions = 0;
                while (cl.isArray()) {
                    dimensions++;
                    cl = cl.getComponentType();
                }
                StringBuffer sb = new StringBuffer();
                sb.append(cl.getName());
                for (int i = 0; i < dimensions; i++) {
                    sb.append("[]");
                }
                return sb.toString();
            } catch (Throwable e) { /*FALLTHRU*/ }
        }
        return type.getName();
    }
}