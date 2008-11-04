/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import com.sun.mdm.multidomain.hierarchy.HierarchyDef;

public class HierarchyScreenConfig extends ObjectScreenConfig {

    private HierarchyDef mHierarchyDef = null;
    private String mChildRoleDisplayName = null;
    private String mParentRoleDisplayName = null;
    
    public HierarchyScreenConfig() {
    }
    
    public HierarchyScreenConfig(HierarchyDef hierarchyDef, 
                                 String hierarchyDisplayName,
                                 String childRoleDisplayName, 
                                 String parentRoleDisplayName) {
        mHierarchyDef = hierarchyDef;
        mChildRoleDisplayName = childRoleDisplayName;
        mParentRoleDisplayName = parentRoleDisplayName;
        setDisplayName(hierarchyDisplayName);
    }
    
     // retrieves the HierarchyDef object for this hierarchy
   
    public HierarchyDef getHierarchyDef() {      
        return mHierarchyDef;
    }
    
    // retrieve the child role name
    
    public String getChildRoleDisplayName() {
        return mChildRoleDisplayName;
    }

    // retrieve the child role name
    
    public void setChildRoleDisplayName(String childRoleDisplayName) {
        mChildRoleDisplayName = childRoleDisplayName;
    }

    // retrieve the parent role name
    
    public String getParentRoleDisplayName() {
        return mParentRoleDisplayName;
    }

    // retrieve the child role name
    
    public void setParentRoleDisplayName(String parentRoleDisplayName) {
        mParentRoleDisplayName = parentRoleDisplayName;
    }

}
