/*
 * TabWebManagerRelationshipTypes.java
 *
 * Created on September 22, 2008, 2:27 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.LinkType;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class TabWebManagerRelationshipTypes extends javax.swing.JPanel {
    
    private ArrayList<LinkType> mLinkTypes = null;
    private JTable mTableFields;
    private boolean mModified = false;
    private EditorMainApp mEditorMainApp = null;

    /** Creates new form TabWebManagerRelationshipTypes */
    public TabWebManagerRelationshipTypes(EditorMainApp editorMainApp, ArrayList<LinkType> relationshipTypes) {
        
        mEditorMainApp = editorMainApp;
        mLinkTypes = relationshipTypes;
        initComponents();
        createLinkTypes();
        jTxtDisplayOrder.setEditable(false);
        
        /**
        mTableFields.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                retreiveFieldProperties();
            }
         
        });
        */
        
        ListSelectionModel rowSM = mTableFields.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                retreiveFieldProperties();
            }
        });
        mTableFields.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
                    int iSelectedRow = mTableFields.getSelectedRow();
                    int lastRow = mTableFields.getRowCount() - 1;
                    String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
                    if (fieldName != null) {
                        RelationFieldReference mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                        //mTableFields.clearSelection();
                        if (mSelectedRow != null) {
                            jTxtDisplayName.setText(mSelectedRow.getFieldDisplayName());
                            if (mSelectedRow.getDisplayOrder() != iSelectedRow + 1) {
                                mSelectedRow.setDisplayOrder(iSelectedRow + 1);
                                jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1));
                            }
                            jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                            jTxtGuiType.setText(mSelectedRow.getGuiType());
                            jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                            jRBKeyValue.setSelected(mSelectedRow.getKeyType());
                        }
                    }
                    jBtnUp.setEnabled(true);
                    jBtnDown.setEnabled(true);
                    
                    if ( iSelectedRow == 0) {
                        jBtnUp.setEnabled(false);
                    } else if (iSelectedRow == mTableFields.getRowCount() - 1) {
                        jBtnDown.setEnabled(false);
                    }
                }
            });
            
        jCBRelationshipType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                String relTypeName = (String) jCBRelationshipType.getSelectedItem();
                ArrayList<RelationFieldReference> fieldRefs = null;
                for (LinkType relType : mLinkTypes) {
                    if (relType.getName().equals(relTypeName)) {
                        fieldRefs = relType.getRelFieldRefs();
                        break;
                    }
                }
                
                if (fieldRefs != null) {
                    createFieldRefs(fieldRefs);                    
                }
                
            }
            
        });
    }
    
    
    private void retreiveFieldProperties() {
        TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
        int iSelectedRow = mTableFields.getSelectedRow();
        if (iSelectedRow >= 0 && iSelectedRow < mTableFields.getRowCount()) {
            int lastRow = mTableFields.getRowCount() - 1;
            String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
            if (fieldName != null) {
                RelationFieldReference mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                //mTableFields.clearSelection();
                if (mSelectedRow != null) {
                    jTxtDisplayName.setText(mSelectedRow.getFieldDisplayName());
                    if (mSelectedRow.getDisplayOrder() != iSelectedRow + 1) {
                        mSelectedRow.setDisplayOrder(iSelectedRow + 1);
                        jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1));
                    }
                    jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                    jTxtGuiType.setText(mSelectedRow.getGuiType());
                    jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    jRBKeyValue.setSelected(mSelectedRow.getKeyType());
                }
            }
            jBtnUp.setEnabled(true);
            jBtnDown.setEnabled(true);

            if (iSelectedRow == 0) {
                jBtnUp.setEnabled(false);
            } else if (iSelectedRow == mTableFields.getRowCount() - 1) {
                jBtnDown.setEnabled(false);
            }
        }

    }
    private void createLinkTypes() {
        
        for (LinkType relType : mLinkTypes) {
            this.jCBRelationshipType.addItem(relType.getName());
        }
        
        if (mLinkTypes != null && mLinkTypes.size() > 0) {
            LinkType relType = mLinkTypes.get(0);
            String relationType = relType.getName();
            //this.jCBRelationshipType.addItem(relationType);
            createFieldRefs(relType.getRelFieldRefs());
            
        }
    }
    
    private void createFieldRefs(ArrayList<RelationFieldReference> fieldRefs) {
            TableModelRelationshipField mTableFieldRefs = new TableModelRelationshipField(fieldRefs);
            if (mTableFields != null) {
                mTableFields.setModel(mTableFieldRefs);
            } else {
                mTableFields = new JTable(mTableFieldRefs);
            }
            mTableFields.getTableHeader().setReorderingAllowed(false);
            mTableFields.setRowSelectionAllowed(true);
            mTableFields.setRowSelectionInterval(0, 0);
            //mTableFields.setR
            jScrollPane1.setViewportView(mTableFields);
            int iSelectedRow = mTableFields.getSelectedRow();
            TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
            String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
            if (fieldName != null) {
                RelationFieldReference mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                //mTableFields.clearSelection();
                if (mSelectedRow != null) {
                    jTxtDisplayName.setText(mSelectedRow.getFieldDisplayName());
                    jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                    jTxtGuiType.setText(mSelectedRow.getGuiType());
                    jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    jRBKeyValue.setSelected(mSelectedRow.getKeyType());
                }
                
                jBtnUp.setEnabled(true);
                jBtnDown.setEnabled(true);

                if (iSelectedRow == 0) {
                    jBtnUp.setEnabled(false);
                } else if (iSelectedRow == mTableFields.getRowCount() - 1) {
                    jBtnDown.setEnabled(false);
                }

            }

        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jCBRelationshipType = new javax.swing.JComboBox();
        jLabelRelTypes = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jTxtDisplayName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTxtDisplayOrder = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTxtMaxLength = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtGuiType = new javax.swing.JTextField();
        jRBKeyValue = new javax.swing.JRadioButton();
        onOK = new javax.swing.JButton();
        onCancel = new javax.swing.JButton();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jScrollPane1.border.title"))); // NOI18N

        jLabelRelTypes.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabelRelTypes.text")); // NOI18N

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLayeredPane1.border.title"))); // NOI18N

        jTxtDisplayName.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jTxtDisplayName.text")); // NOI18N
        jTxtDisplayName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtDisplayNameActionPerformed(evt);
            }
        });
        jTxtDisplayName.setBounds(110, 50, 167, 19);
        jLayeredPane1.add(jTxtDisplayName, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel1.text")); // NOI18N
        jLabel1.setBounds(10, 50, 90, 14);
        jLayeredPane1.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel2.text")); // NOI18N
        jLabel2.setBounds(10, 80, 87, 14);
        jLayeredPane1.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTxtDisplayOrder.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jTxtDisplayOrder.text")); // NOI18N
        jTxtDisplayOrder.setBounds(110, 80, 107, 19);
        jLayeredPane1.add(jTxtDisplayOrder, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel3.text")); // NOI18N
        jLabel3.setBounds(10, 110, 87, 14);
        jLayeredPane1.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTxtMaxLength.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jTxtMaxLength.text")); // NOI18N
        jTxtMaxLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtMaxLengthActionPerformed(evt);
            }
        });
        jTxtMaxLength.setBounds(110, 110, 107, 19);
        jLayeredPane1.add(jTxtMaxLength, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel4.text")); // NOI18N
        jLabel4.setBounds(10, 140, 87, 14);
        jLayeredPane1.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTxtGuiType.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jTxtGuiType.text")); // NOI18N
        jTxtGuiType.setBounds(110, 140, 100, 19);
        jLayeredPane1.add(jTxtGuiType, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jRBKeyValue.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jRBKeyValue.text")); // NOI18N
        jRBKeyValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBKeyValueActionPerformed(evt);
            }
        });
        jRBKeyValue.setBounds(110, 170, 73, 23);
        jLayeredPane1.add(jRBKeyValue, javax.swing.JLayeredPane.DEFAULT_LAYER);

        onOK.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "LBL_OK")); // NOI18N
        onOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOKActionPerformed(evt);
            }
        });

        onCancel.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "LBL_Cancel")); // NOI18N

        jBtnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/editor/arrowup.gif"))); // NOI18N
        jBtnUp.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jBtnUp.text")); // NOI18N
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpBtn(evt);
            }
        });

        jBtnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/editor/arrowdown.gif"))); // NOI18N
        jBtnDown.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jBtnDown.text")); // NOI18N
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDownBtn(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jBtnUp, 0, 0, Short.MAX_VALUE)
                    .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, Short.MAX_VALUE))
                .add(28, 28, 28)
                .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .add(56, 56, 56))
            .add(layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jLabelRelTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jCBRelationshipType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(316, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(369, Short.MAX_VALUE)
                .add(onOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(onCancel)
                .add(94, 94, 94))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCBRelationshipType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelRelTypes))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(onCancel)
                                    .add(onOK)))
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 297, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(92, 92, 92)
                        .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jBtnDown)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jRBKeyValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBKeyValueActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRBKeyValueActionPerformed

private void jTxtDisplayNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtDisplayNameActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTxtDisplayNameActionPerformed

private void jTxtMaxLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtMaxLengthActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTxtMaxLengthActionPerformed

private void onOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOKActionPerformed
// TODO add your handling code here:
    this.mModified = true;
    mEditorMainApp.enableSaveAction(true);
}//GEN-LAST:event_onOKActionPerformed

private void onUpBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUpBtn
// TODO add your handling code here:
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    int iSelectedRow = mTableFields.getSelectedRow();
    RelationFieldReference movedUpRow = model.getRow(iSelectedRow);
    RelationFieldReference movedDownow = model.getRow(iSelectedRow - 1);
    movedUpRow.setDisplayOrder(iSelectedRow);
    model.removeRow(iSelectedRow);
    //if (iSelectedRow ==
    iSelectedRow--;
    model.addRow(iSelectedRow, movedUpRow);
    mTableFields.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    movedDownow.setDisplayOrder(iSelectedRow + 1);

    
}//GEN-LAST:event_onUpBtn

private void onDownBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDownBtn
// TODO add your handling code here:
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    int iSelectedRow = mTableFields.getSelectedRow();
    RelationFieldReference movedRow = model.getRow(iSelectedRow);
    model.removeRow(iSelectedRow);
    RelationFieldReference previousRow = model.getRow(iSelectedRow);
    previousRow.setDisplayOrder(iSelectedRow + 1);
    //jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1))
    //if (iSelectedRow ==
    iSelectedRow++;
    movedRow.setDisplayOrder(iSelectedRow + 1);
    model.addRow(iSelectedRow, movedRow);
    mTableFields.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    
}//GEN-LAST:event_onDownBtn

class TableModelRelationshipField extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "LBL_RELATIONSHIP_TYPES_FILED"),                                           
                                        };
        ArrayList fieldRows;
        final static int iColFieldName = 0;
        
        TableModelRelationshipField(ArrayList rows) {
            fieldRows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (fieldRows != null) {
                return fieldRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (fieldRows != null) {
                RelationFieldReference singleRow = (RelationFieldReference) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColFieldName:
                            return singleRow.getFieldName();                            
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public RelationFieldReference getRow(int row) {
            if (fieldRows != null) {
                RelationFieldReference singleRow = (RelationFieldReference) fieldRows.get(row);
                return singleRow;
            }
            return null;
        }
        
        public Class getColumnClass(int c) {
            Object colNameObj = getValueAt(0, c);
            return colNameObj.getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                        case iColFieldName:
                             ((RelationFieldReference) fieldRows.get(row)).setFieldName((String) value);  
                             break;
                            
                    }
           
            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }
        
        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, RelationFieldReference row) {
            //fieldRows.add(row);
            fieldRows.add(index, row);
            this.fireTableRowsInserted(index, index);
        }

        /**
        public MatchRuleRowPerProbType getRow(int index) {
            //MatchRuleRowPerProbType row = (MatchRuleRowPerProbType) matchRuleRows.get(index);
            return row;
        }
         */

        public int findRowByFieldName(String fieldName) {
            for (int i=0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }
        
        public RelationFieldReference findRelTypeByFieldName(String fieldName) {
            for (int i=0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return (RelationFieldReference) fieldRows.get(i);
                }
            }
            return null;
        }        
        

    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnDown;
    private javax.swing.JButton jBtnUp;
    private javax.swing.JComboBox jCBRelationshipType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelRelTypes;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jRBKeyValue;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTxtDisplayName;
    private javax.swing.JTextField jTxtDisplayOrder;
    private javax.swing.JTextField jTxtGuiType;
    private javax.swing.JTextField jTxtMaxLength;
    private javax.swing.JButton onCancel;
    private javax.swing.JButton onOK;
    // End of variables declaration//GEN-END:variables

}
