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
package com.sun.mdm.index.query;

import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.HashSet;


/**
 * Result object
 * @author aegloff
 */
public class ResultObject implements java.io.Serializable, Cloneable {

    private Map map;
    private MetaData meta;


    /**
     * Creates new BlockElement
     */
    public ResultObject() {
        map = new Hashtable();
        meta = new MetaData();
    }


    /** return the meta data associated with this MatchObject
     *
     * @return meta data
     */
    public MetaData getMetaData() {
        return meta;
    }


    /** Get values
     * @param name field name
     * @return values
     */
    public Set getValues(String name) {
        return (Set) map.get(name);
    }


    /**
     * returns a ResultObject containing the values for a particular type
     *
     * @param type type of the object, i.e. Address, phone, etc
     * @return a MatchObject containing only the fields that makes up the type
     */
    public ResultObject getValuesByType(String type) {
        ResultObject mo = new ResultObject();
        Collection fieldNames = meta.getMapping(type);
        Iterator iter = fieldNames.iterator();
        MetaData md = new MetaData();

        while (iter.hasNext()) {
            String fieldName = (String) iter.next();
            Set s = getValues(fieldName);
            mo.setValues(fieldName, s);
            md.addFieldName(fieldName);
        }
        mo.setMetaData(md);
        return mo;
    }


    /** set the meta data
     *
     * @param md meta data
     */
    public void setMetaData(ResultObject.MetaData md) {
        meta = md;
    }


    /**
     * replace the set of values for a given name
     *
     * @param name field name
     * @param s set representing the value
     */
    public void setValues(String name, Set s) {
        map.put(name, s);
    }


    /**
     * add a field value to the set, given the field name
     *
     * @param name field name
     * @param value value to be added to the set
     */
    public void addField(String name, Object value) {
        Set s = (Set) map.get(name);
        if (s == null) {
            s = new LinkedHashSet();
        }
        s.add(value);
        map.put(name, s);
    }


    /** Clone object
     * @throws CloneNotSupportedException clone not supported
     * @return cloned object
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    /** String representation
     * @return string representation
     */
    public String toString() {
        return map.toString();
    }


    /** Meta data
     */
    public class MetaData implements java.io.Serializable {
        private Collection fieldNames;
        private Map typeToNameMapping;


        private MetaData() {
            typeToNameMapping = new Hashtable();
            fieldNames = new HashSet();
        }


        /** Get field names
         * @return a list of field names in the MatchObject
         */
        public Collection getFieldNames() {
            return fieldNames;
        }


        /** Get mapping
         * @return mapping
         * @param type type
         */
        public Collection getMapping(String type) {
            return (Collection) typeToNameMapping.get(type);
        }


        /** Get types
         * @return types
         */
        public Collection getTypes() {
            return typeToNameMapping.keySet();
        }


        /**
         * Adds a feature to the FieldName attribute of the MetaData object
         *
         * @param s The feature to be added to the FieldName attribute
         */
        public void addFieldName(String s) {
            fieldNames.add(s);
        }


        /** add mapping for list of fields per object type
         *
         * @param type type
         * @param fieldNames field names
         */
        public void addMapping(String type, Collection fieldNames) {
            typeToNameMapping.put(type, fieldNames);
        }


        /** String representation
         * @return string representation
         */
        public String toString() {
            return fieldNames.toString();
        }

    }
}
