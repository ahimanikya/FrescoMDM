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

import com.sun.mdm.index.objects.ObjectNode;


/**
 * HierarchyObject class.
 * represents association between a parent and a child ObjectNode in an hierarchy.
 * Both parent and child are the top level primary objects in a Master Index domain.
 * 
 * @author SwaranjitDua
 */
public class HierarchyObject {
    private ObjectNode parentObject;
    private ObjectNode childObject;
    private Hierarchy hierarchy;
    
    /**
     * Public constructor
     */
    public HierarchyObject() {
    }
    
    /**
     * Public constructor
     */
    public HierarchyObject(ObjectNode parent, ObjectNode child, Hierarchy hierarchy) {
    	/* @@Todo validate that euids in parentObject and childObject are consistent with 
    	 * the EUIDs in hierarchy*/
    	this.parentObject = parent;
    	this.childObject = child;
    	this.hierarchy = hierarchy;
    	
    }
    
    /**
     * Set parent object node.
     * @param parentObject
     */
    public void setParentObject(ObjectNode parentObject) {
        this.parentObject = parentObject;
    }
    
    /**
     * Get parent object node.
     * @return parent object node
     */
    public ObjectNode getParentObject() {
        return parentObject;
    }
    
    /**
     * Set target object node.
     * @param targetObject
     */
    public void setChildObject(ObjectNode childObject) {
        this.childObject = childObject;
    }
    /**
     * Get target domain object node.
     * @return target object node 
     */
    public ObjectNode getChildObject() {
        return childObject;
    }  
    
    /**
     * Set Hierarchy 
     * @param Hierarchy 
     */
    public void setHierarchy(Hierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }
    
    /**
     * Get Hierarchy instance.
     * @return Hierarchy instance
     */
    public Hierarchy getHierarchy() {
        return hierarchy;
    }     
}
