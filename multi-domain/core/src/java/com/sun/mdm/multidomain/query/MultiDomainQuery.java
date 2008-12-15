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
package com.sun.mdm.multidomain.query;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
        
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.attributes.AttributesValue;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.service.RelationshipService;
import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.attributes.Attribute;

import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.multidomain.ejb.service.MasterIndexLocator;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.objects.SystemObject;
import java.sql.Connection;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Workhorse class for executing multi domain query.
 * To search for relationships that has filter conditions across multi domains, goes through these steps -
 * 1.  Search EUIDs for each domain using search criteria for that domain specified in input MultiDomainSearchCriteria.
 * 2.  Take the EUIDs from step 1 and use these as 'IN' clause of EUIDS to search Relationship. Also combine the IN clause
 *  from different domains using 'OR' and combine with Relationship filter criteria using 'AND' operator.
 * 3. Intersection - The results of relationship query will be a superset of actual relationships set because step 2 only
 * has to satisfy if either sourceEUID or targetEUID is in set of EUIDs from 1. 
 * Intersect result data from Step 2 with result from step 1 - by checking that in each relationship row, sourceEUID and targetEUID 
 * are present in that domain result set from step 1. The result of step 3 is the set of EUIDs for each domain that
 * satisfy the query.
 * 4. Retrieve the other attributes for domain that are part of MultiSearchOption from EUID result of step 3.
 * 5. combine 4 and 3 to form PageIterator consisting of MultiObjects.
 * @author SwaranjitDua
*/

public class MultiDomainQuery {
    
   private HashMap<String, EOSearchResultIterator> domainEUIDIterators = new HashMap<String, EOSearchResultIterator>();
   private String sourceDomain;
   private Set<String> domains;
   private Relationship relationship; 
   private int maxElements = 1000;
   private static int MAX_IN_RECORDS = 1000;

   Connection con = null;


   public PageIterator<MultiObject> searchRelationships(MultiDomainSearchOptions searchOptions, 
            MultiDomainSearchCriteria mDsearchCriteria, Connection con)
        throws ProcessingException, UserException {

        this.con = con;
        domains = getDomains(searchOptions, mDsearchCriteria);
        sourceDomain = searchOptions.getPrimaryDomain();
        relationship = mDsearchCriteria.getRelationship();
        if (searchOptions.getMaxElements() > 0 ) {
            maxElements = searchOptions.getMaxElements();
        }

        MasterController mc = null;
        for(String d: domains) {
           
            EOSearchCriteria searchCriteria = mDsearchCriteria.getDomainSearchCriteria(d);
                   
            if ( searchCriteria != null) {
                mc = getJNDI(d, con);
            	EOSearchResultIterator it = searchEUIDs(d, mc, searchCriteria, searchOptions);
            	pushDomainEUIDs(d, it);            	            	
            }
        }
        
     List<MultiObject> multiObjects = null;

      while (true) {      
        List<Relationship> relationships = intersectEUIDs();
        if (relationships == null || relationships.size() == 0) {
            break;
        }

        /* Map<domain, Set<EUID>>  */
        Map<String, Set<String>> domainFilteredEUIDs = constructDomainEUIDs(relationships);

                 
        /* domainObjectMap of <domain,<EUID,EOSearchResultRecord>> */
        Map<String, Map<String, EOSearchResultRecord>> domainObjectMap = 
                new HashMap<String, Map<String, EOSearchResultRecord>>();
        
        for(String domain: domains) {
            
            if ( searchOptions.getOptions(domain) != null ) {
                mc  = getJNDI(domain, con);
                Set<String> euids = domainFilteredEUIDs.get(domain);
            	Map<String, EOSearchResultRecord> euidresult = searchObjects(domain, mc, euids, searchOptions);
                domainObjectMap.put(domain, euidresult);         
            }
        }

         /* convert List of relationshps to a map for a euid and list of relationships for that EUID */
        Map<String, List<Relationship>> euidRels = constructEUIDRelationships(sourceDomain, relationships);

         multiObjects = constructMultiObjects(sourceDomain, euidRels, domainObjectMap);
       }
       PageIterator pageIterator = new PageIterator(multiObjects);
       return pageIterator;                            
    }
    

    /**
     * get Relatonships for input 'euid' from domain 'sourceDomain'. Search options are used to retrieve attributes
     * from each domain. Get relationships in which the input euid is either source or target.
     * @param searchOptions
     * @param euid
     * @param sourceDomain
     * @return
     * @throws com.sun.mdm.index.master.ProcessingException
     * @throws com.sun.mdm.index.master.UserException
     */
    public List<MultiObject> getRelationships(MultiDomainSearchOptions searchOptions, 
            String euid, String sourceDomain, Connection con )
        throws ProcessingException, UserException {
        List<MultiObject> multiObjects = null;
     try {
              
        List<Relationship> relationships = searchRelationships(sourceDomain, euid);
        
        Map<String, Set<String>> domainEUIDs = constructDomainEUIDs(relationships);
        
        domains = domainEUIDs.keySet();
        /* create a domainObjectMap of <domain, Map<EUID, EOSearchResultRecord>> */
        Map<String, Map<String, EOSearchResultRecord>> domainObjectMap = 
                new HashMap<String, Map<String, EOSearchResultRecord>>();
        for(String d: domains) {
            MasterController mc = getJNDI(d, con);
            Set<String> euids = domainEUIDs.get(d);
            Map<String, EOSearchResultRecord> euidObjects = 
            searchObjects(d, mc, euids, searchOptions);
            domainObjectMap.put(d, euidObjects);
        }
        
        Map<String, List<Relationship>> euidRels = constructEUIDRelationships(sourceDomain, relationships);
        
        multiObjects = constructMultiObjects(sourceDomain, euidRels, domainObjectMap);
      } catch (Exception ex) {
          throwException(ex);
      }                                       
        return multiObjects;
    }
    
    
    
    private void pushDomainEUIDs(String domain, EOSearchResultIterator it) {
      domainEUIDIterators.put(domain, it);
    }
    
    
   private List<Relationship> intersectEUIDs() throws UserException, ProcessingException {
        
      Map<String, Set<String>> domainEUIDs = new HashMap<String, Set<String>>(); // Map - {domain, Set<EUIDs>}
      Map<String, Set<String>> sourceEUIDs = new HashMap<String, Set<String>>();
      for(String d: domains) {
          Set<String> euids = getDomainEUIDs(d);
          if (!d.equals(sourceDomain)) {
            domainEUIDs.put(d,euids);
          } else {
            sourceEUIDs.put(d, euids);
          }
      }
      
      List<Relationship> relList = searchRelationships(sourceDomain, relationship, sourceEUIDs, domainEUIDs);
      
      List<RelationshipFilter> relFlagList = addFlags(relList);
      
      flag( relFlagList, domainEUIDs);
      
      relList = excludeNonPair(relFlagList);
     
      return relList;
      
    }
   
    
    /*
     * transforms List of Relationships to a Map{EUID, List<Relationship>} with respect to input domain.
     * The keys EUID in the returned map, correspond to the euids for the input sDomain
     */
    private Map<String, List<Relationship>> constructEUIDRelationships(String sDomain, List<Relationship> relationships) {
      Map<String, List<Relationship>> euidRels = new HashMap<String, List<Relationship>>();
      
        for (Relationship rel : relationships) {
            String sd = rel.getRelationshipDef().getSourceDomain();
            String td = rel.getRelationshipDef().getTargetDomain();
            String euid = null;
            if (sd.equals(sDomain)) {
                euid = rel.getSourceEUID();
            } else if (td.equals(sDomain)) {
                euid = rel.getTargetEUID();
            }
            if (euid == null) {
                continue;
            }
            List<Relationship> rels = euidRels.get(euid);
            if (rels == null) {
                rels = new ArrayList<Relationship>();
                euidRels.put(euid, rels);
            }
            rels.add(rel);
        }
        
        return euidRels;
    }
    
    /*  Take input list of relationships, separate them based on domain and retrun Map of domain and EUID set.
     *  @ret Map<domain, Set<EUID>>
     */
    private Map<String, Set<String>> constructDomainEUIDs(List<Relationship> relationships) {
      Map<String, Set<String>> domainEUIDs = new HashMap<String, Set<String>>();
      
        for (Relationship rel : relationships) {
            String sd = rel.getRelationshipDef().getSourceDomain();
            String td = rel.getRelationshipDef().getTargetDomain();
            String seuid = rel.getSourceEUID();
            String teuid = rel.getTargetEUID();
            
            Set<String> euids = domainEUIDs.get(sd);
            if (euids == null) {
                euids = new HashSet<String>();
                domainEUIDs.put(sd, euids);
            }
            euids.add(seuid);
            euids = domainEUIDs.get(td);
            if (euids == null) {
                euids = new HashSet<String>();
                domainEUIDs.put(td, euids);
            }
            euids.add(teuid);
            
        }
        
        return domainEUIDs;
    }

    /*  combine Relationships in relationshipMap and Map of {EUID,EOSearchResultRecord} in domainObjectMap
     *  to a List of MultiObject.
     * @param sDomain sourceDomain to which respect MultiObjects need to be constructed.
     * @param relationshipMap Map of {EUID, List<Relationship>}
     * @param domainObjectMap Map {domain, Map<EUID, EOSearchResultRecord}}
     */
    private List<MultiObject> constructMultiObjects(String sDomain, Map<String, List<Relationship>> relationshipMap,
            Map<String, Map<String, EOSearchResultRecord>> domainObjectMap) {

        List<MultiObject> multiObjects = new ArrayList<MultiObject>();

        Set<Map.Entry<String, List<Relationship>>> entries = relationshipMap.entrySet();
        for (Map.Entry<String, List<Relationship>> entry: entries) {
            String euid = entry.getKey();
            ObjectNode sobject = domainObjectMap.get(sDomain).get(euid).getObject();
            MultiObject multiObject = new MultiObject(sobject); //new MultiObject(sobject);
            multiObjects.add(multiObject);

            List<Relationship> rels = entry.getValue();
            Map<String, List<Relationship>> domainMap = new HashMap<String, List<Relationship>>();
            /* transform list of Relationship rels to domainMap{domain, domain specific relationships}
             * So the idea is for each source euid, create a MultiObject.
             */
            for (Relationship rel: rels) {
              String td = rel.getRelationshipDef().getTargetDomain();
              List<Relationship> rs  = domainMap.get(td);

              if (rs == null) {
                 rs = new ArrayList<Relationship>();
                 domainMap.put(td, rs);
              }
              rs.add(rel);

            }

            Set<Map.Entry<String, List<Relationship>>> domainEntries = domainMap.entrySet();

            for(Map.Entry<String, List<Relationship>> e : domainEntries) {
                String d = e.getKey();
                List<Relationship> drels = e.getValue();
                MultiObject.RelationshipDomain rdomain = new MultiObject.RelationshipDomain(d);
                for (Relationship rel: drels) {
                    String tareuid = rel.getTargetEUID();
                    EOSearchResultRecord record = domainObjectMap.get(sDomain).get(tareuid);
                    ObjectNode node = record.getObject();
                    MultiObject.RelationshipObject relObject = new MultiObject.RelationshipObject(rel, node);
                    rdomain.addRelationshipObejct(relObject);
                }
                multiObject.addRelationshipDomain(rdomain);
            }
        }
        return multiObjects;
    }
    
    
    private Set<String> getDomainEUIDs(String d) 
            throws UserException, ProcessingException {
        
      Set<String> euids = null;
      try {
        EOSearchResultIterator iterator = domainEUIDIterators.get(d);
        euids = new HashSet<String>();
        while (iterator.hasNext()) {
            String euid = iterator.next().getEUID();
            euids.add(euid);
        }
        
      } catch (Exception ex) {
            throwException(ex);
      }
       return euids;
    }
    
    
  
   /**
    * flag as true each relationship in relationships set, whose both sourceEUID and targetEUID are found in domainEUIDs. 
    * @param relationships
    * @param domainEuids Map<domain, Set<EUIDs>>
    */
   private void flag(List<RelationshipFilter> relationships, Map<String, Set<String>> domainEuids) {
      
       for (RelationshipFilter rel: relationships) {
         String tarDomain = rel.relationship.getRelationshipDef().getTargetDomain();
         String sDomain = rel.relationship.getRelationshipDef().getSourceDomain();
         
         Set set = domainEuids.get(tarDomain);
         if (set == null || set.contains(rel.relationship.getTargetEUID())) {
                 rel.inTarget = true;
         }
             
         set = domainEuids.get(sourceDomain);
         if (set == null || set.contains(rel.relationship.getSourceEUID())) {
                 rel.inSource = true;
         }         
     }  
   }
   
   
   private List<Relationship> excludeNonPair(List<RelationshipFilter> relFlagList) {     
       List <Relationship> resultList = new ArrayList<Relationship>();
       for (RelationshipFilter relFilter: relFlagList) {
         if (relFilter.inSource == true && relFilter.inTarget == true) {          
             resultList.add(relFilter.relationship);
         }               
       } 
       return resultList;
   }
   
   
   private Set<String> getDomains(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria mDSearchCriteria) {
       Map<String, MultiDomainSearchOptions.DomainSearchOption> options = searchOptions.getOptions();
       Set<String> sdomainSet = mDSearchCriteria.getDomains();
       Set<String> odomainSet = options.keySet();
       Set<String> domainSet = new HashSet<String>();
       domainSet.addAll(sdomainSet);
       domainSet.addAll(odomainSet);
       return domainSet;
   }

  
   private static class RelationshipFilter {
       private Relationship relationship;
       private boolean inSource;
       private boolean inTarget;
       
       RelationshipFilter(Relationship relationship) {
           this.relationship = relationship;
       }
       
   }
   
   
   private Map<String, EOSearchResultRecord>  searchObjects(String domain, MasterController mc, Set<String> euids,
          MultiDomainSearchOptions searchOptions)throws UserException, ProcessingException {
      
       Map<String, EOSearchResultRecord> resultMap = new HashMap<String, EOSearchResultRecord>();
       if (euids == null) {
           return resultMap;
       }
       try {
       
       MultiDomainSearchOptions.DomainSearchOption dSearchOption = searchOptions.getOptions(domain);
                
       EOSearchOptions dsearchOptions = new EOSearchOptions(dSearchOption.getSearchId(), dSearchOption.getOptions());

       String[] seuids = new String[euids.size()];
               
       euids.toArray(seuids);

       EOSearchResultIterator iterator = searchEnterpriseObject(seuids, dsearchOptions, mc);
       
       while(iterator.hasNext()) {
           EOSearchResultRecord record = iterator.next();
           String euid = record.getEUID();
           resultMap.put(euid, record);
       }
           
       } catch (Exception ex) {
           throwException(ex);
       }
        return resultMap;
   }
   
   
    EOSearchResultIterator searchEUIDs(String domain, MasterController mc, EOSearchCriteria searchCriteria, MultiDomainSearchOptions searchOptions)
     throws ProcessingException, UserException {
        
        //searchOptions.getMaxElements(); 
        
        EPathArrayList epaths = new EPathArrayList();
        epaths.add("Enterprise.SystemSBR." + domain + ".EUID");
        MultiDomainSearchOptions.DomainSearchOption dSearchOption = searchOptions.getOptions(domain);
                
        EOSearchOptions dsearchOptions = new EOSearchOptions(dSearchOption.getSearchId(), epaths);
        
        EOSearchResultIterator iterator = searchEnterpriseObject(searchCriteria, dsearchOptions, mc);
        
        return iterator;
                
    }

   
 

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#searchRelationships()
     */                    
    public PageIterator<MultiObject> searchRelationships(String sourceDomain, EPathArrayList[] sourceEPathList, 
                                                 String targetDomain, EPathArrayList[] targetEPathList, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#searchEnterprises()
     */                    
    public EOSearchResultIterator searchEnterprises(String domain, EOSearchOptions searchOptions, EOSearchCriteria searchCriteria,
            Connection con)
        throws ProcessingException, UserException {        
         MasterController mc = getJNDI(domain, con);
         EOSearchResultIterator results = mc.searchEnterpriseObject(searchCriteria, searchOptions);
         return results;
    }
    
    
    MasterController getJNDI(String domain, Connection con) throws ProcessingException {
       MasterIndexLocator mcLocator = new MasterIndexLocator();
       return mcLocator.getMasterController(domain, con);
    }
    
    
    List<RelationshipFilter> addFlags(List<Relationship> relList) {
       
        List<RelationshipFilter> relFilterList = new ArrayList<RelationshipFilter>();
        if (relList == null) {
            return relFilterList;
        }
        for (Relationship rel: relList) {
            RelationshipFilter relFilter = new RelationshipFilter(rel);
            relFilterList.add(relFilter);
        }
        return relFilterList;
    }
    
    
     List<Relationship> searchRelationships(String sourceDomain, String euid)
     throws ProcessingException {
         try {
           RelationshipService relService = new RelationshipService(con);
           List<Relationship> rels = relService.searchRelationShips(sourceDomain, euid);
           return rels;
         } catch (Exception e) {
            throw new ProcessingException(e);
         }
     }
     
     List<Relationship> searchRelationships(String sourceDomain, Relationship relationship, 
             Map<String, Set<String>> sourceEUIDs, Map<String, Set<String>> domainEUIDs)
             throws ProcessingException {

        try {
         RelationshipService relService = new RelationshipService(con);

         List<Relationship> rels = relService.searchRelationShips(sourceEUIDs, domainEUIDs);

          return rels;
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
     }
     
     EOSearchResultIterator searchEnterpriseObject(EOSearchCriteria searchCriteria, EOSearchOptions searchOptions
             ,MasterController mc) 
         throws ProcessingException, UserException   {
         
                  
         return mc.searchEnterpriseObject(searchCriteria, searchOptions);
     
     }

     EOSearchResultIterator searchEnterpriseObject(String[] seuids, EOSearchOptions searchOptions
             , MasterController mc) 
         throws ProcessingException, UserException   {
         
         return mc.searchEnterpriseObject(seuids, searchOptions);
     }

     
     private void throwException(Exception ex) 
             throws ProcessingException, UserException {
         if (ex instanceof UserException) {
             throw (UserException)ex;
         } else if (ex instanceof ProcessingException) {
             throw (ProcessingException) ex;
         }  else {
             throw new ProcessingException(ex);             
         }
     }
        

}
