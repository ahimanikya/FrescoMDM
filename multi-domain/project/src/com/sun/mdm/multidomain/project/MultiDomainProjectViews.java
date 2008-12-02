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
package com.sun.mdm.multidomain.project;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;

import org.openide.loaders.DataFolder;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

import org.netbeans.spi.java.project.support.ui.PackageView;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.PropertyEvaluator;
//import org.netbeans.modules.j2ee.dd.api.ejb.DDProvider;

import org.openide.filesystems.FileChangeListener;
import org.openide.util.actions.SystemAction;
import org.openide.filesystems.FileObject;
import javax.swing.Action;

import com.sun.mdm.multidomain.project.nodes.MultiDomainDomainsFolderNode;
import com.sun.mdm.multidomain.project.nodes.MultiDomainConfigurationFolderNode;
import com.sun.mdm.multidomain.project.nodes.MultiDomainDBScriptFolderNode;
import com.sun.mdm.multidomain.project.nodes.MultiDomainPlugInsFolderNode;
//import com.sun.mdm.multidomain.project.editor.MiDataObject;
//import com.sun.mdm.multidomain.project.editor.MiDataLoader;
import org.openide.nodes.CookieSet;
import org.openide.loaders.DataObject;

class MultiDomainProjectViews {

    private MultiDomainProjectViews() {
    }

    static final class LogicalViewChildren extends Children.Keys implements FileChangeListener {

        private static final String KEY_SOURCE_DIR = "srcDir"; // NOI18N
        private static final String KEY_DOC_BASE = "docBase"; //NOI18N
        private static final String KEY_EJBS = "ejbKey"; //NOI18N
        private static final String WEBSERVICES_DIR = "webservicesDir"; // NOI18N
        private static final String KEY_SETUP_DIR = "setupDir"; //NOI18N
        private static final String KEY_CONFIGURATION_DIR = "configurationDir"; // NOI18N
        private static final String KEY_DOMAINS_DIR = "domainsDir"; // NOI18N
        private static final String KEY_DBSCRIPT_DIR = "dbscriptDir"; // NOI18N
        private static final String KEY_PLUGINS_DIR = "pluginsDir"; // NOI18N
        private static final String CONFIGURATION_FOLDER_DISPLAY_NAME = "Configuration"; //NOI18N
        private static final String DOMAINS_FOLDER_DISPLAY_NAME = "Domains"; //NOI18N
        private static final String DATABASE_SCRIPT_FOLDER_DISPLAY_NAME = "Database Script"; //NOI18N
        private static final String PLUGINS_FOLDER_DISPLAY_NAME = "Plug-ins"; //NOI18N
        
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

            DataFolder srcDir = getFolder(MultiDomainProjectProperties.SRC_DIR);
            if (srcDir != null) {
                l.add(KEY_CONFIGURATION_DIR);
                l.add(KEY_DOMAINS_DIR);
                l.add(KEY_DBSCRIPT_DIR);
                l.add(KEY_PLUGINS_DIR);
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
            FileObject srcRoot = helper.resolveFileObject(evaluator.getProperty (MultiDomainProjectProperties.SRC_DIR));

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
                FileObject configurationFolder = srcRoot.getFileObject(MultiDomainProjectProperties.CONFIGURATION_FOLDER);
                if (configurationFolder != null) {
                    n = new MultiDomainConfigurationFolderNode(CONFIGURATION_FOLDER_DISPLAY_NAME, DataFolder.findFolder(configurationFolder), project);
                }
            } else if (key == KEY_DOMAINS_DIR) {
                
                FileObject domainsFolder = srcRoot.getFileObject(MultiDomainProjectProperties.DOMAINS_FOLDER);
                if (domainsFolder != null) {
                    n = new MultiDomainDomainsFolderNode(DOMAINS_FOLDER_DISPLAY_NAME, DataFolder.findFolder(domainsFolder));
                }
            } else if (key == KEY_DBSCRIPT_DIR) {
                FileObject dbscriptFolder = srcRoot.getFileObject(MultiDomainProjectProperties.DATABASE_SCRIPT_FOLDER);
                if (dbscriptFolder != null) {
                    n = new MultiDomainDBScriptFolderNode(DATABASE_SCRIPT_FOLDER_DISPLAY_NAME, DataFolder.findFolder(dbscriptFolder));
                }
            } else if (key == KEY_PLUGINS_DIR) {
                FileObject pluginsFolder = srcRoot.getFileObject(MultiDomainProjectProperties.RELATIONSHIP_PLUGINS_FOLDER);
                if (pluginsFolder != null) {
                    n = new MultiDomainPlugInsFolderNode(PLUGINS_FOLDER_DISPLAY_NAME, DataFolder.findFolder(pluginsFolder));                                                
                }
            } else if (key == KEY_EJBS) {
                Project project = FileOwnerQuery.getOwner (srcRoot);
                //DDProvider provider = DDProvider.getDefault();
            } 
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
            return "LBL_Node_DocBase"; //NOI18N
        }
        
        @Override
        public Action[] getActions( boolean context ) {
            return new Action[] {
                SystemAction.get(org.openide.actions.FindAction.class),
                null,
                SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
            };
        }
    }
}
