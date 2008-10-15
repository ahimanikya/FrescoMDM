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
package com.sun.mdm.multidomain.association;

/**
 *  AttributeType class.
 * @author cye
 */
public class AttributeType {
    public static final int UNKNOWN = -1;
    /** Integer */
    public static final int INTEGER = 0;
    /** Boolean */
    public static final int BOOLEAN = 1;
    /** String */
    public static final int STRING = 2;
    /** Byte */
    public static final int BYTE = 3;
    /** Long */
    public static final int LONG = 4;
    /** Raw */
    public static final int RAW = 5;
    /** Float */
    public static final int FLOAT = 7;
    /** Date */
    public static final int DATE = 6;
    /** Timestamp */
    public static final int TIMESTAMP = 8;
    /** Character */
    public static final int CHAR = 9;
    /** Max number of attribute types */
    static final int MAX_TYPES = 10;

    static final String[] DESCRIPTION = {"unknown", "Integer", "Boolean", "String", "Byte",
                          "Long", "Blob", "Float", "Date", "Timestamp", "Character"};
            
    private int type = UNKNOWN;
            
    /**
     * Create an instance of AttributeType.
     */
    public AttributeType() {
    }
        
    /**
     * Create an instance of AttributeType.
     * @param type Attribute type.
     */
    public AttributeType(int type) {
        this.type = type;
    }
    
    /**
     * Set type.
     * @param type Type.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * Get type.
     * @return int Type.
     */
    public int getType() {
        return type;
    }
    
    /**
     * Get description in string.
     * @return String Description.
     */
    public String getDescription() {
        if (type > -1 &&
            type < 10) {
            return DESCRIPTION[type + 1];    
        } else {
            return DESCRIPTION[0];
        }
    }
    
    /**
     * @see java.lang.Object#equals().
     */
    @Override
    public boolean equals(Object that) {
        if(this == that) {
            return true;
        } else if (that == null) {
            return false;
        }
        AttributeType thatType = (AttributeType)that;
        if (thatType != null &&
            this.getType() == thatType.getType()) {
            return true;
        } else {
            return false;
        }
    }
}
