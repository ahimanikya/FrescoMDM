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
package com.sun.mdm.index.project.ui.applicationeditor.blocking;

import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import com.sun.mdm.index.parser.QueryType;
import com.sun.mdm.index.parser.QueryType.QueryBuilder;
import com.sun.mdm.index.parser.QueryType.BlockRule;
import com.sun.mdm.index.parser.QueryType.BlockDefinition;
import com.sun.mdm.index.project.ui.applicationeditor.EviewEditorMainApp;

public class BlockingQueryDialog extends javax.swing.JDialog {
    private JTable mTblBlockingRuleDefinitions;
    private QueryType mQueryType;
    private QueryBuilder mQueryBuilder =  null;
    private EviewEditorMainApp mEviewEditorMainApp;
    private boolean mModified = false;
    
    /** Creates new form BlockingQueryDialog */
    public BlockingQueryDialog(EviewEditorMainApp eviewEditorMainApp, QueryBuilder queryBuilder, ArrayList alDefinedBlockDefinitions, boolean editMode, QueryType queryType) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mEviewEditorMainApp = eviewEditorMainApp;
        mQueryType = queryType;
        initComponents();
        // populate properties
        String queryBuilderName="";
        String queryBuilderClass="";
        String parserClass="";
        if (editMode) {
            mQueryBuilder = queryBuilder;
            queryBuilderName = queryBuilder.getQueryBuilderName();
            queryBuilderClass = queryBuilder.getQueryBuilderClass();
            parserClass = queryBuilder.getParserClass();
            jCheckBoxStandardize.setSelected(queryBuilder.getStandardize());    
            jCheckBoxPhoneticize.setSelected(queryBuilder.getPhoneticize());    
        } else {
            queryBuilderClass = QueryType.DEFALUT_BLOCKER_QUERY_BUILDER_CLASS;
            parserClass = QueryType.DEFALUT_BLOCKER_QUERY_BUILDER_PARSER_CLASS;
        }
        jTextFieldQueryBuilderName.setEnabled(!editMode);
        jTextFieldQueryBuilderName.setText(queryBuilderName);
        jTextFieldQueryBuildClass.setText(queryBuilderClass);
        jTextFieldParserClass.setText(parserClass);
        createRuleDefinitionTable(queryBuilder);
        enableBtnOK();
    }
    
    void createRuleDefinitionTable(QueryBuilder queryBuilder) {
        if (mQueryType == null) {
            mTblBlockingRuleDefinitions = null;
            return;
        }        
        // load TableModelRuleDefinitions
        ArrayList rows = new ArrayList();
        if (queryBuilder != null) {
            //ArrayList alSelectedBlockDefinitions = queryBuilder.getSelectedBlockDefinitions();
            ArrayList alDefinedBlockDefinitions = mQueryType.getAllBlockDefinitions();
            for (int i=0; alDefinedBlockDefinitions != null && i < alDefinedBlockDefinitions.size(); i++) {
                BlockDefinition blockDefinition = (BlockDefinition) alDefinedBlockDefinitions.get(i);
                if (blockDefinition.getQueryBuilderName().equals(queryBuilder.getQueryBuilderName())) {
                    BlockRule blockRule = blockDefinition.getBlockRule();
                    String sourceFields = getSourceFieldsString(blockRule);
                    ArrayList alBlockBys = blockRule.getBlockBys();
                    if (alBlockBys != null && alBlockBys.size() > 0 && sourceFields != null) {
                        RuleDefinitionRow r = new RuleDefinitionRow(blockDefinition.getName(), sourceFields);
                        rows.add(r);
                    }
                }
            }
        }
        TableModelRuleDefinitions tableModelRuleDefinitions = new TableModelRuleDefinitions(rows);
        mTblBlockingRuleDefinitions = new JTable(tableModelRuleDefinitions);
        mTblBlockingRuleDefinitions.getTableHeader().setReorderingAllowed(false);
        mTblBlockingRuleDefinitions.getColumnModel().getColumn(0).setPreferredWidth(60); 
        mTblBlockingRuleDefinitions.getColumnModel().getColumn(1).setPreferredWidth(310);       
        mTblBlockingRuleDefinitions.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editBlockingRuleDefinitionActionPerformed(null);
                } else {                    
                    btnRemoveBlockingRuleDefinition.setEnabled(true);
                    btnEditBlockingRuleDefinition.setEnabled(mTblBlockingRuleDefinitions.getSelectedRowCount() == 1);
                }
            }
        });
        jScrollPaneTop.setViewportView(mTblBlockingRuleDefinitions);
    }
    
    // Table model for Rule Definitions
    class TableModelRuleDefinitions extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(BlockingQueryDialog.class, "LBL_RuleName"), 
                                         NbBundle.getMessage(BlockingQueryDialog.class, "LBL_SourceFields")
                                        };
        ArrayList ruleDefinitionRows;
        final int iColRuleName = 0;
        final int iColSourceFields = 1;
        
        TableModelRuleDefinitions(ArrayList rows) {
            ruleDefinitionRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (ruleDefinitionRows != null) {
                return ruleDefinitionRows.size();
            }
            return 0;
        }
        
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (ruleDefinitionRows != null) {
                RuleDefinitionRow singleRow = (RuleDefinitionRow) ruleDefinitionRows.get(row);

                if (singleRow != null) {
                    switch (col) {
                        case iColRuleName:
                           return singleRow.getRuleName();
                        case iColSourceFields:
                            return singleRow.getSourceFields();
                        default:
                            return null;
                    }
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
            if (ruleDefinitionRows != null) {
                RuleDefinitionRow singleRow = (RuleDefinitionRow) ruleDefinitionRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColRuleName:
                            singleRow.setRuleName((String) value);                            
                            break;
                        case iColSourceFields:
                            singleRow.setSources((String) value);                            
                            break;
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
                this.fireTableRowsUpdated(row, row);
            }
        }
        
        public void removeRow(int index) {
            ruleDefinitionRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, RuleDefinitionRow row) {
            ruleDefinitionRows.add(row);
            this.fireTableRowsInserted(index, index);
        }
    }
    
    class RuleDefinitionRow {
    	private String ruleName;
        private String sourceFields;

    	RuleDefinitionRow(String name, String sourceFields) {
            this.ruleName = name;
            this.sourceFields = sourceFields;
    	}

    	public String getRuleName() {
            return ruleName;
    	}

    	public String getSourceFields() {
            return sourceFields;
    	}
        
    	public void setRuleName(String name) {
            this.ruleName = name;
    	}

    	public void setSources(String source) {
            this.sourceFields = source;
    	}
    }
    
    private String getSourceFieldsString(BlockRule blockRule) {
        String sourceFields = null;
        int iFieldCnt = 0;

        ArrayList alBlockBys = blockRule.getBlockBys();
        if (alBlockBys != null) {
            iFieldCnt = alBlockBys.size();
            for (int k=0; k < alBlockBys.size(); k++) {
                QueryType.BlockBy blockBy = (QueryType.BlockBy) alBlockBys.get(k);
                String source;
                if (blockBy.useConstant()) {
                    source = blockBy.getConstant();
                } else {
                    source = blockBy.getSource();
                }
                if (sourceFields == null) {
                    sourceFields = source;
                } else {
                    sourceFields += ";" + source;
                }
            }
        }

        return sourceFields;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jCheckBoxStandardize = new javax.swing.JCheckBox();
        jCheckBoxPhoneticize = new javax.swing.JCheckBox();
        jLabelParserClass = new javax.swing.JLabel();
        jTextFieldParserClass = new javax.swing.JTextField();
        lblQueryBuilderClass = new javax.swing.JLabel();
        jTextFieldQueryBuildClass = new javax.swing.JTextField();
        lblQueryBuilderName = new javax.swing.JLabel();
        jTextFieldQueryBuilderName = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnOK.setEnabled(false);
        btnCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneTop = new javax.swing.JScrollPane();
        btnRemoveBlockingRuleDefinition = new javax.swing.JButton();
        btnEditBlockingRuleDefinition = new javax.swing.JButton();
        btnAddBlockingRuleDefinition = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle"); // NOI18N
        setTitle(bundle.getString("TITLE_Blocking_Query_Builder")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/blocking/Bundle"); // NOI18N
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle1.getString("Title_Query_Builder"))); // NOI18N

        jCheckBoxStandardize.setText("Standardize");
        jCheckBoxStandardize.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxStandardize.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxStandardize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxStandardizeStateChanged(evt);
            }
        });

        jCheckBoxPhoneticize.setText("Phoneticize");
        jCheckBoxPhoneticize.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxPhoneticize.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxPhoneticize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxPhoneticizeStateChanged(evt);
            }
        });

        jLabelParserClass.setText("Parser Class:");

        jTextFieldParserClass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldParserClassKeyReleased(evt);
            }
        });

        lblQueryBuilderClass.setText("Query Builder Class:");

        jTextFieldQueryBuildClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                onQueryBuilderClassKeyReleased(evt);
            }
        });

        lblQueryBuilderName.setText("Query Builder Name:");

        jTextFieldQueryBuilderName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onQueryBuilderNameKeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(lblQueryBuilderName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldQueryBuilderName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 400, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(lblQueryBuilderClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldQueryBuildClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 400, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelParserClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldParserClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 400, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jCheckBoxStandardize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(jCheckBoxPhoneticize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblQueryBuilderName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldQueryBuilderName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblQueryBuilderClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldQueryBuildClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelParserClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldParserClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxStandardize)
                    .add(jCheckBoxPhoneticize)))
        );

        btnOK.setLabel("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setLabel("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("LBL_Blocking_Rule_Definitions"))); // NOI18N

        btnRemoveBlockingRuleDefinition.setText(bundle1.getString("LBL_Remove")); // NOI18N
        btnRemoveBlockingRuleDefinition.setEnabled(false);
        btnRemoveBlockingRuleDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBlockingRuleDefinitionActionPerformed(evt);
            }
        });

        btnEditBlockingRuleDefinition.setText(bundle1.getString("LBL_Edit")); // NOI18N
        btnEditBlockingRuleDefinition.setEnabled(false);
        btnEditBlockingRuleDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBlockingRuleDefinitionActionPerformed(evt);
            }
        });

        btnAddBlockingRuleDefinition.setText(bundle1.getString("LBL_Add")); // NOI18N
        btnAddBlockingRuleDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBlockingRuleDefinitionActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jScrollPaneTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 520, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(284, 284, 284)
                .add(btnAddBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnRemoveBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnEditBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnAddBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnRemoveBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnEditBlockingRuleDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(390, 390, 390)
                .add(btnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnOK)
                    .add(btnCancel)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-564)/2, (screenSize.height-493)/2, 564, 493);
    }// </editor-fold>//GEN-END:initComponents

    private void editBlockingRuleDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBlockingRuleDefinitionActionPerformed
        int iSelectedRow = mTblBlockingRuleDefinitions.getSelectedRow();
        TableModelRuleDefinitions modelR = (TableModelRuleDefinitions) mTblBlockingRuleDefinitions.getModel();
        String queryBuilderName = this.jTextFieldQueryBuilderName.getText();
        if (queryBuilderName == null || queryBuilderName.length() == 0) {
            return;
        }
        String selectedBlockDefinitionName = (String) modelR.getValueAt(iSelectedRow, modelR.iColRuleName);
        BlockDefinition blockDefinition = mQueryType.getBlockDefinitionByName(queryBuilderName, selectedBlockDefinitionName);
        BlockingRuleDefinitionDialog dlg = new BlockingRuleDefinitionDialog(mEviewEditorMainApp.getEviewEditorMainPanel().getEntityTree(true), queryBuilderName, blockDefinition, true);
        dlg.setVisible(true);
        mEviewEditorMainApp.getEviewEditorMainPanel().setEntityTreePane();        
        if (dlg.isModified()) {
            String newBlockDefinitionName = dlg.getBlockDefinitionName();
            BlockRule blockRule = dlg.getBlockRule();
            // check duplicates
            // remove/replace
            if (!newBlockDefinitionName.equals(selectedBlockDefinitionName)) { 
                if (mQueryType.getBlockDefinitionByName(queryBuilderName, newBlockDefinitionName) != null) {
                    // prompt for replacing it
                    String prompt = NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Duplicated_Row_Prompt", newBlockDefinitionName);
                    NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                             prompt, 
                                             NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Duplicated_Row_Prompt_Title"), 
                                             NotifyDescriptor.YES_NO_OPTION);
                    if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
                        mQueryType.deleteBlockDefinitionByName(queryBuilderName, newBlockDefinitionName);
                        for (int i=0; i<modelR.getRowCount(); i++) {
                            if (modelR.getValueAt(i, modelR.iColRuleName).equals(newBlockDefinitionName)) {
                                modelR.removeRow(i);
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                }
                // update blockDefinition with newBlockDefinitionName, hint and blockRule
                mQueryType.updateBlockDefinition(queryBuilderName, selectedBlockDefinitionName, newBlockDefinitionName, dlg.getHint(), blockRule);
            } else { //if (newBlockDefinitionName.equals(selectedBlockDefinitionName)) {
                // update blockDefinition with hint and blockRule
                mQueryType.updateBlockDefinition(queryBuilderName, selectedBlockDefinitionName, newBlockDefinitionName, dlg.getHint(), blockRule);
            }
            
            modelR.setValueAt(newBlockDefinitionName, iSelectedRow, modelR.iColRuleName);
            modelR.setValueAt(getSourceFieldsString(blockRule), iSelectedRow, modelR.iColSourceFields);

            mQueryType.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            enableBtnOK();
        }        
    }//GEN-LAST:event_editBlockingRuleDefinitionActionPerformed

    private void removeBlockingRuleDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBlockingRuleDefinitionActionPerformed
        String queryBuilderName = this.jTextFieldQueryBuilderName.getText();
        if (queryBuilderName == null || queryBuilderName.length() == 0) {
            return;
        }
        int rs[] = mTblBlockingRuleDefinitions.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelRuleDefinitions modelR = (TableModelRuleDefinitions) mTblBlockingRuleDefinitions.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
            
                // update modelQ - BlockingQueries
                queryBuilderName = this.jTextFieldQueryBuilderName.getText(); //String) modelR.getValueAt(j, modelR.iColQueryBuilderName);
                String ruleName = (String) modelR.getValueAt(j, modelR.iColRuleName);
                //updateModelQueries(queryBuilderName, ruleName, -1);
                // update mQueryType
                mQueryType.deleteBlockDefinitionByName(queryBuilderName, ruleName);
                modelR.removeRow(j);
                // ToDo - update blocking check box 
            }
            btnRemoveBlockingRuleDefinition.setEnabled(false);
            btnEditBlockingRuleDefinition.setEnabled(false);
            mQueryType.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            enableBtnOK();
        }
    }//GEN-LAST:event_removeBlockingRuleDefinitionActionPerformed

    private void addBlockingRuleDefinitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBlockingRuleDefinitionActionPerformed
        String queryBuilderName = this.jTextFieldQueryBuilderName.getText();
        if (queryBuilderName == null || queryBuilderName.length() == 0) {
            return;
        }
        BlockingRuleDefinitionDialog dlg = new BlockingRuleDefinitionDialog(mEviewEditorMainApp.getEviewEditorMainPanel().getEntityTree(true), queryBuilderName, null, false);
        dlg.setVisible(true);
        mEviewEditorMainApp.getEviewEditorMainPanel().setEntityTreePane();
        if (dlg.isModified()) {
            TableModelRuleDefinitions model = (TableModelRuleDefinitions) mTblBlockingRuleDefinitions.getModel();
            String newQueryBuilderName = dlg.getQueryBuilderName();
            String newBlockDefinitionName = dlg.getBlockDefinitionName();
            
            // check duplicates
            // remove/replace
            if (mQueryType.getBlockDefinitionByName(newQueryBuilderName, newBlockDefinitionName) != null) {
                // prompt for replacing it
                String prompt = NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Duplicated_Row_Prompt", newBlockDefinitionName);
                NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                         prompt, 
                                         NbBundle.getMessage(BlockingQueryDialog.class, "MSG_Confirm_Remove_Duplicated_Row_Prompt_Title"), 
                                         NotifyDescriptor.YES_NO_OPTION);
                if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
                    mQueryType.deleteBlockDefinitionByName(newQueryBuilderName, newBlockDefinitionName);
                    for (int i=0; i<model.getRowCount(); i++) {
                        String ruleName = (String) model.getValueAt(i, model.iColRuleName);

                        if (ruleName.equals(newBlockDefinitionName)) {
                            //updateModelQueries(queryBuilderName, ruleName, 1);
                            model.removeRow(i);
                            break;
                        }
                    }
                } else {
                    return;
                }
            }

            ArrayList alBlockBys = dlg.getBlockBys();
            BlockRule blockRule = dlg.getBlockRule();
            if (mQueryBuilder == null) {
                mQueryBuilder = mQueryType.getNewQueryBuilder();
            }
            BlockDefinition blockDefinition = new BlockDefinition(newQueryBuilderName, newBlockDefinitionName, dlg.getHint(), blockRule);
            mQueryBuilder.setQueryBuilderName(newQueryBuilderName);
            mQueryBuilder.addBlockDefinition(blockDefinition);
            
            //mQueryType.createBlockDefinition(newQueryBuilderName, newBlockDefinitionName, dlg.getHint(), blockRule);
            
            String sourceFields = getSourceFieldsString(blockRule);
            RuleDefinitionRow r = new RuleDefinitionRow(newBlockDefinitionName, sourceFields);
            int iInsertRow = model.getRowCount();
            model.addRow(iInsertRow, r);
            
            mTblBlockingRuleDefinitions.clearSelection();
            mTblBlockingRuleDefinitions.addRowSelectionInterval(iInsertRow, iInsertRow);   
            mTblBlockingRuleDefinitions.setEditingRow(iInsertRow);
            this.btnEditBlockingRuleDefinition.setEnabled(true);
            this.btnRemoveBlockingRuleDefinition.setEnabled(true);
            //
            //updateModelQueries(newQueryBuilderName, newBlockDefinitionName, 1);
            mQueryType.setModified(true);
            mEviewEditorMainApp.enableSaveAction(true);
            enableBtnOK();
        }

    }//GEN-LAST:event_addBlockingRuleDefinitionActionPerformed

    private void jCheckBoxPhoneticizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxPhoneticizeStateChanged
        enableBtnOK();
    }//GEN-LAST:event_jCheckBoxPhoneticizeStateChanged

    private void jCheckBoxStandardizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxStandardizeStateChanged
        enableBtnOK();
    }//GEN-LAST:event_jCheckBoxStandardizeStateChanged

    private void jTextFieldParserClassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldParserClassKeyReleased
        enableBtnOK();
    }//GEN-LAST:event_jTextFieldParserClassKeyReleased

    private void onQueryBuilderClassKeyReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onQueryBuilderClassKeyReleased
        enableBtnOK();
    }//GEN-LAST:event_onQueryBuilderClassKeyReleased
                                
    private void onQueryBuilderNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onQueryBuilderNameKeyReleased
        enableBtnOK();
    }//GEN-LAST:event_onQueryBuilderNameKeyReleased

    private void enableBtnOK() {
        boolean flag = (jTextFieldQueryBuilderName.getText().length() == 0 || 
                        jTextFieldQueryBuildClass.getText().length() == 0 || 
                        jTextFieldParserClass.getText().length() == 0);
        this.btnAddBlockingRuleDefinition.setEnabled(!flag);
        flag = flag || (mTblBlockingRuleDefinitions.getRowCount() == 0);
        this.btnOK.setEnabled(!flag);
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mModified = false;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        mModified = true;
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed
    
    public boolean isModified() {
        return mModified;
    }
    
    public QueryBuilder getQueryBuilder() {
        return mQueryBuilder;
    }
    
    public String getQueryBuilderName() {
        return jTextFieldQueryBuilderName.getText();
    }
    
    public String getQueryBuilderClass() {
        return jTextFieldQueryBuildClass.getText();
    }
    
    public String getParserClass() {
        return jTextFieldParserClass.getText();
    }
    
    public boolean getStandardize() {
        return this.jCheckBoxStandardize.isSelected();
    }
    
    public boolean getPhoneticize() {
        return this.jCheckBoxPhoneticize.isSelected();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //new BlockingQueryDialog(new javax.swing.JFrame(), true).show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddBlockingRuleDefinition;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEditBlockingRuleDefinition;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnRemoveBlockingRuleDefinition;
    private javax.swing.JCheckBox jCheckBoxPhoneticize;
    private javax.swing.JCheckBox jCheckBoxStandardize;
    private javax.swing.JLabel jLabelParserClass;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPaneTop;
    private javax.swing.JTextField jTextFieldParserClass;
    private javax.swing.JTextField jTextFieldQueryBuildClass;
    private javax.swing.JTextField jTextFieldQueryBuilderName;
    private javax.swing.JLabel lblQueryBuilderClass;
    private javax.swing.JLabel lblQueryBuilderName;
    // End of variables declaration//GEN-END:variables
    
}
