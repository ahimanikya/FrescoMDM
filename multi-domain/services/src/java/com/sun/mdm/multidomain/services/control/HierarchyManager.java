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
import java.util.Arrays;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.hierarchy.HierarchyObject;
import com.sun.mdm.multidomain.hierarchy.HierarchyObjectTree;

import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * HierarchyManager class.
 * @author cye
 */
public class HierarchyManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.HierarchyManager");
    private static Localizer localizer = Localizer.getInstance();

    private MultiDomainService multiDomainService;
    private MultiDomainMetaService multiDomainMetaService;
	
    /**
     * HierarchyManager class.
     */
    public HierarchyManager() {
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
    }
        
    /**
     * Add a new HierarchyDef.
     * @param HierarchyDef HierarchyDef.
     * @return String HierarchyDef identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addHierarchyDef(HierarchyDef HierarchyDef) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Update an existing HierarchyDef.
     * @param HierarchyDef HierarchyDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateHierarchyDef(HierarchyDef HierarchyDef) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }
    
    /**
     * Delete an existing HierarchyDef.
     * @param HierarchyDef HierarchyDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteHierarchyDef(HierarchyDef HierarchyDef) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");     
    }   
    
    /**
     * Get a total count of hierarchy HierarchyObject types for the given domain.
     * @param domain Domain name.
     * @return In Count of hierarchy HierarchyObject type
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyDefCount(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");             
    }
    
    /**
     * Get a list of hierarchy types for the given domain.
     * @param domain Domain name.
     * @return List<HierarchyDef> List of HierarchyObject type
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<HierarchyDef> getHierarchyDefs(String domain) throws ServiceException {
    	List<HierarchyDef> hierarchys = null;      
        try {
            HierarchyDef[] HierarchyDefs = multiDomainMetaService.getHierarchyDefs(domain);
            hierarchys = Arrays.asList(HierarchyDefs);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }                 
    	return hierarchys;
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
