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
import com.sun.mdm.multidomain.hierarchy.HierarchyObjectTree;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.group.Group;
import com.sun.mdm.multidomain.group.GroupMember;
import com.sun.mdm.multidomain.attributes.AttributesValue;

import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
 
/**
 * MultiDomainService interface.
 * @author SwaranjitDua
 * @author cye
 */
public interface MultiDomainService {

    /***
     * Create a relationship that associates two EUIDs in the database.     
     * @param relationshp Relationship including fixed and extended attributes associates two EUIDs.
     * @return int Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public int createRelationship(Relationship relationship)
        throws ProcessingException, UserException;
    
    /**
     * Create a relationship that associates two local system records when EUID is unknown to the invoker.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system record local Identifier.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system record local Identifier.
     * @param relationshipValue  AttributesValue including predefined and extended attributes associates two source system records.
     * @return int Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public int createRelationship(String sourceSystemCode, String sourceLID, 
                                     String targetSystemCode, String targetLID, 
                                     AttributesValue relationshipValue) 
        throws ProcessingException, UserException;

    /**
     * Create a relationship between source domain and target domain entities which 
     * are identified using unique MultiPairValue.
     * @param sourceMultiPairValue Source MultiPairValue.
     * @param targetMultiPairValue Target MultiPairValue.
     * @param relationshipValue AttributesValue including fixed and extended attributes associates 
     * source domain and target domain entities.
     * @return int Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source MultiPairValue and target 
     * MultiPairValue or relationship is passed as a parameter.
     */
    public int createRelationship(MultiFieldValuePair sourceMultiPairValue, MultiFieldValuePair targetMultiPairValue, 
                                     AttributesValue relationshipValue) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing relationship.
     * @param relationship Relationship including fixed and extended attributes associates two EUIDs.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID and target EUID or relationship is passed as a parameter.
     */
    public void updateRelationship(Relationship relationship) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relationship.
     * @param relationshp Relationship including fixed and extended attributes that associates two EUIDs
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationshipid is passed as a parameter.
     */
    public void deleteRelationship(int relationshipid) 
        throws ProcessingException, UserException;
            
    /**
     * Delete an existing relationship for the given source system LID and target system LID and relationship.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system entity LID.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system entity LID.
     * @param relationship Relationship including fixed and extended attributes associates two LIDs.
     * @param relationshipName relationship Name
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source system code and 
     * target system code or relationship is passed as a parameter.
     */
    public void deleteRelationship(String sourceSystemCode, String sourceLID, 
                                     String targetSystemCode, String targetLID, String sourceDomain, String targetDomain, String relationshipName)
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
    public PageIterator<MultiObject> searchRelationships(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException;
    
    /**
     * Get detailed relations object for the given relationship id defined in Relationship.
     * @param relationship Relationship
     * @return MultiObject Detailed relationship object.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public MultiObject getRelationship(Relationship relationship)
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
    public PageIterator<MultiObject> searchRelationships(String sourceDomain, EPathArrayList[] sourceEPathList, 
                                                         String targetDomain, EPathArrayList[] targetEPathList, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException;
    
    /**
     * Get ObjectNode for the given domain and EUID.
     * @param domain Domain name.
     * @param euid EUID.
     * @return ObjectNode ObjectNode.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public ObjectNode getEnterprise(String domain, String euid)
        throws ProcessingException, UserException;
    
    /**
     * Search domain master index and get master index records that qualify the search criteria. 
     * The method does not search or retrieve the relationship tables.
     * @param domain master index domain to search 
     * @param searchOptions Master Index SearchOptions.
     * @param searchCriteria Master Index SearchCriteria.
     * @return PageSingleIterator ObjectNode Iterator.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public PageIterator<ObjectNode> searchEnterprises(String domain, EOSearchOptions searchOptions, EOSearchCriteria searchCriteria)
        throws ProcessingException, UserException;
    
    /**
     * Persists the new hierarchy node in the hierarchy tables. A node encapsulates link between a parentEUID and a child EUID. 
     * @param hierarchyNode hierarchy Node instance. 
     * @return int Hierarchy Node identifer which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
   // public String addHierarchyNode(String partentEUID, HierarchyNode hierarchyLink)
     //   throws ProcessingException, UserException;

    
    //public String addHierarchyNode(String parentEUID, HierarchyNode node);
    
    public int addHierarchyNode(HierarchyNode childNode)
            throws ProcessingException, UserException ;
    
    /**
     * add set of hierarchy Nodes to a parent nodeid.
     * @param parentNodeID
     * @param nodes
     * @return
     */
    
    public int[] addHierarchyNodes(int parentNodeID, HierarchyNode[] nodes)
            throws ProcessingException, UserException ;
    
    
    

    /*
     * Create a new hierarchy instance between a parent record and a list of child records. 
     * Both parent record and child records are identified using MultiFieldValuePair.
     * @param parentFieldValues Parent FieldValuePair.
     * @param childFieldValues A list of child FieldValuePair.
     * @param mpdeAttributesValue nodes attributes Value. The attribute values will be used for all parent and child hierarchy associations
     * @return int[] Hierarchy identifer which are newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public int[] addHierarchyNodes(MultiFieldValuePair parentFieldValues, MultiFieldValuePair[] childFieldValues, 
    		AttributesValue nodeAttributesValue)
        throws ProcessingException, UserException;


    /**
     * Delete a hierarchy node identified by nodeid.
     * delete all its children as well.
     * @param hierarchyid hierarchyid that uniquely identifies hierarchy relatiolnship.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteHierarchy(int nodeid)
        throws ProcessingException, UserException;

 

    /**
     * Update an existing hierarchy between parent EUID and a child EUID from hierarchy tables.
     * @param hierarchy Hierarchy instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */        
    public void updateHierarchyNode(HierarchyNode node)
        throws ProcessingException, UserException;
    
    /**
     * Search hierarchy for the given EUID from  hierarchy tables.
     * @param hierarchytypeid hierarchy type for which hierarchy tree is retrieved
     * @param EUID EUID for which Hierarchy is searched.
     * @param ePathFields EPathArrayList specifies the fields that needs to retrieved.
     * @return HierarchyObjectTree HierarchyObjectTree contains all ancestors for input EUID and all its immediate children.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public HierarchyObjectTree searchHierarchy(int hierarchyDefID, String EUID, EPathArrayList ePathFields)
        throws ProcessingException, UserException;
    
    /**
     * Move a set of nodes to have a new Parent
     * @param nodeIDs[] set of nodes that are moved
     * @param newParentNodeID new parent node id.
     */
    
    public void moveHierarchyNodes(int[] nodeIDs, int newParentNodeID)
            throws ProcessingException, UserException;;
            
    
    /**
     * Create a group.
     * @param group Group.
     * @return String group Id which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public String createGroup(Group group)
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
    public String[] createGroupMembers(GroupMember[] members)
        throws ProcessingException, UserException;
            
    
    /**
     * Delete a group for the given group Id.
     * @param groupId Group Identifier.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroup(int groupId)
        throws ProcessingException, UserException;

    /**
     * Delete a group member from the given group.
     * @param groupMemeber ID group member Id.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroupMember(int groupMemberID)
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group.
     * @param group Group instance.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void updateGroup(Group group)
            throws ProcessingException, UserException;
            
    /**
     * Search a list of groups for the given group relationship attributes.
     * @param group Group.
     * @return Group[] A list of groups that are qualified by input group values.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public Group[] searchGroup(Group group)
            throws ProcessingException, UserException;
    
    /**
     *  Get group members that  have association with the given groupId.
     * @param groupId Group Identifier.
     * @param fields A list of fields to retrieve for all the members EUIDs.
     * @return A list of object nodes that have relationship with the given groupId.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public ObjectNode[] getGroupMembers(int groupId, EPathArrayList fields) 
            throws ProcessingException, UserException;
            
    /**
     * Get a list of groups that the given group member EUID associates with.
     * @param EUID EUID of a group member.
     * @return Group[] A list of groups that the given EUID associates with.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public Group[] getGroups(String EUID)
            throws ProcessingException, UserException;

    /**
     * Update a group relationship.
     * @param GroupMember groupMember.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void updateGroupMember(GroupMember groupMember) 
            throws ProcessingException, UserException;
    
}
