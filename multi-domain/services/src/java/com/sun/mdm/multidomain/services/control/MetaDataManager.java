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
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.master.ProcessingException;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.services.model.Domain;

import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * MultiDomainManager
 * @author cye
 */
public class MetaDataManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.MetaDataManager");
    private static Localizer localizer = Localizer.getInstance();

    private MultiDomainMetaService multiDomainMetaService;
    
    /**
     * Create an instance of MetaDataManager.
     */
    public MetaDataManager() {   
    }
  
    /**
     * Create a instance of MetaDataManager with the given MultiDomainMetaService. 
     * @param multiDomainMetaService MultiDomainMetaService.
     */
    public MetaDataManager (MultiDomainMetaService multiDomainMetaService) {
        this.multiDomainMetaService = multiDomainMetaService;
        logger.info(localizer.x("SVC004: MetaDataManager initialization completed."));
    }
    
    /**
     * Get a list of domains in name.
     * @return List<Domain> List of Domain.
     * @exception ServiceException Thrown if an error occurs during processing.
     */
    public List<Domain> getDomains() throws ServiceException {
        
        List<Domain> domains = new ArrayList<Domain>();
        //TBD
        /*
        try {
            String[] domainNames = multiDomainMetaService.getDomains();
            for(String domain : domainNames) {
                domains.add(new Domain(domain));
            }
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        */
    	// demo data
        domains.add(new Domain("Person"));
        domains.add(new Domain("Company"));
        domains.add(new Domain("Product")); 
        domains.add(new Domain("Employee")); 
        domains.add(new Domain("Customer"));         
        domains.add(new Domain("Doctor"));                 
        domains.add(new Domain("Patient"));                         
        domains.add(new Domain("Hospital"));  
        domains.add(new Domain("Account"));          
        domains.add(new Domain("Order"));                  
        return domains;
    }
         
    /**
     * Get system information in string.
     * @return String System info
     * @throws ServiceException Thrown if an error occurs during processing.
     */
     public String getMuliDomainSystemInfo() throws ServiceException {
    	// demo data    	
    	return "mutli-domain service build 2.0 running on localhost:8080";
     }
        
    /**
     * Get a list of source system definition for the given domain.
     * @param domain Domain name.
     * @return List<SystemDefinition> List of SystemDefinition
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<SystemDefinition> getDomainSystemDefinition(String domain) throws ServiceException {
    	// demo data
    	SystemDefinition sd1 = new SystemDefinition();
    	sd1.setSystemCode("SystemA");
    	sd1.setDescription("Source system A");
    	sd1.setStatus("A");
    	sd1.setIdLength(20);
    	sd1.setFormat("[0-9A-Z]");
    	sd1.setInputMask("DDD-DDD-DDDD");
    	sd1.setValueMask("DDD^DDD^DDDD");
    	sd1.setCreateDate(new Date());
    	sd1.setCreateUserId("foo");
    	SystemDefinition sd2 = new SystemDefinition();
    	sd2.setSystemCode("SystemB");
    	sd2.setDescription("Source system B");
    	sd2.setStatus("A");
    	sd2.setIdLength(20);
    	sd2.setFormat("[0-9A-Z]");
    	sd2.setInputMask("DDD-DDD-DDDD");
    	sd2.setValueMask("DDD^DDD^DDDD");
    	sd2.setCreateDate(new Date());
    	sd2.setCreateUserId("foo");
    	ArrayList<SystemDefinition> systemDefinitions = new ArrayList<SystemDefinition>();    	
    	systemDefinitions.add(sd1);
    	systemDefinitions.add(sd2);    	
    	return systemDefinitions;
    }
    
    /**
     * Get a list of codes for the given domain.
     * @param domain Domain name.
     * @param module Module name.
     * @return List<String> List of codes
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<String> getCodes(String domain, String module) throws ServiceException {
        //TBD multidomain service API needs to provide.        
    	// SUFFIX,TITLE,GENDER,MSTATUS,RACE,ETHNIC,RELIGION,
        // LANGUAGE,NATIONAL,CITIZEN,ADDRTYPE,PHONETYPE    	
    	// demo data    	
    	String[] gender = new String[]{"F", "M"};     	 	
    	return Arrays.asList(gender);
    }
    
    /**
     * Get a map of codes for the given domain.
     * @param domain Domain name.
     * @return Map<String, List<String>> Map of codes
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public Map<String, List<String>> getCodeMap(String domain) throws ServiceException {
        //TBD multidomain service API needs to provide.        
        Map<String, List<String>> codeMap = new HashMap<String, List<String>>();
    	// SUFFIX,TITLE,GENDER,MSTATUS,RACE,ETHNIC,RELIGION,
        // LANGUAGE,NATIONAL,CITIZEN,ADDRTYPE,PHONETYPE    	
    	// demo data
        String[] gender = new String[]{"F", "M"};
    	codeMap.put("GENDER", Arrays.asList(gender));
    	return codeMap;
    }
    
    /**
     * Get a list of user codes for the given domain.
     * @param domain Domain name.
     * @param module Module name.
     * @return List<String> List of user codes.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<String> getUserCodes(String domain, String module) throws ServiceException {
        //TBD multidomain service API needs to provide.
        throw new ServiceException("Not Implemented Yet!");
    }
 
    /**
     * Get a map of user codes for the given domain.
     * @param domain Domain name.
     * @return Map<String, List<String>> List of user codes.
     * @throws ServiceException Thrown if an error occurs during processing.
     */   
    public Map<String, List<String>> getUserCodeMap(String domain) throws ServiceException {
        //TBD multidomain service API needs to provide.
    	throw new ServiceException("Not Implemented Yet!");
    }
}
