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

import com.sun.mdm.multidomain.relationship.RelationshipType;
import com.sun.mdm.multidomain.hierarchy.HierarchyType;
import com.sun.mdm.multidomain.group.GroupType;
import com.sun.mdm.multidomain.group.GroupMemberType;

        
/**
 * MultiDomainMetaService interface.
 * @author SwaranjitDua
 * @author cye
 */
public interface MultiDomainMetaService {
    
    /**
     * Get all domain names.
     * @return String[] An array of domain.
     * @exception ProcessingException Thrown if an error occurs during processing.
     */
    public String[] getDomains() 
        throws ProcessingException;
    
    /**
     * Get all relational types types for all domains.
     * @return RelationshipType[] An array of relationship type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public RelationshipType[] getRelationshipTypes() 
        throws ProcessingException;
    
    /**
     * Get all relationship types of the given source domain and target domains.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return RelationshipType[] An array of relation type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source domain or target domain 
     * is passed as a parameter.
     */
    public RelationshipType[] getRelationshipTypes(String sourceDomain, String targetDomain) 
        throws ProcessingException, UserException;
    
    /**
     * Create a relationship type and persist the relationship type in the database.
     * @param relationshipType RelationshipType.
     * @return String Relationship type identifier which is newly created. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship type is passed as a parameter.
     */
    public String createRelationshipType(RelationshipType relationshipType)  
        throws ProcessingException, UserException;
    
    /**
     * Update an existing relationship type and persist in the database.
     * @param relationshipType RelationshipType.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship type is passed as a parameter.
     */
    public void updateRelationshipType(RelationshipType relationshipType)  
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relation type from the database.
     * @param relationshipType RelationshipType.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship type is passed as a parameter.
     */
    public void deleteRelationshipType(RelationshipType relationshipType)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing relation type from the database for the given source domain 
     * and the target domain and relationship type name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @param relationshipType Relationship type name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source domain or target domain or 
     * relationship type name is passed as a parameter.
     */
    public void deleteRelationshipType(String sourceDomain, String targetDomain, String relationshipTypeName)  
        throws ProcessingException, UserException;
    
    /**
     * Get all Hierarchy types for the given domain.
     * @param domain Domain name.
     * @return HierarchyType[] An array of relationship type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name is passed as a parameter.
     */
    public HierarchyType[] getHierarchyTypes(String domain) 
        throws ProcessingException, UserException;
   
    /**
     * Create a new hierarchy type and persist the hierarchy type in the database.
     * @param hierarchyType HierarchyType.
     * @return String Hierarchy type identifier which is newly created. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public String createHierarchyType(HierarchyType hierarchyType)  
        throws ProcessingException, UserException;
    
    
    /**
     * Update an existing hierarchy type and persist the hierarchy type in the database.
     * @param hierarchyType HierarchyType.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public void updateHierarchyType(HierarchyType hierarchyType)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing hierarchy type from the database for the given hierarchy type.
     * @param hierarchyType HierarchyType.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public void deleteHierarchyType(HierarchyType hierarchyType) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing hierarchy type from the database for the given domain and 
     * hierarchy type name.
     * @param domain Domain name.
     * @param name HierarchyType name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or hierarchy name is 
     * passed as a parameter.
     */
    public void deleteHierarchyType(String domain, String name) 
        throws ProcessingException, UserException;
         
    /**
     * Get all group types for the given domain name.
     * @param domain Domain name.
     * @return GroupType[] An array of group type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public GroupType[] getGroupTypes(String domain) 
        throws ProcessingException;
        
    /**
     * Create a new group type and persist in the database.
     * @param groupType Group Type.
     * @return String Group type identifier which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public String createGroupType(GroupType groupType) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group type and persist in the database.
     * @param groupType Group Type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public void updateGroupType(GroupType groupType) 
        throws ProcessingException, UserException;
    

    /**
     * Delete an existing group type for the given group type.
     * @param groupType Group Type
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public void deleteGroupType(GroupType groupType) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing group type for the given domain name.
     * @param domain Domain name.
     * @param name Group type name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or group type name is 
     * passed as a parameter.
     */
    public void deleteGroupType(String domain, String name) 
        throws ProcessingException, UserException;
            
    /**
     * Get all group member types for the given domain name.
     * @param domain Domain name.
     * @return GroupMemberType[] An array of group type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public GroupMemberType[] getGroupMemberTypes(String domain) 
        throws ProcessingException;
        
    /**
     * Create a new group type and persist in the database.
     * @param groupMemberType Group Member Type.
     * @return String Group member type identifier which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public String createGroupMemberType(GroupMemberType groupMemberType) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group member type and persist in the database.
     * @param groupMemberType Group Member Type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public void updateGroupMemberType(GroupMemberType groupMemberType) 
        throws ProcessingException, UserException;
    

    /**
     * Delete an existing group member type for the given group member type.
     * @param groupMemberType GroupMemberType
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public void deleteGroupMemberType(GroupMemberType groupMemberType) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing group Member type for the given domain name.
     * @param domain Domain name.
     * @param name Group Member type name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or group type name is 
     * passed as a parameter.
     */
    public void deleteGroupMemberType(String domain, String name) 
        throws ProcessingException, UserException;
}
