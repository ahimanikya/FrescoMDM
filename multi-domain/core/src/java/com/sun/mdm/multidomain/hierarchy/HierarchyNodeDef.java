package com.sun.mdm.multidomain.hierarchy;

import java.io.Serializable;

import com.sun.mdm.multidomain.attributes.AttributesDef;

public class HierarchyNodeDef extends AttributesDef implements Serializable {

    private int nodeDefId;
        
    public int getNodeDefId() {
        return nodeDefId;
    }
    
    public void setNodeDefId(int nodeDefId) {
        this.nodeDefId = nodeDefId;
    }
}
