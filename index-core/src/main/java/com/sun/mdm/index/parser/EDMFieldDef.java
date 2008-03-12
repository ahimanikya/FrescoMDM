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
package com.sun.mdm.index.parser;

/**
 *
 * @author  kkao
 */
public class EDMFieldDef {
    String fieldName;
    String displayName; // string
    String displayOrder;// int
    String maxLength;   // int
    String guiType;     // TextBox, MenuList
    String valueType;   // string, etc.
    String keyType;     // true/false
    String valueList;   // SUFFIX, SUFFIX, GENDER, MSTATUS, RACE, ETHNIC, 
                        // RELIGION, LANGUAGE, NATIONAL, CITIZEN, ADDRTYPE, PHONTYPE
    String valueMask;   // DDDxDDxDDDD, etc.
    String inputMask;   // DDD-DD-DDDD, etc.
    boolean usedInSearchScreen;
    String requiredInSearchScreen;
    boolean usedInSearchResult;
    boolean report;
        
    /** Creates a new instance of EDMFieldDef */
    EDMFieldDef() {
    }
        
    /** Creates a new instance of EDMFieldDef */
    EDMFieldDef(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
    *@param String fieldName
    */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
    *@return String fieldName
    */
    public String getFieldName() {
        return fieldName;
    }

    /**
    *@param String displayName
    */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
    *@return String displayName
    */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
    *@param String displayOrder
    */
    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    /**
    *@return String displayOrder
    */
    public String getDisplayOrder() {
        return displayOrder;
    }
    
    /**
    *@param String maxLength
    */
    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }
    
    /**
    *@return String maxLength
    */
    public String getMaxLength() {
        return maxLength;
    }
    
    /**
    *@param String guiType
    */
    public void setGuiType(String guiType) {
        this.guiType = guiType;
    }
    
    /**
    *@return String guiType
    */
    public String getguiType() {
        return guiType;
    }
    
    /**
    *@param String valueMask
    */
    public void setValueMask(String valueMask) {
        this.valueMask = valueMask;
    }
    
    /**
    *@return String valueMask
    */
    public String getValueMask() {
        return valueMask;
    }
    
    /**
    *@param String inputMask
    */
    public void setInputMask(String inputMask) {
        this.inputMask = inputMask;
    }
    
    /**
    *@return String inputMask
    */
    public String getInputMask() {
        return inputMask;
    }
    
    /**
    *@param boolean usedInSearchScreen
    */
    public void setUsedInSearchScreen(boolean usedInSearchScreen) {
        this.usedInSearchScreen = usedInSearchScreen;
    }
    
    /**
    *@return boolean usedInSearchScreen
    */
    public boolean getUsedInSearchScreen() {
        return usedInSearchScreen;
    }
    
    /**
    *@param String requiredInSearchScreen
    */
    public void setRequiredInSearchScreen(String requiredInSearchScreen) {
        this.requiredInSearchScreen = requiredInSearchScreen;
    }
    
    /**
    *@return String requiredInSearchScreen
    */
    public String getRequiredInSearchScreen() {
        return requiredInSearchScreen;
    }

    /**
    *@param boolean usedInSearchResult
    */
    public void setUsedInSearchResult(boolean usedInSearchResult) {
        this.usedInSearchResult = usedInSearchResult;
    }

    /**
    *@return boolean usedInSearchResult
    */
    public boolean getUsedInSearchResult() {
        return usedInSearchResult;
    }
    
    /**
    *@param boolean report
    */
    public void setReport(boolean report) {
        this.report = report;
    }
    
    /**
    *@return boolean report
    */
    public boolean getReport() {
        return report;
    }
    
    /**
    *@param String valueType
    */
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    /**
    *@return String valueType
    */
    public String getValueType() {
        return this.valueType;
    }
    
    /**
    *@param String keyType
    */
    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
    
    /**
    *@return String keyType
    */
    public String getKeyType() {
        return this.keyType;
    }
    
    /**
    *@param String valueList
    */
    public void setValueList(String valueList) {
        this.valueList = valueList;
    }
    
    /**
    *@return String valueList
    */
    public String getValueList() {
        return this.valueList;
    }
}
