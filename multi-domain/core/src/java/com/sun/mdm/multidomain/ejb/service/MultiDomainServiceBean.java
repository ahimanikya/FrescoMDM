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

import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionAttribute;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.annotation.security.DeclareRoles;
        
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;

import com.sun.mdm.multidomain.association.AssociationValue;
import com.sun.mdm.multidomain.hierarchy.HierarchyAssociation;
import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.hierarchy.HierarchyObjectTree;
import com.sun.mdm.multidomain.group.Group;
import com.sun.mdm.multidomain.group.GroupMember;

import com.sun.mdm.multidomain.relationship.MultiObject;
import com.sun.mdm.multidomain.query.MultiFieldValuePair;
import com.sun.mdm.multidomain.query.PageIterator;
import com.sun.mdm.multidomain.query.MultiDomainSearchCriteria;
import com.sun.mdm.multidomain.query.MultiDomainSearchOptions;


/**
 * The enterprise beans implementation of MultiDomainService that is exposed to the clients.
 * @author SwaranjitDua
 * @author cye
 */
@Stateless(mappedName="ejb/MULTIDOMAIN_APPLICATION_TOKEN_MultiDomainService")
@Remote(MultiDomainServiceRemote.class)
@Local(MultiDomainServiceLocal.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
@DeclareRoles({"MultiDomain.Admin","MultiDomain.User"})
/*
@EJB will be decided.
@Resources will be decided.
*/
public class MultiDomainServiceBean implements MultiDomainServiceRemote, MultiDomainServiceLocal {

    private SessionContext sessionContext;
      
    /**
     * Set SessionContext and called by the container when the bean is created.
     * @param sessionContext SessionContext.
     */
    @Resource
    public void setSessionContext(SessionContext sessionContext){
    	  this.sessionContext = sessionContext;
    }
     
    /**
     * Initialize resources.And onInitialization invocation occurs before the first 
     * business method invocation.
     * @throws ProcessingException Thrown if an error occurs during processing.
     */
    @PostConstruct
    public void onInitialization() 
        throws ProcessingException {
    }
    
    /**
     * Clean resources. And onTermination invocation occurs before the instance is 
     * removed by the container.
     */
    @PreDestroy
    public void onTermination(){
    }    
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createRelationship()
     */        
    public String createRelationship(Relationship relationship)
    throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createRelationship()
     */            
    
    public String createRelationship(String sourceSystemCode, String sourceLID, 
            String targetSystemCode, String targetLID, 
            AssociationValue relationshipValue)     
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createRelationship()
     */            
    public String createRelationship(MultiFieldValuePair sourceMultiPairValue, MultiFieldValuePair targetMultiPairValue, 
            AssociationValue relationshipValue) 
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#updateRelationship()
     */ 
    public void updateRelationship(Relationship relationship)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#deleteRelationship()
     */   
    public void deleteRelationship(String relationshipid) 
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#deleteRelationship()
     */
    public void deleteRelationship(String sourceSystemCode, String sourceLID, 
            String targetSystemCode, String targetLID, String sourceDomain, String targetDomain, String relationshipName)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#searchRelationships()
     */                
    public PageIterator<MultiObject> searchRelationships(MultiDomainSearchOptions searchOptions, MultiDomainSearchCriteria searchCriteria) 
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
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
    public PageIterator<ObjectNode> searchEnterprises(String domain, EOSearchOptions searchOptions, EOSearchCriteria searchCriteria)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createHierarchy()
     */ 
    public String createHierarchy(HierarchyAssociation hierarchy)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createHierarchy()
     */  
    public String[] createHierarchy(MultiFieldValuePair parentFieldValues, MultiFieldValuePair[] childFieldValues, AssociationValue hierarchyValue)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#deleteHierarchy()
     */ 
    
    public void deleteHierarchy(String hierarchyid)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }


    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#updateHierarchy()
     */ 
    public void updateHierarchy(HierarchyAssociation hierarchy)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#searchHierarchy()
     */
    public HierarchyObjectTree searchHierarchy(String hierarchytypeid, String EUID, EPathArrayList ePathFields)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
            
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createGroup()
     */                                    
    public String createGroup(Group group)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#createGroupMembers()
     */                                        
    public String[] createGroupMembers(GroupMember[] members)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
            
 
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#deleteGroup()
     */                                            
    public void deleteGroup(String groupId)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#deleteGroupMember()
     */                                            
    public void deleteGroupMember(String groupMemberID)
        throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
    
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#updateGroup()
     */                                            
    public void updateGroup(Group group)
            throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
            
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#searchGroup()
     */                                            
    public Group[] searchGroup(Group group)
            throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#getGroupMembers()
     */                                            
    public ObjectNode[] getGroupMembers(String groupId, EPathArrayList fields) 
            throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
            
    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#getGroups()
     */                                            
    public Group[] getGroups(String EUID)
            throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }

    /**
     * @see com.sun.mdm.multidomain.ejb.service.MultiDomainService#updateGroupMember()
     */                                            
    public void updateGroupMember(GroupMember groupMember) 
            throws ProcessingException, UserException {
        throw new ProcessingException("Not Implemented Yet.");
    }
                    
}
