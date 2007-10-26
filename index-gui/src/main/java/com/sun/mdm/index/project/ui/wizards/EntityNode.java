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
import javax.swing.tree.DefaultMutableTreeNode;

public class EntityNode extends DefaultMutableTreeNode {
    static final String NODE_ROOT = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Root");
    static final String NODE_PRIMARY = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Primary");
    static final String NODE_PRIMARY_FIELDS = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Primary_Fields");
    static final String NODE_SUB = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Sub");
    static final String NODE_FIELD = NbBundle.getMessage(EntityNode.class,
            "MSG_NodeType_Field");
    String mNodeName;
    String mNodeType;
    JTabbedPane mEntityProperty = null;
    PropertySheetModel mPropertySheetModel = null;
    int mDisplayOrder = 0;

    /** Creates a new instance of EntityNodes
     *
     *@param nodeName node name
     *@param nodeType node type
     */
    public EntityNode(String nodeName, String nodeType) {
        super(nodeName);
        this.mNodeName = nodeName;
        this.mNodeType = nodeType;

        //EntityNodeKeyListener myKeyListener = new EntityNodeKeyListener();
    }

    /** Creates a new instance of EntityNodes
     *
     *@param nodeName node name
     *@param nodeType node type
     *@param defaultDataType default data type
     */
    public EntityNode(String nodeName, String nodeType, String defaultDataType) {
        super(nodeName);
        this.mNodeName = nodeName;
        this.mNodeType = nodeType;
        mPropertySheetModel = new PropertySheetModel(nodeName, defaultDataType);
    }

    /** Creates a new instance of EntityNodes
     *
     *@param nodeName node name
     *@param nodeType node type
     *@param defaultDataType default data type
     *@param displayOrder gui display order
     */
    public EntityNode(String nodeName, String nodeType, String defaultDataType,
        int displayOrder) {
        super(nodeName);
        this.mNodeName = nodeName;
        this.mNodeType = nodeType;
        this.mDisplayOrder = displayOrder;
        mPropertySheetModel = new PropertySheetModel(nodeName, defaultDataType);
    }

    /** Creates a new instance of EntityNodes
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
    public EntityNode(String nodeName, String defaultDisplayName,
        String nodeType, String defaultDataType, int displayOrder,
        String defaultInputMask, String defaultValueMask,
        String defaultSearchable, String defaultDisplayedInResult,
        String defaultKeyType, String defaultUpdateable,
        String defaultRequired, String defaultMatchType,
        String defaultDataSize, String defaultCodeModule,
        String defaultPattern,  String defaultBlocking, 
        String defaultUserCode, String defaultConstraintBy,
        String defaultGenerateReport) {
        super(nodeName);
        this.mNodeName = nodeName;
        this.mNodeType = nodeType;
        this.mDisplayOrder = displayOrder;
        mPropertySheetModel = new PropertySheetModel(defaultDisplayName,
                defaultDataType, defaultInputMask, defaultValueMask,
                defaultSearchable, defaultDisplayedInResult, defaultKeyType,
                defaultUpdateable, defaultRequired, defaultMatchType,
                defaultDataSize, defaultCodeModule, defaultPattern,
                defaultBlocking, defaultUserCode, defaultConstraintBy, defaultGenerateReport);
    }

    /** Set the node name
     *
     *@param nodeName name
     */
    public void setName(String nodeName) {
        this.mNodeName = nodeName;
        this.setUserObject(nodeName);
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
     *@return JPanel that contains properties
     */
    public JTabbedPane getPropertySheet() {
        if (isField() && (mEntityProperty == null)) {
            mEntityProperty = mPropertySheetModel.getPropertySheet();
        }

        return mEntityProperty;
    }

    /**
     *@return if it is a PrimaryNode
     */
    public boolean isPrimary() {
        return (this.mNodeType == NODE_PRIMARY);
    }

    /**
     *@return if it is a PrimaryFieldsNode
     */
    public boolean isPrimaryFields() {
        return (this.mNodeType == NODE_PRIMARY_FIELDS);
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

    // Return
    // eDataType - return String
    // eDataSize - return (int)
    // eDisplayName - return String
    // eInputMask - return String
    // eMatchType - return String
    // eKeyType - return (Boolean)
    // eUsedInSearchScreen - return (Boolean)
    // eDisplayedInSearchResult - return (Boolean)
    // 

    /*
    public String getPropertyValue() {
        String val = mPropertySheetModel.getPropertyValue();
        return val;
    }
    */

    /**
     *@return DataType
     */
    public String getDataType() {
        return mPropertySheetModel.getDataType();
    }

    /**
     *@return MatchType
     */
    public String getMatchType() {
        String matchType = mPropertySheetModel.getMatchType();

        if (matchType == "None") {
            matchType = null;
        }

        return matchType;
    }

    /**
     *@return Blocking
     */
    public boolean getBlocking() {
        return (mPropertySheetModel.getBlocking() == "true");
    }

    /**
     *@return DataSize
     */
    public String getDataSize() {
        return mPropertySheetModel.getDataSize();
    }

    /**
     *@return KeyType
     */
    public String getKeyType() {
        return mPropertySheetModel.getKeyType();
    }

    /**
     *@return Required
     */
    public String getRequired() {
        return mPropertySheetModel.getRequired();
    }

    /**
     *@return Updateable
     */
    public String getUpdateable() {
        return mPropertySheetModel.getUpdateable();
    }

    /**
     *@return Code Module
     */
    public String getCodeModule() {
        return mPropertySheetModel.getCodeModule();
    }

    /**
     *@return User Code
     */
    public String getUserCode() {
        return mPropertySheetModel.getUserCode();
    }

    /**
     *@return Constraint By
     */
    public String getConstraintBy() {
        return mPropertySheetModel.getConstraintBy();
    }

    /**
     *@return Pattern
     */
    public String getPattern() {
        return mPropertySheetModel.getPattern();
    }

    /**
     *@return DisplayName
     */
    public String getDisplayName() {
        return mPropertySheetModel.getDisplayName();
    }

    /**
     *@return InputMask
     */
    public String getInputMask() {
        return mPropertySheetModel.getInputMask();
    }

    /**
     *@return ValueMask
     */
    public String getValueMask() {
        return mPropertySheetModel.getValueMask();
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
    public boolean getUsedInSearchScreen() {
        return mPropertySheetModel.getUsedInSearchScreen();
    }

    /**
     *@return SearchRequired
     */
    public String getSearchRequired() {
        return mPropertySheetModel.getSearchRequired();
    }

    /**
     *@return DisplayedInSearchResult
     */
    public boolean getDisplayedInSearchResult() {
        return mPropertySheetModel.getDisplayedInSearchResult();
    }

    /**
     *@return GenerateReport
     */
    public boolean getGenerateReport() {
        return mPropertySheetModel.getGenerateReport();
    }

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
     *@return NODE_PRIMARY_FIELDS
     */
    public static String getPrimaryFieldsNodeType() {
        return NODE_PRIMARY_FIELDS;
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

}
