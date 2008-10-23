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
package com.sun.mdm.multidomain.services.relationship;

import java.util.Map;
import java.util.HashMap;

/**
 * AttributeDefinition class.
 * @author cye
 */
public class AttributeDefinition {
    
    private Map<String, String> fields;         
    /* data object mode
    private String id;
    private String name;
    private String columnName;
    private boolean searchable;
    private boolean isRequired;
    private String dataType; 
    private String dataSize;
    private String pattern;
    private String codeModel;
    private String defaultValue; */ 
    /* display model 
    private String displayName;
    private String displayOrder;
    private String maxLength;
    private String guiType;
    private String valueList; */
    
    public AttributeDefinition() {
        fields = new HashMap<String, String>();
    }   
    public String getId(){
        return fields.get("id");
    }
    public void setId(String id){
        fields.put("id", id);
    }
    public String getName(){
        return fields.get("name");
    }
    public void setName(String name){
        fields.put("name", name);
    }    
    public String getColumnName(){
        return fields.get("columnName");
    }
    public void setColumnName(String columnName){
        fields.put("columnName", columnName);
    }  
    public boolean getSearchable(){
        String value = fields.get("searchable");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }
    }
    public void setSearchable(boolean searchable) {
        fields.put("searchable", searchable ? "true" : "false");
    }
    public boolean getIsRequired(){
        String value = fields.get("isRequired");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        } 
    }
    public void setIsRequired(boolean isRequired){
        fields.put("isRequired", isRequired ? "true" : "false");        
    }    
    public void setDataType(String dataType) {
       fields.put("dataType", dataType);
    }
    public String getDataType(){
        return fields.get("dataType");
    }
    public String getDataSize(){
        return fields.get("dataSize");
    }
    public void setDataSize(String dataSize){
        fields.put("dataSize", dataSize);
    }        
    public String getPattern(){
        return fields.get("pattern");
    }
    public void setPattern(String pattern){
       fields.put("pattern", pattern);
    }
    public String getCodeModel(){
        return fields.get("codeModel");
    }
    public void setCodeModel(String codeModel){
        fields.put("codeModel", codeModel);
    }
    public String getDefaultValue(){
        return fields.get("defaultValue");
    }
    public void setDefaultValue(String defaultValue){
        fields.put("defaultValue", defaultValue);
    }
    public String getDisplayName(){
        return fields.get("displayName");
    }
    public void setDisplayName(String displayName){
        fields.put("displayName", displayName);
    }
    public String getDisplayOrder(){
        return fields.get("displayOrder");
    }
    public void setDisplayOrder(String displayOrder){
        fields.put("displayOrder", displayOrder);
    }
    public String getMaxLength(){
       return fields.get("maxLength");
    }
    public void setMaxLength(String maxLength) {
        fields.put("maxLength", maxLength);
    }
    public String getGuiType(){
        return fields.get("guiType");
    }
    public void setGuiType(String guiType){
        fields.put("guiType",guiType);
    }
    public String getValueList(){
        return fields.get("valueList");
    }
    public void settValueList(String valueList){
        fields.put("valueList", valueList);
    }
    public Map<String, String> getFields() {
        return fields;
    }
    public void setFields(Map<String, String> fields) {
        this.fields =  fields;
    }
    public int size() {
        return fields.size();
    }
}
