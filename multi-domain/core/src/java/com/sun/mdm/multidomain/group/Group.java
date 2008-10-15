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
 * Group class.
 * represents a group instance of a particular groupType ex. instance of Household group type
 * @author SwaranjitDua
 */
public class Group extends Association {
    private String groupID;

    
    /**
     * Create an instance of Group.
     */
    public Group() {    	
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
    
}
