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
 * @author SwaranjitDua
 */
public class Attribute {
             
	private String attributeID;
    private String name;
    private String columnName;
    private String displayName;
    private boolean searchable;
    private boolean isRequired;
    private AttributeType type;
    private String defaultValue;
  
    /**
     * Create an instance of relationship Attribute class.
     */
    public Attribute() {    	
    }
    
    /**
     * Create an instance of relationship Attribute class.
     * @param name Attribute name.
     * @param displayName Attribute displayname.
     * @param type Attribute type.
     * @param value Attribute value.
     */
    public Attribute(String attributeID, String name, String displayName, AttributeType type, String value) {
    	this.attributeID = attributeID;
    	this.name = name;
    	this.displayName = displayName;
    	this.columnName = name;
    	this.searchable = true;
    	this.isRequired = true;
    	this.type = type;
    	this.defaultValue = value;
    } 

    /**
     * Get attributeID attribute.
     * @return String attributeID attribute.
     */
    public String getAttributeID() {
        return attributeID;
    }
    
    /**
     * Set name attribute.
     * @param name Name attribute.
     */
    public void setAttributeID(String attributeID) {
        this.attributeID = attributeID;;
    }

    /**
     * Get name attribute.
     * @return String Name attribute.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set name attribute.
     * @param name Name attribute.
     */
    public void setName(String name) {
        this.name = name;;
    }
    
    /**
     * Get display name attribute.
     * @return String Display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Set display name attribute.
     * @param String displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;;
    }
    
    /**
     * Get colummn name attribute.
     * @return String columnName.
     */
    public String getColumnName() {
        return columnName;
    }
    
    /**
     * Set column name attribute.
     * @param String columnName
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }    
    
    /**
     * Get searchable attribute.
     * @return boolean Searchable attribute.
     */
    public boolean getSearchable() {
        return searchable;
    }
    
    /**
     * Set searchable attribute.
     * @param searchable Searchable attribute.
     */
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;;
    }
    
    /**
     * Get isRequired attribute.
     * @return boolean IsRequired attribute.
     */
    public boolean getIsRequired() {
        return isRequired;
    }
    
    /**
     * Set isRequired attribute.
     * @param isRequired IsRequired attribute.
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;;
    }
    
    /**
     * Set attribute date type attribute.
     * @param type Attribute data type attribute.
     */
    public void setType(AttributeType type) {
        this.type = type;
    }   
    
    /**
     * Get attribute data type attribute.
     * @return AttributeType Attribute data type attribute.
     */
    public AttributeType getType() {
        return type;
    }    
    
    /**
     * Set attribute default value.
     * @param defaultValue Default value.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }    
    
    /**
     * Get attribute default value.
     * @return String attribute default value.
     */
    public String getDefaultValue() {
        return defaultValue;
    }    
}
