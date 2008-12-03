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

package com.sun.mdm.multidomain.parser;

/**
 *
 * @author kkao
 */
public class Attribute {
    public final String TYPE_PREDEFINED = "0";
    public final String TYPE_EXTENDED = "1";
    
    String name = "";
    String type = "";           // Predefined 0, Extended 1
    String columnName = "";     // column name in the table the attribute bind to
    String displayName = "";
    String searchable = "true";
    String required = "true";
    String defaultValue = "";
    String dataType = "";
    String included = "true";   // predefined attribute
    String startdate = "";      // ?
    String enddate = "";        // ?
        
    public Attribute() {
        
    }
    
    /** Predefined attribute
     * 
     * @param name
     * @param included
     * @param required
     */
    public Attribute(String name, String included, String required) {
        this.name = name;
        this.included = included;
        this.required = required;
        this.type = TYPE_PREDEFINED;
        this.dataType = "date";
    }
    
    /** Extended attribute
     * 
     * @param name
     * @param columnName
     * @param dataType
     * @param defaultValue
     * @param searchable
     * @param required
     * @param attributeID
     */
    public Attribute(String name, String columnName, String dataType, String defaultValue,
                     String searchable, String required) {
        this.name = name;
        this.columnName = columnName;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.searchable = searchable;
        this.required = required;
        this.type = TYPE_EXTENDED;
    }
       
    public Attribute createCopy() {
        Attribute attr = new Attribute();
        attr.name = this.name;
        attr.columnName = this.columnName;
        attr.dataType = this.dataType;
        attr.defaultValue = this.defaultValue;
        attr.included = this.included;
        attr.required = this.required;
        attr.searchable = this.searchable;
        attr.type = this.type;
        return attr;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRequired(String required) {
        this.required = required;
    }
        
    public void setIncluded(String included) {
        this.included = included;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSearchable(String searchable) {
        this.searchable = searchable;
    }

    public void setType(String type) {
        this.type = type;
        }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isRequired() {
        return required.equals("true");
    }

    public String getName() {
        return name;
    }

    public String getRequired() {
        return required;
    }

    public String getIncluded() {
        return included;
    }

    public String getSearchable() {
        return searchable;
    }

    public boolean isSearchable() {
        return searchable.equals("true");
    }

    public String getType() {
        return type;
    }

    public String getEndDate() {
        return enddate;
    }

    public void setEndDate(String enddate) {
        this.enddate = enddate;
    }

    public String getStartDate() {
        return startdate;
    }

    public void setStartDate(String startdate) {
        this.startdate = startdate;
    }
}