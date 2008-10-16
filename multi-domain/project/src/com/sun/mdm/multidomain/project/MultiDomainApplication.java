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
package com.sun.mdm.multidomain.project;

import com.sun.mdm.multidomain.project.nodes.MultiDomainConfigurationFolderNode;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileLock;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;

import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.parser.MultiDomainModel;
import com.sun.mdm.multidomain.parser.RelationshipWebManager;
import com.sun.mdm.multidomain.project.editor.ObjectTopComponent;
import com.sun.mdm.multidomain.util.Logger;

public class MultiDomainApplication extends MultiDomainProject {
    private static final Logger mLog = Logger.getLogger(
            MultiDomainApplication.class.getName()
        
        );

    private AntProjectHelper mHelper;
    private MultiDomainConfigurationFolderNode mAssociatedNode;
    private ObjectTopComponent mObjectTopComponent;
    public final String MULTIDOMAIN_APPLICATION_NAME = "multiDomainApplicationName";
    public final String MULTIDOMAIN_OBJECT_NAME = "multiDomainObjectName";

    // the following variables can be modified by Configuration Editor
    // and need to be synched up with xml files or reloaded
    private boolean mModified = false;
    private MultiDomainModel mMultiDomainModel = null;      // OBJECT_XML

    private RelationshipWebManager mRelationshipWebManager = null;      // RelationshipWebManager.xml
    private FileObject fileRelationshipMappings;
    private FileObject fileMultiDomainModel;
    private FileObject fileobjRelationshipWebManager;
    private FileObject fileobjMultiDomainModel;
    private FileObject fileRelationshipWebManager;
    
    
    /** Creates a new instance of MultiDomainApplication */
    public MultiDomainApplication(final AntProjectHelper helper) throws IOException  {
        super(helper);
        mHelper = helper;
    }
    
    public AntProjectHelper getHelper() {
        return mHelper;
    }
    
    private EditableProperties getEditableProperties() {
        EditableProperties ep = mHelper.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        return ep;
    }
    
    /** Parses object.xml and returns com.sun.mdm.index.parser.MultiDomainModel by parsing object.xml
     * @throws RepositoryException failed to get value
     * @return the MultiDomainModel
     */    
    public MultiDomainModel getMultiDomainModel(boolean bRefresh) {
        try {
            if (mMultiDomainModel == null || bRefresh == true) {
                FileObject rmf = this.getMultiDomainModelFile();
                if (rmf != null) {
                    InputStream objectdef = rmf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mMultiDomainModel = Utils.parseMultiDomainModel(source);
                }
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return mMultiDomainModel;
    }

    /** Parses object.xml and returns com.sun.mdm.index.parser.RelationshipWebManager by parsing object.xml
     * @throws RepositoryException failed to get value
     * @return the RelationshipWebManager
     */    
    public RelationshipWebManager getRelationshipWebMAnager(boolean bRefresh) {
        try {
            if (mRelationshipWebManager == null || bRefresh == true) {
                FileObject cf = this.getRelationshipWebManagerFile();
                if (cf != null) {
                    InputStream objectdef = cf.getInputStream();
                    InputSource source = new InputSource(objectdef);
                    mRelationshipWebManager = Utils.parseRelationshipWebManager(source);
                }
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return mRelationshipWebManager;
    }
        
    /**
     * 
     * @param MultiDomainConfigurationFolderNode node
     */
    public void setAssociatedNode(MultiDomainConfigurationFolderNode node) {
        mAssociatedNode = node;
    }
    
    /**
     * 
     * @return MultiDomainConfigurationFolderNode
     */
    public MultiDomainConfigurationFolderNode getAssociatedNode() {
        return mAssociatedNode;
    }
    
    /**
     * 
     * @param ObjectTopComponent otc
     */
    public void setObjectTopComponent(ObjectTopComponent otc) {
        mObjectTopComponent = otc;
    }
    
    /**
     * 
     * @return ObjectTopComponent
     */
    public ObjectTopComponent getObjectTopComponent() {
        return mObjectTopComponent;
    }
       
    /** chaeck out RelationshipWebManager
     *
     *@return boolean checked out
     */
    private FileObject getRelationshipWebManagerXml() {
        try {
            fileobjRelationshipWebManager = getRelationshipWebManagerFile();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return fileobjRelationshipWebManager;
    }

    /** Save OBJECT_DEF_FILE
     *
     *@param data
     */
    public void saveRelationshipWebManagerXml(String strXml) {
        try {
            if (fileobjRelationshipWebManager != null) {
                writeConfigurationFile(fileobjRelationshipWebManager, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    /** chaeck out RELATIONSHIP_MODEL_XML
     *
     *@return boolean checked out
     */
    private FileObject getMultiDomainModelXml() {
        try {
            fileobjMultiDomainModel = getConfigurationFile(MultiDomainProjectProperties.CONFIGURATION_FOLDER, MultiDomainProjectProperties.MULTI_DOMAIN_MODEL_XML, false);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return fileobjMultiDomainModel;
    }
    

    /** update UpdateConfigurationFile in repository
     *
     *@param data
     */
    public void saveMultiDomainModelXml(String strXml) {
        try {
            if (fileobjMultiDomainModel != null) {
                writeConfigurationFile(fileobjMultiDomainModel, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }

    
    /** update UpdateConfigurationFile in repository
     *
     *@param data
     */
    public void saveRelationshipWebManagerXml(String strXml, String comment, boolean checkIn) {
        try {
            if (fileRelationshipWebManager != null) {
                writeConfigurationFile(fileRelationshipWebManager, strXml);
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }    
    
    /* Reset all modified flag
     *@param flag
     */
    public void resetModified(boolean flag) {
        mModified = flag; 
        setModified(flag);
    }
    
    /** return the FileObject for object.xml
     * @return FileObject fo
     */ 
    public FileObject getMultiDomainModelFile() {
        fileobjMultiDomainModel = getConfigurationFile(MultiDomainProjectProperties.CONFIGURATION_FOLDER, MultiDomainProjectProperties.MULTI_DOMAIN_MODEL_XML, false);
        return fileobjMultiDomainModel;
    }
    
    /** return the FileObject for object.xml
     * @return FileObject fo
     */ 
    public FileObject getRelationshipWebManagerFile() {
        FileObject fo = getConfigurationFile(MultiDomainProjectProperties.CONFIGURATION_FOLDER, MultiDomainProjectProperties.RELATIONSHIP_WEB_MANAGER_XML, false);
        return fo;
    }
    
    /** returns Configuration FileObject from project directory
    If create == true, then it will create empty file if it does not exist
    Returns null, if no File exists and create == false
    throws EviewRepositoryException if  it can’t create a file
    */

    public FileObject getConfigurationFile(String folder, String name, boolean create) {
        FileObject file = null;
        try{
            FileObject srcDir = this.getSourceDirectory();
            FileObject confDir = srcDir.getFileObject(folder);            
            
            file = confDir.getFileObject(name);
            if (file == null && create == true) {
                file = confDir.createData(name);
            }
        } catch (IOException ex) {
            mLog.severe(ex.getMessage());
        }
        return file;
    }
     
    /* Checkout all configurable files for editing
     *
     *@return bCheckedOut
     *
     */
    public boolean getAllConfigurableFiles() {
        return (getMultiDomainModelXml() != null) &&
               (getRelationshipWebManagerXml() != null);
    }
    
    private void writeConfigurationFile(FileObject file, String str) {
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
    
    /* 
     * Reload all in-memory const
     */
    public void loadAll() {
        try {
            getAllConfigurableFiles();
            setModified(false);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }

    /* set dirty bit by various configuration panels
     * so we know we need to rewrite xml files
     */
    public void setModified(boolean flag) {
        mModified = flag;
        /**
        if (flag == false) {
            return;
        }
         */ 
        mAssociatedNode.setModified(flag);
        mObjectTopComponent.setModified(flag);
    }
    
    /** Check if configuration is modified
     * 
     * @return mModified
     */
    public boolean isModified() {
        return mModified;
    }
    
    /**
     * Not implemented
     * @return
     */
    public boolean isValidated() {
        boolean validated = true;
        return validated;
    }


}
