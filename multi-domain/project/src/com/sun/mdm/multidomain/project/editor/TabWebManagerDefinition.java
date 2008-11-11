/*
 * TabWebManagerDefinition.java
 *
 * Created on September 22, 2008, 2:27 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.Attribute;
import com.sun.mdm.multidomain.parser.Definition;
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
public class TabWebManagerDefinition extends javax.swing.JPanel {
    
    private ArrayList<Definition> mRelationshipTypes = null;
    private JTable mTableFields;
    private boolean mModified = false;
    private EditorMainApp mEditorMainApp = null;
    private Definition mLinkType = null;
    //private 

    /** Creates new form TabWebManagerDefinition */
    public TabWebManagerDefinition(EditorMainApp editorMainApp, Definition linkType) {
        
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
                            jTxtDataType.setText(mSelectedRow.getValueType());
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
        String valueList = modelAttr.getType();
        String valueType = modelAttr.getType();
        boolean sensitive = false;

        FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder,
                maxLen, true, guiType, valueList, valueType, sensitive, "", "");
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
                    jTxtDataType.setText(mSelectedRow.getValueType());
                    jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                    this.jTxtInputMask.setText(mSelectedRow.getInputMask());
                    this.jTxtValueMask.setText(mSelectedRow.getValueMask());
                    this.jRBSensitive.setSelected(mSelectedRow.isSensitive());
                }
            }
            jBtnUp.setEnabled(true);
            jBtnDown.setEnabled(true);
            this.jPanelFieldProperties.setVisible(true);

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
                boolean senstive = field.isSensitive();
                String inputMask = field.getInputMask();
                String valueMask = field.getOutputMask();

                FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, true, guiType, valueList, valueType, senstive, inputMask, valueMask);
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
                boolean senstive = field.isSensitive();
                String inputMask = field.getInputMask();
                String valueMask = field.getOutputMask();


                FieldAttributeRow row = new FieldAttributeRow(fieldName, displayName, displayFieldOrder, 
                        maxLen, false, guiType, valueList, valueType, senstive, inputMask, valueMask);
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
                        jTxtMaxLength.setText(String.valueOf(mSelectedRow.getMaxLength()));
                        jTxtDataType.setText(mSelectedRow.getValueType());
                        this.jTxtInputMask.setText(mSelectedRow.getInputMask());
                        this.jTxtValueMask.setText(mSelectedRow.getValueMask());
                        this.jRBSensitive.setSelected(mSelectedRow.isSensitive());
                    }

                    this.jPanelFieldProperties.setVisible(true);

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
        onOK = new javax.swing.JButton();
        onCancel = new javax.swing.JButton();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();
        jTextFieldLinkType = new javax.swing.JTextField();
        jPanelFieldProperties = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtDisplayName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTxtDisplayOrder = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTxtMaxLength = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtDataType = new javax.swing.JTextField();
        jLabelInputMask = new javax.swing.JLabel();
        jTxtInputMask = new javax.swing.JTextField();
        jLabelValueMask = new javax.swing.JLabel();
        jTxtValueMask = new javax.swing.JTextField();
        jRBSensitive = new javax.swing.JRadioButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELDS"))); // NOI18N

        jLabelRelTypes.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_Links_Defined")); // NOI18N

        onOK.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_OK")); // NOI18N
        onOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOKActionPerformed(evt);
            }
        });

        onCancel.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_Cancel")); // NOI18N

        jBtnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/editor/UpArrow.jpg"))); // NOI18N
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpBtn(evt);
            }
        });

        jBtnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/editor/DownArrow.JPG"))); // NOI18N
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDownBtn(evt);
            }
        });

        jTextFieldLinkType.setEditable(false);

        jPanelFieldProperties.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_PROPERTIES"))); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_DISPLAY_NAME")); // NOI18N

        jTxtDisplayName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDisplayNameActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_DISPLAY_ORDER")); // NOI18N

        jTxtDisplayOrder.setEditable(false);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_MAX_LEN")); // NOI18N

        jTxtMaxLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onMaxLengthActionPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_TYPE")); // NOI18N

        jTxtDataType.setEditable(false);
        jTxtDataType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onjTxtDataType(evt);
            }
        });

        jLabelInputMask.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_INPUT_MASK")); // NOI18N

        jTxtInputMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onInputMask(evt);
            }
        });

        jLabelValueMask.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_FIELD_REF_VALUE_MASK")); // NOI18N

        jTxtValueMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onValueMask(evt);
            }
        });

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
            .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelInputMask, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelValueMask, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRBSensitive, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(29, 29, 29)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTxtDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTxtMaxLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTxtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTxtDisplayOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTxtDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTxtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanelFieldPropertiesLayout.setVerticalGroup(
            jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(jTxtDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jTxtDisplayOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(13, 13, 13)
                        .add(jTxtMaxLength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4)))
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(jLabel1)
                        .add(30, 30, 30)
                        .add(jLabel2)
                        .add(18, 18, 18)
                        .add(jLabel3)))
                .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(66, 66, 66)
                        .add(jTxtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelFieldPropertiesLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jPanelFieldPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTxtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(28, 28, 28)
                        .add(jLabelValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 19, Short.MAX_VALUE)
                .add(jRBSensitive))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelRelTypes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldLinkType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(onOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(onCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createSequentialGroup()
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jBtnDown, 0, 0, Short.MAX_VALUE)
                                .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanelFieldProperties, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {onCancel, onOK}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelRelTypes)
                    .add(jTextFieldLinkType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 297, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanelFieldProperties, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(onCancel)
                            .add(onOK)))
                    .add(layout.createSequentialGroup()
                        .add(78, 78, 78)
                        .add(jBtnUp)
                        .add(20, 20, 20)
                        .add(jBtnDown)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void onRBSensitive(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRBSensitive
// TODO add your handling code here:
    int selectedRow = mTableFields.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setSensitive(this.jRBSensitive.isSelected());
    
}//GEN-LAST:event_onRBSensitive

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
            boolean isSensitive = row.isSensitive();
            String inputMask = row.getInputMask();
            String valueMask = row.getValueMask();
            
            RelationFieldReference field = new RelationFieldReference(name, displayName, displayOrder,
                    maxLen, guiType, valueList, valueType, inputMask, valueMask, isSensitive);
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
    iSelectedRow--;
    model.addRow(iSelectedRow, movedUpRow);
    mTableFields.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    movedDownow.setDisplayOrder(iSelectedRow + 1);
    jTxtDisplayOrder.setText(String.valueOf(movedUpRow.getDisplayOrder()));
    

    
}//GEN-LAST:event_onUpBtn

private void onDownBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDownBtn
// TODO add your handling code here:
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    int iSelectedRow = mTableFields.getSelectedRow();
    FieldAttributeRow movedRow = model.getRow(iSelectedRow);
    model.removeRow(iSelectedRow);
    FieldAttributeRow previousRow = model.getRow(iSelectedRow);
    previousRow.setDisplayOrder(iSelectedRow + 1);
    iSelectedRow++;
    movedRow.setDisplayOrder(iSelectedRow + 1);
    model.addRow(iSelectedRow, movedRow);
    jTxtDisplayOrder.setText(String.valueOf(movedRow.getDisplayOrder()));
    mTableFields.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    
}//GEN-LAST:event_onDownBtn

private void onjTxtDataType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onjTxtDataType
// TODO add your handling code here:
}//GEN-LAST:event_onjTxtDataType

private void onInputMask(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onInputMask
// TODO add your handling code here:
    int selectedRow = mTableFields.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setInputMask(this.jTxtInputMask.getText());
    
}//GEN-LAST:event_onInputMask

private void onValueMask(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onValueMask
// TODO add your handling code here:
    int selectedRow = mTableFields.getSelectedRow();
    TableModelRelationshipField model = (TableModelRelationshipField) mTableFields.getModel();
    FieldAttributeRow fieldRef = model.getRow(selectedRow);
    fieldRef.setValueMask(this.jTxtValueMask.getText());
    
}//GEN-LAST:event_onValueMask

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
        private	String columnNames [] = {NbBundle.getMessage(TabWebManagerDefinition.class, "LBL_RELATIONSHIP_TYPES_FILED"),                                           
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
    private javax.swing.JLabel jLabelInputMask;
    private javax.swing.JLabel jLabelRelTypes;
    private javax.swing.JLabel jLabelValueMask;
    private javax.swing.JPanel jPanelFieldProperties;
    private javax.swing.JRadioButton jRBSensitive;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldLinkType;
    private javax.swing.JTextField jTxtDataType;
    private javax.swing.JTextField jTxtDisplayName;
    private javax.swing.JTextField jTxtDisplayOrder;
    private javax.swing.JTextField jTxtInputMask;
    private javax.swing.JTextField jTxtMaxLength;
    private javax.swing.JTextField jTxtValueMask;
    private javax.swing.JButton onCancel;
    private javax.swing.JButton onOK;
    // End of variables declaration//GEN-END:variables

}
