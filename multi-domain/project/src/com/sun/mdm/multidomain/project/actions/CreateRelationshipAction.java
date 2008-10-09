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


import org.openide.ErrorManager;
import org.openide.util.actions.CookieAction;
import org.openide.util.RequestProcessor;
import org.openide.util.NbBundle;
import org.openide.util.HelpCtx;
import org.openide.nodes.Node;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import javax.swing.JFileChooser;

import org.netbeans.api.progress.ProgressHandle;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;

/**
 *
 * @author kkao
 */
public class CreateRelationshipAction extends CookieAction {
    private ProgressHandle mLoadProgress = null;

    /**
     * The log4j logger
     */
    private static final Logger mLog = Logger.getLogger(
            CreateRelationshipAction.class.getName());

    @Override
    protected Class<?>[] cookieClasses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int mode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CreateRelationshipAction() {
    }

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
        return NbBundle.getMessage(CreateRelationshipAction.class, "LBL_Action_Add_Relationship");
    }
    
    public void perform(final EditorMainApp editorMainApp) {
        //editorMainApp.addRelationship(domain1, domain2);
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
                    } catch (Exception e) {
                        mLog.severe(NbBundle.getMessage(CreateRelationshipAction.class, "MSG_FAILED_To_Add_Relationship")); // NOI18N
                        ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                        ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                    } finally {

                    }
                }
            });
        } catch (Exception ex) {
            String msg = NbBundle.getMessage(CreateRelationshipAction.class, "MSG_FAILED_To_Perform_AddRelationshipAction") + ex.getMessage();
            mLog.info(msg);
            NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
            DialogDisplayer.getDefault().notify(desc);
        }
    }

}
