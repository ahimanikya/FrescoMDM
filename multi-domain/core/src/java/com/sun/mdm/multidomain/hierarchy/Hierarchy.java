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
import com.sun.mdm.multidomain.association.Association;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Hierarchy  class.
 * associates a particular parent EUID and child EUID in an hierarchy.
 * @author SwaranjitDua
 */
public class Hierarchy extends Association {
    private String hierarchyID; // unique association identified by this id.
    private String parentEUID; 
    private String childEUID;
    
    
    /**
     * Create an instance of Hierarchy.
     */
    public Hierarchy() {    	
    }
    
    /**
     * Get hierarchy Id.
     * @return String Hierarchy Id.
     */
    public String getHierarchyID() {
    	return hierarchyID;
    }
    
    /**
     * Set hierarchy Id.
     * @param hierarchyID Hierarchy Id.
     */
    public void setHierarchyID(String hierarchyID){
    	this.hierarchyID = hierarchyID;
    }
    
    /** 
     * Get Parent EUID
     * @return parentEUID
     */
    public String getparentEUID() {
    	return parentEUID;
    }
    
    /**
     *  set parentEUID
     * @param parentEUID
     */
    public void setParentEUID(String parentEUID) {
    	this.parentEUID = parentEUID;
    }
    
    /** 
     * Get child EUID
     * @return childEUID
     */
    public String getChildEUID() {
    	return childEUID;
    }
    
    /**
     *  set childEUID
     * @param childEUID
     */
    public void setTargetEUID(String childEUID) {
    	this.childEUID = childEUID;
    }
}
