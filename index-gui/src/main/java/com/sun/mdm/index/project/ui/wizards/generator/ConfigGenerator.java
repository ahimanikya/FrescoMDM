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

import java.util.ArrayList;
import java.util.HashMap;
import com.sun.mdm.index.project.generator.TemplateWriter;
import com.sun.mdm.index.project.generator.exception.TemplateWriterException;
import org.openide.WizardDescriptor;
import com.sun.mdm.index.project.ui.wizards.Properties;

public class ConfigGenerator {
    /**
     * The configuration identifier for EDM 
     */
    public static final String CONFIG_EDM = "EDM";
    /**
     * The configuration identifier for MIDM 
     */
    public static final String CONFIG_MIDM = "MIDM";
    /**
     * The configuration identifier for mefa 
     */
    public static final String CONFIG_MEFA = "MEFA";
    /**
     * The configuration identifier for master 
     */    
    public static final String CONFIG_MASTER = "MASTER";
    /**
     * The configuration identifier for query
     */    
    public static final String CONFIG_QUERY = "QUERY";
    /**
     * The configuration identifier for update
     */    
    public static final String CONFIG_UPDATE = "UPDATE";
    /**
     * The configuration identifier for validation
     */    
    public static final String CONFIG_VALIDATION = "VALIDATION";
    /**
     * The configuration identifier for security
     */    
    public static final String CONFIG_SECURITY = "SECURITY";
    /**
     * Seebeyond match engine directory
     */ 
    public static final String SEEBEYOND_DIR = "seebeyond";
                                                     
    /**
     * Defines where to find the fragments for fields without match type
     */        
    protected static String defaultFragmentDir = "fragments" + java.io.File.separator + "default";   
    
    private static String edm;
    private static String midm;
    private static String mefa;
    private static String query;
    private static String update;
    private static ArrayList candidateFields;
    private static String master;
    private static String security;
    private static String validation;
    
    /**
     * Generates the Master Index configurations edm, mefa, master, query, update, validation
     * @param genpath an optional path to write the configuration files to. Passing in <code>null<code>
     * will mean that no files are written, the generated configurations will be returned as Strings
     * from the method.
     * @param settings the settings for all the Fields to generate the configuration for.
     * @param wiz  the wizard descriptor
     * @return a HashMap from the configuration identifier to the generated configuration String
     * @throws ConfigGeneratorException if the generation failed
     */
    public static HashMap generate(String genpath, ConfigSettings settings, WizardDescriptor wiz) 
            throws ConfigGeneratorException {
                
        HashMap configs = new HashMap();
        try {
            if (settings.getMasterIndexEDM() == true) {
                EDMWriter edmW = new EDMWriter(genpath, settings);
                //edm = edmW.generate();
                //configs.put(CONFIG_EDM, edm);
                //if (wiz != null) {
                //    wiz.putProperty(Properties.PROP_XML_GUI_CONFIG_FILE, edm);
                //}
                midm = edmW.generateMidm();
                configs.put(CONFIG_MIDM, midm);
                if (wiz != null) {
                    wiz.putProperty(Properties.PROP_XML_MIDM_CONFIG_FILE, midm);
                }
            }

            MefaWriter mefaW = new MefaWriter(genpath, settings);
            mefa = mefaW.generate();
            configs.put(CONFIG_MEFA, mefa);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_MEFA_CONFIG_FILE, mefa);
            }

            MasterWriter masterW = new MasterWriter(genpath, settings);
            master = masterW.generate();
            configs.put(CONFIG_MASTER, master);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_MASTER_CONFIG_FILE, master);
            }

            QueryWriter queryW = new QueryWriter(genpath, settings);
            query = queryW.generate();
            configs.put(CONFIG_QUERY, query);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_QUERY_CONFIG_FILE, query);
            }

            UpdateWriter updateW = new UpdateWriter(genpath, settings);
            update = updateW.generate();
            configs.put(CONFIG_UPDATE, update);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_UPDATE_CONFIG_FILE, update);
            }
            candidateFields = updateW.getCandidateFields();
            // TODO: log as debug
            //System.out.println("Generating - update");            
            ValidationWriter validationW = new ValidationWriter(genpath, settings);
            validation = validationW.generate();
            configs.put(CONFIG_VALIDATION, validation);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_VALID_CONFIG_FILE, validation);
            }
            
            SecurityWriter securityW = new SecurityWriter(genpath, settings);
            String security = securityW.generate();
            configs.put(CONFIG_SECURITY, security);
            if (wiz != null) {
                wiz.putProperty(Properties.PROP_XML_SECURITY_CONFIG_FILE, security);
            }
            
            if (genpath != null) {
                String mPath = genpath;
                String mefaFileName = mPath + java.io.File.separator + "mefa.xml";
                java.io.FileOutputStream mefaOut = new java.io.FileOutputStream(mefaFileName);
                mefaOut.write(mefa.getBytes());
                mefaOut.flush();
                mefaOut.close();
                String masterFileName = mPath + java.io.File.separator + "master.xml";
                java.io.FileOutputStream masterOut = new java.io.FileOutputStream(masterFileName);
                masterOut.write(master.getBytes());
                masterOut.flush();
                masterOut.close();
                String queryFileName = mPath + java.io.File.separator + "query.xml";
                java.io.FileOutputStream queryOut = new java.io.FileOutputStream(queryFileName);
                queryOut.write(query.getBytes());
                queryOut.flush();
                queryOut.close();
                String updateFileName = mPath + java.io.File.separator + "update.xml";
                java.io.FileOutputStream updateOut = new java.io.FileOutputStream(updateFileName);
                updateOut.write(update.getBytes());
                updateOut.flush();
                updateOut.close();
                String validationFileName = mPath + java.io.File.separator + "validation.xml";
                java.io.FileOutputStream validationOut = new java.io.FileOutputStream(validationFileName);
                validationOut.write(validation.getBytes());
                validationOut.flush();
                validationOut.close();
                String securityFileName = mPath + java.io.File.separator + "security.xml";
                java.io.FileOutputStream securityOut = new java.io.FileOutputStream(securityFileName);
                securityOut.write(security.getBytes());
                securityOut.flush();
                securityOut.close();
            }
        } catch (Exception ex) {
            throw new ConfigGeneratorException("Failed to generate configuration: " + ex.getMessage(), ex);
        }
        return configs;
    }    
    
    /**
     * Get all the available match type definitions
     * @param matchEngineType match engine type
     * @return the available match types
     */
    public static MatchType[] getMatchTypes(String matchEngineType) {
        MatchType[] matchTypes = null;
        matchTypes = new MatchType[8];
        int matchTypeIndex = 0;
        
        MatchType matchType = new MatchType();
        matchType.setMatchTypeID("Address");
        matchType.setDescription("Address"); // Properties file
        
        java.util.HashMap fragmentIDToGenerator = new java.util.HashMap();
        
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_STANDARDIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaStandardizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_NORMALIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaNormalizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_PHONETICIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaPhoneticizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        fragmentIDToGenerator.put(ObjectFieldWriter.FRAGMENT_TYPE_OBJECT_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.ObjectFieldWriter");
        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "address");
        matchType.setGenerators(fragmentIDToGenerator);
        
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("PersonFirstName");
        matchType.setDescription("Person Firstname"); // Properties file
        fragmentIDToGenerator = new java.util.HashMap();
        
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_STANDARDIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaStandardizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_NORMALIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaNormalizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_PHONETICIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaPhoneticizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        fragmentIDToGenerator.put(ObjectFieldWriter.FRAGMENT_TYPE_OBJECT_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.ObjectFieldWriter");
        matchType.setFragmentDir("fragments" + java.io.File.separator 
                + SEEBEYOND_DIR + java.io.File.separator + "personnamefirstname");
        matchType.setGenerators(fragmentIDToGenerator);

        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("PersonLastName");
        matchType.setDescription("Person Lastname"); // Properties file
        fragmentIDToGenerator = new java.util.HashMap();
        
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_STANDARDIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaStandardizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_NORMALIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaNormalizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_PHONETICIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaPhoneticizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        fragmentIDToGenerator.put(ObjectFieldWriter.FRAGMENT_TYPE_OBJECT_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.ObjectFieldWriter");
        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "personnamelastname");
        matchType.setGenerators(fragmentIDToGenerator);

        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;
        
        
        matchType = new MatchType();
        matchType.setMatchTypeID("BusinessName");
        matchType.setDescription("Business Name"); // Properties file
        fragmentIDToGenerator = new java.util.HashMap();
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_STANDARDIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaStandardizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_NORMALIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaNormalizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_PHONETICIZATION, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaPhoneticizationWriter");
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        fragmentIDToGenerator.put(ObjectFieldWriter.FRAGMENT_TYPE_OBJECT_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.ObjectFieldWriter");
        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "businessname");
        matchType.setGenerators(fragmentIDToGenerator);
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("Char");
        matchType.setDescription("Character"); // Properties file

        fragmentIDToGenerator = new java.util.HashMap();
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        matchType.setGenerators(fragmentIDToGenerator);

        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "char");
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("String");
        matchType.setDescription("String"); // Properties file

        fragmentIDToGenerator = new java.util.HashMap();
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        matchType.setGenerators(fragmentIDToGenerator);

        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "string");
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("Date");
        matchType.setDescription("Date"); // Properties file

        fragmentIDToGenerator = new java.util.HashMap();
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        matchType.setGenerators(fragmentIDToGenerator);

        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "date");
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;

        matchType = new MatchType();
        matchType.setMatchTypeID("Number");
        matchType.setDescription("Number"); // Properties file

        fragmentIDToGenerator = new java.util.HashMap();
        fragmentIDToGenerator.put(MefaWriter.FRAGMENT_TYPE_MATCHING, 
            "com.sun.mdm.index.project.ui.wizards.generator.MefaMatchingWriter");
        fragmentIDToGenerator.put(QueryWriter.FRAGMENT_TYPE_BLOCK, 
            "com.sun.mdm.index.project.ui.wizards.generator.QueryBlockWriter");
        fragmentIDToGenerator.put(UpdateWriter.FRAGMENT_TYPE_CANDIDATE_FIELD, 
            "com.sun.mdm.index.project.ui.wizards.generator.UpdateCandidateFieldWriter");
        matchType.setGenerators(fragmentIDToGenerator);

        matchType.setFragmentDir("fragments" + java.io.File.separator 
            + SEEBEYOND_DIR + java.io.File.separator + "number");
        matchTypes[matchTypeIndex] = matchType;
        matchTypeIndex++;
        
        return matchTypes;
    }
    
    /**
     * Get a match type definition by ID
     * @param matchTypeID the id of the match type to get the definition for
     * @return the match type definition
     */
    public static MatchType getMatchTypeDefinition(String matchTypeID, ConfigSettings configSettings) {
        MatchType ret = null;
        
        String matchEngineType = configSettings.getMatchEngine();
        MatchType[] matchTypes = getMatchTypes(matchEngineType);
        
        for (int matchCount = 0; ret == null && matchCount < matchTypes.length; matchCount++) {
            if (matchTypes[matchCount].getMatchTypeID().equals(matchTypeID)) {
                ret = matchTypes[matchCount];
            }
        }
        return ret;
    }
    
    /**
     * utility function to determine the prefix to use for the generated fields 
     * corresponding to a given object model field
     * @param settings the settings for the field to get the prefix for
     * @return the prefix to use with added, generated fields
     */ 
    protected static String getGenFieldPrefix(FieldSettings settings) {
        return settings.getUnQualifiedFieldName() + "_";
    }
    
    /**
     * Get the relevant fragment template writer
     * @param matchType the match type to get the template writer for
     * @param unQualifiedTemplateName the name of the template definition
     * @return the relavant TemplateWriter
     * @throws com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException if the template 
     * could not be found for the template writer.
     */
    protected static TemplateWriter getFragmentTemplateWriter(MatchType matchType, String unQualifiedTemplateName) 
            throws com.sun.mdm.index.project.generator.exception.TemplateFileNotFoundException {
        TemplateWriter writer = null;
        String templateName = getQualifiedTemplateName(matchType, unQualifiedTemplateName);
        // TODO: Retrive from repository as an option
        java.io.InputStream is = ConfigGenerator.class.getResourceAsStream(templateName);
        // Try with a name with forward slashes just in case the classloader doesn't understand the separator.
        if (is == null)  {
            String replacedWithSlash = templateName.replace(java.io.File.separatorChar, '/');
            is = ConfigGenerator.class.getResourceAsStream(replacedWithSlash);            
        }
        if (is != null) {
            writer = new TemplateWriter(is, templateName);        
        } else {
            writer = null;
        }
        return writer;
    }
        
    /**
     * Converts a template definition name (unqualified) into the qualified definition name,
     * determined by the matchType. For example a unqualified template name 'mefa.tmpl' might get
     * converted into address/mefa.tmpl to find the template relevant to the address match type.
     * @param matchType the match type to get the qualified name for
     * @param unQualifiedTemplateName the unqualified template name
     * @return the qualified template name
     */
    protected static String getQualifiedTemplateName(MatchType matchType, String unQualifiedTemplateName) {
        String templatePath = null;
        if (matchType != null) {
            templatePath = matchType.getFragmentDir();
        } else {
            templatePath = getDefaultFragmentDir();
        }
        String qualifiedTemplateName = templatePath + java.io.File.separator + unQualifiedTemplateName;
        return qualifiedTemplateName;
    }
    
    /**
     * Get the folder of where the fragment definitions are for fields
     * without a match type.
     * @return the directory of the folder containing the fragments
     */
    protected static String getDefaultFragmentDir() {
        return defaultFragmentDir;
    }
    
    /**
     * Generate the complete fragment for a given fragment type, for all fields.
     * @param fragmentType the fragment type identifier to generate the fragment for
     * @param configSettings the settings for all the fields to generate for
     * @return an ArrayList of Strings containing the single fragment String for each 
     * field a fragment was generated for.
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     */
    public static ArrayList generateFragment(String fragmentType, ConfigSettings configSettings) 
            throws TemplateWriterException, ConfigGeneratorException {
        ArrayList fragments = new ArrayList();
        FieldSettings[] fieldSettings = configSettings.getFieldSettings();
        
        //  This is for the Seebeyond Match Engine and non-matching fragments
        for (int fieldCount = 0; fieldSettings != null && fieldCount < fieldSettings.length; fieldCount++) {
            String matchTypeID = fieldSettings[fieldCount].getMatchTypeID();
            MatchType matchType = null;
            if (matchTypeID != null && matchTypeID.length() > 0) {
                matchType = ConfigGenerator.getMatchTypeDefinition(matchTypeID, configSettings);
            }
            GenInterface gen = FragmentGeneratorFactory.getGenInstance(matchType, fragmentType);
            if (gen != null) {
                String fragment = gen.generate(fieldSettings[fieldCount], configSettings);
                if (fragment != null) {
                    fragments.add(fragment);
                }
            }
        }
        return fragments;
    }    
    
    
    /**
     * Generate the a single fragment for a given fragment type, for one field.
     * @param fragmentType the fragment type identifier to generate the fragment for
     * @param fieldSettings the settings for the fields to generate for
     * @param configSettings the settings for all the fields in the object model
     * @return the String containing the single fragment for the given field. 
     * May return null if no fragment is to be generated for it.
     * @throws TemplateWriterException A problem occurred whilst using a generator template
     * @throws ConfigGeneratorException A problem occurred during the configuration generation
     */    
    public static String generateSingleFragment(String fragmentType, FieldSettings fieldSettings, 
            ConfigSettings configSettings) 
            throws TemplateWriterException, ConfigGeneratorException {
        String singleFragment = "";
        if (fieldSettings != null) {
            String matchTypeID = fieldSettings.getMatchTypeID();
            MatchType matchType = null;
            if (matchTypeID != null && matchTypeID.length() > 0) {
                matchType = ConfigGenerator.getMatchTypeDefinition(matchTypeID, configSettings);
            }
            GenInterface gen = FragmentGeneratorFactory.getGenInstance(matchType, fragmentType);
            if (gen != null) {
                String fragment = gen.generate(fieldSettings, configSettings);
                if (fragment != null) {
                    singleFragment = fragment;
                }
            }
        }
        
        return singleFragment;
    }    
        
    public static String getMefa() {
        return mefa;
    }
    
    public static String getQuery() {
        return query;
    }
    
    public static String getMaster() {
        return master;
    }
    
    public static String getUpdate() {
        return update;
    }
    
    public static ArrayList getCandidateFields() {
        return candidateFields;
    }
    
    public static String getSecurity() {
        return security;
    }
    
    public static String getValidation() {
        return validation;
    }
}
