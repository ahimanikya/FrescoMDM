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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
//import javax.swing.table.TableRowSorter;

import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.parser.Attribute;
/**
 *
 * @author  kkao
 */
public class TabHierarchyDef extends javax.swing.JPanel {
    /** Creates new form TabAttributes */
    public TabHierarchyDef(LinkType linkType) {
        initComponents();
        
        this.jTextName.setText(linkType.getName());
        // ToDo Get plugin list
        this.jComboBoxPlugin.insertItemAt(linkType.getPlugin(), 0);
        this.jComboBoxPlugin.setSelectedIndex(0);
        //this.jComboBoxPlugin.setSelectedItem(linkType.getPlugin());
        //ToDo
        //this.jTextEffectiveFrom.setText(linkType.getEffectiveFrom());
        //this.jTextEffectiveTo.setText(linkType.getEffectiveTo());
        String description = linkType.getDescription();
        this.jTextAreaDescription.setText(description);
        
        ArrayList <PredefinedAttributeRow> rowsPredefinedAttribute = new ArrayList();
        TableModelPredefinedAttribute modelPredefinedAttribute = new TableModelPredefinedAttribute(rowsPredefinedAttribute);
        this.jTableFixedAttibutes.setModel(modelPredefinedAttribute);
        
        ArrayList <ExtendedAttributeRow> rowsExtendedAttribute = new ArrayList();
        TableModelExtendedAttribute modelExtendedAttribute = new TableModelExtendedAttribute(rowsExtendedAttribute);
        this.jTableExtendedAttributes.setModel(modelExtendedAttribute);
        // Predefined attributes
        modelPredefinedAttribute.rows.clear();
        ArrayList <Attribute> al = linkType.getPredefinedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            PredefinedAttributeRow row = new PredefinedAttributeRow(attr.getName(), attr.getValue());
            modelPredefinedAttribute.addRow(j, row);
        }
        // Extended attributes
        modelExtendedAttribute.rows.clear();
        al = linkType.getExtendedAttributes();
        for (int j=0; al != null && j < al.size(); j++) {
            Attribute attr = (Attribute) al.get(j);
            ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), attr.getColumnName(), 
                        attr.getDataType(), attr.getDefaultValue(),
                        attr.getSearchable(), attr.getRequired(), attr.getAttributeID());
            modelExtendedAttribute.addRow(j, row);
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

        jScrollPaneAttributes = new javax.swing.JScrollPane();
        jTableFixedAttibutes = new javax.swing.JTable();
        jScrollPaneExtendedAttibutes = new javax.swing.JScrollPane();
        jTableExtendedAttributes = new javax.swing.JTable();
        jButtonAddExtendedAttribute = new javax.swing.JButton();
        jButtonDeleteExtendedAttribute = new javax.swing.JButton();
        jLabelName = new javax.swing.JLabel();
        jTextName = new javax.swing.JTextField();
        jLabelPlugin = new javax.swing.JLabel();
        jComboBoxPlugin = new javax.swing.JComboBox();
        jLabelDescription = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDescription = new javax.swing.JTextArea();
        jLabelEffectiveFrom = new javax.swing.JLabel();
        jTextEffectiveFrom = new javax.swing.JTextField();
        jLabelEffectiveTo = new javax.swing.JLabel();
        jTextEffectiveTo = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(null);

        jScrollPaneAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Predefined_Attributes"))); // NOI18N

        jTableFixedAttibutes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Name", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneAttributes.setViewportView(jTableFixedAttibutes);

        add(jScrollPaneAttributes);
        jScrollPaneAttributes.setBounds(10, 190, 550, 200);

        jScrollPaneExtendedAttibutes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Extended_Attributes"))); // NOI18N

        jTableExtendedAttributes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Name", "Data Type", "Value"
            }
        ));
        jScrollPaneExtendedAttibutes.setViewportView(jTableExtendedAttributes);

        add(jScrollPaneExtendedAttibutes);
        jScrollPaneExtendedAttibutes.setBounds(10, 390, 550, 190);

        jButtonAddExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Add")); // NOI18N
        add(jButtonAddExtendedAttribute);
        jButtonAddExtendedAttribute.setBounds(380, 590, 90, 23);

        jButtonDeleteExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Remove")); // NOI18N
        add(jButtonDeleteExtendedAttribute);
        jButtonDeleteExtendedAttribute.setBounds(470, 590, 80, 23);

        jLabelName.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Name")); // NOI18N
        add(jLabelName);
        jLabelName.setBounds(20, 20, 70, 20);
        add(jTextName);
        jTextName.setBounds(110, 20, 170, 20);

        jLabelPlugin.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Plugin")); // NOI18N
        add(jLabelPlugin);
        jLabelPlugin.setBounds(300, 20, 80, 20);

        add(jComboBoxPlugin);
        jComboBoxPlugin.setBounds(380, 20, 180, 20);

        jLabelDescription.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_Description")); // NOI18N
        add(jLabelDescription);
        jLabelDescription.setBounds(20, 90, 70, 20);

        jTextAreaDescription.setColumns(20);
        jTextAreaDescription.setRows(5);
        jScrollPane1.setViewportView(jTextAreaDescription);

        add(jScrollPane1);
        jScrollPane1.setBounds(110, 90, 450, 90);

        jLabelEffectiveFrom.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_EffectiveFrom")); // NOI18N
        add(jLabelEffectiveFrom);
        jLabelEffectiveFrom.setBounds(20, 50, 90, 20);
        add(jTextEffectiveFrom);
        jTextEffectiveFrom.setBounds(110, 50, 170, 19);

        jLabelEffectiveTo.setText(org.openide.util.NbBundle.getMessage(TabHierarchyDef.class, "LBL_EffectiveTo")); // NOI18N
        add(jLabelEffectiveTo);
        jLabelEffectiveTo.setBounds(300, 50, 80, 20);
        add(jTextEffectiveTo);
        jTextEffectiveTo.setBounds(380, 50, 180, 19);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddExtendedAttribute;
    private javax.swing.JButton jButtonDeleteExtendedAttribute;
    private javax.swing.JComboBox jComboBoxPlugin;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelEffectiveFrom;
    private javax.swing.JLabel jLabelEffectiveTo;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPlugin;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneAttributes;
    private javax.swing.JScrollPane jScrollPaneExtendedAttibutes;
    private javax.swing.JTable jTableExtendedAttributes;
    private javax.swing.JTable jTableFixedAttibutes;
    private javax.swing.JTextArea jTextAreaDescription;
    private javax.swing.JTextField jTextEffectiveFrom;
    private javax.swing.JTextField jTextEffectiveTo;
    private javax.swing.JTextField jTextName;
    // End of variables declaration//GEN-END:variables
    
    
    class PredefinedAttributeRow {
        private String name;
        private String defaultValue;

        public PredefinedAttributeRow(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    // Table model for Relationship Type
    class TableModelPredefinedAttribute extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabHierarchyDef.class, "LBL_Attribute_Default_Value"), 
                                        };
        ArrayList <PredefinedAttributeRow> rows;
        final static int iColName = 0;
        final static int iColDefaultValue = 1;
        
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
                        case iColDefaultValue:
                            return singleRow.getDefaultValue();
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
                        case iColDefaultValue:
                            singleRow.setDefaultValue((String) value);                            
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

    public String getHierarchyDefName() {
        return jTextName.getText();
    }

}