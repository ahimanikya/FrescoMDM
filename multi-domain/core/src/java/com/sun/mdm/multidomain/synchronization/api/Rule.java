package com.sun.mdm.multidomain.synchronization.api;

import com.sun.mdm.multidomain.association.Domain;
import com.sun.mdm.multidomain.relationship.RelationshipType;

public interface Rule {
    
    /**
     * Execute this rule
     * 
     * @param sourceDomain the source domain
     * @param targetDomain the target domain
     * @param relationshipType the relationship type
     */
    public void execute(Domain sourceDomain, Domain targetDomain, RelationshipType relationshipType);
    
    /**
     * Return name of this rule.
     * 
     * @return the rule name
     */
    public String getName();
}
