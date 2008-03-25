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

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.Component;

import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;

import com.sun.mdm.index.parser.NodeDef;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.index.util.Logger;

/*
 * EntityTree.java
 *
 */
public class EntityTree extends JTree implements CellEditorListener {
    /** The logger. */
    private static Logger mLog = Logger.getLogger(EntityTree.class.getName());

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
    static final ImageIcon TABDEPLOYMENTIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/PropertiesStandardization.png"));
    static final ImageIcon TABBLOCKINGIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/PropertiesStandardization.png"));
    static final ImageIcon TABSTANDARDIZATIONIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/PropertiesStandardization.png"));
    static final ImageIcon TABNORMALIZATIONIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/index/project/ui/applicationeditor/PropertiesNormalization.png"));

    //private static JTree this = null;
    private EviewApplication mEviewApplication;
    private EviewEditorMainApp mEviewEditorMainApp;
    private static EntityNode mRootNode;
    private EntityNode mPrimaryNode;
    private String mEviewApplicationName;
    private boolean bCheckedOut;
    
    final JTextField mText = new JTextField();
    EntityNode mPreviousSelectedNode = null;
    //String mOldPrimaryObjName = null;
    String mOldText = null;
    
    /** Creates a new instance of EntityTree */
    public EntityTree(EviewEditorMainApp eviewEditorMainApp, EviewApplication eviewApplication) {
        super();
        try {
            mEviewEditorMainApp = eviewEditorMainApp;
            mEviewApplication = eviewApplication;
            bCheckedOut = eviewEditorMainApp.isCheckedOut();
            mEviewApplicationName = eviewApplication.getApplicationName();
            createEntityTree();
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
    }
    
    private void createEntityTree() {
        mRootNode = new EntityNode(this, mEviewApplicationName, EntityNode.getRootNodeType());
        ((DefaultTreeModel) this.getModel()).setRoot(mRootNode);       
        this.setEditable(bCheckedOut);
        this.setDragEnabled(true);
        this.setBorder(new javax.swing.border.TitledBorder(""));
        this.setCellRenderer(new EntityTreeRenderer());
        this.addKeyListener(new EntityTreeKeyAdapter());
        try {
            loadEntityTree(mEviewApplication.getEIndexObject(true).getNodes());  
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        addCellEditor();
    }

    public void setDragSource() {
        //this.setDragSource(true);
        //this.setDropTarget(false);
        //mDragSource = DragSource.getDefaultDragSource();
        //mTreeDnDAdapter = new EntityTreeDnDAdapter(this);
        //mDragSource.addDragSourceListener(mTreeDnDAdapter);
    }
    
    /** set Entity Tree
     *@param  alEoNodes Eo Nodes (array of Eo nodes)
     */
    private void loadEntityTree(ArrayList alEoNodes) {       
        if ((alEoNodes != null) && (alEoNodes.size() > 0)) {
            boolean bPrimaryNode = true;
            EntityNode currentNode;
            TreePath selectionPath = new TreePath(mRootNode.getPath());

            Iterator it = alEoNodes.iterator();
            while (it.hasNext()) {
                NodeDef node = (NodeDef) it.next();
                if (bPrimaryNode) { // Assume 1st node is the Primary
                    currentNode = addPrimaryEntity(node.getTag());
                    bPrimaryNode = false;
                } else {
                    TreePath primaryNodePath = new TreePath(mPrimaryNode.getPath());
                    this.setSelectionPath(primaryNodePath);
                    currentNode = addSubEntity(node.getTag());
                }
                addFieldNodes(node, currentNode);
                this.setSelectionPath(selectionPath);
            }
        }
        this.collapsePath(new TreePath(mPrimaryNode.getPath()));
    }
    
    private void addCellEditor() {
        mText.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent ev) {
                    mText.selectAll();
                    mOldText = mText.getText();
                }

                public void focusLost(FocusEvent ev) {
                    editingStopped(null);
                }
            });
        DefaultCellEditor dce = new DefaultCellEditor(mText) {
            @Override
            public boolean isCellEditable(java.util.EventObject event) {
                boolean bEditable = false;
                if (event instanceof MouseEvent) {
                    MouseEvent mouseEvent = (MouseEvent) event;
                    TreePath hitPath = getPathForLocation(
                            mouseEvent.getX(),
                            mouseEvent.getY());
                    if (hitPath != null) {
                        EntityNode entityNode = (EntityNode) hitPath.getLastPathComponent();
                        if (entityNode != null && 
                            !entityNode.isRoot() && 
                            //!entityNode.isPrimary() &&
                            mPreviousSelectedNode == entityNode) {
                            bEditable = true;
                        }
                    }
                }
                return bEditable && bCheckedOut;
            }
        };
        dce.addCellEditorListener(this);
        this.setCellEditor(dce);
    }
    
    public void setPreviousSelectedNode(EntityNode selectedNode) {
        mPreviousSelectedNode = selectedNode;
    }

    public EntityNode getRootNode() {
        return mRootNode;
    }
    
    public EviewEditorMainApp getEviewEditorMainApp() {
        return mEviewEditorMainApp;
    }
    
    public EntityNode addPrimaryEntity(String name) {
        // add a 1st level node to the tree
        String nodeName = name;
        if (name == null) {
            nodeName = mEviewApplicationName;
        }
        EntityNode childNode = new EntityNode(this, nodeName, EntityNode.getPrimaryNodeType());
        ((DefaultTreeModel) this.getModel()).insertNodeInto(childNode, mRootNode, mRootNode.getChildCount());
        if (mEviewEditorMainApp.getEviewEditorMainPanel() != null) {
            mEviewEditorMainApp.enableSaveAction(true);
            mEviewApplication.setModified(true);
        }
        
        TreePath selectionPath = new TreePath(childNode.getPath());
        expandAll(this, selectionPath, false);
        this.scrollPathToVisible(selectionPath);
        this.setSelectionPath(selectionPath);
        this.startEditingAtPath(selectionPath);

        //mSplitPane.setRightComponent(childNode.getConfigPropertySheet());
        mPrimaryNode = childNode;
        return childNode;
    }

    public EntityNode addSubEntity(String name) {
        EntityNode childNode = null;
        TreePath path = this.getAnchorSelectionPath();

        if (path != null) {
            EntityNode parentNode = (EntityNode) path.getLastPathComponent();
            String nodeName = name;
            if (nodeName == null || nodeName.length() == 0) {
                nodeName = EntityNode.getSubNodeType() +
                    (parentNode.getChildCount() - parentNode.getFieldCnt());
            }
            // add a sub node to the current path
            childNode = new EntityNode(this, nodeName, EntityNode.getSubNodeType());
            ((DefaultTreeModel) this.getModel()).insertNodeInto(childNode, parentNode,
                                                                parentNode.getChildCount());
            if (mEviewEditorMainApp.getEviewEditorMainPanel() != null) {
                mEviewEditorMainApp.enableSaveAction(true);
                mEviewApplication.setModified(true);
            }

            expandAll(this, path, false);

            TreePath selectionPath = new TreePath(childNode.getPath());
            this.scrollPathToVisible(selectionPath);
            this.setSelectionPath(selectionPath);
            this.startEditingAtPath(selectionPath);

            //mSplitPane.setRightComponent(childNode.getConfigPropertySheet());
        }
        return childNode;
    }

    public void addField(FieldDef fieldDef) {
        // add a field node to the current level (Primary or nested object)
        TreePath path = this.getAnchorSelectionPath();

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

            EntityNode childNode = null;
            String nodeName = null;
            if (fieldDef != null) {
                nodeName = fieldDef.getFieldName();
                boolean bBlockOn = false;

                try {
                    if (mEviewApplication.getQueryType(false) != null) {
                        ArrayList alBlockingSources = mEviewApplication.getQueryType(false).getBlockingSources();
                        if (alBlockingSources != null) {
                            String strField = parentNode.getName() + "." + nodeName;
                            if (parentNode.isSub()) {
                                strField = mPrimaryNode.getName() + "." +  strField;
                            }
                            bBlockOn = alBlockingSources.contains(strField);
                        }
                    }
                    childNode = new EntityNode(this, nodeName, fieldDef, bBlockOn);
                } catch (Exception ex) {
                    mLog.severe(ex.getMessage());
                }
            } else {
                nodeName = parentNode.getName() + EntityNode.getFieldNodeType() + fieldCnt;
                childNode = new EntityNode(this, nodeName, nodeName, EntityNode.getFieldNodeType(), 
                                       "string", 1, "", "", false, false, false,
                                       false, false, "None", 32, "", "", false, "", "", false); 
                childNode.setJustAdded();
           }

            // Insert new node after the last field node
            ((DefaultTreeModel) this.getModel()).insertNodeInto(childNode, parentNode, insertIdx);
            
            if (fieldDef == null) {
                expandAll(this, path, false);

                TreePath selectionPath = new TreePath(childNode.getPath());
                this.scrollPathToVisible(selectionPath);
                this.setSelectionPath(selectionPath);
                this.startEditingAtPath(selectionPath);

                //mSplitPane.setRightComponent(childNode.getConfigPropertySheet());
            }
            if (mEviewEditorMainApp.getEviewEditorMainPanel() != null) {
                mEviewEditorMainApp.enableSaveAction(true);
                mEviewApplication.setModified(true);
            }
        }
    }

    public void addFieldNodes(NodeDef node, EntityNode currentNode) {
        TreePath selectionPath = new TreePath(currentNode.getPath());
        ArrayList fields = node.getFields();
        for (int i = 0; i < fields.size(); i++) {
            FieldDef field = (FieldDef) fields.get(i);
            addField(field);
            this.setSelectionPath(selectionPath);
        }
    }

    public void deleteNode() {
        TreePath path = this.getAnchorSelectionPath();
        EntityNode nodeToBeDeleted = (EntityNode) path.getLastPathComponent();
        if (nodeToBeDeleted.isRoot()) { // || nodeToBeDeleted.isPrimary()) {
            return;
        }
        String prompt = NbBundle.getMessage(EntityTree.class, "MSG_Confirm_Remove_Node_Prompt");


        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                 prompt, 
                                 NbBundle.getMessage(EntityTree.class, "MSG_Confirm_Remove_Node_Title"), 
                                 NotifyDescriptor.YES_NO_OPTION);
        if (DialogDisplayer.getDefault().notify(d) == NotifyDescriptor.YES_OPTION) {
        
            //remove all cross-referenced in various tabs
            mRootNode.removeReferencedField(nodeToBeDeleted);

            EntityNode parentNode = (EntityNode) nodeToBeDeleted.getParent();
            int newSelectedNodeIndex = parentNode.getIndex(nodeToBeDeleted) - 1;
            ((DefaultTreeModel) this.getModel()).removeNodeFromParent(nodeToBeDeleted);
            if (mEviewEditorMainApp.getEviewEditorMainPanel() != null) {
                mEviewEditorMainApp.enableSaveAction(true);
                mEviewApplication.setModified(true);
            }

            // set new selection node
            EntityNode newSelectedNode = parentNode;

            if (newSelectedNodeIndex >= 0) {
                newSelectedNode = (EntityNode) parentNode.getChildAt(newSelectedNodeIndex);
            }

            TreePath selectionPath = new TreePath(newSelectedNode.getPath());
            this.scrollPathToVisible(selectionPath);
            this.setSelectionPath(selectionPath);
        }
    }
    
    private class EntityTreeKeyAdapter extends KeyAdapter {
        /**
         * key typed
         * @param evt event
         */
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                deleteNode();
            }
        }
    }
    
    private class EntityTreeRenderer extends DefaultTreeCellRenderer {
        public EntityTreeRenderer() {
        }

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

    /* CellEditorListener */
    /** This tells the listeners the editor has canceled editing  */
    public void editingCanceled(ChangeEvent e) {
    }
    
    /** This tells the listeners the editor has ended editing  */
    public void editingStopped(ChangeEvent e) {
        if (e != null) { // for now, only for call from FocusLost
            // but we really need not to check against self
            return;
        }
        String value = mText.getText();
        if (mOldText.equals(value)) {
            return;
        }
        TreePath path = this.getAnchorSelectionPath();
        EntityNode selectedNode = (EntityNode) path.getLastPathComponent();
        /* Commented out for now
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
                        this.startEditingAtPath(path);
                        mText.setText(mOldPrimaryObjName);
                        mText.selectAll();
                    }
                    return;
                }
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
        }
         */

        boolean bValidName = EntityNode.checkNodeNameValue(value);
        if (!bValidName) {
            mText.setText(mOldText);
            selectedNode.setName(mOldText);
            this.startEditingAtPath(path);
            ((DefaultTreeModel) this.getModel()).reload(selectedNode);
        } else {
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
                String msg = "\"" + value + "\" " + 
                        NbBundle.getMessage(EntityTree.class, "MSG_Field_Name_Has_Been_Used");                
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
                mText.setText(selectedNode.getName());
                selectedNode.setName(selectedNode.getName());
                ((DefaultTreeModel) this.getModel()).reload();
                this.scrollPathToVisible(path);
                this.startEditingAtPath(path);
                this.setSelectionPath(path);
            } else {
                selectedNode.setNodeName(value);
            }
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
    
    //public void valueChanged(TreeSelectionEvent e) {
    //}
    
    public void setSelectedNode(String targetFieldName) {
        if (targetFieldName == null || targetFieldName.length() == 0) {
            return;
        }
        String temp = targetFieldName.replace('.', ':'); // regex '.' is predefined for any character
        String[] s = temp.split(":");
        String[] p = new String[s.length + 1];
        for (int i=0; s != null && i < s.length; i++) {
            String nodeName = s[i];
            int j = nodeName.indexOf("[*]");
            if (j > 0) {
                s[i] = nodeName.substring(0, j);
            }
        }
        boolean isPrimary = s.length == 2;
        boolean isSub = s.length == 3;
        EntityNode currNode = (EntityNode) this.getRootNode().getChildAt(0);
        int cnt = currNode.getChildCount();
        EntityNode parentNode = null;
        EntityNode childNode = null;
        for (int k=0; k < cnt; k++) {
            EntityNode node = (EntityNode) currNode.getChildAt(k);
            if (isPrimary && node.isField() && node.getName().equals(s[s.length - 1])) {
                childNode = node;
                break;
            }
            if (isSub && node.isSub() && node.getName().equals(s[1])) {
                int cnt1 = node.getChildCount();
                for (int l=0; l < cnt1; l++) {
                    EntityNode node1 = (EntityNode) node.getChildAt(l);
                    if (node1.getName().equals(s[s.length - 1])) {
                        childNode = node1;
                        break;
                    }
                }
                break;
            }
        }
        if (childNode != null) {
            EntityNode parent = (EntityNode) childNode.getParent();
            expandAll(this, new TreePath(parent.getPath()), false);   
            TreePath selectionPath = new TreePath(childNode.getPath());    

            this.scrollPathToVisible(selectionPath);
            this.setSelectionPath(selectionPath);
            this.startEditingAtPath(selectionPath);            
        }
    }
    
    /**
     * @return instance of EviewApplication
     */
    public EviewApplication getEviewApplication() {
        return this.mEviewApplication;
    }
}
