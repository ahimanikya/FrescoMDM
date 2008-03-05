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
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.modules.InstalledFileLocator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.jar.JarFile;
import com.sun.mdm.index.project.EviewProjectProperties;

/**
 * To import Comparator jar
 * 
 */
public class ImportComparatorAction extends CookieAction {
   
    /**
     * The log4j logger
     */
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            ImportComparatorAction.class.getName()        
                
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
        return NbBundle.getMessage(ImportComparatorAction.class, "LBL_Action_Import_Comparator");
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
                        fc.setMultiSelectionEnabled(false);
                        fc.setFileFilter(new JarFileFilter()); // comparator jar
                        fc.setAcceptAllFileFilterUsed(false);
            
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            EviewMatchEngineFolderCookie cookie = activatedNodes[0].getCookie(EviewMatchEngineFolderCookie.class);
                            EviewMatchEngineFolderNode matchEngineFolderNode = cookie.getEviewMatchEngineFolderNode();
                            FileObject fobjMatchEngine = matchEngineFolderNode.getFileObject();
                            FileObject fobjMatchEngineLib = fobjMatchEngine.getFileObject("lib");
                            if (fobjMatchEngineLib == null) {
                                fobjMatchEngineLib = fobjMatchEngine.createFolder("lib");
                            }
                            
                            File selectedFile = fc.getSelectedFile();
                            String pathSelectedFile = selectedFile.getAbsolutePath();
                            FileObject fo = FileUtil.toFileObject(selectedFile);
                            FileUtil.copyFile(fo, fobjMatchEngineLib, fo.getName());

                            String msg = NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "MSG_Imported_Matcher_Plugin", fo.getName());
                            NotifyDescriptor desc = new NotifyDescriptor.Confirmation(msg);
                            desc.setOptionType(NotifyDescriptor.YES_NO_OPTION);
                            DialogDisplayer.getDefault().notify(desc);
                            if (desc.getValue().equals(NotifyDescriptor.YES_OPTION)) {
                                File pluginFolder = InstalledFileLocator.getDefault().locate(EviewProjectProperties.MATCHER_PLUGIN_LOCATION, "", false);
                                FileObject targetFolder = null;
                                if (pluginFolder == null) {
                                    File templateFolder = InstalledFileLocator.getDefault().locate(EviewProjectProperties.MATCH_TEMPLATE_LOCATION, "", false);
                                    targetFolder = FileUtil.toFileObject(templateFolder).createFolder("lib");
                                }
                                if (targetFolder != null && targetFolder.isFolder()) {
                                    FileObject file = FileUtil.toFileObject(selectedFile);
                                    FileUtil.copyFile(file, targetFolder, file.getName());
                                }
                            }
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportComparatorAction.class, "MSG_FAILED_To_Import_Comparator_Plugin")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {
                    }
                }
            });
        } catch (Exception ex) {
            mLog.severe(NbBundle.getMessage(ImportComparatorAction.class, "MSG_FAILED_To_Perform") + ex.getMessage());
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
        return new Class[]{ImportComparatorAction.class};
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
    
    private class JarFileFilter extends FileFilter {
        
        public boolean accept(java.io.File file) {
            return ( file.isDirectory() || file.getName().endsWith(".jar") );
        }
        
        public String getDescription() {
            return "Jar Files";
        }
        
    }    
}
