/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.multidomain.query;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.io.Serializable;        

import com.sun.mdm.index.objects.epath.EPathArrayList;
import java.io.Serializable;

/**
 * MultiDomainSearchOptions class.
 * @author SwaranjitDua
 * @author cye
 */
public class MultiDomainSearchOptions implements Serializable {
    /**
     * EPathArrayList is defined in Master Index. Each domain has a EPathArrayList
     * which contains fully-qualified field name named FQFN. e.g., Person.FirstName,
     * Person.LastName.
     */
    private Map<String, DomainSearchOption> domainSearchOptions 
            = new HashMap<String, DomainSearchOption>();
    /**
     * Maximum elements to be retrieved.
     */
    private int maxElements; 
    /**
     * Page size.
     */
    private int pageSize;
    
    private String primaryDomain;
 
    
  /**
   * Public constructor.
   */
  public MultiDomainSearchOptions() {
	  pageSize = 10;
	  maxElements = 100;;
  }
  
  /**
   * Get the page size.
   * @return page size
   */
  public int getPageSize() {
    return pageSize;
  }
  
  /**
   * Set the page size.
   * @param pageSize
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
  public String getPrimaryDomain() {
      return primaryDomain;
  }
  
  public void setPrimaryDomain(String primaryDomain) {
      this.primaryDomain = primaryDomain;
  }
  /**
   * Get maxElements.
   * @return maxElements;
   */
  public int getMaxElements() {
	  return maxElements;
  }
  
  /**
   * Set maxElements.
   * @param maxElements
   */
  public void setMaxElements(int maxElements) {
	  this.maxElements = maxElements;
  }
  
 
  
  /**
   * Set Domain Search Option.
   * @param searchOptions A domain specific search options
   * @param domain
   */
  public void setOptions(String domain, DomainSearchOption searchOptions) {
    domainSearchOptions.put(domain, searchOptions);
  } 
  
  /**
   * Get Domain specific search option for the given domain.
   * @param domain
   * @return DomainSearchOption
   */
  public DomainSearchOption getOptions(String domain) {
    return domainSearchOptions.get(domain);
  }            
  
  /**
   * Get complete map of all Domain specific search option.
   * @return Map<domain name, DomainSearchOption>
  
   */
  public Map<String, DomainSearchOption> getOptions() {
    return domainSearchOptions;
  }
  
  public Set<String> getDomains() {
        return domainSearchOptions.keySet();
    }
  
  
  public static class DomainSearchOption implements Serializable  {
    
    private String domain;
    /**
     * Whether to use weighted option.
     */
    private boolean isWeighted;
    /**
     * QueryBuilder serach Id.
     */
    private String searchId;
    
    private EPathArrayList ePathArrayList;
   
       /**
   * Get isWeighted.
   * @return isWeighted
   */
  public boolean getIsWeighted() {
	  return isWeighted;
  }
  
  /**
   * Set IsWeighted.
   * @param isWeighted
   */
  public void setIsWeighted(boolean isWeighted) {
	  this.isWeighted = isWeighted;
  }
  
  /**
   * Get searchId.
   * @return searchId
   */
  public String getSearchId() {
	  return searchId;
  }
  
  /**
   * Set searchId.
   * @param searchId
   */
  public void setSearchId(String searchId) {
	  this.searchId = searchId;
  }
  
  /**
   * Set epath array list.
   * @param ePathArrayList
   * @param domain
   */
  public void setOptions(EPathArrayList fields) {
    ePathArrayList = fields;
  } 
  
  /**
   * Get a epath array list for the given domain.
   * @param domain
   * @return epath array list.
   */
  public EPathArrayList getOptions() {
    return ePathArrayList;
  }
  
  /**
   * set domain
   * @param domain
   */
  public void setDomain(String domain) {
      this.domain = domain;
  }
  
  /**
   * get Domain 
   * @return domain name
   */
  
  public String getDomain() {
      return domain;
  }
            
  } 
}
