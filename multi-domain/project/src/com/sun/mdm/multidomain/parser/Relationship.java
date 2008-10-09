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
    public class Relationship {

    public void setDomain1(String domain1) {
        this.domain1 = domain1;
    }

    public void setDomain2(String domain2) {
        this.domain2 = domain2;
    }

    public String getDomain1() {
        return domain1;
    }

    public String getDomain2() {
        return domain2;
    }
        public static final String TYPE_RELATIONSHIP = "relationship";
        public static final String TYPE_HIERARCHY = "hierarchy";
        public static final String TYPE_GROUP = "group";
        public static final String TYPE_CATEGORY = "category";

        String domain1;
        String domain2;
        ArrayList <RelationshipType> alRelationshipTypes = new ArrayList();
        ArrayList <String> alRelationshipTypeNames = new ArrayList();
        
        RelationshipType addRelationshipType() {
            RelationshipType relationshipType = new RelationshipType();
            alRelationshipTypes.add(relationshipType);
            return relationshipType;
        }
        
        void addRelationshipType(RelationshipType relationshipType) {
            alRelationshipTypes.add(relationshipType);
        }
        
        RelationshipType getRelationshipType(String name) {
            RelationshipType relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.getRelTypeName().equals(name)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        RelationshipType getRelationshipType(String name, String sourceDomain, String targetDomain) {
            RelationshipType relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.getRelTypeName().equals(name) &&
                    relationshipType.getSource().equals(sourceDomain) &&
                    relationshipType.getDestionation().equals(targetDomain)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        void deleteRelationshipType(String name) {
            RelationshipType relationshipType = getRelationshipType(name);
            if (relationshipType != null) {
                alRelationshipTypes.remove(relationshipType);
            }
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
        
        ArrayList <RelationshipType> getRelationshipTypesByType(String type) { // type="relationship/hierarchy/group/category
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                RelationshipType relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.type.equals(type)) {
                    al.add(relationshipType); 
                }
            }
            return al;
        }
        
        ArrayList <RelationshipType> getRelationshipTypesByDomain(String domainName) {
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                RelationshipType relationshipType = (RelationshipType) alRelationshipTypes.get(i);
                if (relationshipType.sourceDomain.equals(domainName) || relationshipType.targetDomain.equals(domainName)) {
                    al.add(relationshipType); 
                }
            }
            return al;
        }
        
        ArrayList <RelationshipType> getRelationships() {         
            return getRelationshipTypesByType(TYPE_RELATIONSHIP);
        }
        
        ArrayList <RelationshipType> getHierarchies() {         
            return getRelationshipTypesByType(TYPE_HIERARCHY);
        }
        
        ArrayList <RelationshipType> getGroups() {
            return getRelationshipTypesByType(TYPE_GROUP);
        }
        
        ArrayList <RelationshipType> getCategories() {
            return getRelationshipTypesByType(TYPE_CATEGORY);
        }
    }
    
