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

     
/*
 * ViewMergeTreeHandler.java 
 * Created on November 29, 2007
 * Author : Sridhar Naringh
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;


import com.sun.mdm.index.edm.presentation.valueobjects.EuidTreeVO;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import net.sf.yui4jsf.component.treeview.node.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ViewMergeTreeHandler  {
    
    private EuidTreeVO htmlNodeTreeDataModel = new EuidTreeVO();
    private String SERVICE_LAYER_ERROR = "SLError";
    private String SUCCESS = "Tree";
    /*
     *  Request Object Handle
     */  
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    /**
     *Http session variable
     */
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    /**
     *get Screen Object from the session
     */
    ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
    
    private String euid = request.getParameter("euid");
    
    public ViewMergeTreeHandler() {
        //createHtmlNodeTreeDataModel();        
    }

    private void createHtmlNodeTreeDataModel() {
        TextNode node1 = new TextNode("n1", "node-1");
        this.addChildNodesTo(node1, 3, 1);
        TextNode node2 = new TextNode("n2", "node-2");
        addChildNodesTo(node2, 4, 2);

        TextNode node3 = new TextNode("n3", "node-3");
        addChildNodesTo(node3, 5, 3);

        htmlNodeTreeDataModel.addNode(node1);
        htmlNodeTreeDataModel.addNode(node2);
        htmlNodeTreeDataModel.addNode(node3);
    }

    private void addChildNodesTo(TextNode parent, int childCount, int htmlNodeIndex) {
        String parentId = parent.getId();
        String parentLabel = parent.getLabel();
        for (int c = 1; c <= childCount; c++) {
            String newId = parentId + c;
            String newLabel = parentLabel + "-" + c;
            BaseNode childNode;
            if (c != htmlNodeIndex) {
                childNode = new TextNode(newId, newLabel);
            } else {
                childNode = new HtmlNode(newId, createHtmlCodeFor(newId, newLabel));
            }
            parent.addNode(childNode);
        }

    }

    private String createHtmlCodeFor(String id, String content) {
        String html = "<div id=\"" + id + "\"" + " style=\"border:1px solid #aaaaaa; " + " position:relative; " + " height:100px; width:200px; " + " margin-bottom:10px; " + " background-color: #c5dbfc\">" + "Info " + content + "</div>";
        return html;
    }

    public EuidTreeVO getHtmlNodeTreeDataModel() throws ProcessingException, UserException {
        if (euid != null && euid.trim().length() > 0) {
            viewMergeTree(euid);    
            return htmlNodeTreeDataModel;
        }  else {
            return null;
        }
        
        //createHtmlNodeTreeDataModel(); uncomment to test the Tree Structure        
    }

    /**
     * 
     * @param htmlNodeTreeDataModel
     */
    public void setHtmlNodeTreeDataModel(EuidTreeVO htmlNodeTreeDataModel) {
        this.htmlNodeTreeDataModel = htmlNodeTreeDataModel;
    }
    
    // added by samba for viewmergeTree
    public String viewMergeTree(String euid)  {
        try {
            int count = 0;
            htmlNodeTreeDataModel = new EuidTreeVO();
            MasterControllerService masterControllerService = new MasterControllerService();
            MergeHistoryNode mergeHistoryNode = masterControllerService.getMergeHistoryNode(euid); // arg: EUID
            
       //String userName, String euid1, String euid2, String function, int screeneID, String detail
        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               euid, 
                                               "",
                                               "View Merge Tree",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View Merge Tree");
            
            
            MergeHistoryNode mergeHistoryNodeCount = mergeHistoryNode;
            while (mergeHistoryNodeCount != null) {
                mergeHistoryNodeCount = mergeHistoryNodeCount.getParentNode();
                ++count;
            }
            TextNode[] euidNodes = new TextNode[count];
            System.out.println("<<== Total Nodes :" + count);
            while (mergeHistoryNode != null) {
                String sourceEUID = mergeHistoryNode.getSourceNode().getEUID();
                String destEUID = mergeHistoryNode.getDestinationNode().getEUID();
                euidNodes[--count] = new TextNode(mergeHistoryNode.getEUID(), mergeHistoryNode.getEUID());
                euidNodes[count].addNode(new TextNode(mergeHistoryNode.getSourceNode().getEUID(), mergeHistoryNode.getSourceNode().getEUID()));
                euidNodes[count].addNode(new TextNode(mergeHistoryNode.getDestinationNode().getEUID(), mergeHistoryNode.getDestinationNode().getEUID()));
                if (mergeHistoryNode.getParentNode() != null) {
                    System.out.println("ParentNode for " + mergeHistoryNode.getEUID() + " is :" + mergeHistoryNode.getParentNode().getEUID());
                }
                mergeHistoryNode = mergeHistoryNode.getParentNode();
            }
            for (int i = 0; i < euidNodes.length; i++) {
                TextNode textNode = euidNodes[i];
                htmlNodeTreeDataModel.addNode(textNode);
            }
        } catch (ProcessingException ex) {
            Logger.getLogger(ViewMergeTreeHandler.class.getName()).log(Level.SEVERE, null, ex);
            return SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            Logger.getLogger(ViewMergeTreeHandler.class.getName()).log(Level.SEVERE, null, ex);
            return SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            Logger.getLogger(ViewMergeTreeHandler.class.getName()).log(Level.SEVERE, null, ex);
            return SERVICE_LAYER_ERROR;
        }
        return SUCCESS;
    }    
}
