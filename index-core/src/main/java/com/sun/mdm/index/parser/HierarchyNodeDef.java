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

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Multi-level object data model hierarchy node definition.
 * @author cye
 */
public class HierarchyNodeDef {
    public final static String tagNode = "node";      
    public final static String tagName = "name";
    
    private String name;
    private HierarchyNodeDef parent;
    private ArrayList<HierarchyNodeDef> children;
            
    /**
     * HierarchyNodeDef constructor.
     */
    public HierarchyNodeDef() {
    }    
    /**
     * HierarchyNodeDef constructor.
     * @param name
     */
    public HierarchyNodeDef(String name) {
        this.name = name;
    }        
    
    /**
     * Get node name. 
     * @return String
     */
    public String getName() {
        return name;
    }
    /**
     * Set node name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }        
    
    /**
     * Get an array list of child hierarchy node.
     * @return ArrayList
     */
    public ArrayList<HierarchyNodeDef> getChildren() {
        return children;
    }
    
    /**
     * Get parent node.
     * @return HierarchyNodeDef
     */
    public HierarchyNodeDef getParent() {
        return parent;
    }

    /**
     * Set parent node.
     * @param parent
     */
    public void setParent(HierarchyNodeDef parent) {
        this.parent = parent;
    }
    
    /**
     * Parse multi-level object data model hierarchy node definition node.
     * @param node
     */
    public void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (tagName.equals(((Element) nl.item(i)).getTagName())) {
                        name = Utils.getStrElementValue(nl.item(i));
                    }
                    if (tagNode.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == children) {
                            children = new ArrayList();
                        }
                        HierarchyNodeDef child = new HierarchyNodeDef();
                        child.setParent(this);
                        child.parse((Node) nl.item(i));
                        children.add(child);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "<" + tagNode + ">" + "\n";
        ret += "    <" + tagName + ">";
        ret += name;
        ret += "</" + tagName + ">" + "\n";     
        if (children != null) {
        	for (HierarchyNodeDef child:children) {
        		ret += "    " + child.toString();
        	}
        }
        ret += "</" + tagNode + ">" + "\n";
        return ret;
    }
}
