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
 * The main generator for the update configuration
 */
public class UpdateWriter {
    /**
     * Candidate field fragment type
     */
    public static final String FRAGMENT_TYPE_CANDIDATE_FIELD = "CANDIDATEFIELD";

    /**
     * The unqualified template file name for this generator.
     */
    public static final String UPDATE_TEMPLATE = "update.xml.tmpl";
    private ConfigSettings mConfigSettings;
    private String mPath;
    private ArrayList candidateFields;

    /**
     * Create instance
     * @param path template path
     * @param configSettings the settings for all the fields in the object model
     */
    public UpdateWriter(String path, ConfigSettings configSettings) {
        mPath = path;
        mConfigSettings = configSettings;
    }

    public ArrayList getCandidateFields() {
        return candidateFields;
    }
    
    /**
     * Generate complete fragment
     * @throws TemplateWriterException An error occurred using the fragment templates for generation
     * @throws ConfigGeneratorException An error occurred generating the fragment
     * @return the fragment Strings
     */
    private ArrayList generateCandidateFields()
        throws TemplateWriterException, ConfigGeneratorException {
        return ConfigGenerator.generateFragment(FRAGMENT_TYPE_CANDIDATE_FIELD,
            mConfigSettings);
    }

    /**
     * Generate the configuration
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     * @return the generated configuration
     */
    public String generate()
        throws TemplateWriterException, ConfigGeneratorException {
        String res = "";

        String objname = mConfigSettings.getPrimaryNode();

        // generate all the fragments
        candidateFields = generateCandidateFields();

        // write the mefa with the accumulated fragments. 
        TemplateWriter mTW = null;
        java.io.InputStream is = this.getClass().getResourceAsStream(UPDATE_TEMPLATE);

        if (is != null) {
            mTW = new TemplateWriter(is, UPDATE_TEMPLATE);

            ArrayList cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(objname);
            values.add(candidateFields);

            String[] aSourceSystems = mConfigSettings.getSourceSystems();
            ArrayList sourcesystems = new ArrayList();

            for (int i = 0; i < aSourceSystems.length; i++) {
                String ss = "<parameter>" + "<quality>SourceSystem</quality>" +
                    "<preference>" + aSourceSystems[i] + "</preference>" +
                    "<utility>100.0</utility>" + "</parameter>";
                sourcesystems.add(ss);
            }

            values.add(sourcesystems);

            res = mTW.writeConstruct((String) cons.get(0), values);
        } else {
            throw new ConfigGeneratorException(
                "Could not generate configuration files. Could not find the resource: " +
                UPDATE_TEMPLATE);
        }

        return res;
    }

}
