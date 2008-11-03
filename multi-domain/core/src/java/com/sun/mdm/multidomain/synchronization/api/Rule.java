package com.sun.mdm.multidomain.synchronization.api;

public interface Rule {
    
    /**
     * Return name of this rule.
     * 
     * @return the rule name
     */
    public String getName();
    
    /**
     * Return the type of this rule.
     * 
     * @return the type
     */
    public RuleType getType();
    
    /**
     * Return a string describing this rule.
     * 
     * @return the description
     */
    public String getDescription();
}
