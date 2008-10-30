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
import com.sun.mdm.multidomain.attributes.AttributesDef;
import java.io.Serializable;

/**
 * HierarchyDef class.
 * Encapsulates attributes related to Hierarchy Definition.
 * @author SwaranjitDua
 */
public class HierarchyDef extends AttributesDef implements Serializable{
     
    private String  domain;
    private int hierarchyDefID;
   
    /**
     * Create an instance of HierarchyType. 
     */
    public HierarchyDef() {    	
    }
    /**
     * Create an instance of HierarchyType.
     * @param name Hierarchy type name. 
    // * @param displayName Relationship type display name.
     * @param id Hierarchy type Id.
     */
    public HierarchyDef(String name, int id) {      
    	super(name);
        this.hierarchyDefID = id;
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
     * Get relationship type Id.
     * @return String Relationship type Id.
     */
    public int getID() {
        return hierarchyDefID;
    }
    
    /**
     * Set hierarchy def Id.
     * @param hierarchyDefID hierarchy def id.
     */
    public void setID(int id) {
        this.hierarchyDefID = id;
    }    
    
          
    /**
     * Copy HierarchyType.
     * @param type HierarchyType.
     */
    public void copy(HierarchyDef def) {
        this.hierarchyDefID = def.hierarchyDefID;
        this.domain = def.domain;     
        super.copy(def);
    }    
}
