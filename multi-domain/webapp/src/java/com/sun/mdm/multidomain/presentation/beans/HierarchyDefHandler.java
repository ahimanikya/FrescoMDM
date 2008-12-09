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

import com.sun.mdm.multidomain.services.control.HierarchyManager;
import com.sun.mdm.multidomain.services.core.ServiceManagerFactory;
import com.sun.mdm.multidomain.services.core.ServiceException;  

/**
 * HierarchyTypeHandler class.
 * @author cye
 */
public class HierarchyDefHandler {
    private HierarchyManager hierarchyManager;
  
    /**
     * Create an instance of HierarchyTypeHandler.
     */
    public HierarchyDefHandler() {
    }
    
    /**
     * Initialize HierarchyHandler.
     * @throws ServiceException Thrown if an error occurs during processing. 
     */
    private void initialize() 
        throws ServiceException {
        if (hierarchyManager == null) {
            hierarchyManager = ServiceManagerFactory.Instance().createHierarchyManager(); 
        }
    }
    
   /**
     * Get all HirarchyDefs for the given domain name.
     * @param domain Domain name.
     * @return List<HierarchyDefEx> List of HierarchyDefEx.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */    
    public List<HierarchyDefExt> getHierarchyDefs(String domain) 
        throws ServiceException { 
        List<HierarchyDefExt> types = null;
        try {
            initialize();
            types = hierarchyManager.getHierarchyDefs(domain);
        } catch(ServiceException sex) {
            throw sex;
        }
        if(types == null) {
            types = new ArrayList<HierarchyDefExt>();
        }
        return types;        
   }
    
    /**
     * Get HierarchyDef definition for the given hierarchy name and domain name.
     * @param name HierarchyDef name.
     * @return HierarchyDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyDefExt getHierarchyDefByName(String name, String domain) 
        throws ServiceException {                
        HierarchyDefExt type = null;
        try {
            initialize();
            type = hierarchyManager.getHierarchyDefByName(name, domain);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return type;
    }
    
     /**
     * Get HierarchyDef definition for the given hierarchy Id.
     * @param hierarchyId HierarchyId.
     * @return HierarchyDefExt HierarchyDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public HierarchyDefExt getHierarchyDefById(long hierarchyId) 
        throws ServiceException {                
        HierarchyDefExt type = null;
        try {
            initialize();
            type = hierarchyManager.getHierarchyDefById(hierarchyId);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return type;
    }
    
    /**
     * Create a new hierarchy definition.
     * @param hDefExt HierarchyDefExt.
     * @return String Hierarchy Identifier which is newly added.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public String addHierarchyDef(HierarchyDefExt hDefExt) 
        throws ServiceException {        
        String HierarchyDefId = null;
        try {
            initialize();
            HierarchyDefId = hierarchyManager.addHierarchyDef(hDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }
        return HierarchyDefId;        
    }
    
    /**
     * Update an existing hierarchy definition.
     * @param hDefExt HierarchyDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public void updateHierarchyDef(HierarchyDefExt hDefExt) 
        throws ServiceException {        
        try {
            initialize();
            hierarchyManager.updateHierarchyDef(hDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }    
    }
    
    /**
     * Delete an existing HierarchyDef.
     * @param hDefExt HierarchyDefExt.
     * @exception ServiceException Thrown if an error occurs during processing. 
     */
    public void deleteHierarchyDef(HierarchyDefExt hDefExt) 
        throws ServiceException {        
        try {
            initialize();
            hierarchyManager.deleteHierarchyDef(hDefExt);
        } catch(ServiceException svcex) {
            throw svcex;
        }        
    }
    
    /**
     * Get total number of HierarchyDef for the given domain.
     * @param domain Domain name.
     * @return int Number of total HierarchyDefs.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getHierarchyDefCount(String domain)
        throws ServiceException { 
        int count = 0;
        try {
            initialize();
            count = hierarchyManager.getHierarchyDefCount(domain);
        } catch(ServiceException svcex) {
            throw svcex;
        }   
        return count;
    }   
}
