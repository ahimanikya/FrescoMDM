package com.sun.mdm.multidomain.synchronization.api;

import java.io.File;
import java.util.Collection;

import com.sun.mdm.multidomain.association.Domain;
import com.sun.mdm.multidomain.relationship.RelationshipType;

public interface SynchronizationEngine {

    /**
     * Start the engine
     * 
     */
    public void start();
    
    /**
     * Stop the engine.
     * 
     */
    public void stop();
    
    /**
     * Delete a particular rule defined by the rule name.
     * 
     * @param ruleName the name of the rule
     * @return the deleted rule
     */
    public Rule deleteRule(String ruleName);

    /**
     * Deletes all rules defined in this engine.
     * 
     */
    public void deleteRules();

    /**
     * Execute a particular rule defined by the rule name.
     * 
     * @param ruleName the rule name
     * @param sourceDomain the source domain
     * @param targetDomain the target domain
     * @param relationshipType the relationship type
     */
    public void executeRule(String ruleName, Domain sourceDomain, Domain targetDomain,
        RelationshipType relationshipType);

    /**
     * Execute all rules defined in this engine.
     * 
     * @param sourceDomain the source domain
     * @param targetDomain the target domain
     * @param relationshipType the relationship type
     */
    public void executeRules(Domain sourceDomain, Domain targetDomain, 
        RelationshipType relationshipType);
    
    /**
     * Get a particular rule defined by the rule name.
     * 
     * @param ruleName the rule to retrieve
     * @return
     */
    public Rule getRule(String ruleName);

    /**
     * Return a collection of all rules defined in this engine.
     *  
     * @return collection of rules
     */
    public Collection<Rule> getRules();

    /**
     * Register a rule in this engine.
     * 
     * @param ruleName the name of the rule
     * @param rule the rule to register
     */
    public void registerRule(String ruleName, Rule rule);

    /**
     * Check if rule exists (defined by the rule name)
     * 
     * @param ruleName the name of the rule
     * @return true if rule exists, false otherwise
     */
    public boolean ruleExists(String ruleName);
    
    /**
     * Load rules contained in artifact in this engine.
     * 
     * @param ruleArtifact the rule(s) artifact
     */
    public void loadRules(File ruleArtifact);
}
