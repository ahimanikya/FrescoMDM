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

    private String nodeId;
    private String nodeEUID;
    private String parentId;
    private String parentEUID; 
    private Date startDate;
    private Date endDate;
    private Date purgeDate;
    private ObjectRecord objectRecord;
            
    public HierarchyNodeRecord() {
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public String getNodeId() {
        return this.nodeId;
    }
            
    public void setNodeEUID(String nodeEUID) {
        this.nodeEUID = nodeEUID;
    }
    
    public String getNodeEUID() {
        return this.nodeId;
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
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getStartDate() {
        return startDate;
    } 
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    } 
}
