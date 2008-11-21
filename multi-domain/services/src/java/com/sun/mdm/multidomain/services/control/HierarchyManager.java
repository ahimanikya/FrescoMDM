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
package com.sun.mdm.multidomain.services.control;

import java.util.List;
import java.util.ArrayList;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.query.HierarchySearchCriteria;
import com.sun.mdm.multidomain.hierarchy.HierarchyTree;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions.DomainSearchOption;
        
import com.sun.mdm.multidomain.services.hierarchy.HierarchySearch;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeView;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyDefExt;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyNodeRecord;
import com.sun.mdm.multidomain.services.hierarchy.HierarchyTreeView;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.util.Localizer;
import com.sun.mdm.multidomain.services.core.ViewBuilder;
import com.sun.mdm.multidomain.services.core.QueryBuilder;

/**
 * HierarchyManager class.
 * @author cye
 */
public class HierarchyManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.HierarchyManager");
    private static Localizer localizer = Localizer.getInstance();

    private MultiDomainService multiDomainService;
    private MultiDomainMetaService multiDomainMetaService;
	
   // demo data
    private List<HierarchyDef> hs = new ArrayList<HierarchyDef>(); 
    private boolean TBD = true;
    
    /**
     * HierarchyManager class.
     */
    public HierarchyManager() {
        //TBD
        init();
    }
    
    /**
     * Create a instance of HierarchyManager with the given MultiDomainMetaService and MultiDomainService. 
     * @param multiDomainMetaService MultiDomainMetaService.
     * @param multiDomainService MultiDomainService.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyManager (MultiDomainMetaService multiDomainMetaService, MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    	this.multiDomainMetaService = multiDomainMetaService;  
        logger.info(localizer.x("SVC005: HierarchyManager initialization completed."));  
        //TBD
        init();
    }
     
    private void init(){
        // demo data
    	HierarchyDef rt1 = new HierarchyDef();
    	rt1.setName("worgchart");
        rt1.setId(1);
        rt1.setDomain("Person");    	
    	Attribute a1 = new Attribute("salary", "yearly income", AttributeType.FLOAT, "500000.0");
    	rt1.setAttribute(a1);
    	
    	HierarchyDef rt2 = new HierarchyDef();
    	rt2.setName("employedby");
        rt2.setId(1);
        rt2.setDomain("Person");   	
    	Attribute a2 = new Attribute("hiredDate", "hired date", AttributeType.DATE, "09/10/2008");
    	rt2.setAttribute(a2);

    	HierarchyDef rt3 = new HierarchyDef();
        rt3.setName("contractwith");
        rt3.setId(1);
    	rt3.setDomain("Person");  	
    	Attribute a3 = new Attribute("startDate", "date started", AttributeType.DATE, "09/10/2008");
    	rt3.setAttribute(a3);
        
    	HierarchyDef rt4 = new HierarchyDef();
    	rt4.setName("investon");
        rt4.setId(2);
        rt4.setDomain("Company");
    	Attribute a4 = new Attribute("invest", "total investment", AttributeType.FLOAT, "500000.0");
    	rt4.setAttribute(a4);
    	
    	HierarchyDef rt5 = new HierarchyDef();
    	rt5.setName("designon");
        rt5.setId(3);
        rt5.setDomain("Person");
    	Attribute a5 = new Attribute("location", "phyiscal location", AttributeType.STRING, "Monrovia");
    	rt5.setAttribute(a5);
    	
        HierarchyDef rt6 = new HierarchyDef();
        rt6.setName("workon");
        rt6.setId(3);
    	rt6.setDomain("Product");
    	Attribute a6 = new Attribute("location", "phyiscal location", AttributeType.STRING, "Monrovia");
    	rt6.setAttribute(a6);    	       
    	hs.add(rt1);
    	hs.add(rt2);
    	hs.add(rt3);    
        hs.add(rt4);   
        hs.add(rt5);   
        hs.add(rt6);                           
    }
        
    /**
     * Add a new HierarchyDef.
     * @param hDefExt HierarchyDefExt.
     * @return String HierarchyDef identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchyDef(HierarchyDefExt hDefExt) 
        throws ServiceException {
        String hDefId = null;
        if (!TBD) {
        try {
            HierarchyDef hDef = ViewBuilder.buildHierarchyDef(hDefExt);
            hDefId = multiDomainMetaService.createHierarchyDef(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }  
        }
        //demo
        hDefExt.setId(Long.toString(System.currentTimeMillis()));
        hs.add(ViewBuilder.buildHierarchyDef(hDefExt));
        hDefId = hDefExt.getId();
        
        return hDefId;
    }
    
    /**
     * Update an existing HierarchyDef.
     * @param hDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchyDef(HierarchyDefExt hDefExt)
        throws ServiceException {
        if (!TBD) {
        try {
            HierarchyDef hDef = ViewBuilder.buildHierarchyDef(hDefExt);
            multiDomainMetaService.updateHierarchyDef(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }   
        }
        // demo data
        boolean updated = false;
        for (HierarchyDef rt:hs) {
            if (rt.getDomain().equals(hDefExt.getDomain()) &&
                rt.getName().equals(hDefExt.getName())) {                 
                HierarchyDef hDef = ViewBuilder.buildHierarchyDef(hDefExt);
                hs.remove(rt);
                hs.add(hDef);
                updated = true;
                break;
             }
    	}
        if (!updated) {
        throw new ServiceException("Invalid HierarchyDef:"  + 
                                   " domain:" + hDefExt.getDomain() +
                                   " name:" + hDefExt.getName());
        }        
    }
    
    /**
     * Delete an existing HierarchyDef.
     * @param hDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchyDef(HierarchyDefExt hDefExt)
        throws ServiceException {
        if (!TBD) {
        try {
            HierarchyDef hDef = ViewBuilder.buildHierarchyDef(hDefExt);
            multiDomainMetaService.deleteHierarchyDef(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }   
        }
         // demo data
        boolean deleted = false;
        List<HierarchyDef> temp = new ArrayList<HierarchyDef>();
        for (HierarchyDef rt:hs) {
            if (rt.getDomain().equals(hDefExt.getDomain()) &&
                rt.getName().equals(hDefExt.getName())) {                      
                deleted = true;
             } else {
                temp.add(rt);
             }
    	}
        if (!deleted) {
        throw new ServiceException("Invalid HierarchyDef:"  + 
                                   " domain:" + hDefExt.getDomain() +
                                   " name:" + hDefExt.getName());
        }   
        hs = temp;       
    }   
    
    /**
     * Get a total count of hierarchy HierarchyObject types for the given domain.
     * @param domain Domain name.
     * @return In Count of hierarchy HierarchyObject type
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyDefCount(String domain) 
        throws ServiceException {
        throw new ServiceException("Not Implemented Yet");             
    }
    
    /**
     * Get HierarchyDef definition for the given hierarchy name and domain name.
     * @param name HierarchyDef name.
     * @return HierarchyDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyDefExt getHierarchyDefByName(String name, String domain) 
        throws ServiceException {                
        HierarchyDefExt hDefExt = null;
        if (!TBD) {
        try {
            HierarchyDef hDef = multiDomainMetaService.getHierarchyDefByName(name, domain);
            hDefExt = ViewBuilder.buildHierarchyDefExt(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        }
         //demo
        for (HierarchyDef rt:hs) {
            if (rt.getName().equals(name) &&
                rt.getDomain().equals(domain)) { 
                hDefExt = ViewBuilder.buildHierarchyDefExt(rt);
                break;
            }
    	}        
        return hDefExt;
    }
    
     /**
     * Get HierarchyDef definition for the given hierarchy Id.
     * @param hierarchyId HierarchyId.
     * @return HierarchyDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyDefExt getHierarchyDefById(long hierarchyId) 
        throws ServiceException {                
        HierarchyDefExt hDefExt = null;
        if (!TBD) {
        try {
            HierarchyDef hDef = multiDomainMetaService.getHierarchyDefById(hierarchyId);
            hDefExt = ViewBuilder.buildHierarchyDefExt(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }     
        }
        //demo
        for (HierarchyDef rt:hs) {
            if (rt.getId() == hierarchyId) {
                hDefExt = ViewBuilder.buildHierarchyDefExt(rt);
                break;
            }
    	}             
        return hDefExt;
    }
    
    /**
     * Get a list of hierarchy definition for the given domain.
     * @param domain Domain name.
     * @return List<HierarchyDefExt> List of HierarchyDefExt
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyDefExt> getHierarchyDefs(String domain) 
        throws ServiceException {
    	List<HierarchyDefExt> hDefs = new ArrayList<HierarchyDefExt>();
        if (!TBD) {
        try {
            HierarchyDef[] HierarchyDefs = multiDomainMetaService.getHierarchyDefs(domain);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }   
        }
    	// demo data	
    	for (HierarchyDef rt:hs) {
            if (rt.getDomain().equals(domain)) {
                hDefs.add(ViewBuilder.buildHierarchyDefExt(rt));	
            }
    	}       
    	return hDefs;
    }
    
    /**
     * Get a total count of hierarchy HierarchyNode instances for the given HierarchyDef.
     * @param hDefExt HierarchyDefExt.
     * @return int Count of hierarchy HierarchyObject instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyNodeCount(HierarchyDefExt hDefExt) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");  
    }
    
    /**
     * Add a new hierarchy node for the given parent node Id and parent node EUID.
     * @param hNodeRecord HierarchyNodeRecord.
     * @return String Hierarchy node identifier of a newly added hierarchy node.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchyNode(HierarchyNodeRecord hNodeRecord) 
        throws ServiceException {
        int hNodeId = 0;
        try {
            HierarchyNode hNode = QueryBuilder.buildHierarchyNode(hNodeRecord);
            hNodeId = multiDomainService.addHierarchyNode(hNode);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }     
        return Integer.toString(hNodeId);
    }
    
     /**
     * Add a list of new hierarchy node for the given parent node Id and parent node EUID.
     * @param hNodeRecords List<HierarchyNodeRecord>.
     * @return List<String> List of hierarchy node Id.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<String> addHierarchyNodes(String parentId, List<HierarchyNodeRecord> hNodeRecords) 
        throws ServiceException {
        List<String> nodeIds = new ArrayList<String>();
        try {
            HierarchyNode[] hNodes = new HierarchyNode[hNodeRecords.size()];
            int i = 0;
            for (HierarchyNodeRecord hNodeRecord : hNodeRecords) {
                hNodes[i++] = QueryBuilder.buildHierarchyNode(hNodeRecord);
            };
            int[] ids = multiDomainService.addHierarchyNodes(Integer.parseInt(parentId), hNodes);
            for (int id : ids) {
                nodeIds.add(Integer.toString(id));
            }
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }     
        return nodeIds;
    }
    
    /**
     * Delete an existing hierarchyNode for the given hierarchyNode Id.
     * @param hierarchyNodeId HierarchyNodeId.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchyNode(int hierarchyNodeId) 
        throws ServiceException {
        try {
             multiDomainService.deleteHierarchyNode(hierarchyNodeId);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }           
    }
       
    /**
     * Update an existing HierarchyNode for the given HierarchyNodeRecord.
     * @param hNodeRecord HierarchyNodeRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchyNode(HierarchyNodeRecord hNodeRecord) 
        throws ServiceException {
        try {
            HierarchyNode hNode = QueryBuilder.buildHierarchyNode(hNodeRecord);
            multiDomainService.updateHierarchyNode(hNode);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }          
    }
    
    /**
     * Get a HierarchyNode for the given hierarchyNode Id.
     * @param hierarchyNodeId HierarchyNode Id.
     * @return HierarchyNodeRecord HierarchyNodeRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyNodeRecord getHierarchyNode(int hierarchyNodeId) 
        throws ServiceException {
        HierarchyNodeRecord hNodeRecord = new HierarchyNodeRecord();
        try {
            HierarchyNode hNode = multiDomainService.getHierarchyNode(hierarchyNodeId);
            hNodeRecord = ViewBuilder.buildHierarchyNodeRecord(hNode);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }            
        return hNodeRecord;
    }
    
    /**
     * Get all children for the given hierarchy node Id.
     * @param hierarchyNodeId HierarchyNode Id.
     * @return List<HierarchyNodeRecord> List of HierarchyNodeRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyNodeRecord> getHierarchyNodeChildren(int hierarchyNodeId)
        throws ServiceException {
        List<HierarchyNodeRecord> hNodeRecords = new ArrayList<HierarchyNodeRecord>();
         try {
            List<HierarchyNode> hNodes = multiDomainService.getHierarchyNodeChildren(hierarchyNodeId);
            for (HierarchyNode hNode : hNodes) {
                HierarchyNodeRecord hNodeRecord = ViewBuilder.buildHierarchyNodeRecord(hNode);
                hNodeRecords.add(hNodeRecord);
            }
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }    
        return hNodeRecords;
    }   
     
    /**
     * Move a set of nodes to a new parentNode.
     * @param nodeIds List of NodeId.
     * @param newParentNodeId New ParentNodeId.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void moveHierarchyNodes(List<Integer> nodeIds, int newParentNodeId)
        throws ServiceException {        
        try {
            int[] iNodeIds = new int[nodeIds.size()]; 
            for(int i = 0; i < nodeIds.size(); i++) {
                iNodeIds[i] = nodeIds.get(i).intValue();
            }
            multiDomainService.moveHierarchyNodes(iNodeIds, newParentNodeId);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }            
    }   
    
    /**
     * Get a list of HierarchyNode for the given search options and criteria.
     * @param dSearch DomainSearch option.
     * @param hNodeSearch HierarchySearch criteria.
     * @return List<HierarchyNodeView> List of HierarchyNodeView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyNodeView> searchHierarchyNodes(DomainSearch dSearch, HierarchySearch hNodeSearch) 
        throws ServiceException {
        List<HierarchyNodeView> hNodeViews = new ArrayList<HierarchyNodeView>();
        try {
            DomainSearchOption dSearchOption = QueryBuilder.buildMultiDomainSearchOption(dSearch);
            HierarchySearchCriteria hSearchCriteria = QueryBuilder.buildHierarchySearchCriteria(hNodeSearch);
            List<HierarchyNode> hNodes = multiDomainService.searchHierarchys(dSearchOption, hSearchCriteria);
        } catch (ConfigException cex) {    
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }                    
        return hNodeViews;
    }
     
    /**
     * Get a HierarchyTree for the given node id and node EUID.
     * @param nodeId Node Id.
     * @param EUID Node EUID.
     * @return HierarchyTreeView HierarchyTreeView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyTreeView getHierarchyTree(int nodeId, String EUID) 
        throws ServiceException {
        HierarchyTreeView hTreeView = new HierarchyTreeView();
        try {
            HierarchyTree hTree = multiDomainService.getHierarchyTree(nodeId, EUID);
            HierarchyNode hNode = hTree.getNode();
            List<HierarchyNode> ancestors = hTree.getAncestors();
            List<HierarchyNode> children = hTree.getChildren();
            hTreeView = ViewBuilder.buildHierarchyTreeView(hNode, ancestors, children);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }                  
        return hTreeView;
    }            
}
