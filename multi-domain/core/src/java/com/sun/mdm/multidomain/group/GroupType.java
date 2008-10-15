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

import com.sun.mdm.multidomain.association.AssociationType;

/**
 * GroupType class.
 * Encapsulates attributes for a particular group type such as Household group.
 * @author SwaranjitDua
 
 */
public class GroupType extends AssociationType {
    
    private String domain;
    private String roleName;
        
    /**
     * Create an instance of GroupType. 
     */
    public GroupType() {    	
    }
    /**
     * Create an instance of GroupType.
     * @param name Group type name. 
     * @param id Group type Id.
     */
    public GroupType(String name, String id) {
    	super(name, id);    	
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
     * Get Group type role name.
     * @return String Group  type role name.
     */
    public String getRoleName() {
        return roleName;
    }
    
    /**
     * Set Group type role name.
     * @param roleName Group type  role name.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }    
        
    
    /**
     * Copy GroupType.
     * @param type GroupType.
     */
    public void copy(GroupType type) {       
        this.domain = type.domain;
        super.copy(type);
        
    }    
}
