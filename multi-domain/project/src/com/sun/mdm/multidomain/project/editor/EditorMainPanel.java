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
import org.openide.util.Utilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JTabbedPane;
import javax.swing.Action;
import javax.swing.JPanel;

import org.openide.util.actions.SystemAction;
import org.openide.filesystems.FileObject;
import java.io.File;
import java.io.IOException;

import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.actions.ImportDomainAction;
import com.sun.mdm.multidomain.project.actions.CreateRelationshipAction;
import com.sun.mdm.multidomain.project.actions.CreateHierarchyAction;
import com.sun.mdm.multidomain.project.actions.CreateGroupAction;
import com.sun.mdm.multidomain.project.actions.CreateCategoryAction;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.util.Logger;

/** The main panel for Multi-Domain MDM Configuration Editor.
 *
 */
public class EditorMainPanel extends JPanel implements ActionListener  {
    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            EditorMainPanel.class.getName()
        
        );

    static final ImageIcon DOMAINIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/MultiDomainFolderNode.png"));
    static final ImageIcon RELATIONSHIPNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/RelationshipNode.png"));
    static final ImageIcon HIERARCHYNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/HierarchyNode.png"));
    static final ImageIcon GROUPNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/GroupNode.png"));
    static final ImageIcon CATEGORYNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/CategoryNode.png"));

    static final ImageIcon DELETENODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Remove.png"));
    static final ImageIcon SAVEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Save.png"));
    static final String TAB_OVERVIEW = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_OVERVIEW");
    static final String TAB_OBJECT_MODEL = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_MD_OBJECT_MODEL");
    static final String TAB_WEB_MANAGER = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_WEB_MANAGER");

    private JPopupMenu mMenu;
    private JButton mButtonAddDomain;
    private JButton mButtonAddRelationship;
    private JButton mButtonAddHierarchy;
    private JButton mButtonAddGroup;
    private JButton mButtonAddCategory;
    private JButton mButtonDelete;
    private JButton mButtonSave;
    private EditorMainApp mEditorMainApp;
    private EditorMainPanel mEditorMainPanel;
    private MultiDomainApplication mMultiDomainApplication;
    private JScrollPane mMultiViewPane;
    private RelationshipCanvas canvas = null; //The component the user draws on
    private final PropertiesModelPanel propertiesModelPanel = new PropertiesModelPanel(true);
    private TabRelationshipWebManager webManagerPanel = null;
    private TabOverview mTabOverview = null;
    
    /**
     * Create the panel and set up some basic properties.
     *
     */
    public EditorMainPanel(EditorMainApp editorMainApp, MultiDomainApplication application) {
        mEditorMainApp = editorMainApp;
        mMultiDomainApplication = application;

        canvas = new RelationshipCanvas(mEditorMainApp);
        mEditorMainPanel = this;

        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        setName(NbBundle.getMessage(EditorMainPanel.class,
                "TITLE_DefineRelationships"));

        this.add(createToolBar(), BorderLayout.PAGE_START);
        // ToDo load domains etc
        // Read MultiDomainModel
        // Populate Model structures
        // Populate canvas with DomainNodes (EditorMainApp has a map for DomainNodes)
        JSplitPane splitPane = createSplitPane(); // Put tree and table in a split pane splitPane

        this.add(splitPane, BorderLayout.CENTER);
    }

    
    private void addListeners() {
    }
    
    private JSplitPane createSplitPane() {
        // Put tree and table in a split pane splitPane
        mMultiViewPane = new JScrollPane();
        mMultiViewPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "LBL_MultiDomain_Model")));

        if (webManagerPanel == null) {
            webManagerPanel = new TabRelationshipWebManager(mEditorMainApp, mMultiDomainApplication.getRelationshipWebMAnager(true));
        }
        
        mMultiViewPane.setViewportView(canvas);
        //ToDo
        //setCurrentDomainNode
        //this.mEditorMainApp.getDomainNode(null).getTabListRelationshipTypes();
        //DomainNode node = this.mEditorMainApp.getDomainNode(null);
        //TabListRelationshipTypes tab = new TabListRelationshipTypes(node.getName(), null);
        //if (node != null) {
        //    tab = node.getTabListRelationshipTypes();
        //}
        
        mTabOverview = new TabOverview(mEditorMainApp.getDomainNodes());
        JTabbedPane propertiesTabbedPane = new JTabbedPane();
        propertiesTabbedPane.add(TAB_OVERVIEW, mTabOverview);
        propertiesTabbedPane.add(TAB_WEB_MANAGER, webManagerPanel);
        
        JScrollPane propertiesPane = new JScrollPane();
        propertiesPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "MSG_Properties")));
        propertiesPane.setViewportView(propertiesTabbedPane);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mMultiViewPane, propertiesPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(250, 100);
        mMultiViewPane.setMinimumSize(minimumSize);
        
        return splitPane;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(null);
        toolBar.setFocusCycleRoot(true);
        //separator for object buttons
        mButtonAddDomain = new JButton(new AddDomainAction(
                    this.DOMAINIMAGEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddDomain")));
        mButtonAddDomain.setBorder(null);
        mButtonAddDomain.setMnemonic('A');
        toolBar.add(mButtonAddDomain);
        mButtonAddRelationship = new JButton(new AddRelationshipAction(
                    this.RELATIONSHIPNODEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddRelationship")));
        mButtonAddRelationship.setBorder(null);
        mButtonAddRelationship.setMnemonic('R');
        toolBar.add(mButtonAddRelationship);
        mButtonAddHierarchy = new JButton(new AddHierarchyAction(
                    this.HIERARCHYNODEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddHierarchy")));
        mButtonAddHierarchy.setBorder(null);
        mButtonAddHierarchy.setMnemonic('H');
        toolBar.add(mButtonAddHierarchy);
        
        mButtonAddGroup = new JButton(new AddGroupAction(
                    this.GROUPNODEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddGroup")));
        mButtonAddGroup.setBorder(null);
        mButtonAddGroup.setMnemonic('G');
        toolBar.add(mButtonAddGroup);
        mButtonAddCategory = new JButton(new AddCategoryAction(
                    this.CATEGORYNODEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddCategory")));
        mButtonAddCategory.setBorder(null);
        mButtonAddCategory.setMnemonic('H');
        toolBar.add(mButtonAddCategory);
        toolBar.addSeparator();
        mButtonSave = new JButton(new SaveAction(this.SAVEIMAGEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_Save")));
        mButtonSave.setBorder(null);
        mButtonSave.setMnemonic('S');
        mButtonSave.setEnabled(true);   // ToDo to false
        toolBar.add(mButtonSave);

        return toolBar;
    }
    /** Create context sensitive menu
     *
     *@param commandName menu item name
     */
    private JMenuItem createMenuItem(String commandName, ImageIcon defaultIcon) {
        JMenuItem item = new JMenuItem(commandName);
        item.addActionListener(this);
        item.setActionCommand(commandName);
        item.setIcon(defaultIcon);

        return item;
    }

    /** Create context sensitive menu
     *
     *@param node for proper menu per node type
     */
    protected void createMenu() {
        mMenu = new JPopupMenu();

        String commandName = null;
        mButtonDelete.setEnabled(false);
    }

    /** Expand the tree
     *@param tree Entity tree
     *@param parent current root
     *@param expand expand or collaps
     */
    public void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);

                if (expand) {
                    expandAll(tree, path, expand);
                }
            }
        }

        // Expansion or collapse must be done bottom-up
        //if (expand) {
        tree.expandPath(parent);

        //} else {
        //    tree.collapsePath(parent);
        //}
    }

    /** ActionListener
     *  Invoked when an action occurs.
     *@param e ActionEvent from menu
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) e.getSource();
                String commandName = item.getText();

                if (commandName.equals(NbBundle.getMessage(
                                EditorMainPanel.class, "MSG_menu_Delete"))) {
                }
            }
        } catch (Exception ev) {
            ev.printStackTrace();
        }
    }
   
    /* Enable save button when configuration changed
     * 
     */
    public void enableSaveButton(boolean flag) {
        mButtonSave.setEnabled(flag);
    }
    
    public void loadDomainNodesToCanvas() {
        ArrayList <DomainNode> al = mEditorMainApp.getDomainNodes();
        for (int i=0; i<al.size(); i++) {
            addDomainNodeToCanvas(al.get(i), i);
        }
    }
    
    /** Add a Domain Node to the canvas
     *
     */
    public boolean addDomainNodeToCanvas(DomainNode node, int index) {
        boolean added = false;
        int cnt = index;
        if (cnt < 0) {
            cnt = this.getComponentCount() + 1;
        }
        JLabel label = new JLabel(node.getName());
        canvas.add(label);
        label.setBounds(10, cnt * 30, 80, 20);
        //mMultiViewPane.setViewportView(canvas);
        //populate properties
        return added;
    }
     
    /** Add a Domain action
     *
     */
    public class AddDomainAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public AddDomainAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            Action action = SystemAction.get(ImportDomainAction.class);
            ((ImportDomainAction) action).perform(mEditorMainApp);
            action = null;
        }
    }
     
    /** Add a Relationship
     *
     */
    public class AddRelationshipAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public AddRelationshipAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            Action action = SystemAction.get(CreateRelationshipAction.class);
            ((CreateRelationshipAction) action).perform(mEditorMainApp);
            action = null;
        }
    }
     
    /** Add a Hierarchy
     *
     */
    public class AddHierarchyAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public AddHierarchyAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            Action action = SystemAction.get(CreateHierarchyAction.class);
            ((CreateHierarchyAction) action).perform(mEditorMainApp);
            action = null;
        }
    }
    
    /** Add a Hierarchy
     *
     */
    public class AddGroupAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public AddGroupAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            Action action = SystemAction.get(CreateGroupAction.class);
            ((CreateGroupAction) action).perform(mEditorMainApp);
            action = null;
        }
    }
    
    /** Add a Category
     *
     */
    public class AddCategoryAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public AddCategoryAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            Action action = SystemAction.get(CreateCategoryAction.class);
            ((CreateCategoryAction) action).perform(mEditorMainApp);
            action = null;
        }
    }

    /** Save configuration
     *
     */
    public class SaveAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public SaveAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mEditorMainApp.save(false);
        }
    }
    

    /** Bring up TabApplicationProperties
     *
     */
    public class TabApplicationPropertiesAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public TabApplicationPropertiesAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {

        }
    }
}
