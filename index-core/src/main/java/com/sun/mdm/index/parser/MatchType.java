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
public class MatchType {
    private final String mFmt = "                [elephantMatchType]";
    private final String mTagMatchType = "match-type";
    private final String mTagStandardizationType = "standardization-type";
    private final String mTagMatchOption = "match-option";
    private ArrayList alMatchOptions;

    private String strMatchType;
    private String strStandardizationType;


    /**
     * @return String ret string
     */
    public String getStandardizationType() {
           return strStandardizationType;
    }


    /**
     * parse
     * @param node Node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagMatchType.equals(((Element) nl.item(i)).getTagName())) {
                        strMatchType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagStandardizationType.equals(((Element) nl.item(i)).getTagName())) {
                        strStandardizationType = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagMatchOption.equals(((Element) nl.item(i)).getTagName())) {
                        if (null == alMatchOptions) {
                            alMatchOptions = new ArrayList();
                        }

                        String mo = Utils.getStrElementValue(nl.item(i));
                        alMatchOptions.add(mo);
                    }
                }
            }
        }
    }


}
