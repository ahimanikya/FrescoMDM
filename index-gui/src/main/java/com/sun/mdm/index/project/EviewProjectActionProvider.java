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
package com.sun.mdm.index.project;

import java.awt.Dialog;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.ReferenceHelper;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.openide.util.Lookup;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;

import org.apache.tools.ant.module.api.support.ActionUtils;

import org.netbeans.modules.compapp.projects.base.ui.customizer.IcanproProjectProperties;
import org.netbeans.modules.compapp.projects.base.ui.NoSelectedServerWarning;
import org.netbeans.modules.compapp.projects.base.IcanproConstants;


/** Action provider of the Web project. This is the place where to do
 * strange things to Web actions. E.g. compile-single.
 */
class EviewProjectActionProvider implements ActionProvider {
    
    // Definition of commands

    // Commands available from Web project
    private static final String[] supportedActions = {
        COMMAND_BUILD, 
        COMMAND_CLEAN, 
        COMMAND_REBUILD, 
        //COMMAND_COMPILE_SINGLE,
        //EviewProject.COMMAND_GENWSDL,
        EviewProject.COMMAND_GENEVIEW,
        EviewProject.COMMAND_GENLOADER,
        //IcanproConstants.COMMAND_REDEPLOY,
        IcanproConstants.COMMAND_DEPLOY,
    };
    
    // Project
    EviewProject project;
    
    // Ant project helper of the project
    private AntProjectHelper antProjectHelper;
    private ReferenceHelper refHelper;
        
    /** Map from commands to ant targets */
    Map/*<String,String[]>*/ commands;
    
    public EviewProjectActionProvider(EviewProject project, AntProjectHelper antProjectHelper, ReferenceHelper refHelper) {
        commands = new HashMap();
        commands.put(COMMAND_BUILD, new String[] {"dist"}); // NOI18N
        commands.put(COMMAND_CLEAN, new String[] {"clean"}); // NOI18N
        commands.put(COMMAND_REBUILD, new String[] {"clean", "dist"}); // NOI18N
        //commands.put(EviewProject.COMMAND_GENWSDL, new String[] {"gen-wsdl"}); // NOI18N
        commands.put(EviewProject.COMMAND_GENEVIEW, new String[] {"gen-mdm-index-files"}); // NOI18N
        commands.put(EviewProject.COMMAND_GENLOADER, new String[] {"gen-loader-zip"}); // NOI18N
        //commands.put(IcanproConstants.COMMAND_REDEPLOY, new String[] {"run"}); // NOI18N
        commands.put(IcanproConstants.COMMAND_DEPLOY, new String[] {"run-deploy"}); // NOI18N

        this.antProjectHelper = antProjectHelper;
        this.project = project;
        this.refHelper = refHelper;
    }
    
    private FileObject findBuildXml() {
        return project.getProjectDirectory().getFileObject(project.getBuildXmlName ());
    }
    
    public String[] getSupportedActions() {
        return supportedActions;
    }
    
    public void invokeAction( String command, Lookup context ) throws IllegalArgumentException {
        Properties p = null;
        String[] targetNames = (String[])commands.get(command);
        //EXECUTION PART
        if (command.equals (IcanproConstants.COMMAND_DEPLOY) || command.equals (IcanproConstants.COMMAND_REDEPLOY)) {
            if (!isSelectedServer ()) {
                return;
            }
        } else {
            p = null;
            if (targetNames == null) {
                throw new IllegalArgumentException(command);
            }
        }
        
        try {
            FileObject fo = findBuildXml();
            ActionUtils.runTarget(fo, targetNames, p);
        } catch (IOException e) {
            ErrorManager.getDefault().notify(e);
        }
        
    }
    
    public boolean isActionEnabled( String command, Lookup context ) {
        return (findBuildXml() != null);
    }
    
    // Private methods -----------------------------------------------------
    
    private boolean isDebugged() {
        return false;
    }
    

    private boolean isSelectedServer () {
        String instance = antProjectHelper.getStandardPropertyEvaluator ().getProperty (IcanproProjectProperties.J2EE_SERVER_INSTANCE);
        boolean selected;
        if (instance != null) {
            selected = true;
        } else {
            // no selected server => warning
            String server = antProjectHelper.getStandardPropertyEvaluator ().getProperty (IcanproProjectProperties.J2EE_SERVER_TYPE);
            NoSelectedServerWarning panel = new NoSelectedServerWarning (server);

            Object[] options = new Object[] {
                DialogDescriptor.OK_OPTION,
                DialogDescriptor.CANCEL_OPTION
            };
            DialogDescriptor desc = new DialogDescriptor (panel,
                    NbBundle.getMessage (NoSelectedServerWarning.class, "CTL_NoSelectedServerWarning_Title"), // NOI18N
                true, options, options[0], DialogDescriptor.DEFAULT_ALIGN, null, null);
            Dialog dlg = DialogDisplayer.getDefault ().createDialog (desc);
            dlg.setVisible (true);
            if (desc.getValue() != options[0]) {
                selected = false;
            } else {
                instance = panel.getSelectedInstance ();
                selected = instance != null;
                if (selected) {
                    IcanproProjectProperties wpp = new IcanproProjectProperties (project, antProjectHelper, refHelper);
                    wpp.put (IcanproProjectProperties.J2EE_SERVER_INSTANCE, instance);
                    wpp.store ();
                }
            }
            dlg.dispose();            
        }
        return selected;
    }
    
}
