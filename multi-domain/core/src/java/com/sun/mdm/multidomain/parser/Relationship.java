/*
 * Copyright (c) 2007, Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Sun Microsystems, Inc. nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.mdm.multidomain.parser;

import java.util.ArrayList;

/**
 *
 * @author kkao
 */
    /*
     * MultiDomainModel.xml
     * <relationship>
     */
    public class Relationship {
        public static final String TYPE_RELATIONSHIP = "relationship";
        public static final String TYPE_HIERARCHY = "hierarchy";
        public static final String TYPE_GROUP = "group";
        public static final String TYPE_CATEGORY = "category";

        String domain1;
        String domain2;
        ArrayList <WebDefinition> alRelationshipTypes = new ArrayList();
        ArrayList <String> alRelationshipTypeNames = new ArrayList();

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

        public WebDefinition addRelationshipType() {
            WebDefinition relationshipType = new WebDefinition();
            alRelationshipTypes.add(relationshipType);
            return relationshipType;
        }
        
        public void addRelationshipType(WebDefinition relationshipType) {
            alRelationshipTypes.add(relationshipType);
        }
        
        public WebDefinition getRelationshipType(String name) {
            WebDefinition relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (WebDefinition) alRelationshipTypes.get(i);
                if (relationshipType.getName().equals(name)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        public WebDefinition getRelationshipType(String name, String sourceDomain, String targetDomain) {
            WebDefinition relationshipType = null;
            for (int i=0; alRelationshipTypes.size() > i; i++) {
                relationshipType = (WebDefinition) alRelationshipTypes.get(i);
                if (relationshipType.getName().equals(name) &&
                    relationshipType.getSource().equals(sourceDomain) &&
                    relationshipType.getTargetDomain().equals(targetDomain)) {
                    break; 
                }
            }
            return relationshipType;
        }
        
        public void deleteRelationshipType(String name) {
            WebDefinition relationshipType = getRelationshipType(name);
            if (relationshipType != null) {
                alRelationshipTypes.remove(relationshipType);
            }
        }
        
        public void deleteRelationshipType(String name, String sourceDomain, String targetDomain) {
            WebDefinition relationshipType = getRelationshipType(name, sourceDomain, targetDomain);
            if (relationshipType != null) {
                alRelationshipTypes.remove(relationshipType);
            }
        }
        
        public ArrayList <WebDefinition> getAllRelationshipTypes() {
            return alRelationshipTypes;
        }
        
        public ArrayList <WebDefinition> getRelationshipTypesByType(String type) { // type="relationship/hierarchy/group/category
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                WebDefinition relationshipType = (WebDefinition) alRelationshipTypes.get(i);
                if (relationshipType.type.equals(type)) {
                    al.add(relationshipType); 
                }
            }
            return al;
        }
        
        public ArrayList <WebDefinition> getRelationshipTypesByDomain(String domainName) {
            ArrayList al = new ArrayList();
            for (int i=0; i<alRelationshipTypes.size(); i++) {
                WebDefinition relationshipType = (WebDefinition) alRelationshipTypes.get(i);
                if (relationshipType.sourceDomain.equals(domainName) || relationshipType.targetDomain.equals(domainName)) {
                    al.add(relationshipType); 
                }
            }
            return al;
        }
        
        public ArrayList <WebDefinition> getRelationships() {         
            return getRelationshipTypesByType(TYPE_RELATIONSHIP);
        }
        
        public ArrayList <WebDefinition> getHierarchies() {         
            return getRelationshipTypesByType(TYPE_HIERARCHY);
        }
        
        public ArrayList <WebDefinition> getGroups() {
            return getRelationshipTypesByType(TYPE_GROUP);
        }
        
        public ArrayList <WebDefinition> getCategories() {
            return getRelationshipTypesByType(TYPE_CATEGORY);
        }
    }
    
