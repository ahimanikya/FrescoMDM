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

import com.sun.mdm.index.project.ui.applicationeditor.EntityTree;
import com.sun.mdm.index.project.ui.applicationeditor.EntityTreeSelectionDialog;
import com.sun.mdm.index.parser.QueryType.BlockBy;

public class BlockByEditDialog extends javax.swing.JDialog {
    EntityTree mEntityTree;
    boolean bModified = false;
    final String RANGE = "range";
    final String NOTDEFINED = "not defined";
    BlockBy mBlockBy;
    
    /** Creates new form BlockByEditDialog */
    public BlockByEditDialog(EntityTree entityTree, BlockBy blockBy) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mEntityTree = entityTree;
        mBlockBy = blockBy;
        initComponents();
        jComboBoxOperator.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    boolean bEnable = jComboBoxOperator.getSelectedItem().equals(RANGE);
                    jCheckBoxUseConstant.setEnabled(!bEnable);
                    jTextFieldConstantValue.setEnabled(jCheckBoxUseConstant.isSelected() && !bEnable);
                    jTextFieldSource.setEnabled(bEnable);
                    jButtonSelectSource.setEnabled(bEnable);
                    jComboBoxUpperBoundType.setEnabled(bEnable);
                    jComboBoxLowerBoundType.setEnabled(bEnable);
                    enableRangeBounds();
                }
            });     
        jComboBoxUpperBoundType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    enableRangeBounds();
                }   
            });                     
        jComboBoxLowerBoundType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    enableRangeBounds();
                }   
            });                     
            
        String field = "";
        String source = "";
        String operator = "equals";
        String lowerBoundType = NOTDEFINED;
        String lowerBoundValue = "";
        String upperBoundType = NOTDEFINED;
        String upperBoundValue = "";
        boolean useConstant = false;
        if (mBlockBy != null) {
            field = mBlockBy.getField();
            useConstant = mBlockBy.useConstant();
            source = mBlockBy.getSource();
            operator = mBlockBy.getOperator();
            lowerBoundType = mBlockBy.getLowerBoundType();
            lowerBoundValue = mBlockBy.getLowerBoundValue();
            upperBoundType = mBlockBy.getUpperBoundType();
            upperBoundValue = mBlockBy.getUpperBoundValue();
        }
        
        this.jTextFieldField.setText(field);
        if (useConstant) {
            this.jCheckBoxUseConstant.setSelected(useConstant);
            this.jTextFieldConstantValue.setText(source);
            this.jTextFieldSource.setEnabled(false);
            this.jButtonSelectSource.setEnabled(false);
        } else {
            this.jTextFieldSource.setText(source);
            this.jCheckBoxUseConstant.setSelected(false);
            this.jTextFieldConstantValue.setEnabled(false);
        }
        
        jCheckBoxUseConstant.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                boolean b = jCheckBoxUseConstant.isSelected();
                    jTextFieldConstantValue.setEnabled(b);
                    jButtonSelectSource.setEnabled(!b);
                    jTextFieldSource.setEnabled(!b);
            }
        });
        
        this.jComboBoxOperator.setSelectedItem(operator);
        if (operator.equals(RANGE)) {
            this.jComboBoxLowerBoundType.setSelectedItem(lowerBoundType==null ? NOTDEFINED : lowerBoundType);
            this.jTextFieldLowerBoundValue.setText(lowerBoundValue==null ? "" : lowerBoundValue);
            this.jComboBoxUpperBoundType.setSelectedItem(upperBoundType==null ? NOTDEFINED : upperBoundType);
            this.jTextFieldUpperBoundValue.setText(upperBoundValue==null ?  "" : upperBoundValue);
        } else {
            this.jTextFieldLowerBoundValue.setEnabled(false);
            this.jTextFieldUpperBoundValue.setEnabled(false);
        }
        
        this.jTextFieldField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });
        this.jTextFieldSource.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });       
        this.jTextFieldUpperBoundValue.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });
        this.jTextFieldLowerBoundValue.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                enableOK();
            }
        });       
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelField = new javax.swing.JLabel();
        jLabelSource = new javax.swing.JLabel();
        jTextFieldField = new javax.swing.JTextField();
        jTextFieldSource = new javax.swing.JTextField();
        jLabelOperator = new javax.swing.JLabel();
        jComboBoxOperator = new javax.swing.JComboBox();
        jButtonSelectField = new javax.swing.JButton();
        jButtonSelectSource = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelLowerBoundType = new javax.swing.JLabel();
        jComboBoxLowerBoundType = new javax.swing.JComboBox();
        jLabelLowerBoundValue = new javax.swing.JLabel();
        jTextFieldLowerBoundValue = new javax.swing.JTextField();
        jLabelUpperBoundType = new javax.swing.JLabel();
        jComboBoxUpperBoundType = new javax.swing.JComboBox();
        jLabelUpperBoundValue = new javax.swing.JLabel();
        jTextFieldUpperBoundValue = new javax.swing.JTextField();
        jCheckBoxUseConstant = new javax.swing.JCheckBox();
        jLabelUpperBoundValue1 = new javax.swing.JLabel();
        jTextFieldConstantValue = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/blocking/Bundle"); // NOI18N
        setTitle(bundle.getString("LBL_Title_BlockBy")); // NOI18N
        setName(bundle.getString("LBL_Title_BlockBy")); // NOI18N

        jLabelField.setText(bundle.getString("LBL_Field")); // NOI18N

        jLabelSource.setText(bundle.getString("LBL_Source")); // NOI18N

        jTextFieldField.setEditable(false);

        jTextFieldSource.setEditable(false);

        jLabelOperator.setText(bundle.getString("LBL_Operator")); // NOI18N

        jComboBoxOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "equals", "not-equals", "greater-than-or-equal", "less-than-or-equal", "range" }));

        jButtonSelectField.setText(bundle.getString("LBL_Browse")); // NOI18N
        jButtonSelectField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFieldActionPerformed(evt);
            }
        });

        jButtonSelectSource.setText(bundle.getString("LBL_Browse")); // NOI18N
        jButtonSelectSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectSourceActionPerformed(evt);
            }
        });

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

        jLabelLowerBoundType.setText(bundle.getString("LBL_Lower_Bound_Type")); // NOI18N

        jComboBoxLowerBoundType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "not defined", "constant", "offset" }));
        jComboBoxLowerBoundType.setEnabled(false);

        jLabelLowerBoundValue.setText(bundle.getString("LBL_Lower_Bound_Value")); // NOI18N

        jTextFieldLowerBoundValue.setEnabled(false);

        jLabelUpperBoundType.setText(bundle.getString("LBL_Upper_Bound_Type")); // NOI18N

        jComboBoxUpperBoundType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "not defined", "constant", "offset" }));
        jComboBoxUpperBoundType.setEnabled(false);

        jLabelUpperBoundValue.setText(bundle.getString("LBL_Upper_Bound_Value")); // NOI18N

        jTextFieldUpperBoundValue.setEnabled(false);

        jCheckBoxUseConstant.setText(bundle.getString("LBL_Use_Constant")); // NOI18N

        jLabelUpperBoundValue1.setText(bundle.getString("LBL_Upper_Bound_Value")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 390, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(jButtonSelectField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 390, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(jButtonSelectSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelOperator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxOperator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jCheckBoxUseConstant, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(120, 120, 120)
                .add(jLabelUpperBoundValue1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldConstantValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jLabelLowerBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxLowerBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jLabelUpperBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldLowerBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jLabelUpperBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxUpperBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jLabelLowerBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldUpperBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(390, 390, 390)
                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonSelectField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonSelectSource, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelOperator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxOperator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxUseConstant, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelUpperBoundValue1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldConstantValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelLowerBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxLowerBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelUpperBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldLowerBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelUpperBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxUpperBoundType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelLowerBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldUpperBoundValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonOk)
                    .add(jButtonCancel)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-567)/2, (screenSize.height-254)/2, 567, 254);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        bModified = false;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        bModified = true;
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void selectFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFieldActionPerformed
        String field = this.jTextFieldField.getText();
        mEntityTree.clearSelection();
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, field, false);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {
            this.jTextFieldField.setText("Enterprise.SystemSBR." + entityTreeSelectionDialog.getTargetFieldName());        
            enableOK();            
        }
    }//GEN-LAST:event_selectFieldActionPerformed

    private void selectSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectSourceActionPerformed
        String source = this.jTextFieldSource.getText();
        mEntityTree.clearSelection();        
        EntityTreeSelectionDialog entityTreeSelectionDialog = new EntityTreeSelectionDialog(mEntityTree, source, false);                                        
        entityTreeSelectionDialog.setVisible(true);
        if (entityTreeSelectionDialog.isSelected()) {        
            this.jTextFieldSource.setText(entityTreeSelectionDialog.getTargetFieldName());                
            enableOK();            
        }
    }//GEN-LAST:event_selectSourceActionPerformed
    
    private void enableRangeBounds() {
        boolean bEnable = jComboBoxOperator.getSelectedItem().equals(RANGE) && 
                          !jComboBoxLowerBoundType.getSelectedItem().equals(NOTDEFINED);
        jTextFieldLowerBoundValue.setEnabled(bEnable);
        bEnable = jComboBoxOperator.getSelectedItem().equals(RANGE) && 
                  !jComboBoxUpperBoundType.getSelectedItem().equals(NOTDEFINED);
        jTextFieldUpperBoundValue.setEnabled(bEnable);
        enableOK();
    }
    
    private void enableOK() {
        boolean enabled = this.jTextFieldField.getText() != null && 
                          !this.jTextFieldField.getText().equals("") &&
                          ( (this.jTextFieldSource.getText() != null && 
                            !this.jTextFieldSource.getText().equals("")) ||
                            (this.jCheckBoxUseConstant.isSelected() &&
                             !this.jTextFieldConstantValue.getText().equals(""))
                          );    
        if (jComboBoxOperator.getSelectedItem().equals(RANGE)) {
            if (!jComboBoxUpperBoundType.getSelectedItem().equals(NOTDEFINED)) {
                enabled =  enabled && 
                           (this.jTextFieldUpperBoundValue.getText() != null && 
                           !this.jTextFieldUpperBoundValue.getText().equals(""));    

            }
            if (!jComboBoxLowerBoundType.getSelectedItem().equals(NOTDEFINED) ) {
                enabled =  enabled && 
                           (this.jTextFieldLowerBoundValue.getText() != null && 
                           !this.jTextFieldLowerBoundValue.getText().equals(""));    
                        
            }
        }
        this.jButtonOk.setEnabled(enabled);
    }
    
    public BlockBy getBlockBy() {
        BlockBy blockBy = new BlockBy(getField(), getOperator(), 
                                      getSource(), getUseConstant(),
                                      getLowerBoundType(), getLowerBoundValue(),
                                      getUpperBoundType(), getUpperBoundValue());
        return blockBy;
    }
    
    public String getField() {
        return this.jTextFieldField.getText();
    }
    
    public String getSource() {
        if (this.jCheckBoxUseConstant.isSelected()) {
            return this.jTextFieldConstantValue.getText();
        } else {
            return this.jTextFieldSource.getText();
        }
    }
    
    public String getConstant() {
        return this.jTextFieldConstantValue.getText();
    }
    
    public String getOperator() {
        return (String) this.jComboBoxOperator.getSelectedItem();
    }
    
    public String getUpperBoundType() {
        return (String) this.jComboBoxUpperBoundType.getSelectedItem();
    }
    
    public String getUpperBoundValue() {
        if (getUpperBoundType().equals(NOTDEFINED)) {
            return null;
        } else {
            return this.jTextFieldUpperBoundValue.getText();
        }
    }
    
    public String getLowerBoundType() {
        return (String) this.jComboBoxLowerBoundType.getSelectedItem();
    }
    
    public String getLowerBoundValue() {
        if (getLowerBoundType().equals(NOTDEFINED)) {
            return null;
        } else {
            return this.jTextFieldLowerBoundValue.getText();
        }
    }
    
    public boolean getUseConstant() {
        return this.jCheckBoxUseConstant.isSelected();
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
                new BlockByEditDialog(null, null);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSelectField;
    private javax.swing.JButton jButtonSelectSource;
    private javax.swing.JCheckBox jCheckBoxUseConstant;
    private javax.swing.JComboBox jComboBoxLowerBoundType;
    private javax.swing.JComboBox jComboBoxOperator;
    private javax.swing.JComboBox jComboBoxUpperBoundType;
    private javax.swing.JLabel jLabelField;
    private javax.swing.JLabel jLabelLowerBoundType;
    private javax.swing.JLabel jLabelLowerBoundValue;
    private javax.swing.JLabel jLabelOperator;
    private javax.swing.JLabel jLabelSource;
    private javax.swing.JLabel jLabelUpperBoundType;
    private javax.swing.JLabel jLabelUpperBoundValue;
    private javax.swing.JLabel jLabelUpperBoundValue1;
    private javax.swing.JTextField jTextFieldConstantValue;
    private javax.swing.JTextField jTextFieldField;
    private javax.swing.JTextField jTextFieldLowerBoundValue;
    private javax.swing.JTextField jTextFieldSource;
    private javax.swing.JTextField jTextFieldUpperBoundValue;
    // End of variables declaration//GEN-END:variables
    
}
