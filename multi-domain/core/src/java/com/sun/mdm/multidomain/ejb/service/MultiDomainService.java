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

import java.util.List;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.multidomain.attributes.AttributesValue;
import com.sun.mdm.multidomain.group.Group;
import com.sun.mdm.multidomain.group.GroupMember;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;
import com.sun.mdm.multidomain.hierarchy.HierarchyTree;
import com.sun.mdm.multidomain.query.HierarchySearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions.DomainSearchOption;
import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.relationship.Relationship;
        
/**
 * MultiDomainService interface.
 * @author SwaranjitDua
 * @author cye
 */
public interface MultiDomainService {

    /***
     * Create a relationship that associates two EUIDs in the database.     
     * @param relationshp Relationship including fixed and extended attributes associates two EUIDs.
     * @return long Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public long createRelationship(Relationship relationship)
        throws ProcessingException, UserException;
    
    /**
     * Create a relationship that associates two local system records when EUID is unknown to the invoker.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system record local Identifier.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system record local Identifier.
     * @param relationshipValue  AttributesValue including predefined and extended attributes associates two source system records.
     * @return long Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID or target EUID or relationship
     * is passed as a parameter.
     */
    public long createRelationship(String sourceSystemCode, String sourceLID, 
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
     * @return long Relationship identifier if the new relationship is created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source MultiPairValue and target 
     * MultiPairValue or relationship is passed as a parameter.
     */
    public long createRelationship(MultiFieldValuePair sourceMultiPairValue, MultiFieldValuePair targetMultiPairValue, 
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
    public void deleteRelationship(long relationshipid) 
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
     * Add hierarchyNode under parent nodeId.
     * @param hierarchyNode HierarchyNode.
     * @return long HierarchyNodeId.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public long addHierarchyNode(HierarchyNode hierarchyNode)
            throws ProcessingException, UserException ;
    
    /**
     * Add set of hierarchy Nodes to a parent nodeid.
     * @param parentNodeId Parent nodeId.
     * @param nodes List of HierarchyNode.
     * @return long[] List of node Ids.
     */
    
    public long[] addHierarchyNodes(long parentNodeId, HierarchyNode[] nodes)
            throws ProcessingException, UserException ;
    
    /*
     * Create a new hierarchy instance between a parent record and a list of child records. 
     * Both parent record and child records are identified using MultiFieldValuePair.
     * @param parentFieldValues Parent FieldValuePair.
     * @param childFieldValues A list of child FieldValuePair.
     * @param mpdeAttributesValue nodes attributes Value. The attribute values will be used for all parent and child hierarchy associations
     * @return long[] Hierarchy identifer which are newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public long[] addHierarchyNodes(MultiFieldValuePair parentFieldValues, MultiFieldValuePair[] childFieldValues, 
    		AttributesValue nodeAttributesValue)
        throws ProcessingException, UserException;

    /**
     * Delete a hierarchy node identified by nodeid.
     * Promote children to parent node.
     * @param hierarchyid hierarchyid that uniquely identifies hierarchy relatiolnship.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteHierarchyNode(long hierarchyNodeId)
        throws ProcessingException, UserException;
    
    /**
     * Delete a hierarchy node identified by nodeid.
     * delete all its children as well.
     * @param hierarchyid hierarchyid that uniquely identifies hierarchy relatiolnship.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteHierarchy(long hierarchyNodeId)
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
     * Get all children nodes for the given hierarchy node Id.
     * @param hierarchyNodeId HierarchyNodeId.
     * @return List<HierarchyNode> List of children nodes.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public List<HierarchyNode> getHierarchyNodeChildren(long hierarchyNodeId)
        throws ProcessingException, UserException; 
       
    /**
     * Get HierarchyNode by NodeId.
     * @param hierarchyNodeId HierarchyNodeId.
     * @return HierarchyNode HierarchyNode.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public HierarchyNode getHierarchyNode(long hierarchyNodeId)
        throws ProcessingException, UserException;
    
    /**
     * Retrieve the root node of a hierarchy instance.
     * 
     * @param hierarchyDefID hierarchy def id for which root node is retrieved
     * @return the root node, or null if empty hierarchy instance
     */
    public HierarchyNode getRootNode(long hierarchyDefID)
            throws ProcessingException, UserException;
     
    /**
     * Move a set of nodes to have a new Parent
     * @param nodeIds[] set of nodes that are moved
     * @param newParentNodeId new parent node id.
     */
    public void moveHierarchyNodes(long[] nodeIds, long newParentNodeId)
            throws ProcessingException, UserException;
    
    /**
     * Move a set of nodes to have a new Parent
     * @param nodes list of nodes that are moved
     * @param parentNode new parent node.
     */
    public void moveHierarchyNodes(List<HierarchyNode> nodes, HierarchyNode parentNode)
            throws ProcessingException, UserException;
   
    /**
     * Get a list of HierarchyNode for the given search options and criteria.
     * @param searchOption DomainSearchOption.
     * @param searchCriteria HierarchySearchCriteria.
     * @return List<HierarchyNode> List of HierarchyNode.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public List<HierarchyNode> searchHierarchyNodes(DomainSearchOption searchOption, HierarchySearchCriteria searchCriteria) 
            throws ProcessingException, UserException;
    
    /**
     * Get a hierarchyTree for the given hierarchyNodeId and EUID.
     * @param hierarchyNodeId HierarchyNodeId.
     * @param EUID EUID.
     * @return HierarchyObjectTree HierarchyObjectTree.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.  
     */
    public HierarchyTree getHierarchyTree(long hierarchyNodeId, String EUID)
        throws ProcessingException, UserException;

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
    public void deleteGroup(long groupId)
        throws ProcessingException, UserException;

    /**
     * Delete a group member from the given group.
     * @param groupMemeber ID group member Id.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid parameter value is passed.
     */
    public void deleteGroupMember(long groupMemberID)
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
    public ObjectNode[] getGroupMembers(long groupId, EPathArrayList fields) 
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
