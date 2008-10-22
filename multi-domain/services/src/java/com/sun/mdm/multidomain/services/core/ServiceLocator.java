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

import net.java.hulp.i18n.Logger;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.multidomain.services.core.context.JndiResource;
import com.sun.mdm.multidomain.services.core.context.JndiProperties;

import com.sun.mdm.multidomain.services.configuration.MDConfigManager;

import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * ServiceLocator class.
 * @author cye
 */
public class ServiceLocator {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.core.ServiceLocator");
    private static Localizer localizer = Localizer.getInstance();

    private static ServiceLocator serviceLocator;
    private InitialContext initialContext;
    private MultiDomainMetaService multiDomainMetaService;
    private MultiDomainService multiDomainService;
    
    /**
     * ServiceLocator singleton class.
     */
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
  
    /**
     * Get initial context.
     * @param jndiProperties JndiProperties.
     * @return InitialContext InitialContext.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    private InitialContext getInitialContext(JndiProperties jndiProperties) 
    	throws ServiceException {    	
    	//"java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory"
    	//"java.naming.provider.url", "iiop://localhost:3100"	  			
    	//"java.naming.security.principal", "edmuser"
    	//"java.naming.security.credentials", "edmuser"
    	if (initialContext == null) {
            try {
                if (jndiProperties != null &&
                    !jndiProperties.isEmpty()) {
                    // external resources
                    Hashtable<String, String> env = new Hashtable<String, String>();
                    env.put("java.naming.factory.initial", jndiProperties.getInitialContextFactory());
                    env.put("java.naming.provider.url", jndiProperties.getProviderUrl());	  			
                    initialContext = new InitialContext(env);		    		
                } else {
                    // internal resources
                    initialContext = new InitialContext();		    		    		
    		}
    		logger.info(localizer.x("WTS001: Initialcontext({0}) is successfully initialized"));
            } catch(NamingException nex) {
                throw new ServiceException(nex);
            }
        }
    	return initialContext;
    }
    
    /**
     * Get a multi-domain meta service.
     * @return MultiDomainMetaService interface.
     * @throws ServiceException Thrown if an error occurs during processing. 
     */
    public MultiDomainMetaService getMultiDomainMetaService()  
    	throws ServiceException { 
    	if (multiDomainMetaService == null) {
            JndiResource jndiResource = new JndiResource(); //TBD: MDConfigManager.getMultiDomainMetaService();
            jndiResource.setName("ejb/MultiDomainMetaService");
            JndiProperties jndiProperties = new JndiProperties(); //TBD: MDConfigManager.getJndiProperties();                
            String jndiName =  jndiResource.getName();    	
            /*TDB
            try {
                Object object = getInitialContext(jndiProperties).lookup(jndiName);
                multiDomainMetaService = (MultiDomainMetaService)object;
            } catch (NamingException nex) {
                throw new ServiceException(nex);    	
            }
            */
    	}
    	return multiDomainMetaService;
    }      
    
    /**
     * Get a multi-domain service.
     * @param MultiDomainService MultiDomainService interface.
     * @return MultiDomainService Thrown if an error occurs during processing.
     */
    public MultiDomainService getMultiDomainService()  
    	throws ServiceException { 
    	if (multiDomainService == null) {    		
            JndiResource jndiResource = new JndiResource(); //TBD: MDConfigManager.getMultiDomainService();
             jndiResource.setName("ejb/MultiDomainService");
            JndiProperties jndiProperties = new JndiProperties(); //TBD: MDConfigManager.getJndiProperties();                
            String jndiName =  jndiResource.getName();  
            /* TDB
            try {
                Object object = getInitialContext(jndiProperties).lookup(jndiName);
                multiDomainService = (MultiDomainService)object;
            } catch (NamingException nex) {
                throw new ServiceException(nex);    	
            }
            */
    	}
    	return multiDomainService;
    }      
}
