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
 * The default generator for the 'matching' fragment for the mefa configuration
 */
class MefaMatchingWriter implements GenInterface {
    /**
     * Definition of TEMPLATE_NAME
     */
    private static final String TEMPLATE_NAME = "MefaMatching.tmpl";

    /**
     * Creates an instance
     */
    public MefaMatchingWriter() {
    }

    /**
     * Generate the single fragment
     * @see GenInterface
     */
    public String generate(FieldSettings fieldSettings,
        ConfigSettings configSettings) throws TemplateWriterException {
        String res = null;

        String objName = configSettings.getPrimaryNode();

        if (fieldSettings != null) {
            String matchTypeID = fieldSettings.getMatchTypeID();

            if (matchTypeID != null) {
                MatchType matchType = ConfigGenerator.getMatchTypeDefinition(matchTypeID,
                        configSettings);
                TemplateWriter templateWriter = ConfigGenerator.getFragmentTemplateWriter(matchType,
                        TEMPLATE_NAME);

                if (templateWriter != null) {
                    String undecoratedQualifiedNodeName = fieldSettings.getFieldQualifier();
                    String unqualifiedFieldName = fieldSettings.getUnQualifiedFieldName();
                    String decoratedFieldQualifier = fieldSettings.getDecoratedFieldQualifier();
                    String nodeName = null;
                    //  handles match fields in child objects
                    if (decoratedFieldQualifier.compareTo(undecoratedQualifiedNodeName) == 0) {
                        nodeName = undecoratedQualifiedNodeName;
                    } else {
                        nodeName = decoratedFieldQualifier;
                    }
                    String[] unqualifiedFieldsToMatch = new String[] {unqualifiedFieldName};
                    String[] fieldsToMatch = new String[] {
                            undecoratedQualifiedNodeName + "." + unqualifiedFieldName
                    };
                    String genFieldPrefix = ConfigGenerator.getGenFieldPrefix(fieldSettings);

                    ArrayList cons = templateWriter.construct();
                    ArrayList values = new ArrayList();
                    values.add(objName);
                    values.add(nodeName);

                    ArrayList fieldsToMatchList = new ArrayList();
                    if (fieldsToMatch != null) {
                        for (int fieldCount = 0;
                                fieldCount < fieldsToMatch.length;
                                fieldCount++) {
                            fieldsToMatchList.add(fieldsToMatch[fieldCount]);
                        }
                    }

                    values.add(fieldsToMatchList);
                    values.add(genFieldPrefix);
                    
                    ArrayList unqualifiedFieldsToMatchList = new ArrayList();
                    if (unqualifiedFieldsToMatch != null) {
                        for (int fieldCount = 0;
                                fieldCount < unqualifiedFieldsToMatch.length;
                                fieldCount++) {
                            unqualifiedFieldsToMatchList.add(unqualifiedFieldsToMatch[fieldCount]);
                        }
                    }
                    values.add(unqualifiedFieldsToMatchList);

                    res = templateWriter.writeConstruct((String) cons.get(0),
                            values);
                }
            }
        }

        return res;
    }
}
