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
//import org.openide.nodes.CookieSet;
import org.openide.util.Lookup;
import javax.swing.JPanel;

import com.sun.mdm.multidomain.parser.LinkType;
import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.editor.TabRelationshipDef;
import com.sun.mdm.multidomain.project.editor.TabHierarchyDef;
import com.sun.mdm.multidomain.project.editor.TabWebManagerRelationshipTypes;

/**
 *
 * @author kkao
 */
public class LinkBaseNode extends AbstractNode {
    LinkType mLinkType;
    LinkType mWebLinkType;
    LinkBaseNode mLinkTypeNode;
    String mLinkName;
    EditorMainApp mEditorMainApp;
    JPanel mTabLinkDef;
    TabWebManagerRelationshipTypes mTabWebManagerRelationshipTypes = null;
    String type;
    
    public LinkBaseNode() {
        super(Children.LEAF);
    }

    public LinkBaseNode(Children arg0, Lookup arg1) {
        super(arg0, arg1);
    }

    public LinkBaseNode(Children arg0) {
        super(arg0);
    }
    
    /**
     * 
     * @param LinkParentNode
     * @param linkType
     */
    public LinkBaseNode(EditorMainApp editorMainApp, LinkType linkType, LinkType webLinkType) {
        super(Children.LEAF);
        mEditorMainApp = editorMainApp;
        mLinkType = linkType;
        mWebLinkType = webLinkType;
        mLinkTypeNode = this;
        addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                //ToDo - notify EditorMainApp
            }
        });
    }

    /**
     * 
     * @return mLinkType
     */
    public LinkType getLinkType() {
        return mLinkType;
    }

    /**
     * 
     * @return mLinkName
     */
    public String getName() {
        return mLinkType.getName();
    }

    public void setType(String type) {
        mLinkType.setType(type);
    }
    
    public String getType() {
        return mLinkType.getType();
    }

    /**
     * 
     * @return sourceDomain
     */
    public String getSourceDomain() {
        return mLinkType.getSourceDomain();
    }

    /**
     * 
     * @return targetDomain
     */
    public String getTargetDomain() {
        return mLinkType.getTargetDomain();
    }

    public JPanel getLinkDefTab(boolean bRefresh) {
        if (bRefresh || mTabLinkDef == null) {
            if (getType().equals(LinkType.TYPE_RELATIONSHIP)) {
                mTabLinkDef = new TabRelationshipDef(getLinkType());
            } else if (getType().equals(LinkType.TYPE_HIERARCHY)) {
                mTabLinkDef = new TabHierarchyDef(getLinkType());
            }
        }
        return mTabLinkDef;
    }
    
    public TabWebManagerRelationshipTypes getRelationshipTypesTab(boolean bRefresh) {
        if (mTabWebManagerRelationshipTypes == null) {
            mTabWebManagerRelationshipTypes = new TabWebManagerRelationshipTypes(mEditorMainApp, mWebLinkType);
        }
        
        return mTabWebManagerRelationshipTypes;
    }
}
