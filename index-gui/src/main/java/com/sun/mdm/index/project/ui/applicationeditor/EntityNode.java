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
import org.openide.util.NbBundle;
import org.openide.NotifyDescriptor;
import org.openide.DialogDisplayer;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sun.mdm.index.parser.FieldDef;
import com.sun.mdm.index.parser.EDMType;
import com.sun.mdm.index.parser.EDMFieldDef;
import com.sun.mdm.index.project.EviewApplication;

public class EntityNode extends DefaultMutableTreeNode {
    /** The logger. */
    private static final java.util.logging.Logger mLog = java.util.logging.Logger.getLogger(
            EntityNode.class.getName()
        );

    static final String NODE_ROOT = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Root");
    static final String NODE_PRIMARY = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Primary");
    static final String NODE_SUB = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Sub");
    static final String NODE_FIELD = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Field");
    static final String TAB_GENERAL = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_General");
    static final String TAB_EDM = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_EDM");
    static final String TAB_QUERY = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Query");
    static final String TAB_MATCHING = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Matching");
    static final String TAB_STANDARDIZATION = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Standardization");
    static final String TAB_NORMALIZATION = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Normalization");
    static final String TAB_PHONETICIZEDFIELDS = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_PhoneticizedFields");   
    static final String TAB_APPLICATION = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Deployment");
    static final String TAB_NAME = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Name");
    static final String TAB_SOURCE = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Source");
    static final String TAB_DEPLOYMENT = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_Deployment");
    static final String TAB_MATCHCONFIGURATION = NbBundle.getMessage(EntityNode.class,
            "MSG_TAB_MatchConfiguration");
    
    
    String mNodeName;
    String mNodeType;
    boolean mBlockOn = false;
    boolean mGeneratedField = false;
    boolean mJustAdded = false;
    JTabbedPane mEntityProperty = null;
    PropertiesNamePanel mPropertiesNamePanel;
    TabGeneralPropertiesPanel mPropertiesGeneralPanel;
    //PropertiesEDMPanel mPropertiesEDMPanel;
    TabMatchConfigPanel mPanelMatchingProperties;
    PropertiesDeploymentPanel mPropertiesDeploymentPanel;
    TabBlockingPanel mTabQueryPanel;
    TabStandardizationPanel mTabStandardizationPanel;
    TabNormalizationPanel mTabNormalizationPanel;
    TabPhoneticizedFieldsPanel mTabPhoneticizedFieldsPanel;
    TabMatchConfigPanel mTabMatchConfigPanel;
    EDMFieldDef edmFieldDef = null;
    
    int mDisplayOrder = 0;
    EntityTree mEntityTree = null;
    FieldDef mFieldDef = null;
    EDMType mEdmType = null;
    EviewEditorMainApp mEviewEditorMainApp;
    EviewApplication mEviewApplication;
    
    final int TAB_DEPLOYMENT_INDEX = -1;
    final int TAB_MATCHCONFIG_INDEX_ROOT = TAB_DEPLOYMENT_INDEX + 1;
    final int TAB_QUERY_INDEX_ROOT = TAB_DEPLOYMENT_INDEX + 2;    
    final int TAB_STANDARDIZATION_INDEX = TAB_DEPLOYMENT_INDEX + 3;
    final int TAB_NORMALIZATION_INDEX = TAB_DEPLOYMENT_INDEX + 4;
    final int TAB_PHONETICIZEDFIELDS_INDEX = TAB_DEPLOYMENT_INDEX + 5;

    
    final int TAB_GENERAL_INDEX = 0;
    //final int TAB_EDM_INDEX = 1;
    final int TAB_MATCHCONFIG_INDEX_FIELD = 1;
    final int TAB_QUERY_INDEX_FIELD = 2;

    /** Creates a new instance of EntityNode
     *
     *@param nodeName node name
     *@param nodeType node type
     */
    public EntityNode(EntityTree entityTree, String nodeName, String nodeType) {
        super(nodeName);
        mEntityTree = entityTree;        
        mNodeName = nodeName;
        mNodeType = nodeType;
        mEviewApplication = entityTree.getEviewApplication();
        try {
            mEdmType = mEviewApplication.getEDMType(false);
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        mEviewEditorMainApp = entityTree.getEviewEditorMainApp();
        
        mTabMatchConfigPanel = mEviewEditorMainApp.getTabMatchConfigPanel();
        mTabQueryPanel = mEviewEditorMainApp.getTabBlockingPanel();
    }

    /** Creates a new instance of EntityNode
     *
     *@param nodeName node name
     *@param nodeType node type
     */
    public EntityNode(EntityTree entityTree, String nodeName, FieldDef fieldDef, boolean bBlockOn) {
        this(entityTree, nodeName, NODE_FIELD);
        mFieldDef = fieldDef;
        mBlockOn = bBlockOn;
    }

    /** Creates a new instance of EntityNode
     *
     *@param nodeName node name
     *@param defaultDisplayName display name
     *@param nodeType node type
     *@param defaultDataType default data type
     *@param displayOrder gui display order
     *@param defaultInputMask for default Input Mask
     *@param defaultValueMask for default Value Mask
     *@param defaultSearchable for default simple search
     *@param defaultDisplayedInResult for displayed in search result
     *@param defaultKeyType for KeyType
     *@param defaultUpdateable for updateable
     *@param defaultRequired for required
     *@param defaultMatchType for MatchType
     *@param defaultDataSize for Size
     *@param defaultCodeModule for code module
     *@param defaultPattern for pattern
     *@param defaultBlocking for blocking
     */
     public EntityNode(EntityTree entityTree, String nodeName, String defaultDisplayName,
            String nodeType, String defaultDataType, 
            int displayOrder, String defaultInputMask, 
            String defaultValueMask, boolean defaultSearchable, 
            boolean defaultDisplayedInResult, boolean defaultKeyType, 
            boolean defaultUpdateable, boolean defaultRequired, 
            String defaultMatchType, int defaultDataSize, 
            String defaultCodeModule, String defaultPattern, 
            boolean defaultBlocking, String defaultUserCode, 
            String defaultConstraintBy, boolean defaultGenerateReport) {
        this(entityTree, nodeName, nodeType);
        mDisplayOrder = displayOrder;
        mFieldDef = new FieldDef(nodeName, nodeType, defaultKeyType, defaultUpdateable,
                        defaultRequired, defaultDataSize, defaultPattern, defaultCodeModule,
                        defaultUserCode, defaultConstraintBy);
    }

     public void setEntityTree(EntityTree entityTree) {
         mEntityTree = entityTree;
     }

     public EntityTree getEntityTree() {
         return mEntityTree;
     }

    /**
     *@return String nodeName
     */
    public String getName() {
        //return this.toString();

        return this.mNodeName;
    }

    /**
     *@return String nodeType
     */
    public String getType() {
        return this.mNodeType;
    }
    
    /**
     *@return JTabbedPane that contains EntityNode's property tabs
     */
    public JTabbedPane getConfigPropertySheetForRootNode() {
        if (isRoot()) {        
            if (mEntityProperty == null) {
                mEntityProperty = new JTabbedPane();
                try {
                    // get combined deployment properties
                    mPropertiesDeploymentPanel = mEviewEditorMainApp.getPropertiesDeploymentPanel(mEviewApplication.getEIndexObject(false), 
                                                                                              mEviewApplication.getUpdateType(false));
                    mPropertiesDeploymentPanel.setBorder(new javax.swing.border.EtchedBorder());
                    //mEntityProperty.addTab(TAB_APPLICATION, mPropertiesDeploymentPanel);
                    
                    mEntityProperty.addTab(TAB_MATCHING, mTabMatchConfigPanel);
                    
                    mEntityProperty.addTab(TAB_QUERY, mTabQueryPanel);
                    
                    mTabStandardizationPanel = mEviewEditorMainApp.getStandardizationPanel();
                    mEntityProperty.addTab(TAB_STANDARDIZATION, mTabStandardizationPanel);
                
                    mTabNormalizationPanel = mEviewEditorMainApp.getNormalizationPanel();
                    mEntityProperty.addTab(TAB_NORMALIZATION, mTabNormalizationPanel);
                               
                    mTabPhoneticizedFieldsPanel = mEviewEditorMainApp.getPhoneticizedFieldsPanel();
                    mEntityProperty.addTab(TAB_PHONETICIZEDFIELDS, mTabPhoneticizedFieldsPanel);
                } catch (Exception ex) {
                    mLog.severe(ex.getMessage());
                }
            }
            int idx = mEntityProperty.getSelectedIndex();
            
            if (mEntityProperty.getTabCount() > TAB_MATCHCONFIG_INDEX_ROOT && !(mEntityProperty.getComponentAt(TAB_MATCHCONFIG_INDEX_ROOT) instanceof TabMatchConfigPanel)) {
                mEntityProperty.add(mTabMatchConfigPanel, TAB_MATCHCONFIG_INDEX_ROOT);
                mEntityProperty.setTitleAt(TAB_MATCHCONFIG_INDEX_ROOT, TAB_MATCHCONFIGURATION);
            }
            if (idx >= 0) {   
                mEntityProperty.setSelectedIndex(idx);
            }
        }
        return mEntityProperty;
    }
    
    /**
     *@return JTabbedPane that contains EntityNode's property tabs
     */
    public JTabbedPane getConfigPropertySheet() {
        try {
            if (mEntityProperty == null) {
                mEntityProperty = new JTabbedPane();
                mEntityProperty.setBorder(new javax.swing.border.TitledBorder(
                                            new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED),
                                            NbBundle.getMessage(EntityNode.class, "MSG_Properties")));
            
                if (isPrimary() || isSub()) {
                    mPropertiesNamePanel = new PropertiesNamePanel(this, mEviewEditorMainApp.isCheckedOut());
                    mPropertiesNamePanel.setBorder(new javax.swing.border.EtchedBorder());
                    mEntityProperty.addTab(TAB_GENERAL, mPropertiesNamePanel);
                } else if (isField()) {
                    edmFieldDef = mEdmType.getEDMFieldDef(((EntityNode) this.getParent()).getName(), this.getName());
                    mPropertiesGeneralPanel = new TabGeneralPropertiesPanel(this.mEviewApplication, this.mEviewEditorMainApp, this, edmFieldDef);
                    mPropertiesGeneralPanel.setBorder(new javax.swing.border.EtchedBorder());

                    mEntityProperty.addTab(TAB_GENERAL, mPropertiesGeneralPanel);
                    //mPropertiesEDMPanel = new PropertiesEDMPanel(this, edmFieldDef);
                    //mPropertiesEDMPanel.setBorder(new javax.swing.border.EtchedBorder());
                    //mEntityProperty.addTab(TAB_EDM, mPropertiesEDMPanel);
                }
            }
            if (isField()) {
                boolean bBlockOn = false;

                try {
                    if (mEviewApplication.getQueryType(false) != null) {
                        ArrayList alBlockingSources = mEviewApplication.getQueryType(false).getBlockingSources();
                        if (alBlockingSources != null) {
                            EntityNode parentNode = (EntityNode) this.getParent();
                            String strField = parentNode.getName() + "." + this.getName();
                            if (parentNode.isSub()) {
                                strField = ((EntityNode) parentNode.getParent()).getName() + "." +  strField;
                            }
                            bBlockOn = alBlockingSources.contains(strField);
                        }
                    }
                    mPropertiesGeneralPanel.setBlockOn(bBlockOn);
                } catch (Exception ex) {
                    mLog.severe(ex.getMessage());
                }
                
                if (this.mEviewApplication.isMatchConfigModified()) {
                    mPropertiesGeneralPanel.setMatchTypeComboBox(mTabMatchConfigPanel.getMatchTypeList());
                }
                String matchType = this.getMatchType();            
                if (matchType != null && mEntityProperty.getTabCount() == 1) {
                    mTabMatchConfigPanel.setTargetMatchType(matchType, this.getFullFieldName());
                    mEntityProperty.addTab(TAB_MATCHCONFIGURATION, mTabMatchConfigPanel);
                    //mEntityProperty.remove(mTabMatchConfigPanel);
                    //mEntityProperty.addTab(TAB_MATCHCONFIGURATION, mTabMatchConfigPanel);
                    //enableMatchingTab(true, matchType, this.getFullFieldName());
                }
            }
        } catch (Exception ex) {
            mLog.severe(ex.getMessage());
        }
        return mEntityProperty;
    }

    /*
    private PropertiesEDMPanel getPropertiesEDMPanel() {
        if (mPropertiesEDMPanel == null) {
                edmFieldDef = mEdmType.getEDMFieldDef(((EntityNode) this.getParent()).getName(), this.getName());
                mPropertiesEDMPanel = new PropertiesEDMPanel(this, edmFieldDef);
                mPropertiesEDMPanel.setBorder(new javax.swing.border.EtchedBorder());
        }
        return mPropertiesEDMPanel;
    }
    */
    
    private TabGeneralPropertiesPanel getGeneralPropertiesPanel() {
        if (mPropertiesGeneralPanel == null) {
            edmFieldDef = mEdmType.getEDMFieldDef(((EntityNode) this.getParent()).getName(), this.getName());
            mPropertiesGeneralPanel = new TabGeneralPropertiesPanel(this.mEviewApplication, this.mEviewEditorMainApp, this, edmFieldDef);
            mPropertiesGeneralPanel.setBorder(new javax.swing.border.EtchedBorder());
        }
        return mPropertiesGeneralPanel;
    }
    
    /**
     *@return if it is a PrimaryNode
     */
    public boolean isPrimary() {
        return (this.mNodeType == NODE_PRIMARY);
    }

    /**
     *@return if it is a SubNode
     */
    public boolean isSub() {
        return (this.mNodeType == NODE_SUB);
    }

    /**
     *@return if it is a FieldNode
     */
    public boolean isField() {
        return (this.mNodeType == NODE_FIELD);
    }

    /**
     *@return field count
     */
    public int getFieldCnt() {
        int iRet = 0;
        int cnt = this.getChildCount();

        if (this.isPrimary()) {
            for (int i = 0; i < cnt; i++) {
                EntityNode subNode = (EntityNode) this.getChildAt(i);

                if (subNode.isField()) {
                    iRet++;
                }
            }
        } else {
            iRet = cnt;
        }

        return iRet;
    }

    /**
     *@return DataType
     */
    public String getDataType() {
        return getGeneralPropertiesPanel().getDataType();
    }

    /**
     *@return MatchType
     */
    public String getMatchType() {
        String matchType = getGeneralPropertiesPanel().getMatchType();

        if (matchType.equals(java.util.ResourceBundle.getBundle("com/sun/mdm/index/project/ui/applicationeditor/Bundle").getString("MSG_None"))) {
            matchType = null;
        }

        return matchType;
    }

    /**
     *@return mBlockOn
     */
    public boolean isBlockOn() {
        return this.mBlockOn;
    }

    /**
     *@return Blocking
     */
    public boolean getBlocking() {
        return (getGeneralPropertiesPanel().getBlocking() == "true");
    }

    /**
     *@return DataSize
     */
    public String getDataSize() {
        return getGeneralPropertiesPanel().getDataSize();
    }

    /**
     *@return KeyType
     */
    public String getKeyType() {
        return getGeneralPropertiesPanel().getKeyType();
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return getGeneralPropertiesPanel().getRequired();
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return getGeneralPropertiesPanel().getUpdateable();
    }

    /**
     *@return Code Module
     */
    public String getCodeModule() {
        return getGeneralPropertiesPanel().getCodeModule();
    }

    /**
     *@return User Code
     */
    public String getUserCode() {
        return getGeneralPropertiesPanel().getUserCode();
    }

    /**
     *@return Constraint By
     */
    public String getConstraintBy() {
        return getGeneralPropertiesPanel().getConstraintBy();
    }

    /**
     *@return Pattern
     */
    public String getPattern() {
        return getGeneralPropertiesPanel().getPattern();
    }

    /**
     *@return DisplayName
     */
    public String getDisplayName() {
        return getGeneralPropertiesPanel().getDisplayName();
    }

    /**
     *@return InputMask
     */
    public String getInputMask() {
        return getGeneralPropertiesPanel().getInputMask();
    }

    /**
     *@return ValueMask
     */
    public String getValueMask() {
        return getGeneralPropertiesPanel().getValueMask();
    }

    /**
     *@param order gui display order
     */
    public void setDisplayOrder(int order) {
        mDisplayOrder = order;
    }

    /**
     *@return DisplayOrder
     */
    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    /**
     *@return UsedInSearchScreen
     */
    //public boolean getUsedInSearchScreen() {
    //    return getPropertiesEDMPanel().getUsedInSearchScreen();
    //}

    /**
     *@return SearchRequired
     */
    //public String getSearchRequired() {
    //    return getPropertiesEDMPanel().getSearchRequired();
    //}

    /**
     *@return DisplayedInSearchResult
     */
    //public boolean getDisplayedInSearchResult() {
    //    return getPropertiesEDMPanel().getDisplayedInSearchResult();
    //}

    /**
     *@return GenerateReport
     */
    //public boolean getGenerateReport() {
    //    return getPropertiesEDMPanel().getGenerateReport();
    //}

    /**
     *@return NODE_PRIMARY
     */
    public static String getRootNodeType() {
        return NODE_ROOT;
    }

    /**
     *@return NODE_PRIMARY
     */
    public static String getPrimaryNodeType() {
        return NODE_PRIMARY;
    }

    /**
     *@return NODE_SUB
     */
    public static String getSubNodeType() {
        return NODE_SUB;
    }

    /**
     *@return NODE_FIELD
     */
    public static String getFieldNodeType() {
        return NODE_FIELD;
    }

    /**
     *@return mGeneratedField
     */
    public boolean isGeneratedField() {
        if (this.mEdmType != null && this.getParent() != null) {
            if (edmFieldDef == null) {
                edmFieldDef = mEdmType.getEDMFieldDef(((EntityNode) this.getParent()).getName(), this.getName());
            }
            // General
            // if edmFieldDef == null - It is generated node (through MatchType)
            mGeneratedField = (edmFieldDef == null);
        }
        return mGeneratedField;
    }
     
    /** Set the node name
     *
     *@param nodeName name
     */
    public void setName(String nodeName) {
        this.mNodeName = nodeName;
        this.setUserObject(nodeName);
        if (mPropertiesGeneralPanel != null) {
            mPropertiesGeneralPanel.setFieldName(nodeName);
        } else if (mPropertiesNamePanel != null) {
            mPropertiesNamePanel.setNameProperty(nodeName);
        }
    }

    /*
     *  Enable save button in main panel and set EviewApplication modified
     */
    public void enableSaveAction() {
        mEviewEditorMainApp.enableSaveAction(true);
        mEviewApplication.setModified(true);
    }
    
    /**
     *@param nodeName
     */
    public void setNodeName(String newName) {
        if (newName.equals(this.mNodeName)) {
            return;
        }
        
        enableSaveAction();
        /*
         * old name
         */
        TreeNode[] nodes = this.getPath();
        String oldNodeName = "";
        String oldNodeNameEdm = "";
        String oldNodeNameUpdateSub = "";        
        String oldNodeNameMefa = "";

        for (int i=0; i < nodes.length; i++) {
            EntityNode node = (EntityNode) nodes[i];
            String nodeName = node.getName();
            if (node.isRoot()) {
            } else if (node.isPrimary()) {
                oldNodeName = nodeName;
                oldNodeNameEdm = nodeName;
                oldNodeNameMefa = nodeName;
            } else if (node.isSub()) {
                oldNodeName += "." + nodeName;
                oldNodeNameEdm = nodeName;
                oldNodeNameMefa += "." + nodeName + "[*]";
            } else { // isField
                oldNodeName += "." + nodeName;
                oldNodeNameEdm += "." + nodeName;
                oldNodeNameMefa += "." + nodeName;
            }
        }
        oldNodeNameUpdateSub = oldNodeName;
        if (this.isPrimary() || this.isSub()) {
            oldNodeName += ".";
            oldNodeNameEdm += ".";
            oldNodeNameMefa += ".";
        }
        if (this.isSub()) {
            oldNodeNameUpdateSub += "[";
        }
        
        setName(newName);
        /*
         * new name
         */
        nodes = this.getPath();
        String newNodeName = "";
        String newNodeNameEdm = "";
        String newNodeNameUpdateSub = "";
        String newNodeNameMefa = "";
        for (int i=0; i < nodes.length; i++) {
            EntityNode node = (EntityNode) nodes[i];
            String nodeName = node.getName();
            if (node.isRoot()) {
            } else if (node.isPrimary()) {
                newNodeName = nodeName;
                newNodeNameEdm = nodeName;
                newNodeNameMefa = nodeName;
            } else if (node.isSub()) {
                newNodeName += "." + nodeName;
                newNodeNameEdm = nodeName;
                newNodeNameMefa += "." + nodeName + "[*]";
            } else { // isField
                newNodeName += "." + nodeName;
                newNodeNameEdm += "." + nodeName;
                newNodeNameMefa += "." + nodeName;
            }
        }
        newNodeNameUpdateSub = newNodeName;
        if (this.isPrimary() || this.isSub()) {
            newNodeName += ".";
            newNodeNameEdm += ".";
            newNodeNameMefa += ".";
        }
        if (this.isSub()) {
            newNodeNameUpdateSub += "[";
        }
        
        if (this.mEntityTree != null) {
            this.mEntityTree.clearSelection();
            this.mEntityTree.setSelectionPath(new TreePath(this));
            EntityNode rootNode = this.mEntityTree.getRootNode();
            rootNode.updateReferencedField(this, oldNodeName, newNodeName, 
                                           oldNodeNameEdm, 
                                           newNodeNameEdm, 
                                           oldNodeNameUpdateSub,
                                           newNodeNameUpdateSub, 
                                           oldNodeNameMefa, newNodeNameMefa);
        }
    }
    
    
    /* Only for root node
     * called by setNodeName() when object/field name changed
     *
     */
    public void updateReferencedField(EntityNode selectedNode, String oldNodeName, String newNodeName, String oldNodeNameEdm, String newNodeNameEdm, String oldNodeNameUpdateSub, String newNodeNameUpdateSub, String oldNodeNameMefa, String newNodeNameMefa) {
        if (isRoot()) {
            String oldName = oldNodeName;
            String newName = newNodeName;
            
            boolean bEdmUpdated = false;
            boolean bUpdateXmlUpdated = false;
            try {
                bEdmUpdated = mEviewApplication.getEDMType(false).updateReferencedField(oldNodeNameEdm, newNodeNameEdm);
                bUpdateXmlUpdated = mEviewApplication.getUpdateType(false).updateReferencedField(oldName, newName, oldNodeNameUpdateSub, newNodeNameUpdateSub);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
            boolean bBlockingUpdated = mTabQueryPanel.updateReferencedField(oldName, newName);
            
            boolean bMatchingUpdated = mTabMatchConfigPanel.updateReferencedField(oldNodeNameMefa, newNodeNameMefa);
            
            // Only for mefa.xml
            boolean bStandardizationUpdated = mTabStandardizationPanel.updateReferencedField(oldNodeNameMefa, newNodeNameMefa);
            boolean bNormalizationUpdated = mTabNormalizationPanel.updateReferencedField(oldNodeNameMefa, newNodeNameMefa);
            boolean bPhoneticizedFieldsUpdated = mTabPhoneticizedFieldsPanel.updateReferencedField(oldNodeNameMefa, newNodeNameMefa);
            if (bEdmUpdated || bMatchingUpdated || bBlockingUpdated || bStandardizationUpdated || bNormalizationUpdated || bPhoneticizedFieldsUpdated) {
                String msg = "\"" + oldNodeName + "\" " + 
                        NbBundle.getMessage(EntityNode.class, "MSG_Update_Cross_Referenced_Fields") +
                        (bEdmUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_EDM_Updated") : "") +
                        (bMatchingUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Matching_Updated") : "") +
                        (bBlockingUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Query_Updated") : "") +
                        (bStandardizationUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Standardization_Updated") : "") +
                        (bNormalizationUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Normalization_Updated") : "") +
                        (bPhoneticizedFieldsUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Phoneticized_Fields_Updated") : "") +
                        (bUpdateXmlUpdated ?  "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Best_Record_Updated") : "");
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }
        }
    }
    
    /* Only for root node
     * called by setNodeName() when object/field name changed
     *
     */
    public void removeReferencedField(EntityNode selectedNode) {
        if (isRoot()) {
            TreeNode[] nodes = selectedNode.getPath();
            String fieldNamePath = "";
            String fieldNamePathEdm = "";
            String fieldNamePathMefa = "";
            for (int i=0; i < nodes.length; i++) {
                EntityNode node = (EntityNode) nodes[i];
                String nodeName = node.getName();
                if (node.isRoot()) {
                } else if (node.isPrimary()) {
                    fieldNamePath = nodeName;
                    fieldNamePathEdm = nodeName;
                    fieldNamePathMefa = nodeName;
                } else if (node.isSub()) {
                    fieldNamePath += "." + nodeName;
                    fieldNamePathEdm = nodeName;
                    fieldNamePathMefa += "." + nodeName + "[*]";
                } else { // isField
                    fieldNamePath += "." + nodeName;
                    fieldNamePathEdm += "." + nodeName;
                    fieldNamePathMefa += "." + nodeName;
                }
            }
            
            boolean bEdmUpdated = false;
            boolean bUpdateXmlUpdated = false;
            try {
                bEdmUpdated = mEviewApplication.getEDMType(false).removeReferencedField(fieldNamePathEdm);
                bUpdateXmlUpdated = mEviewApplication.getUpdateType(false).removeReferencedField(fieldNamePath);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
            boolean bBlockingUpdated = mTabQueryPanel.removeReferencedField(fieldNamePath);
            boolean bMatchingUpdated = mTabMatchConfigPanel.removeReferencedField(fieldNamePathMefa);
            // Only for mefa.xml
            // use fieldNamePathMefa
            boolean bStandardizationUpdated = mTabStandardizationPanel.removeReferencedField(fieldNamePathMefa);
            boolean bNormalizationUpdated = mTabNormalizationPanel.removeReferencedField(fieldNamePathMefa);
            boolean bPhoneticizedFieldsUpdated = mTabPhoneticizedFieldsPanel.removeReferencedField(fieldNamePathMefa);
            if (bEdmUpdated || bMatchingUpdated || bBlockingUpdated || bStandardizationUpdated || bNormalizationUpdated || bPhoneticizedFieldsUpdated) {
                String msg = "\"" + selectedNode.getName() + "\" " + 
                        NbBundle.getMessage(EntityNode.class, "MSG_Remove_Cross_Referenced_Fields") +
                        (bEdmUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_EDM_Updated") : "") +
                        (bMatchingUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Matching_Updated") : "") +
                        (bBlockingUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Query_Updated") : "") +
                        (bStandardizationUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Standardization_Updated") : "") +
                        (bNormalizationUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Normalization_Updated") : "") +
                        (bPhoneticizedFieldsUpdated ? "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Phoneticized_Fields_Updated") : "") +
                        (bUpdateXmlUpdated ?  "\n" + NbBundle.getMessage(EntityNode.class, "MSG_Best_Record_Updated") : "");
                NotifyDescriptor desc = new NotifyDescriptor.Message(msg);
                DialogDisplayer.getDefault().notify(desc);
            }

        }
    }
    
    public boolean updateCheckedAttributes(String attributeName, boolean checked, String required) {
            boolean bEdmUpdated = false;
            TreeNode[] nodes = this.getPath();
            String fieldNamePathEdm = "";
            for (int i=0; i < nodes.length; i++) {
                EntityNode node = (EntityNode) nodes[i];
                String nodeName = node.getName();
                if (node.isRoot()) {
                } else if (node.isPrimary()) {
                    fieldNamePathEdm = nodeName;
                } else if (node.isSub()) {
                    fieldNamePathEdm = nodeName;
                } else { // isField
                    fieldNamePathEdm += "." + nodeName;
                }
            }

            try {
                bEdmUpdated = mEviewApplication.getEDMType(false).updateCheckedAttributes(fieldNamePathEdm, attributeName, checked, required);
            } catch (Exception ex) {
                mLog.severe(ex.getMessage());
            }
            return bEdmUpdated;
    }
    
    public FieldDef getFieldDef() {
        return this.mFieldDef;
    }
    
    public String getFullFieldName() {
        String fullFieldName;
        EntityNode parentNode = (EntityNode) this.getParent();
        if (parentNode.isSub()) {
            EntityNode grandParentNode = (EntityNode) parentNode.getParent();
            fullFieldName = grandParentNode.getName() + "." + parentNode.getName() + "[*]." + this.getName();
        } else {
            fullFieldName = parentNode.getName() + "." + this.getName();
        }
        return fullFieldName;
    }
     
    public String getEnterpriseFieldName() {
        EntityNode parentNode = (EntityNode) this.getParent();
        String fieldName = this.getName();
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
     *@param enabled
     */
    public void enableMatchingTab(boolean enabled, String matchType, String fullFieldName) {
        if (mTabMatchConfigPanel != null && mEntityProperty != null) {
            if (enabled) {
                int index = -1;
                int tabCnt = mEntityProperty.getTabCount();
                if (tabCnt == 1) {
                    index = 1;
                } else if (tabCnt == 2 && !(mEntityProperty.getComponentAt(1) instanceof TabMatchConfigPanel)) {
                    index = 1;
                }
                
                if (index != -1) {
                    mEntityProperty.addTab(TAB_MATCHCONFIGURATION, mTabMatchConfigPanel);
                    mEntityProperty.remove(mTabMatchConfigPanel);
                    mEntityProperty.addTab(TAB_MATCHCONFIGURATION, mTabMatchConfigPanel);
                    //mEntityProperty.setTitleAt(index, TAB_MATCHCONFIGURATION);
                    //mEntityProperty.setSelectedComponent(mPropertiesGeneralPanel);
                    //mEntityProperty.setSelectedIndex(0);    // set it back to General
                }
                mTabMatchConfigPanel.setTargetMatchType(matchType, fullFieldName);
            } else {
                mTabMatchConfigPanel.setTargetMatchType(null, fullFieldName);
                mEntityProperty.remove(mTabMatchConfigPanel);
            }
        }
    }
    
    /**
     *@param enabled
     */
    public void enableBlockingTab(boolean enabled) {
        if (mTabQueryPanel != null && mEntityProperty != null) {
            if (enabled) {
                int index = -1;
                int tabCnt = mEntityProperty.getTabCount();
                if (tabCnt == 2) {
                    index = 2;
                } else if (tabCnt == 3 && !(mEntityProperty.getComponentAt(2) instanceof TabBlockingPanel)) {
                    index = 3;
                }
                
                if (index != -1) {
                    mEntityProperty.add(mTabQueryPanel, index);
                    mEntityProperty.setTitleAt(index, TAB_QUERY);
                    mEntityProperty.setSelectedComponent(mPropertiesGeneralPanel);
                    //mEntityProperty.setSelectedIndex(0);    // set it back to General
                }
            } else {
                mEntityProperty.remove(mTabQueryPanel);
            }
        }
    }
    
    public void setSelectedTabApplication() {
        mEntityProperty.setSelectedComponent(mPropertiesDeploymentPanel);
    }
    
    public void setSelectedTabBlocking() {
        mEntityProperty.setSelectedComponent(mTabQueryPanel);
    }
    
    public void setSelectedTabStandardization() {
        mEntityProperty.setSelectedComponent(mTabStandardizationPanel);
    }
    
    public void setSelectedTabNormalization() {
        mEntityProperty.setSelectedComponent(mTabNormalizationPanel);
    }
    
    /* Obsolete
     */
    public void setSelectedTabMatchConfig(String matchType) {
        mEntityProperty.setSelectedComponent(mTabMatchConfigPanel);
        mTabMatchConfigPanel.setTargetMatchType(matchType, null);
    }
    
    
    public void setSelectedTabPhoneticizedFields() {
        mEntityProperty.setSelectedComponent(mTabPhoneticizedFieldsPanel);
    }

    
    public void setJustAdded() {
        mJustAdded = true;
    }
    
    public boolean isJustAdded() {
        return mJustAdded;
    }
    
    
    public static boolean checkNodeNameValue(String value) {
        boolean bRet = true;
        NotifyDescriptor desc;
        if (value.length() == 0) {
            Toolkit.getDefaultToolkit().beep();

            desc = new NotifyDescriptor.Message(NbBundle.getMessage(EntityNode.class, "MSG_Name_Cannot_Be_Empty"));
            desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
            desc.setTitle("Error");
            desc.setTitle(NbBundle.getMessage(EntityNode.class, "MSG_Error"));
            DialogDisplayer.getDefault().notify(desc);
            return false;
        }
        if (value.length() > 20) {
            Toolkit.getDefaultToolkit().beep();
            String prompt = NbBundle.getMessage(EntityNode.class, "MSG_Long_Field_Name", value);
            desc = new NotifyDescriptor.Message(prompt);
            desc.setMessageType(NotifyDescriptor.WARNING_MESSAGE);
            desc.setTitle(NbBundle.getMessage(EntityNode.class, "MSG_Warning"));
            DialogDisplayer.getDefault().notify(desc);
        }
        
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' || c == ' ') {
                Toolkit.getDefaultToolkit().beep();
                String msgC = "" + c;
                String msg = NbBundle.getMessage(EntityNode.class, "MSG_Invalid_Character", msgC);
                desc = new NotifyDescriptor.Message(msg);
                desc.setMessageType(NotifyDescriptor.ERROR_MESSAGE);
                desc.setTitle(NbBundle.getMessage(EntityNode.class, "MSG_Error"));
                DialogDisplayer.getDefault().notify(desc);
                bRet = false;
                break;
            }
        }
        return bRet;
    }
}
