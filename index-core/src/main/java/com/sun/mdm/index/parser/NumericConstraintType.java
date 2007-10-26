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
public class NumericConstraintType {
    private final String mFmt = "                [NumericConstraintType]";
    private final String mTagNumericConstraint = "numeric-constraint";
    private final String mTagStartOP = "start-op";
    private final String mTagStartValue = "start-value";
    private final String mTagEndOP = "end-op";
    private final String mTagEndValue = "end-value";

    private String strStartOp = null;
    private String strStartValue = null;
    private String strEndOp = null;
    private String strEndValue = null;


    /**
     * @param node node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagStartOP.equals(((Element) nl.item(i)).getTagName())) {
                        strStartOp = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagStartValue.equals(((Element) nl.item(i)).getTagName())) {
                        strStartValue = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagEndOP.equals(((Element) nl.item(i)).getTagName())) {
                        strEndOp = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagEndValue.equals(((Element) nl.item(i)).getTagName())) {
                        strEndValue = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }
}
