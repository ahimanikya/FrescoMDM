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
public class TabAttributes extends javax.swing.JPanel {
    /** Creates new form TabAttributes */
    public TabAttributes(LinkType linkType) {
        initComponents();
        
        ArrayList <PredefinedAttributeRow> rowsPredefinedAttribute = new ArrayList();
        TableModelPredefinedAttribute modelPredefinedAttribute = new TableModelPredefinedAttribute(rowsPredefinedAttribute);
        this.jTableFixedAttibutes.setModel(modelPredefinedAttribute);
        
        ArrayList <ExtendedAttributeRow> rowsExtendedAttribute = new ArrayList();
        TableModelExtendedAttribute modelExtendedAttribute = new TableModelExtendedAttribute(rowsExtendedAttribute);
        this.jTableExtendedAttributes.setModel(modelExtendedAttribute);
        String typeName = linkType.getName();
        String sourceDomain = linkType.getSourceDomain();
        String targetDomain = linkType.getTargetDomain();
        // Fixed attributes
        modelPredefinedAttribute.rows.clear();
        ArrayList <Attribute> al = linkType.getFixedAttributes();
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

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), org.openide.util.NbBundle.getMessage(TabAttributes.class, "LBL_Attributes"))); // NOI18N
        setLayout(null);

        jScrollPaneAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabAttributes.class, "LBL_Predefined_Attributes"))); // NOI18N

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
        jScrollPaneAttributes.setBounds(10, 20, 550, 240);

        jScrollPaneExtendedAttibutes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabAttributes.class, "LBL_Extended_Attributes"))); // NOI18N

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
        jScrollPaneExtendedAttibutes.setBounds(10, 270, 550, 230);

        jButtonAddExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabAttributes.class, "LBL_Add")); // NOI18N
        add(jButtonAddExtendedAttribute);
        jButtonAddExtendedAttribute.setBounds(380, 510, 90, 23);

        jButtonDeleteExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabAttributes.class, "LBL_Remove")); // NOI18N
        add(jButtonDeleteExtendedAttribute);
        jButtonDeleteExtendedAttribute.setBounds(473, 510, 80, 23);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddExtendedAttribute;
    private javax.swing.JButton jButtonDeleteExtendedAttribute;
    private javax.swing.JScrollPane jScrollPaneAttributes;
    private javax.swing.JScrollPane jScrollPaneExtendedAttibutes;
    private javax.swing.JTable jTableExtendedAttributes;
    private javax.swing.JTable jTableFixedAttibutes;
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
        private	String columnNames [] = {NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Default_Value"), 
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
        private	String columnNames [] = {NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_ColumnName"), 
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_DataType"), 
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Default_Value"), 
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Searchable"), 
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_Required"), 
                                         NbBundle.getMessage(TabAttributes.class, "LBL_Attribute_ID"), 
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
                            return singleRow.getDataType();
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
                ExtendedAttributeRow singleRow = (ExtendedAttributeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColName:
                            singleRow.setName((String) value);                            
                            break;
                        case iColColumnName:
                            singleRow.setDataType((String) value);                            
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
        
        public void addRow(int index, ExtendedAttributeRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public ExtendedAttributeRow getRow(int index) {
            ExtendedAttributeRow row = (ExtendedAttributeRow) rows.get(index);
            return row;
        }
    }

}