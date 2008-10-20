package com.sun.mdm.multidomain.synchronization.api;

public interface Rule {
    
    public static final String RELATIONSHIP = "RELATIONSHIP";
    public static final String HIERARCHY = "HIERARCHY";
    public static final String GROUP = "GROUP";
    public static final String CATEGORY = "CATEGORY";
    
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
    public String getType();
}
