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


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author gzheng
 * @version
 */
public class WeblogicType {
    private final String tagWeblogic = "weblogic";
    private final String tagVersion = "version";
    private final String tagJavac = "javac";
    private final String tagWeblogicHome = "weblogic-home";
    private final String tagWorkingPath = "working-path";
    private final String tagDataSource = "data-source";
    private final String tagCreateDefaultTables = "create-default-tables";
    private String strCreateDefaultTables;
    private String strDataSource;
    private String strJavac;
    private String strVersion;
    private String strWebLogicHome;
    private String strWorkingPath;


    /**
     * default constructor
     */
    public WeblogicType() {
    }

    /**
     * Getter for CreateDefaultTables attribute of the
     *      WeblogicType object
     * @return String ret string
     */
    public String getCreateDefaultTables() {
        return strCreateDefaultTables;
    }

    /**
     * Getter for DataSource attribute of the WeblogicType
     *      object
     * @return String ret string
     */
    public String getDataSource() {
        return strDataSource;
    }

    /**
     * Getter for Javac attribute of the WeblogicType object
     * @return String ret string
     */
    public String getJavac() {
        return strJavac;
    }

    /**
     * Getter for Version attribute of the WeblogicType object
     * @return String ret string
     */
    public String getVersion() {
        return strVersion;
    }

    /**
     * Getter for WebLogicHome attribute of the WeblogicType
     *      object
     * @return String ret string
     */
    public String getWebLogicHome() {
        return strWebLogicHome;
    }

    /**
     * Getter for WorkingPath attribute of the WeblogicType
     *      object
     * @return String ret string
     */
    public String getWorkingPath() {
        return strWorkingPath;
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

            if ((null != element) 
                    && ((Element) element).getTagName().equals(tagWeblogic) 
                    && element.hasChildNodes()) {
                nl = element.getChildNodes();

                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        if (tagVersion.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strVersion = Utils.getStrElementValue(nl.item(i));
                        } else if (tagJavac.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strJavac = Utils.getStrElementValue(nl.item(i));
                        } else if (tagWeblogicHome.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strWebLogicHome 
                                = Utils.getStrElementValue(nl.item(i));
                        } else if (tagWorkingPath.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strWorkingPath 
                                = Utils.getStrElementValue(nl.item(i));
                        } else if (tagDataSource.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strDataSource = Utils.getStrElementValue(nl.item(i));
                        } else if (tagCreateDefaultTables.equals(
                                    ((Element) nl.item(i)).getTagName())) {
                            strCreateDefaultTables 
                                = Utils.getStrElementValue(nl.item(i));
                        }
                    }
                }
            }
        }
    }

    /**
     * @return String ret string
     */
    @Override
    public String toString() {
        String ret = "";
        ret += ("(" + tagWeblogic + ")" + "\n");
        ret += (tagVersion + ": " + strVersion + "\n");
        ret += (tagJavac + ": " + strJavac + "\n");
        ret += (tagWeblogicHome + ": " + strWebLogicHome + "\n");
        ret += (tagWorkingPath + " : " + strWorkingPath + "\n");
        ret += (tagDataSource + " : " + strDataSource + "\n");
        ret += (tagCreateDefaultTables + " : " + strCreateDefaultTables + "\n");

        return ret;
    }
}
