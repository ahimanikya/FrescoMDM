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

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.WebDefinition;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import java.awt.Toolkit;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author  wee
 */
public class TabWebManagerDefinition extends javax.swing.JPanel {
    
    private JTable mTablePredefinedAttrs;
    private JTable mTableExtendedAttrs;
    private JTable mTableOnFocus;
    private Map <String, RelationFieldReference> mMapRelationFieldReference = new HashMap();
    private RelationFieldReference mRelationFieldReference;
    private boolean mModified = false;
    private EditorMainApp mEditorMainApp = null;
    private Definition mDefinition = null;
    private String mOldDisplayName;
    //private 

    /** Creates new form TabWebManagerDefinition */
    public TabWebManagerDefinition(EditorMainApp editorMainApp, Definition definition) {
        
        mEditorMainApp = editorMainApp;
        mDefinition = definition;
        initComponents();
        
        if (mDefinition != null) {
            createAttributeTables();
        } else {
            mTablePredefinedAttrs = new JTable();
            mTableExtendedAttrs = new JTable();
        }
        jTxtDisplayOrder.setEditable(false);

        jTxtDisplayName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTxtDisplayName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String newName = jTxtDisplayName.getText();
                if (newName == null || newName.length() == 0) {
                    Toolkit.getDefaultToolkit().beep();

                    NotifyDescriptor desc = new NotifyDescriptor.Message(NbBundle.getMessage(TabWebManagerDefinition.class, "MSG_Name_Cannot_Be_Empty"));
                    desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
                    desc.setTitle(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Error"));
                    DialogDisplayer.getDefault().notify(desc);
                    jTxtDisplayName.setText(mOldDisplayName);
                } else if (!newName.equals(mOldDisplayName)) {
                    setDisplayName(newName);
                    mOldDisplayName = jTxtDisplayName.getText();
                }
            }
        });
        
        jTxtInputMask.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setInputMask();
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTxtValueMask.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setValueMask();
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        mTablePredefinedAttrs.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    retreiveFieldProperties(false);
                }
            });

        mTableExtendedAttrs.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    retreiveFieldProperties(true);
                }
            });

        mTablePredefinedAttrs.setRowSelectionInterval(0, 0);
        retreiveFieldProperties(false);
    }
        
    private void retreiveFieldProperties(boolean extended) {
        if (extended) {
            mTableOnFocus = mTableExtendedAttrs;
            mTablePredefinedAttrs.clearSelection();
        } else {
            mTableOnFocus = mTablePredefinedAttrs;
            mTableExtendedAttrs.clearSelection();
        }
        
        TableModelRelationshipField model = (TableModelRelationshipField) mTableOnFocus.getModel();
        int iSelectedRow = mTableOnFocus.getSelectedRow();
        if (iSelectedRow >= 0 && iSelectedRow < mTableOnFocus.getRowCount()) {
            int lastRow = mTableOnFocus.getRowCount() - 1;
            String fieldName = (String) model.getValueAt(iSelectedRow, model.iColFieldName);
            if (fieldName != null) {
                FieldAttributeRow mSelectedRow = ((TableModelRelationshipField) mTableOnFocus.getModel()).findRelTypeByFieldName(fieldName);
                //table.clearSelection();
                if (mSelectedRow != null) {
                    mOldDisplayName = mSelectedRow.getDisplayName();
                    jTxtDisplayName.setText(mSelectedRow.getDisplayName());
                    if (mSelectedRow.getDisplayOrder() != iSelectedRow + 1) {
                        mSelectedRow.setDisplayOrder(iSelectedRow + 1);
                        jTxtDisplayOrder.setText(String.valueOf(iSelectedRow + 1));
                    } else {
                        jTxtDisplayOrder.setText(String.valueOf(mSelectedRow.getDisplayOrder()));
                    }
                    jTxtDataType.setText(mSelectedRow.getValueType());
                    jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    this.jTxtInputMask.setText(mSelectedRow.getInputMask());
                    this.jTxtValueMask.setText(mSelectedRow.getValueMask());
                    this.jRBSensitive.setSelected(mSelectedRow.isSensitive());
                    mRelationFieldReference = mMapRelationFieldReference.get(fieldName);
                }
            }
            this.jPanelFieldProperties.setVisible(true);
            if (extended) {
                jBtnUp.setEnabled(true);
                jBtnDown.setEnabled(true);

                if (iSelectedRow == 0) {
                    jBtnUp.setEnabled(false);
                } else if (iSelectedRow == mTableOnFocus.getRowCount() - 1) {
                    jBtnDown.setEnabled(false);
                }
            } else {
                jBtnUp.setEnabled(false);
                jBtnDown.setEnabled(false);
            }
        }

    }
    
    private void createAttributeTables() {
        WebDefinition definition = (WebDefinition) mDefinition;
        String relationType = definition.getName();
        createPredefinedFieldRefs(definition);
        createExtendedFieldRefs(definition);
    }
    
    private void createPredefinedFieldRefs(WebDefinition definition) {
        ArrayList<FieldAttributeRow> attrs = new ArrayList<FieldAttributeRow>();
        for (RelationFieldReference field : definition.getPredefinedFieldRefs()) {
            String fieldName = field.getFieldName();
            String displayName = field.getFieldDisplayName();
            int displayFieldOrder = field.getDisplayOrder();
            int maxLen = field.getMaxLength();
            String guiType = field.getGuiType();
            String valueList = field.getValueList();
            String valueType = field.getValueType();
            boolean senstive = field.isSensitive();
            String inputMask = field.getInputMask();
            String valueMask = field.getOutputMask();

            FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, true, guiType, valueList, valueType, senstive, inputMask, valueMask);
            attrs.add(row);
            
            mMapRelationFieldReference.put(fieldName, field);
        }
        TableModelRelationshipField model = new TableModelRelationshipField(attrs);
        mTablePredefinedAttrs = new JTable(model);
        this.jScrollPanePredefinedAttrs.setViewportView(mTablePredefinedAttrs);
    }
    
    private void createExtendedFieldRefs(WebDefinition definition) {
        ArrayList<FieldAttributeRow> attrs = new ArrayList<FieldAttributeRow>();
        for (RelationFieldReference field : definition.getExtendedRelFieldRefs()) {
            String fieldName = field.getFieldName();
            String displayName = field.getFieldDisplayName();
            int displayFieldOrder = field.getDisplayOrder();
            int maxLen = field.getMaxLength();
            String guiType = field.getGuiType();
            String valueList = field.getValueList();
            String valueType = field.getValueType();
            boolean senstive = field.isSensitive();
            String inputMask = field.getInputMask();
            String valueMask = field.getOutputMask();

            FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, false, guiType, valueList, valueType, senstive, inputMask, valueMask);
            attrs.add(row);
            mMapRelationFieldReference.put(fieldName, field);
        }
        TableModelRelationshipField model = new TableModelRelationshipField(attrs);
        mTableExtendedAttrs = new JTable(model);
        mTableExtendedAttrs.getTableHeader().setReorderingAllowed(false);
        mTableExtendedAttrs.setRowSelectionAllowed(true);
        this.jScrollPaneExtendedAttrs.setViewportView(mTableExtendedAttrs);
    }
    
    public void addRelationFieldReference(RelationFieldReference field) {
        String fieldName = field.getFieldName();
        String displayName = field.getFieldDisplayName();
        int displayFieldOrder = field.getDisplayOrder();
        int maxLen = field.getMaxLength();
        String guiType = field.getGuiType();
        String valueList = field.getValueList();
        String valueType = field.getValueType();
        boolean senstive = field.isSensitive();
        String inputMask = field.getInputMask();
        String valueMask = field.getOutputMask();
        FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, false, guiType, valueList, valueType, senstive, inputMask, valueMask);
        
        TableModelRelationshipField model = (TableModelRelationshipField) mTableExtendedAttrs.getModel();
        int rowCount = model.getRowCount();
        model.addRow(rowCount, row);
        model.fireTableDataChanged();
        mTableExtendedAttrs.setRowSelectionInterval(rowCount, rowCount);
        mMapRelationFieldReference.put(fieldName, field);
        retreiveFieldProperties(true);        
    }
    
    public void deleteRelationFieldReference(String attrName) {
        mMapRelationFieldReference.remove(attrName);
        TableModelRelationshipField model = (TableModelRelationshipField) mTableExtendedAttrs.getModel();
        int selectedRow = mTableExtendedAttrs.getSelectedRow();
        int length = model.getRowCount();
        int removedRow = model.findRowByFieldName(attrName);
        model.removeRow(removedRow);
        model.fireTableDataChanged();
        if (selectedRow == removedRow) {
            mTableExtendedAttrs.setRowSelectionInterval(0, 0);
            retreiveFieldProperties(true);
        }
    }
    
    private void clearFieldProperties() {
            jTxtDisplayName.setText("");
            jTxtDisplayOrder.setText("");
            jTxtMaxLength.setText("");
            jTxtDataType.setText("");
            this.jTxtInputMask.setText("");
            this.jTxtValueMask.setText("");
            this.jRBSensitive.setSelected(false);
        
    }
    
    /** Update extended attribute reference when changes occurred
     *  Only affect extended attributes
     * @param attrName
     * @param field
     */
    public void updateRelationFieldReference(String attrName, RelationFieldReference field) {
        TableModelRelationshipField model = (TableModelRelationshipField) mTableExtendedAttrs.getModel();
        int length = model.getRowCount();
        for (int i=0; i < length; i++) {
            FieldAttributeRow row = model.getRow(i);
            if (row.getName().equals(attrName)) {
                row.setName(field.getFieldName());
                row.setValueType(field.getValueType());
                model.fireTableDataChanged();
                break;
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

        jScrollPanePredefinedAttrs = new javax.swing.JScrollPane();
        jScrollPaneExtendedAttrs = new javax.swing.JScrollPane();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();
        jPanelFieldProperties = new javax.swing.JPanel();
        jLabelDisplayName = new javax.swing.JLabel();
        jTxtDisplayName = new javax.swing.JTextField();
        jLabelDisplayOrder = new javax.swing.JLabel();
        jTxtDisplayOrder = new javax.swing.JTextField();
        jLabelMaxLength = new javax.swing.JLabel();
        jTxtMaxLength = new javax.swing.JTextField();
        jLabelDataType = new javax.swing.JLabel();
        jTxtDataType = new javax.swing.JTextField();
        jLabelInputMask = new javax.swing.JLabel();
        jTxtInputMask = new javax.swing.JTextField();
        jLabelValueMask = new javax.swing.JLabel();
        jTxtValueMask = new javax.swing.JTextField();
        jRBSensitive = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jScrollPanePredefinedAttrs.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_PREDEFINED_ATTRS"))); // NOI18N

        jScrollPaneExtendedAttrs.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_EXTENDED_ATTRS"))); // NOI18N

        jBtnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/UpArrow.jpg"))); // NOI18N
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpBtn(evt);
            }
        });

        jBtnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/DownArrow.JPG"))); // NOI18N
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDownBtn(evt);
            }
        });

        jPanelFieldProperties.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_PROPERTIES"))); // NOI18N

        jLabelDisplayName.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_DISPLAY_NAME")); // NOI18N

        jLabelDisplayOrder.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_DISPLAY_ORDER")); // NOI18N

        jTxtDisplayOrder.setEditable(false);

        jLabelMaxLength.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_MAX_LEN")); // NOI18N

        jLabelDataType.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_TYPE")); // NOI18N

        jTxtDataType.setEditable(false);

        jLabelInputMask.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_INPUT_MASK")); // NOI18N

        jLabelValueMask.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_VALUE_MASK")); // NOI18N

        jRBSensitive.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_IS_SENSITIVE")); // NOI18N
        jRBSensitive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRBSensitive(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelFieldPropertiesLayout = new org.jdesktop.layout.GroupLayout(jPanelFieldProperties);
        jPanelFieldProperties.setLayout(jPanelFieldPropertiesLayout);
        jPanelFieldPropertiesLayout.setHorizontalGroup(
            jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelFieldPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRBSensitive, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelValueMask, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelInputMask, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelMaxLength, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelDataType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelDisplayName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .add(jLabelDisplayOrder, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTxtDisplayOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTxtMaxLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTxtDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTxtDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTxtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTxtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanelFieldPropertiesLayout.setVerticalGroup(
            jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(104, 104, 104)
                        .add(jTxtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtDisplayName)
                            .add(jLabelDisplayName))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtDisplayOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelDisplayOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtMaxLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelMaxLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRBSensitive)))
                .add(81, 81, 81))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPanePredefinedAttrs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPaneExtendedAttrs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelFieldProperties, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(63, 63, 63))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelFieldProperties, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(jScrollPanePredefinedAttrs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPaneExtendedAttrs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(12, 12, 12)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void onRBSensitive(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRBSensitive
    int selectedRow = mTableOnFocus.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableOnFocus.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setSensitive(this.jRBSensitive.isSelected());
}//GEN-LAST:event_onRBSensitive

private void onDownBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDownBtn
    TableModelRelationshipField model = (TableModelRelationshipField) mTableExtendedAttrs.getModel();
    int iSelectedRow = mTableExtendedAttrs.getSelectedRow();
    FieldAttributeRow movedRow = model.getRow(iSelectedRow);
    model.removeRow(iSelectedRow);
    FieldAttributeRow previousRow = model.getRow(iSelectedRow);
    previousRow.setDisplayOrder(iSelectedRow + 1);
    iSelectedRow++;
    movedRow.setDisplayOrder(iSelectedRow + 1);
    model.addRow(iSelectedRow, movedRow);
    jTxtDisplayOrder.setText(String.valueOf(movedRow.getDisplayOrder()));
    mTableExtendedAttrs.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    jBtnUp.setEnabled(true);
}//GEN-LAST:event_onDownBtn

private void onUpBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUpBtn
    TableModelRelationshipField model = (TableModelRelationshipField) mTableExtendedAttrs.getModel();
    int iSelectedRow = mTableExtendedAttrs.getSelectedRow();
    FieldAttributeRow movedUpRow = model.getRow(iSelectedRow);
    FieldAttributeRow movedDownow = model.getRow(iSelectedRow - 1);
    movedUpRow.setDisplayOrder(iSelectedRow);
    model.removeRow(iSelectedRow);
    iSelectedRow--;
    model.addRow(iSelectedRow, movedUpRow);
    mTableExtendedAttrs.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    movedDownow.setDisplayOrder(iSelectedRow + 1);
    jTxtDisplayOrder.setText(String.valueOf(movedUpRow.getDisplayOrder()));
    jBtnDown.setEnabled(true);
}//GEN-LAST:event_onUpBtn

    private void setRelFieldRefs(JTable table) {
        TableModelRelationshipField model = (TableModelRelationshipField) table.getModel();
        int rowCount = model.getRowCount();
        for (FieldAttributeRow row : model.fieldRows) {

            String name = row.getName();
            String displayName = row.getDisplayName();
            int displayOrder = row.getDisplayOrder();
            int maxLen = row.getMaxLength();
            String guiType = row.getGuiType();
            String valueList = row.getValueList();
            String valueType = row.getValueType();
            boolean isSensitive = row.isSensitive();
            String inputMask = row.getInputMask();
            String valueMask = row.getValueMask();
            
            RelationFieldReference field = new RelationFieldReference(name, displayName, displayOrder,
                    maxLen, guiType, valueList, valueType, inputMask, valueMask, isSensitive);
            if (row.isPredefined()) {
                ((WebDefinition) mDefinition).getPredefinedFieldRefs().add(field);
            } else {
                ((WebDefinition) mDefinition).getExtendedRelFieldRefs().add(field);
            }
            
        }
    }

    private void setDisplayName(String newDisplayName) {
        int selectedRow = mTableOnFocus.getSelectedRow();
        TableModelRelationshipField model = (TableModelRelationshipField) mTableOnFocus.getModel();
        FieldAttributeRow fieldRef = model.getRow(selectedRow);
        fieldRef.setDisplayName(this.jTxtDisplayName.getText());
        mRelationFieldReference.setFieldDisplayName(this.jTxtDisplayName.getText());
    }

    private void setInputMask() {
        int selectedRow = mTableOnFocus.getSelectedRow();
        TableModelRelationshipField model = (TableModelRelationshipField) mTableOnFocus.getModel();
        FieldAttributeRow fieldRef = model.getRow(selectedRow);
        fieldRef.setInputMask(this.jTxtInputMask.getText());
        mRelationFieldReference.setInputMask(this.jTxtInputMask.getText());
    }

    private void setValueMask() {
        int selectedRow = mTableOnFocus.getSelectedRow();
        TableModelRelationshipField model = (TableModelRelationshipField) mTableOnFocus.getModel();
        FieldAttributeRow fieldRef = model.getRow(selectedRow);
        fieldRef.setValueMask(this.jTxtValueMask.getText());
        mRelationFieldReference.setOutputMask(this.jTxtValueMask.getText());
    }

    class FieldAttributeRow {
        private String name;
        private String displayName;
        private int displayOrder;
        private int maxLength;
        private boolean predefined;
        private String guiType;                
        private String valueList;        
        private String valueType;        
        private boolean sensitive;
        private String inputMask;
        private String valueMask;



        public FieldAttributeRow(String name, String displayName, int displayOrder, 
                int maxLength, boolean isPredefined, String guiType, String valueList, String valueType,
                boolean sensitive, String inputMask, String valueMask) {
            this.name = name;
            this.displayName = displayName;
            this.displayOrder = displayOrder;
            this.maxLength = maxLength;
            this.predefined = isPredefined;
            this.guiType = guiType;
            this.valueList = valueList;
            this.valueType = valueType;
            this.sensitive = sensitive;
            this.inputMask = inputMask;
            this.valueMask = valueMask;
            
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

        public boolean isSensitive() {
            return sensitive;
        }

        public void setSensitive(boolean sensitive) {
            this.sensitive = sensitive;
        }

        public String getInputMask() {
            return inputMask;
        }

        public void setInputMask(String inputMask) {
            this.inputMask = inputMask;
        }

        public String getValueMask() {
            return valueMask;
        }

        public void setValueMask(String valueMask) {
            this.valueMask = valueMask;
        }

    }

class TableModelRelationshipField extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_Attribute_Name"),                                           
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
    
    public void setDefinitionName(String newName) {
        mDefinition.setName(newName);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnDown;
    private javax.swing.JButton jBtnUp;
    private javax.swing.JLabel jLabelDataType;
    private javax.swing.JLabel jLabelDisplayName;
    private javax.swing.JLabel jLabelDisplayOrder;
    private javax.swing.JLabel jLabelInputMask;
    private javax.swing.JLabel jLabelMaxLength;
    private javax.swing.JLabel jLabelValueMask;
    private javax.swing.JPanel jPanelFieldProperties;
    private javax.swing.JRadioButton jRBSensitive;
    private javax.swing.JScrollPane jScrollPaneExtendedAttrs;
    private javax.swing.JScrollPane jScrollPanePredefinedAttrs;
    private javax.swing.JTextField jTxtDataType;
    private javax.swing.JTextField jTxtDisplayName;
    private javax.swing.JTextField jTxtDisplayOrder;
    private javax.swing.JTextField jTxtInputMask;
    private javax.swing.JTextField jTxtMaxLength;
    private javax.swing.JTextField jTxtValueMask;
    // End of variables declaration//GEN-END:variables

}
