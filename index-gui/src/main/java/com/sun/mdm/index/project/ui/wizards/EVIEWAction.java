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
package com.sun.mdm.index.project.ui.wizards;

import org.openide.ErrorManager;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.NodeAction;

import java.awt.Dialog;
import javax.swing.SwingUtilities;


/** Start a wizard.
 *
 */
public class EVIEWAction extends NodeAction {
    /**
     * Action for bringing up eView Wizard
     */
    @Override
    public void performAction() {
        final WizardDescriptor desc = new EVIEWDescriptor();
        final Dialog dlg = DialogDisplayer.getDefault().createDialog(desc);

        // The following assumes the wizard descriptor is modal:

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    dlg.show();
                }
            });
        } catch (Exception e) { // InterruptedException, InvocationTargetException
            ErrorManager.getDefault().notify(e);
            return;
        }
        if (desc.getValue() == WizardDescriptor.FINISH_OPTION) {
            // Do whatever it is you want to do here.
            // User finished the wizard // NOI18N
        } else {
            // User closed or cancelled the wizard // NOI18N
        }

        // If you are using a nonmodal wizard, try this instead:
        desc.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent ev) {
                    if (WizardDescriptor.PROP_VALUE.equals(ev.getPropertyName())) {
                        desc.removePropertyChangeListener(this);

                        if (WizardDescriptor.FINISH_OPTION.equals(
                                    ev.getNewValue())) {
                            // Do whatever it is you want to do here.
                            // Remember you are in the Swing event thread here, so be careful!
                            // You might use RequestProcessor.postRequest(Runnable) if you
                            // plan to do something other than manipulate the GUI.
                            // User finished the wizard // NOI18N
                        } else {
                            // User closed or cancelled the wizard // NOI18N
                        }
                    }
                }
            });
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //alaska dlg.show();
                }
            });

        // The action will exit at this point.
    }

    /**
     *@return name of the action
     */
    public String getName() {
        return NbBundle.getMessage(EVIEWAction.class, "LBL_Action");
    }

    /**
     *@return resource URL
     */
    protected String iconResource() {
        return "com/sun/mdm/index/project/ui/wizards/EVIEWActionIcon.gif";
    }

    /** getHelpCtx
     *@return default help
     */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;

        // If you will provide context help then use:
        // return new HelpCtx(EVIEWAction.class);
    }

    /** always return true
     * @param node list of nodes
     * @return true if action is enabled
     */
    protected boolean enable(org.openide.nodes.Node[] node) {
        return true;
    }

    /** Bring up the wizard
     * @param node list of nodes
     */
    protected void performAction(org.openide.nodes.Node[] node) {
        performAction();
    }

    /** Perform extra initialization of this action's singleton.
     * PLEASE do not use constructors for this purpose!
     * protected void initialize() {
     * super.initialize();
     * putProperty(Action.SHORT_DESCRIPTION, NbBundle.getMessage(EVIEWAction.class, "HINT_Action"));
     * }
     */
}
