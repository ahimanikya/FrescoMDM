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
package com.sun.mdm.multidomain.hierarchy;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
//import com.sun.mdm.multidomain.attributes.AttributesValue;
import com.sun.mdm.multidomain.attributes.Attribute;

/**
 * HierarchyNode  class.
 * associates a particular parent EUID and child EUID in an hierarchy.
 * @author SwaranjitDua
 */
public class HierarchyNode implements Serializable {
    private int nodeID; // unique node identified by this id.
    private String EUID; 
    private String parentEUID; 
    private Date effectiveFromDate;
    private Date effectiveToDate;
    private Date purgeDate;
    private Map<Attribute, String> attributeValues = new HashMap<Attribute, String>();
    private HierarchyDef hierarchyDef;
    
 //   private AttributesValue attValues;
    
    
    /**
     * Create an instance of Hierarchy.
     */
    public HierarchyNode() {    	
    }
    
    /**
     * Get hierarchy Node Id.
     * @return String Hierarchy Node Id.
     */
    public int getNodeID() {
    	return nodeID;
    }
    
    /**
     * Set hierarchy Node Id.
     * @param nodeID Hierarchy Node Id.
     */
    public void setNodeID(int nodeID){
    	this.nodeID = nodeID;
    }
    
    /** 
     * Get Parent EUID
     * @return parentEUID
     */
    public String getparentEUID() {
    	return parentEUID;
    }
    
    /**
     *  set parentEUID
     * @param parentEUID
     */
    public void setParentEUID(String parentEUID) {
    	this.parentEUID = parentEUID;
    }
    
    /** 
     * Get child EUID
     * @return childEUID
     */
    public String getEUID() {
    	return EUID;
    }
    
    /**
     *  set childEUID
     * @param childEUID
     */
    public void setEUID(String EUID) {
    	this.EUID = EUID;
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