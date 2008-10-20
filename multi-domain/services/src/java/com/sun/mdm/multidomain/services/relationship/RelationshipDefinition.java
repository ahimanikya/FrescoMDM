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

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    
    private Map<String, String> fixedAttributes;  
    private List<AttributeDefinition> extendedAttributes;

    public  RelationshipDefinition(){
        fixedAttributes = new HashMap<String, String>();
    }
    public void setFixedAttributes(Map<String, String> fixedAttributes) {
        this.fixedAttributes = fixedAttributes;
    }
    public Map<String, String> gettFixedAttributes(){
        return fixedAttributes;
    }
    public void setExtendedAttributes(List<AttributeDefinition> extendedAttributes){
        this.extendedAttributes = extendedAttributes;
    }
    public List<AttributeDefinition> getExtendedAttributes(){
        return extendedAttributes;
    }
    public String getName(){
        return fixedAttributes.get("name");
    }
    public void setName(String name){
        fixedAttributes.put("name", name);
    }
    public String getId(){
        return fixedAttributes.get("id"); 
    }
    public void setId(String id){
        fixedAttributes.put("id",id);
    }
    public String getSourceDomain(){
        return fixedAttributes.get("sourceDomain");
    }
    public void setSourceDomain(String sourceDomain){
        fixedAttributes.put("sourceDomain",sourceDomain);
    }
    public String getTargetDomain(){
        return fixedAttributes.get("targetDomain");
    }
    public void setTargetDomain(String targetDomain){
        fixedAttributes.put("targetDomain",targetDomain);
    }    
    public String getPlugin(){
        return fixedAttributes.get("plugin");
    }
    public void setPlugin(String plugin){
        fixedAttributes.put("plugin",plugin);
    }
    public boolean getBiDirection(){
        String value = fixedAttributes.get("biDirection");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setBiDirection(boolean biDirection){ 
        fixedAttributes.put("biDirection", biDirection ? "true" : "false");  
    }
    public String getDateFormat(){
        return fixedAttributes.get("dateFormat");
    }
    public void setDateFormat(String dateFormat){
        fixedAttributes.put("dateFormat", dateFormat);
    }
    public boolean getStartDate(){
        String value = fixedAttributes.get("startDate");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setStartDate(boolean startDate){ 
        fixedAttributes.put("startDate", startDate ? "true" : "false");  
    }   
    public boolean getEndDate(){
        String value = fixedAttributes.get("endDate");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setEndDate(boolean endDate){ 
        fixedAttributes.put("endDate", endDate ? "true" : "false");  
    }      
    public boolean getPurgeDate(){
        String value = fixedAttributes.get("purgeDate");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setPurgeDate(boolean purgeDate){ 
        fixedAttributes.put("purgeDate", purgeDate ? "true" : "false");  
    }         
    public boolean getStartDateRequired(){
        String value = fixedAttributes.get("startDateRequired");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setStartDateRequired(boolean startDateRequired){ 
        fixedAttributes.put("startDateRequired", startDateRequired ? "true" : "false");  
    }     
    public boolean getEndDateRequired(){
        String value = fixedAttributes.get("endDateRequired");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }
    public void setEndDateRequired(boolean endDateRequired){ 
        fixedAttributes.put("endDateRequired", endDateRequired ? "true" : "false");  
    }      
    public boolean getPurgeDateRequired(){
        String value = fixedAttributes.get("purgeDateRequired");
        if(value != null && value.equals("true")){
            return true;
        } else {
            return false;
        }     
    }    
    public String getDisplayName(){
        return fixedAttributes.get("displayName");
    }
    public void setDisplayName(String displayName){
         fixedAttributes.put("displayName", displayName);
    }
}
