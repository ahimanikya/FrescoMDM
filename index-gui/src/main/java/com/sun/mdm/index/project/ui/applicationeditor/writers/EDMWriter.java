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
    static final String tab4 = "    ";
    static final String tab8 = "        ";
    static final String tab12 = "            ";
    static final String tab16 = "                ";
    static final String tab20 = "                    ";
    static final String tab24 = "                    ";
    static final String tab28 = "                        ";
    static final String tab32 = "                            ";
    static final String tab36 = "                                ";

    /** Creates a new instance of ObjectWriter */
    public EDMWriter() {
    }
    
    /**
     * Generate the configuration
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     * @return the generated configuration
     */
    public String generateEDM(EntityNode primaryNode, String viewName, EDMType edmType) {
        mPrimaryNode = primaryNode;
        mViewName = viewName;
        
        // create edm.xml
        String tagHeaderEDM = "<edm xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"schema/edm.xsd\">\n";
        String tagTailEDM = "</edm>";

        String strXml = xmlHEADER + tagHeaderEDM +
            getAllNodesForEDM(mPrimaryNode) + getRelationships() + edmType.getImplDetailsXML(false) +
            edmType.getGuiDefinitionXML(false) + tagTailEDM;
        
        return strXml;
    }
    
    public String generateMIDM(EntityNode primaryNode, String viewName, EDMType edmType) {
        mPrimaryNode = primaryNode;
        mViewName = viewName;
        
        // create edm.xml
        String tagHeaderEDM = "<midm xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"schema/midm.xsd\">\n";
        String tagTailEDM = "</midm>";

        String strXml = xmlHEADER + tagHeaderEDM +
            getAllNodesForMIDM(mPrimaryNode) + getRelationships() + edmType.getImplDetailsXML(true) +
            edmType.getGuiDefinitionXML(true) + tagTailEDM;
        
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
    private String getFieldNodesForMidm(EntityNode currentNode, String tagName,
        int displayOrder) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            nodes = tab4 + "<node>\n";
            nodes += tab8 + "<name>" + tagName + "</name>\n";
            if (displayOrder > 0) {
                nodes += tab8 + "<display-order>" + displayOrder + "</display-order>\n";
            }

            int fieldDisplayOrder = 1;

            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && (subNode.isJustAdded() || !subNode.isGeneratedField())) {
                    nodes += (tab8 + "<field>\n");
                    nodes += (tab12 + "<name>" +
                        subNode.getName() + "</name>\n");
                    nodes += (tab12 + "<display-name>" +
                        subNode.getDisplayName() + "</display-name>\n");
                    nodes += (tab12 + "<display-order>" +
                        fieldDisplayOrder++ + "</display-order>\n");
                    nodes += (tab12 + "<max-length>" +
                        subNode.getDataSize() + "</max-length>\n");

                    if ((subNode.getCodeModule() != null) &&
                            (subNode.getCodeModule().length() > 0)) {
                        nodes += tab12 + "<gui-type>MenuList</gui-type>\n";
                        nodes += (tab12 + "<value-list>" + subNode.getCodeModule() + "</value-list>\n");
                    } else if ((subNode.getUserCode() != null) &&
                            (subNode.getUserCode().length() > 0)) {
                        nodes += tab12 + "<gui-type>MenuList</gui-type>\n";
                        nodes += (tab12 + "<value-list>" + subNode.getUserCode() + "</value-list>\n");
                    } else {
                        nodes += tab12 + "<gui-type>TextBox</gui-type>\n";
                    }

                    if ((subNode.getInputMask() != null) &&
                            (subNode.getInputMask().length() > 0)) {
                        nodes += (tab12 + "<input-mask>" + subNode.getInputMask() + "</input-mask>\n");
                    }

                    if ((subNode.getValueMask() != null) &&
                            (subNode.getValueMask().length() > 0)) {
                        nodes += (tab12 + "<value-mask>" + subNode.getValueMask() + "</value-mask>\n");
                    }
                    nodes += (tab12 + "<value-type>" + subNode.getDataType() + "</value-type>\n");
                    nodes += (tab12 + "<key-type>" + subNode.getKeyType() + "</key-type>\n");
                    nodes += (tab8 + "</field>\n");
                }
            }

            nodes += (tab4 + "</node>\n");
        }

        return nodes;
    }

    /** Get all nodes for current node
     *  For edm.xml
     *
     *@return XML string
     *
     */

    private String getAllNodesForMIDM(EntityNode currentNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                nodes += getFieldNodesForMidm(targetNode, currentNode.getName(),
                    i);
                i = 1;
            }

            int j = 1;
            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    nodes += getFieldNodesForMidm(subNode, subNode.getName(), j++);
                }
            }
        }

        return nodes;
    }
    
    private String getFieldNodesForEDM(EntityNode currentNode, String tagName,
        int displayOrder) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            if (displayOrder > 0) {
                nodes = tab4 + "<node-" + tagName + " display-order=\"" +
                    displayOrder + "\">\n";
            } else {
                nodes = tab4 + "<node-" + tagName + ">\n";
            }

            int fieldDisplayOrder = 1;

            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && (subNode.isJustAdded() || !subNode.isGeneratedField())) {
                    nodes += (tab8 + "<field-" + subNode.getName() + ">\n");
                    nodes += (tab12 + "<display-name>" +
                    subNode.getDisplayName() + "</display-name>\n");
                    nodes += (tab12 + "<display-order>" +
                    fieldDisplayOrder++ + "</display-order>\n");
                    nodes += (tab12 + "<max-length>" +
                    subNode.getDataSize() + "</max-length>\n");

                    if ((subNode.getCodeModule() != null) &&
                            (subNode.getCodeModule().length() > 0)) {
                        nodes += tab12 + "<gui-type>MenuList</gui-type>\n";
                        nodes += (tab12 + "<value-list>" + subNode.getCodeModule() + "</value-list>\n");
                    } else if ((subNode.getUserCode() != null) &&
                            (subNode.getUserCode().length() > 0)) {
                        nodes += tab12 + "<gui-type>MenuList</gui-type>\n";
                        nodes += (tab12 + "<value-list>" + subNode.getUserCode() + "</value-list>\n");
                    } else {
                        nodes += tab12 + "<gui-type>TextBox</gui-type>\n";
                    }

                    if ((subNode.getInputMask() != null) &&
                            (subNode.getInputMask().length() > 0)) {
                        nodes += (tab12 + "<input-mask>" + subNode.getInputMask() + "</input-mask>\n");
                    }

                    if ((subNode.getValueMask() != null) &&
                            (subNode.getValueMask().length() > 0)) {
                        nodes += (tab12 + "<value-mask>" + subNode.getValueMask() + "</value-mask>\n");
                    }
                    nodes += (tab12 + "<value-type>" + subNode.getDataType() + "</value-type>\n");
                    nodes += (tab12 + "<key-type>" + subNode.getKeyType() + "</key-type>\n");
                    nodes += (tab8 + "</field-" + subNode.getName() + ">\n");
                }
            }

            nodes += (tab4 + "</node-" + tagName + ">\n");
        }

        return nodes;
    }

    private String getAllNodesForEDM(EntityNode currentNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                nodes += getFieldNodesForEDM(targetNode, currentNode.getName(),
                    i);
                i = 1;
            }

            int j = 1;
            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    nodes += getFieldNodesForEDM(subNode, subNode.getName(), j++);
                }
            }
        }

        return nodes;
    }

}
