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
//import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.border.Border;
//import javax.swing.border.EmptyBorder;

//import javax.swing.table.TableModel;
//import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ImageIcon;

import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.actions.SystemAction;
import org.openide.nodes.Node;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;
import org.netbeans.api.project.Project;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;

import java.io.File;

import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.DefinitionNode;
import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.project.actions.ImportDomainAction;

/**
 *
 * @author  kkao
 */
public class TabOverview extends javax.swing.JPanel implements MouseListener, MouseMotionListener, ActionListener  {
    private final String ALL_DOMAINS = org.openide.util.NbBundle.getMessage(TabOverview.class, "All_Domains");
    TabOverview mTabOverview;
    EditorMainPanel mEditorMainPanel;
    EditorMainApp mEditorMainApp;
    DefinitionNode mSelectedDefinitionNode;
    private ArrayList <DomainNode> mAlDomainNodes;
    private ArrayList <String> mAlDomainNames = new ArrayList();;
    private Map <String, DomainNode> mMapDomainNodes = new HashMap();  // domainName, DomainNode
    static final Image DOMAINIMAGE = Utilities.loadImage("com/sun/mdm/multidomain/project/resources/DomainNode.png");
    static final ImageIcon DOMAINIMAGEICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/DomainNode.png"));
    static final ImageIcon DEFINITIONIMAGEICON = new ImageIcon(Utilities.loadImage("com/sun/mdm/multidomain/project/resources/Definition.png"));
    private BufferedImage backingImage;
    private Point last;
    private JPopupMenu mDomainPopupMenu = new JPopupMenu();;
    private JPopupMenu mDefinitionPopupMenu = new JPopupMenu();;
    private MyTableHeaderRenderer mMyTableHeaderRenderer = new MyTableHeaderRenderer();
    private DefinitionTableColumnRenderer mDefinitionTableColumnRenderer = new DefinitionTableColumnRenderer();
    
    public TabOverview(EditorMainPanel editorMainPanel, EditorMainApp editorMainApp) {
        initComponents();
        mTabOverview = this;
        mEditorMainPanel = editorMainPanel;
        mEditorMainApp = editorMainApp;
        jLabelDomainName.setIcon(DOMAINIMAGEICON);
        jLabelDefinition.setIcon(DEFINITIONIMAGEICON);
        jButtonAddDomain.setIcon(EditorMainPanel.ADDDOMAINIMAGEICON);
        jButtonAddRelationship.setIcon(EditorMainPanel.RELATIONSHIPICONSMALL);
        jButtonAddHierarchy.setIcon(EditorMainPanel.HIERARCHYICONSMALL);
        jButtonRemoveDomain.setIcon(EditorMainPanel.DELETEIMAGEICON);
        jButtonRemoveDefinition.setIcon(EditorMainPanel.DELETEIMAGEICON);
        jButtonRemoveDomain.setEnabled(false);
        jButtonRemoveDefinition.setEnabled(false);
        
        jButtonAddDomain.setToolTipText(NbBundle.getMessage(TabOverview.class, "MSG_ToolTip_AddDomain"));
        jButtonAddRelationship.setToolTipText(NbBundle.getMessage(TabOverview.class, "MSG_ToolTip_AddRelationship"));
        jButtonAddHierarchy.setToolTipText(NbBundle.getMessage(TabOverview.class, "MSG_ToolTip_AddHierarchy"));
        jButtonRemoveDomain.setToolTipText(NbBundle.getMessage(TabOverview.class, "MSG_ToolTip_Delete"));
        jButtonRemoveDefinition.setToolTipText(NbBundle.getMessage(TabOverview.class, "MSG_ToolTip_Delete"));
        
        // load domains
        ArrayList rows = loadDomains();
        TableModelDomains modelDomains = new TableModelDomains(rows);
        jTableDomains.setModel(modelDomains);
        DomainNode domainNode = null;
        if (rows.size() > 0) {
            jTableDomains.setRowSelectionInterval(0, 0);
            jButtonRemoveDomain.setEnabled(true);
            mEditorMainPanel.enableDeleteButton(true);
            domainNode = mAlDomainNodes.get(0);
        }
        //configureTableColumns(jTableDomains);
        mDomainPopupMenu.add(createMenuItem(NbBundle.getMessage(TabOverview.class, "MSG_menu_Delete"), null));
        jTableDomains.addMouseListener(new DomainMouseListener());
        
        mDefinitionPopupMenu.add(createMenuItem(NbBundle.getMessage(TabOverview.class, "MSG_menu_Copy"), null));
        mDefinitionPopupMenu.add(createMenuItem(NbBundle.getMessage(TabOverview.class, "MSG_menu_Delete"), null));
        // load all definitions
        rows = loadDefinitions(false);
        // load definitions per domain
        //rows = loadDefinitionsByDomain(domainNode, false);
        TableModelDefinition modelDefinitions = new TableModelDefinition(rows);
        jTableDefinitions.setModel(modelDefinitions);
        int w = jTableDefinitions.getColumnModel().getTotalColumnWidth();
        int iw = (w * 1/24);
        jTableDefinitions.getColumnModel().getColumn(0).setPreferredWidth(iw);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        //jTableDefinitions.setRowSorter(sorter);
        jTableDefinitions.addMouseListener(new DefinitionMouseListener());
        configureTableColumns(jTableDefinitions);
        
        this.jRadioButtonShowAll.setSelected(true);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        //Drag and drop
        jTableDomains.getTableHeader().setDropTarget(new DropTarget(this, new DomainDropTarget()));
        jLabelDomainName.setDropTarget(new DropTarget(this, new DomainDropTarget()));
        jButtonAddDomain.setDropTarget(new DropTarget(this, new DomainDropTarget()));
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
                ArrayList al = domainNode.getDefinitions();
                int size = (al == null) ? 0 : al.size();
                DomainRow r = new DomainRow(domainName, Integer.toString(size));
                rows.add(r);
            }
            jTableDomains.setRowSelectionInterval(0, 0);
            mEditorMainPanel.enableAddRelationship(mAlDomainNodes.size() > 1);
            jButtonAddRelationship.setEnabled(mAlDomainNodes.size() > 1);
            jButtonAddHierarchy.setEnabled(true);
        } else {
            // make sure the properties tab is empty
            mEditorMainPanel.loadDomainProperties(null);
            mEditorMainPanel.enableAddRelationship(false);
            jButtonAddRelationship.setEnabled(false);
            jButtonAddHierarchy.setEnabled(false);
        }
        return rows;
    }
    
    private ArrayList <DefinitionRow> loadDefinitions(boolean refresh) {
        TableModelDefinition model = null;
        if (refresh) {
            model = (TableModelDefinition) jTableDefinitions.getModel();
            model.removeAll();
            jTableDefinitions.removeAll();
        }
        ArrayList <DefinitionNode> alDefinitionNodes = mEditorMainApp.getDefinitionNodes();
        ArrayList <DefinitionRow> rows = new ArrayList();
        for (int i=0; alDefinitionNodes != null && i<alDefinitionNodes.size(); i++) {
            DefinitionNode definitionNode = alDefinitionNodes.get(i);
            Definition definition = definitionNode.getDefinition();
            String sourceDomain = definition.getSourceDomain();
            String targetDomain = definition.getTargetDomain();
            DefinitionRow r = new DefinitionRow(definition.getType(), definition.getName(), sourceDomain, targetDomain);
            rows.add(r);
            if (model != null) {
                model.addRow(i, r);
            }
        }
        if (refresh) {
            model.fireTableDataChanged();
        }
        return rows;
    }
    
    private ArrayList <DefinitionRow> loadDefinitionsByDomain(DomainNode domainNode, boolean refresh) {
        TableModelDefinition model = null;
        if (refresh) {
            model = (TableModelDefinition) jTableDefinitions.getModel();
            model.removeAll();
            jTableDefinitions.removeAll();
        }
        ArrayList <DefinitionRow> rows = new ArrayList();
        if (domainNode != null) {
            ArrayList <Definition> alDefinitions = domainNode.getDefinitions();
            for (int i=0; alDefinitions != null && i<alDefinitions.size(); i++) {
                Definition definition = alDefinitions.get(i);
                String sourceDomain = definition.getSourceDomain();
                String targetDomain = definition.getTargetDomain();
                DefinitionRow r = new DefinitionRow(definition.getType(), definition.getName(), sourceDomain, targetDomain);
                rows.add(r);
                if (model != null) {
                    model.addRow(i, r);
                }
            }
        }
        if (refresh) {
            model.fireTableDataChanged();
        }
        return rows;
    }
    
    public void updateDefinitionName(String oldDefName, Definition definition) {
        String definitionName = definition.getName();
        //String definitionType = definition.getType();
        String sourceDomain = definition.getSourceDomain();
        String targetDomain = definition.getTargetDomain();
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        int length = model.getRowCount();
        for (int i=0; i < length; i++) {
            DefinitionRow row = model.getRow(i);
            if (row.getDefinitionName().equals(oldDefName) && 
                row.getSourceDomain().equals(sourceDomain) &&
                row.getTargetDomain().equals(targetDomain)) {
                row.setDefinitionName(definitionName);
                row.setSourceDomain(sourceDomain);
                row.setTargetDomain(targetDomain);
                model.fireTableDataChanged();
                jTableDefinitions.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
    /*
    public void updatePlugin(String oldPlugin, Definition definition) {
        String plugin = definition.getPlugin();
        //String definitionType = definition.getType();
        String sourceDomain = definition.getSourceDomain();
        String targetDomain = definition.getTargetDomain();
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        int length = model.getRowCount();
        for (int i=0; i < length; i++) {
            DefinitionRow row = model.getRow(i);
            if (row.getPlugin().equals(oldPlugin) && 
                row.getSourceDomain().equals(sourceDomain) &&
                row.getTargetDomain().equals(targetDomain)) {
                row.setPlugin(oldPlugin);
                row.setSourceDomain(sourceDomain);
                row.setTargetDomain(targetDomain);
                model.fireTableDataChanged();
                break;
            }
        }
    }
    */
    
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
        jButtonAddRelationship = new javax.swing.JButton();
        jButtonRemoveDefinition = new javax.swing.JButton();
        jRadioButtonShowAll = new javax.swing.JRadioButton();
        jButtonAddHierarchy = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Overview"))); // NOI18N

        jLabelDomainName.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Domains")); // NOI18N

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

        jButtonAddDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddDomain.setIconTextGap(2);
        jButtonAddDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddDomain(evt);
            }
        });

        jButtonRemoveDomain.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Remove")); // NOI18N
        jButtonRemoveDomain.setIconTextGap(2);
        jButtonRemoveDomain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveDomain(evt);
            }
        });

        jLabelDefinition.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Definitions")); // NOI18N

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

        jButtonAddRelationship.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddRelationship.setIconTextGap(2);
        jButtonAddRelationship.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddRelationship(evt);
            }
        });

        jButtonRemoveDefinition.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Remove")); // NOI18N
        jButtonRemoveDefinition.setIconTextGap(2);
        jButtonRemoveDefinition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveDefinition(evt);
            }
        });

        jRadioButtonShowAll.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Show_All_Definitions")); // NOI18N
        jRadioButtonShowAll.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onShowAllStateChanged(evt);
            }
        });

        jButtonAddHierarchy.setText(org.openide.util.NbBundle.getMessage(TabOverview.class, "LBL_Add")); // NOI18N
        jButtonAddHierarchy.setIconTextGap(2);
        jButtonAddHierarchy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddHierarchy(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                .add(layout.createSequentialGroup()
                    .add(jLabelDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonAddDomain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jButtonRemoveDomain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jLabelDomainName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(layout.createSequentialGroup()
                .add(jRadioButtonShowAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(6, 6, 6)
                .add(jButtonAddRelationship, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonAddHierarchy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonRemoveDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButtonRemoveDomain)
                            .add(jButtonAddDomain))
                        .add(25, 25, 25))
                    .add(layout.createSequentialGroup()
                        .add(jLabelDefinition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButtonShowAll)
                    .add(jButtonAddRelationship)
                    .add(jButtonAddHierarchy)
                    .add(jButtonRemoveDefinition))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void onAddDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddDomain
    Action action = SystemAction.get(ImportDomainAction.class);
    ((ImportDomainAction) action).perform(mEditorMainApp);
}//GEN-LAST:event_onAddDomain

private void onRemoveDomain(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveDomain
    onRemoveDomain();
}//GEN-LAST:event_onRemoveDomain

    private void onRemoveDomain() {                                
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
                int cnt = mEditorMainApp.deleteDefinition(domainName);
                //update jTableDomains's # of definitions
                //updateDomainDefinitionCount(row.getDomainName(), row.getDomainName(), -cnt);
                model.removeRow(idx);
            }
            // ToDo just need to update the definition cnt
            ArrayList <DomainRow> rows = loadDomains();
            model.removeAll();
            for (int i= 0; i <rows.size(); i++) {
                DomainRow r = rows.get(i);
                model.addRow(i, r);
            }
        
            loadDefinitions(true);
            // load all definitions
            if (this.jRadioButtonShowAll.isSelected()) {
                //loadDefinitions(true);
            } else {
            
            }
            if (model.getRowCount() > 0) {
                jTableDomains.setRowSelectionInterval(0, 0);
                onDomainSelected();
            } else {
                this.jButtonRemoveDomain.setEnabled(false);
                mEditorMainPanel.enableDeleteButton(false);
                mEditorMainPanel.loadDomainEntityTree(null);
            }
            mEditorMainApp.enableSaveAction(true);
        }
    }  

private void onRemoveDefinition(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveDefinition
    onRemoveDefinition();
}//GEN-LAST:event_onRemoveDefinition

    private void onRemoveDefinition() {                                    
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
                    //update jTableDomains's # of definitions
                    updateDomainDefinitionCount(row.getSourceDomain(), row.getTargetDomain(), -1);
                }
                model.fireTableDataChanged();
                // update properties tab
                if (model.getRowCount() > 0) {
                    jTableDefinitions.setRowSelectionInterval(0, 0);
                    onDefinitionSelected();
                } else {
                    mEditorMainPanel.loadDefinitionProperties(null);
                    this.jButtonRemoveDefinition.setEnabled(false);
                    mEditorMainPanel.enableDeleteButton(false);
                }
                mEditorMainApp.enableSaveAction(true);
            }
    }                                   

    public void performDeleteAction() {
        if (jTableDefinitions.getSelectedRowCount() > 0) {
            onRemoveDefinition(null);
        } else if (jTableDomains.getSelectedRowCount() > 0) {
            onRemoveDomain(null);
        }
    }
    
    // add new DefinitionNode
    private void addDefinitionNode(Definition definition) {
        DefinitionNode definitionNode = mEditorMainApp.addDefinition(definition);
        mEditorMainPanel.loadDefinitionProperties(definitionNode);
        // add a new row
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        DefinitionRow r = new DefinitionRow(definition.getType(), definition.getName(), definition.getSourceDomain(), definition.getTargetDomain());
        int idx = model.getRowCount();
        model.addRow(idx, r);
        model.fireTableDataChanged();
        jTableDefinitions.setRowSelectionInterval(idx, idx);
        onDefinitionSelected();
        mEditorMainApp.enableSaveAction(true);
        //update jTableDomains's # of definitions
        updateDomainDefinitionCount(definition.getSourceDomain(), definition.getTargetDomain(), 1);
    }
    
    private void performAddRelationship() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AddRelationshipDialog dialog = new AddRelationshipDialog(mAlDomainNames, mEditorMainApp);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (dialog.getReturnStatus() == AddDefinitionDialog.RET_OK) {
                            String definitionName = dialog.getDefinitionName();
                            String sourceDomain = dialog.getSourceDomain();
                            String targetDomain = dialog.getTargetDomain();
                            String plugin = dialog.getPlugin();
                            if (mEditorMainApp.getDefinitionNode(definitionName, sourceDomain, targetDomain) != null) {
                                //Already exists
                            } else {
                                // add new DefinitionNode
                                Definition definition = new Definition(definitionName, Definition.TYPE_RELATIONSHIP, sourceDomain, targetDomain, plugin, null);
                                addDefinitionNode(definition);
                            }
                        }
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    private void performAddHierarchy() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AddHierarchyDialog dialog = new AddHierarchyDialog(mAlDomainNames, mEditorMainApp);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        if (dialog.getReturnStatus() == AddDefinitionDialog.RET_OK) {
                            String definitionName = dialog.getDefinitionName();
                            String domain = dialog.getDomain();
                            String plugin = dialog.getPlugin();
                            if (mEditorMainApp.getDefinitionNode(definitionName, domain, domain) != null) {
                                //Already exists
                            } else {
                                // add new DefinitionNode
                                Definition definition = new Definition(definitionName, Definition.TYPE_HIERARCHY, domain, domain, plugin, null);
                                addDefinitionNode(definition);
                            }
                        }
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    //ToDo update jTableDomains's # of definitions
    private void updateDomainDefinitionCount(String sourceDomain, String targetDomain, int diff) {
        TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
        int length = model.getRowCount();
        for (int i=0; i < length; i++) {
            DomainRow row = model.getRow(i);
            if (row.getDomainName().equals(sourceDomain) || row.getDomainName().equals(targetDomain)) {
                String definitionCnt = row.getDefinitionCnt();
                int cnt = Integer.parseInt(definitionCnt) + diff;
                row.setDefinitionCnt(Integer.toString(cnt));
            }
        }
        model.fireTableDataChanged();
    }

private void onAddRelationship(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddRelationship
    performAddRelationship();
}//GEN-LAST:event_onAddRelationship

private void onShowAllStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onShowAllStateChanged
    if (this.jRadioButtonShowAll.isSelected()) {
        loadDefinitions(true);
    } else {
        if (this.jTableDomains.getSelectedRow() >= 0) {
            onDomainSelected();
        }
    }
}//GEN-LAST:event_onShowAllStateChanged

private void onAddHierarchy(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddHierarchy
    performAddHierarchy();
}//GEN-LAST:event_onAddHierarchy

    public void onAddRelationship() {
        performAddRelationship();
    }

    public void onViewScreenProperties() {
        WebScreenPropertiesDialog screenDlg = new WebScreenPropertiesDialog(mEditorMainApp.getMultiDomainWebManager(false).getPageDefinition());
        screenDlg.setVisible(true);
        if (screenDlg.isModified()) {
            mEditorMainApp.enableSaveAction(true);
        }
    }

    public void onJNDIResProperties() {
        JNDIPropertiesDialog screenDlg = new JNDIPropertiesDialog(mEditorMainApp.getMultiDomainWebManager(false).getJndiResources());
        screenDlg.setVisible(true);
        if (screenDlg.isModified()) {
            mEditorMainApp.enableSaveAction(true);
        }
    }

    public void onAddHierarchy() {
        performAddHierarchy();
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddDomain;
    private javax.swing.JButton jButtonAddHierarchy;
    private javax.swing.JButton jButtonAddRelationship;
    private javax.swing.JButton jButtonRemoveDefinition;
    private javax.swing.JButton jButtonRemoveDomain;
    private javax.swing.JLabel jLabelDefinition;
    private javax.swing.JLabel jLabelDomainName;
    private javax.swing.JRadioButton jRadioButtonShowAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDefinitions;
    private javax.swing.JTable jTableDomains;
    // End of variables declaration//GEN-END:variables

    private void onDomainSelected() {
        this.jTableDefinitions.clearSelection();
        this.jButtonRemoveDefinition.setEnabled(false);
        this.jButtonRemoveDomain.setEnabled(true);       
        int iSelectedRow = jTableDomains.getSelectedRow();
        TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
        String domainName = (String) model.getValueAt(iSelectedRow,  model.iColDomainName);
        
        DomainNode domainNode = mMapDomainNodes.get(domainName);
        
        // load definitions per domain
        if (this.jRadioButtonShowAll.isSelected()) {
            loadDefinitions(true);
        } else {
            loadDefinitionsByDomain(domainNode, true);
        }
        mEditorMainPanel.loadDomainEntityTree(domainNode);
        
        //get properties panel from domainNode and present it
        mEditorMainPanel.loadDomainProperties(domainNode);
        mEditorMainPanel.enableDeleteButton(true);
    }
    
    private void hightlightDomianInDefinition(String domainName) {
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        int length = model.getRowCount();
        for (int i=0; i < length; i++) {
            DefinitionRow row = model.getRow(i);
            if (row.getSourceDomain().equals(domainName) ||
                row.getTargetDomain().equals(domainName)) {
            }
        }
    }
    
    private void onDefinitionSelected() {
        this.jTableDomains.clearSelection();
        this.jButtonRemoveDomain.setEnabled(false);
        this.jButtonRemoveDefinition.setEnabled(true);
        int iSelectedRow = jTableDefinitions.getSelectedRow();
        TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
        String definitionType = (String) model.getValueAt(iSelectedRow,  model.iColDefinitionType);
        String definitionName = (String) model.getValueAt(iSelectedRow,  model.iColDefinitionName);
        String sourceDomain = (String) model.getValueAt(iSelectedRow,  model.iColSourceDomain);
        String targetDomain = (String) model.getValueAt(iSelectedRow,  model.iColTargetDomain);
        mSelectedDefinitionNode = mEditorMainApp.getDefinitionNode(definitionName, sourceDomain, targetDomain);
        mEditorMainPanel.loadDefinitionProperties(mSelectedDefinitionNode);
        mEditorMainPanel.loadDomainEntityTree(null);
        mEditorMainPanel.enableDeleteButton(true);
     }

    class DomainRow {
        private String domainName;
        private String definitionCnt;

        public DomainRow(String domainName, String definitionCnt) {
            this.domainName = domainName;
            this.definitionCnt = definitionCnt;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getDefinitionCnt() {
            return definitionCnt;
        }

        public void setDefinitionCnt(String definitionCnt) {
            this.definitionCnt = definitionCnt;
        }
    }
    
    // Table model for link definitions
    class TableModelDomains extends AbstractTableModel {
        private	String columnNames [] = {NbBundle.getMessage(TabOverview.class, "LBL_Domain_Name"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Number_Of_Definitions"), 
                                        };
        ArrayList <DomainRow> rows;
        final static int iColDomainName = 0;
        final static int iColDefinitionCnt = 1;
        
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
                        case iColDefinitionCnt:
                            return singleRow.getDefinitionCnt();
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
                        case iColDefinitionCnt:
                            singleRow.setDefinitionCnt((String) value);                            
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
        
        public void removeAll() {
            rows.clear();
        }
    }
    
    class DefinitionRow {
        private String definitionName;
        private String type;
        private String sourceDomain;
        private String targetDomain;

        public DefinitionRow(String type, String definitionName, String sourceDomain, String targetDomain) {
            this.definitionName = definitionName;
            this.type = type;
            this.sourceDomain = sourceDomain;
            this.targetDomain = targetDomain;
        }

        public String getDefinitionName() {
            return definitionName;
        }

        public void setDefinitionName(String definitionName) {
            this.definitionName = definitionName;
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
        private	String columnNames [] = {" ",//NbBundle.getMessage(TabOverview.class, "LBL_Definition_Icon"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Definition_Type"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Definition_Name"),
                                         NbBundle.getMessage(TabOverview.class, "LBL_Source_Domain"), 
                                         NbBundle.getMessage(TabOverview.class, "LBL_Target_Domain"), 
                                        };
        ArrayList <DefinitionRow> rows;
        final static int iColIcon = 0;
        final static int iColDefinitionType = 1;
        final static int iColDefinitionName = 2;
        final static int iColSourceDomain = 3;
        final static int iColTargetDomain = 4;
        
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
                        case iColDefinitionName:
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
                        case iColDefinitionName:
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
        
        public void removeAll() {
            rows.clear();
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
        if (bNew || !this.mAlDomainNames.contains(domainName)) {
            this.mAlDomainNodes.add(node);
            this.mMapDomainNodes.put(domainName, node);
            this.mAlDomainNames.add(domainName);

            // add a new row
            TableModelDomains model = (TableModelDomains) jTableDomains.getModel();
            ArrayList al = node.getDefinitions();
            int size = (al == null) ? 0 : al.size();
            DomainRow r = new DomainRow(domainName, Integer.toString(size));
            int idx = model.getRowCount();
            model.addRow(idx, r);
            model.fireTableDataChanged();
            mEditorMainApp.enableSaveAction(true);
            jTableDomains.setRowSelectionInterval(idx, idx);
            //jTableDomains.setEditingRow(idx);
            onDomainSelected();
            mEditorMainPanel.enableAddRelationship(mAlDomainNodes.size() > 1);
            jButtonAddRelationship.setEnabled(mAlDomainNodes.size() > 1);
            jButtonAddHierarchy.setEnabled(true);
        } else {
            // find it and set it
            //jTableDomains.setRowSelectionInterval(idx, idx);
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
    
    class DomainDropTarget implements DropTargetListener {
        public DomainDropTarget() {
        }
        
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
    }      
    
    class DomainMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                onDomainSelected();
            } else {
                if (jTableDomains.getSelectedRowCount() > 0) {
                    maybeShowPopup(evt);
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent evt) {
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            //maybeShowPopup(evt);
        }

        private void maybeShowPopup(MouseEvent evt) {
            //if (evt.isPopupTrigger()) {
            if (evt.getButton() != MouseEvent.BUTTON1) {
                mDomainPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }
    
    class DefinitionMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                onDefinitionSelected();
            } else {
                if (jTableDefinitions.getSelectedRowCount() > 0) {
                    maybeShowPopup(evt);
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent evt) {
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            //maybeShowPopup(evt);
        }

        private void maybeShowPopup(MouseEvent evt) {
            //if (evt.isPopupTrigger()) {
            if (evt.getButton() != MouseEvent.BUTTON1) {
                mDefinitionPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }
    
    /** Create context sensitive menu
     *
     *@param commandName menu item name
     */
    private JMenuItem createMenuItem(String commandName, ImageIcon defaultIcon) {
        JMenuItem item = new JMenuItem(commandName);
        item.addActionListener(this);
        item.setActionCommand(commandName);
        if (defaultIcon != null) {
            item.setIcon(defaultIcon);
        }
        return item;
    }
    
    /** ActionListener
     *  Invoked when an action occurs.
     *@param e ActionEvent from menu
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem item = (JMenuItem) e.getSource();
            String commandName = item.getText();
            if (commandName.equals(NbBundle.getMessage(TabOverview.class, "MSG_menu_Copy"))) {
                // copy selected definitions
                TableModelDefinition model = (TableModelDefinition) jTableDefinitions.getModel();
                int rs[] = jTableDefinitions.getSelectedRows();
                int length = rs.length;
                for (int i=0; i < length; i++) {
                    int iSelectedRow = rs[i];
                    DefinitionRow row = model.getRow(iSelectedRow);
                    //String definitionType = (String) model.getValueAt(iSelectedRow,  model.iColDefinitionType);
                    String definitionName = (String) model.getValueAt(iSelectedRow,  model.iColDefinitionName);
                    String sourceDomain = (String) model.getValueAt(iSelectedRow,  model.iColSourceDomain);
                    String targetDomain = (String) model.getValueAt(iSelectedRow,  model.iColTargetDomain);
                    mSelectedDefinitionNode = mEditorMainApp.copyDefinitionNode(definitionName, sourceDomain, targetDomain);
                    Definition definition = mSelectedDefinitionNode.getDefinition();
                    DefinitionRow r = new DefinitionRow(definition.getType(), definition.getName(), definition.getSourceDomain(), definition.getTargetDomain());
                    int idx = model.getRowCount();
                    model.addRow(idx, r);
                    model.fireTableDataChanged();
                    jTableDefinitions.setRowSelectionInterval(idx, idx);
                    onDefinitionSelected();
                    mEditorMainApp.enableSaveAction(true);
                    //update jTableDomains's # of definitions
                    updateDomainDefinitionCount(sourceDomain, targetDomain, 1);
                    
                    //mEditorMainPanel.loadDefinitionProperties(mSelectedDefinitionNode);
                    //mEditorMainPanel.loadDomainEntityTree(null);
                }
            } else if (commandName.equals(NbBundle.getMessage(TabOverview.class, "MSG_menu_Delete"))) {
                if (jTableDomains.getSelectedRowCount() > 0) {
                    onRemoveDomain(e);
                } else if (jTableDefinitions.getSelectedRowCount() > 0) {
                    onRemoveDefinition(e);
                }
            }
        }
    }
    
    protected void configureTableColumns(JTable table) {
        int cnt = table.getColumnCount();
        //for (int i = 0; i < cnt; i++) {
            TableColumn col = table.getColumnModel().getColumn(0);
            //col.setHeaderRenderer(mMyTableHeaderRenderer);
            col.setCellRenderer(mDefinitionTableColumnRenderer);
        //}
    }
    class MyTableHeaderRenderer extends DefaultTableCellRenderer {
        // This method is called each time a column header
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int colIndex) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    //setForeground(header.getForeground());
                    //setBackground(header.getBackground());
                    //setFont(header.getFont());
                    //setBorder(header.getBorder());
                }
            }
            //setHorizontalAlignment(JLabel.CENTER);
            return this;
        }

        protected int getColumnIndex(int column) {
            return column;
        }
    }
    
    class DefinitionTableColumnRenderer extends DefaultTableCellRenderer {
        // This method is called each time a column header
        // using this renderer needs to be rendered.
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int colIndex) {
            if (table != null) {
                if (table.getModel() instanceof TableModelDefinition) {
                    TableModelDefinition model = (TableModelDefinition) table.getModel();
                    DefinitionRow row = model.getRow(rowIndex);
                    if (colIndex == TableModelDefinition.iColIcon) {
                        switch (colIndex) {
                            case TableModelDefinition.iColIcon:
                                setBackground(new Color(255,255,255));
                                if (row.getDefinitionType().equals(Definition.TYPE_RELATIONSHIP)) {
                                    setIcon(mEditorMainPanel.RELATIONSHIPICONSMALL);
                                } else if (row.getDefinitionType().equals(Definition.TYPE_HIERARCHY)) {
                                    setIcon(mEditorMainPanel.HIERARCHYICONSMALL);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            return this;
        }

        protected int getColumnIndex(int column) {
            return column;
        }
    }
}
