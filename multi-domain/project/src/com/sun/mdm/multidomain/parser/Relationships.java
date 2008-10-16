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
    
