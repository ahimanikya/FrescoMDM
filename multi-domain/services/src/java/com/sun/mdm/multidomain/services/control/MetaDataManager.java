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
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Attribute;
import com.sun.mdm.multidomain.relationship.AttributeType;
import com.sun.mdm.multidomain.services.core.MultiDomainService;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.util.Localizer;

/**
 * MultiDomainManager
 * @author cye
 */
public class MetaDataManager {
	private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.MetaDataManager");
	private static Localizer localizer = Localizer.getInstance();

    public static final int METADATA_MANAGER = 0;	
    public static final int RELATIONSHIP_MANAGER = 1;
    public static final int HIERARCHY_MANAGER = 2;
    public static final int GROUP_MANAGER = 3;
    
    public enum ServiceManagerType {METADATA_MANAGER,RELATIONSHIP_MANAGER,HIERARCHY_MANAGER, GROUP_MANAGER};
        
    private MultiDomainService multiDomainService;
    
    /**
     * Create an instance of MetaDataManager.
     */
    public MetaDataManager() {   
    }
  
    /**
     * Create a instance of MetaDataManager with the given MultiDomainService. 
     * @param multiDomainService
     * @throws ServiceException
     */
    public MetaDataManager (MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    }
    
    /**
     * Get a list of domains in name.
     * @return ArrayList
     * @exception ServiceException
     */
    public List<String> getDomains() throws ServiceException {
    	// demo data
    	String[] domains = new String[]{"Person", "Company", "Product"}; 
        return Arrays.asList(domains);
    }
    
    /**
     * Get system information in string.
     * @return system info
     * @throws ServiceException
     */
    public String getSystemInfo() throws ServiceException {
    	// demo data    	
    	return "mutli-domain service build 2.0 running on localhost:8080";
    }
    
    /**
     * Get a list of RelationshipTypes
     * @return a list of RelationshipType.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipType> getRelationshipTypes() throws ServiceException {
    	// demo data
    	RelationshipType rt1 = new RelationshipType("workfor", "a relationship of a Person works for a Company", "1");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	RelationshipType rt2 = new RelationshipType("investon", "a relationship of a Company invests on a Product", "2");
    	rt2.setSourceDomain("Company");
    	rt2.setTargetDomain("Product");    	    	
    	RelationshipType rt3 = new RelationshipType("designon", "a relationship of a Person designs a Product", "3");
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Product");    	    	
    	ArrayList<RelationshipType> relationshipTypes = new ArrayList<RelationshipType>();
    	relationshipTypes.add(rt1);
    	relationshipTypes.add(rt2);
    	relationshipTypes.add(rt3);    	
        return relationshipTypes;
    }
    
    /**
     * Get a list of RelationshipTypes by the given domain.
     * @param domain
     * @return a list of RelationshipType.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String domain) throws ServiceException {
    	// demo data
    	RelationshipType rt1 = new RelationshipType("workfor", "a relationship of a Person works for a Company", "1");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	RelationshipType rt2 = new RelationshipType("investon", "a relationship of a Company invests on a Product", "2");
    	rt2.setSourceDomain("Company");
    	rt2.setTargetDomain("Product");    	    	
    	RelationshipType rt3 = new RelationshipType("designon", "a relationship of a Person designs a Product", "3");
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Product");    	
    	ArrayList<RelationshipType> rts = new ArrayList<RelationshipType>();    	
    	rts.add(rt1);
    	rts.add(rt2);
    	rts.add(rt3);    	
    	ArrayList<RelationshipType> relationshipTypes = new ArrayList<RelationshipType>();    	
    	for (RelationshipType rt:rts) {
    		if (rt.getSourceDomain().equals(domain) || 
    			rt.getTargetDomain().equals(domain)) {
    			relationshipTypes.add(rt);	
    		}
    	}
    	return relationshipTypes;
    }
  
    /**
     * Get a list of RelationshipTypes by the given source domain and target domain.
     * @param domain
     * @return a list of RelationshipType.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String sourceDomain, String targetDomain) throws ServiceException {
    	// demo data
    	RelationshipType rt1 = new RelationshipType("workfor", "a relationship of a Person works for a Company", "1");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	Attribute a1 = new Attribute("salary", "yearly income", AttributeType.FLOAT, "500000.0");
    	rt1.setAttribute(a1);
    	
    	RelationshipType rt2 = new RelationshipType("investon", "a relationship of a Company invests on a Product", "2");
    	rt2.setSourceDomain("Company");
    	rt2.setTargetDomain("Product");
    	Attribute a2 = new Attribute("invest", "total investment", AttributeType.FLOAT, "500000.0");
    	rt2.setAttribute(a2);
    	
    	RelationshipType rt3 = new RelationshipType("designon", "a relationship of a Person designs a Product", "3");
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Product");
    	Attribute a3 = new Attribute("location", "phyiscal location", AttributeType.STRING, "Monrovia");
    	rt3.setAttribute(a3);
    	
    	ArrayList<RelationshipType> rts = new ArrayList<RelationshipType>();    	
    	rts.add(rt1);
    	rts.add(rt2);
    	rts.add(rt3);    	
    	ArrayList<RelationshipType> relationshipTypes = new ArrayList<RelationshipType>();    	
    	for (RelationshipType rt:rts) {
    		if (rt.getSourceDomain().equals(sourceDomain) && 
    			rt.getTargetDomain().equals(targetDomain)) {
    			relationshipTypes.add(rt);	
    		}
    	}
    	return relationshipTypes;
    }
    
    /**
     * Get a list of source system definitions for the given domain.
     * @param domain
     * @return a list of SystemDefinition
     * @throws ServiceException
     */
    public List<SystemDefinition> getSystemDefinitions(String domain) throws ServiceException {
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
     * @param domain
     * @param module
     * @return a list of codes
     * @throws ServiceException
     */
    public List<String> getCodes(String domain, String module) throws ServiceException {
    	// demo data SUFFIX
    	// demo data TITLE    	
    	// demo data GENDER
    	String[] gender = new String[]{"F", "M"};     	
    	// demo data MSTATUS
    	// demo data RACE
    	// demo data ETHNIC
    	// demo data RELIGION
    	// demo data LANGUAGE
    	// demo data NATIONAL
    	// demo data CITIZEN
    	// demo data ADDRTYPE
    	// demo data PHONETYPE    	
    	return Arrays.asList(gender);
    }
    
    /**
     * Get a map of codes for the given domain.
     * @param domain
     * @return a map of codes
     * @throws ServiceException
     */
    public Map<String, List<String>> getCodeMap(String domain) throws ServiceException {
    	Map<String, List<String>> codeMap = new HashMap<String, List<String>>();
    	// demo data SUFFIX
    	// demo data TITLE    	
    	// demo data GENDER
    	String[] gender = new String[]{"F", "M"};     	
    	// demo data MSTATUS
    	// demo data RACE
    	// demo data ETHNIC
    	// demo data RELIGION
    	// demo data LANGUAGE
    	// demo data NATIONAL
    	// demo data CITIZEN
    	// demo data ADDRTYPE
    	// demo data PHONETYPE
    	codeMap.put("GENDER", Arrays.asList(gender));
    	return codeMap;
    }
    
    /**
     * Get a list of user codes for the given domain.
     * @param domain
     * @param module
     * @return a list of user codes
     * @throws ServiceException
     */
    public List<String> getUserCodes(String domain, String module) throws ServiceException {
    	List<String> codes = null;
    	return codes;
    }
 
    /**
     * Get a map of user codes for the given domain.
     * @param domain
     * @param module
     * @return a list of user codes
     * @throws ServiceException
     */   
    public Map<String, List<String>> getUserCodeMap(String domain) throws ServiceException {
    	Map<String, List<String>> codeMap = null;
    	return codeMap;
    }
    
    /**
     * Get a system object for the given localId from the given source system of the domain. 
     * @param domain
     * @param systemCode
     * @param LocalId
     * @return system object
     * @throws ServiceException
     */
    public SystemObject getSystemObject(String domain, String systemCode, String LocalId) throws ServiceException {
    	SystemObject systemObject = null;
    	return systemObject;
    }
    
    /**
     * Query enterpriseObjects based the given search criteria and search options from the domain.
     * @param domain
     * @param eoSearchCriteria
     * @param eoSearchOptions
     * @return a list of EnterpriseObject
     * @throws ServiceException
     */
    public List<EnterpriseObject> searchEnterpriseObject(String domain, EOSearchCriteria eoSearchCriteria, EOSearchOptions eoSearchOptions)throws ServiceException {
    	List<EnterpriseObject> enterpriseObjects = null;
    	// demo data
    	return enterpriseObjects;
    }
    
    /**
     * Get a enterprise object for the given euid from the domain.
     * @param domain
     * @param euid
     * @return enterprise object
     * @throws ServiceException
     */
    public EnterpriseObject getEnterpriseObject(String domain, String euid) throws ServiceException {
    	EnterpriseObject enterpriseObject = null;
    	return enterpriseObject;
    }
    
    /**
     * Get a enterprise object for the given system obecjt from the domain.
     * @param domain
     * @param systemObject
     * @return enterprise object
     * @throws ServiceException
     */
    public EnterpriseObject getEnterpriseObject(String domain, SystemObject systemObject) throws ServiceException {
    	EnterpriseObject enterpriseObject = null;
    	return enterpriseObject;
    }
    
}
