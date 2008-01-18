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
package com.sun.mdm.index.project.ui.applicationeditor.matching;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;

import com.sun.mdm.matcher.comparators.configurator.ComparatorsConfigBean;
import com.sun.mdm.matcher.comparators.ComparatorsManager;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.util.LogUtil;

public class AdvancedMatchConfigDialog extends javax.swing.JDialog {
    private static Logger mLogger = LogUtil.getLogger(AdvancedMatchConfigDialog.class);
    private boolean mModified = false;
    private static ArrayList<String> mAlFunctions;
    private static ArrayList<String> mAlFunctionsDesc;
    private static ArrayList<String> mAlRequiredExtraParameters;
    private static ArrayList<String> mAlNullFields;
    private static ArrayList<String> mAlNullFieldsDesc;
    private static Map mMapNullFields = new HashMap();
    private static Map mMapNullFields2 = new HashMap();
    private static Map mMapDescKeyFunctions = new HashMap();
    private static Map mMapShortKeyFunctions = new HashMap();
    private static Map mMapRequiredExtraParameters = new HashMap();
    private static String mCoparatorListPath;
    private static ComparatorsManager mComparatorsManager;
    private static Map<String, Map<String, ArrayList>> mParmsMap;
    
    /** Creates new form AdvancedMatchConfigDialog */
    public AdvancedMatchConfigDialog(String probabilityType,
                                     String matchType, 
                                     String matchSize,
                                     String nullField, 
                                     String function, 
                                     String agreementW,
                                     String disagreementW,
                                     String parameters,
                                     String coparatorListPath) {
        super(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
        initComponents();
        //build components
        mCoparatorListPath = coparatorListPath;
        
        //populate components
        this.jTextFieldMatchType.setText(matchType);
        
        //this.jSpinnerMatchSize.setValue(Integer.parseInt(matchSize));
        String s = String.valueOf(matchSize);
        Integer size = Integer.valueOf(s);
        this.jSpinnerMatchSize.setValue(size);

        if (probabilityType.equals("0")) {
            this.jTextFieldAgreementWeight.setText("");
            this.jTextFieldDisagreementWeight.setText("");
            this.jTextFieldAgreementWeight.setEditable(false);
            this.jTextFieldDisagreementWeight.setEditable(false);
            this.jTextFieldMProbability.setText(agreementW);
            this.jTextFieldUProbability.setText(disagreementW);
        } else {
            this.jTextFieldMProbability.setText("");
            this.jTextFieldUProbability.setText("");
            this.jTextFieldMProbability.setEditable(false);
            this.jTextFieldUProbability.setEditable(false);
            this.jTextFieldAgreementWeight.setText(agreementW);
            this.jTextFieldDisagreementWeight.setText(disagreementW);
        }
        
        buildMaps();
        for (int i=0; i<mAlNullFields.size(); i++) {
            jComboBoxNullField.addItem(mAlNullFieldsDesc.get(i));
        }
        this.jComboBoxNullField.setSelectedItem(mMapNullFields2.get(nullField));

        for (int i=0; i<mAlFunctions.size(); i++) {
            jComboBoxFunction.addItem(mAlFunctionsDesc.get(i));
        }
        this.jComboBoxFunction.setSelectedItem(function); //mMapShortKeyFunctions.get(function));
        this.jTextFieldParameters.setText(parameters);
    }
    
    private void buildMaps() {
        buildNullFields();
        buildFunctions();
        createDefaultExtraParameters();
    }
    
    private static void buildNullFields() {
        mAlNullFieldsDesc = new ArrayList<String>();
        mAlNullFieldsDesc.add("Zero weight"); // 0
        mAlNullFieldsDesc.add("Full combination weight"); // 1
        mAlNullFieldsDesc.add("Full agreement weight");   // a1
        mAlNullFieldsDesc.add("1/2 the agreement weight");   // a2
        mAlNullFieldsDesc.add("1/3 of the agreement weight");
        mAlNullFieldsDesc.add("1/4 of the agreement weight");
        mAlNullFieldsDesc.add("1/5 of the agreement weight");
        mAlNullFieldsDesc.add("1/6 of the agreement weight");
        mAlNullFieldsDesc.add("1/7 of the agreement weight");        
        mAlNullFieldsDesc.add("1/8 of the agreement weight");
        mAlNullFieldsDesc.add("1/9 of the agreement weight");
        mAlNullFieldsDesc.add("1/10 of the agreement weight");
        mAlNullFieldsDesc.add("Full disagreement weight");        
        mAlNullFieldsDesc.add("1/2 the disagreement weight");
        mAlNullFieldsDesc.add("1/3 of the disagreement weight");
        mAlNullFieldsDesc.add("1/4 of the disagreement weight");
        mAlNullFieldsDesc.add("1/5 of the disagreement weight");        
        mAlNullFieldsDesc.add("1/6 of the disagreement weight");
        mAlNullFieldsDesc.add("1/7 of the disagreement weight");
        mAlNullFieldsDesc.add("1/8 of the disagreement weight");
        mAlNullFieldsDesc.add("1/9 of the disagreement weight");    
        mAlNullFieldsDesc.add("1/10 of the disagreement weight");

        mAlNullFields = new ArrayList<String>();
        mAlNullFields.add("0");
        mAlNullFields.add("1");
        mAlNullFields.add("a1");
        mAlNullFields.add("a2");
        mAlNullFields.add("a3");
        mAlNullFields.add("a4");
        mAlNullFields.add("a5");
        mAlNullFields.add("a6");
        mAlNullFields.add("a7");        
        mAlNullFields.add("a8");
        mAlNullFields.add("a9");
        mAlNullFields.add("a10");
        mAlNullFields.add("d1");        
        mAlNullFields.add("d2");
        mAlNullFields.add("d3");
        mAlNullFields.add("d4");
        mAlNullFields.add("d5");        
        mAlNullFields.add("d6");
        mAlNullFields.add("d7");
        mAlNullFields.add("d8");
        mAlNullFields.add("d9");        
        mAlNullFields.add("d10");
        
        for (int i=0; i<mAlNullFields.size(); i++) {
            mMapNullFields.put(mAlNullFieldsDesc.get(i), mAlNullFields.get(i));  
            mMapNullFields2.put(mAlNullFields.get(i), mAlNullFieldsDesc.get(i));  
        }
    }
    
    private static ComparatorsConfigBean getComparatorsConfigBean() {
        //if (mComparatorsManager == null) {
           try {
            mComparatorsManager = new ComparatorsManager(mCoparatorListPath);
            } catch (Exception ex) {
                mLogger.error(ex.getMessage());
            }
        //}
        ComparatorsConfigBean instance = mComparatorsManager.getComparatorsConfigBean();
        return instance;
    }
    
    private static void buildFunctions() {
        //Get comparator and description from comparatorsList.xml
        try {
            ComparatorsConfigBean instance = getComparatorsConfigBean();
            mMapShortKeyFunctions = instance.getCodeNamesDesc();
            mAlFunctionsDesc = new ArrayList<String>();
            mAlFunctions = new ArrayList<String>();
        
            Set set = mMapShortKeyFunctions.keySet();
            Iterator iter = set.iterator();
            for (int i=0; i < set.size(); i++) {
                String strFunction = iter.next().toString();
                String strDesc = mMapShortKeyFunctions.get(strFunction).toString();
                mAlFunctionsDesc.add(strDesc);
                mAlFunctions.add(strFunction);
                mMapDescKeyFunctions.put(strDesc, strFunction);  
            }
            mParmsMap = instance.getParametersDetailsForGUI();
        } catch (Exception ex) {
            mLogger.error(ex.getMessage());
        }
    }
    
    private static void createDefaultExtraParameters() {
        try {
            //ComparatorsManager comparatorsManager = new ComparatorsManager(mCoparatorListPath);
            //ComparatorsConfigBean instance = comparatorsManager.getComparatorsConfigBean();
            //mMapRequiredExtraParameters = instance.getCodeNamesDesc();
        } catch (Exception ex) {
            mLogger.error(ex.getMessage());
        }

        mAlRequiredExtraParameters = new ArrayList<String>();
        mAlRequiredExtraParameters.add("");   // b1
        mAlRequiredExtraParameters.add("");  // b2
        mAlRequiredExtraParameters.add("");  // u
        mAlRequiredExtraParameters.add(""); // ua
        mAlRequiredExtraParameters.add("");   // uf
        mAlRequiredExtraParameters.add("");    // ul
        mAlRequiredExtraParameters.add(""); // un
        mAlRequiredExtraParameters.add("");   // us
        mAlRequiredExtraParameters.add("de");    // usu
        mAlRequiredExtraParameters.add("");  // c
        mAlRequiredExtraParameters.add("y 8");  // n
        mAlRequiredExtraParameters.add("n"); // nI
        mAlRequiredExtraParameters.add("n"); // nR  
        mAlRequiredExtraParameters.add("an");   // nS
        mAlRequiredExtraParameters.add(""); // dY
        mAlRequiredExtraParameters.add("n"); // dM
        mAlRequiredExtraParameters.add("y 15 30");   // dD
        mAlRequiredExtraParameters.add("y 30 60");  // dH
        mAlRequiredExtraParameters.add("y 300 600");  // dm
        mAlRequiredExtraParameters.add("y 75 60");  // ds
        mAlRequiredExtraParameters.add("20 5 5");    // p
    }
    
    /**
     *return mAlNullFields
     */
    public static ArrayList getNullFields() {
        if (mAlNullFields == null) {
            buildNullFields();
        }
        return mAlNullFields;
    }
    
    /**
     *return mAlNullFieldsDesc
     */
    public static ArrayList getNullFieldsDesc() {
        if (mAlNullFieldsDesc == null) {
            buildNullFields();
        }
        return mAlNullFieldsDesc;
    }
    
    /**
     *return mMapNullFields
     */
    public static Map getMapNullFields() {
        return mMapNullFields;
    }

    /** 
     *return mAlFunctions
     */
    public static ArrayList getFunctions(String coparatorListPath) {
        mCoparatorListPath = coparatorListPath;
        //if (mAlFunctions == null) {
            buildFunctions();
        //}
        return mAlFunctions;
    }
    
    /** 
     *return mAlFunctionsDesc
     */

    public static ArrayList getFunctionsDesc() {
        //if (mAlFunctionsDesc == null) {
            buildFunctions();
        //}
        return mAlFunctionsDesc;
    }
    
    /**
     *return mMapDescKeyFunctions
     */
    public static Map getMapDescKeyFunctions() {
        return mMapDescKeyFunctions;
    }
    
    /**
     *return mMapShortKeyFunctions
     */
    public static Map getMapShortKeyFunctions() {
        return mMapShortKeyFunctions;
    }
    
    /**
     *return match type
     */
    public String getMatchType() {
        return this.jTextFieldMatchType.getText();
    }
    
    /**
     *return match size
     */
    public String getMatchSize() {
        return this.jSpinnerMatchSize.getValue().toString();
    }
    
    /**
     *return AgreementWeight
     */
    public String getAgreementWeight() {
        return this.jTextFieldAgreementWeight.getText();
    }
    
    /**
     *return DisagreementWeight
     */
    public String getDisagreementWeight() {
        return this.jTextFieldDisagreementWeight.getText();
    }
    
    /**
     *return M-Probability
     */
    public String getMProbability() {
        return this.jTextFieldMProbability.getText();
    }
    
    /**
     *return U-Probability
     */
    public String getUProbability() {
        return this.jTextFieldUProbability.getText();
    }
    
    /**
     *return Null Field
     */
    public String getNullField() {
        return (String) mMapNullFields.get(jComboBoxNullField.getSelectedItem().toString());
    }
    
    /**
     *return function
     */
    public String getFunction() {
        return jComboBoxFunction.getSelectedItem().toString();
    }
    
    public String getParameters() {
        return this.jTextFieldParameters.getText();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelMatchType = new javax.swing.JLabel();
        jLabelMatchSize = new javax.swing.JLabel();
        jTextFieldMatchType = new javax.swing.JTextField();
        jSpinnerMatchSize = new javax.swing.JSpinner();
        jLabelNullField = new javax.swing.JLabel();
        jComboBoxNullField = new javax.swing.JComboBox();
        jLabelFunction = new javax.swing.JLabel();
        jComboBoxFunction = new javax.swing.JComboBox();
        jLabelAgreementWeight = new javax.swing.JLabel();
        jLabelDisagreementWeight = new javax.swing.JLabel();
        jTextFieldAgreementWeight = new javax.swing.JTextField();
        jTextFieldDisagreementWeight = new javax.swing.JTextField();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabelMProbability = new javax.swing.JLabel();
        jLabelUProbability = new javax.swing.JLabel();
        jTextFieldMProbability = new javax.swing.JTextField();
        jTextFieldUProbability = new javax.swing.JTextField();
        jLabelParameters = new javax.swing.JLabel();
        jTextFieldParameters = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/matching/Bundle"); // NOI18N
        setTitle(bundle.getString("TITLE_Edit_Matching_Rule")); // NOI18N

        jLabelMatchType.setText(bundle.getString("LBL_Match_Type")); // NOI18N

        jLabelMatchSize.setText(bundle.getString("LBL_Match_Size")); // NOI18N

        jTextFieldMatchType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onMatchTypePropertyChanged(evt);
            }
        });

        jSpinnerMatchSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                onMatchSizeStateChanged(evt);
            }
        });

        jLabelNullField.setText(bundle.getString("LBL_Null_Field")); // NOI18N

        jComboBoxNullField.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onNullFieldItemStateChanged(evt);
            }
        });

        jLabelFunction.setText(bundle.getString("LBL_Function")); // NOI18N

        jComboBoxFunction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                onFunctionItemStateChanged(evt);
            }
        });

        jLabelAgreementWeight.setText(bundle.getString("LBL_Agreement_Weight")); // NOI18N

        jLabelDisagreementWeight.setText(bundle.getString("LBL_Disagreement_Weight")); // NOI18N

        jTextFieldAgreementWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onAgreementWeightKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onAgreementWeightKeyTyped(evt);
            }
        });

        jTextFieldDisagreementWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onDisagreementWeightKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onDisagreementWeightKeyTyped(evt);
            }
        });

        jButtonOK.setText(bundle.getString("LBL_OK")); // NOI18N
        jButtonOK.setEnabled(false);
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOK(evt);
            }
        });

        jButtonCancel.setText(bundle.getString("LBL_Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        jLabelMProbability.setText(bundle.getString("LBL_MProbability")); // NOI18N

        jLabelUProbability.setText(bundle.getString("LBL_UProbability")); // NOI18N

        jTextFieldMProbability.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onMProbabilityKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onMProbabilityKeyTyped(evt);
            }
        });

        jTextFieldUProbability.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                onUProbabilityKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onUProbabilityKeyTyped(evt);
            }
        });

        jLabelParameters.setText(bundle.getString("LBL_Parameters")); // NOI18N

        jTextFieldParameters.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                onParametersKeyTyped(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelMatchSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jSpinnerMatchSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelNullField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxNullField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jComboBoxFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelAgreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldAgreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(60, 60, 60)
                .add(jLabelMProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldMProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelDisagreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldDisagreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(60, 60, 60)
                .add(jLabelUProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jTextFieldUProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabelParameters, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(jTextFieldParameters, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(layout.createSequentialGroup()
                .add(300, 300, 300)
                .add(jButtonOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelMatchSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jSpinnerMatchSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelNullField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxNullField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBoxFunction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelAgreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldAgreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelMProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldMProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelDisagreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldDisagreementWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelUProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldUProbability, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabelParameters, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldParameters, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonOK)
                    .add(jButtonCancel)))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-475)/2, (screenSize.height-285)/2, 475, 285);
    }// </editor-fold>//GEN-END:initComponents

    private void onParametersKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onParametersKeyTyped
        char c = evt.getKeyChar();
        if (c == '\n') {
            this.jTextFieldMatchType.requestFocus();
        }
    }//GEN-LAST:event_onParametersKeyTyped

    private void onUProbabilityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onUProbabilityKeyReleased
        enableOK();
    }//GEN-LAST:event_onUProbabilityKeyReleased

    private void onMProbabilityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onMProbabilityKeyReleased
        enableOK();
    }//GEN-LAST:event_onMProbabilityKeyReleased

    private void onDisagreementWeightKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onDisagreementWeightKeyReleased
        enableOK();
    }//GEN-LAST:event_onDisagreementWeightKeyReleased

    private void onAgreementWeightKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onAgreementWeightKeyReleased
        enableOK();
    }//GEN-LAST:event_onAgreementWeightKeyReleased

    private void onUProbabilityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onUProbabilityKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldUProbability.getText().indexOf('.') >= 0)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
        if (c == '\n') {
            this.jTextFieldParameters.requestFocus();
        }
    }//GEN-LAST:event_onUProbabilityKeyTyped

    private void onMProbabilityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onMProbabilityKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldMProbability.getText().indexOf('.') >= 0)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
        if (c == '\n') {
            this.jTextFieldUProbability.requestFocus();
        }
    }//GEN-LAST:event_onMProbabilityKeyTyped

    private void onDisagreementWeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onDisagreementWeightKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '-' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldDisagreementWeight.getText().indexOf('.') >= 0) ||
            (c == '-' && this.jTextFieldDisagreementWeight.getText().indexOf('-') >= 0) ) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
        if (c == '\n') {
            this.jTextFieldParameters.requestFocus();
        }
    }//GEN-LAST:event_onDisagreementWeightKeyTyped

    private void onAgreementWeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onAgreementWeightKeyTyped
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '.' && c != '\b' && c != '\n') || 
            (c == '.' && this.jTextFieldAgreementWeight.getText().indexOf('.') >= 0)) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
        if (c == '\n') {
            this.jTextFieldDisagreementWeight.requestFocus();
        }
    }//GEN-LAST:event_onAgreementWeightKeyTyped

    private void onFunctionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_onFunctionItemStateChanged
        // Check if extra parameters required for the selected comparator
        String desc = (String) jComboBoxFunction.getSelectedItem();
        String func = (String) mMapDescKeyFunctions.get(desc);
        //Map<String, Map<String, ArrayList>>
        Map mapParam = mParmsMap.get(func);
        
        if (mapParam != null) {
            Set set = mapParam.keySet();
            Collection c = mapParam.values();

            Iterator iter = set.iterator();
            for (int i=0; i < set.size(); i++) {
                String strFunction = iter.next().toString();

            }
        }
        //String parameters = mAlRequiredExtraParameters.get(i);
        //this.jTextFieldParameters.setText(parameters);
        //this.jTextFieldParameters.setEnabled(parameters.length() > 0);
        enableOK();
    }//GEN-LAST:event_onFunctionItemStateChanged

    private void onNullFieldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_onNullFieldItemStateChanged
        enableOK();
    }//GEN-LAST:event_onNullFieldItemStateChanged

    private void onMatchSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_onMatchSizeStateChanged
        enableOK();
    }//GEN-LAST:event_onMatchSizeStateChanged

    private void onMatchTypePropertyChanged(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_onMatchTypePropertyChanged
        enableOK();
        char c = evt.getKeyChar();
        if (c == '\n') {
            this.jSpinnerMatchSize.requestFocus();
        }
    }//GEN-LAST:event_onMatchTypePropertyChanged

    private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
        mModified = false;
        this.dispose();
    }//GEN-LAST:event_onCancel

    private void onOK(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOK
        mModified = true;
        this.dispose();
    }//GEN-LAST:event_onOK
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new AdvancedMatchConfigDialog("", , "", "", "", "", "", "");
            }
        });
    }
    private void enableOK() {
        boolean enabled = this.jTextFieldMatchType.getText() != null && 
                          !this.jTextFieldMatchType.getText().equals("") &&
                          (this.jTextFieldAgreementWeight.isEnabled() &&
                           this.jTextFieldAgreementWeight.getText() != null && 
                           !this.jTextFieldAgreementWeight.getText().equals("") &&    
                           this.jTextFieldDisagreementWeight.getText() != null && 
                           !this.jTextFieldDisagreementWeight.getText().equals("") &&
                           !this.jTextFieldDisagreementWeight.getText().equals("-")) ||
                          (this.jTextFieldMProbability.isEnabled() &&
                           this.jTextFieldMProbability.getText() != null && 
                           !this.jTextFieldMProbability.getText().equals("") &&    
                           this.jTextFieldUProbability.getText() != null && 
                           !this.jTextFieldUProbability.getText().equals(""));   

        this.jButtonOK.setEnabled(enabled);
    }
    
    public boolean isModified() {
        return mModified;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBoxFunction;
    private javax.swing.JComboBox jComboBoxNullField;
    private javax.swing.JLabel jLabelAgreementWeight;
    private javax.swing.JLabel jLabelDisagreementWeight;
    private javax.swing.JLabel jLabelFunction;
    private javax.swing.JLabel jLabelMProbability;
    private javax.swing.JLabel jLabelMatchSize;
    private javax.swing.JLabel jLabelMatchType;
    private javax.swing.JLabel jLabelNullField;
    private javax.swing.JLabel jLabelParameters;
    private javax.swing.JLabel jLabelUProbability;
    private javax.swing.JSpinner jSpinnerMatchSize;
    private javax.swing.JTextField jTextFieldAgreementWeight;
    private javax.swing.JTextField jTextFieldDisagreementWeight;
    private javax.swing.JTextField jTextFieldMProbability;
    private javax.swing.JTextField jTextFieldMatchType;
    private javax.swing.JTextField jTextFieldParameters;
    private javax.swing.JTextField jTextFieldUProbability;
    // End of variables declaration//GEN-END:variables
    
}
