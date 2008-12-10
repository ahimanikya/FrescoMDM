package com.sun.mdm.multidomain.synchronization.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.multidomain.attributes.AttributesDef;
import com.sun.mdm.multidomain.ejb.service.MultiDomainService;
import com.sun.mdm.multidomain.synchronization.api.Rule;
import com.sun.mdm.multidomain.synchronization.api.RuleType;
import com.sun.mdm.multidomain.synchronization.api.SynchronizationEngine;

public class DefaultSynchronizationEngine  implements SynchronizationEngine {

    private Map<String, Map<String, Rule>> rules;
    private MultiDomainService multiDomainService;
    
    public DefaultSynchronizationEngine() {
        rules = new HashMap<String, Map<String, Rule>>();
    }

    @Override
    public Rule deleteRule(String domain, String ruleName) {
        Map<String, Rule> domainRules = rules.get(domain);
        
        if (domainRules != null) {
            return domainRules.remove(ruleName);
        }
        
        return null;
    }

    @Override
    public void deleteRules(String domain) {
        Map<String, Rule> domainRules = rules.get(domain);
        
        if (domainRules != null) {
            domainRules.clear();
        }
        
    }

    @Override
    public void executeAllRules(AttributesDef attributesDef,
            String sourceEUID, String eventType) {
        
        Collection<Map<String, Rule>> values = rules.values();
        for (Map<String, Rule> allDomainRules : values) {
            Collection<Rule> domainRules = allDomainRules.values();
            for (Rule rule : domainRules) {
                execute(rule, attributesDef, sourceEUID, eventType);
            }
        }
    }

    @Override
    public void executeRule(String domain, String ruleName,
            AttributesDef attributesDef, String sourceEUID, String eventType)  {

        Map<String, Rule> domainRules = rules.get(domain);
        Rule rule = null;
        
        if (domainRules != null) {
            rule = domainRules.get(ruleName);
        }
        
        execute(rule, attributesDef, sourceEUID, eventType);
    }

    @Override
    public void executeRules(String domain, AttributesDef attributesDef, String sourceEUID,
            String eventType) {
        
        Map<String, Map<String, Rule>> myRules = null;
        Map<String, Rule> domainRules = myRules.get(domain);
        
        Collection<Rule> values = domainRules.values();
        for (Rule rule : values) {
            execute(rule, attributesDef, sourceEUID, eventType);
        }
    }
    
    private void execute(Rule rule, AttributesDef attributesDef, String sourceEUID, String eventType) {
        try {
            if (rule != null) {
                rule.execute(attributesDef, sourceEUID, eventType, this.multiDomainService);
            }
        } catch (ProcessingException pe) {
//TODO handle this            
        } catch (UserException ue) {
//TODO handle this            
        }
    }
    
    @Override
    public Rule getRule(String domain, String ruleName) {
        Map<String, Rule> domainRules = rules.get(domain);
        
        if (domainRules != null) {
            return domainRules.get(ruleName);
        }
        
        return null;
    }

    @Override
    public Collection<Rule> getRulesByDomain(String domain) {
        Map<String, Rule> domainRules = rules.get(domain);
        
        if (domainRules != null) {
            return domainRules.values();
        }
        
        return null;
    }
    
    public Collection<Rule> getRulesByType(RuleType type) {
        List<Rule> listOfRules = new ArrayList<Rule>();
        
        Collection<Map<String, Rule>> values = rules.values();
        for (Map<String, Rule> allDomainRules : values) {
            Collection<Rule> domainRules = allDomainRules.values();
            for (Rule rule : domainRules) {
                if (rule.getType().equals(type)) {
                    listOfRules.add(rule);
                }
            }
        }
        
        return listOfRules;
    }

    @Override
    public void loadRules(File ruleArtifact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerRule(String domain, Rule rule) {
        Map<String, Rule> domainRules = rules.get(domain);
  
        if (domainRules == null) {
            domainRules = new HashMap<String, Rule>();
            rules.put(domain, domainRules);
        }
        
        domainRules.put(rule.getName(), rule);
    }

    @Override
    public boolean ruleExists(String domain, String ruleName) {
        Map<String, Rule> domainRules = rules.get(domain);
        
        if (domainRules != null) {
            return domainRules.containsKey(ruleName);
        }
        
        return false;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<String> getDomains() {
        return rules.keySet();
    }

    /**
     * @return the rules
     */
    public Map<String, Map<String, Rule>> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(Map<String, Map<String, Rule>> rules) {
        this.rules = rules;
    }

    /**
     * @return the multiDomainService
     */
    public MultiDomainService getMultiDomainService() {
        return multiDomainService;
    }

    /**
     * @param multiDomainService the multiDomainService to set
     */
    public void setMultiDomainService(MultiDomainService multiDomainService) {
        this.multiDomainService = multiDomainService;
    }


}
