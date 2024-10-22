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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David Peh
 */
public class HierarchyNodeDto implements Serializable {

    /**
     * This attribute maps to the column HIERARCHY_NODE_ID in the hierarchy_node table.
     */
    protected long hierarchyNodeId;
    protected long parentNodeId;
    /**
     * This attribute maps to the column HIERARCHY_DEF_ID in the hierarchy_node table.
     */
    protected long hierarchyDefId;
    /**
     * This attribute maps to the column EUID in the hierarchy_node table.
     */
    protected String euid;
    /**
     * This attribute maps to the column PARENT_EUID in the hierarchy_node table.
     */
    protected String parentEuid;
    /**
     * This attribute maps to the column effective_from_date in the hierarchy_node table.
     */
    protected Date effectiveFromDate;
    /**
     * This attribute maps to the column effective_to_date in the hierarchy_node table.
     */
    protected Date effectiveToDate;
    private Map<HierarchyNodeEaDto, String> attributeValues = null;

    /**
     * Method 'HierarchyNodeDto'
     *
     */
    public HierarchyNodeDto() {
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
     * Method 'getHierarchyDefId'
     *
     * @return long
     */
    public long getHierarchyDefId() {
        return hierarchyDefId;
    }

    /**
     * Method 'setParentNodeId'
     *
     * @param parentId
     */
    public void setParentNodeId(long parentId) {
        this.parentNodeId = parentId;
    }

    /**
     * Method 'getParentNodeId'
     *
     * @return long
     */
    public long getParentNodeId() {
        return parentNodeId;
    }

    /**
     * Method 'setHierarchyDefId'
     *
     * @param hierarchyDefId
     */
    public void setHierarchyDefId(long hierarchyDefId) {
        this.hierarchyDefId = hierarchyDefId;
    }

    /**
     * Method 'getEuid'
     *
     * @return String
     */
    public String getEuid() {
        return euid;
    }

    /**
     * Method 'setEuid'
     *
     * @param euid
     */
    public void setEuid(String euid) {
        this.euid = euid;
    }

    /**
     * Method 'getParentEuid'
     *
     * @return String
     */
    public String getParentEuid() {
        return parentEuid;
    }

    /**
     * Method 'setParentEuid'
     *
     * @param parentEuid
     */
    public void setParentEuid(String parentEuid) {
        this.parentEuid = parentEuid;
    }

    /**
     * Method 'getEffectiveFromDate'
     *
     * @return Date
     */
    public Date getEffectiveFromDate() {
        return effectiveFromDate;
    }

    /**
     * Method 'setEffectiveFromDate'
     *
     * @param effectiveFromDate
     */
    public void setEffectiveFromDate(Date effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }

    /**
     * Method 'getEffectiveToDate'
     *
     * @return Date
     */
    public Date getEffectiveToDate() {
        return effectiveToDate;
    }

    /**
     * Method 'setEffectiveToDate'
     *
     * @param effectiveToDate
     */
    public void setEffectiveToDate(Date effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }

    /**
     * Method 'setHierarchyAttributes'
     *
     * @param attrValues
     */
    public void setHierarchyAttributes(Map<HierarchyNodeEaDto, String> attrValues) {
        this.attributeValues = attrValues;
    }

    /**
     * Method 'getHierarchyAttributes'
     *
     * @return attributeValues
     */
    public Map<HierarchyNodeEaDto, String> getHierarchyAttributes() {
        if (attributeValues == null) {
            attributeValues = new HashMap<HierarchyNodeEaDto, String>();
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

        if (!(_other instanceof HierarchyNodeDto)) {
            return false;
        }

        final HierarchyNodeDto _cast = (HierarchyNodeDto) _other;
        if (_cast.hierarchyNodeId != hierarchyNodeId) {
            return false;
        }

        if (_cast.hierarchyDefId != hierarchyDefId) {
            return false;
        }

        if (euid == null ? _cast.euid != euid : !euid.equals(_cast.euid)) {
            return false;
        }

        if (parentEuid == null ? _cast.parentEuid != parentEuid : !parentEuid.equals(_cast.parentEuid)) {
            return false;
        }

        if (effectiveFromDate == null ? _cast.effectiveFromDate != effectiveFromDate : !effectiveFromDate.equals(_cast.effectiveFromDate)) {
            return false;
        }

        if (effectiveToDate == null ? _cast.effectiveToDate != effectiveToDate : !effectiveToDate.equals(_cast.effectiveToDate)) {
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

        _hashCode = 29 * _hashCode + (int) hierarchyNodeId;



        _hashCode = 29 * _hashCode + (int) hierarchyDefId;


        if (euid != null) {
            _hashCode = 29 * _hashCode + euid.hashCode();
        }

        if (parentEuid != null) {
            _hashCode = 29 * _hashCode + parentEuid.hashCode();
        }

        if (effectiveFromDate != null) {
            _hashCode = 29 * _hashCode + effectiveFromDate.hashCode();
        }

        if (effectiveToDate != null) {
            _hashCode = 29 * _hashCode + effectiveToDate.hashCode();
        }

        return _hashCode;
    }

    /**
     * Method 'toString'
     *
     * @return String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.mycompany.myapp.dto.HierarchyNode: ");
        ret.append("hierarchyNodeId=" + hierarchyNodeId);
        ret.append(", hierarchyDefId=" + hierarchyDefId);
        ret.append(", euid=" + euid);
        ret.append(", parentEuid=" + parentEuid);
        ret.append(", effectiveFromDate=" + effectiveFromDate);
        ret.append(", effectiveToDate=" + effectiveToDate);
        return ret.toString();
    }
}
