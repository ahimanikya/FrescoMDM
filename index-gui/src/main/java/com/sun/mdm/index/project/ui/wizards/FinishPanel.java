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
package com.sun.mdm.index.project.ui.wizards;

import com.sun.mdm.index.project.ui.wizards.generator.ConfigGenerator;
import com.sun.mdm.index.project.ui.wizards.generator.ConfigSettings;
import com.sun.mdm.index.project.ui.wizards.generator.FieldSettings;

import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;

/** A single panel descriptor for a wizard.
 * You probably want to make a wizard iterator to hold it.
 *
 */
public class FinishPanel implements WizardDescriptor.Panel {
    // The eVision generator 'factory factory' class to use
    static final String DEFAULT_EVISION_GENERATOR_FACTORY_FACTORY = "com.stc.dap.ui.gen.GeneratorFactoryFactory";

    // The identifier to pass to eVision to generate the Master Index site.
    static final String EVISION_GENERATOR_EVIEW = "eView";
    static final String DB_ORACLE = "oracle";
    static final String DB_SYBASE = "sybase";
    static final String DB_SQLSERVER = "sql server";
    static final String DB_DB2 = "db2";
    static final String CODE_LIST_ORACLE_TEMPLATE = "codelist.oracle.tmpl";
    static final String CODE_LIST_SYBASE_TEMPLATE = "codelist.sybase.tmpl";
    static final String CODE_LIST_SQLSERVER_TEMPLATE = "codelist.sqlserver.tmpl";
    static final String CODE_LIST_DB2_TEMPLATE = "codelist.db2.tmpl";
    static final String SYSTEMS_ORACLE_TEMPLATE = "system.oracle.tmpl";
    static final String SYSTEMS_SYBASE_TEMPLATE = "system.sybase.tmpl";
    static final String SYSTEMS_SQLSERVER_TEMPLATE = "system.sqlserver.tmpl";
    static final String SYSTEMS_DB2_TEMPLATE = "system.db2.tmpl";
    static final String TAG_HEADER = "<header>";
    static final String TAG_TRAILER = "<trailer>";
    static final String TAG_REPEAT = "<repeat>";
    static final String TAG_HEADER_END = "</header>";
    static final String TAG_TRAILER_END = "</trailer>";
    static final String TAG_REPEAT_END = "</repeat>";
    static final String TAG_MODULE_COMMENT = "$<module-comment>";
    static final String TAG_MODULE = "$<module>";
    static final String TAG_MODULE_DESCRIPTION = "$<module-description>";
    static final String TAG_CODE = "$<code>";
    static final String TAG_CODE_DESCRIPTION = "$<code-description>";
    static final String TAG_SYSTEM = "$<system>";
    static final String TAG_SYSTEM_DESCRIPTION = "$<system-description>";
    static final String tab4 = "    ";
    static final String tab8 = "        ";
    static final String tab12 = "            ";
    static final String tab16 = "                ";
    static final String tab20 = "                    ";
    static final String tab24 = "                    ";
    static final String tab28 = "                        ";
    static final String tab32 = "                            ";
    static final String tab36 = "                                ";
    
    /** The visual component that displays this panel.
     * If you need to access the component from this class,
     * just use getComponent().
     */
    private FinishVisualPanel mComponent;
    private DefaultTreeModel mEntityTreeModel = null;
    private EntityNode mRootNode;
    private EntityNode mPrimaryNode;
    private ConfigSettings mConfigSettings;
    final String xmlHEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            FinishPanel.class.getName()
        );

    String mViewName;
    String mDbName;
    String mMatchEngine;
    String mDateFormat;
    String mTransaction;
    String mMasterIndexEDM;
    ArrayList mList = new ArrayList();
    final boolean bDEBUG = false;

    //Instance mInstance;  // Master Index App Instance

    /** Create the wizard panel descriptor. */
    public FinishPanel() {
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.

    /**
     *
     *@return FinishVisualPanel
     */
    public Component getComponent() {
        if (mComponent == null) {
            mComponent = new FinishVisualPanel(this);
        }

        return mComponent;
    }

    /**
     *@return HelpCtx
     */
    public HelpCtx getHelp() {
        return new HelpCtx("Generate");
    }

    /**
     *@return if Next button should be enabled
     */
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;

        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    /** not implemented
     *@param l ChangeListener
     */
    public final void addChangeListener(ChangeListener l) {
    }

    /** not implemented
     *@param l ChangeListener
     */
    public final void removeChangeListener(ChangeListener l) {
    }

    /*
    private final Set listeners = new HashSet(1); // Set<ChangeListener>
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent() {
        Iterator it;
        synchronized (listeners) {
            it = new HashSet(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            ((ChangeListener)it.next()).stateChanged(ev);
        }
    }
     */

    // You can use a settings object to keep track of state.
    // Normally the settings object will be the WizardDescriptor,
    // so you can use WizardDescriptor.getProperty & putProperty
    // to store information entered by the user.

    /** reade user inputs from previous panels
     *
     *@param settings WizardDescriptor
     */
    public void readSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        String viewName = wiz.getProperty(Properties.PROP_TARGET_VIEW_NAME)
                             .toString();

        // set the visual
        getComponent();
        mComponent.setViewName(viewName);
    }

    /** Save user inputs
     *@param settings WizardDescriptor
     */
    public void storeSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        instantiate(wiz);
        wiz.putProperty(Properties.PROP_OBJECT_NAME, mPrimaryNode.getName());
        wiz.putProperty(Properties.PROP_AUTO_GENERATE, (mComponent.getAutoGenerate() ? "Yes" : "No"));
        wiz.putProperty("WizardPanel_image", null);
    }

    /**
     * When Finish button is pressed...
     * Generate the configuration files -
     * <View>.xml, EDM.xml and runtime config
     *
     * @param wiz the WizardDescriptor
     */
    public void instantiate(WizardDescriptor wiz) {
        // get stuff for <object>.xml
        mViewName = wiz.getProperty(Properties.PROP_TARGET_VIEW_NAME).toString();
        mDbName = wiz.getProperty(Properties.PROP_DATABASE).toString();
        mMatchEngine = wiz.getProperty(Properties.PROP_MATCH_ENGINE).toString();
        mDateFormat = wiz.getProperty(Properties.PROP_DATE_FORMAT).toString();
        mTransaction = wiz.getProperty(Properties.PROP_TRANSACTION).toString();
        mMasterIndexEDM = wiz.getProperty(Properties.PROP_MASTER_INDEX_EDM).toString();
        
        mEntityTreeModel = DefineEntityVisualPanel.getTreeModel();
        mRootNode = (EntityNode) mEntityTreeModel.getRoot();
        mPrimaryNode = (EntityNode) mRootNode.getChildAt(0);

        createRuntimeConfig(wiz);
        createObjectXml(wiz);
        if (mMasterIndexEDM.equals("No")) {
            createClassicEDMXml(wiz);
        }
        createDatabaseCodeList(wiz);
        createDatabaseSystemList(wiz);

        // SearchInfo for eVision
        setSearchInfo();

        try {
            // Call eVision generators
            // in eVision terms, instance name is the view name defined in the wizard
            String eViewInstanceName = mViewName;

            // in eVision, the 'view' name refers to the primary object node name
            String businessObjectName = mPrimaryNode.getName();

            // @TODO: replace with the project name the wizard was started from once available from repository impl.
            String projectName = mPrimaryNode.getName();

            invokeEVisionGenerators(eViewInstanceName, businessObjectName,
                projectName);
        } catch (Exception ex) {
//            mLogger.debug("Failed to generate eVision site. ");
            ex.printStackTrace();
        }
    }

    /**
     * Generate an eVision site if eVision generators are available.
     * @param eViewInstanceName the Master Index instance name defined in the wizard
     * @param businessObjectName the primary object name
     * @param projectName the project name the wizard was started from
     * @throws SiteGenerationException if an error was encountered during site generation
     */
    private void invokeEVisionGenerators(String eViewInstanceName,
        String businessObjectName, String projectName)
        throws SiteGenerationException {
        java.util.Map context = new java.util.HashMap();

        // in eVision terms, instance name is the view name defined in the wizard
        context.put("eViewInstanceName", eViewInstanceName);

        // in eVision, the 'view' name refers to the primary object node name
        context.put("viewName", businessObjectName);
        context.put("projectName", projectName);

        // If the eVision generator is available, invoke the equivalent of the
        // following code via reflection.
        //      com.stc.dap.ui.gen.GeneratorFactory fact = 
        //           com.stc.dap.ui.gen.GeneratorFactoryFactory.getInstance("eView");
        //      com.stc.dap.ui.gen.Generator generator = fact.createGenerator();
        //      generator.generate(context);
        String factoryFactoryName = DEFAULT_EVISION_GENERATOR_FACTORY_FACTORY;
        java.lang.Class factoryFactory = null;

        try {
            factoryFactory = Class.forName(factoryFactoryName);
        } catch (ClassNotFoundException ex) {
            // This may not be an error. Possibly only Master Index is installed and not eVision (on purpose)
//            mLogger.debug("No eVision generator factory is available for " +
//                EVISION_GENERATOR_EVIEW +
//                ". Skipping generation of eVision site.");
            factoryFactory = null;
        }

        try {
            if (factoryFactory != null) {
                // call the static factory factory 'getInstance' method
                java.lang.reflect.Method getInstanceMeth = factoryFactory.getMethod("getInstance",
                        new Class[] {String.class});
                java.lang.Object factoryInstance = getInstanceMeth.invoke((java.lang.Object) null,
                        new java.lang.Object[] {EVISION_GENERATOR_EVIEW});

                // call the factory 'createGenerator' method                                    
                java.lang.reflect.Method createGeneratorMeth = factoryInstance.getClass()
                                                                              .getMethod("createGenerator",
                        null);
                java.lang.Object generatorInstance = createGeneratorMeth.invoke(factoryInstance,
                        null);

                // call the Generator 'generate' method                
                java.lang.reflect.Method generatorMeth = generatorInstance.getClass()
                                                                          .getMethod("generate",
                        new Class[] {java.util.Map.class});
                generatorMeth.invoke(generatorInstance,
                    new java.lang.Object[] {context});
            }
        } catch (Exception ex) {
            throw new SiteGenerationException("Failed to generate Master Index site in eVision.",
                ex);
        }
    }

    private StringBuffer getCodeListFromNode(EntityNode subNode, String sRepeat) {
        String code = subNode.getCodeModule();
        StringBuffer codeBuffer = null;
        
        if ((code != null) && (code.length() != 0)) {
	        codeBuffer = new StringBuffer(sRepeat);
            replaceString(codeBuffer, TAG_MODULE_COMMENT, code);
            replaceString(codeBuffer, TAG_MODULE, code);
            replaceString(codeBuffer, TAG_MODULE_DESCRIPTION,
                "module description");
            replaceString(codeBuffer, TAG_CODE, "code");
            replaceString(codeBuffer, TAG_CODE_DESCRIPTION,
                "code description");
        }
        return codeBuffer;
    }
    
    private void createDatabaseCodeList(WizardDescriptor wiz) {
        String template = null;

        if (mDbName.equalsIgnoreCase(DB_ORACLE)) {
            template = CODE_LIST_ORACLE_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_SYBASE)) {
            template = CODE_LIST_SYBASE_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_SQLSERVER)) {
            template = CODE_LIST_SQLSERVER_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_DB2)) {
            template = CODE_LIST_DB2_TEMPLATE;
        } else {
//            mLogger.debug("Unsupported database vendor type: " + mDbName);
        }

        java.io.InputStream is = this.getClass().getResourceAsStream(template);

        if (is == null) {
//            mLogger.debug("Could not find the resource: " + template);
        }

        StringBuffer sb = new StringBuffer();

        try {
            byte[] buffer = new byte[4096];
            int count;

            while ((count = is.read(buffer)) >= 0) {
                sb.append(new String(buffer, 0, count, "ISO8859-1"));
            }

            is.close();
        } catch (java.io.IOException e) {
//            mLogger.debug("Error in accessing database script template: " +
//                e.getMessage());
            e.printStackTrace();
        }

        int from = sb.indexOf(TAG_HEADER) + TAG_HEADER.length();
        int to = sb.indexOf(TAG_HEADER_END, from);
        String sHeader = sb.substring(from, to);

        from = sb.indexOf(TAG_REPEAT, to) + TAG_REPEAT.length();
        to = sb.indexOf(TAG_REPEAT_END, from);

        String sRepeat = sb.substring(from, to);

        from = sb.indexOf(TAG_TRAILER, to) + TAG_TRAILER.length();
        to = sb.indexOf(TAG_TRAILER_END, from);

        String sTrailer = sb.substring(from, to);

        StringBuffer strCodeList = new StringBuffer(sHeader);

        int count = 0;
        for (int i = 0; i < mPrimaryNode.getChildCount(); i++) {
            StringBuffer sbTemp;
            EntityNode node = (EntityNode) mPrimaryNode.getChildAt(i);
            if (node.isField()) {
                sbTemp = getCodeListFromNode(node, sRepeat);
                if (sbTemp != null) {
                    strCodeList.append(sbTemp);
                    count++;
                }
            } else if (node.isSub()) {
                for (int j = 0; j < node.getChildCount(); j++) {
                    EntityNode subNode = (EntityNode) node.getChildAt(j);

                    if (subNode.isField()) {
                        sbTemp = getCodeListFromNode(subNode, sRepeat);
                        if (sbTemp != null) {
                            strCodeList.append(sbTemp);
                            count++;
                        }
                    }
                }
            }
        }
        if (count > 0) {
            strCodeList.setLength(strCodeList.lastIndexOf(","));
        }
        strCodeList.append(sTrailer);

        if (wiz != null) {
            wiz.putProperty(Properties.PROP_DBSCRIPT_CODELIST,
                strCodeList.toString());
        }
    }

    private void createDatabaseSystemList(WizardDescriptor wiz) {
        String template = null;

        if (mDbName.equalsIgnoreCase(DB_ORACLE)) {
            template = SYSTEMS_ORACLE_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_SYBASE)) {
            template = SYSTEMS_SYBASE_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_SQLSERVER)) {
            template = SYSTEMS_SQLSERVER_TEMPLATE;
        } else if (mDbName.equalsIgnoreCase(DB_DB2)) {
            template = SYSTEMS_DB2_TEMPLATE;
        } else {
//            mLogger.debug("Unsupported database vendor type: " + mDbName);
        }

        java.io.InputStream is = this.getClass().getResourceAsStream(template);

        if (is == null) {
//            mLogger.debug("Could not find the resource: " + template);
        }

        StringBuffer sb = new StringBuffer();

        try {
            byte[] buffer = new byte[4096];
            int count;

            while ((count = is.read(buffer)) >= 0) {
                sb.append(new String(buffer, 0, count, "ISO8859-1"));
            }

            is.close();
        } catch (java.io.IOException e) {
//            mLogger.debug(
//                "Error in accessing database script template for systems: " +
//                e.getMessage());
            e.printStackTrace();
        }

        int from = sb.indexOf(TAG_HEADER) + TAG_HEADER.length();
        int to = sb.indexOf(TAG_HEADER_END, from);
        String sHeader = sb.substring(from, to);

        from = sb.indexOf(TAG_REPEAT, to) + TAG_REPEAT.length();
        to = sb.indexOf(TAG_REPEAT_END, from);

        String sRepeat = sb.substring(from, to);

        from = sb.indexOf(TAG_TRAILER, to) + TAG_TRAILER.length();
        to = sb.indexOf(TAG_TRAILER_END, from);

        String sTrailer = sb.substring(from, to);

        String systemList = wiz.getProperty(Properties.PROP_SOURCE_SYSTEMS)
                               .toString();
        StringBuffer strSystems = new StringBuffer(sHeader);

        StringTokenizer st = new StringTokenizer(systemList, "\t");

        while (st.hasMoreTokens()) {
            String tmp = st.nextToken();
            try {
                tmp = tmp.replaceAll("'", "''");
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (PatternSyntaxException pe) {
                pe.printStackTrace();
            }
            StringBuffer sBuffer = new StringBuffer(sRepeat);
            replaceString(sBuffer, TAG_SYSTEM, tmp);
            replaceString(sBuffer, TAG_SYSTEM_DESCRIPTION, tmp);
            strSystems.append(sBuffer);
        }

        strSystems.append(sTrailer);

        if (wiz != null) {
            wiz.putProperty(Properties.PROP_DBSCRIPT_SYSTEMS,
                strSystems.toString());
        }
    }

    private void createObjectXml(WizardDescriptor wiz) {
        String tagHeaderObject = "<Configuration xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"schema/eIndex.xsd\">\n";
        String tagTailObject = "</Configuration>";

        String strXml = xmlHEADER + tagHeaderObject + "    <name>" + mViewName +
            "</name>" + "\n    <database>" + mDbName + "</database>" +
            "\n    <dateformat>" + mDateFormat + "</dateformat>\n" +
            getSubNodes(mPrimaryNode) + getRelationships() + tagTailObject;

        // Write xml to repository
        try {
            wiz.putProperty(Properties.PROP_XML_OBJECT_DEF_FILE, strXml);
        } catch (Exception e) {
            e.printStackTrace();
//            mLogger.debug("Write to PROP_XML_OBJECT_DEF_FILE failed!");
        }
    }

    private void createClassicEDMXml(WizardDescriptor wiz) {
        // create EDM.xml
        String tagHeaderEDM = "<edm xmlns:xsi=" +
            "\"http://www.w3.org/2001/XMLSchema-instance" +
            "\" xsi:noNamespaceSchemaLocation=\"schema/EDM.xsd\">\n";
        String tagTailEDM = "</edm>";

        String strXml = xmlHEADER + tagHeaderEDM +
            getAllNodesForGUI(mPrimaryNode) + getRelationships() + getImpl() +
            getGUIDefinition() + tagTailEDM;

        // Write xml to repository
        try {
            wiz.putProperty(Properties.PROP_XML_GUI_CONFIG_FILE, strXml);
        } catch (Exception e) {
            e.printStackTrace();
//            mLogger.debug("Write to PROP_XML_GUI_CONFIG_FILE failed!");
        }
    }

    private void getFieldSettings(Vector vec, EntityNode currentNode,
        String primaryNodeName, boolean bPrimaryNode) {
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

    private void createRuntimeConfig(WizardDescriptor wiz) {
        try {
            mConfigSettings = new ConfigSettings();

            String primaryNodeName = mPrimaryNode.getName();
            mConfigSettings.setPrimaryNode(primaryNodeName);
            mConfigSettings.setMatchEngine(mMatchEngine);
            mConfigSettings.setMatchEngineSeebeyond(true);
            mConfigSettings.setTransaction(mTransaction);
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

                //
                String systemList = wiz.getProperty(Properties.PROP_SOURCE_SYSTEMS)
                                       .toString();

                StringTokenizer st = new StringTokenizer(systemList, "\t");
                int cntTokens = st.countTokens();
                String[] sourceSystems = new String[cntTokens];
                int index = 0;

                while (st.hasMoreTokens()) {
                    String tmp = st.nextToken();
                    sourceSystems[index++] = tmp;
                }

                mConfigSettings.setSourceSystems(sourceSystems);

                // New MI EDM
                if (mMasterIndexEDM.equals("Yes")) {
                    ArrayList alEdmAllNodes = getAlAllNodesForGUI(mPrimaryNode);
                    ArrayList alSubObjects = getSubObjects();
                    mConfigSettings.setEdmAllNodes(alEdmAllNodes);
                    mConfigSettings.setSubObjects(alSubObjects);
                    //getAlSimpleSearchFieldRef
                    mConfigSettings.setSimpleSearchFieldGroup(getAlSimpleSearchFieldGroup());
                    mConfigSettings.setSearchResultFieldRef(getAlSearchResultFieldRef(tab28));
                    mConfigSettings.setSearchResultFieldRef2(getAlSearchResultFieldRef(tab36));
                    mConfigSettings.setReportFields(getAlReportFields());
                    mConfigSettings.setMasterIndexEDM(true);
                }
                ConfigGenerator.generate(null, mConfigSettings, wiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Get all field nodes for current node
     *
     *@return XML string
     *
     */
    private String getFieldNodes(EntityNode currentNode, String tagName,
        boolean bPrimaryNode) {
        String nodes = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            nodes = tab4 + "<nodes>\n";
            nodes += (tab8 + "<tag>" + tagName + "</tag>\n");

            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField()) {
                    nodes += (tab8 + "<fields>\n");
                    nodes += (tab12 + "<field-name>" + subNode.getName() + "</field-name>\n");
                    nodes += (tab12 + "<field-type>" + subNode.getDataType() + "</field-type>\n");
                    nodes += (tab12 + "<size>" + subNode.getDataSize() + "</size>\n");
                    nodes += (tab12 + "<updateable>" +
                    subNode.getUpdateable() + "</updateable>\n");
                    nodes += (tab12 + "<required>" + subNode.getRequired() + "</required>\n");
                    nodes += (tab12 + "<code-module>" + subNode.getCodeModule() + "</code-module>\n");
                    nodes += (tab12 + "<pattern>" + subNode.getPattern() + "</pattern>\n");
                    nodes += (tab12 + "<key-type>" + subNode.getKeyType() +"</key-type>\n");
                    if ((subNode.getUserCode() != null) &&
                            (subNode.getUserCode().length() > 0)) {
                        nodes += (tab12 + "<user-code>" + subNode.getUserCode() + "</user-code>\n");
                    }
                    if ((subNode.getConstraintBy() != null) &&
                            (subNode.getConstraintBy().length() > 0)) {
                        nodes += (tab12 + "<constraint-by>" + subNode.getConstraintBy() + "</constraint-by>\n");
                    }
                    nodes += (tab8 + "</fields>\n");

                    //
                    FieldSettings field = new FieldSettings();
                    String matchType = subNode.getMatchType();
                    field.setMatchTypeID(matchType);
                    field.setUnQualifiedFieldName(subNode.getName());

                    if (bPrimaryNode) {
                        field.setDecoratedFieldQualifier(mPrimaryNode.getName());
                        field.setFieldQualifier(mPrimaryNode.getName());
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

            nodes += tab4 + "</nodes>\n";
        }

        return nodes;
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
    private String getFieldNodesForGUI(EntityNode currentNode, String tagName,
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

                if (subNode.isField()) {
                    nodes += (tab8 + "<field-" + subNode.getName() +
                    ">\n");
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

                    nodes += (tab12 + "<value-type>" + subNode.getDataType() + "</value-type>\n");

                    if ((subNode.getInputMask() != null) &&
                            (subNode.getInputMask().length() > 0)) {
                        nodes += (tab12 + "<input-mask>" + subNode.getInputMask() + "</input-mask>\n");
                    }

                    if ((subNode.getValueMask() != null) &&
                            (subNode.getValueMask().length() > 0)) {
                        nodes += (tab12 + "<value-mask>" + subNode.getValueMask() + "</value-mask>\n");
                    }

                    nodes += (tab12 + "<key-type>" + subNode.getKeyType() + "</key-type>\n");
                    nodes += (tab8 + "</field-" + subNode.getName() + ">\n");
                }
            }

            nodes += (tab4 + "</node-" + tagName + ">\n");
        }

        return nodes;
    }

    private ArrayList getAlAllNodesForGUI(EntityNode currentNode) {
        ArrayList nodes = new ArrayList();
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                nodes.add(getFieldNodesForGUI(targetNode, currentNode.getName(), i));
                i = 1;
            }

            int j = 1;
            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    nodes.add(getFieldNodesForGUI(subNode, subNode.getName(), j++));
                }
            }
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

    /** get Relationship
     *
     *@return XML string
     *
     */
    private String getRelationships() {
        String relationships = tab4 + "<relationships>\n";
        relationships += tab8 + "<name>" + mPrimaryNode.getName() + "</name>\n";
        int cnt = mPrimaryNode.getChildCount();
        int subNodeCnt = cnt - mPrimaryNode.getFieldCnt();
        if (subNodeCnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) mPrimaryNode.getChildAt(i);
                if (subNode.isSub()) {
                    relationships += tab8 + "<children>" + subNode.getName() + "</children>\n";
                }
            }
        }
        relationships += "    </relationships>\n";
        
        return relationships;
    }
    
    private ArrayList getSubObjects() {
        ArrayList relationships = new ArrayList();
        int cnt = mPrimaryNode.getChildCount();
        int subNodeCnt = cnt - mPrimaryNode.getFieldCnt();
        if (subNodeCnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) mPrimaryNode.getChildAt(i);

                if (subNode.isSub()) {
                    relationships.add(tab8 + "<children>" + subNode.getName() + "</children>");
                }
            }
        }
        return relationships;
    }
    
    private ArrayList getAlGUIDefinition() {
        ArrayList guiDefinition = new ArrayList();
        //guiDefinition.add(getRecordDetails());
        //guiDefinition.add(getTransactions());
        //guiDefinition.add(getDuplicateRecords());
        //guiDefinition.add(getAssumedMatches());
        //guiDefinition.add(getReports());
        guiDefinition.add(getAuditLog());
        return guiDefinition;
    }

    /** Get gui definition
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getGUIDefinition() {
        String guiDefinition;

        guiDefinition = tab4 + "<gui-definition>\n";
        guiDefinition += tab8 + "<system-display-name-overrides>\n";
        guiDefinition += tab12 + "<local-id-header>" + geLocalIdIdentifier() + "</local-id-header>\n";
        guiDefinition += tab12 + "<local-id>" + geLocalIdIdentifier() + "</local-id>\n";
        guiDefinition += tab8 + "</system-display-name-overrides>\n";
        guiDefinition += tab8 + "<page-definition>\n";
        guiDefinition += tab12 + "<initial-screen>" + getInitialScreen() + "</initial-screen>\n";
        guiDefinition += tab12 + "<eo-search>\n";
        guiDefinition += tab16 + "<root-object>" + mPrimaryNode.getName() + "</root-object>\n";
        guiDefinition += tab16 + "<tab-name>" + mPrimaryNode.getName() + " Search</tab-name>\n";
        guiDefinition += tab16 + "<tab-entrance>/EnterEOSearchSimpleAction.do</tab-entrance>\n";
        guiDefinition += getSimpleSearch(mPrimaryNode);
        guiDefinition += getSimpleLookup();
        guiDefinition += getSearchResultList(mPrimaryNode);
        guiDefinition += getEoViewPage();
        guiDefinition += tab12 + "</eo-search>\n";
        guiDefinition += getCreateEO();
        guiDefinition += getHistory();
        guiDefinition += getMatchingReview();
        guiDefinition += getReportGenerator(mPrimaryNode);
        guiDefinition += getAuditLog();
        guiDefinition += tab8 + "</page-definition>\n";
        guiDefinition += tab4 + "</gui-definition>\n";

        return guiDefinition;
    }

    private ArrayList getAlFieldGroups(EntityNode currentNode, String tagName, String tab) {
        ArrayList alFieldGroup = new ArrayList();
        boolean bFound = false;
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && subNode.getUsedInSearchScreen()) {
                    if (!bFound) {
                        alFieldGroup.add(tab + "<field-group>");
                        alFieldGroup.add(tab + "    <description>" + tagName + "</description>");
                        bFound = true;
                    }

                    alFieldGroup.add(tab + "    <field-ref required=\"" + subNode.getSearchRequired() + "\">" + tagName + "." +
                                    subNode.getName() + "</field-ref>");
                }
            }

            if (bFound) {
                alFieldGroup.add(tab + "</field-group>");
            }
        }

        return alFieldGroup;
    }
    
    /** Get field group for a subnode
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getFieldGroups(EntityNode currentNode, String tagName, String tab) {
        String fieldGroup = "";
        boolean bFound = false;
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && subNode.getUsedInSearchScreen()) {
                    if (!bFound) {
                        fieldGroup = tab + "<field-group>\n";
                        fieldGroup += tab + "    <description>" + tagName + "</description>\n";
                        bFound = true;
                    }

                    fieldGroup += tab + "    <field-ref required=\"" + subNode.getSearchRequired() + "\">" + tagName + "." +
                                    subNode.getName() + "</field-ref>\n";
                }
            }

            if (bFound) {
                fieldGroup += tab + "</field-group>\n";
            }
        }

        return fieldGroup;
    }

    /**  Retrieve the initial screen
     *
     * @return  String containing information about the initial screen
     */
    private String getInitialScreen() {
        return "EO Search";
    }
    
    /**  Retrieve the local ID identifier
     *
     * @return  String containing the local ID identifier
     */
    private String geLocalIdIdentifier() {
        return "Local ID";
    }
    
    /** Get simple search
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getSimpleSearch(EntityNode currentNode) {
        String simpleSearch;
        // simple search
        simpleSearch = tab16 + "<simple-search-page>\n";
        simpleSearch += tab20 + "<screen-title>" + mPrimaryNode.getName() + " Search</screen-title>\n";
        simpleSearch += tab20 + "<field-per-row>2</field-per-row>\n";
        simpleSearch += tab20 + "<show-euid>false</show-euid>\n";
        simpleSearch += tab20 + "<show-lid>false</show-lid>\n";

        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                simpleSearch += getFieldGroups(targetNode, currentNode.getName(), tab20);
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    simpleSearch += getFieldGroups(subNode, subNode.getName(), tab20);
                }
            }
        }

        simpleSearch += tab20 + "<search-option>\n";
        simpleSearch += tab24 + "<display-name>Phonetic Search</display-name>\n";
        simpleSearch += tab24 + "<query-builder>PHONETIC-SEARCH</query-builder>\n";
        simpleSearch += tab24 + "<weighted>true</weighted>\n";
        simpleSearch += tab24 + "<parameter>\n";
        simpleSearch += tab28 + "<name>UseWildCard</name>\n";
        simpleSearch += tab28 + "<value>false</value>\n";
        simpleSearch += tab24 + "</parameter>\n";
        simpleSearch += tab20 + "</search-option>\n";
        simpleSearch += tab20 + "<search-option>\n";
        simpleSearch += tab24 + "<display-name>Alpha Search</display-name>\n";
        simpleSearch += tab24 + "<query-builder>ALPHA-SEARCH</query-builder>\n";
        simpleSearch += tab24 + "<weighted>false</weighted>\n";
        simpleSearch += tab24 + "<parameter>\n";
        simpleSearch += tab28 + "<name>UseWildCard</name>\n";
        simpleSearch += tab28 + "<value>true</value>\n";
        simpleSearch += tab24 + "</parameter>\n";
        simpleSearch += tab20 + "</search-option>\n";
        simpleSearch += tab20 + "<search-option>\n";
        simpleSearch += tab24 + "<display-name>Blocker Search</display-name>\n";
        simpleSearch += tab24 + "<query-builder>BLOCKER-SEARCH</query-builder>\n";
        simpleSearch += tab24 + "<weighted>true</weighted>\n";
        simpleSearch += tab24 + "<parameter>\n";
        simpleSearch += tab28 + "<name>UseWildCard</name>\n";
        simpleSearch += tab28 + "<value>false</value>\n";
        simpleSearch += tab24 + "</parameter>\n";
        simpleSearch += tab20 + "</search-option>\n";
        simpleSearch += tab16 + "</simple-search-page>\n";

        return simpleSearch;
    }

    /** Get field ref
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private ArrayList getAlFieldRef(EntityNode currentNode, String tagName, String tab) {
        ArrayList alFieldRef = new ArrayList();
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && subNode.getDisplayedInSearchResult()) {
                    alFieldRef.add(tab + "<field-ref>" + tagName + "." + subNode.getName() + "</field-ref>");
                }
            }
        }

        return alFieldRef;
    }

    /** Get field ref
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getSearchResultFieldRef(EntityNode currentNode, String tagName, String tab) {
        String fieldRef = "";
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isField() && subNode.getDisplayedInSearchResult()) {
                    fieldRef += (tab + "<field-ref>");
                    fieldRef += (tagName + "." + subNode.getName());
                    fieldRef += "</field-ref>\n";
                }
            }
        }

        return fieldRef;
    }

    // simple lookup
    private String getSimpleLookup() {
        String simpleLookup;

        simpleLookup = tab16 + "<simple-search-page>\n";
        simpleLookup += tab20 + "<screen-title>" + mPrimaryNode.getName() + " Lookup</screen-title>\n";
        simpleLookup += tab20 + "<field-per-row>2</field-per-row>\n";
        simpleLookup += tab20 + "<show-euid>true</show-euid>\n";
        simpleLookup += tab20 + "<show-lid>true</show-lid>\n";
        simpleLookup += tab20 + "<search-option>\n";
        simpleLookup += tab24 + "<display-name>Alpha Search</display-name>\n";
        simpleLookup += tab24 + "<query-builder>ALPHA-SEARCH</query-builder>\n";
        simpleLookup += tab24 + "<weighted>false</weighted>\n";
        simpleLookup += tab24 + "<parameter>\n";
        simpleLookup += tab28 + "<name>UseWildCard</name>\n";
        simpleLookup += tab28 + "<value>true</value>\n";
        simpleLookup += tab24 + "</parameter>\n";
        simpleLookup += tab20 + "</search-option>\n";
        simpleLookup += tab16 + "</simple-search-page>\n";

        return simpleLookup;
    }

    private String getSearchResultList(EntityNode currentNode) {
        String searchResultList = "";

        searchResultList += tab16 + "<search-result-list-page>\n";
        searchResultList += tab20 + "<item-per-page>10</item-per-page>\n";
        searchResultList += tab20 + "<max-result-size>100</max-result-size>\n";

        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                searchResultList += getSearchResultFieldRef(targetNode, currentNode.getName(), tab20);
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    searchResultList += getSearchResultFieldRef(subNode, subNode.getName(), tab20);
                }
            }
        }

        searchResultList += tab16 + "</search-result-list-page>\n";

        return searchResultList;
    }

    private String getEoViewPage() {
        String s;
        String tab1 = "";
        s = tab16 + "<eo-view-page>\n";
        s += tab20 + "<field-per-row>2</field-per-row>\n";
        s += tab16 + "</eo-view-page>\n";

        return s;
    }

    private String getCreateEO() {
        String s;

        s = tab12 + "<create-eo>\n";
        s += tab16 + "<root-object>" + mPrimaryNode.getName() + "</root-object>\n";
        s += tab16 + "<tab-name>Create System Record</tab-name>\n";
        s += tab16 + "<tab-entrance>/EnterEOCreateAction.do</tab-entrance>\n";
        s += tab12 + "</create-eo>\n";

        return s;
    }

    private String getHistory() {
        String history;

        history = tab12 + "<history>\n";
        history += tab16 + "<root-object>" + mPrimaryNode.getName() + "</root-object>\n";
        history += tab16 + "<tab-name>History</tab-name>\n";
        history += tab16 + "<tab-entrance>/EnterXASearchAction.do</tab-entrance>\n";
        history += tab16 + "<xa-search-page>\n";
        history += tab20 + "<field-per-row>2</field-per-row>\n";
        history += tab16 + "</xa-search-page>\n";

        // ????
        history += tab16 + "<search-result-list-page>\n";
        history += tab20 + "<item-per-page>10</item-per-page>\n";
        history += tab20 + "<max-result-size>100</max-result-size>\n";
        history += tab16 + "</search-result-list-page>\n";
        history += tab12 + "</history>\n";

        return history;
    }

    private String getMatchingReview() {
        String s;
        s = tab12 + "<matching-review>\n";
        s += tab16 + "<root-object>" + mPrimaryNode.getName() + "</root-object>\n";
        s += tab16 + "<tab-name>Matching Review</tab-name>\n";
        s += tab16 + "<tab-entrance>/EnterPDSearchAction.do</tab-entrance>\n";
        s += tab16 + "<pd-search-page>\n";
        s += tab20 + "<field-per-row>2</field-per-row>\n";
        s += tab16 + "</pd-search-page>\n";

        // ????
        s += tab16 + "<search-result-list-page>\n";
        s += tab20 + "<item-per-page>10</item-per-page>\n";
        s += tab20 + "<max-result-size>100</max-result-size>\n";
        s += tab16 + "</search-result-list-page>\n";
        s += tab12 + "</matching-review>\n";

        return s;
    }
  
    private ArrayList getAlSimpleSearchFieldGroup() {
        EntityNode currentNode = mPrimaryNode;
        String fieldGroup = "";
        ArrayList alRet = new ArrayList();
        int cnt = currentNode.getChildCount();
        if (cnt > 0) {
            int i = 0;
            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode;
                fieldGroup += getFieldGroups(targetNode, currentNode.getName(), tab28);
                i = 1;
            }
            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    fieldGroup += getFieldGroups(subNode, currentNode.getName() + "." + subNode.getName(), tab28);
                }
            }
        }
        int idx = fieldGroup.lastIndexOf('\n');
        if (idx > 0) {
            fieldGroup = fieldGroup.substring(0, idx);
        }

        alRet.add(fieldGroup);
        return alRet;
    }

    private ArrayList getAlSearchResultFieldRef(String tab) {
        EntityNode currentNode = mPrimaryNode;
        String fieldRef = "";
        ArrayList alRet = new ArrayList();
        
        int cnt = currentNode.getChildCount();
        if (cnt > 0) {
            int i = 0;
            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode;
                String str = getSearchResultFieldRef(targetNode, currentNode.getName(), tab + tab4);
                if (str.length() > 0) {
                    fieldRef += (tab + "<field-group>\n");
                    fieldRef += (tab + tab4 + "<description/>\n");
                    fieldRef += str;
                    fieldRef += (tab + "</field-group>\n");
                }
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    String str = getSearchResultFieldRef(subNode, currentNode.getName() + "." + subNode.getName(), tab + tab4);
                    if (str.length() > 0) {
                        fieldRef += (tab + "<field-group>\n");
                        fieldRef += (tab + tab4 + "<description/>\n");
                        fieldRef += str;
                        fieldRef += (tab + "</field-group>\n");
                    }
                }
            }
        }
        int idx = fieldRef.lastIndexOf('\n');
        if (idx > 0) {
            fieldRef = fieldRef.substring(0, idx);
        }
        alRet.add(fieldRef);
        return alRet;
    }
    
    private ArrayList getAlReportFields() {
        EntityNode currentNode = mPrimaryNode;
        String fieldReport = "";
        ArrayList alRet = new ArrayList();
        int cnt = currentNode.getChildCount();
        if (cnt > 0) {
            int i = 0;
            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode;
                fieldReport += getReportFields(targetNode, currentNode.getName(), tab36);
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);
                if (subNode.isSub()) {
                    fieldReport += getReportFields(subNode, subNode.getName(), tab36);
                }
            }
        }
        alRet.add(fieldReport);
        return alRet;
    }

    /** Get field report for a subnode
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getReportFields(EntityNode currentNode, String tagName, String tab) {
        String fieldReport = "";
        int cnt = currentNode.getChildCount();
        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);
                if (subNode.isField() && subNode.getGenerateReport()) {
                    fieldReport += tab + "<field-ref>" + tagName + "." +
                                   subNode.getName() + "</field-ref>\n";
                }
            }
        }
        return fieldReport;
    }

    /** Get Report Generator
     *  For EDM.xml
     *
     *@return XML string
     *
     */
    private String getReportGenerator(EntityNode currentNode) {
        String reportGenerator;

        // reports
        reportGenerator = tab12 + "<reports>\n";
        reportGenerator += (tab16 + "<root-object>" + mPrimaryNode.getName() +
        "</root-object>\n");
        reportGenerator += tab16 + "<tab-name>Reports</tab-name>\n";
        reportGenerator += tab16 + "<tab-entrance>/EnterReportSearchAction.do</tab-entrance>\n";
        reportGenerator += tab16 + "<search-page-field-per-row>2</search-page-field-per-row>\n";

        String reportFields = tab20 + "<fields>\n";

        int cnt = currentNode.getChildCount();
        if (cnt > 0) {
            int i = 0;

            if (currentNode.isPrimary()) {
                EntityNode targetNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                reportFields += getReportFields(targetNode, currentNode.getName(), tab24);
                i = 1;
            }

            for (; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    reportFields += getReportFields(subNode, subNode.getName(), tab24);
                }
            }
        }
        reportFields += tab20 + "</fields>\n";
        
        // dynamic reports
        reportGenerator += tab16 + "<report name=\"Assumed Match\" title=\"Assumed Match Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Potential Duplicate\" title=\"Potential Duplicate Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Deactivated\" title=\"Deactivated Record Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Merged\" title=\"Merged Transaction Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Unmerged\" title=\"Unmerged Transaction Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Update\" title=\"Updated Record Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += reportFields;
        reportGenerator += tab16 + "</report>\n";
        // fixed reports
        reportGenerator += tab16 + "<report name=\"Weekly Activity\" title=\"Transaction Audit Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += tab20 + "<fields></fields>\n";
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Monthly Activity\" title=\"Transaction Summary Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += tab20 + "<fields></fields>\n";
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab16 + "<report name=\"Yearly Activity\" title=\"Transaction Summary Report\">\n";
        reportGenerator += tab20 + "<enable>true</enable>\n";
        reportGenerator += tab20 + "<max-result-size>2000</max-result-size>\n";
        reportGenerator += tab20 + "<fields></fields>\n";
        reportGenerator += tab16 + "</report>\n";
        
        reportGenerator += tab12 + "</reports>\n";

        return reportGenerator;
    }

    private String getAuditLog() {
        String s;
        s = tab12 + "<audit-log>\n";
        s += tab16 + "<allow-insert>false</allow-insert>\n";
        s += tab12 + "</audit-log>\n";

        return s;
    }

    private String getImpl() {
        String implDetails = 
            tab4 + "<impl-details>\n" +
            tab8 + "<master-controller-jndi-name>ejb/" + mViewName + "MasterController</master-controller-jndi-name>\n" +
            tab8 + "<validation-service-jndi-name>ejb/" + mViewName + "CodeLookup</validation-service-jndi-name>\n" +
            tab8 + "<usercode-jndi-name>ejb/" + mViewName + "UserCodeLookup</usercode-jndi-name>\n" +
            tab8 + "<reportgenerator-jndi-name>ejb/" + mViewName + "ReportGenerator</reportgenerator-jndi-name>\n" +
            tab8 + "<debug-flag>true</debug-flag>\n" + 
            tab8 + "<debug-dest>console</debug-dest>\n" +
            tab8 + "<enable-security>false</enable-security>\n" +
            tab8 + "<object-sensitive-plug-in-class></object-sensitive-plug-in-class>\n" +
            tab4 + "</impl-details>\n";

        return implDetails;
    }

    /**
    * @param str string to be coneverted to File.
    * @param  outFileName file where str needs to put
    * @throws IOException standard IOException
    */
    private static void stringToFile(String str, String outFileName)
        throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(
                    outFileName));
        os.write(str.getBytes());
        os.close();
    }

    private void setSearchInfo() {
        SearchInfo si = new SearchInfoImpl(mPrimaryNode);

        si.setAvailableSearchFields(getFieldsForSearch(mPrimaryNode, true, false));

        if (bDEBUG) {
            String[] siRet = si.getAvailableSearchFields(mViewName);

            for (int i = 0; i < siRet.length; i++) {
//                mLogger.debug("available search field: " + siRet[i]);
            }
        }

        si.setDefaultSearchFields(getFieldsForSearch(mPrimaryNode, false, false));

        if (bDEBUG) {
            String[] siRet = si.getDefaultSearchFields(mViewName);

            for (int i = 0; i < siRet.length; i++) {
//                mLogger.debug("default search field: " + siRet[i]);
            }
        }

        si.setAvailableResultFields(getFieldsForSearch(mPrimaryNode, true, true));

        if (bDEBUG) {
            String[] siRet = si.getAvailableResultFields(mViewName);

            for (int i = 0; i < siRet.length; i++) {
//                mLogger.debug("available result field: " + siRet[i]);
            }
        }

        si.setDefaultResultFields(getFieldsForSearch(mPrimaryNode, false, true));

        if (bDEBUG) {
            String[] siRet = si.getDefaultResultFields(mViewName);

            for (int i = 0; i < siRet.length; i++) {
//                mLogger.debug("default result field: " + siRet[i]);
            }
        }
    }

    /** Get field in object.field format
     *  for serach info used in web services
     *
     * Build strings in array which will be converted to String[]
     * Use: mList
     *
     */
    private void getFields(EntityNode currentNode, String tagName,
        boolean bGetAvailable, boolean bForResult) {
        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) currentNode.getChildAt(i);

                if ((subNode.isField()) &&
                        (bGetAvailable ||
                        (bForResult && subNode.getDisplayedInSearchResult()) ||
                        (!bForResult && subNode.getUsedInSearchScreen()))) {
                    mList.add(tagName + "." + subNode.getName());
                }
            }
        }
    }

    /** Get field in object.field format
     *  for serach info used in web services
     *
     * Build strings by convering mList
     * Use: mList
     *
     */
    private String[] getFieldsForSearch(EntityNode currentNode,
        boolean bGetAvailable, boolean bForResult) {
        mList.clear();

        int cnt = currentNode.getChildCount();

        if (cnt > 0) {
            int i = 0;
            EntityNode subNode;

            if (currentNode.isPrimary()) {
                subNode = currentNode; //(EntityNode) currentNode.getChildAt(0);
                getFields(subNode, currentNode.getName(), bGetAvailable,
                    bForResult);
                i = 1;
            }

            for (; i < cnt; i++) {
                subNode = (EntityNode) currentNode.getChildAt(i);

                if (subNode.isSub()) {
                    getFields(subNode, subNode.getName(), bGetAvailable,
                        bForResult);
                }
            }
        }

        String[] searchFields = null;

        if (mList.size() > 0) {
            int i = 0;
            Iterator it = mList.iterator();
            searchFields = new String[mList.size()];

            while (it.hasNext()) {
                String s = (String) it.next();
                searchFields[i++] = s;
            }
        }

        return searchFields;
    }

    private StringBuffer replaceString(StringBuffer source, String sFind,
        String sReplace) {
        if (sFind.length() > source.length()) {
            return source;
        }

        int idx = source.toString().indexOf(sFind);
        source.delete(idx, idx + sFind.length());
        source.insert(idx, sReplace);

        return source;
    }
}
