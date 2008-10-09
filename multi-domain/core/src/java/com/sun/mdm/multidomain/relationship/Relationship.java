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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Relationship class.
 * @author cye
 * @author SwaranjitDua
 */
public class Relationship {
    private RelationshipType relationshipType;
    private String id;
    private Date startDate;
    private Date endDate;
    private Date purgeDate;
    private Map<Attribute, String> attributeValues = new HashMap<Attribute, String>();
    
    /**
     * Create an instance of Relationship.
     */
    public Relationship() {    	
    }
    
    /**
     * Get relationship Id.
     * @return String Relationship Id.
     */
    public String getId() {
    	return id;
    }
    
    /**
     * Set relationship Id.
     * @param id Relationship Id.
     */
    public void setId(String id){
    	this.id = id;
    }
    
    /**
     * Get relationship type.
     * @return RelationshipType Relationship type.
     */
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }
    
    /**
     * Set relationship type.
     * @param relationshipType Relationship type.
     */
    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }    
    
    /**
     * Get start date attribute.
     * @return Date Start date attribute.
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * Set Start date attribute.
     * @param startDate Start date attribute.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }  
    
    /**
     * Get end date attribute.
     * @return Date End date attribute.
     */    
    public Date getEndDate() {
        return endDate;
    }
  
    /**
     * Set End date attribute.
     * @param endDate End date attribute.
     */    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
}
