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
import java.io.Serializable;


/**
 * HierarchyObject class.
 * represents association between a parent and a child ObjectNode in an hierarchy.
 * Both parent and child are the top level primary objects in a Master Index domain.
 * 
 * @author SwaranjitDua
 */
public class HierarchyObject implements Serializable {
    private ObjectNode object;
    private HierarchyNode node;
    
    /**
     * Public constructor
     */
    public HierarchyObject() {
    }
    
    /**
     * Public constructor
     */
    public HierarchyObject(ObjectNode object, HierarchyNode node) {    	
    	this.object = object;
    	this.node = node;
    	
    }
    
    
    /**
     * Set target object node.
     * @param targetObject
     */
    public void setObject(ObjectNode object) {
        this.object = object;
    }
    /**
     * Get target domain object node.
     * @return target object node 
     */
    public ObjectNode getObject() {
        return object;
    }  
    
    /**
     * Set Hierarchy 
     * @param Hierarchy 
     */
    public void setHierarchyNode(HierarchyNode hierarchy) {
        this.node = hierarchy;
    }
    
    /**
     * Get Hierarchy instance.
     * @return Hierarchy instance
     */
    public HierarchyNode getHierarchyNode() {
        return node;
    }     
}
