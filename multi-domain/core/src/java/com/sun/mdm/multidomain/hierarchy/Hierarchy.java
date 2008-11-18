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
import java.util.Date;

public class Hierarchy implements Serializable {

    private int hierarchyId;
    private Date effectiveFrom;
    private Date effectiveTo;
    private Date purgeDate;
    
    private HierarchyDef hierarchyDef;
    private HierarchyNode rootNode;
    
    public int getHierarchyId() {
        return hierarchyId;
    }
    
    public void setHierarchyId(int hierarchyId) {
        this.hierarchyId = hierarchyId;
    }
    
    public Date getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public Date getEffectiveTo() {
        return effectiveTo;
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    }
    
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public HierarchyDef getHierarchyDef() {
        return hierarchyDef;
    }
    
    public void setHierarchyDef(HierarchyDef hierarchyDef) {
        this.hierarchyDef = hierarchyDef;
    }
    
    public HierarchyNode getRootNode() {
        return rootNode;
    }
    
    public void setRootNode(HierarchyNode rootNode) {
        this.rootNode = rootNode;
    } 
}
