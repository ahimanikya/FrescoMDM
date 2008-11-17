package com.sun.mdm.multidomain.hierarchy;

public interface HierarchyActions {

    /**
     * Creates an empty hierarchy.
     * 
     * @param name the name of the hierarchy
     * @param domain the domain
     * @return the hierarchy
     */
    public HierarchyDef createHierarchyDef(String name, String domain);
    
    /**
     * Creates an empty hierarchy node and assigns it to a hierarchy.
     * 
     * @param euid the euid of the node
     * @param hierarchyDef the hierarchy
     * @return the hierarchy node
     */
    public HierarchyNode createHierarchyNode(String euid, HierarchyDef hierarchyDef);
    
    /**
     * Find a particular hierarchy node.
     * 
     * @param hierarchy the hierarchy definition
     * @param node the start node to search from, or null if root
     * @param euid the euid of the node to find
     * @return the hierarchy node as denoted by euid, null if it cannot be found.
     */
    public HierarchyNode findHierarchyNode(HierarchyDef hierarchy, HierarchyNode node, String euid);
    
    /**
     * Retrieve a hierarchy by name and domain.
     * 
     * @param hierarchyName the hierarchy name
     * @param domain the domain
     * @return the hierarchy definition
     */
    public HierarchyDef getHierarchyDef(String hierarchyName, String domain);
    
    /**
     * Add a hierarchy node to a parent node.
     * 
     * @param hierarchy the hierarchy definition
     * @param euid the euid of the parent node, or null if root
     * @param node the node to add
     * @return true if add was successful, false otherwise.
     */
    public boolean addHierarchyNode(HierarchyDef hierarchy, String euid, HierarchyNode node);
    
    /**
     * Move a node from one parent to another parent.
     *
     * @param hierarchy the hierarchy definition
     * @param euid the euid of the node to move
     * @param oldParentEuid the old parent euid
     * @param newParentEuid the new parent euid
     * @return true if move was successful, false otherwise.
     */
    public boolean moveHierarchyNode(HierarchyDef hierarchy, String euid, String oldParentEuid, String newParentEuid);
    
    /**
     * Replace a hierarchy node with a new node.
     * 
     * @param hierarchy the hierarchy definition
     * @param euid the euid of the node to be replaced
     * @param node the new hierarchy node
     * @return true if old node was replaced by new node, false otherwise.
     */
    public boolean replaceHierarchyNode(HierarchyDef hierarchy, String euid, HierarchyNode node);
    
    /**
     * Delete a hierarchy node.
     *
     * @param hierarchy the hierarchy definition
     * @param euid the euid of the node to delete.
     * @return the deleted hierarchy node, or null if node not found
     */
    public HierarchyNode deleteHierarchyNode(HierarchyDef hierarchy, String euid);
}