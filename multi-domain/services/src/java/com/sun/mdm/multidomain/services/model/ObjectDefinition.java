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
package com.sun.mdm.multidomain.services.model;

import java.util.List;
import java.util.ArrayList;

/**
 * This represent the ObjectDefinition or metaData for a given object and refered to midm.
 * @author cye
 */
public class ObjectDefinition {

    private String name;
    private String dateFormat;
    private String database;
    
    /**
     * list of fields
     */
    List<Field> fields = new ArrayList<Field>();
    /**
     * list of children
     */
    List<ObjectDefinition> children = new ArrayList<ObjectDefinition>();

    /**
     * Create an instance of ObjectDefinition.
     * @param name Object name.
     */
    public ObjectDefinition(String name) {
        this.name = name;
    }
    
    /**
     * Create an instance of ObjectDefinition.
     * @param name Object tag.
     * @param fields List of Field.
     * @param children List of ObjectDefinition.
     */
    public ObjectDefinition(String name, List<Field> fields, List<ObjectDefinition> children) {
        this.name = name;
        this.fields = fields;
        this.children = children;
    }

    /**
     * Add field into the object.
     * @param f Field.
     * @return boolean True if add successfully.
     */
    public boolean addField(Field f) {
        return fields.add(f);
    }

    /**
     * Add field at a specified location, shift the elements from the current
     * position to the right.
     * @param index Index.
     * @param element Field.
     */
    public void addField(int index, Field element) {
        fields.add(index, element);
    }

    /**
     * Ensure minimium capacity.
     * @param minCapacity Minimium Capacity.
     */
    private void ensureFieldCapacity(int minCapacity) {
        int size = minCapacity + 1;
        if (fields.size() < size) {
            for (int i = fields.size(); i < size; i++) {
                fields.add(i, null);
            }
        }
    }

    /**
     * Get field for the given index.
     * @param index Field index.
     * @return Field.
     */
    public Field getField(int index) {
        return fields.get(index);
    }

    /**
     * Set the field at the given index.
     * @param index
     * @param element
     * @return Field
     */
    public Field setField(int index, Field element) {
        ensureFieldCapacity(index);
        return fields.set(index, element);
    }

    /**
     * Add child at the given index.
     * @param index index.
     * @param element Object definition.
     */
    public void addchild(int index, ObjectDefinition element) {
        children.add(index, element);
    }

    /**
     * Add child.
     * @param o ObjectDefinition.
     * @return boolean True if add successfully.
     */
    public boolean addchild(ObjectDefinition o) {
        return children.add(o);
    }

    /**
     * Get child at the given index.
     * @param index Index.
     * @return ObjectDefinition CHild object definition.
     */
    public ObjectDefinition getchild(int index) {
        return children.get(index);
    }

    /**
     * Return the child ObjectDefinition, if the child with a given name exist,
     * return null otherwise. Note this access is much slower that the indexed
     * approach, the performance can degrade if the number of children is high.
     * @param childName Child name.
     * @return 
     */
    public ObjectDefinition getchild(String childName) {
        for (ObjectDefinition child : children) {
            if (child.getName().equals(childName)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Set child at the given index.
     * @param index Index.
     * @param element Object definition.
     * @return ObjectDefinition.
     */
    public ObjectDefinition setchild(int index, ObjectDefinition element) {
        ensureChildCapacity(index);
        return children.set(index, element);
    }

   /**
     * Ensure minimium capacity.
     * @param minCapacity Minimium Capacity.
     */    
    private void ensureChildCapacity(int minCapacity) {
        int size = minCapacity + 1;

        if (children.size() < size) {
            for (int i = children.size(); i < size; i++) {
                children.add(i, null);
            }
        }
    }

    /**
     * Get children.
     * @return List<ObjectDefinition> Children.
     */
    public List<ObjectDefinition> getChildren() {
        return children;
    }

    /**
     * Set children.
     * @param List<ObjectDefinition> Children.
     */
    public void setChildren(List<ObjectDefinition> children) {
        this.children = children;
    }

    /**
     * Get fields.
     * @return List<Field> List of Field.
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * Set fields.
     * @param fields List of Field.
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * Get object name.
     * @return name Object name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set object name.
     * @param name  Object name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("name: " + name + "\n");
        sb.append(" Fields: \n");
        sb.append(fields.toString());
        if (!children.isEmpty()) {
            sb.append("\nChildren: ");
        }
        for (ObjectDefinition c : children) {

            sb.append("\n child ");
            if (c != null) {
                sb.append(c.toString());
            }
        }
        return sb.toString();
    }

    /**
     * Get date format.
     * @return String Date format.
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Set date format.
     * @param dateFormat Date format.
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Get database name.
     * @return String Database name.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Set database name.
     * @param database Database name.
     */
    public void setDatabase(String database) {
        this.database = database;
    }
}
