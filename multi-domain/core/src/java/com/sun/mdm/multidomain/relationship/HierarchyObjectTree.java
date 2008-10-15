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
 * HierarchyObjectTree class.
 * This represents an hierarchy of a particular object. 
 * Contains an object, all its ancestors and all its immediate children.
 * @author SwaranjitDua
 */
public class HierarchyObjectTree {
    /**
     * Current object.
     */
    private ObjectNode object;
    /**
     * All ancestors of the current object up to root. The ancestors are in order of 
     * position in the array. The 1st element is the 1st parent  and the last element is root.
     * An HiearchyAssociation ith element in the array - contains parent ObjectNode that is child ObjectNode 
     * for i+1 element in this array and contains child ObjectNode that is parent Object of i-1 element.
     */
    private HierarchyAssociationObject[] ancestors;
    /**
     * All the children of the current object.
     */
    private HierarchyAssociationObject[] children;
    
    /**
     * Create an instance of HierarchyObject.
     */
    public HierarchyObjectTree(){
    }
    
    /**
     * Set object.
     * @param object Object.
     */
    public void setObject(ObjectNode object){
         this.object =  object;
    }
    
    /**
     * Get object.
     * @return ObjectNode Object.
     */
    public ObjectNode getObject(){
         return object;
    }   
    
    /**
     * Set ancestors of the object. 
     * @param ancestors Ancestors of the object.
     */
    public void setAncestors(HierarchyAssociationObject[] ancestors){
         this.ancestors =  ancestors;
    }
    
    /**
     * Get ancestors of the object.  
     * @return ObjectNode[] Ancestors of the object. 
     */
    public HierarchyAssociationObject[] getAncestors(){
         return ancestors;
    }
    
    /**
     * Set children of the object.
     * @param children Children of the object.
     */
    public void setChildren(HierarchyAssociationObject[] children){
         this.children =  children;
    }
    
    /**
     * Get children of the object.
     * @return ObjectNode[] Children of the object.
     */
    public HierarchyAssociationObject[] getchildren(){
         return children;
    }
}
