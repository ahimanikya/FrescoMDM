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
package com.sun.mdm.multidomain.group;

import com.sun.mdm.multidomain.attributes.AttributesDef;
import java.io.Serializable;

/**
 * GroupType class.
 * Encapsulates attributes for a particular group type such as Household group.
 * @author SwaranjitDua
 
 */
public class GroupDef extends AttributesDef implements Serializable {
    
    private String domain;
    private int groupDefID;
        
    /**
     * Create an instance of GroupType. 
     */
    public GroupDef() {    	
    }
    /**
     * Create an instance of GroupType.
     * @param name Group type name. 
     * @param id Group type Id.
     */
    public GroupDef(String name, int id) {
    	super(name);    	
        this.groupDefID = id;
    }
    
       /**
     * Get group Def Id.
     * @return int Group Id.
     */
    public int getGroupDefID() {
    	return groupDefID;
    }
    
    /**
     * Set group Id.
     * @param groupID Group Id.
     */
    public void setGroupDefID(int groupDefID){
    	this.groupDefID = groupDefID;
    }
    
    /**
     * Get Group type  domain.
     * @return String Group type  domain.
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * Set group type domain.
     * @param domain Group type domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    
    
    /**
     * Copy GroupDef.
     * @param def GroupDef.
     */
    public void copy(GroupDef def) {       
        this.domain = def.domain;
        super.copy(def);
        
    }    
}
