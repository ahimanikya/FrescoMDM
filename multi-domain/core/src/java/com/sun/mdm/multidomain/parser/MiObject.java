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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class MiObject {
    private final String mTagConfiguration = "Configuration";
    private final String mTagName = "name"; // Application Name
    private final String mTagDatabase = "database";
    private final String mTagDateFormat = "dateformat";
    private final String mTagNodes = "nodes";
    private final String mTagRelationships  = "relationships";
    private ArrayList mNodes = null;
    private ArrayList mRelationships = null;
    private String strDatabase;
    private String strDateFormat = "MM/dd/yyyy";
    private String strApplicationName;
    private String strObjectName;
    private boolean mModified = false;


    /**
     * The main program for the EIndexObject class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            Document doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param String dateformat
     */
    public void setDateFormat(String dateformat) {
        strDateFormat = dateformat;
    }

    /**
     * @return String DateFormat String
     */
    public String getDateFormat() {
        return strDateFormat;
    }

    /**
     * @param String database
     */
    public void setDataBase(String database) {
        strDatabase = database;
    }

    /**
     * @return String ret String
     */
    public String getDataBase() {
        return strDatabase;
    }

    /** Application Name
     * It can be different from Primary Object Name
     * @return String ret String
     */
    public String getApplicationName() {
        return strApplicationName;
    }
    
    /**
     * @return String ret String
     */
    public String getObjectName() {
        return strObjectName;
    }

    /**
     * @return String ret String
     */
    public String getName() {
        return strObjectName;
    }

    /**
     * @param name node name
     * @return MiNodeDef node definition
     */
    public MiNodeDef getNode(String name) {
        MiNodeDef ret = null;

        if (null != mNodes) {
            for (int i = 0; i < mNodes.size(); i++) {
                MiNodeDef node = (MiNodeDef) mNodes.get(i);
                if (node.getTag().equals(name)) {
                    ret = node;
                    break;
                }
            }
        }

        return ret;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getNodes() {
        return mNodes;
    }
    public ArrayList getRelationships() {
        return mRelationships;
    }

    /**
     * @param node node
     */
    public void parse(Node node) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            NodeList nl = node.getChildNodes();
            Node element = null;
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    element = nl.item(i);
                    break;
                }
            }

            if (null != element
                     && ((Element) element).getTagName().equals(mTagConfiguration)
                     && element.hasChildNodes()) {
                nl = element.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                            strApplicationName = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagDatabase.equals(((Element) nl.item(i)).getTagName())) {
                            strDatabase = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagDateFormat.equals(((Element) nl.item(i)).getTagName())) {
                            strDateFormat = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagNodes.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mNodes) {
                                mNodes = new ArrayList();
                            }

                            MiNodeDef nt = new MiNodeDef();
                            nt.parse(nl.item(i));
                            mNodes.add(nt);
                        }
                        if (mTagRelationships.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mRelationships) {
                                mRelationships = new ArrayList();
                            }

                            MiRelationDef rt = new MiRelationDef();
                            rt.parse(nl.item(i));
                            mRelationships.add(rt);
                            strObjectName = rt.getName();
                        }

                    }
                }
            }
        }
    }
    
    /**
    *@param boolean flag
    */
    public void setModified(boolean flag) {
        mModified = flag;
    }
    
    /**
    *@return boolean flag
    */
    public boolean isModified() {
        return mModified;
    }


    /**
     * @return String ret String
     */
    public String toString() {
        String ret = "";
        ret += "(" + mTagConfiguration + ")" + mTagName + ": " + strApplicationName + ": \n";
        ret += "(" 
               + mTagConfiguration 
               + ")" 
               + mTagDatabase 
               + ": " 
               + strDatabase 
               + ": " 
               + "\n";
        ret += "mNodes: \n" 
               + mNodes 
               + "\n";
        
        ret += "mRelationships: \n" 
               + mRelationships 
               + "\n";
        return ret;
    }
}