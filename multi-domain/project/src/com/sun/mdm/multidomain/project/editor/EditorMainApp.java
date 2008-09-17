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
package com.sun.mdm.multidomain.project.editor;

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

import com.sun.mdm.multidomain.parser.RelationshipModel;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

/**
 * Main application class for Master Index Configuration Editor
 *
 */
public class EditorMainApp {

    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            EditorMainApp.class.getName()
        
        );
   
    /**
     * this is a singleton
     */
    private static EditorMainApp mInstance;
    private static ObjectTopComponent mObjectTopComponent = null;
    private static Map mMapInstances = new HashMap();  // path, instance
    private MultiDomainApplication mMultiDomainApplication;
    private EditorMainPanel mEditorMainPanel;
    //private PropertiesDeploymentPanel mPropertiesDeploymentPanel;
    //private TabDeploymentPanel mTabDeploymentPanel = null;
    private ArrayList candidateFields;
    private String validationMsg = "";
    private boolean bCheckedOut = true;
    
    /**
     * Creates a new instance of EditorMainApp the constructor is decleared private
     * so it cannot be instantiated
     *
     * @param name DOCUMENT ME!
     */
    private EditorMainApp() {
    }

    private EditorMainApp(String instanceName) {
        mMapInstances.put(instanceName, this);
    }
    
     /**
     * get the singalton instance per name
     *
     * @param name Master Index Configuration Editor name
     * @return EditorMainApp instance
     */
    public static EditorMainApp createInstance(String instanceName) {
        mInstance = (EditorMainApp) mMapInstances.get(instanceName);
        if (mInstance == null) {
            mInstance = new EditorMainApp(instanceName);
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
     * @return EditorMainApp instance
     */
    public static EditorMainApp getInstance(String instanceName) {
        mInstance = (EditorMainApp) mMapInstances.get(instanceName);
        return mInstance;
    }
    /** Make TopComponent on focus
     * 
     */
    public static void requestFocus() {
        mObjectTopComponent.requestActive();
    }

    void checkOutAll() {
        bCheckedOut = mMultiDomainApplication.getAllConfigurableFiles();
        if (bCheckedOut == false) {
            String msg = mMultiDomainApplication.getMsgCheckedOut();
            msg += "\n\n" + NbBundle.getMessage(EditorMainApp.class, "MSG_Read_Only");
            NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
            DialogDisplayer.getDefault().notify(desc);
        }
    }
    /**
     * Stating point of Master Index Configuration Editor from editable Master Index Instance
     *
     * @param MultiDomainApplication
     */
    public void startEditorApp(MultiDomainApplication application) {
        mMultiDomainApplication = application;
        try {
            checkOutAll();
            String path = mMultiDomainApplication.getProjectDirectory().getName();

            mObjectTopComponent = new ObjectTopComponent();
            mObjectTopComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mMultiDomainApplication.setObjectTopComponent(mObjectTopComponent);
            if (mObjectTopComponent.startTopComponent(getInstance(path), path, application)) {
                mObjectTopComponent.open();
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        } finally {
            mObjectTopComponent.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * @return EditorMainPanel
     */
    public EditorMainPanel newEditorMainPanel() {
        if (mEditorMainPanel == null) {
            mMultiDomainApplication.loadAll();
            mEditorMainPanel = new EditorMainPanel(this, mMultiDomainApplication);
        }
        return mEditorMainPanel;
    }

    /**
     * @return EditorMainPanel
     */
    public EditorMainPanel getEditorMainPanel() {
        return mEditorMainPanel;
    }
    
    
    /**
     *@return xml string for RelationshipModel.xml
     */
    public String getRelationshipModelXmlString() throws Exception {
        String data = null;
        return data;
    }
    
    public void enableSaveAction(boolean flag) {
        this.mMultiDomainApplication.setModified(flag);
        this.mEditorMainPanel.enableSaveButton(bCheckedOut && flag);
    }
    
    public RelationshipModel getRelationshipModel(String objectXml) {
        RelationshipModel relationshipModel = null;
        try {
            InputStream objectdef = new ByteArrayInputStream(objectXml.getBytes());
            InputSource source = new InputSource(objectdef);
            relationshipModel = com.sun.mdm.multidomain.parser.Utils.parseRelationshipModel(source);
        } catch (Exception ex) {
            displayError(ex);
        }
        return relationshipModel;
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
            // RelationshipModel.xml
            String relationshipModelXml = getRelationshipModelXmlString();
            if (relationshipModelXml != null) {
                mMultiDomainApplication.saveRelationshipModelXml(relationshipModelXml, comment, checkIn);
            } else {
                String msg = NbBundle.getMessage(EditorMainApp.class, "MSG_Save_Failed");

                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
            mMultiDomainApplication.resetModified(false);
            enableSaveAction(false);
        } catch (Exception ex) {
            displayError(ex);
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
