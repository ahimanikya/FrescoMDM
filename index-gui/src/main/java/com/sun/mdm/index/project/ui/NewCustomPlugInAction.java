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
import org.openide.nodes.Node;
import org.openide.nodes.FilterNode;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

/**
 * Implements the methods for the delete action.
 * 
 */
public class NewCustomPlugInAction extends CookieAction {

    /**
     * The log4j logger
     */
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            NewCustomPlugInAction.class.getName()
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
        return "New...";
    }


    /**
     * Performs the cookie action for the selected node.
     *
     * @param activatedNodes data nodes that activate the elected action
     */
    public void performAction(final Node[] activatedNodes) {
        try {
                RequestProcessor.getDefault().post(
                new Runnable() {
                    public void run() {
                        //Add Custom Plug-in Node here
                        try {
                            String newCustomPlugInName = getnewCustomPlugInName();
                            if (newCustomPlugInName != null) {
                                FilterNode ch = (FilterNode) activatedNodes[0];
                                String name = ch.getName();
                                

                                //EviewCustomPlugInsFolderNode node = (EviewCustomPlugInsFolderNode) n1; //activatedNodes[0];
                                
                                //EviewCustomPlugInsFolderNode node = (EviewCustomPlugInsFolderNode) ch.getCookie(EviewCustomPlugInsFolderNode.class);

                                //FileObject fo = node.getFileObject();
                                //fo.createData(newCustomPlugInName + ".java");
                            }
                        } catch (Exception e) {
                            mLog.log(java.util.logging.Level.SEVERE, "Failed to add Custom Plug-in Node");
                            ErrorManager.getDefault().log(ErrorManager.ERROR, e.getMessage());
                            ErrorManager.getDefault().notify(ErrorManager.ERROR, e);
                        } finally {
                        }
                    }
                });

        } catch (Exception ex) {
            mLog.log(java.util.logging.Level.SEVERE, "Failed to act.." + ex.getMessage());
        }           
    }

    /**
     * Provides the associated cookie class.
     *
     * @return the list of cookie class
     */
    protected Class[] cookieClasses() {
        return new Class[]{NewCustomPlugInAction.class};
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

    private String getnewCustomPlugInName() {
        String newname = null;
        NotifyDescriptor.InputLine dlg =
            new NotifyDescriptor.InputLine(
                    "Name",
                    "Input Custom Plug-in Name");
        dlg.setInputText("");
        if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(dlg))) {
            try {
                newname = dlg.getInputText();
                if ((newname != null) && (newname.trim().length() > 0)) {
                    return newname;
                }
            } catch (IllegalArgumentException e) {
                mLog.log(java.util.logging.Level.SEVERE, e.toString());
            }
        }
        return newname;
    }
}
