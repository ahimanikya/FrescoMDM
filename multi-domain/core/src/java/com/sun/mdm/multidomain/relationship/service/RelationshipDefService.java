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
package com.sun.mdm.multidomain.relationship.service;

import java.io.Serializable;
import java.util.ArrayList;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDefDaoException;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipEaDaoException;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipDef.DirectionMode;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEavDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDefDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEaDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDefDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDefDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidp
 */
public class RelationshipDefService implements Serializable {

    private DatabaseType mDbType = null;
    private RelationshipDefDao mRelDef = null;

    /**
     * Method 'RelationshipDefService'
     *
     */
    public RelationshipDefService() {
        getDaoInstances();

    }

    public RelationshipDefService(DatabaseType dbtype) {
        this.mDbType = dbtype;
        getDaoInstances();

    }

    /**
     * Method 'getRelationshipDef'
     *
     */
    private void getDaoInstances() {
        if (mRelDef == null) {
            mRelDef = (RelationshipDefDaoImpl) AbstractDaoFactory.getDoaFactory(mDbType).getRelationshipDefDao();
        }
    }

    /**
     * Method 'create'
     *
     */
    public void create(RelationshipDef rel) throws RelationshipDefDaoException, RelationshipEaDaoException {

        /* RelationshipDef object */
        RelationshipDefDto relDto = new RelationshipDefDto();

        copyToRelDto(rel, relDto);

        long relDefID = 0;
        RelationshipDefDaoImpl relDefDao = new RelationshipDefDaoImpl();
        try {
            relDefID = relDefDao.insert(relDto);
        } catch (RelationshipDefDaoException ex) {
            Logger.getLogger(RelationshipDefService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        /* RelationshipDef Extend Attributes */
        RelationshipEaDto attDto = new RelationshipEaDto();
        ArrayList<Attribute> attrList = (ArrayList<Attribute>) rel.getAttributes();
        RelationshipEaDaoImpl relEaDao = new RelationshipEaDaoImpl();
        for (Attribute att : attrList) {
            attDto.setRelationshipDefId(relDefID);
            attDto.setAttributeName(att.getName());
            attDto.setColumnName(att.getColumnName());
            attDto.setColumnType(att.getType().name());
            attDto.setDefaultValue(att.getDefaultValue());
            attDto.setIsIncluded(att.getIsIncluded());
            attDto.setIsRequired(att.getIsRequired());
            attDto.setIsSearchable(att.getIsSearchable());
            relEaDao.insert(attDto);
        }
    }

    /**
     * Method 'delete'
     *
     */
    public void delete(RelationshipDef rel) {
    }

    /**
     * Method 'update'
     *
     */
    public void update(RelationshipDef rel) throws RelationshipDefDaoException {
        /* RelationshipDef object */
        RelationshipDefDto dto = new RelationshipDefDto();
        copyToRelDto(rel, dto);
        RelationshipDefDaoImpl dao = new RelationshipDefDaoImpl();
        dao.update(dto);
    }

    /**
     * Method 'search'
     *
     * @return RelationshipDefDto
     */
    public List<RelationshipDef> search(String sourceDomain, String targetDomain) throws RelationshipDefDaoException {
        RelationshipDefDaoImpl dao = new RelationshipDefDaoImpl();
        ArrayList relDefList = new ArrayList();
        for (RelationshipDefDto relDto : dao.search(sourceDomain, targetDomain)) {
            RelationshipDef relDef = new RelationshipDef();
            copyFromRelDto(relDef, relDto);
            relDefList.add(relDef);
        }
        return relDefList;
    }

    /**
     * Method 'copyTorelDto'
     *
     */
    private void copyToRelDto(RelationshipDef rel, RelationshipDefDto dto) {
        dto.setRelationshipDefId(rel.getReldefID());
        dto.setRelationshipName(rel.getName());
        dto.setDescription(rel.getDescription());
        dto.setSourceDomain(rel.getSourceDomain());
        dto.setTargetDomain(rel.getTargetDomain());
        if (rel.getDirection() != null) {
            dto.setBidirectional(rel.getDirection().IsBidirectional() ? "Y" : "N");
        }
        dto.setEffectiveFromReq(rel.getEffectiveFromRequired() ? "Y" : "N");
        dto.setEffectiveToReq(rel.getEffectiveToRequired() ? "Y" : "N");
        dto.setPurgeDateReq(rel.getPurgeDateRequired() ? "Y" : "N");
        dto.setPlugIn(rel.getPlugin());
    }

    /**
     * Method 'copyFromrelDto'
     *
     */
    private void copyFromRelDto(RelationshipDef rel, RelationshipDefDto dto) {
        rel.setReldefID(dto.getRelationshipDefId());
        rel.setName(dto.getRelationshipName());
        rel.setDescription(dto.getDescription());
        rel.setSourceDomain(dto.getSourceDomain());
        rel.setTargetDomain(dto.getTargetDomain());
        rel.setDirection(dto.getBidirectional().equalsIgnoreCase("Y") ? DirectionMode.BIDIRECTIONAL : DirectionMode.UNIDIRECTIONAL);
        rel.setEffectiveFromRequired(dto.getEffectiveFromReq().equalsIgnoreCase("Y") ? true : false);
        rel.setEffectiveToRequired(dto.getEffectiveToReq().equalsIgnoreCase("Y") ? true : false);
        rel.setPurgeDateRequired(dto.getPurgeDateReq().equalsIgnoreCase("Y") ? true : false);
        rel.setPlugin(dto.getPlugIn());
        List<RelationshipEaDto> eaDto = dto.getAttributeDefs();
        for (RelationshipEaDto ea : eaDto) {
            Attribute att = new Attribute();
            att.setId(ea.getRelationshipDefId());
            att.setName(ea.getAttributeName());
            att.setColumnName(ea.getColumnName());
            att.setType(AttributeType.valueOf(ea.getColumnType()));
            att.setDefaultValue(ea.getDefaultValue());
            att.setIsSearchable(ea.getIsSearchable());
            att.setIsRequired(ea.getIsRequired());
            att.setIsIncluded(ea.getIsIncluded());
            rel.getAttributes().add(att);
        }
    }
}
