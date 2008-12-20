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
package com.sun.mdm.multidomain.attributes;

import java.io.Serializable;

/**
 * Flexible Attribute class.
 * @author cye
 * @author SwaranjitDua
 */
public class Attribute implements Serializable {
             
    private long id;
    private String name;
    private String columnName;
    private boolean isSearchable;
    private boolean isRequired;
    private AttributeType type; 
    private String defaultValue;
  
    /**
     * Create an instance of  Attribute class.
     */
    public Attribute() {    	
    }
    
   /**
     * Create an instance of Attribute class.
     * @param columnName Attribute colummn name.
     * @param name Attribute name.
     * @param type Attribute type.
     * @param value Attribute value.
     */
    public Attribute(String name, String columnName, AttributeType type, String value) {
    	this.name = name;
    	this.columnName = columnName;
    	this.isSearchable = true;
    	this.isRequired = true;
    	this.type = type;
    	this.defaultValue = value;
    } 
    
    /**
     * Create an instance of  Attribute class.
     * @param id Attribute Id.
     * @param name Attribute name.
     * @param type Attribute type.
     * @param value Attribute value.
     */
    public Attribute(long id, String name,  AttributeType type, String value) {
    	this.id = id;
    	this.name = name;
    	this.columnName = name;
    	this.isSearchable = true;
    	this.isRequired = true;
    	this.type = type;
    	this.defaultValue = value;
    } 
    
    /**
     * Get Id attribute.
     * @return String Id attribute.
     */
    public long getId() {
        return id;
    }
    
    /**
     * Set Id attribute.
     * @param id Id attribute.
     */
    public void setId(long id) {
        this.id = id;
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
    public boolean getIsSearchable() {
        return isSearchable;
    }
    
    /**
     * Set searchable attribute.
     * @param searchable Searchable attribute.
     */
    public void setIsSearchable(boolean searchable) {
        this.isSearchable = searchable;;
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
    
    /**
     * Create and return a copy of this object.
     * @return Attribute Copy of this object.
     */
    @Override
    public Attribute clone() {
        Attribute copy = new Attribute();
        copy.setName(this.name);
        copy.setColumnName(this.columnName);
        copy.setIsSearchable(this.isSearchable);
        copy.setIsRequired(this.isRequired);
        copy.setType(this.type);        
        return copy;
    }
}
