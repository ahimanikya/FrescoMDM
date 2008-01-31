/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.valueobjects;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

import net.sf.yui4jsf.component.treeview.node.BaseNode;
import net.sf.yui4jsf.component.treeview.node.TextNode;
import net.sf.yui4jsf.component.treeview.node.HtmlNode;

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BasicTreeDataModel.java




/**
 * 
 * @author Admin
 */
public class EuidTreeVO extends net.sf.yui4jsf.component.treeview.model.impl.BaseTreeDataModelImpl
{

    public EuidTreeVO()
    {
        //addNode(createBasicTreeDataModel());
    }

    private BaseNode createBasicTreeDataModel()
    {
        HtmlNode htmlNode = new HtmlNode("n1", "node 1");
        htmlNode.setContent("<div><p>Hello World </p> </div>");
        
        TextNode node1 = new TextNode("n1", "node 1");
        TextNode node11 = createNodeWithSubNodes(0, "n11", "node 1-1");
        TextNode node12 = createNodeWithSubNodes(5, "n12", "node 1-2");
        
        
        node12.setHref("http://yui4jsf.sourceforge.net/");
        node12.setLabel("node with link");
        
        
        TextNode node1221 = createNodeWithSubNodes(4, "n1221", "node 1-2-2-1");
        TextNode node1222 = new TextNode("n1222", "node 1-2-2-2");
        TextNode node1223 = new TextNode("n1223", "node 1-2-2-3");
        
        
        node1.addNode(node11);
        node1.addNode(node12);
        
        
        BaseNode secondChildOfNode12 = node12.getNodeAt(1);
        
        secondChildOfNode12.addNode(node1221);
        secondChildOfNode12.addNode(node1222);
        secondChildOfNode12.addNode(node1223);
        
        return node1;
    }

    private TextNode createNodeWithSubNodes(int subNodeCount, String baseNodeId, String baseNodeLabel)
    {
        TextNode baseNode = new TextNode(baseNodeId, baseNodeLabel);
        for(int c = 1; c <= subNodeCount; c++)
            baseNode.addNode(new HtmlNode(baseNodeId + c, baseNodeLabel + "-" + c));

        return baseNode;
    }
}
