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
package com.sun.mdm.index.query;



/**
 * This data structure contains attributes for each object that is created by
 * Assembler Engine and these attributes are used during assembling process. All
 * of these attributes are derived from SQLDescriptor. These attributes are
 * never modified during assembling. In other words the collection of these
 * objects is unique to a particular QueryObject. During assembling
 * initialization, these attributes are copied to QueryResultSet.CurrentObject
 * which contains some attributes that are particular to an instance of JDBC
 * ResultSet.
 *
 * @author sdua
 */
class CreateObjectMeta {
    // CreateObjectMeta is stored in a particular CreateObjectList, that represents
    // objects to be created in a particular SQL query. If an object that is not a leaf
    // node (so has a child), then mchildIndex will store the value of index of the child
    // in the same CreateObjectList.
    private int mchildIndex = -1;
    private boolean mcreateFlag = true;
    private final String mobjName;

    /// If an object in the object graph is not a root and has more than one child,
    // then all other children except one will be retrieved in other JDBC ResultSets.
    // So in that case,  objects have to be saved in some place so that it can be
    // referenced by the children in other result sets.massObjState stores the
    // AssembleObjectState of such object.
    private AssembleObjectState massObjState;
    private int mattrIndexHigh;
    private int mattrIndexLow;
    private String[] mattributes;
    private boolean misLeaf;
    private Integer[] mkeyIndices;
    private Integer[] mparentKeyIndices;

    // This references the AssembleObjectState of an object that is parent to this
    // object, whose AssembleObjectState is not null.
    private AssembleObjectState mparentObjState;

    // The ValueMetaNode corresponding to this CreateObjectMeta.
    private ValueMetaNode mvalueMetaNode;


    /**
     * Creates a new instance of CreateObjectMeta
     *
     * @param valueMetaNode
     */
    CreateObjectMeta(ValueMetaNode valueMetaNode) {
        mvalueMetaNode = valueMetaNode;
        mobjName = valueMetaNode.getName();
    }


    final AssembleObjectState getAssembleObjectState() {
        return massObjState;
    }


    /*
          public CreateObjectMeta(String objName) {
          mobjName = objName;
          }
      */
    final int getAttrIndexHigh() {
        return mattrIndexHigh;
    }


    final int getAttrIndexLow() {
        return mattrIndexLow;
    }


    final String[] getAttributes() {
        return mattributes;
    }


    final int getChildIndex() {
        return mchildIndex;
    }


    final Integer[] getKeyIndices() {
        return mkeyIndices;
    }


    final String getObjName() {
        return mobjName;
    }


    final Integer[] getParentKeyIndices() {
        return mparentKeyIndices;
    }


    final AssembleObjectState getParentObjectState() {
        return mparentObjState;
    }


    final ValueMetaNode getValueMetaNode() {
        return mvalueMetaNode;
    }


    final void setAssembleObjectState(AssembleObjectState assObjState) {
        massObjState = assObjState;
    }


    final void setAttrIndexHigh(int attrIndexHigh) {
        mattrIndexHigh = attrIndexHigh;
    }


    final void setAttrIndexLow(int attrIndexLow) {
        mattrIndexLow = attrIndexLow;
    }


    final void setAttributes(String[] attributes) {
        mattributes = attributes;
    }


    final void setChildIndex(int index) {
        mchildIndex = index;
    }


    final void setCreateFlag(boolean createFlag) {
        mcreateFlag = createFlag;
    }


    final void setKeyIndices(Integer[] keyIndices) {
        mkeyIndices = keyIndices;
    }


    final void setParentKeyIndices(Integer[] parentIndices) {
        mparentKeyIndices = parentIndices;
    }


    final void setParentObjectState(AssembleObjectState assObjState) {
        mparentObjState = assObjState;
    }


    final void setValueMetaNode(ValueMetaNode valueMetaNode) {
        mvalueMetaNode = valueMetaNode;
    }
}
