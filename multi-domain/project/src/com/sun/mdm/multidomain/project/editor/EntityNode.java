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

import org.openide.util.NbBundle;
import javax.swing.tree.DefaultMutableTreeNode;
import com.sun.mdm.multidomain.parser.MiFieldDef;

public class EntityNode extends DefaultMutableTreeNode {
    /** The logger. */
    private static final com.sun.mdm.multidomain.util.Logger mLog = com.sun.mdm.multidomain.util.Logger.getLogger(
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
    
    String mNodeName;
    String mNodeType;
    EntityTree mEntityTree = null;
    MiFieldDef mMiFieldDef = null;

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
    }

    /** Creates a new instance of EntityNode
     *
     *@param nodeName node name
     *@param nodeType node type
     */
    public EntityNode(EntityTree entityTree, String nodeName, MiFieldDef fieldDef, boolean bBlockOn) {
        this(entityTree, nodeName, NODE_FIELD);
        mMiFieldDef = fieldDef;
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
        mMiFieldDef = new MiFieldDef(nodeName, defaultDataType, null, defaultBlocking, defaultKeyType, defaultUpdateable,
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
     
    /** Set the node name
     *
     *@param nodeName name
     */
    public void setName(String nodeName) {
        this.mNodeName = nodeName;
        this.setUserObject(nodeName);
    }

    /*
     *  Enable save button in main panel and set EviewApplication modified
     */
    public void enableSaveAction() {
    }
    
    
    
    public MiFieldDef getFieldDef() {
        return this.mMiFieldDef;
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
    
}
