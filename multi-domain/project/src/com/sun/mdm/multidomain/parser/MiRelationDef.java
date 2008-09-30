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
package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class MiRelationDef {
    private final String mTagRelationships = "relationships";
    private final String mTagName = "name";
    private final String mTagChildren = "children";
    private final String mTagRelations = "relationships";
    private ArrayList mChildren = null;
    private ArrayList mRelations = null;

    private String strName;


    /**
     * @return ArrayList list
     */
    public ArrayList getChildren() {
        return mChildren;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getElements() {
        ArrayList ret = null;

        if (null != mChildren) {
            if (null == ret) {
                ret = new ArrayList();
            }
            ret.addAll(mChildren);
        }
        if (null != mRelations) {
            if (null == ret) {
                ret = new ArrayList();
            }

            for (int i = 0; i < mRelations.size(); i++) {
                MiRelationDef rel = (MiRelationDef) mRelations.get(i);
                ret.addAll(rel.getElements());
                ret.add(rel.getName());
            }
        }

        return ret;
    }


    /**
     * @return String string
     */
    public String getName() {
        return strName;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getRelations() {
        return mRelations;
    }


    /**
     * @param node node
     */
    public void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                        strName = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagChildren.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == mChildren) {
                            mChildren = new ArrayList();
                        }

                        mChildren.add(Utils.getStrElementValue(nl.item(i)));
                    }
                    if (mTagRelations.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == mRelations) {
                            mRelations = new ArrayList();
                        }
                        MiRelationDef rd = new MiRelationDef();
                        rd.parse(nl.item(i));
                        mRelations.add(rd);
                    }
                }
            }
        }
    }


    /**
     * @return String string
     */
    @Override
    public String toString() {
        String ret = "";
        ret += "    <" + mTagRelationships + ">\r\n";
        ret += "       <name>" + strName + "</name>\r\n";
        for (int i = 0; mChildren != null && i < mChildren.size(); i++) {
                ret += "       <children>" + (String) mChildren.get(i) + "</children>\r\n";
        }
        for (int j = 0; mRelations != null && j < mRelations.size(); j++) {
                ret += "  " + ((MiRelationDef) mRelations.get(j)).toString();
        }
        ret += "    </" + mTagRelationships + ">\r\n";
        return ret;
    }
}
