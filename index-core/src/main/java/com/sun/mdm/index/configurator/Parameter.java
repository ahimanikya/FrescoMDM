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
package com.sun.mdm.index.configurator;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;



/**
 * Represents a single parameter. Converts text values into actual Java objects.
 *
 * @version $Revision: 1.1 $
 */
public class Parameter implements Cloneable, java.io.Serializable {

    /** Allowed Types */    
    private static Class[] allowedTypes = {
            java.lang.String.class,
            java.lang.Boolean.class,
            java.lang.Integer.class,
            java.lang.Double.class,
            java.lang.Byte.class,
            java.lang.Short.class,
            java.lang.Long.class,
            java.lang.Float.class,
            java.lang.Character.class,
            };

    /** name */            
    protected String mName;

    /** type */    
    protected String mType;

    /** value */    
    protected String mValue;

    private transient static final Logger mLogger = Logger.getLogger(Parameter.class.getName());
    private transient static final Localizer mLocalizer = Localizer.get();

    /**
     * Creates new StrategyParameter.
     *
     * @param name Name of the parameter
     * @param type Type of the parameter value, must match one of the allowed
     * types.
     * @param value Actual parameter value.
     */
    public Parameter(String name, String type, String value) {
        mName = name;
        mType = type;
        checkType(mType);
        mValue = value;
    }


    /**
     * Return the value object after conversion from string to specified type.
     *
     * @param string Value as a string.
     * @param type Type to convert to.
     * @throws IllegalArgumentException if an error occurs.
     * @return value object.
     */
    static Object getObjectFromString(java.lang.String string, java.lang.Class type) {
        if (string == null || ("".equals(string) && !type.equals(String.class))) {
            return null;
        }

        try {
            if (String.class.equals(type)) {
                return string;
            } else if (Boolean.class.equals(type)) {
                return Boolean.valueOf(string);
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
                    throw new IllegalArgumentException(mLocalizer.t("CFG504: Encountered " + 
                                                           "an unrecognized object type: {0}", type));
                } else {
                    return new Character(string.charAt(0));
                }
            }
        } catch (Throwable t) {
            throw new IllegalArgumentException(mLocalizer.t("CFG505: Error encountered " + 
                                                            "while retrieving an object from a string: {0}", t));
        }
        throw new IllegalArgumentException(mLocalizer.t("CFG506: General error encountered " + 
                                                            "while retrieving an object from satring."));
    }


    /**
     * Returns the name of the parameter.
     *
     * @return name of the parameter.
     */
    public String getName() {
        return mName;
    }


    /**
     * Returns the string representation of the parameter value type.
     *
     * @return parameter value type.
     */
    public String getType() {
        return mType;
    }


    /**
     * Returns the original string representation of the value.
     *
     * @return value as string.
     */
    public String getValue() {
        return mValue;
    }


    /**
     * Return the value object after converted to the specified type.
     *
     * @return value object.
     */
    public Object getValueObject() {
        return getObjectFromString(mValue, getValueType());
    }


    /**
     * Return the class representing the value type.
     *
     * @return class of the value type.
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


    /**
     * Check if type is loadable using the current class loader and if it's one
     * of the allowed types.
     *
     * @param type string representation of the type.
     * @throws IllegalArgumentException if an error occurs.
     */
    void checkType(String type) {
        if (type != null) {
            Class typeClass = null;
            // is it loadable ?
            try {
                typeClass = Class.forName(type);
            } catch (Throwable t) {
                throw new IllegalArgumentException(mLocalizer.t("CFG507: {0} is not a recognized property value type", t));
            }

            boolean allowedType = false;
            for (int i = 0; i < allowedTypes.length; i++) {
                if (allowedTypes[i].equals(typeClass)) {
                    allowedType = true;
                    break;
                }
            }
            if (!allowedType) {
                throw new IllegalArgumentException(mLocalizer.t("CFG508: \"{0}\" is not an allowed property value type", type));
            }
        }
    }

}
