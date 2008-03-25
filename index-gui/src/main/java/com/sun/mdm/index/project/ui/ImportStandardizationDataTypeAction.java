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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.modules.InstalledFileLocator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.util.zip.ZipFile;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;

import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.standardizer.introspector.Descriptor;
import com.sun.mdm.standardizer.introspector.VariantDescriptor;
import com.sun.mdm.standardizer.introspector.StandardizationIntrospector;
import com.sun.mdm.index.project.EviewProjectProperties;

/**
 * To get Standardization jar with dataTypeDescription.xml
 * 
 */
public class ImportStandardizationDataTypeAction extends CookieAction {
    private ProgressHandle mLoadProgress = null;

    /**
     * The log4j logger
     */
    private static final com.sun.mdm.index.util.Logger mLog = com.sun.mdm.index.util.Logger.getLogger(
            ImportStandardizationDataTypeAction.class.getName());

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
        return NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "LBL_Action_Import_Standardization_Plugin");
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
                        fc.setFileFilter(new ZipFileFilter()); // look for implementation zip
                        fc.setAcceptAllFileFilterUsed(false);
            
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {

                            mLoadProgress = ProgressHandleFactory.createHandle(
                                NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "MSG_Importing_Standardization_Plugin"));
                            mLoadProgress.start();
                            mLoadProgress.switchToIndeterminate();
                            
                            EviewStandardizationFolderCookie cookie = activatedNodes[0].getCookie(EviewStandardizationFolderCookie.class);
                            EviewStandardizationFolderNode standardizationFolderNode = cookie.getEviewStandardizationFolderNode();
                            FileObject standFolder = standardizationFolderNode.getFileObject();
                            
                            File selectedFile = fc.getSelectedFile();
                            String pathSelectedFile = selectedFile.getAbsolutePath();

                            //Call util to get the data type, create sub folder, e.g. Address
                            ZipFile zipDataType = new ZipFile(pathSelectedFile);

                            EviewApplication eviewApplication = standardizationFolderNode.getEviewApplication();
                            StandardizationIntrospector introspector = eviewApplication.getStandardizationIntrospector();
                            Descriptor dataTypeDescriptor = introspector.deploy(zipDataType);
                            String descDT = dataTypeDescriptor.getDescription();
                            String dataType = dataTypeDescriptor.getName();
                            String variantType = null;
                            if (dataTypeDescriptor instanceof VariantDescriptor) {
                                dataType = ((VariantDescriptor) dataTypeDescriptor).getTypeName();
                                variantType = ((VariantDescriptor) dataTypeDescriptor).getVariantType();
                            }

                            mLoadProgress.finish();
                            
                            String msg = NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "MSG_Imported_Standardization_Plugin", descDT, dataType);
                            NotifyDescriptor desc = new NotifyDescriptor.Confirmation(msg);
                            desc.setOptionType(NotifyDescriptor.YES_NO_OPTION);
                            DialogDisplayer.getDefault().notify(desc);
                            if (desc.getValue().equals(NotifyDescriptor.YES_OPTION)) {
                                File pluginFolder = InstalledFileLocator.getDefault().locate(EviewProjectProperties.STANDARDIZATION_PLUGIN_LOCATION, "", false);
                                FileObject foPluginFolder = null;
                                if (pluginFolder == null) {
                                    File deploymentFolder = InstalledFileLocator.getDefault().locate(EviewProjectProperties.STANDARDIZATION_DEPLOYMENT_LOCATION, "", false);
                                    foPluginFolder = FileUtil.toFileObject(deploymentFolder).createFolder(EviewProjectProperties.PLUGIN);
                                }
                                FileObject targetFolder = null;
                                if (variantType != null) {  // Variant Plugin
                                    File folder = InstalledFileLocator.getDefault().locate(EviewProjectProperties.STANDARDIZATION_PLUGIN_LOCATION + File.separatorChar + dataType, "", false);
                                    if (folder == null || !folder.isDirectory()) {
                                        targetFolder = foPluginFolder.createFolder(dataType);
                                    } else {
                                        targetFolder = FileUtil.toFileObject(folder);
                                    }
                                } else {    // DataType Plugin
                                    targetFolder = foPluginFolder;
                                }
                                if (targetFolder != null && targetFolder.isFolder()) {
                                    FileObject file = FileUtil.toFileObject(selectedFile);
                                    FileUtil.copyFile(file, targetFolder, file.getName());
                                }
                            }
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "MSG_FAILED_To_Import_Standardization_Plugin")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {

                    }
                }
            });
        } catch (Exception ex) {
            String msg = NbBundle.getMessage(ImportStandardizationDataTypeAction.class, "MSG_FAILED_To_Perform") + ex.getMessage();
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
        return new Class[]{ImportStandardizationDataTypeAction.class};
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
    
    private class ZipFileFilter extends FileFilter {
        
        public boolean accept(java.io.File file) {
            return ( file.isDirectory() || file.getName().endsWith(".zip") );
        }
        
        public String getDescription() {
            return "Zip Files";
        }
        
    }    
}
