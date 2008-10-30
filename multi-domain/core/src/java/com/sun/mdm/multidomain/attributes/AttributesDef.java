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
package com.sun.mdm.multidomain.attributes;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * AttributesDef class.
 * Abstract class for different kinds of  types.
 * @author SwaranjitDua
 */
public abstract class AttributesDef {
    
    //public enum RelationshipMode {RELATIONSHIP,HIERARCHY, GROUP};
    //public enum DirectionMode {UNIDIRECTIONAL,BIDIRECTIONAL};
    
    private String name;
   // private String id;    
    private boolean effectiveFromRequired;
    private boolean effectiveToRequired;
    private boolean purgeDateRequired; 
    private List<Attribute> attributes; //extended attributes.

    /**
     * Create an instance of AttributesDef. 
     */
    public AttributesDef() {
        this.attributes = new ArrayList<Attribute>();
    }
    /**
     * Create an instance of RelationshipType.
     * @param name Relationship type name. 
     * @param displayName Relationship type display name.
     * @param id Relationship type Id.
     */
    public AttributesDef(String name) {
    	this.name = name;
    	this.effectiveFromRequired = true;
    	this.effectiveToRequired = true;
    	this.purgeDateRequired = true;
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
    
    /**
     * Copy AttributesDef.
     * @param atdef Attributes Def.
     */
    public void copy(AttributesDef atdef) {
        this.name = atdef.getName(); 
        this.effectiveFromRequired = atdef.effectiveFromRequired;
        
        this.purgeDateRequired = atdef.purgeDateRequired;
        this.effectiveToRequired = atdef.effectiveFromRequired;            
        this.attributes.clear();        
        for(Attribute attribute : atdef.getAttributes()) {            
            attributes.add(attribute.clone());
        }
    }    
}
