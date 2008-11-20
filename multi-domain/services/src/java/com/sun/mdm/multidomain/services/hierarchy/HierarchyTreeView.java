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
package com.sun.mdm.multidomain.services.hierarchy;

import java.util.List;
import java.util.ArrayList;

/**
 * HierarchyTreeView class.
 * @author cye
 */
public class HierarchyTreeView {
    private HierarchyNodeView node;
    private HierarchyNodeView parent;
    private List<HierarchyNodeView> children;

    public HierarchyTreeView() {        
    }
    
    public HierarchyNodeView getNode() {
        return this.node;
    }

    public void setNode(HierarchyNodeView node) {
        this.node = node;
    }

    public HierarchyNodeView getParent() {
        return this.parent;
    }

    public void setParent(HierarchyNodeView parent) {
        this.parent = parent;
    }

    public List<HierarchyNodeView> getChildren() {
        return this.children;
    }

    public void setChildren(List<HierarchyNodeView> children) {
        this.children = children;
    }
    
    public void addChild(HierarchyNodeView child) {
        if (children == null) {
            children = new ArrayList<HierarchyNodeView>();
        }
        children.add(child);
    }
    
}
