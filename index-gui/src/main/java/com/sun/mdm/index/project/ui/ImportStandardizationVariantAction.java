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
import org.openide.loaders.DataFolder;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
//import java.io.FileNotFoundException;

/**
 * To get Standardization jar with dataTypeDescription.xml
 * 
 */
public class ImportStandardizationVariantAction extends CookieAction {
   
    /**
     * The log4j logger
     */
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            ImportStandardizationVariantAction.class.getName()        
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
        return NbBundle.getMessage(ImportStandardizationVariantAction.class, "LBL_Action_Import_Standardization_Variant");
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
                        fc.setFileFilter(new JarFileFilter()); // jar with variantDescriptor.xml
                        fc.setAcceptAllFileFilterUsed(false);
            
                        int returnVal = fc.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            EviewStandardizationVariantCookie cookie = activatedNodes[0].getCookie(EviewStandardizationVariantCookie.class);
                            EviewStandardizationDataTypeNode standardizationDataTypeNode = cookie.getEviewStandardizationDataTypeNode();
                            FileObject fobjStandDataType = standardizationDataTypeNode.getFileObject();

                            File jarFile = fc.getSelectedFile();

                            //ToDo Kevin/Ricardo/Shant
                            //Call util to get the variant, create sub folder, e.g. FR
                            //VariantDescriptor variantDescriptor = introspectVariant(JarFile jarFile);
                            //if (VariantDescriptor != null) {
                                String strVariant = "FR"; //variantDescriptor.getId();
                                FileObject fo = FileUtil.toFileObject(jarFile);
                                FileUtil.copyFile(fo, fobjStandDataType, fo.getName());                            
                                FileObject newVariantFolder = fobjStandDataType.getFileObject(strVariant);
                                if (newVariantFolder == null) {
                                    newVariantFolder = FileUtil.createFolder(fobjStandDataType, strVariant);
                                }
                            //}
                        }                          
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(ImportStandardizationVariantAction.class, "MSG_FAILED_To_Import_Standardization_Plugin")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {
                    }
                }
            });
        } catch (Exception ex) {
            mLog.severe(NbBundle.getMessage(ImportStandardizationVariantAction.class, "MSG_FAILED_To_Perform") + ex.getMessage());
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
        return new Class[]{ImportStandardizationVariantAction.class};
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
