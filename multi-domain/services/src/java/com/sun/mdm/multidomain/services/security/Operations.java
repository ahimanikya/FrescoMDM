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
package com.sun.mdm.multidomain.services.security;

import java.util.List;
import java.util.ArrayList;

/**
 * Security Operations.
 * @author cye
 */
public class Operations {
              
    public static final String RelationshipDef_ReadWrite = "RelationshipDef_ReadWrite";
    public static final String RelationshipDef_Read = "RelationshipDef_Read";
    public static final String Relationship_ReadWrite = "Relationship_ReadWrite";
    public static final String Relationship_Read = "Relationship_Read";
    public static final String Relationship_History_Read = "Relationship_History_Read";
    
    public static final String HierarchyDef_ReadWrite = "HierarchyDef_ReadWrite";
    public static final String HierarchyDef_Read = "HierarchyDef_Read";
    public static final String Hierarchy_ReadWrite = "Hierarchy_ReadWrite";
    public static final String Hierarchy_Read = "Hierarchy_Read";
    public static final String Hierarchy_History_Read = "Hierarchy_History_Read";
    
    public static final String GroupDef_ReadWrite = "GroupDef_ReadWrite";
    public static final String GroupDef_Read = "GroupDef_Read";
    public static final String Group_ReadWrite = "Group_ReadWrite ";
    public static final String Group_Read = "Group_Read";
    public static final String Group_History_Read = "Group_History_Read";
    
    public static final String Domain_getDomains = "Domain_getDomains";
    public static final String RelationshipDef_getSensitiveField = "RelationshipDef_getSensitiveField"; 
    public static final String RelationshipDef_getDefs = "RelationshipDef_getDefs";  
    public static final String RelationshipDef_getRelationshipDefs = "RelationshipDef_getRelationshipDefs"; 
    public static final String RelationshipDef_addRelationshipDef = "RelationshipDef_addRelationshipDef";           
    public static final String RelationshipDef_deleteRelationshipDef = "RelationshipDef_deleteRelationshipDef";
    public static final String RelationshipDef_updateRelationshipDef = "RelationshipDef_updateRelationshipDef";
    public static final String RelationshipDef_getRelationshipDefCount = "RelationshipDef_getRelationshipDefCount";
    public static final String RelationshipDef_getDomainRelationshipDefsObjects = "RelationshipDef_getDomainRelationshipDefsObjects";   
         
    public static final String Relationship_getSensitiveField = "Relationship_getSensitiveField"; 
    public static final String Relationship_searchRelationships = "Relationship_searchRelationships";
    public static final String Relationship_getRelationship = "Relationship_getRelationship";
    public static final String Relationship_searchEnterprises = "Relationship_searchEnterprises";
    public static final String Relationship_getEnterprise = "Relationship_getEnterprise";
    public static final String Relationship_addRelationship = "Relationship_addRelationship";
    public static final String Relationship_updateRelationship = "Relationship_updateRelationship";
    public static final String Relationship_deleteRelationship = "Relationship_deleteRelationship";
    public static final String Relationship_searchRelationshipsByRecord = "Relationship_searchRelationshipsByRecord";     
               
    public Operations(){
    }
      
    public boolean isField_VIP() {
        return false;
    }
    
    public static List<String> getOperations() {
        ArrayList<String> operations = new ArrayList<String>();
        
        return operations;
    }
    
    public static List<ACL.Entry> getMethods(String operation) {
        ArrayList<ACL.Entry> methods = new ArrayList<ACL.Entry>();
        
        return methods;       
    }
}
