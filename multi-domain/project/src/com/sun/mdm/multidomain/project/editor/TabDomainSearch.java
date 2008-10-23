/*
 * TabDomainSearch.java
 *
 * Created on September 22, 2008, 2:27 PM
 */
package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.DomainForWebManager;
import com.sun.mdm.multidomain.parser.DomainsForWebManager;
import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.MIQueryBuilder;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.parser.SimpleSearchType;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author  wee
 */
public class TabDomainSearch extends javax.swing.JPanel {

    private EditorMainApp mEditorMainApp = null;
    private DomainsForWebManager mDomains = null;
    private JTable mTableSearchResult = null;
    private JTable mTableSearchType = null;
    private ArrayList<RecordDetail> mRecordDetail;
    private  ArrayList<SearchDetail> mSearchResult;
    private ArrayList<SimpleSearchType> mSearchTypes;
    private JComboBox mJComboSearchResult = new JComboBox();
    private JComboBox mJComboRecordDetail = new JComboBox();
    
           

    /** Creates new form TabDomainSearch */
    public TabDomainSearch(EditorMainApp editorMainApp, DomainsForWebManager domains) {
        mEditorMainApp = editorMainApp;
        mDomains = domains;
        initComponents();
        //this.g
        ArrayList<DomainNode> domainNodes = mEditorMainApp.getDomainNodes();
        if (domainNodes.size() > 0) {
            for (DomainNode node : domainNodes) {
                this.jCBDomainList.addItem(node.getName());
            }

            this.jCBDomainList.setSelectedIndex(0);
            getDomain((String) this.jCBDomainList.getSelectedItem());
            mTableSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            mTableSearchType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        this.jCBDomainList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                String selectDomain = (String) jCBDomainList.getSelectedItem();
                getDomain(selectDomain);
            }
        });
        
    }
    
    private void getDomain(String domainName) {
        
        DomainForWebManager domain = mDomains.getDomain(domainName);
        if (domain == null) {
            domain = new DomainForWebManager(domainName);
            mDomains.addDomain(domain);
        }
        
        mRecordDetail = domain.getRecordDetailList();
        TableModelRecordDetail mTableRecordDetailModel = new TableModelRecordDetail(mRecordDetail);
        //create table for Search Result
        mSearchResult = domain.getSearchDetail();
        TableModelSearchResult mTableSearchResultModel = new TableModelSearchResult(mSearchResult);
        mTableSearchResult = new JTable(mTableSearchResultModel);
        mJComboRecordDetail.removeAllItems();;
        for (RecordDetail recDetail : mRecordDetail) {
            mJComboRecordDetail.addItem(recDetail.getDisplayName());
        }
        mJComboRecordDetail.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enableSave();
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
        mJComboSearchResult.removeAllItems();;
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
                enableSave();
            }
        });

        TableColumn columnSearchRes = mTableSearchType.getColumnModel().getColumn(1);
        columnSearchRes.setCellEditor(new DefaultCellEditor(mJComboSearchResult));
        jScrollPaneSearchPage.setViewportView(mTableSearchType);
        
        
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPaneSearchPage = new javax.swing.JScrollPane();
        jBtnAddSearchPage = new javax.swing.JButton();
        jBtnRemoveSearchPage = new javax.swing.JButton();
        jBtnEditSearchType = new javax.swing.JButton();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();
        jCBDomainList = new javax.swing.JComboBox();
        jPanelSearchResult = new javax.swing.JPanel();
        jScrollPaneSearchResult = new javax.swing.JScrollPane();
        jBtnAddSearchResult = new javax.swing.JButton();
        jBtnRemoveSearchResult = new javax.swing.JButton();
        jBtnEditSearchResult = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "MSG_Name")); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jPanel1.border.title"))); // NOI18N

        jBtnAddSearchPage.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnAddSearchPage.text")); // NOI18N
        jBtnAddSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchPage(evt);
            }
        });

        jBtnRemoveSearchPage.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnRemoveSearchPage.text")); // NOI18N
        jBtnRemoveSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchPage(evt);
            }
        });

        jBtnEditSearchType.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Edit")); // NOI18N
        jBtnEditSearchType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditSearchType(evt);
            }
        });

        jBtnUp.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnUp.text")); // NOI18N
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUp(evt);
            }
        });

        jBtnDown.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnDown.text")); // NOI18N
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDown(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jBtnAddSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnEditSearchType)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveSearchPage))
                    .add(jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnUp))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddSearchPage)
                    .add(jBtnEditSearchType)
                    .add(jBtnRemoveSearchPage)))
            .add(jPanel1Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jBtnUp)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jBtnDown))
        );

        jPanelSearchResult.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jPanelSearchResult.border.title"))); // NOI18N

        jBtnAddSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnAddSearchResult.text")); // NOI18N
        jBtnAddSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchResult(evt);
            }
        });

        jBtnRemoveSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "TabDomainSearch.jBtnRemoveSearchResult.text")); // NOI18N
        jBtnRemoveSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchResult(evt);
            }
        });

        jBtnEditSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Edit")); // NOI18N
        jBtnEditSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditSearchResult(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanelSearchResultLayout = new org.jdesktop.layout.GroupLayout(jPanelSearchResult);
        jPanelSearchResult.setLayout(jPanelSearchResultLayout);
        jPanelSearchResultLayout.setHorizontalGroup(
            jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelSearchResultLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPaneSearchResult, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelSearchResultLayout.createSequentialGroup()
                        .add(jBtnAddSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnEditSearchResult)
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
                    .add(jBtnAddSearchResult)
                    .add(jBtnEditSearchResult)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCBDomainList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 174, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(jPanelSearchResult, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(30, 30, 30))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jCBDomainList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanelSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void onAddSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSearchResult
// TODO add your handling code here:
    
    DomainForWebManager domain = mDomains.getDomain((String) jCBDomainList.getSelectedItem());
    int searchDetailID = domain.generateSearchResultID();

    SearchDetail searchDetail = new SearchDetail(searchDetailID, 1, 1, 1, "", new ArrayList<FieldGroup>());
    DomainSearchResultDialog dlg = new DomainSearchResultDialog(searchDetail, mRecordDetail, true);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        TableModelSearchResult searchResultModel = (TableModelSearchResult) this.mTableSearchResult.getModel();
        int row = searchResultModel.getRowCount();
        searchResultModel.addRow(row , searchDetail);
        mTableSearchResult.setRowSelectionInterval(row, row);
        this.jBtnEditSearchResult.setEnabled(true);
        this.jBtnRemoveSearchResult.setEnabled(false);
        mTableSearchResult.setModel(searchResultModel);
        this.mJComboRecordDetail.setSelectedItem(dlg.getSelectRecordDetail());
        this.jScrollPaneSearchResult.setViewportView(mTableSearchResult);
        this.mJComboSearchResult.addItem(searchDetail.getDisplayName());
        this.enableSave();
        
    }
}//GEN-LAST:event_onAddSearchResult

private void onRemoveSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSearchResult
// TODO add your handling code here:
    int rs[] = this.mTableSearchResult.getSelectedRows();
    int selectedRow = this.mTableSearchResult.getSelectedRow();
    TableModelSearchResult model = (TableModelSearchResult) mTableSearchResult.getModel();
    SearchDetail searchDetail = model.getRow(selectedRow);
    if (mDomains.getDomain((String) jCBDomainList.getSelectedItem()).isSearchDetailUsed(searchDetail)) {
       String warningMsg = NbBundle.getMessage(TabDomainSearch.class, "MSG_ERROR_DELETING_SEARCH_DETAIL");
        NotifyDescriptor errorNotify = new NotifyDescriptor.Message(
                warningMsg,
                NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(errorNotify);

    } else {
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Row_Prompt")
                : NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Rows_Prompt");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                prompt,
                NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Row_Title"),
                NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            //TableModelSearchResult model = (TableModelSearchResult) mTableSearchResult.getModel();
            for (int i = length - 1; i >= 0 && i < length; i--) {
                int j = rs[i];
                Object searchResult = (Object) model.getValueAt(j, model.iColSearchResultName);
                mJComboSearchResult.removeItem(searchResult);
                model.removeRow(j);
            }
            if (mTableSearchResult.getRowCount() > 0) {
                this.jBtnRemoveSearchResult.setEnabled(true);
                mTableSearchResult.setRowSelectionInterval(0, 0);
            } else {
                this.jBtnRemoveSearchResult.setEnabled(false);
                this.jBtnEditSearchResult.setEnabled(false);
            }
            this.jScrollPaneSearchResult.setViewportView(mTableSearchResult);
            this.enableSave();
        }
    }
}//GEN-LAST:event_onRemoveSearchResult

private void onAddSearchPage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSearchPage
// TODO add your handling code here:
    SimpleSearchType searchType = new SimpleSearchType("", -1, "", mTableSearchType.getRowCount(), new ArrayList<FieldGroup>());
    MIQueryBuilder queryBuilder = mEditorMainApp.getDomainNode((String) this.jCBDomainList.getSelectedItem()).getMiQueryBuilder();
    DomainSearchTypePanel dlg = new DomainSearchTypePanel(searchType, mSearchResult, queryBuilder, true);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        TableModelSearchPage searchModel = (TableModelSearchPage) this.mTableSearchType.getModel();
        int row = searchModel.getRowCount();
        searchModel.addRow(row, searchType);        
        mTableSearchType.setModel(searchModel);
        mTableSearchType.setRowSelectionInterval(row, row);
        mJComboSearchResult.setSelectedItem(dlg.getSelectedSearchResult());
        this.jBtnEditSearchType.setEnabled(true);
        this.jBtnRemoveSearchPage.setEnabled(true);
        jScrollPaneSearchPage.setViewportView(mTableSearchType);
        this.enableSave();
    }

}//GEN-LAST:event_onAddSearchPage

private void onRemoveSearchPage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSearchPage
// TODO add your handling code here:
    
    int rs[] = this.mTableSearchType.getSelectedRows();
    int length = rs.length;
    String prompt = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Row_Prompt")
            : NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Rows_Prompt");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelSearchPage model = (TableModelSearchPage) mTableSearchType.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object searchPage = (Object) model.getValueAt(j, model.iColSearchPageName);
                model.removeRow(j);
            }
            if (mTableSearchType.getRowCount() > 0) {
                this.jBtnRemoveSearchPage.setEnabled(true);
                mTableSearchType.setRowSelectionInterval(0, 0);
            } else {
                this.jBtnRemoveSearchPage.setEnabled(false);
                this.jBtnRemoveSearchPage.setEnabled(false);
            }
            this.enableSave();
        }

}//GEN-LAST:event_onRemoveSearchPage

private void onEditSearchType(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditSearchType
// TODO add your handling code here:
    int selectedRow = mTableSearchType.getSelectedRow();
    TableModelSearchPage searchPageModel = (TableModelSearchPage) mTableSearchType.getModel();
    SimpleSearchType searchType = searchPageModel.getRow(selectedRow);
    MIQueryBuilder queryBuilder = mEditorMainApp.getDomainNode((String) this.jCBDomainList.getSelectedItem()).getMiQueryBuilder();
    DomainSearchTypePanel dlg = new DomainSearchTypePanel(searchType, mSearchResult, queryBuilder, false);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        if (dlg.isRefreshSearchResult()) {
             mJComboSearchResult.setSelectedItem(dlg.getSelectedSearchResult());
             jScrollPaneSearchPage.setViewportView(mTableSearchType);
        }
        this.enableSave();
    }
}//GEN-LAST:event_onEditSearchType

private void onEditSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditSearchResult
// TODO add your handling code here:
    int selectedRow = this.mTableSearchResult.getSelectedRow();
    TableModelSearchResult searchResult = (TableModelSearchResult) mTableSearchResult.getModel();
    SearchDetail searchDetail = searchResult.getRow(selectedRow);
    DomainSearchResultDialog dlg = new DomainSearchResultDialog(searchDetail, mRecordDetail, false);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        this.mJComboRecordDetail.setSelectedItem(dlg.getSelectRecordDetail());
        this.jScrollPaneSearchResult.setViewportView(mTableSearchResult);
        this.enableSave();
    }
}//GEN-LAST:event_onEditSearchResult

private void onUp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUp
// TODO add your handling code here:
    TableModelSearchPage model = (TableModelSearchPage) mTableSearchType.getModel();
    int iSelectedRow = mTableSearchType.getSelectedRow();
    SimpleSearchType movedUpRow = model.getRow(iSelectedRow);
    int screenOrderUpRow = movedUpRow.getScreenOrder();
    SimpleSearchType movedDownRow = model.getRow(iSelectedRow - 1);
    int screenOrderDownRow = movedDownRow.getScreenOrder();
    movedDownRow.setScreenOrder(screenOrderUpRow);
    movedUpRow.setScreenOrder(screenOrderDownRow);
    model.removeRow(iSelectedRow);
    //if (iSelectedRow ==
    iSelectedRow--;
    model.addRow(iSelectedRow, movedUpRow);
    mTableSearchType.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    this.enableSave(); 

}//GEN-LAST:event_onUp

private void onDown(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDown
// TODO add your handling code here:
    TableModelSearchPage model = (TableModelSearchPage) mTableSearchType.getModel();
    int iSelectedRow = mTableSearchType.getSelectedRow();
    SimpleSearchType movedRow = model.getRow(iSelectedRow);
    int screenOrderMovedRow = movedRow.getScreenOrder();
    model.removeRow(iSelectedRow);
    SimpleSearchType previousRow = model.getRow(iSelectedRow);
    int screenOrderPrevRow = previousRow.getScreenOrder();
    previousRow.setScreenOrder(screenOrderMovedRow);
    iSelectedRow++;
    movedRow.setScreenOrder(screenOrderPrevRow);
    model.addRow(iSelectedRow, movedRow);
    mTableSearchType.setRowSelectionInterval(iSelectedRow, iSelectedRow);
    this.enableSave();
}//GEN-LAST:event_onDown

    class TableModelRecordDetail extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_RECORD_DETAIL_FIELD"),};
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

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_SEARCH_RESULT_FIELD"),
            NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_RECORD_DETAIL_FIELD"),
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

        public void addRow(int index, SearchDetail row) {
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

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_SEARCH_PAGE_FIELD"),
            NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_SEARCH_RESULT_FIELD"),
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
    private javax.swing.JButton jBtnAddSearchPage;
    private javax.swing.JButton jBtnAddSearchResult;
    private javax.swing.JButton jBtnDown;
    private javax.swing.JButton jBtnEditSearchResult;
    private javax.swing.JButton jBtnEditSearchType;
    private javax.swing.JButton jBtnRemoveSearchPage;
    private javax.swing.JButton jBtnRemoveSearchResult;
    private javax.swing.JButton jBtnUp;
    private javax.swing.JComboBox jCBDomainList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelSearchResult;
    private javax.swing.JScrollPane jScrollPaneSearchPage;
    private javax.swing.JScrollPane jScrollPaneSearchResult;
    // End of variables declaration//GEN-END:variables
}
