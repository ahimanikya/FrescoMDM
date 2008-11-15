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
import com.sun.mdm.multidomain.hierarchy.HierarchyObject;
import com.sun.mdm.multidomain.hierarchy.HierarchyObjectTree;

import com.sun.mdm.multidomain.services.hierarchy.HierarchyDefExt;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;
import com.sun.mdm.multidomain.services.core.ViewHelper;

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
    	Attribute a1 = new Attribute("salary", "yearly income", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt1.setAttribute(a1);
    	
    	HierarchyDef rt2 = new HierarchyDef();
    	rt2.setName("employedby");
        rt2.setId(1);
        rt2.setDomain("Person");   	
    	Attribute a2 = new Attribute("hiredDate", "hired date", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt2.setAttribute(a2);

    	HierarchyDef rt3 = new HierarchyDef();
        rt3.setName("contractwith");
        rt3.setId(1);
    	rt3.setDomain("Person");  	
    	Attribute a3 = new Attribute("startDate", "date started", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt3.setAttribute(a3);
        
    	HierarchyDef rt4 = new HierarchyDef();
    	rt4.setName("investon");
        rt4.setId(2);
        rt4.setDomain("Company");
    	Attribute a4 = new Attribute("invest", "total investment", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt4.setAttribute(a4);
    	
    	HierarchyDef rt5 = new HierarchyDef();
    	rt5.setName("designon");
        rt5.setId(3);
        rt5.setDomain("Person");
    	Attribute a5 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
    	rt5.setAttribute(a5);
    	
        HierarchyDef rt6 = new HierarchyDef();
        rt6.setName("workon");
        rt6.setId(3);
    	rt6.setDomain("Product");
    	Attribute a6 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
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
            HierarchyDef hDef = ViewHelper.toHierarchyDef(hDefExt);
            hDefId = multiDomainMetaService.createHierarchyDef(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }  
        }
        //demo
        hDefExt.setId(Long.toString(System.currentTimeMillis()));
        hs.add(ViewHelper.toHierarchyDef(hDefExt));
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
            HierarchyDef hDef = ViewHelper.toHierarchyDef(hDefExt);
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
                rt.setEffectiveFromRequired(hDefExt.getStartDateRequired().equalsIgnoreCase("true") ? true : false);
                updated = true;
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
            HierarchyDef hDef = ViewHelper.toHierarchyDef(hDefExt);
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
            hDefExt = ViewHelper.toHierarchyDefExt(hDef);
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
                hDefExt = ViewHelper.toHierarchyDefExt(rt);
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
            hDefExt = ViewHelper.toHierarchyDefExt(hDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }     
        }
        //demo
        for (HierarchyDef rt:hs) {
            if (rt.getId() == hierarchyId) {
                hDefExt = ViewHelper.toHierarchyDefExt(rt);
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
                hDefs.add(ViewHelper.toHierarchyDefExt(rt));	
            }
    	}       
    	return hDefs;
    }
    
    /**
     * Get a total count of hierarchy HierarchyObject instances for the given HierarchyObject type.
     * @param HierarchyDef HierarchyDef.
     * @return int Count of hierarchy HierarchyObject instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyObjectCount(HierarchyDef HierarchyDef) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");  
    }
    
    /**
     * Get a list of hierarchy HierarchyObject instances for the given HierarchyObject type.
     * @param HierarchyDef HierarchyDef.
     * @return List<HierarchyObject> List of hierarchy HierarchyObject instances
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyObject> getHierarchyObjects(HierarchyDef HierarchyDef) throws ServiceException {
    	List<HierarchyObject> hierarchys = null;
    	return hierarchys;
    }
       
    /**
     * Add a new hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchy HierarchyObject.
     * @return String Hierarchy identifier of a newly added hierarchy instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchy(String domain, String parentEUID, String childEUID, HierarchyObject hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Add a new hierarchy instance between patent entity and children entities for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs.
     * @param hierarchy HierarchyObject.
     * @return String Hierarchy identifier of a newly added hierarchy instance.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchy(String domain, String parentEUID, List<String> childEUIDs, HierarchyObject hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }    
    
    /**
     * Update an existing hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchy HierarchyObject.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchy(String domain, String parentEUID, String childEUID, HierarchyObject hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Update an existing hierarchy instance between patent entity and children entities for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs.
     * @param hierarchy HierarchyObject.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchy(String domain, String parentEUID, List<String> childEUIDs, HierarchyObject hierarchy) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    } 
    
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain Domain name. 
     * @param parentEUID Parent entity EUID.
     * @param childEUID Child entity EUID.
     * @param hierarchyName HierarchyName.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchy(String domain, String parentEUID, String childEUID, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }     
 
    /**
     * Delete an existing hierarchy instance for the given domain.
     * @param domain Domain name.
     * @param parentEUID Parent entity EUID.
     * @param childEUIDs Children entities EUIDs. 
     * @param hierarchyName HierarchyName.
     * @throws ServiceException Thrown if an error occurs during processing.
     */    
    public void deleteHierarchy(String domain, String parentEUID, List<String> childEUIDs, String hierarchyName) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
}
