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

import com.sun.mdm.multidomain.project.EjbProjectManager;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.project.nodes.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.openide.ErrorManager;
import org.openide.util.actions.CookieAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.nodes.Node;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.netbeans.api.project.Project;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import com.sun.mdm.multidomain.util.Logger;
import java.util.ArrayList;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;


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
            javax.swing.SwingUtilities.invokeLater (new Runnable () {
                public void run() {
                    JFileChooser fc = new JFileChooser();
                    try {
                        fc.setMultiSelectionEnabled(false);
                        FileFilter fileFilter = new RelationsipPlugInFilter();
                        fc.setFileFilter(fileFilter); // look for relationship plug-in
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
    
    private void AddPlugInJarToEjbProject(File plugInJarFile, Node activatedNode  ) 
            throws IOException, FileNotFoundException, Exception{
        
//        File file = fc.getSelectedFile();
//        MultiDomainPlugInsCookieImpl cookie = 
//                activatedNodes[0].getCookie(MultiDomainPlugInsCookieImpl.class);
        MultiDomainPlugInsCookieImpl cookie = 
                activatedNode.getCookie(MultiDomainPlugInsCookieImpl.class);
        final MultiDomainPlugInsFolderNode node = cookie.getMultiDomainPlugInsFolderNode();
        FileObject plugInDir = node.getFileObject();
        File mdProjectFile = FileUtil.toFile(plugInDir).getParentFile().getParentFile();
        FileObject mdProjectDir = FileUtil.toFileObject(FileUtil.normalizeFile(mdProjectFile));
        Project mdProject = ProjectManager.getDefault().findProject(mdProjectDir);
        AntProjectHelper aph = (AntProjectHelper) mdProject.getLookup().lookup(AntProjectHelper.class);
        EditableProperties ep = aph.getProperties(AntProjectHelper.PROJECT_PROPERTIES_PATH);
        String ejbProjectLocation = ep.getProperty(MultiDomainProjectProperties.EJB_DIR);
        File ejbProjectDir = new File(mdProjectFile.getCanonicalPath() + 
                File.separator + ejbProjectLocation);
        if (!ejbProjectDir.exists() || plugInJarFile ==null) {
            DialogDisplayer.getDefault().notify(
                new NotifyDescriptor.Message(
                    NbBundle.getMessage(ImportRelationshipPluginAction.class, 
                        "MSG_FAILED_To_Perform_ImportRelationshipPluginAction"),
                    NotifyDescriptor.ERROR_MESSAGE));
            return;
        }
        ArrayList<String> libs = new ArrayList<String>();
        libs.add(plugInJarFile.getName());
        String relativeLocation = "../src/Plug-ins";
        EjbProjectManager.addLibsToEjbProject(ejbProjectDir, libs, relativeLocation ); 
    
    }
    
    private class RelationsipPlugInFilter extends FileFilter {
        
        public boolean accept(java.io.File file) {
            return ( file.isDirectory() || file.getName().endsWith(".jar") );
        }
        
        public String getDescription() {
            return NbBundle.getMessage(ImportRelationshipPluginAction.class, "MSG_Relationship_Plugin_Files");
        }
        
    }
    
}
