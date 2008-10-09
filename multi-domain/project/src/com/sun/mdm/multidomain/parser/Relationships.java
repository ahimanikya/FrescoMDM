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
     *   <relationshp>
     */
    public class Relationships {
        ArrayList <Relationship> alRelationships = new ArrayList();
        
        Relationship addRelationship() {
            Relationship relationship = new Relationship();
            alRelationships.add(relationship);
            return relationship;
        }
        
        void addRelationship(Relationship relationship) {
            alRelationships.add(relationship);
        }
        
        Relationship getRelationship(String sourceDomain, String targetDomain) {
            Relationship relationship = null;
            for (int i=0; alRelationships.size() > i; i++) {
                relationship = (Relationship) alRelationships.get(i);
                if (relationship.getDomain1().equals(sourceDomain) &&
                    relationship.getDomain2().equals(targetDomain)) {
                    break; 
                }
            }
            return relationship;
        }
        
        void deleteRelationship(String sourceDomain, String targetDomain) {
            Relationship relationship = getRelationship(sourceDomain, targetDomain);
            if (relationship != null) {
                alRelationships.remove(relationship);
            }
        }
        
        ArrayList <Relationship> getAllRelationships() {
            return alRelationships;
        }
        
        ArrayList <Relationship> getRelationshipsByDomain(String domainName) {
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationships.size(); i++) {
                Relationship relationship = (Relationship) alRelationships.get(i);
                if (relationship.getDomain1().equals(domainName) || relationship.getDomain2().equals(domainName)) {
                    al.add(relationship); 
                }
            }
            return al;
        }
    }
    
