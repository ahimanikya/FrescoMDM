package com.sun.mdm.multidomain.hierarchy;

import java.io.Serializable;
import java.util.List;

public class HierarchyTree implements Serializable {

    private HierarchyNode node;
    private List<HierarchyNode> ancestors;
    private List<HierarchyNode> children;
    
    public HierarchyNode getNode() {
        return node;
    }
    
    public void setNode(HierarchyNode node) {
        this.node = node;
    }
    
    public List<HierarchyNode> getAncestors() {
        return ancestors;
    }
    
    public void setAncestors(List<HierarchyNode> ancestors) {
        this.ancestors = ancestors;
    }
    
    public List<HierarchyNode> getChildren() {
        return children;
    }
    
    public void setChildren(List<HierarchyNode> children) {
        this.children = children;
    }
    
}
