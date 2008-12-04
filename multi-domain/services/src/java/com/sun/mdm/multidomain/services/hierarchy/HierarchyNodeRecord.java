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

import com.sun.mdm.multidomain.services.model.AttributeList;
import com.sun.mdm.multidomain.services.model.ObjectRecord;
import java.util.Date;

/**
 * HierarchyNodeRecord class.
 * @author cye
 */
public class HierarchyNodeRecord extends AttributeList {

    private String id;
    private String EUID;
    private String parentId;
    private String parentEUID; 
    private String startDate;
    private String endDate;
    private String purgeDate;
    private ObjectRecord objectRecord;
            
    public HierarchyNodeRecord() {
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
            
    public void setEUID(String EUID) {
        this.EUID = EUID;
    }
    
    public String getEUID() {
        return this.EUID;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getParentId() {
        return this.parentId;
    }
            
    public void setParentEUID(String nodeEUID) {
        this.parentEUID = parentEUID;
    }
    
    public String getParentEUID() {
        return this.parentEUID;
    }
    
    public ObjectRecord getObjectRecord() {
        return this.objectRecord;
    }
    
    public void setObjectRecord(ObjectRecord objectRecord) {
        this.objectRecord = objectRecord;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getStartDate() {
        return startDate;
    } 
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setPurgeDate(String purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public String getPurgeDate() {
        return purgeDate;
    } 
}
