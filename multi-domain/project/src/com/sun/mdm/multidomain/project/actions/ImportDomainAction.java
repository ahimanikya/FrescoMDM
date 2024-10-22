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
package com.sun.mdm.multidomain.project.actions;

import org.openide.ErrorManager;
import org.openide.util.actions.CookieAction;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.openide.util.NbBundle;
import org.openide.nodes.Node;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;

import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.nodes.*;

/**
 * To get Relationship jar with dataTypeDescription.xml
 * 
 */
public class ImportDomainAction extends CookieAction {
    private ProgressHandle mLoadProgress = null;

    /**
     * The log4j logger
     */
    private static final Logger mLog = Logger.getLogger(
            ImportDomainAction.class.getName());

    /**
     * Gets the help context.
     *
     * @return HelpCtx.DEFAULT_HELP
     */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Gets the name to show in the context menu.
     *
     * @return the cookie action name
     */
    public String getName() {
        return NbBundle.getMessage(ImportDomainAction.class, "LBL_Action_Import_Domain");
    }
    
    /**
     * Performs the cookie action for the selected node.
     *
     * @param activatedNodes data nodes that activate the elected action
     */
    public void performAction(final Node[] activatedNodes) {
    }
    
    public void perform(final EditorMainApp editorMainApp) {
        try {
            javax.swing.SwingUtilities.invokeLater (new Runnable () {
                public void run() {
                    JFileChooser fc = new JFileChooser();
                    try {
                        fc.setMultiSelectionEnabled(false);
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.setFileFilter(new DomainFilter(fc)); // look for MDM domains
                        fc.setAcceptAllFileFilterUsed(false);
                        fc.setDialogTitle(NbBundle.getMessage(ImportDomainAction.class, "MSG_Select_Master_Index_Title"));
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            mLoadProgress = ProgressHandleFactory.createHandle(
                                NbBundle.getMessage(ImportDomainAction.class, "MSG_Importing_Domain"));
                            mLoadProgress.start();
                            mLoadProgress.switchToIndeterminate();
                            //Get MDM object.xml
                            File selectedDomain = fc.getSelectedFile();
                            String domainName = selectedDomain.getName();
                            editorMainApp.addDomain(selectedDomain);
                            mLoadProgress.finish();
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportDomainAction.class, "MSG_FAILED_To_Import_Domain")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {
                    }
                }
            });
        } catch (Exception ex) {
            String msg = NbBundle.getMessage(ImportDomainAction.class, "MSG_FAILED_To_Perform_ImportDomainAction") + ex.getMessage();
            mLog.info(msg);
            NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
            DialogDisplayer.getDefault().notify(desc);
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /**
     * Provides the associated cookie class.
     *
     * @return the list of cookie class
     */
    protected Class[] cookieClasses() {
        return new Class[]{ImportDomainAction.class};
    }

    /**
     * Answers whether this action is enabled.
     *
     * @param activatedNodes data nodes that activate the elected action
     *
     * @return true
     */
    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    /**
     * Answers which mode this action should operate in. In this case, only one
     * node can be selected at a time.
     *
     * @return returns CookieAction.MODE_EXACTLY_ONE
     */
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }
    
    /**
     * Check if file is instanceof EviewApplication
     */
    private boolean isMasterIndexProject(File file) {
        boolean bRet = false;
        FileObject fobj = FileUtil.toFileObject(file);
        if (fobj != null) {
            try {
                Project p = ProjectManager.getDefault().findProject(fobj);
                if (p != null) {
                    String clsName = p.getClass().getName();
                    if (clsName.equals("com.sun.mdm.index.project.EviewApplication")) {
                        bRet = true;
                    }
                }
            } catch (Exception ex) {
                String msg = ex.getMessage();
            }
        }
        return bRet;
    }

    private class DomainFilter extends FileFilter {
        JFileChooser fc;
        public DomainFilter(JFileChooser fc) {
            this.fc = fc;
        }
        
        public boolean accept(java.io.File file) {
            boolean accepted = isMasterIndexProject(file);
            return (file.isDirectory() || accepted);
        }
        
        public String getDescription() {
            return NbBundle.getMessage(ImportDomainAction.class, "MSG_Master_Index_Project");
        }
        
    }    
}
