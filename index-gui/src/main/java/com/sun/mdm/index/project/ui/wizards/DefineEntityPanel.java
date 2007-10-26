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

import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import java.awt.Component;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/** A single panel descriptor for a wizard.
 * You probably want to make a wizard iterator to hold it.
 *
 */
public class DefineEntityPanel implements WizardDescriptor.Panel {
    /** The visual component that displays this panel.
     * If you need to access the component from this class,
     * just use getComponent().
     */
    private DefineEntityVisualPanel component;
    private final Set listeners = new HashSet(1); // Set<ChangeListener>
    public boolean mForcedStay = false;

    /** Create the wizard panel descriptor. */
    public DefineEntityPanel() {
    }

    /** Get the visual component for the panel. In this template, the component
     * is kept separate. This can be more efficient: if the wizard is created
     * but never displayed, or not all panels are displayed, it is better to
     * create only those which really need to be visible.
     */
    /**
     *
     *@return DefineEntityVisualPanel
     */
    public Component getComponent() {
        if (component == null) {
            component = new DefineEntityVisualPanel(this);
        }

        return component;
    }

    /**
     *@return HelpCtx
     */
    public HelpCtx getHelp() {
        return new HelpCtx("DefineObjectStructure");
    }

    /**
     *@return if Next button should be enabled
     */
    public boolean isValid() {
        getComponent();

        return component.isEntityStructureValid();
    }

    /**
     *@param l ChangeListener
     */
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    /**
     *@param l ChangeListener
     */
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    /** triggered to check isValid()
     *
     */
    protected final void fireChangeEvent() {
        Iterator it;

        synchronized (listeners) {
            it = new HashSet(listeners).iterator();
        }

        ChangeEvent ev = new ChangeEvent(this);

        while (it.hasNext()) {
            ((ChangeListener) it.next()).stateChanged(ev);
        }
    }

    // You can use a settings object to keep track of state.
    // Normally the settings object will be the WizardDescriptor,
    // so you can use WizardDescriptor.getProperty & putProperty
    // to store information entered by the user.

    /** reade user inputs from previous panels
     *
     *@param settings WizardDescriptor
     */
    public void readSettings(Object settings) {
        getComponent();
        //WizardDescriptor wiz = (WizardDescriptor) settings;
        WizardDescriptor wiz = (WizardDescriptor) settings;
        String viewName = wiz.getProperty(Properties.PROP_TARGET_VIEW_NAME)
                             .toString();
        component.setViewName(viewName);
        //toDo component.setProject(wiz.getProject());
    }

    /** Save user inputs
     *@param settings WizardDescriptor
     */
    public void storeSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        Object option = wiz.getValue();
        if (!option.equals(WizardDescriptor.CANCEL_OPTION)) {
            try {
                String msg = component.validateEntityTree();
                if (!msg.equals("Success")) {
                    Toolkit.getDefaultToolkit().beep();
                    NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                    DialogDisplayer.getDefault().notify(desc);
                    mForcedStay = true;
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
