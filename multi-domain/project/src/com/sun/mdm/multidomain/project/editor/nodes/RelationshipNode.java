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

package com.sun.mdm.multidomain.project.editor.nodes;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

import java.util.ArrayList;

import com.sun.mdm.multidomain.parser.RelationshipType;
//import com.sun.mdm.multidomain.project.editor.TabRelationshipWebManager;

/**
 *
 * @author kkao
 */
public class RelationshipNode extends AbstractNode {
    String domain1;
    String domain2;
    ArrayList <RelationshipTypeNode> alRelationshipTypeNodes = new ArrayList();
    
    public RelationshipNode() {
        super(Children.LEAF);
    }

    public RelationshipNode(Children arg0, Lookup arg1) {
        super(arg0, arg1);
    }

    public RelationshipNode(Children arg0) {
        super(arg0);
    }
    
    public RelationshipNode(String sourceDomain, String targetDomain) {
        super(Children.LEAF);
    }
    
    public void addRelationshipTypeNode(RelationshipType relationshipType) {
        RelationshipTypeNode relationshipTypeNode = new RelationshipTypeNode(this, relationshipType);
        alRelationshipTypeNodes.add(relationshipTypeNode);
    }
    
    public void addRelationshipTypeNode(RelationshipTypeNode relationshipTypeNode ) {
        relationshipTypeNode.setParentRelationshipNode(this);
        alRelationshipTypeNodes.add(relationshipTypeNode);
    }
    
    public void deleteRelationshipTypeNode(RelationshipTypeNode relationshipTypeNode ) {
        
    }
    
    public void deleteRelationshipTypeNode(String relationshipTypeName ) {
        
    }
    
    public ArrayList<RelationshipTypeNode> getAlRelationshipTypeNodes() {
        return alRelationshipTypeNodes;
    }

}