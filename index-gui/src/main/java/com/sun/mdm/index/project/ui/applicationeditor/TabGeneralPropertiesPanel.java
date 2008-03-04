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

import java.util.ArrayList;

import com.sun.mdm.index.project.EviewApplication;
import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.EDMFieldDef;
import com.sun.mdm.index.parser.MatchFieldDef;

public class TabGeneralPropertiesPanel extends javax.swing.JPanel {
    private EviewApplication mEviewApplication;
    private EviewEditorMainApp mEviewEditorMainApp;
    private EntityNode mEntityNode;
    private MatchFieldDef mMatchFieldDef;
    private String mOriginalDataSize;
    private String MATCH_TYPE_NONE = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle").getString("MSG_None");
    private boolean bCheckedOut;
    
    /** Creates new form TabGeneralPropertiesPanel */
    public TabGeneralPropertiesPanel(EviewApplication eviewApplication, EviewEditorMainApp eviewEditorMainApp, EntityNode entityNode, EDMFieldDef edmFieldDef) {
        mEviewApplication = eviewApplication;
        mEviewEditorMainApp = eviewEditorMainApp;
        bCheckedOut = eviewEditorMainApp.isCheckedOut();
        mEntityNode = entityNode;
        mMatchFieldDef = mEviewApplication.getMatchFieldDef(false);
        initComponents();
        cbDataType.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    String type = (String) cbDataType.getSelectedItem();
                    if (type.equals("char") || type.equals("boolean")) {
                        setDataSize(1);
                    }
                }
            });

        setMatchTypeComboBox(mEviewApplication.getMatchTypeList(false));
        addListeners1();
        loadProperties(entityNode.getFieldDef());
        loadEDMProperties(edmFieldDef);
        // add other listeners after properties are loaded
        addListeners2();
        enableComponents();
    }
    
    private void enableComponents() {
        this.txtName.setEnabled(bCheckedOut);
        this.cbDataType.setEnabled(bCheckedOut);
        this.cbMatchType.setEnabled(bCheckedOut);
        //this.chkBlocking.setEnabled(bCheckedOut);
        this.chkKeyType.setEnabled(bCheckedOut);
        this.chkUpdateable.setEnabled(bCheckedOut);
        this.chkRequired.setEnabled(bCheckedOut);
        this.spFieldSize.setEnabled(bCheckedOut);
        this.txtPattern.setEnabled(bCheckedOut);
        this.txtCodeModule.setEnabled(bCheckedOut);
        this.txtUserCode.setEnabled(bCheckedOut);        
        this.txtConstrainedBy.setEnabled(bCheckedOut);
        this.txtDisplayName.setEnabled(bCheckedOut);
        this.txtInputMask.setEnabled(bCheckedOut);
        this.txtValueMask.setEnabled(bCheckedOut);
    }
    
    private void addListeners1() {
        txtName.addFocusListener(new java.awt.event.FocusListener() {
                String oldName;
                public void focusGained(java.awt.event.FocusEvent ev) {
                    oldName = txtName.getText();
                }

                public void focusLost(java.awt.event.FocusEvent ev) {
                    String newName = txtName.getText();
                    if (mEntityNode.checkNodeNameValue(newName)) {
                        mEntityNode.setNodeName(newName);
                        if (txtDisplayName.getText().equals(oldName)) {
                            txtDisplayName.setText(newName);
                        }
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
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        cbDataType.requestFocus();
                    }
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onNameKeyTyped(evt);}
        });
        
        chkBlocking.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                mEntityNode.enableBlockingTab(chkBlocking.isSelected());
            }
        });
    }
    
    private void addListeners2() {
        cbMatchType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                String matchType = (String) cbMatchType.getSelectedItem();
                if (matchType == null) {
                    matchType = MATCH_TYPE_NONE;
                }
                String fullFieldName = mEntityNode.getFullFieldName();

                if (matchType.equals(MATCH_TYPE_NONE)) {
                    // disable Matching Tab
                    mEntityNode.enableMatchingTab(false, matchType, fullFieldName);
                    //btnMatchTypeAutoGen.setEnabled(false);
                } else {
                    // enable Matching Tab
                    mEntityNode.enableMatchingTab(true, matchType, fullFieldName);
                    //btnMatchTypeAutoGen.setEnabled(true);
                }
                mMatchFieldDef.updateMatchType(mEntityNode.getEnterpriseFieldName(),  (matchType.equals(MATCH_TYPE_NONE)) ? null : matchType);
                mMatchFieldDef.setModified(true);
                mEviewEditorMainApp.enableSaveAction(true);
            }
        });
        
        this.cbDataType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                enableSaveAction();
            }
        });
        
        this.chkKeyType.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enableSaveAction();
            }
        });

        this.chkRequired.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enableSaveAction();
            }
        });
        
        this.chkUpdateable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enableSaveAction();
            }
        });
        
        this.spFieldSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                if (!mOriginalDataSize.equals(getDataSize())) {
                    enableSaveAction();
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

                    mEntityNode.enableSaveAction();
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

                    mEntityNode.enableSaveAction();
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

                    mEntityNode.enableSaveAction();
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
                    mEntityNode.enableSaveAction();
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        //EDM
        txtDisplayName.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtInputMask.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        txtInputMask.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtValueMask.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });
        
        txtValueMask.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyReleased(evt);
                    char c = evt.getKeyChar();
                    if (c == '\n') {
                        txtName.requestFocus();
                    }
                    mEntityNode.enableSaveAction();
                }
                @Override
                public void keyTyped(java.awt.event.KeyEvent evt) {
                    onTextFieldKeyTyped(evt);
                }
        });

    }
    
    private void enableSaveAction() {
        mEntityNode.enableSaveAction();
    }
    
    /**
     *param FieldDef
     */
    public void loadProperties(FieldDef fieldDef) {
        if (fieldDef != null) {
            setFieldName(fieldDef.getFieldName());
            setDataType(fieldDef.getFieldType());
            EntityNode parentNode = (EntityNode) mEntityNode.getParent();
            setMatchType(parentNode, fieldDef.getFieldName());
            //setStandardizationType(parentNode, fieldDef.getFieldName());
            //cbMatchType.setEnabled(!mEntityNode.isGeneratedField());
            //chkBlocking.setEnabled(!mEntityNode.isGeneratedField());
            setBlockOn(mEntityNode.isBlockOn());
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
    
    /**
     *param FieldDef
     */
    public void loadProperties(String fieldName, String fieldType, 
            boolean keyType, boolean updateable,
            boolean required, int fieldSize, 
            String pattern, String codeModule, 
            String userCode, String constrainedBy,
            String matchType) {
        setFieldName(fieldName);
        setDataType(fieldType);
        //setMatchType(matchType);
        setBlockOn(mEntityNode.isBlockOn());
        setKeyType(keyType);
        setUpdateable(updateable);
        setRequired(required);
        setDataSize(fieldSize);
        setPattern(pattern);
        setCodeModule(codeModule);
        setUserCode(userCode);
        setConstraintBy(constrainedBy);
    }
    
    private void loadEDMProperties(EDMFieldDef edmFieldDef) {
        if (edmFieldDef != null) {
            txtDisplayName.setText(edmFieldDef.getDisplayName());
            txtInputMask.setText(edmFieldDef.getInputMask());
            txtValueMask.setText(edmFieldDef.getValueMask());
       } else { // It is generated node (through MatchType) or just added
            boolean isJustAdded = mEntityNode.isJustAdded();
            if (isJustAdded) {
                txtDisplayName.setText(mEntityNode.getName());            
            }
            txtDisplayName.setEnabled(isJustAdded);
            txtInputMask.setEnabled(isJustAdded);
            txtValueMask.setEnabled(isJustAdded);
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
        String oldName = txtName.getText();
        if (!name.equals(oldName)) {
            txtName.setText(name);
            if (txtDisplayName.getText().equals(oldName)) {
                txtDisplayName.setText(name);
            }
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
        if (dataType.equals("char")) {
            dataType = "byte";
        }
        return dataType;
    }
   
    /**
     * Should be called from TabMatchConfigPanel when match type list is changed
     *@param alMatchTypes
     */
    public void setMatchTypeComboBox(ArrayList alMatchTypes) {
        String matchType = getMatchType();
        cbMatchType.removeAllItems();
        cbMatchType.addItem(MATCH_TYPE_NONE);
        if (alMatchTypes != null) {
            for (int i = 0; i < alMatchTypes.size(); i++) {
                cbMatchType.addItem(alMatchTypes.get(i));
            }
        }
        cbMatchType.setSelectedItem(matchType);
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
     *@param MatchField
     */
    private void setMatchType(EntityNode parentNode, String fieldName) {
        String fullFieldName = getEnterpriseFieldName(parentNode, fieldName);
        String matchType = mMatchFieldDef.getMatchType(fullFieldName);
        if (matchType != null) {
            this.cbMatchType.setSelectedItem(matchType);
            //this.btnConfigureMatchType.setEnabled(true);            
        } else {
            this.cbMatchType.setSelectedIndex(0);  //"None"
            //this.btnConfigureMatchType.setEnabled(false);
        }
    }

    /**
     *@param MatchField
     */
    /*
    public void setStandardizationType(EntityNode parentNode, String fieldName) {
        String fullFieldName;
        if (parentNode.isSub()) {
            EntityNode grandParentNode = (EntityNode) parentNode.getParent();
            fullFieldName = grandParentNode.getName() + "." + parentNode.getName() + "[*]." + fieldName;
        } else {
            fullFieldName = parentNode.getName() + "." + fieldName;
        }
        ArrayList alGroups = mMatchFieldDef.getFreeFormGroups();
        for (int i=0; alGroups != null && i < alGroups.size(); i++) {
            MatchFieldDef.Group group = (MatchFieldDef.Group) alGroups.get(i);
            ArrayList alSourceFieldNames = group.getUnstandardizedSourceFields();
            if (alSourceFieldNames.contains(fullFieldName)) {
                String standardizationType = group.getStandardizationType();
                if (standardizationType != null) {
                    this.cbMatchType.setSelectedItem(standardizationType);
                } else {
                    this.cbMatchType.setSelectedIndex(0);  //"None"
                }
                break;
            }
        }
    }
    */
    
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
        chkBlocking.setSelected(bBlocking);
    }

    /**
     *@return Blocking
     */
    public String getBlocking() {
        return chkBlocking.isSelected() ? "true" : "false";
    }

    /**
     *@param keyType
     */
    private void setKeyType(boolean keyType) {
        chkKeyType.setSelected(keyType);
    }
    /**
     *@return KeyType
     */
    public String getKeyType() {
        return chkKeyType.isSelected() ? "true" : "false";
    }

    /**
     *@param Required
     */
    private void setRequired(boolean required) {
        chkRequired.setSelected(required);
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return chkRequired.isSelected() ? "true" : "false";
    }

    /**
     *@param Updateable
     */
    private void setUpdateable(boolean updateable) {
        chkUpdateable.setSelected(updateable);
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return chkUpdateable.isSelected() ? "true" : "false";
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabelDataType = new javax.swing.JLabel();
        cbDataType = new javax.swing.JComboBox();
        jLabelMatchType = new javax.swing.JLabel();
        cbMatchType = new javax.swing.JComboBox();
        jLabelBlocking = new javax.swing.JLabel();
        chkBlocking = new javax.swing.JCheckBox();
        jLabelKeyType = new javax.swing.JLabel();
        chkKeyType = new javax.swing.JCheckBox();
        jLabelUpdateable = new javax.swing.JLabel();
        chkUpdateable = new javax.swing.JCheckBox();
        jLabelRequired = new javax.swing.JLabel();
        chkRequired = new javax.swing.JCheckBox();
        jLabelFieldSize = new javax.swing.JLabel();
        spFieldSize = new javax.swing.JSpinner();
        jLabelPattern = new javax.swing.JLabel();
        jLabelCodeModule = new javax.swing.JLabel();
        txtPattern = new javax.swing.JTextField();
        txtCodeModule = new javax.swing.JTextField();
        jLabelUserCode = new javax.swing.JLabel();
        txtUserCode = new javax.swing.JTextField();
        jLabelConstrainedBy = new javax.swing.JLabel();
        txtConstrainedBy = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabelDisplayName = new javax.swing.JLabel();
        jLabelInputMask = new javax.swing.JLabel();
        jLabelValueMask = new javax.swing.JLabel();
        txtDisplayName = new javax.swing.JTextField();
        txtInputMask = new javax.swing.JTextField();
        txtValueMask = new javax.swing.JTextField();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("Title_Object"))); // NOI18N

        jLabelName.setText(bundle.getString("MSG_Name")); // NOI18N

        jLabelDataType.setText(bundle.getString("MSG_DataType")); // NOI18N

        cbDataType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "string", "char", "date", "int", "float", "boolean" }));

        jLabelMatchType.setText(bundle.getString("MSG_MatchType")); // NOI18N

        jLabelBlocking.setText(bundle.getString("MSG_Blocking")); // NOI18N

        chkBlocking.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkBlocking.setEnabled(false);
        chkBlocking.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabelKeyType.setText(bundle.getString("MSG_KeyType")); // NOI18N

        chkKeyType.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkKeyType.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabelUpdateable.setText(bundle.getString("MSG_Updateable")); // NOI18N

        chkUpdateable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkUpdateable.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabelRequired.setText(bundle.getString("MSG_Required")); // NOI18N

        chkRequired.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkRequired.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabelFieldSize.setText(bundle.getString("MSG_FieldSize")); // NOI18N

        jLabelPattern.setText(bundle.getString("MSG_Pattern")); // NOI18N

        jLabelCodeModule.setText(bundle.getString("MSG_CodeModule")); // NOI18N

        txtCodeModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodeModuleActionPerformed(evt);
            }
        });

        jLabelUserCode.setText(bundle.getString("MSG_UserCode")); // NOI18N

        jLabelConstrainedBy.setText(bundle.getString("MSG_ConstrainedBy")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cbDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cbMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelBlocking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(chkBlocking))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelKeyType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(chkKeyType))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelUpdateable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(chkUpdateable))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelRequired, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(chkRequired))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(spFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel1Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbDataType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cbMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelBlocking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkBlocking, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelKeyType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkKeyType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelUpdateable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkUpdateable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelRequired, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkRequired, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(spFieldSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtPattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCodeModule, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtUserCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtConstrainedBy, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("MSG_EDM"))); // NOI18N

        jLabelDisplayName.setText(bundle.getString("MSG_DisplayName")); // NOI18N

        jLabelInputMask.setText(bundle.getString("MSG_InputMask")); // NOI18N

        jLabelValueMask.setText(bundle.getString("MSG_ValueMask")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(jPanel2Layout.createSequentialGroup()
                .add(4, 4, 4)
                .add(jLabelValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(txtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtDisplayName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtInputMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtValueMask, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 480, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 480, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodeModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodeModuleActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_txtCodeModuleActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbDataType;
    private javax.swing.JComboBox cbMatchType;
    private javax.swing.JCheckBox chkBlocking;
    private javax.swing.JCheckBox chkKeyType;
    private javax.swing.JCheckBox chkRequired;
    private javax.swing.JCheckBox chkUpdateable;
    private javax.swing.JLabel jLabelBlocking;
    private javax.swing.JLabel jLabelCodeModule;
    private javax.swing.JLabel jLabelConstrainedBy;
    private javax.swing.JLabel jLabelDataType;
    private javax.swing.JLabel jLabelDisplayName;
    private javax.swing.JLabel jLabelFieldSize;
    private javax.swing.JLabel jLabelInputMask;
    private javax.swing.JLabel jLabelKeyType;
    private javax.swing.JLabel jLabelMatchType;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPattern;
    private javax.swing.JLabel jLabelRequired;
    private javax.swing.JLabel jLabelUpdateable;
    private javax.swing.JLabel jLabelUserCode;
    private javax.swing.JLabel jLabelValueMask;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner spFieldSize;
    private javax.swing.JTextField txtCodeModule;
    private javax.swing.JTextField txtConstrainedBy;
    private javax.swing.JTextField txtDisplayName;
    private javax.swing.JTextField txtInputMask;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPattern;
    private javax.swing.JTextField txtUserCode;
    private javax.swing.JTextField txtValueMask;
    // End of variables declaration//GEN-END:variables
    
}
