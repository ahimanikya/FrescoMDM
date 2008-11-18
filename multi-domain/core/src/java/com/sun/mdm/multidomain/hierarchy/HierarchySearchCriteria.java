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

import java.io.Serializable;

import com.sun.mdm.index.objects.SystemObject;

public class HierarchySearchCriteria implements Serializable {

    private HierarchyNode hierarchyNode;
    private SystemObject systemObject;
    
    public HierarchyNode getHierarchyNode() {
        return hierarchyNode;
    }
    
    public void setHierarchyNode(HierarchyNode hierarchyNode) {
        this.hierarchyNode = hierarchyNode;
    }
    
    public SystemObject getSystemObject() {
        return systemObject;
    }
    
    public void setSystemObject(SystemObject systemObject) {
        this.systemObject = systemObject;
    }
}
