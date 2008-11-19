package com.sun.mdm.multidomain.synchronization.api;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.multidomain.attributes.AttributesDef;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;

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
    
    /**
     * Execute this rule.
     * 
     * @param attributesDef the attributes definition
     * @param sourceEUID the source record EUID
     * @param eventType the event type
     */
    public void execute(AttributesDef attributesDef, String sourceEUID, String eventType,
            MultiDomainService multiDomainService) throws ProcessingException, UserException;
}
