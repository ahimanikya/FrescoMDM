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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.openide.nodes.*;
import org.openide.util.*;
import org.openide.util.actions.SystemAction;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.SubprojectProvider;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.PropertyEvaluator;
import org.netbeans.spi.project.support.ant.ReferenceHelper;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.ProjectSensitiveActions;
import org.netbeans.spi.java.project.support.ui.BrokenReferencesSupport;
import org.netbeans.modules.compapp.projects.base.ui.customizer.IcanproProjectProperties;
import org.netbeans.modules.compapp.projects.base.ui.IcanproLogicalViewProvider;
import org.netbeans.modules.compapp.projects.base.IcanproConstants;
import com.sun.mdm.index.project.EviewProject;
import org.openide.loaders.DataFolder;
import org.openide.util.lookup.Lookups;
import org.openide.loaders.DataFolder;

/**
 * Support for creating logical views.
 */
public class EviewProjectLogicalViewProvider implements LogicalViewProvider {

    private final Project project;
    private final AntProjectHelper helper;
    private final PropertyEvaluator evaluator;
    private final SubprojectProvider spp;
    private final ReferenceHelper resolver;
    private Node rootnode;


    public EviewProjectLogicalViewProvider(Project project, AntProjectHelper helper, PropertyEvaluator evaluator, SubprojectProvider spp, ReferenceHelper resolver) {
        this.project = project;
        assert project != null;
        this.helper = helper;
        assert helper != null;
        this.evaluator = evaluator;
        assert evaluator != null;
        this.spp = spp;
        assert spp != null;
        this.resolver = resolver;
    }

    public Node createLogicalView() {
        rootnode = new eViewLogicalViewRootNode();
        return rootnode;
    }
    
    public Node getRootNode () {
        return rootnode;
    }

    public Node findPath( Node root, Object target ) {
        // XXX implement
        return null;
    }

   private static Lookup createLookup( Project project ) {
        DataFolder rootFolder = DataFolder.findFolder( project.getProjectDirectory() );
        // XXX Remove root folder after FindAction rewrite
        return Lookups.fixed( new Object[] { project, rootFolder } );
    }

    // Private innerclasses ----------------------------------------------------

    private static final String[] BREAKABLE_PROPERTIES = new String[] {
        IcanproProjectProperties.JAVAC_CLASSPATH,
        IcanproProjectProperties.DEBUG_CLASSPATH,
        IcanproProjectProperties.SRC_DIR,
    };

    public static boolean hasBrokenLinks(AntProjectHelper helper, ReferenceHelper resolver) {
        return BrokenReferencesSupport.isBroken(helper, resolver, BREAKABLE_PROPERTIES,
            new String[] {IcanproProjectProperties.JAVA_PLATFORM});
    }

    /** Filter node containin additional features for the J2SE physical
     */
    private final class eViewLogicalViewRootNode extends AbstractNode {

        private Action brokenLinksAction;
        private boolean broken;


        public eViewLogicalViewRootNode() {
            super( new EviewProjectViews.LogicalViewChildren( helper, evaluator, project ), createLookup( project ) );
            setIconBaseWithExtension("com/sun/mdm/index/project/ui/resources/eviewproProjectIcon.png" ); // NOI18N
            setName( ProjectUtils.getInformation( project ).getDisplayName() );
            if (hasBrokenLinks(helper, resolver)) {
                broken = true;
                brokenLinksAction = new BrokenLinksAction();
            }
        }
        
        @Override
        public Action[] getActions( boolean context ) {
            if ( context )
                return super.getActions( true );
            else
                return getAdditionalActions();
        }

        @Override
        public boolean canRename() {
            return false;
        }

        // Private methods -------------------------------------------------

        private Action[] getAdditionalActions() {

            ResourceBundle bundle = NbBundle.getBundle(IcanproLogicalViewProvider.class);

            return new Action[] {
                // disable new action at the top...
                // CommonProjectActions.newFileAction(),
                // null,
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_BUILD, bundle.getString( "LBL_BuildAction_Name" ), null ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_REBUILD, bundle.getString( "LBL_RebuildAction_Name" ), null ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_CLEAN, bundle.getString( "LBL_CleanAction_Name" ), null ), // NOI18N
                null,
                //ProjectSensitiveActions.projectCommandAction( EviewProject.COMMAND_GENWSDL, "Generate WSDL", null ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( EviewProject.COMMAND_GENEVIEW, NbBundle.getMessage(EviewProjectLogicalViewProvider.class, "LBL_Generate_Master_Index_Files"), null ),
                ProjectSensitiveActions.projectCommandAction( EviewProject.COMMAND_GENLOADER, NbBundle.getMessage(EviewProjectLogicalViewProvider.class, "LBL_Generate_Loader_Zip"), null ),
                null,
                ProjectSensitiveActions.projectCommandAction( IcanproConstants.COMMAND_REDEPLOY, bundle.getString( "LBL_RedeployAction_Name" ), null ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( IcanproConstants.COMMAND_DEPLOY, bundle.getString( "LBL_DeployAction_Name" ), null ), // NOI18N
                null,
                CommonProjectActions.setAsMainProjectAction(),
                CommonProjectActions.openSubprojectsAction(),
                CommonProjectActions.closeProjectAction(),
                null,
                SystemAction.get( org.openide.actions.FindAction.class ),
                null,
                SystemAction.get(org.openide.actions.OpenLocalExplorerAction.class),
                null,
                brokenLinksAction,
                CommonProjectActions.customizeProjectAction(),
            };
        }

        /** This action is created only when project has broken references.
         * Once these are resolved the action is disabled.
         */
        private class BrokenLinksAction extends AbstractAction implements PropertyChangeListener {

            public BrokenLinksAction() {
                evaluator.addPropertyChangeListener(this);
                putValue(Action.NAME, NbBundle.getMessage(IcanproLogicalViewProvider.class, "LBL_Fix_Broken_Links_Action"));
            }

            public void actionPerformed(ActionEvent e) {
                BrokenReferencesSupport.showCustomizer(helper, resolver, BREAKABLE_PROPERTIES, new String[]{IcanproProjectProperties.JAVA_PLATFORM});
                if (!hasBrokenLinks(helper, resolver)) {
                    disable();
                }
            }

            public void propertyChange(PropertyChangeEvent evt) {
                if (!broken) {
                    disable();
                    return;
                }
                broken = hasBrokenLinks(helper, resolver);
                if (!broken) {
                    disable();
                }
            }

            private void disable() {
                broken = false;
                setEnabled(false);
                evaluator.removePropertyChangeListener(this);
                fireIconChange();
                fireOpenedIconChange();
            }

        }

    }

    /** Factory for project actions.<BR>
     * XXX This class is a candidate for move to org.netbeans.spi.project.ui.support
     */
    public static class Actions {

        private Actions() {} // This is a factory

        public static Action createAction( String key, String name, boolean global ) {
            return new ActionImpl( key, name, global ? Utilities.actionsGlobalContext() : null );
        }

        private static class ActionImpl extends AbstractAction implements ContextAwareAction {

            Lookup context;
            String name;
            String command;

            public ActionImpl( String command, String name, Lookup context ) {
                super( name );
                this.context = context;
                this.command = command;
                this.name = name;
            }

            public void actionPerformed( ActionEvent e ) {

                Project project = context.lookup( Project.class );
                ActionProvider ap = project.getLookup().lookup( ActionProvider.class);

                ap.invokeAction( command, context );

            }

            public Action createContextAwareInstance( Lookup lookup ) {
                return new ActionImpl( command, name, lookup );
            }
        }

    }

}
