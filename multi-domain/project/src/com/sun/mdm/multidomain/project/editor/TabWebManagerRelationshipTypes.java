/*
 * TabWebManagerRelationshipTypes.java
 *
 * Created on September 22, 2008, 2:27 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.Attribute;
import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.RelationshipType;
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
    
    private ArrayList<LinkType> mRelationshipTypes = null;
    private JTable mTableFields;
    private boolean mModified = false;
    private EditorMainApp mEditorMainApp = null;
    private LinkType mLinkType = null;
    //private 

    /** Creates new form TabWebManagerRelationshipTypes */
    public TabWebManagerRelationshipTypes(EditorMainApp editorMainApp, LinkType linkType) {
        
        mEditorMainApp = editorMainApp;
        //mRelationshipTypes = relationshipTypes;
        mLinkType = linkType;
        initComponents();
        mTableFields = new JTable();
        if (mLinkType != null) {
            createRelationshipTypes();
            this.jTextFieldLinkType.setText(mLinkType.getName());
        }
        jTxtDisplayOrder.setEditable(false);
        this.jTextFieldLinkType.setEditable(false);
        

        mTableFields.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                retreiveFieldProperties();
            }
         
        });
        
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
                        FieldAttributeRow mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                        //mTableFields.clearSelection();
                        if (mSelectedRow != null) {
                            jTxtDisplayName.setText(mSelectedRow.getDisplayName());
                            if (mSelectedRow.getDisplayOrder() != iSelectedRow + 1) {
                                mSelectedRow.setDisplayOrder(iSelectedRow + 1);
                                jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1));
                            }
                            jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                            //jTxtGuiType.setText(mSelectedRow.getGuiType());
                            jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                            //jRBKeyValue.setSelected(mSelectedRow.getKeyType());
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
            
         //jTxtDisplayName.addActionListener(l);
            

    }
    

    public void addFieldReference(Attribute modelAttr) {
        String fieldName = modelAttr.getName();
        String displayName = modelAttr.getName();
        int displayFieldOrder = mTableFields.getRowCount() + 1;
        int maxLen = 1;
        String guiType = "TextBox";
        String valueList = null;
        String valueType = modelAttr.getType();
        boolean keyType = false;

        FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder,
                maxLen, true, guiType, valueList, valueType, keyType);
        TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
        model.addRow(mTableFields.getRowCount(), row);
        mTableFields.setModel(model);

    }
    
    private void retreiveFieldProperties() {
        TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
        int iSelectedRow = mTableFields.getSelectedRow();
        if (iSelectedRow >= 0 && iSelectedRow < mTableFields.getRowCount()) {
            int lastRow = mTableFields.getRowCount() - 1;
            String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
            if (fieldName != null) {
                FieldAttributeRow mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                //mTableFields.clearSelection();
                if (mSelectedRow != null) {
                    jTxtDisplayName.setText(mSelectedRow.getDisplayName());
                    if (mSelectedRow.getDisplayOrder() != iSelectedRow + 1) {
                        mSelectedRow.setDisplayOrder(iSelectedRow + 1);
                        jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1));
                    }
                    jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                    //jTxtGuiType.setText(mSelectedRow.getGuiType());
                    jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    //jRBKeyValue.setSelected(mSelectedRow.getKeyType());
                }
            }
            jBtnUp.setEnabled(true);
            jBtnDown.setEnabled(true);
            jLayeredPane1.setVisible(true);

            if (iSelectedRow == 0) {
                jBtnUp.setEnabled(false);
            } else if (iSelectedRow == mTableFields.getRowCount() - 1) {
                jBtnDown.setEnabled(false);
            }
        }

    }
    private void createRelationshipTypes() {
        RelationshipType relType = (RelationshipType) mLinkType;
        String relationType = relType.getName();

        createFieldRefs(relType);
    /**
        if (mRelationshipTypes != null && mRelationshipTypes.size() > 0) {
            
        } else {
            ArrayList<RelationFieldReference> fieldRefs = new ArrayList<RelationFieldReference>();
            TableModelRelationshipField mTableFieldRefs = new TableModelRelationshipField(fieldRefs);
            mTableFields = new JTable(mTableFieldRefs);
        }
             */ 
    }
    
    private void createFieldRefs(RelationshipType relType) {
            //ArrayList<RelationFieldReference> fieldRefs = relType.getFixedRelFieldRefs();
            ArrayList<FieldAttributeRow> attrs = new ArrayList<FieldAttributeRow>();
            for (RelationFieldReference field : relType.getFixedRelFieldRefs()) {
                String fieldName = field.getFieldName();
                String displayName = field.getFieldDisplayName();
                int displayFieldOrder = field.getDisplayOrder();
                int maxLen = field.getMaxLength();
                String guiType = field.getGuiType();
                String valueList = field.getValueList();
                String valueType = field.getValueType();
                boolean keyType = field.getKeyType();

                FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, true, guiType, valueList, valueType, keyType);
                attrs.add(row);
            }
            for (RelationFieldReference field : relType.getExtendedRelFieldRefs()) {
                String fieldName = field.getFieldName();
                String displayName = field.getFieldDisplayName();
                int displayFieldOrder = field.getDisplayOrder();
                int maxLen = field.getMaxLength();
                String guiType = field.getGuiType();
                String valueList = field.getValueList();
                String valueType = field.getValueType();
                boolean keyType = field.getKeyType();


                FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, false, guiType, valueList, valueType, keyType);
                attrs.add(row);
            }
            TableModelRelationshipField mTableFieldRefs = new TableModelRelationshipField(attrs);
            mTableFields = new JTable(mTableFieldRefs);
            mTableFields.getTableHeader().setReorderingAllowed(false);
            mTableFields.setRowSelectionAllowed(true);
            if (attrs.size() > 0 ) {
                mTableFields.setRowSelectionInterval(0, 0);
                jScrollPane1.setViewportView(mTableFields);
                int iSelectedRow = mTableFields.getSelectedRow();
                TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
                String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
                if (fieldName != null) {
                    FieldAttributeRow mSelectedRow = ((TableModelRelationshipField) mTableFields.getModel()).findRelTypeByFieldName(fieldName);
                    //mTableFields.clearSelection();
                    if (mSelectedRow != null) {
                        jTxtDisplayName.setText(mSelectedRow.getDisplayName());
                        jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                        //jTxtGuiType.setText(mSelectedRow.getGuiType());
                        jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    //jRBKeyValue.setSelected(mSelectedRow.getKeyType());
                    }

                    jLayeredPane1.setVisible(true);

                    jBtnUp.setEnabled(true);
                    jBtnDown.setEnabled(true);

                    if (iSelectedRow == 0) {
                        jBtnUp.setEnabled(false);
                    } else if (iSelectedRow == mTableFields.getRowCount() - 1) {
                        jBtnDown.setEnabled(false);
                    }

                }
            }
            //mTableFields.setR

        
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
        jTextFieldLinkType = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jScrollPane1.border.title"))); // NOI18N

        jLabelRelTypes.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabelRelTypes.text")); // NOI18N

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLayeredPane1.border.title"))); // NOI18N

        jTxtDisplayName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDisplayNameActionPerformed(evt);
            }
        });
        jTxtDisplayName.setBounds(110, 50, 120, 19);
        jLayeredPane1.add(jTxtDisplayName, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel1.text")); // NOI18N
        jLabel1.setBounds(10, 50, 90, 14);
        jLayeredPane1.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel2.text")); // NOI18N
        jLabel2.setBounds(10, 80, 87, 14);
        jLayeredPane1.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jTxtDisplayOrder.setBounds(110, 80, 107, 19);
        jLayeredPane1.add(jTxtDisplayOrder, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel3.text")); // NOI18N
        jLabel3.setBounds(10, 110, 87, 14);
        jLayeredPane1.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTxtMaxLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onMaxLengthActionPerformed(evt);
            }
        });
        jTxtMaxLength.setBounds(110, 110, 107, 19);
        jLayeredPane1.add(jTxtMaxLength, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jLabel4.text")); // NOI18N
        jLabel4.setBounds(10, 140, 87, 14);
        jLayeredPane1.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTxtGuiType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onjTxtGuiType(evt);
            }
        });
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

        jTextFieldLinkType.setEditable(false);
        jTextFieldLinkType.setText(org.openide.util.NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "TabWebManagerRelationshipTypes.jTextFieldLinkType.text")); // NOI18N

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
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jTextFieldLinkType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(263, Short.MAX_VALUE))
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
                    .add(jLabelRelTypes)
                    .add(jTextFieldLinkType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                .add(18, 18, 18)
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

private void onDisplayNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDisplayNameActionPerformed
// TODO add your handling code here:            
    int selectedRow = mTableFields.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setDisplayName(this.jTxtDisplayName.getText());
}//GEN-LAST:event_onDisplayNameActionPerformed

private void onMaxLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onMaxLengthActionPerformed
// TODO add your handling code here:
    int selectedRow = mTableFields.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setMaxLength(Integer.valueOf(this.jTxtMaxLength.getText()).intValue());
    
}//GEN-LAST:event_onMaxLengthActionPerformed

private void onOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOKActionPerformed
// TODO add your handling code here:
    this.mModified = true;
    if (mLinkType instanceof RelationshipType) {
        ((RelationshipType) mLinkType).getExtendedRelFieldRefs().clear();
        ((RelationshipType) mLinkType).getFixedRelFieldRefs().clear();
        TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
        int rowCount = model.getRowCount();
        for (FieldAttributeRow row : model.fieldRows) {

            String name = row.getName();
            String displayName = row.getDisplayName();
            int displayOrder = row.getDisplayOrder();
            int maxLen = row.getMaxLength();
            String guiType = row.getGuiType();
            String valueList = row.getValueList();
            String valueType = row.getValueType();
            boolean keyType = row.isKeyType();
            
            RelationFieldReference field = new RelationFieldReference(name, displayName, displayOrder,
                    maxLen, guiType, valueList, valueType, keyType);
            if (row.isPredefined()) {
                ((RelationshipType) mLinkType).getFixedRelFieldRefs().add(field);
            } else {
                ((RelationshipType) mLinkType).getExtendedRelFieldRefs().add(field);
            }
            
        }
    }
    mEditorMainApp.enableSaveAction(true);
}//GEN-LAST:event_onOKActionPerformed

private void onUpBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUpBtn
// TODO add your handling code here:
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    int iSelectedRow = mTableFields.getSelectedRow();
    FieldAttributeRow movedUpRow = model.getRow(iSelectedRow);
    FieldAttributeRow movedDownow = model.getRow(iSelectedRow - 1);
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
    FieldAttributeRow movedRow = model.getRow(iSelectedRow);
    model.removeRow(iSelectedRow);
    FieldAttributeRow previousRow = model.getRow(iSelectedRow);
    previousRow.setDisplayOrder(iSelectedRow + 1);
    //jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1))
    //if (iSelectedRow ==
    iSelectedRow++;
    movedRow.setDisplayOrder(iSelectedRow + 1);
    model.addRow(iSelectedRow, movedRow);
    mTableFields.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    
}//GEN-LAST:event_onDownBtn

private void onjTxtGuiType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onjTxtGuiType
// TODO add your handling code here:
}//GEN-LAST:event_onjTxtGuiType

    class FieldAttributeRow {
        private String name;
        private String displayName;
        private int displayOrder;
        private int maxLength;
        private boolean predefined;
        private String guiType;                
        private String valueList;        
        private String valueType;        
        private boolean keyType;



        public FieldAttributeRow(String name, String displayName, int displayOrder, 
                int maxLength, boolean isPredefined, String guiType, String valueList, String valueType,
                boolean keyType) {
            this.name = name;
            this.displayName = displayName;
            this.displayOrder = displayOrder;
            this.maxLength = maxLength;
            this.predefined = isPredefined;
            this.guiType = guiType;
            this.valueList = valueList;
            this.valueType = valueType;
            this.keyType = keyType;
            
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public int getMaxLength() {
            return this.maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }
        
        public boolean isPredefined() {
            return this.predefined;
        }
        
        public void setPredefined(boolean predefined) {
            this.predefined = predefined;
        }

        public String getGuiType() {
            return guiType;
        }

        public void setGuiType(String guiType) {
            this.guiType = guiType;
        }

        public String getValueList() {
            return valueList;
        }

        public void setValueList(String valueList) {
            this.valueList = valueList;
        }

        public String getValueType() {
            return valueType;
        }

        public void setValueType(String valueType) {
            this.valueType = valueType;
        }

        public boolean isKeyType() {
            return keyType;
        }

        public void setKeyType(boolean keyType) {
            this.keyType = keyType;
        }

    }

class TableModelRelationshipField extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabWebManagerRelationshipTypes.class, "LBL_RELATIONSHIP_TYPES_FILED"),                                           
                                        };
        ArrayList<FieldAttributeRow> fieldRows;
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
                FieldAttributeRow singleRow = (FieldAttributeRow) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColFieldName:
                            return singleRow.getName();                            
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public FieldAttributeRow getRow(int row) {
            if (fieldRows != null) {
                FieldAttributeRow singleRow = (FieldAttributeRow) fieldRows.get(row);
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
                             ((FieldAttributeRow) fieldRows.get(row)).setName((String) value);  
                             break;
                            
                    }
           
            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }
        
        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, FieldAttributeRow row) {
            //fieldRows.add(row);
            fieldRows.add(index, row);
            this.fireTableRowsInserted(index, index);
        }

        public int findRowByFieldName(String fieldName) {
            for (int i=0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }
        
        public FieldAttributeRow findRelTypeByFieldName(String fieldName) {
            for (int i=0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return (FieldAttributeRow) fieldRows.get(i);
                }
            }
            return null;
        }        
        

    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnDown;
    private javax.swing.JButton jBtnUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelRelTypes;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jRBKeyValue;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldLinkType;
    private javax.swing.JTextField jTxtDisplayName;
    private javax.swing.JTextField jTxtDisplayOrder;
    private javax.swing.JTextField jTxtGuiType;
    private javax.swing.JTextField jTxtMaxLength;
    private javax.swing.JButton onCancel;
    private javax.swing.JButton onOK;
    // End of variables declaration//GEN-END:variables

}
