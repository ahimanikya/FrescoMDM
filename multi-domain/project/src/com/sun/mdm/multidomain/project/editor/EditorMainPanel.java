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
import org.openide.util.actions.SystemAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JTabbedPane;
import javax.swing.Action;
import javax.swing.JPanel;

import com.sun.mdm.multidomain.project.MultiDomainApplication;
import com.sun.mdm.multidomain.project.actions.ImportDomainAction;
import com.sun.mdm.multidomain.project.actions.CreateRelationshipAction;
import com.sun.mdm.multidomain.project.actions.CreateHierarchyAction;
import com.sun.mdm.multidomain.project.actions.CreateGroupAction;
import com.sun.mdm.multidomain.project.actions.CreateCategoryAction;
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.editor.nodes.DefinitionNode;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.parser.Definition;

/** The main panel for Multi-Domain MDM Configuration Editor.
 *
 */
public class EditorMainPanel extends JPanel implements ActionListener  {
    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            EditorMainPanel.class.getName()
        
        );

    static final ImageIcon DOMAINIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/DomainNode.png"));
    static final ImageIcon ADDDOMAINIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/DomainNode.png"));
    static final ImageIcon ADDRELATIONSHIPICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/RelationshipNode.png"));
    static final ImageIcon ADDHIERARCHYICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/HierarchyNode.png"));
    static final ImageIcon RELATIONSHIPICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/RelationshipNode.png"));
    static final ImageIcon HIERARCHYICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/HierarchyNode.png"));
    static final ImageIcon SCREENPROPERTIESICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/screen.png"));    
    static final ImageIcon GROUPNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/GroupNode.png"));
    static final ImageIcon CATEGORYNODEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/CategoryNode.png"));

    static final ImageIcon DELETEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Remove.png"));
    static final ImageIcon SAVEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Save.png"));
    static final ImageIcon VALIDATIONIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Validation.png"));
    
    
    static final String TAB_OVERVIEW = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_OVERVIEW");
    static final String TAB_RELATIONSHIP = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_RELATIONSHIP");
    static final String TAB_HIERARCHY = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_HIERARCHY");
    static final String TAB_GROUP = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_GROUP");
    static final String TAB_CATEGORY = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_CATEGORY");
    static final String TAB_WEB_MANAGER_Domain_VIEW = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_WEB_MANAGER_Domain_VIEW");
    static final String TAB_WEB_MANAGER_Domain_SEARCH = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_WEB_MANAGER_Domain_SEARCH");
    static final String TAB_WEB_MANAGER_PAGE_DEFINITIONS = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_WEB_MANAGER_PAGE_DEFINITIONS");
    


    private JPopupMenu mMenu;
    private JButton mButtonAddDomain;
    private JButton mButtonAddRelationship;
    private JButton mButtonScreenProperties;
    private JButton mButtonAddHierarchy;
    //private JButton mButtonAddGroup;
    //private JButton mButtonAddCategory;
    private JButton mButtonDelete;
    private JButton mButtonSave;
    private JButton mButtonValidation;
    private EditorMainApp mEditorMainApp;
    private EditorMainPanel mEditorMainPanel;
    private MultiDomainApplication mMultiDomainApplication;
    private JScrollPane mMultiViewPane;
    private JTabbedPane mPropertiesTabbedPane = new JTabbedPane();
    private JScrollPane mPropertiesScrollPane = new JScrollPane();
    private JSplitPane mLeftSplitPane;
    private JScrollPane mEntityTreeScrollPane  = new JScrollPane();
    private JScrollPane mOverviewScrollPane  = new JScrollPane();
    //private RelationshipCanvas canvas = null; //The component the user draws on
    private TabOverview mTabOverview = null;
    private EntityTree mEntityTree = null;
    
    /**
     * Create the panel and set up some basic properties.
     *
     */
    public EditorMainPanel(EditorMainApp editorMainApp, MultiDomainApplication application) {
        mEditorMainApp = editorMainApp;
        mMultiDomainApplication = application;

        //canvas = new RelationshipCanvas(mEditorMainApp);
        mEditorMainPanel = this;

        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        setName(NbBundle.getMessage(EditorMainPanel.class,
                "TITLE_DefineRelationships"));

        this.add(createToolBar(), BorderLayout.PAGE_START);
        JSplitPane splitPane = createSplitPane(); // Put tree and table in a split pane splitPane
        this.add(splitPane, BorderLayout.CENTER);
    }
    
    private void addListeners() {
    }
    
    public void loadDomainEntityTree(DomainNode currentDomainNode) {
        if (currentDomainNode != null) {
            mEntityTree = currentDomainNode.getMainEntityTree();
            mEntityTreeScrollPane.setViewportView(mEntityTree);
            mLeftSplitPane.setBottomComponent(mEntityTreeScrollPane);
        } else {
            mLeftSplitPane.setBottomComponent(null);
        }
        mLeftSplitPane.setDividerLocation(380);
        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(400, 380);
        //mMultiViewPane.setMinimumSize(minimumSize);
        mLeftSplitPane.setMinimumSize(minimumSize);
    }
    
    public void loadDomainProperties(DomainNode currentDomainNode) {
        mPropertiesTabbedPane.removeAll();
        if (currentDomainNode != null) {
            mPropertiesTabbedPane.add(TAB_WEB_MANAGER_Domain_VIEW, currentDomainNode.getDomainViewTab(true));
            mPropertiesTabbedPane.add(TAB_WEB_MANAGER_Domain_SEARCH, currentDomainNode.getDoaminsTab(true));
        }
        mPropertiesScrollPane.setViewportView(mPropertiesTabbedPane);
    }
    
    public void loadDefinitionProperties(DefinitionNode currentDefinitionNode) {
        mPropertiesTabbedPane.removeAll();
        if (currentDefinitionNode != null) {
            String title = "Unknown";
            if (currentDefinitionNode.getType().equals(Definition.TYPE_RELATIONSHIP)) {
                title = TAB_RELATIONSHIP;
            } else if (currentDefinitionNode.getType().equals(Definition.TYPE_HIERARCHY)) {
                title = TAB_HIERARCHY;
            } else if (currentDefinitionNode.getType().equals(Definition.TYPE_GROUP)) {
                title = TAB_GROUP;
            } else if (currentDefinitionNode.getType().equals(Definition.TYPE_CATEGORY)) {
                title = TAB_CATEGORY;
            }
            mPropertiesTabbedPane.add(title, currentDefinitionNode.getDefinitionTab(true));
            mPropertiesTabbedPane.add(TAB_WEB_MANAGER_PAGE_DEFINITIONS, currentDefinitionNode.getWebManagerDefinitionTab(true));
        }
        mPropertiesScrollPane.setViewportView(mPropertiesTabbedPane);
    }
    
    private JSplitPane createSplitPane() {
        // Put tree and table in a split pane splitPane
        mMultiViewPane = new JScrollPane();
        mMultiViewPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "LBL_MultiDomain_Model")));
               
        mPropertiesScrollPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "MSG_Properties")));
        
        ArrayList <DomainNode> alDomainNodes = mEditorMainApp.getDomainNodes();
        DomainNode currentDomainNode = null; // use this to load web tabs for domain
        if (alDomainNodes != null && alDomainNodes.size() > 0) {
            currentDomainNode = alDomainNodes.get(0);
            mEntityTree = currentDomainNode.getMainEntityTree();
            mEntityTreeScrollPane.setViewportView(mEntityTree);
            loadDomainProperties(currentDomainNode);
        }

        mTabOverview = new TabOverview(this, mEditorMainApp);
        mOverviewScrollPane.setViewportView(mTabOverview);
        mLeftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                mOverviewScrollPane, mEntityTreeScrollPane);
        mLeftSplitPane.setOneTouchExpandable(true);
        mLeftSplitPane.setDividerLocation(380);
        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(400, 380);
        //mMultiViewPane.setMinimumSize(minimumSize);
        mLeftSplitPane.setMinimumSize(minimumSize);
        mMultiViewPane.setViewportView(mLeftSplitPane);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mLeftSplitPane, mPropertiesScrollPane);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(400);
        
        return mainSplitPane;
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
        mButtonAddDomain.setMnemonic('M');
        toolBar.add(mButtonAddDomain);
        mButtonAddRelationship = new JButton(new AddRelationshipAction(
                    this.RELATIONSHIPICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddRelationship")));
        mButtonAddRelationship.setBorder(null);
        mButtonAddRelationship.setMnemonic('R');
        toolBar.add(mButtonAddRelationship);
        mButtonAddHierarchy = new JButton(new AddHierarchyAction(
                    this.HIERARCHYICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_AddHierarchy")));
        mButtonAddHierarchy.setBorder(null);
        mButtonAddHierarchy.setMnemonic('H');
        toolBar.add(mButtonAddHierarchy);

        mButtonScreenProperties = new JButton(new ViewScreenPropertiesAction(
                    this.SCREENPROPERTIESICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_ScreenProperties")));
        mButtonScreenProperties.setBorder(null);
        mButtonScreenProperties.setMnemonic('P');
        toolBar.add(mButtonScreenProperties);
        /*
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
         */
        toolBar.addSeparator();
        mButtonValidation = new JButton(new ValidateAction(this.VALIDATIONIMAGEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_Validation")));
        mButtonValidation.setBorder(null);
        mButtonValidation.setMnemonic('V');
        mButtonValidation.setEnabled(true);
        toolBar.add(mButtonValidation);
        
        mButtonDelete = new JButton(new DeleteAction(this.DELETEIMAGEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_Delete")));
        mButtonDelete.setBorder(null);
        mButtonDelete.setMnemonic('D');
        mButtonDelete.setEnabled(false);
        toolBar.add(mButtonDelete);
        
        mButtonSave = new JButton(new SaveAction(this.SAVEIMAGEICON,
                    NbBundle.getMessage(EditorMainPanel.class,
                        "MSG_ToolTip_Save")));
        mButtonSave.setBorder(null);
        mButtonSave.setMnemonic('S');
        mButtonSave.setEnabled(false);   // ToDo to false
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
   
    /* Enable delete button when domain or definition selected
     * 
     */
    public void enableDeleteButton(boolean flag) {
        mButtonDelete.setEnabled(flag);
    }
   
    /* Enable save button when configuration changed
     * 
     */
    public void enableSaveButton(boolean flag) {
        mButtonSave.setEnabled(flag);
    }
    
    public void loadDomainNodesToOverview() {
        ArrayList <DomainNode> al = mEditorMainApp.getDomainNodes();
        for (int i=0; i<al.size(); i++) {
            addDomainNodeToOverview(al.get(i), false);
        }
    }
    
    /** Add a Domain Node to the overview
     *
     */
    public void addDomainNodeToOverview(DomainNode node, boolean bNew) {
        //mMultiViewPane.setViewportView(canvas);
        mTabOverview.setCurrentDomainNode(node, bNew);
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
     
    /**
     * To view screen properties
     */
    public class ViewScreenPropertiesAction extends AbstractAction {
        
        public ViewScreenPropertiesAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }
        
        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {           
            mTabOverview.onViewScreenProperties();
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
            mTabOverview.onAddRelationship();
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
            mTabOverview.onAddHierarchy();
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
    public class ValidateAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public ValidateAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            //mEditorMainApp.validate();
        }
    }
    
    /** Delete domain or definition
     *
     */
    public class DeleteAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public DeleteAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mTabOverview.performDeleteAction();
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
    
    public TabOverview getTabOverview() {
        return mTabOverview;
    }
}
