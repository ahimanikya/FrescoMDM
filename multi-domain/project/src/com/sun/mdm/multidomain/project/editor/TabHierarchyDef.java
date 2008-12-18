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

import org.openide.util.NbBundle;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import java.awt.Toolkit;

import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.nodes.DefinitionNode;
import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.Attribute;
/**
 *
 * @author  kkao
 */
public class TabHierarchyDef extends javax.swing.JPanel {
    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            TabHierarchyDef.class.getName()
        
        );
    EditorMainApp mEditorMainApp;
    DefinitionNode mDefinitionNode;
    Definition mDefinition;
    private String mOldDefName;
    /** Creates new form TabAttributes */
    public TabHierarchyDef(EditorMainApp editorMainApp, DefinitionNode definitionNode) {
        initComponents();
        mEditorMainApp = editorMainApp;
        mDefinitionNode = definitionNode;
        mDefinition = definitionNode.getDefinition();
        mOldDefName = mDefinition.getName();
        this.jTextName.setText(mDefinition.getName());
        // Get plugin list
        ArrayList <String> alPlugins = mEditorMainApp.getPluginList(mDefinition.getDomain(), Definition.TYPE_HIERARCHY);
        for (int i=0; alPlugins != null && i < alPlugins.size(); i++) {
            this.jComboBoxPlugin.insertItemAt(alPlugins.get(i), i);
            
        }
        if (mDefinition.getPlugin() != null) {
            if (!alPlugins.contains(mDefinition.getPlugin())) {
                this.jComboBoxPlugin.insertItemAt(mDefinition.getPlugin(), 0);
                this.jComboBoxPlugin.setSelectedIndex(0);
            } else {
                this.jComboBoxPlugin.setSelectedItem(mDefinition.getPlugin());
            }
        } else {
            this.jComboBoxPlugin.setSelectedIndex(0);
            mDefinition.setPlugin((String) jComboBoxPlugin.getSelectedItem());
        }
        //this.jComboBoxPlugin.setSelectedItem(definition.getPlugin());
        
        String effectiveFrom = mDefinition.getEffectiveFrom();
        String effectiveTo = mDefinition.getEffectiveTo();
        this.jTextEffectiveFrom.setText(effectiveFrom);
        this.jTextEffectiveTo.setText(effectiveTo);
        jButtonEffectiveFrom.setIcon(EditorMainPanel.CALENDARICON);
        jButtonEffectiveTo.setIcon(EditorMainPanel.CALENDARICON);
        
        String description = mDefinition.getDescription();
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
        ArrayList <Attribute> al = mDefinition.getPredefinedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            PredefinedAttributeRow row = new PredefinedAttributeRow(attr.getName(), attr.getIncluded(), attr.getRequired());
            modelPredefinedAttribute.addRow(j, row);
        }
        // Extended attributes
        modelExtendedAttribute.rows.clear();
        al = mDefinition.getExtendedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), attr.getDataType(), 
                                            attr.getDefaultValue(), attr.getSearchable(), attr.getRequired());
            modelExtendedAttribute.addRow(j, row);
        }
        
        // Listeners
        jTextName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTextName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String newName = jTextName.getText();
                if (newName == null || newName.length() == 0) {
                    Toolkit.getDefaultToolkit().beep();

                    NotifyDescriptor desc = new NotifyDescriptor.Message(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Name_Cannot_Be_Empty"));
                    desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
                    desc.setTitle(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Error"));
                    DialogDisplayer.getDefault().notify(desc);
                    jTextName.setText(mOldDefName);
                } else if (!newName.equals(mOldDefName)) {
                    mDefinition.setName(newName);
                    mEditorMainApp.getEditorMainPanel().getTabOverview().updateDefinitionName(mOldDefName, mDefinition);
                    mEditorMainApp.getEditorMainPanel().updatePropertiesScrollPaneTitle(newName);
                    mDefinitionNode.updateDefinitionName(newName);
                    mOldDefName = jTextName.getText();
                }
            }
        });
        
        jTextEffectiveFrom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTextEffectiveFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mDefinition.setEffectiveFrom(jTextEffectiveFrom.getText());
            }
        });
        
        jTextEffectiveTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
            }
        });
        
        jTextEffectiveTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mDefinition.setEffectiveTo(jTextEffectiveTo.getText());
            }
        });
        
        this.jTextAreaDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mEditorMainApp.enableSaveAction(true);
                mDefinition.setDescription(jTextAreaDescription.getText());
            }
        });
        
        this.jComboBoxPlugin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mEditorMainApp.enableSaveAction(true);
                mDefinition.setPlugin((String) jComboBoxPlugin.getSelectedItem());
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
        jButtonEffectiveFrom = new javax.swing.JButton();
        jButtonEffectiveTo = new javax.swing.JButton();

        jLabelName.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Name_Colon")); // NOI18N

        jLabelPlugin.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Plugin_Colon")); // NOI18N

        jLabelDescription.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Description")); // NOI18N

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        jLabelPredefinedAttributes.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Predefined_Attributes")); // NOI18N

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

        jButtonEditPredefinedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Edit")); // NOI18N
        jButtonEditPredefinedAttribute.setEnabled(false);
        jButtonEditPredefinedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditPredefinedAttribute(evt);
            }
        });

        jLabelExtendedAttributes.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Extended_Attributes")); // NOI18N

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

        jButtonAddExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Add")); // NOI18N
        jButtonAddExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddExtendedAttribute(evt);
            }
        });

        jButtonDeleteExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Remove")); // NOI18N
        jButtonDeleteExtendedAttribute.setEnabled(false);
        jButtonDeleteExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveExtendedAttribute(evt);
            }
        });

        jButtonEditExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Edit")); // NOI18N
        jButtonEditExtendedAttribute.setEnabled(false);
        jButtonEditExtendedAttribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditExtendedAttribute(evt);
            }
        });

        jLabelEffectiveFrom.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Effective_From_Colon")); // NOI18N

        jTextEffectiveFrom.setEnabled(false);

        jLabelEffectiveTo.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Effective_To")); // NOI18N

        jTextEffectiveTo.setEnabled(false);

        jButtonEffectiveFrom.setMaximumSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveFrom.setMinimumSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveFrom.setPreferredSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonEffectiveFrom(evt);
            }
        });

        jButtonEffectiveTo.setMaximumSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveTo.setMinimumSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveTo.setPreferredSize(new java.awt.Dimension(16, 16));
        jButtonEffectiveTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onButtonEffectiveTo(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelPredefinedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabelDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .add(jLabelEffectiveFrom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabelName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 436, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(jTextName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 164, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                        .add(jTextEffectiveFrom)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButtonEffectiveFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(layout.createSequentialGroup()
                                        .add(jLabelPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(2, 2, 2)
                                        .add(jComboBoxPlugin, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(layout.createSequentialGroup()
                                        .add(jLabelEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jTextEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jButtonEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(jLabelExtendedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonEditPredefinedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPanePredefinedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(jButtonAddExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jButtonDeleteExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(4, 4, 4)
                            .add(jButtonEditExtendedAttribute, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPaneExtendedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(52, 52, 52))
        );

        layout.linkSize(new java.awt.Component[] {jButtonEffectiveFrom, jButtonEffectiveTo}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

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
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextEffectiveFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jButtonEffectiveFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jComboBoxPlugin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(8, 8, 8)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabelEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jButtonEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextEffectiveTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(jLabelPredefinedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(jScrollPanePredefinedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(jLabelExtendedAttributes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonEditPredefinedAttribute)))
                .add(6, 6, 6)
                .add(jScrollPaneExtendedAttr, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonEditExtendedAttribute)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButtonAddExtendedAttribute)
                        .add(jButtonDeleteExtendedAttribute)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jButtonEffectiveFrom, jButtonEffectiveTo}, org.jdesktop.layout.GroupLayout.VERTICAL);

    }// </editor-fold>//GEN-END:initComponents

private void onEditPredefinedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditPredefinedAttribute
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
        mDefinitionNode.updatePredefinedAttribute(oldName, attr);
        // update row
        row.setName(attrName);
        row.setIncluded(included);
        row.setRequired(required);
        model.fireTableDataChanged();
        jTablePredefinedAttr.setRowSelectionInterval(idx, idx);
        mEditorMainApp.enableSaveAction(true);
    }
}//GEN-LAST:event_onEditPredefinedAttribute

private void onAddExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddExtendedAttribute
    final ExtendedAttributeDialog dialog = new ExtendedAttributeDialog();
    dialog.setVisible(true);
    if (dialog.isModified()) {
        String attrName = dialog.getAttributeName();
        if (mDefinition.getExtendedAttribute(attrName) == null) {
            String dataType = dialog.getDataType();
            String defaultValue = dialog.getDefaultValue();
            String searchable = dialog.getSearchable() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            // add new Attribute
            Attribute attr = new Attribute(attrName, "", dataType, defaultValue,
                                           searchable, required);
            mDefinitionNode.addExtendedAttribute(attr);
            // add a new row
            TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
            ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), 
                                                        attr.getDataType(), attr.getDefaultValue(),
                                                        attr.getSearchable(), attr.getRequired());
            model.addRow(model.getRowCount(), row);
            model.fireTableDataChanged();
            mEditorMainApp.enableSaveAction(true);
        } else {
            displayExtendedAttributeFound(attrName);
        }
    }
}//GEN-LAST:event_onAddExtendedAttribute

    private void displayExtendedAttributeFound(String attrName) {
        NotifyDescriptor desc = new NotifyDescriptor.Message(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Attribute_Exists", attrName));
        desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
        desc.setTitle(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Error"));
        DialogDisplayer.getDefault().notify(desc);
    }

private void onRemoveExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveExtendedAttribute
TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
        int rs[] = jTableExtendedAttr.getSelectedRows();
        int length = rs.length;
        String type = (length == 1) ? NbBundle.getMessage(TabHierarchyDef.class, "TITLE_Extended_Attribute")
                                    :  NbBundle.getMessage(TabHierarchyDef.class, "TITLE_Extended_Attributes");
        String prompt = (length == 1) ? NbBundle.getMessage(TabRelationshipDef.class, "MSG_Confirm_Remove_Prompt", type)
                                      : NbBundle.getMessage(TabRelationshipDef.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
        String title = NbBundle.getMessage(TabHierarchyDef.class, "MSG_Confirm_Remove_Title", type);
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 title, 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            for (int i=length - 1; i>=0 && i < length; i--) {
                int idx = rs[i];
                ExtendedAttributeRow row = model.getRow(idx);
                mDefinitionNode.deleteExtendedAttribute(row.getName());
                model.removeRow(idx);
            }
            model.fireTableDataChanged();
            mEditorMainApp.enableSaveAction(true);
            jButtonDeleteExtendedAttribute.setEnabled(false);
            jButtonEditExtendedAttribute.setEnabled(false);
        }
}//GEN-LAST:event_onRemoveExtendedAttribute

private void onEditExtendedAttribute(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditExtendedAttribute
    TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
    int idx = this.jTableExtendedAttr.getSelectedRow();
    ExtendedAttributeRow row = model.getRow(idx);
    String oldName = row.getName();
    final ExtendedAttributeDialog dialog = new ExtendedAttributeDialog(row.getName(), row.getDataType(), 
                                                row.getDefaultValue(), row.getSearchable(), row.getRequired());
    dialog.setVisible(true);
    if (dialog.isModified()) {
        String attrName = dialog.getAttributeName();
        if (oldName.equals(attrName) || mDefinition.getExtendedAttribute(attrName) == null) {
            String dataType = dialog.getDataType();
            String defaultValue = dialog.getDefaultValue();
            String searchable = dialog.getSearchable() == true ? "true" : "false";
            String required = dialog.getRequired() == true ? "true" : "false";
            // Replace Attribute
            Attribute attr = new Attribute(attrName, "", dataType, defaultValue,
                                           searchable, required);
            mDefinitionNode.updateExtendedAttribute(oldName, attr);
            // update row
            row.setName(attrName);
            row.setDataType(dataType);
            row.setDefaultValue(defaultValue);
            row.setSearchable(searchable);
            row.setRequired(required);
            model.fireTableDataChanged();
            jTableExtendedAttr.setRowSelectionInterval(idx, idx);
            mEditorMainApp.enableSaveAction(true);
        } else {
            displayExtendedAttributeFound(attrName);
        }
    }
}//GEN-LAST:event_onEditExtendedAttribute

private void onButtonEffectiveFrom(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonEffectiveFrom
    performDatePicker(true);
}//GEN-LAST:event_onButtonEffectiveFrom

private void onButtonEffectiveTo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onButtonEffectiveTo
    performDatePicker(false);
}//GEN-LAST:event_onButtonEffectiveTo

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
    private javax.swing.JButton jButtonEffectiveFrom;
    private javax.swing.JButton jButtonEffectiveTo;
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
        private String dataType;
        private String defaultValue;
        private String searchable;
        private String required;

        public ExtendedAttributeRow(String name, String dataType, String defaultValue,
                String searchable, String required) {
            this.name = name;
            this.dataType = dataType;
            this.defaultValue = defaultValue;
            this.searchable = searchable;
            this.required = required;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_DataType"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Default_Value"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Searchable"), 
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Required"), 
                                        };
        ArrayList <ExtendedAttributeRow> rows;
        final static int iColName = 0;
        final static int iColDataType = 1;
        final static int iColDefaultValue = 2;
        final static int iColSearchable = 3;
        final static int iColRequired = 4;
        
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
                        case iColDataType:
                            return singleRow.getDataType();
                        case iColDefaultValue:
                            return singleRow.getDefaultValue();
                        case iColSearchable:
                            return singleRow.getSearchable();
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
                ExtendedAttributeRow singleRow = (ExtendedAttributeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            singleRow.setName((String) value);                            
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

    /**
     * 
     * @return String DefinitionName
     */
    public String getDefinitionName() {
        return this.jTextName.getText();
    }
    
    /**
     * 
     * @return String Plugin
     */
    public String getPlugin() {
        return (String) jComboBoxPlugin.getSelectedItem();
    }

    /**
     * 
     * @return String EffectiveFrom
     */
    public String getEffectiveFrom() {
        return this.jTextEffectiveFrom.getText();
    }
    
    /**
     * 
     * @return String EffectiveTo
     */
    public String getEffectiveTo() {
        return this.jTextEffectiveTo.getText();
    }
    
    /**
     * 
     * @return String Description
     */
    public String getDescription() {
        return jTextAreaDescription.getText();
    }
    
    /**
     * 
     * @return ArrayList <Attribute> PredefinedAttributes
     */
    public ArrayList <Attribute> getPredefinedAttributes() {
        ArrayList <Attribute> al = new ArrayList();
        TableModelPredefinedAttribute model = (TableModelPredefinedAttribute) jTablePredefinedAttr.getModel();
        for (int i=0; i < model.getRowCount(); i++) {
            PredefinedAttributeRow row = model.getRow(i);
            Attribute attr = new Attribute(row.getName(), row.getIncluded(), row.getRequired());
            al.add(attr);
        }
        return al;
    }
    
    /**
     * 
     * @return ArrayList <Attribute> ExtendedAttributes
     */
    public ArrayList <Attribute> getExtendedAttributes() {
        ArrayList <Attribute> al = new ArrayList();
        TableModelExtendedAttribute model = (TableModelExtendedAttribute) jTableExtendedAttr.getModel();
        for (int i=0; i < model.getRowCount(); i++) {
            ExtendedAttributeRow row = model.getRow(i);
            Attribute attr = new Attribute(row.getName(), "", 
                                           row.getDataType(), row.getDefaultValue(),
                                           row.getSearchable(), row.getRequired());
            al.add(attr);
        }
        return al;
    }
    
    private void displayIncorrectDatePicked(final boolean from, String fromDate, String toDate) {
        
        NotifyDescriptor desc;
        if (from) {
            desc = new NotifyDescriptor.Message(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Incorrect_From_Date_Picked", fromDate, toDate));
        } else {
            desc = new NotifyDescriptor.Message(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Incorrect_To_Date_Picked", toDate, fromDate));
        }
        desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
        desc.setTitle(NbBundle.getMessage(TabHierarchyDef.class, "MSG_Incorrect_Date"));
        DialogDisplayer.getDefault().notify(desc);
    }
    
    private void performDatePicker(final boolean from) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final CalendarDialog dialog = new CalendarDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (dialog.getReturnStatus() == AddDefinitionDialog.RET_OK) {
                            java.util.Date date = dialog.getDate();
                            String df = mEditorMainApp.getMultiDomainModel(false).getDateFormat();
                            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(df);
                            String dateStr = formatter.format(date);
                            boolean setIt = true;
                            if (from) {
                                // check if before effectiveto date
                                String toDateStr = getEffectiveTo();
                                if (toDateStr != null && toDateStr.length() > 0) {
                                    try {
                                        java.util.Date toDate = formatter.parse(toDateStr);
                                        if (date.after(toDate)) {
                                            setIt = false;
                                            displayIncorrectDatePicked(from, dateStr, toDateStr);
                                        }
                                    } catch (java.text.ParseException ex) {
                                        setIt = false;
                                        mLog.error(ex.getMessage());
                                    }
                                }
                                if (setIt) {
                                    jTextEffectiveFrom.setText(dateStr);
                                    mDefinition.setEffectiveFrom(dateStr);
                                }
                            } else {
                                // check if after effectivefrom date
                                String fromDateStr = getEffectiveFrom();
                                if (fromDateStr != null && fromDateStr.length() > 0) {
                                    try {
                                        java.util.Date fromDate = formatter.parse(fromDateStr);
                                        if (date.before(fromDate)) {
                                            setIt = false;
                                            displayIncorrectDatePicked(from, fromDateStr, dateStr);
                                        }
                                    } catch (java.text.ParseException ex) {
                                        setIt = false;
                                        mLog.error(ex.getMessage());
                                    }
                                }
                                if (setIt) {
                                    jTextEffectiveTo.setText(dateStr);
                                    mDefinition.setEffectiveTo(dateStr);
                                }
                            }
                            if (setIt) {
                                mEditorMainApp.enableSaveAction(true);
                            }
                        }
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}
