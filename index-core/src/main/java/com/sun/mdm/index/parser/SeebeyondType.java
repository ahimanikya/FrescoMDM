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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


/**
 * @author gzheng
 * @version
 */
public class SeebeyondType {
    private final String mFmt = "            [elephantSeebeyondType]";
    private final String mTagSeebeyondHome = "seebeyond-home";
    private final String mTagDataSource = "data-source";
    private String strDataSource;
    private String strSeebeyondHome;


    /**
     * @return String string
     */
    public String getDataSource() {
        return strDataSource;
    }


    /**
     * @return String string
     */
    public String getSeebeyondHome() {
        return strSeebeyondHome;
    }


    /**
     * default constructor
     */
    public SeebeyondType() {
    }


    /**
     * @param node node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagSeebeyondHome.equals(((Element) nl.item(i)).getTagName())) {
                        strSeebeyondHome = Utils.getStrElementValue(nl.item(i));
                    } else if (mTagDataSource.equals(((Element) nl.item(i)).getTagName())) {
                        strDataSource = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }

}
