package com.sun.mdm.multidomain.synchronization.api;

import com.sun.mdm.multidomain.hierarchy.HierarchyDef;
import com.sun.mdm.multidomain.hierarchy.HierarchyNode;

public interface HierarchyRule extends Rule {

    /**
     * Establish a possible hierarchy between a source record EUID 
     * and target record EUID.
     * 
     * @param hierarchyType the hierarchy type
     * @param sourceEUID the source record EUID
     * @return the hierarchy association
     */
    public HierarchyNode createHierarchy(HierarchyDef hierarchyDef, String sourceEUID);
}
