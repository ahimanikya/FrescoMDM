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
import java.sql.Connection;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipDefDaoException;
import com.sun.mdm.multidomain.relationship.ops.exceptions.RelationshipEaDaoException;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.relationship.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipDefDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.impl.RelationshipEaDaoImpl;
import com.sun.mdm.multidomain.relationship.ops.dao.RelationshipDefDao;
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
    private Connection mConn = null;

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

    public RelationshipDefService(Connection conn) {
        this.mConn = conn;
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
    public long create(RelationshipDef relDef) throws RelationshipDefDaoException, RelationshipEaDaoException {
        long relDefID = 0;
        RelationshipDefDaoImpl relDefDao = new RelationshipDefDaoImpl(mConn);
        try {
            relDefID = relDefDao.insert(relDef);
        } catch (RelationshipDefDaoException ex) {
            Logger.getLogger(RelationshipDefService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        /* RelationshipDef Extend Attributes */
        RelationshipEaDto attDto = new RelationshipEaDto();
        ArrayList<Attribute> attrList = (ArrayList<Attribute>) relDef.getAttributes();
        RelationshipEaDaoImpl relEaDao = new RelationshipEaDaoImpl(mConn);
        for (Attribute att : attrList) {
            attDto.setRelationshipDefId(relDefID);
            attDto.setAttributeName(att.getName());
            attDto.setColumnName(att.getColumnName());
            attDto.setColumnType(att.getType().name());
            attDto.setDefaultValue(att.getDefaultValue());
            attDto.setIsRequired(att.getIsRequired());
            attDto.setIsSearchable(att.getIsSearchable());
            relEaDao.insert(attDto);
        }
        return relDefID;
    }

    /**
     * Method 'delete'
     *
     */
    public void delete(RelationshipDef relDef) throws RelationshipDefDaoException {
        new RelationshipDefDaoImpl(mConn).delete(relDef.getId());
    }

    /**
     * Method 'update'
     *
     */
    public int update(RelationshipDef rel) throws RelationshipDefDaoException {
        RelationshipDefDaoImpl dao = new RelationshipDefDaoImpl(mConn);
        return dao.update(rel);
    }

    /**
     * Method 'search'
     *
     * @return RelationshipDef
     */
    public List<RelationshipDef> search(String sourceDomain, String targetDomain) throws RelationshipDefDaoException {
        RelationshipDefDaoImpl dao = new RelationshipDefDaoImpl(mConn);
        return dao.search(sourceDomain, targetDomain);
    }

    /**
     * Method 'search'
     *
     * @return RelationshipDef[]
     */
    public RelationshipDef[] getRelationshipDefs() throws RelationshipDefDaoException {
        return new RelationshipDefDaoImpl(mConn).getRelationshipDefs();
    }
}
