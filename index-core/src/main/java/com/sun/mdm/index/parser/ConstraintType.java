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
public class ConstraintType {
    private final String mFmt = "            [elephantConstraintType]";
    private final String mTagConstraint = "ConstraintType";
    private final String mTagNumericConstraint = "numeric-constraint";
    private final String mTagStringConstraint = "string-constraint";
    private final String mTagDateConstraint = "date-constraint";

    private NumericConstraintType nctNumericConstraint = null;
    private StringConstraintType sctStringConstraint = null;
    private DateConstraintType dctDateConstraint = null;


    /**
     * parse
     * @param node Node
     */
    void parse(Node node) {
        if (node.hasChildNodes()) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if (mTagNumericConstraint.equals(((Element) nl.item(i)).getTagName())) {
                        nctNumericConstraint = new NumericConstraintType();
                        nctNumericConstraint.parse(nl.item(i));
                    }
                    if (mTagDateConstraint.equals(((Element) nl.item(i)).getTagName())) {
                        dctDateConstraint = new DateConstraintType();
                        dctDateConstraint.parse(nl.item(i));
                    }
                    if (mTagStringConstraint.equals(((Element) nl.item(i)).getTagName())) {
                        sctStringConstraint = new StringConstraintType();
                        sctStringConstraint.parse(nl.item(i));
                    }
                }
            }
        }
    }
}
