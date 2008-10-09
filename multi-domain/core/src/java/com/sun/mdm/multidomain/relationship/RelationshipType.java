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
import java.util.Iterator;

/**
 * RelationshipType class.
 * @author SwaranjitDua
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
    private List<Attribute> attributes; //extended attributes.

    /**
     * Create an instance of RelationshipType. 
     */
    public RelationshipType() {    	
    }
    /**
     * Create an instance of RelationshipType.
     * @param name Relationship type name. 
     * @param displayName Relationship type display name.
     * @param id Relationship type Id.
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
    
    /**
     * Get relationship type name.
     * @return String relationship type name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set relationship type name.
     * @param name Relationship type name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get relationship type Id.
     * @return String Relationship type Id.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Set relationship type Id.
     * @param id Relationship type Id.
     */
    public void setId(String id) {
        this.id = id;
    }    
    
    /**
     * Get Relationship type display name.
     * @return String Relationship type display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Set Relationship type display name.
     * @param displayName Relationship type display name.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Get Relationship type source domain.
     * @return String Relationship type source domain.
     */
    public String getSourceDomain() {
        return sourceDomain;
    }
    
    /**
     * Set relationship type source domain.
     * @param sourceDomain Relationship type source domain.
     */
    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }
    
    /**
     * Get relationship type target domain.
     * @return String Relationship type target domain.
     */
    public String getTargetDomain() {
        return targetDomain;
    }
    
    /**
     * Set relationship type target domain.
     * @param targetDomain Relationship type target domain.
     */
    public void setTargetDomain(String targetDomain) {
        this.targetDomain = targetDomain;
    }
    
    /**
     * Get relationship type source role name.
     * @return String Relationship type source role name.
     */
    public String getSourceRoleName() {
        return sourceRoleName;
    }
    
    /**
     * Set relationship type source role name.
     * @param sourceRoleName Relationship type source role name.
     */
    public void setSourceRoleName(String sourceRoleName) {
        this.sourceRoleName = sourceRoleName;
    }
    
    /**
     * Get relationship type target role name.
     * @return String Relationship type target role name.
     */
    public String getTargetRoleName() {
        return targetRoleName;
    }
    
    /**
     * Set relationship type target role name.
     * @param targetRoleName Relationship type target role name.
     */
    public void setTargetRoleName(String targetRoleName) {
        this.targetRoleName = targetRoleName;
    }    
    
    /**
     * Get relationship type mode.
     * @return RelationshipMode Relationship type mode.
     */
    public RelationshipMode getMode() {
        return mode;
    }
    
    /**
     * Set relationship type mode.
     * @param mode Relationship type mode.
     */
    public void setMode(RelationshipMode mode) {
        this.mode = mode;
    }
    
    /**
     * Get relationship type direction.
     * @return DirectionMode Relationship type direction.
     */
    public DirectionMode getDirection() {
        return direction;
    }
    
    /**
     * Set relationship type direction.
     * @param direction Relationship type direction.
     */
    public void setDirection(DirectionMode direction) {
        this.direction = direction;
    }        
    
    /**
     * Get effectiveFromRequired attribute.
     * @return boolean EffectiveFromRequired attribute.
     */
    public boolean getEffectiveFromRequired() {
    	return effectiveFromRequired;
    }
    
    /**
     * Set effectiveFromRequired attribute.
     * @param effectiveFromRequired EffectiveFromRequired attribute.
     */
    public void setEffectiveFromRequired(boolean effectiveFromRequired) {
    	this.effectiveFromRequired = effectiveFromRequired;
    }    
    
    /**
     * Get effectiveToRequired attribute.
     * @return boolean effectiveToRequired attribute.
     */
    public boolean getEffectiveToRequired() {
    	return effectiveToRequired;
    }
    
    /**
     * Set effectiveToRequired attribute.
     * @param effectiveToRequired EffectiveToRequired attribute.
     */
    public void setEffectiveToRequired(boolean effectiveToRequired) {
    	this.effectiveToRequired = effectiveToRequired; 
    }    
    
    /**
     * Get purgeDateRequired attribute.
     * @return boolean PurgeDateRequired attribute.
     */
    public boolean getPurgeDateRequired() {
    	return purgeDateRequired;
    }
    
    /**
     * Set purgeDateRequired attribute.
     * @param purgeDateRequired PurgeDateRequired attribute.
     */
    public void setPurgeDateRequired(boolean purgeDateRequired) {
    	this.purgeDateRequired = purgeDateRequired;
    }   
    
    /**
     * Get effectiveFrom attribute.
     * @return boolean EffectiveFrom attribute.
     */
    public boolean getEffectiveFrom() {
    	return effectiveFrom;
    }
    
    /**
     * Set effectiveFrom attribute.
     * @param effectiveFrom EffectiveFrom attribute.
     */
    public void setEffectiveFrom(boolean effectiveFrom) {
    	this.effectiveFrom = effectiveFrom;
    }    
    
    /**
     * Get effectiveTo attribute.
     * @return boolean EffectiveTo attribute.
     */
    public boolean getEffectiveTo() {
    	return effectiveTo;
    }
    
    /**
     * Set effectiveTo attribute.
     * @param effectiveTo EffectiveTo attribute.
     */
    public void setEffectiveTo(boolean effectiveTo) {
    	this.effectiveTo = effectiveTo;
    }    
    
    /**
     * Get purgeDate attribute.
     * @return boolean PurgeDate attribute.
     */
    public boolean getPurgeDate(){     
    	return purgeDate;
    }
    
    /**
     * Set purgeDate attribute.
     * @param purgeDate PurgeDate attribute.
     */
    public void setPurgeDate(boolean purgeDate){     
    	this.purgeDate = purgeDate;
    }
    
    /**
     * Get all extended attributes.
     * @return List<Attribute> A list of extended attributes.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    /**
     * Get the attribute for the given attribute name.
     * @param name Attribute name.
     * @return Attribute Attribute if exits otherwise return null.
     */
    public Attribute getAttribute(String name) {
    	Iterator<Attribute> attrs = attributes.iterator();
    	while(attrs.hasNext()) {
    		Attribute attr = (Attribute)attrs.next();
    		if (attr.getName().equals(name)) {
    			return attr;
    		}
    	}
    	return null;
    }
    
    /**
     * Set all extended attributes.
     * @param attributes A list of extended attributes.
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }    
    
    /**
     * Set an extended attribute.
     * @param attribute Extended attribute.
     */
    public void setAttribute(Attribute attribute) {
        attributes.add(attribute);
    }        
}
