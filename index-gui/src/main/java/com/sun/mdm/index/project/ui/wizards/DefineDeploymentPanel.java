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
import java.awt.Component;
import javax.swing.event.ChangeListener;


/** A single panel descriptor for a wizard.
 * You probably want to make a wizard iterator to hold it.
 *
 */
public class DefineDeploymentPanel implements WizardDescriptor.Panel {
    static String matchEngine;

    /** The visual component that displays this panel.
     * If you need to access the component from this class,
     * just use getComponent().
     */
    private DefineDeploymentVisualPanel mComponent;

    /** Create the wizard panel descriptor. */
    public DefineDeploymentPanel() {
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.

    /**
     *
     *@return DefineDeploymentVisualPanel
     */
    public Component getComponent() {
        if (mComponent == null) {
            mComponent = new DefineDeploymentVisualPanel(this);
        }

        return mComponent;
    }

    /**
     *@return HelpCtx
     */
    public HelpCtx getHelp() {
        return new HelpCtx("DefineEnvironment");
    }

    /**
     *@return if Next button should be enabled
     */
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;

        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    /** not implemented
     *@param l ChangeListener
     */
    public final void addChangeListener(ChangeListener l) {
    }

    /** not implemented
     *@param l ChangeListener
     */
    public final void removeChangeListener(ChangeListener l) {
    }

    /*
    private final Set listeners = new HashSet(1); // Set<ChangeListener>
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent() {
        Iterator it;
        synchronized (listeners) {
            it = new HashSet(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            ((ChangeListener)it.next()).stateChanged(ev);
        }
    }
     */

    // You can use a settings object to keep track of state.
    // Normally the settings object will be the WizardDescriptor,
    // so you can use WizardDescriptor.getProperty & putProperty
    // to store information entered by the user.

    /** reade user inputs from previous panels
     *
     *@param settings WizardDescriptor
     */
    public void readSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        String viewName = wiz.getProperty(Properties.PROP_TARGET_VIEW_NAME)
                             .toString();

        // set the visual
        getComponent();
        mComponent.setViewName(viewName);
    }

    /** Save user inputs
     *@param settings WizardDescriptor
     */
    public void storeSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;

        try {
            Properties.setDatabase(mComponent.getDatabase());
            wiz.putProperty(Properties.PROP_DATABASE, mComponent.getDatabase());
            matchEngine = mComponent.getMatchEngine();
            wiz.putProperty(Properties.PROP_MATCH_ENGINE, matchEngine);
            TemplateObjects.setMatchTypes(matchEngine);
            wiz.putProperty(Properties.PROP_DATE_FORMAT, mComponent.getDateFormat());
            wiz.putProperty(Properties.PROP_TRANSACTION, mComponent.getTransaction());
            wiz.putProperty(Properties.PROP_MASTER_INDEX_EDM, mComponent.getMasterIndexEDM());
        } catch (Exception e) {
            wiz.putProperty(Properties.PROP_DATABASE, "");
            wiz.putProperty(Properties.PROP_MATCH_ENGINE, "");
            wiz.putProperty(Properties.PROP_DATE_FORMAT, "");
            wiz.putProperty(Properties.PROP_TRANSACTION, "");
            wiz.putProperty(Properties.PROP_MASTER_INDEX_EDM, "");
        }
    }
}
