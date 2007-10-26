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
package com.sun.mdm.index.project.ui.wizards.generator;

/**
 * The settings for one field in the object model
 */
public class FieldSettings {
    private String fieldQualifier; // e.g. Person.Address
    private String decoratedFieldQualifier; // e.g. Person.Address[*]    
    private String unQualifiedFieldName; // e.g. City
    private String matchTypeID; // e.g. Address
    private String displayName; // e.g. Home address
    private boolean searchable; // e.g. true
    private boolean inResultList; // e.g. true
    private boolean blockOn; // e.g. true
    private String fieldtype; // e.g. String
    private int fieldsize; // e.g. 8
    private boolean updateable; // e.g. true
    private boolean required; // e.g. false
    private boolean keytype; // e.g. false
    private boolean visible; // e.g. true

    /**
     * Get the field qualifier for this field
     * @return the field qualifier
     */
    public String getFieldQualifier() {
        return fieldQualifier;
    }

    /**
     * Set the field qualifier for this field
     * @param val the field qualifier
     */
    public void setFieldQualifier(String val) {
        fieldQualifier = val;
    }

    /**
     * Get the decorated field qualifier for this field
     * @return the decorated field qualifier
     */
    public String getDecoratedFieldQualifier() {
        return decoratedFieldQualifier;
    }

    /**
     * Set the deoorated field qualifier for this field
     * @param val the decorated field qualifier
     */
    public void setDecoratedFieldQualifier(String val) {
        decoratedFieldQualifier = val;
    }

    /**
     * Get the unqualified field name for this field
     * @return the unqualified field name
     */
    public String getUnQualifiedFieldName() {
        return unQualifiedFieldName;
    }

    /**
     * Set the unqualified field name for this field
     * @param val the unqualified field name
     */
    public void setUnQualifiedFieldName(String val) {
        unQualifiedFieldName = val;
    }

    /**
     * Get the match type ID for this field
     * @return the match type ID
     */
    public String getMatchTypeID() {
        return matchTypeID;
    }

    /**
     * Set the match type ID for this field
     * @param val the match type ID
     */
    public void setMatchTypeID(String val) {
        matchTypeID = val;
    }

    /**
     * Get the searchable attribue for this field
     * @return the searchable attribue
     */
    public boolean getSearchable() {
        return searchable;
    }

    /**
     * Set the searchable attribue for this field
     * @param val the searchable attribue
     */
    public void setSearchable(boolean val) {
        searchable = val;
    }

    /**
     * Get the result list attribute for this field
     * @return the result list attribute
     */
    public boolean getInResultList() {
        return inResultList;
    }

    /**
     * Set the result list attribute for this field
     * @param val the result list attribute
     */
    public void setInResultList(boolean val) {
        inResultList = val;
    }

    /**
     * Get the include in block attribute for this field
     * @return the include in block attribute
     */
    public boolean getBlockOn() {
        return blockOn;
    }

    /**
     * Set the include in block attribute for this field
     * @param val the include in block attribute
     */
    public void setBlockOn(boolean val) {
        blockOn = val;
    }

    /**
     * Get the display name for this field
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the display name for this field
     * @param val the display name
     */
    public void setDisplayName(String val) {
        displayName = val;
    }

    /**
     * Get the field type for this field
     * @return the field type
     */
    public String getFieldType() {
        return displayName;
    }

    /**
     * Set the field type for this field
     * @param val the field type
     */
    public void setFieldType(String val) {
        displayName = val;
    }

    /**
     * Get the field size for this field
     * @return the field size
     */
    public int getFieldSize() {
        return fieldsize;
    }

    /**
     * Set the field size for this field
     * @param val the field size
     */
    public void setFieldSize(int val) {
        fieldsize = val;
    }

    /**
     * Get the updateable attribute for this field
     * @return the updateable attribute
     */
    public boolean getUpdateable() {
        return updateable;
    }

    /**
     * Set the updateable attribute for this field
     * @param val the updateable attribute
     */
    public void setUpdateable(boolean val) {
        updateable = val;
    }

    /**
     * Get the required attribute for this field
     * @return the required attribute
     */
    public boolean getRequired() {
        return required;
    }

    /**
     * Set the required attribute for this field
     * @param val the required attribute
     */
    public void setRequired(boolean val) {
        required = val;
    }

    /**
     * Get the key type attribute for this field
     * @return the key type attribute
     */
    public boolean getKeytype() {
        return keytype;
    }

    /**
     * Set the key type attribute for this field
     * @param val the key type attribute
     */
    public void setKeytype(boolean val) {
        keytype = val;
    }

    /**
     * Get the visible attribute for this field
     * @return the visible attribute
     */
    public boolean getVisible() {
        return visible;
    }

    /**
     * Set the visible attribute for this field
     * @param val the visible attribute
     */
    public void setVisible(boolean val) {
        visible = val;
    }
}
