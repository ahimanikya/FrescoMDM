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
package com.sun.mdm.index.project.ui.applicationeditor.standardization;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.parser.MatchFieldDef.NormalizationGroup;
import com.sun.mdm.index.parser.MatchFieldDef.LocaleCode;
import com.sun.mdm.index.project.ui.applicationeditor.EntityTree;
import com.sun.mdm.index.project.ui.applicationeditor.EntityTreeSelectionDialog;

public class NormalizedFieldEditDialog extends javax.swing.JDialog {
    EntityTree mEntityTree;
    private MatchFieldDef mMatchFieldDef;
    private NormalizationGroup mGroup;
    private JTable mTblLocaleCodes;
    boolean bModified = false;
    
    /** Creates new form NormalizedFieldEditDialog */
    public NormalizedFieldEditDialog(EntityTree entityTree, 
            MatchFieldDef matchFieldDef, 
            NormalizationGroup group,
            boolean editMode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mEntityTree = entityTree;
        mMatchFieldDef = matchFieldDef;
        mGroup = group;
        initComponents();

        cbStandardizationTypes.addItem("BusinessName");
        cbStandardizationTypes.addItem("PersonName");
        cbStandardizationTypes.setEnabled(!editMode);
        cbStandardizationTypes.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    loadFieldIDs(cbStandardizationTypes.getSelectedItem().toString());
                    enableOK();
                }
            });
        if (mGroup != null) {
            cbStandardizationTypes.setSelectedItem(mGroup.getStandardizationType());
        } else {
            cbStandardizationTypes.setSelectedIndex(0);
        }
        loadFieldIDs(cbStandardizationTypes.getSelectedItem().toString());
        
        this.cbDomainSelector.addItem(MatchFieldDef.MULTIPLE_DOMAIN_SELECTOR);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_AU);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_FR);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_UK);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_US);

        if (editMode) {
            this.cbDomainSelector.setSelectedItem(mGroup.getDomainSelector());
            String localeFieldName = mGroup.getLocaleFieldName();
            if (localeFieldName == null) {
                localeFieldName = "";
            }
            this.jTextFieldLocaleFieldName.setText(localeFieldName);

            this.jTextFieldUnnormalizedSource.setText(mGroup.getUnnormalizedSourceFieldName());
            this.cbUnnormalizedFieldID.setSelectedItem(mGroup.getUnnormalizedFieldId());
            this.jTextFieldStandardizedObject.setText(mGroup.getNormalizedSourceFieldName());
            this.cbNormalizedFieldID.setSelectedItem(mGroup.getNormalizedFieldId());
        } else {
            this.cbDomainSelector.setSelectedItem(MatchFieldDef.MULTIPLE_DOMAIN_SELECTOR);
        }
        
        this.cbDomainSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    enableOK();
            }
        });
        this.jTextFieldUnnormalizedSource.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });       
        this.jTextFieldStandardizedObject.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });
        this.cbUnnormalizedFieldID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    enableOK();
            }
        });
        this.cbNormalizedFieldID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    enableOK();
            }
        });
        loadLocaleCodesTable(editMode);

    }
    
    private void loadFieldIDs(String standardizationType) {
        this.cbUnnormalizedFieldID.removeAllItems();
        this.cbNormalizedFieldID.removeAllItems();
        
        if (standardizationType.equals("Address")) {
            this.cbUnnormalizedFieldID.addItem("HouseNumber");
            this.cbUnnormalizedFieldID.addItem("StreetName");
            this.cbUnnormalizedFieldID.addItem("StreetDir");
            this.cbUnnormalizedFieldID.addItem("StreetType");
            
            this.cbNormalizedFieldID.addItem("HouseNumber");
            this.cbNormalizedFieldID.addItem("StreetName");
            this.cbNormalizedFieldID.addItem("StreetDir");
            this.cbNormalizedFieldID.addItem("StreetType");
            
        } else if (standardizationType.equals("BusinessName")) {
            this.cbUnnormalizedFieldID.addItem("PrimaryName");
            this.cbUnnormalizedFieldID.addItem("OrgTypeKeyword");
            this.cbUnnormalizedFieldID.addItem("AssocTypeKeyword");
            this.cbUnnormalizedFieldID.addItem("IndustryTypeKeyword");
            this.cbUnnormalizedFieldID.addItem("AliasList");
            this.cbUnnormalizedFieldID.addItem("IndustrySectorList");
            
            this.cbNormalizedFieldID.addItem("PrimaryName");
            this.cbNormalizedFieldID.addItem("OrgTypeKeyword");
            this.cbNormalizedFieldID.addItem("AssocTypeKeyword");
            this.cbNormalizedFieldID.addItem("IndustryTypeKeyword");
            this.cbNormalizedFieldID.addItem("AliasList");
            this.cbNormalizedFieldID.addItem("IndustrySectorList");

        } else if (standardizationType.equals("PersonName")) {
            this.cbUnnormalizedFieldID.addItem("FirstName");
            this.cbUnnormalizedFieldID.addItem("MiddleName");
            this.cbUnnormalizedFieldID.addItem("LastName");
            this.cbUnnormalizedFieldID.addItem("Title");
            this.cbUnnormalizedFieldID.addItem("Gender");
            this.cbUnnormalizedFieldID.addItem("GenSuffix");
            
            this.cbNormalizedFieldID.addItem("FirstName");
            this.cbNormalizedFieldID.addItem("MiddleName");
            this.cbNormalizedFieldID.addItem("LastName");
            this.cbNormalizedFieldID.addItem("Title");
            this.cbNormalizedFieldID.addItem("Gender");
            this.cbNormalizedFieldID.addItem("GenSuffix");
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanelUnnormalizedSource = new javax.swing.JPanel();
        jLabelUnnormalizedSource = new javax.swing.JLabel();
        jLabelUnnormalizedFieldID = new javax.swing.JLabel();
        jTextFieldUnnormalizedSource = new javax.swing.JTextField();
        jButtonSelectSource = new javax.swing.JButton();
        cbUnnormalizedFieldID = new javax.swing.JComboBox();
        jPanelNormalizationTargets = new javax.swing.JPanel();
        jLabelStandardizedObject = new javax.swing.JLabel();
        jLabelStandardizedTarget = new javax.swing.JLabel();
        jTextFieldStandardizedObject = new javax.swing.JTextField();
        btnSelectStandardizedObject = new javax.swing.JButton();
        cbNormalizedFieldID = new javax.swing.JComboBox();
        lblDesc = new javax.swing.JLabel();
        cbStandardizationTypes = new javax.swing.JComboBox();
        jScrollPaneLocaleCodes = new javax.swing.JScrollPane();
        btnAddLocaleMap = new javax.swing.JButton();
        btnRemoveLocaleMap = new javax.swing.JButton();
        btnRemoveLocaleMap.setEnabled(false);
        btnEditLocaleMap = new javax.swing.JButton();
        btnEditLocaleMap.setEnabled(false);
        jLabelLocaleFieldName = new javax.swing.JLabel();
        jTextFieldLocaleFieldName = new javax.swing.JTextField();
        jButtonSelectLocaleFieldName = new javax.swing.JButton();
        jLabelDomainSelector = new javax.swing.JLabel();
        cbDomainSelector = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/standardization/Bundle"); // NOI18N
        setTitle(bundle.getString("LBL_Title_NormalizedFieldEdit")); // NOI18N
        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/blocking/Bundle"); // NOI18N
        setName(bundle1.getString("LBL_Title_BlockBy")); // NOI18N

        jButtonOk.setText(bundle.getString("LBL_OK")); // NOI18N
        jButtonOk.setEnabled(false);
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jButtonCancel.setText(bundle.getString("LBL_Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanelUnnormalizedSource.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("TITLE_Unnormalized_Source")))); // NOI18N
        jPanelUnnormalizedSource.setLayout(null);

        jLabelUnnormalizedSource.setText(bundle.getString("LBL_Unnormalized_Source")); // NOI18N
        jPanelUnnormalizedSource.add(jLabelUnnormalizedSource);
        jLabelUnnormalizedSource.setBounds(10, 30, 140, 20);

        jLabelUnnormalizedFieldID.setText(bundle.getString("LBL_Unnormalized_Target_Field")); // NOI18N
        jPanelUnnormalizedSource.add(jLabelUnnormalizedFieldID);
        jLabelUnnormalizedFieldID.setBounds(10, 60, 260, 20);

        jTextFieldUnnormalizedSource.setEditable(false);
        jPanelUnnormalizedSource.add(jTextFieldUnnormalizedSource);
        jTextFieldUnnormalizedSource.setBounds(150, 30, 320, 20);

        jButtonSelectSource.setText(bundle.getString("LBL_Browse")); // NOI18N
        jButtonSelectSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectUnnormalizedSourceActionPerformed(evt);
            }
        });
        jPanelUnnormalizedSource.add(jButtonSelectSource);
        jButtonSelectSource.setBounds(471, 30, 80, 20);

        jPanelUnnormalizedSource.add(cbUnnormalizedFieldID);
        cbUnnormalizedFieldID.setBounds(270, 60, 200, 22);

        jPanelNormalizationTargets.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("TITLE_Normalization_Target"))); // NOI18N
        jPanelNormalizationTargets.setLayout(null);

        jLabelStandardizedObject.setText(bundle.getString("LBL_Standardized_Object")); // NOI18N
        jPanelNormalizationTargets.add(jLabelStandardizedObject);
        jLabelStandardizedObject.setBounds(10, 60, 140, 20);

        jLabelStandardizedTarget.setText(bundle.getString("LBL_Standardized_Target_Field")); // NOI18N
        jPanelNormalizationTargets.add(jLabelStandardizedTarget);
        jLabelStandardizedTarget.setBounds(10, 30, 260, 20);

        jTextFieldStandardizedObject.setEditable(false);
        jPanelNormalizationTargets.add(jTextFieldStandardizedObject);
        jTextFieldStandardizedObject.setBounds(150, 60, 320, 20);

        btnSelectStandardizedObject.setText(bundle.getString("LBL_Browse")); // NOI18N
        btnSelectStandardizedObject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectStandardizedObjectActionPerformed(evt);
            }
        });
        jPanelNormalizationTargets.add(btnSelectStandardizedObject);
        btnSelectStandardizedObject.setBounds(471, 60, 80, 20);

        jPanelNormalizationTargets.add(cbNormalizedFieldID);
        cbNormalizedFieldID.setBounds(270, 30, 200, 20);

        lblDesc.setText(bundle.getString("LBL_Select_Standardization_Type")); // NOI18N

        cbStandardizationTypes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbStandardizationTypesonTypeSelected(evt);
            }
        });

        jScrollPaneLocaleCodes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Locale_Codes"))); // NOI18N

        btnAddLocaleMap.setText(bundle.getString("LBL_Add")); // NOI18N
        btnAddLocaleMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddLocaleCode(evt);
            }
        });

        btnRemoveLocaleMap.setText(bundle.getString("LBL_Remove")); // NOI18N
        btnRemoveLocaleMap.setEnabled(false);
        btnRemoveLocaleMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveLocaleCode(evt);
            }
        });

        btnEditLocaleMap.setText(bundle.getString("LBL_Edit")); // NOI18N
        btnEditLocaleMap.setEnabled(false);
        btnEditLocaleMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditLocaleCode(evt);
            }
        });

        jLabelLocaleFieldName.setText(bundle.getString("LBL_Locale_Field_Name")); // NOI18N

        jTextFieldLocaleFieldName.setEnabled(false);

        jButtonSelectLocaleFieldName.setText(bundle.getString("LBL_Browse")); // NOI18N
        jButtonSelectLocaleFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonSelectLocaleFieldName(evt);
            }
        });

        jLabelDomainSelector.setText(bundle.getString("LBL_Domain_Selector")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(lblDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cbStandardizationTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelDomainSelector, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cbDomainSelector, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 420, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jButtonSelectLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jScrollPaneLocaleCodes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(330, 330, 330)
                .add(btnAddLocaleMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnRemoveLocaleMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnEditLocaleMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanelUnnormalizedSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanelNormalizationTargets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(410, 410, 410)
                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblDesc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbStandardizationTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDomainSelector, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbDomainSelector, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonSelectLocaleFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jScrollPaneLocaleCodes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAddLocaleMap)
                    .add(btnRemoveLocaleMap)
                    .add(btnEditLocaleMap))
                .add(7, 7, 7)
                .add(jPanelUnnormalizedSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanelNormalizationTargets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonOk)
                    .add(jButtonCancel)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-586)/2, (screenSize.height-523)/2, 586, 523);
    }// </editor-fold>//GEN-END:initComponents

    private void onButtonSelectLocaleFieldName(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonSelectLocaleFieldName
        String localeFieldName = this.jTextFieldLocaleFieldName.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, localeFieldName, true);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldLocaleFieldName.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }
    }//GEN-LAST:event_onButtonSelectLocaleFieldName

    private void onEditLocaleCode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditLocaleCode
        LocaleCodesTableModel model = (LocaleCodesTableModel) mTblLocaleCodes.getModel();
        int iEditRow = mTblLocaleCodes.getSelectedRow();
        String value = (String) model.getValueAt(iEditRow, model.iColValue);
        String locale = (String) model.getValueAt(iEditRow, model.iColLocale);
        LocaleCodeDialog dlg = new LocaleCodeDialog(value, locale, true);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            model.setValueAt(dlg.getValue(), iEditRow, model.iColValue);
            model.setValueAt(dlg.getLocaleCode(), iEditRow, model.iColLocale);
            enableOK();
        }
    }//GEN-LAST:event_onEditLocaleCode

    private void onRemoveLocaleCode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveLocaleCode
        int rs[] = mTblLocaleCodes.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            LocaleCodesTableModel model = (LocaleCodesTableModel) mTblLocaleCodes.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                String value = (String) model.getValueAt(j, model.iColValue);
                String locale = (String) model.getValueAt(j, model.iColLocale);
                model.removeRow(j);
            }
            btnRemoveLocaleMap.setEnabled(false);
            btnEditLocaleMap.setEnabled(false);
            enableOK();
        }
    }//GEN-LAST:event_onRemoveLocaleCode

    private void onAddLocaleCode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddLocaleCode
        LocaleCodeDialog dlg = new LocaleCodeDialog("", "US", false);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            LocaleCode r = new LocaleCode(dlg.getValue(), dlg.getLocaleCode());
            LocaleCodesTableModel model = (LocaleCodesTableModel) mTblLocaleCodes.getModel();
            model.addRow(r);
            enableOK();
        }
    }//GEN-LAST:event_onAddLocaleCode

    private void cbStandardizationTypesonTypeSelected(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbStandardizationTypesonTypeSelected
        enableOK();
    }//GEN-LAST:event_cbStandardizationTypesonTypeSelected

    private void btnSelectStandardizedObjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectStandardizedObjectActionPerformed
        String source = this.jTextFieldStandardizedObject.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, source, true);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldStandardizedObject.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }

    }//GEN-LAST:event_btnSelectStandardizedObjectActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        bModified = false;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        bModified = true;
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void selectUnnormalizedSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectUnnormalizedSourceActionPerformed
        String source = this.jTextFieldUnnormalizedSource.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, source, true);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldUnnormalizedSource.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }
    }//GEN-LAST:event_selectUnnormalizedSourceActionPerformed
    
    private void enableOK() {
        boolean enabled = this.jTextFieldUnnormalizedSource.getText() != null && 
                          !this.jTextFieldUnnormalizedSource.getText().equals("") &&
                          this.jTextFieldStandardizedObject.getText() != null && 
                          !this.jTextFieldStandardizedObject.getText().equals("");

        this.jButtonOk.setEnabled(enabled);
    }
    
    public String getDomainSelector() {
        return this.cbDomainSelector.getSelectedItem().toString();
    }
    
    public String getLocaleFieldName() {
        String localeFieldName = this.jTextFieldLocaleFieldName.getText();
        if (localeFieldName == null) {
            localeFieldName = "";
        }
        return localeFieldName;
    }
    
    /*
     *@return ArrayList of LocaleRow
     */
    public ArrayList getLocaleCodeRows() {
        ArrayList localeCodeRows;
        LocaleCodesTableModel model = (LocaleCodesTableModel) mTblLocaleCodes.getModel();
        return model.getLocaleCodeRows();
    }
    
    public String getStandardizationType() {
        return this.cbStandardizationTypes.getSelectedItem().toString();
    }
    
    public String getUnnormalizedFieldID() {
        return this.cbUnnormalizedFieldID.getSelectedItem().toString();
    }
    
    public String getUnnormalizedSource() {
        return this.jTextFieldUnnormalizedSource.getText();
    }
    
    public String getNormalizedFieldID() {
        return this.cbNormalizedFieldID.getSelectedItem().toString();
    }
    
    public String getStandardizedTarget() {
        return this.jTextFieldStandardizedObject.getText();
    }
    
    public boolean isModified() {
        return bModified;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new NormalizedFieldEditDialog(null, "field", "encoding", "source");
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddLocaleMap;
    private javax.swing.JButton btnEditLocaleMap;
    private javax.swing.JButton btnRemoveLocaleMap;
    private javax.swing.JButton btnSelectStandardizedObject;
    private javax.swing.JComboBox cbDomainSelector;
    private javax.swing.JComboBox cbNormalizedFieldID;
    private javax.swing.JComboBox cbStandardizationTypes;
    private javax.swing.JComboBox cbUnnormalizedFieldID;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSelectLocaleFieldName;
    private javax.swing.JButton jButtonSelectSource;
    private javax.swing.JLabel jLabelDomainSelector;
    private javax.swing.JLabel jLabelLocaleFieldName;
    private javax.swing.JLabel jLabelStandardizedObject;
    private javax.swing.JLabel jLabelStandardizedTarget;
    private javax.swing.JLabel jLabelUnnormalizedFieldID;
    private javax.swing.JLabel jLabelUnnormalizedSource;
    private javax.swing.JPanel jPanelNormalizationTargets;
    private javax.swing.JPanel jPanelUnnormalizedSource;
    private javax.swing.JScrollPane jScrollPaneLocaleCodes;
    private javax.swing.JTextField jTextFieldLocaleFieldName;
    private javax.swing.JTextField jTextFieldStandardizedObject;
    private javax.swing.JTextField jTextFieldUnnormalizedSource;
    private javax.swing.JLabel lblDesc;
    // End of variables declaration//GEN-END:variables
    
    private void loadLocaleCodesTable(boolean editMode) {
        ArrayList rows = new ArrayList();
        if (editMode) {
            if (mGroup != null) {
                ArrayList alLocaleCodes = mGroup.getLocaleCodes();

                for (int i=0; alLocaleCodes!= null && i < alLocaleCodes.size(); i++) {
                    LocaleCode localeCode = (LocaleCode) alLocaleCodes.get(i);
                    if (localeCode != null) {
                        rows.add(localeCode);
                    }
                }
            }
        }
        LocaleCodesTableModel model = new LocaleCodesTableModel(rows);
        mTblLocaleCodes = new JTable(model);
        mTblLocaleCodes.getTableHeader().setReorderingAllowed(false);
        jScrollPaneLocaleCodes.setViewportView(mTblLocaleCodes);
        
        mTblLocaleCodes.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        onEditLocaleCode(null);
                    } else {
                        btnRemoveLocaleMap.setEnabled(true);
                        if (mTblLocaleCodes.getSelectedRowCount() > 1) {
                            btnEditLocaleMap.setEnabled(false);
                        } else {
                            btnEditLocaleMap.setEnabled(true);
                        }
                    }
                }
            });
    }
    
    // Table model for Locale Maps
    class LocaleCodesTableModel extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Value"),
                                         NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Locale")
                                        };
        int iColValue = 0;
        int iColLocale = 1;
        ArrayList localeCodeRows;
        
        LocaleCodesTableModel(ArrayList rows) {
            localeCodeRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (localeCodeRows != null) {
                return localeCodeRows.size();
            }
            return 0;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (localeCodeRows != null) {
                LocaleCode singleRow = (LocaleCode) localeCodeRows.get(row);
                if (singleRow != null && col == iColValue) {
                    return singleRow.getValue();
                } else if (singleRow != null && col == iColLocale) {
                    return singleRow.getLocaleCode();
                }
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            if (localeCodeRows != null) {
                LocaleCode singleRow = (LocaleCode) localeCodeRows.get(row);
                if (col == iColValue) {
                    singleRow.setValue((String) value);
                } else if (col == iColLocale) {
                    singleRow.setLocaleCode((String) value);
                }
            }
            fireTableCellUpdated(row, col);
        }
        
        public void addRow(LocaleCode singleRow) {
            localeCodeRows.add(singleRow);
            this.fireTableRowsInserted(0, getRowCount());
        }
        
        public void removeRow(int index) {
            localeCodeRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public ArrayList getLocaleCodeRows() {
            return localeCodeRows;
        }
    }
    
}
