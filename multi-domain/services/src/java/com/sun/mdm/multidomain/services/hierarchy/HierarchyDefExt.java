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
package com.sun.mdm.multidomain.services.hierarchy;

import com.sun.mdm.multidomain.services.model.AttributeDefExt;

import java.util.List;
import java.util.ArrayList;

/**
 * HierarchyDefExt class.
 * @author cye
 */
public class HierarchyDefExt {
    
    /* data model   
    private String name;
    private String description;
    private String id; 
    private String domain;
    private String plugin;
    private String pluginInfo;     
    private String dateFormat;
    private String startDate;
    private String endDate;
    private String purgeDate;
    private String startDateRequired;
    private String endDateRequired;
    private String purgeDateRequired; */
     /* display model 
    private String displayName; */
    
    private List<AttributeDefExt> fixedAttributes;  
    private List<AttributeDefExt> extendedAttributes;

    public  HierarchyDefExt(){
        fixedAttributes = new ArrayList<AttributeDefExt>();
        extendedAttributes = new ArrayList<AttributeDefExt>();        
    }
    
    public void setFixedAttributes(ArrayList<AttributeDefExt> fixedAttributes) {
        this.fixedAttributes = fixedAttributes;
    }
    
    public List<AttributeDefExt> gettFixedAttributes(){
        return fixedAttributes;
    }
    
    public void setExtendedAttributes(List<AttributeDefExt> extendedAttributes){
        this.extendedAttributes = extendedAttributes;
    }
    
    public List<AttributeDefExt> getExtendedAttributes(){
        return extendedAttributes;
    }
    
    public String getName(){
        AttributeDefExt aDef = getFixedAttribute("name");
        return aDef != null ? aDef.getDefaultValue() : null;
    }
    
    public void setName(String name){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("name");
        aDef.setDefaultValue(name);
        aDef.setDataType("String");
        setFixedAttribute(aDef);
    }
    
    public String getDescription(){
        AttributeDefExt aDef = getFixedAttribute("description");
        return aDef != null ? aDef.getDefaultValue() : null;
    }
    
    public void setDescription(String name){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("description");
        aDef.setDefaultValue(name);
        aDef.setDataType("String");
        setFixedAttribute(aDef);
    } 
    
    public String getId(){
        AttributeDefExt aDef = getFixedAttribute("id");
        return aDef != null ? aDef.getDefaultValue() : null;        
    }
    
    public void setId(String id){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("id");
        aDef.setDefaultValue(id);
        aDef.setDataType("String");
        setFixedAttribute(aDef);
    }
    
    public String getDomain(){
        AttributeDefExt aDef = getFixedAttribute("domain");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setDomain(String sourceDomain){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("domain");
        aDef.setDefaultValue(sourceDomain);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
        
    public String getPlugin(){
        AttributeDefExt aDef = getFixedAttribute("plugin");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setPlugin(String plugin){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("plugin");
        aDef.setDefaultValue(plugin);
        aDef.setDataType("String");
        setFixedAttribute(aDef);                        
    }

    public String getPluginInfo(){
        AttributeDefExt aDef = getFixedAttribute("pluginInfo");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setPluginInfo(String pluginInfo){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("pluginInfo");
        aDef.setDefaultValue(pluginInfo);
        aDef.setDataType("String");
        setFixedAttribute(aDef);                        
    }
       
    public String getDateFormat(){
        AttributeDefExt aDef = getFixedAttribute("dateFormat");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setDateFormat(String dateFormat){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("biDirection");
        aDef.setDefaultValue(dateFormat);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
    
    public String getStartDate(){
        AttributeDefExt aDef = getFixedAttribute("startDate");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setStartDate(String startDate){ 
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("startDate");
        aDef.setDefaultValue(startDate.toUpperCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public String getEndDate(){
        AttributeDefExt aDef = getFixedAttribute("endDate");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setEndDate(String endDate){ 
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("endDate");
        aDef.setDefaultValue(endDate.toLowerCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public String getPurgeDate(){
        AttributeDefExt aDef = getFixedAttribute("purgeDate");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setPurgeDate(String purgeDate){ 
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("purgeDate");
        aDef.setDefaultValue(purgeDate.toLowerCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public String getStartDateRequired(){
        AttributeDefExt aDef = getFixedAttribute("startDateRequired");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setStartDateRequired(String startDateRequired){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("startDateRequired");
        aDef.setDefaultValue(startDateRequired.toUpperCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                            
    }
    
    public String getEndDateRequired(){
        AttributeDefExt aDef = getFixedAttribute("endDateRequired");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setEndDateRequired(String endDateRequired){ 
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("endDateRequired");
        aDef.setDefaultValue(endDateRequired.toLowerCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                                    
    }
    
    public String getPurgeDateRequired(){
        AttributeDefExt aDef = getFixedAttribute("purgeDateRequired");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
        
    public void setPurgeDateRequired(String purgeDateRequired){ 
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("endDateRequired");
        aDef.setDefaultValue(purgeDateRequired.toLowerCase());
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                                    
    }
    
    public String getDisplayName(){
        AttributeDefExt aDef = getFixedAttribute("displayName");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setDisplayName(String displayName){
        AttributeDefExt aDef = new AttributeDefExt();
        aDef.setName("displayName");
        aDef.setDefaultValue(displayName);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
    
    public AttributeDefExt getFixedAttribute(String name) {
        AttributeDefExt attributeDef = null;
        if (fixedAttributes != null && name != null) {
            for (AttributeDefExt aDef : fixedAttributes) {
                if (name.equals(aDef.getName())) {
                    attributeDef = aDef;
                    break;
                }
            }
        }
        return attributeDef;
    }

    public void setFixedAttribute(AttributeDefExt aDef) {        
        if (fixedAttributes == null ) {
            fixedAttributes = new ArrayList<AttributeDefExt>();
        }            
        fixedAttributes.add(aDef);
    }
    
    public AttributeDefExt getExtendeddAttribute(String name) {
        AttributeDefExt attributeDef = null;
        if (extendedAttributes != null && name != null) {
            for (AttributeDefExt aDef : extendedAttributes) {
                if (name.equals(aDef.getName())) {
                    attributeDef = aDef;
                    break;
                }
            }
        }
        return attributeDef;
    }    
    
    public void setExtendedAttribute(AttributeDefExt aDef) {        
        if (extendedAttributes == null ) {
            extendedAttributes = new ArrayList<AttributeDefExt>();
        }            
        extendedAttributes.add(aDef);
    }    
}
