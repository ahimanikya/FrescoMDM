/*
 * TabRelationshipTypes.java
 *
 * Created on October 1, 2008, 9:54 AM
 */

package com.sun.mdm.multidomain.project.editor;

import org.openide.util.NbBundle;
import java.util.ArrayList;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;

import com.sun.mdm.multidomain.parser.RelationshipType;
/**
 *
 * @author  kkao
 */
public class TabListRelationshipTypes extends javax.swing.JPanel {

    /** Creates new form TabRelationshipTypes */
    public TabListRelationshipTypes(ArrayList <RelationshipType> alRelationshipTypes) {
        initComponents();
        
        //Load relationship type list
        ArrayList rows = new ArrayList();
        for (int i=0; alRelationshipTypes != null && i < alRelationshipTypes.size(); i++) {
            RelationshipType type = alRelationshipTypes.get(i);
            RelationshipTypeRow r = new RelationshipTypeRow(type.getName(), type.getType(), type.getSourceDomain(), type.getTargetDomain());
            rows.add(r);
        }
        TableModelRelationshipType model = new TableModelRelationshipType(rows);
        this.jTableRelationshipTypes.setModel(model);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneRelationshipTypes = new javax.swing.JScrollPane();
        jTableRelationshipTypes = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), org.openide.util.NbBundle.getMessage(TabListRelationshipTypes.class, "LBL_Relationship_Types"))); // NOI18N

        jTableRelationshipTypes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Source Domain", "Target Domain"
            }
        ));
        jScrollPaneRelationshipTypes.setViewportView(jTableRelationshipTypes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneRelationshipTypes, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPaneRelationshipTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(223, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneRelationshipTypes;
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
        private	String columnNames [] = {NbBundle.getMessage(TabListRelationshipTypes.class, "LBL_RelationshipType_Name"),
                                         NbBundle.getMessage(TabListRelationshipTypes.class, "LBL_RelationshipType_Type"), 
                                         NbBundle.getMessage(TabListRelationshipTypes.class, "LBL_Source_Domain"), 
                                         NbBundle.getMessage(TabListRelationshipTypes.class, "LBL_Target_Domain"), 
                                        };
        ArrayList rows;
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
                RelationshipTypeRow singleRow = (RelationshipTypeRow) rows.get(row);
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

        public int findRowByRelationshipTypeName(String name) {
            for (int i=0; i < rows.size(); i++) {
                if (getValueAt(i, iColRelationshipTypeName).equals(name)) {
                    return i;
                }
            }
            return -1;
        }
    }

}