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

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.Component;

import org.openide.util.Utilities;

import com.sun.mdm.multidomain.parser.MiObject;
import com.sun.mdm.multidomain.parser.MiNodeDef;
import com.sun.mdm.multidomain.parser.MiFieldDef;
import com.sun.mdm.multidomain.util.Logger;

/*
 * EntityTree.java
 *
 */
public class EntityTree extends JTree {
    /** The logger. */
    private static Logger mLog = Logger.getLogger(EntityTree.class.getName());

    static final ImageIcon ROOTIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/DomainNode.png"));
    static final ImageIcon PRIMARYNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/PrimaryObject.png"));
    static final ImageIcon SUBNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/SubObject.png"));
    static final ImageIcon FIELDNODEIMAGEICON = new ImageIcon(Utilities.loadImage(
                "com/sun/mdm/multidomain/project/resources/Field.png"));

    private MiObject mMiObject;
    private static EntityNode mRootNode;
    private EntityNode mPrimaryNode;
    private String mDomainName;
    
   
    /** Creates a new instance of EntityTree */
    public EntityTree(MiObject miObject) {
        super();
        mMiObject = miObject;
        mDomainName = miObject.getApplicationName();
        createEntityTree();
    }
    
    private void createEntityTree() {
        mRootNode = new EntityNode(this, mDomainName, EntityNode.getRootNodeType());
        ((DefaultTreeModel) this.getModel()).setRoot(mRootNode);       
        this.setEditable(false);
        this.setDragEnabled(true);
        this.setBorder(new javax.swing.border.TitledBorder(""));
        this.setCellRenderer(new EntityTreeRenderer());
        this.addKeyListener(new EntityTreeKeyAdapter());
        try {
            loadEntityTree(mMiObject.getNodes());  
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
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
                MiNodeDef node = (MiNodeDef) it.next();
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

    public EntityNode getRootNode() {
        return mRootNode;
    }
    
    public EntityNode addPrimaryEntity(String name) {
        // add a 1st level node to the tree
        String nodeName = name;
        EntityNode childNode = new EntityNode(this, nodeName, EntityNode.getPrimaryNodeType());
        ((DefaultTreeModel) this.getModel()).insertNodeInto(childNode, mRootNode, mRootNode.getChildCount());
        
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

            expandAll(this, path, false);

            TreePath selectionPath = new TreePath(childNode.getPath());
            this.scrollPathToVisible(selectionPath);
            this.setSelectionPath(selectionPath);
            this.startEditingAtPath(selectionPath);

            //mSplitPane.setRightComponent(childNode.getConfigPropertySheet());
        }
        return childNode;
    }


    public void addField(MiFieldDef fieldDef) {
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
                childNode = new EntityNode(this, nodeName, fieldDef, false);
            } else {
                nodeName = parentNode.getName() + EntityNode.getFieldNodeType() + fieldCnt;
                childNode = new EntityNode(this, nodeName, nodeName, EntityNode.getFieldNodeType(), 
                                       "string", 1, "", "", false, false, false,
                                       false, false, "None", 32, "", "", false, "", "", false); 
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
        }
    }

    public void addFieldNodes(MiNodeDef node, EntityNode currentNode) {
        TreePath selectionPath = new TreePath(currentNode.getPath());
        ArrayList fields = node.getFields();
        for (int i = 0; i < fields.size(); i++) {
            MiFieldDef field = (MiFieldDef) fields.get(i);
            addField(field);
            this.setSelectionPath(selectionPath);
        }
    }
    
    private class EntityTreeKeyAdapter extends KeyAdapter {
        /**
         * key typed
         * @param evt event
         */
        public void keyPressed(KeyEvent evt) {
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
                setIcon(ROOTIMAGEICON);
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
}
