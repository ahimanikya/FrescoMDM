/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.objects.configuration;

import com.sun.mdm.multidomain.relationship.Relationship;

import java.util.HashMap;
import java.util.ArrayList;

public class Domain {
    private Integer domainID;           // needed?
    private String domainName;          // name of the domain
    private HashMap<String, Relationship> relationships;      // Names of Relationships.
//    private DirectedTree hierarchies;       // directed tree for all Hierarchy instances
    private HashMap<String, Relationship> groups;         // each entry contains a Group
//    private EnterpriseObject  eo;           // EO for the domain (is this needed?)
//    private EIndexObject  eindexObject;     // eIndexObject
    private ArrayList domainAttributes;     // ArrayList of custom attributes (ObjectAttributes)

    public Domain() {
    }
    
    public Integer getDomainID() {  //  retrieves the domain ID.
        return domainID;
    }

    public void setDomainID(Integer id) {  //  sets the domain ID.
    }

    public String getDomainName() {  //  retrieves the domain name.
        return domainName;
    }

    public void setDomainName(String hierarchyName) {  //  retrieves the domain name.
    }

//    public SummaryID getSummaryID() {  //  retrieves the SummaryID object for the domain.
//    }

//    public void getSummaryID(SummaryID) {  //  sets the SummaryID object for the domain.
//    }

//    public DirectedTree getHierarchies() {  //  retrieves all the hierarchies for the domain.
//    }

//    public void setHierarchies(DirectedTree hierarchies) {  //  set the hierarchies for the domain.
//    }

    public ArrayList<Relationship> getRelationships() {  // retrieves all Relationships for the domain.
        return new ArrayList(relationships.values());
    }

    public Relationship getRelationship(String relationshipName, String targetDomain) {  //  retrieves a relationship for the domain and the targetDomain.
        return null;
    }

    public void addRelationship(Relationship relationship, String targetDomain) {  //  adds a relationship for the domain and the targetDomain.
    }

    public void deleteRelationship(String relationshipName, String targetDomain) {  //  deletes a relationship for the domain and the targetDomain.
    }

    public ArrayList<Relationship> getGroups() {  // retrieves all Groups for the domain.
        return new ArrayList(groups.values());
    }

//    public Group getGroup(String groupName) {  //  retrieves a group for the domain.
//    }

//    public void addGroup(Group group) {  //  adds a group for the domain.
//    }

    public Relationship getGroup(String groupName) {  //  retrieves a group for the domain.
        return null;
    }

    public void addGroup(Relationship group) {  //  adds a group for the domain.
    }

    public void deleteGroup(String group) {  //  deletes a group for the domain.
    }

}
