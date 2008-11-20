/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author wee
 * @version 
 */
public class DomainsForWebManager {
    
    private ArrayList<DomainForWebManager> mDomains = new ArrayList<DomainForWebManager>();
    
    public ArrayList<DomainForWebManager> getDomains() {
        return this.mDomains;
    }
    
    public void addDomain(DomainForWebManager domain) {
        this.mDomains.add(domain);
    }
    
    public void removeDomain(DomainForWebManager domain) {
        this.mDomains.remove(domain);
    }
    
    public DomainForWebManager getDomain(String domainName) {
        for (DomainForWebManager domain : mDomains) {
            if (domain.getDomainName().equals(domainName)) {
                return domain;
            }
        }
        
        
        return null;
    }

    public void removeDomain(String domainName) {
        for (DomainForWebManager domain : mDomains) {
            if (domain.getDomainName().equals(domainName)) {
                mDomains.remove(domain);
                break;
            }
        }
        
    }
    
}
