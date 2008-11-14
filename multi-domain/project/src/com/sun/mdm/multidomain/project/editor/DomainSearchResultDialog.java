/*
 * DomainSearchResultDialog.java
 *
 * Created on October 15, 2008, 3:42 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class DomainSearchResultDialog extends javax.swing.JDialog {

    private SearchDetail mSearchDetail = null;
    
    private ArrayList<RecordDetail> mRecordDetailList = null;
    
    private boolean bModified = false;
    
    private NumbericVerifier verifier = new NumbericVerifier();
    
    private DomainNode mDomainNode = null;
    
    /** Creates new form DomainSearchResultDialog */
    public DomainSearchResultDialog(SearchDetail searchDetail, boolean isNew, DomainNode domainNode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mSearchDetail = searchDetail;
        mDomainNode = domainNode;
        initComponents();
        jSpinnerItemPerPage.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerMaxItems.setModel(new SpinnerNumberModel(1, 1, 300000, 1));
        jSpinnerItemPerPage.setInputVerifier(verifier);
        jSpinnerMaxItems.setInputVerifier(verifier);
        if (isNew) {
            loadNewSearchDetail();
        } else {
            loadSearchDetail();
        }
        
        jTableFieldGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTableFieldGroup.getSelectedRow();
                GroupRow fieldGroup = ((TableModelFieldGroup) jTableFieldGroup.getModel()).getRow(selectedRow);
                TableModelField fieldModel = new TableModelField(fieldGroup);
                jTableField.setModel(fieldModel);
                jBtnAddField.setEnabled(true);
                jBtnRemoveGroup.setEnabled(true);

            }
        });
        
        ListSelectionModel rowSM = jTableFieldGroup.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //retreiveFieldProperties();
                TableModelFieldGroup FieldGropModel = (TableModelFieldGroup) jTableFieldGroup.getModel();
                int iSelectedRow = jTableFieldGroup.getSelectedRow();
                if (iSelectedRow >= 0) {
                    int row = jTableFieldGroup.getSelectedRow();
                    GroupRow selectGroup = FieldGropModel.getRow(iSelectedRow);
                    TableModelField fieldModel = new TableModelField(selectGroup);
                    jTableField.setModel(fieldModel);
                }

            }
        });

        this.jTableField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtnRemoveField.setEnabled(true);

            }
        });
        jTxtResultName.requestFocus();
    }
    

    public boolean isModified() {
        return this.bModified;
    }
    
    /**
     * load info for New Search Detail
     */
    private void loadNewSearchDetail() {
        //jCBRecordDetail.setSelectedIndex(0);
        TableModelFieldGroup groupModel = new TableModelFieldGroup(mSearchDetail.getFieldGroups());
        jTableFieldGroup.setModel(groupModel);
        /**
        TableModelField fieldModel = new TableModelField(new FieldGroup());
        jTableField.setModel(fieldModel);
         */ 

    }
    
    /**
     * The method will load info from SearchDetail for existing SearchDetail row.
     */
    private void loadSearchDetail() {
        jTxtResultName.setText(mSearchDetail.getDisplayName());
        jSpinnerItemPerPage.setValue(setIntegerValue(mSearchDetail.getItemPerPage()));
        jSpinnerMaxItems.setValue(setIntegerValue(mSearchDetail.getMaxResultSize()));
        //jCBRecordDetail.setSelectedIndex(mSearchDetail.getRecordDetailID() - 1);
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        for (FieldGroup fieldGroup : mSearchDetail.getFieldGroups()) {
            GroupRow group = new GroupRow(fieldGroup.getDescription());
            group.setGroupId(fieldGroup.hashCode());
            ArrayList<FieldRow> fields = new ArrayList<FieldRow>();
            group.setFieldRows(fields);
            for (FieldGroup.FieldRef field : fieldGroup.getFieldRefs()) {
                fields.add(new FieldRow(field.getFieldName()));
            }
            groups.add(group);
        }
        TableModelFieldGroup groupModel = new TableModelFieldGroup(groups);
        jTableFieldGroup.setModel(groupModel);
        TableModelField fieldModel = new TableModelField(groups.get(0));
        jTableField.setModel(fieldModel);
    }
    
    private Integer setIntegerValue(int value) {
        return Integer.valueOf(value);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelSearchResultName = new javax.swing.JLabel();
        jTxtResultName = new javax.swing.JTextField();
        jLabelItemPerPage = new javax.swing.JLabel();
        jSpinnerItemPerPage = new javax.swing.JSpinner();
        jLabelMaxItems = new javax.swing.JLabel();
        jSpinnerMaxItems = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableFieldGroup = new javax.swing.JTable();
        jBtnAddGroup = new javax.swing.JButton();
        jBtnRemoveGroup = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableField = new javax.swing.JTable();
        jBtnRemoveField = new javax.swing.JButton();
        jBtnAddField = new javax.swing.JButton();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_DOMAIN_SEARCH_RESULT_PROPERTIES")); // NOI18N
        setResizable(false);

        jLabelSearchResultName.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_DOMAIN_SEARCH_RESULT_NAME")); // NOI18N

        jTxtResultName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtResultNameActionPerformed(evt);
            }
        });

        jLabelItemPerPage.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_DOMAIN_SEARCH_RESULT_ITEM_PER_PAGE")); // NOI18N

        jLabelMaxItems.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_DOMAIN_SEARCH_RESULT_MAX_ITEMS")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_FIELD_GROUPS"))); // NOI18N

        jTableFieldGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Field Group"
            }
        ));
        jScrollPane1.setViewportView(jTableFieldGroup);
        jTableFieldGroup.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_FIELD_GROUP")); // NOI18N

        jBtnAddGroup.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_Add")); // NOI18N
        jBtnAddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddGroup(evt);
            }
        });

        jBtnRemoveGroup.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_Remove")); // NOI18N
        jBtnRemoveGroup.setEnabled(false);
        jBtnRemoveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveGroup(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jBtnAddGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(32, 32, 32)
                        .add(jBtnRemoveGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddGroup)
                    .add(jBtnRemoveGroup))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_FIELDS"))); // NOI18N

        jTableField.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Field"
            }
        ));
        jScrollPane2.setViewportView(jTableField);
        jTableField.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_FIELD")); // NOI18N

        jBtnRemoveField.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_Remove")); // NOI18N
        jBtnRemoveField.setEnabled(false);
        jBtnRemoveField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveField(evt);
            }
        });

        jBtnAddField.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_Add")); // NOI18N
        jBtnAddField.setEnabled(false);
        jBtnAddField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddField(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .add(jBtnAddField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jBtnRemoveField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {jBtnAddField, jBtnRemoveField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddField)
                    .add(jBtnRemoveField))
                .addContainerGap())
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(1, 1, 1)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(39, Short.MAX_VALUE)))
        );

        jBtnOK.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_OK")); // NOI18N
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOK(evt);
            }
        });

        jBtnCancel.setText(org.openide.util.NbBundle.getMessage(DomainSearchResultDialog.class, "LBL_Cancel")); // NOI18N
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
                        .add(19, 19, 19)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelItemPerPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelSearchResultName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelMaxItems))
                        .add(35, 35, 35)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTxtResultName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerMaxItems)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jSpinnerItemPerPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)))
                        .add(87, 87, 87))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(jBtnOK)
                                .add(18, 18, 18)
                                .add(jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jBtnCancel, jBtnOK}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelSearchResultName)
                    .add(jTxtResultName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelItemPerPage)
                    .add(jSpinnerItemPerPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMaxItems)
                    .add(jSpinnerMaxItems, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnCancel)
                    .add(jBtnOK))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jPanel1, jPanel2}, org.jdesktop.layout.GroupLayout.VERTICAL);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-522)/2, (screenSize.height-428)/2, 522, 428);
    }// </editor-fold>//GEN-END:initComponents

private void jTxtResultNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtResultNameActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTxtResultNameActionPerformed

private void onOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOK
// TODO add your handling code here:
    bModified = true;
    mSearchDetail.setDisplayName(this.jTxtResultName.getText());
    mSearchDetail.setItemPerPage(getIntValue(this.jSpinnerItemPerPage.getValue()));
    mSearchDetail.setMaxResultSize(getIntValue(this.jSpinnerMaxItems.getValue()));
    mSearchDetail.getFieldGroups().clear();
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    ArrayList<GroupRow> groups = fieldGroupModel.groupRows;
    for (GroupRow group : fieldGroupModel.groupRows) {
        FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setDescription(group.getGroupName());
        for (FieldRow field : group.getFieldRows()) {
            fieldGroup.addFieldRef(fieldGroup.createFieldRef(field.getFieldName()));
        }
        mSearchDetail.addFieldGroup(fieldGroup);
        
    }
    this.dispose();
}//GEN-LAST:event_onOK

private int getIntValue(Object intObject) {
    return ((Integer) intObject).intValue();
}
private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
// TODO add your handling code here:
    bModified = false;
    this.dispose();
}//GEN-LAST:event_onCancel

private void onAddGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddGroup
// TODO add your handling code here:
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    int iInsertRow = fieldGroupModel.getRowCount();
    //FieldGroup newFieldGroup = new FieldGroup();
    //Parameter newParam = mSearchType.getSearchOption().createParameter("", "");
    GroupRow newFieldGroup = new GroupRow("New Group");
    fieldGroupModel.addRow(iInsertRow, newFieldGroup);
    jTableFieldGroup.setModel(fieldGroupModel);
    jTableFieldGroup.clearSelection();
    jTableFieldGroup.addRowSelectionInterval(iInsertRow, iInsertRow);
    jTableFieldGroup.setEditingRow(iInsertRow);
    jTableFieldGroup.setFocusTraversalKeysEnabled(true);
}//GEN-LAST:event_onAddGroup

private void onRemoveGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveGroup
// TODO add your handling code here:
    int rs[] = this.jTableFieldGroup.getSelectedRows();
    int length = rs.length;
    String prompt = (length == 1) ? NbBundle.getMessage(DomainSearchResultDialog.class, "MSG_Confirm_Remove_Row_Prompt")
            : NbBundle.getMessage(DomainSearchResultDialog.class, "MSG_Confirm_Remove_Rows_Prompt");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(DomainSearchResultDialog.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelFieldGroup model = (TableModelFieldGroup) jTableFieldGroup.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object fieldGroup = (Object) model.getValueAt(j, model.iFieldGroupName);
                model.removeRow(j);
            }
            if (jTableFieldGroup.getRowCount() > 0) {
                this.jBtnRemoveGroup.setEnabled(true);
                jTableFieldGroup.setRowSelectionInterval(0, 0);
            }
            this.bModified = true;
        }
    
    
}//GEN-LAST:event_onRemoveGroup

private void onRemoveField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveField
// TODO add your handling code here:
}//GEN-LAST:event_onRemoveField

private void onAddField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddField
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) jTableFieldGroup.getModel();
    TableModelField fieldModel = (TableModelField) jTableField.getModel();
    int selectedRow = jTableFieldGroup.getSelectedRow();
    GroupRow group = fieldGroupModel.getRow(selectedRow);
    FieldGroup fieldGroup = null;
    if (group.getGroupName().length() > 0) {
        fieldGroup = mSearchDetail.getFieldGroup(group.getGroupId());
    } else {
        fieldGroup = mSearchDetail.getFieldGroup(group.getGroupName());
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

}//GEN-LAST:event_onAddField

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
        final static int iFieldGroupName = 0;

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
                        case iFieldGroupName:
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
                GroupRow singleRow = (GroupRow) groupRows.get(row);
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
                case iFieldGroupName:
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

        public ArrayList<GroupRow> getAllRows() {
            return this.groupRows;
        }
        /**
        public MatchRuleRowPerProbType getRow(int index) {
        //MatchRuleRowPerProbType row = (MatchRuleRowPerProbType) matchRuleRows.get(index);
        return row;
        }
         */
        public int findRowByFieldName(String fieldName) {
            for (int i = 0; i < groupRows.size(); i++) {
                if (getValueAt(i, iFieldGroupName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public GroupRow findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < groupRows.size(); i++) {
                if (getValueAt(i, iFieldGroupName).equals(fieldName)) {
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
        private GroupRow mFieldGroup = null;

        TableModelField(GroupRow fieldGroup) {
            mFieldGroup = fieldGroup;
            fieldRows = fieldGroup.getFieldRows();
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
                FieldRow singleRow = fieldRows.get(row);
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


     class NumbericVerifier extends InputVerifier {
         public boolean verify(JComponent input) {
               JTextField tf = (JTextField) input;
               String numStr = tf.getText();
               if (Integer.parseInt(numStr) > 0) {
                   return true;
               }
               return false;
         }
     }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAddField;
    private javax.swing.JButton jBtnAddGroup;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JButton jBtnRemoveField;
    private javax.swing.JButton jBtnRemoveGroup;
    private javax.swing.JLabel jLabelItemPerPage;
    private javax.swing.JLabel jLabelMaxItems;
    private javax.swing.JLabel jLabelSearchResultName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerItemPerPage;
    private javax.swing.JSpinner jSpinnerMaxItems;
    private javax.swing.JTable jTableField;
    private javax.swing.JTable jTableFieldGroup;
    private javax.swing.JTextField jTxtResultName;
    // End of variables declaration//GEN-END:variables

}
