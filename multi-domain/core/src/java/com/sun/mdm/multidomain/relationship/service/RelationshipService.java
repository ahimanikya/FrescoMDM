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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDaoException;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipEavDaoException;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipEavDao;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDao;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipDto;
import com.sun.mdm.multidomain.relationship.ops.dto.RelationshipEavDto;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEavDaoImpl;

/**
 *
 * @author davidp
 */
public class RelationshipService implements Serializable {

    private DatabaseType mDbType = null;
    private RelationshipDao mRelDef = null;
    private RelationshipEavDao mRelEav = null;
    private Connection mConn = null;

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

    public RelationshipService(Connection conn) {
        this.mConn = conn;
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
    public long create(Relationship rel) throws RelationshipDaoException, RelationshipEavDaoException {

        long relID = 0;
        RelationshipDaoImpl relDao = new RelationshipDaoImpl(mConn);
        try {
            relID = relDao.insert(rel);
        } catch (RelationshipDaoException ex) {
            Logger.getLogger(RelationshipService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        /* Relationship Extend Attributes Values */
        RelationshipEavDto attDto = new RelationshipEavDto();
        attDto.setRelationshipId(relID);
        attDto.setAttributes(rel.getAttributes());
        RelationshipEavDaoImpl relEavDao = new RelationshipEavDaoImpl(mConn);
        relEavDao.insert(attDto);
        return relID;
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
    public List<Relationship> search(String sourceEUID, String targetEUID) throws RelationshipDaoException {
        RelationshipDaoImpl dao = new RelationshipDaoImpl(mConn);
        return dao.search(sourceEUID, targetEUID);
    }

    /**
     * Method 'copyTorelDto'
     *
     */
    private void copyToRelDto(Relationship rel, RelationshipDto dto) {

        dto.setRelationshipDefId(rel.getRelationshipDef().getId());
        dto.setRelationshipId(rel.getRelationshipId());
        dto.setSourceEuid(rel.getSourceEUID());
        dto.setTargetEuid(rel.getTargetEUID());
        dto.setEffectiveFromDate(rel.getEffectiveFromDate());
        dto.setEffectiveToDate(rel.getEffectiveToDate());
        dto.setPurgeDate(rel.getPurgeDate());
    }
}
