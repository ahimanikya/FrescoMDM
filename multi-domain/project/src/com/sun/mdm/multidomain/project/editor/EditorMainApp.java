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
import org.openide.filesystems.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;

import com.sun.mdm.multidomain.parser.RelationshipModel;
import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.parser.RelationshipWebManager;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import org.openide.filesystems.FileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.xml.sax.InputSource;

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
    private MultiDomainApplication mMultiDomainApplication;
    //private EditorMainApp mInstance;
    private ObjectTopComponent mObjectTopComponent = null;
    private static Map mMapInstances = new HashMap();  // projName, instance
    private Map mMapDomainObjectXmls = new HashMap();  // domainName, FileObject of object.xml
    private Map mMapDomainNodes = new HashMap();  // domainName, DomainNode
    private ArrayList mAlRelationshipTypeNodes = new ArrayList();
    private EditorMainPanel mEditorMainPanel;
    private TabRelationshipWebManager mTabRelshipWebManager = null;
    private RelationshipModel mRelationshipModel;
    
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
        EditorMainApp instance = (EditorMainApp) mMapInstances.get(instanceName);
        if (instance == null) {
            instance = new EditorMainApp(instanceName);
        }
        return instance;
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
        EditorMainApp instance = (EditorMainApp) mMapInstances.get(instanceName);
        return instance;
    }
    
    /**
     * Look for src\Domains dir for participating domains
     * Need to verify the list against RelationshipModel.xml?
     */
    private void loadDomains() {
        // Load mMapDomainObjectXmls and mMapDomainNodes
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
                        String domainName = domain.getName();
                        mMapDomainObjectXmls.put(domainName, objectXml);
                        DomainNode domainNode = new DomainNode(domainName, FileUtil.toFile(domain));
                        mMapDomainNodes.put(domainName, domainNode);
                        //ToDo: populate relationship types for domainNode
                        ArrayList <RelationshipType> alRelationshipTypes = this.mRelationshipModel.getRelationshipTypesByDomain(domainName);
                        domainNode.loadRelationshipTypes(alRelationshipTypes);
                    }
                }
            }
        }
    }
    
    public DomainNode getDomainNode(String name) {
        DomainNode node = null;
        if (mMapDomainNodes != null) {
            if (name == null) {
                Object[] objs = mMapDomainNodes.values().toArray();
                node = (DomainNode) objs[0];

            } else {
                node = (DomainNode) mMapDomainNodes.get(name);
            }
        }
        return node;
    }
    /**
     * 
     * @param selectedDomain
     * @return
     * @throws java.io.IOException
     */
    public boolean addDomain(File selectedDomain) {
        boolean added = false;
        String domainName = selectedDomain.getName();
        if (!mMapDomainObjectXmls.containsKey(domainName)) {
            //Copy domain's object.xml
            try {
                FileObject objectXml = getDomainConfigurationFile(selectedDomain, MultiDomainProjectProperties.OBJECT_XML);
                FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
                FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
                FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                if (domainsFolder == null) {
                    domainsFolder = srcFolder.createFolder(MultiDomainProjectProperties.DOMAINS_FOLDER);
                }
                FileObject newDomainFolder = domainsFolder.createFolder(domainName);
                FileUtil.copyFile(objectXml, newDomainFolder, objectXml.getName());
                mMapDomainObjectXmls.put(domainName, objectXml);
                DomainNode domainNode = new DomainNode(domainName, FileUtil.toFile(newDomainFolder));
                mMapDomainNodes.put(domainName, domainNode);
                mEditorMainPanel.addDomainNode(domainNode);
            } catch (IOException ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return added;
    }
    
    public boolean removeDomain(String domainName) {
        boolean removed = false;
        if (mMapDomainObjectXmls.containsKey(domainName)) {
            try {
                mMapDomainObjectXmls.remove(domainName);
                mMapDomainNodes.remove(domainName);
                FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
                FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
                FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                domainsFolder.delete();
                removed = true;
            } catch (IOException ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return removed;
    }

    /**
     * 
     * @param selectedDomain
     * @return
     */
    public static FileObject getSavedDomainObjectXml(File selectedDomain) {
        FileObject objectXml = null;
        try {
            FileObject fobjSavedSomain = FileUtil.toFileObject(selectedDomain);
            objectXml = fobjSavedSomain.getFileObject(MultiDomainProjectProperties.OBJECT_XML);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return objectXml;
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
     * @param selectedDomain
     * @return
     */
    public static FileObject getDomainObjectXml(File selectedDomain) {
        FileObject objectXml = null;
        try {
            objectXml = getDomainConfigurationFile(selectedDomain, MultiDomainProjectProperties.OBJECT_XML);
        } catch (IOException ex) {
            mLog.severe(ex.getMessage());
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
    public static FileObject getDomainConfigurationFile(File selectedDomain, String fileName) throws IOException {
        String folder = selectedDomain.getAbsolutePath() + File.separator + MultiDomainProjectProperties.SRC_FOLDER + File.separator + MultiDomainProjectProperties.CONFIGURATION_FOLDER;
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
    public void requestFocus() {
        mObjectTopComponent.requestActive();
    }

    /**
     * Stating point of Multi-Domain MDM Configuration Editor from editable Multi-Domain MDM Instance
     *
     * @param MultiDomainApplication
     */
    public void startEditorApp(MultiDomainApplication application) {
        mMultiDomainApplication = application;
        String projName = mMultiDomainApplication.getProjectDirectory().getName();
        try {
            mMultiDomainApplication.loadAll();

            getRelationshipModel();
            // Load mMapDomainObjectXmls
            loadDomains();
            // Let TopObjectComponent/EditorMainPanel to do DomainNodes loading
            //loadRelationshipTypeNodes();


            mEditorMainPanel = new EditorMainPanel(this, mMultiDomainApplication);
            mObjectTopComponent = new ObjectTopComponent();
            mObjectTopComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mMultiDomainApplication.setObjectTopComponent(mObjectTopComponent);
            if (mObjectTopComponent.startTopComponent(getInstance(projName), projName, application, mEditorMainPanel)) {
                mObjectTopComponent.open();
            }
            mObjectTopComponent.setCursor(Cursor.getDefaultCursor());
        } catch (Exception ex) {
            mMapInstances.remove(projName);
            mLog.severe(ex.getMessage());
        }
    }
    
    /**
     *@return xml string for RelationshipModel.xml
     */
    public String getRelationshipModelXmlString() throws Exception {
        String data = null;
        return data;
    }

    /**
     * Will get RelationshipWebManager object into xml format
     * @return xmlStr - xml format for RelationshipWebManager
     * @throws java.lang.Exception
     */
    public String getRelationshipWebManagerXmlString() throws Exception {
        String xmlStr = null;
        xmlStr = mMultiDomainApplication.getRelationshipWebMAnager(false).writeToString();
        return xmlStr;
    }
    
    public void enableSaveAction(boolean flag) {
        this.mMultiDomainApplication.setModified(flag);
        this.mEditorMainPanel.enableSaveButton(flag);
    }
    

    public RelationshipModel getRelationshipModel() {
        mRelationshipModel = mMultiDomainApplication.getRelationshipModel(true);
        return mRelationshipModel;
    }
    

    public RelationshipWebManager getRelationshipWebManager(String objectXml) {
        RelationshipWebManager relationshipWebManager = null;
        try {
            InputStream objectdef = new ByteArrayInputStream(objectXml.getBytes());
            InputSource source = new InputSource(objectdef);
            relationshipWebManager = com.sun.mdm.multidomain.parser.Utils.parseRelationshipWebManager(source);
        } catch (Exception ex) {
            displayError(ex);
        }
        return relationshipWebManager;
    }
    

    /* Save all edited files
     * RelationshipModel.xml and RelationshipWebManager.xml
     *
     *@param bClosing == true when closing the editor, user will be promted fo checking in
     *                == false when save icon is pressed and will save changes to work space only
     */
    public void save(boolean bClosing) {
        try {
            // RelationshipModel.xml
            String relationshipModelXml = getRelationshipModelXmlString();
            if (relationshipModelXml != null) {
                mMultiDomainApplication.saveRelationshipModelXml(relationshipModelXml);
            } else {
                String msg = NbBundle.getMessage(EditorMainApp.class, "MSG_Save_Failed");
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
            
            String relationshipWebManagerXml = getRelationshipWebManagerXmlString();
            if (relationshipWebManagerXml != null) {
                mMultiDomainApplication.saveRelationshipWebManagerXml(relationshipWebManagerXml, "", true);
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
