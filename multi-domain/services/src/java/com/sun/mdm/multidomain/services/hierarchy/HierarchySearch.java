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
package com.sun.mdm.multidomain.services.hierarchy;

import java.util.List;
import java.util.Date;

import com.sun.mdm.multidomain.services.model.AttributeList;
import com.sun.mdm.multidomain.services.model.Attribute;

/**
 * HierarchySearch class.
 * @author cye
 */
public class HierarchySearch extends AttributeList {

    private String name;    
    private String domain;        
    private String startDate;
    private String endDate;
    private String purgeDate;
    
    /**
     * Create an instance of HierarchySearch.
     */
    public HierarchySearch() {
        super();
    }
    
    /**
     * Create an instance of HierarchySearch.
     * @param attributes List of Attribute.
     */
    public HierarchySearch(List<Attribute> attributes) {
        super(attributes);
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDomain() {
        return this.domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    } 
    
    public String getPurgeDate() {
        return purgeDate;
    }
    
    public void setPurgeDate(String purgeDate) {
        this.purgeDate = purgeDate;
    }     
    
}
