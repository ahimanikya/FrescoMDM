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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.nodes.NodeAdapter;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.cookies.SaveCookie;

import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.parser.RelationshipModel;
import com.sun.mdm.multidomain.parser.RelationshipWebManager;
import com.sun.mdm.multidomain.util.Logger;


/**
 * A wapper top component contains Multi-Domain MDM configuration editor
 *
 */
public class ObjectTopComponent
         extends TopComponent {

    /** The log4j message logger. */
    private static Logger mLog = Logger.getLogger(ObjectTopComponent.class.getName());
    public static final String MODE_NAME = "MULTI_DOMAIN_MDM";

    /**
     * name of this TopComponent
     */
    private String mTabName;
    private String mPath;
    private EditorMainApp mEditorMainApp;
    private EditorMainPanel mEditorMainPanel;
    private MultiDomainApplication mMultiDomainApplication;
    private RelationshipModel mRelationshipModel;
    private RelationshipWebManager mRelationshipWebManager;

    /**
     * an empty constructor is required by Netbeans
     */
    public ObjectTopComponent() {
        super();
        putClientProperty("PersistenceType", "Never");
    }

    /**
     * get name of the top component
     *
     * @return name String
     */
    @Override
    public String getName() {
        if (mTabName == null) {
            try {
                mTabName = mMultiDomainApplication.getName();
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mTabName;
    }

    /** called by MultiDomainApplication
     * 
     * @param flag
     */
    public void setModified(boolean flag) {
        if (flag) {
            mTabName = mPath + "*";
        } else {
            mTabName = mPath;
        }
        super.setDisplayName(mTabName);
    }

    /**
     * called by Netbeans to determine if the workspace can close shall return
     * false when document changed
     *
     * @return boolean canClose
     */
    @Override
    public boolean canClose() {
        try {
            if (mMultiDomainApplication.isValidated() &&  mMultiDomainApplication.isModified()) {
                int ret = JOptionPane.showConfirmDialog(
                            this,
                            NbBundle.getMessage(ObjectTopComponent.class, "Msg_Do_you_want_to_save_Configuration")
                                + mPath
                                + "?",
                            NbBundle.getMessage(ObjectTopComponent.class, "LBL_Editor"),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    this.mEditorMainApp.save(true);
                    this.mEditorMainApp.removeInstance(mPath);
                    return super.canClose();
                } else if (ret == JOptionPane.NO_OPTION) {
                    // Reload RelationshipModel and others
                    this.mMultiDomainApplication.loadAll();
                    this.mEditorMainApp.removeInstance(mPath);
                    return super.canClose();
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        mEditorMainApp.removeInstance(mPath);
        return super.canClose();
    }   

    /**
     * Open the editor panel
     */
    //@Override
    //public void open() {
        //super.open();
        //this.requestActive();
    //}
   
    /**
     * request Focus from Netbeans
     */
    //@Override
    //public void requestFocus() {
        //super.requestFocus();
    //}


    /**
     * request visible from Netbeans
     */
    @Override
    public void requestVisible() {
        super.requestVisible();
    }

    @Override
    protected String preferredID() { 
        return MODE_NAME + Math.random();
    } 

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
    
    /**
     * starting point of Top Component
     *
     * @param multiDomainApplication instance
     * @return boolean successful
     */
    public boolean startTopComponent(EditorMainApp editorMainApp, String path, MultiDomainApplication multiDomainApplication, EditorMainPanel editorMainPanel) {
        // get static copy of Mode name from Tools App Context
        if (multiDomainApplication == null
            || path == null) {
            return false;
        }

        try {
            mPath = path;
            mTabName = path;
            mMultiDomainApplication = multiDomainApplication;
            mEditorMainApp = editorMainApp;
            mEditorMainPanel = editorMainPanel;
            //ToDo-
            //getRelationshipModel
            mRelationshipModel = mMultiDomainApplication.getRelationshipModel(false);
            //from RelationshipModel get all object model from participating domains
            //-ToDo
            mRelationshipWebManager = mMultiDomainApplication.getRelationshipWebMAnager(true);
            
            // init main panel, which init the data model and all the views
            boolean ret = true;
            if (ret) {
                // add the Main Panel to this Top Component
                this.setLayout(new BorderLayout(0, 0));
                this.add(mEditorMainPanel, BorderLayout.CENTER);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
            return false;
        }
    }

    /**
     *
     * @return String
     */
    public String getInstancePath() {
        return mPath;
    }

    /**
     * Implementation of SaveCookie which will be performed once global Save is 
     * clicked on for this top component
     */
    private class Save implements SaveCookie {

        private ObjectTopComponent myTC = null;
        
        public Save(ObjectTopComponent tc) {
            myTC = tc;
        }
        
        public void save() throws java.io.IOException {
            myTC.mEditorMainApp.save(false);
        }                 
    }
    /**
     * Getter for the save cookie
     *
     * @return a new save cookie
     */
    public SaveCookie getSaveCookie() {
        return new Save(this);
    }    

    private void addListeners() {
        if (mMultiDomainApplication == null) {
            return;
        }
        try {
            final Node node = mMultiDomainApplication.getAssociatedNode();
            node.addNodeListener(new NodeAdapter() {
                @Override
                public void propertyChange(PropertyChangeEvent ev) {
                    if (ev.getPropertyName().equals(Node.PROP_DISPLAY_NAME)) {
                        mTabName = node.getDisplayName();
                        ObjectTopComponent.this.setName(mTabName);
                        if (ObjectTopComponent.this.isOpened()) {
                            ObjectTopComponent.this.close();
                            ObjectTopComponent.this.open();
                            ObjectTopComponent.this.requestActive();
                        }
                        //try {
                        //    mPath = mPath.substring(0, mPath.lastIndexOf('/') + 1) + mMultiDomainApplication.getApplicationName();
                        //} catch (Exception ex) {
                        //    mLog.severe(ex.getMessage());
                        //}
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
