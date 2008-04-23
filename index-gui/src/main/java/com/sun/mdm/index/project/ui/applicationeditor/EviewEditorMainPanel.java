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
package com.sun.mdm.index.project.ui.applicationeditor;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.mdm.index.project.EviewApplication;

/** The main panel for Master Index Configuration Editor.
 *
 */
public class EviewEditorMainPanel extends javax.swing.JPanel implements TreeSelectionListener, ActionListener  {
    private EntityTree mEntityTree = null;
    private boolean mIgnoreSelection = false;    
    final JTextField mText = new JTextField();
    EntityNode mPreviousSelectedNode = null;
    EntityNode mCurrentSelectedNode = null;
    String mOldPrimaryObjName = null;
    String mOldText = null;

    static final ImageIcon ROOTIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Root.gif"));
    static final ImageIcon PRIMARYNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/PrimaryObject.png"));
    static final ImageIcon SUBNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/SubObject.png"));
    static final ImageIcon FIELDNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Field.png"));
    static final ImageIcon DELETENODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Remove.png"));
    static final ImageIcon SAVEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Save.png"));
    static final ImageIcon TEMPLATESIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplateDropdown.png"));
    static final ImageIcon TEMPLATECOMPANYIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplateCompany.png"));
    static final ImageIcon TEMPLATEPERSONIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplatePerson.png"));
    static final ImageIcon TABAPPLICATIONPROPERTIESIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/edit_properties.png"));

    private JSplitPane mSplitPane = null;
    private JScrollPane entityTreePane;
    private JScrollPane entityPropertyPane = new JScrollPane();
    private EntityNode mRootNode;
    private JPopupMenu mMenu;
    private JPopupMenu mTemplatesMenu;
    private JPopupMenu mSubTemplatesMenu;
    private JLabel jLabelNoProperties;
    private JButton mButtonAddPrimary;
    private JButton mButtonAddSub;
    private JButton mButtonAddField;
    private JButton mButtonTemplates;
    private JButton mButtonTemplateCompany;
    private JButton mButtonTemplatePerson;
    private JButton mButtonDelete;
    private JButton mButtonSave;
    private JButton mButtonTabApplicationProperties;
    private EviewEditorMainApp mEviewEditorMainApp;
    private EviewApplication mEviewApplication;
    private boolean bCheckedOut;
    
    /**
     * Create the panel and set up some basic properties.
     *
     */
    public EviewEditorMainPanel(EviewEditorMainApp eviewEditorMainApp, EviewApplication eviewApplication) {
        mEviewEditorMainApp = eviewEditorMainApp;
        mEviewApplication = eviewApplication;
        bCheckedOut = eviewEditorMainApp.isCheckedOut();
        try {
            mEviewApplication.setStandardizerRepositoryDir();
        } catch (Exception ex) {
            
        }
        initComponents();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        jLabelNoProperties = new javax.swing.JLabel();
        jLabelNoProperties.setText(NbBundle.getMessage(
                EviewEditorMainPanel.class, "MSG_Property_NoProperties"));
        jLabelNoProperties.setEnabled(false);
        setName(NbBundle.getMessage(EviewEditorMainPanel.class,
                "TITLE_DefineEntity"));

        this.add(createToolBar(), BorderLayout.PAGE_START);
        createEntityTree(); // construct mEntityTree
        createSplitPane(); // Put tree and table in a split pane mSplitPane

        this.add(mSplitPane, BorderLayout.CENTER);
        // Force to display properties for root node
        setSelectedNode(mRootNode);
        try {
            entityPropertyPane.setViewportView(mRootNode.getConfigPropertySheetForRootNode());
            mSplitPane.setRightComponent(entityPropertyPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mEntityTree.setEditable(false);

    }

    private void createEntityTree() {
        mEntityTree = new EntityTree(mEviewEditorMainApp, mEviewApplication);
        mRootNode = mEntityTree.getRootNode();
        createMenu(mRootNode);
        addListeners();
    }
    
    private void addListeners() {
        //mEntityTree.addKeyListener(new EntityTreeKeyAdapter());
        mEntityTree.addTreeSelectionListener(this);
        mEntityTree.addMouseListener(new PopupListener());
    }
    
    private void createSplitPane() {
        // Put tree and table in a split pane mSplitPane
        entityTreePane = new JScrollPane(mEntityTree);
        entityTreePane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EntityTree.class, "LBL_Object_Definition")));
        entityPropertyPane.setViewportView(jLabelNoProperties);
        entityPropertyPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EntityTree.class, "MSG_Properties")));
        
        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                entityTreePane, entityPropertyPane);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(250);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(250, 100);
        entityTreePane.setMinimumSize(minimumSize);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(null);
        toolBar.setFocusCycleRoot(true);

        //separator for object buttons
        mButtonAddPrimary = new JButton(new PrimaryEntityAddAction(
                    this.PRIMARYNODEIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_AddPrimaryEntity")));
        mButtonAddPrimary.setBorder(null);
        mButtonAddPrimary.setMnemonic('P');
        toolBar.add(mButtonAddPrimary);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonAddSub = new JButton(new SubEntityAddAction(
                    this.SUBNODEIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_AddSubEntity")));
        mButtonAddSub.setBorder(null);
        mButtonAddSub.setMnemonic('O');
        toolBar.add(mButtonAddSub);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonAddField = new JButton(new FieldAddAction(
                    this.FIELDNODEIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_AddField")));
        mButtonAddField.setBorder(null);
        mButtonAddField.setMnemonic('F');
        toolBar.add(mButtonAddField);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonDelete = new JButton(new DeleteAction(this.DELETENODEIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_Delete")));
        mButtonDelete.setBorder(null);
        mButtonDelete.setMnemonic('D');
        toolBar.add(mButtonDelete);
        
        //separator for template buttons
        //toolBar.addSeparator(new Dimension(20, 0));
        toolBar.addSeparator();
        
        mButtonTemplateCompany = new JButton(new TemplateCompanyAction(
                    this.TEMPLATECOMPANYIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class, "MSG_Company")));
        mButtonTemplateCompany.setBorder(null);
        mButtonTemplateCompany.setMnemonic('C');
        mButtonTemplateCompany.setName(NbBundle.getMessage(EviewEditorMainPanel.class, "MSG_Company"));

        mButtonTemplatePerson = new JButton(new TemplatePersonAction(
                    this.TEMPLATEPERSONIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class, "MSG_Person")));
        mButtonTemplatePerson.setBorder(null);
        mButtonTemplatePerson.setMnemonic('N');
        
        mTemplatesMenu = new JPopupMenu();
        mTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddTemplateCompany"), TEMPLATECOMPANYIMAGEICON));
        mTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddTemplatePerson"), TEMPLATEPERSONIMAGEICON));
        
        mSubTemplatesMenu = new JPopupMenu();
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAlias"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAddress"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodePhone"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAuxId"), SUBNODEIMAGEICON));

        mButtonTemplates = new JButton(this.TEMPLATESIMAGEICON);
        mButtonTemplates.setToolTipText(NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_Templates"));
        mButtonTemplates.addMouseListener(new TemplatePopupListener());
        mButtonTemplates.addFocusListener(new java.awt.event.FocusListener() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (mButtonTemplates.isEnabled()) {
                    //mTemplatesMenu.show(e.getComponent(), e.getX(), e.getY());
                    JButton button = (JButton) e.getComponent();
                    int height = button.getHeight();
                    int width = button.getWidth();
                    int orX = button.getX();
                    int orY = button.getY();
                    int dispX = orX - (orX);
                    int dispY = orY + (height - 1);

                    if (mCurrentSelectedNode.isPrimary()) {
                        mSubTemplatesMenu.show(e.getComponent(),
                               dispX, dispY);
                    } else {
                        mTemplatesMenu.show(e.getComponent(),
                               dispX, dispY);
                    }
                }
            }

            public void focusLost(java.awt.event.FocusEvent ev) {
            }
        });
        mButtonTemplates.setMnemonic('T');
        toolBar.add(mButtonTemplates);

        // Add sctions for tabs
        
        toolBar.addSeparator();
        mButtonTabApplicationProperties = new JButton(new TabApplicationPropertiesAction(this.TABAPPLICATIONPROPERTIESIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_TAB_Application_Properties")));
        mButtonTabApplicationProperties.setBorder(null);
        mButtonTabApplicationProperties.setMnemonic('A');
        toolBar.add(mButtonTabApplicationProperties);
        
        toolBar.addSeparator();
        mButtonSave = new JButton(new SaveAction(this.SAVEIMAGEICON,
                    NbBundle.getMessage(EviewEditorMainPanel.class,
                        "MSG_ToolTip_Save")));
        mButtonSave.setBorder(null);
        mButtonSave.setMnemonic('S');
        mButtonSave.setEnabled(false);
        toolBar.add(mButtonSave);
        
        return toolBar;
    }

    private void addSubNodeTemplate(String commandName) {
        TreePath path = mEntityTree.getAnchorSelectionPath();

        if (path != null) {
            EntityNode parentNode = (EntityNode) path.getLastPathComponent();
            EntityNode newNode = null;

            if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddSubNodeAlias"))) {
                newNode = TemplateObjects.addSubNodeAlias(getEntityTree(),
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddSubNodeAddress"))) {
                newNode = TemplateObjects.addSubNodeAddress(getEntityTree(),
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddSubNodePhone"))) {
                newNode = TemplateObjects.addSubNodePhone(getEntityTree(),
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddSubNodeAuxId"))) {
                newNode = TemplateObjects.addSubNodeAuxId(getEntityTree(),
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddTemplateCompany"))) {
                getTemplateCompany();
            } else if (commandName.equals(NbBundle.getMessage(
                            EviewEditorMainPanel.class,
                            "MSG_menu_AddTemplatePerson"))) {
                getTemplatePerson();
            }

            if (newNode != null) {
                expandAll(mEntityTree, new TreePath(newNode.getPath()), true);
                setSelectedNode(newNode);
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
        item.setIcon(defaultIcon);

        return item;
    }

    /** Create context sensitive menu
     *
     *@param node for proper menu per node type
     */
    protected void createMenu(EntityNode node) {
        mMenu = new JPopupMenu();

        String commandName = null;
        mButtonAddPrimary.setEnabled(false);
        mButtonAddSub.setEnabled(false);
        mButtonAddField.setEnabled(false);
        mButtonTemplates.setEnabled(false);
        mButtonTemplateCompany.setEnabled(false);
        mButtonTemplatePerson.setEnabled(false);
        mButtonDelete.setEnabled(false);
        mButtonTabApplicationProperties.setEnabled(!node.isRoot());
        
        if (node.isRoot()) {
            JMenuItem item = createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_NewPrimaryEntity"), null);

            if (mRootNode.getChildCount() != 0) { // only one primary for now
                item.setEnabled(false);
            } else {
                mButtonTemplates.setEnabled(bCheckedOut);
                mButtonTemplateCompany.setEnabled(bCheckedOut);
                mButtonTemplatePerson.setEnabled(bCheckedOut);
                mButtonAddPrimary.setEnabled(bCheckedOut);
            }

            mMenu.add(item);

            // SubMenu for complete Templates (Company and Person)
            mMenu.addSeparator();

            JMenu smTemplates = new JMenu(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_Templates"));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddTemplateCompany"), TEMPLATECOMPANYIMAGEICON));
            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddTemplatePerson"), TEMPLATEPERSONIMAGEICON));

            mMenu.add(smTemplates);

            if (node.getChildCount() != 0) {
                smTemplates.setEnabled(false);
            }
        }

        if (node.isPrimary()) { // || node.isSub()) only 1 level for now
            mButtonTemplates.setEnabled(bCheckedOut);
            mMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_NewSubEntity"),
                    null));
            mButtonAddSub.setEnabled(bCheckedOut);
        }

        if (!node.isRoot()) {
            mMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_NewField"),
                    null));
            mButtonAddField.setEnabled(bCheckedOut);
        }

        // SubMenu for templates
        if (node.isPrimary()) {
            mMenu.addSeparator();

            JMenu smTemplates = new JMenu(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_Templates"));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAlias"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAddress"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodePhone"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class,
                        "MSG_menu_AddSubNodeAuxId"), SUBNODEIMAGEICON));

            mMenu.add(smTemplates);
            
            mMenu.addSeparator();
            mMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_Delete"), null));
            mButtonDelete.setEnabled(bCheckedOut);

        }

        if (node.isSub() || node.isField()) {
            if (node.isSub()) {
                mMenu.addSeparator();
            }

            mMenu.add(createMenuItem(NbBundle.getMessage(
                        EviewEditorMainPanel.class, "MSG_menu_Delete"), null));
            mButtonDelete.setEnabled(bCheckedOut);
        }
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
                                EviewEditorMainPanel.class,
                                "MSG_menu_NewPrimaryEntity"))) {
                    mEntityTree.addPrimaryEntity(null);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_NewSubEntity"))) {
                    mEntityTree.addSubEntity(null);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_NewField"))) {
                    mEntityTree.addField(null);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class, "MSG_menu_Delete"))) {
                    mEntityTree.deleteNode();
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddTemplateCompany"))) {
                    getTemplateCompany();
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddTemplatePerson"))) {
                    getTemplatePerson();
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddSubNodeAlias"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddSubNodeAddress"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddSubNodePhone"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                EviewEditorMainPanel.class,
                                "MSG_menu_AddSubNodeAuxId"))) {
                    addSubNodeTemplate(commandName);
                }
            }
        } catch (Exception ev) {
            ev.printStackTrace();
        }
    }

    /** TreeSelectionListener
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     *
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (mIgnoreSelection) {
            return;
        }
        mCurrentSelectedNode = (EntityNode) e.getPath().getLastPathComponent();
        if (mPreviousSelectedNode != mCurrentSelectedNode) {
            mEntityTree.setPreviousSelectedNode(mCurrentSelectedNode);
            mPreviousSelectedNode = mCurrentSelectedNode;
        }
        if (mCurrentSelectedNode.isPrimary()) {
            mOldPrimaryObjName = mCurrentSelectedNode.toString();
        }
        // Just hide it
        mTemplatesMenu.setVisible(false);
        mSubTemplatesMenu.setVisible(false);
        
        // We should create the menu at selection
        createMenu(mCurrentSelectedNode);

        try {
            if (mCurrentSelectedNode.isRoot()) {
                entityPropertyPane.setViewportView(mCurrentSelectedNode.getConfigPropertySheetForRootNode());
                mSplitPane.setRightComponent(entityPropertyPane);
                mEntityTree.setEditable(false);
            } else {
                entityPropertyPane.setViewportView(mCurrentSelectedNode.getConfigPropertySheet());
                mSplitPane.setRightComponent(entityPropertyPane);
                mEntityTree.setEditable(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getTemplateCompany() {
        if (mRootNode.getChildCount() == 0) {
            TemplateCompany tc = new TemplateCompany(mRootNode);
            expandAll(mEntityTree, new TreePath(mRootNode), false);
            setSelectedNode((EntityNode) mRootNode.getChildAt(0));
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void getTemplatePerson() {
        if (mRootNode.getChildCount() == 0) {
            TemplatePerson tc = new TemplatePerson(mRootNode);
            expandAll(mEntityTree, new TreePath(mRootNode), false);
            setSelectedNode((EntityNode) mRootNode.getChildAt(0));
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * to trigger propertyChangeEvent
     *@param selectedNode current node
     */
    private void setSelectedNode(EntityNode selectedNode) {
        TreePath selectionPath = new TreePath(selectedNode.getPath());
        mEntityTree.scrollPathToVisible(selectionPath);
        mEntityTree.setSelectionPath(selectionPath);
    }

    /** validate Entity Tree
     *@return  String
     */
    public String validateEntityTree() {
        String msg = "Success";
        EntityNode primaryNode = (EntityNode) mRootNode.getChildAt(0);
        int cnt = primaryNode.getChildCount();
	    if (cnt > 0) {
            EntityNode fieldNode = (EntityNode) primaryNode.getChildAt(0);
            if (!fieldNode.isField()) {
                msg = "Primary object node \"" + primaryNode.getName() + "\" contains no fields!";
            } else {
                for (int i = 0; i < cnt; i++) {
                    EntityNode objNode = (EntityNode) primaryNode.getChildAt(i);
                    if (objNode.isSub() && objNode.getChildCount() <= 0) {
                        msg = "Object node \"" + objNode.getName() + "\" contains no fields!";
                        break;
                    }
                }
            }
        } else {
            msg = "Primary object node \"" + primaryNode.getName() + "\" contains no fields!";
        }
        return msg;
    }
    
    /**
    *@return Entity tree model
    */
    public EntityTree getEntityTree() {
        return mEntityTree;
    }
    
    /**
    *@return Entity tree model
    */
    public EntityTree getEntityTree(boolean ignoreSelection) {
        mIgnoreSelection = ignoreSelection;
        return mEntityTree;
    }

    
    public void setEntityTreePane() {
        if (mIgnoreSelection && mPreviousSelectedNode != null) {
            mEntityTree.setSelectionPath(new TreePath(mPreviousSelectedNode.getPath()));
        }
        mIgnoreSelection = false;
        entityTreePane = new JScrollPane(mEntityTree);
        entityTreePane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EntityTree.class, "LBL_Object_Definition")));
       
        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(250, 100);
        entityTreePane.setMinimumSize(minimumSize);

        mSplitPane.setLeftComponent(entityTreePane);
    }

    /**
     *@return if structure is valid
     */
    public boolean isEntityStructureValid() {
        return (mRootNode.getChildCount() > 0);
    }
   
    /** Add a Primary node
     *
     */
    public class PrimaryEntityAddAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public PrimaryEntityAddAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mEntityTree.addPrimaryEntity(null);
        }
    }

    /** Add a Sub node
     *
     */
    public class SubEntityAddAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public SubEntityAddAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mEntityTree.addSubEntity(null);
        }
    }

    /** Add a field node
     *
     */
    public class FieldAddAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public FieldAddAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mEntityTree.addField(null);
        }
    }

    /** Delete a node
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
            mEntityTree.deleteNode();
        }
    }
    
    public void enableSaveButton(boolean flag) {
        mButtonSave.setEnabled(flag);
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
            mEviewEditorMainApp.save(false);
        }
    }



    public void invokeMatchConfigAction(String matchType) {
        performeMatchConfigAction(matchType);
    }
    
    private void performeMatchConfigAction(String matchType) {
        mEntityTree.setSelectionPath(new TreePath(mRootNode.getPath()));
        mRootNode.setSelectedTabMatchConfig(matchType);
    }

    public void invokeBlockingAction() {
        performeBlockingAction();
    }
    
    private void performeBlockingAction() {
        mEntityTree.setSelectionPath(new TreePath(mRootNode.getPath()));
        mRootNode.setSelectedTabBlocking();
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
            mEntityTree.setSelectionPath(new TreePath(mRootNode.getPath()));
            //mRootNode.setSelectedTabMatchConfig(null);
        }
    }

    /** Bring up TabNormalization
     *
     */
    public class TabNormalizationAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public TabNormalizationAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            mEntityTree.setSelectionPath(new TreePath(mRootNode.getPath()));
            mRootNode.setSelectedTabNormalization();
        }
    }

    /** fill the tree with Company stuff
     *
     */
    public class TemplateCompanyAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public TemplateCompanyAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            getTemplateCompany();
        }
    }

    /** fill the tree with Person stuff
     *
     */
    public class TemplatePersonAction extends AbstractAction {
        /**
         *@param icon image icon
         *@param desc description
         */
        public TemplatePersonAction(ImageIcon icon, String desc) {
            super(null, icon);
            putValue(SHORT_DESCRIPTION, desc);
        }

        /**
         *@param e Action event
         */
        public void actionPerformed(java.awt.event.ActionEvent e) {
            getTemplatePerson();
        }
    }

    class TemplatePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (mButtonTemplates.isEnabled()) {
                //mTemplatesMenu.show(e.getComponent(), e.getX(), e.getY());
                JButton button = (JButton) e.getComponent();
                int height = button.getHeight();
                int width = button.getWidth();
                int orX = button.getX();
                int orY = button.getY();
                int dispX = orX - (orX);
                int dispY = orY + (height - 1);

                if (mCurrentSelectedNode.isPrimary()) {
                    mSubTemplatesMenu.show(e.getComponent(),
                               dispX, dispY);
                } else {
                    mTemplatesMenu.show(e.getComponent(),
                               dispX, dispY);
                }
            }
        }
    }
    
    class PopupListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (mEntityTree.getAnchorSelectionPath() != null) {
                maybeShowPopup(e);
            }
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger() && bCheckedOut) {
                mMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    private class EntityTreeKeyAdapter extends java.awt.event.KeyAdapter {

        /**
         * key typed
         * @param evt event
         */
        @Override
        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                mEntityTree.deleteNode();
            }
        }
    }
}
