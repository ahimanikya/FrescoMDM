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
package com.sun.mdm.multidomain.hierarchy.ops.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David Peh
 */
public class HierarchyNodeEavDto implements Serializable {

    /**
     * This attribute maps to the column EAV_ID in the hierarchy_node_eav table.
     */
    protected long eavId;
    /**
     * This attribute maps to the column HIERARCHY_NODE_ID in the hierarchy_node_eav table.
     */
    protected long hierarchyNodeId;
    private Map<String, String> attributeValues = null;

    /**
     * Method 'HierarchyNodeEav'
     *
     */
    public HierarchyNodeEavDto() {
    }

    /**
     * Method 'getEavId'
     *
     * @return Integer
     */
    public long getEavId() {
        return eavId;
    }

    /**
     * Method 'setEavId'
     *
     * @param eavId
     */
    public void setEavId(long eavId) {
        this.eavId = eavId;
    }

    /**
     * Method 'getHierarchyNodeId'
     *
     * @return Integer
     */
    public long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    /**
     * Method 'setHierarchyNodeId'
     *
     * @param hierarchyNodeId
     */
    public void setHierarchyNodeId(long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }

    /**
     * Set attribute value.
     * @param attribute Attribute.
     * @param value Attribute value.
     */
    public void setAttributes(Map<String, String> attributeValues) {
        this.attributeValues = attributeValues;
    }

    /**
     * Get attribute value.
     * @param attribute Attribute.
     * @return String Attribute value.
     */
    public Map<String, String> getAttributes() {
        if (attributeValues == null) {
            attributeValues = new HashMap<String, String>();
        }
        return attributeValues;
    }

    /**
     * Method 'equals'
     *
     * @param _other
     * @return boolean
     */
    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }

        if (_other == this) {
            return true;
        }

        if (!(_other instanceof HierarchyNodeEavDto)) {
            return false;
        }

        final HierarchyNodeEavDto _cast = (HierarchyNodeEavDto) _other;
        if (_cast.eavId != eavId) {
            return false;
        }

        if (_cast.hierarchyNodeId != hierarchyNodeId) {
            return false;
        }

        return true;
    }

    /**
     * Method 'hashCode'
     *
     * @return int
     */
    public int hashCode() {
        int _hashCode = 0;

        _hashCode = 29 * _hashCode + (int) eavId;
        _hashCode = 29 * _hashCode + (int) hierarchyNodeId;


        return _hashCode;
    }

    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.mycompany.myapp.dto.HierarchyNodeEav: ");
        ret.append("eavId=" + eavId);
        ret.append(", hierarchyNodeId=" + hierarchyNodeId);
        return ret.toString();
    }
}
