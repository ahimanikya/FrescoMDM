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
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyDaoException;
import com.sun.mdm.multidomain.hierarchy.ops.exceptions.HierarchyEavDaoException;
import com.sun.mdm.multidomain.hierarchy.ops.factory.AbstractDaoFactory;
import com.sun.mdm.multidomain.hierarchy.ops.factory.AbstractDaoFactory.DatabaseType;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEavDto;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeEavDaoImpl;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author davidp
 */
public class HierarchyNodeService implements Serializable {

    private DatabaseType mDbType = null;
    private HierarchyNodeDao mHierDef = null;

    /**
     * Method 'HierarchyService'
     *
     */
    public HierarchyNodeService() {
        getDaoInstances();

    }

    public HierarchyNodeService(DatabaseType dbtype) {
        this.mDbType = dbtype;
        getDaoInstances();

    }

    /**
     * Method 'getHierarchy'
     *
     */
    private void getDaoInstances() {
        if (mHierDef == null) {
            mHierDef = (HierarchyNodeDaoImpl) AbstractDaoFactory.getDoaFactory(mDbType).getHierarchyNodeDao();
        }

    }

    /**
     * Method 'create'
     *
     */
    public void addHierarchyNode(HierarchyNode hier) throws HierarchyDaoException, HierarchyEavDaoException {

        /* HierarchyNode object */
        HierarchyNodeDto hierDto = new HierarchyNodeDto();

        copyToHierDto(hier, hierDto);

        long hierID = 0;
        HierarchyNodeDaoImpl hierDao = new HierarchyNodeDaoImpl();
        try {
            hierID = hierDao.insert(hierDto);
        } catch (HierarchyDaoException ex) {
            Logger.getLogger(HierarchyNodeService.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        /* Hierarchy Extend Attributes Values */
        HierarchyNodeEavDto attDto = new HierarchyNodeEavDto();
        attDto.setHierarchyNodeId(hierID);
        attDto.setAttributes(hier.getAttributes());
        HierarchyNodeEavDaoImpl hierEavDao = new HierarchyNodeEavDaoImpl();
        hierEavDao.insert(attDto);
    }

    /**
     * Method 'delete'
     *
     */
    public void delete(HierarchyNode rel) {
    }

    /**
     * Method 'update'
     *
     */
    public void update(HierarchyNode hier) throws HierarchyDaoException {
        /* Hierarchy object */
        HierarchyNodeDto dto = new HierarchyNodeDto();
        copyToHierDto(hier, dto);
        HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl();
    //dao.update(dto);
    }

    /**
     * Method 'search'
     *
     * @return HierarchyDto
     */
    public List<HierarchyNode> search(String euid) throws HierarchyDaoException {
        HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl();
        ArrayList<HierarchyNode> relList = new ArrayList<HierarchyNode>();
        for (HierarchyNodeDto hierDto : dao.search(euid)) {
            HierarchyNode hier = new HierarchyNode();
            copyFromHierDto(hier, hierDto);
            relList.add(hier);
        }
        return relList;
    }

    /**
     * Method 'copyTorelDto'
     *
     */
    private void copyToHierDto(HierarchyNode hier, HierarchyNodeDto dto) {
        dto.setHierarchyNodeId(hier.getNodeID());
        dto.setHierarchyDefId(hier.getHierarchyDef().getID());
        dto.setEuid(hier.getEUID());
        dto.setEffectiveFromDate(hier.getEffectiveFromDate());
        dto.setEffectiveToDate(hier.getEffectiveToDate());
    }

    /**
     * Method 'copyFromrelDto'
     *
     */
    private void copyFromHierDto(HierarchyNode hier, HierarchyNodeDto hierDto) {
        hier.setNodeID(hierDto.getHierarchyNodeId());

        hier.setEffectiveFromDate(hierDto.getEffectiveFromDate());
        hier.setEffectiveToDate(hierDto.getEffectiveToDate());
        Iterator iter = hierDto.getHierarchyAttributes().keySet().iterator();
        while (iter.hasNext()) {
            HierarchyNodeEaDto eaDto = (HierarchyNodeEaDto) iter.next();
            String attrValue = hierDto.getHierarchyAttributes().get(eaDto);
            Attribute attr = new Attribute();
            attr.setId(eaDto.getEaId());
            attr.setName(eaDto.getAttributeName());
            attr.setColumnName(eaDto.getColumnName());
            attr.setType(AttributeType.valueOf(eaDto.getColumnType()));
            attr.setDefaultValue(eaDto.getDefaultValue());
            attr.setIsIncluded(eaDto.getIsIncluded());
            attr.setIsRequired(eaDto.getIsRequired());
            attr.setIsSearchable(eaDto.getIsSearchable());
            hier.getAttributes().put(attr, attrValue);
        }
    }
}
