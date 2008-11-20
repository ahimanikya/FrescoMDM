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
package com.sun.mdm.multidomain.presentation.beans;

import  java.util.List;
import  java.util.ArrayList;
        
import com.sun.mdm.multidomain.services.hierarchy.HierarchyDefExt;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeRecord;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeView;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyTreeView;
import com.sun.mdm.multidomain.services.hierarchy.HierarchySearch;
import com.sun.mdm.multidomain.services.model.DomainSearch;

import com.sun.mdm.multidomain.services.control.HierarchyManager;
import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.core.ServiceException;  

/**
 * HierarchyHandler class.
 * @author cye
 */
public class HierarchyHandler {
    private HierarchyManager hierarchyManager;

    /**
     * Create an instance of HierarchyHandler.
     */    
    public HierarchyHandler() 
        throws ServiceException { 
        hierarchyManager = ServiceManagerFactory.Instance().createHierarchyManager();
    }
    
    /**
     * Get total count of hierarchy node for the given hierarchy def id and domain.
     * @param hDefExt HierarchyDefExt.
     * @return int Count of total hierarchy node.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyNodeCount(HierarchyDefExt hDefExt) 
        throws ServiceException {
        int count = 0;
        try {
            count = hierarchyManager.getHierarchyNodeCount(hDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }        
        return count;
    }
    
    /**
     * Add a new HierarchyNode under the parent node.
     * @param hNodeRecord HierarchyNodeRecord.
     * @return String HierarchyNode Id.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchyNode(HierarchyNodeRecord hNodeRecord) 
        throws ServiceException {
        String hNodeId = null;
        try {
            hNodeId = hierarchyManager.addHierarchyNode(hNodeRecord);
        } catch(ServiceException svcex) {
            throw svcex;
        }                
        return hNodeId;
    }
    
    /**
     * Add a list of HierarchyNodeRecord under the specified parent node.
     * @param parentId Parent hierarchy node Id.
     * @param hNodeRecords List<HierarchyNodeRecord>.
     * @return List<String> List of hierarchy node Id.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<String> addHierarchyNodes(String parentId, List<HierarchyNodeRecord> hNodeRecords) 
        throws ServiceException {
        List<String> ids = new ArrayList<String>();
        try {
            ids = hierarchyManager.addHierarchyNodes(parentId, hNodeRecords);
        } catch(ServiceException svcex) {
            throw svcex;
        }        
        return ids;
    }
    
    /**
     * Delete an existing hierarchy node for the given hierarchy node Id.
     * @param hierarchyNodeId Hierarchy NodeId.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchyNode(int hierarchyNodeId) 
        throws ServiceException {
        try {
            hierarchyManager.deleteHierarchyNode(hierarchyNodeId);
        } catch(ServiceException svcex) {
            throw svcex;
        }                
    }
       
    /**
     * Update an existing HierarchyNode.
     * @param hNodeRecord HierarchyNodeRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchyNode(HierarchyNodeRecord hNodeRecord) 
        throws ServiceException {
        try {
            hierarchyManager.updateHierarchyNode(hNodeRecord);
        } catch(ServiceException svcex) {
            throw svcex;
        }                        
    }
    
    /**
     * Get an existing HierarchyNode for the given hierarchy node Id.
     * @param hierarchyNodeId HierarchyNode Id.
     * @return HierarchyNodeRecord HierarchyNodeRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyNodeRecord getHierarchyNode(int hierarchyNodeId) 
        throws ServiceException {
        HierarchyNodeRecord hNodeRecord = new HierarchyNodeRecord();
        try {
            hNodeRecord = hierarchyManager.getHierarchyNode(hierarchyNodeId);
        } catch(ServiceException svcex) {
            throw svcex;
        }                 
        return hNodeRecord;
    }
    
    /**
     * Get a list of HierarchyNodeRecord for the given hierarchy node Id.
     * @param hierarchyNodeId
     * @return List<HierarchyNodeRecord> List of HierarchyNode.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyNodeRecord> getHierarchyNodeChildren(int hierarchyNodeId)
        throws ServiceException {
        List<HierarchyNodeRecord> children = new ArrayList<HierarchyNodeRecord>();
        try {
            children = hierarchyManager.getHierarchyNodeChildren(hierarchyNodeId);
        } catch(ServiceException svcex) {
            throw svcex;
        }                         
        return children;
    }   
     
    /**
     * Move a set of hierarchy nodes to a new parent node.
     * @param nodeIds List of hierarchy node Id.
     * @param newParentNodeId New parent hierarchy node Id.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void moveHierarchyNodes(List<Integer> nodeIds, int newParentNodeId)
        throws ServiceException {     
        try {
            hierarchyManager.moveHierarchyNodes(nodeIds, newParentNodeId);
        } catch(ServiceException svcex) {
            throw svcex;
        }                                 
    }   
    
    /**
     * Search all HierarchyNodes for the given domain search options and hierarchy node search criteria.
     * @param dSearch Domain search options.
     * @param hNodeSearch Hierarchy node search criteria.
     * @return List<HierarchyNodeView> List of HierarchyNode.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyNodeView> searchHierarchyNodes(DomainSearch dSearch, HierarchySearch hNodeSearch) 
        throws ServiceException {
        List<HierarchyNodeView> hNodeViews = new ArrayList<HierarchyNodeView>();
        try {
            hNodeViews = hierarchyManager.searchHierarchyNodes(dSearch, hNodeSearch);
        } catch(ServiceException svcex) {
            throw svcex;
        }                                         
        return hNodeViews;
    }
     
    /**
     * Get a hierarchy tree for the specified node Id and node EUID.
     * @param nodeId Hierarchy node Id.
     * @param EUID  Hierarchy node EUID.
     * @return HierarchyTreeView HierarchyTreeView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyTreeView getHierarchyTree(int nodeId, String EUID) 
        throws ServiceException {
        HierarchyTreeView hTreeView = new HierarchyTreeView();
        try {
            hTreeView = hierarchyManager.getHierarchyTree(nodeId, EUID);
        } catch(ServiceException svcex) {
            throw svcex;
        }                                         
        return hTreeView;
    }    
}
