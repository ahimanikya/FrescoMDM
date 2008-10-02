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
import com.sun.mdm.multidomain.project.editor.nodes.DomainNode;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
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
    static final ImageIcon DELETENODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Remove.png"));
    static final ImageIcon SAVEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Save.png"));
    
    static final String TAB_OBJECT_MODEL = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_MD_OBJECT_MODEL");
    static final String TAB_WEB_MANAGER = NbBundle.getMessage(EditorMainPanel.class,
            "MSG_TAB_WEB_MANAGER");


    private JSplitPane mSplitPane = null;
    private JScrollPane multiViewPane;
    private JScrollPane propertiesPane = new JScrollPane();
    private JPopupMenu mMenu;
    private JLabel jLabelNoProperties;
    private JButton mButtonAddDomain;
    private JButton mButtonDelete;
    private JButton mButtonSave;
    private EditorMainApp mEditorMainApp;
    private EditorMainPanel mEditorMainPanel;
    private MultiDomainApplication mMultiDomainApplication;
    private RelationshipCanvas canvas = new RelationshipCanvas(); //The component the user draws on
    //private JPanel canvas = new JPanel();
    private final PropertiesModelPanel propertiesModelPanel = new PropertiesModelPanel(true);
    private TabRelationshipWebManager webManagerPanel = null;
    JTabbedPane propertiesTabbedPane = new JTabbedPane();
    
    /**
     * Create the panel and set up some basic properties.
     *
     */
    public EditorMainPanel(EditorMainApp editorMainApp, MultiDomainApplication application) {
        mEditorMainApp = editorMainApp;
        mMultiDomainApplication = application;

        if (webManagerPanel == null) {
            webManagerPanel = new TabRelationshipWebManager(editorMainApp, mMultiDomainApplication.getRelationshipWebMAnager(true));
        }

        mEditorMainPanel = this;


        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        jLabelNoProperties = new javax.swing.JLabel();
        jLabelNoProperties.setText(NbBundle.getMessage(
                EditorMainPanel.class, "MSG_Property_NoProperties"));
        jLabelNoProperties.setEnabled(false);
        setName(NbBundle.getMessage(EditorMainPanel.class,
                "TITLE_DefineRelationships"));

        this.add(createToolBar(), BorderLayout.PAGE_START);
        // ToDo load domains etc
        // Read RelationshipModel
        // Populate Model structures
        // Populate canvas with DomainNodes (EditorMainApp has a map for DomainNodes)
        createSplitPane(); // Put tree and table in a split pane mSplitPane

        this.add(mSplitPane, BorderLayout.CENTER);
    }

    
    private void addListeners() {
    }
    
    private void createSplitPane() {
        // Put tree and table in a split pane mSplitPane
        multiViewPane = new JScrollPane();
        multiViewPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "LBL_Relationship_Model")));
        //propertiesPane.setViewportView(jLabelNoProperties);
        propertiesPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "MSG_Properties")));

        multiViewPane.setViewportView(canvas);
        //ToDo
        //setCurrentDomainNode
        //this.mEditorMainApp.getDomainNode(null).getTabListRelationshipTypes();
        DomainNode node = this.mEditorMainApp.getDomainNode(null);
        TabListRelationshipTypes tab = new TabListRelationshipTypes(null);
        if (node != null) {
            tab = node.getTabListRelationshipTypes();
        }
        propertiesTabbedPane.add(TAB_OBJECT_MODEL, tab);
        //Wee add web properties tab here
        propertiesTabbedPane.add(TAB_WEB_MANAGER, webManagerPanel);
        
        propertiesPane.setViewportView(propertiesTabbedPane);
        
        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                multiViewPane, propertiesPane);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(250);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(250, 100);
        multiViewPane.setMinimumSize(minimumSize);
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
        mButtonAddDomain.setMnemonic('P');
        toolBar.add(mButtonAddDomain);
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
    
    /** Add a Domain Node to the canvas
     *
     */
    public boolean addDomainNode(DomainNode node) {
        boolean added = false;
        
        int cnt = this.getComponentCount() + 1;
        JLabel label = new JLabel(node.getName());
        canvas.add(new JLabel(node.getName()), new java.awt.GridBagConstraints());
        label.setBounds(90, cnt * 30, 80, 20);

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
