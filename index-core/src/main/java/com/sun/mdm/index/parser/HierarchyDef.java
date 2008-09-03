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
package com.sun.mdm.index.parser;

import org.w3c.dom.Node;

/**
 * Multi-level object data model hierarchy definition
 * @author cye
 */
public class HierarchyDef {
    public final static String tagHierarchy = "hierarchy";      
    private HierarchyNodeDef node;

    /**
     * HierarchyDef constructor
     */
    public HierarchyDef() {        
    }
    
    /**
     * Get hierarchy root node.
     * @return HierarchyNodeDef
     */
    public HierarchyNodeDef getNode() {
        return node;
    }
    
    /**
     * Parse multi-level object data model hierarchy definition node.
     * @param node
     */
    public void parse(Node node) {
        if (node.hasChildNodes() && tagHierarchy.equals(node.getNodeName())) {
            if (node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
                if (HierarchyNodeDef.tagNode.equals(node.getFirstChild().getNodeName())) {
                    this.node = new HierarchyNodeDef();           
                    this.node.parse(node.getFirstChild());
                 }
            }
        }    
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "<" + tagHierarchy + ">" + "\n";
        if (node != null) {
            ret += "    " + node.toString();
        }
        ret += "</" + tagHierarchy + ">" + "\n";
        return ret;
    }        
}
