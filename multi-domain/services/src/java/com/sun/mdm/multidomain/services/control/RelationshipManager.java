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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.multidomain.relationship.Attribute;
import com.sun.mdm.multidomain.relationship.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.core.MultiDomainService;
import com.sun.mdm.multidomain.services.core.ServiceLocator;
import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
import com.sun.mdm.multidomain.services.query.RelationshipObject;
import com.sun.mdm.multidomain.services.util.Localizer;

import com.sun.mdm.multidomain.services.util.ObjectBuilder;

/**
 * RelationshipManager class
 * @author cye
 */
public class RelationshipManager implements ServiceManager {
	private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.RelationshipManager");
	private static Localizer localizer = Localizer.getInstance();
		
	private MultiDomainService multiDomainService;
	
    /**
     * Create a instance of RelationshipManager.
     */
    public RelationshipManager () {   
    }

    /**
     * Create a instance of RelationshipManager with the given MultiDomainService. 
     * @param multiDomainService
     * @throws ServiceException
     */
    public RelationshipManager (MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
    }
    
    /**
     * Add a relationshipType.
     * @param relationshType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void addType(RelationshipType relationshipType) 
        throws ServiceException {
        throw new ServiceException("Not Implemented Yet");    	
    }
    
    /**
     * Update a relationshipType.
     * @param relationshType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateType(RelationshipType relationshType) 
        throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                
    }
    
    /**
     * Delete a RelationshipType.
     * @param relationshType
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void deleteType(RelationshipType relationshipType) 
        throws ServiceException {
        throw new ServiceException("Not Implemented Yet");                            	
    }
    
    /**
     * Get a total count of relationship types for the given domain.
     * @param domain
     * @return count of relationship type
     * @throws ServiceException
     */
    public int getRelationshipTypeCount(String domain) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain
     * @return list of relationship type
     * @throws ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String domain) throws ServiceException {
    	// demo data
    	RelationshipType rt1 = new RelationshipType("workfor", "a relationship of a Person works for a Company", "1");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	RelationshipType rt2 = new RelationshipType("investon", "a relationship of a Company invests on a Product", "2");
    	rt1.setSourceDomain("Company");
    	rt1.setTargetDomain("Product");    	    	
    	RelationshipType rt3 = new RelationshipType("designon", "a relationship of a Person designs a Product", "3");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Product");    	
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
     * Get a total count of relationship instances for the given relationship type.
     * @param relationshipType
     * @return count of relationship instances
     * @throws ServiceException
     */
    public int getRelationshipCount(RelationshipType relationshipType) throws ServiceException {
    	return 0;
    }
    
    /**
     * Get a list of relationship instances for the given relationship type.
     * @param relationshipType
     * @return list of relationship instances
     * @throws ServiceException
     */
    public List<Relationship> getRelationships(RelationshipType relationshipType) throws ServiceException {
    	List<Relationship> relationships = null;
    	return relationships;
    }

    /**
     * Get a list of RelationshipType for the given source domain and targetDomain.
     * @param sourceDomain
     * @param targetDomain 
     * @return a list of RelationshipType.
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipType> getRelationshipTypes(String sourceDomain, String targetDomain) 
        throws ServiceException {
    	// demo data
    	RelationshipType rt1 = new RelationshipType("workfor", "a relationship of a Person works for a Company", "1");
    	rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	Attribute a1 = new Attribute("salary", "yearly income", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt1.setAttribute(a1);
    	
    	RelationshipType rt2 = new RelationshipType("investon", "a relationship of a Company invests on a Product", "2");
    	rt2.setSourceDomain("Company");
    	rt2.setTargetDomain("Product");
    	Attribute a2 = new Attribute("invest", "total investment", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt2.setAttribute(a2);
    	
    	RelationshipType rt3 = new RelationshipType("designon", "a relationship of a Person designs a Product", "3");
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Product");
    	Attribute a3 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
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
     * Add a relationship instance.
     * @param sourceDomain
     * @param sourceEUID
     * @param targetDomain
     * @param targetEUID
     * @param relationTypeValue
     * @return relationship id
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                  Relationship relationTypeValue) 
        throws ServiceException {        
    	// demo data
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Add a relationship instance.
     * @param sourceDomain
     * @param sourceSystemCode
     * @param sourceLID
     * @param targetDomain
     * @param targetSystemCode
     * @param targetLID
     * @param relationTypeValue
     * @return relationship id
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public String addRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                  String targetDomain, String targetSystemCode, String targetLID,
                                  Relationship relationTypeValue) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Update an existing relationship instance.
     * @param sourceDomain
     * @param sourceEUID
     * @param targetDomain
     * @param targetEUID
     * @param relationTypeValue
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                   Relationship relationTypeValue) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Update an existing relationship instance.
     * @param sourceDomain
     * @param sourceSystemCode
     * @param sourceLID
     * @param targetDomain
     * @param targetSystemCode
     * @param targetLID
     * @param relationTypeValue
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void updateRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                   String targetDomain, String targetSystemCode, String targetLID,
                                   Relationship relationTypeValue) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }
    
    /**
     * Delete a relationship instance.
     * @param sourceDomain
     * @param sourceEUID
     * @param targetDomain
     * @param targetEUID
     * @param relationTypeValue
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void deleteRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                   Relationship relationTypeValue) 
        throws ServiceException {        
    	// demo data
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Delete a relationship instance.
     * @param sourceDomain
     * @param sourceSystemCode
     * @param sourceLID
     * @param targetDomain
     * @param targetSystemCode
     * @param targetLID
     * @param relationTypeValue
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public void deleteRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                   String targetDomain, String targetSystemCode, String targetLID,
                                   Relationship relationTypeValue) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }
    
    /**
     * Search relationship instances for the given query filter and query object.
     * @param searchOptions
     * @param searchCriteria
     * @return a list of relationship object
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipObject> searchRelationships(SearchOptions searchOptions, SearchCriteria searchCriteria)
        throws ServiceException {
    	// demo data
      	List<RelationshipObject> ros = new ArrayList<RelationshipObject>();    	
    	
      	// build ro1
    	RelationshipObject ro1 = new RelationshipObject();    	
    	Relationship  relationship1 = searchCriteria.getRelationship();
    	Map<String, String> sourceValues = new HashMap<String, String>();
    	sourceValues.put("Person.FirstName","sFoo1");
    	sourceValues.put("Person.LastName","sFoo1");    	
    	ObjectNode object1 = ObjectBuilder.createObjectNode("Person",sourceValues);
    	
    	Map<String, String> targetValues = new HashMap<String, String>();
    	targetValues.put("Person.FirstName","tFoo1");
    	targetValues.put("Person.LastName","tFoo1");    	    	
    	// Fix me Company
    	ObjectNode object2 = ObjectBuilder.createObjectNode("Person",targetValues);
    	ro1.setRelationship(relationship1);
    	ro1.setSourceObject(object1);
    	ro1.setTargetObject(object2);    	    	
      	ros.add(ro1);

      	// build ro2
    	RelationshipObject ro2 = new RelationshipObject();    	
    	Relationship  relationship2 = searchCriteria.getRelationship();
    	sourceValues = new HashMap<String, String>();
    	sourceValues.put("Person.FirstName","sFoo2");
    	sourceValues.put("Person.LastName","sFoo2");    	
    	object1 = ObjectBuilder.createObjectNode("Person",sourceValues);
    	targetValues = new HashMap<String, String>();
    	targetValues.put("Person.FirstName","tFoo2");
    	targetValues.put("Person.LastName","tFoo2");   
    	// Fix me Company    	
    	object2 = ObjectBuilder.createObjectNode("Person",targetValues);
    	ro2.setRelationship(relationship2);
    	ro2.setSourceObject(object1);
    	ro2.setTargetObject(object2);    	    	
      	ros.add(ro2);
      	
      	// build ro3
    	RelationshipObject ro3 = new RelationshipObject();    	
    	Relationship  relationship3 = searchCriteria.getRelationship();
    	sourceValues = new HashMap<String, String>();
    	sourceValues.put("Person.FirstName","sFoo3");
    	sourceValues.put("Person.LastName","sFoo3");    	
    	object1 = ObjectBuilder.createObjectNode("Person",sourceValues);
    	targetValues = new HashMap<String, String>();
    	targetValues.put("Person.FirstName","tFoo3");
    	targetValues.put("Person.LastName","tFoo3");   
    	// Fix me Company
    	object2 = ObjectBuilder.createObjectNode("Person",targetValues);
    	ro3.setRelationship(relationship3);
    	ro3.setSourceObject(object1);
    	ro3.setTargetObject(object2);    	    	
      	ros.add(ro3);
      	
    	return ros;
    }
    
    /**
     * Search relationship instances for the given query filter and query object.
     * @param sourceDomain
     * @param sourceCriteria
     * @param targetDomain
     * @param targetCriteria
     * @return a list of relationship object
     * @throws com.sun.mdm.multidomain.services.core.ServiceException
     */
    public List<RelationshipObject> searchRelationships(String sourceDomain, SearchOptions sourceCriteria, 
                                                        String targetDomain, SearchOptions targetCriteria)
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }   
}
