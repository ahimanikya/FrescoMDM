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
import javax.swing.JTabbedPane;

public class PropertySheetModel {
    static final String PROPERTIES = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_Properties");
    static final String EDM = NbBundle.getMessage(PropertySheetModel.class,
            "MSG_EDM");
    
    JTabbedPane mPropertiesTabbedPane = null;
    
    String mDisplayName = null;
    String mDefaultDataType = "string";
    String mDefaultDataSize = "32";
    String mDefaultInputMask = "";
    String mDefaultValueMask = "";
    String mDefaultSearchable = "false";
    String mDefaultSearchRequired = "false";
    String mDefaultDisplayedInResult = "false";
    String mDefaultGenerateReport = "false";
    String mDefaultBlocking = "false";
    String mDefaultKeyType = "false";
    String mDefaultUpdateable = "true";
    String mDefaultRequired = "false";
    String mDefaultMatchType = "None";
    String mDefaultCodeModule = "";
    String mDefaultUserCode = "";
    String mDefaultConstraintBy = "";
    String mDefaultPattern = "";
    
    TabGeneralPropertiesPanel tabGeneralPropertiesPanel;
    TabEDMPropertiesJPanel tabEDMPropertiesJPanel;
    EntityNode mEntityNode;
    
    /** Creates a new instance of PropertySheetModel
     *
     *@param fieldName for default display name
     *@param defaultDataType for default data type
     *
     */
    public PropertySheetModel(String fieldName, String defaultDataType, EntityNode entityNode) {
        this.mDisplayName = fieldName; // use field name as default Display Name
        this.mDefaultDataType = defaultDataType; // best guess of Data Type
        mEntityNode = entityNode;
        getPropertySheet();
    }

    /** Creates a new instance of PropertySheetModel
     *
     *@param defaultDisplayName for default display name
     *@param defaultDataType for default data type
     *@param defaultInputMask for default Input Mask
     *@param defaultValueMask for default Value Mask
     *@param defaultSearchable for default simple search
     *@param defaultDisplayedInResult for displayed in search result
     *@param defaultGenerateReport for report
     *@param defaultKeyType for KeyType
     *@param defaultUpdateable for updateable
     *@param defaultRequired for required
     *@param defaultMatchType for MatchType
     *@param defaultDataSize for Size
     *@param defaultCodeModule for code module
     *@param defaultPattern for pattern
     *@param defaultBlocking for blocking
     *
     */
    public PropertySheetModel(String defaultDisplayName,
        String defaultDataType, String defaultInputMask,
        String defaultValueMask, String defaultSearchable,
        String defaultDisplayedInResult, String defaultKeyType,
        String defaultUpdateable, String defaultRequired,
        String defaultMatchType, String defaultDataSize,
        String defaultCodeModule, String defaultPattern, 
        String defaultBlocking, String defaultUserCode,
        String defaultConstraintBy, String defaultGenerateReport, EntityNode entityNode) {
        this.mDisplayName = defaultDisplayName; // use field name as default Display Name
        this.mDefaultDataType = defaultDataType; // best guess of Data Type
        this.mDefaultInputMask = defaultInputMask; // best guess of Input Mask
        this.mDefaultValueMask = defaultValueMask; // best guess of Value Mask
        this.mDefaultSearchable = defaultSearchable;
        this.mDefaultDisplayedInResult = defaultDisplayedInResult;
        this.mDefaultGenerateReport = defaultGenerateReport;
        this.mDefaultKeyType = defaultKeyType;
        this.mDefaultUpdateable = defaultUpdateable;
        this.mDefaultRequired = defaultRequired;
        this.mDefaultMatchType = defaultMatchType;
        this.mDefaultDataSize = defaultDataSize;
        this.mDefaultCodeModule = defaultCodeModule;
        this.mDefaultUserCode = defaultUserCode;
        this.mDefaultConstraintBy = defaultConstraintBy;
        this.mDefaultPattern = defaultPattern;
        this.mDefaultBlocking = defaultBlocking;
        mEntityNode = entityNode;
        getPropertySheet();
    }

    // I tried to use netBeans BeanNode with propertySheet built in, but...
    // Creating my own property sheet, not the best solution
    // but it is less netBeans hassels
    //
    // All the properties will get put in to the panel
    // that can be inserted in DefineEntity wizard panel

    /**
     *@return JTabbedPane that contains field's property sheet
     */
    public JTabbedPane getPropertySheet() {
        if (mPropertiesTabbedPane == null) {
            mPropertiesTabbedPane = new JTabbedPane();
            tabGeneralPropertiesPanel = new TabGeneralPropertiesPanel(mEntityNode);
            mPropertiesTabbedPane.addTab(PROPERTIES, tabGeneralPropertiesPanel);
            tabEDMPropertiesJPanel = new TabEDMPropertiesJPanel(this.mDisplayName, this.mDefaultInputMask, this.mDefaultValueMask,
                                    this.mDefaultSearchable.equals("true") ? true : false, this.mDefaultDisplayedInResult.equals("true") ? true : false, 
                                    this.mDefaultGenerateReport.equals("true") ? true : false, this.mDefaultSearchRequired);
            mPropertiesTabbedPane.addTab(EDM, tabEDMPropertiesJPanel);
        }

        return mPropertiesTabbedPane;
    }
    
    public void setFieldName(String newName) {
        String oldName = tabGeneralPropertiesPanel.getFieldName();
        tabGeneralPropertiesPanel.setFieldName(newName);
        updateDefaultDisplayName(oldName, newName);
    }
    
    public void updateDefaultDisplayName(String oldName, String newName) {
        tabEDMPropertiesJPanel.updateDefaultDisplayName(oldName, newName);
    }

    /**
     *@return DataType
     */
    public String getDataType() {
        String dataType = tabGeneralPropertiesPanel.getDataType();
        return dataType;
    }

    /**
     *@return MatchType
     */
    public String getMatchType() {
        return tabGeneralPropertiesPanel.getMatchType();
    }

    /**
     *@return Blocking
     */
    public String getBlocking() {
        return tabGeneralPropertiesPanel.getBlocking();
    }

    /**
     *@return DataSize
     */
    public String getDataSize() {
        return tabGeneralPropertiesPanel.getDataSize();
    }

    /**
     *@return Pattern
     */
    public String getPattern() {
        return tabGeneralPropertiesPanel.getPattern();
    }

    /**
     *@return Code Module
     */
    public String getCodeModule() {
        return tabGeneralPropertiesPanel.getCodeModule();
    }

    /**
     *@return User Code
     */
    public String getUserCode() {
        return tabGeneralPropertiesPanel.getUserCode();
    }

    /**
     *@return Constraint By
     */
    public String getConstraintBy() {
        return tabGeneralPropertiesPanel.getConstraintBy();
    }

    /**
     *@return KeyType
     */
    public String getKeyType() {
        return tabGeneralPropertiesPanel.getKeyType();
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return tabGeneralPropertiesPanel.getRequired();
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return tabGeneralPropertiesPanel.getUpdateable();
    }

    /**
     *@return DisplayName
     */
    public String getDisplayName() {
        return tabEDMPropertiesJPanel.getDisplayName();
    }

    /**
     *@return InputMask
     */
    public String getInputMask() {
        return tabEDMPropertiesJPanel.getInputMask();
    }

    /**
     *@return ValueMask
     */
    public String getValueMask() {
        return tabEDMPropertiesJPanel.getValueMask();
    }

    /**
     *@return UsedInSearchScreen
     */
    public boolean getUsedInSearchScreen() {
        return tabEDMPropertiesJPanel.getUsedInSearchScreen();
    }
    
    /**
     *@return SearchRequired
     */
    public String getSearchRequired() {
        return tabEDMPropertiesJPanel.getSearchRequired();
    }

    /**
     *@return DisplayedInSearchResult
     */
    public boolean getDisplayedInSearchResult() {
        return tabEDMPropertiesJPanel.getDisplayedInSearchResult();
    }

    /**
     *@return GenerateReport
     */
    public boolean getGenerateReport() {
        return tabEDMPropertiesJPanel.getGenerateReport();
    }    
}
