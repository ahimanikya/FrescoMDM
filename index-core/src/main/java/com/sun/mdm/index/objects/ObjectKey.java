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
import com.sun.mdm.index.objects.exception.UnfoundKeyNameException;
import com.sun.mdm.index.objects.exception.InvalidKeyValueException;
import com.sun.mdm.index.objects.exception.InvalidKeyNameValueException;
import com.sun.mdm.index.objects.exception.InvalidKeyNameException;

import java.io.Externalizable;
import java.io.Serializable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * ObjectKey definition class
 *
 * @author gzheng
 */
public class ObjectKey implements Externalizable {
    static final long serialVersionUID = -6848089204690030523L;
    public static final int version = 1;

    private static final ObjectException INVALID_KEY_NAME_VALUE = new InvalidKeyNameValueException();
    private static final ObjectException INVALID_KEY_NAME = new InvalidKeyNameException();
    private static final ObjectException UNFOUND_KEY_NAME = new UnfoundKeyNameException();
    private TreeMap mFieldTypes = null;
    private TreeMap mFieldValues = null;
    private TreeMap mFieldNames = null;

    public ObjectKey() {}
        
    /**
     * Creates new instance of ObjectKey by list of field names,types, and
     * values
     *
     * @param names ArrayList of field names
     * @param types ArrayList of field types
     * @param values ArrayList of field values
     * @exception ObjectException ObjectException
     * @todo Document this constructor
     */
    public ObjectKey(ArrayList names, ArrayList types, ArrayList values)
    throws ObjectException {
        if ((null == names) || (null == values) || (null == types)) {
            throw new InvalidKeyNameValueException("Invalid key values: \n"
            + names + "\n" + values + "\n" + types);
        }
        
        if ((names.size() != values.size()) || (names.size() != types.size())) {
            throw new InvalidKeyNameValueException("Invalid key values: \n"
            + names + "\n" + values + "\n" + types);
        }
        
        for (int i = 0; i < types.size(); i++) {
            int type = ((Integer) types.get(i)).intValue();
            
            if (!ObjectField.isValueValid(type, values.get(i))) {
                String classname = (values.get(i).getClass()).getName();
                String t = ObjectField.getTypeString(type);
                throw new InvalidKeyValueException("Field("
                + i + ") '" + (String) names.get(i) + "' of " + t + ": " + classname);
            }
        }
        
        mFieldTypes = null;
        mFieldValues = null;
        mFieldNames = null;
        for (int i = 0; i < names.size(); i++) {
            String name = (String) names.get(i);
            Object type = types.get(i);
            Object value = values.get(i);
            
            if (mFieldTypes == null) {
                mFieldTypes = new TreeMap();
            }
            
            if (mFieldValues == null) {
                mFieldValues = new TreeMap();
            }
            
            if (mFieldNames == null) {
                mFieldNames = new TreeMap();
            }
            
            mFieldTypes.put(name, type);
            mFieldValues.put(name, value);
            mFieldNames.put(name, name);
        }
    }
    
    /**
     * gets a list of key field names
     *
     * @return ArrayList
     */
    public ArrayList getKeyNames() {
        ArrayList ret = null;
        
        if (mFieldNames != null) {
            Iterator it = mFieldNames.values().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                if (ret == null) {
                    ret = new ArrayList();
                }
                
                ret.add(name);
            }
        }
        
        return ret;
    }
    
    /**
     * gets a list of key types
     *
     * @return ArrayList
     */
    public ArrayList getKeyTypes() {
        ArrayList ret = null;
        
        if (mFieldTypes != null) {
            Iterator it = mFieldTypes.values().iterator();
            while (it.hasNext()) {
                Object type = it.next();
                if (ret == null) {
                    ret = new ArrayList();
                }
                
                ret.add(type);
            }
        }
        
        return ret;
    }

    /**
     * gets key type for given field
     * @param name name
     * @return ArrayList
     * @exception ObjectException ObjectException
     */
    public int getKeyType(String name) throws ObjectException {
        int ret;
        
        if (mFieldValues != null) {
            Integer value = (Integer) mFieldTypes.get(name);
            if (value != null) {
                ret = value.intValue();
            } else {
                throw new UnfoundKeyNameException("Unrecognized key field name: " + name);
            }
        } else {
            throw new UnfoundKeyNameException("Unrecognized key field name: " + name);
        }
        
        return ret;
    }    
    
    /**
     * gets key value
     *
     * @param name key field name
     * @exception ObjectException ObjectException
     * @return Object value
     */
    public Object getKeyValue(String name) throws ObjectException {
        Object value = null;
        if (mFieldValues != null) {
            value = mFieldValues.get(name);
            if (value == null) {
                if (!mFieldValues.containsKey(name)) {
                    throw new UnfoundKeyNameException("Unrecognized key field name: " + name);
                }
            }
        } else {
            throw new UnfoundKeyNameException("Unrecognized key field name: " + name);
        }
        
        return value;
    }
    
    /**
     * gets key values
     *
     * @return ArrayList
     */
    public ArrayList getKeyValues() {
        ArrayList ret = null;
        
        if (mFieldValues != null) {
            Iterator it = mFieldValues.values().iterator();
            while (it.hasNext()) {
                Object value = it.next();
                if (ret == null) {
                    ret = new ArrayList();
                }
                
                ret.add(value);
            }
        }
        
        return ret;
    }
    
    /**
     * sets key values by field name
     *
     * @param name key field name
     * @param value key value
     * @exception ObjectException ObjectException
     */
    public void setKeyValue(String name, Object value)
    throws ObjectException {
        if (mFieldValues != null && mFieldTypes != null) {
            if (mFieldValues.containsKey(name)) {
                Object v = mFieldValues.get(name);
                int type = ((Integer) mFieldTypes.get(name)).intValue();

                if (!(ObjectField.isValueValid(type, value))) {
                    String classname = (value.getClass()).getName();
                    String t = ObjectField.getTypeString(type);
                    throw new InvalidKeyValueException("Field(" + name + ") of " + t + ": " + classname);
                }
                mFieldValues.remove(name);
                mFieldValues.put(name, value);
            } else {
                throw new UnfoundKeyNameException("Unrecognized key field name '" + name + "'"
                + " not in " + mFieldValues);
            }
        } else {
            throw new UnfoundKeyNameException("Unrecognized key field name '" + name + "'");
        }
    }
    
    /**
     * compares if itself equals to the passed key object
     *
     * @param key ObjectKey
     * @return boolean
     */
    public boolean equals(ObjectKey key) {
        if ((null == key.getKeyNames()) || (0 == key.getKeyNames().size())) {
            return false;
        } else {
            return (this.hashCode() == key.hashCode());
        }
    }
    
    /**
     * calculates hashCode 1. for each key field, concatenates field name and
     * field string value 2. adds up all key fields 3. calc. hashCode
     *
     * @return hashCode
     */
    public int hashCode() {
        int hRet = 0;
        
        String buf = "";

        if (mFieldNames != null && mFieldValues != null && mFieldTypes != null) {
            Iterator nameIt = mFieldNames.values().iterator();
            Iterator valueIt = mFieldValues.values().iterator();
            while (nameIt.hasNext() && valueIt.hasNext()) {
                String name = (String) nameIt.next();
                Object value = valueIt.next();
                buf += (name + value);
            }
        }
        
        hRet = buf.hashCode();
        
        return hRet;
    }
    
    /**
     * toString
     *
     * @return String
     */
    public String toString() {
        String strRet = "\n";
        
        strRet += "KEY: \n";

        if (mFieldNames != null && mFieldTypes != null && mFieldValues != null) {
            Iterator nameIt = mFieldNames.values().iterator();
            Iterator typeIt = mFieldTypes.values().iterator();
            Iterator valueIt = mFieldValues.values().iterator();
            
            while (nameIt.hasNext() && typeIt.hasNext() && valueIt.hasNext()) {
                strRet += ("    Key Value["
                + (String) nameIt.next()
                + "][" + typeIt.next() + "]: " + valueIt.next() + "\n");
            }
        }
        
        return strRet;
    }

    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};
        
        void writeExternal(ObjectOutput out) throws java.io.IOException {
            out.writeObject(mFieldTypes);
            out.writeObject(mFieldValues);
            out.writeObject(mFieldNames);
        }
        
        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mFieldTypes = (TreeMap) in.readObject();
            mFieldValues = (TreeMap) in.readObject();
            mFieldNames = (TreeMap) in.readObject();
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
