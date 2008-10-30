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
package com.sun.mdm.multidomain.group;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import com.sun.mdm.multidomain.attributes.AttributesDef;
import com.sun.mdm.multidomain.attributes.Attribute;
import java.io.Serializable;

/**
 * Group class.
 * represents a group instance of a particular groupType ex. instance of Household group type
 * @author SwaranjitDua
 */
public class Group implements Serializable{
    private int groupID;
    private Date effectiveFromDate;
    private Date effectiveToDate;
    private Date purgeDate;
    private Map<Attribute, String> attributeValues = new HashMap<Attribute, String>();
    private AttributesDef attributeDef;
    
    /**
     * Create an instance of Group.
     */
    public Group() {    	
    }
    
    /**
     * Get group Id.
     * @return String Group Id.
     */
    public int getGroupID() {
    	return groupID;
    }
    
    /**
     * Set group Id.
     * @param groupID Group Id.
     */
    public void setGroupID(int groupID){
    	this.groupID = groupID;
    }
    
        /**
     * Get start date attribute.
     * @return Date Start date attribute.
     */
    public Date getEffectiveFromDate() {
        return effectiveFromDate;
    }
    
    /**
     * Set Start date attribute.
     * @param effectiveFromDate Start date attribute.
     */
    public void setEffectiveFromDate(Date effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }  
    
    /**
     * Get end date attribute.
     * @return Date End date attribute.
     */    
    public Date getEffectiveToDate() {
        return effectiveToDate;
    }
  
    /**
     * Set End date attribute.
     * @param effectiveToDate End date attribute.
     */    
    public void setEffectiveToDate(Date effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }
    
    /**
     * Get Purge date attribute.
     * @return Date Purge date attribute.
     */     
    public Date getPurgeDate() {
        return purgeDate;
    }
   
    /**
     * Set Purge date attribute.
     * @param purgeDate Purge date attribute.
     */       
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    } 
    
    /**
     * Set attribute value.
     * @param attribute Attribute.
     * @param value Attribute value.
     */
    public void setAttributeValue(Attribute attribute, String value) {
    	attributeValues.put(attribute, value);
    }
    
    /**
     * Get attribute value.
     * @param attribute Attribute.
     * @return String Attribute value.
     */
    public String getAttributeValue(Attribute attribute) {
    	return attributeValues.get(attribute);
    }
    
    /**
     * Set attribute value.
     * @param attribute Attribute.
     * @param value Attribute value.
     */
    public void setAttributes(Map<Attribute,String> attributeValues) {
    	this.attributeValues = attributeValues;
    }
    
    /**
     * Get attribute value.
     * @param attribute Attribute.
     * @return String Attribute value.
     */
    public Map<Attribute,String> getAttributes() {
    	return attributeValues;
    }        
    
}
