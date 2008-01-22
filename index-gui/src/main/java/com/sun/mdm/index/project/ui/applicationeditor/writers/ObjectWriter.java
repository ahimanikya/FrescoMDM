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
import com.sun.mdm.index.project.ui.wizards.generator.ConfigGenerator;
import com.sun.mdm.index.project.ui.wizards.generator.ConfigSettings;
import com.sun.mdm.index.project.ui.wizards.generator.FieldSettings;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

public class ObjectWriter {
    final String xmlHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    ConfigSettings mConfigSettings;
    EntityNode mPrimaryNode;
    String mMatchEngine;
    private ArrayList candidateFields = new ArrayList();
    
    /** Creates a new instance of ObjectWriter */
    public ObjectWriter() {
    }
    
    /**
     * Generate the configuration
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     * @return the generated configuration
     */
    public String generate(EntityNode primaryNode, String viewName, String matchEngine, String database, String dateFormat, String systemList, String duplicateThreshold, String matchThreshold) {
        mPrimaryNode = primaryNode;
        mMatchEngine = matchEngine;
        createRuntimeConfig(systemList, duplicateThreshold, matchThreshold);
        
        String tagHeaderObject = "<Configuration xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"schema/eIndex.xsd\">\n";
        String tagTailObject = "</Configuration>";

        String strXml = xmlHEADER + tagHeaderObject + "    <name>" + viewName +
            "</name>" + "\n    <database>" + database + "</database>" +
            "\n    <dateformat>" + dateFormat + "</dateformat>\n" +
            getSubNodes(primaryNode) + getRelationships() + tagTailObject;

        
        return strXml;
    }
    

    /** Get all nodes for current node
     *
     *@return XML string
     *
     */
    private String getSubNodes(EntityNode currentNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                nodes += getFieldNodes(targetNode, currentNode.getName(), true);
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    nodes += getFieldNodes(subNode, subNode.getName(), false);
                    candidateFields.add(mPrimaryNode.getName() + "." + subNode.getName() + "[*].*");
                }
            }
        }

        return nodes;
    }
    
    /** Get all field nodes for current node
     *
     *@return XML string
     *
     */
    private String getFieldNodes(EntityNode currentNode, String tagName, boolean bPrimaryNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            nodes = "    <nodes>\n";
            nodes += ("        <tag>" + tagName + "</tag>\n");

            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField()) {
                    nodes += ("        " + "<fields>\n");
                    nodes += ("            <field-name>" + subNode.getName() +
                    "</field-name>\n");
                    nodes += ("            <field-type>" +
                    subNode.getDataType() + "</field-type>\n");
                    nodes += ("            <size>" + subNode.getDataSize() +
                    "</size>\n");
                    nodes += ("            <updateable>" +
                    subNode.getUpdateable() + "</updateable>\n");
                    nodes += ("            <required>" + subNode.getRequired() +
                    "</required>\n");

                    //nodes += "            <minimum-value>1900-01-01</minimum-value>\n";
                    nodes += ("            <code-module>" +
                    subNode.getCodeModule() + "</code-module>\n");
                    nodes += ("            <pattern>" + subNode.getPattern() +
                    "</pattern>\n");
                    nodes += ("            <key-type>" + subNode.getKeyType() +
                    "</key-type>\n");
                    if ((subNode.getUserCode() != null) &&
                            (subNode.getUserCode().length() > 0)) {
                        nodes += ("            <user-code>" + subNode.getUserCode() +
                        "</user-code>\n");
                    }
                    if ((subNode.getConstraintBy() != null) &&
                            (subNode.getConstraintBy().length() > 0)) {
                        nodes += ("            <constraint-by>" + subNode.getConstraintBy() +
                        "</constraint-by>\n");
                    }
                    nodes += ("        " + "</fields>\n");

                    //
                    FieldSettings field = new FieldSettings();
                    String matchType = subNode.getMatchType();
                    field.setMatchTypeID(matchType);
                    field.setUnQualifiedFieldName(subNode.getName());

                    if (bPrimaryNode) {
                        field.setDecoratedFieldQualifier(mPrimaryNode.getName());
                        field.setFieldQualifier(mPrimaryNode.getName());
                        candidateFields.add(mPrimaryNode.getName() + "." + subNode.getName());
                    } else {
                        field.setDecoratedFieldQualifier(mPrimaryNode.getName() +
                            "." + currentNode.getName() + "[*]");
                        field.setFieldQualifier(mPrimaryNode.getName() + "." +
                            currentNode.getName());
                    }

                    field.setBlockOn(subNode.getBlocking());

                    String fragment = "";

                    try {
                        fragment = 
                            ConfigGenerator.generateSingleFragment(
                                com.sun.mdm.index.project.ui.wizards.generator.ObjectFieldWriter.FRAGMENT_TYPE_OBJECT_FIELD,
                                field, 
                                mConfigSettings);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!fragment.equals("")) {
                        nodes += fragment + "\n";
                    }
                }
            }

            nodes += "    </nodes>\n";
        }

        return nodes;
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
    

    private void getFieldSettings(Vector vec, EntityNode currentNode, String primaryNodeName, boolean bPrimaryNode) {
        int cnt = currentNode.getChildCount();
        FieldSettings field = null;

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField()) {
                    field = new FieldSettings();

                    String matchType = subNode.getMatchType();
                    field.setMatchTypeID(matchType);
                    field.setUnQualifiedFieldName(subNode.getName());

                    if (bPrimaryNode) {
                        field.setDecoratedFieldQualifier(primaryNodeName);
                        field.setFieldQualifier(primaryNodeName);
                    } else {
                        field.setDecoratedFieldQualifier(primaryNodeName + "." +
                            currentNode.getName() + "[*]");
                        field.setFieldQualifier(primaryNodeName + "." +
                            currentNode.getName());
                    }

                    field.setBlockOn(subNode.getBlocking());
                    vec.add(field);
                }
            }
        }
    }

    private void createRuntimeConfig(String systemList, String duplicateThreshold, String matchThreshold) {
        try {
            mConfigSettings = new ConfigSettings();

            String primaryNodeName = mPrimaryNode.getName();
            mConfigSettings.setPrimaryNode(primaryNodeName);
            mConfigSettings.setMatchEngine(mMatchEngine);
            mConfigSettings.setMatchEngineSeebeyond(true);
            mConfigSettings.setDuplicateThreshold(duplicateThreshold);
            mConfigSettings.setMatchThreshold(matchThreshold);

            int cnt = mPrimaryNode.getChildCount();
            EntityNode currentNode = mPrimaryNode;
            Vector vec = new Vector(cnt, 1);

            if (cnt > 0) {
                int i = 0;
                EntityNode subNode;

                if (currentNode.isPrimary()) {
                    subNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                    getFieldSettings(vec, subNode, primaryNodeName, true);
                    i = 1;
                }

                for (; i < cnt; i++) {
                    subNode = (EntityNode) currentNode.getChildAt(i);

                    if (subNode.isSub()) {
                        getFieldSettings(vec, subNode, primaryNodeName, false);
                    }
                }

                int t = vec.capacity();
                FieldSettings[] fds = new FieldSettings[t];

                for (int j = 0; j < t; j++) {
                    fds[j] = (FieldSettings) vec.get(j);
                }

                mConfigSettings.setFieldSettings(fds);

                StringTokenizer st = new StringTokenizer(systemList, "\t");
                int cntTokens = st.countTokens();
                String[] sourceSystems = new String[cntTokens];
                int index = 0;

                while (st.hasMoreTokens()) {
                    String tmp = st.nextToken();
                    sourceSystems[index++] = tmp;
                }

                mConfigSettings.setSourceSystems(sourceSystems);

                //
                ConfigGenerator.generate(null, mConfigSettings, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList getCandidateFields() {
        return candidateFields;
    }

}
