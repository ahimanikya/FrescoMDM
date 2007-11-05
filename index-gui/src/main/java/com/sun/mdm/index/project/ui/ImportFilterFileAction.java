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
package com.sun.mdm.index.project.ui;

import org.openide.ErrorManager;
import org.openide.util.actions.CookieAction;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.openide.util.NbBundle;
import org.openide.nodes.Node;
import org.openide.nodes.FilterNode;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Implements the methods for the delete action.
 * 
 */
public class ImportFilterFileAction extends CookieAction {
   
    /**
     * The log4j logger
     */
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            ImportFilterFileAction.class.getName()
        
        );

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
        return NbBundle.getMessage(ImportFilterFileAction.class, "MSG_Action_Import_Filter_Files");
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
                    try {
                        final JFileChooser fc = new JFileChooser();
                        fc.setMultiSelectionEnabled(true);
                        fc.setFileFilter(new FilterFileFilter());
                        fc.setAcceptAllFileFilterUsed(false);
            
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File[] files = fc.getSelectedFiles();
                            for (int i = 0; i < files.length; i++) {
                                if (!files[i].exists()) {
                                    throw new FileNotFoundException("File: " + files[i].getName() + " not found.");
                                }
                            }
                            EviewFilterCookie cookie = activatedNodes[0].getCookie(EviewFilterCookie.class);
                            EviewFilterFolderNode filterFolderNode = cookie.getEviewFilterFolderNode();
                            FileObject filterFolder = filterFolderNode.getFileObject();
                            for (int i=0; i < files.length; i++) {
                                FileObject fo = FileUtil.toFileObject(files[i]);
                                FileUtil.copyFile(fo, filterFolder, fo.getName());
                            }
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportFilterFileAction.class, "MSG_FAILED_To_Import_Filter_Files")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {
                    }
                }
            });
        } catch (Exception ex) {
            mLog.severe(NbBundle.getMessage(ImportFilterFileAction.class, "MSG_FAILED_To_Perform") + ex.getMessage());
        }           
    }

    /**
     * Provides the associated cookie class.
     *
     * @return the list of cookie class
     */
    protected Class[] cookieClasses() {
        return new Class[]{ImportFilterFileAction.class};
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
    
    private class FilterFileFilter extends FileFilter {
        
        public boolean accept(java.io.File file) {
            return ( file.isDirectory() || file.getName().endsWith(".txt") || file.getName().endsWith(".xml") );
        }
        
        public String getDescription() {
            return "XML/Text Files";
        }
        
    }    
}
