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
import com.sun.mdm.multidomain.project.editor.nodes.DefinitionNode;
import com.sun.mdm.multidomain.parser.Definition;
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
    private Map <String, DomainNode> mMapDomainNodes = new HashMap();  // domainName, DomainNode
    static final Image DOMAINIMAGE = Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png");
    static final ImageIcon DOMAINIMAGEICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png"));
    static final ImageIcon DEFINITIONIMAGEICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/Definition.png"));
    private BufferedImage backingImage;
    private Point last;
    public TabOverview(EditorMainPanel editorMainPanel, EditorMainApp editorMainApp) {
        initComponents();
        mTabOverview = this;
        mEditorMainPanel = editorMainPanel;
        mEditorMainApp = editorMainApp;
        jLabelDomainName.setIcon(DOMAINIMAGEICON);
        jLabelDefinition.setIcon(DEFINITIONIMAGEICON);
        // load domain nodes
        jButtonRemoveDomain.setEnabled(false);
        jButtonRemoveDefinition.setEnabled(false);
        ArrayList rows = loadDomains();
        TableModelDomains modelDomains = new TableModelDomains(rows);
        jTableDomains.setModel(modelDomains);
        if (rows.size() > 0) {
            jTableDomains.setRowSelectionInterval(0, 0);
            jButtonRemoveDomain.setEnabled(true);
        }
        jTableDomains.getTableHeader();
        // load definitions
        rows = loadDefinitions(false);
        TableModelDefinition modelDefinitions = new TableModelDefinition(rows);
        jTableDefinitions.setModel(modelDefinitions);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        //jTableDefinitions.setRowSorter(sorter);
        jTableDomains.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    onDomainSelected();
                }
            });
        jTableDefinitions.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    onDefinitionSelected();
                }
            });
            
        //Drag and drop
        addMouseListener(this);
        addMouseMotionListener(this);
        this.jLabelDomainName.setDropTarget(new DropTarget(this, new DropTargetListener() { 
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
                                    Graphics2D g2D = (Graphics2D) jLabelDomainName.getGraphics();
                                    Rectangle visRect = jLabelDomainName.getVisibleRect();
                                    jLabelDomainName.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
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

    private ArrayList loadDomains() {
        mAlDomainNames.clear();
        mMapDomainNodes.clear();
        mAlDomainNodes = mEditorMainApp.getDomainNodes();
        ArrayList rows = new ArrayList();
        if ( mAlDomainNodes != null && mAlDomainNodes.size() > 0) {
            for (int i=0; i < mAlDomainNodes.size(); i++) {
                DomainNode domainNode = mAlDomainNodes.get(i);
                String domainName = domainNode.getName();
                mAlDomainNames.add(domainName);
                mMapDomainNodes.put(domainName, domainNode);
                DomainRow r = new DomainRow(domainName);
                rows.add(r);
            }
            jTableDomains.setRowSelectionInterval(0, 0);
        } else {
            // make sure the properties tab is empty
            mEditorMainPanel.loadDomainProperties(null);
        }
        return rows;
    }
    
    private ArrayList loadDefinitions(boolean refresh) {
        TableModelDefinition model = null;
        if (refresh) {
            model = (TableModelDefinition) jTableDefinitions.getModel();
            jTableDefinitions.removeAll();
        }
        ArrayList <DefinitionNode> alDefinitionNodes = mEditorMainApp.getDefinitionNodes();
        ArrayList rows = new ArrayList();
        for (int i=0; alDefinitionNodes != null && i<alDefinitionNodes.size(); i++) {
            DefinitionNode definitionNode = alDefinitionNodes.get(i);
            Definition type = definitionNode.getLinkType();
            String sourceDomain = type.getSourceDomain();
            String targetDomain = type.getTargetDomain();
            DefinitionRow r = new DefinitionRow(type.getType(), type.getName(), type.getSourceDomain(), type.getTargetDomain());
            rows.add(r);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDomains = new javax.swing.JTable();
        jButtonAddDomain = new javax.swing.JButton();
        jButtonRemoveDomain = new javax.swing.JButton();
        jLabelDefinition = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDefinitions = new javax.swing.JTable();
        jButtonAddDefinition = new javax.swing.JButton();
        jButtonRemoveDefinition = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Overview"))); // NOI18N

        jLabelDomainName.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jLabelDomainName.text")); // NOI18N

        jTableDomains.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableDomains);

        jButtonAddDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonAddDomain.text")); // NOI18N
        jButtonAddDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddDomain(evt);
            }
        });

        jButtonRemoveDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonRemoveDomain.text")); // NOI18N
        jButtonRemoveDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveDomain(evt);
            }
        });

        jLabelDefinition.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jLabelDefinition.text")); // NOI18N

        jTableDefinitions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTableDefinitions);

        jButtonAddDefinition.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonAddDefinition.text")); // NOI18N
        jButtonAddDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddDefinition(evt);
            }
        });

        jButtonRemoveDefinition.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "TabOverview.jButtonRemoveDefinition.text")); // NOI18N
        jButtonRemoveDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveDefinition(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .add(180, 180, 180)
                .add(jButtonAddDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonRemoveDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                .add(layout.createSequentialGroup()
                    .add(jLabelDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonAddDomain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonRemoveDomain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jLabelDomainName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelDomainName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jButtonAddDomain)
                            .add(jButtonRemoveDomain))
                        .add(25, 25, 25))
                    .add(layout.createSequentialGroup()
                        .add(jLabelDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonAddDefinition)
                    .add(jButtonRemoveDefinition)))
        );
    }// </editor-fold>//GEN-END:initComponents

private void onAddDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddDomain
// TODO add your handling code here:
    Action action = SystemAction.get(ImportDomainAction.class);
    ((ImportDomainAction) action).perform(mEditorMainApp);
}//GEN-LAST:event_onAddDomain

private void onRemoveDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveDomain
TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
    int rs[] = jTableDomains.getSelectedRows();
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
            DomainRow row = model.getRow(idx);
            String domainName = row.getDomainName();
            mEditorMainApp.deleteDomain(domainName);
            model.removeRow(idx);
        }
        mAlDomainNodes = mEditorMainApp.getDomainNodes();
        loadDomains();
        loadDefinitions(true);
        if (model.getRowCount() > 0) {
            jTableDomains.setRowSelectionInterval(0, 0);
            onDomainSelected();
        } else {
            this.jButtonRemoveDomain.setEnabled(false);       
        }
        mEditorMainApp.enableSaveAction(true);
    }
}//GEN-LAST:event_onRemoveDomain

private void onRemoveDefinition(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveDefinition
TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        int rs[] = jTableDefinitions.getSelectedRows();
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
                    DefinitionRow row = model.getRow(idx);
                    mEditorMainApp.deleteDefinition(row.getDefinitionName(), row.getSourceDomain(), row.getTargetDomain());
                    model.removeRow(idx);
                }
                model.fireTableDataChanged();
                // update properties tab
                if (model.getRowCount() > 0) {
                    jTableDefinitions.setRowSelectionInterval(0, 0);
                    onDefinitionSelected();
                } else {
                    mEditorMainPanel.loadDefinitionProperties(null);
                    this.jButtonRemoveDefinition.setEnabled(false);
                }
                mEditorMainApp.enableSaveAction(true);
            }
}//GEN-LAST:event_onRemoveDefinition

private void onAddDefinition(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddDefinition
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AddDefinitionDialog dialog = new AddDefinitionDialog(mAlDomainNames);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (dialog.getReturnStatus() == AddDefinitionDialog.RET_OK) {
                            String type = dialog.getDefinitionType();
                            String linkName = dialog.getDefinitionName();
                            String sourceDomain = dialog.getSourceDomain();
                            String targetDomain = dialog.getTargetDomain();
                            DefinitionNode definitionNode = mEditorMainApp.getDefinitionNode(linkName, sourceDomain, targetDomain);
                            if (definitionNode != null) {
                            //Already exists
                            } else {
                                // add new LinkNode
                                Definition definitionType = new Definition(linkName, type, sourceDomain, targetDomain, null, null);
                                definitionNode = mEditorMainApp.addDefinition(definitionType);
                                mEditorMainPanel.loadDefinitionProperties(definitionNode);
                                // add a new row
                                TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
                                DefinitionRow r = new DefinitionRow(definitionType.getType(), definitionType.getName(), definitionType.getSourceDomain(), definitionType.getTargetDomain());
                                model.addRow(model.getRowCount(), r);
                                model.fireTableDataChanged();
                                mEditorMainApp.enableSaveAction(true);
                            }
                        }
                    }
                });
                dialog.setVisible(true);
            }
        });

}//GEN-LAST:event_onAddDefinition


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddDefinition;
    private javax.swing.JButton jButtonAddDomain;
    private javax.swing.JButton jButtonRemoveDefinition;
    private javax.swing.JButton jButtonRemoveDomain;
    private javax.swing.JLabel jLabelDefinition;
    private javax.swing.JLabel jLabelDomainName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDefinitions;
    private javax.swing.JTable jTableDomains;
    // End of variables declaration//GEN-END:variables

private void onRemoveLink(java.awt.event.ActionEvent evt) {                              
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        int rs[] = jTableDefinitions.getSelectedRows();
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
                    DefinitionRow row = model.getRow(idx);
                    mEditorMainApp.deleteDefinition(row.getDefinitionName(), row.getSourceDomain(), row.getTargetDomain());
                    model.removeRow(idx);
                }
                model.fireTableDataChanged();
                // update properties tab
                if (model.getRowCount() > 0) {
                    jTableDefinitions.setRowSelectionInterval(0, 0);
                    onDefinitionSelected();
                } else {
                    mEditorMainPanel.loadDefinitionProperties(null);
                    this.jButtonRemoveDefinition.setEnabled(false);
                }
                mEditorMainApp.enableSaveAction(true);
            }
}                             

    private void onDomainSelected() {
        this.jTableDefinitions.clearSelection();
        this.jButtonRemoveDefinition.setEnabled(false);
        this.jButtonRemoveDomain.setEnabled(true);       
        int iSelectedRow = jTableDomains.getSelectedRow();
        TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
        String domainName = (String) model.getValueAt(iSelectedRow,  model.iColDomainName);
        
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        
        mEditorMainPanel.loadDomainEntityTree(domainNode);
        
        //get properties panel from domainNode and present it
        mEditorMainPanel.loadDomainProperties(domainNode);
    }
    
    private void onDefinitionSelected() {
        this.jTableDomains.clearSelection();
        this.jButtonRemoveDomain.setEnabled(false);
        this.jButtonRemoveDefinition.setEnabled(true);
        int iSelectedRow = jTableDefinitions.getSelectedRow();
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        String definitionType = (String) model.getValueAt(iSelectedRow,  model.iColDefinitionType);
        String linkName = (String) model.getValueAt(iSelectedRow,  model.iColLinkName);
        String sourceDomain = (String) model.getValueAt(iSelectedRow,  model.iColSourceDomain);
        String targetDomain = (String) model.getValueAt(iSelectedRow,  model.iColTargetDomain);
        DefinitionNode definitionNode = mEditorMainApp.getDefinitionNode(linkName, sourceDomain, targetDomain);
        mEditorMainPanel.loadDefinitionProperties(definitionNode);
     }

    class DomainRow {
        private String domainName;

        public DomainRow(String domainName) {
            this.domainName = domainName;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }
    }
    
    // Table model for link definitions
    class TableModelDomains extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Domain_Name"), 
                                        };
        ArrayList <DomainRow> rows;
        final static int iColDomainName = 0;
        
        TableModelDomains(ArrayList rows) {
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
                DomainRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColDomainName:
                            return singleRow.getDomainName();
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
                DomainRow singleRow = (DomainRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColDomainName:
                            singleRow.setDomainName((String) value);                            
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
        
        public void addRow(int index, DomainRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public DomainRow getRow(int index) {
            DomainRow row = (DomainRow) rows.get(index);
            return row;
        }
    }
    
    class DefinitionRow {
        private String linkName;
        private String type;
        private String sourceDomain;
        private String targetDomain;

        public DefinitionRow(String type, String linkName, String sourceDomain, String targetDomain) {
            this.linkName = linkName;
            this.type = type;
            this.sourceDomain = sourceDomain;
            this.targetDomain = targetDomain;
        }

        public String getDefinitionName() {
            return linkName;
        }

        public void setDefinitionName(String linkName) {
            this.linkName = linkName;
        }
        
        public String getDefinitionType() {
            return type;
        }

        public void setDefinitionType(String type) {
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

    // Table model for link definitions
    class TableModelDefinition extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Definition_Type"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Definition_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_Source_Domain"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Target_Domain"), 
                                        };
        ArrayList <DefinitionRow> rows;
        final static int iColDefinitionType = 0;
        final static int iColLinkName = 1;
        final static int iColSourceDomain = 2;
        final static int iColTargetDomain = 3;
        
        TableModelDefinition(ArrayList rows) {
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
                DefinitionRow singleRow = rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColLinkName:
                            return singleRow.getDefinitionName();
                        case iColDefinitionType:
                            return singleRow.getDefinitionType();
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
                DefinitionRow singleRow = (DefinitionRow) rows.get(row);
                if (singleRow != null) {
                    switch (col) {
                        case iColLinkName:
                            singleRow.setDefinitionName((String) value);                            
                            break;
                        case iColDefinitionType:
                            singleRow.setDefinitionType((String) value);                            
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
        
        public void addRow(int index, DefinitionRow row) {
            rows.add(row);
            this.fireTableRowsInserted(index, index);
        }

        public DefinitionRow getRow(int index) {
            DefinitionRow row = (DefinitionRow) rows.get(index);
            return row;
        }
    }

    public void setCurrentDomainNode(DomainNode node, boolean bNew) {
        String domainName = node.getName();
        if (bNew) {
            this.mAlDomainNodes.add(node);
            this.mMapDomainNodes.put(domainName, node);
            // add a new row
            TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
            DomainRow r = new DomainRow(domainName);
            int idx = model.getRowCount();
            model.addRow(idx, r);
            model.fireTableDataChanged();
            mEditorMainApp.enableSaveAction(true);
            jTableDomains.setRowSelectionInterval(idx, idx);
            jTableDomains.setEditingRow(idx);
            onDomainSelected();
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
