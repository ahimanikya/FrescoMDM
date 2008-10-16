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
package com.sun.mdm.multidomain.association;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Association class. Represents high level generic relationship. 
 * Encapsulates fixed and flexible attributes and values for a concrete association.
 * @author SwaranjitDua
 */
public abstract class Association {
    private AssociationValue associationValue;
                
    /**
     * Get Association Value.
     * @return AssociationValue.
     */
    public AssociationValue getAssociationValue() {
        return associationValue;
    }
    
    /**
     * Set AssociationValue.
     * @param AssociationValue AssociationValue.
     */
    public void setAssociationValue(AssociationValue associationValue) {
        this.associationValue = associationValue;
    } 
    
    /**
     * get AssocationType that is related to this AssociationValue
     * @return AssociationType
     */
    AssociationType getAssocationType() {
    	return associationValue.getAssociationType();
    }
    
    /**
     * Get association type.
     * @return AssociationType Association type.
     */
    public AssociationType getAssociationType() {
        return associationValue.getAssociationType();
    }
    
    /**
     * Set association type.
     * @param associationType Association type.
     */
    public void setAssociationType(AssociationType associationType) {
        associationValue.setAssociationType(associationType);
    }    
    
    /**
     * Get effective from date attribute.
     * @return Date Start date attribute.
     */
    public Date getEffectiveFromDate() {
        return associationValue.getEffectiveFromDate();
    }
    
    /**
     * Set effective From date attribute.
     * @param effectiveFromDate Start date attribute.
     */
    public void setEffectiveFromDate(Date effectiveFromDate) {
    	associationValue.setEffectiveFromDate(effectiveFromDate);
    }  
    
    /**
     * Get end date attribute.
     * @return Date End date attribute.
     */    
    public Date getEffectiveToDate() {
        return associationValue.getEffectiveToDate();
    }
  
    /**
     * Set End date attribute.
     * @param effectiveToDate End date attribute.
     */    
    public void setEffectiveToDate(Date effectiveToDate) {
    	associationValue.setEffectiveToDate(effectiveToDate);
    }
    
    /**
     * Get Purge date attribute.
     * @return Date Purge date attribute.
     */     
    public Date getPurgeDate() {
        return associationValue.getPurgeDate();
    }
   
    /**
     * Set Purge date attribute.
     * @param purgeDate Purge date attribute.
     */       
    public void setPurgeDate(Date purgeDate) {
    	associationValue.setPurgeDate(purgeDate);
    } 
    
    /**
     * Set attribute value.
     * @param attribute Attribute.
     * @param value Attribute value.
     */
    public void setAttributeValue(Attribute attribute, String value) {
    	associationValue.setAttributeValue(attribute, value);
    }
    
    /**
     * Get attribute value.
     * @param attribute Attribute.
     * @return String Attribute value.
     */
    public String getAttributeValue(Attribute attribute) {
    	return associationValue.getAttributeValue(attribute);
    }
    
    /**
     * Set attribute value.
     * @param attribute Attribute.
     * @param value Attribute value.
     */
    public void setAttributes(Map<Attribute,String> attributeValues) {
    	this.associationValue.setAttributes(attributeValues);
    }
    
    /**
     * Get attribute value.
     * @param attribute Attribute.
     * @return String Attribute value.
     */
    public Map<Attribute,String> getAttributes() {
    	return associationValue.getAttributes();
    }        
    
    
}
