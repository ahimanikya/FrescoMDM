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

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

import org.netbeans.spi.java.project.support.ui.PackageView;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.PropertyEvaluator;

import org.netbeans.modules.compapp.projects.base.ui.customizer.IcanproProjectProperties;
import org.netbeans.modules.compapp.projects.base.ui.IcanproLogicalViewProvider;

import org.netbeans.modules.j2ee.dd.api.ejb.DDProvider;
//import org.netbeans.modules.j2ee.dd.api.ejb.EjbJar;
//import org.netbeans.modules.j2ee.spi.ejbjar.EjbJarImplementation;
//import org.netbeans.api.java.classpath.ClassPath;
//import org.netbeans.spi.java.classpath.ClassPathProvider;

import org.openide.filesystems.FileChangeListener;
import org.openide.util.actions.SystemAction;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import javax.swing.Action;
//import java.io.IOException;

import com.sun.mdm.index.project.EviewProjectProperties;

class EviewProjectViews {

    private EviewProjectViews() {
    }

    static final class LogicalViewChildren extends Children.Keys implements FileChangeListener {

        private static final String KEY_SOURCE_DIR = "srcDir"; // NOI18N
        private static final String KEY_DOC_BASE = "docBase"; //NOI18N
        private static final String KEY_EJBS = "ejbKey"; //NOI18N
        private static final String WEBSERVICES_DIR = "webservicesDir"; // NOI18N
        private static final String KEY_SETUP_DIR = "setupDir"; //NOI18N
        private static final String KEY_CONFIGURATION_DIR = "configurationDir"; // NOI18N
        private static final String KEY_DBSCRIPT_DIR = "dbscriptDir"; // NOI18N
        private static final String KEY_STANDARDIZATION_DIR = "standardizationDir"; // NOI18N
        private static final String KEY_STANDARDIZATION_DATA_TYPE_DIR = "standardizationDataTypeDir"; // NOI18N
        private static final String KEY_MATCH_ENGINE_DIR = "matchengineDir"; // NOI18N        
        private static final String KEY_CUSTOM_PLUG_INS_DIR = "custompluginsDir"; // NOI18N
        private static final String KEY_FILTER_DIR = "filterDir"; // NOI18N        
        private static final String CONFIGURATION_FOLDER_DISPLAY_NAME = "Configuration"; //NOI18N
        private static final String MATCH_ENGINE_FOLDER_DISPLAY_NAME = "Match Engine"; //NOI18N
        private static final String FILTER_FOLDER_DISPLAY_NAME = "Filter"; //NOI18N
        private static final String STANDARDIZATION_ENGINE_FOLDER_DISPLAY_NAME = "Standardization Engine"; //NOI18N
        private static final String DATABASE_SCRIPT_FOLDER_DISPLAY_NAME = "Database Script"; //NOI18N
        private static final String CUSTOM_PLUG_INS_FOLDER_DISPLAY_NAME = "Custom Plug-ins"; //NOI18N
        
        private AntProjectHelper helper;
        private final PropertyEvaluator evaluator;
        private FileObject projectDir;
        private Project project;

        public LogicalViewChildren (AntProjectHelper helper, PropertyEvaluator evaluator, Project project) {
            assert helper != null;
            this.helper = helper;
            projectDir = helper.getProjectDirectory();
            this.evaluator = evaluator;
            this.project = project;
        }

        @Override
        protected void addNotify() {
            super.addNotify();
            projectDir.addFileChangeListener(this);
            createNodes();
        }

        private void createNodes() {
            List<String> l = new ArrayList<String>();

            l.add(KEY_EJBS);

            /*
            DataFolder docBaseDir = getFolder(IcanproProjectProperties.META_INF);
            if (docBaseDir != null) {
                l.add(KEY_DOC_BASE);
            }
            */

            DataFolder srcDir = getFolder(IcanproProjectProperties.SRC_DIR);
            if (srcDir != null) {
                //l.add(KEY_SOURCE_DIR);
                l.add(KEY_CONFIGURATION_DIR);
                l.add(KEY_DBSCRIPT_DIR);
                l.add(KEY_STANDARDIZATION_DIR);
                //l.add(KEY_STANDARDIZATION_DATA_TYPE_DIR);
                l.add(KEY_MATCH_ENGINE_DIR);
                l.add(KEY_FILTER_DIR);
            }

            FileObject setupFolder = getSetupFolder();
            if (setupFolder != null && setupFolder.isFolder()) {
                l.add(KEY_SETUP_DIR);
            }

            l.add(WEBSERVICES_DIR);

            setKeys(l);
        }

        private FileObject getSetupFolder() {
            return projectDir.getFileObject("setup"); //NOI18N
        }

        @Override
        protected void removeNotify() {
            setKeys(Collections.EMPTY_SET);
            projectDir.removeFileChangeListener(this);
            super.removeNotify();
        }

        protected Node[] createNodes(Object key) {
            Node n = null;
            Node[] nodes = null;
            FileObject srcRoot = helper.resolveFileObject(evaluator.getProperty (IcanproProjectProperties.SRC_DIR));

            if (key == KEY_SOURCE_DIR) {
                Project p = FileOwnerQuery.getOwner (srcRoot);
                SourceGroup sgs [] = ProjectUtils.getSources (p).getSourceGroups (JavaProjectConstants.SOURCES_TYPE_JAVA);
                for (int i = 0; i < sgs.length; i++) {
                    if (sgs [i].contains (srcRoot)) {
                        n = PackageView.createPackageView (sgs [i]);
                        break;
                    }
                }
            } else if (key == KEY_CONFIGURATION_DIR) {
                FileObject configurationFolder = srcRoot.getFileObject(EviewProjectProperties.CONFIGURATION_FOLDER);
                if (configurationFolder != null) {
                    n = new EviewConfigurationFolderNode(CONFIGURATION_FOLDER_DISPLAY_NAME, DataFolder.findFolder(configurationFolder), project);                                                
                }
            } else if (key == KEY_DBSCRIPT_DIR) {
                FileObject dbscriptFolder = srcRoot.getFileObject(EviewProjectProperties.DATABASE_SCRIPT_FOLDER);
                if (dbscriptFolder != null) {
                    n = new EviewDBScriptFolderNode(DATABASE_SCRIPT_FOLDER_DISPLAY_NAME, DataFolder.findFolder(dbscriptFolder));                                                
                }
            } else if (key == KEY_STANDARDIZATION_DIR) {
                FileObject standardizationEngineFolder = srcRoot.getFileObject(EviewProjectProperties.STANDARDIZATION_ENGINE_FOLDER);
                if (standardizationEngineFolder != null) {
                    n = new EviewStandardizationFolderNode(STANDARDIZATION_ENGINE_FOLDER_DISPLAY_NAME, DataFolder.findFolder(standardizationEngineFolder));
                    /*
                    FileObject[] children = standardizationEngineFolder.getChildren();
                    if (children != null && children.length > 0) {
                        nodes = new Node[children.length + 1];
                        nodes[0] = n;
                        for (int i=0; children != null && i < children.length; i++) {
                            nodes[i+1] = new EviewStandardizationDataTypeNode(children[i].getName(), DataFolder.findFolder(children[i]));
                        }
                    } */
                }
            } else if (key == KEY_MATCH_ENGINE_DIR) {
                FileObject matchEngineFolder = srcRoot.getFileObject(EviewProjectProperties.MATCH_ENGINE_FOLDER);
                if (matchEngineFolder != null) {
                    n = new EviewConfigurationFolderNode(MATCH_ENGINE_FOLDER_DISPLAY_NAME, DataFolder.findFolder(matchEngineFolder));                                                
                }
            } else if (key == KEY_FILTER_DIR) {
                FileObject filterFolder = srcRoot.getFileObject(EviewProjectProperties.FILTER_FOLDER);
                if (filterFolder != null) {
                    n = new EviewFilterFolderNode(FILTER_FOLDER_DISPLAY_NAME, DataFolder.findFolder(filterFolder));                                                
                }
            } else if (key == KEY_CUSTOM_PLUG_INS_DIR) {
                FileObject customPluginsFolder = srcRoot.getFileObject(EviewProjectProperties.CUSTOM_PLUG_INS_FOLDER);
                if (customPluginsFolder != null) {
                    n = new EviewCustomPlugInsFolderNode(CUSTOM_PLUG_INS_FOLDER_DISPLAY_NAME, projectDir, DataFolder.findFolder(customPluginsFolder));                                                
                }
            } else if (key == KEY_EJBS) {
                Project project = FileOwnerQuery.getOwner (srcRoot);
                DDProvider provider = DDProvider.getDefault();
//                EjbJarImplementation jp = project.getLookup().lookup(EjbJarImplementation.class);
//                if (jp != null) {
//                    EjbJar ejbJar = null;
//                    try {
//                        ejbJar = provider.getDDRoot(jp.getDeploymentDescriptor());
//                    } catch (IOException ioe) {
//                        //ErrorManager.getDefault().notify(ioe);
//                    }
//                    ClassPathProvider cpp = project.getLookup().lookup(ClassPathProvider.class);
//                    assert cpp != null;
//                    ClassPath classPath = cpp.findClassPath(srcRoot, ClassPath.SOURCE);
//                    //n = new EjbContainerNode(ejbJar, classPath);
//                    //Node nws =  new WebServicesNode(ejbJar, classPath);
//                }
            } 
            /*
             } else if (key == WEBSERVICES_DIR){
                //WebServicesView webServicesView = WebServicesView.getWebServicesView(srcRoot);
                //if(webServicesView != null) {
                //    n = webServicesView.createWebServicesView(srcRoot);
                //}
            } else if (key == KEY_DOC_BASE) {
                n = new DocBaseNode (getFolder(IcanproProjectProperties.META_INF).getNodeDelegate());
            } else if (key == KEY_SETUP_DIR) {
                try {
                    DataObject sdo = DataObject.find(getSetupFolder());
                    n = new ServerResourceNode(project); // sdo.getNodeDelegate());
                } catch (org.openide.loaders.DataObjectNotFoundException dnfe) {}
            } 
            */
            if (nodes != null) {
                return nodes;
            }
            return n == null ? new Node[0] : new Node[] {n};
        }

        private DataFolder getFolder(String propName) {
            FileObject fo = helper.resolveFileObject(evaluator.getProperty (propName));
            if (fo != null) {
                DataFolder df = DataFolder.findFolder(fo);
                return df;
            }
            return null;
        }

        // file change events in the project directory
        public void fileAttributeChanged(org.openide.filesystems.FileAttributeEvent fe) {
        }

        public void fileChanged(org.openide.filesystems.FileEvent fe) {
        }

        public void fileDataCreated(org.openide.filesystems.FileEvent fe) {
        }

        public void fileDeleted(org.openide.filesystems.FileEvent fe) {
            // setup folder deleted
           createNodes();
        }

        public void fileFolderCreated(org.openide.filesystems.FileEvent fe) {
            // setup folder could be created
            createNodes();
        }

        public void fileRenamed(org.openide.filesystems.FileRenameEvent fe) {
            // setup folder could be renamed
            createNodes();
        }
    }

    private static final class DocBaseNode extends FilterNode {
        private static Image CONFIGURATION_FILES_BADGE = Utilities.loadImage( "com/sun/jbi/ui/devtool/projects/icanpro/ui/resources/docjar.gif", true ); // NOI18N

        DocBaseNode (Node orig) {
            super (orig);
        }

        @Override
        public Image getIcon( int type ) {
            return computeIcon( false, type );
        }

        public Image getOpenedIcon( int type ) {
            return computeIcon( true, type );
        }

        private Image computeIcon( boolean opened, int type ) {
            Node folderNode = getOriginal();
            Image image = opened ? folderNode.getOpenedIcon( type ) : folderNode.getIcon( type );
            return Utilities.mergeImages( image, CONFIGURATION_FILES_BADGE, 7, 7 );
        }

        @Override
        public String getDisplayName () {
            return NbBundle.getMessage(IcanproLogicalViewProvider.class, "LBL_Node_DocBase"); //NOI18N
        }
        
        @Override
        public Action[] getActions( boolean context ) {
            return new Action[] {
                //SystemAction.get(NewCustomPlugInAction.class),
                //null,
                SystemAction.get(org.openide.actions.FindAction.class),
                null,
                SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
            };
        }
    }
}
