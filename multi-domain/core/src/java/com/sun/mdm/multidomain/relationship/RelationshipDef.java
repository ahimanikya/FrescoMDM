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
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributesDef;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * RelationshipDef class.
 * @author SwaranjitDua
 */
public class RelationshipDef extends AttributesDef implements Serializable {
    
    public static final int UNIDIRECTIONAL= 0;
    public static final int BIDIRECTIONAL = 1;

    public enum DirectionMode {UNIDIRECTIONAL,BIDIRECTIONAL};       
    private String sourceDomain;
    private String targetDomain;  
    private DirectionMode direction; 
    private int reldefID;    
        
    /**
     * Create an instance of RelationshipType. 
     */
    public RelationshipDef() {    	
    }
    /**
     * Create an instance of RelationshipType.
     * @param name Relationship type name. 
     * @param displayName Relationship type display name.
     * @param id Relationship def Id.
     */
    public RelationshipDef(String name, int id) {
        super(name);
    	this.reldefID = id;
    	    	
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
     * Copy RelationshipType.
     * @param type RelationshipType.
     */
    public void copy(RelationshipDef type) {
        
        this.sourceDomain = type.getSourceDomain();
        this.targetDomain = type.getTargetDomain();

        this.direction = type.getDirection();    
        
        
    }    
}
