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
import com.sun.mdm.multidomain.hierarchy.HierarchyTree;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeDaoImpl;
import com.sun.mdm.multidomain.hierarchy.ops.dao.HierarchyNodeDao;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyDefDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEaDto;
import com.sun.mdm.multidomain.hierarchy.ops.dto.HierarchyNodeEavDto;
import com.sun.mdm.multidomain.hierarchy.ops.impl.HierarchyNodeEavDaoImpl;
import java.sql.Connection;
import java.sql.SQLException;
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
    private Connection mConn = null;

    /**
     * Method 'HierarchyService'
     *
     */
    public HierarchyNodeService() {
        getDaoInstances();

    }

    public HierarchyNodeService(DatabaseType dbtype, Connection conn) {
        this.mDbType = dbtype;
        this.mConn = conn;
        getDaoInstances();

    }

    public HierarchyNodeService(Connection conn) {
        this.mConn = conn;
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
     * Method 'addHierarchyNode'
     *
     */
    public long addHierarchyNode(HierarchyNode hierNode) throws HierarchyDaoException, HierarchyEavDaoException {

        /* HierarchyNode object */
        HierarchyNodeDto hierDto = new HierarchyNodeDto();

        copyToHierDto(hierNode, hierDto);

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
        attDto.setAttributes(hierNode.getAttributes());
        HierarchyNodeEavDaoImpl hierEavDao = new HierarchyNodeEavDaoImpl();
        return hierEavDao.insert(attDto);
    }

    public void addHiearchyNodes(long parentNodeId, HierarchyNode[] hierNodes) throws HierarchyDaoException, HierarchyEavDaoException {
        for (HierarchyNode node : hierNodes) {
            node.setParentNodeID(parentNodeId);
            addHierarchyNode(node);
        }
    }

    public HierarchyTree getHierarchyTree(long parentNodeId) {
        HierarchyTree hierTree = new HierarchyTree();
        try {
            HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl(mConn);

            for (HierarchyNodeDto hierDto : dao.findChildren(parentNodeId)) {
                HierarchyNode hier = new HierarchyNode();
                copyFromHierDto(hier, hierDto);
                hierTree.getChildren().add(hier);
            }

            for (HierarchyNodeDto hierDto : dao.findAncestors(parentNodeId)) {
                HierarchyNode hier = new HierarchyNode();
                copyFromHierDto(hier, hierDto);
                hierTree.getAncestors().add(hier);
            }

        } catch (HierarchyDaoException ex) {
            Logger.getLogger(HierarchyNodeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hierTree;
    }

    public List<HierarchyNode> getChildren(long nodeId) throws HierarchyDaoException {
       List<HierarchyNode> children = new ArrayList<HierarchyNode>();
       HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl(mConn);
       
       List<HierarchyNodeDto> childrenDto = dao.findChildren(nodeId);
       for (HierarchyNodeDto dto : childrenDto) {
           HierarchyNode node = new HierarchyNode();
           copyFromHierDto(node, dto);
           children.add(node);
       }
       
       return children;
    }
    
    /**
     * Method 'delete'
     * @throws HierarchyDaoException 
     *
     */
    public void deleteHierarchy(long nodeId) throws HierarchyDaoException {
        HierarchyNodeDaoImpl hierDao = new HierarchyNodeDaoImpl();
        hierDao.delete(nodeId);
    }
    
    /**
     * Deletes the node and moves children up to parent node
     * 
     * @param nodeId the id of the node to delete
     * @throws HierarchyDaoException
     */
    public void deleteHierarchyNode(long nodeId) throws HierarchyDaoException {
        List<HierarchyNodeDto> children = null;
        
        HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl(mConn);
        children = dao.findChildren(nodeId);
        
        HierarchyNodeDto node = dao.findNode(nodeId);
        
        //cannot delete root node and move children up
        if (node.getParentEuid() != null && node.getParentNodeId() != 0) {
            for (HierarchyNodeDto child : children) {
                HierarchyNode childNode = new HierarchyNode();
                copyFromHierDto(childNode, child);
                childNode.setParentEUID(node.getParentEuid());
                childNode.setParentNodeID(node.getParentNodeId());
                update(childNode);
            }
        }
    }
    
    public HierarchyNode getHierarchyNode(long nodeId) throws HierarchyDaoException {
        HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl(mConn);
        HierarchyNode node = new HierarchyNode();
        HierarchyNodeDto dtoNode = dao.findNode(nodeId);
        
        copyFromHierDto(node, dtoNode);
        
        List<HierarchyNode> children = this.getChildren(nodeId);
        node.setChildren(children);
        
        return node;
    }
    
    public HierarchyNode getRootNode(long hierarchyDefId) throws HierarchyDaoException {
        HierarchyNodeDaoImpl dao = new HierarchyNodeDaoImpl(mConn);
        HierarchyNode node = new HierarchyNode();
        HierarchyNodeDto dtoNode;
        dtoNode = dao.getRootHierarchyNode(hierarchyDefId);
        
        copyFromHierDto(node, dtoNode);
        
        List<HierarchyNode> children = this.getChildren(node.getNodeID());
        node.setChildren(children);
        
        return node;
    }

    public void moveHierarchyNodes(List<HierarchyNode> nodes, HierarchyNode parentNode) throws HierarchyDaoException {
        for (HierarchyNode node : nodes) {
            node.setParentEUID(parentNode.getEUID());
            node.setParentNodeID(parentNode.getNodeID());
            update(node);
        }
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
        ArrayList<HierarchyNode> hierList = new ArrayList<HierarchyNode>();
        for (HierarchyNodeDto hierDto : dao.search(euid)) {
            HierarchyNode hier = new HierarchyNode();
            copyFromHierDto(hier, hierDto);
            hierList.add(hier);
        }
        return hierList;
    }

    /**
     * Method 'copyTorelDto'
     *
     */
    private void copyToHierDto(HierarchyNode hier, HierarchyNodeDto dto) {
        dto.setHierarchyDefId(hier.getHierarchyDef().getId());
        dto.setParentNodeId(hier.getParentNodeID());
        dto.setEuid(hier.getEUID());
        dto.setParentEuid(hier.getParentEUID());
        dto.setEffectiveFromDate(hier.getEffectiveFromDate());
        dto.setEffectiveToDate(hier.getEffectiveToDate());
    }

    /**
     * Method 'copyFromrelDto'
     *
     */
    private void copyFromHierDto(HierarchyNode hier, HierarchyNodeDto hierDto) {
        hier.setNodeID(hierDto.getHierarchyNodeId());
        hier.setParentNodeID(hierDto.getParentNodeId());
        hier.setEUID(hierDto.getEuid());
        hier.setParentEUID(hierDto.getParentEuid());
        hier.setEffectiveFromDate(hierDto.getEffectiveFromDate());
        hier.setEffectiveToDate(hierDto.getEffectiveToDate());
        Iterator iter = hierDto.getHierarchyAttributes().keySet().iterator();
        while (iter.hasNext()) {
            HierarchyNodeEaDto eaDto = (HierarchyNodeEaDto) iter.next();
            String attrValue = hierDto.getHierarchyAttributes().get(eaDto);
            hier.getAttributes().put(eaDto.getAttributeName(), attrValue);
        }
    }
}
