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

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.logging.Logger;
import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.openide.util.Utilities;
import org.openide.nodes.Node;
import org.openide.nodes.NodeAdapter;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

import com.sun.mdm.index.project.EviewApplication;


/**
 * A wapper top component contains eView configuration editor
 *
 */
public class ObjectTopComponent
         extends TopComponent {

    /** The log4j message logger. */
    private static Logger mLog = Logger.getLogger(ObjectTopComponent.class.getName());
    private static Image mIcon = Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/InstanceNode.png");
    public static final String MODE_NAME = "EVIEW_MODE";

    /**
     * name of this TopComponent
     */
    private String mNodeName;
    private String mObjectName;
    private String mPath;
    private EviewEditorMainApp mEviewEditorMainApp;
    private EviewEditorMainPanel mEviewEditorMainPanel;
    private EviewApplication mEviewApplication;

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
        if (mNodeName == null) {
            try {
                mNodeName = mEviewApplication.getName();
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
        return mNodeName;
    }


    /**
     * set name for the top component
     *
     * @param name name for the top component
     */
    @Override
    public void setName(String name) {
        mNodeName = name;
        super.setName(mNodeName);
    }


    /**
     * called from EviewEditorMainPanel
     *
     * @param name TC name
     */
    public void setTCName(String name) {
        super.setName(name);
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
            if (this.mEviewEditorMainApp.isCheckedOut()) { 
                if (mEviewApplication.isValidated() &&  mEviewApplication.isModified()) {
                    int ret = JOptionPane.showConfirmDialog(
                                this,
                                NbBundle.getMessage(ObjectTopComponent.class, "Msg_Do_you_want_to_save_eView")
                                    + mObjectName
                                    + "?",
                                NbBundle.getMessage(ObjectTopComponent.class, "LBL_eView_Editor"),
                                JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.YES_OPTION) {
                        this.mEviewEditorMainApp.save(true);
                        this.mEviewEditorMainApp.removeInstance(mPath);
                        return super.canClose();
                    } else if (ret == JOptionPane.NO_OPTION) {
                        // Reload eIndexObject and others
                        //this.mEviewApplication.undoCheckoutAllConfigurableFiles();
                        this.mEviewApplication.loadAll();
                        this.mEviewEditorMainApp.removeInstance(mPath);
                        return super.canClose();
                    } else {
                        return false;
                    }
                } else if (mEviewApplication.getNeedToCheckIn() == true) {
                    /*
                    String comment = "";
                    SaveDialog dlg = new SaveDialog(true);
                    dlg.setVisible(true);
                    if (dlg.getSaveOption() == SaveDialog.CHECKIN_OPTION) {
                        comment = dlg.getComment();
                        this.mEviewApplication.checkinAllConfigurableFiles(comment);
                    }
                     */
                } else {
                    /*
                    int ret = JOptionPane.showConfirmDialog(
                                this,
                                NbBundle.getMessage(ObjectTopComponent.class, "Msg_Do_you_want_to_undo_checkout_eView"), 
                                NbBundle.getMessage(ObjectTopComponent.class, "LBL_eView_Editor"),
                                JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.YES_OPTION) {
                        this.mEviewApplication.undoCheckoutAllConfigurableFiles();
                    } else if (ret == JOptionPane.CANCEL_OPTION) {
                        return false;
                    }
                     */ 
                }
            }

        } catch (Exception ex) {
            // this is an intermittent bug of Netbeans. Do nothing
            mLog.severe(ex.getMessage());
        }
        mEviewEditorMainApp.removeInstance(mPath);
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

    /**
     * starting point of Top Component
     *
     * @param EviewApplication instance
     * @return boolean successful
     */
    public boolean startEviewTopComponent(EviewEditorMainApp eviewEditorMainApp, String path, EviewApplication eviewApplication) {
        // get static copy of Mode name from Tools App Context
        if (eviewApplication == null
            || path == null) {
            return false;
        }

        try {
            mPath = path;
            mEviewEditorMainApp = eviewEditorMainApp;
            mEviewApplication = eviewApplication;
            mNodeName = path; //mEviewApplication.getName();
            
            mObjectName = mEviewApplication.getObjectName();
            
            mEviewEditorMainPanel = mEviewEditorMainApp.newEviewEditorMainPanel();
            //addListeners();
            // init main panel, which init the data model and all the views
            boolean ret = true;
            if (ret) {
                // add the Main Panel to this Top Component
                this.setLayout(new BorderLayout(0, 0));
                this.add(mEviewEditorMainPanel, BorderLayout.CENTER);
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
     * Saves the configuration
     */
    public void save() {
        // 
        //rootNode
    }
    
    private void addListeners() {
        if (mEviewApplication == null) {
            return;
        }
        try {
            final Node node = mEviewApplication.getRootNode();
            node.addNodeListener(new NodeAdapter() {
                @Override
                public void propertyChange(PropertyChangeEvent ev) {
                    if (ev.getPropertyName().equals(Node.PROP_DISPLAY_NAME)) {
                        mNodeName = node.getDisplayName();
                        ObjectTopComponent.this.setName(mNodeName);
                        if (ObjectTopComponent.this.isOpened()) {
                            ObjectTopComponent.this.close();
                            ObjectTopComponent.this.open();
                            ObjectTopComponent.this.requestActive();
                        }
                        try {
                            mPath = mPath.substring(0, mPath.lastIndexOf('/') + 1) + mEviewApplication.getApplicationName();
                        } catch (Exception ex) {
                            mLog.severe(ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
