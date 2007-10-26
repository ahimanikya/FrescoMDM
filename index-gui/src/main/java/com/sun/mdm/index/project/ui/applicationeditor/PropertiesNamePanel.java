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

import javax.swing.JLabel;
import javax.swing.JTextField;

public class PropertiesNamePanel extends javax.swing.JPanel {
    // Variables declaration - do not modify
    private JLabel jLabelName;
    private JTextField txtName;
    private EntityNode mEntityNode;

    /** Creates a new instance of PropertiesNamePanel */
    public PropertiesNamePanel(EntityNode node, boolean bCheckedOut) {
        mEntityNode = node;
        initComponents();
        setNameProperty(node.getName());
        txtName.setEnabled(bCheckedOut);
    }
    
    private void initComponents() {
        setLayout(null);
        jLabelName = new JLabel();
        txtName = new JTextField();
        jLabelName.setText(NbBundle.getMessage(PropertiesNamePanel.class,
                "MSG_Name"));
        if (mEntityNode.isPrimary()) {
            //txtName.setEnabled(false);
        }
        
        add(jLabelName);
        add(txtName);
        jLabelName.setBounds(5, 30, 80, 20);
        txtName.setBounds(90, 30, 230, 20);

        txtName.addFocusListener(new java.awt.event.FocusListener() {
                String oldName;
                public void focusGained(java.awt.event.FocusEvent ev) {
                    oldName = txtName.getText();
                    txtName.selectAll();
                }

                public void focusLost(java.awt.event.FocusEvent ev) {
                    String newName = txtName.getText();
                    if (mEntityNode.checkNodeNameValue(newName)) {
                        mEntityNode.setNodeName(newName);
                    } else {
                        txtName.setText(oldName);
                        txtName.requestFocus();
                    }
                }
            });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onNameKeyReleased(evt);
            }
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onNameKeyTyped(evt);
            }
        });
    }

    private void onNameKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != '\b' || c == ' ') {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
    
    private void onNameKeyReleased(java.awt.event.KeyEvent evt) {
        //String newName = txtName.getText();
    }
    
    /** 
     *@param name
     */
    public void setNameProperty(String name) {
        if (!name.equals(txtName.getText())) {
            txtName.setText(name);
        }
    }
    
    /** 
     *@return name
     */
    public String getNameProperty() {
        return txtName.getText();
    }
}
