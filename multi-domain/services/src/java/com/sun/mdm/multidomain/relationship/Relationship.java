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
 */
public class Relationship {
    private RelationshipType relationshipType;
    private Date startDate;
    private Date endDate;
    private Date purgeDate;
    private Map<Attribute, String> attributeValues = new HashMap<Attribute, String>();
    
    public Relationship() {    	
    }
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }
    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }    
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }    
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    }
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    } 
    public void setAttribute(Attribute attribute, String value) {
    	attributeValues.put(attribute, value);
    }
    public String getAttribute(Attribute attribute) {
    	return attributeValues.get(attribute);
    }        
}
