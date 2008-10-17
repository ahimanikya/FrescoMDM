package com.sun.mdm.multidomain.synchronization.api;

import java.io.File;
import java.util.Collection;

import com.sun.mdm.multidomain.association.AssociationType;

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
     * Execute a particular rule defined by name to establish a association between two EUID's.
     * 
     * @param ruleName the rule name to execute
     * @param associationType the association type
     * @param sourceEUID the source record EUID
     * @param targetEUID the target record EUID
     */
    public void executeRule(String ruleName, AssociationType associationType, String sourceEUID);
    
   /**
     * Execute all rules defined in this engine to establish a association between two EUID's.
     * 
     * @param associationType the association type
     * @param sourceEUID the source record EUID
     * @param targetEUID the target record EUID
     */
    public void executeRules(AssociationType associationType, String sourceEUID);
       
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
