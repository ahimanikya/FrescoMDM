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
package com.sun.mdm.index.project.ui.wizards.generator;

import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;

import java.util.ArrayList;


/**
 * The default generator for the 'candidate field' fragment for the update configuration
 */
class UpdateCandidateFieldWriter implements GenInterface {
    private static final String PRIMARY_NODE_TEMPLATE_NAME = "UpdateCandidateField.tmpl";
    private static final String SECONDARY_NODE_TEMPLATE_NAME = "UpdateCandidateNodeSecondary.tmpl"; 


    /**
     * Creates an instance
     */
    public UpdateCandidateFieldWriter() {
    }

    /**
     * @see GenInterface
     */
    public String generate(FieldSettings fieldSettings,
        ConfigSettings configSettings) throws TemplateWriterException {
        String res = null;

        String objName = configSettings.getPrimaryNode();

        if (fieldSettings != null) {
            String matchTypeID = fieldSettings.getMatchTypeID();
            MatchType matchType = null;

            if (matchTypeID != null) {
                matchType = ConfigGenerator.getMatchTypeDefinition(matchTypeID,
                        configSettings);
            }
            String targetQualifiedNodeName = fieldSettings.getDecoratedFieldQualifier();
            boolean isSecondary = targetQualifiedNodeName.indexOf('.') > -1;
            TemplateWriter templateWriter = null;

            if (isSecondary) {
                if (isFirstFieldInNode(configSettings, fieldSettings)) {
                    templateWriter = ConfigGenerator.getFragmentTemplateWriter(matchType,
                            SECONDARY_NODE_TEMPLATE_NAME);
                }
            } else {
                templateWriter = ConfigGenerator.getFragmentTemplateWriter(matchType,
                        PRIMARY_NODE_TEMPLATE_NAME);
            }
            if (templateWriter != null) {
                String fieldName = fieldSettings.getUnQualifiedFieldName();
                String genFieldPrefix = ConfigGenerator.getGenFieldPrefix(fieldSettings);

                ArrayList cons = templateWriter.construct();
                ArrayList values = new ArrayList();
                values.add(objName);
                values.add(targetQualifiedNodeName);
                values.add(genFieldPrefix);
                values.add(fieldName);

                res = templateWriter.writeConstruct((String) cons.get(0), values);
            }
        }

        return res;
    }
    
    /**
     * Check whether the current field is the first on an object node.
     * @param configSettings the configuration for all the fields in the object model
     * @param fieldSettings the configuration for the current field to check
     * @return true if it is the first
     */

    private boolean isFirstFieldInNode(ConfigSettings configSettings, FieldSettings fieldSettings) {
        FieldSettings[] fields = configSettings.getFieldSettings();
        java.util.List fieldsList = java.util.Arrays.asList(fields);
        int currentFieldPos = fieldsList.indexOf(fieldSettings);
        boolean isFirst = true;

        if (currentFieldPos > 0) {
            FieldSettings currentField = (FieldSettings) fieldsList.get(currentFieldPos);
            String currentFieldQualifier = currentField.getFieldQualifier();
            FieldSettings previousField = (FieldSettings) fieldsList.get(currentFieldPos - 1);
            String previousFieldQualifier = previousField.getFieldQualifier();
            // if the field qualifier for the previous field is the same as the current field, 
            //the current field is not the first field on a node.
            if (previousFieldQualifier.equals(currentFieldQualifier)) {
                isFirst = false;
            }
        }

        return isFirst;
    }
}
