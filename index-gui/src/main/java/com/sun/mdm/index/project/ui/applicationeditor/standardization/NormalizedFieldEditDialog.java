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
import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.standardizer.StandardizerIntrospector;
import com.sun.mdm.standardizer.DataTypeDescriptor;
import com.sun.mdm.standardizer.VariantDescriptor;

public class NormalizedFieldEditDialog extends javax.swing.JDialog {
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            NormalizedFieldEditDialog.class.getName()
        );

    EntityTree mEntityTree;
    private MatchFieldDef mMatchFieldDef;
    private NormalizationGroup mGroup;
    private JTable mTblVariants;
    boolean bModified = false;
    private ArrayList mAlSupportedVariants = new ArrayList();
    private EviewApplication mEviewApplication;
    
    /** Creates new form NormalizedFieldEditDialog */
    public NormalizedFieldEditDialog(EviewApplication eviewApplication, 
            EntityTree entityTree, 
            MatchFieldDef matchFieldDef, 
            NormalizationGroup group,
            boolean editMode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mEviewApplication = eviewApplication;        
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
                    loadSupportedVariants(cbStandardizationTypes.getSelectedItem().toString());
                    enableOK();
                }
            });
        if (mGroup != null) {
            cbStandardizationTypes.setSelectedItem(mGroup.getStandardizationType());
        } else {
            cbStandardizationTypes.setSelectedIndex(0);
        }
        loadFieldIDs(cbStandardizationTypes.getSelectedItem().toString());
        loadSupportedVariants(cbStandardizationTypes.getSelectedItem().toString());
        
        this.cbDomainSelector.addItem(MatchFieldDef.MULTIPLE_DOMAIN_SELECTOR);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_AU);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_FR);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_UK);
        this.cbDomainSelector.addItem(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_US);

        if (editMode) {
            this.cbDomainSelector.setSelectedItem(mGroup.getDomainSelector());
            String variantFieldName = mGroup.getLocaleFieldName();
            if (variantFieldName == null) {
                variantFieldName = "";
            }
            this.jTextFieldVariantFieldName.setText(variantFieldName);

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
        loadVariantTable(editMode);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        jScrollPaneVariants = new javax.swing.JScrollPane();
        btnAddVariantMap = new javax.swing.JButton();
        btnRemoveVariantMap = new javax.swing.JButton();
        btnRemoveVariantMap.setEnabled(false);
        btnEditVariantMap = new javax.swing.JButton();
        btnEditVariantMap.setEnabled(false);
        jLabelVariantFieldName = new javax.swing.JLabel();
        jTextFieldVariantFieldName = new javax.swing.JTextField();
        jButtonSelectVariantFieldName = new javax.swing.JButton();
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

        jScrollPaneVariants.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Variants"))); // NOI18N

        btnAddVariantMap.setText(bundle.getString("LBL_Add")); // NOI18N
        btnAddVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddVariant(evt);
            }
        });

        btnRemoveVariantMap.setText(bundle.getString("LBL_Remove")); // NOI18N
        btnRemoveVariantMap.setEnabled(false);
        btnRemoveVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveVariant(evt);
            }
        });

        btnEditVariantMap.setText(bundle.getString("LBL_Edit")); // NOI18N
        btnEditVariantMap.setEnabled(false);
        btnEditVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditVariant(evt);
            }
        });

        jLabelVariantFieldName.setText(bundle.getString("LBL_Variant_Field_Name")); // NOI18N

        jTextFieldVariantFieldName.setEnabled(false);

        jButtonSelectVariantFieldName.setText(bundle.getString("LBL_Browse")); // NOI18N
        jButtonSelectVariantFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonSelectVariantFieldName(evt);
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
                .add(jLabelVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jButtonSelectVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jScrollPaneVariants, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(330, 330, 330)
                .add(btnAddVariantMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnRemoveVariantMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnEditVariantMap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
                    .add(jLabelVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonSelectVariantFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jScrollPaneVariants, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAddVariantMap)
                    .add(btnRemoveVariantMap)
                    .add(btnEditVariantMap))
                .add(7, 7, 7)
                .add(jPanelUnnormalizedSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanelNormalizationTargets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonOk)
                    .add(jButtonCancel)))
        );

        jScrollPaneVariants.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Variants")); // NOI18N
        jLabelVariantFieldName.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Variant_Field_Name")); // NOI18N

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-586)/2, (screenSize.height-523)/2, 586, 523);
    }// </editor-fold>//GEN-END:initComponents

    private void onButtonSelectVariantFieldName(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonSelectVariantFieldName
        String variantFieldName = this.jTextFieldVariantFieldName.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, variantFieldName, true);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldVariantFieldName.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }
}//GEN-LAST:event_onButtonSelectVariantFieldName

    private void onEditVariant(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditVariant
        VariantTableModel model = (VariantTableModel) mTblVariants.getModel();
        int iEditRow = mTblVariants.getSelectedRow();
        String value = (String) model.getValueAt(iEditRow, model.iColValue);
        String locale = (String) model.getValueAt(iEditRow, model.iColVariant);
        LocaleCodeDialog dlg = new LocaleCodeDialog(value, locale, true, mAlSupportedVariants);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            model.setValueAt(dlg.getValue(), iEditRow, model.iColValue);
            model.setValueAt(dlg.getVariant(), iEditRow, model.iColVariant);
            enableOK();
        }
}//GEN-LAST:event_onEditVariant

    private void onRemoveVariant(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveVariant
        int rs[] = mTblVariants.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(NormalizedFieldEditDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            VariantTableModel model = (VariantTableModel) mTblVariants.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                String value = (String) model.getValueAt(j, model.iColValue);
                String variant = (String) model.getValueAt(j, model.iColVariant);
                model.removeRow(j);
            }
            btnRemoveVariantMap.setEnabled(false);
            btnEditVariantMap.setEnabled(false);
            enableOK();
        }
}//GEN-LAST:event_onRemoveVariant

    private void onAddVariant(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddVariant
        LocaleCodeDialog dlg = new LocaleCodeDialog("", "US", false, mAlSupportedVariants);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            LocaleCode r = new LocaleCode(dlg.getValue(), dlg.getVariant());
            VariantTableModel model = (VariantTableModel) mTblVariants.getModel();
            model.addRow(r);
            enableOK();
        }
}//GEN-LAST:event_onAddVariant

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
    
    public String getVariantFieldName() {
        String variantFieldName = this.jTextFieldVariantFieldName.getText();
        if (variantFieldName == null) {
            variantFieldName = "";
        }
        return variantFieldName;
    }
    
    /*
     *@return ArrayList of VariantRow
     */
    public ArrayList getVariantRows() {
        VariantTableModel model = (VariantTableModel) mTblVariants.getModel();
        return model.getVariantRows();
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
    private javax.swing.JButton btnAddVariantMap;
    private javax.swing.JButton btnEditVariantMap;
    private javax.swing.JButton btnRemoveVariantMap;
    private javax.swing.JButton btnSelectStandardizedObject;
    private javax.swing.JComboBox cbDomainSelector;
    private javax.swing.JComboBox cbNormalizedFieldID;
    private javax.swing.JComboBox cbStandardizationTypes;
    private javax.swing.JComboBox cbUnnormalizedFieldID;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSelectSource;
    private javax.swing.JButton jButtonSelectVariantFieldName;
    private javax.swing.JLabel jLabelDomainSelector;
    private javax.swing.JLabel jLabelStandardizedObject;
    private javax.swing.JLabel jLabelStandardizedTarget;
    private javax.swing.JLabel jLabelUnnormalizedFieldID;
    private javax.swing.JLabel jLabelUnnormalizedSource;
    private javax.swing.JLabel jLabelVariantFieldName;
    private javax.swing.JPanel jPanelNormalizationTargets;
    private javax.swing.JPanel jPanelUnnormalizedSource;
    private javax.swing.JScrollPane jScrollPaneVariants;
    private javax.swing.JTextField jTextFieldStandardizedObject;
    private javax.swing.JTextField jTextFieldUnnormalizedSource;
    private javax.swing.JTextField jTextFieldVariantFieldName;
    private javax.swing.JLabel lblDesc;
    // End of variables declaration//GEN-END:variables
    
    private void loadSupportedVariants(String dataType) {
        try {
            StandardizerIntrospector introspector = mEviewApplication.getStandardizerIntrospector();
            DataTypeDescriptor dataTypeDescriptor = introspector.getDataType(dataType);
            VariantDescriptor[] variantDescriptors = dataTypeDescriptor.variants();
            for (VariantDescriptor variantDescriptor: variantDescriptors) {
                mAlSupportedVariants.add(variantDescriptor.getName());
            }
        } catch (Exception ex) {
            // ToDo: Kevin/Ricardo/Shant Need to remove this when all data types are supported
            mAlSupportedVariants.add("AU");
            mAlSupportedVariants.add("FR");
            mAlSupportedVariants.add("UK");
            mAlSupportedVariants.add("US");
            mLog.severe(ex.getMessage());
        }
    }
    
    private void loadVariantTable(boolean editMode) {
        ArrayList rows = new ArrayList();
        if (editMode) {
            if (mGroup != null) {
                ArrayList alVariants = mGroup.getLocaleCodes();

                for (int i=0; alVariants!= null && i < alVariants.size(); i++) {
                    LocaleCode variant = (LocaleCode) alVariants.get(i);
                    if (variant != null) {
                        rows.add(variant);
                    }
                }
            }
        }
        VariantTableModel model = new VariantTableModel(rows);
        mTblVariants = new JTable(model);
        mTblVariants.getTableHeader().setReorderingAllowed(false);
        jScrollPaneVariants.setViewportView(mTblVariants);
        
        mTblVariants.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        onEditVariant(null);
                    } else {
                        btnRemoveVariantMap.setEnabled(true);
                        if (mTblVariants.getSelectedRowCount() > 1) {
                            btnEditVariantMap.setEnabled(false);
                        } else {
                            btnEditVariantMap.setEnabled(true);
                        }
                    }
                }
            });
    }
    
    // Table model for Variant Maps
    class VariantTableModel extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Value"),
                                         NbBundle.getMessage(NormalizedFieldEditDialog.class, "LBL_Variant")
                                        };
        int iColValue = 0;
        int iColVariant = 1;
        ArrayList variantRows;
        
        VariantTableModel(ArrayList rows) {
            variantRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (variantRows != null) {
                return variantRows.size();
            }
            return 0;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (variantRows != null) {
                LocaleCode singleRow = (LocaleCode) variantRows.get(row);
                if (singleRow != null && col == iColValue) {
                    return singleRow.getValue();
                } else if (singleRow != null && col == iColVariant) {
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
            if (variantRows != null) {
                LocaleCode singleRow = (LocaleCode) variantRows.get(row);
                if (col == iColValue) {
                    singleRow.setValue((String) value);
                } else if (col == iColVariant) {
                    singleRow.setLocaleCode((String) value);
                }
            }
            fireTableCellUpdated(row, col);
        }
        
        public void addRow(LocaleCode singleRow) {
            variantRows.add(singleRow);
            this.fireTableRowsInserted(0, getRowCount());
        }
        
        public void removeRow(int index) {
            variantRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public ArrayList getVariantRows() {
            return variantRows;
        }
    }
    
}
