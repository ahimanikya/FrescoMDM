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

import com.sun.mdm.multidomain.project.nodes.*;
import org.openide.ErrorManager;
import org.openide.util.actions.NodeAction;
import org.openide.util.actions.CookieAction;
import org.openide.util.HelpCtx;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;

import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.MultiDomainApplication;
//import com.sun.mdm.index.util.Logger;
import com.sun.mdm.multidomain.util.Logger;

/**
 * Implements the methods for the Open action.
 * 
 */
public class ApplicationEditAction extends NodeAction {

    /**
     * The log4j logger
     */
    private static final Logger mLog = Logger.getLogger(
            ApplicationEditAction.class.getName()
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
        return NbBundle.getMessage(ApplicationEditAction.class, "LBL_Action_Edit");
    }

    /**
     * Performs the cookie action for the selected node.
     *
     * @param activatedNodes data nodes that activate the elected action
     */
    public void performAction(final Node[] activatedNodes) {
        MultiDomainCookie cookie = activatedNodes[0].getCookie(MultiDomainCookie.class);
        final MultiDomainConfigurationFolderNode node = cookie.getMultiDomainFolderNode();
        try {
            MultiDomainApplication multiDomainApplication = (MultiDomainApplication) node.getProject();
            String path = multiDomainApplication.getProjectDirectory().getName();
            EditorMainApp editorMainApp = EditorMainApp.getInstance(path);
            if (editorMainApp == null) {
                editorMainApp = EditorMainApp.createInstance(path);
                editorMainApp.startEditorApp(multiDomainApplication);
            }
            // request focus
            editorMainApp.requestFocus();
        } catch (Exception e) {
            mLog.severe(NbBundle.getMessage (ApplicationEditAction.class, "MSG_FAILED_To_OPEN_CONFIG_EDITOR")); // NOI18N
            ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
            ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
        }
    }
    
    /**
     * Provides the associated cookie class.
     *
     * @return the list of cookie class
     */
    protected Class[] cookieClasses() {
        return new Class[]{ApplicationEditAction.class};
    }

    /**
     * Answers whether this action is enabled.
     *
     * @param activatedNodes data nodes that activate the elected action
     *
     * @return true
     */
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
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
}