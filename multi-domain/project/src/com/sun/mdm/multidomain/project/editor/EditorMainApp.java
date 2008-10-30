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

import com.sun.mdm.multidomain.parser.Attribute;
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

import com.sun.mdm.multidomain.parser.MultiDomainModel;
import com.sun.mdm.multidomain.parser.MultiDomainWebManager;
import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.LinkBaseNode;
import org.openide.filesystems.FileObject;

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
    private static EditorMainApp mInstance;
    private ObjectTopComponent mObjectTopComponent = null;
    private static Map mMapInstances = new HashMap();  // projName, instance
    private Map mMapDomainObjectXmls = new HashMap();  // domainName, FileObject of object.xml
    private Map mMapDomainQueryXmls = new HashMap();  // domainName, FileObject of query.xml
    private Map mMapDomainMidmXmls = new HashMap();  // domainName, FileObject of midm.xml
    private Map mMapDomainNodes = new HashMap();  // domainName, DomainNode
    private ArrayList mAlDomainNodes = new ArrayList();   // DomainNode
    private ArrayList <LinkBaseNode> mAlLinkNodes = new ArrayList <LinkBaseNode>();   // RelationshipNode
    private EditorMainPanel mEditorMainPanel;
    private TabRelationshipWebManager mTabRelshipWebManager = null;
    private MultiDomainModel mMultiDomainModel;
    private MultiDomainWebManager mMultiDomainWebManager;
    
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
        
    /** Add a Link 
     * 
     * @param linkType
     * @return
     */
    public LinkBaseNode addLink(LinkType linkType) {
        mMultiDomainModel.addLink(linkType);
        LinkType webLinkType = mMultiDomainWebManager.getLinkType(linkType.getName(), linkType.getSourceDomain(), linkType.getTargetDomain());        
        if (webLinkType == null) {
            webLinkType = mMultiDomainWebManager.createLinkType(linkType.getName(), linkType.getSourceDomain(), linkType.getTargetDomain());
            int displayOrder = 1;
            for (Attribute al : linkType.getPredefinedAttributes()) {
                RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                        displayOrder++, 1, "TextBox", null, null, false);
                ((RelationshipType) webLinkType).addFixedRelFieldRef(fieldRef);
            }

            for (Attribute al : linkType.getExtendedAttributes()) {
                RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                        displayOrder++, 1, "TextBox", null, null, false);
                ((RelationshipType) webLinkType).addExtendedRelFieldRef(fieldRef);
            }

            //linkType.getExtendedAttributes().get(0).
        }
        LinkBaseNode node = new LinkBaseNode(this, linkType, webLinkType);
        this.mAlLinkNodes.add(node);
        return node;
    }
    
    /** Delete a link from MultiDomainModel, MultiDomainWebManager and mAlLinkNodes
     * 
     * @param defName
     * @param sourceDomain
     * @param targetDomain
     */
    public void deleteLink(String defName, String sourceDomain, String targetDomain) {      
        mMultiDomainModel.deleteLink(defName, sourceDomain, targetDomain);
        // delete webLinkType here
        LinkType webLinkType = mMultiDomainWebManager.getLinkType(defName, sourceDomain, targetDomain);        
        if (webLinkType != null) {
            //ToDo - wee
            mMultiDomainWebManager.deleteLinkType(webLinkType);
        }
        for (int i=0; mAlLinkNodes!=null && i<mAlLinkNodes.size(); i++) {
            LinkBaseNode node = (LinkBaseNode) mAlLinkNodes.get(i);
            if (node.getName().equals(defName) && node.getSourceDomain().equals(sourceDomain) && node.getTargetDomain().equals(targetDomain)) {
                mAlLinkNodes.remove(i);
                break;
            }
        }
    }
    
    private void loadLinks() {
        // build mAlLinkNodes
        ArrayList <LinkType> alLinkTypes = mMultiDomainModel.getAllLinks();
        //ArrayList <LinkType> allWebLinkTypes = this.mMultiDomainWebManager.getRelationshipTypes();
        
        for (int i=0; alLinkTypes!=null && i<alLinkTypes.size(); i++) {
            LinkType linkType = (LinkType) alLinkTypes.get(i);
            LinkType webLinkType = mMultiDomainWebManager.getLinkType(linkType.getName(), linkType.getSourceDomain(), linkType.getTargetDomain());        
            if (webLinkType == null) {
                webLinkType = mMultiDomainWebManager.createLinkType(linkType.getName(), linkType.getSourceDomain(), linkType.getTargetDomain());       
                //ArrayList
                int displayOrder = 1;
                for (Attribute al : linkType.getPredefinedAttributes()) {
                    
                    RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                            displayOrder++, 1, "TextBox", null, null, false);
                    ((RelationshipType) webLinkType).addFixedRelFieldRef(fieldRef);
                }

                for (Attribute al : linkType.getExtendedAttributes()) {
                    RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                            displayOrder++, 1, "TextBox", null, null, false);
                    ((RelationshipType) webLinkType).addExtendedRelFieldRef(fieldRef);
                }
                
            }
            LinkBaseNode node = new LinkBaseNode(this, linkType, webLinkType);
            this.mAlLinkNodes.add(node);
        }
        
    }
    
    public ArrayList <LinkBaseNode> getLinkNodes() {
        return mAlLinkNodes;
    }
    
    public LinkBaseNode getLinkNode(String defName, String sourceDomain, String targetDomain) {
        LinkBaseNode nodeFound = null;
        for (int i=0; mAlLinkNodes!=null && i<mAlLinkNodes.size(); i++) {
            LinkBaseNode node = (LinkBaseNode) mAlLinkNodes.get(i);
            if (node.getName().equals(defName) && node.getSourceDomain().equals(sourceDomain) && node.getTargetDomain().equals(targetDomain)) {
                nodeFound = node;
                break;
            }
        }
        return nodeFound;
    }
    
    /**
     * Look for src\Domains dir for participating domains
     * Need to verify the list against MultiDomainModel.xml?
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
                        ArrayList <String> alAssociatedDomains = this.mMultiDomainModel.getAssociatedDomains(domainName);
                        ArrayList <LinkType> alLinkTypes = this.mMultiDomainModel.getLinkTypesByDomain(domainName);
                        DomainNode domainNode = new DomainNode(mInstance, domainName, FileUtil.toFile(domain), alAssociatedDomains, alLinkTypes);
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
                DomainNode domainNode = new DomainNode(mInstance, domainName, FileUtil.toFile(newDomainFolder), null, null);
                mMapDomainNodes.put(domainName, domainNode);
                mEditorMainPanel.addDomainNodeToCanvas(domainNode, -1, true);
                mMultiDomainWebManager.getDomains().addDomain(domainNode.getMidmObject());
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
    public boolean deleteDomain(String domainName) {
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
     * Get saved object.xml for the domain
     * @param fileDomain
     * @return
     */
    public static FileObject getSavedDomainQueryXml(File fileDomain) {
        FileObject objectXml = null;
        try {
            FileObject fobjSavedSomain = FileUtil.toFileObject(fileDomain);
            objectXml = fobjSavedSomain.getFileObject(MultiDomainProjectProperties.QUERY_XML);
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
        if (mMapDomainQueryXmls.containsKey(domainName)) {
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
        if (mMapDomainMidmXmls.containsKey(domainName)) {
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

            mMultiDomainModel = getMultiDomainModel(true);
            mMultiDomainWebManager = getMultiDomainWebManager(true);
            loadLinks();

            // Load mMapDomainObjectXmls
            loadDomains();
            // Let TopObjectComponent/EditorMainPanel to do DomainNodes loading
            //loadLinkTypeNodes();

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
     *@return xml string for MultiDomainModel.xml
     */
    public String getMultiDomainModelXmlString() throws Exception {
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
        xmlStr = mMultiDomainApplication.getMultiDomainWebManager(false).writeToString();
        return xmlStr;
    }

    public MultiDomainModel getMultiDomainModel(boolean refresh) {
        return mMultiDomainApplication.getMultiDomainModel(refresh);
    }

    public MultiDomainWebManager getMultiDomainWebManager(boolean refresh) {
        return mMultiDomainApplication.getMultiDomainWebManager(refresh);
    }
 
    
    
    public void enableSaveAction(boolean flag) {
        this.mMultiDomainApplication.setModified(flag);
        this.mEditorMainPanel.enableSaveButton(flag);
    }   

    /* Save all edited files
     * MultiDomainModel.xml and RelationshipWebManager.xml
     *
     *@param bClosing == true when closing the editor, user will be promted fo checking in
     *                == false when save icon is pressed and will save changes to work space only
     */
    public void save(boolean bClosing) {
        try {
            // MultiDomainModel.xml
            String multiDomainModelXml = getMultiDomainModelXmlString();
            if (multiDomainModelXml != null) {
                mMultiDomainApplication.saveMultiDomainModelXml(multiDomainModelXml);
            } else {
                String msg = NbBundle.getMessage(EditorMainApp.class, "MSG_Save_Failed");
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
            
            String relationshipWebManagerXml = getRelationshipWebManagerXmlString();
            if (relationshipWebManagerXml != null) {
                mMultiDomainApplication.saveMultiDomainWebManagerXml(relationshipWebManagerXml);
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
