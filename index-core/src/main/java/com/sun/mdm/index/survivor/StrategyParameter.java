/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.survivor;

import com.sun.mdm.index.util.Localizer;

/**
 * The <b>StrategyParameter</b> class represents a single parameter for the
 * survivor strategy. These parameters are specified in the Best Record
 * configuration file. This class converts text values into actual Java
 * objects.
 *
 * 
 * @version $Revision: 1.1 $
 */
public class StrategyParameter implements Cloneable, java.io.Serializable {
    
    private transient static final Localizer mLocalizer = Localizer.get();
    
    //private Object valueObject;
    private static Class[] allowedTypes = {
        java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class,
        java.lang.Double.class, java.lang.Byte.class, java.lang.Short.class,
        java.lang.Long.class, java.lang.Float.class, java.lang.Character.class,
    };

    /** The name of the parameter */
    protected String mName;

    /** The type of parameter */
    protected String mType;

    /** The value of the parameter */
    protected String mValue;

    /**
     * Creates a new instance of the StrategyParameter class given the name,
     * type, and value of the parameter.
	 * <p>
     * @param name The name of the parameter.
     * @param type The type of parameter. This must match one of the following
     * allowed types.
     * <UL>
	 * <LI>String
	 * <LI>Boolean
	 * <LI>Integer
	 * <LI>Double
	 * <LI>Byte
	 * <LI>Short
	 * <LI>Long
	 * <LI>Float
	 * <LI>Character
     * </UL>
     * @param value The value of the parameter.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public StrategyParameter(String name, String type, String value) {
        mName = name;
        mType = type;
        checkType(mType);
        mValue = value;
    }

    /**
     * Verifies the specified parameter type to ensure that it is an allowed type and
     * that it can be loaded using the current class loader.
     * <p>
     * @param type The parameter type.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    void checkType(String type) {
        if (type != null) {
            Class typeClass = null;

            // is it loadable ?
            try {
                typeClass = Class.forName(type);
            } catch (Throwable t) {
                throw new IllegalArgumentException(mLocalizer.t("SUR514: This is an invalid " +
                                        "property value type: {0}", type));
            }

            boolean allowedType = false;

            for (int i = 0; i < allowedTypes.length; i++) {
                if (allowedTypes[i].equals(typeClass)) {
                    allowedType = true;

                    break;
                }
            }

            if (!allowedType) {
                throw new IllegalArgumentException(mLocalizer.t("SUR515: This is an invalid " +
                                        "property value type: {0}", type));
            }
        }
    }

    /**
     * Retrieves the parameter name from an instance of StrategyParameter.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The name of the strategy parameter.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getName() {
        return mName;
    }

    /**
     * Retrieves the string representation of the parameter type from an instance
     * of StrategyParameter.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The strategy parameter type as a string.
     * <DT><B>Throws:</B><DD>None.
     * @include
     *
     */
    public String getType() {
        return mType;
    }

    /**
     * Retrieves the original string representation of the parameter value from
     * an instance of StrategyParameter.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The strategy parameter value.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Retrieves the parameter value after it is converted to the specified type.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Object</CODE> - The parameter value converted to an object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Object getValueObject() {
        return getObjectFromString(mValue, getValueType());
    }

    /**
     * Retrieves the Java class representing the value type.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Class</CODE> - The Java class of the value type.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public Class getValueType() {
        if (mType == null) {
            return String.class;
        } else {
            try {
                return Class.forName(mType);
            } catch (Throwable t) {
                return null;
            }
        }
    }

    /** return the value object, after conversion from string to specified type
     * @param string value as a string
     * @param type type to convert to
     * @return value object
     */
    static Object getObjectFromString(java.lang.String string,
        java.lang.Class type) {
        if ((string == null) || ("".equals(string) && !type.equals(String.class))) {
            return null;
        }

        try {
            if (String.class.equals(type)) {
                return string;
            } else if (Boolean.class.equals(type)) {
                return Boolean.TRUE;
            } else if (Integer.class.equals(type)) {
                return new Integer(string);
            } else if (Double.class.equals(type)) {
                return new Double(string);
            } else if (Float.class.equals(type)) {
                return new Float(string);
            } else if (Short.class.equals(type)) {
                return new Short(string);
            } else if (Byte.class.equals(type)) {
                return new Byte(string);
            } else if (Long.class.equals(type)) {
                return new Long(string);
            } else if (Character.class.equals(type)) {
                if (string.length() != 1) {
                    throw new IllegalArgumentException(mLocalizer.t("SUR516: This is an " +
                                        "value for the string parameter: {0}", string));
                } else {
                    return new Character(string.charAt(0));
                }
            }
        } catch (Throwable t) {
            throw new IllegalArgumentException(mLocalizer.t("SUR517: Could not " +
                                        "return the value object from a string: {0}", t));
        }

        throw new IllegalArgumentException();
    }
}
