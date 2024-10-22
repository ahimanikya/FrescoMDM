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
package com.sun.mdm.multidomain.hierarchy.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyDefDaoException;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyEaDaoException;
import com.sun.mdm.multidomain.hierarchy.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.hierarchy.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyDefDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyDefDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyDefDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;

/**
 *
 * @author David Peh
 */
public class HierarchyDefService implements Serializable {

    private DatabaseType mDbType = null;
    private HierarchyDefDao mHierDef = null;
    private Connection mConn = null;

    /**
     * Method 'HierarchyDefService'
     *
     */
    public HierarchyDefService() {
        getDaoInstances();

    }

    public HierarchyDefService(DatabaseType dbtype) {
        this.mDbType = dbtype;
        getDaoInstances();

    }

    public HierarchyDefService(Connection conn) {
        this.mConn = conn;
        getDaoInstances();
    }

    /**
     * Method 'getDaoInstances'
     *
     */
    private void getDaoInstances() {
        if (mHierDef == null) {
            mHierDef = (HierarchyDefDaoImpl) AbstractDaoFactory.getDoaFactory(mDbType).getHierarchyDefDao();
        }
    }

    /**
     * Method 'create'
     *
     */
    public long create(HierarchyDef hierDef) throws HierarchyDefDaoException, HierarchyEaDaoException {

        long hierDefID = 0;
        HierarchyDefDaoImpl hierDefDao = new HierarchyDefDaoImpl(mConn);
        try {
            hierDefID = hierDefDao.insert(hierDef);
        } catch (HierarchyDefDaoException ex) {
            Logger.getLogger(HierarchyDefService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return hierDefID;
    }

    /**
     * Method 'delete'
     *
     */
    public void delete(HierarchyDef hier) throws HierarchyDefDaoException {
        HierarchyDefDaoImpl dao = new HierarchyDefDaoImpl(mConn);
        dao.delete(hier.getId());
    }

    /**
     * Method 'update'
     *
     */
    public void update(HierarchyDef hierDef) throws HierarchyDefDaoException {
        /* HierarchyDef object */
        HierarchyDefDaoImpl dao = new HierarchyDefDaoImpl(mConn);
        dao.update(hierDef);
    }

    /**
     * Method 'search'
     *
     * @return HierarchyDef
     */
    public List<HierarchyDef> search(String domain) throws HierarchyDefDaoException {
        HierarchyDefDaoImpl dao = new HierarchyDefDaoImpl(mConn);
        ArrayList hierDefList = new ArrayList();
        for (HierarchyDefDto hierDto : dao.search(domain)) {
            HierarchyDef hierDef = new HierarchyDef();
            copyFromHierDto(hierDef, hierDto);
            hierDefList.add(hierDef);
        }
        return hierDefList;
    }

    public HierarchyDef searchById(long hierarchyId) throws HierarchyDefDaoException {
        HierarchyDefDaoImpl dao = new HierarchyDefDaoImpl(mConn);
        HierarchyDef hierarchyDef = new HierarchyDef();

        HierarchyDefDto hierarchyDefDto = dao.searchById(hierarchyId);
        copyFromHierDto(hierarchyDef, hierarchyDefDto);

        return hierarchyDef;
    }

    /**
     * Method 'copyFromHierDto'
     *
     */
    private void copyFromHierDto(HierarchyDef hier, HierarchyDefDto dto) {
        hier.setId(dto.getHierarchyDefId());
        hier.setName(dto.getHierarchyName());
        hier.setDescription(dto.getDescription());
        hier.setDomain(dto.getDomain());
        hier.setEffectiveFromDate(dto.getEffectiveFromDate());
        hier.setEffectiveToDate(dto.getEffectiveToDate());
        hier.setEffectiveFromRequired(dto.getEffectiveFromReq().equalsIgnoreCase("T") ? true : false);
        hier.setEffectiveToRequired(dto.getEffectiveToReq().equalsIgnoreCase("T") ? true : false);
        hier.setEffectiveFromIncluded(dto.getEffectiveFromInc().equalsIgnoreCase("T") ? true : false);
        hier.setEffectiveToIncluded(dto.getEffectiveToInc().equalsIgnoreCase("T") ? true : false);
        hier.setPlugin(dto.getPlugIn());
        List<HierarchyNodeEaDto> eaDto = dto.getAttributeDefs();
        for (HierarchyNodeEaDto ea : eaDto) {
            Attribute att = new Attribute();
            att.setId(ea.getHierarchyDefId());
            att.setName(ea.getAttributeName());
            att.setColumnName(ea.getColumnName());
            att.setType(AttributeType.valueOf(ea.getColumnType().toUpperCase()));
            att.setDefaultValue(ea.getDefaultValue());
            att.setIsSearchable(ea.getIsSearchable());
            att.setIsRequired(ea.getIsRequired());
            hier.getAttributes().add(att);
        }
    }
}
