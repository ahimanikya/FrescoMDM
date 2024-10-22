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
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;

import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.index.project.EviewApplication.MatchRuleRow;
import com.sun.mdm.index.project.ui.applicationeditor.matching.AdvancedMatchConfigDialog;
import com.sun.mdm.index.parser.MatchFieldDef;
import com.sun.mdm.index.parser.MasterType;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.util.LogUtil;

import com.sun.mdm.matcher.comparators.configurator.ComparatorsConfigBean;
import com.sun.mdm.matcher.comparators.ComparatorsManager;

public class TabMatchConfigPanel extends javax.swing.JPanel {
    private static Logger mLogger = LogUtil.getLogger(TabMatchConfigPanel.class);
    private EviewEditorMainApp mEviewEditorMainApp;
    private EviewApplication mEviewApplication;
    private MatchFieldDef mMatchFieldDef;
    private MasterType mMasterType;
    private com.sun.mdm.index.parser.Utils.Parameter parameterDuplicateThreshold;
    private com.sun.mdm.index.parser.Utils.Parameter parameterMatchThreshold;
    private ArrayList mAlMatchRuleRows;
    private String mProbabilityType;
    private ArrayList mAlFunctions;
    private ArrayList mAlFunctionsDesc;
    private Map mMapDescKeyFunctions = new HashMap();;
    private Map mMapShortKeyFunctions = null;
    private Map mSecondaryCodeNameDesc;
    private Map<String, Map<String, ArrayList>> mMapParams;
    private ArrayList mAlNullFields;
    private ArrayList mAlNullFieldsDesc;
    private Map mMapNullFields = null;
    private JTable mTableFieldsMatchTypeSelected;    
    private JTable mTableMatchingRules;
    private boolean bCheckedOut;
    private String mComparatorListPath;
    
    /** Creates new form TabMatchConfigPanel */
    public TabMatchConfigPanel(EviewEditorMainApp eviewEditorMainApp, EviewApplication eviewApplication) {
        initComponents();
        mEviewEditorMainApp = eviewEditorMainApp;
        bCheckedOut = eviewEditorMainApp.isCheckedOut();
        mEviewApplication = eviewApplication;
        FileObject fo = mEviewApplication.getComparatorListFile();
        mComparatorListPath = FileUtil.toFile(fo).getAbsolutePath();
        mMasterType = mEviewApplication.getMasterType(false);
        mMatchFieldDef = mEviewApplication.getMatchFieldDef(false);
        parameterDuplicateThreshold = mMasterType.getDecisionMakerConfigParameterByName(MasterType.DUPLICATETHRESHOLD);
        if (parameterDuplicateThreshold != null) {
            jTextFieldDuplicateThreshold.setText(parameterDuplicateThreshold.getValue());
        }
        parameterMatchThreshold = mMasterType.getDecisionMakerConfigParameterByName(MasterType.MATCHTHRESHOLD);
        if (parameterMatchThreshold != null) {
            jTextFieldMatchThreshold.setText(parameterMatchThreshold.getValue());
        }
        jComboBoxProbabilityType.setEnabled(bCheckedOut);
        jTextFieldDuplicateThreshold.setEnabled(bCheckedOut);
        jTextFieldMatchThreshold.setEnabled(bCheckedOut);
        jButtonAdd.setEnabled(bCheckedOut);
        
        mProbabilityType = mEviewApplication.getMatchingProbabilityType();
        mAlMatchRuleRows = mEviewApplication.getMatchRuleRows();   //get matching rules
        createFieldsMatchTypeSelectedTable();
        createMatchFieldsTable();
        // Call this after the table is created
        jComboBoxProbabilityType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Use Agree/Disagreement Weight Ranges", "Use M-Probabilities/U-Probabilities"}));
        jComboBoxProbabilityType.setSelectedIndex(mProbabilityType.equals("0") ? 1 : 0);
        jComboBoxProbabilityType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onProbabilityTypeItemStateChanged(evt);
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

        jLabelProbabilityType = new javax.swing.JLabel();
        jComboBoxProbabilityType = new javax.swing.JComboBox();
        jLabelDuplicateThreshold = new javax.swing.JLabel();
        jLabelMatchThreshold = new javax.swing.JLabel();
        jTextFieldDuplicateThreshold = new javax.swing.JTextField();
        jTextFieldMatchThreshold = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneMatchingRules = new javax.swing.JScrollPane();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonAdvanced = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPaneFieldsMatchTypeSelected = new javax.swing.JScrollPane();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle"); // NOI18N
        jLabelProbabilityType.setText(bundle.getString("LBL_Probability_Type")); // NOI18N

        jLabelDuplicateThreshold.setText(bundle.getString("LBL_DuplicateThreshold")); // NOI18N

        jLabelMatchThreshold.setText(bundle.getString("LBL_MatchThreshold")); // NOI18N

        jTextFieldDuplicateThreshold.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onDuplicateThresholdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onDuplicateThresholdKeyTyped(evt);
            }
        });

        jTextFieldMatchThreshold.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onMatchThresholdKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onMatchThresholdKeyTyped(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Matching_Rules"))); // NOI18N

        jButtonAdd.setMnemonic('A');
        jButtonAdd.setText(bundle.getString("LBL_Add")); // NOI18N
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddMatchRuleActionPerformed(evt);
            }
        });

        jButtonRemove.setMnemonic('R');
        jButtonRemove.setText(bundle.getString("LBL_Remove")); // NOI18N
        jButtonRemove.setEnabled(false);
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveMatchRuleActionPerformed(evt);
            }
        });

        jButtonAdvanced.setMnemonic('E');
        jButtonAdvanced.setText(bundle.getString("MSG_button_Edit")); // NOI18N
        jButtonAdvanced.setEnabled(false);
        jButtonAdvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdvancedMatchRuleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jScrollPaneMatchingRules, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(304, 304, 304)
                .add(jButtonAdd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonRemove, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonAdvanced, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneMatchingRules, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonAdd)
                    .add(jButtonRemove)
                    .add(jButtonAdvanced)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Fields_Matching_Type_Selected"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jScrollPaneFieldsMatchTypeSelected, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPaneFieldsMatchTypeSelected, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jLabelProbabilityType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxProbabilityType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jLabelDuplicateThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldDuplicateThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(388, 388, 388)
                .add(jLabelMatchThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldMatchThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 560, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelProbabilityType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxProbabilityType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDuplicateThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldDuplicateThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelMatchThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldMatchThreshold, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void onMatchThresholdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onMatchThresholdKeyReleased
        parameterMatchThreshold.setValue(this.jTextFieldMatchThreshold.getText());
    }//GEN-LAST:event_onMatchThresholdKeyReleased

    private void onDuplicateThresholdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onDuplicateThresholdKeyReleased
        parameterDuplicateThreshold.setValue(this.jTextFieldDuplicateThreshold.getText());
    }//GEN-LAST:event_onDuplicateThresholdKeyReleased

    private void onMatchThresholdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onMatchThresholdKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldMatchThreshold.getText().indexOf('.') >= 0)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        } else {
            mMasterType.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            if (c == '\n') {
                jTextFieldDuplicateThreshold.requestFocus();
            }
        }
    }//GEN-LAST:event_onMatchThresholdKeyTyped

    private void onDuplicateThresholdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onDuplicateThresholdKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldDuplicateThreshold.getText().indexOf('.') >= 0)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        } else {
            mMasterType.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            if (c == '\n') {
                jTextFieldMatchThreshold.requestFocus();
            }
        }
    }//GEN-LAST:event_onDuplicateThresholdKeyTyped

    private void onAdvancedMatchRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAdvancedMatchRuleActionPerformed
        int row = mTableMatchingRules.getSelectedRow();
        TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
        String matchType = (String) model.getValueAt(row, model.iColMatchType);
        String matchSize = (String) model.getValueAt(row, model.iColSize);
        String nullField = (String) model.getValueAt(row, model.iColNullField);
        String function = (String) model.getValueAt(row, model.iColFunction);
        String agreementW = (String) model.getValueAt(row, model.iColMProbAgreementWeight);
        String disagreementW = (String) model.getValueAt(row, model.iColUProbDisagreementWeight);
        String parameters = (String) model.getValueAt(row, model.iColParameters);
        
        mProbabilityType = getProbabilityType();
        AdvancedMatchConfigDialog dlg = new AdvancedMatchConfigDialog(mProbabilityType, matchType, matchSize,
                nullField, function, agreementW, disagreementW, parameters, 
                mMapDescKeyFunctions, mAlFunctionsDesc, mMapParams);
        dlg.setVisible(true);
        if (dlg.isModified()) {
            // update table
            model.setValueAt(dlg.getMatchType(), row, model.iColMatchType);
            model.setValueAt(dlg.getMatchSize(), row, model.iColSize);
            model.setValueAt(dlg.getNullField(), row, model.iColNullField);
            if (mProbabilityType.equals("0")) {
                model.setValueAt(dlg.getMProbability(), row, model.iColMProbAgreementWeight);
                model.setValueAt(dlg.getUProbability(), row, model.iColUProbDisagreementWeight);
            } else {
                model.setValueAt(dlg.getAgreementWeight(), row, model.iColMProbAgreementWeight);
                model.setValueAt(dlg.getDisagreementWeight(), row, model.iColUProbDisagreementWeight);
            }
            model.setValueAt(dlg.getFunction(), row, model.iColFunction);
            model.setValueAt(dlg.getParameters(), row, model.iColParameters);
            // setModified
            mEviewApplication.setModifiedMatchConfig(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }

    }//GEN-LAST:event_onAdvancedMatchRuleActionPerformed

    private void onRemoveMatchRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveMatchRuleActionPerformed
        int rs[] = mTableMatchingRules.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(TabMatchConfigPanel.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(TabMatchConfigPanel.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(TabMatchConfigPanel.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                model.removeRow(j);
            }
            jButtonRemove.setEnabled(false);
            jButtonAdvanced.setEnabled(false);
            // setModified
            mEviewApplication.setModifiedMatchConfig(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }
        
    }//GEN-LAST:event_onRemoveMatchRuleActionPerformed
    
    private void onProbabilityTypeItemStateChanged(java.awt.event.ItemEvent evt) {                                                   
        TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
        TableColumn colMProbAgreementW = mTableMatchingRules.getColumnModel().getColumn(model.iColMProbAgreementWeight);        
        TableColumn colUProbDisagreementW = mTableMatchingRules.getColumnModel().getColumn(model.iColUProbDisagreementWeight);
        mProbabilityType = getProbabilityType();
        if (mProbabilityType.equals("0")) {
            colMProbAgreementW.setHeaderValue(NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MProb"));
            colUProbDisagreementW.setHeaderValue(NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_UProb"));
        } else {
            colMProbAgreementW.setHeaderValue(NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_AgreementWeight"));
            colUProbDisagreementW.setHeaderValue(NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_DisagreementWeight"));
        }
        model.fireTableDataChanged();
        // setModified
        mEviewApplication.setModifiedMatchConfig(true);
        mEviewEditorMainApp.enableSaveAction(true);
    }                                                  

    private void onAddMatchRuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddMatchRuleActionPerformed
        AdvancedMatchConfigDialog dlg;
        mProbabilityType = getProbabilityType();
        if (mProbabilityType.equals("0")) {
            dlg = new AdvancedMatchConfigDialog(mProbabilityType, "", "30",
                "0", "us", "0.99", "0.001", "", 
                mMapDescKeyFunctions, mAlFunctionsDesc, mMapParams);
        } else {
            dlg = new AdvancedMatchConfigDialog(mProbabilityType, "", "30",
                "0", "us", "10", "-10", "", 
                mMapDescKeyFunctions, mAlFunctionsDesc, mMapParams);
        }
        dlg.setVisible(true);
        if (dlg.isModified()) {
            String f = dlg.getFunction(); // descriptive function name
            String shortFunctionName = this.mMapDescKeyFunctions.get(f).toString();
            
            MatchRuleRow matchRuleRow = mEviewApplication.createMatchRuleRow(dlg.getMatchType(), 
                                                                                dlg.getMatchSize(), 
                                                                                dlg.getNullField(),
                                                                                shortFunctionName, //dlg.getFunction(), 
                                                                                dlg.getMProbability(), 
                                                                                dlg.getUProbability(),
                                                                                dlg.getAgreementWeight(), 
                                                                                dlg.getDisagreementWeight(),
                                                                                dlg.getParameters());
            MatchRuleRowPerProbType r = new MatchRuleRowPerProbType(matchRuleRow);
        
            int iInsertRow = mTableMatchingRules.getRowCount();
            ((TableModelMatchRules) mTableMatchingRules.getModel()).addRow(iInsertRow, r);
            mTableMatchingRules.clearSelection();
            mTableMatchingRules.addRowSelectionInterval(iInsertRow, iInsertRow);           
            mTableMatchingRules.setEditingRow(iInsertRow);
            mTableMatchingRules.setEditingColumn(0);
            jButtonRemove.setEnabled(bCheckedOut);
            jButtonAdvanced.setEnabled(bCheckedOut);
            // setModified
            mEviewApplication.setModifiedMatchConfig(true);
            mEviewEditorMainApp.enableSaveAction(true);
        }
    }//GEN-LAST:event_onAddMatchRuleActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAdvanced;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JComboBox jComboBoxProbabilityType;
    private javax.swing.JLabel jLabelDuplicateThreshold;
    private javax.swing.JLabel jLabelMatchThreshold;
    private javax.swing.JLabel jLabelProbabilityType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPaneFieldsMatchTypeSelected;
    private javax.swing.JScrollPane jScrollPaneMatchingRules;
    private javax.swing.JTextField jTextFieldDuplicateThreshold;
    private javax.swing.JTextField jTextFieldMatchThreshold;
    // End of variables declaration//GEN-END:variables
    
    private void createFieldsMatchTypeSelectedTable() {
        // load TableModelMatchRules
        ArrayList rows = new ArrayList();
        ArrayList alMatchColumns = mMatchFieldDef.getMatchColumns();
        
        for (int i=0; alMatchColumns != null && i < alMatchColumns.size(); i++) {
            MatchFieldDef.MatchColumn mc = (MatchFieldDef.MatchColumn) alMatchColumns.get(i);
            FieldMatchTypeSelectedRow r = new FieldMatchTypeSelectedRow(mc.getMatchType(), mc.getOriginalColumnName());
            rows.add(r);
        }
        TableModelFieldsMatchTypeSelected model = new TableModelFieldsMatchTypeSelected(rows);

        mTableFieldsMatchTypeSelected = new JTable(model);
        mTableFieldsMatchTypeSelected.getTableHeader().setReorderingAllowed(false);
        mTableFieldsMatchTypeSelected.getColumnModel().getColumn(0).setPreferredWidth(420); 
        mTableFieldsMatchTypeSelected.getColumnModel().getColumn(1).setPreferredWidth(140);       

        jScrollPaneFieldsMatchTypeSelected.setViewportView(mTableFieldsMatchTypeSelected);
        mTableFieldsMatchTypeSelected.setEnabled(bCheckedOut);
        mTableFieldsMatchTypeSelected.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    TableModelFieldsMatchTypeSelected model = (TableModelFieldsMatchTypeSelected) mTableFieldsMatchTypeSelected.getModel();
                    int iSelectedRow = mTableFieldsMatchTypeSelected.getSelectedRow();
                    String matchType = (String) model.getValueAt(iSelectedRow, model.iColMatchType);
                    if (matchType != null) {
                        iSelectedRow = ((TableModelMatchRules) mTableMatchingRules.getModel()).findRowByMatchType(matchType);
                        mTableMatchingRules.clearSelection();
                        if (iSelectedRow >=0) {
                            mTableMatchingRules.addRowSelectionInterval(iSelectedRow, iSelectedRow);           
                            mTableMatchingRules.setEditingRow(iSelectedRow);
                            mTableMatchingRules.setEditingColumn(0);
                            jButtonAdvanced.setEnabled(bCheckedOut);
                        }
                    }
                }
            });
    }
    // Table model for Rule Definitions
    class TableModelFieldsMatchTypeSelected extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_Field_Match_Type_Selected"),
                                         NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MatchType"), 
                                        };
        ArrayList matchFieldRows;
        final static int iColFieldName = 0;
        final static int iColMatchType = 1;
        
        TableModelFieldsMatchTypeSelected(ArrayList rows) {
            matchFieldRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (matchFieldRows != null) {
                return matchFieldRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (matchFieldRows != null) {
                FieldMatchTypeSelectedRow singleRow = (FieldMatchTypeSelectedRow) matchFieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColMatchType:
                           return singleRow.getMatchType();
                        case iColFieldName:
                            return singleRow.getFieldName();
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
            if (matchFieldRows != null && row >=0 && row < matchFieldRows.size()) {
                FieldMatchTypeSelectedRow singleRow = (FieldMatchTypeSelectedRow) matchFieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColMatchType:
                            singleRow.setMatchType((String) value);                            
                            break;
                        case iColFieldName:
                            singleRow.setFieldName((String) value);                            
                            break;
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
            }
        }
        
        public void removeRow(int index) {
            matchFieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, FieldMatchTypeSelectedRow row) {
            matchFieldRows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public FieldMatchTypeSelectedRow getRow(int index) {
            FieldMatchTypeSelectedRow row = (FieldMatchTypeSelectedRow) matchFieldRows.get(index);
            return row;
        }

        public int findRowByFieldName(String fullFieldName) {
            for (int i=0; i < matchFieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fullFieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public int findRowByMatchType(String matchType) {
            for (int i=0; i < matchFieldRows.size(); i++) {
                if (getValueAt(i, iColMatchType).equals(matchType)) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    class FieldMatchTypeSelectedRow {
        private String matchType;
        private String fieldName;
        
        FieldMatchTypeSelectedRow(String matchType, String fieldName) {
            this.matchType = matchType;
            this.fieldName = fieldName;
        }
        
        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public String getMatchType() {
            return this.matchType;
        }
        
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return this.fieldName;
        }
    }
    
    private void createMatchFieldsTable() {
        mAlNullFieldsDesc = AdvancedMatchConfigDialog.getNullFieldsDesc();
        mAlNullFields = AdvancedMatchConfigDialog.getNullFields();
        mMapNullFields = AdvancedMatchConfigDialog.getMapNullFields();
        //Get comparator and description from comparatorsList.xml
        try {
            ComparatorsManager mComparatorsManager = new ComparatorsManager(mComparatorListPath);
            ComparatorsConfigBean instance = mComparatorsManager.getComparatorsConfigBean();
            Map mapCodeNameDesc = instance.getCodeNamesDesc();
            mSecondaryCodeNameDesc = instance.getSecondaryCodeNamesDesc();
            mMapShortKeyFunctions = mapCodeNameDesc;
            mAlFunctionsDesc = new ArrayList<String>();
            mAlFunctions = new ArrayList<String>();
        
            Set set = mMapShortKeyFunctions.keySet();
            Iterator iter = set.iterator();
            for (int i=0; i < set.size(); i++) {
                String strFunction = iter.next().toString();
                String strDesc = mMapShortKeyFunctions.get(strFunction).toString();
                mAlFunctionsDesc.add(strDesc);
                mAlFunctions.add(strFunction);
                mMapDescKeyFunctions.put(strDesc, strFunction);  
            }
            mMapParams = instance.getParametersDetailsForGUI();
        } catch (Exception ex) {
            mLogger.error(ex.getMessage());
        }
        
        // load TableModelMatchRules
        ArrayList rows = new ArrayList();
        for (int i=0; mAlMatchRuleRows != null && i < mAlMatchRuleRows.size(); i++) {
            MatchRuleRow matchRuleRow = (MatchRuleRow) mAlMatchRuleRows.get(i);
            MatchRuleRowPerProbType r = new MatchRuleRowPerProbType(matchRuleRow);
            rows.add(r);
        }
        TableModelMatchRules tableModelMatchFields = new TableModelMatchRules(rows);

        mTableMatchingRules = new JTable(tableModelMatchFields);
        mTableMatchingRules.getTableHeader().setReorderingAllowed(false);
        mTableMatchingRules.getColumnModel().getColumn(0).setPreferredWidth(140); 
        mTableMatchingRules.getColumnModel().getColumn(1).setPreferredWidth(80);       
        mTableMatchingRules.getColumnModel().getColumn(2).setPreferredWidth(200);   
        mTableMatchingRules.getColumnModel().getColumn(3).setPreferredWidth(70);   
        mTableMatchingRules.getColumnModel().getColumn(4).setPreferredWidth(70);   
        mTableMatchingRules.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        if (bCheckedOut) {
                            onAdvancedMatchRuleActionPerformed(null);
                        }
                    } else { 
                        jButtonRemove.setEnabled(bCheckedOut);
                        jButtonAdvanced.setEnabled(bCheckedOut);
                    }
                    //Find it in mTableFieldsMatchTypeSelected
                    int iSelectedRow = mTableMatchingRules.getSelectedRow();
                    TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
                    String matchType = (String) model.getValueAt(iSelectedRow, model.iColMatchType);
                    if (matchType != null) {    
                        iSelectedRow = ((TableModelFieldsMatchTypeSelected) mTableFieldsMatchTypeSelected.getModel()).findRowByMatchType(matchType);
                        mTableFieldsMatchTypeSelected.clearSelection();
                        if (iSelectedRow >=0) {
                            mTableFieldsMatchTypeSelected.addRowSelectionInterval(iSelectedRow, iSelectedRow);           
                            mTableFieldsMatchTypeSelected.setEditingRow(iSelectedRow);
                            mTableFieldsMatchTypeSelected.setEditingColumn(0);
                        }
                    }

                }
            });
       
        jScrollPaneMatchingRules.setViewportView(mTableMatchingRules);
        mTableMatchingRules.setEnabled(bCheckedOut);
    }
    
    // Table model for Rule Definitions
    class TableModelMatchRules extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MatchType"), 
                                         NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MatchSize"),
                                         //NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_NullField"),
                                         NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_Function"),     
                                         //NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MProb"),
                                         //NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_UProb"),
                                         NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_AgreementWeight"),
                                         NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_DisagreementWeight"),
                                         //NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_Parameters"),        
                                        };
        ArrayList matchRuleRows;
        final static int iColMatchType = 0;
        final static int iColSize = 1;
        final static int iColFunction = 2;
        final static int iColMProbAgreementWeight = 3;
        final static int iColUProbDisagreementWeight = 4;
        final static int iColNullField = 5; // hide it
        //final static int iColAgreementWeight = 6;
        //final static int iColDisagreementWeight = 7;
        final static int iColParameters = 6;    // hide it

        
        TableModelMatchRules(ArrayList rows) {
            matchRuleRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (matchRuleRows != null) {
                return matchRuleRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            if (col == this.iColMProbAgreementWeight) {
                if (mProbabilityType.equals("0")) {
                    return NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_MProb");
                } else {
                    return NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_AgreementWeight");
                }
            }
            if (col == this.iColUProbDisagreementWeight) {
                if (mProbabilityType.equals("0")) {
                    return NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_UProb");
                } else {
                    return NbBundle.getMessage(TabMatchConfigPanel.class, "LBL_DisagreementWeight");
                }
            }

            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (matchRuleRows != null) {
                MatchRuleRowPerProbType singleRow = (MatchRuleRowPerProbType) matchRuleRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColMatchType:
                           return singleRow.getMatchType();
                        case iColSize:
                            return singleRow.getSize();
                        case iColNullField:
                            return singleRow.getNullField();
                        case iColFunction:
                            return singleRow.getFunction();                            
                        case iColMProbAgreementWeight:
                            return singleRow.getMProbAgreementWeight();
                        case iColUProbDisagreementWeight:
                            return singleRow.getUProbDisagreementWeight();
                       case iColParameters:
                            return singleRow.getParameters();
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
            if (matchRuleRows != null && row >=0 && row < matchRuleRows.size()) {
                MatchRuleRowPerProbType singleRow = (MatchRuleRowPerProbType) matchRuleRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColMatchType:
                            singleRow.setMatchType((String) value);                            
                            break;
                        case iColSize:
                            singleRow.setSize((String) value);                            
                            break;
                        case iColNullField:
                            singleRow.setNullField((String) value);                            
                            break;
                        case iColFunction:
                            singleRow.setFunction((String) value);                            
                            break;
                        case iColMProbAgreementWeight:
                            singleRow.setMProbAgreementWeight((String) value);                            
                            break;
                        case iColUProbDisagreementWeight:
                            singleRow.setUProbDisagreementWeight((String) value);                            
                            break;
                        case iColParameters:
                            singleRow.setParameters((String) value);                            
                            break;                           
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
            }
        }
        
        public void removeRow(int index) {
            matchRuleRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, MatchRuleRowPerProbType row) {
            matchRuleRows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public MatchRuleRowPerProbType getRow(int index) {
            MatchRuleRowPerProbType row = (MatchRuleRowPerProbType) matchRuleRows.get(index);
            return row;
        }

        public int findRowByMatchType(String matchType) {
            for (int i=0; i < matchRuleRows.size(); i++) {
                if (getValueAt(i, iColMatchType).equals(matchType)) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    class MatchRuleRowPerProbType {
        private String matchType;
        private String size;
        private String nullField;
        private String function;
        private String agreementW;
        private String disagreementW;
        private String mprob;
        private String uprob;
        private String parameters = "";
        
        MatchRuleRowPerProbType(MatchRuleRow matchRuleRow) {
            this.matchType = matchRuleRow.getMatchType();
            this.size = matchRuleRow.getSize();
            this.nullField = matchRuleRow.getNullField();
            //String f = matchRuleRow.getFunction(); // short function name
            this.function = (String) mMapShortKeyFunctions.get(matchRuleRow.getFunction());    // b1 -> full descriptive name
            if (this.function == null) {
                String strFunction = matchRuleRow.getFunction();
                String mappedFunction = (String) mSecondaryCodeNameDesc.get(strFunction);
                this.function = (String) mMapShortKeyFunctions.get(mappedFunction);
            }
            this.parameters = matchRuleRow.getParameters();
            
            this.mprob = matchRuleRow.getMProb();
            this.uprob = matchRuleRow.getUProb();                
            this.agreementW = matchRuleRow.getAgreementWeight();
            this.disagreementW = matchRuleRow.getDisagreementWeight();                
        }

    	public String getMatchType() {
            return this.matchType;
        }

        public String getSize() {
            return this.size;
        }
        
        public String getNullField() {
            return this.nullField;
        }
        
        public String getFunction() {
            return this.function;
        }
        
        public String getShortFunction() {
            String shortFunction = (String) mMapDescKeyFunctions.get(this.function);
            return shortFunction;
        }
        
        public String getMProbAgreementWeight() {
            if (mProbabilityType.equals("0")) {
                return this.mprob;
            } else {
                return this.agreementW;
            }
        }
        
        public String getUProbDisagreementWeight() {
            if (mProbabilityType.equals("0")) {
                return this.uprob;
            } else {
                return this.disagreementW;
            }
        }
        
        public String getMProb() {
            if (this.mprob.length() == 0) {
                this.mprob = "0.99";
            }
            return this.mprob;
        }
        
        public String getUProb() {
            if (this.uprob.length() == 0) {
                this.uprob = "0.001";
            }

            return this.uprob;
        }
        
        public String getAgreementWeight() {
            if (this.agreementW.length() == 0) {
                this.agreementW = "10";
            }
            return agreementW;
        }
        
        public String getDisagreementWeight() {
            if (this.disagreementW.length() == 0) {
                this.disagreementW = "10";
            }
            return disagreementW;
        }
        
        public String getParameters() {
            return parameters;
        }
        
        public void setMatchType(String matchType) {
            this.matchType = matchType;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public void setNullField(String nullField) {
            this.nullField = nullField;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public void setMProbAgreementWeight(String value) {
            if (mProbabilityType.equals("0")) {
                this.mprob = value;
            } else {
                this.agreementW = value;
            }
        }

        public void setUProbDisagreementWeight(String value) {
            if (mProbabilityType.equals("0")) {
                this.uprob = value;
            } else {
                this.disagreementW = value;
            }
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }
        
    }
    
    public void setTargetMatchType(String matchType, String fullFieldName) {
        int iSelectedRow;
        if (matchType != null) {
            iSelectedRow = ((TableModelMatchRules) mTableMatchingRules.getModel()).findRowByMatchType(matchType);
            if (iSelectedRow >=0) {
                mTableMatchingRules.clearSelection();
                mTableMatchingRules.addRowSelectionInterval(iSelectedRow, iSelectedRow);           
                mTableMatchingRules.setEditingRow(iSelectedRow);
                mTableMatchingRules.setEditingColumn(0);
                jButtonAdvanced.setEnabled(bCheckedOut);
            }
        }
        
        TableModelFieldsMatchTypeSelected model = (TableModelFieldsMatchTypeSelected) mTableFieldsMatchTypeSelected.getModel();    
        iSelectedRow = model.findRowByFieldName(fullFieldName);
        if (iSelectedRow >=0) {
            if (matchType == null) {
                model.removeRow(iSelectedRow);
                iSelectedRow = -1;
            } else {    // replace if matchType is not matched
                String oldMatchType = (String) model.getValueAt(iSelectedRow, model.iColMatchType);
                if (!oldMatchType.equals(matchType)) {
                    model.setValueAt(matchType, iSelectedRow, model.iColMatchType);
                }
            }
        } else if (matchType != null) {
            // add
            FieldMatchTypeSelectedRow r = new FieldMatchTypeSelectedRow(matchType, fullFieldName);
            iSelectedRow = model.getRowCount();
            model.addRow(iSelectedRow,  r);
        }
        mTableFieldsMatchTypeSelected.clearSelection();
        if (iSelectedRow >=0) {
            mTableFieldsMatchTypeSelected.addRowSelectionInterval(iSelectedRow, iSelectedRow);           
            mTableFieldsMatchTypeSelected.setEditingRow(iSelectedRow);
        }
    }
    
    /* Called by EntityNode when field name changed
     *@param oldName
     *@param newName
     *
     * Update Field Name in the table
     */
    public boolean updateReferencedField(String oldNodeNameMefa, String newName) {
        boolean bRet = false;
        int index;
        String oldNameRegex = oldNodeNameMefa.replaceAll("\\[", "\\\\["); 
        oldNameRegex = oldNameRegex.replaceAll("\\*","\\\\*");
        oldNameRegex = oldNameRegex.replaceAll("\\]","\\\\]");

        TableModelFieldsMatchTypeSelected model = (TableModelFieldsMatchTypeSelected) mTableFieldsMatchTypeSelected.getModel();    
        for (int i=0; i<model.getRowCount(); i++) {
            // update Field Name column
            String fieldName = (String) model.getValueAt(i, model.iColFieldName);
            index = fieldName.indexOf(oldNodeNameMefa);
            if (fieldName.equals(oldNodeNameMefa) || 
                (index >= 0 &&
                 fieldName.length() > oldNodeNameMefa.length() &&
                 (index + oldNodeNameMefa.length() < fieldName.length() &&
                    (fieldName.charAt(index + oldNodeNameMefa.length() - 1) == '.' ||
                     fieldName.charAt(index + oldNodeNameMefa.length() - 1) == ';')))) {
                
                String newValue = fieldName.replaceAll(oldNameRegex, newName);
                model.setValueAt(newValue, i, model.iColFieldName);
                bRet = true;
            }
        }
        
        if (bRet) {
            ArrayList alMatchColumns = mMatchFieldDef.getMatchColumns();
            for (int i=0; alMatchColumns != null && i < alMatchColumns.size(); i++) {
                MatchFieldDef.MatchColumn mc = (MatchFieldDef.MatchColumn) alMatchColumns.get(i);
                String oldValue = mc.getColumnName();
                String newValue = oldValue.replaceAll(oldNameRegex, newName);
                mc.setColumnName(newValue);
            }
        }
        return bRet;
    }
    
    /* Called by EntityNode when field removed
     *@param fieldNameMefa
     *
     * Remove rows that match Field Name in the table
     */
    public boolean removeReferencedField(String fieldNameMefa) {
        boolean bRet = false;
        int index;

        TableModelFieldsMatchTypeSelected model = (TableModelFieldsMatchTypeSelected) mTableFieldsMatchTypeSelected.getModel();
        for (int i=model.getRowCount() - 1; i>=0 && i < model.getRowCount(); i--) {
            String fieldName = (String) model.getValueAt(i, model.iColFieldName);
            if (fieldName.equals(fieldNameMefa)) {
                model.removeRow(i);
                bRet = true;
            }
        }
        
        if (bRet) {
            ArrayList alMatchColumns = mMatchFieldDef.getMatchColumns();
            for (int i=alMatchColumns.size() - 1; i>=0 && i < alMatchColumns.size(); i--) {
                MatchFieldDef.MatchColumn mc = (MatchFieldDef.MatchColumn) alMatchColumns.get(i);
                if (mc.getColumnName().equals("Enterprise.SystemSBR." + fieldNameMefa)) {
                    alMatchColumns.remove(i);
                }
            }
        }

        return bRet;
    }
    
    public String getProbabilityType() {
        return (jComboBoxProbabilityType.getSelectedIndex() == 0) ? "1" : "0";
    }
    
    public ArrayList getMatchTypeList() {
        ArrayList alMatchTypes = new ArrayList();
        TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
        for (int i=0; i<model.getRowCount(); i++) {
            String matchType = (String) model.getValueAt(i, model.iColMatchType);
            alMatchTypes.add(matchType);
        }
        return alMatchTypes;
    }
    
    /**
    ** pad a string S with a size of N with char C 
    ** on the left (True) or on the right(flase)
    **/
    public synchronized String paddingString(String s, int n, char c, boolean paddingLeft) {
        StringBuffer str = new StringBuffer(s);
        int strLength  = str.length();
        int l = n;
        if (n <= strLength) {
            l = strLength + 1;
        }
        if (l > 0) {
            for (int i = 0; i <= l ; i ++ ) {
                if (paddingLeft) {
                    if (i < l - strLength ) {
                        str.insert(0, c);
                    }
                }
                else {
                    if (i > strLength) {
                        str.append(c);
                    }
                }
            }
        }
        return str.toString();
    }
    
    /*
     *ProbabilityType            1
     *
     *PrimaryName            30  0   us    0.7   0.007   13  -2 parameters
     */
    public String getMatchConfigFileString() {
        String data = "ProbabilityType            " + getProbabilityType() + "\r\r";
        TableModelMatchRules model = (TableModelMatchRules) mTableMatchingRules.getModel();
        for (int i=0; i<model.getRowCount(); i++) {
            String line = null;
            MatchRuleRowPerProbType row = model.getRow(i);
            String matchType = paddingString((String) row.getMatchType(), 24, ' ',  false);
            String matchSize = paddingString((String) row.getSize(), 4, ' ', false);
            String nullField = paddingString((String) row.getNullField(), 4, ' ', false);
            String function = paddingString((String) row.getShortFunction(), 6, ' ', false);
            String MProb = paddingString((String) row.getMProb(), 8, ' ', false);
            String UProb = paddingString((String) row.getUProb(), 8, ' ', false);
            String agreementW = paddingString((String) row.getAgreementWeight(), 4, ' ', false);
            String disagreementW = paddingString((String) row.getDisagreementWeight(), 4, ' ', false);
            String parameters = (String) row.getParameters();
            
            line = matchType + matchSize +  
                    nullField + function +  
                    MProb + UProb +  
                    agreementW + disagreementW;
            if (parameters.length() > 0) {
                line += parameters;
            }
            int index = line.indexOf('\r');
            if (index == -1) {
                line += '\r';
            } else {
                // remove that extra spaces
                line = line.substring(0,  index + 1);
            }

            data += line;
        }
        String line = data.replace('\r', '\n');
        return line;
    }
    
    /** Get DuplicateThreshold
     * @return DuplicateThreshold
     */
    public String getDuplicateThreshold() {
        return this.jTextFieldDuplicateThreshold.getText();
    }
    
    /** Get MatchThreshold
     * @return MatchThreshold
     */
    public String getMatchThreshold() {
        return this.jTextFieldMatchThreshold.getText();
    }
}
