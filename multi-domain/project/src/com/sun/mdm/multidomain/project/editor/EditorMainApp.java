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
import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.WebDefinition;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.DefinitionNode;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileLock;


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
    private Map mMapDomainNodesSaved = new HashMap();  // domainName, DomainNode
    private ArrayList <DomainNode> mAlDomainNodes = new ArrayList <DomainNode>();   // DomainNode
    private ArrayList <DefinitionNode> mAlDefinitionNodes = new ArrayList <DefinitionNode>();   // RelationshipNode
    private EditorMainPanel mEditorMainPanel;
    private MultiDomainModel mMultiDomainModel;
    private MultiDomainWebManager mMultiDomainWebManager;
    private boolean bValid = false;
    private String validationMsg = "";
    
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
    
    /**
     * Validate MultiDomainModel.xml and MultiDomainWebManager.xml 
     * before Save button is enabled.
     */
    public boolean validate() {
        if (!mMultiDomainWebManager.validateWebManagerXML()) {
            validationMsg = mMultiDomainWebManager.getValidationStr();
            mLog.error(mMultiDomainWebManager.getValidationStr());
            return false;
        }
        return true;
    }
        
    /** Add a Definition 
     * 
     * @param definition
     * @return
     */
    public DefinitionNode addDefinition(Definition definition) {
        mMultiDomainModel.addDefinition(definition);
        Definition webDefinition = mMultiDomainWebManager.getLinkType(definition.getName(), definition.getSourceDomain(), definition.getTargetDomain());        
        if (webDefinition == null) {
            webDefinition = mMultiDomainWebManager.createWebDefinition(definition.getName(), definition.getSourceDomain(), definition.getTargetDomain(), definition.getType());
            int displayOrder = 1;
            for (Attribute al : definition.getPredefinedAttributes()) {
                RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                        displayOrder++, 1, "TextBox", null, al.getDataType(), false);                        
                ((WebDefinition) webDefinition).addPredefinedFieldRef(fieldRef);
            }

            for (Attribute al : definition.getExtendedAttributes()) {
                RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                        displayOrder++, 1, "TextBox", null, al.getDataType(), false);
                ((WebDefinition) webDefinition).addExtendedRelFieldRef(fieldRef);
            }

            //definition.getExtendedAttributes().get(0).
        }
        DefinitionNode node = new DefinitionNode(this, definition, (WebDefinition) webDefinition);
        mAlDefinitionNodes.add(node);
        return node;
    }
    
    /** Delete a definition from MultiDomainModel, MultiDomainWebManager and mAlDefinitionNodes
     * 
     * @param defName
     * @param sourceDomain
     * @param targetDomain
     */
    public void deleteDefinition(String defName, String sourceDomain, String targetDomain) {      
        mMultiDomainModel.deleteDefinition(defName, sourceDomain, targetDomain);
        // delete webDefinition here
        Definition webDefinition = mMultiDomainWebManager.getLinkType(defName, sourceDomain, targetDomain);        
        if (webDefinition != null) {
            mMultiDomainWebManager.deleteWebDefinition(webDefinition);
        }
        for (int i=0; mAlDefinitionNodes!=null && i<mAlDefinitionNodes.size(); i++) {
            DefinitionNode node = (DefinitionNode) mAlDefinitionNodes.get(i);
            if (node.getName().equals(defName) && node.getSourceDomain().equals(sourceDomain) && node.getTargetDomain().equals(targetDomain)) {
                mAlDefinitionNodes.remove(i);
                break;
            }
        }
    }
    
    /** Delete a definition from MultiDomainModel, MultiDomainWebManager and mAlDefinitionNodes
     *  when domain is removed
     * 
     * @param sourceDomain
     */
    public int deleteDefinition(String domain) {      
        int cnt = mMultiDomainModel.deleteDefinition(domain);
        deleteDefinitionFromDomainNode(domain);
        if (mAlDefinitionNodes!=null) {
            int length = this.mAlDefinitionNodes.size();
            for (int i=length - 1; i>=0 && i < length; i--) {
                DefinitionNode node = (DefinitionNode) mAlDefinitionNodes.get(i);
                if (node.getSourceDomain().equals(domain) || node.getTargetDomain().equals(domain)) {
                    mAlDefinitionNodes.remove(i);
                }
            }
        }
        // delete webDefinition here
        Definition webDefinition = mMultiDomainWebManager.getLinkType(domain);        
        while (webDefinition != null) {
            mMultiDomainWebManager.deleteWebDefinition(webDefinition);
            webDefinition = mMultiDomainWebManager.getLinkType(domain);
        }
        return cnt;
    }
    
    private void loadDefinitions() {
        // build mAlDefinitionNodes
        ArrayList <Definition> alDefinitions = mMultiDomainModel.getAllDefinitions();
        
        for (int i=0; alDefinitions!=null && i<alDefinitions.size(); i++) {
            Definition definition = (Definition) alDefinitions.get(i);
            Definition webDefinition = mMultiDomainWebManager.getLinkType(definition.getName(), definition.getSourceDomain(), definition.getTargetDomain());        
            if (webDefinition == null) {
                webDefinition = mMultiDomainWebManager.createWebDefinition(definition.getName(), definition.getSourceDomain(), definition.getTargetDomain(), definition.getType());       
                //ArrayList
                int displayOrder = 1;
                for (Attribute al : definition.getPredefinedAttributes()) {
                    
                    RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                            displayOrder++, 1, "TextBox", null, al.getDataType(), false);
                    ((WebDefinition) webDefinition).addPredefinedFieldRef(fieldRef);
                }

                for (Attribute al : definition.getExtendedAttributes()) {
                    RelationFieldReference fieldRef = new RelationFieldReference(al.getName(), al.getName(),
                            displayOrder++, 1, "TextBox", null,  al.getDataType(), false);
                    ((WebDefinition) webDefinition).addExtendedRelFieldRef(fieldRef);
                }
                
            }
            DefinitionNode node = new DefinitionNode(this, definition, (WebDefinition) webDefinition);
            this.mAlDefinitionNodes.add(node);
        }
        
    }
    
    /**
     * 
     * @return ArrayList <DefinitionNode>
     */
    public ArrayList <DefinitionNode> getDefinitionNodes() {
        return mAlDefinitionNodes;
    }
    
    public DefinitionNode getDefinitionNode(String defName, String sourceDomain, String targetDomain) {
        DefinitionNode nodeFound = null;
        for (int i=0; mAlDefinitionNodes!=null && i<mAlDefinitionNodes.size(); i++) {
            DefinitionNode node = (DefinitionNode) mAlDefinitionNodes.get(i);
            if (node.getName().equals(defName) && node.getSourceDomain().equals(sourceDomain) && node.getTargetDomain().equals(targetDomain)) {
                nodeFound = node;
                break;
            }
        }
        return nodeFound;
    }
    
    public DefinitionNode copyDefinitionNode(String defName, String sourceDomain, String targetDomain) {
        DefinitionNode nodeFound = null;
        DefinitionNode nodeNew = null;
        for (int i=0; mAlDefinitionNodes!=null && i<mAlDefinitionNodes.size(); i++) {
            DefinitionNode node = (DefinitionNode) mAlDefinitionNodes.get(i);
            if (node.getName().equals(defName) && node.getSourceDomain().equals(sourceDomain) && node.getTargetDomain().equals(targetDomain)) {
                nodeFound = node;
                break;
            }
        }
        if (nodeFound != null) {           
            nodeNew = new DefinitionNode(nodeFound.getEditorMainApp(), 
                                         nodeFound.getDefinition().createCopy(), 
                                         nodeFound.getWebDefinition().createCopy());
            mAlDefinitionNodes.add(nodeNew);
            mMultiDomainWebManager.addWebDefinition(nodeNew.getWebDefinition());
        }
        return nodeNew;
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
                        ArrayList <Definition> alDefinitions = this.mMultiDomainModel.getDefinitionsByDomain(domainName);
                        DomainNode domainNode = new DomainNode(mInstance, domainName, FileUtil.toFile(domain), alAssociatedDomains, alDefinitions);
                        mMapDomainNodes.put(domainName, domainNode);
                        mMapDomainNodesSaved.put(domainName, domainNode);
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
            //Copy domain's object.xml, query.xml and midm.xml
            try {
                FileObject objectXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.OBJECT_XML);
                FileObject queryXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.QUERY_XML);
                FileObject midmXml = getDomainConfigurationFile(fileDomain, MultiDomainProjectProperties.MIDM_XML);
                FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
                FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
                FileObject configFolder = srcFolder.getFileObject(MultiDomainProjectProperties.CONFIGURATION_FOLDER);
                FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                if (domainsFolder == null) {
                    domainsFolder = srcFolder.createFolder(MultiDomainProjectProperties.DOMAINS_FOLDER);
                }
                FileObject newDomainFolder = domainsFolder.getFileObject(domainName);
                if (newDomainFolder != null) {
                    newDomainFolder.delete();
                }
                newDomainFolder = domainsFolder.createFolder(domainName);
                FileUtil.copyFile(objectXml, newDomainFolder, objectXml.getName());
                FileUtil.copyFile(queryXml, newDomainFolder, queryXml.getName());
                FileUtil.copyFile(midmXml, newDomainFolder, midmXml.getName());
                mMapDomainObjectXmls.put(domainName, objectXml);
                mMapDomainQueryXmls.put(domainName, queryXml);
                mMapDomainMidmXmls.put(domainName, midmXml);
                DomainNode domainNode = new DomainNode(mInstance, domainName, FileUtil.toFile(newDomainFolder), null, null);
                mMapDomainNodes.put(domainName, domainNode);
                mMapDomainNodesSaved.put(domainName, domainNode);
                mEditorMainPanel.addDomainNodeToOverview(domainNode, true);
                /**
                FileObject midmDomainFolder = configFolder.getFileObject(MultiDomainProjectProperties.MIDM_NODE_DOMAIN_FOLDER);
                if (midmDomainFolder == null) {
                    midmDomainFolder = configFolder.createFolder(MultiDomainProjectProperties.MIDM_NODE_DOMAIN_FOLDER);
                }
                 */ 
                FileObject file = newDomainFolder.getFileObject(MultiDomainProjectProperties.MIDM_NODE_DOMAIN_XML);

                if (file == null) {
                    file = newDomainFolder.createData(MultiDomainProjectProperties.MIDM_NODE_DOMAIN_XML);
                }

                String midmNodeXml = domainNode.writeDomainObject();
                writeMidmObjectNodeFile(file, midmNodeXml);
                if (mMultiDomainWebManager.getDomains().getDomain(domainNode.getName()) == null) {
                    mMultiDomainWebManager.getDomains().addDomain(domainNode.getMidmObject());
                }
                added = true;
            } catch (IOException ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return added;
    }
    
    private void writeMidmObjectNodeFile(FileObject file, String str) {
        if (file != null) {
            try {
                FileLock fileLock = file.lock();
                OutputStream out = file.getOutputStream(fileLock);
                OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                writer.write(str);
                writer.close();
                fileLock.releaseLock();
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
    }

    /** Called from save()
     *  Do actual remove when save
     * @return
     */
    private boolean deleteDomainFileObject() {
        boolean removed = false;
        for (Object domain : mMapDomainNodesSaved.keySet()) {
            String domainName = (String) domain;
            if (!mMapDomainNodes.containsKey(domain)) {
                FileObject projectDir = mMultiDomainApplication.getProjectDirectory();
                FileObject srcFolder = projectDir.getFileObject(MultiDomainProjectProperties.SRC_FOLDER);
                FileObject domainsFolder = srcFolder.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                FileObject selectedDomainFolder = domainsFolder.getFileObject((String) domain);
                if (selectedDomainFolder != null) {
                    try {
                        selectedDomainFolder.delete();
                        removed = true;
                    } catch (IOException ex) {
                        mLog.severe(ex.getMessage());
                    }
                }
            }
        }
        return removed;
    }
    
    /**
     * Remove domain from the model
     * @param domainName
     * @return
     */
    public boolean deleteDomain(String domainName) {
        boolean removed = false;
        if (mMapDomainObjectXmls.containsKey(domainName)) {
            mMapDomainObjectXmls.remove(domainName);
            mMapDomainQueryXmls.remove(domainName);
            mMapDomainMidmXmls.remove(domainName);
            mAlDomainNodes.remove(mMapDomainNodes.get(domainName));
            mMapDomainNodes.remove(domainName);
                mMultiDomainWebManager.getDomains().removeDomain(domainName);
                
                
            //Remove all domain's definitions too
            /*
            int cnt = 0;
            for (int i=0; mAlDefinitionNodes!=null && i<mAlDefinitionNodes.size(); i++) {
                DefinitionNode node = (DefinitionNode) mAlDefinitionNodes.get(i);
                if (node.getSourceDomain().equals(domainName) || node.getTargetDomain().equals(domainName)) {
                    mAlDefinitionNodes.remove(i);
                    cnt++;
                }
            }
            */
        }
        return removed;
    }
    
    public void deleteDefinitionFromDomainNode(String associatedDomain) {
        for (DomainNode node : mAlDomainNodes) {
            ArrayList <Definition> alDefinitions = node.getDefinitions();
            if (alDefinitions != null) {
                int length = alDefinitions.size();
                for (int i = length - 1; i>= 0 && i < length; i--) {
                    Definition definition = alDefinitions.get(i);
                    if (definition.getSourceDomain().equals(associatedDomain) || definition.getTargetDomain().equals(associatedDomain)) {
                        alDefinitions.remove(i);
                    }
                }
            }
        }
    }
    
    /** get plugin list per domain and definition type
     * 
     * @param domain
     * @param type
     * @return
     */
    public ArrayList <String> getPluginList(String domain, String type) {
        //ToDo call Shant's api
        ArrayList <String> al = new ArrayList();
        al.add(type + "PluginFor" + domain);
        return al;
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
            loadDefinitions();
            loadDomains();
            mEditorMainPanel = new EditorMainPanel(this, mMultiDomainApplication);
            mEditorMainPanel.loadDomainNodesToOverview();
            mObjectTopComponent = new ObjectTopComponent();
            mObjectTopComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            mMultiDomainApplication.setObjectTopComponent(mObjectTopComponent);
            if (mObjectTopComponent.startTopComponent(getInstance(projName), projName, application, mEditorMainPanel)) {
                mObjectTopComponent.open();
            }
            mObjectTopComponent.setCursor(Cursor.getDefaultCursor());
            this.requestFocus();
        } catch (Exception ex) {
            mMapInstances.remove(projName);
            mLog.severe(ex.getMessage());
            displayError(ex);
        }
    }
    
    public EditorMainPanel getEditorMainPanel() {
        return mEditorMainPanel;
    }
    
    /**
     *@return xml string for MultiDomainModel.xml
     */
    public String getMultiDomainModelXmlString() throws Exception {
        String xmlStr = null;
        //MultiDomainModel multiDomainModel = mMultiDomainApplication.getMultiDomainModel(false);
        ArrayList <String> alDomainNames = new ArrayList <String>();
        for (DomainNode node : mAlDomainNodes) {
            alDomainNames.add(node.getName());
        }
        mMultiDomainModel.setDomainNames(alDomainNames);
        xmlStr = mMultiDomainModel.writeToString();
        return xmlStr;
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
 
    public void enableDeleteAction(boolean flag) {
        this.mEditorMainPanel.enableDeleteButton(flag);
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
            deleteDomainFileObject();
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

    public void setValid(boolean bValid) {
        this.bValid = bValid;
    }

    public String getValidationMsg() {
        return validationMsg;
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
