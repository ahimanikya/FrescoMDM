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

import java.util.Map;
import java.util.HashMap;
import java.awt.Cursor;

import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import com.sun.mdm.multidomain.parser.RelationshipModel;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;

/**
 * Main application class for Multi-Domain MDM Configuration Editor
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
    private static Map mMapDomainObjectXmls = new HashMap();  // domainName, FileObject of object.xml
    private MultiDomainApplication mMultiDomainApplication;
    private EditorMainPanel mEditorMainPanel;
    
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
     * @param name Multi-Domain MDM Configuration Editor name
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
     * @param name Multi-Domain MDM Configuration Editor name
     * @return EditorMainApp instance
     */
    public static EditorMainApp getInstance(String instanceName) {
        mInstance = (EditorMainApp) mMapInstances.get(instanceName);
        return mInstance;
    }
    
    private void loadDomainMaps() {
        // Load mMapDomainObjectXmls
        FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
        FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
        FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
        if (domainsFolder != null) {
            FileObject[] children = domainsFolder.getChildren();
            for (int j = 0; j < children.length; j++) {
                FileObject domain = children[j];
                if (domain.isFolder()) {
                    FileObject objectXml = domain.getFileObject(MultiDomainProjectProperties.OBJECT_XML);
                    if (objectXml != null) {
                        mMapDomainObjectXmls.put(domain.getName(), objectXml);
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param selectedDomain
     * @return
     * @throws java.io.IOException
     */
    public boolean addDomain(File selectedDomain) throws IOException {
        boolean added = false;
        String domainName = selectedDomain.getName();
        if (!mMapDomainObjectXmls.containsKey(domainName)) {
            //Copy domain's object.xml
            String path = selectedDomain.getAbsolutePath() + File.separator + MultiDomainProjectProperties.SRC_FOLDER + File.separator + MultiDomainProjectProperties.CONFIGURATION_FOLDER;
            FileObject objectXml = getDomainConfigurationFile(path, MultiDomainProjectProperties.OBJECT_XML);
            FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
            FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
            FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
            FileObject newDomainFolder = domainsFolder.createFolder(domainName);
            FileUtil.copyFile(objectXml, newDomainFolder, objectXml.getName());
            mMapDomainObjectXmls.put(domainName, objectXml);
            added = true;
        }
        return added;
    }
    
    public boolean removeDomain(String domainName) throws IOException {
        boolean removed = false;
        if (mMapDomainObjectXmls.containsKey(domainName)) {
            mMapDomainObjectXmls.remove(domainName);
            FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
            FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
            FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
            domainsFolder.delete();
            removed = true;
        }
        return removed;
    }
    
    /**
     * 
     * @param domainName
     * @return
     */
    public FileObject getDomainObjectXml(String domainName) {
        FileObject objectXml = null;
        if (!mMapDomainObjectXmls.containsKey(domainName)) {
            objectXml = (FileObject) mMapDomainObjectXmls.get(domainName);
        }
        return objectXml;
    }

    /**
     * 
     * @param folder
     * @param name
     * @return
     * @throws java.io.IOException
     */
    public FileObject getDomainConfigurationFile(String folder, String fileName) throws IOException {
        FileObject fileObj = null;
        if (folder != null) {
            FileObject dir = FileUtil.toFileObject(new File(folder));
            if (dir == null || !dir.isFolder()) {
                return null;
            }
            fileObj = dir.getFileObject(fileName);
        }
        return fileObj;
    }

    /** Make TopComponent on focus
     * 
     */
    public static void requestFocus() {
        mObjectTopComponent.requestActive();
    }

    /**
     * Stating point of Multi-Domain MDM Configuration Editor from editable Multi-Domain MDM Instance
     *
     * @param MultiDomainApplication
     */
    public void startEditorApp(MultiDomainApplication application) {
        mMultiDomainApplication = application;
        try {
            mMultiDomainApplication.loadAll();
            mEditorMainPanel = new EditorMainPanel(this, mMultiDomainApplication);

            // Load mMapDomainObjectXmls
            loadDomainMaps();
            // Let TopObjectComponent to do DomainNodes loading

            String path = mMultiDomainApplication.getProjectDirectory().getName();

            mObjectTopComponent = new ObjectTopComponent();
            mObjectTopComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mMultiDomainApplication.setObjectTopComponent(mObjectTopComponent);
            if (mObjectTopComponent.startTopComponent(getInstance(path), path, application, mEditorMainPanel)) {
                mObjectTopComponent.open();
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        } finally {
            mObjectTopComponent.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     *@return xml string for RelationshipModel.xml
     */
    public String getRelationshipModelXmlString() throws Exception {
        String data = null;
        return data;
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
    
    /**
     * Enable save button
     * @param flag
     */
    public void enableSaveAction(boolean flag) {
        this.mMultiDomainApplication.setModified(flag);
        this.mEditorMainPanel.enableSaveButton(flag);
    }

    /* Save all edited files
     * RelationshipModel.xml and RelationshipWebManager.xml
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
