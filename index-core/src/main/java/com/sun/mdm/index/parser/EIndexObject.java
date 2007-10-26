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
//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class EIndexObject {
    private final String mTagEindexObject = "eView";
    private final String mTagName = "name";
    private final String mTagDatabase = "database";
    private final String mTagAppServer = "appserver";
    private final String mTagDateFormat = "dateformat";
    private final String mTagSystems = "systems";
    private final String mTagNodes = "nodes";
    private final String mTagRelationships  = "relationships";
    private ArrayList mSourceSystems = null;
    private ArrayList mNodes = null;
    private ArrayList mRelationships = null;
    private String strAppServer;
    private String strDatabase;
    private String strDateFormat = "MM/dd/yyyy";
    private String strName;
    private boolean mModified = false;


    /**
     * The main program for the EIndexObject class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            Document doc;
            /*
            DOMParser parser = new DOMParser();
            parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", false);
            parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);

            String xmlpath = args[0];

            parser.parse(xmlpath);

            doc = parser.getDocument();
            EIndexObject eo = new EIndexObject();

            eo.parse(doc);
            System.out.println(eo);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return String ret String
     */
    public String getAppServer() {
        return strAppServer;
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

    /**
     * @return String ret String
     */
    public String getName() {
        return strName;
    }


    /**
     * @param name node name
     * @return NodeDef node definition
     */
    public NodeDef getNode(String name) {
        NodeDef ret = null;

        if (null != mNodes) {
            for (int i = 0; i < mNodes.size(); i++) {
                NodeDef node = (NodeDef) mNodes.get(i);
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


    /**
     * @return ArrayList list
     */
    public ArrayList getRelationships() {
        return mRelationships;
    }


    /**
     * @param ArrayList list
     */
    public void setSystems(ArrayList systems) {
        mSourceSystems = systems;
    }

    /**
     * @return ArrayList list
     */
    public ArrayList getSystems() {
        return mSourceSystems;
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
                     && ((Element) element).getTagName().equals(mTagEindexObject)
                     && element.hasChildNodes()) {
                nl = element.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                            strName = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagDatabase.equals(((Element) nl.item(i)).getTagName())) {
                            strDatabase = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagDateFormat.equals(((Element) nl.item(i)).getTagName())) {
                            strDateFormat = Utils.getStrElementValue(nl.item(i));
                        }
                        if (mTagAppServer.equals(((Element) nl.item(i)).getTagName())) {
                            strAppServer = Utils.getStrElementValue(nl.item(i));
                        }
                        
                        if (mTagSystems.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mSourceSystems) {
                                mSourceSystems = new ArrayList();
                            }
                            mSourceSystems.add(Utils.getStrElementValue(nl.item(i)));
                        }
                        if (mTagNodes.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mNodes) {
                                mNodes = new ArrayList();
                            }

                            NodeDef nt = new NodeDef();
                            nt.parse(nl.item(i));
                            mNodes.add(nt);
                        }
                        if (mTagRelationships.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mRelationships) {
                                mRelationships = new ArrayList();
                            }

                            RelationDef rt = new RelationDef();
                            rt.parse(nl.item(i));
                            mRelationships.add(rt);
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
        ret += "(" + mTagEindexObject + ")" + mTagName + ": " + strName + ": \n";
        ret += "(" 
               + mTagEindexObject 
               + ")" 
               + mTagDatabase 
               + ": " 
               + strDatabase 
               + ": " 
               + strAppServer 

               + ": "
               + 
 mSourceSystems 
               +
 "\n";
        ret += "mNodes: \n" 
               + mNodes 
               + "\n";
        
        ret += "mRelationships: \n" 
               + mRelationships 
               + "\n";

        return ret;
    }
}
