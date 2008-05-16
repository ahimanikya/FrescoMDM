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

import org.openide.util.NbBundle;
import com.sun.mdm.index.project.ui.wizards.generator.ConfigGenerator;
import com.sun.mdm.index.project.ui.wizards.generator.MatchType;
import com.sun.mdm.index.parser.FieldDef;

public class TabGeneralPropertiesPanel extends javax.swing.JPanel {
    private String mOriginalDataSize;
    private String MATCH_TYPE_NONE = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle").getString("MSG_None");
    private EntityNode mEntityNode;
    
    /** Creates new form TabGeneralPropertiesPanel */
    public TabGeneralPropertiesPanel(EntityNode entityNode) {
        this.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(TabGeneralPropertiesPanel.class, 
                "MSG_Properties")); // NOI18N

        initComponents();
        mEntityNode = entityNode;
        cbDataType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    String type = (String) cbDataType.getSelectedItem();
                    if (type.equals("char") || type.equals("boolean")) {
                        setDataSize(1);
                        spFieldSize.setEnabled(false);
                    } else {
                        spFieldSize.setEnabled(true);
                    }
                }
            });
        MatchType[] matchTypes;
        matchTypes = ConfigGenerator.getMatchTypes(DefineDeploymentPanel.matchEngine);
        setMatchTypeComboBox(matchTypes);
        
        addListeners1();
        loadProperties(entityNode.getFieldDef());
        // add other listeners after properties are loaded
        addListeners2();
    }
    
    private void addListeners1() {
        txtName.addFocusListener(new java.awt.event.FocusListener() {
                String oldName;
                public void focusGained(java.awt.event.FocusEvent ev) {
                    oldName = txtName.getText();
                }

                public void focusLost(java.awt.event.FocusEvent ev) {
                    String newName = txtName.getText();
                    if (!oldName.equals(newName) && com.sun.mdm.index.project.ui.applicationeditor.EntityNode.checkNodeNameValue(newName)) {
                        txtName.setText(newName);
                        mEntityNode.setNodeName(newName);
                    }
                }
            });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onNameKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        cbDataType.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onNameKeyTyped(evt);}
        });
    }
    
    private void addListeners2() {
        cbMatchType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                String matchType = (String) cbMatchType.getSelectedItem();
                if (matchType == null) {
                    matchType = MATCH_TYPE_NONE;
                }

                if (matchType.equals(MATCH_TYPE_NONE)) {
                    // disable Matching Tab
                    //btnMatchTypeAutoGen.setEnabled(false);
                } else {
                    // enable Matching Tab
                    //btnMatchTypeAutoGen.setEnabled(true);
                }
            }
        });
        
        this.cbDataType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
            }
        });
               
        this.spFieldSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                if (!mOriginalDataSize.equals(getDataSize())) {
                    
                }
            }
        });
        
        this.txtPattern.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtCodeModule.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    // Allow regex patterns
                    onTextFieldKeyTyped(evt);
                }
        });
        
        this.txtCodeModule.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtUserCode.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        this.txtUserCode.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtConstrainedBy.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        this.txtConstrainedBy.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtName.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
    }
    
    /**
     *param FieldDef
     */
    public void loadProperties(FieldDef fieldDef) {
        if (fieldDef != null) {
            setFieldName(fieldDef.getFieldName());
            setDataType(fieldDef.getFieldType());
            String matchType = fieldDef.getMatchType();
            if (matchType != null) {
                cbMatchType.setSelectedItem(matchType);
            } else {
                cbMatchType.setSelectedIndex(0);
            }
            setBlockOn(fieldDef.isBlockOn());
            setKeyType(fieldDef.isKeyType());
            setUpdateable(fieldDef.isUpdateable());
            setRequired(fieldDef.isRequired());
            setDataSize(fieldDef.getFieldSize());
            setPattern(fieldDef.getPattern());
            setCodeModule(fieldDef.getCodeModule());
            setUserCode(fieldDef.getUserCode());
            setConstraintBy(fieldDef.getConstraintBy());
        } else {
            // set defaults
        }
    }
    
    private void ConfigureBlockingActionPerformed(java.awt.event.ActionEvent evt) {
        //java.awt.Frame parentWindow = (java.awt.Frame) WindowManager.getDefault().getMainWindow();
        //BlockingConfigurationPanel mBlockingConfigurationPanel = new BlockingConfigurationPanel(parentWindow);
        //mBlockingConfigurationPanel.show();
        //EviewEditorMainPanel.invokeBlockingAction();
    }

    private void onNameKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != '\b' && c != '\n' || c == ' ') {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
    
    private void onNameKeyReleased(java.awt.event.KeyEvent evt) {
        //String newName = txtName.getText();
    }

    /*
     * Check if char entered will cause xml to break
     * such as < > /
     */
    private void onTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c) && 
                c != ' ' &&
                c != '-' && c != '_' && 
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
     *@return DataType
     */
    public void setFieldName(String name) {
        if (!name.equals(txtName.getText())) {
            txtName.setText(name);
        }
    }

    /**
     *@return Field Name
     */
    public String getFieldName() {
        return this.txtName.getText();
    }

    /**
     *@return DataType
     */
    private void setDataType(String dataType) {
        if (dataType.equals("byte")) {
            dataType = "char";
        }
        this.cbDataType.setSelectedItem(dataType);
    }

    /**
     *@return DataType
     */
    public String getDataType() {
        String dataType = this.cbDataType.getSelectedItem().toString();
        return dataType;
    }
   
    /**
     * Should be called from TabMatchConfigPanel when match type list is changed
     *@param alMatchTypes
     */
    public void setMatchTypeComboBox(MatchType[] matchTypes) {
        cbMatchType.removeAllItems();
        cbMatchType.addItem(MATCH_TYPE_NONE);
        if (matchTypes != null) {
            for (int i = 0; i < matchTypes.length; i++) {
                cbMatchType.addItem(matchTypes[i].getMatchTypeID());
            }
        }
    }
    
    private String getEnterpriseFieldName(EntityNode parentNode, String fieldName) {
        String fullFieldName;
        if (parentNode.isSub()) {
            EntityNode grandParentNode = (EntityNode) parentNode.getParent();
            fullFieldName = "Enterprise.SystemSBR." + grandParentNode.getName() + "." + parentNode.getName() + "[*]." + fieldName;
        } else {
            fullFieldName = "Enterprise.SystemSBR." + parentNode.getName() + "." + fieldName;
        }
        return fullFieldName;
    }
    
    /**
     *@return MatchType
     */
    public String getMatchType() {
        String matchType = (String) this.cbMatchType.getSelectedItem();
        if (matchType == null) {
            matchType = MATCH_TYPE_NONE;
        }

        return matchType;
    }

    /**
     *@return Blocking
     */
    public void setBlockOn(boolean bBlocking) {
        jComboBoxBlocking.setSelectedItem(bBlocking ? "true" : "false");
    }

    /**
     *@return Blocking
     */
    public String getBlocking() {
        return jComboBoxBlocking.getSelectedItem().toString();
    }

    /**
     *@param keyType
     */
    private void setKeyType(boolean keyType) {
        jComboBoxKeyType.setSelectedItem(keyType ? "true" : "false");
    }
    /**
     *@return KeyType
     */
    public String getKeyType() {
        return jComboBoxKeyType.getSelectedItem().toString();
    }

    /**
     *@param Required
     */
    private void setRequired(boolean required) {
        jComboBoxRequired.setSelectedItem(required ? "true" : "false");
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return jComboBoxRequired.getSelectedItem().toString();
    }

    /**
     *@param Updateable
     */
    private void setUpdateable(boolean updateable) {
        jComboBoxUpdateable.setSelectedItem(updateable ? "true" : "false");
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return jComboBoxUpdateable.getSelectedItem().toString();
    }

    /**
     *@param DataSize
     */
    private void setDataSize(int dataSize) {
        mOriginalDataSize = String.valueOf(dataSize);
        Integer size = Integer.valueOf(mOriginalDataSize);
        spFieldSize.setValue(size);
    }

    /**
     *@return DataSize
     */
    public String getDataSize() {
        String dataType = this.cbDataType.getSelectedItem().toString();
        if (dataType.equals("char")) {
            return "1";
        }

        return this.spFieldSize.getValue().toString();
    }

    /**
     *@param Pattern
     */
    private void setPattern(String pattern) {
        this.txtPattern.setText(pattern);
    }

    /**
     *@return Pattern
     */
    public String getPattern() {
        return this.txtPattern.getText();
    }

    /**
     *@param Code Module
     */
    private void setCodeModule(String codeModule) {
        this.txtCodeModule.setText(codeModule);
    }

    /**
     *@return Code Module
     */
    public String getCodeModule() {
        return this.txtCodeModule.getText();
    }

    /**
     *@param User Code
     */
    private void setUserCode(String userCode) {
        this.txtUserCode.setText(userCode);
    }

    /**
     *@return User Code
     */
    public String getUserCode() {
        return this.txtUserCode.getText();
    }

    /**
     *@param Constrained By
     */
    private void setConstraintBy(String constrainedBy) {
        this.txtConstrainedBy.setText(constrainedBy);
    }

    /**
     *@return Constrained By
     */
    public String getConstraintBy() {
        return this.txtConstrainedBy.getText();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabelDataType = new javax.swing.JLabel();
        cbDataType = new javax.swing.JComboBox();
        jLabelMatchType = new javax.swing.JLabel();
        cbMatchType = new javax.swing.JComboBox();
        jLabelBlocking = new javax.swing.JLabel();
        jLabelKeyType = new javax.swing.JLabel();
        jLabelUpdateable = new javax.swing.JLabel();
        jLabelRequired = new javax.swing.JLabel();
        jLabelFieldSize = new javax.swing.JLabel();
        spFieldSize = new javax.swing.JSpinner();
        jLabelPattern = new javax.swing.JLabel();
        txtPattern = new javax.swing.JTextField();
        jLabelCodeModule = new javax.swing.JLabel();
        txtCodeModule = new javax.swing.JTextField();
        jLabelUserCode = new javax.swing.JLabel();
        txtUserCode = new javax.swing.JTextField();
        jLabelConstrainedBy = new javax.swing.JLabel();
        txtConstrainedBy = new javax.swing.JTextField();
        jComboBoxBlocking = new javax.swing.JComboBox();
        jComboBoxKeyType = new javax.swing.JComboBox();
        jComboBoxUpdateable = new javax.swing.JComboBox();
        jComboBoxRequired = new javax.swing.JComboBox();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabelName.setLabelFor(txtName);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle"); // NOI18N
        jLabelName.setText(bundle.getString("MSG_Name")); // NOI18N

        jLabelDataType.setLabelFor(cbDataType);
        jLabelDataType.setText(bundle.getString("MSG_DataType")); // NOI18N

        cbDataType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "string", "char", "date", "int", "float", "boolean" }));

        jLabelMatchType.setLabelFor(cbMatchType);
        jLabelMatchType.setText(bundle.getString("MSG_MatchType")); // NOI18N

        jLabelBlocking.setLabelFor(jComboBoxBlocking);
        jLabelBlocking.setText(bundle.getString("MSG_Blocking")); // NOI18N

        jLabelKeyType.setLabelFor(jComboBoxKeyType);
        jLabelKeyType.setText(bundle.getString("MSG_KeyType")); // NOI18N

        jLabelUpdateable.setLabelFor(jComboBoxUpdateable);
        jLabelUpdateable.setText(bundle.getString("MSG_Updateable")); // NOI18N

        jLabelRequired.setLabelFor(jComboBoxRequired);
        jLabelRequired.setText(bundle.getString("MSG_Required")); // NOI18N

        jLabelFieldSize.setLabelFor(spFieldSize);
        jLabelFieldSize.setText(bundle.getString("MSG_FieldSize")); // NOI18N

        jLabelPattern.setLabelFor(txtPattern);
        jLabelPattern.setText(bundle.getString("MSG_Pattern")); // NOI18N

        jLabelCodeModule.setLabelFor(txtCodeModule);
        jLabelCodeModule.setText(bundle.getString("MSG_CodeModule")); // NOI18N

        txtCodeModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodeModuleActionPerformed(evt);
            }
        });

        jLabelUserCode.setLabelFor(txtUserCode);
        jLabelUserCode.setText(bundle.getString("MSG_UserCode")); // NOI18N

        jLabelConstrainedBy.setLabelFor(txtConstrainedBy);
        jLabelConstrainedBy.setText(bundle.getString("MSG_ConstrainedBy")); // NOI18N

        jComboBoxBlocking.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "true", "false" }));
        jComboBoxBlocking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBlockingActionPerformed(evt);
            }
        });

        jComboBoxKeyType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "true", "false" }));

        jComboBoxUpdateable.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "true", "false" }));

        jComboBoxRequired.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "true", "false" }));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabelUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabelConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabelPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelUpdateable, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelKeyType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(jLabelRequired, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelBlocking, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelMatchType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelDataType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabelName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cbDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, spFieldSize)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBoxRequired, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBoxUpdateable, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBoxKeyType, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBoxBlocking, 0, 56, Short.MAX_VALUE)))))
                .add(76, 76, 76))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelBlocking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxBlocking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelKeyType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxKeyType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelUpdateable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxUpdateable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelRequired, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxRequired, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(spFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodeModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodeModuleActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_txtCodeModuleActionPerformed

private void jComboBoxBlockingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBlockingActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jComboBoxBlockingActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbDataType;
    private javax.swing.JComboBox cbMatchType;
    private javax.swing.JComboBox jComboBoxBlocking;
    private javax.swing.JComboBox jComboBoxKeyType;
    private javax.swing.JComboBox jComboBoxRequired;
    private javax.swing.JComboBox jComboBoxUpdateable;
    private javax.swing.JLabel jLabelBlocking;
    private javax.swing.JLabel jLabelCodeModule;
    private javax.swing.JLabel jLabelConstrainedBy;
    private javax.swing.JLabel jLabelDataType;
    private javax.swing.JLabel jLabelFieldSize;
    private javax.swing.JLabel jLabelKeyType;
    private javax.swing.JLabel jLabelMatchType;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPattern;
    private javax.swing.JLabel jLabelRequired;
    private javax.swing.JLabel jLabelUpdateable;
    private javax.swing.JLabel jLabelUserCode;
    private javax.swing.JSpinner spFieldSize;
    private javax.swing.JTextField txtCodeModule;
    private javax.swing.JTextField txtConstrainedBy;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPattern;
    private javax.swing.JTextField txtUserCode;
    // End of variables declaration//GEN-END:variables
    
}
