package com.sun.mdm.multidomain.hierarchy;

import java.io.Serializable;
import java.util.Date;

public class Hierarchy implements Serializable {

    private int hierarchyId;
    private Date effectiveFrom;
    private Date effectiveTo;
    private Date purgeDate;
    private HierarchyDef hierarchyDef;
    private HierarchyNode rootNode;
    
    public int getHierarchyId() {
        return hierarchyId;
    }
    
    public void setHierarchyId(int hierarchyId) {
        this.hierarchyId = hierarchyId;
    }
    
    public Date getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public Date getEffectiveTo() {
        return effectiveTo;
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
    
    public Date getPurgeDate() {
        return purgeDate;
    }
    
    public void setPurgeDate(Date purgeDate) {
        this.purgeDate = purgeDate;
    }
    
    public HierarchyDef getHierarchyDef() {
        return hierarchyDef;
    }
    
    public void setHierarchyDef(HierarchyDef hierarchyDef) {
        this.hierarchyDef = hierarchyDef;
    }
    
    public HierarchyNode getRootNode() {
        return rootNode;
    }
    
    public void setRootNode(HierarchyNode rootNode) {
        this.rootNode = rootNode;
    } 
}
