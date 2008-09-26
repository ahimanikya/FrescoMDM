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
package com.sun.mdm.multidomain.ejb.service;

import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.HierarchyObject;

import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.PageMultiIterator;
import com.sun.mdm.multidomain.query.PageSingleIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
 
/**
 * MultiDomainService interface.
 * @author cye
 */
public interface MultiDomainService {

    /***
     * Create a relationship that associates two EUIDs in the database.
     * @param sourceEUDI Source EUID.
     * @param targetEUID Target EUID.
     * @param relationshp Relationship including fixed and extended attributes associates two EUIDs.
     * @return String Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public String createRelationship(String sourceEUDI, String targetEUID, Relationship relationshp)
        throws ProcessingException, UserException;
    
    /**
     * Create a relationship that associates two local system records where EUID is unknow in the database.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system record local Identifier.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system record local Identifier.
     * @param relationship Relationship including fixed and extended attributes associates two source system records.
     * @return String Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public String createRelationship(String sourceSystemCode, String sourceLID, 
                                     String targetSystemCode, String targetLID, 
                                     Relationship relationship) 
        throws ProcessingException, UserException;

    /**
     * Create a relationship between source domain and target domain entities which 
     * are identified using unqiue MultiPairValue.
     * @param sourceMultiPairValue Source MultiPairValue.
     * @param targetMultiPairValue Target MultiPairValue.
     * @param relationship Relationship including fixed and extended attributes associates 
     * source domain and target domain entities.
     * @return String Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source MultiPairValue and target 
     * MultiPairValue or relationship is passed as a parameter.
     */
    public String createRelationship(MultiFieldValuePair sourceMultiPairValue, MultiFieldValuePair targetMultiPairValue, 
                                     Relationship relationship) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing relationship for the given source EUID and target EUID.
     * @param sourceEUID Source entity EUID.
     * @param targetEUID Target entity EUID.
     * @param relationship Relationship including fixed and extended attributes associates two EUIDs.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID and target EUID or relationship is passed as a parameter.
     */
    public void updateRelationship(String sourceEUID, String targetEUID, Relationship relationship) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relationship for the given source EUID and target EUID.
     * @param sourceEUID Source entity EUID.
     * @param targetEUID Target entity EUID.
     * @param relationshp Relationship including fixed and extended attributes associates two EUIDs
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID and target EUID or relationship is passed as a parameter.
     */
    public void deleteRelationship(String sourceEUID, String targetEUID, Relationship relationshp) 
        throws ProcessingException, UserException;
            
    /**
     * Delete an existing relationship for the given source system LID and target system LID and relationship.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system entity LID.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system entity LID.
     * @param relationship Relationship including fixed and extended attributes associates two LIDs.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source system code and 
     * target system code or relationship is passed as a parameter.
     */
    public void deleteRelationship(String sourceSystemCode, String sourceLID, 
                                     String targetSystemCode, String targetLID, Relationship relationship)
        throws ProcessingException, UserException;

    /**
     * Search relationship tables to retrieve relationship records that qualifies search criteria 
     * and search source domain and target domain that qualifies search options to retrieve 
     * master index records which have specified relationships.
     * @param searchOptions MultiDomainSearchOptions.
     * @param searchCriteria MultiDomainSearchCriteria.
     * @return PageMultiIterator Page Iterator of multiple relationship objects.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public PageMultiIterator searchRelationships(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException;
    
    /**
     * Search relationship tables to retrieve relationship records that qualifies search criteria 
     * and search source domain and target domain that qualifies search options to retrieve 
     * master index records which have specified relationships. 
     * @param sourceDomain Source domain.
     * @param sourceEPathList Source domain search EPathList.
     * @param targetDomain Target domain.
     * @param targetEPathList Target domain search EPathList.
     * @param searchCriteria MultiDomainSearchCriteria.
     * @return PageMultiIterator Page Iterator of multiple relationship objects.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions is passed as a parameter.
     */
    public PageMultiIterator searchRelationships(String sourceDomain, EPathArrayList[] sourceEPathList, 
                                                 String targetDomain, EPathArrayList[] targetEPathList, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException;
    
    /**
     * Search different master indexes and get master index records that qualify the search criteria. 
     * The method does not search or retrieve the relationship tables. 
     * @param searchOptions Master Index SearchOptions.
     * @param searchCriteria Master Index SearchCriteria.
     * @return PageSingleIterator ObjectNode Iterator.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public PageSingleIterator searchEnterprises(String domain, EOSearchOptions searchOptions, EOSearchCriteria searchCriteria)
        throws ProcessingException, UserException;
    
    /**
     * Create a new hierarchy instance between a parentEUID and a child EUID 
     * in the specified domain and persist it in the hierarchy tables.
     * @param parentEUID parent EUID.
     * @param childEUID child EUID.
     * @param hierarchy Hierarchy instance. 
     * @return String Hierarchy identifer which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public String createHierarchy(String parentEUID, String childEUID, Relationship hierarchy)
        throws ProcessingException, UserException;

    /**
     * Create a new hierarchy instance between a parentEUID and a list of child EUID 
     * in the specified domain and persist it in the hierarchy tables.
     * @param parentEUID parent EUID.
     * @param childEUID child EUID.
     * @param hierarchy Hierarchy instance. 
     * @return String Hierarchy identifer which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */    
    public String createHierarchy(String parentEUID, String[] childEUIDs, Relationship hierarchy)
        throws ProcessingException, UserException;
    

    /**
     * Create a new hierarchy instance between a parent record and a list of child records. 
     * Both parent record and child records are identified using MultiFieldValuePair.
     * @param parentFieldValues Parent FieldValuePair.
     * @param childFieldValues A list of child FieldValuePair.
     * @param hierarchy Hierarchy instance.
     * @return String Hierarchy identifer which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public String createHierarchy(MultiFieldValuePair parentFieldValues, MultiFieldValuePair[] childFieldValues, Relationship hierarchy)
        throws ProcessingException, UserException;


    /**
     * Delete a hierarchy between parent EUID and a child EUID record from hierarchy tables.
     * @param parentEUID Parent EUID.
     * @param childEUID Child EUID.
     * @param hierarchy Hierarchy instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteHierarchy(String parentEUID, String childEUID, Relationship hierarchy)
        throws ProcessingException, UserException;

    /**
     * Delete a hierarchy between parent EUID and children EUIDs from hierarchy tables.
     * @param parentEUID Parent EUID.
     * @param childEUIDs An array of Child EUID.
     * @param hierarchy Hierarchy instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */    
    public void deleteHierarchy(String parentEUID, String[] childEUIDs, Relationship hierarchy)
        throws ProcessingException, UserException;

    /**
     * Update an existing hierarchy between parent EUID and a child EUID from hierarchy tables.
     * @param parentEUID Parent EUID.
     * @param childEUIDs child EUID.
     * @param hierarchy Hierarchy instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */        
    public void updateHierarchy(String parentEUID, String childEUID, Relationship hierarchy)
        throws ProcessingException, UserException;

    /**
     * Update an existing hierarchy between parent EUID and childred EUIDs from hierarchy tables.
     * @param parentEUID Parent EUID.
     * @param childEUIDs An array of child EUID.
     * @param hierarchy Hierarchy instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */            
    public void updateHierarchy(String parentEUID, String[] childEUIDs, Relationship hierarchy)
        throws ProcessingException, UserException;
    
    /**
     * Search hierarchy for the given EUID from  hierarchy tables.
     * @param domain Domain name.
     * @param EUID EUID.
     * @param ePathFields EPathArrayList.
     * @return HierarchyObject HierarchyObject.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public HierarchyObject searchHierarchy(String domain, String EUID, EPathArrayList ePathFields)
        throws ProcessingException, UserException;
            

    /**
     * Create a group.
     * @param group Relationship.
     * @return String group Id which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public String createGroup(Relationship group)
        throws ProcessingException, UserException;
            
    /**
     * Create group members.
     * @param groupId Group Identifier.
     * @param EUIDs An array of EUIDs.
     * @param relationship Group relationship.
     * @return String[] A list of relationshipIds that are assocated between the given groupId and EUIDs.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public String[] createGroupMembers(String groupId, String EUIDs[], Relationship relationship)
        throws ProcessingException, UserException;
            
    /**
     * Delete a group.
     * @param group
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroup(Relationship group)
            throws ProcessingException, UserException;
    
    /**
     * Delete a group for the given domain and group Id.
     * @param domain Domain name.
     * @param groupId Group Identifier.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroup(String domain, String groupId)
        throws ProcessingException, UserException;

    /**
     * Delete a EUID from the given group.
     * @param domain Domain name.
     * @param groupId Group Identifier.
     * @param EUID EUID.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroupMember(String domain, String groupId, String EUID)
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group.
     * @param group Group instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void updateGroup(Relationship group)
            throws ProcessingException, UserException;
            
    /**
     * Search a list of groups for the given group relationship attributes.
     * @param relationship Relationship.
     * @return Relationship[] A list of groups that are represented by relationship.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public Relationship[] searchGroup(Relationship relationship)
            throws ProcessingException, UserException;
    
    /**
     *  Get group members that are have relationship with the given groupId.
     * @param domain Domain name.
     * @param groupId Group Identifier.
     * @param fields A list of fields to retrieve for all the members EUIDs.
     * @return A list of object nodes that have relationship with the given groupId.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public ObjectNode[] getGroupMembers(String domain, String groupId, EPathArrayList fields) 
            throws ProcessingException, UserException;
            
    /**
     * Get a list of groups that the given EUID assocates with.
     * @param domain Domain name.
     * @param EUID EUID.
     * @return Relationship[] A list of groups that the given EUID assocates with.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public Relationship[] getGroups(String domain, String EUID)
            throws ProcessingException, UserException;

    /**
     * Update a group relationship for the given EUID.
     * @param domain Domain name.
     * @param EUID EUID.
     * @param relationship Relationship.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void updateGroupMember(String domain, String EUID, Relationship relationship) 
            throws ProcessingException, UserException;
    
}
