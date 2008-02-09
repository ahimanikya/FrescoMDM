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
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.parser.MatchFieldDef.FreeFormGroup;
import com.sun.mdm.index.parser.MatchFieldDef.LocaleCode;
import com.sun.mdm.index.project.ui.applicationeditor.EntityTreeSelectionDialog;
import com.sun.mdm.index.project.ui.applicationeditor.EntityTree;
import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.standardizer.introspector.StandardizationIntrospector;
import com.sun.mdm.standardizer.introspector.DataTypeDescriptor;
import com.sun.mdm.standardizer.introspector.VariantDescriptor;

public class StandardizationTypeAddDialog extends javax.swing.JDialog {
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            StandardizationTypeAddDialog.class.getName()
        );
    private boolean mModified = false;
    private MatchFieldDef mMatchFieldDef;
    private EntityTree mEntityTree;
    private String mMatchEngine;
    private ArrayList mAlSourceFieldNames = new ArrayList();
    private ArrayList mAlFieldIDs = new ArrayList();
    private ArrayList mAlSupportedVariants = new ArrayList();
    private javax.swing.JList mLstSources = new javax.swing.JList();
    private javax.swing.JList mLstFieldIDs = new javax.swing.JList();
    private JTable mTblVariants;    
    private JTable mTblTargetMappings;
    private Map mMapSourceFields;   // key:standardizationType
    private Map mMapFieldIDs;       // key:standardizationType
    private Map mMapTargetFields;
    private Map mMapFieldIDsPerTargetField; // key:targetField
    private FreeFormGroup mFreeFormGroup;
    private EviewApplication mEviewApplication;
    
    /** Creates new form StandardizationTypeAddDialog */
    public StandardizationTypeAddDialog(EviewApplication eviewApplication, Map sourceFieldsMap, Map fieldIDsMap, Map targetFieldsMap, Map fieldIDsPerTargetFieldMap, String matchEngine, EntityTree entityTree, MatchFieldDef matchFieldDef, String dataType, boolean editMode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mEviewApplication = eviewApplication;
        mMatchEngine = matchEngine;
        mEntityTree = entityTree;
        mEntityTree.setEditable(false);        
        mMatchFieldDef = matchFieldDef;
        mMapSourceFields = sourceFieldsMap;
        mMapFieldIDs = fieldIDsMap;
        mMapTargetFields = targetFieldsMap;
        mMapFieldIDsPerTargetField = fieldIDsPerTargetFieldMap;
        initComponents();

        /* ToDo Kevin/Ricardo/Shant
         * Get domain selectors per Data Type
        ArrayList alDomainSelectors = getDomainSelectors();
        */
        ArrayList alDomainSelectors = new ArrayList();
        alDomainSelectors.add(MatchFieldDef.MULTIPLE_DOMAIN_SELECTOR);
        alDomainSelectors.add(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_AU);
        alDomainSelectors.add(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_FR);
        alDomainSelectors.add(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_UK);
        alDomainSelectors.add(MatchFieldDef.SINGLE_DOMAIN_SELECTOR_US);
        for (int i = 0; alDomainSelectors != null && i < alDomainSelectors.size(); i++) {
            this.cbDomainSelector.addItem(alDomainSelectors.get(i));
        }
        
        if (editMode) {
            mFreeFormGroup = mMatchFieldDef.getFreeFormGroup(dataType);
            String domainSelector = mFreeFormGroup.getDomainSelector();
            if (!alDomainSelectors.contains(domainSelector)) {
                this.cbDomainSelector.addItem(domainSelector);
            }

            this.cbDomainSelector.setSelectedItem(domainSelector);
            String variantFieldName = mFreeFormGroup.getLocaleFieldName();
            if (variantFieldName != null) {
                jTextFieldVariant.setText(variantFieldName);
            }
        } else {
            /* ToDo ??? Kevin/Ricardo/Shant
             * VariantDescriptor variantDescriptor = DataTypeDescriptor.getDefaultVariant();
             * String defaultDomainSelector = variantDescriptor.getFactoryClass(); //"com.sun.mdm.standardizer.datatype.address.USStandardizerFactory"
             */
            String defaultDomainSelector = MatchFieldDef.MULTIPLE_DOMAIN_SELECTOR;
            this.cbDomainSelector.setSelectedItem(defaultDomainSelector);
        }
        
        loadStandardizationDataTypes(mMatchEngine, dataType, editMode);
        loadVariantTable(editMode);
        loadSupportedVariants(dataType);
        // these methods will be invoked twice, 1st by setting dataType
        // see onTypeSelected()
        loadSelectedSourceFields();
        loadTargetMappingsTable();
        
        mLstSources.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveSource.setEnabled(true);
            }
        });

        cbStandardizationDataTypes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loadSelectedSourceFields();
                loadTargetMappingsTable();
                loadSupportedVariants(getStandardizationDataType());
            }
        });

        this.cbDomainSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    enableOK();
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlConfiguration = new javax.swing.JPanel();
        btnAddSource = new javax.swing.JButton();
        btnAddTargetMapping = new javax.swing.JButton();
        btnRemoveTargetMapping = new javax.swing.JButton();
        btnRemoveTargetMapping.setEnabled(false);
        btnEditTargetMapping = new javax.swing.JButton();
        btnEditTargetMapping.setEnabled(false);
        jScrollPaneTargetMappings = new javax.swing.JScrollPane();
        jScrollPaneSourceFields = new javax.swing.JScrollPane();
        btnRemoveSource = new javax.swing.JButton();
        btnRemoveSource.setEnabled(false);
        lblDesc = new javax.swing.JLabel();
        cbStandardizationDataTypes = new javax.swing.JComboBox();
        lblStep4 = new javax.swing.JLabel();
        jScrollPaneVariants = new javax.swing.JScrollPane();
        btnAddVariantMap = new javax.swing.JButton();
        btnRemoveVariantMap = new javax.swing.JButton();
        btnRemoveTargetMapping.setEnabled(false);
        btnEditVariantMap = new javax.swing.JButton();
        btnEditTargetMapping.setEnabled(false);
        jTextFieldVariant = new javax.swing.JTextField();
        btnSelectVariantFieldName = new javax.swing.JButton();
        jLabelDomainSelector = new javax.swing.JLabel();
        cbDomainSelector = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        btnOK.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Standardization Type");
        setModal(true);

        pnlConfiguration.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlConfiguration.setMinimumSize(new java.awt.Dimension(560, 400));
        pnlConfiguration.setName(""); // NOI18N
        pnlConfiguration.setLayout(null);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/standardization/Bundle"); // NOI18N
        btnAddSource.setText(bundle.getString("LBL_Add")); // NOI18N
        btnAddSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSourceFields(evt);
            }
        });
        pnlConfiguration.add(btnAddSource);
        btnAddSource.setBounds(410, 380, 80, 23);

        btnAddTargetMapping.setText(bundle.getString("LBL_Add")); // NOI18N
        btnAddTargetMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddTargetMapping(evt);
            }
        });
        pnlConfiguration.add(btnAddTargetMapping);
        btnAddTargetMapping.setBounds(330, 540, 80, 23);

        btnRemoveTargetMapping.setText(bundle.getString("LBL_Remove")); // NOI18N
        btnRemoveTargetMapping.setEnabled(false);
        btnRemoveTargetMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveTargetMapping(evt);
            }
        });
        pnlConfiguration.add(btnRemoveTargetMapping);
        btnRemoveTargetMapping.setBounds(410, 540, 80, 23);

        btnEditTargetMapping.setText(bundle.getString("LBL_Edit")); // NOI18N
        btnEditTargetMapping.setEnabled(false);
        btnEditTargetMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditTargetMapping(evt);
            }
        });
        pnlConfiguration.add(btnEditTargetMapping);
        btnEditTargetMapping.setBounds(490, 540, 80, 23);

        jScrollPaneTargetMappings.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Specifying_Target_Mappings"))); // NOI18N
        pnlConfiguration.add(jScrollPaneTargetMappings);
        jScrollPaneTargetMappings.setBounds(10, 410, 560, 120);

        jScrollPaneSourceFields.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Select_Fields_ to_be_Standardized"))); // NOI18N
        pnlConfiguration.add(jScrollPaneSourceFields);
        jScrollPaneSourceFields.setBounds(10, 250, 560, 120);

        btnRemoveSource.setText(bundle.getString("LBL_Remove")); // NOI18N
        btnRemoveSource.setEnabled(false);
        btnRemoveSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSourceFields(evt);
            }
        });
        pnlConfiguration.add(btnRemoveSource);
        btnRemoveSource.setBounds(490, 380, 80, 23);

        lblDesc.setText(bundle.getString("LBL_Select_Standardization_Type")); // NOI18N
        pnlConfiguration.add(lblDesc);
        lblDesc.setBounds(10, 10, 120, 20);
        pnlConfiguration.add(cbStandardizationDataTypes);
        cbStandardizationDataTypes.setBounds(130, 10, 440, 22);

        lblStep4.setText(bundle.getString("LBL_Variant_Field_Name")); // NOI18N
        pnlConfiguration.add(lblStep4);
        lblStep4.setBounds(10, 70, 120, 20);

        jScrollPaneVariants.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Variants"))); // NOI18N
        pnlConfiguration.add(jScrollPaneVariants);
        jScrollPaneVariants.setBounds(10, 100, 560, 110);

        btnAddVariantMap.setText(bundle.getString("LBL_Add")); // NOI18N
        btnAddVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddVariant(evt);
            }
        });
        pnlConfiguration.add(btnAddVariantMap);
        btnAddVariantMap.setBounds(330, 220, 80, 23);

        btnRemoveVariantMap.setText(bundle.getString("LBL_Remove")); // NOI18N
        btnRemoveVariantMap.setEnabled(false);
        btnRemoveVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveVariant(evt);
            }
        });
        pnlConfiguration.add(btnRemoveVariantMap);
        btnRemoveVariantMap.setBounds(410, 220, 80, 23);

        btnEditVariantMap.setText(bundle.getString("LBL_Edit")); // NOI18N
        btnEditVariantMap.setEnabled(false);
        btnEditVariantMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditVariant(evt);
            }
        });
        pnlConfiguration.add(btnEditVariantMap);
        btnEditVariantMap.setBounds(490, 220, 80, 23);

        jTextFieldVariant.setEnabled(false);
        pnlConfiguration.add(jTextFieldVariant);
        jTextFieldVariant.setBounds(130, 70, 350, 19);

        btnSelectVariantFieldName.setText(bundle.getString("LBL_Select")); // NOI18N
        btnSelectVariantFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonSelectVariantFieldName(evt);
            }
        });
        pnlConfiguration.add(btnSelectVariantFieldName);
        btnSelectVariantFieldName.setBounds(490, 70, 80, 20);

        jLabelDomainSelector.setText(bundle.getString("LBL_Domain_Selector")); // NOI18N
        pnlConfiguration.add(jLabelDomainSelector);
        jLabelDomainSelector.setBounds(10, 40, 120, 20);

        cbDomainSelector.setEditable(true);
        pnlConfiguration.add(cbDomainSelector);
        cbDomainSelector.setBounds(130, 40, 440, 22);

        btnCancel.setText(bundle.getString("LBL_Cancel")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        btnOK.setText(bundle.getString("LBL_OK")); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOK(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(pnlConfiguration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 580, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(450, 450, 450)
                .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(pnlConfiguration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 580, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnOK)
                    .add(btnCancel)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-604)/2, (screenSize.height-661)/2, 604, 661);
    }// </editor-fold>//GEN-END:initComponents

    private void onButtonSelectVariantFieldName(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonSelectVariantFieldName
        String variantFieldName = this.jTextFieldVariant.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, variantFieldName, true);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldVariant.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }

}//GEN-LAST:event_onButtonSelectVariantFieldName

    private void onEditVariant(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditVariant
        VariantsTableModel model = (VariantsTableModel) mTblVariants.getModel();
        int iEditRow = mTblVariants.getSelectedRow();
        String value = (String) model.getValueAt(iEditRow, model.iColValue);
        String variant = (String) model.getValueAt(iEditRow, model.iColVariant);
        LocaleCodeDialog dlg = new LocaleCodeDialog(value, variant, true, mAlSupportedVariants);
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
        String prompt = (length == 1) ? NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            VariantsTableModel model = (VariantsTableModel) mTblVariants.getModel();
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
            VariantsTableModel model = (VariantsTableModel) mTblVariants.getModel();
            model.addRow(r);
            enableOK();
        }
}//GEN-LAST:event_onAddVariant

    private void onRemoveSourceFields(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSourceFields
        int[] itemsBeingRemoved = mLstSources.getSelectedIndices();
        int length = itemsBeingRemoved.length;

        String prompt = (length == 1) ? NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
        
            for (int index = length - 1; (index >= 0) && (index < length);
                    index--) {
                int i = itemsBeingRemoved[index];
                mAlSourceFieldNames.remove(i);
            }
        
            if (mAlSourceFieldNames != null) {
                mLstSources.setListData(mAlSourceFieldNames.toArray());
            } else {
                ArrayList dummy = new ArrayList();
                mLstSources.setListData(dummy.toArray());
            }
            jScrollPaneSourceFields.setViewportView(mLstSources);
            btnRemoveSource.setEnabled(false);
            enableOK();
        }
    }//GEN-LAST:event_onRemoveSourceFields

    private void onEditTargetMapping(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditTargetMapping
        int iSelectedRow = mTblTargetMappings.getSelectedRow();
        TargetMappingsTableModel model = (TargetMappingsTableModel) mTblTargetMappings.getModel();
        String targetFieldName = (String) model.getValueAt(iSelectedRow,  0);    // ToDo hard-coded
        String fieldIDs = (String) model.getValueAt(iSelectedRow,  1);    // ToDo hard-coded
        String dataType = getStandardizationDataType();
        TargetMappingDialog dlg = new TargetMappingDialog(mEntityTree, dataType, targetFieldName, fieldIDs, true);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            ArrayList alFieldIDs = dlg.getFieldIDs();
            mMapFieldIDsPerTargetField.put(targetFieldName, alFieldIDs);
            String sFieldIDs = "";
            for (int i=0; i < alFieldIDs.size(); i++) {
                if (sFieldIDs.equals("")) {
                    sFieldIDs = (String) alFieldIDs.get(i);
                } else {
                    sFieldIDs += ";" + (String) alFieldIDs.get(i);
                }
            }
            model.setValueAt(dlg.getTargetField(), iSelectedRow, 0);
            model.setValueAt(sFieldIDs, iSelectedRow, 1);
            jScrollPaneTargetMappings.setViewportView(mTblTargetMappings);
            enableOK();
        }
    }//GEN-LAST:event_onEditTargetMapping

    private void onRemoveTargetMapping(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveTargetMapping
        String dataType = getStandardizationDataType();

        int rs[] = mTblTargetMappings.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(StandardizationTypeAddDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TargetMappingsTableModel model = (TargetMappingsTableModel) mTblTargetMappings.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                String targetFieldName = (String) model.getValueAt(j,  0);    // ToDo hard-coded
                ArrayList alTargetFields = (ArrayList) mMapTargetFields.get(dataType);
                alTargetFields.remove(targetFieldName);
                mMapTargetFields.put(dataType, alTargetFields);
                mMapFieldIDsPerTargetField.remove(targetFieldName);
                model.removeRow(j);
            }
            btnRemoveTargetMapping.setEnabled(false);
            btnEditTargetMapping.setEnabled(false);
            enableOK();
        }
    }//GEN-LAST:event_onRemoveTargetMapping

    private void onAddTargetMapping(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddTargetMapping
        String dataType = getStandardizationDataType();
        TargetMappingDialog dlg = new TargetMappingDialog(mEntityTree, dataType, "", null, false);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            //TODO check if target Field already selected
            ArrayList alFieldIDs = dlg.getFieldIDs();
            mMapFieldIDsPerTargetField.put(dlg.getTargetField(), alFieldIDs);
            ArrayList alTargetFields = (ArrayList) mMapTargetFields.get(dataType);
            if (alTargetFields == null) {
                alTargetFields = new ArrayList();
                mMapTargetFields.put(dataType, alTargetFields);
            }
            alTargetFields.add(dlg.getTargetField());
            mMapTargetFields.put(dataType, alTargetFields);
            String sFieldIDs = "";
            for (int i=0; i < alFieldIDs.size(); i++) {
                if (sFieldIDs.equals("")) {
                    sFieldIDs = (String) alFieldIDs.get(i);
                } else {
                    sFieldIDs += ";" + (String) alFieldIDs.get(i);
                }
            }
            TargetMappingsRow r = new TargetMappingsRow(dlg.getTargetField(), sFieldIDs);
            TargetMappingsTableModel model = (TargetMappingsTableModel) mTblTargetMappings.getModel();
            model.addRow(r);
            int iSelectedRow = model.getRowCount() - 1;
            mTblTargetMappings.clearSelection();
            mTblTargetMappings.addRowSelectionInterval(iSelectedRow, iSelectedRow);
            mTblTargetMappings.setEditingRow(iSelectedRow);
            enableOK();
        }

        btnEditTargetMapping.setEnabled(mTblTargetMappings.getSelectedRowCount() == 1);
        btnRemoveTargetMapping.setEnabled(mTblTargetMappings.getSelectedRowCount() > 0);
    }//GEN-LAST:event_onAddTargetMapping

    private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
        mModified = false;
        {
            //TODO need to restore 
            // mMapSourceFields
            // mMapFieldIDs
            // mMapTargetFields
            // mMapFieldIDsPerTargetField
        }
        
        this.dispose();
    }//GEN-LAST:event_onCancel

    private void onOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOK
        // ToDo validate 
        if (this.mAlSourceFieldNames.size() == 0) {
            // no good
        }
        // TargetMappings
        mModified = true;
        this.dispose();
    }//GEN-LAST:event_onOK

    private void onAddSourceFields(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSourceFields
        ArrayList alSourceFieldNames = new ArrayList();
        if (mAlSourceFieldNames != null) {
	    alSourceFieldNames.addAll(mAlSourceFieldNames);
        }
        SourceFieldsDialog dlg = new SourceFieldsDialog(mEntityTree, mAlSourceFieldNames, false);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            mAlSourceFieldNames = dlg.getSelectedSourceFields();
            String dataType = getStandardizationDataType();
            mMapSourceFields.put(dataType, mAlSourceFieldNames);
            mLstSources.setListData(mAlSourceFieldNames.toArray());
            jScrollPaneSourceFields.setViewportView(mLstSources);
            enableOK();
        } else {
            mAlSourceFieldNames = alSourceFieldNames;
        }
    }//GEN-LAST:event_onAddSourceFields
    
    private void loadStandardizationDataTypes(String matchEngine, String dataType, boolean editMode) {
        try {
            ArrayList alTempDataTypes = new ArrayList();
            ArrayList alDataTypes = mEviewApplication.getStandardizationDataTypes();
            alTempDataTypes.addAll(alDataTypes);
            if (!editMode) {
                for (int i = 0; i < alDataTypes.size(); i++) {
                    ArrayList alSourceFields = (ArrayList) mMapSourceFields.get(alDataTypes.get(i));
                    if (alSourceFields != null && alSourceFields.size() > 0) {
                        alTempDataTypes.remove(alDataTypes.get(i));
                    }
                }
            }

            for (int i = 0; i < alTempDataTypes.size(); i++) {
                cbStandardizationDataTypes.addItem(alTempDataTypes.get(i));
            }

            if (dataType != null) {
                cbStandardizationDataTypes.setSelectedItem(dataType);
            } else {
                cbStandardizationDataTypes.setSelectedIndex(0);
            }
            cbStandardizationDataTypes.setEnabled(!editMode);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    private void loadSelectedSourceFields() {
        String dataType = getStandardizationDataType();
        //get this from memory
        mAlSourceFieldNames = (ArrayList) mMapSourceFields.get(dataType);
        if (mAlSourceFieldNames != null) {
            mLstSources.setListData(mAlSourceFieldNames.toArray());
        } else {
            ArrayList dummy = new ArrayList();
            mLstSources.setListData(dummy.toArray());
        }
        jScrollPaneSourceFields.setViewportView(mLstSources);
    }
    
    private void loadSelectedFieldIDs() {
        String dataType = getStandardizationDataType();
        mAlFieldIDs = (ArrayList) mMapFieldIDs.get(dataType);
        if (mAlFieldIDs != null) {
            mLstFieldIDs.setListData(mAlFieldIDs.toArray());
        } else {
            ArrayList dummy = new ArrayList();
            mLstFieldIDs.setListData(dummy.toArray());
        }
        //jScrollPaneFieldIDs.setViewportView(mLstFieldIDs);
    }
    
    private void loadVariantTable(boolean editMode) {
        ArrayList rows = new ArrayList();
        if (editMode) {
            ArrayList alVariants = mFreeFormGroup.getLocaleCodes();

            for (int i=0; alVariants != null && i < alVariants.size(); i++) {
                LocaleCode variant = (LocaleCode) alVariants.get(i);
                if (variant != null) {
                    rows.add(variant);
                }
            }
        }
        VariantsTableModel model = new VariantsTableModel(rows);
        mTblVariants = new JTable(model);
        mTblVariants.getTableHeader().setReorderingAllowed(false);
        jScrollPaneVariants.setViewportView(mTblVariants);
        
        mTblVariants.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
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
    class VariantsTableModel extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(StandardizationTypeAddDialog.class, "LBL_Value"),
                                         NbBundle.getMessage(StandardizationTypeAddDialog.class, "LBL_Variant")
                                        };
        int iColValue = 0;
        int iColVariant = 1;
        ArrayList variantRows;
        
        VariantsTableModel(ArrayList rows) {
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

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
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
    
    private void loadSupportedVariants(String dataType) {
        try {
            StandardizationIntrospector introspector = mEviewApplication.getStandardizationIntrospector();
            DataTypeDescriptor dataTypeDescriptor = introspector.getDataType(dataType);
            VariantDescriptor[] variantDescriptors = dataTypeDescriptor.getVariants();
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
    
    private void loadTargetMappingsTable() {
        //mMapFieldIDsPerTargetField
        //mMapTargetFields
        String dataType = getStandardizationDataType();
        ArrayList mAlTargetFields = (ArrayList) mMapTargetFields.get(dataType);
        ArrayList rows = new ArrayList();
        for (int i=0; mAlTargetFields != null && i < mAlTargetFields.size(); i++) {
            String fieldName = (String) mAlTargetFields.get(i);
            String fieldIDs = null;
            ArrayList alFieldIDs = (ArrayList) mMapFieldIDsPerTargetField.get(fieldName);
            for (int k=0; alFieldIDs != null && k < alFieldIDs.size(); k++) {
                if (fieldIDs == null) {
                    fieldIDs = (String) alFieldIDs.get(k);
                } else {
                    fieldIDs += ";" + (String) alFieldIDs.get(k);
                }
            }
            if (fieldIDs != null) {
                TargetMappingsRow r = new TargetMappingsRow(fieldName, fieldIDs);
                rows.add(r);
            }
        }
        TargetMappingsTableModel targetMappingsTableModel = new TargetMappingsTableModel(rows);
        mTblTargetMappings = new JTable(targetMappingsTableModel);
        mTblTargetMappings.getTableHeader().setReorderingAllowed(false);
        jScrollPaneTargetMappings.setViewportView(mTblTargetMappings);
        
        mTblTargetMappings.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        onEditTargetMapping(null);
                    } else {
                        btnRemoveTargetMapping.setEnabled(true);
                        if (mTblTargetMappings.getSelectedRowCount() > 1) {
                            btnEditTargetMapping.setEnabled(false);
                        } else {
                            btnEditTargetMapping.setEnabled(true);
                        }
                    }
                }
            });
    }
    
    // Table model for TargetMapping Types
    class TargetMappingsTableModel extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(StandardizationTypeAddDialog.class, "LBL_Target_Field_Name"),
                                         NbBundle.getMessage(StandardizationTypeAddDialog.class, "LBL_Field_IDs")
                                        };
        ArrayList targetMappingsRows;
        
        TargetMappingsTableModel(ArrayList rows) {
            targetMappingsRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (targetMappingsRows != null) {
                return targetMappingsRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (targetMappingsRows != null) {
                TargetMappingsRow singleRow = (TargetMappingsRow) targetMappingsRows.get(row);
                //if (singleRow != null && col == 0) {
                //return singleRow.getName();
                //}
                if (singleRow != null && col == 0) {
                    return singleRow.getTargetFieldName();
                }              
                if (singleRow != null && col == 1)
                {
                    return singleRow.getFieldIDs();
                }
            }
            return null;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return false;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (targetMappingsRows != null) {
                TargetMappingsRow singleRow = (TargetMappingsRow) targetMappingsRows.get(row);
                if (col == 0) {
                    singleRow.setTargetFieldName((String) value);
                }
                if (col == 1) {
                    singleRow.setFieldIDs((String) value);
                }
            }
            fireTableCellUpdated(row, col);
        }
        
        public void addRow(TargetMappingsRow singleRow) {
            targetMappingsRows.add(singleRow);
            this.fireTableRowsInserted(0, getRowCount());
        }
        
        public void removeRow(int index) {
            targetMappingsRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
    }
    
    class TargetMappingsRow
    {
    	//private String name;
        private String targetFieldName;
        private String fieldIDs;

    	TargetMappingsRow(/*String name, */String targetFieldName, String fieldIDs)
    	{
            //this.name = name;
            this.targetFieldName = targetFieldName;
            this.fieldIDs = fieldIDs;
    	}
        
    	public String getTargetFieldName()
    	{
            return targetFieldName;
    	}

    	public String getFieldIDs()
    	{
            return fieldIDs;
    	}

    	public void setTargetFieldName(String targetFieldName)
    	{
            this.targetFieldName = targetFieldName;
    	}

    	public void setFieldIDs(String fieldIDs)
    	{
            this.fieldIDs = fieldIDs;
    	}
    }
    
    /*
     *@return dataType
     */
    public String getStandardizationDataType() {
        String dataType = (String) cbStandardizationDataTypes.getSelectedItem();
        return dataType;
    }
    
    public String getDomainSelector() {
        return this.cbDomainSelector.getSelectedItem().toString();
    }
    
    public String getVariantFieldName() {
        return this.jTextFieldVariant.getText();
    }
    
    /*
     *@return ArrayList of VariantRow
     */
    public ArrayList getVariantRows() {
        VariantsTableModel model = (VariantsTableModel) mTblVariants.getModel();
        return model.getVariantRows();
    }
    
    public ArrayList getSelectedSourceFields() {
        return mAlSourceFieldNames;
    }
    
    private void enableOK() {
        boolean enabled = (mAlSourceFieldNames != null && mAlSourceFieldNames.size() > 0) && 
                          (mTblTargetMappings.getRowCount() > 0);
        btnOK.setEnabled(enabled);
    }
    
    public boolean isModified() {
        return mModified;
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddSource;
    private javax.swing.JButton btnAddTargetMapping;
    private javax.swing.JButton btnAddVariantMap;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEditTargetMapping;
    private javax.swing.JButton btnEditVariantMap;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRemoveSource;
    private javax.swing.JButton btnRemoveTargetMapping;
    private javax.swing.JButton btnRemoveVariantMap;
    private javax.swing.JButton btnSelectVariantFieldName;
    private javax.swing.JComboBox cbDomainSelector;
    private javax.swing.JComboBox cbStandardizationDataTypes;
    private javax.swing.JLabel jLabelDomainSelector;
    private javax.swing.JScrollPane jScrollPaneSourceFields;
    private javax.swing.JScrollPane jScrollPaneTargetMappings;
    private javax.swing.JScrollPane jScrollPaneVariants;
    private javax.swing.JTextField jTextFieldVariant;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblStep4;
    private javax.swing.JPanel pnlConfiguration;
    // End of variables declaration//GEN-END:variables
    
}
