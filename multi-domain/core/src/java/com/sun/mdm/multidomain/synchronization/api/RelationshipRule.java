package com.sun.mdm.multidomain.synchronization.api;

import com.sun.mdm.multidomain.relationship.Relationship;
import com.sun.mdm.multidomain.relationship.RelationshipType;

public interface RelationshipRule extends Rule {

    /**
     * Establish a possible relationship between a source record EUID 
     * and target record EUID.
     * 
     * @param relationshipType the relationship type
     * @param sourceEUID the source record EUID
     * @return the relationship or null if none
     */
    public Relationship createRelationship(RelationshipType relationshipType, String sourceEUID);
    
}
