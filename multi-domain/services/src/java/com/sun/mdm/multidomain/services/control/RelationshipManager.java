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
import java.util.Arrays;
        
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.ObjectNode;
 import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.ejb.service.MultiDomainMetaService;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.relationship.Relationship;    
import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions.DomainSearchOption;
         
import com.sun.mdm.multidomain.services.core.ViewBuilder;
import com.sun.mdm.multidomain.services.core.QueryBuilder;
import com.sun.mdm.multidomain.services.model.DomainSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipsObject;
import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;
import com.sun.mdm.multidomain.services.relationship.RelationshipDefView;
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.relationship.RelationshipMoveComposite;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipsObject;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefsObject;
import com.sun.mdm.multidomain.services.core.ServiceException;
import com.sun.mdm.multidomain.services.core.ConfigException;
import com.sun.mdm.multidomain.services.util.Localizer;      
import com.sun.mdm.multidomain.services.configuration.MDConfigManager;

/**
 * RelationshipManager class
 * @author cye
 */
public class RelationshipManager {
    private static Logger logger = Logger.getLogger("com.sun.mdm.multidomain.services.control.RelationshipManager");
    private static Localizer localizer = Localizer.getInstance();
		
    private MultiDomainService multiDomainService;
    private MultiDomainMetaService multiDomainMetaService;

    private boolean TBD = true;

    /**
     * Create a instance of RelationshipManager.
     */
    public RelationshipManager () {   
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
        logger.info(localizer.x("SVC007: RelationshipManager initialization completed."));                
    }
    
    /**
     * Add a new RelationshipDef.
     * @param rDefExt RelationshipDefExt.
     * @return String RelationshipDef identifier which is newly added.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {
        
        long relationshId = -1;
        try {
            RelationshipDef rDef = ViewBuilder.buildRelationshipDef(rDefExt);
            relationshId = multiDomainMetaService.createRelationshipDef(rDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        } 
        return Long.toString(relationshId);
    }
    
    /**
     * Update an existing RelationshipDef.
     * @param rDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {
        try {
            RelationshipDef rDef = ViewBuilder.buildRelationshipDef(rDefExt);
            multiDomainMetaService.updateRelationshipDef(rDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
    }
    
    /**
     * Delete an existing RelationshipDef.
     * @param rDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationshipDef(RelationshipDefExt rDefExt) 
        throws ServiceException {
        try {
            multiDomainMetaService.deleteRelationshipDef(ViewBuilder.buildRelationshipDef(rDefExt));
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }        
    }
    
    /**
     * Get a total count of relationship types for the given domain.
     * @param domain Domain name.
     * @return int Count of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public int getRelationshipDefCount(String domain) throws ServiceException {
        //TBD multidomain service API needs to provide a method.
        throw new ServiceException("Not Implemented Yet");
    }
    
    /**
     * Get Relationship definition for the given name and source domains and target domain.
     * @param name RelationshipDef name.
     * @param sourceDomain Source domain.
     * @param targetDomain Target domain.
     * @return RelationshipDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */    
    public RelationshipDefExt getRelationshipDefByName(String name, String sourceDomain, String targetDomain) 
        throws ServiceException {
        RelationshipDefExt rDefExt = null; 
        try {
            // multiDomainMetaService.getRelationshipDefByName(name, sourceDomain, targetDomain);
            RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs(sourceDomain, targetDomain); 
            if (relationships != null) {
                for (RelationshipDef rDef : relationships) {
                    if (rDef.getName().equals(name)) {
                        rDefExt = ViewBuilder.buildRelationshipDefExt(rDef);   
                        break;
                    }
                }
            }
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        return rDefExt;
    }
 
     /**
     * Get Relationship definition for the given relationship Id.
     * @param relationshipDefId RelationshipDef Identifier.
     * @return RelationshipDefExt RelationshipDefExt.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
     public RelationshipDefExt getRelationshipDefById(long relationshipDefId) 
            throws ServiceException {
        RelationshipDefExt rDefExt = null;
        try {
            RelationshipDef rDef = multiDomainMetaService.getRelationshipDefById(relationshipDefId);
            rDefExt = ViewBuilder.buildRelationshipDefExt(rDef);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
        return rDefExt;               
     }  
    
    /**
     * Get a list of relationship types for the given domain.
     * @param domain Domain name.
     * @return List<RelationshipDefExt> List of relationship type.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipDefExt> getRelationshipDefs(String domain) 
       throws ServiceException {       
        List<RelationshipDefExt> hDefs = new ArrayList<RelationshipDefExt>();
        try {
            RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs();
            for (int i = 0; i < relationships.length; i++) {
                hDefs.add(ViewBuilder.buildRelationshipDefExt(relationships[i]));
            }            
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }  
        return hDefs;
    }
    
    /**
     * Get a list of RelationshipDefs by the given source domain and target domain.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return List<RelationshipDefExt> List of RelationshipDef.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipDefExt> getRelationshipDefs(String sourceDomain, String targetDomain) 
        throws ServiceException {
 
        List<RelationshipDefExt> relationshipDefs = new ArrayList<RelationshipDefExt>();
        try {
            RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs(sourceDomain, targetDomain);
            for (RelationshipDef relationship : relationships) {
                if(sourceDomain.equals(relationship.getSourceDomain()) ||
                   targetDomain.equals(relationship.getTargetDomain())) {                    
                   relationshipDefs.add(ViewBuilder.buildRelationshipDefExt(relationship)); 
                }
            }
         } catch(UserException uex) {
             throw new ServiceException(uex);
        } catch(ProcessingException pex) {
            throw new ServiceException(pex);
        }
    	return relationshipDefs;
    }
          
    /**
     *  Get a list of DomainRelationshipDefObject for the given domain.
     * @param domain Domain name.
     * @return List<DomainRelationshipDefObject> List of DomainRelationshipDefObject.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<DomainRelationshipDefsObject> getDomainRelationshipDefsObjects(String domain) 
        throws ServiceException {
        List<DomainRelationshipDefsObject> types = new ArrayList<DomainRelationshipDefsObject>();
        try {
            List<RelationshipDefExt> typeList = getRelationshipDefs(domain);
            for(RelationshipDefExt type : typeList) {
                String key = null;
                if (domain.equals(type.getSourceDomain())) {
                    key = type.getTargetDomain();
                } else if (domain.equals(type.getTargetDomain())) {
                    key = type.getSourceDomain();                        
                } 
                int index = types.indexOf(new DomainRelationshipDefsObject(key));
                if(index == - 1) {
                  DomainRelationshipDefsObject value = new DomainRelationshipDefsObject(key);
                  types.add(value);  
                  index = types.indexOf(value);
                } 
                DomainRelationshipDefsObject value = types.get(index);
                value.add(type);                
            }
        } catch(ServiceException sex) {
            throw sex;
        }
        return types;
    }
    
    /**
     * Search relationshipsByRecord for the given domain search.
     * @param domainSearch Domain search.
     * @return DomainRelationshipObject DomainRelationshipObject.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public DomainRelationshipsObject searchRelationshipsByRecord(DomainSearch domainSearch)
        throws ServiceException {
        DomainRelationshipsObject domainRelationshipsObject  = new DomainRelationshipsObject();
        
        //TDB
        domainSearch = new DomainSearch();
        domainSearch.setName("Person");
        domainSearch.setType("Advanced Person Lookup (Alpha)");
        domainSearch.setAttributeValue("Person.FirstName", "George");
        domainSearch.setAttributeValue("Person.LastName", "Denise");
        domainSearch.setAttributeValue("Person.SSN", "888888888");
        domainSearch.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
        domainSearch.setAttributeValue("Person.Address.City", "Foo");
        
        if(!TBD) {
        try {
            DomainSearchOption mdSearchOption = QueryBuilder.buildMultiDomainSearchOption(domainSearch);            
            MultiDomainSearchOptions mdSearchOptions = new MultiDomainSearchOptions();            
            mdSearchOptions.setOptions(domainSearch.getName(),mdSearchOption);
            MultiDomainSearchCriteria mdSearchCriteria = new MultiDomainSearchCriteria();
            PageIterator<MultiObject> pages = multiDomainService.searchRelationships(mdSearchOptions, mdSearchCriteria);            
            domainRelationshipsObject = ViewBuilder.buildRelationshipView(pages, domainSearch.getName());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }        
        }
        
        //TBD
        domainRelationshipsObject.setDomain("Person");
        
        ObjectView personView = new ObjectView();
        personView.setName("Person");
        personView.setHighLight("John McCain");
        personView.setEUID("0000000011");
        domainRelationshipsObject.setPrimaryObject(personView);
                        
        RelationshipsObject relsObject1 = new RelationshipsObject();        
        RelationshipDefView relDef1 = new RelationshipDefView();
        relDef1.setName("EmployedBy");
        relDef1.setSourceDomain("Person");
        relDef1.setTargetDomain("Company");
        relDef1.setBiDirection(true) ;
        relDef1.setId("01");              
        relsObject1.setRelationshipDefView(relDef1);        
        
        RelationshipView rel1 = new RelationshipView();
        rel1.setId("01");
        rel1.setName("EmployedBy");
        rel1.setSourceDomain("Person");
        rel1.setTargetDomain("Company");
        rel1.setSourceEUID("0000000001");
        rel1.setTargetEUID("0000000002");
        rel1.setSourceHighLight("John McCain");
        rel1.setTargetHighLight("Sun Microsystems Java");        
        relsObject1.setRelationshipView(rel1);

        RelationshipView rel2 = new RelationshipView();
        rel2.setId("01");
        rel2.setName("EmployedBy");
        rel2.setSourceDomain("Person");
        rel2.setTargetDomain("Company");
        rel2.setSourceEUID("0000000001");
        rel2.setTargetEUID("0000000002");
        rel2.setSourceHighLight("John McCain");
        rel2.setTargetHighLight("Sun Microsystems Java");        
        relsObject1.setRelationshipView(rel2);
        
        domainRelationshipsObject.setRelationshipsObject(relsObject1);
        
        RelationshipsObject relsObject2 = new RelationshipsObject();        
        RelationshipDefView relDef2 = new RelationshipDefView();
        relDef2.setName("EmployedBy");
        relDef2.setSourceDomain("Person");
        relDef2.setTargetDomain("Company");
        relDef2.setBiDirection(true) ;
        relDef2.setId("01");              
        relsObject2.setRelationshipDefView(relDef2);        
        
        rel1 = new RelationshipView();
        rel1.setId("01");
        rel1.setName("EmployedBy");
        rel1.setSourceDomain("Person");
        rel1.setTargetDomain("Company");
        rel1.setSourceEUID("0000000001");
        rel1.setTargetEUID("0000000002");
        rel1.setSourceHighLight("John McCain");
        rel1.setTargetHighLight("Sun Microsystems Java");        
        relsObject2.setRelationshipView(rel1);

        rel2 = new RelationshipView();
        rel2.setId("01");
        rel2.setName("EmployedBy");
        rel2.setSourceDomain("Person");
        rel2.setTargetDomain("Company");
        rel2.setSourceEUID("0000000001");
        rel2.setTargetEUID("0000000002");
        rel2.setSourceHighLight("John McCain");
        rel2.setTargetHighLight("Sun Microsystems Java");        
        relsObject2.setRelationshipView(rel2);
        
        domainRelationshipsObject.setRelationshipsObject(relsObject2);
                
        return domainRelationshipsObject;
    }    
    
    /**
     * Search a list of RelationshipView for the given domain and relationship search options and criteria.
     * @param sourceDomainSearch Source domain search options and criteria.
     * @param targetDomainSearch Target domain search options and criteria.
     * @param relationshipSearch Relationship search options and criteria.
     * @return List<RelationshipView> List of RelationshipView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<RelationshipView> searchRelationships(DomainSearch sourceDomainSearch, 
                                                      DomainSearch targetDomainSearch, 
                                                      RelationshipSearch relationshipSearch) 
        throws ServiceException {
        
        List<RelationshipView> relationships = new ArrayList<RelationshipView>(); 
        
        //TDB
        sourceDomainSearch = new DomainSearch();
        sourceDomainSearch.setName("Person");
        sourceDomainSearch.setType("Advanced Person Lookup (Alpha)");
        sourceDomainSearch.setAttributeValue("Person.FirstName", "George");
        sourceDomainSearch.setAttributeValue("Person.LastName", "Denise");
        sourceDomainSearch.setAttributeValue("Person.SSN", "888888888");
        sourceDomainSearch.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
        sourceDomainSearch.setAttributeValue("Person.Address.City", "Foo");
            
        targetDomainSearch = new DomainSearch();
        targetDomainSearch.setName("Company");
        targetDomainSearch.setType("Advanced Company Lookup (Phonetic)");
        targetDomainSearch.setAttributeValue("Company.CompanyName", "Centerpoint");
        targetDomainSearch.setAttributeValue("Company.StockSymbol", "Java");
        targetDomainSearch.setAttributeValue("Company.TaxPayerID", "888");
        targetDomainSearch.setAttributeValue("Company.Address.AddressLine1", "100 Foo Avenue");
        targetDomainSearch.setAttributeValue("Company.Address.City", "Foo");
            
        relationshipSearch = new RelationshipSearch();
        relationshipSearch.setName("EmployedBy");
        relationshipSearch.setSourceDomain(sourceDomainSearch.getName());
        relationshipSearch.setTargetDomain(targetDomainSearch.getName());
        relationshipSearch.setStartDate("11/01/2008");
        relationshipSearch.setEndDate("11/31/2008");
        relationshipSearch.setAttributeValue("hiredDate", "11/01/2008");    
        
        if(!TBD) {
        try {
             // build search options and criteria for source and target
            MultiDomainSearchOptions mdSearchOptions = QueryBuilder.buildMultiDomainSearchOptions(sourceDomainSearch, targetDomainSearch);
            MultiDomainSearchCriteria mdSearchCriteria = QueryBuilder.buildMultiDomainSearchCriteria(sourceDomainSearch, targetDomainSearch, relationshipSearch);
            PageIterator<MultiObject> pages = multiDomainService.searchRelationships(mdSearchOptions, mdSearchCriteria);
            relationships = ViewBuilder.buildRelationshipView(pages, sourceDomainSearch.getName(), targetDomainSearch.getName(), relationshipSearch.getName());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        }
        
        //TDB
        RelationshipView rel1 = new RelationshipView();
        rel1.setName("EmployedBy");
        rel1.setId("000001");
        rel1.setSourceDomain(relationshipSearch.getSourceDomain());
        rel1.setTargetDomain(relationshipSearch.getTargetDomain());
        rel1.setSourceEUID("0000000001");
        rel1.setTargetEUID("0000000002");
        rel1.setSourceHighLight("George Denise");
        rel1.setTargetHighLight("Centerpoint Java");
        relationships.add(rel1);
        RelationshipView rel2 = new RelationshipView();
        rel2.setName("EmployedBy");
        rel2.setId("000002");
        rel2.setSourceDomain(relationshipSearch.getSourceDomain());
        rel2.setTargetDomain(relationshipSearch.getTargetDomain());
        rel2.setSourceEUID("0000000001");
        rel2.setTargetEUID("0000000003");
        rel2.setSourceHighLight("George Denise");
        rel2.setTargetHighLight("Sun Java");
        relationships.add(rel2);
        
        return relationships; 
    }
    
    /**
     * Get a detailed relationship object for the given RelationshipView.
     * @param relationshipView RelationshipView.
     * @return RelationshipComposite RelationshipComposite.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public RelationshipComposite getRelationship(RelationshipView relationshipView)
        throws ServiceException {
   
        RelationshipComposite relationshipComposite = new RelationshipComposite();
        if(!TBD) {
        try {
             // build search options and criteria for source and target
            Relationship relationship = new Relationship();
            relationship.setRelationshipId(Integer.parseInt(relationshipView.getId()));
            relationship.setSourceEUID(relationshipView.getSourceEUID());
            relationship.setTargetEUID(relationshipView.getTargetEUID());
            // need to add a new method in multiDomainService to getRelationship
            MultiObject relationshipObject = multiDomainService.getRelationship(relationship);
            relationshipComposite = ViewBuilder.buildRelationshipComposite(relationshipObject);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);            
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        }
        //TBD
        ObjectRecord sourceRecord = new ObjectRecord();
        sourceRecord.setName("Person");
        sourceRecord.setEUID("0000000001");
        sourceRecord.setAttributeValue("Person.FirstName", "George");
        sourceRecord.setAttributeValue("Person.MiddleName", "Denise");
        sourceRecord.setAttributeValue("Person.LastName", "Denise");
        sourceRecord.setAttributeValue("Person.SSN", "888888888");
        sourceRecord.setAttributeValue("Person.DOB", "11/01/2008");
        sourceRecord.setAttributeValue("Person.Gender", "M");
        sourceRecord.setAttributeValue("Person.Title", "CEO");
        sourceRecord.setAttributeValue("Person.Citizenship", "UN");
        sourceRecord.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
        sourceRecord.setAttributeValue("Person.Address.AddressLine2", "APT# 001");
        sourceRecord.setAttributeValue("Person.Address.City", "Foo");
        sourceRecord.setAttributeValue("Person.Address.StateCode", "CA");
        sourceRecord.setAttributeValue("Person.Address.County", "USA");
        sourceRecord.setAttributeValue("Person.Phone.Phone", "(626)4700000");   
        sourceRecord.setAttributeValue("Person.Phone.PhoneExt", "8888");
           
        ObjectRecord targetRecord = new ObjectRecord(); 
        targetRecord.setName("Company");
        targetRecord.setEUID("0000000001");
        targetRecord.setAttributeValue("Company.CompanyName", "SunSet");
        targetRecord.setAttributeValue("Company.CompanyType", "Public");
        targetRecord.setAttributeValue("Company.StockSymbol", "Sun");
        targetRecord.setAttributeValue("Company.Industry", "Software");
        targetRecord.setAttributeValue("Company.TaxPayerID", "0001");
        targetRecord.setAttributeValue("Company.NoOfEmployees", "100");
        targetRecord.setAttributeValue("Company.Address.AddressLine1", "100 Foo Avenue");
        targetRecord.setAttributeValue("Company.Address.AddressLine2", "APT# 001");
        targetRecord.setAttributeValue("Company.Address.City", "Foo");
        targetRecord.setAttributeValue("Company.Address.StateCode", "CA");
        targetRecord.setAttributeValue("Company.Address.County", "USA");
        targetRecord.setAttributeValue("Company.Phone.Phone", "(626)4700000");   
        targetRecord.setAttributeValue("Company.Phone.PhoneExt", "8888");
        
        RelationshipRecord relationshipRecord = new RelationshipRecord();
        relationshipRecord.setName("EmployedBy");
        relationshipRecord.setId("000001");
        relationshipRecord.setSourceDomain("Person");
        relationshipRecord.setTargetDomain("Company");
        relationshipRecord.setStartDate("11/01/2008");
        relationshipRecord.setEndDate("11/31/2008");
        relationshipRecord.setAttributeValue("hiredDate", "11/01/2008");
        relationshipRecord.setAttributeValue("title", "CEO");
        relationshipComposite.setSourceRecord(sourceRecord);
        relationshipComposite.setTargetRecord(targetRecord);
        relationshipComposite.setRelationshipRecord(relationshipRecord);
    
        return relationshipComposite;       
    }
    
    /**
     * Get all domain records regardless of search options and criteria.
     * @param domainSearch DomainSearch.
     * @return List<ObjectView> List of object view records.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<ObjectView> getEnterprises(DomainSearch domainSearch) 
        throws ServiceException {
        List<ObjectView> objects = new ArrayList<ObjectView>();  
        if (!TBD) {
        try {
            PageIterator<ObjectNode> pages = multiDomainService.searchEnterprises(domainSearch.getName(), null, null);
            // TBD: should return a lits of ObjectNode, EUID, and ComparisonScore.
            objects = ViewBuilder.buildObjectViews(domainSearch.getName(), pages);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        }        
        //TBD
        if ("Person".equals(domainSearch.getName())) {
            ObjectView record1 = new ObjectView();
            record1.setName("Person");
            record1.setEUID("0000000001");
            record1.setHighLight("John Denise");
            objects.add(record1);            
            ObjectView record2 = new ObjectView();
            record2.setName("Person");
            record2.setEUID("0000000002");
            record2.setHighLight("Andrea Denise");
            objects.add(record2);                       
        } else if ("Company".equals(domainSearch.getName())) {          
            ObjectView record1 = new ObjectView(); 
            record1.setName("Company");
            record1.setEUID("0000000001");
            record1.setHighLight("IBM Websphere");
            objects.add(record1);  
            ObjectView record2 = new ObjectView(); 
            record2.setName("Company");
            record2.setEUID("0000000002");
            record2.setHighLight("Microsoft MSN");
            objects.add(record2);
        }        
        return objects;
    }
                        
    /**
     * Get a list of Enterprise objects for the give domain search.
     * @param domainSearch DomainSearch.
     * @return List<ObjectView> List of ObjectView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public List<ObjectRecord> searchEnterprises(DomainSearch domainSearch)
        throws ServiceException {      
        
        List<ObjectRecord> objects = new ArrayList<ObjectRecord>();  
        if (!TBD) {
        try {
             // build search options and criteria for source and target
            EOSearchOptions eoSearchOptions = QueryBuilder.buildEOSearchOptions(domainSearch);
            EOSearchCriteria eoSearchCriteria = QueryBuilder.buildEOSearchCriteria(domainSearch);
            PageIterator<ObjectNode> pages = multiDomainService.searchEnterprises(domainSearch.getName(), eoSearchOptions, eoSearchCriteria);
            // TBD: should return a lits of ObjectNode, EUID, and ComparisonScore.
            objects = ViewBuilder.buildObjectRecords(domainSearch.getName(), pages, eoSearchOptions.isWeighted());
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        }
        }
        
        //TBD
        if ("Person".equals(domainSearch.getName())) {
            ObjectRecord record1 = new ObjectRecord();
            record1.setName("Person");
            record1.setEUID("0000000001");
            record1.setAttributeValue("Person.FirstName", "George");
            record1.setAttributeValue("Person.MiddleName", "Denise");
            record1.setAttributeValue("Person.LastName", "Denise");
            record1.setAttributeValue("Person.SSN", "888888888");
            record1.setAttributeValue("Person.DOB", "11/01/2008");
            record1.setAttributeValue("Person.Gender", "M");
            record1.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
            record1.setAttributeValue("Person.Address.AddressLine2", "APT# 001");
            record1.setAttributeValue("Person.Address.City", "Foo");
            objects.add(record1);
            
            ObjectRecord record2 = new ObjectRecord();
            record2.setName("Person");
            record2.setEUID("0000000002");
            record2.setAttributeValue("Person.FirstName", "Mary");
            record2.setAttributeValue("Person.MiddleName", "Denise");
            record2.setAttributeValue("Person.LastName", "Denise");
            record2.setAttributeValue("Person.SSN", "999999999");
            record2.setAttributeValue("Person.DOB", "11/01/2008");
            record2.setAttributeValue("Person.Gender", "F");
            record2.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
            record2.setAttributeValue("Person.Address.AddressLine2", "APT# 001");
            record2.setAttributeValue("Person.Address.City", "Foo");
            objects.add(record2);
                       
        } else if ("Company".equals(domainSearch.getName())) {
          
            ObjectRecord record1 = new ObjectRecord(); 
            record1.setName("Company");
            record1.setEUID("0000000001");
            record1.setAttributeValue("Company.CompanyName", "SunSet");         
            record1.setAttributeValue("Company.StockSymbol", "Sun"); 
            record1.setAttributeValue("Company.TaxPayerID", "0001");
            record1.setAttributeValue("Company.Address.AddressLine1", "100 Foo Avenue");
            record1.setAttributeValue("Company.Address.AddressLine2", "APT# 001");
            record1.setAttributeValue("Company.Address.City", "Foo");
            objects.add(record1);
  
            ObjectRecord record2 = new ObjectRecord(); 
            record2.setName("Company");
            record2.setEUID("0000000002");
            record2.setAttributeValue("Company.CompanyName", "SunShine");         
            record2.setAttributeValue("Company.StockSymbol", "Sun"); 
            record2.setAttributeValue("Company.TaxPayerID", "0002");
            record2.setAttributeValue("Company.Address.AddressLine1", "100 Foo Avenue");
            record2.setAttributeValue("Company.Address.AddressLine2", "APT# 001");
            record2.setAttributeValue("Company.Address.City", "Foo");
            objects.add(record2);
        }
        return objects;
    }
    
    /**
     * Get a detailed object.
     * @param object ObjectView
     * @return ObjectRecord ObjectRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public ObjectRecord getEnterprise(ObjectView object)
        throws ServiceException {
        
        ObjectRecord objectRecord = null;
        if (!TBD) {        
        try {           
            // need to add a new method in multiDomainService to getObject
            ObjectNode objectNoe = multiDomainService.getEnterprise(object.getName(), object.getEUID());
            objectRecord = ViewBuilder.buildObjectRecord(object.getName(), object.getEUID(),objectNoe);
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch(UserException uex) {
            throw new ServiceException(uex);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);
        }
        }
        
        //TBD
        if ("Person".equals(object.getName())) {
            objectRecord = new ObjectRecord();
            objectRecord.setName("Person");
            objectRecord.setEUID("0000000001");
            objectRecord.setAttributeValue("Person.FirstName", "George");
            objectRecord.setAttributeValue("Person.MiddleName", "Denise");
            objectRecord.setAttributeValue("Person.LastName", "Denise");
            objectRecord.setAttributeValue("Person.SSN", "888888888");
            objectRecord.setAttributeValue("Person.DOB", "11/01/2008");
            objectRecord.setAttributeValue("Person.Gender", "M");
            objectRecord.setAttributeValue("Person.Address.AddressLine1", "100 Foo Avenue");
            objectRecord.setAttributeValue("Person.Address.AddressLine2", "APT# 001");
            objectRecord.setAttributeValue("Person.Address.City", "Foo");
        } else if ("Company".equals(object.getName())) {          
            objectRecord = new ObjectRecord(); 
            objectRecord.setName("Company");
            objectRecord.setEUID("0000000001");
            objectRecord.setAttributeValue("Company.CompanyName", "SunSet");         
            objectRecord.setAttributeValue("Company.StockSymbol", "Sun"); 
            objectRecord.setAttributeValue("Company.TaxPayerID", "0001");
            objectRecord.setAttributeValue("Company.Address.AddressLine1", "100 Foo Avenue");
            objectRecord.setAttributeValue("Company.Address.AddressLine2", "APT# 001");
            objectRecord.setAttributeValue("Company.Address.City", "Foo");
        }
        
        return objectRecord;        
    } 
    
    /**
     * Add a new relationship.
     * @param relRecord RelationshipRecord.
     * @return int Relationship Identifier. 
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationship(RelationshipRecord relRecord)
        throws ServiceException {
        long id = 0;
        try {            
            RelationshipDef relDef = getRelationshipDef(relRecord.getName(), relRecord.getSourceDomain(), relRecord.getTargetDomain());
            Relationship relationship = QueryBuilder.buildRelationship(relRecord, relDef);            
            id = multiDomainService.createRelationship(relationship);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);                  
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }
        return Long.toString(id); 
    }
    
    /**
     * Move relationships.
     * @param movedRelationhips Move relationships
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void moveRelationships(RelationshipMoveComposite movedRelationhips)
        throws ServiceException {                
    }
                    
    /**
     * Delete an existing relationship.
     * @param relationshipView RelationshipView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void deleteRelationship(RelationshipView relView)
        throws ServiceException {
         try {
             if (relView.getId() != null ) {
                multiDomainService.deleteRelationship(Long.parseLong(relView.getId()));                 
             } else {
                 throw new UserException("invalid relationship id: null");
             }
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }            
    }
    
    /**
     * Update an existing relationship. 
     * @param relRecord RelationshipRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationship(RelationshipRecord relRecord)
        throws ServiceException {
        try {
            RelationshipDef relDef = getRelationshipDef(relRecord.getName(), relRecord.getSourceDomain(), relRecord.getTargetDomain());
            Relationship relationship = QueryBuilder.buildRelationship(relRecord, relDef);
            multiDomainService.updateRelationship(relationship);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);                  
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }        
    }    
    
    /**
     * Get relationshipDef for the given name, source domain and target domain.
     * @param name RelationshipDef name.
     * @param sDomain Source domain name.
     * @param tDomain Target domain name.
     * @return RelationshipDef.
     * @throws ConfigException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an error occurs during processing.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    private RelationshipDef getRelationshipDef(String name, String sDomain, String tDomain) 
        throws ConfigException, UserException,  ProcessingException{        
        MDConfigManager configManager = MDConfigManager.getInstance();
        RelationshipDef relDef = null;
        if (configManager.relationshipScreenConfigInstanceExists(sDomain, tDomain, name)) {
            relDef = configManager.getRelationshipScreenConfig(sDomain, tDomain, name).getRelationshipDef();
            return relDef;
        }         
        // multiDomainMetaService.getRelationshipDefByName(name, sourceDomain, targetDomain);
        RelationshipDef[] relationships = multiDomainMetaService.getRelationshipDefs(sDomain, tDomain); 
        if (relationships != null) {
            for (RelationshipDef rDef : relationships) {
                if (rDef.getName().equals(name)) {
                    relDef = rDef;
                    break;
                }
            }
        }
        return relDef;
    }
}
