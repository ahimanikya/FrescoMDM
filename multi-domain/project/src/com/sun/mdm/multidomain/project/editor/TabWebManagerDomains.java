/*
 * TabWebManagerDomains.java
 *
 * Created on September 22, 2008, 2:27 PM
 */
package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.DomainForWebManager;
import com.sun.mdm.multidomain.parser.DomainsForWebManager;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.RelationFieldReference;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.parser.SimpleSearchType;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class TabWebManagerDomains extends javax.swing.JPanel {

    private EditorMainApp mEditorMainApp = null;
    private DomainsForWebManager mDomains = null;
    private JTable mTableRecordDetail = null;
    private JTable mTableSearchResult = null;
    private JTable mTableSearchType = null;
    private ArrayList<RecordDetail> mRecordDetail;
    private  ArrayList<SearchDetail> mSearchResult;
    private ArrayList<SimpleSearchType> mSearchTypes;
    private JComboBox mJComboSearchResult = new JComboBox();
    private JComboBox mJComboRecordDetail = new JComboBox();
           

    /** Creates new form TabWebManagerDomains */
    public TabWebManagerDomains(EditorMainApp editorMainApp, DomainsForWebManager domains) {
        mEditorMainApp = editorMainApp;
        mDomains = domains;
        initComponents();
        createDomain(0);
        
    }

    private void createDomain(int domainIdx) {
        if (mDomains.getDomains().size() > 0) {
            DomainForWebManager domain = mDomains.getDomains().get(domainIdx);
            jTxtDomainName.setText(domain.getDomainName());
            
            // create table for Record Detail            
            mRecordDetail = domain.getRecordDetailList();
            TableModelRecordDetail mTableRecordDetailModel = new TableModelRecordDetail(mRecordDetail);
            mTableRecordDetail = new JTable(mTableRecordDetailModel);
            
            jScrollPaneRecDetail.setViewportView(mTableRecordDetail);
            
            //create table for Search Result
            mSearchResult = domain.getSearchDetail();
            TableModelSearchResult mTableSearchResultModel = new TableModelSearchResult(mSearchResult);
            mTableSearchResult = new JTable(mTableSearchResultModel);
            for (RecordDetail recDetail : mRecordDetail) {
                mJComboRecordDetail.addItem(recDetail.getDisplayName());
            }
            mJComboRecordDetail.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    enableSave();
                //System.out.println("Testing");
                }
            });
            TableColumn column = mTableSearchResult.getColumnModel().getColumn(1);
            column.setCellEditor(new DefaultCellEditor(mJComboRecordDetail));


            //comboBoxRecDetail.setMaximumRowCount(25);

            jScrollPaneSearchResult.setViewportView(mTableSearchResult);
            
            //create table for Search Type
            mSearchTypes = domain.getSearchType();
            TableModelSearchPage mTableSearchTypeModel = new TableModelSearchPage(mSearchTypes);
            mTableSearchType = new JTable(mTableSearchTypeModel);
            int idx = 0;
            for (SearchDetail searchRes : mSearchResult) {
                mJComboSearchResult.addItem(searchRes.getDisplayName());
                if (searchRes.getSearchResultID() != mSearchTypes.get(0).getScreenResultID()) {
                    idx++;
                }
                
            }
            
//            comboBoxSearchRes.getS
            mJComboSearchResult.setSelectedItem(idx);
            mJComboSearchResult.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    //mTableSearchType.setValueAt(ui, WIDTH, WIDTH)
                    int selectSearchTypeRow = mTableSearchType.getSelectedRow();
                    TableModelSearchPage searchTableModel = (TableModelSearchPage) mTableSearchType.getModel();
                    SimpleSearchType selectedSearchType = searchTableModel.getRow(selectSearchTypeRow);
                    mJComboSearchResult.getSelectedIndex();
                    String displayName = (String) mJComboSearchResult.getSelectedItem();
                    for ( SearchDetail det : mSearchResult) {
                        if (det.getDisplayName().equals(displayName)) {
                            selectedSearchType.setScreenResultID(det.getSearchResultID());
                            break;
                        }
                    }
                    enableSave();
                }
            });
            
            TableColumn columnSearchRes = mTableSearchType.getColumnModel().getColumn(1);
            columnSearchRes.setCellEditor(new DefaultCellEditor(mJComboSearchResult));
            jScrollPaneSearchPage.setViewportView(mTableSearchType);

        }
    }
    
    
    private void enableSave() {
        //mEditorMainApp.save(true);
        mEditorMainApp.enableSaveAction(true);
        
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
        jTxtDomainName = new javax.swing.JTextField();
        jPanelRecordDetail = new javax.swing.JPanel();
        jScrollPaneRecDetail = new javax.swing.JScrollPane();
        jBtnAddRecDetail = new javax.swing.JButton();
        jBtnRemoveRecDetail = new javax.swing.JButton();
        jPanelSearchResult = new javax.swing.JPanel();
        jScrollPaneSearchResult = new javax.swing.JScrollPane();
        jBtnAddSearchResult = new javax.swing.JButton();
        jBtnRemoveSearchResult = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneSearchPage = new javax.swing.JScrollPane();
        jBtnAddSearchPage = new javax.swing.JButton();
        jBtnRemoveSearchPage = new javax.swing.JButton();
        jBtnEditSearchType = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "MSG_Name")); // NOI18N

        jTxtDomainName.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jTxtDomainName.text")); // NOI18N

        jPanelRecordDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jPanelRecordDetail.border.title"))); // NOI18N

        jBtnAddRecDetail.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "LBL_Add")); // NOI18N
        jBtnAddRecDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddRecDetail(evt);
            }
        });

        jBtnRemoveRecDetail.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "LBL_Remove")); // NOI18N
        jBtnRemoveRecDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveRecDetail(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelRecordDetailLayout = new org.jdesktop.layout.GroupLayout(jPanelRecordDetail);
        jPanelRecordDetail.setLayout(jPanelRecordDetailLayout);
        jPanelRecordDetailLayout.setHorizontalGroup(
            jPanelRecordDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelRecordDetailLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelRecordDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelRecordDetailLayout.createSequentialGroup()
                        .add(jBtnAddRecDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveRecDetail))
                    .add(jScrollPaneRecDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRecordDetailLayout.setVerticalGroup(
            jPanelRecordDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelRecordDetailLayout.createSequentialGroup()
                .add(jScrollPaneRecDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanelRecordDetailLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnRemoveRecDetail)
                    .add(jBtnAddRecDetail))
                .addContainerGap())
        );

        jPanelSearchResult.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jPanelSearchResult.border.title"))); // NOI18N

        jBtnAddSearchResult.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jBtnAddSearchResult.text")); // NOI18N
        jBtnAddSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchResult(evt);
            }
        });

        jBtnRemoveSearchResult.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jBtnRemoveSearchResult.text")); // NOI18N
        jBtnRemoveSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchResult(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelSearchResultLayout = new org.jdesktop.layout.GroupLayout(jPanelSearchResult);
        jPanelSearchResult.setLayout(jPanelSearchResultLayout);
        jPanelSearchResultLayout.setHorizontalGroup(
            jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSearchResultLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPaneSearchResult, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelSearchResultLayout.createSequentialGroup()
                        .add(jBtnAddSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveSearchResult)))
                .addContainerGap())
        );
        jPanelSearchResultLayout.setVerticalGroup(
            jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSearchResultLayout.createSequentialGroup()
                .add(jScrollPaneSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnRemoveSearchResult)
                    .add(jBtnAddSearchResult)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jPanel1.border.title"))); // NOI18N

        jBtnAddSearchPage.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jBtnAddSearchPage.text")); // NOI18N
        jBtnAddSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchPage(evt);
            }
        });

        jBtnRemoveSearchPage.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "TabWebManagerDomains.jBtnRemoveSearchPage.text")); // NOI18N
        jBtnRemoveSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchPage(evt);
            }
        });

        jBtnEditSearchType.setText(org.openide.util.NbBundle.getMessage(TabWebManagerDomains.class, "LBL_Edit")); // NOI18N
        jBtnEditSearchType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditSearchType(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jBtnAddSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnEditSearchType)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jBtnRemoveSearchPage)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnRemoveSearchPage)
                    .add(jBtnEditSearchType)
                    .add(jBtnAddSearchPage)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelRecordDetail, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTxtDomainName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanelSearchResult, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTxtDomainName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelRecordDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void onAddRecDetail(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddRecDetail
// TODO add your handling code here:
}//GEN-LAST:event_onAddRecDetail

private void onRemoveRecDetail(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveRecDetail
// TODO add your handling code here:
}//GEN-LAST:event_onRemoveRecDetail

private void onAddSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSearchResult
// TODO add your handling code here:
}//GEN-LAST:event_onAddSearchResult

private void onRemoveSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSearchResult
// TODO add your handling code here:
}//GEN-LAST:event_onRemoveSearchResult

private void onAddSearchPage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSearchPage
// TODO add your handling code here:
}//GEN-LAST:event_onAddSearchPage

private void onRemoveSearchPage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSearchPage
// TODO add your handling code here:
}//GEN-LAST:event_onRemoveSearchPage

private void onEditSearchType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditSearchType
// TODO add your handling code here:
    int selectedRow = mTableSearchType.getSelectedRow();
    TableModelSearchPage searchPageModel = (TableModelSearchPage) mTableSearchType.getModel();
    SimpleSearchType searchType = searchPageModel.getRow(selectedRow);
    DomainSearchTypePanel dlg = new DomainSearchTypePanel(searchType, mSearchResult);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        if (dlg.isRefreshSearchResult()) {
             mJComboSearchResult.setSelectedItem(dlg.getSelectedSearchResult());
             jScrollPaneSearchPage.setViewportView(mTableSearchType);

            //mJComboSearchResult.s
        }
        this.enableSave();
    }
}//GEN-LAST:event_onEditSearchType

    class TableModelRecordDetail extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabWebManagerDomains.class, "LBL_DOMAIN_RECORD_DETAIL_FIELD"),};
        ArrayList fieldRows;
        final static int iColRecordDetailName = 0;

        TableModelRecordDetail(ArrayList rows) {
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
                RecordDetail singleRow = (RecordDetail) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColRecordDetailName:
                            return singleRow.getDisplayName();
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public RecordDetail getRow(int row) {
            if (fieldRows != null) {
                RecordDetail singleRow = (RecordDetail) fieldRows.get(row);
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
                case iColRecordDetailName:
                    ((RecordDetail) fieldRows.get(row)).setDisplayName((String) value);
                    //((RecordDetail) fieldRows.get(row)).setRecordDetailId(Integer.parseInt((String) value));
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, RecordDetail row) {
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
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public RecordDetail findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return (RecordDetail) fieldRows.get(i);
                }
            }
            return null;
        }
    }

    class TableModelSearchResult extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabWebManagerDomains.class, "LBL_DOMAIN_SEARCH_RESULT_FIELD"),
            NbBundle.getMessage(TabWebManagerDomains.class, "LBL_DOMAIN_RECORD_DETAIL_FIELD"),
        };
        ArrayList fieldRows;
        final static int iColRecordDetailName = 1;
        final static int iColSearchResultName = 0;

        TableModelSearchResult(ArrayList rows) {
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
                SearchDetail singleRow = (SearchDetail) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColSearchResultName:
                            return singleRow.getDisplayName();
                        case iColRecordDetailName:
                            //SearchDetail singleRow = (SearchDetail) fieldRows.get(row);
                            for (RecordDetail recDet : mRecordDetail) {
                                if (recDet.getRecordDetailId() == singleRow.getRecordDetailID())
                                    return recDet.getDisplayName();
                            }
                            //return (mDetailRows.singleRow.getRecordDetailID();
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public SearchDetail getRow(int row) {
            if (fieldRows != null) {
                SearchDetail singleRow = (SearchDetail) fieldRows.get(row);
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
                case iColSearchResultName:
                    ((SearchDetail) fieldRows.get(row)).setSearchResultID(Integer.parseInt((String) value));
                    break;
                case iColRecordDetailName:
                    for (RecordDetail recDet : mRecordDetail) {
                        if (recDet.getDisplayName().equals(value)) {
                            ((SearchDetail) fieldRows.get(row)).setRecordDetailID(recDet.getRecordDetailId());
                            break;
                        }
                    }
                    
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, RelationFieldReference row) {
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
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public SearchDetail findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColRecordDetailName).equals(fieldName)) {
                    return (SearchDetail) fieldRows.get(i);
                }
            }
            return null;
        }
    }

    class TableModelSearchPage extends AbstractTableModel implements TableModelListener {

        private String columnNames[] = {NbBundle.getMessage(TabWebManagerDomains.class, "LBL_DOMAIN_SEARCH_PAGE_FIELD"),
            NbBundle.getMessage(TabWebManagerDomains.class, "LBL_DOMAIN_SEARCH_RESULT_FIELD"),
        };
        ArrayList fieldRows;
        final static int iColSearchPageName = 0;
        final static int iColSearchResultName = 1;

        TableModelSearchPage(ArrayList rows) {
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
                SimpleSearchType singleRow = (SimpleSearchType) fieldRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColSearchPageName:
                            return singleRow.getScreenTitle();
                        case iColSearchResultName:
                            for (SearchDetail searchDet : mSearchResult) {
                                if (searchDet.getSearchResultID() == singleRow.getScreenResultID())
                                    return searchDet.getDisplayName();
                            }
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public SimpleSearchType getRow(int row) {
            if (fieldRows != null) {
                SimpleSearchType singleRow = (SimpleSearchType) fieldRows.get(row);
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
                case iColSearchPageName:
                    ((SimpleSearchType) fieldRows.get(row)).setScreenTitle((String) value);
                    break;                    
                case iColSearchResultName :
                    for (SearchDetail searchDet : mSearchResult) {
                        if (searchDet.getDisplayName().equals(value)) {
                            ((SimpleSearchType) fieldRows.get(row)).setScreenResultID(searchDet.getSearchResultID());
                        }
                        break;
                    }
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            fieldRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, SimpleSearchType row) {
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
                if (getValueAt(i, iColSearchPageName).equals(fieldName)) {
                    return i;
                }
            }
            return -1;
        }

        public SimpleSearchType findRelTypeByFieldName(String fieldName) {
            for (int i = 0; i < fieldRows.size(); i++) {
                if (getValueAt(i, iColSearchPageName).equals(fieldName)) {
                    return (SimpleSearchType) fieldRows.get(i);
                }
            }
            return null;
        }
       
        public void tableChanged(TableModelEvent evt) {
             if (evt.getType() == TableModelEvent.UPDATE) {
                 int column = evt.getColumn();
                 int row = evt.getFirstRow();
                 System.out.println("row: " + row + " column: " + column);
                 enableSave();
             }
         }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAddRecDetail;
    private javax.swing.JButton jBtnAddSearchPage;
    private javax.swing.JButton jBtnAddSearchResult;
    private javax.swing.JButton jBtnEditSearchType;
    private javax.swing.JButton jBtnRemoveRecDetail;
    private javax.swing.JButton jBtnRemoveSearchPage;
    private javax.swing.JButton jBtnRemoveSearchResult;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelRecordDetail;
    private javax.swing.JPanel jPanelSearchResult;
    private javax.swing.JScrollPane jScrollPaneRecDetail;
    private javax.swing.JScrollPane jScrollPaneSearchPage;
    private javax.swing.JScrollPane jScrollPaneSearchResult;
    private javax.swing.JTextField jTxtDomainName;
    // End of variables declaration//GEN-END:variables
}
