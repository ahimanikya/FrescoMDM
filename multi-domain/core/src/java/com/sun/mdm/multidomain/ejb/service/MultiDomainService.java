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

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.HierarchyObject;

import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.PageMultiIterator;
import com.sun.mdm.multidomain.query.PageSingleIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;
 
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
     * @return String Relationship identifier if the relationship is newly updated.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID and target EUID or relationship is passed as a parameter.
     */
    public String updateRelationship(String sourceEUID, String targetEUID, Relationship relationship) 
        throws ProcessingException, UserException;
    
    /**
     * Delete an existing relationship for the given source EUID and target EUID.
     * @param sourceEUID Source entity EUID.
     * @param targetEUID Target entity EUID.
     * @param relationshp Relationship including fixed and extended attributes associates two EUIDs
     * @return String Relationship identifier if the relationship is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source EUID and target EUID or relationship is passed as a parameter.
     */
    public String deleteRelationship(String sourceEUID, String targetEUID, Relationship relationshp) 
        throws ProcessingException, UserException;
            
    /**
     * Delete an existing relationship for the given source system LID and target system LID and relationship.
     * @param sourceSystemCode Source system code.
     * @param sourceLID Source system entity LID.
     * @param targetSystemCode Target system code.
     * @param targetLID Target system entity LID.
     * @param relationship Relationship including fixed and extended attributes associates two LIDs.
     * @return String Relationship identifier if the relationship is newly deleted.
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid source system code and 
     * target system code or relationship is passed as a parameter.
     */
    public String deleteRelationship(String sourceSystemCode, String sourceLID, 
                                     String targetSystemCode, String targetLID, Relationship relationship)
        throws ProcessingException, UserException;

    /**
     * Search 
     * @param searchOptions
     * @param searchCriteria
     * @return
     * @throws ProcessingException Thrown if an error occurs during processing.
     * @throws UserException Thrown if an invalid searchOptions or searchCriteria is passed as a parameter.
     */
    public PageMultiIterator searchRelationships(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException;
            
    public PageMultiIterator searchRelationships(String sourceDomain, EPathArrayList[] sourceEPathList, 
                                                 String targetDomain, EPathArrayList[] targetEPathList, MultiDomainSearchOptions searchOptions) 
        throws ProcessingException, UserException;
    
    public PageSingleIterator searchEnterprise(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria searchCriteria)
        throws ProcessingException, UserException;
    
    public String createHierarchy(String parentEUID, String childEUID, Relationship relationship)
        throws ProcessingException, UserException;
    
    public String createHierarchy(String parentEUID, String[] childEUIDs, Relationship relationship)
        throws ProcessingException, UserException;
    
    public String createHierarchy(MultiFieldValuePair parentFieldValues, MultiFieldValuePair[] childFieldValues, Relationship relationship)
        throws ProcessingException, UserException;


    public String deleteHierarchy(String parentEUID, String childEUID, Relationship relationship)
        throws ProcessingException, UserException;
    
    public String deleteHierarchy(String parentEUID, String[] childEUIDs, Relationship relationship)
        throws ProcessingException, UserException;
    
    public String updateHierarchy(String parentEUID, String childEUID, Relationship relationship)
        throws ProcessingException, UserException;
    
    public String updateHierarchy(String parentEUID, String[] childEUIDs, Relationship relationship)
        throws ProcessingException, UserException;
    
    public HierarchyObject searchHierarchy(String domain, String EUID, EPathArrayList ePathFields)
        throws ProcessingException, UserException;
            
    public String createGroup(Relationship relationship)
        throws ProcessingException, UserException;
            
    public String[] createGroups(String groupId, String EUIDs[], Relationship relationship)
        throws ProcessingException, UserException;
            
    public String deleteGroup(Relationship group)
            throws ProcessingException, UserException;
    
    public String deleteGroup(String domain, String groupId)
        throws ProcessingException, UserException;

    public String deleteGroup(String domain, String groupId, String EUID)
        throws ProcessingException, UserException;
    
    public String updateGroup(Relationship group)
            throws ProcessingException, UserException;
            
    public Relationship[] searchGroup(Relationship group)
            throws ProcessingException, UserException;
    
    public ObjectNode[] getGroupMembers(String domain, String groupId, EPathArrayList fields) 
            throws ProcessingException, UserException;
            
    public Relationship[] getGroups(String domain, String EUID)
            throws ProcessingException, UserException;

    public String updateGroup(String domain, String EUID, Relationship group) 
            throws ProcessingException, UserException;    
                
}
