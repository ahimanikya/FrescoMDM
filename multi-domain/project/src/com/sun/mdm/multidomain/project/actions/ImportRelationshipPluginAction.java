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

import com.sun.mdm.multidomain.project.nodes.*;
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
import org.openide.modules.InstalledFileLocator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;


/**
 * To get Relationship jar with dataTypeDescription.xml
 * 
 */
public class ImportRelationshipPluginAction extends CookieAction {
    private ProgressHandle mLoadProgress = null;

    /**
     * The log4j logger
     */
    private static final Logger mLog = Logger.getLogger(
            ImportRelationshipPluginAction.class.getName());

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
        return NbBundle.getMessage(ImportRelationshipPluginAction.class, "LBL_Action_Import_Relationship_Plugin");
    }
    
    /**
     * Performs the cookie action for the selected node.
     *
     * @param activatedNodes data nodes that activate the elected action
     */
    public void performAction(final Node[] activatedNodes) {
        try {
            RequestProcessor.getDefault().post(new Runnable() {
                public void run() {
                    JFileChooser fc = new JFileChooser();
                    try {
                        fc.setMultiSelectionEnabled(false);
                        fc.setFileFilter(new RelationsipPlugInFilter()); // look for relationship plug-in
                        fc.setAcceptAllFileFilterUsed(false);
            
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {

                            mLoadProgress = ProgressHandleFactory.createHandle(
                                NbBundle.getMessage(ImportRelationshipPluginAction.class, "MSG_Importing_Relationship_Plugin"));
                            mLoadProgress.start();
                            mLoadProgress.switchToIndeterminate();
                            

                            mLoadProgress.finish();
                            
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportRelationshipPluginAction.class, "MSG_FAILED_To_Import_Relationship_Plugin")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {

                    }
                }
            });
        } catch (Exception ex) {
            String msg = NbBundle.getMessage(ImportRelationshipPluginAction.class, "MSG_FAILED_To_Perform_ImportRelationshipPluginAction") + ex.getMessage();
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
        return new Class[]{ImportRelationshipPluginAction.class};
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
    
    private class RelationsipPlugInFilter extends FileFilter {
        
        public boolean accept(java.io.File file) {
            return ( file.isDirectory() || file.getName().endsWith(".zip") );
        }
        
        public String getDescription() {
            return NbBundle.getMessage(ImportRelationshipPluginAction.class, "MSG_Relationship_Plugin_Files");
        }
        
    }    
}