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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.openide.nodes.*;
import org.openide.util.*;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.loaders.DataFolder;

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
//import org.netbeans.modules.compapp.projects.base.ui.IcanproLogicalViewProvider;


//import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
/**
 * Support for creating logical views.
 */
public class MultiDomainProjectLogicalViewProvider implements LogicalViewProvider {

    private final Project project;
    private final AntProjectHelper helper;
    private final PropertyEvaluator evaluator;
    private final SubprojectProvider spp;
    private final ReferenceHelper resolver;
    private Node rootnode;
    private String oldName;

    public MultiDomainProjectLogicalViewProvider(Project project, AntProjectHelper helper, PropertyEvaluator evaluator, SubprojectProvider spp, ReferenceHelper resolver) {
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
        rootnode = new multiDomainLogicalViewRootNode();
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
        MultiDomainProjectProperties.JAVAC_CLASSPATH,
        MultiDomainProjectProperties.DEBUG_CLASSPATH,
        MultiDomainProjectProperties.SRC_DIR,
    };

    public static boolean hasBrokenLinks(AntProjectHelper helper, ReferenceHelper resolver) {
        return BrokenReferencesSupport.isBroken(helper, resolver, BREAKABLE_PROPERTIES,
            new String[] {MultiDomainProjectProperties.JAVA_PLATFORM});
    }

    /** Filter node containin additional features for the J2SE physical
     */
    private final class multiDomainLogicalViewRootNode extends AbstractNode {

        private Action brokenLinksAction;
        private boolean broken;       

        public multiDomainLogicalViewRootNode() {
            super( new MultiDomainProjectViews.LogicalViewChildren( helper, evaluator, project ), createLookup( project ) );
            setIconBaseWithExtension("com/sun/mdm/multidomain/project/resources/MultiDomainMDMProjectIcon.png" ); // NOI18N
            String name = ProjectUtils.getInformation( project ).getDisplayName();
            this.setDisplayName(name);
            name = ProjectUtils.getInformation( project ).getName();
            setName( name );
            if (hasBrokenLinks(helper, resolver)) {
                broken = true;
                brokenLinksAction = new BrokenLinksAction();
            }
            final Node node = this;
            oldName = node.getName();
            this.addNodeListener(new NodeAdapter() {
                @Override
                public void propertyChange(PropertyChangeEvent ev) {
                    if (ev.getPropertyName().equals(Node.PROP_NAME)) {
                        String newName = node.getName();
                        //if (!oldName.equals(newName) && !com.sun.mdm.index.project.ui.applicationeditor.EntityNode.checkNodeNameValue(newName)) {
                            node.setName(oldName);
                        //} else {
                            //DefaultProjectOperations.performDefaultRenameOperation(project, newName);
                        //}
                        oldName = node.getName();
                    }
                }
            });
        }
        
        @Override
        public boolean canRename() {
            return false;
        }

        @Override
        public Action[] getActions( boolean context ) {
            if ( context )
                return super.getActions( true );
            else
                return getAdditionalActions();
        }

        // Private methods -------------------------------------------------

        private Action[] getAdditionalActions() {
            return new Action[] {
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_BUILD, NbBundle.getMessage(MultiDomainProjectLogicalViewProvider.class, "LBL_BuildAction"  ), null  ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_REBUILD, NbBundle.getMessage(MultiDomainProjectLogicalViewProvider.class, "LBL_RebuildAction"  ), null  ), // NOI18N
                ProjectSensitiveActions.projectCommandAction( ActionProvider.COMMAND_CLEAN, NbBundle.getMessage(MultiDomainProjectLogicalViewProvider.class, "LBL_CleanAction"  ), null  ), // NOI18N
                null,
                ProjectSensitiveActions.projectCommandAction( MultiDomainProject.COMMAND_GENEVIEW, NbBundle.getMessage(MultiDomainProjectLogicalViewProvider.class, "LBL_GenerateAction"), null  ),
                null,
                ProjectSensitiveActions.projectCommandAction( MultiDomainProject.COMMAND_DEPLOY, NbBundle.getMessage(MultiDomainProjectLogicalViewProvider.class, "LBL_DeployAction"  ), null  ), // NOI18N
                null,
                CommonProjectActions.setAsMainProjectAction(),
                CommonProjectActions.openSubprojectsAction(),
                CommonProjectActions.closeProjectAction(),
                //null,
                //CommonProjectActions.renameProjectAction(),
                //CommonProjectActions.moveProjectAction(),
                //CommonProjectActions.copyProjectAction(),
                //CommonProjectActions.deleteProjectAction(),
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
                //putValue(Action.NAME, NbBundle.getMessage(IcanproLogicalViewProvider.class, "LBL_Fix_Broken_Links_Action"));
            }

            public void actionPerformed(ActionEvent e) {
                BrokenReferencesSupport.showCustomizer(helper, resolver, BREAKABLE_PROPERTIES, new String[]{MultiDomainProjectProperties.JAVA_PLATFORM});
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
