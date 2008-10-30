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
 * GroupMemberDef class.
 * @author SwaranjitDua
 
 */
public class GroupMemberDef extends AttributesDef implements Serializable {
    private int groupMemberDefID;
            
    /**
     * Create an instance of GroupMemberDef. 
     */
    public GroupMemberDef() {    	
    }
    /**
     * Create an instance of GroupMemberDef.
     * @param name Group Member Def name. 
     * @param id Group Member Def Id.
     */
    public GroupMemberDef(String name, int id) {
    	super(name);    	
        groupMemberDefID = id;
    }
    
           /**
     * Get group Member Def Id.
     * @return int Group Id.
     */
    public int getGroupMemberDefID() {
    	return groupMemberDefID;
    }
    
    /**
     * Set group Member Id.
     * @param groupID Group Id.
     */
    public void setGroupMemberDefID(int groupMemberDefID){
    	this.groupMemberDefID = groupMemberDefID;
    }
    
    
    /**
     * Copy GroupMemberDef.
     * @param type GroupMemberDef.
     */
    public void copy(GroupMemberDef def) {       
        super.copy(def);
        
    }    
}
