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

import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.editor.TabRelationshipDef;
import com.sun.mdm.multidomain.project.editor.TabHierarchyDef;
import com.sun.mdm.multidomain.project.editor.TabWebManagerDefinition;

/**
 *
 * @author kkao
 */
public class DefinitionNode extends AbstractNode {
    Definition mDefinition;
    Definition mWebDefinition;
    DefinitionNode mDefinitionNode;
    EditorMainApp mEditorMainApp;
    JPanel mTabDefinitionDef;
    TabWebManagerDefinition mTabWebManagerDefinition = null;
    String type;
    
    public DefinitionNode() {
        super(Children.LEAF);
    }

    public DefinitionNode(Children arg0, Lookup arg1) {
        super(arg0, arg1);
    }

    public DefinitionNode(Children arg0) {
        super(arg0);
    }
    
    /**
     * 
     * @param EditorMainApp
     * @param definition
     * @param webDefinition
     */
    public DefinitionNode(EditorMainApp editorMainApp, Definition definition, Definition webDefinition) {
        super(Children.LEAF);
        mEditorMainApp = editorMainApp;
        mDefinition = definition;
        mWebDefinition = webDefinition;
        mDefinitionNode = this;
        addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                //ToDo - notify EditorMainApp
            }
        });
    }

    /**
     * 
     * @return mDefinition
     */
    public Definition getDefinition() {
        return mDefinition;
    }

    /**
     * 
     * @return Definition Name
     */
    public String getName() {
        return mDefinition.getName();
    }

    public void setType(String type) {
        mDefinition.setType(type);
    }
    
    public String getType() {
        return mDefinition.getType();
    }

    /**
     * 
     * @return sourceDomain
     */
    public String getSourceDomain() {
        return mDefinition.getSourceDomain();
    }

    /**
     * 
     * @return targetDomain
     */
    public String getTargetDomain() {
        return mDefinition.getTargetDomain();
    }

    public JPanel getDefinitionTab(boolean bRefresh) {
        if (bRefresh || mTabDefinitionDef == null) {
            if (getType().equals(Definition.TYPE_RELATIONSHIP)) {
                mTabDefinitionDef = new TabRelationshipDef(mEditorMainApp, this);
            } else if (getType().equals(Definition.TYPE_HIERARCHY)) {
                mTabDefinitionDef = new TabHierarchyDef(mEditorMainApp, this);
            }
        }
        return mTabDefinitionDef;
    }
    
    public TabWebManagerDefinition getWebManagerDefinitionTab(boolean bRefresh) {
        if (mTabWebManagerDefinition == null) {
            mTabWebManagerDefinition = new TabWebManagerDefinition(mEditorMainApp, mWebDefinition);
        }
        
        return mTabWebManagerDefinition;
    }
}
