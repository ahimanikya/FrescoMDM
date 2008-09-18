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
package com.sun.mdm.multidomain.services.query;

import com.sun.mdm.index.objects.ObjectNode;

import com.sun.mdm.multidomain.relationship.Relationship;

/**
 * RelationshipObject class.
 * @author cye
 */
public class RelationshipObject {
    private ObjectNode sourceObject;
    private ObjectNode targetObject;
    private Relationship relationship;
    
    /**
     * Public constructor
     */
    public RelationshipObject() {
    }
    
    /**
     * Set source object node.
     * @param sourceObject
     */
    public void setSourceObject(ObjectNode sourceObject) {
        this.sourceObject = sourceObject;
    }
    
    /**
     * Get source object node.
     * @return source object node
     */
    public ObjectNode getSourceObject() {
        return sourceObject;
    }
    
    /**
     * Set target object node.
     * @param targetObject
     */
    public void setTargetObject(ObjectNode targetObject) {
        this.targetObject = targetObject;
    }
    /**
     * Get target domain object node.
     * @return target object node 
     */
    public ObjectNode getTargetObject() {
        return targetObject;
    }  
    
    /**
     * Set relationship instance.
     * @param relationship instance
     */
    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
    
    /**
     * Get relationship instance.
     * @return relationship instance
     */
    public Relationship getRelationship() {
        return relationship;
    }     
}
