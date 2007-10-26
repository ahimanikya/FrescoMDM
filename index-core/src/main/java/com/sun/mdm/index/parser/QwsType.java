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
public class QwsType {
    private final String mFmt = "                [elephantQWSType]";
    private final String mTagDisplayName = "display-name";
    private final String mTagScreen = "screen";
    private final String mTagShowOrHide = "show-or-hide";
    private final String mTagView = "view";
    private final String mTagOrder = "order";
    private boolean bShow;

    private String strDisplayName;
    private String strOrder;
    private String strScreen;
    private String strView;


    /**
     * default constructor
     */
    public QwsType() {
    }


    /**
     * @param node node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagDisplayName.equals(((Element) nl.item(i)).getTagName())) {
                        strDisplayName = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagScreen.equals(((Element) nl.item(i)).getTagName())) {
                        strScreen = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagShowOrHide.equals(((Element) nl.item(i)).getTagName())) {
                        if ("show" == Utils.getStrElementValue(nl.item(i))) {
                            bShow = true;
                        } else {
                            bShow = false;
                        }
                    }
                    if (mTagView.equals(((Element) nl.item(i)).getTagName())) {
                        strView = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagOrder.equals(((Element) nl.item(i)).getTagName())) {
                        strOrder = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }
}
