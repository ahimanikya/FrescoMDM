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

import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import java.util.EventObject;
import java.awt.event.MouseEvent;
import javax.swing.tree.TreePath;
import javax.swing.JTree;

public class EntityTreeCellEditor extends DefaultTreeCellEditor {

    private JTree mTree;

    /**
     * Constructor
     *
     * @param tree to be editing
     * @param renderer of the tree to be editing
     */
    public EntityTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
        mTree = tree;
    }

    /**
     * The cell is only editable if it is a leaf node and
     * the number of mouse clicks is equal to the value
     * returned from the getClickCountToStart method.
     * <p>
     * @param event The event to test for cell editablity.
     * @return True if the cell is editable given the current event.
     **/
    public boolean isCellEditable(EventObject event) {
        boolean bEditable = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getClickCount() == 2) {
                TreePath hitPath = tree.getPathForLocation(
                    mouseEvent.getX(),
                    mouseEvent.getY());
                EntityNode treeNode = (EntityNode) hitPath.getLastPathComponent();
                if (treeNode != null && !treeNode.isRoot()) {
                    bEditable = true;
                }
            }
        }
        return bEditable;
    }

    /**
     * Returns the cell editor value
     *
     * @return user object of the tree node
     */
    public Object getCellEditorValue() {
        EntityNode treeNode =
            (EntityNode) mTree.getLastSelectedPathComponent();

        if (treeNode == null) {
            return null;
        }

        //Object userObj = treeNode.getUserObject();

            Object value = super.getCellEditorValue();
            treeNode.setName((String) value);

        return value;
    }

    /**
     * This is invoked if a <code>TreeCellEditor</code> is not supplied
     * in the constructor. It returns a <code>TextField</code> editor.
     *
     * @return   a new <code>TextField</code> editor
     */
    protected TreeCellEditor createTreeCellEditor() {
        TreeCellEditor editor = super.createTreeCellEditor();

        return editor;
    }
}
