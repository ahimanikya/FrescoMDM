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
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.util.Lookup;

import java.util.ArrayList;

import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
/**
 *
 * @author kkao
 */
public class RelationshipNode extends AbstractNode {
    EditorMainApp mEditorMainApp;
    RelationshipNode mRelationshipNode;
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
    
    public RelationshipNode(EditorMainApp editorMainApp, String domain1, String domain2) {
        super(Children.LEAF);
        mEditorMainApp = editorMainApp;
        mRelationshipNode = this;
        this.domain1 = domain1;
        this.domain2 = domain2;
        addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                mEditorMainApp.deleteRelationshipNode(mRelationshipNode);
            }
        });
    }
    
    /**
     * 
     * @param relationshipType
     */
    public void addRelationshipTypeNode(RelationshipType relationshipType) {
        RelationshipTypeNode relationshipTypeNode = new RelationshipTypeNode(this, relationshipType);
        alRelationshipTypeNodes.add(relationshipTypeNode);
    }
    
    /**
     * 
     * @param relationshipTypeNode
     */
    public void addRelationshipTypeNode(RelationshipTypeNode relationshipTypeNode ) {
        relationshipTypeNode.setParentRelationshipNode(this);
        alRelationshipTypeNodes.add(relationshipTypeNode);
    }
    
    /**
     * Find and remove RelationshipTypeNode that matches relationshipTypeNode
     * @param relationshipTypeNode
     */
    public void deleteRelationshipTypeNode(RelationshipTypeNode relationshipTypeNode ) {
        if (relationshipTypeNode != null &&
            relationshipTypeNode.getParentRelationshipNode() != null &&
            relationshipTypeNode.getParentRelationshipNode() == this) {
            alRelationshipTypeNodes.remove(relationshipTypeNode);
        }
    }
    
    /**
     * Find and remove RelationshipTypeNode that matches these params
     * @param relationshipTypeName
     * @param sourceDomain
     * @param targetDomain
     */
    public void deleteRelationshipTypeNode(String relationshipTypeName, String sourceDomain, String targetDomain ) {
        // 
        for (int i=0; alRelationshipTypeNodes != null && i<alRelationshipTypeNodes.size(); i++) {
            RelationshipTypeNode relationshipTypeNode = (RelationshipTypeNode) alRelationshipTypeNodes.get(i);
            if (relationshipTypeNode.getRelationshipTypeName().equals(relationshipTypeName) &&
                relationshipTypeNode.getRelationshipType().getSourceDomain().equals(sourceDomain) &&
                relationshipTypeNode.getRelationshipType().getTargetDomain().equals(targetDomain)) {
                alRelationshipTypeNodes.remove(i);
                break;
            }
        }
    }

    /**
     * 
     * @return domain1
     */
    public String getDomain1() {
        return domain1;
    }

    /**
     * 
     * @return domain2
     */
    public String getDomain2() {
        return domain2;
    }

    /**
     * 
     * @return ArrayList <RelationshipTypeNode>
     */
    public ArrayList <RelationshipTypeNode> getAllRelationshipTypeNodes() {
        return alRelationshipTypeNodes;
    }

}
