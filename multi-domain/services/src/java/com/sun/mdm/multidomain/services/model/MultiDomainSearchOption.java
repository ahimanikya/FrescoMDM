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
package com.sun.mdm.multidomain.services.model;

import com.sun.mdm.index.objects.epath.EPathArrayList;

/**
 * MultiDomainSearchOption class.
 * @author cye
 */
public class MultiDomainSearchOption {

    private EPathArrayList ePathArrayList;
    private int maxElements; 
    private int pageSize;
    private boolean isWeighted;
    private String searchId;
    
    /* This class should be defined in multidomain core service.*/
    public MultiDomainSearchOption(){        
    }
    
    public EPathArrayList getEPathArrayList(){
        return ePathArrayList;
    }
    
    public void setEPathArrayList(EPathArrayList ePathArrayList){
        this.ePathArrayList = ePathArrayList;
    }
    
    public boolean getIsWeighted(){
        return isWeighted;
    }
    
    public void setIsWeighted(boolean isWeighted){
        this.isWeighted = isWeighted;
    }
    
    public String getSearchId(){
        return searchId;
    }
    
    public void setSearchId(String searchId){
        this.searchId = searchId;
    }
    
    public int getPageSize(){
        return pageSize;
    }
    
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    
    public int getMaxElements(){
        return maxElements;
    }  
    
    public void setMaxElements(int maxElements){
        this.maxElements = maxElements;
    }        
}
