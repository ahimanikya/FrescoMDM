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

import net.java.hulp.i18n.Logger;

import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.control.GroupManager;
import com.sun.mdm.multidomain.services.control.HierarchyManager;
import com.sun.mdm.multidomain.services.control.MetaDataManager;
import com.sun.mdm.multidomain.services.core.ServiceLocator;
import com.sun.mdm.multidomain.services.core.MultiDomainService;
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

	/**
	 * Create an instance of ServiceManagerFactory
	 */
	public ServiceManagerFactory() {
	}
	
	/**
	 * Initialize ServiceManagerFactory.
	 * @throws ServiceException
	 */
	public void init() throws ServiceException {
		serviceLocator = ServiceLocator.getInstance();
		multiDomainService = serviceLocator.getMultiDomainService();
	}
	
	/**
	 * Create an instance of group manager
	 * @return group manager
	 * @throws ServiceException
	 */
	public GroupManager createGroupManager() throws ServiceException {
		return new GroupManager(multiDomainService); 
	}

	/**
	 * Create an instance of hierarchy manager
	 * @return hierarchy manager
	 * @throws ServiceException
	 */	
	public HierarchyManager createHierarchyManager() throws ServiceException {
		
		return new HierarchyManager(multiDomainService); 
	}

	/**
	 * Create an instance of relationship manager
	 * @return relationship manager
	 * @throws ServiceException
	 */		
	public RelationshipManager createRelationshipManager() throws ServiceException {
		return new RelationshipManager(multiDomainService);
	}

	/**
	 * Create an instance of metadata manager
	 * @return metadata manager
	 * @throws ServiceException
	 */			
	public MetaDataManager createMetaDataManager() throws ServiceException {
		return new MetaDataManager(multiDomainService); 
	}
	
}
