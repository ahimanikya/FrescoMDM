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

import com.sun.mdm.multidomain.services.model.AttributeDef;

import java.util.List;
import java.util.ArrayList;

/**
 * RelationshipDefinition class.
 * @author cye
 */
public class RelationshipDefinition {
    /* data model   
    private String name;
    private String id; 
    private String sourceDomain;
    private String targetDomain;
    private String plugin;
    private String sourceRoleName;
    private String targetRoleName;    
    private boolean biDirection;  
    private String dateFormat;
    private boolean startDate;
    private boolean endDate;
    private boolean purgeDate;
    private boolean startDateRequired;
    private boolean endDateRequired;
    private boolean purgeDateRequired; */
     /* display model 
    private String displayName; */
    
    private List<AttributeDef> fixedAttributes;  
    private List<AttributeDef> extendedAttributes;

    public  RelationshipDefinition(){
        fixedAttributes = new ArrayList<AttributeDef>();
    }
    
    public void setFixedAttributes(ArrayList<AttributeDef> fixedAttributes) {
        this.fixedAttributes = fixedAttributes;
    }
    
    public List<AttributeDef> gettFixedAttributes(){
        return fixedAttributes;
    }
    
    public void setExtendedAttributes(List<AttributeDef> extendedAttributes){
        this.extendedAttributes = extendedAttributes;
    }
    
    public List<AttributeDef> getExtendedAttributes(){
        return extendedAttributes;
    }
    
    public String getName(){
        AttributeDef aDef = getFixedAttribute("name");
        return aDef != null ? aDef.getDefaultValue() : null;
    }
    
    public void setName(String name){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("name");
        aDef.setDefaultValue(name);
        aDef.setDataType("String");
        setFixedAttribute(aDef);
    }
    
    public String getId(){
        AttributeDef aDef = getFixedAttribute("id");
        return aDef != null ? aDef.getDefaultValue() : null;        
    }
    
    public void setId(String id){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("id");
        aDef.setDefaultValue(id);
        aDef.setDataType("String");
        setFixedAttribute(aDef);
    }
    
    public String getSourceDomain(){
        AttributeDef aDef = getFixedAttribute("sourceDomain");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setSourceDomain(String sourceDomain){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("sourceDomain");
        aDef.setDefaultValue(sourceDomain);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
    
    public String getTargetDomain(){
        AttributeDef aDef = getFixedAttribute("targetDomain");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setTargetDomain(String targetDomain){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("targetDomain");
        aDef.setDefaultValue(targetDomain);
        aDef.setDataType("String");
        setFixedAttribute(aDef);                
    }
    
    public String getPlugin(){
        AttributeDef aDef = getFixedAttribute("plugin");
        return aDef != null ? aDef.getDefaultValue() : null;                
    }
    
    public void setPlugin(String plugin){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("plugin");
        aDef.setDefaultValue(plugin);
        aDef.setDataType("String");
        setFixedAttribute(aDef);                        
    }
    
    public boolean getBiDirection(){
        AttributeDef aDef = getFixedAttribute("biDirection");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        }
    }
    
    public void setBiDirection(boolean biDirection){ 
        AttributeDef aDef = new AttributeDef();
        aDef.setName("biDirection");
        aDef.setDefaultValue(biDirection ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                
    }
    
    public String getDateFormat(){
        AttributeDef aDef = getFixedAttribute("dateFormat");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setDateFormat(String dateFormat){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("biDirection");
        aDef.setDefaultValue(dateFormat);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
    
    public boolean getStartDate(){
        AttributeDef aDef = getFixedAttribute("startDate");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        }        
    }
    
    public void setStartDate(boolean startDate){ 
        AttributeDef aDef = new AttributeDef();
        aDef.setName("startDate");
        aDef.setDefaultValue(startDate ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public boolean getEndDate(){
        AttributeDef aDef = getFixedAttribute("endDate");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        }        
    }
    
    public void setEndDate(boolean endDate){ 
        AttributeDef aDef = new AttributeDef();
        aDef.setName("endDate");
        aDef.setDefaultValue(endDate ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public boolean getPurgeDate(){
        AttributeDef aDef = getFixedAttribute("purgeDate");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        }        
    }
    
    public void setPurgeDate(boolean purgeDate){ 
        AttributeDef aDef = new AttributeDef();
        aDef.setName("purgeDate");
        aDef.setDefaultValue(purgeDate ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                    
    }
    
    public boolean getStartDateRequired(){
        AttributeDef aDef = getFixedAttribute("startDateRequired");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        } 
    }
    
    public void setStartDateRequired(boolean startDateRequired){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("startDateRequired");
        aDef.setDefaultValue(startDateRequired ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                            
    }
    
    public boolean getEndDateRequired(){
        AttributeDef aDef = getFixedAttribute("endDateRequired");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        } 
    }
    
    public void setEndDateRequired(boolean endDateRequired){ 
        AttributeDef aDef = new AttributeDef();
        aDef.setName("endDateRequired");
        aDef.setDefaultValue(endDateRequired ? "true" : "false");
        aDef.setDataType("String");
        setFixedAttribute(aDef);                                                    
    }
    
    public boolean getPurgeDateRequired(){
        AttributeDef aDef = getFixedAttribute("purgeDateRequired");
        if (aDef != null ) {
            return "true".equalsIgnoreCase(aDef.getDefaultValue());
        } else {
            return false;
        }         
    }
        
    public String getDisplayName(){
        AttributeDef aDef = getFixedAttribute("displayName");
        return aDef != null ? aDef.getDefaultValue() : null;                        
    }
    
    public void setDisplayName(String displayName){
        AttributeDef aDef = new AttributeDef();
        aDef.setName("displayName");
        aDef.setDefaultValue(displayName);
        aDef.setDataType("String");
        setFixedAttribute(aDef);        
    }
    
    public AttributeDef getFixedAttribute(String name) {
        AttributeDef attributeDef = null;
        if (fixedAttributes != null && name != null) {
            for (AttributeDef aDef : fixedAttributes) {
                if (name.equals(aDef.getName())) {
                    attributeDef = aDef;
                    break;
                }
            }
        }
        return attributeDef;
    }

    public void setFixedAttribute(AttributeDef aDef) {        
        if (fixedAttributes == null ) {
            fixedAttributes = new ArrayList<AttributeDef>();
        }            
        fixedAttributes.add(aDef);
    }
    
    public AttributeDef getExtendeddAttribute(String name) {
        AttributeDef attributeDef = null;
        if (extendedAttributes != null && name != null) {
            for (AttributeDef aDef : extendedAttributes) {
                if (name.equals(aDef.getName())) {
                    attributeDef = aDef;
                    break;
                }
            }
        }
        return attributeDef;
    }    
    
    public void setExtendedAttribute(AttributeDef aDef) {        
        if (extendedAttributes == null ) {
            extendedAttributes = new ArrayList<AttributeDef>();
        }            
        extendedAttributes.add(aDef);
    }
    
}
