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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.awt.Rectangle;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;

import org.openide.nodes.Node;
import org.netbeans.api.project.Project;
import java.io.File;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;
import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.LinkBaseNode;
import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.project.actions.ImportDomainAction;

/**
 *
 * @author  kkao
 */
public class TabOverview extends javax.swing.JPanel implements MouseListener, MouseMotionListener  {
    private final String ALL_DOMAINS = org.openide.util.NbBundle.getMessage(TabOverview.class, "All_Domains");
    TabOverview mTabOverview;
    EditorMainPanel mEditorMainPanel;
    EditorMainApp mEditorMainApp;
    private ArrayList <DomainNode> mAlDomainNodes;
    private ArrayList <String> mAlDomainNames = new ArrayList();;
    private ArrayList <LinkType> mAlLinkTypes;
    private Map <String, DomainNode> mMapDomainNodes = new HashMap();  // domainName, DomainNode
    static final Image DOMAINIMAGE = Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png");
    static final ImageIcon DOMAINIMAGEICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png"));
    private BufferedImage backingImage;
    private Point last;
    public TabOverview(EditorMainPanel editorMainPanel, EditorMainApp editorMainApp) {
        initComponents();
        mTabOverview = this;
        mEditorMainPanel = editorMainPanel;
        mEditorMainApp = editorMainApp;
        jLabelDomainName.setIcon(DOMAINIMAGEICON);
        //jComboBoxAssociatedDomains.removeAllItems();
        //jComboBoxAssociatedDomains.addItem(ALL_DOMAINS);
        // load domain nodes
        loadDomains();
        // load link types
        ArrayList rows = loadLinks(false);
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
            
        //Drag and drop
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setDropTarget(new DropTarget(this, new DropTargetListener() { 
            String dragTargetName = "";
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            public void dragExit(DropTargetEvent dte) {

            }

            public void dragOver(DropTargetDragEvent dtde) {
                if (isMasterIndexProject(dtde)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                } else {
                    dtde.rejectDrag();
                }
            }

            public void drop(DropTargetDropEvent dtde) {
                // Add DomainNode to the canvas
                Transferable trans = dtde.getTransferable();
                DataFlavor[] dfs = trans.getTransferDataFlavors();
                if (dfs.length > 0) {
                    try {
                        Object obj = trans.getTransferData(dfs[0]);
                        if (obj instanceof Node) {
                            Node node = (Node) obj;
                            Project p = node.getLookup().lookup(Project.class);
                            if (p != null) {
                                FileObject pfobj = p.getProjectDirectory();
                                File selectedDomain = FileUtil.toFile(pfobj);
                                String domainName = selectedDomain.getName();
                                mEditorMainApp.addDomain(selectedDomain);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {

            }
            
            public boolean isMasterIndexProject(DropTargetDragEvent dtde) {
                dragTargetName = "";
                boolean bRet = false;
                Transferable trans = dtde.getTransferable();

                DataFlavor[] dfs = trans.getTransferDataFlavors();
                if (dfs.length > 0) {
                    try {
                        Object obj = trans.getTransferData(dfs[0]);
                        if (obj instanceof Node) {
                            Node node = (Node) obj;
                            dragTargetName = node.getName();

                            Project p = node.getLookup().lookup(Project.class);
                            if (p != null) {
                                String clsName = p.getClass().getName();
                                if (clsName.equals("com.sun.mdm.index.project.EviewApplication")) {
                                    bRet = true;
                                    Graphics2D g2D = (Graphics2D) mTabOverview.getGraphics();
                                    Rectangle visRect = mTabOverview.getVisibleRect();
                                    mTabOverview.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                                    g2D.drawImage(DOMAINIMAGE, AffineTransform.getTranslateInstance(dtde.getLocation().getX(), dtde.getLocation().getY()), null);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return bRet;
            }
        }));          
    }

    private void loadDomains() {
        mAlDomainNames.clear();
        mMapDomainNodes.clear();
        jComboBoxSelectedDomain.removeAllItems();
        mAlDomainNodes = mEditorMainApp.getDomainNodes();
        if ( mAlDomainNodes != null && mAlDomainNodes.size() > 0) {
            for (int i=0; i < mAlDomainNodes.size(); i++) {
                DomainNode domainNode = mAlDomainNodes.get(i);
                String domainName = domainNode.getName();
                mAlDomainNames.add(domainName);
                mMapDomainNodes.put(domainName, domainNode);
                jComboBoxSelectedDomain.addItem(domainName);
            }
            jComboBoxSelectedDomain.setSelectedIndex(0);
        }
    }
    
    private ArrayList loadLinks(boolean refresh) {
        TableModelLinkType model = null;
        if (refresh) {
            model = (TableModelLinkType) jTableLinkTypes.getModel();
            jTableLinkTypes.removeAll();
        }
        ArrayList rows = new ArrayList();
        if (mAlDomainNodes != null && mAlDomainNodes.size() > 0) {
            DomainNode domainNode = mAlDomainNodes.get(0);
            String domainName = domainNode.getName();
            ArrayList <LinkType> alLinkTypes = domainNode.getLinkTypes();
            ArrayList <String> alAssociatedDomains = domainNode.getAssociatedDomains();
            if (alLinkTypes != null &&  alLinkTypes.size() > 0) {
                for (int i=0; alLinkTypes != null && i < alLinkTypes.size(); i++) {
                    LinkType type = alLinkTypes.get(i);
                    String sourceDomain = type.getSourceDomain();
                    String targetDomain = type.getTargetDomain();
                    String domainWithLinks = (domainName.equals(sourceDomain)) ? targetDomain : sourceDomain;
                    LinkTypeRow r = new LinkTypeRow(type.getType(), type.getName(), type.getSourceDomain(), type.getTargetDomain());
                    rows.add(r);
                }
            }
        }
        if (refresh) {
            model.fireTableDataChanged();
        }
        return rows;
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
        jButtonDeleteDomain = new javax.swing.JButton();
        jButtonAddDomain = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Participating_Domains"))); // NOI18N
        setLayout(null);

        jLabelDomainName.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Domain")); // NOI18N
        add(jLabelDomainName);
        jLabelDomainName.setBounds(20, 30, 170, 20);
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
        jScrollPaneLinkTypes.setBounds(10, 90, 370, 180);

        add(jComboBoxSelectedDomain);
        jComboBoxSelectedDomain.setBounds(200, 30, 180, 22);

        jButtonAddLink.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddLink(evt);
            }
        });
        add(jButtonAddLink);
        jButtonAddLink.setBounds(200, 280, 90, 23);

        jButtonDeleteLink.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Remove")); // NOI18N
        jButtonDeleteLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveLink(evt);
            }
        });
        add(jButtonDeleteLink);
        jButtonDeleteLink.setBounds(290, 280, 90, 23);

        jButtonDeleteDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Remove")); // NOI18N
        jButtonDeleteDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveDomain(evt);
            }
        });
        add(jButtonDeleteDomain);
        jButtonDeleteDomain.setBounds(290, 60, 90, 23);

        jButtonAddDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddDomain(evt);
            }
        });
        add(jButtonAddDomain);
        jButtonAddDomain.setBounds(200, 60, 90, 23);
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

private void onRemoveDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveDomain
    String domainName = (String) jComboBoxSelectedDomain.getSelectedItem();
    mEditorMainApp.deleteDomain(domainName);
    mAlDomainNodes = mEditorMainApp.getDomainNodes();
    loadDomains();
    loadLinks(true);
    //this.mAlDomainNodes.remove(domainName);
    //this.mAlDomainNames.remove(domainName);
    //this.mMapDomainNodes.remove(domainName);
    //int idx = jComboBoxSelectedDomain.getSelectedIndex();
    //jComboBoxSelectedDomain.remove(idx);    


}//GEN-LAST:event_onRemoveDomain

private void onAddDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddDomain
// TODO add your handling code here:
    Action action = SystemAction.get(ImportDomainAction.class);
    ((ImportDomainAction) action).perform(mEditorMainApp);

}//GEN-LAST:event_onAddDomain

    private void onSelectedDomainItemStateChanged(java.awt.event.ItemEvent evt) {
        String domainName = (String) jComboBoxSelectedDomain.getSelectedItem();
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        
        mEditorMainPanel.loadDomainEntityTree(domainNode);
        
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
    private javax.swing.JButton jButtonAddDomain;
    private javax.swing.JButton jButtonAddLink;
    private javax.swing.JButton jButtonDeleteDomain;
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
            jComboBoxSelectedDomain.requestFocus();
            this.mAlDomainNodes.add(node);
            this.mMapDomainNodes.put(domainName, node);
            int idx = jComboBoxSelectedDomain.getItemCount();
            jComboBoxSelectedDomain.insertItemAt(domainName, idx);
            jComboBoxSelectedDomain.setSelectedItem(domainName);
        } else {
            //jComboBoxAllDomains.setSelectedItem(domainName);
        }
        
    }
    
    
    
    // For drag/drop
    public BufferedImage getImage() {
        int width = Math.min(getWidth(), 1600);
        int height = Math.min(getHeight(),1200);
        if (backingImage == null || backingImage.getWidth() != width || backingImage.getHeight() != height) {
            BufferedImage old = backingImage;
            backingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics g = backingImage.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            if (old != null) {
                ((Graphics2D) backingImage.getGraphics()).drawRenderedImage(old,
                        AffineTransform.getTranslateInstance(0, 0));
            }
        }
        return backingImage;
    }
    
    public Paint getPaint() {
        return Color.BLUE;
    }
    
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        Graphics2D g = getImage().createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(getPaint());
        g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (last == null) {
            last = p;
        }
        g.drawLine(last.x, last.y, p.x, p.y);
        repaint(Math.min(last.x, p.x) - 10 / 2 - 1,
                Math.min(last.y, p.y) - 10 / 2 - 1,
                Math.abs(last.x - p.x) + 10 + 2,
                Math.abs(last.y - p.y) + 10 + 2);
        last = p;
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
        mouseClicked(e);
    }
    
    public void mouseMoved(MouseEvent e) {
        //last = null;
    }
    
    
}
