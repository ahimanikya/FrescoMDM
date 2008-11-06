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
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
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
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipObject;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefObject;
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
    private ArrayList<RelationshipDef> rts = new ArrayList<RelationshipDef>();    	

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
     * Add a new RelationshipDef.
     * @param relationshType RelationshipDef.
     * @return String RelationshipDef identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationshipDef(RelationshipDef RelationshipDef) 
        throws ServiceException {
        /* TBD
        String relationshId = null;
        try {
            relationshId = multiDomainMetaService.createRelationshipDef(RelationshipDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }          
        */   
        // demo data
        for (RelationshipDef rt:rts) {
            if (rt.getSourceDomain().equals(RelationshipDef.getSourceDomain()) &&
                rt.getTargetDomain().equals(RelationshipDef.getTargetDomain()) &&
                rt.getName().equals(RelationshipDef.getName())) {  
                throw new ServiceException("Invalid RelationshipDef:"  + 
                                           " source:" + RelationshipDef.getSourceDomain() +
                                           " target:" + RelationshipDef.getTargetDomain() +
                                           " name:" + RelationshipDef.getName());
            }
    	}       
        RelationshipDef.setId((int)System.currentTimeMillis());
        rts.add(RelationshipDef);     
        return Integer.toString(RelationshipDef.getId());
    }
    
    /**
     * Update an existing RelationshipDef.
     * @param RelationshipDef RelationshipDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationshipDef(RelationshipDef RelationshipDef) 
        throws ServiceException {
        /* TBD
        try {
            multiDomainMetaService.updateRelationshipDef(RelationshipDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        */         
        // demo data
        boolean updated = false;
        for (RelationshipDef rt:rts) {
            if (rt.getSourceDomain().equals(RelationshipDef.getSourceDomain()) &&
                rt.getTargetDomain().equals(RelationshipDef.getTargetDomain()) &&
                rt.getName().equals(RelationshipDef.getName())) {                      
                rt.copy(RelationshipDef);
                updated = true;
             }
    	}
        if (!updated) {
        throw new ServiceException("Invalid RelationshipDef:"  + 
                                   " source:" + RelationshipDef.getSourceDomain() +
                                   " target:" + RelationshipDef.getTargetDomain() +
                                   " name:" + RelationshipDef.getName());
        }
    }
    
    /**
     * Delete an existing  RelationshipDef.
     * @param relationshType RelationshipDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationshipDef(RelationshipDef RelationshipDef) 
        throws ServiceException {
        /* TBD
        try {
            multiDomainMetaService.deleteRelationshipDef(RelationshipDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }        
        */
        // demo data
        boolean deleted = false;
        ArrayList<RelationshipDef> temp = new ArrayList<RelationshipDef>();
        for (RelationshipDef rt:rts) {
            if (rt.getSourceDomain().equals(RelationshipDef.getSourceDomain()) &&
                rt.getTargetDomain().equals(RelationshipDef.getTargetDomain()) &&
                rt.getName().equals(RelationshipDef.getName())) {                      
                deleted = true;
             } else {
                temp.add(rt);
             }
    	}
        if (!deleted) {
        throw new ServiceException("Invalid RelationshipDef:"  + 
                                   " source:" + RelationshipDef.getSourceDomain() +
                                   " target:" + RelationshipDef.getTargetDomain() +
                                   " name:" + RelationshipDef.getName());
        }   
        rts = temp;
    }
    
    /**
     * Get a total count of relationship types for the given domain.
     * @param domain Domain name.
     * @return int Count of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipDefCount(String domain) throws ServiceException {
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipDef> List of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipDef> getRelationshipDefs(String domain) throws ServiceException {
        
        List<RelationshipDef> RelationshipDefs = new ArrayList<RelationshipDef>();
        /* TBD
        try {
            RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs();
            for (RelationshipDef relationship : relationships) {
                if(domain.equals(relationship.getSourceDomain()) ||
                   domain.equals(relationship.getTargetDomain())) {
                   RelationshipDefs.add(relationship); 
                }
            }
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }          
        */
    	// demo data	
    	for (RelationshipDef rt:rts) {
    		if (rt.getSourceDomain().equals(domain) || 
    			rt.getTargetDomain().equals(domain)) {
    			RelationshipDefs.add(rt);	
    		}
    	}
    	return RelationshipDefs;
    }
    
    /**
     * Get a list of RelationshipDefs by the given source domain and target domain.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipDef> List of RelationshipDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipDef> getRelationshipDefs(String sourceDomain, String targetDomain) throws ServiceException {
 
        List<RelationshipDef> RelationshipDefs = new ArrayList<RelationshipDef>();
        /* TBD
        try {
            RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs(sourceDomain, targetDomain);
            for (RelationshipDef relationship : relationships) {
                if(sourceDomain.equals(relationship.getSourceDomain()) ||
                   targetDomain.equals(relationship.getTargetDomain())) {
                   RelationshipDefs.add(relationship); 
                }
            }
         } catch(UserException uex) {
             throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        */             
        // demo data  	
    	for (RelationshipDef rt:rts) {
    		if (rt.getSourceDomain().equals(sourceDomain) && 
                    rt.getTargetDomain().equals(targetDomain)) {
    		    RelationshipDefs.add(rt);	
    		}
    	}
    	return RelationshipDefs;
    }
        
    private void init(){
    // demo data
    	RelationshipDef rt1 = new RelationshipDef();
    	rt1.setName("worksfor");
        rt1.setId(1);
        rt1.setSourceDomain("Person");
    	rt1.setTargetDomain("Company");    	
    	Attribute a1 = new Attribute("salary", "yearly income", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt1.setAttribute(a1);
    	
    	RelationshipDef rt2 = new RelationshipDef();
    	rt2.setName("employedby");
        rt2.setId(1);
        rt2.setSourceDomain("Person");
    	rt2.setTargetDomain("Company");    	
    	Attribute a2 = new Attribute("hiredDate", "hired date", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt2.setAttribute(a2);

    	RelationshipDef rt3 = new RelationshipDef();
        rt3.setName("contractwith");
        rt3.setId(1);
    	rt3.setSourceDomain("Person");
    	rt3.setTargetDomain("Company");    	
    	Attribute a3 = new Attribute("startDate", "date started", new AttributeType(AttributeType.DATE), "09/10/2008");
    	rt3.setAttribute(a3);
        
    	RelationshipDef rt4 = new RelationshipDef();
    	rt4.setName("investon");
        rt4.setId(2);
        rt4.setSourceDomain("Company");
    	rt4.setTargetDomain("Product");
    	Attribute a4 = new Attribute("invest", "total investment", new AttributeType(AttributeType.FLOAT), "500000.0");
    	rt4.setAttribute(a4);
    	
    	RelationshipDef rt5 = new RelationshipDef();
    	rt5.setName("designon");
        rt5.setId(3);
        rt5.setSourceDomain("Person");
    	rt5.setTargetDomain("Product");
    	Attribute a5 = new Attribute("location", "phyiscal location", new AttributeType(AttributeType.STRING), "Monrovia");
    	rt5.setAttribute(a5);
    	
        RelationshipDef rt6 = new RelationshipDef();
        rt6.setName("workon");
        rt6.setId(3);
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
    
    public List<DomainRelationshipDefObject> getDomainRelationshipDefObjects(String domain) 
        throws ServiceException {
        List<DomainRelationshipDefObject> types = new ArrayList<DomainRelationshipDefObject>();
        try {
            List<RelationshipDef> typeList = getRelationshipDefs(domain);
            for(RelationshipDef type : typeList) {
                String key = null;
                if (domain.equals(type.getSourceDomain())) {
                    key = type.getTargetDomain();
                } else if (domain.equals(type.getTargetDomain())) {
                    key = type.getSourceDomain();                        
                } 
                int index = types.indexOf(new DomainRelationshipDefObject(key));
                if(index == - 1) {
                  DomainRelationshipDefObject value = new DomainRelationshipDefObject(key);
                  types.add(value);  
                  index = types.indexOf(value);
                } 
                DomainRelationshipDefObject value = types.get(index);
                value.add(type);                
            }
        } catch(ServiceException sex) {
            throw sex;
        }
        return types;
    }
    
    public DomainRelationshipObject searchDomainRelationshipObjects(DomainSearch domainSearch)
        throws ServiceException {
        DomainRelationshipObject domainRelationshipObject  = new DomainRelationshipObject();
        try {
            MultiDomainSearchOption mdSearchOption = QueryBuilder.buildMultiDomainSearchOption(domainSearch);            
            MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();            
            //TBD mdSearchOptions.setDomainSearchOption(domainSearch.getName(),mdSearchOption);
            MultiDomainSearchCriteria mdSearchCriteria = new MultiDomainSearchCriteria();
            PageIterator<MultiObject> pages = multiDomainService.searchRelationships(mdSearchOptions, mdSearchCriteria);            
            domainRelationshipObject = ViewHelper.buildRelationshipView(pages, domainSearch.getName());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }        
        return domainRelationshipObject;
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
            relationships = ViewHelper.buildRelationshipView(pages, sourceDomainSearch.getName(), targetDomainSearch.getName(), relationshipSearch.getName());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        //return relationships;
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
             com.sun.mdm.multidomain.services.model.Attribute sf = sourceDomainSearch.next();
             System.out.println("Source => SearchField:" + sf.getName() + ":" + sf.getValue());
         }
                
         while(targetDomainSearch.hasNext()) {
             com.sun.mdm.multidomain.services.model.Attribute sf = targetDomainSearch.next();
             System.out.println("target => SearchField:" + sf.getName() + ":" + sf.getValue());
         }
         
         while(relationshipSearch.hasNext()) {
             com.sun.mdm.multidomain.services.model.Attribute sf = relationshipSearch.next();
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
        sr.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo1","Foo1"));
        rsc.setSourceRecord(sr);
        
        ObjectRecord tr = new ObjectRecord();
        tr.setName(relationshipView.getSourceDomain());
        tr.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo2","Foo2"));
        rsc.setTargetRecord(tr);
        
        RelationshipRecord rs = new RelationshipRecord();
        rs.setId(relationshipView.getId());
        rs.setName(relationshipView.getName());
        rs.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo3","Foo3"));
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
            // TBD: should return a lits of ObjectNode, EUID, and ComparisonScore.
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
        record.add(new com.sun.mdm.multidomain.services.model.Attribute("foo","foo"));
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
            multiDomainService.deleteRelationship(Integer.parseInt(relationshipView.getId()));
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
