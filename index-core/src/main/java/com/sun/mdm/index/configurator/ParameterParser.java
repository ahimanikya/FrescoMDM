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
package com.sun.mdm.index.configurator;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Parser for Parameter configuration object
 *
 * @author dcidon
 */
public class ParameterParser {


    static final String TAG_PARAMETERS = "parameters";
    static final String TAG_PARAMETER = "parameter";
    static final String TAG_PARAMETER_NAME = "parameter-name";
    static final String TAG_PARAMETER_TYPE = "parameter-type";
    static final String TAG_PARAMETER_VALUE = "parameter-value";


    /**
     * Creates a new instance of ParameterParser
     */
    public ParameterParser() {
    }


    /**
     * Getter for Parameters attribute of the ParameterParser object.
     *
     * @param parameterList DOM node for a list of paramters.
     * @return a parsed list of parameters.
     */
    public ArrayList getParameters(Element parameterList) {
        ArrayList paramInsts = new ArrayList();
        // parameter instances collection

        // for each parameter
        NodeList parameters = parameterList.getElementsByTagName(TAG_PARAMETER);
        int paramCount = parameters.getLength();
        for (int i = 0; i < paramCount; i++) {
            Element parameter = (Element) parameters.item(i);

            String paramName = null;
            String paramValue = null;
            String paramType = null;

            NodeList paramSpec = parameter.getChildNodes();
            // parameter specifics : name, value, type
            int paramSpecCount = paramSpec.getLength();
            for (int j = 0; j < paramSpecCount; j++) {
                // get parameter name, value, and type
                Node node = (Node) paramSpec.item(j);

                String nodeName = node.getNodeName();

                if (nodeName.equals(TAG_PARAMETER_NAME)) {
                    // get parameter-name
                    paramName = getStrElementValue(node);
                }
                if (nodeName.equals(TAG_PARAMETER_VALUE)) {
                    // get parameter-value
                    paramValue = getStrElementValue(node);
                }
                if (nodeName.equals(TAG_PARAMETER_TYPE)) {
                    // get parameter-type
                    paramType = getStrElementValue(node);
                }
            }
            // create parameter object
            Parameter p = new Parameter(paramName, paramType, paramValue);

            // store parameter in collection
            paramInsts.add(p);
        }
        return paramInsts;
    }


    /**
     * returns the #text value of an XML node
     *
     * @param node XML node
     * @return #text value as a String object
     */
    private String getStrElementValue(Node node) {
        Node tnode = node.getFirstChild();
        return tnode.getNodeValue();
    }
}
