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
import org.openide.loaders.TemplateWizard;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import com.sun.mdm.index.project.EviewProjectGenerator;

import java.util.Enumeration;
import org.netbeans.api.project.ProjectManager;

/** A wizard iterator (sequence of panels).
 * Used to create a wizard. Create one or more
 * panels from template as needed too.
 *
 */
public class EVIEWIterator implements WizardDescriptor.InstantiatingIterator {
    private java.awt.Image mSideBar = Utilities.loadImage(
            "com/sun/mdm/index/project/ui/wizards/resources/images/WizardSide.jpg");
    private transient WizardDescriptor mWiz;
    private transient int mIndex = 0;
    private transient WizardDescriptor.Panel[] mPanels;
    private transient WizardDescriptor.Panel[] panels = null;

    // Also the list of steps in the left pane:
    private transient String[] steps = null;

    /** You should define what panels you want to use here:
     *
     *@return an array of Panels
     */
    protected WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[] {
            new PanelConfigureProject(getDefaultName()),
            new OpenPanel(), new DefineSourcePanel(),
            new DefineDeploymentPanel(), new DefineEntityPanel(),
            new FinishPanel()
        };
    }

    /** And the list of step names:
     *
     *@return steps in String array
     */
    protected String[] createSteps() {
        return new String[] {
            NbBundle.getMessage(EVIEWIterator.class, "LBL_NWP1_ProjectTitleName"), //NOI18N            
            NbBundle.getMessage(EVIEWIterator.class, "LBL_step_DefineView"),
            NbBundle.getMessage(EVIEWIterator.class, "LBL_step_DefineSource"),
            NbBundle.getMessage(EVIEWIterator.class,
                "LBL_step_DefineDeploymentEnvironment"),
            NbBundle.getMessage(EVIEWIterator.class, "LBL_step_DefineEntity"),
            NbBundle.getMessage(EVIEWIterator.class, "LBL_step_Finish")
        };
    }

    // --- The rest probably does not need to be touched. ---
    // Keep track of the panels and selected panel:
    // Also package-accessible to descriptor:

    /**
     *@return panel index
     */
    protected final int getIndex() {
        return mIndex;
    }

    /**
     *@return Panels
     */
    protected final WizardDescriptor.Panel[] getPanels() {
        if (mPanels == null) {
            mPanels = createPanels();
        }

        return mPanels;
    }

    /** Also package-accessible to descriptor:
     *@return Steps
     */
    protected final String[] getSteps() {
        if (steps == null) {
            steps = createSteps();
        }

        return steps;
    }

    // --- WizardDescriptor.Iterator METHODS: ---
    // Note that this is very similar to WizardDescriptor.Iterator, but with a
    // few more options for customization. If you e.g. want to make panels appear
    // or disappear dynamically, go ahead.

    /**
     *@return title
     */
    public String name() {
        return NbBundle.getMessage(EVIEWIterator.class, "TITLE_x_of_y",
            new Integer(mIndex + 1), new Integer(getPanels().length));
    }

    /**
     *@return if next panel is available
     */
    public boolean hasNext() {
        return mIndex < (getPanels().length - 1);
    }

    /**
     *@return if previous panel is available
     */
    public boolean hasPrevious() {
        return mIndex > 0;
    }

    /** if next panel is available, increment the current panel index
     */
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        mIndex++;
    }

    /** if previous panel is available, decrement the current panel index
     */
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }

        mIndex--;
    }

    /**
     *@return current panel
     */
    public WizardDescriptor.Panel current() {
        if ((mIndex == 2) && ((OpenPanel) mPanels[1]).mForcedStay) {
            ((OpenPanel) mPanels[1]).mForcedStay = false;
            mIndex = 1;
        }

        if ((mIndex == 5) && ((DefineEntityPanel) mPanels[4]).mForcedStay) {
            ((DefineEntityPanel) mPanels[4]).mForcedStay = false;
            mIndex = 4;
        }
        return getPanels()[mIndex];
    }

    // If nothing unusual changes in the middle of the wizard, simply:

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

    // If something changes dynamically (besides moving between panels),
    // e.g. the number of panels changes in response to user input, then
    // uncomment the following and call when needed:
    // fireChangeEvent();

    /*
    private transient Set listeners = new HashSet(1); // Set<ChangeListener>
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
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new HashSet(1);
    }
     */

    /**
     * @see org.openide.loaders.TemplateWizard.Iterator#initialize
     * (org.openide.loaders.TemplateWizard)
     */
    public void initialize(WizardDescriptor wiz) {    
    //public void initialize(TemplateWizard wiz) {
        JComponent jc;
        mWiz = wiz;

        // set left pane to SeeBeyond's graphic
        wiz.putProperty("WizardPanel_image", // NOI18N
            mSideBar); // NOI18N

        //
        mIndex = 0;
        mPanels = createPanels();
        steps = createSteps();

        for (int i = 0; i < mPanels.length; i++) {
            jc = (JComponent) mPanels[i].getComponent();

            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = jc.getName();
            }

            //jc.putClientProperty("WizardPanel_contentNumbered", Boolean.FALSE);
            // Step #.
            jc.putClientProperty("WizardPanel_contentSelectedIndex",
                new Integer(i));

            // NOI18N
            // Step name (actually the whole list for reference).
            jc.putClientProperty("WizardPanel_contentData", steps);

            // NOI18N
        }
    }

    /**
     * @see org.openide.loaders.TemplateWizard.Iterator#uninitialize
     * (org.openide.loaders.TemplateWizard)
     */
    public void uninitialize(TemplateWizard wiz) {
        mWiz = null;
        mPanels = null;
    }

    /**
     * @see org.openide.loaders.TemplateWizard.Iterator#instantiate
     * (org.openide.loaders.TemplateWizard)
     */
    public Set instantiate(TemplateWizard wiz) throws IOException {
        return instantiate((WizardDescriptor) wiz);
    }

    /**
     * Instantiates the Wizard
     *
     * @see org.openide.loaders.TemplateWizard.Iterator#instantiate
     * (org.openide.loaders.WizardDescriptor)
     */
    public Set instantiate(WizardDescriptor wiz) throws IOException {
        // When Finish button is pressed...
        // Generate the configuration files -
        // eIndex50.xml, elephantGUI.xml and runtimeconfig.xml
        //
        return Collections.EMPTY_SET;
    }
    
    public Set instantiate() throws IOException {
        Set resultSet = new LinkedHashSet();
        File dirF = (File) this.mWiz.getProperty(WizardProperties.PROJECT_DIR);
        //String mainProjectName = (String) this.mWiz.getProperty(WizardProperties.NAME);
        //String j2eeLevel = (String) this.mWiz.getProperty(WizardProperties.J2EE_LEVEL);
        
        //createProject(dirF, mainProjectName, j2eeLevel);
        EviewProjectGenerator.createProject(mWiz);
        FileObject dir = FileUtil.toFileObject(dirF);
        
        resultSet.add(dir);
        
        // Look for nested projects to open as well:

        Enumeration e = dir.getFolders(true);
        while (e.hasMoreElements()) {
            FileObject subfolder = (FileObject) e.nextElement();
            if (ProjectManager.getDefault().isProject(subfolder)) {
                resultSet.add(subfolder);
            }
        }


        // Returning set of FileObject of project diretory. 
        // Project will be open and set as main
        return resultSet;
    }
    
    public void uninitialize(WizardDescriptor wiz) {
        this.mWiz.putProperty(WizardProperties.PROJECT_DIR,null);
        this.mWiz.putProperty(WizardProperties.NAME,null);
        this.mWiz = null;
        panels = null;
    }
    
    protected String getDefaultName() {
        return NbBundle.getMessage(EVIEWIterator.class, "LBL_NPW1_DefaultProjectName"); //NOI18N        
    }
}
