/*
 * DomainRecordDetailDialog.java
 *
 * Created on October 17, 2008, 10:42 AM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class DomainRecordDetailDialog extends javax.swing.JDialog {

    private RecordDetail mRecordDetail = null;
    
    private DomainNode mDomainNode = null;
    
    private boolean bModified = false;
    /** Creates new form DomainRecordDetailDialog */
    public DomainRecordDetailDialog(RecordDetail recDet, boolean isNew, DomainNode domainNode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mRecordDetail = recDet;
        initComponents();
        mDomainNode = domainNode;
        if (isNew) {
            loadNewRecordDetail();
        } else {
            loadRecordDetail();
        }
        
        jTableFieldGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTableFieldGroup.getSelectedRow();
                //FieldGroup fieldGroup = ((TableModelFieldGroup) jTableFieldGroup.getModel()).getRow(selectedRow);
                GroupRow group =  ((TableModelFieldGroup) jTableFieldGroup.getModel()).getRow(selectedRow);
                TableModelField fieldModel = new TableModelField(group);
                jTableField.setModel(fieldModel);
                jBtnAddField.setEnabled(true);
                jBtnRemoveGroup.setEnabled(true);

            }
        });
        
        jTableField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtnRemoveField.setEnabled(true);

            }
        });
        
        jTxtRecordDetail.requestFocus();
        
        jTxtRecordDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBtnOK.setEnabled(setEnabledOK());
            }
        });
        
        jBtnOK.setEnabled(setEnabledOK());
        
        //this.jTableFieldGroup.add
    }
    
   private void loadNewRecordDetail() {
        this.jTxtRecordDetail.setText(mRecordDetail.getDisplayName());
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        /**
        for (FieldGroup fieldGroup : mRecordDetail.getFieldGroups()) {
            GroupRow group = new GroupRow(fieldGroup.getDescription());
            ArrayList<FieldRow> fields = new ArrayList<FieldRow>();
            group.setFieldRows(fields);
            for (FieldGroup.FieldRef field : fieldGroup.getFieldRefs()) {
                fields.add(new FieldRow(field.getFieldName()));
            }
            groups.add(group);
        }
         */ 
        TableModelFieldGroup fieldGroupModel = new TableModelFieldGroup(groups);
        
        jTableFieldGroup.setModel(fieldGroupModel);
        
        //jTableFieldGroup.setRowSelectionInterval(0, 0);
        /**
        TableModelField fieldModel = new TableModelField(new GroupRow("New Group"));
        this.jTableField.setModel(fieldModel);
         */ 
        
        
    }
   
    private void loadRecordDetail() {
        this.jTxtRecordDetail.setText(mRecordDetail.getDisplayName());
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        for (FieldGroup fieldGroup : mRecordDetail.getFieldGroups()) {
            GroupRow group = new GroupRow(fieldGroup.getDescription());
            group.setGroupId(fieldGroup.hashCode());
            ArrayList<FieldRow> fields = new ArrayList<FieldRow>();
            group.setFieldRows(fields);
            for (FieldGroup.FieldRef field : fieldGroup.getFieldRefs()) {
                fields.add(new FieldRow(field.getFieldName()));
            }
            groups.add(group);
        }
        TableModelFieldGroup fieldGroupModel = new TableModelFieldGroup(groups);
        jTableFieldGroup.setModel(fieldGroupModel);
        if (groups.size() > 0) {
            TableModelField fieldModel = new TableModelField(groups.get(0));
            this.jTableField.setModel(fieldModel);
        }
        
        
    }

    private boolean setEnabledOK() {
         boolean setEnabled = true;
         
         if (jTxtRecordDetail.getText() == null || jTxtRecordDetail.getText().length() == 0) {
             return !setEnabled;
         }
    
         if (this.jTableFieldGroup.getRowCount() == 0 ) {
             return !setEnabled;
         }
         
         TableModelFieldGroup groupModel = (TableModelFieldGroup) jTableFieldGroup.getModel();
         ArrayList groups =  groupModel.groupRows;
         for (Object groupObj : groups) {
             GroupRow group = (GroupRow)  groupObj;
             if (group.getFieldRows().size() == 0) {
                 return !setEnabled;
             }
         }
         
         return setEnabled;
        
    }
    
    public boolean isModifed() {
        return this.bModified;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTxtRecordDetail = new javax.swing.JTextField();
        jPanelFieldGroup = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFieldGroup = new javax.swing.JTable();
        jBtnAddGroup = new javax.swing.JButton();
        jBtnRemoveGroup = new javax.swing.JButton();
        jPanelField = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableField = new javax.swing.JTable();
        jBtnAddField = new javax.swing.JButton();
        jBtnRemoveField = new javax.swing.JButton();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_DOMAIN_RECORD_DETAIL_PROPERTIES")); // NOI18N
        setResizable(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_DOMAIN_RECORD_DETAIL_FIELD")); // NOI18N

        jPanelFieldGroup.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_FIELD_GROUP_PROPERTIES"))); // NOI18N

        jTableFieldGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Field Group"
            }
        ));
        jScrollPane1.setViewportView(jTableFieldGroup);
        jTableFieldGroup.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_FIELD_GROUP")); // NOI18N

        jBtnAddGroup.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_Add")); // NOI18N
        jBtnAddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddGroup(evt);
            }
        });

        jBtnRemoveGroup.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_Remove")); // NOI18N
        jBtnRemoveGroup.setEnabled(false);
        jBtnRemoveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveGroup(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelFieldGroupLayout = new org.jdesktop.layout.GroupLayout(jPanelFieldGroup);
        jPanelFieldGroup.setLayout(jPanelFieldGroupLayout);
        jPanelFieldGroupLayout.setHorizontalGroup(
            jPanelFieldGroupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelFieldGroupLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .add(jBtnAddGroup)
                .add(30, 30, 30)
                .add(jBtnRemoveGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(jPanelFieldGroupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanelFieldGroupLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 231, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(11, Short.MAX_VALUE)))
        );

        jPanelFieldGroupLayout.linkSize(new java.awt.Component[] {jBtnAddGroup, jBtnRemoveGroup}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanelFieldGroupLayout.setVerticalGroup(
            jPanelFieldGroupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelFieldGroupLayout.createSequentialGroup()
                .addContainerGap(148, Short.MAX_VALUE)
                .add(jPanelFieldGroupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddGroup)
                    .add(jBtnRemoveGroup)))
            .add(jPanelFieldGroupLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanelFieldGroupLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );

        jPanelField.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_FIELD_PROPERTIES"))); // NOI18N

        jTableField.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Field"
            }
        ));
        jScrollPane2.setViewportView(jTableField);
        jTableField.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_FIELD")); // NOI18N

        jBtnAddField.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_Add")); // NOI18N
        jBtnAddField.setEnabled(false);
        jBtnAddField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddField(evt);
            }
        });

        jBtnRemoveField.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_Remove")); // NOI18N
        jBtnRemoveField.setEnabled(false);
        jBtnRemoveField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveField(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelFieldLayout = new org.jdesktop.layout.GroupLayout(jPanelField);
        jPanelField.setLayout(jPanelFieldLayout);
        jPanelFieldLayout.setHorizontalGroup(
            jPanelFieldLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelFieldLayout.createSequentialGroup()
                .add(47, 47, 47)
                .add(jBtnAddField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jBtnRemoveField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelFieldLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelFieldLayout.linkSize(new java.awt.Component[] {jBtnAddField, jBtnRemoveField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanelFieldLayout.setVerticalGroup(
            jPanelFieldLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelFieldLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(jPanelFieldLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddField)
                    .add(jBtnRemoveField)))
        );

        jBtnOK.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_OK")); // NOI18N
        jBtnOK.setEnabled(false);
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOK(evt);
            }
        });

        jBtnCancel.setText(org.openide.util.NbBundle.getMessage(DomainRecordDetailDialog.class, "LBL_Cancel")); // NOI18N
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jLabel1)
                        .add(18, 18, 18)
                        .add(jTxtRecordDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 327, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jBtnOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(8, 8, 8))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanelFieldGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(61, 61, 61))
        );

        layout.linkSize(new java.awt.Component[] {jBtnCancel, jBtnOK}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTxtRecordDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jPanelField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jBtnCancel)
                            .add(jBtnOK)))
                    .add(jPanelFieldGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-572)/2, (screenSize.height-320)/2, 572, 320);
    }// </editor-fold>//GEN-END:initComponents

private void onAddGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddGroup
// TODO add your handling code here:
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    int iInsertRow = fieldGroupModel.getRowCount();
    //FieldGroup newFieldGroup = new FieldGroup();
    GroupRow newFieldGroup = new GroupRow("New Group");
    //Parameter newParam = mSearchType.getSearchOption().createParameter("", "");
    fieldGroupModel.addRow(iInsertRow, newFieldGroup);
    jTableFieldGroup.setModel(fieldGroupModel);
    jTableFieldGroup.clearSelection();
    jTableFieldGroup.addRowSelectionInterval(iInsertRow, iInsertRow);
    jTableFieldGroup.setEditingRow(iInsertRow);
    jTableFieldGroup.setFocusTraversalKeysEnabled(true);
    jBtnOK.setEnabled(setEnabledOK());
    
}//GEN-LAST:event_onAddGroup

private void onRemoveGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveGroup
// TODO add your handling code here:
// TODO add your handling code here:
    int rs[] = this.jTableFieldGroup.getSelectedRows();
    int length = rs.length;
    String prompt = (length == 1) ? NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Row_Prompt")
            : NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Rows_Prompt");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelFieldGroup model = (TableModelFieldGroup) jTableFieldGroup.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object fieldGroup = (Object) model.getValueAt(j, model.iColRecordDetailName);
                model.removeRow(j);
            }
            if (jTableFieldGroup.getRowCount() > 0) {
                this.jBtnRemoveGroup.setEnabled(true);
                jTableFieldGroup.setRowSelectionInterval(0, 0);
            } else {
                this.jBtnOK.setEnabled(false);
            }
            this.bModified = true;
        }
    
}//GEN-LAST:event_onRemoveGroup

private void onAddField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddField
// TODO add your handling code here:
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) jTableFieldGroup.getModel();
    TableModelField fieldModel = (TableModelField) jTableField.getModel();
    int selectedRow = jTableFieldGroup.getSelectedRow();
    GroupRow group = fieldGroupModel.getRow(selectedRow);
    FieldGroup fieldGroup = mRecordDetail.getFieldGroup(group.getGroupName());
    if (fieldGroup == null) {
        fieldGroup = mRecordDetail.getFieldGroup(group.getGroupId());
    }
    
    EntityTreeDialog entityDlg = new EntityTreeDialog(mDomainNode.getEntityTree(), fieldGroup);
    entityDlg.setVisible(true);
    if (entityDlg.isSelected()) {
        if (entityDlg.getFieldList().size() > 0) {
            for (String fieldName : entityDlg.getFieldList()) {
                FieldRow fieldRef = new FieldRow(fieldName);
                fieldModel.addRow(fieldModel.getRowCount(), fieldRef);
            }
            jTableField.setModel(fieldModel);
        }
    }
    
    jBtnOK.setEnabled(setEnabledOK());
    
}//GEN-LAST:event_onAddField

private void onRemoveField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveField
// TODO add your handling code here:
// TODO add your handling code here:
    int rs[] = jTableField.getSelectedRows();
    int length = rs.length;
    String prompt = (length == 1) ? NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Row_Prompt")
            : NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Rows_Prompt");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(DomainRecordDetailDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelField model = (TableModelField) jTableField.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object standardizationType = (Object) model.getValueAt(j, model.iColFieldName);
                model.removeRow(j);
            }
            
            
            if (jTableField.getRowCount() > 0) {
                this.jBtnRemoveField.setEnabled(true);
                jTableField.setRowSelectionInterval(0, 0);
            } else {
                this.jBtnOK.setEnabled(false);
            }

            this.bModified = true;
        }
    
}//GEN-LAST:event_onRemoveField

private void onOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOK
// TODO add your handling code here:
    bModified = true;
    mRecordDetail.setDisplayName(this.jTxtRecordDetail.getText());
    mRecordDetail.getFieldGroups().clear();
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    ArrayList<GroupRow> groups = fieldGroupModel.groupRows;
    for (GroupRow group : fieldGroupModel.groupRows) {
        FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setDescription(group.getGroupName());
        for (FieldRow field : group.getFieldRows()) {
            fieldGroup.addFieldRef(fieldGroup.createFieldRef(field.getFieldName()));
        }
        mRecordDetail.addFieldGroup(fieldGroup);
        
    }
    this.dispose();

}//GEN-LAST:event_onOK

private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
// TODO add your handling code here:
    bModified = false;
    this.dispose();
}//GEN-LAST:event_onCancel


    class FieldRow {
        private String fieldName;
        
        
        public FieldRow(String name) {
            fieldName = name;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

    }
    class GroupRow {
        
        private String groupName;
        
        private int groupId;

        private ArrayList<FieldRow> fieldRows = null;
        
        public GroupRow(String groupName) {
            this.groupName = groupName;
        }
        
        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        public ArrayList<FieldRow> getFieldRows() {
            return fieldRows;
        }

        public void setFieldRows(ArrayList<FieldRow> fieldRows) {
            this.fieldRows = fieldRows;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }
        
        
    }
    class TableModelFieldGroup extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_FIELD_GROUP"),};
        ArrayList<GroupRow> groupRows;
        final static int iColRecordDetailName = 0;

        TableModelFieldGroup(ArrayList rows) {
            groupRows = rows;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (groupRows != null) {
                return groupRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (groupRows != null) {
                GroupRow singleRow = (GroupRow) groupRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColRecordDetailName:
                            if (singleRow.getGroupName() != null && singleRow.getGroupName().length() > 0) {
                                return singleRow.getGroupName();
                            }
                            int rowIdx = row + 1;
                            return "FieldGroup-" + rowIdx;
                            //return singleRow.getDisplayName();
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public GroupRow getRow(int row) {
            if (groupRows != null) {
                 //singleRow = (FieldGroup) groupRows.get(row);
                return groupRows.get(row);
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
                case iColRecordDetailName:
                    ((GroupRow) groupRows.get(row)).setGroupName((String) value);
                    //((RecordDetail) fieldRows.get(row)).setRecordDetailId(Integer.parseInt((String) value));
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            groupRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, GroupRow row) {
            //fieldRows.add(row);
            groupRows.add(index, row);
            this.fireTableRowsInserted(index, index);
        }

        /**
        public MatchRuleRowPerProbType getRow(int index) {
        //MatchRuleRowPerProbType row = (MatchRuleRowPerProbType) matchRuleRows.get(index);
        return row;
        }
         */
        public int findRowByFieldName(String fieldName) {
            for (int i = 0; i < groupRows.size(); i++) {
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public GroupRow findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < groupRows.size(); i++) {
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return (GroupRow) groupRows.get(i);
                }
            }
            return null;
        }
    }


     class TableModelField extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_FIELD"),};
        ArrayList<FieldRow> fieldRows;
        final static int iColFieldName = 0;
        private GroupRow mGroup = null;

        TableModelField(GroupRow group) {
           // mFieldGroup = fieldGroup;
           // fieldRows = fieldGroup.getFieldRefs();
            mGroup = group;
            fieldRows = mGroup.getFieldRows();
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
                FieldRow singleRow = (FieldRow) fieldRows.get(row);
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

        public FieldRow getRow(int row) {
            if (fieldRows != null) {
                FieldRow singleRow = (FieldRow) fieldRows.get(row);
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
                    fieldRows.set(row, (FieldRow) value);
                   // mFieldGroup.createFieldRef((String) value);
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, FieldRow row) {
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
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public FieldRow findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColFieldName).equals(fieldName)) {
                    return (FieldRow) fieldRows.get(i);
                }
            }
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAddField;
    private javax.swing.JButton jBtnAddGroup;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JButton jBtnRemoveField;
    private javax.swing.JButton jBtnRemoveGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelField;
    private javax.swing.JPanel jPanelFieldGroup;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableField;
    private javax.swing.JTable jTableFieldGroup;
    private javax.swing.JTextField jTxtRecordDetail;
    // End of variables declaration//GEN-END:variables

}
