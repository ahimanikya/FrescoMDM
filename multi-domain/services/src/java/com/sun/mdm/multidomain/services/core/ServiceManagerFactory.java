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
package com.sun.mdm.multidomain.services.core;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.services.control.GroupManager;
import com.sun.mdm.multidomain.services.control.HierarchyManager;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.control.MetaDataManager;
import com.sun.mdm.multidomain.services.core.ServiceLocator;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * ServiceManagerFactory class
 * @author cye
 *
 */
public class ServiceManagerFactory {
	private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.ServiceManagerFactory");
	private static Localizer localizer = Localizer.getInstance();

	private static ServiceLocator serviceLocator;
	private static MultiDomainService multiDomainService;
	private static MultiDomainMetaService multiDomainMetaService;       
        private static ServiceManagerFactory instance;
        private boolean isInitialized;
        
	/**
	 * Create an instance of ServiceManagerFactory
	 */
	private ServiceManagerFactory() {
	}
	
        public static ServiceManagerFactory Instance() {
            if (instance == null) {
                instance = new ServiceManagerFactory();
            }
            return instance;
        }
        
	/**
	 * Initialize ServiceManagerFactory.
	 * @throws ServiceException Thrown if an error occurs during processing.
	 */
	public void initialize() throws ServiceException {
            if (!isInitialized) {
                serviceLocator = ServiceLocator.getInstance();
                multiDomainService = serviceLocator.getMultiDomainService();
                multiDomainMetaService = serviceLocator.getMultiDomainMetaService();                
                isInitialized = true;
           }
	}
	
	/**
	 * Create an instance of group manager.
	 * @return group manager Group Manager.
	 * @throws ServiceException Thrown if an error occurs during processing.
	 */
	public GroupManager createGroupManager() throws ServiceException {
                initialize();
		return new GroupManager(multiDomainMetaService, multiDomainService); 
	}

	/**
	 * Create an instance of hierarchy manager
	 * @return hierarchy manager Hierarchy Manager.
	 * @throws ServiceException Thrown if an error occurs during processing.
	 */	
	public HierarchyManager createHierarchyManager() throws ServiceException {		
            initialize();            
            return new HierarchyManager(multiDomainMetaService, multiDomainService); 
	}

	/**
	 * Create an instance of relationship manager
	 * @return relationship manager Relationship Manager.
	 * @throws ServiceException Thrown if an error occurs during processing.
	 */		
	public RelationshipManager createRelationshipManager() throws ServiceException {
            initialize();            
            return new RelationshipManager(multiDomainMetaService, multiDomainService);
	}

	/**
	 * Create an instance of metadata manager.
	 * @return metadata manager MetaDataManager.
	 * @throws ServiceException Thrown if an error occurs during processing.
	 */			
	public MetaDataManager createMetaDataManager() throws ServiceException {
            initialize();            
            return new MetaDataManager(multiDomainMetaService, multiDomainService); 
	}
	
}
