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
package com.sun.mdm.index.project.ui.applicationeditor;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.Cursor;

import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.sun.mdm.index.parser.EIndexObject;
import com.sun.mdm.index.parser.UpdateType;
import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.index.project.EviewProjectProperties;

import com.sun.mdm.index.project.ui.applicationeditor.writers.ObjectWriter;
import com.sun.mdm.index.project.ui.applicationeditor.writers.EDMWriter;

/**
 * Main application class for Master Index Configuration Editor
 *
 */
public class EviewEditorMainApp {

    /** The logger. */
    private static final com.sun.mdm.index.util.Logger mLog = com.sun.mdm.index.util.Logger.getLogger(
            EviewEditorMainApp.class.getName()
        );
   
    /**
     * app context: mEviewModeName
     */
    private static String mEviewModeName;

    /**
     * this is a singleton
     */
    private static EviewEditorMainApp mInstance;
    private static ObjectTopComponent mObjectTopComponent = null;
    private static Map mMapInstances = new HashMap();  // path, instance
    private EviewApplication mEviewApplication;
    private EviewEditorMainPanel mEviewEditorMainPanel;
    private PropertiesDeploymentPanel mPropertiesDeploymentPanel;
    private TabDeploymentPanel mTabDeploymentPanel = null;
    private TabMatchConfigPanel mTabMatchConfigPanel = null;
    private TabBlockingPanel mTabBlockingPanel = null;
    private TabStandardizationPanel mTabStandardizationPanel = null;
    private TabNormalizationPanel mTabNormalizationPanel = null;
    private TabPhoneticizedFieldsPanel mTabPhoneticizedFieldsPanel = null;
    private EntityNode mRootNode;
    private ArrayList candidateFields;
    private String validationMsg = "";
    private boolean bCheckedOut = true;
    private boolean bMidm = true;
    
    /**
     * Creates a new instance of EviewEditorMainApp the constructor is decleared private
     * so it cannot be instantiated
     *
     * @param name DOCUMENT ME!
     */
    private EviewEditorMainApp() {
    }

    private EviewEditorMainApp(String instanceName) {
        mMapInstances.put(instanceName, this);
    }
    
     /**
     * get the singalton instance per name
     *
     * @param name Master Index Configuration Editor name
     * @return EviewEditorMainApp instance
     */
    public static EviewEditorMainApp createInstance(String instanceName) {
        mInstance = (EviewEditorMainApp) mMapInstances.get(instanceName);
        if (mInstance == null) {
            mInstance = new EviewEditorMainApp(instanceName);
        }
        return mInstance;
    }

    /**
     * remove instance from the Map
     * @param instanceName
     */
    public static void removeInstance(String instanceName) {
        mMapInstances.remove(instanceName);
    }
    
     /**
     * get the singalton instance per name
     *
     * @param name Master Index Configuration Editor name
     * @return EviewEditorMainApp instance
     */
    public static EviewEditorMainApp getInstance(String instanceName) {
        mInstance = (EviewEditorMainApp) mMapInstances.get(instanceName);
        return mInstance;
    }
    /** Make TopComponent on focus
     * 
     */
    public static void requestFocus() {
        mObjectTopComponent.requestActive();
    }

    void checkOutAll() {
        bCheckedOut = mEviewApplication.getAllConfigurableFiles();
        if (bCheckedOut == false) {
            String msg = mEviewApplication.getMsgCheckedOut();
            msg += "\n\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Read_Only");
            NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
            DialogDisplayer.getDefault().notify(desc);
        }
    }
    /**
     * Stating point of Master Index Configuration Editor from editable Master Index Instance
     *
     * @param EviewApplication
     */
    public void startEviewEditorApp(EviewApplication application) {
        mEviewApplication = application;
        try {
            checkOutAll();
            String path = mEviewApplication.getProjectDirectory().getName();

            mObjectTopComponent = new ObjectTopComponent();
            mObjectTopComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mEviewApplication.setObjectTopComponent(mObjectTopComponent);
            if (mObjectTopComponent.startEviewTopComponent(getInstance(path), path, application)) {
                mObjectTopComponent.open();
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        } finally {
            mObjectTopComponent.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * @return EviewEditorMainPanel
     */
    public EviewEditorMainPanel newEviewEditorMainPanel() {
        if (mEviewEditorMainPanel == null) {
            mEviewApplication.loadAll();
            mEviewEditorMainPanel = new EviewEditorMainPanel(this, mEviewApplication);
            mRootNode = mEviewEditorMainPanel.getEntityTree().getRootNode();
        }
        return mEviewEditorMainPanel;
    }

    /**
     * @return EviewEditorMainPanel
     */
    public EviewEditorMainPanel getEviewEditorMainPanel() {
        return mEviewEditorMainPanel;
    }
    
    /**
     * @return PropertiesDeploymentPanel
     */
    public PropertiesDeploymentPanel getPropertiesDeploymentPanel(EIndexObject eIndexObject, UpdateType updateType) {
        if (mPropertiesDeploymentPanel == null) {
            mPropertiesDeploymentPanel = new PropertiesDeploymentPanel(eIndexObject, updateType);
        }
        return mPropertiesDeploymentPanel;
    }
    
    public PropertiesDeploymentPanel getPropertiesDeploymentPanel() {
        return mPropertiesDeploymentPanel;
    }
    
    public TabDeploymentPanel getTabDeploymentPanel() {
        if (mTabDeploymentPanel == null) {
            mTabDeploymentPanel = new TabDeploymentPanel(this, mEviewApplication.getMasterType(false));
        }
        return mTabDeploymentPanel;
    }
       
    /**
     * @return TabMatchConfigPanel
     */
    public TabMatchConfigPanel getTabMatchConfigPanel() {
        if (mTabMatchConfigPanel == null) {
            mTabMatchConfigPanel = new TabMatchConfigPanel(this, mEviewApplication);
        }
        return mTabMatchConfigPanel;
    }
    
    /**
     * @return TabBlockingPanel
     */
    public TabBlockingPanel getTabBlockingPanel() {
        if (mTabBlockingPanel == null) {
            try {
                mTabBlockingPanel = new TabBlockingPanel(this, mEviewApplication, mEviewApplication.getQueryType(false));            
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mTabBlockingPanel;
    }

    /**
     * @return TabStandardizationPanel
     */
    public TabStandardizationPanel getStandardizationPanel() {
        if (mTabStandardizationPanel == null) {
            try {
                mTabStandardizationPanel = new TabStandardizationPanel(this, mEviewApplication);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mTabStandardizationPanel;
    }
    
    /**
     * @return TabNormalizationPanel
     */
    public TabNormalizationPanel getNormalizationPanel() {
        if (mTabNormalizationPanel == null) {
            try {
                mTabNormalizationPanel = new TabNormalizationPanel(this, mEviewApplication);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mTabNormalizationPanel;
    }

    /**
     * @return TabPhoneticizedFieldsPanel
     */
    public TabPhoneticizedFieldsPanel getPhoneticizedFieldsPanel() {
        if (mTabPhoneticizedFieldsPanel == null) {
            try {
                mTabPhoneticizedFieldsPanel = new TabPhoneticizedFieldsPanel(this, mEviewApplication);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mTabPhoneticizedFieldsPanel;
    }
    
    /**
     *@return xml string for object.xml
     */
    public String getObjectXmlString() {
        String data = null;
        try {
            EntityNode primaryNode = (EntityNode) mRootNode.getChildAt(0);
            String viewName = this.mEviewApplication.getApplicationName();
            String matchEngine = this.getPropertiesDeploymentPanel().getMatchEngine();
            String database = this.getPropertiesDeploymentPanel().getDatabase();
            String dateFormat = this.getPropertiesDeploymentPanel().getDateFormat();
            String systemList = this.getPropertiesDeploymentPanel().getSoureSystemsList();
            String duplicateThreshold = this.getTabMatchConfigPanel().getDuplicateThreshold();
            String matchThreshold = this.getTabMatchConfigPanel().getMatchThreshold();
            ObjectWriter objectWriter = new ObjectWriter();
            data = objectWriter.generate(primaryNode, viewName, matchEngine, database, dateFormat, systemList, duplicateThreshold, matchThreshold);
            candidateFields = objectWriter.getCandidateFields();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return data;
    }
    
    
    /**
     *@return xml string for edm.xml
     */
    public String getEDMXmlString() {
        String data = null;
        try {
            EntityNode primaryNode = (EntityNode) mRootNode.getChildAt(0);
            String viewName = this.mEviewApplication.getApplicationName();
            EDMWriter edmWriter = new EDMWriter();
            FileObject cf = mEviewApplication.getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.MIDM_XML, false);
            if (cf == null) {
                bMidm = false;
                cf = mEviewApplication.getConfigurationFile(EviewProjectProperties.CONFIGURATION_FOLDER, EviewProjectProperties.EDM_XML, false);
                data = edmWriter.generateEDM(primaryNode, viewName, mEviewApplication.getEDMType(false));
            } else {
                bMidm = true;
                data = edmWriter.generateMIDM(primaryNode, viewName, mEviewApplication.getEDMType(false));
            }

        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }

        return data;
    }
    
    /**
     *@return xml string for mefa.xml
     */
    public String getMefaXmlString() {
        try {
            return mEviewApplication.getMatchFieldDef(false).getXMLString();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return null;

    }
    
    /**
     *@return xml string for master.xml
     */
    public String getMasterXmlString() {
        try {
            return mEviewApplication.getMasterType(false).getXMLString();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return null;
    }
    
    /**
     *@return xml string for query.xml
     */
    public String getQueryXmlString() {
        try {
            return mEviewApplication.getQueryType(false).getXMLString();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return null;
    }
    
    /**
     *@return xml string for update.xml
     */
    public String getUpdateXmlString() {
        try {
            return mEviewApplication.getUpdateType(false).getXMLString(candidateFields);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return null;
    }
    
    /**
     *@return xml string for security.xml
     */    
    //public String getSecurityXmlString() {
    //    return mEviewApplication.getSecurityType(false).getXMLString();
    //}
    
    /**
     *@return xml string for validation.xml
     */    
    //public String getValidationXmlString() {
    //    return mEviewApplication.getValidationType(false).getXMLString();
    //}
    
    public void enableSaveAction(boolean flag) {
        this.mEviewApplication.setModified(flag);
        this.mEviewEditorMainPanel.enableSaveButton(bCheckedOut && flag);
    }
    
    public EIndexObject getEIndexObject(String objectXml) {
        EIndexObject eIndexObject = null;
        try {
            InputStream objectdef = new ByteArrayInputStream(objectXml.getBytes());
            InputSource source = new InputSource(objectdef);
            eIndexObject = com.sun.mdm.index.parser.Utils.parseEIndexObject(source);
        } catch (Exception ex) {
            this.displayError(ex);
        }
        return eIndexObject;
    }
    
    /*
    public boolean validateXML(EIndexObject eIndexObject, String xmlFileName, String xmlString) {
        boolean bValid = true;

        try {              
            DocumentBuilderFactory builderFact = DocumentBuilderFactory.newInstance();
                
            builderFact.setNamespaceAware(true);
            builderFact.setAttribute("http://apache.org/xml/features/validation/schema", Boolean.TRUE);
            builderFact.setValidating(false);
                
            DocumentBuilder builder = builderFact.newDocumentBuilder();
               
            //builder.setEntityResolver(new RepositoryEntityResolver());
            builder.setErrorHandler(new Handler());
                
            ObjectFactory.setObject(eIndexObject);
            ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes());
            InputSource input = new InputSource(bais);
            // Static, using xsd
            Document doc = builder.parse(input);
                
            // Dynamic, using epath
            EpathParser ret = new EpathParser();
            try {
                ret.parse(doc);
            } catch (ValidationException ex) {
                validationMsg += xmlFileName + "\n" + "\n \"" + ex.getMessage() + "\"";
                return false;
            }

            java.util.ArrayList al = ret.getEpath();
            Iterator i = al.iterator();

            validationMsg += "\n\"" + xmlFileName + "\"" + " - Invalid epath:";
            while (i.hasNext()) {
                String epath = (String) i.next();
                boolean b = ObjectFactory.isEPathValid(epath);
                if (!b) {
                    bValid = false;
                    validationMsg += "\n\"" + epath + "\"";
                }
            }
            if (bValid) {
                validationMsg += "\n\"" + xmlFileName + "\"" + " is valid";
                return true;
            } else {
                return false;
            }
        } catch (Exception rex) {
            // Static against XSD
            validationMsg += "\n\"" + xmlFileName + "\"" + rex.getMessage();
            mLog.severe(rex.getMessage());
            return false;
        }
    }

    
    public boolean validate(EIndexObject eIndexObject) {
        boolean bValid = true;
        validationMsg = "";

        bValid = bValid && validateXML(eIndexObject, "EDM", getEDMXmlString());
        bValid = bValid && validateXML(eIndexObject, "Mefa", getMefaXmlString());
        bValid = bValid && validateXML(eIndexObject, "Query", getQueryXmlString());
        bValid = bValid && validateXML(eIndexObject, "Master", getMasterXmlString());
        bValid = bValid && validateXML(eIndexObject, "Update", getUpdateXmlString());
        return bValid;
    }
    */
    
    /* Running editor in read only mode in not bCheckedOut
     *
     *@return bCheckedOut
     */
    public boolean isCheckedOut() {
        // Always true
        // Use cvs to do check in/check out
        return bCheckedOut;
    }
    
    /* Save all edited files
     * object.xml, edm.xml, mefa.xml, query.xml, master.xml, update.xml
     * and matchConfigFile.cfg
     *
     *@param bClosing == true when closing the editor, user will be promted fo checking in
     *                == false when save icon is pressed and will save changes to work space only
     */
    public void save(boolean bClosing) {
        boolean checkIn = true;
        String comment = "";
        try {
            // object.xml
            String objectXml = getObjectXmlString();
            // edm.xml
            String edmXml = getEDMXmlString();
            // mefa.xml
            String mefaXml = getMefaXmlString();
            // query.xml
            String queryXml = getQueryXmlString();
            // master.xml
            String masterXml = getMasterXmlString();
            // update.xml
            String updateXml = getUpdateXmlString();
            
            String matchConfigData = getTabMatchConfigPanel().getMatchConfigFileString();
            /*
            EIndexObject eIndexObject = getEIndexObject(objectXml);
            boolean bValid = validate(eIndexObject);
            if (!bValid) {
                String msg = NbBundle.getMessage(EntityNode.class, "MSG_Validate_Failed") + validationMsg;
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
            */
            if (objectXml != null && edmXml != null &&
                mefaXml != null && queryXml != null &&
                masterXml != null && updateXml != null &&
                matchConfigData != null) {
                mEviewApplication.saveObjectXml(objectXml, comment, checkIn);
                mEviewApplication.saveEDMXml(edmXml, comment, checkIn);
                mEviewApplication.saveMefaConfigurationFile(mefaXml, comment, checkIn);
                mEviewApplication.saveQueryConfigurationFile(queryXml, comment, checkIn);
                mEviewApplication.saveMasterConfigurationFile(masterXml, comment, checkIn);
                mEviewApplication.saveUpdateXml(updateXml, comment, checkIn);
                
                mEviewApplication.saveMatchConfigFile(matchConfigData, comment, checkIn);
                // security.xml
                //String securityXml = getSecurityXmlString();
                //mEviewApplication.saveSecurityConfigurationFile(securityXml, comment, checkIn);

                // EntityTree and EntityNode -> validation.xml
                //String validationXml = getValidationXmlString();
                //mEviewApplication.saveValidationConfigurationFile(validationXml, comment, checkIn);
            } else {
                String msg = NbBundle.getMessage(EntityNode.class, "MSG_Save_Failed") +
                        ((objectXml == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_ObjectXml") : "") +
                        ((edmXml == null && !bMidm) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_EDMXml") : "") +
                        ((edmXml == null && bMidm) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_MIDMXml") : "") +
                        ((mefaXml == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_MefaXml") : "") +
                        ((queryXml == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_QueryXml") : "") +
                        ((masterXml == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_MasterXml") : "") +
                        ((updateXml == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_UpdateXml") : "") +
                        ((matchConfigData == null) ? "\n" + NbBundle.getMessage(EviewEditorMainApp.class, "MSG_Empty_MatchConfigFile") : "");

                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
            mEviewApplication.resetModified(false);
            enableSaveAction(false);
        } catch (Exception ex) {
            this.displayError(ex);
        }

    }
    
    /**
     * display error
     * @param Exception to display
     * @param String userFriendlyMsg
     */
    public static void displayError(Exception exception, String userFriendlyMsg) {
        if (null != userFriendlyMsg && userFriendlyMsg.length() > 0)    {
            Throwable throwable =
                    ErrorManager.getDefault().annotate(exception, userFriendlyMsg);
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, throwable);
        } else  {
            ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, exception);
        }
    }

    /**
     * display error
     * @param Exception to display
     */
    public static void displayError(Exception exception) {
        displayError(exception, null);
    }

    /**
     * display message
     * @param String userFriendlyMsg to display
     */
    public static void displayMessage(String userFriendlyMsg) {
        if (null != userFriendlyMsg && userFriendlyMsg.length() > 0)    {
            ErrorManager.getDefault().notify(
                          ErrorManager.WARNING, new Exception(userFriendlyMsg));
        }
    }
    
    /**
     *
     */
    public class Handler implements ErrorHandler {
       
        /** Creates a new instance of DefaultHandler */
        public Handler() {
        }
        
        public void warning(SAXParseException ex) {
            mLog.warn(ex.getMessage());
        }
        
        /**
         * Report maximally getMaxErrorCount() errors then stop the parser.
         */
        public void error(SAXParseException ex) throws SAXException {
            throw ex;
        }
        
        public void fatalError(SAXParseException exception) throws SAXException {
            throw exception;
        }
        
    }

}
