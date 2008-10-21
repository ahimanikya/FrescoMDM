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

import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.ObjectNode;
 import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;
import com.sun.mdm.multidomain.association.Attribute;
import com.sun.mdm.multidomain.association.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.relationship.Relationship;    
import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.query.PageIterator;
  
import com.sun.mdm.multidomain.services.core.ViewHelper;
import com.sun.mdm.multidomain.services.core.QueryBuilder;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.model.MultiDomainSearchOption;
import com.sun.mdm.multidomain.services.model.RelationshipSearch;
import com.sun.mdm.multidomain.services.model.Field;
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.util.Localizer;
      
/**
 * RelationshipManager class
 * @author cye
 */
public class RelationshipManager {
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
        
    private void init(){
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
    
    public List<RelationshipView> searchRelationships(DomainSearch sourceDomainSearch, 
                                                      DomainSearch targetDomainSearch, 
                                                      RelationshipSearch relationshipSearch) 
        throws ServiceException {
        
        List<RelationshipView> relationships = new ArrayList<RelationshipView>(); 
        /* TBD
        try {
             // build search options and criteria for source and target
            MultiDomainSearchOptions mdSearchOptions = QueryBuilder.buildMultiDomainSearchOptions(sourceDomainSearch, targetDomainSearch);
            MultiDomainSearchCriteria mdSearchCriteria = QueryBuilder.buildMultiDomainSearchCriteria(sourceDomainSearch, targetDomainSearch, relationshipSearch);
            PageIterator<MultiObject> pages = multiDomainService.searchRelationships(mdSearchOptions, mdSearchCriteria);
            relationships = ViewHelper.buildRelationshipView(pages);
        } catch (ConfigException cex) 
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        return relationships;
        */  
        //demo
        RelationshipView rs1 = new RelationshipView();
        rs1.setSourceDomain(sourceDomainSearch.getName());
        rs1.setTargetDomain(targetDomainSearch.getName());
        rs1.setName(relationshipSearch.getName());
        rs1.setId("relationshipId");
        rs1.setSourceEUID("sourceEUID");
        rs1.setTargetEUID("targetEUID");
        rs1.setSourceHighLight("sourceHighLight");
        rs1.setTargetHighLight("targetHighLight");
        relationships.add(rs1);
        
         while(sourceDomainSearch.hasNext()) {
             Field sf = sourceDomainSearch.next();
             System.out.println("Source => SearchField:" + sf.getName() + ":" + sf.getValue());
         }
                
         while(targetDomainSearch.hasNext()) {
             Field sf = targetDomainSearch.next();
             System.out.println("target => SearchField:" + sf.getName() + ":" + sf.getValue());
         }
         
         while(relationshipSearch.hasNext()) {
             Field sf = relationshipSearch.next();
             System.out.println("relationship => SearchField:" + sf.getName() + ":" + sf.getValue());
         }
        
        RelationshipView rs2 = new RelationshipView();
        rs2.setSourceDomain(sourceDomainSearch.getName());
        rs2.setTargetDomain(targetDomainSearch.getName());
        rs2.setName(relationshipSearch.getName());
        rs2.setId("relationshipId");
        rs2.setSourceEUID("sourceEUID");
        rs2.setTargetEUID("targetEUID");
        rs2.setSourceHighLight("sourceHighLight");
        rs2.setTargetHighLight("targetHighLight");        
        relationships.add(rs2);
        
        
        return relationships; 
    }
    public RelationshipComposite getRelationship(RelationshipView relationshipView)
        throws ServiceException {
      
        RelationshipComposite relationshipComposite = new RelationshipComposite();
        /* TBD
        try {
             // build search options and criteria for source and target
            Relationship relationship = new Relationship();
            relationship.setRelationshipID(relationshipView.getId());
            relationship.setSourceEUID(relationshipView.getSourceEUID());
            relationship.setTargetEUID(relationshipView.getTargetEUID());
            // need to add a new method in multiDomainService to getRelationship
            MultiObject relationshipObject = multiDomainService.getRelationship(relationship);
            relationshipComposite = ViewHelper.buildRelationshipComposite(relationshipObject);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        return relationshipComposite;        
        */
        
        //demo
        System.out.println(relationshipView.getSourceDomain() + ":" + relationshipView.getTargetDomain());
        
        RelationshipComposite rsc = new RelationshipComposite();
        
        ObjectRecord sr = new ObjectRecord();
        sr.setName(relationshipView.getSourceDomain());
        sr.add(new Field("Foo1","Foo1"));
        rsc.setSourceRecord(sr);
        
        ObjectRecord tr = new ObjectRecord();
        tr.setName(relationshipView.getSourceDomain());
        tr.add(new Field("Foo2","Foo2"));
        rsc.setTargetRecord(tr);
        
        RelationshipRecord rs = new RelationshipRecord();
        rs.setId(relationshipView.getId());
        rs.setName(relationshipView.getName());
        rs.add(new com.sun.mdm.multidomain.services.relationship.Attribute("Foo3","Foo3"));
        rsc.setRelationshipRecord(rs);
        return rsc;
    }
    public List<ObjectView> searchEnterprises(DomainSearch domainSearch)
        throws ServiceException {
           
        List<ObjectView> objects = new ArrayList<ObjectView>();
        
        try {
             // build search options and criteria for source and target
            EOSearchOptions eoSearchOptions = QueryBuilder.buildEOSearchOptions(domainSearch);
            EOSearchCriteria eoSearchCriteria = QueryBuilder.buildEOSearchCriteria(domainSearch);
            PageIterator<ObjectNode> pages = multiDomainService.searchEnterprises(domainSearch.getName(), eoSearchOptions, eoSearchCriteria);
            objects = ViewHelper.buildObjectView(pages, eoSearchOptions.isWeighted());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        
        //demo
        objects.add(new ObjectView("foo","000-000-0000", "I am foo"));
        objects.add(new ObjectView("foo","000-000-0001", "I am foo too"));
        return objects;
    } 
    public ObjectRecord getEnterprise(ObjectView object)
        throws ServiceException {
        /* TBD
        ObjectRecord objectRecord = null;
        try {           
            // need to add a new method in multiDomainService to getObject
            ObjectNode objectNoe = multiDomainService.getObject(object.getName(), object.getEUID());
            objectRecord = ViewHelper.buildObjectRecord(objectNoe);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        return objectRecord;        
        */
        //demo
        ObjectRecord record = new ObjectRecord(object.getName(), object.getEUID());
        record.add(new Field("foo","foo"));
        return record;
    }  
    public String addRelationship(RelationshipRecord relationshipRecord)
        throws ServiceException { 
        /* TBD
        String id = null;
        try {
            Relationship relationship = QueryBuilder.buildRelationship(relationshipRecord); 
            id = multiDomainService.createRelationship(relationship);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }  
        */
        //demo
        return relationshipRecord.toString();  
    }
    public void deleteRelationship(RelationshipView relationshipView)
        throws ServiceException {
         try {
            multiDomainService.deleteRelationship(relationshipView.getId());
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }            
    }
    public void updateRelationship(RelationshipRecord relationshipRecord)
        throws ServiceException {
        try {
            Relationship relationship = QueryBuilder.buildRelationship(relationshipRecord);
            multiDomainService.updateRelationship(relationship);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }        
    }    
}
