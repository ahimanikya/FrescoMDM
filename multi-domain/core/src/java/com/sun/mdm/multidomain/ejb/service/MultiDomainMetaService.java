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

import com.sun.mdm.multidomain.relationship.RelationshipDef;
import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.group.GroupDef;
import com.sun.mdm.multidomain.group.GroupMemberDef;

        
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
     * Get all relational Defs Defs for all domains.
     * @return RelationshipDef[] An array of relationship Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public RelationshipDef[] getRelationshipDefs() 
        throws ProcessingException;
    
    /**
     * Get all relationship Defs of the given source domain and target domains.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @return RelationshipDef[] An array of relation Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source domain or target domain 
     * is passed as a parameter.
     */
    public RelationshipDef[] getRelationshipDefs(String sourceDomain, String targetDomain) 
        throws ProcessingException, UserException;
    
    /**
     * Create a relationship Def and persist the relationship Def in the database.
     * @param relationshipDef RelationshipDef.
     * @return String Relationship Def identifier which is newly created. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship Def is passed as a parameter.
     */
    public String createRelationshipDef(RelationshipDef relationshipDef)  
        throws ProcessingException, UserException;
    
    /**
     * Update an existing relationship Def and persist in the database.
     * @param relationshipDef RelationshipDef.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship Def is passed as a parameter.
     */
    public void updateRelationshipDef(RelationshipDef relationshipDef)  
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relation Def from the database.
     * @param relationshipDef RelationshipDef.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship Def is passed as a parameter.
     */
    public void deleteRelationshipDef(RelationshipDef relationshipDef)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing relation Def from the database for the given source domain 
     * and the target domain and relationship Def name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @param relationshipDef Relationship Def name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source domain or target domain or 
     * relationship Def name is passed as a parameter.
     */
    public void deleteRelationshipDef(String sourceDomain, String targetDomain, String relationshipDefName)  
        throws ProcessingException, UserException;
    
    /**
     * Get all Hierarchy Defs for the given domain.
     * @param domain Domain name.
     * @return HierarchyDef[] An array of relationship Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name is passed as a parameter.
     */
    public HierarchyDef[] getHierarchyDefs(String domain) 
        throws ProcessingException, UserException;
   
    /**
     * Create a new hierarchy Def and persist the hierarchy Def in the database.
     * @param hierarchyDef HierarchyDef.
     * @return String Hierarchy Def identifier which is newly created. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy Def is passed as a parameter.
     */
    public String createHierarchyDef(HierarchyDef hierarchyDef)  
        throws ProcessingException, UserException;
    
    
    /**
     * Update an existing hierarchy Def and persist the hierarchy Def in the database.
     * @param hierarchyDef HierarchyDef.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy Def is passed as a parameter.
     */
    public void updateHierarchyDef(HierarchyDef hierarchyDef)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing hierarchy Def from the database for the given hierarchy Def.
     * @param hierarchyDef HierarchyDef.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy Def is passed as a parameter.
     */
    public void deleteHierarchyDef(HierarchyDef hierarchyDef) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing hierarchy Def from the database for the given domain and 
     * hierarchy Def name.
     * @param domain Domain name.
     * @param name HierarchyDef name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or hierarchy name is 
     * passed as a parameter.
     */
    public void deleteHierarchyDef(String domain, String name) 
        throws ProcessingException, UserException;
         
    /**
     * Get all group Defs for the given domain name.
     * @param domain Domain name.
     * @return GroupDef[] An array of group Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public GroupDef[] getGroupDefs(String domain) 
        throws ProcessingException;
        
    /**
     * Create a new group Def and persist in the database.
     * @param groupDef Group Def.
     * @return String Group Def identifier which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public String createGroupDef(GroupDef groupDef) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group Def and persist in the database.
     * @param groupDef Group Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public void updateGroupDef(GroupDef groupDef) 
        throws ProcessingException, UserException;
    

    /**
     * Delete an existing group Def for the given group Def.
     * @param groupDef Group Def
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public void deleteGroupDef(GroupDef groupDef) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing group Def for the given domain name.
     * @param domain Domain name.
     * @param name Group Def name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or group Def name is 
     * passed as a parameter.
     */
    public void deleteGroupDef(String domain, String name) 
        throws ProcessingException, UserException;
            
    /**
     * Get all group member Defs for the given domain name.
     * @param domain Domain name.
     * @return GroupMemberDef[] An array of group Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public GroupMemberDef[] getGroupMemberDefs(String domain) 
        throws ProcessingException;
        
    /**
     * Create a new group Def and persist in the database.
     * @param groupMemberDef Group Member Def.
     * @return String Group member Def identifier which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public String createGroupMemberDef(GroupMemberDef groupMemberDef) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group member Def and persist in the database.
     * @param groupMemberDef Group Member Def.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public void updateGroupMemberDef(GroupMemberDef groupMemberDef) 
        throws ProcessingException, UserException;
    

    /**
     * Delete an existing group member Def for the given group member Def.
     * @param groupMemberDef GroupMemberDef
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group Def is passed as a parameter.
     */
    public void deleteGroupMemberDef(GroupMemberDef groupMemberDef) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing group Member Def for the given domain name.
     * @param domain Domain name.
     * @param name Group Member Def name.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or group Def name is 
     * passed as a parameter.
     */
    public void deleteGroupMemberDef(String domain, String name) 
        throws ProcessingException, UserException;
}
