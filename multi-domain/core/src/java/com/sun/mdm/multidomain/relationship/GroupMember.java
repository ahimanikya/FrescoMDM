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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import com.sun.mdm.multidomain.association.Association;

/**
 * GroupMember class
 * represents a particular groupMember association with a group.
 * The assocation is between a member EUID and a groupID.
 * @author SwaranjitDua
 */
public class GroupMember extends Association {
    private String groupMemberID;
    private String memberEUID;
    private String groupID;

    
    /**
     * Create an instance of Group.
     */
    public GroupMember() {    	
    }
    
    /**
     * Get group Id.
     * @return String Group Id.
     */
    public String getGroupID() {
    	return groupID;
    }
    
    /**
     * Set group Id.
     * @param groupID Group Id.
     */
    public void setGroupID(String groupID){
    	this.groupID = groupID;
    }
    
    /**
     * Get groupMember Id.
     * @return String Group Member Id.
     */
    public String getGroupMemberID() {
    	return groupMemberID;
    }
    
    /**
     * Set groupMemeber Id.
     * @param groupID GroupMemeber Id.
     */
    public void setGroupMemberID(String groupMemberID){
    	this.groupMemberID = groupMemberID;
    }
    
    
    /** 
     * Get Member EUID
     * @return memberEUID
     */
    public String getMemberEUID() {
    	return memberEUID;
    }
    
    /**
     *  set memberEUID
     * @param memberEUID
     */
    public void setMemberEUID(String memberEUID) {
    	this.memberEUID = memberEUID;
    }
    
   
}
