/*
 * DomainSearchTypePanel.java
 *
 * Created on October 8, 2008, 5:14 PM
 */

package com.sun.mdm.multidomain.project.editor;

import com.sun.mdm.multidomain.parser.FieldGroup;
import com.sun.mdm.multidomain.parser.MIQueryBuilder;
import com.sun.mdm.multidomain.parser.RecordDetail;
import com.sun.mdm.multidomain.parser.SearchDetail;
import com.sun.mdm.multidomain.parser.SearchOptions;
import com.sun.mdm.multidomain.parser.SearchOptions.Parameter;
import com.sun.mdm.multidomain.parser.SimpleSearchType;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
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
public class DomainSearchTypePanel extends javax.swing.JDialog {
    

    private SimpleSearchType mSearchType = null;
    
    private ArrayList<SearchDetail> mSearchResultList = null;
    
    private boolean bModified = false;
    
    private boolean bRefreshSearchResult = false;
    
    private boolean bRefreshQueryBuilder = false;
    
    private boolean bModifiedInstruction = false;
    
    private MIQueryBuilder mQueryBuilder = null;
    
    private DomainNode mDomainNode = null;
    
    
    /** Creates new form DomainSearchTypePanel */
    public DomainSearchTypePanel(SimpleSearchType searchType, ArrayList<SearchDetail> searchResultList, MIQueryBuilder queryBuilder, boolean isNewSearch,
            DomainNode domainNode) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        mSearchType = searchType;
        mSearchResultList = searchResultList;
        mQueryBuilder = queryBuilder;
        mDomainNode = domainNode;
        initComponents();
        if (isNewSearch) {
            loadNewSearchType();
        } else {
            loadSearchType();
        }
        jTableFieldGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = jTableFieldGroup.getSelectedRow();
                GroupRow fieldGroup =  ((TableModelFieldGroup) jTableFieldGroup.getModel()).getRow(selectedRow);
                TableModelField fieldModel = new TableModelField(fieldGroup);
                jTableFields.setModel(fieldModel);
                jBtnAddField.setEnabled(true);
                jBtnRemoveFieldGroup.setEnabled(true);

            }
        });
        
        this.jTableFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtnRemoveField.setEnabled(true);

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
                    jTableFields.setModel(fieldModel);
                }

            }
        });
        jCBSearchResultList.addItemListener(new java.awt.event.ItemListener() {
            
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bRefreshSearchResult = true;

            }
        });
        
        this.jCBQueryBuilder.addItemListener(new java.awt.event.ItemListener() {           
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bRefreshQueryBuilder = true;
           }
        });
        
        
        jTableQueryParameter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtnRemoveParameter.setEnabled(true);

            }
        });        
        
        jTxtScreenTitle.requestFocus();
        
    }

    

    private void loadNewSearchType() {

         for (SearchDetail detail : mSearchResultList) {
            jCBSearchResultList.addItem(detail.getDisplayName());
        }
        jCBSearchResultList.setSelectedIndex(0);
        boolean foundQuery = false;
        for (Object builderName : mQueryBuilder.getQueryBuilders()) {
            this.jCBQueryBuilder.addItem(builderName);
        }

        this.mSearchType.setSearchOption(new SearchOptions());
        this.jCBQueryBuilder.setSelectedItem(0);
        this.jCheckBoxWeighted.setSelected(true);
        TableModelFieldGroup groupModel = new TableModelFieldGroup(new ArrayList<GroupRow>());
        this.jTableFieldGroup.setModel(groupModel);
        /**
        TableModelField fieldModel = new TableModelField(new GroupRow());
        this.jTableFields.setModel(fieldModel);
         */ 
        TableModelParameter paramModel = new TableModelParameter(new ArrayList<ParameterRow>());
        this.jTableQueryParameter.setModel(paramModel);
        bRefreshSearchResult = true;
        jBtnAddField.setEnabled(false);
        

    }

    private void loadSearchType() {
        jTxtScreenTitle.setText(mSearchType.getScreenTitle());
        jTxtInstruction.setText(mSearchType.getInstruction());
        int setIdx = 0;
        boolean foundIdx = false;
        for (SearchDetail detail : mSearchResultList) {
            jCBSearchResultList.addItem(detail.getDisplayName());
            if (!foundIdx) {
                if ( mSearchType.getScreenResultID() != detail.getSearchResultID()) {
                    setIdx++;
                } else {
                    foundIdx = true;
                }
            }

        }
        
        
        jCBSearchResultList.setSelectedIndex(setIdx);

        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        for (FieldGroup fieldGroup : mSearchType.getFieldGroups()) {
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
        
        //jTableFieldGroup.setRowSelectionInterval(0, 0);
        TableModelField fieldModel = new TableModelField(groups.get(0));
        this.jTableFields.setModel(fieldModel);
 
         boolean foundQuery = false;
         for (Object builderName : mQueryBuilder.getQueryBuilders()) {
            this.jCBQueryBuilder.addItem(builderName);
        }
         
        this.jCBQueryBuilder.setSelectedItem(mSearchType.getSearchOption().getQueryBulder());
        
        this.jCheckBoxWeighted.setSelected(mSearchType.getSearchOption().getWeighted());
       
        ArrayList<ParameterRow> paramRows = new ArrayList<ParameterRow>();
        for (Parameter param : mSearchType.getSearchOption().getParameterList()) {
            ParameterRow paramRow = new ParameterRow();
            paramRow.setParamName(param.getName());
            paramRow.setParamValue(param.getValue());
            paramRows.add(paramRow);
        }
        TableModelParameter paramModel = new TableModelParameter(paramRows);
        this.jTableQueryParameter.setModel(paramModel);
        
    }
    


    public boolean isModified() {
        return this.bModified;
    }
    
    public Object getSelectedSearchResult() {
        return this.jCBSearchResultList.getSelectedItem();
    }
    
    public boolean isRefreshSearchResult() {
        return this.bRefreshSearchResult;
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
        jLabel2 = new javax.swing.JLabel();
        jCBSearchResultList = new javax.swing.JComboBox();
        jLabelInstruction = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTxtInstruction = new javax.swing.JTextArea();
        jBtnOK = new javax.swing.JButton();
        jBtnCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableFieldGroup = new javax.swing.JTable();
        jBtnAddFieldGroup = new javax.swing.JButton();
        jBtnRemoveFieldGroup = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPaneFields = new javax.swing.JScrollPane();
        jTableFields = new javax.swing.JTable();
        jBtnRemoveField = new javax.swing.JButton();
        jBtnAddField = new javax.swing.JButton();
        jTxtScreenTitle = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jCBQueryBuilder = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jCheckBoxWeighted = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableQueryParameter = new javax.swing.JTable();
        jBtnAddParameter = new javax.swing.JButton();
        jBtnRemoveParameter = new javax.swing.JButton();

        setTitle(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_DOMAIN_SEARCH_PAGE_PROPERTIES")); // NOI18N
        setResizable(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_SEARCH_SCREEN_TITLE")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_DOMAIN_SEARCH_RESULT_FIELD_Colon")); // NOI18N

        jLabelInstruction.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_DOMAIN_SEARCH_TYPE_INSTRUCTION")); // NOI18N

        jTxtInstruction.setColumns(20);
        jTxtInstruction.setRows(5);
        jScrollPane1.setViewportView(jTxtInstruction);

        jBtnOK.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_OK")); // NOI18N
        jBtnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOKBtn(evt);
            }
        });

        jBtnCancel.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Cancel")); // NOI18N
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancelBtn(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELD_GROUPS"))); // NOI18N

        jTableFieldGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Field Group"
            }
        ));
        jScrollPane2.setViewportView(jTableFieldGroup);
        jTableFieldGroup.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELD_GROUP")); // NOI18N

        jBtnAddFieldGroup.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Add")); // NOI18N
        jBtnAddFieldGroup.setMaximumSize(new java.awt.Dimension(71, 23));
        jBtnAddFieldGroup.setPreferredSize(new java.awt.Dimension(71, 23));
        jBtnAddFieldGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddFieldGroup(evt);
            }
        });

        jBtnRemoveFieldGroup.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Remove")); // NOI18N
        jBtnRemoveFieldGroup.setEnabled(false);
        jBtnRemoveFieldGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveFieldGroup(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jBtnAddFieldGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveFieldGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddFieldGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnRemoveFieldGroup)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELDS"))); // NOI18N

        jTableFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Fields"
            }
        ));
        jScrollPaneFields.setViewportView(jTableFields);
        jTableFields.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELDS")); // NOI18N

        jBtnRemoveField.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Remove")); // NOI18N
        jBtnRemoveField.setEnabled(false);
        jBtnRemoveField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveField(evt);
            }
        });

        jBtnAddField.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Add")); // NOI18N
        jBtnAddField.setEnabled(false);
        jBtnAddField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBtnAddField(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPaneFields, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jBtnAddField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPaneFields, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnAddField)
                    .add(jBtnRemoveField)))
        );

        jTxtScreenTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTxtScreenTitleActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_DOMAIN_SEARCH_SCREEN_OPTIONS"))); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_QUERY_BUILDER")); // NOI18N

        jCheckBoxWeighted.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_WEIGHTED")); // NOI18N

        jTableQueryParameter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Name", "Value"
            }
        ));
        jScrollPane3.setViewportView(jTableQueryParameter);
        jTableQueryParameter.getColumnModel().getColumn(0).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_PARAMETER_NAME")); // NOI18N
        jTableQueryParameter.getColumnModel().getColumn(1).setHeaderValue(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_PARAMETER_VALUE")); // NOI18N

        jBtnAddParameter.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Add")); // NOI18N
        jBtnAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddParameter(evt);
            }
        });

        jBtnRemoveParameter.setText(org.openide.util.NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Remove")); // NOI18N
        jBtnRemoveParameter.setEnabled(false);
        jBtnRemoveParameter.setPreferredSize(new java.awt.Dimension(63, 23));
        jBtnRemoveParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveParameter(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel3Layout.createSequentialGroup()
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                            .addContainerGap())
                        .add(jPanel3Layout.createSequentialGroup()
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jCBQueryBuilder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(18, 18, 18)
                            .add(jCheckBoxWeighted)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(jBtnAddParameter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnRemoveParameter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel3Layout.linkSize(new java.awt.Component[] {jBtnAddParameter, jBtnRemoveParameter}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBoxWeighted)
                    .add(jCBQueryBuilder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .add(18, 18, 18)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnRemoveParameter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBtnAddParameter)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jBtnOK)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBtnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(21, 21, 21))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(8, 8, 8)
                                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .add(2, 2, 2)
                                .add(jCBSearchResultList, 0, 457, Short.MAX_VALUE)))
                        .add(25, 25, 25))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTxtScreenTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                        .add(25, 25, 25))
                    .add(layout.createSequentialGroup()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(25, 25, 25))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 4, Short.MAX_VALUE)
                        .add(jLabelInstruction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 455, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(25, 25, 25))))
        );

        layout.linkSize(new java.awt.Component[] {jBtnCancel, jBtnOK}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jTxtScreenTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCBSearchResultList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelInstruction))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jBtnCancel)
                    .add(jBtnOK))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jPanel1, jPanel2}, org.jdesktop.layout.GroupLayout.VERTICAL);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-599)/2, (screenSize.height-669)/2, 599, 669);
    }// </editor-fold>//GEN-END:initComponents

private void onOKBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOKBtn
// TODO add your handling code here:
    //if ()
    bModified = true;
    mSearchType.setInstruction(this.jTxtInstruction.getText());
    mSearchType.setScreenTitle(this.jTxtScreenTitle.getText());
    if (this.bRefreshSearchResult) {
        for (SearchDetail searchResult : mSearchResultList) {
            if (searchResult.getDisplayName().equals((String) this.jCBSearchResultList.getSelectedItem())) {
                mSearchType.setScreenResultID(searchResult.getSearchResultID());
                break;
            }
        }
    }
    mSearchType.getFieldGroups().clear();
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    ArrayList<GroupRow> groups = fieldGroupModel.groupRows;
    for (GroupRow group : fieldGroupModel.groupRows) {
        FieldGroup fieldGroup = new FieldGroup();
        fieldGroup.setDescription(group.getGroupName());
        for (FieldRow field : group.getFieldRows()) {
            fieldGroup.addFieldRef(fieldGroup.createFieldRef(field.getFieldName()));
        }
        mSearchType.addFieldGroup(fieldGroup);
        
    }
    
    mSearchType.getSearchOption().setQueryBuilder((String) jCBQueryBuilder.getSelectedItem());      
    
    mSearchType.getSearchOption().setWeighted(this.jCheckBoxWeighted.isSelected());
    TableModelParameter paramModel = (TableModelParameter) this.jTableQueryParameter.getModel();
    mSearchType.getSearchOption().getParameterList().clear();
    for (ParameterRow param : paramModel.getAllRows()) {
        mSearchType.getSearchOption().addParameter(param.getParamName(), param.getParamValue());
    }
    
    
    
    this.dispose();
    
}//GEN-LAST:event_onOKBtn

private void onCancelBtn(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancelBtn
// TODO add your handling code here:
    bModified = false;
    this.dispose();
}//GEN-LAST:event_onCancelBtn

private void onAddFieldGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddFieldGroup
// TODO add your handling code here:
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) this.jTableFieldGroup.getModel();
    int iInsertRow = fieldGroupModel.getRowCount();
    GroupRow newFieldGroup = new GroupRow("New Group");
    fieldGroupModel.addRow(iInsertRow, newFieldGroup);
    jTableFieldGroup.setModel(fieldGroupModel);
    jTableFieldGroup.clearSelection();
    jTableFieldGroup.addRowSelectionInterval(iInsertRow, iInsertRow);
    jTableFieldGroup.setEditingRow(iInsertRow);
    jTableFieldGroup.setFocusTraversalKeysEnabled(true);
    jBtnAddField.setEnabled(true);
    jBtnRemoveFieldGroup.setEnabled(true);

}//GEN-LAST:event_onAddFieldGroup

private void onRemoveFieldGroup(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveFieldGroup
    int rs[] = this.jTableFieldGroup.getSelectedRows();
    int length = rs.length;
    String type = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELD_GROUP")
                                : NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELD_GROUPS");
    String prompt = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Prompt", type)
                                  : NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
    String title = NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Title", type);
         NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 title, 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelFieldGroup model = (TableModelFieldGroup) jTableFieldGroup.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object fieldGroup = (Object) model.getValueAt(j, model.iFieldGroupName);
                model.removeRow(j);
            }
            if (jTableFieldGroup.getRowCount() > 0) {
                this.jBtnRemoveFieldGroup.setEnabled(true);
                jTableFieldGroup.setRowSelectionInterval(0, 0);
            }
            this.bModified = true;
        }
    
}//GEN-LAST:event_onRemoveFieldGroup

private void onRemoveField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveField
    int rs[] = jTableFields.getSelectedRows();
    int length = rs.length;
    String type = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELD")
                                : NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_FIELDS");
    String prompt = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Prompt", type)
                                  : NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
    String title = NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Title", type);
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 title, 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelField model = (TableModelField) jTableFields.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object standardizationType = (Object) model.getValueAt(j, model.iColFieldName);
                model.removeRow(j);
            }
            
            
            if (jTableFields.getRowCount() > 0) {
                this.jBtnRemoveField.setEnabled(true);
                jTableFields.setRowSelectionInterval(0, 0);
            }

            this.bModified = true;
        }

}//GEN-LAST:event_onRemoveField

private void jTxtScreenTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTxtScreenTitleActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTxtScreenTitleActionPerformed

private void onAddParameter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddParameter
    TableModelParameter model = (TableModelParameter) jTableQueryParameter.getModel();
    int iInsertRow = model.getRowCount();
    //Parameter newParam = mSearchType.getSearchOption().createParameter("", "");
    ParameterRow newParameter = new ParameterRow();
    newParameter.setParamName("");
    newParameter.setParamValue("");
    model.addRow(iInsertRow, newParameter);
    jTableQueryParameter.setModel(model);
    jTableQueryParameter.clearSelection();
    jTableQueryParameter.addRowSelectionInterval(iInsertRow, iInsertRow);
    jTableQueryParameter.setEditingRow(iInsertRow);
    jTableQueryParameter.setFocusTraversalKeysEnabled(true);
}//GEN-LAST:event_onAddParameter

private void onRemoveParameter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveParameter
    int rs[] = jTableQueryParameter.getSelectedRows();
    int length = rs.length;
    String type = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Parameter")
                                : NbBundle.getMessage(DomainSearchTypePanel.class, "LBL_Parameters");
    String prompt = (length == 1) ? NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Prompt", type)
                                  : NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Multiple_Prompt", length, type);;
    String title = NbBundle.getMessage(DomainSearchTypePanel.class, "MSG_Confirm_Remove_Title", type);
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 title, 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
            TableModelParameter model = (TableModelParameter) jTableQueryParameter.getModel();
            for (int i=length - 1; i>=0 && i < length; i--) {
                int j = rs[i];
                Object standardizationType = (Object) model.getValueAt(j, model.iColParamName);
                model.removeRow(j);
            }
            
            if (jTableQueryParameter.getRowCount() > 0) {
                this.jBtnRemoveParameter.setEnabled(true);
                jTableQueryParameter.setRowSelectionInterval(0, 0);
            }

            this.bModified = true;
        }

}//GEN-LAST:event_onRemoveParameter

private void onBtnAddField(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBtnAddField
// TODO add your handling code here:
    TableModelFieldGroup fieldGroupModel = (TableModelFieldGroup) jTableFieldGroup.getModel();
    TableModelField fieldModel = (TableModelField) jTableFields.getModel();
    int selectedRow = jTableFieldGroup.getSelectedRow();
    GroupRow group = fieldGroupModel.getRow(selectedRow);
    FieldGroup fieldGroup = null;
    if (group.getGroupName() == null || group.getGroupName().length() == 0) {
        fieldGroup = mSearchType.getFieldGroup(group.getGroupId());
        if (fieldGroup == null) {
            fieldGroup = new FieldGroup();
        }

    }  else {
        fieldGroup = mSearchType.getFieldGroup(group.getGroupName());
        if (fieldGroup == null) {
            fieldGroup = mSearchType.getFieldGroup(group.getGroupId());
            if (fieldGroup == null) {
                fieldGroup = new FieldGroup();
            }
        }
    }
    EntityTreeDialog entityDlg = new EntityTreeDialog(mDomainNode.getEntityTree(), fieldGroup);
    entityDlg.setVisible(true);
    if (entityDlg.isSelected()) {
        if (entityDlg.getFieldList().size() > 0) {
            for (String fieldName : entityDlg.getFieldList()) {
                FieldRow fieldRef = new FieldRow(fieldName);
                fieldModel.addRow(fieldModel.getRowCount(), fieldRef);
            }
            jTableFields.setModel(fieldModel);
        }
    }
    
}//GEN-LAST:event_onBtnAddField

    class ParameterRow {
        
        private String paramName;
        private String paramValue;

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getParamValue() {
            return paramValue;
        }

        public void setParamValue(String paramValue) {
            this.paramValue = paramValue;
        }
        
        
    }
    
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

        private ArrayList<FieldRow> fieldRows = new ArrayList<FieldRow>();
        
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

    class TableModelParameter extends AbstractTableModel {

        private String columnNames[] = {NbBundle.getMessage(TabDomainSearch.class, "LBL_PARAMETER_NAME"),
                                        NbBundle.getMessage(TabDomainSearch.class, "LBL_PARAMETER_VALUE"),
                                       };
        ArrayList<ParameterRow> parameterRows;
        final static int iColParamName = 0;
        final static int iColParamValue = 1;

        TableModelParameter(ArrayList<ParameterRow> rows) {
            parameterRows = rows;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (parameterRows != null) {
                return parameterRows.size();
            }
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            if (parameterRows != null) {
                ParameterRow singleRow = (ParameterRow) parameterRows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColParamName:
                            return singleRow.getParamName();
                        case iColParamValue:
                            return singleRow.getParamValue();
                        default:
                            return null;
                    }
                }
            }
            return null;
        }

        public ParameterRow getRow(int row) {
            if (parameterRows != null) {
                return parameterRows.get(row);
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
                case iColParamName:
                    ((ParameterRow) parameterRows.get(row)).setParamName((String) value);
                    //((RecordDetail) fieldRows.get(row)).setRecordDetailId(Integer.parseInt((String) value));
                    break;
                case iColParamValue:
                    ((ParameterRow) parameterRows.get(row)).setParamValue((String) value);
                    //((RecordDetail) fieldRows.get(row)).setRecordDetailId(Integer.parseInt((String) value));
                    break;

            }

            //fieldRows.set(row, value);
            fireTableCellUpdated(row, col);

        }

        public void removeRow(int index) {
            parameterRows.remove(index);
            this.fireTableRowsDeleted(index, index);
        }

        public void addRow(int index, ParameterRow row) {
            //fieldRows.add(row);
            parameterRows.add(index, row);
            this.fireTableRowsInserted(index, index);
        }
        
        public ArrayList<ParameterRow> getAllRows() {
            return this.parameterRows;
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
                GroupRow singleRow = groupRows.get(row);
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

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAddField;
    private javax.swing.JButton jBtnAddFieldGroup;
    private javax.swing.JButton jBtnAddParameter;
    private javax.swing.JButton jBtnCancel;
    private javax.swing.JButton jBtnOK;
    private javax.swing.JButton jBtnRemoveField;
    private javax.swing.JButton jBtnRemoveFieldGroup;
    private javax.swing.JButton jBtnRemoveParameter;
    private javax.swing.JComboBox jCBQueryBuilder;
    private javax.swing.JComboBox jCBSearchResultList;
    private javax.swing.JCheckBox jCheckBoxWeighted;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelInstruction;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneFields;
    private javax.swing.JTable jTableFieldGroup;
    private javax.swing.JTable jTableFields;
    private javax.swing.JTable jTableQueryParameter;
    private javax.swing.JTextArea jTxtInstruction;
    private javax.swing.JTextField jTxtScreenTitle;
    // End of variables declaration//GEN-END:variables

}
