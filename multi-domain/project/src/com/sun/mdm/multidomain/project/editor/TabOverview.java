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
import javax.swing.table.AbstractTableModel;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableModel;
//import javax.swing.table.TableRowSorter;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.LinkBaseNode;
import com.sun.mdm.multidomain.parser.LinkType;
//import com.sun.mdm.multidomain.parser.Attribute;
/**
 *
 * @author  kkao
 */
public class TabOverview extends javax.swing.JPanel {
    private final String ALL_DOMAINS = org.openide.util.NbBundle.getMessage(TabOverview.class, "All_Domains");
    EditorMainPanel mEditorMainPanel;
    EditorMainApp mEditorMainApp;
    private ArrayList <DomainNode> mAlDomainNodes;
    private ArrayList <String> mAlDomainNames = new ArrayList();;
    private ArrayList <LinkType> mAlLinkTypes;
    private Map <String, DomainNode> mMapDomainNodes = new HashMap();  // domainName, DomainNode
    /** Creates new form TabOverview */
    public TabOverview(EditorMainPanel editorMainPanel, EditorMainApp editorMainApp, ArrayList <DomainNode> alDomainNodes, ArrayList <LinkBaseNode> alLinkNodes) {
        initComponents();
        mEditorMainPanel = editorMainPanel;
        mEditorMainApp = editorMainApp;
        mAlDomainNodes = alDomainNodes;
        jComboBoxSelectedDomain.removeAllItems();
        //jComboBoxAssociatedDomains.removeAllItems();
        //jComboBoxAssociatedDomains.addItem(ALL_DOMAINS);

        DomainNode domainNode;
        String domainName;
        if ( mAlDomainNodes != null && mAlDomainNodes.size() > 0) {
            for (int i=0; i < mAlDomainNodes.size(); i++) {
                domainNode = mAlDomainNodes.get(i);
                domainName = domainNode.getName();
                mAlDomainNames.add(domainName);
                mMapDomainNodes.put(domainName, domainNode);
                jComboBoxSelectedDomain.addItem(domainName);
            }
            jComboBoxSelectedDomain.setSelectedIndex(0);
        }
        
        ArrayList rows = new ArrayList();
        if (mAlDomainNodes != null && mAlDomainNodes.size() > 0) {
            domainNode = mAlDomainNodes.get(0);
            domainName = domainNode.getName();
            ArrayList <LinkType> alLinkTypes = domainNode.getLinkTypes();
            ArrayList <String> alAssociatedDomains = domainNode.getAssociatedDomains();
            if (alLinkTypes == null ||  alLinkTypes.size() == 0) {
                /*
                for (int i=0; alAssociatedDomains != null && i < alAssociatedDomains.size(); i++) {
                    String domainWithLinks = alAssociatedDomains.get(i);
                    boolean bAdd = true;
                    for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                        String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                        if (associatedDomainName.equals(domainWithLinks)) {
                            bAdd = false;
                            break;
                        }
                    }
                    if (bAdd) {
                        jComboBoxAssociatedDomains.addItem(domainWithLinks);
                    }
                }
                 */
            } else {
                for (int i=0; alLinkTypes != null && i < alLinkTypes.size(); i++) {
                    LinkType type = alLinkTypes.get(i);
                    String sourceDomain = type.getSourceDomain();
                    String targetDomain = type.getTargetDomain();
                    String domainWithLinks = (domainName.equals(sourceDomain)) ? targetDomain : sourceDomain;
                    /*
                    boolean bAdd = true;
                    for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                        String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                        if (associatedDomainName.equals(domainWithLinks)) {
                            bAdd = false;
                            break;
                        }
                    }
                    if (bAdd) {
                        jComboBoxAssociatedDomains.addItem(domainWithLinks);
                    }
                     */
                    LinkTypeRow r = new LinkTypeRow(type.getType(), type.getName(), type.getSourceDomain(), type.getTargetDomain());
                    rows.add(r);
                }
            }
        }
        //jComboBoxAssociatedDomains.setSelectedIndex(0);
        TableModelLinkType model = new TableModelLinkType(rows);
        jTableLinkTypes.setModel(model);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        //jTableLinkTypes.setRowSorter(sorter);
        
        jComboBoxSelectedDomain.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onSelectedDomainItemStateChanged(evt);
            }
        });
        jComboBoxSelectedDomain.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    onSelectedDomainItemStateChanged(null);
                }
            });
        /*
        jComboBoxAssociatedDomains.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onAssociatedDomainsItemStateChanged(evt);
            }
        });
        */
        jTableLinkTypes.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    onLinkTypeSelected();
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
        jScrollPaneLinkTypes = new javax.swing.JScrollPane();
        jTableLinkTypes = new javax.swing.JTable();
        jComboBoxSelectedDomain = new javax.swing.JComboBox();
        jButtonAddLink = new javax.swing.JButton();
        jButtonDeleteLink = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Participating_Domains"))); // NOI18N
        setLayout(null);

        jLabelDomainName.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Domain")); // NOI18N
        add(jLabelDomainName);
        jLabelDomainName.setBounds(20, 30, 100, 20);
        jLabelDomainName.getAccessibleContext().setAccessibleName("jLabelDomainName");

        jScrollPaneLinkTypes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Links_Defined"))); // NOI18N

        jTableLinkTypes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Source Domain", "Target Domain"
            }
        ));
        jScrollPaneLinkTypes.setViewportView(jTableLinkTypes);

        add(jScrollPaneLinkTypes);
        jScrollPaneLinkTypes.setBounds(10, 60, 370, 180);

        add(jComboBoxSelectedDomain);
        jComboBoxSelectedDomain.setBounds(120, 30, 260, 22);

        jButtonAddLink.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddLink(evt);
            }
        });
        add(jButtonAddLink);
        jButtonAddLink.setBounds(240, 240, 70, 23);

        jButtonDeleteLink.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Remove")); // NOI18N
        jButtonDeleteLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveLink(evt);
            }
        });
        add(jButtonDeleteLink);
        jButtonDeleteLink.setBounds(310, 240, 71, 23);
    }// </editor-fold>//GEN-END:initComponents

private void onRemoveLink(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveLink
        TableModelLinkType model = (TableModelLinkType) jTableLinkTypes.getModel();
        int rs[] = jTableLinkTypes.getSelectedRows();
        int length = rs.length;
        String prompt = (length == 1) ? NbBundle.getMessage(TabOverview.class, "MSG_Confirm_Remove_Row_Prompt")
                                        : NbBundle.getMessage(TabOverview.class, "MSG_Confirm_Remove_Rows_Prompt");

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(TabOverview.class, "MSG_Confirm_Remove_Row_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
            if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
                for (int i=length - 1; i>=0 && i < length; i--) {
                    int idx = rs[i];
                    LinkTypeRow row = model.getRow(idx);
                    mEditorMainApp.deleteLink(row.getLinkName(), row.getSourceDomain(), row.getTargetDomain());
                    model.removeRow(idx);
                }
                model.fireTableDataChanged();
                // update properties tab
                if (model.getRowCount() > 0) {
                    jTableLinkTypes.setRowSelectionInterval(0, 0);
                    onLinkTypeSelected();
                } else {
                    mEditorMainPanel.loadLinkProperties(null);
                }
            }
}//GEN-LAST:event_onRemoveLink

private void onAddLink(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddLink
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AddLinkDialog dialog = new AddLinkDialog(mAlDomainNames);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (dialog.getReturnStatus() == AddLinkDialog.RET_OK) {
                            String type = dialog.getLinkType();
                            String linkName = dialog.getLinkName();
                            String sourceDomain = dialog.getSourceDomain();
                            String targetDomain = dialog.getTargetDomain();
                            LinkBaseNode linkNode = mEditorMainApp.getLinkNode(linkName, sourceDomain, targetDomain);
                            if (linkNode != null) {
                            //Already exists
                            } else {
                                // add new LinkNode
                                LinkType linkType = new LinkType(linkName, type, sourceDomain, targetDomain, null);
                                linkNode = mEditorMainApp.addLink(linkType);
                                mEditorMainPanel.loadLinkProperties(linkNode);
                                // add a new row
                                TableModelLinkType model = (TableModelLinkType) jTableLinkTypes.getModel();
                                LinkTypeRow r = new LinkTypeRow(linkType.getType(), linkType.getName(), linkType.getSourceDomain(), linkType.getTargetDomain());
                                model.addRow(model.getRowCount(), r);
                                model.fireTableDataChanged();

                            }
                        }
                    }
                });
                dialog.setVisible(true);
            }
        });
}//GEN-LAST:event_onAddLink

    private void onSelectedDomainItemStateChanged(java.awt.event.ItemEvent evt) {
        String domainName = (String) jComboBoxSelectedDomain.getSelectedItem();
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        
        //get properties panel from domainNode and present it
        mEditorMainPanel.loadDomainProperties(domainNode);
        
        mAlLinkTypes = domainNode.getLinkTypes();
        // ArrayList <String> alAssociatedDomains = domainNode.getAssociatedDomains();
        // if (jComboBoxAssociatedDomains.getItemCount() > 0) {
        //     jComboBoxAssociatedDomains.removeAllItems();
        // }
        // jComboBoxAssociatedDomains.addItem(ALL_DOMAINS);
        if (mAlLinkTypes == null ||  mAlLinkTypes.size() == 0) {
            /*
            for (int i=0; alAssociatedDomains != null && i < alAssociatedDomains.size(); i++) {
                String domainWithLinks = alAssociatedDomains.get(i);
                boolean bAdd = true;
                for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                    String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                    if (associatedDomainName.equals(domainWithLinks)) {
                        bAdd = false;
                        break;
                    }
                }
                if (bAdd) {
                    jComboBoxAssociatedDomains.addItem(domainWithLinks);
                }
            }
             */
        } else {
            for (int i=0; mAlLinkTypes != null && i < mAlLinkTypes.size(); i++) {
                LinkType type = mAlLinkTypes.get(i);
                String sourceDomain = type.getSourceDomain();
                String targetDomain = type.getTargetDomain();
                String domainWithLinks = (domainName.equals(sourceDomain)) ? targetDomain : sourceDomain;
                /*
                boolean bAdd = true;
                for (int j=0; j < jComboBoxAssociatedDomains.getItemCount(); j++) {
                    String associatedDomainName = (String) jComboBoxAssociatedDomains.getItemAt(j);
                    if (associatedDomainName.equals(domainWithLinks)) {
                        bAdd = false;
                        break;
                    }
                }
                if (bAdd) {
                    jComboBoxAssociatedDomains.addItem(domainWithLinks);
                }
                 */
            }
        }
        //jComboBoxAssociatedDomains.setSelectedIndex(0);
    }

    /*
    private void onAssociatedDomainsItemStateChanged(java.awt.event.ItemEvent evt) {
        String domainName = (String) jComboBoxSelectedDomain.getSelectedItem();
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        mAlLinkTypes = domainNode.getLinkTypes();

        //String associatedDomain = (String) jComboBoxAssociatedDomains.getSelectedItem();
        TableModelLinkType model = (TableModelLinkType) jTableLinkTypes.getModel();
        model.rows.clear();
        int index = 0;

        for (int i=0; mAlLinkTypes != null && i < mAlLinkTypes.size(); i++) {
            LinkType type = mAlLinkTypes.get(i);
            String sourceDomain = type.getSourceDomain();
            String targetDomain = type.getTargetDomain();
            boolean associated = false;
            if (associatedDomain != null) {
                associated = (associatedDomain.equals(sourceDomain)) | (associatedDomain.equals(targetDomain));            }
            if (jComboBoxAssociatedDomains.getSelectedIndex() == 0 || associated) {
                LinkTypeRow r = new LinkTypeRow(type.getType(), type.getName(), type.getSourceDomain(), type.getTargetDomain());
                model.addRow(index++, r);
            }
        }
        model.fireTableDataChanged();
    }
     */
    
    private void onLinkTypeSelected() {
        int iSelectedRow = jTableLinkTypes.getSelectedRow();
        TableModelLinkType model = (TableModelLinkType) jTableLinkTypes.getModel();
        String linkType = (String) model.getValueAt(iSelectedRow,  model.iColLinkType);
        String linkName = (String) model.getValueAt(iSelectedRow,  model.iColLinkName);
        String sourceDomain = (String) model.getValueAt(iSelectedRow,  model.iColSourceDomain);
        String targetDomain = (String) model.getValueAt(iSelectedRow,  model.iColTargetDomain);
        LinkBaseNode linkNode = mEditorMainApp.getLinkNode(linkName, sourceDomain, targetDomain);
        mEditorMainPanel.loadLinkProperties(linkNode);
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLink;
    private javax.swing.JButton jButtonDeleteLink;
    private javax.swing.JComboBox jComboBoxSelectedDomain;
    private javax.swing.JLabel jLabelDomainName;
    private javax.swing.JScrollPane jScrollPaneLinkTypes;
    private javax.swing.JTable jTableLinkTypes;
    // End of variables declaration//GEN-END:variables
    
    class LinkTypeRow {
        private String linkName;
        private String type;
        private String sourceDomain;
        private String targetDomain;

        public LinkTypeRow(String type, String linkName, String sourceDomain, String targetDomain) {
            this.linkName = linkName;
            this.type = type;
            this.sourceDomain = sourceDomain;
            this.targetDomain = targetDomain;
        }

        public String getLinkName() {
            return linkName;
        }

        public void setLinkName(String linkName) {
            this.linkName = linkName;
        }
        
        public String getLinkType() {
            return type;
        }

        public void setLinkType(String type) {
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
    class TableModelLinkType extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Link_Type"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Link_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_Source_Domain"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Target_Domain"), 
                                        };
        ArrayList <LinkTypeRow> rows;
        final static int iColLinkType = 0;
        final static int iColLinkName = 1;
        final static int iColSourceDomain = 2;
        final static int iColTargetDomain = 3;
        
        TableModelLinkType(ArrayList rows) {
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
                LinkTypeRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColLinkName:
                            return singleRow.getLinkName();
                        case iColLinkType:
                            return singleRow.getLinkType();
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
                LinkTypeRow singleRow = (LinkTypeRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColLinkName:
                            singleRow.setLinkName((String) value);                            
                            break;
                        case iColLinkType:
                            singleRow.setLinkType((String) value);                            
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
        
        public void addRow(int index, LinkTypeRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public LinkTypeRow getRow(int index) {
            LinkTypeRow row = (LinkTypeRow) rows.get(index);
            return row;
        }
    }

    public void setCurrentDomainNode(DomainNode node, boolean bNew) {
        String domainName = node.getName();
        if (bNew) {
            this.mAlDomainNodes.add(node);
            this.mMapDomainNodes.put(domainName, node);
            int idx = jComboBoxSelectedDomain.getItemCount();
            jComboBoxSelectedDomain.insertItemAt(domainName, idx);
            jComboBoxSelectedDomain.setSelectedItem(idx);
        } else {
            //jComboBoxAllDomains.setSelectedItem(domainName);
        }
        
    }
}
