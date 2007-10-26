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

import org.netbeans.api.project.Project;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

import java.text.MessageFormat;


/** A wizard descriptor.
 *
 */
public class EVIEWDescriptor extends WizardDescriptor {
    private final EVIEWIterator iterator;
    private java.awt.Image mSideBar = Utilities.loadImage(
            "com/sun/mdm/index/project/ui/wizards/resources/images/WizardSide.jpg");
    private Project mProject = null;

    /** Make a descriptor suited to use EVIEWIterator.
     * Sets up various wizard properties to follow recommended
     * style guidelines.
     */
    public EVIEWDescriptor() {
        this(new EVIEWIterator());
    }

    private EVIEWDescriptor(EVIEWIterator iterator) {
        super(iterator);
        this.iterator = iterator;

        // Set title for the dialog:
        setTitleFormat(new MessageFormat("{0}"));

        setTitle(NbBundle.getMessage(EVIEWDescriptor.class, "TITLE_wizard"));

        // Make the left pane appear:
        putProperty("WizardPanel_autoWizardStyle", Boolean.TRUE); // NOI18N

        // Make the left pane show list of steps etc.:
        putProperty("WizardPanel_contentDisplayed", Boolean.TRUE); // NOI18N

        // Number the steps.
        putProperty("WizardPanel_contentNumbered", Boolean.TRUE); // NOI18N

        putProperty("WizardPanel_image", // NOI18N
            mSideBar); // NOI18N
    }

    /** Called when user moves forward or backward etc.:
     */
    protected void updateState() {
        Object option = this.getValue();
        if (option == null || !option.equals(WizardDescriptor.CANCEL_OPTION)) {
            super.updateState();
        }
        putProperty("WizardPanel_contentData", iterator.getSteps()); // NOI18N
        putProperty("WizardPanel_contentSelectedIndex",
            new Integer(iterator.getIndex())); // NOI18N
    }

    /** set Project for checking existing view
     *@param proj Project that invoke this wizard
     */
    public void setProject(Project proj) {
        mProject = proj;
    }

    /** get Project for checking existing view
     *@return Project that invoke this wizard
     */
    public Project getProject() {
        return mProject;
    }
}
