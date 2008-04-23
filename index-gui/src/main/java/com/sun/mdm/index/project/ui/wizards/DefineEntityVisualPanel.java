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
package com.sun.mdm.index.project.ui.wizards;

import com.sun.mdm.index.parser.NodeDef;

import org.netbeans.api.project.Project;

import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/** A single panel for a wizard - the GUI portion.
 *
 */
public class DefineEntityVisualPanel extends javax.swing.JPanel
    implements TreeSelectionListener, ActionListener, CellEditorListener {
    //private DragSource mDragSource;
    //private EntityTreeDnDAdapter mTreeDnDAdapter;
    private static JTree mEntityTree = null;
    final JTextField mText = new JTextField();
    static EntityNode mPreviousSelectedNode = null;
    EntityNode mCurrentSelectedNode = null;
    
    static String mOldObjName = null;    
    static final boolean DEBUG = false;
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
    static final ImageIcon TEMPLATESIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplateDropdown.png"));
    static final ImageIcon TEMPLATECOMPANYIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplateCompany.png"));
    static final ImageIcon TEMPLATEPERSONIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/TemplatePerson.png"));

    /** The wizard panel descriptor associated with this GUI panel.
     * If you need to fire state changes or something similar, you can
     * use this handle to do so.
     */
    private final DefineEntityPanel panel;
    private String mViewName;
    private JSplitPane mSplitPane = null;
    private JScrollPane mEntityTreePane;
    private EntityNode mRootNode;
    private JPopupMenu mMenu;
    private JPopupMenu mTemplatesMenu;
    private JPopupMenu mSubTemplatesMenu;
    private boolean mCreated = false;
    JLabel jLabelNoProperties;
    private JButton mButtonAddPrimary;
    private JButton mButtonAddSub;
    private JButton mButtonAddField;
    private JButton mButtonTemplates;
    private JButton mButtonTemplateCompany;
    private JButton mButtonTemplatePerson;
    private JButton mButtonDelete;
    private Project mProject = null;
    
    /**
     * Create the wizard panel and set up some basic properties.
     *
     *@param panel DefineEntityPanel
     */
    public DefineEntityVisualPanel(DefineEntityPanel panel) {
        this.panel = panel;
        initComponents();

        jLabelNoProperties = new javax.swing.JLabel();
        jLabelNoProperties.setText(NbBundle.getMessage(
                DefineEntityVisualPanel.class, "MSG_Property_NoProperties"));
        jLabelNoProperties.setEnabled(false);

        /*
        // Optional: provide a special description for this pane.
        // You must have turned on WizardDescriptor.WizardPanel_helpDisplayed
        // (see descriptor in standard iterator template for an example of this).
        try {
            putClientProperty("WizardPanel_helpURL", // NOI18N
                new URL("nbresloc:/eview/src/java/com/sun/mdm/index/project/ui/wizards/ObjectVisualHelp.html")); // NOI18N
        } catch (MalformedURLException mfue) {
            throw new IllegalStateException(mfue.toString());
        }
         */
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    private void createEntityTree() {
        mRootNode = new EntityNode(null, mViewName, EntityNode.getRootNodeType());
        mEntityTree = new JTree(mRootNode);
        mEntityTree.setEditable(true);
        mEntityTree.setDragEnabled(true);

        //mEntityTree.setDragSource(true);
        //mEntityTree.setDropTarget(false);
        //mDragSource = DragSource.getDefaultDragSource();
        //mTreeDnDAdapter = new EntityTreeDnDAdapter(mEntityTree);
        //mDragSource.addDragSourceListener(mTreeDnDAdapter);
        mEntityTree.setBorder(new javax.swing.border.TitledBorder(""));

        mEntityTree.setSelectionPath(new TreePath(mRootNode.getPath()));
        createMenu(mRootNode);

        EntityTreeRenderer mRenderer = new EntityTreeRenderer();
        mEntityTree.setCellRenderer(mRenderer);
        mText.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent ev) {
                    mText.selectAll();
                }

                public void focusLost(FocusEvent ev) {
                }
            });
        mText.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    char c = evt.getKeyChar();
                    int keyCode = evt.getKeyCode();
                    String s = "~`!@#$%^&*()_+-=|{}[]\\:\";\'<>?,./";
                    boolean bInvalid = s.indexOf(c) >= 0;
                    if (bInvalid) {
                        Toolkit.getDefaultToolkit().beep();
                        String msg = "Invalid character '" + c + "' !";
                        NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                        desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
                        desc.setTitle("Error");                       
                        DialogDisplayer.getDefault().notify(desc);
                        evt.consume();
                    }
                }
        });
 
        DefaultCellEditor dce = new DefaultCellEditor(mText) {
            @Override
            public boolean isCellEditable(java.util.EventObject event) {
                boolean bEditable = true;
                if (event instanceof MouseEvent) {
                    bEditable = false;
                    MouseEvent mouseEvent = (MouseEvent) event;
                    TreePath hitPath = mEntityTree.getPathForLocation(
                            mouseEvent.getX(),
                            mouseEvent.getY());
                    if (hitPath != null) {
                        EntityNode treeNode = (EntityNode) hitPath.getLastPathComponent();
                        if (treeNode != null && !treeNode.isRoot() && mPreviousSelectedNode == treeNode) {
                            bEditable = true;
                        }
                    }
                }
                return bEditable;
            }
        };
        mEntityTree.setCellEditor(dce);
        
        mEntityTree.getCellEditor().addCellEditorListener(this);
        mEntityTree.addKeyListener(new EntityTreeKeyAdapter());

        mEntityTree.addTreeSelectionListener(this);

        mEntityTree.addMouseListener(new PopupListener());
        mEntityTree.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    panel.fireChangeEvent();
                }
            });

        mEntityTreePane = new JScrollPane(mEntityTree);
    }

    private void createSplitPane() {
        // Put tree and table in a split pane mSplitPane
        mSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                mEntityTreePane, jLabelNoProperties);
        mSplitPane.setOneTouchExpandable(true);
        mSplitPane.setDividerLocation(200);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(180, 350);
        mEntityTreePane.setMinimumSize(minimumSize);
    }

    // This method adds a new column to table without reconstructing
    // all the other columns.
    private void betterAddColumn(JTable table, Object headerLabel,
        Object[] values) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableColumn col = new TableColumn(model.getColumnCount());

        // Ensure that auto-create is off
        if (table.getAutoCreateColumnsFromModel()) {
            throw new IllegalStateException();
        }

        col.setHeaderValue(headerLabel);
        table.addColumn(col);
        model.addColumn(headerLabel.toString(), values);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(null);
        toolBar.setFocusCycleRoot(true);

        //separator for object buttons
        mButtonAddPrimary = new JButton(new PrimaryEntityAddAction(
                    PRIMARYNODEIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_AddPrimaryEntity")));
        mButtonAddPrimary.setBorder(null);
        mButtonAddPrimary.setMnemonic('P');
        toolBar.add(mButtonAddPrimary);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonAddSub = new JButton(new SubEntityAddAction(
                    SUBNODEIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_AddSubEntity")));
        mButtonAddSub.setBorder(null);
        mButtonAddSub.setMnemonic('S');
        toolBar.add(mButtonAddSub);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonAddField = new JButton(new FieldAddAction(
                    FIELDNODEIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_AddField")));
        mButtonAddField.setBorder(null);
        mButtonAddField.setMnemonic('F');
        toolBar.add(mButtonAddField);

        toolBar.addSeparator(new Dimension(0, 5));
        mButtonDelete = new JButton(new DeleteAction(DELETENODEIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_Delete")));
        mButtonDelete.setBorder(null);
        mButtonDelete.setMnemonic('D');
        toolBar.add(mButtonDelete);

        //separator for template buttons
        //toolBar.addSeparator(new Dimension(20, 0));
        toolBar.addSeparator();
        
        mButtonTemplateCompany = new JButton(new TemplateCompanyAction(
                    TEMPLATECOMPANYIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_TemplateCompany")));
        mButtonTemplateCompany.setBorder(null);
        mButtonTemplateCompany.setName("Company");
        
        mButtonTemplatePerson = new JButton(new TemplatePersonAction(
                    TEMPLATEPERSONIMAGEICON,
                    NbBundle.getMessage(DefineEntityVisualPanel.class,
                        "MSG_ToolTip_TemplatePerson")));
        mButtonTemplatePerson.setBorder(null);
        
        mTemplatesMenu = new JPopupMenu();
        mTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddTemplateCompany"), TEMPLATECOMPANYIMAGEICON));
        mTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddTemplatePerson"), TEMPLATEPERSONIMAGEICON));
        
        mSubTemplatesMenu = new JPopupMenu();
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAlias"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAddress"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodePhone"), SUBNODEIMAGEICON));
        mSubTemplatesMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAuxId"), SUBNODEIMAGEICON));


        mButtonTemplates = new JButton(TEMPLATESIMAGEICON);
        mButtonTemplates.setToolTipText(NbBundle.getMessage(DefineEntityVisualPanel.class,
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

        return toolBar;
    }

    private void addPrimaryEntity() {
        // add a 1st level node to the tree
        EntityNode childNode = new EntityNode(mEntityTree, mViewName, EntityNode.getPrimaryNodeType());
        DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();

        model.insertNodeInto(childNode, mRootNode, mRootNode.getChildCount());
        
        TreePath selectionPath = new TreePath(childNode.getPath());
        expandAll(mEntityTree, selectionPath, false);
        mEntityTree.scrollPathToVisible(selectionPath);
        mEntityTree.setSelectionPath(selectionPath);
        mEntityTree.startEditingAtPath(selectionPath);

        mSplitPane.setRightComponent(jLabelNoProperties);
    }

    private void addSubEntity() {
        TreePath path = mEntityTree.getAnchorSelectionPath();

        if (path != null) {
            // add a sub node to the current path
            EntityNode parentNode = (EntityNode) path.getLastPathComponent();
            String nodeName = EntityNode.getSubNodeType() +
                                (parentNode.getChildCount() - parentNode.getFieldCnt());
            EntityNode childNode = new EntityNode(mEntityTree, nodeName, EntityNode.getSubNodeType());
            DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();
            model.insertNodeInto(childNode, parentNode,
                parentNode.getChildCount());
            expandAll(mEntityTree, path, false);

            TreePath selectionPath = new TreePath(childNode.getPath());
            mEntityTree.scrollPathToVisible(selectionPath);
            mEntityTree.setSelectionPath(selectionPath);
            mEntityTree.startEditingAtPath(selectionPath);
            
            mSplitPane.setRightComponent(jLabelNoProperties);
        }
    }

    private void addField() {
        // add a field node to the current level (Primary or nested object)
        TreePath path = mEntityTree.getAnchorSelectionPath();

        if (path != null) {
            EntityNode parentNode = (EntityNode) path.getLastPathComponent();
            int fieldCnt;
            int insertIdx;

            fieldCnt = parentNode.getFieldCnt();
            insertIdx = fieldCnt;

            if (parentNode.isField()) {
                EntityNode fieldNode = parentNode;
                parentNode = (EntityNode) fieldNode.getParent();
                fieldCnt = parentNode.getFieldCnt();
                insertIdx = parentNode.getIndex(fieldNode) + 1;
            }

            EntityNode childNode = new EntityNode(mEntityTree, parentNode.getName() + EntityNode.getFieldNodeType() +
                    fieldCnt, EntityNode.getFieldNodeType(), "string");

            //childNode.setDisplayOrder(fieldCnt + 1);
            // Insert new node after the last field node
            DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();
            model.insertNodeInto(childNode, parentNode, insertIdx);
            expandAll(mEntityTree, path, false);

            TreePath selectionPath = new TreePath(childNode.getPath());
            mEntityTree.scrollPathToVisible(selectionPath);
            mEntityTree.setSelectionPath(selectionPath);
            mEntityTree.startEditingAtPath(selectionPath);

            // add property here
            mSplitPane.setRightComponent(childNode.getPropertySheet());
        }
    }

    private void deleteNode() {
        EntityNode newSelectedNode = null;
        TreePath[] paths = mEntityTree.getSelectionPaths();
        for (int i=0; i < paths.length; i++) {
            
            TreePath path = paths[i]; //mEntityTree.getAnchorSelectionPath();
            EntityNode nodeToBeDeleted = (EntityNode) path.getLastPathComponent();
            if (nodeToBeDeleted.isRoot()) {
                continue;
            }
            EntityNode parentNode = (EntityNode) nodeToBeDeleted.getParent();
            int newSelectedNodeIndex = parentNode.getIndex(nodeToBeDeleted) - 1;
            DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();
            model.removeNodeFromParent(nodeToBeDeleted);

            // set new selection node
            newSelectedNode = parentNode;

            if (newSelectedNodeIndex >= 0) {
                newSelectedNode = (EntityNode) parentNode.getChildAt(newSelectedNodeIndex);
            }
        }
        
        if (newSelectedNode != null) {
            TreePath selectionPath = new TreePath(newSelectedNode.getPath());
            mEntityTree.scrollPathToVisible(selectionPath);
            mEntityTree.setSelectionPath(selectionPath);
        }
    }

    private void addSubNodeTemplate(String commandName) {
        TreePath path = mEntityTree.getAnchorSelectionPath();

        if (path != null) {
            EntityNode parentNode = (EntityNode) path.getLastPathComponent();
            EntityNode newNode = null;

            if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
                            "MSG_menu_AddSubNodeAlias"))) {
                newNode = TemplateObjects.addSubNodeAlias(mEntityTree, 
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
                            "MSG_menu_AddSubNodeAddress"))) {
                newNode = TemplateObjects.addSubNodeAddress(mEntityTree, 
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
                            "MSG_menu_AddSubNodePhone"))) {
                newNode = TemplateObjects.addSubNodePhone(mEntityTree, 
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
                            "MSG_menu_AddSubNodeAuxId"))) {
                newNode = TemplateObjects.addSubNodeAuxId(mEntityTree, 
                        parentNode);
            } else if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
                            "MSG_menu_AddTemplateCompany"))) {
                getTemplateCompany();
            } else if (commandName.equals(NbBundle.getMessage(
                            DefineEntityVisualPanel.class,
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

        if (node.isRoot()) {
            JMenuItem item = createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_NewPrimaryEntity"), null);

            if (mRootNode.getChildCount() != 0) { // only one primary for now
                item.setEnabled(false);
            } else {
                mButtonTemplates.setEnabled(true);
                mButtonTemplateCompany.setEnabled(true);
                mButtonTemplatePerson.setEnabled(true);
                mButtonAddPrimary.setEnabled(true);
            }

            mMenu.add(item);

            // SubMenu for complete Templates (Company and Person)
            mMenu.addSeparator();

            JMenu smTemplates = new JMenu(NbBundle.getMessage(
                        DefineEntityVisualPanel.class, "MSG_menu_Templates"));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddTemplateCompany"), TEMPLATECOMPANYIMAGEICON));
            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddTemplatePerson"), TEMPLATEPERSONIMAGEICON));

            mMenu.add(smTemplates);

            if (node.getChildCount() != 0) {
                smTemplates.setEnabled(false);
            }
        }

        if (node.isPrimary()) {
            mMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class, "MSG_menu_NewSubEntity"),
                    null));
            mButtonAddSub.setEnabled(true);
        }

        if (!node.isRoot()) {
            mMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class, "MSG_menu_NewField"),
                    null));
            mButtonAddField.setEnabled(true);
        }

        // SubMenu for templates
        if (node.isPrimary()) {
            mMenu.addSeparator();

            JMenu smTemplates = new JMenu(NbBundle.getMessage(
                        DefineEntityVisualPanel.class, "MSG_menu_Templates"));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAlias"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAddress"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodePhone"), SUBNODEIMAGEICON));

            smTemplates.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class,
                        "MSG_menu_AddSubNodeAuxId"), SUBNODEIMAGEICON));

            mMenu.add(smTemplates);
        }

        if (!node.isRoot() && !node.isPrimaryFields()) {
            if (!node.isField()) {
                mMenu.addSeparator();
            }

            mMenu.add(createMenuItem(NbBundle.getMessage(
                        DefineEntityVisualPanel.class, "MSG_menu_Delete"), null));
            mButtonDelete.setEnabled(true);
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
        tree.expandPath(parent);
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
                                DefineEntityVisualPanel.class,
                                "MSG_menu_NewPrimaryEntity"))) {
                    addPrimaryEntity();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_NewSubEntity"))) {
                    addSubEntity();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_NewField"))) {
                    addField();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class, "MSG_menu_Delete"))) {
                    deleteNode();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_AddTemplateCompany"))) {
                    getTemplateCompany();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_AddTemplatePerson"))) {
                    getTemplatePerson();
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_AddSubNodeAlias"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_AddSubNodeAddress"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
                                "MSG_menu_AddSubNodePhone"))) {
                    addSubNodeTemplate(commandName);
                } else if (commandName.equals(NbBundle.getMessage(
                                DefineEntityVisualPanel.class,
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
        mCurrentSelectedNode = (EntityNode) e.getPath().getLastPathComponent();
        if (mPreviousSelectedNode != mCurrentSelectedNode) {
            mPreviousSelectedNode = mCurrentSelectedNode;
        }
        mOldObjName = mCurrentSelectedNode.toString();
        // Just hide it
        mTemplatesMenu.setVisible(false);
        
        // We should create the menu at selection
        createMenu(mCurrentSelectedNode);

        if (mCurrentSelectedNode.isField()) {
            mSplitPane.setRightComponent(mCurrentSelectedNode.getPropertySheet());
        } else {
            mSplitPane.setRightComponent(jLabelNoProperties);
        }

        if (mCurrentSelectedNode.isRoot() || mCurrentSelectedNode.isPrimaryFields()) {
            mEntityTree.setEditable(false);
        } else {
            mEntityTree.setEditable(true);
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

    /** set view name for panel title
     *@param newViewName new view name
     */
    public void setViewName(String newViewName) {
        mViewName = newViewName;
        setName(NbBundle.getMessage(DefineEntityVisualPanel.class,
                "TITLE_DefineEntity"));

        // Create the tree after view name set
        // This is bad, but until I figure out how to set node name for root
        if (!mCreated) {
            mCreated = true;
            this.add(createToolBar(), BorderLayout.PAGE_START);
            createEntityTree(); // construct mEntityTreePane
            createSplitPane(); // Put tree and table in a split pane mSplitPane

            this.add(mSplitPane, BorderLayout.CENTER);
        } else {
            mRootNode.setUserObject(mViewName);
        }
    }

    /** set Entity Tree
     *@param  alEoNodes Eo Nodes (array of Eo nodes)
     */
    public void setEntityTree(ArrayList alEoNodes) {

        if ((alEoNodes != null) && (alEoNodes.size() > 0)) {
            //mRootNode.removeAll();
            int i = 0;
            Iterator it = alEoNodes.iterator();
            DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();
            EntityNode childNode;

            while (it.hasNext()) {
                // Primary and Sub objects
                NodeDef node = (NodeDef) it.next();

                //
                if (i == 0) {
                    childNode = new EntityNode(mEntityTree, node.getTag(),
                            EntityNode.getPrimaryNodeType());
                } else {
                    childNode = new EntityNode(mEntityTree, node.getTag(),
                            EntityNode.getSubNodeType());
                }

                model.insertNodeInto(childNode, mRootNode,
                    mRootNode.getChildCount());
            }
        }
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
    public static JTree getEntityTree() {
        return mEntityTree;
    }

    /**
    *@return Entity tree model
    */
    public static DefaultTreeModel getTreeModel() {
        return (DefaultTreeModel) mEntityTree.getModel();
    }

    /**
     *@return if structure is valid
     */
    public boolean isEntityStructureValid() {
        return (mRootNode.getChildCount() > 0);
    }

    /* CellEditorListener */
    /** This tells the listeners the editor has canceled editing  */
    public void editingCanceled(ChangeEvent e) {
    }
    
    /** This tells the listeners the editor has ended editing  */
    public void editingStopped(ChangeEvent e) {

        String value = mText.getText();
        TreePath path = mEntityTree.getAnchorSelectionPath();
        EntityNode selectedNode = (EntityNode) path.getLastPathComponent();

        if (value.length() == 0) {
            Toolkit.getDefaultToolkit().beep();

            NotifyDescriptor d = new NotifyDescriptor.Message("Object/Field name cannot be null!");
            d.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
            d.setTitle("Error");
            DialogDisplayer.getDefault().notify(d);

            selectedNode.setName(mOldObjName);
            mEntityTree.setSelectionPath(path);
            mEntityTree.startEditingAtPath(path);
            mText.setText(mOldObjName);
            mText.selectAll();
           
            return;
        }
        
        //ToDo, commented out for alaska
        /*
        if (selectedNode.isPrimary()) {
            try {
                String objectName = value;
                ProjectElement dupProjectElement = mProject.getProjectElement(objectName);

                if (dupProjectElement != null) {
                    Toolkit.getDefaultToolkit().beep();

                    NotifyDescriptor d = new NotifyDescriptor.Confirmation("Caution! \"" +
                        objectName + "\" is used as an object name in a previous Project.\n" +
                        "Reusing this name can cause components in that Project to become unusable.\n\n" +
                        "Press Yes to keep \"" + objectName + "\",\n" +
                        "or press No to rename the object.", "Confirm Dialog",
                        NotifyDescriptor.YES_NO_OPTION);

                    if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.NO_OPTION) {
                        selectedNode.setName(mOldObjName);
                        mEntityTree.startEditingAtPath(path);
                        mText.setText(mOldObjName);
                        mText.selectAll();
                    }
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        */
        
        if (value.length() > 20) {
            Toolkit.getDefaultToolkit().beep();

            NotifyDescriptor d = new NotifyDescriptor.Message("\"" +
                        value + "\" is longer than 20 characters.\n\n" +
                        "After appending Master Index qualifier it may exceed maximum\n" +
                        "length for a database identifier.");
            d.setMessageType(NotifyDescriptor.WARNING_MESSAGE);
            d.setTitle("Warning");
            DialogDisplayer.getDefault().notify(d);

            selectedNode.setName(mOldObjName);
            mEntityTree.startEditingAtPath(path);
            mText.setText(mOldObjName);
            mText.selectAll();
            return;
        }       

        // Check duplicate
        boolean bDuplicated = false;
        EntityNode parentNode;
        if (selectedNode.isPrimary() || selectedNode.isSub()) {
            if (selectedNode.isSub()) {
                // Go to primary node
                parentNode = (EntityNode) selectedNode.getParent();
                // Compare primary node first
                if (value.equals(parentNode.getName())) {
                    bDuplicated = true;
                }
            } else {
                // It is primary node
                parentNode = selectedNode;
            }
            if (!bDuplicated) {
                int cnt = parentNode.getChildCount();
                for (int i = 0; i < cnt; i++) {
                    EntityNode subNode = (EntityNode) parentNode.getChildAt(i);

                    if (subNode.isSub() && value.equals(subNode.getName())) {
                        bDuplicated = true;
                        break;
                    }
                }
            }
        } else {
            // Go to primary node
            parentNode = (EntityNode) mRootNode.getChildAt(0);
            if (value.equals(parentNode.getName())) {
                bDuplicated = true;
            } else {
                int cnt = parentNode.getChildCount();
                for (int i = 0; i < cnt && !bDuplicated; i++) {
                    EntityNode subNode = (EntityNode) parentNode.getChildAt(i);

                    if (value.equals(subNode.getName())) {
                        bDuplicated = true;
                        break;
                    }
                    if (subNode.isSub()) {
                        EntityNode parentNode2 = subNode;
                        int cnt2 = parentNode2.getChildCount();
                        for (int j = 0; j < cnt2; j++) {
                            EntityNode subNode2 = (EntityNode) parentNode2.getChildAt(j);

                            if (value.equals(subNode2.getName())) {
                                bDuplicated = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (bDuplicated) {
            Toolkit.getDefaultToolkit().beep();
            String msg = "\"" + value + "\" has been used, please select another name.";
            NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
            DialogDisplayer.getDefault().notify(desc);
            mText.setText(selectedNode.getName());
            selectedNode.setName(selectedNode.getName());
            DefaultTreeModel model = (DefaultTreeModel) mEntityTree.getModel();
            model.reload();
            mEntityTree.scrollPathToVisible(path);
            mEntityTree.startEditingAtPath(path);
            mEntityTree.setSelectionPath(path);
        } else {
            selectedNode.setName(value);
            mOldObjName = value;
        }
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
            addPrimaryEntity();
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
            addSubEntity();
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
            addField();
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
            deleteNode();
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
        @Override
        public void mousePressed(MouseEvent e) {
            if (mButtonTemplates.isEnabled()) {
                JButton button = (JButton) e.getComponent();
                int height = button.getHeight();
                int width = button.getWidth();
                int orX = button.getX();
                int orY = button.getY();
                int dispX = orX - (orX);
                int dispY = orY + (height - 1);

                mTemplatesMenu.show(e.getComponent(),
                           dispX, dispY);
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
            if (e.isPopupTrigger()) {
                mMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    private class EntityTreeRenderer extends DefaultTreeCellRenderer {
        public EntityTreeRenderer() {
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                leaf, row, hasFocus);

            EntityNode node = (EntityNode) value;

            if (node.isRoot()) {
                setIcon(null /*ROOTIMAGEICON*/);
                setToolTipText("Root Node");
            } else if (node.isPrimary()) {
                setIcon(PRIMARYNODEIMAGEICON);
                setToolTipText("Primary Object Node");
            } else if (node.isPrimaryFields()) {
                setIcon(null);
                setToolTipText("Primary Fields Node (Cannot be renamed)");
            } else if (node.isSub()) {
                setIcon(SUBNODEIMAGEICON);
                setToolTipText("Sub Object Node");
            } else if (node.isField()) {
                setIcon(FIELDNODEIMAGEICON);
                setToolTipText("Field Node");
            } else {
                setIcon(null);

                //setToolTipText(null);
            }

            return this;
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
                deleteNode();
            }
        }
    }
    
    /** set Project for checking existing view
     *@param proj Project that invoke this wizard
     */
    public void setProject(Project proj) {
        mProject = proj;
    }
}
