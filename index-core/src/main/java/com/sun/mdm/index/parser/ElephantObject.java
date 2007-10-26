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
 * @author gzheng
 * @version
 */
public class ElephantObject {
    private final String mTagElephantObject = "elephant-object";
    private final String mTagName = "name";
    private final String mTagBBEName = "bbe-home";
    private final String mTagDatabase = "database";
    private final String mTagWeblogic = "weblogic";
    private final String mTagMatchEngine = "match-engine";
    private final String mTagSystem = "system";
    private final String mTagPrimaryObject = "primary-object";
    private final String mTagSecondaryObjects = "secondary-objects";
    private final String mTagBlockDefinition = "block-definition";
    private WeblogicType wtAppServer = null;
    private ElephantObjectType eotPrimaryObject = null;
    private ArrayList eotSecondaryObjects = null;
    private ArrayList blockDefinitions = null;
    private ArrayList mSystems;
    private String strBBEHome;
    private String strDatabase;
    private String strMatchEngine;

    private String strName;


    /**
     * @return String ret string
     */ 
    public String getBBEHome() {
        return strBBEHome;
    }


    /**
     * @return Arraylist list
     */ 
    public ArrayList getBlockDefinitions() {
        return blockDefinitions;
    }


    /**
     * @return String ret string
     */ 
    public String getName() {
        return strName;
    }


    /**
     * @return ElephantObjectType elephant object type
     */ 
    public ElephantObjectType getPrimaryObject() {
        return eotPrimaryObject;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getSecondaryList() {
        ArrayList al = null;
        if (null != eotSecondaryObjects) {
            al = new ArrayList();
            for (int i = 0; i < eotSecondaryObjects.size(); i++) {
                ElephantObjectType eot = (ElephantObjectType) eotSecondaryObjects.get(i);
                al.add(eot.getName());
            }
        }

        return al;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getSecondaryObjects() {
        return eotSecondaryObjects;
    }


    /**
     * @return ArrayList list
     */
    public ArrayList getmSystems() {
        return mSystems;
    }


    /**
     * @return WeblogicType weblogic type
     */
    public WeblogicType getWeblogicAppServer() {
        return wtAppServer;
    }


    /**
     * @param node Node
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
                     && ((Element) element).getTagName().equals(mTagElephantObject)
                     && element.hasChildNodes()) {
                nl = element.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if (mTagName.equals(((Element) nl.item(i)).getTagName())) {
                            strName = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagBBEName.equals(((Element) nl.item(i)).getTagName())) {
                            strBBEHome = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagWeblogic.equals(((Element) nl.item(i)).getTagName())) {
                            wtAppServer = new WeblogicType();
                            wtAppServer.parse(nl.item(i));
                        } else if (mTagDatabase.equals(((Element) nl.item(i)).getTagName())) {
                            strDatabase = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagMatchEngine.equals(((Element) nl.item(i)).getTagName())) {
                            strMatchEngine = Utils.getStrElementValue(nl.item(i));
                        } else if (mTagSystem.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == mSystems) {
                                mSystems = new ArrayList();
                            }
                            mSystems.add(Utils.getStrElementValue(nl.item(i)));
                        } else if (mTagPrimaryObject.equals(((Element) nl.item(i)).getTagName())) {
                            eotPrimaryObject = new ElephantObjectType();
                            eotPrimaryObject.parse(nl.item(i));
                        } else if (mTagSecondaryObjects.equals(((Element) nl.item(i)).getTagName())) {
                            if (null == eotSecondaryObjects) {
                                eotSecondaryObjects = new ArrayList();
                            }

                            ElephantObjectType eot = new ElephantObjectType();
                            eot.parse(nl.item(i));
                            eotSecondaryObjects.add(eot);
                        } else if (mTagBlockDefinition.equals(((Element) nl.item(i)).getTagName())) {
                            // block-definition
                            if (null == blockDefinitions) {
                                blockDefinitions = new ArrayList();
                            }

                            BlockType blockType = new BlockType();
                            blockType.parse(nl.item(i));
                            blockDefinitions.add(blockType);
                        }
                    }
                }
            }
        }
    }
}
