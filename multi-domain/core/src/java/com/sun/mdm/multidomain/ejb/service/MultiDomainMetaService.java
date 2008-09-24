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
        
/**
 * MultiDomainMetaService interface.
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
     * Get all relationship types for all domains.
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
     * @return String Relationship type identifier which is newly updated.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship type is passed as a parameter.
     */
    public String updateRelationshipType(RelationshipType relationshipType)  
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relation type from the database.
     * @param relationshipType RelationshipType.
     * @return String Relationship type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid relationship type is passed as a parameter.
     */
    public String deleteRelationshipType(RelationshipType relationshipType)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing relation type from the database for the given source domain 
     * and the target domain and relationship type name.
     * @param sourceDomain Source domain name.
     * @param targetDomain Target domain name.
     * @param relationshipType Relationship type name.
     * @return String Relationship type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source domain or target domain or 
     * relationship type name is passed as a parameter.
     */
    public String deleteRelationshipType(String sourceDomain, String targetDomain, String relationshipTypeName)  
        throws ProcessingException, UserException;
    
    /**
     * Get all Hierarchy types for the given domain.
     * @param domain Domain name.
     * @return RelationshipType[] An array of relationship type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name is passed as a parameter.
     */
    public RelationshipType[] getHierarchyTypes(String domain) 
        throws ProcessingException, UserException;
   
    /**
     * Create a new hierarchy type and persist the hierarchy type in the database.
     * @param hierarchyType RelationshipType.
     * @return String Hierarchy type identifier which is newly created. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public String createHierarchyType(RelationshipType hierarchyType)  
        throws ProcessingException, UserException;
    
    
    /**
     * Update an existing hierarchy type and persist the hierarchy type in the database.
     * @param hierarchyType RelationshipType.
     * @return String Hierarchy type identifier which is newly updated. 
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public String updateHierarchyType(RelationshipType hierarchyType)  
        throws ProcessingException, UserException;

    /**
     * Delete an existing hierarchy type from the database for the given hierarchy type.
     * @param hierarchyType RelationshipType.
     * @return String Hierarchy type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid hierarchy type is passed as a parameter.
     */
    public String deleteHierarchyType(RelationshipType hierarchyType) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing hierarchy type from the database for the given domain and 
     * hierarchy type name.
     * @param domain Domain name.
     * @param name HierarchyType name.
     * @return String Hierarchy type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or hierarchy name is 
     * passed as a parameter.
     */
    public String deleteHierarchyType(String domain, String name) 
        throws ProcessingException, UserException;
         
    /**
     * Get aal group types for the given domain name.
     * @param domain Domain name.
     * @return RelationshipType[] An array of group type.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    public RelationshipType[] getGroupTypes(String domain) 
        throws ProcessingException;
        
    /**
     * Create a new group type and persist in the database.
     * @param groupType RelationshipType.
     * @return String Group type identifier which is newly created.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public String createGroupType(RelationshipType groupType) 
        throws ProcessingException, UserException;
    
    /**
     * Update an existing group type and persist in the database.
     * @param groupType RelationshipType.
     * @return String Group type identifier which is newly updated.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public String updateGroupType(RelationshipType groupType) 
        throws ProcessingException, UserException;
    

    /**
     * Delete an existing group type for the given group type.
     * @param groupType RelationshipType
     * @return String Group type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid group type is passed as a parameter.
     */
    public String deleteGroupType(RelationshipType groupType) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing group type for the given domain name.
     * @param domain Domain name.
     * @param name Group type name.
     * @return String Group type identifier which is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid domain name or group type name is 
     * passed as a parameter.
     */
    public String deleteGroupType(String domain, String name) 
        throws ProcessingException, UserException;
            
}
