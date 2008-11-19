package com.sun.mdm.multidomain.hierarchy;

import java.util.List;

import com.sun.mdm.multidomain.attributes.Attribute;

public interface HierarchyActions {

    /**
     * Creates an empty hierarchy.
     * 
     * @param name the name of the hierarchy
     * @param domain the domain
     * @return the hierarchy definition
     */
    public HierarchyDef createHierarchyDef(String name, String domain);
    
    /**
     * Create a hierarchy instance.
     * 
     * @param hierarchyDef the hierarchy definition
     * @param root the root node, or null for an empty hierarchy
     * @return the hierarchy
     */
    public Hierarchy createHierarchy(HierarchyDef hierarchyDef, HierarchyNode root);
    
    /**
     * Create a hierarchy node definition.
     * 
     * @param attributes the list of attributes
     * @return the hierarchy node definition
     */
    public HierarchyNodeDef createHierarchyNodeDef(List<Attribute> attributes);
    
    /**
     * Creates an empty hierarchy node and assigns it to a hierarchy.
     * 
     * @param euid the euid of the node
     * @param hierarchyDef the hierarchy
     * @return the hierarchy node
     */
    public HierarchyNode createHierarchyNode(String euid, HierarchyDef hierarchyDef);
    
    /**
     * Find a particular hierarchy node, starting from the specified node.
     * 
     * @param hierarchy the hierarchy
     * @param node the start node to search from, or null if root
     * @param euid the euid of the node to find
     * @return the hierarchy node as denoted by euid, null if it cannot be found.
     */
    public HierarchyNode findHierarchyNode(Hierarchy hierarchy, HierarchyNode node, String euid);
    
    /**
     * Find a particular hierarchy node, starting from the root node.
     * 
     * @param hierarchy the hierarchy
     * @param euid the euid of the node to find
     * @return the hierarchy node as denoted by euid, null if it cannot be found.
     */
    public HierarchyNode findHierarchyNode(Hierarchy hierarchy, String euid);
    
    /**
     * Retrieve a hierarchy definition by name and domain.
     * 
     * @param hierarchyName the hierarchy name
     * @param domain the domain
     * @return the hierarchy definition
     */
    public HierarchyDef getHierarchyDef(String hierarchyName, String domain);

    /**
     * Retrieve the list of hierarchy instances as defined by a hierarchy definition.
     * 
     * @param hierarchyDef the hierarchy definition
     * @return list of hierarchy instances
     */
    public List<Hierarchy> getHierarchies(HierarchyDef hierarchyDef);
    
    /**
     * Retrieve the root node of the hierarchy.
     * 
     * @param hierarchy the hierarchy instance
     * @return the root node, or null if empty hierarchy
     */
    public HierarchyNode getRootNode(Hierarchy hierarchy);
    
    /**
     * Add a hierarchy node to a parent node.
     * 
     * @param hierarchy the hierarchy
     * @param euid the euid of the parent node, or null if root
     * @param node the node to add
     * @return true if add was successful, false otherwise.
     */
    public boolean addHierarchyNode(Hierarchy hierarchy, String euid, HierarchyNode node);
    
    /**
     * Move a node from one parent to another parent.
     *
     * @param hierarchy the hierarchy
     * @param euid the euid of the node to move
     * @param oldParentEuid the old parent euid
     * @param newParentEuid the new parent euid
     * @return true if move was successful, false otherwise.
     */
    public boolean moveHierarchyNode(Hierarchy hierarchy, String euid, String oldParentEuid, String newParentEuid);
    
    /**
     * Replace a hierarchy node with a new node.
     * 
     * @param hierarchy the hierarchy
     * @param euid the euid of the node to be replaced
     * @param node the new hierarchy node
     * @return true if old node was replaced by new node, false otherwise.
     */
    public boolean replaceHierarchyNode(Hierarchy hierarchy, String euid, HierarchyNode node);
    
    /**
     * Return the tree including all ancestors up to the root, and children 1 level deep.
     *  
     * @param hierarcy the hierarchy
     * @param euid the euid of the node
     * @return the hierarchy tree representing the euid
     */
    public HierarchyTree getHierarchyTree(Hierarchy hierarcy, String euid);
    
    /**
     * Delete a hierarchy node.
     *
     * @param hierarchy the hierarchy
     * @param euid the euid of the node to delete.
     * @return the deleted hierarchy node, or null if node not found
     */
    public HierarchyNode deleteHierarchyNode(Hierarchy hierarchy, String euid);
}