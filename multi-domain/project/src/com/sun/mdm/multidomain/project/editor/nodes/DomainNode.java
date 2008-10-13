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

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.netbeans.api.visual.widget.Widget;
import org.openide.ErrorManager;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.filesystems.FileObject;

import java.io.File;
import java.io.InputStream;
import org.xml.sax.InputSource;
import java.util.Iterator;
import java.util.Set;

import com.sun.mdm.multidomain.parser.RelationshipType;
import com.sun.mdm.multidomain.parser.MiNodeDef;
import com.sun.mdm.multidomain.parser.MiFieldDef;
import com.sun.mdm.multidomain.parser.MiObject;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.editor.TabListRelationshipTypes;

/**
 *
 * @author kkao
 */
public class DomainNode extends AbstractNode {
    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            EditorMainApp.class.getName()
        
        );
    ArrayList <MiNodeDef> alMiNodeDefs = new ArrayList();
    private ArrayList <RelationshipType> alRelationshipTypes = new ArrayList();
    TabListRelationshipTypes mTabListRelationshipTypes = null;
    File selectedDomain = null;
    
    /**
     * 
     */
    public DomainNode() {
        super(Children.LEAF);
    }

    /**
     * 
     * @param arg0
     * @param arg1
     */
    public DomainNode(Children arg0, Lookup arg1) {
        super(arg0, arg1);
    }

    /**
     * 
     * @param arg0
     */
    public DomainNode(Children arg0) {
        super(arg0);
    }
    
    /** EditorMainApp is the keeper of DomainNode's
     * 
     * @param domainName
     * @param selectedDomain
     */
    public DomainNode(String domainName, File selectedDomain) {
        super(Children.LEAF);
        setName(domainName);
        this.selectedDomain = selectedDomain;
        
        loadMiObjectNodes(selectedDomain);
    }
    
    private MiObject getMiObject(File selectedDomain) {
        MiObject mMiObject = null;
        try {
            FileObject objectXml = EditorMainApp.getSavedDomainObjectXml(selectedDomain);
            if (objectXml != null) {
                InputStream objectdef = objectXml.getInputStream();
                InputSource source = new InputSource(objectdef);
                mMiObject = Utils.parseMiObject(source);
            }
        } catch (Exception ex) {
                mLog.severe(ex.getMessage());
        }
        return mMiObject;
    }

    /**
     * Build graphical representation of domain object
     * @param selectedDomain
     */
    private void loadMiObjectNodes(File selectedDomain) {       
        MiObject miObject = getMiObject(selectedDomain);
        ArrayList alMiNodes = miObject.getNodes();
        if ((alMiNodes != null) && (alMiNodes.size() > 0)) {
            String miObjectName = miObject.getObjectName();

            Iterator it = alMiNodes.iterator();
            while (it.hasNext()) {
                MiNodeDef node = (MiNodeDef) it.next();
                alMiNodeDefs.add(node);
                if (node.getTag().equals(miObjectName)) { // check if is the Primary Node
                } else {
                }
                addChildren(node);
            }
        }
    }
    
    private void addChildren(MiNodeDef node) {
        ArrayList children = node.getChildren();
        if (children != null) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                MiNodeDef child = (MiNodeDef) it.next();
                addChildren(child); // Support multi-tier object
            }
        }
        ArrayList fields = node.getFields();
        for (int i = 0; i < fields.size(); i++) {
            MiFieldDef fieldDef = (MiFieldDef) fields.get(i);
            if (fieldDef != null) {
                String nodeName = fieldDef.getFieldName();
            }
        }
    }

    /**
     * 
     * @return ArrayList<MiNodeDef> alMiNodeDefs
     */
    public ArrayList<MiNodeDef> getMiNodeDefs() {
        return alMiNodeDefs;
    }
    
    /**
     * 
     * @param MiNodeDef node
     * @return ArrayList<MiNodeDef>
     */
    public ArrayList<MiNodeDef> getMiNodeDefs(MiNodeDef node) {
        return node.getChildren();
    }
    
    /**
     * 
     * @param MiNodeDef node
     * @return ArrayList<MiFieldDef>
     */
    public ArrayList<MiFieldDef> getMiFieldDefs(MiNodeDef node) {
        return node.getFields();
    }
    
    public void addRelationshipType(String relationshipTypeName, String sourceDomainName, String targetDomainName) {
        //updateRelationshipTypes
    }
    
    public void removeRelationshipType(String relationshipTypeName, String sourceDomainName, String targetDomainName) {
        //updateRelationshipTypes
    }
    
    public ArrayList getRelationshipTypes() {
        return alRelationshipTypes;
    }
    
    /** Update alRelationshipTypes and mTabListRelationshipTypes
     * 
     */
    public void loadRelationshipTypes(ArrayList <RelationshipType> alRelationshipTypes) {
        this.alRelationshipTypes = alRelationshipTypes;
        if (alRelationshipTypes != null) {
            mTabListRelationshipTypes = new TabListRelationshipTypes(this.alRelationshipTypes);
        }
    }
    
    public TabListRelationshipTypes getTabListRelationshipTypes() {
        return mTabListRelationshipTypes;
    }
}
