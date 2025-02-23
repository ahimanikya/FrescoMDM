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
import com.sun.mdm.multidomain.parser.MIDMObjectDef;
import java.io.InputStream;
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
import org.openide.filesystems.FileObject;
import org.xml.sax.InputSource;


/**
 *
 * @author  wee
 */
public class TabDomainSearch extends javax.swing.JPanel {

    private EditorMainApp mEditorMainApp = null;
    private DomainForWebManager mDomain = null;
    private JTable mTableSearchResult = null;
    private JTable mTableSearchType = null;
    private ArrayList<RecordDetail> mRecordDetail;
    private  ArrayList<SearchDetail> mSearchResult;
    private ArrayList<SimpleSearchType> mSearchTypes;
    private JComboBox mJComboSearchResult = new JComboBox();
    private JComboBox mJComboRecordDetail = new JComboBox();
    private DomainNode mDomainNode = null;
    private String mDomainName;
    
    /** Creates new form TabDomainSearch */
    public TabDomainSearch(EditorMainApp editorMainApp, DomainForWebManager domain) {
        mEditorMainApp = editorMainApp;
        mDomain = domain;
        initComponents();
        mDomainName = domain.getDomainName();
        getDomain(mDomainName);
        mDomainNode = mEditorMainApp.getDomainNode(mDomainName);

        //ArrayList<DomainNode> domainNodes = mEditorMainApp.getDomainNodes();
        mTableSearchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mTableSearchType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        if (mTableSearchResult != null && mTableSearchResult.getRowCount() == 0) {
            this.jBtnRemoveSearchResult.setEnabled(false);
        }
        
        if (mTableSearchType != null && mTableSearchType.getRowCount() == 0) {
            this.jBtnRemoveSearchPage.setEnabled(false);
        }
        
        mTableSearchType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                     onEditSearchType(null);
                 }
                jBtnRemoveSearchPage.setEnabled(true);
                jBtnEditSearchType.setEnabled(true);
            }
        });
        
        mTableSearchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                     onEditSearchResult(null);
                 }
                jBtnRemoveSearchResult.setEnabled(true);
                jBtnEditSearchResult.setEnabled(true);
            }
        });
        
        
    }
    
    private void getDomain(String domainName) {       
        mRecordDetail = mDomain.getRecordDetailList();
        mSearchResult = mDomain.getSearchDetail();
        TableModelSearchResult mTableSearchResultModel = new TableModelSearchResult(mSearchResult);
        mTableSearchResult = new JTable(mTableSearchResultModel);

        //comboBoxRecDetail.setMaximumRowCount(25);

        jScrollPaneSearchResult.setViewportView(mTableSearchResult);

        //create table for Search Type
        mSearchTypes = mDomain.getSearchType();
        TableModelSearchPage mTableSearchTypeModel = new TableModelSearchPage(mSearchTypes);
        mTableSearchType = new JTable(mTableSearchTypeModel);
        int idx = 0;
        mJComboSearchResult.removeAllItems();
        for (SearchDetail searchRes : mSearchResult) {
            mJComboSearchResult.addItem(searchRes.getDisplayName());
            if (searchRes.getSearchResultID() != mSearchTypes.get(0).getScreenResultID()) {
                idx++;
            }

        }

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

        jPanel1 = new javax.swing.JPanel();
        jScrollPaneSearchPage = new javax.swing.JScrollPane();
        jBtnUp = new javax.swing.JButton();
        jBtnDown = new javax.swing.JButton();
        jBtnRemoveSearchPage = new javax.swing.JButton();
        jBtnEditSearchType = new javax.swing.JButton();
        jBtnAddSearchPage = new javax.swing.JButton();
        jPanelSearchResult = new javax.swing.JPanel();
        jScrollPaneSearchResult = new javax.swing.JScrollPane();
        jBtnAddSearchResult = new javax.swing.JButton();
        jBtnRemoveSearchResult = new javax.swing.JButton();
        jBtnEditSearchResult = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_SEARCH_PAGE_PROPERTIES"))); // NOI18N

        jBtnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/UpArrow.jpg"))); // NOI18N
        jBtnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUp(evt);
            }
        });

        jBtnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/mdm/multidomain/project/resources/DownArrow.JPG"))); // NOI18N
        jBtnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDown(evt);
            }
        });

        jBtnRemoveSearchPage.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_REMOVE")); // NOI18N
        jBtnRemoveSearchPage.setEnabled(false);
        jBtnRemoveSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchPage(evt);
            }
        });

        jBtnEditSearchType.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Edit")); // NOI18N
        jBtnEditSearchType.setEnabled(false);
        jBtnEditSearchType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditSearchType(evt);
            }
        });

        jBtnAddSearchPage.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Add")); // NOI18N
        jBtnAddSearchPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchPage(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(234, 234, 234)
                        .add(jBtnAddSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnEditSearchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 510, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {jBtnAddSearchPage, jBtnEditSearchType, jBtnRemoveSearchPage}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPaneSearchPage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnEditSearchType)
                    .add(jBtnRemoveSearchPage)
                    .add(jBtnAddSearchPage)))
            .add(jPanel1Layout.createSequentialGroup()
                .add(37, 37, 37)
                .add(jBtnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jBtnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        jPanelSearchResult.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_DOMAIN_SEARCH_RESULT_PROPERTIES"))); // NOI18N

        jBtnAddSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Add")); // NOI18N
        jBtnAddSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSearchResult(evt);
            }
        });

        jBtnRemoveSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_REMOVE")); // NOI18N
        jBtnRemoveSearchResult.setEnabled(false);
        jBtnRemoveSearchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveSearchResult(evt);
            }
        });

        jBtnEditSearchResult.setText(org.openide.util.NbBundle.getMessage(TabDomainSearch.class, "LBL_Edit")); // NOI18N
        jBtnEditSearchResult.setEnabled(false);
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
                .add(jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanelSearchResultLayout.createSequentialGroup()
                        .add(jBtnAddSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnEditSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPaneSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 510, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jPanelSearchResultLayout.linkSize(new java.awt.Component[] {jBtnAddSearchResult, jBtnEditSearchResult, jBtnRemoveSearchResult}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanelSearchResultLayout.setVerticalGroup(
            jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelSearchResultLayout.createSequentialGroup()
                .add(jScrollPaneSearchResult, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelSearchResultLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddSearchResult)
                    .add(jBtnRemoveSearchResult)
                    .add(jBtnEditSearchResult)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanelSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelSearchResult, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void onAddSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSearchResult
// TODO add your handling code here:
    
    //DomainForWebManager domain = mDomains.getDomain((String) jCBDomainList.getSelectedItem());
    int searchDetailID = mDomain.generateSearchResultID();

    SearchDetail searchDetail = new SearchDetail(searchDetailID, 1, 1, 1, "", new ArrayList<FieldGroup>());
    DomainSearchResultDialog dlg = new DomainSearchResultDialog(searchDetail, true, mDomainNode);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        TableModelSearchResult searchResultModel = (TableModelSearchResult) this.mTableSearchResult.getModel();
        int row = searchResultModel.getRowCount();
        searchResultModel.addRow(row , searchDetail);
        mTableSearchResult.setRowSelectionInterval(row, row);
        this.jBtnEditSearchResult.setEnabled(true);
        this.jBtnRemoveSearchResult.setEnabled(true);
        mTableSearchResult.setModel(searchResultModel);
        //this.mJComboRecordDetail.setSelectedItem(dlg.getSelectRecordDetail());
        this.jScrollPaneSearchResult.setViewportView(mTableSearchResult);
        this.mJComboSearchResult.addItem(searchDetail.getDisplayName());
        this.enableSave();
        
    }
}//GEN-LAST:event_onAddSearchResult

private void onRemoveSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveSearchResult
    int rs[] = this.mTableSearchResult.getSelectedRows();
    int selectedRow = this.mTableSearchResult.getSelectedRow();
    TableModelSearchResult model = (TableModelSearchResult) mTableSearchResult.getModel();
    SearchDetail searchDetail = model.getRow(selectedRow);
    if (mDomain.isSearchDetailUsed(searchDetail)) {
       String warningMsg = NbBundle.getMessage(TabDomainSearch.class, "MSG_ERROR_DELETING_SEARCH_DETAIL");
        NotifyDescriptor errorNotify = new NotifyDescriptor.Message(
                warningMsg,
                NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(errorNotify);

    } else {
        int length = rs.length;
        String type = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "LBL_SEARCH_RESULT")
                                    : NbBundle.getMessage(TabDomainSearch.class, "LBL_SEARCH_RESULTS");
        String prompt = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Prompt", type)
                                      : NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
        String title = NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Title", type);
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                prompt,
                title,
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
    MIQueryBuilder queryBuilder = mEditorMainApp.getDomainNode(mDomainName).getMiQueryBuilder();
    DomainSearchTypePanel dlg = new DomainSearchTypePanel(searchType, mSearchResult, queryBuilder, true, mDomainNode);
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
    int rs[] = this.mTableSearchType.getSelectedRows();
    int length = rs.length;
    String type = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "LBL_SEARCH_PAGE")
                                : NbBundle.getMessage(TabDomainSearch.class, "LBL_SEARCH_PAGES");
    String prompt = (length == 1) ? NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Prompt", type)
                                  : NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
    String title = NbBundle.getMessage(TabDomainSearch.class, "MSG_Confirm_Remove_Title", type);
    NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 title, 
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
    MIQueryBuilder queryBuilder = mEditorMainApp.getDomainNode(mDomainName).getMiQueryBuilder();
    DomainSearchTypePanel dlg = new DomainSearchTypePanel(searchType, mSearchResult, queryBuilder, false, mDomainNode);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        if (dlg.isRefreshSearchResult()) {
             mJComboSearchResult.setSelectedItem(dlg.getSelectedSearchResult());
             jScrollPaneSearchPage.setViewportView(mTableSearchType);
        }
        searchPageModel.fireTableDataChanged();
        mTableSearchType.setRowSelectionInterval(selectedRow, selectedRow);
        this.enableSave();
    }
}//GEN-LAST:event_onEditSearchType

private void onEditSearchResult(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditSearchResult
// TODO add your handling code here:
    int selectedRow = this.mTableSearchResult.getSelectedRow();
    TableModelSearchResult searchResult = (TableModelSearchResult) mTableSearchResult.getModel();
    SearchDetail searchDetail = searchResult.getRow(selectedRow);
    DomainSearchResultDialog dlg = new DomainSearchResultDialog(searchDetail, false, mDomainNode);
    dlg.setVisible(true);
    if (dlg.isModified()) {
        jScrollPaneSearchResult.setViewportView(mTableSearchResult);
        ((TableModelSearchPage) mTableSearchType.getModel()).fireTableDataChanged();
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
            return false;
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
            return false;
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
            return false;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelSearchResult;
    private javax.swing.JScrollPane jScrollPaneSearchPage;
    private javax.swing.JScrollPane jScrollPaneSearchResult;
    // End of variables declaration//GEN-END:variables
}
