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

import org.openide.util.NbBundle;


/** A single panel for a wizard - the GUI portion.
 *
 */
public class FinishVisualPanel extends javax.swing.JPanel {
    /** The wizard panel descriptor associated with this GUI panel.
     * If you need to fire state changes or something similar, you can
     * use this handle to do so.
     */
    private final FinishPanel panel;
    javax.swing.JCheckBox mAutoGenerateOption = new javax.swing.JCheckBox(NbBundle.getMessage(DefineDeploymentVisualPanel.class,
                "LBL_Auto_Generate_Files"));

    /**
     * Create the wizard panel and set up some basic properties.
     *
     *@param panel FinishPanel
     */
    public FinishVisualPanel(FinishPanel panel) {
        this.panel = panel;
        this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(FinishVisualPanel.class, 
                "TITLE_FinishPanel")); // NOI18N
        initComponents();

        // Provide a name in the title bar.
        //setName(NbBundle.getMessage(FinishVisualPanel.class, "TITLE_FinishPanel"));

        /*
        // Optional: provide a special description for this pane.
        // You must have turned on WizardDescriptor.WizardPanel_helpDisplayed
        // (see descriptor in standard iterator template for an example of this).
        try {
            putClientProperty("WizardPanel_helpURL", // NOI18N
                new URL("nbresloc:/eview/src/java/com/sun/mdm/index/project/ui/wizards/FinishVisualHelp.html")); // NOI18N
        } catch (MalformedURLException mfue) {
            throw new IllegalStateException(mfue.toString());
        }
         */
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        setLayout(null);

        javax.swing.JLabel jLabelFinish = new javax.swing.JLabel();
        jLabelFinish.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Finish"));

        javax.swing.JLabel jLabelFinish2 = new javax.swing.JLabel();
        jLabelFinish2.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Finish2"));

        javax.swing.JLabel jLabelXmlConfiguration = new javax.swing.JLabel();
        jLabelXmlConfiguration.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Xml_Configuration"));
        
        javax.swing.JLabel jLabelXmlObject = new javax.swing.JLabel();
        jLabelXmlObject.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Xml_Object_Definition"));

        //javax.swing.JLabel jLabelXmlEDM = new javax.swing.JLabel();
        //jLabelXmlEDM.setText(NbBundle.getMessage(
        //        FinishVisualPanel.class, "MSG_Xml_EDM"));
        
        javax.swing.JLabel jLabelXmlMIDM = new javax.swing.JLabel();
        jLabelXmlMIDM.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Xml_MIDM"));
        
        javax.swing.JLabel jLabelXmlMIDMSecurity = new javax.swing.JLabel();
        jLabelXmlMIDMSecurity.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Xml_MIDM_Security"));

        javax.swing.JLabel jLabelXmlMefa = new javax.swing.JLabel();
        jLabelXmlMefa.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_Xml_MatchField_Configuration"));

        javax.swing.JLabel jLabelXmlMaster = new javax.swing.JLabel();
        jLabelXmlMaster.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_Xml_Master_Configuration"));

        javax.swing.JLabel jLabelXmlUpdate = new javax.swing.JLabel();
        jLabelXmlUpdate.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_Xml_Update_Configuration"));

        javax.swing.JLabel jLabelXmlQuery = new javax.swing.JLabel();
        jLabelXmlQuery.setText(NbBundle.getMessage(
                FinishVisualPanel.class, "MSG_Xml_Query_Configuration"));

        javax.swing.JLabel jLabelXmlMatchEngine = new javax.swing.JLabel();
        jLabelXmlMatchEngine.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_Xml_Validate_Configuration"));

        javax.swing.JLabel jLabelXmlSecurity = new javax.swing.JLabel();
        jLabelXmlSecurity.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_Xml_Security_Configuration"));

        javax.swing.JLabel jLabelDBScript = new javax.swing.JLabel();
        jLabelDBScript.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_DB_Script"));

        javax.swing.JLabel jLabelDBScriptSystems = new javax.swing.JLabel();
        jLabelDBScriptSystems.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_DB_Script_Systems"));

        javax.swing.JLabel jLabelDBScriptCodeList = new javax.swing.JLabel();
        jLabelDBScriptCodeList.setText(NbBundle.getMessage(
                FinishVisualPanel.class,
                "MSG_DB_Script_CodeList"));

        jLabelFinish.setBounds(0, 0, 400, 20);
        int yPos = 30;
        jLabelXmlConfiguration.setFont(new java.awt.Font("TimesRoman",java.awt.Font.BOLD,12));
        jLabelXmlConfiguration.setBounds(0, yPos, 400, 20);
            yPos += 20;
            jLabelXmlObject.setBounds(10, yPos, 400, 20);
            yPos += 20;
            //jLabelXmlEDM.setBounds(10, yPos, 400, 20);
            jLabelXmlMIDM.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlMIDMSecurity.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlMaster.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlMefa.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlQuery.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlUpdate.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlMatchEngine.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelXmlSecurity.setBounds(10, yPos, 400, 20);
            yPos += 20;
        jLabelDBScript.setFont(new java.awt.Font("TimesRoman",java.awt.Font.BOLD,12));
        jLabelDBScript.setBounds(0, yPos, 400, 20);
            yPos += 20;
            jLabelDBScriptSystems.setBounds(10, yPos, 400, 20);
            yPos += 20;
            jLabelDBScriptCodeList.setBounds(10, yPos, 400, 20);

        add(jLabelFinish);
        add(jLabelXmlConfiguration);
        add(jLabelXmlObject);
        //add(jLabelXmlEDM);
        add(jLabelXmlMIDM);
        add(jLabelXmlMIDMSecurity);
        add(jLabelXmlMaster);
        add(jLabelXmlMefa);
        add(jLabelXmlQuery);
        add(jLabelXmlUpdate);
        add(jLabelXmlMatchEngine);
        add(jLabelXmlSecurity);
        add(jLabelDBScript);
        add(jLabelDBScriptSystems);
        add(jLabelDBScriptCodeList);
        
        //mAutoGenerateOption.setSelected(true);
        yPos += 30;
        mAutoGenerateOption.getAccessibleContext().setAccessibleName(NbBundle.getMessage(FinishVisualPanel.class, 
                "LBL_Auto_Generate_Files")); // NOI18N
        mAutoGenerateOption.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(FinishVisualPanel.class, 
                "LBL_Auto_Generate_Files")); // NOI18N

        mAutoGenerateOption.setBounds(0, yPos, 400, 20);
        mAutoGenerateOption.setBorder(null);
        add(mAutoGenerateOption);
    }

    // Variables declaration - do not modify//variables
    // End of variables declaration//variables

    /** set view name for panel title
     *@param newViewName new view name
     */
    public void setViewName(String newViewName) {
        setName(NbBundle.getMessage(FinishVisualPanel.class, "TITLE_FinishPanel"));
    }
    
    public boolean getAutoGenerate() {
        return mAutoGenerateOption.isSelected();

    }
}
