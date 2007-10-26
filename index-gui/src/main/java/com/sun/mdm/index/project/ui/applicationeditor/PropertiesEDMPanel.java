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
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.sun.mdm.index.parser.EDMFieldDef;

/**
 * Not used, some EDM properties moved to TabGeneralPropertiesPanel
 * Keep this for future enhancements
 */
public class PropertiesEDMPanel extends javax.swing.JPanel {
    // Variables declaration - do not modify
    private JLabel jLabelDisplayName;
    private JLabel jLabelInputMask;
    private JLabel jLabelValueMask;
    private JLabel jLabelSearchScreen;
    private JLabel jLabelSearchRequired;
    private JLabel jLabelSearchResult;
    private JLabel jLabelGenerateReport;
    
    private JTextField txtDisplayName;
    private JTextField txtInputMask;
    private JTextField txtValueMask;
    private JCheckBox chkSearchScreen;
    private JCheckBox chkSearchResult;
    private JCheckBox chkGenerateReport;
    private JComboBox cbSearchRequired;
    private EntityNode mEntityNode;

 
    /** Creates a new instance of PropertiesEDMPanel */
    public PropertiesEDMPanel(EntityNode node, EDMFieldDef edmFieldDef) {
        mEntityNode = node;
        initComponents(edmFieldDef);
        addListeners();
    }
    
    private void initComponents(EDMFieldDef edmFieldDef) {
        jLabelDisplayName = new JLabel();
        jLabelInputMask = new JLabel();
        jLabelValueMask = new JLabel();
        jLabelSearchScreen = new JLabel();
        jLabelSearchRequired = new JLabel();
        jLabelSearchResult = new JLabel();
        jLabelGenerateReport = new JLabel();

        txtDisplayName = new JTextField();
        txtInputMask = new JTextField();
        txtValueMask = new JTextField();
        
        cbSearchRequired = new JComboBox();
        cbSearchRequired.addItem("true");
        cbSearchRequired.addItem("false");
        cbSearchRequired.addItem("oneof");
        
        chkSearchScreen = new JCheckBox();
        chkSearchScreen.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                if (!chkSearchScreen.isSelected()) {
                    cbSearchRequired.setSelectedItem("false");
                    cbSearchRequired.setEnabled(false);
                } else {
                    cbSearchRequired.setEnabled(true);
                }
            }
        });

        chkSearchResult = new JCheckBox();
        chkGenerateReport = new JCheckBox();

        if (edmFieldDef != null) {
            txtDisplayName.setText(edmFieldDef.getDisplayName());
            txtInputMask.setText(edmFieldDef.getInputMask());
            txtValueMask.setText(edmFieldDef.getValueMask());
            String strRequiredInSearch = edmFieldDef.getRequiredInSearchScreen();
            if (strRequiredInSearch == null || !edmFieldDef.getUsedInSearchScreen()) {
                strRequiredInSearch = "false";
            }
            cbSearchRequired.setSelectedItem(strRequiredInSearch);
            cbSearchRequired.setEnabled(edmFieldDef.getUsedInSearchScreen());
            chkSearchScreen.setSelected(edmFieldDef.getUsedInSearchScreen());
            chkSearchResult.setSelected(edmFieldDef.getUsedInSearchResult());
            chkGenerateReport.setSelected(edmFieldDef.getReport());
        } else { // It is generated node (through MatchType) or just added
            boolean isJustAdded = mEntityNode.isJustAdded();
            if (isJustAdded) {
                txtDisplayName.setText(mEntityNode.getName());            
            }
            txtDisplayName.setEnabled(isJustAdded);
            txtInputMask.setEnabled(isJustAdded);
            txtValueMask.setEnabled(isJustAdded);
            chkSearchScreen.setEnabled(isJustAdded);
            cbSearchRequired.setSelectedItem("false");
            cbSearchRequired.setEnabled(isJustAdded);
            chkSearchResult.setEnabled(isJustAdded);
            chkGenerateReport.setEnabled(isJustAdded);
        }

        setLayout(null);

        jLabelDisplayName.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_DisplayName"));
        jLabelInputMask.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_InputMask"));
        jLabelValueMask.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_ValueMask"));
        jLabelSearchScreen.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_SearchScreen"));
        jLabelSearchRequired.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_SearchRequired"));
        jLabelSearchResult.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_SearchResult"));
        jLabelGenerateReport.setText(NbBundle.getMessage(PropertiesEDMPanel.class,
                "MSG_GenerateReport"));
        
        add(jLabelDisplayName);
        add(jLabelInputMask);
        add(jLabelValueMask);
        add(jLabelSearchScreen);
        add(jLabelSearchRequired);
        add(jLabelSearchResult);
        add(jLabelGenerateReport);

        add(txtDisplayName);
        add(txtInputMask);
        add(txtValueMask);
        add(chkSearchScreen);
        add(cbSearchRequired);
        add(chkSearchResult);
        add(chkGenerateReport);
        
        jLabelDisplayName.setBounds(10, 10, 90, 20);
        jLabelInputMask.setBounds(10, 35, 90, 20);
        jLabelValueMask.setBounds(10, 60, 90, 20);
        jLabelSearchScreen.setBounds(10, 85, 90, 20);
        jLabelSearchRequired.setBounds(150, 85, 90, 20);
        jLabelSearchResult.setBounds(10, 110, 90, 20);
        jLabelGenerateReport.setBounds(10, 135, 90, 20);
        
        txtDisplayName.setBounds(100, 10, 200, 20);
        txtInputMask.setBounds(100, 35, 200, 20);
        txtValueMask.setBounds(100, 60, 200, 20);
        chkSearchScreen.setBounds(95, 85, 20, 20);
        cbSearchRequired.setBounds(210, 85, 90, 20);
        chkSearchResult.setBounds(95, 110, 20, 20);
        chkGenerateReport.setBounds(95, 135, 20, 20);
    }
    
    private void addListeners() {
        /*
        txtDisplayName.addFocusListener(new java.awt.event.FocusListener() {
                String oldName;
                public void focusGained(java.awt.event.FocusEvent ev) {
                    oldName = txtDisplayName.getText();
                }

                public void focusLost(java.awt.event.FocusEvent ev) {
                    String newName = txtDisplayName.getText();
                }
            });
         */
        txtDisplayName.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtInputMask.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        txtInputMask.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtValueMask.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        txtValueMask.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        chkSearchScreen.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        

        this.chkSearchScreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mEntityNode.updateCheckedAttributes("searchScreen", 
                                                    chkSearchScreen.isSelected(), 
                                                    chkSearchScreen.isSelected() ? cbSearchRequired.getSelectedItem().toString() : null);
                mEntityNode.enableSaveAction();
            }
        });
        
        this.chkSearchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mEntityNode.updateCheckedAttributes("searchResult", 
                                                    chkSearchResult.isSelected(), 
                                                    null);
                mEntityNode.enableSaveAction();
            }
        });
        
        this.chkGenerateReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mEntityNode.updateCheckedAttributes("report", 
                                                    chkGenerateReport.isSelected(), 
                                                    null);
                mEntityNode.enableSaveAction();
            }
        });
        
        this.cbSearchRequired.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                mEntityNode.enableSaveAction();
            }
        });
    }
    
    /*
     * Check if char entered will cause xml to break
     * such as < > /
     */
    private void onTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c) && 
                c != '-' && c != '_'&& 
                c != '(' && c != ')' && 
                c != '[' && c != ']' && 
                c != '{' && c != '}' && 
                c != '\b' && c != '\n') {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
    
    private void onTextFieldKeyReleased(java.awt.event.KeyEvent evt) {
    }

    /**
     *@return DisplayName
     */
    public String getDisplayName() {
        return txtDisplayName.getText();
    }

    /**
     *@return InputMask
     */
    public String getInputMask() {
        return txtInputMask.getText();
    }

    /**
     *@return ValueMask
     */
    public String getValueMask() {
        return txtValueMask.getText();
    }

    /**
     *@param order gui display order
     */
    public void setDisplayOrder(int order) {
        //mDisplayOrder = order;
    }

    /**
     *@return DisplayOrder
     */
    public int getDisplayOrder() {
        return 0;
    }

    /**
     *@return UsedInSearchScreen
     */
    public boolean getUsedInSearchScreen() {
        return chkSearchScreen.isSelected();
    }

    /**
     *@return SearchRequired
     */
    public String getSearchRequired() {
        return this.cbSearchRequired.getSelectedItem().toString();
    }

    /**
     *@return DisplayedInSearchResult
     */
    public boolean getDisplayedInSearchResult() {
        return chkSearchResult.isSelected();
    }

    /**
     *@return GenerateReport
     */
    public boolean getGenerateReport() {
        return chkGenerateReport.isSelected();
    }
}
