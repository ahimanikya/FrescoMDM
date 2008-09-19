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

import com.sun.mdm.multidomain.project.MultiDomainApplication;

/** The main panel for Master Index Configuration Editor.
 *
 */
public class EditorMainPanel extends javax.swing.JPanel implements ActionListener  {
    //private EntityTree mEntityTree = null;
    private boolean mIgnoreSelection = false;    
    final JTextField mText = new JTextField();
    //EntityNode mPreviousSelectedNode = null;
    //EntityNode mCurrentSelectedNode = null;
    String mOldPrimaryObjName = null;
    String mOldText = null;

    static final ImageIcon DELETENODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Remove.png"));
    static final ImageIcon SAVEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/wizards/resources/images/Save.png"));

    private JSplitPane mSplitPane = null;
    private JScrollPane entityTreePane;
    private JScrollPane entityPropertyPane = new JScrollPane();
    //private EntityNode mRootNode;
    private JPopupMenu mMenu;
    private JLabel jLabelNoProperties;
    private JButton mButtonDelete;
    private JButton mButtonSave;
    private EditorMainApp mEditorMainApp;
    private MultiDomainApplication mMultiDomainApplication;
    
    /**
     * Create the panel and set up some basic properties.
     *
     */
    public EditorMainPanel(EditorMainApp eviewEditorMainApp, MultiDomainApplication application) {
        mEditorMainApp = eviewEditorMainApp;
        mMultiDomainApplication = application;

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
        createSplitPane(); // Put tree and table in a split pane mSplitPane

        this.add(mSplitPane, BorderLayout.CENTER);
    }

    
    private void addListeners() {
    }
    
    private void createSplitPane() {
        // Put tree and table in a split pane mSplitPane
        entityTreePane = new JScrollPane();
        entityTreePane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "LBL_Object_Definition")));
        entityPropertyPane.setViewportView(jLabelNoProperties);
        entityPropertyPane.setBorder(new javax.swing.border.TitledBorder(
                    new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                    NbBundle.getMessage(EditorMainPanel.class, "MSG_Properties")));
        
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
    
    private class EntityTreeKeyAdapter extends java.awt.event.KeyAdapter {
        /**
         * key typed
         * @param evt event
         */
        @Override
        public void keyPressed(KeyEvent evt) {

            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {

            }
        }
    }
}
