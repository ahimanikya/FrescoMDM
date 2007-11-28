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
package com.sun.mdm.index.objects;

import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.exception.InvalidFieldValueException;
import com.sun.mdm.index.objects.exception.InvalidKeyTypeException;
import com.sun.mdm.index.util.Localizer;

import java.io.Externalizable;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

import java.util.Date;

/**
 * Object Field definition
 * @author gzheng
 */
public class ObjectField implements Externalizable  {
    static final long serialVersionUID = 2508652257817000470L;
    public static final int version = 1;

    private transient static final Localizer mLocalizer = Localizer.get();

    /**
     * undefined field type
     */
    public static final int OBJECTMETA_UNDEFINED_TYPE = -1;

    /**
     * int type
     */
    public static final int OBJECTMETA_INT_TYPE = 0;

    /**
     * bool type
     */
    public static final int OBJECTMETA_BOOL_TYPE = 1;

    /**
     * string type
     */
    public static final int OBJECTMETA_STRING_TYPE = 2;

    /**
     * byte type
     */
    public static final int OBJECTMETA_BYTE_TYPE = 3;

    /**
     * long type
     */
    public static final int OBJECTMETA_LONG_TYPE = 4;

    /**
     * blob type
     */
    public static final int OBJECTMETA_BLOB_TYPE = 5;

    /**
     * date type
     */
    public static final int OBJECTMETA_DATE_TYPE = 6;

    /**
     * float type
     */
    public static final int OBJECTMETA_FLOAT_TYPE = 7;

    /**
     * timestamp type
     */
    public static final int OBJECTMETA_TIMESTAMP_TYPE = 8;

    /**
     * char type
     */
    public static final int OBJECTMETA_CHAR_TYPE = 9;

    /**
     * link type - for SBR
     */
    public static final int OBJECTMETA_LINK_TYPE = 10;
    /**
     * undefined type string
     */
    public static final String OBJECTMETA_UNDEFINED_STRING = "Unknown";

    /**
     * int type string
     */
    public static final String OBJECTMETA_INT_STRING = "Integer";

    /**
     * bool type string
     */
    public static final String OBJECTMETA_BOOL_STRING = "Boolean";

    /**
     * string type string
     */
    public static final String OBJECTMETA_STRING_STRING = "String";

    /**
     * link type string
     */
    public static final String OBJECTMETA_LINK_STRING = "String";
    /**
     * byte type string
     */
    public static final String OBJECTMETA_BYTE_STRING = "Byte";

    /**
     * long type string
     */
    public static final String OBJECTMETA_LONG_STRING = "Long";

    /**
     * blob type string
     */
    public static final String OBJECTMETA_BLOB_STRING = "Blob";

    /**
     * date type string
     */
    public static final String OBJECTMETA_DATE_STRING = "Date";

    /**
     * float type string
     */
    public static final String OBJECTMETA_FLOAT_STRING = "Float";

    /**
     * char type string
     */
    public static final String OBJECTMETA_CHAR_STRING = "Character";
    
    
    /**
     * timestamp type string
     */
    public static final String OBJECTMETA_TIMESTAMP_STRING = "Timestamp";
    private FieldFlag mFlag;
    private String mName;
    private int mType;
    private Object mValue;

    public ObjectField() {}
    
    /**
     * Creates a new instance of ObjectFieldFlag
     *
     * @param name String field name
     * @param type int field type
     */
    public ObjectField(String name, int type) {
        mName = name;
        mType = type;
        mFlag = new FieldFlag();
        mFlag.setNull(true);
    }

    /**
     * Creates new ObjectField
     *
     * @param name field name
     * @param type field type
     * @param value field value
     * @exception ObjectException ObjectException
     */
    public ObjectField(String name, int type, Object value)
        throws ObjectException {
        mName = name;

        if (!isValueValid(type, value)) {
            String classname = (value.getClass()).getName();
            throw new InvalidFieldValueException(mLocalizer.t("OBJ506: Unmatched " + 
                                    "field value type {0} in class {1}", 
                                    getTypeString(type), classname));
        }

        mType = type;
        mValue = value;
        mFlag = new FieldFlag();
        mFlag.setNull(false);
    }

    /**
     * Creates new ObjectField
     *
     * @param name field name
     * @param type field type
     * @param value field value
     * @param flag initial field flag
     * @exception ObjectException ObjectException
     */
    public ObjectField(String name, int type, Object value, FieldFlag flag)
        throws ObjectException {
        mName = name;

        if (!isValueValid(type, value)) {
            String classname = (value.getClass()).getName();
            throw new InvalidFieldValueException(mLocalizer.t("OBJ507: Unmatched " + 
                                    "field value type {0} in class {1}", 
                                    getTypeString(type), classname));
        }

        mType = type;
        mValue = value;
        mFlag = flag.copy();
        mFlag.setNull(false);
    }

    public ObjectField(String name, int type, Object value, int flag)
        throws ObjectException {
        mName = name;

        if (!isValueValid(type, value)) {
            String classname = (value.getClass()).getName();
            throw new InvalidFieldValueException(mLocalizer.t("OBJ508: Unmatched " + 
                                    "field value type {0} in class {1}", 
                                    getTypeString(type), classname));
        }

        mType = type;
        mValue = value;
        mFlag = new FieldFlag(flag);
    }

    public FieldFlag getFieldFlag() {
        return mFlag;
    }
    
    /**
     * gets Class
     *
     * @param type field type
     * @exception ObjectException ObjectException
     * @return Class
     */
    public static Class getClass(int type) throws ObjectException {
        Class classRet = null;

        try {
            switch (type) {
            case OBJECTMETA_INT_TYPE:
                classRet = Class.forName("java.lang.Integer");

                break;

            case OBJECTMETA_BOOL_TYPE:
                classRet = Class.forName("java.lang.Boolean");

                break;

            case OBJECTMETA_STRING_TYPE:
                classRet = Class.forName("java.lang.String");

                break;

            case OBJECTMETA_BYTE_TYPE:
                classRet = Class.forName("java.lang.Byte");

                break;

            case OBJECTMETA_LONG_TYPE:
                classRet = Class.forName("java.lang.Long");

                break;

            case OBJECTMETA_BLOB_TYPE:
                classRet = Class.forName("java.lang.Object");

                break;

            case OBJECTMETA_DATE_TYPE:
                classRet = Class.forName("java.util.Date");

                break;

            case OBJECTMETA_TIMESTAMP_TYPE:
                classRet = Class.forName("java.util.Date");

                break;

            case OBJECTMETA_FLOAT_TYPE:
                classRet = Class.forName("java.lang.Float");

                break;

            case OBJECTMETA_CHAR_TYPE:
                classRet = Class.forName("java.lang.Character");

                break;
            }
        } catch (java.lang.ClassNotFoundException e) {
            throw new ObjectException(mLocalizer.t("OBJ509: Unsupported " + 
                                      "field value type {0} encountered: {1}", 
                                      type, e));
        }

        return classRet;
    }

    /**
     * gets string for int type
     *
     * @param type int value
     * @return String
     */
    public static String getTypeString(int type) {
        String str = null;

        switch (type) {
        case OBJECTMETA_INT_TYPE:
            str = OBJECTMETA_INT_STRING;

            break;

        case OBJECTMETA_BOOL_TYPE:
            str = OBJECTMETA_BOOL_STRING;

            break;
        case OBJECTMETA_LINK_TYPE:
        	str = OBJECTMETA_LINK_STRING;
        	
        	break;
        case OBJECTMETA_STRING_TYPE:
            str = OBJECTMETA_STRING_STRING;

            break;

        case OBJECTMETA_BYTE_TYPE:
            str = OBJECTMETA_BYTE_STRING;

            break;

        case OBJECTMETA_LONG_TYPE:
            str = OBJECTMETA_LONG_STRING;

            break;

        case OBJECTMETA_BLOB_TYPE:
            str = OBJECTMETA_BLOB_STRING;

            break;

        case OBJECTMETA_DATE_TYPE:
            str = OBJECTMETA_DATE_STRING;

            break;

        case OBJECTMETA_TIMESTAMP_TYPE:
            str = OBJECTMETA_TIMESTAMP_STRING;

            break;

        case OBJECTMETA_FLOAT_TYPE:
            str = OBJECTMETA_FLOAT_STRING;

            break;

        case OBJECTMETA_CHAR_TYPE:
            str = OBJECTMETA_CHAR_STRING;

            break;            
        }

        return str;
    }

    /**
     * check if field value type matches
     *
     * @param type field type
     * @param value field value
     * @return boolean
     */
    public static boolean isValueValid(int type, Object value) {
        boolean bRet = true;

        if (null == value) {
            return bRet;
        }

        switch (type) {
        case OBJECTMETA_INT_TYPE:

            if (!(value instanceof java.lang.Integer)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_BOOL_TYPE:

            if (!(value instanceof java.lang.Boolean)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_LINK_TYPE:
        case OBJECTMETA_STRING_TYPE:

            if (!(value instanceof java.lang.String)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_BYTE_TYPE:

            if (!(value instanceof java.lang.Byte)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_LONG_TYPE:

            if (!(value instanceof java.lang.Long)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_BLOB_TYPE:
            break;

        case OBJECTMETA_DATE_TYPE:

            if (!(value instanceof java.util.Date)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_TIMESTAMP_TYPE:

            if (!(value instanceof java.util.Date)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_FLOAT_TYPE:

            if (!(value instanceof java.lang.Float)) {
                bRet = false;
            }

            break;

        case OBJECTMETA_CHAR_TYPE:

            if (!(value instanceof java.lang.Character)) {
                bRet = false;
            }

            break;

        default:
            bRet = false;
        }

        return bRet;
    }

    /**
     * checks if two ObjectFields are the same
     *
     * @param field1 ObjectField
     * @param field2 ObjectField
     * @exception ObjectException ObjectException
     * @return boolean
     */
    public static boolean equals(ObjectField field1, ObjectField field2)
        throws ObjectException {
        boolean ret = true;

        if (field1 == null && field2 == null) {
            ret = true;
        } else if (!(field1 != null && field2 != null)) {
            ret = false;
        } else {
            try {
                if (field1.getType() != field2.getType()) {
                    ret = false;
                } else {
                    int type = field1.getType();
                    Object value1 = field1.getValue();
                    Object value2 = field2.getValue();
                    
                    if (value1 == null && value2 == null) {
                        ret = true;
                    } else if (!(value1 != null && value2 != null)) {
                        ret = false;
                    } else {
                        switch (type) {
                        case OBJECTMETA_INT_TYPE:
                            int intvalue1 = ((Integer) field1.getValue()).intValue();
                            int intvalue2 = ((Integer) field2.getValue()).intValue();
                        
                            if (intvalue1 != intvalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_BOOL_TYPE:
                        
                            boolean boolvalue1 = ((Boolean) field1.getValue()).booleanValue();
                            boolean boolvalue2 = ((Boolean) field2.getValue()).booleanValue();
                        
                            if (boolvalue1 != boolvalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_LINK_TYPE:
                        case OBJECTMETA_STRING_TYPE:
                        
                            String strvalue1 = (String) field1.getValue();
                            String strvalue2 = (String) field2.getValue();
                        
                            if (!strvalue1.equals(strvalue2)) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_BYTE_TYPE:
                        
                            byte bytevalue1 = ((Byte) field1.getValue()).byteValue();
                            byte bytevalue2 = ((Byte) field2.getValue()).byteValue();
                        
                            if (bytevalue1 != bytevalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_LONG_TYPE:
                        
                            long longvalue1 = ((Long) field1.getValue()).longValue();
                            long longvalue2 = ((Long) field2.getValue()).longValue();
                        
                            if (longvalue1 != longvalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_BLOB_TYPE:
                            break;
                        
                        case OBJECTMETA_DATE_TYPE:
                        
                            Date datevalue1 = (Date) field1.getValue();
                            Date datevalue2 = (Date) field2.getValue();
                        
                            if (!datevalue1.equals(datevalue2)) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_TIMESTAMP_TYPE:
                        
                            Date tsvalue1 = (Date) field1.getValue();
                            Date tsvalue2 = (Date) field2.getValue();
                        
                            if (!tsvalue1.equals(tsvalue2)) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_FLOAT_TYPE:
                        
                            float floatvalue1 = ((Float) field1.getValue()).floatValue();
                            float floatvalue2 = ((Float) field2.getValue()).floatValue();
                        
                            if (floatvalue1 != floatvalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        case OBJECTMETA_CHAR_TYPE:
                            
                            char charvalue1 = ((Character) field1.getValue()).charValue();
                            char charvalue2 = ((Character) field2.getValue()).charValue();
                        
                            if (charvalue1 != charvalue2) {
                                ret = false;
                            }
                        
                            break;
                        
                        default:
                            ret = false;
                        }
                    }
                }
            } catch (Exception e) {
                throw new ObjectException(mLocalizer.t("OBJ501: Unmatched " + 
                                      "fields: {0} and {1}:{2}" , field1.toString(),
                                      field2.toString(), e));
            }
        }
        
        return ret;
    }

    /**
     * Getter for Flag attribute of the ObjectField object
     *
     * @param mask int flag mask
     * @exception ObjectException ObjectException
     * @return boolean
     */
    public boolean getFlag(int mask) throws ObjectException {
        boolean bRet = false;

        switch (mask) {
        case FieldFlag.VISIBLETYPE:
            bRet = isVisible();

            break;

        case FieldFlag.SEARCHEDTYPE:
            bRet = isSearched();

            break;

        case FieldFlag.CHANGEDTYPE:
            bRet = isChanged();

            break;

        case FieldFlag.KEYTYPETYPE:
            bRet = isKeyType();

            break;

        case FieldFlag.NULLABLETYPE:
            bRet = isNullable();

            break;

        case FieldFlag.NULLTYPE:
            bRet = isNull();

            break;

        case FieldFlag.READACCESSTYPE:
            bRet = hasReadAccess();

            break;

        case FieldFlag.UPDATEACCESSTYPE:
            bRet = hasUpdateAccess();

            break;

        default:
            throw new InvalidKeyTypeException(mLocalizer.t("OBJ510: Unrecognized " + 
                                      "bit mask \"{0}\"" , mask));
        }

        return bRet;
    }

    /**
     * Getter for Name attribute of the ObjectField object
     *
     * @return String
     */
    public String getName() {
        return mName;
    }

    /**
     * Getter for Type attribute of the ObjectField object
     *
     * @return int type
     */
    public int getType() {
        return mType;
    }

    /**
     * Getter for Value attribute of the ObjectField object
     *
     * @return Object value
     */
    public Object getValue() {
        if (isNull()) {
            return null;
        } else {
            return mValue;
        }
    }

    /**
     * check to see if the field has read access
     *
     * @return boolean
     */
    public boolean hasReadAccess() {
        return mFlag.hasReadAccess();
    }

    /**
     * check to see if the field has update access
     *
     * @return boolean
     */
    public boolean hasUpdateAccess() {
        return mFlag.hasUpdateAccess();
    }

    /**
     * check to see if the field is changed
     *
     * @return boolean
     */
    public boolean isChanged() {
        return mFlag.isChanged();
    }

    /**
     * checks if the field is of Key
     *
     * @return boolean
     */
    public boolean isKeyType() {
        return mFlag.isKeyType();
    }

    /**
     * checks if the field is holding NULL value
     *
     * @return boolean;
     */
    public boolean isNull() {
        return mFlag.isNull();
    }

    /**
     * checks if the field is nullable
     *
     * @return boolean;
     */
    public boolean isNullable() {
        return mFlag.isNullable();
    }

    /**
     * checks if the field is searched on
     *
     * @return boolean
     */
    public boolean isSearched() {
        return mFlag.isSearched();
    }

    /**
     * checks if the field is visible
     *
     * @return boolean
     */
    public boolean isVisible() {
        return mFlag.isVisible();
    }

    /**
     * sets if the field is changed
     *
     * @param flag changed
     */
    public void setChanged(boolean flag) {
        mFlag.setChanged(flag);
    }

    /**
     * sets the bit flag by bit mask
     *
     * @param mask flag
     * @param flag flag
     * @exception ObjectException ObjectException
     */
    public void setFlag(int mask, boolean flag) throws ObjectException {
        switch (mask) {
        case FieldFlag.VISIBLETYPE:
            setVisible(flag);

            break;

        case FieldFlag.SEARCHEDTYPE:
            setSearched(flag);

            break;

        case FieldFlag.CHANGEDTYPE:
            setChanged(flag);

            break;

        case FieldFlag.KEYTYPETYPE:
            setKeyType(flag);

            break;

        case FieldFlag.NULLABLETYPE:
            setNullable(flag);

            break;

        case FieldFlag.NULLTYPE:
            setNull(flag);

            break;

        case FieldFlag.READACCESSTYPE:
            setReadAccess(flag);

            break;

        case FieldFlag.UPDATEACCESSTYPE:
            setUpdateAccess(flag);

            break;

        default:
            throw new InvalidKeyTypeException(mLocalizer.t("OBJ511: Unrecognized " + 
                                      "bit mask \"{0}\"" , mask));
        }
    }

    /**
     * set field's read access
     *
     * @param flag read access
     */
    public void setReadAccess(boolean flag) {
        mFlag.setReadAccess(flag);
    }

    /**
     * set field's update access
     *
     * @param flag update access
     */
    public void setUpdateAccess(boolean flag) {
        mFlag.setUpdateAccess(flag);
    }

    /**
     * set if the field is of key
     *
     * @param flag key type
     */
    public void setKeyType(boolean flag) {
        mFlag.setKeyType(flag);
    }

    /**
     * sets if the field is holding NULL value
     *
     * @param flag null
     */
    public void setNull(boolean flag) {
        mFlag.setNull(flag);
    }

    /**
     * sets if the field is nullable
     *
     * @param flag nullable
     */
    public void setNullable(boolean flag) {
        mFlag.setNullable(flag);
    }

    /**
     * sets if the field is searched on
     *
     * @param flag searched
     */
    public void setSearched(boolean flag) {
        mFlag.setSearched(flag);
    }

    /**
     * sets the value into the field
     *
     * @param value value
     * @exception ObjectException ObjectException
     */
    public void setValue(Object value) throws ObjectException {
        if (!(null == value)){
        	if (value instanceof java.lang.String){
        		String strValue = (String) value;
        		if (strValue.length()>0 && strValue.charAt(0)=='[' && strValue.charAt(strValue.length()-1)==']') {
        			mType = 10;
        		}
        	}
        }
        if (!isValueValid(mType, value)) {
            String classname = (value.getClass()).getName();
            String type = getTypeString(mType);
            throw new InvalidFieldValueException(mLocalizer.t("OBJ512: Unmatched " + 
                                      "field. {0} of type {1} receives {2}", 
                                      getName(), type, classname));
        }

        mValue = value;
        setChanged(true);
    }

    /**
     * sets if the field is visible
     *
     * @param flag visible
     */
    public void setVisible(boolean flag) {
        mFlag.setVisible(flag);
    }

    /**
     * self copy
     *
     * @exception ObjectException ObjectException
     * @return ObjectField
     */
    public ObjectField copy() throws ObjectException {
        ObjectField field = null;

        try {
            field = new ObjectField(mName, mType, mValue, mFlag);
        } catch (ObjectException e) {
            throw e;
        }

        return field;
    }

    /**
     * toString
     *
     * @return String
     */
    public String toString() {
        String v = "v";
        String s = "s";
        String c = "c";
        String k = "k";
        String n = "n";
        String na = "n?";
        String read = "r";
        String update = "u";

        if (isVisible()) {
            v = "V";
        }

        if (isSearched()) {
            s = "S";
        }

        if (isChanged()) {
            c = "C";
        }

        if (isKeyType()) {
            k = "K";
        }

        if (isNullable()) {
            na = "N?";
        }

        if (isNull()) {
            n = "N";
        }

        if (hasReadAccess()) {
            read = "R";
        }

        if (hasUpdateAccess()) {
            update = "U";
        }

        return "[" + v + s + c + k + na + n + "][" + read + update + "]" + getName() + ": " + getValue() + "\n";
    }

    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeInt(mFlag.getFlagValue());
            out.writeObject(mName);
            out.writeInt(mType);
            out.writeObject(mValue);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mFlag = new FieldFlag(in.readInt());
            mName = (String) in.readObject();
            mType = in.readInt();
            mValue = in.readObject();
        }
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
        ev.writeExternal(out);
    }

    public void readExternal(ObjectInput in) 
	throws IOException, java.lang.ClassNotFoundException 
    {
        int version = in.readInt();
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        }
    }
}
