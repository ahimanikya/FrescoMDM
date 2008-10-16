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

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.multidomain.association.Attribute;
import com.sun.mdm.multidomain.association.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.MultiObject.RelationshipObject;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.query.SearchCriteria;
import com.sun.mdm.multidomain.services.query.SearchOptions;
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
    private MultiDomainMetaService multiDomainMetaService;

    // demo data
    private ArrayList<RelationshipType> rts = new ArrayList<RelationshipType>();    	

    /**
     * Create a instance of RelationshipManager.
     */
    public RelationshipManager () {   
        init();
    }

    /**
     * Create a instance of RelationshipManager with the given MultiDomainMetaService and MultiDomainService.
     * @param multiDomainMetaService MultiDomainMetaService.
     * @param multiDomainService MultiDomainService.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public RelationshipManager (MultiDomainMetaService multiDomainMetaService, MultiDomainService multiDomainService) 
    	throws ServiceException {
    	this.multiDomainService = multiDomainService;
        this.multiDomainMetaService = multiDomainMetaService; 
        init();
    }
    
    /**
     * Add a new relationshipType.
     * @param relationshType RelationshipType.
     * @return String RelationshipType identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addType(RelationshipType relationshipType) 
        throws ServiceException {
        // demo data
        for (RelationshipType rt:rts) {
            if (rt.getSourceDomain().equals(relationshipType.getSourceDomain()) &&
                rt.getTargetDomain().equals(relationshipType.getTargetDomain()) &&
                rt.getName().equals(relationshipType.getName())) {  
                throw new ServiceException("Invalid RelationshipType:"  + 
                                           " source:" + relationshipType.getSourceDomain() +
                                           " target:" + relationshipType.getTargetDomain() +
                                           " name:" + relationshipType.getName());
            }
    	}       
        relationshipType.setId(Long.toString(System.currentTimeMillis()));
        rts.add(relationshipType);     
        return relationshipType.getId();
    }
    
    /**
     * Update an existing relationshipType.
     * @param relationshipType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateType(RelationshipType relationshipType) 
        throws ServiceException {
        // demo data
        boolean updated = false;
        for (RelationshipType rt:rts) {
            if (rt.getSourceDomain().equals(relationshipType.getSourceDomain()) &&
                rt.getTargetDomain().equals(relationshipType.getTargetDomain()) &&
                rt.getName().equals(relationshipType.getName())) {                      
                rt.copy(relationshipType);
                updated = true;
             }
    	}
        if (!updated) {
        throw new ServiceException("Invalid RelationshipType:"  + 
                                   " source:" + relationshipType.getSourceDomain() +
                                   " target:" + relationshipType.getTargetDomain() +
                                   " name:" + relationshipType.getName());
        }
    }
    
    /**
     * Delete an existing  relationshipType.
     * @param relationshType RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteType(RelationshipType relationshipType) 
        throws ServiceException {
        // demo data
        boolean deleted = false;
        ArrayList<RelationshipType> temp = new ArrayList<RelationshipType>();
        for (RelationshipType rt:rts) {
            if (rt.getSourceDomain().equals(relationshipType.getSourceDomain()) &&
                rt.getTargetDomain().equals(relationshipType.getTargetDomain()) &&
                rt.getName().equals(relationshipType.getName())) {                      
                deleted = true;
             } else {
                temp.add(rt);
             }
    	}
        if (!deleted) {
        throw new ServiceException("Invalid RelationshipType:"  + 
                                   " source:" + relationshipType.getSourceDomain() +
                                   " target:" + relationshipType.getTargetDomain() +
                                   " name:" + relationshipType.getName());
        }   
        rts = temp;
    }
    
    /**
     * Get a total count of relationship types for the given domain.
     * @param domain Domain name.
     * @return int Count of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getTypeCount(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipType> List of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipType> getTypes(String domain) throws ServiceException {
    	// demo data
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
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipType> List of RelationshipType.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipType> getRelationshipTypes(String sourceDomain, String targetDomain) throws ServiceException {
    	// demo data
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
     * Get a total count of relationship instances for the given relationship type.
     * @param relationshipType RelationshipType.
     * @return int Count of relationship instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipCount(RelationshipType relationshipType) throws ServiceException {
        // demo data
    	return 3;
    }
    
    /**
     * Get a list of relationship instances for the given relationship type.
     * @param relationshipType RelationshipType.
     * @return List<Relationship> List of relationship instances.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<Relationship> getRelationships(RelationshipType relationshipType) throws ServiceException {
    	List<Relationship> relationships = null;
    	return relationships;
    }
    
    /**
     * Add a relationship instance for the given source entity EUID and target entity EUID.
     * @param sourceDomain Source domain name.
     * @param sourceEUID Source entity EUID.
     * @param targetDomain Target domain name.
     * @param targetEUID Target entity EUID.
     * @param relationship Relationship instance.
     * @return String Relationship identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                  Relationship relationship) 
        throws ServiceException {        
    	// demo data
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Add a relationship instance for the given source entity localId and target entity localId.
     * @param sourceDomain Source domain name.
     * @param sourceSystemCode Source domain system code.
     * @param sourceLID Source domain entity localId.
     * @param targetDomain Target domain name.
     * @param targetSystemCode Target domain system code.
     * @param targetLID Target domain entity localId.
     * @param relationship Relationship instance.
     * @return String Relationship identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                  String targetDomain, String targetSystemCode, String targetLID,
                                  Relationship relationship) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Update an existing relationship instance for the given source entity EUID and target entity EUID.
     * @param sourceDomain Source domain name.
     * @param sourceEUID Source entity EUID.
     * @param targetDomain Target domain name.
     * @param targetEUID Target entity EUID.
     * @param relationship Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                   Relationship relationship) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Update an existing relationship instance for the give source entity localId and target entity localId.
     * @param sourceDomain Source domain name.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source entity localId.
     * @param targetDomain Target domain name.
     * @param targetSystemCode Target system code.
     * @param targetLID Target entity localId.
     * @param relationship Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                   String targetDomain, String targetSystemCode, String targetLID,
                                   Relationship relationship) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }
    
    /**
     * Delete a relationship instance for the given source entity EUID and target entity EUID.
     * @param sourceDomain Source domain name.
     * @param sourceEUID Source entity EUID.
     * @param targetDomain Target domain name.
     * @param targetEUID Target entity EUID.
     * @param relationship Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationship(String sourceDomain, String sourceEUID, String targetDomain, String targetEUID, 
                                   Relationship relationship) 
        throws ServiceException {        
    	// demo data
        throw new ServiceException("Not Implemented Yet");                        
    }

    /**
     * Delete a relationship instance for the source entity localId and target entity localId.
     * @param sourceDomain Source domain name.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source entity localId.
     * @param targetDomain Target domain name.
     * @param targetSystemCode Target system code.
     * @param targetLID Target entity localId.
     * @param relationship Relationship.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationship(String sourceDomain, String sourceSystemCode, String sourceLID, 
                                   String targetDomain, String targetSystemCode, String targetLID,
                                   Relationship relationship) 
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }
    
    /**
     * Search relationship instances for the given query filter and query object.
     * @param searchOptions
     * @param searchCriteria
     * @return a list of relationship object
     * @throws ServiceException Thrown if an error occurs during processing.
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
    	//fixme ro1.setSourceObject(object1);
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
    	//fixme ro2.setSourceObject(object1);
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
    	//fixme ro3.setSourceObject(object1);
    	ro3.setTargetObject(object2);    	    	
      	ros.add(ro3);
      	
    	return ros;
    }
    
    /**
     * Search relationship instances for the given query options and query criteria.
     * @param sourceDomain Source domain name.
     * @param sourceCriteria Source search criteria.
     * @param targetDomain Target domain name.
     * @param targetCriteria target search criteria.
     * @return List<RelationshipObject> List of relationship object.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipObject> searchRelationships(String sourceDomain, SearchOptions sourceCriteria, 
                                                        String targetDomain, SearchOptions targetCriteria)
        throws ServiceException {        
        throw new ServiceException("Not Implemented Yet");                        
    }   
    
    private void init() {
    	// demo data
    	RelationshipType rt1 = new RelationshipType();
    	rt1.setName("worksfor");
        rt1.setId("1");
        rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	Attribute a1 = new Attribute("salary", "yearly income", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt1.setAttribute(a1);
    	
    	RelationshipType rt2 = new RelationshipType();
    	rt2.setName("employedby");
        rt2.setId("1");
        rt2.setSourceDomain("Person");
    	rt2.setTargetDomain("Company");    	
    	Attribute a2 = new Attribute("hiredDate", "hired date", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt2.setAttribute(a2);

    	RelationshipType rt3 = new RelationshipType();
        rt3.setName("contractwith");
        rt3.setId("1");
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Company");    	
    	Attribute a3 = new Attribute("startDate", "date started", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt3.setAttribute(a3);
        
    	RelationshipType rt4 = new RelationshipType();
    	rt4.setName("investon");
        rt4.setId("2");
        rt4.setSourceDomain("Company");
    	rt4.setTargetDomain("Product");
    	Attribute a4 = new Attribute("invest", "total investment", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt4.setAttribute(a4);
    	
    	RelationshipType rt5 = new RelationshipType();
    	rt5.setName("designon");
        rt5.setId("3");
        rt5.setSourceDomain("Person");
    	rt5.setTargetDomain("Product");
    	Attribute a5 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
    	rt5.setAttribute(a5);
    	
        RelationshipType rt6 = new RelationshipType();
        rt6.setName("workon");
        rt6.setId("3");
    	rt6.setSourceDomain("Person");
    	rt6.setTargetDomain("Product");
    	Attribute a6 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
    	rt6.setAttribute(a6);    	       
    	rts.add(rt1);
    	rts.add(rt2);
    	rts.add(rt3);    
        rts.add(rt4);   
        rts.add(rt5);   
        rts.add(rt6);                           
    }
}
