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
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDaoException;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipEavDaoException;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEavDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEaDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEavDto;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEavDaoImpl;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidp
 */
public class RelationshipService implements Serializable {

    private DatabaseType mDbType = null;
    private RelationshipDao mRelDef = null;
    private RelationshipEavDao mRelEav = null;

    /**
     * Method 'RelationshipService'
     *
     */
    public RelationshipService() {
        getDaoInstances();

    }

    public RelationshipService(DatabaseType dbtype) {
        this.mDbType = dbtype;
        getDaoInstances();

    }

    /**
     * Method 'getRelationship'
     *
     */
    private void getDaoInstances() {
        if (mRelDef == null) {
            mRelDef = (RelationshipDaoImpl) AbstractDaoFactory.getDoaFactory(mDbType).getRelationshipDao();
        }
        if (mRelEav == null) {
            mRelEav = AbstractDaoFactory.getDoaFactory(mDbType).getRelationshipEavDao();
        }
    }

    /**
     * Method 'create'
     *
     */
    public void create(Relationship rel) throws RelationshipDaoException, RelationshipEavDaoException {

        /* Relationship object */
        RelationshipDto relDto = new RelationshipDto();

        copyToRelDto(rel, relDto);

        long relID = 0;
        RelationshipDaoImpl relDao = new RelationshipDaoImpl();
        try {
            relID = relDao.insert(relDto);
        } catch (RelationshipDaoException ex) {
            Logger.getLogger(RelationshipService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        /* Relationship Extend Attributes Values */
        RelationshipEavDto attDto = new RelationshipEavDto();
        attDto.setRelationshipId(relID);
        attDto.setAttributes(rel.getAttributes());
        RelationshipEavDaoImpl relEavDao = new RelationshipEavDaoImpl();
        relEavDao.insert(attDto);
    }

    /**
     * Method 'delete'
     *
     */
    public void delete(Relationship rel) {
    }

    /**
     * Method 'update'
     *
     */
    public void update(Relationship rel) throws RelationshipDaoException {
        /* Relationship object */
        RelationshipDto dto = new RelationshipDto();
        copyToRelDto(rel, dto);
        RelationshipDaoImpl dao = new RelationshipDaoImpl();
    //dao.update(dto);
    }

    /**
     * Method 'search'
     *
     * @return RelationshipDto
     */
    public List<Relationship> search(String sourceEUID, String targetEUIG) throws RelationshipDaoException {
        RelationshipDaoImpl dao = new RelationshipDaoImpl();
        ArrayList<Relationship> relList = new ArrayList<Relationship>();
        for (RelationshipDto relDto : dao.search(sourceEUID, targetEUIG)) {
            Relationship rel = new Relationship();
            copyFromRelDto(rel, relDto);
            relList.add(rel);
        }
        return relList;
    }

    /**
     * Method 'copyTorelDto'
     *
     */
    private void copyToRelDto(Relationship rel, RelationshipDto dto) {

        dto.setRelationshipDefId(rel.getRelationshipDef().getReldefID());
        dto.setRelationshipId(rel.getRelationshipId());
        dto.setSourceEuid(rel.getSourceEUID());
        dto.setTargetEuid(rel.getTargetEUID());
        dto.setEffectiveFromDate(rel.getEffectiveFromDate());
        dto.setEffectiveToDate(rel.getEffectiveToDate());
        dto.setPurgeDate(rel.getPurgeDate());
    }

    /**
     * Method 'copyFromrelDto'
     *
     */
    private void copyFromRelDto(Relationship rel, RelationshipDto relDto) {
        rel.setRelationshipId(relDto.getRelationshipId());
        rel.setSourceEUID(relDto.getSourceEuid());
        rel.setTargetEUID(relDto.getTargetEuid());
        rel.setEffectiveFromDate(relDto.getEffectiveFromDate());
        rel.setEffectiveToDate(relDto.getEffectiveToDate());
        rel.setPurgeDate(relDto.getPurgeDate());
        Iterator iter = relDto.getRelationshipAttributes().keySet().iterator();
        while (iter.hasNext()) {
            RelationshipEaDto eaDto = (RelationshipEaDto) iter.next();
            String attrValue = relDto.getRelationshipAttributes().get(eaDto);
            Attribute attr = new Attribute();
            attr.setId(eaDto.getEaId());
            attr.setName(eaDto.getAttributeName());
            attr.setColumnName(eaDto.getColumnName());
            attr.setType(AttributeType.valueOf(eaDto.getColumnType()));
            attr.setDefaultValue(eaDto.getDefaultValue());
            attr.setIsIncluded(eaDto.getIsIncluded());
            attr.setIsRequired(eaDto.getIsRequired());
            attr.setIsSearchable(eaDto.getIsSearchable());
            rel.getAttributes().put(attr, attrValue);
        }
    }
}
