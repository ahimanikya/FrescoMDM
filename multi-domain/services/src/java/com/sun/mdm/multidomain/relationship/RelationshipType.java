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

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * RelationshipType class.
 * @author cye
 */
public class RelationshipType {
    
    public static final int RELATIONSHIP = 0;
    public static final int HIERARCHY = 1;
    public static final int GROUP = 2;
    public static final int UNIDIRECTIONAL= 0;
    public static final int BIDIRECTIONAL = 1;

    public enum RelationshipMode {RELATIONSHIP,HIERARCHY, GROUP};
    public enum DirectionMode {UNIDIRECTIONAL,BIDIRECTIONAL};
    
    private String name;
    private String displayName;
    private String id; //unique primary key across domains being used during run time.
    private String sourceDomain;
    private String targetDomain;
    private String sourceRoleName;
    private String targetRoleName;    
    private RelationshipMode mode;
    private DirectionMode direction;    
    private boolean effectiveFromRequired;
    private boolean effectiveToRequired;
    private boolean purgeDateRequired;
    private boolean effectiveFrom;
    private boolean effectiveTo;
    private boolean purgeDate;     
    private List<Attribute> attributes; //fixed attributes and extended attributes.

    /**
     * Create an instance of RelationshipType. 
     */
    public RelationshipType() {    	
    }
    
    /**
     * Create an instance of RelationshipType.
     * @param name
     * @param displayName
     * @param id
     */
    public RelationshipType(String name, String displayName, String id) {
    	this.name = name;
    	this.displayName = displayName;
    	this.id = id;
    	this.mode = RelationshipMode.RELATIONSHIP;
    	this.direction = DirectionMode.UNIDIRECTIONAL;
    	this.effectiveFromRequired = true;
    	this.effectiveToRequired = true;
    	this.purgeDateRequired = true;
    	this.effectiveFrom = true;
    	this.effectiveTo = true;
    	this.purgeDate = true;
    	this.attributes = new ArrayList<Attribute>();    	
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }    
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getSourceDomain() {
        return sourceDomain;
    }
    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }
    public String getTargetDomain() {
        return targetDomain;
    }
    public void setTargetDomain(String targetDomain) {
        this.targetDomain = targetDomain;
    }
    public String getSourceRoleName() {
        return sourceRoleName;
    }
    public void setSourceRoleName(String sourceRoleName) {
        this.sourceRoleName = sourceRoleName;
    }
    public String getTargetRoleName() {
        return targetRoleName;
    }
    public void setTargetRoleName(String targetRoleName) {
        this.targetRoleName = targetRoleName;
    }    
    public RelationshipMode getMode() {
        return mode;
    }
    public void setMode(RelationshipMode mode) {
        this.mode = mode;
    }
    public DirectionMode getDirection() {
        return direction;
    }
    public void setDirection(DirectionMode direction) {
        this.direction = direction;
    }        
    public boolean getEffectiveFromRequired() {
    	return effectiveFromRequired;
    }
    public void setEffectiveFromRequired(boolean effectiveFromRequired) {
    	this.effectiveFromRequired = effectiveFromRequired;
    }    
    public boolean getEffectiveToRequired() {
    	return effectiveToRequired;
    }
    public void setEffectiveToRequired(boolean effectiveToRequired) {
    	this.effectiveToRequired = effectiveToRequired; 
    }    
    public boolean getPurgeDateRequired() {
    	return purgeDateRequired;
    }
    public void setPurgeDateRequired(boolean purgeDateRequired) {
    	this.purgeDateRequired = purgeDateRequired;
    }   
    public boolean getEffectiveFrom() {
    	return effectiveFrom;
    }
    public void setEffectiveFrom(boolean effectiveFrom) {
    	this.effectiveFrom = effectiveFrom;
    }    
    public boolean getEffectiveTo() {
    	return effectiveTo;
    }
    public void setEffectiveTo(boolean effectiveTo) {
    	this.effectiveTo = effectiveTo;
    }    
    public boolean getPurgeDate(){     
    	return purgeDate;
	}
    public void setPurgeDate(boolean purgeDate){     
    	this.purgeDate = purgeDate;
	}    
    public List<Attribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }    
    public void setAttribute(Attribute attribute) {
        attributes.add(attribute);
    }    
    
}
