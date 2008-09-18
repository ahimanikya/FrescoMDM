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
package com.sun.mdm.multidomain.relationship;

/**
 * Relationship Attribute class.
 * @author cye
 */
public class Attribute {
             
    private String name;
    private String columnName;
    private String displayName;
    private boolean searchable;
    private boolean isRequired;
    private int dataType;
    private String defaultValue;
    // validation rule and display rule
    
    public Attribute() {    	
    }
    
    public Attribute(String name, String displayName, int type, String value) {
    	this.name = name;
    	this.displayName = displayName;
    	this.columnName = name;
    	this.searchable = true;
    	this.isRequired = true;
    	this.dataType = type;
    	this.defaultValue = value;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }    
    public boolean getSearchable() {
        return searchable;
    }
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;;
    }
    public boolean getIsRequired() {
        return isRequired;
    }
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;;
    }
    public void setAttributeType(int attributeType) {
        this.dataType = attributeType;
    }    
    public int getAttributeType() {
        return dataType;
    }    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }    
    public String getDefaultValue() {
        return defaultValue;
    }    
}
