/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author kkao
 */
    /*
     * RelationshipModel.xml
     * <relationships>
     *   <relationshp-type>
     */
    public class Relationships {
        public static final String TYPE_RELATIONSHIP = "relationship";
        public static final String TYPE_HIERARCHY = "hierarchy";
        public static final String TYPE_GROUP = "group";
        public static final String TYPE_CATEGORY = "category";

        ArrayList <RelationshipType> alRelationshipTypes = new ArrayList();
        ArrayList <String> alRelationshipTypeNames = new ArrayList();
        
        void addRelationshipType(RelationshipType relationshipType) {
            alRelationshipTypes.add(relationshipType);
        }
        
        RelationshipType getRelationshipType(String name, String sourceDomain, String targetDomain) {
            RelationshipType relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.name.equals(name) &&
                    relationshipType.sourceDomain.equals(sourceDomain) &&
                    relationshipType.targetDomain.equals(targetDomain)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        void deleteRelationshipType(String name, String sourceDomain, String targetDomain) {
            RelationshipType relationshipType = getRelationshipType(name, sourceDomain, targetDomain);
            if (relationshipType != null) {
                alRelationshipTypes.remove(relationshipType);
            }
        }
        
        ArrayList <RelationshipType> getAllRelationshipTypes() {
            return alRelationshipTypes;
        }
        
        ArrayList <RelationshipType> getRelationshipsByType(String type) { // type="relationship/hierarchy/group/category
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                RelationshipType relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.type.equals(type)) {
                    al.add(relationshipType); 
                }

            }
            return al;
        }
        
        ArrayList <RelationshipType> getRelationships() {         
            return getRelationshipsByType(TYPE_RELATIONSHIP);
        }
        
        ArrayList <RelationshipType> getHierarchies() {         
            return getRelationshipsByType(TYPE_HIERARCHY);
        }
        
        ArrayList <RelationshipType> getGroups() {
            return getRelationshipsByType(TYPE_GROUP);
        }
        
        ArrayList <RelationshipType> getCategories() {
            return getRelationshipsByType(TYPE_CATEGORY);
        }
    }
    
