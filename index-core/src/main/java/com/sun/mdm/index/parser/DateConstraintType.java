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
public class DateConstraintType {
    private final String mFmt = "            [DateConstraintType]";
    private final String mTagDateConstraint = "date-constraint";
    private final String mTagBeginDateOP = "begin-date-op";
    private final String mTagBeginDateValue = "begin-date-value";
    private final String mTagEndDateOP = "end-date-op";
    private final String mTagEndDateValue = "end-date-value";

    private String strBeginDateOp = null;
    private String strBeginDateValue = null;
    private String strEndDateOp = null;
    private String strEndDateValue = null;


    /**
     * parse
     * @param node Node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagBeginDateOP.equals(((Element) nl.item(i)).getTagName())) {
                        strBeginDateOp = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagBeginDateValue.equals(((Element) nl.item(i)).getTagName())) {
                        strBeginDateValue = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagEndDateOP.equals(((Element) nl.item(i)).getTagName())) {
                        strEndDateOp = Utils.getStrElementValue(nl.item(i));
                    }
                    if (mTagEndDateValue.equals(((Element) nl.item(i)).getTagName())) {
                        strEndDateValue = Utils.getStrElementValue(nl.item(i));
                    }
                }
            }
        }
    }
}
