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
 * The main generator for the mefa configuration
 */
public class MefaWriter {
    /**
     * Standardization fragment type
     */
    public static final String FRAGMENT_TYPE_STANDARDIZATION = "STANDARDIZATION";

    /**
     * Normalization fragment type
     */
    public static final String FRAGMENT_TYPE_NORMALIZATION = "NORMALIZATION";

    /**
     * Phoneticization fragment type
     */
    public static final String FRAGMENT_TYPE_PHONETICIZATION = "PHONETICIZATION";

    /**
     * Matching fragment type
     */
    public static final String FRAGMENT_TYPE_MATCHING = "MATCHING";

    /**
     * The default template file name for this generator.
     */
    public static final String MEFA_TEMPLATE = "Mefa.xml.tmpl";

    private ConfigSettings mConfigSettings;
    private String mPath;
    private String mMefaTmpl = MEFA_TEMPLATE;

    /**
     * Create instance
     * @param path template path
     * @param configSettings the settings for all the fields in the object model
     */
    public MefaWriter(String path, ConfigSettings configSettings) {
        mPath = path;
        mConfigSettings = configSettings;
    }

    /**
     * Generate complete fragment
     * @throws TemplateWriterException An error occurred using the fragment templates for generation
     * @throws ConfigGeneratorException An error occurred generating the fragment
     * @return the fragment Strings
     */
    private ArrayList generateStandardization()
        throws TemplateWriterException, ConfigGeneratorException {
        return ConfigGenerator.generateFragment(FRAGMENT_TYPE_STANDARDIZATION,
            mConfigSettings);
    }

    /**
     * Generate complete fragment
     * @throws TemplateWriterException An error occurred using the fragment templates for generation
     * @throws ConfigGeneratorException An error occurred generating the fragment
     * @return the fragment Strings
     */
    private ArrayList generateNormalization()
        throws TemplateWriterException, ConfigGeneratorException {
        return ConfigGenerator.generateFragment(FRAGMENT_TYPE_NORMALIZATION,
            mConfigSettings);
    }

    /**
     * Generate complete fragment
     * @throws TemplateWriterException An error occurred using the fragment templates for generation
     * @throws ConfigGeneratorException An error occurred generating the fragment
     * @return the fragment Strings
     */
    private ArrayList generatePhoneticization()
        throws TemplateWriterException, ConfigGeneratorException {
        return ConfigGenerator.generateFragment(FRAGMENT_TYPE_PHONETICIZATION,
            mConfigSettings);
    }

    /**
     * Generate complete fragment
     * @throws TemplateWriterException An error occurred using the fragment templates for generation
     * @throws ConfigGeneratorException An error occurred generating the fragment
     * @return the fragment Strings
     */
    private ArrayList generateMatching()
        throws TemplateWriterException, ConfigGeneratorException {
        return ConfigGenerator.generateFragment(FRAGMENT_TYPE_MATCHING,
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
        ArrayList normalization = generateNormalization();
        ArrayList standardization = generateStandardization();
        ArrayList phoneticization = generatePhoneticization();
        ArrayList matching = generateMatching();

        // write the mefa with the accumulated fragments. 
        TemplateWriter mTW = null;
        java.io.InputStream is = this.getClass().getResourceAsStream(mMefaTmpl);

        if (is != null) {
            mTW = new TemplateWriter(is, mMefaTmpl);

            ArrayList cons = mTW.construct();
            ArrayList values = new ArrayList();
            values.add(objname);
            values.add(normalization);
            values.add(standardization);
            values.add(phoneticization);
            values.add(matching);

            res = mTW.writeConstruct((String) cons.get(0), values);
        } else {
            throw new ConfigGeneratorException(
                "Could not generate configuration files. Could not find the resource: " +
                mMefaTmpl);
        }

        return res;
    }

}
