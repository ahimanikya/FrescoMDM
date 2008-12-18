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
import com.sun.mdm.multidomain.attributes.Attribute;
import com.sun.mdm.multidomain.attributes.AttributeType;
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
import com.sun.mdm.multidomain.services.model.AttributeDefExt;
import com.sun.mdm.multidomain.services.relationship.RelationshipSearch;
import com.sun.mdm.multidomain.services.relationship.RelationshipDefExt;
import com.sun.mdm.multidomain.services.model.ObjectView;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipView;
import com.sun.mdm.multidomain.services.relationship.RelationshipRecord;
import com.sun.mdm.multidomain.services.relationship.RelationshipComposite;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipsObject;
import com.sun.mdm.multidomain.services.relationship.DomainRelationshipDefsObject;
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
        if (!TBD) {
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
        return relationshipComposite;        
        }
        
        //demo
        System.out.println(relationshipView.getSourceDomain() + ":" + relationshipView.getTargetDomain());
        
        RelationshipComposite rsc = new RelationshipComposite();
        
        ObjectRecord sr = new ObjectRecord();
        sr.setName(relationshipView.getSourceDomain());
        sr.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo1","Foo1"));
        sr.add(new com.sun.mdm.multidomain.services.model.Attribute("FirstName","Foo1"));
        sr.add(new com.sun.mdm.multidomain.services.model.Attribute("LastName","Foo1"));
        rsc.setSourceRecord(sr);
        
        ObjectRecord tr = new ObjectRecord();
        tr.setName(relationshipView.getSourceDomain());
        tr.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo2","Foo2"));
        tr.add(new com.sun.mdm.multidomain.services.model.Attribute("FirstName","Foo2"));
        tr.add(new com.sun.mdm.multidomain.services.model.Attribute("LastName","Foo2"));        
        rsc.setTargetRecord(tr);
        
        RelationshipRecord rs = new RelationshipRecord();
        rs.setId(relationshipView.getId());
        rs.setName(relationshipView.getName());
        rs.add(new com.sun.mdm.multidomain.services.model.Attribute("Foo3","Foo3"));
        rsc.setRelationshipRecord(rs);
        return rsc;
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
        
        //demo
        ObjectRecord r1 = new ObjectRecord("foo","000-000-0000");
        r1.add(new com.sun.mdm.multidomain.services.model.Attribute("FOO1", "FOO1"));
        objects.add(r1);
        ObjectRecord r2 = new ObjectRecord("foo","000-000-0001");
        r2.add(new com.sun.mdm.multidomain.services.model.Attribute("FOO2", "FOO2"));       
        objects.add(r2);
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
        
        if (!TBD) {
        ObjectRecord objectRecord = null;
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
        return objectRecord;        
        }
        
        //demo
        ObjectRecord record = new ObjectRecord(object.getName(), object.getEUID());
        record.add(new com.sun.mdm.multidomain.services.model.Attribute("foo","foo"));
        return record;
    } 
    
    /**
     * Add a new relationship.
     * @param relationshipRecord RelationshipRecord.
     * @return int Relationship Identifier. 
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public String addRelationship(RelationshipRecord relationshipRecord)
        throws ServiceException {
        long id = 0;
        try {
            Relationship relationship = QueryBuilder.buildRelationship(relationshipRecord);
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
     * Delete an existing relationship.
     * @param relationshipView RelationshipView.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
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
    
    /**
     * Update an existing relationship. 
     * @param relationshipRecord RelationshipRecord.
     * @throws ServiceException Thrown if an error occurs during processing.
     */
    public void updateRelationship(RelationshipRecord relationshipRecord)
        throws ServiceException {
        try {
            Relationship relationship = QueryBuilder.buildRelationship(relationshipRecord);
            multiDomainService.updateRelationship(relationship);
        } catch (ConfigException cex) {
            throw new ServiceException(cex);                  
        } catch (ProcessingException pex) {
            throw new ServiceException(pex);
        } catch (UserException uex) {
            throw new ServiceException(uex);
        }        
    }    
}
