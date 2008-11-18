package com.sun.mdm.multidomain.hierarchy;

import java.io.Serializable;
import java.util.Date;

import com.sun.mdm.multidomain.attributes.AttributesDef;

public class HierarchyNodeDef extends AttributesDef implements Serializable {

    private int nodeDefId;
    private Date effectiveFromDate;
    private Date effectiveToDate;
    private Date purgeDate;
    
    public Date getEffectiveFromDate() {
        return effectiveFromDate;
    }
    public void setEffectiveFromDate(Date effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }
    
    public Date getEffectiveToDate() {
        return effectiveToDate;
    }
    
    public void setEffectiveToDate(Date effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    }
    
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public int getNodeDefId() {
        return nodeDefId;
    }
    
    public void setNodeDefId(int nodeDefId) {
        this.nodeDefId = nodeDefId;
    }
}
