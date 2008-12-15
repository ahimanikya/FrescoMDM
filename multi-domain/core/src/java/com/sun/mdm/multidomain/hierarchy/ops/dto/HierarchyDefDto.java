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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author David Peh
 */
public class HierarchyDefDto implements Serializable {

    /**
     * This attribute maps to the column HIERARCHY_DEF_ID in the hierarchy_def table.
     */
    protected long hierarchyDefId;
    /**
     * This attribute maps to the column HIERARCHY_NAME in the hierarchy_def table.
     */
    protected String hierarchyName;
    /**
     * This attribute maps to the column DOMAIN in the hierarchy_def table.
     */
    protected String domain;
    /**
     * This attribute maps to the column DESCRIPTION in the hierarchy_def table.
     */
    protected String description;
    /**
     * This attribute maps to the column effective_from_date in the hierarchy_def table.
     */
    protected Date effectiveFromDate;
    /**
     * This attribute maps to the column effective_to_date in the hierarchy_def table.
     */
    protected Date effectiveToDate;
    /**
     * This attribute maps to the column effective_from_incl in the hierarchy_def table.
     */
    protected String effectiveFromReq;
    /**
     * This attribute maps to the column effective_to_incl in the hierarchy_def table.
     */
    protected String effectiveToReq;
    /**
     * This attribute maps to the column effective_from_incl in the hierarchy_def table.
     */
    protected String effectiveFromInc;
    /**
     * This attribute maps to the column effective_to_incl in the hierarchy_def table.
     */
    protected String effectiveToInc;
    /**
     * This attribute maps to the column PLUGIN in the hierarchy_def table.
     */
    protected String plugIn;
    private List<HierarchyNodeEaDto> attributes = new ArrayList<HierarchyNodeEaDto>();

    /**
     * Method 'HierarchyDef'
     *
     */
    public HierarchyDefDto() {
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
     * Method 'getHierarchyName'
     *
     * @return String
     */
    public String getHierarchyName() {
        return hierarchyName;
    }

    /**
     * Method 'setHierarchyName'
     *
     * @param hierarchyName
     */
    public void setHierarchyName(String hierarchyName) {
        this.hierarchyName = hierarchyName;
    }

    /**
     * Method 'getDomain'
     *
     * @return String
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Method 'setDomain'
     *
     * @param domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Method 'getDescription'
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method 'setDescription'
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Method 'setEffectiveFromReq'
     *
     * @param effectiveFromReq
     */
    public void setEffectiveFromReq(String effectiveFromReq) {
        this.effectiveFromReq = effectiveFromReq;
    }

    /**
     * Method 'getEffectiveFromReq'
     *
     * @return String
     */
    public String getEffectiveFromReq() {
        return effectiveFromReq;
    }

    /**
     * Method 'setEffectiveToReq'
     *
     * @param effectiveToReq
     */
    public void setEffectiveToReq(String effectiveToReq) {
        this.effectiveToReq = effectiveToReq;
    }

    /**
     * Method 'getEffectiveToReq'
     *
     * @return String
     */
    public String getEffectiveToReq() {
        return effectiveToReq;
    }

    /**
     * Method 'setEffectiveToInc'
     *
     * @param effectiveFromInc
     */
    public void setEffectiveFromInc(String effectiveFromInc) {
        this.effectiveFromInc = effectiveFromInc;
    }

    /**
     * Method 'getEffectiveFromInc'
     *
     * @return String
     */
    public String getEffectiveFromInc() {
        return effectiveFromInc;
    }

    /**
     * Method 'setDffectiveToInc'
     *
     * @param effectiveToInc
     */
    public void setEffectiveToInc(String effectiveToInc) {
        this.effectiveToInc = effectiveToInc;
    }

    /**
     * Method 'getEffectiveToInc'
     *
     * @return String
     */
    public String getEffectiveToInc() {
        return effectiveToInc;
    }

    /**
     * Method 'getPlugIn'
     *
     * @return String
     */
    public String getPlugIn() {
        return plugIn;
    }

    /**
     * Method 'setPlugIn'
     *
     * @param plugIn
     */
    public void setPlugIn(String plugIn) {
        this.plugIn = plugIn;
    }

    /**
     * Method 'setAttributeDefs'
     *
     * @param attr
     */
    public void setAttributeDefs(List<HierarchyNodeEaDto> attr) {
        this.attributes = attr;
    }

    /**
     * Method 'getAttributeDefs'
     *
     * @return attributes
     */
    public List<HierarchyNodeEaDto> getAttributeDefs() {
        if (attributes == null) {
            attributes = new ArrayList<HierarchyNodeEaDto>();
        }
        return attributes;
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

        if (!(_other instanceof HierarchyDefDto)) {
            return false;
        }

        final HierarchyDefDto _cast = (HierarchyDefDto) _other;
        if (_cast.hierarchyDefId != hierarchyDefId) {
            return false;
        }

        if (_cast.hierarchyName != hierarchyName) {
            return false;
        }

        if (domain == null ? _cast.domain != domain : !domain.equals(_cast.domain)) {
            return false;
        }

        if (description == null ? _cast.description != description : !description.equals(_cast.description)) {
            return false;
        }

        if (effectiveFromDate == null ? _cast.effectiveFromDate != effectiveFromDate : !effectiveFromDate.equals(_cast.effectiveFromDate)) {
            return false;
        }

        if (effectiveToDate == null ? _cast.effectiveToDate != effectiveToDate : !effectiveToDate.equals(_cast.effectiveToDate)) {
            return false;
        }
        if (plugIn == null ? _cast.plugIn != plugIn : !plugIn.equals(_cast.plugIn)) {
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

        _hashCode = 29 * _hashCode + (int) hierarchyDefId;

        if (hierarchyName != null) {
            _hashCode = 29 * _hashCode + hierarchyName.hashCode();
        }

        if (domain != null) {
            _hashCode = 29 * _hashCode + domain.hashCode();
        }

        if (description != null) {
            _hashCode = 29 * _hashCode + description.hashCode();
        }

        if (effectiveFromDate != null) {
            _hashCode = 29 * _hashCode + effectiveFromDate.hashCode();
        }

        if (effectiveToDate != null) {
            _hashCode = 29 * _hashCode + effectiveToDate.hashCode();
        }

        if (plugIn != null) {
            _hashCode = 29 * _hashCode + plugIn.hashCode();
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
        ret.append("com.mycompany.myapp.dto.HierarchyDef: ");
        ret.append("hierarchyDefId=" + hierarchyDefId);
        ret.append(", hierarchyName=" + hierarchyName);
        ret.append(", domain=" + domain);
        ret.append(", description=" + description);
        ret.append(", effectiveFromDate=" + effectiveFromDate);
        ret.append(", effectiveToDate=" + effectiveToDate);
        ret.append(", plugIn=" + plugIn);
        return ret.toString();
    }
}
