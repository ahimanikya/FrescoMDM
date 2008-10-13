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
import com.sun.mdm.multidomain.parser.Relationship;
import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.parser.RelationshipWebManager;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.RelationshipNode;
import com.sun.mdm.multidomain.project.editor.nodes.RelationshipTypeNode;
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
    private Map mMapDomainQueryXmls = new HashMap();  // domainName, FileObject of query.xml
    private Map mMapDomainMidmXmls = new HashMap();  // domainName, FileObject of midm.xml
    private Map mMapDomainNodes = new HashMap();  // domainName, DomainNode
    private ArrayList mAlDomainNodes = new ArrayList();   // DomainNode
    private ArrayList mAlRelationshipNodes = new ArrayList();   // RelationshipNode
    private EditorMainPanel mEditorMainPanel;
    private TabRelationshipWebManager mTabRelshipWebManager = null;
    private RelationshipModel mRelationshipModel;
    
    /**
     * Creates a new instance of EditorMainApp the constructor is decleared private
     * so it cannot be instantiated
     *
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
    
    private void loadRelationships() {
        //mAlRelationshipNodes
        ArrayList <Relationship> alRelationships = mRelationshipModel.getAllRelationships();
        for (int i=0; alRelationships!=null && i<alRelationships.size(); i++) {
            Relationship relationship = (Relationship) alRelationships.get(i);
            RelationshipNode node = new RelationshipNode(relationship.getDomain1(), relationship.getDomain2());
            //create RelationshipTypeNode list
            ArrayList <RelationshipType> alRelationshipTypes = relationship.getAllRelationshipTypes();
            for (int j=0; alRelationshipTypes!=null && j<alRelationshipTypes.size(); j++) {
                RelationshipType relationshipType = (RelationshipType) alRelationshipTypes.get(j);
                RelationshipTypeNode relationshipNode = new RelationshipTypeNode(node, relationshipType);
            }
            this.mAlRelationshipNodes.add(node);
        }
    }
    
    /**
     * Look for src\Domains dir for participating domains
     * Need to verify the list against RelationshipModel.xml?
     */
    public void loadDomains() {
        // Load mMapDomainObjectXmls and mMapDomainNodes
        FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
        FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
        FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
        if (domainsFolder != null) {
            FileObject[] children = domainsFolder.getChildren();
            for (int i = 0; i < children.length; i++) {
                FileObject domain = children[i];
                if (domain.isFolder()) {
                    FileObject objectXml = domain.getFileObject(MultiDomainProjectProperties.OBJECT_XML);
                    FileObject queryXml = domain.getFileObject(MultiDomainProjectProperties.QUERY_XML);
                    FileObject midmXml = domain.getFileObject(MultiDomainProjectProperties.MIDM_XML);
                    if (objectXml != null) {
                        String domainName = domain.getName();
                        mMapDomainObjectXmls.put(domainName, objectXml);
                        mMapDomainQueryXmls.put(domainName, queryXml);
                        mMapDomainMidmXmls.put(domainName, midmXml);
                        ArrayList <RelationshipType> alRelationshipTypes = this.mRelationshipModel.getRelationshipTypesByDomain(domainName);
                        DomainNode domainNode = new DomainNode(domainName, FileUtil.toFile(domain), alRelationshipTypes);
                        mMapDomainNodes.put(domainName, domainNode);
                        mAlDomainNodes.add(domainNode);
                    }
                }
            }
        }
    }
    
    /**
     * Get DomainNode by name
     * @param name
     * @return
     */
    public DomainNode getDomainNode(String name) {
        DomainNode node = null;
        if (mMapDomainNodes != null) {
            if (name == null) {
                Object[] objs = mMapDomainNodes.values().toArray();
                if (objs.length > 0) {
                    node = (DomainNode) objs[0];
                }

            } else {
                node = (DomainNode) mMapDomainNodes.get(name);
            }
        }
        return node;
    }
    public ArrayList <DomainNode> getDomainNodes() {
        return mAlDomainNodes;
    }

    /**
     * Add domain to the model
     * @param fileDomain
     * @return
     * @throws java.io.IOException
     */
    public boolean addDomain(File fileDomain) {
        boolean added = false;
        String domainName = fileDomain.getName();
        if (!mMapDomainObjectXmls.containsKey(domainName)) {
            //Copy domain's object.xml
            try {
                FileObject objectXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.OBJECT_XML);
                FileObject queryXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.QUERY_XML);
                FileObject midmXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.MIDM_XML);
                FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
                FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
                FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                if (domainsFolder == null) {
                    domainsFolder = srcFolder.createFolder(MultiDomainProjectProperties.DOMAINS_FOLDER);
                }
                FileObject newDomainFolder = domainsFolder.createFolder(domainName);
                FileUtil.copyFile(objectXml, newDomainFolder, objectXml.getName());
                FileUtil.copyFile(queryXml, newDomainFolder, queryXml.getName());
                FileUtil.copyFile(midmXml, newDomainFolder, midmXml.getName());
                mMapDomainObjectXmls.put(domainName, objectXml);
                mMapDomainQueryXmls.put(domainName, queryXml);
                mMapDomainMidmXmls.put(domainName, midmXml);
                DomainNode domainNode = new DomainNode(domainName, FileUtil.toFile(newDomainFolder), null);
                mMapDomainNodes.put(domainName, domainNode);
                mEditorMainPanel.addDomainNodeToCanvas(domainNode, -1);
            } catch (IOException ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return added;
    }
    
    /**
     * Remove domain from the model
     * @param domainName
     * @return
     */
    public boolean removeDomain(String domainName) {
        boolean removed = false;
        if (mMapDomainObjectXmls.containsKey(domainName)) {
            try {
                mMapDomainObjectXmls.remove(domainName);
                mMapDomainQueryXmls.remove(domainName);
                mMapDomainMidmXmls.remove(domainName);
                mAlDomainNodes.remove(mMapDomainNodes.get(domainName));
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
    
    public void addRelationshp(String domain1, String domain2) {
        
    }

    /**
     * Get saved object.xml for the domain
     * @param fileDomain
     * @return
     */
    public static FileObject getSavedDomainObjectXml(File fileDomain) {
        FileObject objectXml = null;
        try {
            FileObject fobjSavedSomain = FileUtil.toFileObject(fileDomain);
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
     * @param fileDomain
     * @return
     */
    public static FileObject getDomainObjectXml(File fileDomain) {
        FileObject objectXml = null;
        try {
            objectXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.OBJECT_XML);
        } catch (IOException ex) {
            mLog.severe(ex.getMessage());
        }
        return objectXml;
    }

    /**
     * 
     * @param domainName
     * @return
     */
    public FileObject getDomainQueryXml(String domainName) {
        FileObject xml = null;
        if (!mMapDomainQueryXmls.containsKey(domainName)) {
            xml = (FileObject) mMapDomainQueryXmls.get(domainName);
        }
        return xml;
    }

    /**
     * 
     * @param domainName
     * @return
     */
    public FileObject getDomainMidmXml(String domainName) {
        FileObject xml = null;
        if (!mMapDomainMidmXmls.containsKey(domainName)) {
            xml = (FileObject) mMapDomainMidmXmls.get(domainName);
        }
        return xml;
    }

    /**
     * 
     * @param folder
     * @param name
     * @return
     * @throws java.io.IOException
     */
    public static FileObject getDomainConfigurationFile(File fileDomain, String fileName) throws IOException {
        String folder = fileDomain.getAbsolutePath() + File.separator + MultiDomainProjectProperties.SRC_FOLDER + File.separator + MultiDomainProjectProperties.CONFIGURATION_FOLDER;
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

            mRelationshipModel = getRelationshipModel(true);
            loadRelationships();

            // Load mMapDomainObjectXmls
            loadDomains();
            // Let TopObjectComponent/EditorMainPanel to do DomainNodes loading
            //loadRelationshipTypeNodes();

            mEditorMainPanel = new EditorMainPanel(this, mMultiDomainApplication);
            mEditorMainPanel.loadDomainNodesToCanvas();
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

    public RelationshipModel getRelationshipModel(boolean refresh) {
        return mMultiDomainApplication.getRelationshipModel(refresh);
    }

    public RelationshipWebManager getRelationshipWebManager(String xml) {
        RelationshipWebManager relationshipWebManager = null;
        try {
            InputStream objectdef = new ByteArrayInputStream(xml.getBytes());
            InputSource source = new InputSource(objectdef);
            relationshipWebManager = com.sun.mdm.multidomain.parser.Utils.parseRelationshipWebManager(source);
        } catch (Exception ex) {
            displayError(ex);
        }
        return relationshipWebManager;
    }
    
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
                mMultiDomainApplication.saveRelationshipWebManagerXml(relationshipWebManagerXml);
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
