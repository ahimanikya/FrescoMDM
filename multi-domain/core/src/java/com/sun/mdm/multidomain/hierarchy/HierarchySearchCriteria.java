package com.sun.mdm.multidomain.hierarchy;

import java.io.Serializable;

import com.sun.mdm.index.objects.SystemObject;

public class HierarchySearchCriteria implements Serializable {

    private HierarchyNode hierarchyNode;
    private SystemObject systemObject;
    
    public HierarchyNode getHierarchyNode() {
        return hierarchyNode;
    }
    
    public void setHierarchyNode(HierarchyNode hierarchyNode) {
        this.hierarchyNode = hierarchyNode;
    }
    
    public SystemObject getSystemObject() {
        return systemObject;
    }
    
    public void setSystemObject(SystemObject systemObject) {
        this.systemObject = systemObject;
    }
}
