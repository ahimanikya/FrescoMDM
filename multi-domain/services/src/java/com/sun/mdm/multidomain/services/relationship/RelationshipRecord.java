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

import java.util.Date;

/**
 * RelationshipRecord class.
 * @author cye
 */
public class RelationshipRecord extends AttributeList {
    
    private String id;
    private String name;
    private String sourceDomain;
    private String targetDomain;
    private String sourceEUID;
    private String targetEUID;
    private Date startDate;
    private Date endDate;
    private Date purgeDate;
    
    /**
     * Create an instance of Relationship.
     */
    public RelationshipRecord() {    	
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
     * @param relationshipID Relationship Id.
     */
    public void setId(String id){
    	this.id = id;
    }
    
    /** 
     * Get source domain EUID
     * @return sourceEUID
     */
    public String getSourceEUID() {
    	return sourceEUID;
    }
    
    /**
     *  Set source domain EUID
     * @param sourceEUID
     */
    public void setSourceEUID(String sourceEUID) {
    	this.sourceEUID = sourceEUID;
    }
    
    /** 
     * Get target domain EUID
     * @return targetEUID
     */
    public String getTargetEUID() {
    	return targetEUID;
    }
    
    /**
     *  Set target domain EUID
     * @param targetEUID
     */
    public void setTargetEUID(String targetEUID) {
    	this.targetEUID = targetEUID;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getStartDate() {
        return startDate;
    } 
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    }        
}