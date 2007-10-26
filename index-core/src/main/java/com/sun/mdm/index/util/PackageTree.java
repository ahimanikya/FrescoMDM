/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.sun.mdm.index.master.ProcessingException;

/**
 *
 * @author  dcidon
 */
public class PackageTree {
    
    private final PackageTreeNode mRootNode;
    
    /** Creates a new instance of PackageTree */
    public PackageTree(Object objValue) throws ProcessingException {
        if (objValue == null) {
            throw new ProcessingException("Object value can not be null for root node");
        }
        mRootNode = new PackageTreeNode(null);
        mRootNode.setObjectValue(objValue);
    }
    
    private class PackageTreeNode {
        
        private final String mPackageComponent;
        private HashMap /* PackageTreeNode */ mChildren;
        private Object mObjectValue;
        
        PackageTreeNode(String packageComponent) {
            mPackageComponent = packageComponent;
        }
        
        String getPackageComponent() {
            return mPackageComponent;
        }
        
        boolean hasChildren() {
            return mChildren != null;
        }
        
        void addChild(PackageTreeNode node) throws ProcessingException {
            String packageComponent = node.getPackageComponent();
            if (mChildren == null) {
                mChildren = new HashMap();
            } else {
                if (mChildren.get(packageComponent) != null) {
                    throw new ProcessingException("Package componenet already exists: " +
                    packageComponent);
                }
            }
            mChildren.put(packageComponent, node);
        }
        
        Iterator getChildren() {
            if (mChildren == null) {
                return null;
            }
            return mChildren.values().iterator();
        }
        
        PackageTreeNode getChild(String componentName) {
            if (mChildren == null) {
                return null;
            }
            return (PackageTreeNode) mChildren.get(componentName);
        }
        
        Object getObjectValue() {
            return mObjectValue;
        }
        
        void setObjectValue(Object obj) {
            mObjectValue = obj;
        }
        
    }
    
    private ArrayList splitPackageString(String packageString) {
        ArrayList list = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(packageString, ".");
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextElement());
        }
        return list;
    }
    
    public void addAssignment(HashMap hm) throws ProcessingException {
        Iterator keys = hm.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            String[] packageStrings = (String[]) hm.get(key);
            for (int i = 0; i < packageStrings.length; i++) {
                addAssignment(packageStrings[i], key); 
            }
        }
    }
    
    public void addAssignment(String packageString, Object objValue) 
    throws ProcessingException {
        ArrayList list = splitPackageString(packageString);
        addAssignment(mRootNode, list.iterator(), objValue);
    }
    
    private void addAssignment(PackageTreeNode node, Iterator i, Object objValue) 
    throws ProcessingException { 
        if (i.hasNext()) {
            String nextPackageComponent = (String) i.next();
            PackageTreeNode childNode = node.getChild(nextPackageComponent);
            if (childNode == null) {
                childNode = new PackageTreeNode(nextPackageComponent);
                node.addChild(childNode);
            }
            addAssignment(childNode, i, objValue);
        } else {
            node.setObjectValue(objValue);
        }
        
    }
    
    public Object getObjectValue(String packageString) {
        ArrayList list = splitPackageString(packageString);
        return getObjectValue(mRootNode, list.iterator(), mRootNode.getObjectValue());
    }
    
    private Object getObjectValue(PackageTreeNode node, Iterator i, Object objValue) {
        if (node.getObjectValue() != null) {
            objValue = node.getObjectValue();
        }
        if (i.hasNext()) {
            String nextPackageComponent = (String) i.next();
            PackageTreeNode childNode = node.getChild(nextPackageComponent);
            
            if (childNode == null) {
                return objValue;
            } else {
                return getObjectValue(childNode, i, objValue);
            }
        } else {
            return objValue;
        }
    }
    
    public static void main(String args[]) {
        try {
            PackageTree tree = new PackageTree("ROOT");
            tree.addAssignment("com.sun.mdm.index.master", "MasterController");
            tree.addAssignment("com.sun.mdm.index.update", "UpdateManager");
            System.out.println(tree.getObjectValue("com.sun.mdm.index.master.SomeClass"));
            System.out.println(tree.getObjectValue("com.sun.mdm.index.update.SomeClass"));
            System.out.println(tree.getObjectValue("com.sun.mdm.index.SomeClass"));
            System.out.println(tree.getObjectValue(""));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
