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

import org.openide.util.actions.CookieAction;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.openide.nodes.Node;

/**
 * Implements the methods for the delete action.
 * 
 */
public class EviewGenerateAction extends CookieAction {

    /**
     * The log4j logger
     */
    private static final com.sun.mdm.index.util.Logger mLog = com.sun.mdm.index.util.Logger.getLogger(
            EviewGenerateAction.class.getName()
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
        return "Generate";
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
                    }
                });

        } catch (Exception ex) {
            mLog.severe("Failed to act.." + ex.getMessage());
        }           
    }

    /**
     * Provides the associated cookie class.
     *
     * @return the list of cookie class
     */
    protected Class[] cookieClasses() {
        return new Class[]{EviewGenerateAction.class};
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
}
