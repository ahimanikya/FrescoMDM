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
import com.sun.mdm.multidomain.association.AssociationType;

/**
 * HierarchyType class.
 * Encapsulates attributes related to Hierarchy type.
 * @author SwaranjitDua
 */
public class HierarchyType extends AssociationType {
     
    private String  domain;
    private String childRoleName;
    private String parentRoleName; 
    
    /**
     * Create an instance of HierarchyType. 
     */
    public HierarchyType() {    	
    }
    /**
     * Create an instance of HierarchyType.
     * @param name Hierarchy type name. 
    // * @param displayName Relationship type display name.
     * @param id Hierarchy type Id.
     */
    public HierarchyType(String name, String id) {
    	super(name, id);    	
    }
    
    
    /**
     * Get Hierarchy type  domain.
     * @return String Hierarchy type  domain.
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * Set Hierarchy type  domain.
     * @param domain Hierarchy type source domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    /**
     * Get Hierarchy type child role name.
     * @return String Hierarchy type child role name.
     */
    public String getChildRoleName() {
        return childRoleName;
    }
    
    /**
     * Set Hierarchy type child role name.
     * @param childRoleName Hierarchy type child role name.
     */
    public void setChildRoleName(String childRoleName) {
        this.childRoleName = childRoleName;
    }
    
    /**
     * Get Hierarchy type parent role name.
     * @return String Hierarchy type parent role name.
     */
    public String getParentRoleName() {
        return parentRoleName;
    }
    
    /**
     * Set Hierarchy type parent role name.
     * @param parentRoleName Hierarchy type parent role name.
     */
    public void setParentRoleName(String parentRoleName) {
        this.parentRoleName = parentRoleName;
    }    
    
        
    
    /**
     * Copy HierarchyType.
     * @param type HierarchyType.
     */
    public void copy(HierarchyType type) {
        
        this.domain = type.domain;
        this.childRoleName = type.childRoleName;
        this.parentRoleName = type.parentRoleName;
        super.copy(type);
        
    }    
}
