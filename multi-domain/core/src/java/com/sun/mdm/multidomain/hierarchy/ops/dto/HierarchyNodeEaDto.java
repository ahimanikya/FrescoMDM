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

/**
 *
 * @author David Peh
 */
public class HierarchyNodeEaDto implements Serializable {

    /**
     * This attribute maps to the column EA_ID in the hierarchy_node_ea table.
     */
    protected long eaId;
    /**
     * This attribute maps to the column HIERARCHY_DEF_ID in the hierarchy_node_ea table.
     */
    protected long hierarchyDefId;
    /**
     * This attribute maps to the column ATTRIBUTE_NAME in the hierarchy_node_ea table.
     */
    protected String attributeName;
    /**
     * This attribute maps to the column COLUMN_NAME in the hierarchy_node_ea table.
     */
    protected String columnName;
    /**
     * This attribute maps to the column COLUMN_TYPE in the hierarchy_node_ea table.
     */
    protected String columnType;
    /**
     * This attribute maps to the column DEFAULT_VALUE in the hierarchy_node_ea table.
     */
    protected String defaultValue;
    /**
     * This attribute maps to the column IS_SEARCHABLE in the hierarchy_node_ea table.
     */
    protected String isSearchable;
    /**
     * This attribute maps to the column IS_REQUIRED in the hierarchy_node_ea table.
     */
    protected String isRequired;

    /**
     * Method 'HierarchyNodeEa'
     *
     */
    public HierarchyNodeEaDto() {
    }

    /**
     * Method 'getEaId'
     *
     * @return Integer
     */
    public long getEaId() {
        return eaId;
    }

    /**
     * Method 'setEaId'
     *
     * @param eaId
     */
    public void setEaId(long eaId) {
        this.eaId = eaId;
    }

    /**
     * Method 'getHierarchyDefId'
     *
     * @return Integer
     */
    public long getHierarchyDefId() {
        return hierarchyDefId;
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
     * Method 'getAttributeName'
     *
     * @return String
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Method 'setAttributeName'
     *
     * @param attributeName
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Method 'getColumnName'
     *
     * @return String
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Method 'setColumnName'
     *
     * @param columnName
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Method 'getColumnType'
     *
     * @return String
     */
    public String getColumnType() {
        return columnType;
    }

    /**
     * Method 'setColumnType'
     *
     * @param columnType
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    /**
     * Method 'getDefaultValue'
     *
     * @return String
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Method 'setDefaultValue'
     *
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Method 'getIsSearchable'
     *
     * @return String
     */
    public boolean getIsSearchable() {
        return isSearchable.equalsIgnoreCase("T") ? true : false;
    }

    /**
     * Method 'setIsSearchable'
     *
     * @param isSearchable
     */
    public void setIsSearchable(boolean isSearchable) {
        this.isSearchable = isSearchable ? "T" : "F";
    }

    /**
     * Method 'getIsRequired'
     *
     * @return String
     */
    public boolean getIsRequired() {
        return isRequired.equalsIgnoreCase("T") ? true : false;
    }

    /**
     * Method 'setIsRequired'
     *
     * @param isRequired
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired ? "T" : "F";
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

        if (!(_other instanceof HierarchyNodeEaDto)) {
            return false;
        }

        final HierarchyNodeEaDto _cast = (HierarchyNodeEaDto) _other;
        if (_cast.eaId != eaId) {
            return false;
        }

        if (_cast.hierarchyDefId != hierarchyDefId) {
            return false;
        }

        if (attributeName == null ? _cast.attributeName != attributeName : !attributeName.equals(_cast.attributeName)) {
            return false;
        }

        if (columnName == null ? _cast.columnName != columnName : !columnName.equals(_cast.columnName)) {
            return false;
        }

        if (columnType == null ? _cast.columnType != columnType : !columnType.equals(_cast.columnType)) {
            return false;
        }

        if (defaultValue == null ? _cast.defaultValue != defaultValue : !defaultValue.equals(_cast.defaultValue)) {
            return false;
        }

        if (isSearchable == null ? _cast.isSearchable != isSearchable : !isSearchable.equals(_cast.isSearchable)) {
            return false;
        }

        if (isRequired == null ? _cast.isRequired != isRequired : !isRequired.equals(_cast.isRequired)) {
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

        _hashCode = 29 * _hashCode + (int) eaId;



        _hashCode = 29 * _hashCode + (int) hierarchyDefId;


        if (attributeName != null) {
            _hashCode = 29 * _hashCode + attributeName.hashCode();
        }

        if (columnName != null) {
            _hashCode = 29 * _hashCode + columnName.hashCode();
        }

        if (columnType != null) {
            _hashCode = 29 * _hashCode + columnType.hashCode();
        }

        if (defaultValue != null) {
            _hashCode = 29 * _hashCode + defaultValue.hashCode();
        }

        if (isSearchable != null) {
            _hashCode = 29 * _hashCode + isSearchable.hashCode();
        }

        if (isRequired != null) {
            _hashCode = 29 * _hashCode + isRequired.hashCode();
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
        ret.append("com.mycompany.myapp.dto.HierarchyNodeEa: ");
        ret.append("eaId=" + eaId);
        ret.append(", hierarchyDefId=" + hierarchyDefId);
        ret.append(", attributeName=" + attributeName);
        ret.append(", columnName=" + columnName);
        ret.append(", columnType=" + columnType);
        ret.append(", defaultValue=" + defaultValue);
        ret.append(", isSearchable=" + isSearchable);
        ret.append(", isRequired=" + isRequired);
        return ret.toString();
    }
}
