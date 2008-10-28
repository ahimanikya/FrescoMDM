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

import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
/**
 *
 * @author kkao
 */
public class LinkParentNode extends AbstractNode {
    EditorMainApp mEditorMainApp;
    LinkParentNode mLinkParentNode;
    String domain1;
    String domain2;
    ArrayList <LinkBaseNode> alLinkBaseNodes = new ArrayList();
    
    public LinkParentNode() {
        super(Children.LEAF);
    }

    public LinkParentNode(Children arg0, Lookup arg1) {
        super(arg0, arg1);
    }

    public LinkParentNode(Children arg0) {
        super(arg0);
    }
    
    public LinkParentNode(EditorMainApp editorMainApp, String domain1, String domain2) {
        super(Children.LEAF);
        mEditorMainApp = editorMainApp;
        mLinkParentNode = this;
        this.domain1 = domain1;
        this.domain2 = domain2;
        addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                //mEditorMainApp.deleteLinkParentNode(mLinkParentNode);
            }
        });
    }
    
    /**
     * 
     * @param linkType
     */
    /**
    public void addLinkBaseNode(LinkType linkType) {
        LinkBaseNode linkTypeNode = new LinkBaseNode(mEditorMainApp, linkType);
        alLinkBaseNodes.add(linkTypeNode);
    }
     */ 
    
    /**
     * 
     * @param linkTypeNode
     */
    public void addLinkBaseNode(LinkBaseNode linkTypeNode ) {
        alLinkBaseNodes.add(linkTypeNode);
    }
    
    /**
     * Find and remove LinkBaseNode that matches these params
     * @param linkName
     * @param sourceDomain
     * @param targetDomain
     */
    public void deleteLinkBaseNode(String linkName, String sourceDomain, String targetDomain ) {
        // 
        for (int i=0; alLinkBaseNodes != null && i<alLinkBaseNodes.size(); i++) {
            LinkBaseNode linkTypeNode = (LinkBaseNode) alLinkBaseNodes.get(i);
            if (linkTypeNode.getName().equals(linkName) &&
                linkTypeNode.getLinkType().getSourceDomain().equals(sourceDomain) &&
                linkTypeNode.getLinkType().getTargetDomain().equals(targetDomain)) {
                alLinkBaseNodes.remove(i);
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
     * @return ArrayList <LinkBaseNode>
     */
    public ArrayList <LinkBaseNode> getAllLinkBaseNodes() {
        return alLinkBaseNodes;
    }

}
