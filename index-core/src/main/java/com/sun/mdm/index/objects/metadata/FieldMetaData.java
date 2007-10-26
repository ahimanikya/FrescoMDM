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
package com.sun.mdm.index.objects.metadata;

import com.sun.mdm.index.objects.ObjectField;


/**
 * Field meta data structure
 *
 * @author gzheng
 */

public class FieldMetaData {
    private String mFieldName;
    private String mFieldType;
    private int mFieldSize;
    private boolean mRequired;
    private boolean mUpdateable;
    
    private String mUserCode;
    private String mConstraintBy;

    /**
     * Creats a new instance of FieldMetaData by field name and type
     *
     * @param name field name
     * @param type field type
     * @param size field size
     * @param required field required
     */
    public FieldMetaData(String name, String type, int size, boolean required,
            boolean updateable, String userCode, String constraintBy) {
        mFieldName = name;
        mFieldType = type;
        mFieldSize = size;
        mRequired  = required;
        mUpdateable = updateable;
        
        mUserCode = userCode;
        mConstraintBy = constraintBy;
    }


    /**
     * Creates a new instance of FieldMetaData by field name and type (Integer)
     *
     * @param name field name
     * @param type field type in integer
     * @param size field size
     * @param required field required
     */
    public FieldMetaData(String name, Integer type, int size, boolean required,
            boolean updateable, String userCode, String constraintBy) {
        mFieldName = name;
        switch (type.intValue()) {
        case ObjectField.OBJECTMETA_BOOL_TYPE:
            mFieldType = ObjectField.OBJECTMETA_BOOL_STRING;
            break;
        case ObjectField.OBJECTMETA_INT_TYPE:
            mFieldType = ObjectField.OBJECTMETA_INT_STRING;
            break;
        case ObjectField.OBJECTMETA_STRING_TYPE:
            mFieldType = ObjectField.OBJECTMETA_STRING_STRING;
            break;
        case ObjectField.OBJECTMETA_BYTE_TYPE:
            mFieldType = ObjectField.OBJECTMETA_BYTE_STRING;
            break;
        case ObjectField.OBJECTMETA_CHAR_TYPE:
            mFieldType = ObjectField.OBJECTMETA_CHAR_STRING;
            break;
        case ObjectField.OBJECTMETA_LONG_TYPE:
            mFieldType = ObjectField.OBJECTMETA_LONG_STRING;
            break;
        case ObjectField.OBJECTMETA_BLOB_TYPE:
            mFieldType = ObjectField.OBJECTMETA_BLOB_STRING;
            break;
        case ObjectField.OBJECTMETA_DATE_TYPE:
            mFieldType = ObjectField.OBJECTMETA_DATE_STRING;
            break;
        case ObjectField.OBJECTMETA_FLOAT_TYPE:
            mFieldType = ObjectField.OBJECTMETA_FLOAT_STRING;
            break;
        case ObjectField.OBJECTMETA_TIMESTAMP_TYPE:
            mFieldType = ObjectField.OBJECTMETA_TIMESTAMP_STRING;
        }
        mFieldSize = size;
        mRequired  = required;
        mUpdateable = updateable;
        
        mUserCode = userCode;
        mConstraintBy = constraintBy;
    }


    /**
     * toString
     *
     * @return String
     */
    public String toString() {
        String ret = null;
        ret = "Field \"" + getName() + "\": " + getType() + "(" + getSize() + ")" + "\n";
        return ret;
    }


    /**
     * gets field name
     *
     * @return String
     */
    public String getName() {
        return mFieldName;
    }


    /**
     * gets field type
     *
     * @return String type
     */
    public String getType() {
        return mFieldType;
    }


    /**
     * gets field size
     *
     * @return int size
     */
    public int getSize() {
        return mFieldSize;
    }
    
    /**
     * gets field required
     *
     * @return boolean required or not
     */
    public boolean isRequired() {
        return mRequired;
    }
    
    /**
     * gets field updateable
     *
     * @return boolean updateable or not
     */
    public boolean isUpdateable() {
        return mUpdateable;
    }
    
    /**
     * gets defined user code for AUXID pull down
     *
     * @return String user code value
     */
    public String getUserCode() {
        return mUserCode;
    }
    
    /**
     * gets defined constraint by for AUXID pull down
     *
     * @return String constraint by value
     */
    public String getConstraintBy() {
        return mConstraintBy;
    }
}
