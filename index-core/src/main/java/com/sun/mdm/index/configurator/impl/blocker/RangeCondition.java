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
package com.sun.mdm.index.configurator.impl.blocker;
import com.sun.mdm.index.configurator.ConfigurationException;


/** Range condition.
 *
 * @version $Revision: 1.1 $
 */
public class RangeCondition extends AbstractCondition {
    
    /** type */
    public static final int TYPE = 7;
    
    public static final int RANGE_TYPE_OFFSET = 0;
    public static final int RANGE_TYPE_CONSTANT = 1;
    
    
    private String defaultLowerValue;
    private String defaultUpperValue;
    
    private int defaultLowerType = -1;
    private int defaultUpperType = -1;
    
    /** Creates a new instance of RangeCondition */
    public RangeCondition() {
    }

    /**
     * Getter for Type attribute of the RangeCondition object.
     *
     * @return type as integer.
     */
    public int getType() {
        return TYPE;
    }
    
    /**
     * Getter for defaultUpperValue attribute of the RangeCondition object.
     *
     * @return default upper value.
     */
    public String getDefaultUpperValue() {
        return defaultUpperValue;
        
    }
    
    /**
     * Getter for defaultLowerValue attribute of the RangeCondition object.
     *
     * @return default lower value.
     */
    public String getDefaultLowerValue() {
        return defaultLowerValue;
        
    }
    
    /**
     * Setter for defaultUpperValue attribute of the RangeCondition object.
     *
     * @param val  Value to set for the default upper value.
     */
    public void setDefaultUpperValue(String val) {
        defaultUpperValue = val;
        
    }
    
    /**
     * Setter for defaultLowerValue attribute of the RangeCondition object.
     *
     * @param val  Value to set for the default lower value.
     */
    public void setDefaultLowerValue(String val) {
        defaultLowerValue = val;
        
    }
    
    /**
     * Getter for defaultUpperType attribute of the RangeCondition object.
     *
     * @return default upper type.
     */
    public int getDefaultUpperType() {
        return defaultUpperType;
        
    }
    
    /**
     * Getter for defaultLowerType attribute of the RangeCondition object.
     *
     * @return default lower type.
     */
    public int getDefaultLowerType() {
        return defaultLowerType;
        
    }
    
    /**
     * Setter for defaultUpperType attribute of the RangeCondition object.
     *
     * @param val  Value to set for the default upper type.
     * @throws ConfigurationException if an error occurs.
     */
    public void setDefaultUpperType(String val) throws ConfigurationException {
        defaultUpperType = translateDefaultType(val);       
    }
    
    /**
     * Setter for defaultLowerType attribute of the RangeCondition object.
     *
     * @param val  Value to set for the default lower type.
     * @throws ConfigurationException if an error occurs.
     */
    public void setDefaultLowerType(String val) throws ConfigurationException {
        defaultLowerType = translateDefaultType(val);        
    }    
    
    /**
     * Translates the default type from a string to its integer equivalent.
     *
     * @param i Default type to translate.
     * @throws ConfigurationException if an error occurs.
     * @return the integer equivalent of the default type.
     */
    private int translateDefaultType(String i) throws ConfigurationException {
        int type;
        if (i.equals("offset")) {
            type = RANGE_TYPE_OFFSET;
        } else if (i.equals("constant")) {
            type = RANGE_TYPE_CONSTANT;
        } else {
            throw new ConfigurationException("Invalid range default type: " + i);
        }
        return type;
    }
    
}
