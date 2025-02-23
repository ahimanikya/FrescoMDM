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
package com.sun.mdm.index.project.ui.applicationeditor;

import org.openide.util.NbBundle;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import java.util.ArrayList;
import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.parser.MatchFieldDef.NormalizationGroup;
import com.sun.mdm.index.project.ui.applicationeditor.standardization.NormalizedFieldEditDialog;
import com.sun.mdm.index.project.EviewApplication;

public class TabNormalizationPanel extends javax.swing.JPanel {
    private JTable mTblNormalizationTypes;
    private EviewEditorMainApp mEviewEditorMainApp;
    private EviewApplication mEviewApplication;
    MatchFieldDef mMatchFieldDef;
    private boolean bCheckedOut;
    
    /** Creates new form TabNormalizationPanel */
    public TabNormalizationPanel(EviewEditorMainApp eviewEditorMainApp, EviewApplication eviewApplication) {
        mEviewEditorMainApp = eviewEditorMainApp;
        mEviewApplication = eviewApplication;
        bCheckedOut = eviewEditorMainApp.isCheckedOut();
        mMatchFieldDef = mEviewApplication.getMatchFieldDef(false);
        initComponents();
        createNormalizationTable();
        mTblNormalizationTypes.setEnabled(bCheckedOut);
        btnAdd.setEnabled(bCheckedOut);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPaneTable = new javax.swing.JScrollPane();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Normalization_Mappings"))); // NOI18N

        jScrollPaneTable.setPreferredSize(new java.awt.Dimension(0, 0));

        btnAdd.setMnemonic('A');
        btnAdd.setText(bundle.getString("MSG_button_Add")); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setMnemonic('R');
        btnRemove.setText(bundle.getString("MSG_button_Remove")); // NOI18N
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnEdit.setMnemonic('E');
        btnEdit.setText(bundle.getString("MSG_button_Edit")); // NOI18N
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jScrollPaneTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(304, 304, 304)
                .add(btnAdd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnRemove, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnEdit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAdd)
                    .add(btnRemove)
                    .add(btnEdit)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int iEditRow = mTblNormalizationTypes.getSelectedRow();
        TableNormalizationTypes model = (TableNormalizationTypes) mTblNormalizationTypes.getModel();
        
        String standardizationType = (String) model.getValueAt(iEditRow, model.iColStandardizationType);
        String unnormalizedFieldID = (String) model.getValueAt(iEditRow, model.iColUnnormalizedFieldID);
        String unnormalizedSourceField = (String) model.getValueAt(iEditRow, model.iColUnnormalizedSourceField);
        String normalizedFieldID = (String) model.getValueAt(iEditRow, model.iColNormalizedFieldID);
        String normalizedTargetField = (String) model.getValueAt(iEditRow, model.iColNormalizedTargetField);
        NormalizationGroup group = mMatchFieldDef.getNormalizationGroup(standardizationType, 
                                                                            unnormalizedFieldID, unnormalizedSourceField,
                                                                            normalizedFieldID, normalizedTargetField);
        String domainSelector = group.getDomainSelector();
        NormalizedFieldEditDialog dlg = new NormalizedFieldEditDialog(mEviewApplication, 
                                                                      mEviewEditorMainApp.getEviewEditorMainPanel().getEntityTree(true), 
                                                                      mMatchFieldDef, 
                                                                      group, 
                                                                      true);
        /*
        NormalizedFieldEditDialog dlg = new NormalizedFieldEditDialog(mEviewEditorMainApp.getEviewEditorMainPanel().getEntityTree(true), 
                standardizationType, domainSelector,
                unnormalizedFieldID, unnormalizedSourceField, 
                normalizedFieldID, normalizedTargetField,
                mMatchFieldDef, true);
         */
        dlg.setVisible(true);
        mEviewEditorMainApp.getEviewEditorMainPanel().setEntityTreePane();        
        if (dlg.isModified()) {
            // update normalizationGroup
            standardizationType = dlg.getStandardizationType();
            unnormalizedFieldID = dlg.getUnnormalizedFieldID();
            unnormalizedSourceField = dlg.getUnnormalizedSource();
            normalizedFieldID = dlg.getNormalizedFieldID();
            normalizedTargetField = dlg.getStandardizedTarget();
            domainSelector = dlg.getDomainSelector();
            String variantFieldName = dlg.getVariantFieldName();
            ArrayList localeCodeRows = dlg.getVariantRows();
            
            group.setDomainSelector(domainSelector);
            group.setLocaleFieldName(variantFieldName != null ? variantFieldName : "");
            group.setLocaleCodes(localeCodeRows);
            group.setUnnormalizedFieldId(unnormalizedFieldID);
            group.setUnnormalizedSourceFieldName(unnormalizedSourceField);
            group.setNormalizedSourceFieldName(normalizedTargetField);
            group.setNormalizedFieldId(normalizedFieldID);

            // update the table
            model.setValueAt(unnormalizedFieldID, iEditRow, model.iColUnnormalizedFieldID);
            model.setValueAt(unnormalizedSourceField, iEditRow, model.iColUnnormalizedSourceField);
            model.setValueAt(normalizedFieldID, iEditRow, model.iColNormalizedFieldID);
            model.setValueAt(normalizedTargetField, iEditRow, model.iColNormalizedTargetField);
            // update mMatchFieldDef
            mMatchFieldDef.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int rs[] = mTblNormalizationTypes.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(TabNormalizationPanel.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(TabNormalizationPanel.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(TabNormalizationPanel.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableNormalizationTypes model = (TableNormalizationTypes) mTblNormalizationTypes.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                String standardizationType = (String) model.getValueAt(j, model.iColStandardizationType);
                String unnormalizedFieldID = (String) model.getValueAt(j, model.iColUnnormalizedFieldID);
                String unnormalizedSourceField = (String) model.getValueAt(j, model.iColUnnormalizedSourceField);
                String normalizedFieldID = (String) model.getValueAt(j, model.iColNormalizedFieldID);
                String normalizedTargetField = (String) model.getValueAt(j, model.iColNormalizedTargetField);
                //delete the group
                mMatchFieldDef.deleteNormalizationGroup(standardizationType, 
                                                        unnormalizedFieldID, unnormalizedSourceField,
                                                        normalizedFieldID, normalizedTargetField);

                model.removeRow(j);
            }
            btnRemove.setEnabled(false);
            btnEdit.setEnabled(false);
            mMatchFieldDef.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }

    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        int iInsertRow = mTblNormalizationTypes.getRowCount();        
        NormalizedFieldEditDialog dlg = new NormalizedFieldEditDialog(mEviewApplication,
                                                                      mEviewEditorMainApp.getEviewEditorMainPanel().getEntityTree(true), 
                                                                      mMatchFieldDef, 
                                                                      null, 
                                                                      false);
        dlg.setVisible(true);
        mEviewEditorMainApp.getEviewEditorMainPanel().setEntityTreePane();
        if (dlg.isModified()) {
            String standardizationType = dlg.getStandardizationType();
            String unnormalizedFieldID = dlg.getUnnormalizedFieldID();
            String unnormalizedSourceField = dlg.getUnnormalizedSource();
            String normalizedFieldID = dlg.getNormalizedFieldID();
            String normalizedTargetField = dlg.getStandardizedTarget();
            String domainSelector = dlg.getDomainSelector();
            String variantFieldName = dlg.getVariantFieldName();
            ArrayList localeCodeRows = dlg.getVariantRows();
            
            NormalizationTypeRow r = new NormalizationTypeRow(standardizationType, 
                    unnormalizedFieldID,
                    unnormalizedSourceField,
                    normalizedFieldID,
                    normalizedTargetField); 

            TableNormalizationTypes model = (TableNormalizationTypes) mTblNormalizationTypes.getModel();                    
            model.addRow(r);
            mTblNormalizationTypes.clearSelection();
            mTblNormalizationTypes.addRowSelectionInterval(iInsertRow, iInsertRow);   
            mTblNormalizationTypes.setEditingRow(iInsertRow);
            btnRemove.setEnabled(bCheckedOut);
            btnEdit.setEnabled(bCheckedOut);
            // update mMatchFieldDef
            mMatchFieldDef.addNormalizationGroup(standardizationType, domainSelector, 
                                                 variantFieldName, localeCodeRows,
                                                 unnormalizedFieldID, unnormalizedSourceField,
                                                 normalizedFieldID, normalizedTargetField);

            mMatchFieldDef.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }

    }//GEN-LAST:event_btnAddActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRemove;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneTable;
    // End of variables declaration//GEN-END:variables
    
    void createNormalizationTable() {
        // load standardizationTypesModel
        ArrayList rows = new ArrayList();
        ArrayList alNormalizationGroups = mMatchFieldDef.getNormalizationGroups();
        for (int i=0; alNormalizationGroups != null && i < alNormalizationGroups.size(); i++) {
            NormalizationGroup group = (NormalizationGroup) alNormalizationGroups.get(i);

            if (group != null) {
                String standardizationType = group.getStandardizationType();
                NormalizationTypeRow r = new NormalizationTypeRow(standardizationType, 
                    group.getUnnormalizedFieldId() , group.getUnnormalizedSourceFieldName(), 
                    group.getNormalizedFieldId(), group.getNormalizedSourceFieldName());
                rows.add(r);
            }
        }
        TableNormalizationTypes normalizationTypesModel = new TableNormalizationTypes(rows);
        mTblNormalizationTypes = new JTable(normalizationTypesModel);
        mTblNormalizationTypes.getTableHeader().setReorderingAllowed(false);
        mTblNormalizationTypes.getColumnModel().getColumn(0).setPreferredWidth(90);
        mTblNormalizationTypes.getColumnModel().getColumn(1).setPreferredWidth(210);
        mTblNormalizationTypes.getColumnModel().getColumn(2).setPreferredWidth(210);
        mTblNormalizationTypes.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        if (bCheckedOut) {
                            btnEditActionPerformed(null);
                        }
                    } else {
                        btnRemove.setEnabled(bCheckedOut);
                        btnEdit.setEnabled(bCheckedOut && mTblNormalizationTypes.getSelectedRowCount() == 1);
                    }
                }
            });
            
        jScrollPaneTable.setViewportView(mTblNormalizationTypes);                
    }
    
    /* Called by EntityNode when field name changed
     *@param oldName
     *@param newName
     *
     */
    public boolean updateReferencedField(String oldNodeNameMefa, String newName) {
        boolean bRet = false;
        String oldValue;
        String newValue;
        int index;
        //String oldNameRegex = oldNodeNameMefa;
        //to escape [*] in oldName string for replaceAll method.

        String oldNameRegex = oldNodeNameMefa.replaceAll("\\[", "\\\\["); 
        oldNameRegex = oldNameRegex.replaceAll("\\*","\\\\*");
        oldNameRegex = oldNameRegex.replaceAll("\\]","\\\\]");

        TableNormalizationTypes model = (TableNormalizationTypes) mTblNormalizationTypes.getModel();
        for (int i=0; i < model.getRowCount(); i++) {
            boolean bUpdate = false;
            String unnormalizedSourceField = (String) model.getValueAt(i, model.iColUnnormalizedSourceField);
            String normalizedTargetField = (String) model.getValueAt(i, model.iColNormalizedTargetField);
            index = unnormalizedSourceField.indexOf(oldNodeNameMefa);
            if (unnormalizedSourceField.equals(oldNodeNameMefa) || 
                (index >= 0 &&
                 unnormalizedSourceField.length() > oldNodeNameMefa.length() &&
                 (index + oldNodeNameMefa.length() < unnormalizedSourceField.length() &&
                  unnormalizedSourceField.charAt(index + oldNodeNameMefa.length() - 1) == '.'))) {
                newValue = unnormalizedSourceField.replaceAll(oldNameRegex, newName);
                model.setValueAt(newValue, i, model.iColUnnormalizedSourceField);
                bUpdate = true;
            }
            index = normalizedTargetField.indexOf(oldNodeNameMefa);
            if (normalizedTargetField.equals(oldNodeNameMefa) || 
                (index >= 0 &&
                 normalizedTargetField.length() > oldNodeNameMefa.length() &&
                 (index + oldNodeNameMefa.length() < normalizedTargetField.length() &&
                  normalizedTargetField.charAt(index + oldNodeNameMefa.length() - 1) == '.'))) {
                newValue = normalizedTargetField.replaceAll(oldNameRegex, newName);
                model.setValueAt(newValue, i, model.iColNormalizedTargetField);
                bUpdate = true;
            }
            if (bUpdate) {
                bRet = true;
            }
        }
        
        if (bRet) {
            mMatchFieldDef.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            ArrayList alNormalizationGroups = mMatchFieldDef.getNormalizationGroups();
            for (int j=0; alNormalizationGroups != null && j < alNormalizationGroups.size(); j++) {
                NormalizationGroup group = (NormalizationGroup) alNormalizationGroups.get(j);
                if (group != null) {
                    oldValue = group.getLocaleFieldName();
                    if (oldValue != null && oldValue.equals(oldNodeNameMefa)) {
                        newValue = oldValue.replaceAll(oldNameRegex, newName);
                        group.setLocaleFieldName(newValue);
                    }
                    oldValue = group.getUnnormalizedSourceFieldName();
                    if (oldValue.equals(oldNodeNameMefa)) {
                        newValue = oldValue.replaceAll(oldNameRegex, newName);
                        group.setUnnormalizedSourceFieldName(newValue);
                    }
                    oldValue = group.getNormalizedSourceFieldName();
                    if (oldValue.equals(oldNodeNameMefa)) {
                        newValue = oldValue.replaceAll(oldNameRegex, newName);
                        group.setNormalizedSourceFieldName(newValue);
                    }
                }
            }
        }
        return bRet;
    }
    
    /* Called by EntityNode when field removed
     *@param fieldName
     *
     */
    public boolean removeReferencedField(String fieldName) {
        boolean bRet = false;

        TableNormalizationTypes model = (TableNormalizationTypes) mTblNormalizationTypes.getModel();
        for (int i=model.getRowCount() - 1; i>=0 && i < model.getRowCount(); i--) {
            boolean bUpdate = false;
            String unnormalizedSourceField = (String) model.getValueAt(i, model.iColUnnormalizedSourceField);
            String normalizedTargetField = (String) model.getValueAt(i, model.iColNormalizedTargetField);
            if (unnormalizedSourceField.equals(fieldName) || normalizedTargetField.equals(fieldName)) {
                model.removeRow(i);
                bUpdate = true;
                bRet = true;
            }
            if (bUpdate) {
                mMatchFieldDef.setModified(true);
                mEviewEditorMainApp.enableSaveAction(true);
                ArrayList alNormalizationGroups = mMatchFieldDef.getNormalizationGroups();
                for (int j=0; alNormalizationGroups != null && j < alNormalizationGroups.size(); j++) {
                    NormalizationGroup group = (NormalizationGroup) alNormalizationGroups.get(j);
                    if (group != null) {
                        if (group.getLocaleFieldName() != null && group.getLocaleFieldName().equals(fieldName)) {
                            group.setLocaleFieldName("");
                        }
                        if (group.getUnnormalizedSourceFieldName().equals(fieldName) ||
                            group.getNormalizedSourceFieldName().equals(fieldName)) {
                            alNormalizationGroups.remove(group);
                        }
                    }
                }
            }
        }
        return bRet;
    }
    
    // Table model for Normalization Types
    class TableNormalizationTypes extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabNormalizationPanel.class, "LBL_Type"), 
                                         //NbBundle.getMessage(TabNormalizationPanel.class, "LBL_AB_Unnormalized_Field_ID"),
                                         NbBundle.getMessage(TabNormalizationPanel.class, "LBL_Unnormalized_Source_Field"),
                                         //NbBundle.getMessage(TabNormalizationPanel.class, "LBL_AB_Normalized_Field_ID"),
                                         NbBundle.getMessage(TabNormalizationPanel.class, "LBL_Normalized_Target_Field")
                                        };
        
        ArrayList normalizationTypeRows;
        final int iColStandardizationType = 0;
        final int iColUnnormalizedFieldID = 3;  // hidden
        final int iColUnnormalizedSourceField = 1;
        final int iColNormalizedFieldID = 4;    // hidden
        final int iColNormalizedTargetField = 2;
        
        TableNormalizationTypes(ArrayList rows) {
            normalizationTypeRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (normalizationTypeRows != null) {
                return normalizationTypeRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (normalizationTypeRows != null)
            {
                NormalizationTypeRow singleRow = (NormalizationTypeRow) normalizationTypeRows.get(row);

                if (singleRow != null) {
                    switch (col) {
                        case iColStandardizationType:
                            return singleRow.getStandardizationType();
                        case iColUnnormalizedFieldID:
                            return singleRow.getUnnormalizedFieldID();
                        case iColUnnormalizedSourceField:
                            return singleRow.getUnnormalizedSourceField();
                        case iColNormalizedFieldID:
                            return singleRow.getNormalizedFieldID();
                        case iColNormalizedTargetField:
                            return singleRow.getNormalizedTargetField();                            
                        default:
                            return null;
                    }
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
            if (normalizationTypeRows != null) {
                NormalizationTypeRow singleRow = (NormalizationTypeRow) normalizationTypeRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColStandardizationType:
                            singleRow.setStandardizationType(value.toString());
                            break;
                        case iColUnnormalizedFieldID:
                            singleRow.setUnnormalizedFieldID(value.toString());
                            break;
                        case iColUnnormalizedSourceField:
                            singleRow.setUnnormalizedSourceField(value.toString());
                            break;
                        case iColNormalizedFieldID:
                            singleRow.setNormalizedFieldID(value.toString());
                            break;
                        case iColNormalizedTargetField:
                            singleRow.setNormalizedTargetField(value.toString());                            
                            break;
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
            }
        }
        
        
        public void addRow(NormalizationTypeRow singleRow) {
            normalizationTypeRows.add(singleRow);
            this.fireTableRowsInserted(0, getRowCount());
        }

        public void removeRow(int index) {
            normalizationTypeRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
    }
    
    class NormalizationTypeRow {
        private String standardizationType;
        private String unnormalizedSourceField;
        private String unnormalizedSourceFieldId;
        private String normalizedTargetField;
        private String normalizedSourceFieldId;

        NormalizationTypeRow(String standardizationType, 
                             String unnormalizedSourceFieldId, 
                             String unnormalizedSourceField, 
                             String normalizedSourceFieldId,
                             String normalizedTargetField) {
            this.standardizationType = standardizationType;
            this.unnormalizedSourceFieldId = unnormalizedSourceFieldId;            
            this.unnormalizedSourceField = unnormalizedSourceField;
            this.normalizedSourceFieldId = normalizedSourceFieldId;
            this.normalizedTargetField = normalizedTargetField;
        }

        public String getStandardizationType() {
            return standardizationType;
        }

        public String getUnnormalizedSourceField() {
            return unnormalizedSourceField;
        }
        
        public String getUnnormalizedFieldID() {
            return unnormalizedSourceFieldId;
        }

        public String getNormalizedTargetField() {
            return normalizedTargetField;
        }

        public String getNormalizedFieldID() {
            return normalizedSourceFieldId;
        }
        
        public void setStandardizationType(String standardizationType) {
            this.standardizationType = standardizationType;
        }
        
        public void setUnnormalizedSourceField(String unnormalizedSourceField) {
            this.unnormalizedSourceField = unnormalizedSourceField;
        }

        public void setUnnormalizedFieldID(String unnormalizedSourceFieldId) {
            this.unnormalizedSourceFieldId = unnormalizedSourceFieldId;
        }

        public void setNormalizedTargetField(String normalizedTargetField) {
            this.normalizedTargetField = normalizedTargetField;
        }

        public void setNormalizedFieldID(String normalizedSourceFieldId) {
            this.normalizedSourceFieldId = normalizedSourceFieldId;
        }
        
    }
}
