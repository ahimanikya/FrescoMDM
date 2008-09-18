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
package com.sun.mdm.multidomain.services.query;

import java.util.Map;
import java.util.HashMap;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;

/**
 * QueryFilter class.
 * @author cye
 */
public class SearchOptions {
  private Map<String, EPathArrayList> ePathLists;
  private int pageSize;
  private int maximumSize;
  private boolean isWeighted;
  
  /**
   * Public constructor.
   */
  public SearchOptions() {
	  ePathLists = new HashMap<String, EPathArrayList>();
	  isWeighted = true;
	  pageSize = 10;
	  maximumSize = 100;;
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
  
  /**
   * Get size of epath array list.
   * @return size
   */
  public int size() {
    return ePathLists != null ? ePathLists.size() : 0;
  }
  
  /**
   * Set epath array list.
   * @param ePathArrayList
   * @param domain
   */
  public void setOptions(String domain, EPathArrayList fields) {
    ePathLists.put(domain, fields);
  } 
  
  /**
   * Get a epath array list for the given domain.
   * @param domain
   * @return epath array list.
   */
  public EPathArrayList getOptions(String domain) {
    return ePathLists.get(domain);
  }        
}
