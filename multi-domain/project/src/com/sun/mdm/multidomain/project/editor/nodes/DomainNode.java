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

import com.sun.mdm.multidomain.parser.DomainForWebManager;
import com.sun.mdm.multidomain.parser.MIQueryBuilder;
import java.util.ArrayList;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.NodeAdapter;
import org.openide.nodes.NodeEvent;
import org.openide.util.Lookup;
import org.openide.filesystems.FileObject;

import java.io.File;
import java.io.InputStream;
import org.xml.sax.InputSource;
import java.util.Iterator;

import com.sun.mdm.multidomain.parser.Definition;
import com.sun.mdm.multidomain.parser.MiNodeDef;
import com.sun.mdm.multidomain.parser.MiFieldDef;
import com.sun.mdm.multidomain.parser.MiObject;
import com.sun.mdm.multidomain.parser.Utils;
import com.sun.mdm.multidomain.project.MultiDomainProjectProperties;
import com.sun.mdm.multidomain.util.Logger;
import com.sun.mdm.multidomain.project.editor.EditorMainApp;
import com.sun.mdm.multidomain.project.editor.TabDomainSearch;
import com.sun.mdm.multidomain.project.editor.TabDomainView;
import com.sun.mdm.multidomain.project.editor.EntityTree;

/**
 *
 * @author kkao
 */
public class DomainNode extends AbstractNode {
    /** The logger. */
    private static final Logger mLog = Logger.getLogger(
            DomainNode.class.getName()
        
        );
    ArrayList <MiNodeDef> alMiNodeDefs = new ArrayList();
    private ArrayList <String> alAssociatedDomains = new ArrayList();
    private ArrayList <Definition> alDefinitions = new ArrayList();
    //TabDomainProperties mTabDomainProperties = null;
    EditorMainApp mEditorMainApp;
    EntityTree mMainEntityTree = null;
    EntityTree mEntityTree = null;
    TabDomainSearch mTabDomainSearch;
    TabDomainView mTabDomainView;
    File mSelectedDomain = null;
    MIQueryBuilder mMIQueryBuilder = null;
    MiObject mMiObject;
    
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
     * @param ArrayList <Definition> alDefinitions
     */
    public DomainNode(EditorMainApp editorMainApp, String domainName, File selectedDomain, ArrayList <String> alAssociatedDomains, ArrayList <Definition> alDefinitions) {
        super(Children.LEAF);
        setName(domainName);
        mEditorMainApp = editorMainApp;
        mSelectedDomain = selectedDomain;
        this.alAssociatedDomains = alAssociatedDomains;
        
        loadMiObjectNodes();
        loadMiQueryBuilder();
        loadDefinitionss(domainName, alDefinitions);
        addNodeListener(new NodeAdapter() {
            @Override
            public void nodeDestroyed(NodeEvent ev) {
                //ToDo - notify EditorMainApp
            }
        });
    }

    /**
     * Build graphical representation of domain object
     */
    private void loadMiObjectNodes() {       
        mMiObject = parseMiObject();
        ArrayList alMiNodes = mMiObject.getNodes();
        if ((alMiNodes != null) && (alMiNodes.size() > 0)) {
            String miObjectName = mMiObject.getObjectName();

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
    
    /**
     * Build querybuilder class from Query.xml file
     */
    private void loadMiQueryBuilder() {
        
        //MiObject miObject = null;
         try {
            FileObject objectXml = EditorMainApp.getSavedDomainQueryXml(mSelectedDomain);
            if (objectXml != null) {
                InputStream objectdef = objectXml.getInputStream();
                InputSource source = new InputSource(objectdef);
                mMIQueryBuilder = Utils.parseMiQueryBuilder(source);
            }
        } catch (Exception ex) {
                mLog.severe(ex.getMessage());
        }
        //return miObject;
       
    }
    
    public MIQueryBuilder getMiQueryBuilder() {
        return this.mMIQueryBuilder;
    }
    
    public MiObject getMiObject() {
        return mMiObject;
    }
    
    private MiObject parseMiObject() {
        mMiObject = null;
        try {
            FileObject objectXml = EditorMainApp.getSavedDomainObjectXml(mSelectedDomain);
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

    public DomainForWebManager getMidmObject() {
        DomainForWebManager domainWebObject = null;
        try {
            //mEditorMainApp.getDomainMidmXml(PROP_NAME)
            FileObject objectXml = mEditorMainApp.getDomainMidmXml(mSelectedDomain.getName());
            if (objectXml != null) {
                InputStream objectdef = objectXml.getInputStream();
                InputSource source = new InputSource(objectdef);
                domainWebObject = Utils.parseMidmObject(source);
            }
        } catch (Exception ex) {
                mLog.severe(ex.getMessage());
        }
        return domainWebObject;
    }
    
    public String writeDomainObject() {
        
        String domainMidmXml = null;
        try {
            //mEditorMainApp.getDomainMidmXml(PROP_NAME)
            FileObject objectXml = mEditorMainApp.getDomainMidmXml(mSelectedDomain.getName());
            if (objectXml != null) {
                InputStream objectdef = objectXml.getInputStream();
                InputSource source = new InputSource(objectdef);
                domainMidmXml = Utils.parseMidmNodeObject(source);
//                domainWebObject = Utils.parseMidmObject(source);
            }
        } catch (Exception ex) {
                mLog.severe(ex.getMessage());
        }
        
        return domainMidmXml;
        
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
    
    public void addDefinition(String defName, String sourceDomain, String targetDomain) {
        //updateDefinitions
    }
    
    public void removeDefinition(String defName, String sourceDomain, String targetDomain) {
        //updateDefinitions
        for (Definition definition : this.alDefinitions) {
            if (definition.getName().equals(defName) &&
                definition.getSourceDomain().equals(sourceDomain) &&
                definition.getSourceDomain().equals(targetDomain)) {
                alDefinitions.remove(definition);
                break;
            }
        }
    }
    
    public ArrayList getAssociatedDomains() {
        return alAssociatedDomains;
    }
    
    public ArrayList getDefinitions() {
        return alDefinitions;
    }
    
    /** Update alDefinitions and mTabRelationshipsPerDomain
     * 
     */
    public void loadDefinitionss(String domainName, ArrayList <Definition> alDefinitions) {
        this.alDefinitions = alDefinitions;
    }
    
    /** to be used by dialog other than main panel
     * 
     * @return EntityTree mEntityTree
     */
    public EntityTree getEntityTree() {
        if (mEntityTree == null) {
            mEntityTree = new EntityTree(this.getMiObject());
        }
        return mEntityTree;
    }
    
    /** to be used main panel
     * 
     * @return EntityTree mMainEntityTree
     */
    public EntityTree getMainEntityTree() {
        if (mMainEntityTree == null) {
            mMainEntityTree = new EntityTree(this.getMiObject());
        }
        return mMainEntityTree;
    }
    
    public TabDomainSearch getDoaminsTab(boolean bRefresh) {
        if (bRefresh || mTabDomainSearch == null) {
            DomainForWebManager domain = mEditorMainApp.getMultiDomainWebManager(false).getDomains().getDomain(this.getName());
            if (domain == null) {
                domain = getMidmObject();
                domain.setMidmXMLLocation(MultiDomainProjectProperties.DOMAINS_FOLDER + File.separator + domain.getDomainName() + File.separator + MultiDomainProjectProperties.MIDM_NODE_DOMAIN_XML);
                mEditorMainApp.getMultiDomainWebManager(false).getDomains().addDomain(domain);
            }
            mTabDomainSearch = new TabDomainSearch(mEditorMainApp, domain);
        }
        return mTabDomainSearch;
    }
    
    public TabDomainView getDomainViewTab(boolean bRefresh) {
        if (bRefresh || mTabDomainView == null) {
            DomainForWebManager domain = mEditorMainApp.getMultiDomainWebManager(false).getDomains().getDomain(this.getName());
            if (domain == null) {
                domain = getMidmObject();
                domain.setMidmXMLLocation(MultiDomainProjectProperties.DOMAINS_FOLDER + File.separator + domain.getDomainName() + File.separator + MultiDomainProjectProperties.MIDM_NODE_DOMAIN_XML);
                //writeDomainObject();
                mEditorMainApp.getMultiDomainWebManager(false).getDomains().addDomain(domain);
            }
            mTabDomainView = new TabDomainView(mEditorMainApp, domain);
        }
        
        return mTabDomainView;
    }
}
