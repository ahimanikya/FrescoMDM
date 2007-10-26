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
package com.sun.mdm.index.project.ui.applicationeditor.writers;

import com.sun.mdm.index.project.ui.applicationeditor.EntityNode;
import com.sun.mdm.index.parser.EDMType;

public class EDMWriter {
    final String xmlHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    EntityNode mPrimaryNode;
    String mViewName;
    
    /** Creates a new instance of ObjectWriter */
    public EDMWriter() {
    }
    
    /**
     * Generate the configuration
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     * @return the generated configuration
     */
    public String generate(EntityNode primaryNode, String viewName, EDMType edmType) {
        mPrimaryNode = primaryNode;
        mViewName = viewName;
        
        // create EDM.xml
        String tagHeaderEDM = "<edm xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"sbyn:SeeBeyond/eView/schema/EDM.xsd\">\n";
        String tagTailEDM = "</edm>";

        String strXml = xmlHEADER + tagHeaderEDM +
            getAllNodesForGUI(mPrimaryNode) + getRelationships() + edmType.getImplDetailsXML() +
            edmType.getGuiDefinitionXML() + tagTailEDM;
        
        return strXml;
    }
           
    /** get Relationship
     *
     *@return XML string
     *
     */
    private String getRelationships() {
        String relationships;
        relationships = "    <relationships>\n";
        relationships += ("        <name>" + mPrimaryNode.getName() +
                        "</name>\n");

        int cnt = mPrimaryNode.getChildCount();
        int subNodeCnt = cnt - mPrimaryNode.getFieldCnt();
        if (subNodeCnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) mPrimaryNode.getChildAt(i);

                if (subNode.isSub()) {
                    relationships += ("        " + "<children>");
                    relationships += subNode.getName();
                    relationships += "</children>\n";
                }
            }
        }

        relationships += "    </relationships>\n";

        return relationships;
    }
    
    /** Get all field nodes for current node
     *
     *@return XML string
     *
     */
    private String getFieldNodesForGUI(EntityNode currentNode, String tagName,
        int displayOrder) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            if (displayOrder > 0) {
                nodes = "    <node-" + tagName + " display-order=\"" +
                    displayOrder + "\">\n";
            } else {
                nodes = "    <node-" + tagName + ">\n";
            }

            int fieldDisplayOrder = 1;

            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && (subNode.isJustAdded() || !subNode.isGeneratedField())) {
                    nodes += ("        " + "<field-" + subNode.getName() +
                    ">\n");
                    nodes += ("            <display-name>" +
                    subNode.getDisplayName() + "</display-name>\n");
                    nodes += ("            <display-order>" +
                    fieldDisplayOrder++ + "</display-order>\n");
                    nodes += ("            <max-length>" +
                    subNode.getDataSize() + "</max-length>\n");

                    if ((subNode.getCodeModule() != null) &&
                            (subNode.getCodeModule().length() > 0)) {
                        nodes += "            <gui-type>MenuList</gui-type>\n";
                        nodes += ("            <value-list>" +
                        subNode.getCodeModule() + "</value-list>\n");
                    } else if ((subNode.getUserCode() != null) &&
                            (subNode.getUserCode().length() > 0)) {
                        nodes += "            <gui-type>MenuList</gui-type>\n";
                        nodes += ("            <value-list>" +
                        subNode.getUserCode() + "</value-list>\n");
                    } else {
                        nodes += "            <gui-type>TextBox</gui-type>\n";
                    }

                    nodes += ("            <value-type>" +
                    subNode.getDataType() + "</value-type>\n");

                    if ((subNode.getInputMask() != null) &&
                            (subNode.getInputMask().length() > 0)) {
                        nodes += ("            <input-mask>" +
                        subNode.getInputMask() + "</input-mask>\n");
                    }

                    if ((subNode.getValueMask() != null) &&
                            (subNode.getValueMask().length() > 0)) {
                        nodes += ("            <value-mask>" +
                        subNode.getValueMask() + "</value-mask>\n");
                    }

                    nodes += ("            <key-type>" + subNode.getKeyType() +
                    "</key-type>\n");
                    nodes += ("        " + "</field-" + subNode.getName() +
                    ">\n");
                }
            }

            nodes += ("    </node-" + tagName + ">\n");
        }

        return nodes;
    }

    /** Get all nodes for current node
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getAllNodesForGUI(EntityNode currentNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                nodes += getFieldNodesForGUI(targetNode, currentNode.getName(),
                    i);
                i = 1;
            }

            int j = 1;
            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    nodes += getFieldNodesForGUI(subNode, subNode.getName(), j++);
                }
            }
        }

        return nodes;
    }

}
