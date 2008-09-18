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

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.control.MetaDataManager.ServiceManagerType;
import com.sun.mdm.multidomain.services.control.RelationshipManager;
import com.sun.mdm.multidomain.services.control.ServiceManager;

/**
 * ServiceLocator class.
 * @author cye
 */
public class ServiceLocator {
	
    private static ServiceLocator serviceLocator;
    private InitialContext initialContext;
    private MultiDomainService multiDomainService;
    
    private ServiceLocator() { 
    }

    /**
     * Get the instance of ServiceLocator.
     * @return service locator instance
     */
    static public ServiceLocator getInstance() 
    	throws ServiceException {
    	if (serviceLocator == null) {
    		serviceLocator = new ServiceLocator();
    	}
        return serviceLocator;
    }
  
    private InitialContext getInitialContext(String factoryInitial, String providerUrl) 
    	throws ServiceException {    	
    	//"java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory"
    	//"java.naming.provider.url", "iiop://localhost:3100"	  			
    	//"java.naming.security.principal", "edmuser"
    	//"java.naming.security.credentials", "edmuser"
    	if (initialContext == null) {
    		try {
    			if (factoryInitial != null &&
    				providerUrl != null	) {
    				Hashtable<String, String> env = new Hashtable<String, String>();
    				env.put("java.naming.factory.initial", factoryInitial);
    				env.put("java.naming.provider.url", providerUrl);	  			
    				initialContext = new InitialContext(env);		    		
    			} else {
    				initialContext = new InitialContext();		    		    		
    			}
    		} catch(NamingException nex) {
    			throw new ServiceException(nex);
    		}
    	}
    	return initialContext;
    }
    
    
    /**
     * Get a multi-domain service.
     * @param serviceType
     * @return service by EJBObject
     */
    public MultiDomainService getMultiDomainService()  
    	throws ServiceException { 
    	if (multiDomainService == null) {
    		// get from contextManager
    		String factoryInitial = null;
    		String providerUrl = null;
    		String serviceJNDIName = null;    	
    		MultiDomainService multiDomainService = null;    	
    		try {
    			Object object = getInitialContext(factoryInitial, providerUrl).lookup(serviceJNDIName);
    			multiDomainService = (MultiDomainService)object;
    		} catch (NamingException nex) {
    			throw new ServiceException(nex);    	
    		}
    	}
    	return multiDomainService;
    }      
}
