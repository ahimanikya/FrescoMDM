package com.sun.mdm.multidomain.synchronization.api;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import com.sun.mdm.multidomain.attributes.AttributesDef;

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
     * Delete a particular rule defined by the rule name in a particular domain.
     * 
     * @param domain the domain in which to delete rule from
     * @param ruleName the name of the rule
     * @return the deleted rule
     */
    public Rule deleteRule(String domain, String ruleName);

    /**
     * Deletes all rules defined in a particular domain.
     * 
     * @param the domain in which to delete all rules from
     */
    public void deleteRules(String domain);
 
    /**
     * Execute a particular rule registered to a specific domain in this engine.
     * 
     * @param domain the domain
     * @param ruleName the name of the rule to execute
     * @param associationType the association type
     * @param sourceEUID the source master record EUID
     * @param eventType the type of event
     */
    public void executeRule(String domain, String ruleName, AttributesDef attributesDef, String sourceEUID,
            String eventType);
    
    /**
     * Execute all rules registered to the specific domain in this engine.
     * 
     * @param domain the domain
     * @param associationType the association type
     * @param sourceEUID the source master record EUID
     * @param eventType the type of event
     */
    public void executeRules(String domain, AttributesDef attributesDef, String sourceEUID,
            String eventType);
    
    /**
     * Execute all rules in all domains registered in this engine.
     * 
     * @param associationType the association type
     * @param sourceEUID the source master record EUID
     * @param eventType the type of event
     */
    public void executeAllRules(AttributesDef attributesDef, String sourceEUID, String eventType);
    
    /**
     * Get a particular rule defined by the rule name.
     * 
     * @param domain the domain to retrieve rule from
     * @param ruleName the rule to retrieve
     * @return
     */
    public Rule getRule(String domain, String ruleName);

    /**
     * Returns registered domains in this engine.
     * 
     * @return the set of registered domains
     */
    public Set<String> getDomains();
    
    /**
     * Return a collection of all rules defined in this engine.
     *  
     * @param domain the domain from which to retrieve all rules from
     * @return collection of rules
     */
    public Collection<Rule> getRulesByDomain(String domain);
    
    /**
     * Return a collection of all rules by rule type.
     * 
     * @param type the type of the rule
     * @return collection of rules
     */
    public Collection<Rule> getRulesByType(RuleType type);

    /**
     * Register a rule in this engine.
     * 
     * @param domain the domain to register this rule to
     * @param rule the rule to register
     */
    public void registerRule(String domain, Rule rule);

    /**
     * Check if rule exists (defined by domain and the rule name)
     * 
     * @param domain the domain to check existence of rule
     * @param ruleName the name of the rule
     * @return true if rule exists, false otherwise
     */
    public boolean ruleExists(String domain, String ruleName);
    
    /**
     * Load rules contained in artifact in this engine.
     * 
     * @param ruleArtifact the rule(s) artifact
     */
    public void loadRules(File ruleArtifact);
}
