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

import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.parser.Relationship;
import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.parser.Attribute;
/**
 *
 * @author  kkao
 */
public class TabOverview extends javax.swing.JPanel {
    private final String ALL_DOMAINS = org.openide.util.NbBundle.getMessage(TabOverview.class, "All_Domains");
    private ArrayList <DomainNode> mAlDomainNodes;
    private ArrayList <Relationship> mAlRelationships;
    private ArrayList <LinkType> mAlLinkTypes;
    private Map <String, DomainNode> mMapDomainNodes = new HashMap();  // domainName, DomainNode
    /** Creates new form TabOverview */
    public TabOverview(ArrayList <DomainNode> alDomainNodes) {
        initComponents();
        mAlDomainNodes = alDomainNodes;
        jComboBoxAllDomains.removeAllItems();
        jComboBoxAssociatedDomains.removeAllItems();
        jComboBoxAssociatedDomains.addItem(ALL_DOMAINS);

        DomainNode domainNode;
        String domainName;
        for (int i=0; mAlDomainNodes != null && i < mAlDomainNodes.size(); i++) {
            domainNode = mAlDomainNodes.get(i);
            domainName = domainNode.getName();
            mMapDomainNodes.put(domainName, domainNode);
            jComboBoxAllDomains.addItem(domainName);
        }
        
        ArrayList rows = new ArrayList();
        if (mAlDomainNodes != null) {
            domainNode = mAlDomainNodes.get(0);
            domainName = domainNode.getName();
            ArrayList <LinkType> alLinkTypes = domainNode.getLinkTypes();
            ArrayList <String> alAssociatedDomains = domainNode.getAssociatedDomains();
            if (alLinkTypes == null ||  alLinkTypes.size() == 0) {
                for (int i=0; alAssociatedDomains != null && i < alAssociatedDomains.size(); i++) {
                    String domainWithRelationship = alAssociatedDomains.get(i);
                    boolean bAdd = true;
                    for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                        String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                        if (associatedDomainName.equals(domainWithRelationship)) {
                            bAdd = false;
                            break;
                        }
                    }
                    if (bAdd) {
                        jComboBoxAssociatedDomains.addItem(domainWithRelationship);
                    }
                }
            } else {
                for (int i=0; alLinkTypes != null && i < alLinkTypes.size(); i++) {
                    LinkType type = alLinkTypes.get(i);
                    String sourceDomain = type.getSourceDomain();
                    String targetDomain = type.getTargetDomain();
                    String domainWithRelationship = (domainName.equals(sourceDomain)) ? targetDomain : sourceDomain;
                    boolean bAdd = true;
                    for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                        String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                        if (associatedDomainName.equals(domainWithRelationship)) {
                            bAdd = false;
                            break;
                        }
                    }
                    if (bAdd) {
                        jComboBoxAssociatedDomains.addItem(domainWithRelationship);
                    }
                    RelationshipTypeRow r = new RelationshipTypeRow(type.getName(), type.getType(), type.getSourceDomain(), type.getTargetDomain());
                    rows.add(r);
                }
            }
        }
        jComboBoxAssociatedDomains.setSelectedIndex(0);
        TableModelRelationshipType model = new TableModelRelationshipType(rows);
        jTableRelationshipTypes.setModel(model);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        //jTableRelationshipTypes.setRowSorter(sorter);
        
        jComboBoxAllDomains.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onAllDomainsItemStateChanged(evt);
            }
        });
        jComboBoxAssociatedDomains.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onDomainsItemStateChanged(evt);
            }
        });
        
        ArrayList <PredefinedAttributeRow> rowsPredefinedAttribute = new ArrayList();
        TableModelPredefinedAttribute modelPredefinedAttribute = new TableModelPredefinedAttribute(rowsPredefinedAttribute);
        this.jTableFixedAttibutes.setModel(modelPredefinedAttribute);
        
        ArrayList <ExtendedAttributeRow> rowsExtendedAttribute = new ArrayList();
        TableModelExtendedAttribute modelExtendedAttribute = new TableModelExtendedAttribute(rowsExtendedAttribute);
        this.jTableExtendedAttributes.setModel(modelExtendedAttribute);

        jTableRelationshipTypes.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    {
                        int iSelectedRow = jTableRelationshipTypes.getSelectedRow();
                        TableModelRelationshipType model = (TableModelRelationshipType) jTableRelationshipTypes.getModel();
                        String relationshipTypeName = (String) model.getValueAt(iSelectedRow,  model.iColRelationshipTypeName);
                        String relationshipSourceDomain = (String) model.getValueAt(iSelectedRow,  model.iColSourceDomain);
                        String relationshipTargetDomain = (String) model.getValueAt(iSelectedRow,  model.iColTargetDomain);
                        // load attributes 
                        // find it from mAlLinkTypes
                        for (int i=0; mAlLinkTypes != null && i < mAlLinkTypes.size(); i++) {
                            LinkType type = mAlLinkTypes.get(i);
                            String typeName = type.getName();
                            String sourceDomain = type.getSourceDomain();
                            String targetDomain = type.getTargetDomain();
                            if (relationshipTypeName.equals(typeName) &&
                                relationshipSourceDomain.equals(sourceDomain) &&
                                relationshipTargetDomain.equals(targetDomain)) {
                                // Fixed attributes
                                TableModelPredefinedAttribute modelPredefinedAttribute = (TableModelPredefinedAttribute) jTableFixedAttibutes.getModel();
                                modelPredefinedAttribute.rows.clear();
                                ArrayList <Attribute> al = type.getFixedAttributes();
                                for (int j=0; al != null && j < al.size(); j++) {
                                    Attribute attr = (Attribute) al.get(j);
                                    PredefinedAttributeRow row = new PredefinedAttributeRow(attr.getName(), attr.getValue());
                                    modelPredefinedAttribute.addRow(j, row);
                                }
                                // Extended attributes
                                TableModelExtendedAttribute modelExtendedAttribute = (TableModelExtendedAttribute) jTableExtendedAttributes.getModel();
                                modelExtendedAttribute.rows.clear();
                                al = type.getExtendedAttributes();
                                for (int j=0; al != null && j < al.size(); j++) {
                                    Attribute attr = (Attribute) al.get(j);
                                    attr.getColumnName();
                                    attr.getDataType();
                                    attr.getDefaultValue();
                                    attr.getType();
                                    ExtendedAttributeRow row = new ExtendedAttributeRow(attr.getName(), attr.getDataType(), attr.getValue());
                                    modelExtendedAttribute.addRow(j, row);
                                }

                                break;
                            }
                        }
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

        jLabelDomainName = new javax.swing.JLabel();
        jLabelAssociated = new javax.swing.JLabel();
        jComboBoxAssociatedDomains = new javax.swing.JComboBox();
        jScrollPaneRelationshipTypes = new javax.swing.JScrollPane();
        jTableRelationshipTypes = new javax.swing.JTable();
        jComboBoxAllDomains = new javax.swing.JComboBox();
        jScrollPaneAttributes = new javax.swing.JScrollPane();
        jTableFixedAttibutes = new javax.swing.JTable();
        jScrollPaneExtendedAttibutes = new javax.swing.JScrollPane();
        jTableExtendedAttributes = new javax.swing.JTable();
        jButtonAddRelationshipType = new javax.swing.JButton();
        jButtonDeleteRelationshipType = new javax.swing.JButton();
        jButtonAddExtendedAttribute = new javax.swing.JButton();
        jButtonDeleteExtendedAttribute = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Participating_Domains"))); // NOI18N
        setLayout(null);

        jLabelDomainName.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Domain")); // NOI18N
        add(jLabelDomainName);
        jLabelDomainName.setBounds(10, 30, 110, 20);
        jLabelDomainName.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabListRelationshipTypes.jLabelDomainName.AccessibleContext.accessibleName")); // NOI18N

        jLabelAssociated.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Associated_Domains")); // NOI18N
        add(jLabelAssociated);
        jLabelAssociated.setBounds(260, 30, 110, 20);

        add(jComboBoxAssociatedDomains);
        jComboBoxAssociatedDomains.setBounds(370, 30, 110, 22);

        jScrollPaneRelationshipTypes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Relationships_Defined"))); // NOI18N

        jTableRelationshipTypes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Source Domain", "Target Domain"
            }
        ));
        jScrollPaneRelationshipTypes.setViewportView(jTableRelationshipTypes);

        add(jScrollPaneRelationshipTypes);
        jScrollPaneRelationshipTypes.setBounds(10, 60, 470, 150);

        add(jComboBoxAllDomains);
        jComboBoxAllDomains.setBounds(120, 30, 110, 22);

        jScrollPaneAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Predefined_Attributes"))); // NOI18N

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
        jScrollPaneAttributes.setBounds(10, 220, 470, 130);

        jScrollPaneExtendedAttibutes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Extended_Attributes"))); // NOI18N

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
        jScrollPaneExtendedAttibutes.setBounds(10, 360, 470, 160);

        jButtonAddRelationshipType.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        add(jButtonAddRelationshipType);
        jButtonAddRelationshipType.setBounds(490, 150, 60, 23);

        jButtonDeleteRelationshipType.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Delete")); // NOI18N
        add(jButtonDeleteRelationshipType);
        jButtonDeleteRelationshipType.setBounds(490, 180, 63, 23);

        jButtonAddExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonAddExtendedAttribute.text")); // NOI18N
        add(jButtonAddExtendedAttribute);
        jButtonAddExtendedAttribute.setBounds(490, 460, 60, 23);

        jButtonDeleteExtendedAttribute.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonDeleteExtendedAttribute.text")); // NOI18N
        add(jButtonDeleteExtendedAttribute);
        jButtonDeleteExtendedAttribute.setBounds(490, 490, 63, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void onAllDomainsItemStateChanged(java.awt.event.ItemEvent evt) {
        String domainName = (String) jComboBoxAllDomains.getSelectedItem();
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        mAlLinkTypes = domainNode.getLinkTypes();
        ArrayList <String> alAssociatedDomains = domainNode.getAssociatedDomains();
        if (jComboBoxAssociatedDomains.getItemCount() > 0) {
            jComboBoxAssociatedDomains.removeAllItems();
        }
        jComboBoxAssociatedDomains.addItem(ALL_DOMAINS);
        if (mAlLinkTypes == null ||  mAlLinkTypes.size() == 0) {
            for (int i=0; alAssociatedDomains != null && i < alAssociatedDomains.size(); i++) {
                String domainWithRelationship = alAssociatedDomains.get(i);
                boolean bAdd = true;
                for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                    String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                    if (associatedDomainName.equals(domainWithRelationship)) {
                        bAdd = false;
                        break;
                    }
                }
                if (bAdd) {
                    jComboBoxAssociatedDomains.addItem(domainWithRelationship);
                }
            }
        } else {
            for (int i=0; mAlLinkTypes != null && i < mAlLinkTypes.size(); i++) {
                LinkType type = mAlLinkTypes.get(i);
                String sourceDomain = type.getSourceDomain();
                String targetDomain = type.getTargetDomain();
                String domainWithRelationship = (domainName.equals(sourceDomain)) ? targetDomain : sourceDomain;
                boolean bAdd = true;
                for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                    String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                    if (associatedDomainName.equals(domainWithRelationship)) {
                        bAdd = false;
                        break;
                    }
                }
                if (bAdd) {
                    jComboBoxAssociatedDomains.addItem(domainWithRelationship);
                }
            }
        }
        jComboBoxAssociatedDomains.setSelectedIndex(0);
    }

    private void onDomainsItemStateChanged(java.awt.event.ItemEvent evt) {
        String domainName = (String) jComboBoxAllDomains.getSelectedItem();
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        mAlLinkTypes = domainNode.getLinkTypes();

        String associatedDomain = (String) jComboBoxAssociatedDomains.getSelectedItem();
        TableModelRelationshipType model = (TableModelRelationshipType) jTableRelationshipTypes.getModel();
        model.rows.clear();
        int index = 0;

        for (int i=0; mAlLinkTypes != null && i < mAlLinkTypes.size(); i++) {
            LinkType type = mAlLinkTypes.get(i);
            String sourceDomain = type.getSourceDomain();
            String targetDomain = type.getTargetDomain();
            boolean associated = false;
            if (associatedDomain != null) {
                associated = (associatedDomain.equals(sourceDomain)) | (associatedDomain.equals(targetDomain));
            }
            if (jComboBoxAssociatedDomains.getSelectedIndex() == 0 || associated) {
                RelationshipTypeRow r = new RelationshipTypeRow(type.getName(), type.getType(), type.getSourceDomain(), type.getTargetDomain());
                model.addRow(index++, r);
            }
        }
        model.fireTableDataChanged();
        
        // clean up attribute tables
        TableModelPredefinedAttribute modelPredefinedAttribute = (TableModelPredefinedAttribute) jTableFixedAttibutes.getModel();
        modelPredefinedAttribute.rows.clear();
        modelPredefinedAttribute.fireTableDataChanged();
        TableModelExtendedAttribute modelExtendedAttribute = (TableModelExtendedAttribute) jTableExtendedAttributes.getModel();
        modelExtendedAttribute.rows.clear();
        modelExtendedAttribute.fireTableDataChanged();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddExtendedAttribute;
    private javax.swing.JButton jButtonAddRelationshipType;
    private javax.swing.JButton jButtonDeleteExtendedAttribute;
    private javax.swing.JButton jButtonDeleteRelationshipType;
    private javax.swing.JComboBox jComboBoxAllDomains;
    private javax.swing.JComboBox jComboBoxAssociatedDomains;
    private javax.swing.JLabel jLabelAssociated;
    private javax.swing.JLabel jLabelDomainName;
    private javax.swing.JScrollPane jScrollPaneAttributes;
    private javax.swing.JScrollPane jScrollPaneExtendedAttibutes;
    private javax.swing.JScrollPane jScrollPaneRelationshipTypes;
    private javax.swing.JTable jTableExtendedAttributes;
    private javax.swing.JTable jTableFixedAttibutes;
    private javax.swing.JTable jTableRelationshipTypes;
    // End of variables declaration//GEN-END:variables
    
    class RelationshipTypeRow {

        private String relationshipTypeName;
        private String type;
        private String sourceDomain;
        private String targetDomain;

        public RelationshipTypeRow(String relationshipTypeName, String type, String sourceDomain, String targetDomain) {
            this.relationshipTypeName = relationshipTypeName;
            this.type = type;
            this.sourceDomain = sourceDomain;
            this.targetDomain = targetDomain;
        }

        public String getRelationshipTypeName() {
            return relationshipTypeName;
        }

        public void setRelationshipTypeName(String relationshipTypeName) {
            this.relationshipTypeName = relationshipTypeName;
        }
        
        public String getRelationshipTypeType() {
            return type;
        }

        public void setRelationshipTypeType(String type) {
            this.type = type;
        }

        public String getSourceDomain() {
            return sourceDomain;
        }

        public void setSourceDomain(String sourceDomain) {
            this.sourceDomain = sourceDomain;
        }

        public String getTargetDomain() {
            return targetDomain;
        }

        public void setTargetDomain(String targetDomain) {
            this.targetDomain = targetDomain;
        }
    }

    // Table model for Relationship Type
    class TableModelRelationshipType extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_RelationshipType_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_RelationshipType_Type"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Source_Domain"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Target_Domain"), 
                                        };
        ArrayList <RelationshipTypeRow> rows;
        final static int iColRelationshipTypeName = 0;
        final static int iColRelationshipTypeType = 1;
        final static int iColSourceDomain = 2;
        final static int iColTargetDomain = 3;
        
        TableModelRelationshipType(ArrayList rows) {
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
                RelationshipTypeRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColRelationshipTypeName:
                            return singleRow.getRelationshipTypeName();
                        case iColRelationshipTypeType:
                            return singleRow.getRelationshipTypeType();
                        case iColSourceDomain:
                           return singleRow.getSourceDomain();
                        case iColTargetDomain:
                           return singleRow.getTargetDomain();
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
                RelationshipTypeRow singleRow = (RelationshipTypeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColRelationshipTypeName:
                            singleRow.setRelationshipTypeName((String) value);                            
                            break;
                        case iColRelationshipTypeType:
                            singleRow.setRelationshipTypeType((String) value);                            
                            break;
                        case iColSourceDomain:
                            singleRow.setSourceDomain((String) value);                            
                            break;
                        case iColTargetDomain:
                            singleRow.setTargetDomain((String) value);                            
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
        
        public void addRow(int index, RelationshipTypeRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public RelationshipTypeRow getRow(int index) {
            RelationshipTypeRow row = (RelationshipTypeRow) rows.get(index);
            return row;
        }
    }
    
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
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Default_Value"), 
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
        private String dataType;
        private String defaultValue;

        public ExtendedAttributeRow(String name, String dataType, String defaultValue) {
            this.name = name;
            this.dataType = dataType;
            this.defaultValue = defaultValue;
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
    }

    // Table model for Relationship Type
    class TableModelExtendedAttribute extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_ColumnName"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_DataType"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Default_Value"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Searchable"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_Required"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Attribute_ID"), 
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
