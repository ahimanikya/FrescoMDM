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

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.openide.util.NbBundle;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

//import javax.swing.table.TableRowSorter;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.Attribute;
/**
 *
 * @author  kkao
 */
public class TabHierarchyDef extends javax.swing.JPanel {
    EditorMainApp mEditorMainApp;
    Definition mDefinition;
    /** Creates new form TabAttributes */
    public TabHierarchyDef(EditorMainApp editorMainApp, Definition definition) {
        initComponents();
        mEditorMainApp = editorMainApp;
        mDefinition = definition;
        this.jTextName.setText(definition.getName());
        // Get plugin list
        if (definition.getPlugin() != null) {
            this.jComboBoxPlugin.insertItemAt(definition.getPlugin(), 0);
            this.jComboBoxPlugin.setSelectedIndex(0);
        }
        //this.jComboBoxPlugin.setSelectedItem(definition.getPlugin());
        String direction = definition.getDirection();
        String description = definition.getDescription();
        if (description != null) {
            this.jTextAreaDescription.setText(description);
        }
        ArrayList <PredefinedAttributeRow> rowsPredefinedAttribute = new ArrayList();
        TableModelPredefinedAttribute modelPredefinedAttribute = new TableModelPredefinedAttribute(rowsPredefinedAttribute);
        this.jTablePredefinedAttr.setModel(modelPredefinedAttribute);
        
        ArrayList <ExtendedAttributeRow> rowsExtendedAttribute = new ArrayList();
        TableModelExtendedAttribute modelExtendedAttribute = new TableModelExtendedAttribute(rowsExtendedAttribute);
        this.jTableExtendedAttr.setModel(modelExtendedAttribute);
        // Predefined attributes
        modelPredefinedAttribute.rows.clear();
        ArrayList <Attribute> al = definition.getPredefinedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            PredefinedAttributeRow row = new PredefinedAttributeRow(attr.getName(), attr.getIncluded(), attr.getRequired());
            modelPredefinedAttribute.addRow(j, row);
        }
        // Extended attributes
        modelExtendedAttribute.rows.clear();
        al = definition.getExtendedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), attr.getColumnName(), 
                        attr.getDataType(), attr.getDefaultValue(),
                        attr.getSearchable(), attr.getRequired(), attr.getAttributeID());
            modelExtendedAttribute.addRow(j, row);
        }
        
        // Listeners
        jTextName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        this.jTextAreaDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        this.jComboBoxPlugin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTablePredefinedAttr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    onEditPredefinedAttribute(null);
                } else {
                    onPredefinedAttributesSelected();
                }
            }
        });
        
        jTableExtendedAttr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    onEditExtendedAttribute(null);
                } else {
                    onExtendedAttributesSelected();
                }
            }
        });

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelName = new javax.swing.JLabel();
        jTextName = new javax.swing.JTextField();
        jLabelPlugin = new javax.swing.JLabel();
        jComboBoxPlugin = new javax.swing.JComboBox();
        jLabelDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jLabelPredefinedAttributes = new javax.swing.JLabel();
        jScrollPanePredefinedAttr = new javax.swing.JScrollPane();
        jTablePredefinedAttr = new javax.swing.JTable();
        jButtonEditPredefinedAttribute = new javax.swing.JButton();
        jLabelExtendedAttributes = new javax.swing.JLabel();
        jScrollPaneExtendedAttr = new javax.swing.JScrollPane();
        jTableExtendedAttr = new javax.swing.JTable();
        jButtonAddExtendedAttribute = new javax.swing.JButton();
        jButtonDeleteExtendedAttribute = new javax.swing.JButton();
        jButtonEditExtendedAttribute = new javax.swing.JButton();
        jLabelEffectiveFrom = new javax.swing.JLabel();
        jTextEffectiveFrom = new javax.swing.JTextField();
        jLabelEffectiveTo = new javax.swing.JLabel();
        jTextEffectiveTo = new javax.swing.JTextField();

        jLabelName.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jLabelName.text")); // NOI18N

        jLabelPlugin.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jLabelPlugin.text")); // NOI18N

        jLabelDescription.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jLabelDescription.text")); // NOI18N

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        jLabelPredefinedAttributes.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jLabelPredefinedAttributes.text")); // NOI18N

        jTablePredefinedAttr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPanePredefinedAttr.setViewportView(jTablePredefinedAttr);

        jButtonEditPredefinedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jButtonEditPredefinedAttribute.text")); // NOI18N
        jButtonEditPredefinedAttribute.setEnabled(false);
        jButtonEditPredefinedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditPredefinedAttributeonEditPredefinedAttribute(evt);
            }
        });

        jLabelExtendedAttributes.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jLabelExtendedAttributes.text")); // NOI18N

        jTableExtendedAttr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPaneExtendedAttr.setViewportView(jTableExtendedAttr);

        jButtonAddExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jButtonAddExtendedAttribute.text")); // NOI18N
        jButtonAddExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddExtendedAttributeonAddExtendedAttribute(evt);
            }
        });

        jButtonDeleteExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jButtonDeleteExtendedAttribute.text")); // NOI18N
        jButtonDeleteExtendedAttribute.setEnabled(false);
        jButtonDeleteExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteExtendedAttributeonRemoveExtendedAttribute(evt);
            }
        });

        jButtonEditExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabRelationshipDef.jButtonEditExtendedAttribute.text")); // NOI18N
        jButtonEditExtendedAttribute.setEnabled(false);
        jButtonEditExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditExtendedAttributeonEditExtendedAttribute(evt);
            }
        });

        jLabelEffectiveFrom.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabHierarchyDef.jLabelEffectiveFrom.text")); // NOI18N

        jLabelEffectiveTo.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "TabHierarchyDef.jLabelEffectiveTo.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelPredefinedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPanePredefinedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jLabelExtendedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(260, 260, 260)
                        .add(jButtonEditPredefinedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPaneExtendedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(270, 270, 270)
                        .add(jButtonAddExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jButtonDeleteExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jButtonEditExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabelDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .add(jLabelEffectiveFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabelName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 436, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(jTextEffectiveFrom)
                                    .add(jTextName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(layout.createSequentialGroup()
                                        .add(jLabelPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(2, 2, 2)
                                        .add(jComboBoxPlugin, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(layout.createSequentialGroup()
                                        .add(jLabelEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jTextEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 156, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))))
                .add(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabelEffectiveFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jTextName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jTextEffectiveFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jComboBoxPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(8, 8, 8)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabelEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(jLabelPredefinedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(jScrollPanePredefinedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(jLabelExtendedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jButtonEditPredefinedAttribute))
                .add(6, 6, 6)
                .add(jScrollPaneExtendedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonAddExtendedAttribute)
                    .add(jButtonDeleteExtendedAttribute)
                    .add(jButtonEditExtendedAttribute))
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButtonEditPredefinedAttributeonEditPredefinedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditPredefinedAttributeonEditPredefinedAttribute
TableModelPredefinedAttribute model = (TableModelPredefinedAttribute) this.jTablePredefinedAttr.getModel();
        int idx = this.jTablePredefinedAttr.getSelectedRow();
        PredefinedAttributeRow row = model.getRow(idx);
        String oldName = row.getName();
        final PredefinedAttributeDialog dialog = new PredefinedAttributeDialog(row.getName(), row.getIncluded(),
                row.getRequired());
        dialog.setVisible(true);
        if (dialog.isModified()) {
            String attrName = dialog.getAttributeName();
            String included = dialog.getIncluded() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            // Replace Attribute
            Attribute attr = new Attribute(attrName, included, required);
            mDefinition.updatePredefinedAttribute(oldName, attr);
            // update row
            row.setName(attrName);
            row.setIncluded(included);
            row.setRequired(required);
            model.fireTableDataChanged();
            jTablePredefinedAttr.setRowSelectionInterval(idx, idx);
            mEditorMainApp.enableSaveAction(true);
        }
}//GEN-LAST:event_jButtonEditPredefinedAttributeonEditPredefinedAttribute

private void jButtonAddExtendedAttributeonAddExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddExtendedAttributeonAddExtendedAttribute
final ExtendedAttributeDialog dialog = new ExtendedAttributeDialog();
            dialog.setVisible(true);
            if (dialog.isModified()) {
                String attrName = dialog.getAttributeName();
                String dataType = dialog.getDataType();
                String columnName = dialog.getColumnName();
                String defaultValue = dialog.getDefaultValue();
                String searchable = dialog.getSearchable() == true ? "true" : "false";
                String required = dialog.getRequired() == true ? "true" : "false";
                String attributeID = ""; //dialog.getAttributeID();
                // add new Attribute
                Attribute attr = new Attribute(attrName, columnName, dataType, defaultValue,
                                               searchable, required, attributeID);
                mDefinition.addExtendedAttribute(attr);
                // add a new row
                TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
                ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), attr.getColumnName(), 
                                                            attr.getDataType(), attr.getDefaultValue(),
                                                            attr.getSearchable(), attr.getRequired(), attr.getAttributeID());
                model.addRow(model.getRowCount(), row);
                model.fireTableDataChanged();
                mEditorMainApp.enableSaveAction(true);
            }
}//GEN-LAST:event_jButtonAddExtendedAttributeonAddExtendedAttribute

private void jButtonDeleteExtendedAttributeonRemoveExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteExtendedAttributeonRemoveExtendedAttribute
TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
        int rs[] = jTableExtendedAttr.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(TabHierarchyDef.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(TabHierarchyDef.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(TabHierarchyDef.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            for (int i=length - 1; i>=0 && i < length; i--) {
                int idx = rs[i];
                ExtendedAttributeRow row = model.getRow(idx);
                mDefinition.deleteExtendedAttribute(row.getName());
                model.removeRow(idx);
            }
            model.fireTableDataChanged();
            mEditorMainApp.enableSaveAction(true);
        }
}//GEN-LAST:event_jButtonDeleteExtendedAttributeonRemoveExtendedAttribute

private void jButtonEditExtendedAttributeonEditExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditExtendedAttributeonEditExtendedAttribute
TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
        int idx = this.jTableExtendedAttr.getSelectedRow();
        ExtendedAttributeRow row = model.getRow(idx);
        String oldName = row.getName();
        final ExtendedAttributeDialog dialog = new ExtendedAttributeDialog(row.getName(), row.getColumnName(),
                row.getDataType(), row.getDefaultValue(), row.getSearchable(), row.getRequired());
        dialog.setVisible(true);
        if (dialog.isModified()) {
            String attrName = dialog.getAttributeName();
            String dataType = dialog.getDataType();
            String columnName = dialog.getColumnName();
            String defaultValue = dialog.getDefaultValue();
            String searchable = dialog.getSearchable() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            String attributeID = ""; //dialog.getAttributeID();
            // Replace Attribute
            Attribute attr = new Attribute(attrName, columnName, dataType, defaultValue,
                                           searchable, required, attributeID);
            mDefinition.updateExtendedAttribute(oldName, attr);
            // update row
            row.setName(attrName);
            row.setColumnName(columnName);
            row.setDataType(dataType);
            row.setDefaultValue(defaultValue);
            row.setSearchable(searchable);
            row.setRequired(required);
            model.fireTableDataChanged();
            jTableExtendedAttr.setRowSelectionInterval(idx, idx);
            mEditorMainApp.enableSaveAction(true);
        }
}//GEN-LAST:event_jButtonEditExtendedAttributeonEditExtendedAttribute

    private void onEditExtendedAttribute(java.awt.event.ActionEvent evt) {                                         
        TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
        int idx = this.jTableExtendedAttr.getSelectedRow();
        ExtendedAttributeRow row = model.getRow(idx);
        String oldName = row.getName();
        final ExtendedAttributeDialog dialog = new ExtendedAttributeDialog(row.getName(), row.getColumnName(),
                row.getDataType(), row.getDefaultValue(), row.getSearchable(), row.getRequired());
        dialog.setVisible(true);
        if (dialog.isModified()) {
            String attrName = dialog.getAttributeName();
            String dataType = dialog.getDataType();
            String columnName = dialog.getColumnName();
            String defaultValue = dialog.getDefaultValue();
            String searchable = dialog.getSearchable() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            String attributeID = ""; //dialog.getAttributeID();
            // Replace Attribute
            Attribute attr = new Attribute(attrName, columnName, dataType, defaultValue,
                                           searchable, required, attributeID);
            mDefinition.updateExtendedAttribute(oldName, attr);
            // update row
            row.setName(attrName);
            row.setColumnName(columnName);
            row.setDataType(dataType);
            row.setDefaultValue(defaultValue);
            row.setSearchable(searchable);
            row.setRequired(required);
            model.fireTableDataChanged();
            jTableExtendedAttr.setRowSelectionInterval(idx, idx);
            mEditorMainApp.enableSaveAction(true);
        }
    }                                        

    private void onEditPredefinedAttribute(java.awt.event.ActionEvent evt) {                                           
        TableModelPredefinedAttribute model = (TableModelPredefinedAttribute) this.jTablePredefinedAttr.getModel();
        int idx = this.jTablePredefinedAttr.getSelectedRow();
        PredefinedAttributeRow row = model.getRow(idx);
        String oldName = row.getName();
        final PredefinedAttributeDialog dialog = new PredefinedAttributeDialog(row.getName(), row.getIncluded(),
                row.getRequired());
        dialog.setVisible(true);
        if (dialog.isModified()) {
            String attrName = dialog.getAttributeName();
            String included = dialog.getIncluded() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            // Replace Attribute
            Attribute attr = new Attribute(attrName, included, required);
            mDefinition.updatePredefinedAttribute(oldName, attr);
            // update row
            row.setName(attrName);
            row.setIncluded(included);
            row.setRequired(required);
            model.fireTableDataChanged();
            jTablePredefinedAttr.setRowSelectionInterval(idx, idx);
            mEditorMainApp.enableSaveAction(true);
        }
    }                                          

    private void onPredefinedAttributesSelected() {
        int cnt = jTablePredefinedAttr.getSelectedRowCount();
        jButtonEditPredefinedAttribute.setEnabled(cnt==1);
    }

    private void onExtendedAttributesSelected() {
        int cnt = jTableExtendedAttr.getSelectedRowCount();
        jButtonDeleteExtendedAttribute.setEnabled(true);
        jButtonEditExtendedAttribute.setEnabled(cnt==1);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddExtendedAttribute;
    private javax.swing.JButton jButtonDeleteExtendedAttribute;
    private javax.swing.JButton jButtonEditExtendedAttribute;
    private javax.swing.JButton jButtonEditPredefinedAttribute;
    private javax.swing.JComboBox jComboBoxPlugin;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelEffectiveFrom;
    private javax.swing.JLabel jLabelEffectiveTo;
    private javax.swing.JLabel jLabelExtendedAttributes;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPlugin;
    private javax.swing.JLabel jLabelPredefinedAttributes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneExtendedAttr;
    private javax.swing.JScrollPane jScrollPanePredefinedAttr;
    private javax.swing.JTable jTableExtendedAttr;
    private javax.swing.JTable jTablePredefinedAttr;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextEffectiveFrom;
    private javax.swing.JTextField jTextEffectiveTo;
    private javax.swing.JTextField jTextName;
    // End of variables declaration//GEN-END:variables

    class PredefinedAttributeRow {
        private String name;
        private String included = "true";
        private String required = "true";

        public PredefinedAttributeRow(String name, String included, String required) {
            this.name = name;
            this.included = included;
            this.required = required;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIncluded() {
            return included;
        }

        public void setIncluded(String included) {
            this.included = included;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }
    }

    // Table model for Relationship Type
    class TableModelPredefinedAttribute extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Included"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Required"), 
                                        };
        ArrayList <PredefinedAttributeRow> rows;
        final static int iColName = 0;
        final static int iColIncluded = 1;
        final static int iColRequired = 2;
        
        TableModelPredefinedAttribute(ArrayList rows) {
            this.rows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (rows != null) {
                return rows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (rows != null) {
                PredefinedAttributeRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            return singleRow.getName();
                        case iColIncluded:
                            return singleRow.getIncluded();
                        case iColRequired:
                            return singleRow.getRequired();
                        default:
                            return null;
                    }
                }
            }
            return null;
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
            if (rows != null && row >=0 && row < rows.size()) {
                PredefinedAttributeRow singleRow = (PredefinedAttributeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            singleRow.setName((String) value);                            
                            break;
                        case iColIncluded:
                            singleRow.setIncluded((String) value);                            
                            break;
                        case iColRequired:
                            singleRow.setRequired((String) value);    
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
            }
        }
        
        public void removeRow(int index) {
            rows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, PredefinedAttributeRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public PredefinedAttributeRow getRow(int index) {
            PredefinedAttributeRow row = (PredefinedAttributeRow) rows.get(index);
            return row;
        }
    }
    
    class ExtendedAttributeRow {
        private String name;
        private String columnName;
        private String dataType;
        private String defaultValue;
        private String searchable;
        private String required;
        private String attributeID;

        public ExtendedAttributeRow(String name, String columnName, String dataType, String defaultValue,
                String searchable, String required, String attributeID) {
            this.name = name;
            this.columnName = columnName;
            this.dataType = dataType;
            this.defaultValue = defaultValue;
            this.searchable = searchable;
            this.required = required;
            this.attributeID = attributeID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getAttributeID() {
            return attributeID;
        }

        public void setAttributeID(String attributeID) {
            this.attributeID = attributeID;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }

        public String getSearchable() {
            return searchable;
        }

        public void setSearchable(String searchable) {
            this.searchable = searchable;
        }
    }

    // Table model for Relationship Type
    class TableModelExtendedAttribute extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_ColumnName"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_DataType"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Default_Value"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Searchable"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Required"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_ID"), 
                                        };
        ArrayList <ExtendedAttributeRow> rows;
        final static int iColName = 0;
        final static int iColColumnName = 1;
        final static int iColDataType = 2;
        final static int iColDefaultValue = 3;
        final static int iColSearchable = 4;
        final static int iColRequired = 5;
        final static int iColAttributeID = 6;
        
        TableModelExtendedAttribute(ArrayList rows) {
            this.rows = rows;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (rows != null) {
                return rows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (rows != null) {
                ExtendedAttributeRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            return singleRow.getName();
                        case iColColumnName:
                            return singleRow.getColumnName();
                        case iColDataType:
                            return singleRow.getDataType();
                        case iColDefaultValue:
                            return singleRow.getDefaultValue();
                        case iColSearchable:
                            return singleRow.getSearchable();
                        case iColRequired:
                            return singleRow.getRequired();
                        case iColAttributeID:
                            return singleRow.getAttributeID();
                        default:
                            return null;
                    }
                }
            }
            return null;
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
            if (rows != null && row >=0 && row < rows.size()) {
                ExtendedAttributeRow singleRow = (ExtendedAttributeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            singleRow.setName((String) value);                            
                            break;
                        case iColColumnName:
                            singleRow.setColumnName((String) value);                            
                            break;
                        case iColDefaultValue:
                            singleRow.setDefaultValue((String) value);                            
                            break;
                        case iColSearchable:
                            singleRow.setSearchable((String) value);
                            break;
                        case iColRequired:
                            singleRow.setRequired((String) value);
                            break;
                        case iColAttributeID:
                            singleRow.setAttributeID((String) value);
                            break;
                        default:
                            return;
                    }
                }
                fireTableCellUpdated(row, col);
            }
        }
        
        public void removeRow(int index) {
            rows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
        
        public void addRow(int index, ExtendedAttributeRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public ExtendedAttributeRow getRow(int index) {
            ExtendedAttributeRow row = (ExtendedAttributeRow) rows.get(index);
            return row;
        }
    }

    public String getPlugin() {
        return (String) jComboBoxPlugin.getSelectedItem();
    }

    public String getDescription() {
        return jTextAreaDescription.getText();
    }

    public String getRelationshipDefName() {
        return jTextName.getText();
    }

}
