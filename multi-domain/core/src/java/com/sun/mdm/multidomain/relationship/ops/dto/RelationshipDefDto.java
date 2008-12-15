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
package com.sun.mdm.multidomain.relationship.ops.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Peh
 */
public class RelationshipDefDto implements Serializable {

    /**
     * This attribute maps to the column RELATIONSHIP_DEF_ID in the relationship_def table.
     */
    protected long relationshipDefId;
    /**
     * This attribute maps to the column RELATIONSHIP_NAME in the relationship_def table.
     */
    protected String relationshipName;
    /**
     * This attribute maps to the column DESCRIPTION in the relationship_def table.
     */
    protected String description;
    /**
     * This attribute maps to the column SOURCE_DOMAIN in the relationship_def table.
     */
    protected String sourceDomain;
    /**
     * This attribute maps to the column TARGET_DOMAIN in the relationship_def table.
     */
    protected String targetDomain;
    /**
     * This attribute maps to the column BIDIRECTIONAL in the relationship_def table.
     */
    protected String bidirectional;
    /**
     * This attribute maps to the column EFFECTIVE_FROM_REQ in the relationship_def table.
     */
    protected String effectiveFromReq;
    /**
     * This attribute maps to the column EFFECTIVE_TO_REQ in the relationship_def table.
     */
    protected String effectiveToReq;
    /**
     * This attribute maps to the column PURGE_DATE_REQ in the relationship_def table.
     */
    protected String purgeDateReq;
    /**
     * This attribute maps to the column EFFECTIVE_FROM_REQ in the relationship_def table.
     */
    protected String effectiveFromInc;
    /**
     * This attribute maps to the column EFFECTIVE_TO_REQ in the relationship_def table.
     */
    protected String effectiveToInc;
    /**
     * This attribute maps to the column PURGE_DATE_REQ in the relationship_def table.
     */
    protected String purgeDateInc;
    /**
     * This attribute maps to the column EFFECTIVE_FROM in the relationship_def table.
     */
    protected String effectiveFrom;
    /**
     * This attribute maps to the column EFFECTIVE_TO in the relationship_def table.
     */
    protected String effectiveTo;
    /**
     * This attribute maps to the column PURGE_DATE in the relationship_def table.
     */
    protected String purgeDate;    
    /**
     * This attribute maps to the column PlugIn in the relationship_def table.
     */
    protected String plugIn;
    private List<RelationshipEaDto> attributes = new ArrayList<RelationshipEaDto>();

    /**
     * Method 'RelationshipDefDto'
     *
     */
    public RelationshipDefDto() {
    }

    /**
     * Method 'getRelationshipDefId'
     *
     * @return int
     */
    public long getRelationshipDefId() {
        return relationshipDefId;
    }

    /**
     * Method 'setRelationshipDefId'
     *
     * @param relationshipDefId
     */
    public void setRelationshipDefId(long relationshipDefId) {
        this.relationshipDefId = relationshipDefId;
    }

    /**
     * Method 'getRelationshipName'
     *
     * @return String
     */
    public String getRelationshipName() {
        return relationshipName;
    }

    /**
     * Method 'setRelationshipName'
     *
     * @param relationshipName
     */
    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
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
     * Method 'getSourceDomain'
     *
     * @return String
     */
    public String getSourceDomain() {
        return sourceDomain;
    }

    /**
     * Method 'setSourceDomain'
     *
     * @param sourceDomain
     */
    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }

    /**
     * Method 'getTargetDomain'
     *
     * @return String
     */
    public String getTargetDomain() {
        return targetDomain;
    }

    /**
     * Method 'setTargetDomain'
     *
     * @param targetDomain
     */
    public void setTargetDomain(String targetDomain) {
        this.targetDomain = targetDomain;
    }

    /**
     * Method 'getBidirectional'
     *
     * @return String
     */
    public String getBidirectional() {
        return bidirectional;
    }

    /**
     * Method 'setBidirectional'
     *
     * @param bidirectional
     */
    public void setBidirectional(String bidirectional) {
        this.bidirectional = bidirectional;
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
     * Method 'setEffectiveFromReq'
     *
     * @param effectiveFromReq
     */
    public void setEffectiveFromReq(String effectiveFromReq) {
        this.effectiveFromReq = effectiveFromReq;
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
     * Method 'setEffectiveToReq'
     *
     * @param effectiveToReq
     */
    public void setEffectiveToReq(String effectiveToReq) {
        this.effectiveToReq = effectiveToReq;
    }

    /**
     * Method 'getPurgeDateReq'
     *
     * @return String
     */
    public String getPurgeDateReq() {
        return purgeDateReq;
    }

    /**
     * Method 'setPurgeDateReq'
     *
     * @param purgeDateReq
     */
    public void setPurgeDateReq(String purgeDateReq) {
        this.purgeDateReq = purgeDateReq;
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
     * Method 'setEffectiveFromInc'
     *
     * @param effectiveFromReq
     */
    public void setEffectiveFromInc(String effectiveFromInc) {
        this.effectiveFromInc = effectiveFromInc;
    }

    /**
     * Method 'getEffectiveFromInc'
     *
     * @return String
     */
    public String getEffectiveToInc() {
        return effectiveToInc;
    }

    /**
     * Method 'setEffectiveToInc'
     *
     * @param effectiveToInc
     */
    public void setEffectiveToInc(String effectiveToInc) {
        this.effectiveToInc = effectiveToInc;
    }

    /**
     * Method 'getPurgeDateInc'
     *
     * @return String
     */
    public String getPurgeDateInc() {
        return purgeDateInc;
    }

    /**
     * Method 'setPurgeDateInc'
     *
     * @param purgeDateInc
     */
    public void setPurgeDateInc(String purgeDateInc) {
        this.purgeDateInc = purgeDateInc;
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
    public void setAttributeDefs(List<RelationshipEaDto> attr) {
        this.attributes = attr;
    }

    /**
     * Method 'getAttributeDefs'
     *
     * @return attributes
     */
    public List<RelationshipEaDto> getAttributeDefs() {
        if (attributes == null) {
            attributes = new ArrayList<RelationshipEaDto>();
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

        if (!(_other instanceof RelationshipDefDto)) {
            return false;
        }

        final RelationshipDefDto _cast = (RelationshipDefDto) _other;
        if (relationshipDefId != _cast.relationshipDefId) {
            return false;
        }

        if (relationshipName == null ? _cast.relationshipName != relationshipName : !relationshipName.equals(_cast.relationshipName)) {
            return false;
        }

        if (description == null ? _cast.description != description : !description.equals(_cast.description)) {
            return false;
        }

        if (sourceDomain == null ? _cast.sourceDomain != sourceDomain : !sourceDomain.equals(_cast.sourceDomain)) {
            return false;
        }

        if (targetDomain == null ? _cast.targetDomain != targetDomain : !targetDomain.equals(_cast.targetDomain)) {
            return false;
        }

        if (bidirectional == null ? _cast.bidirectional != bidirectional : !bidirectional.equals(_cast.bidirectional)) {
            return false;
        }

        if (effectiveFromReq == null ? _cast.effectiveFromReq != effectiveFromReq : !effectiveFromReq.equals(_cast.effectiveFromReq)) {
            return false;
        }

        if (effectiveToReq == null ? _cast.effectiveToReq != effectiveToReq : !effectiveToReq.equals(_cast.effectiveToReq)) {
            return false;
        }

        if (purgeDateReq == null ? _cast.purgeDateReq != purgeDateReq : !purgeDateReq.equals(_cast.purgeDateReq)) {
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
        _hashCode = 29 * _hashCode + (int) relationshipDefId;
        if (relationshipName != null) {
            _hashCode = 29 * _hashCode + relationshipName.hashCode();
        }

        if (description != null) {
            _hashCode = 29 * _hashCode + description.hashCode();
        }

        if (sourceDomain != null) {
            _hashCode = 29 * _hashCode + sourceDomain.hashCode();
        }

        if (targetDomain != null) {
            _hashCode = 29 * _hashCode + targetDomain.hashCode();
        }

        if (bidirectional != null) {
            _hashCode = 29 * _hashCode + bidirectional.hashCode();
        }

        if (effectiveFromReq != null) {
            _hashCode = 29 * _hashCode + effectiveFromReq.hashCode();
        }

        if (effectiveToReq != null) {
            _hashCode = 29 * _hashCode + effectiveToReq.hashCode();
        }

        if (purgeDateReq != null) {
            _hashCode = 29 * _hashCode + purgeDateReq.hashCode();
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
        ret.append("com.sun.mdm.multidomain.ops.dto.RelationshipDef: ");
        ret.append("relationshipDefId=" + relationshipDefId);
        ret.append(", relationshipName=" + relationshipName);
        ret.append(", description=" + description);
        ret.append(", sourceDomain=" + sourceDomain);
        ret.append(", targetDomain=" + targetDomain);
        ret.append(", bidirectional=" + bidirectional);
        ret.append(", effectiveFromReq=" + effectiveFromReq);
        ret.append(", effectiveToReq=" + effectiveToReq);
        ret.append(", purgeDateReq=" + purgeDateReq);
        ret.append(", plugIn=" + plugIn);
        return ret.toString();
    }
}
