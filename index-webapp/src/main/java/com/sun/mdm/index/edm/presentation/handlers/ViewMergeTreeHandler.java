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
 *  
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.valueobjects.EuidTreeVO;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.search.merge.MergeHistoryNode;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.Stack;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import net.sf.yui4jsf.component.treeview.node.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.xml.bind.ValidationException;
import net.java.hulp.i18n.LocalizationSupport;

public class ViewMergeTreeHandler {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
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
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");

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
        } else {
            return null;
        }


    }

    /**
     * 
     * @param htmlNodeTreeDataModel
     */
    public void setHtmlNodeTreeDataModel(EuidTreeVO htmlNodeTreeDataModel) {
        this.htmlNodeTreeDataModel = htmlNodeTreeDataModel;
    }

    // added by samba for viewmergeTree
    public String viewMergeTree(String euid) {
        try {

            htmlNodeTreeDataModel = new EuidTreeVO();
            MasterControllerService masterControllerService = new MasterControllerService();
            MergeHistoryNode mergeHistoryNode = masterControllerService.getMergeHistoryNode(euid); // arg: EUID


            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                    euid,
                    "",
                    "View Merge Tree",
                    new Integer(screenObject.getID()).intValue(),
                    "View Merge Tree");

            MergeHistoryNode node = mergeHistoryNode;
            TextNode euidNode = null;
            euidNode = buildTree(node, euidNode);

            htmlNodeTreeDataModel.addNode(euidNode);

//        } catch (ProcessingException ex) {
//            mLogger.error(mLocalizer.x("VMT001: Failed to get view merge tree  records:{0}", ex.getMessage()), ex);
//            return SERVICE_LAYER_ERROR;
//        } catch (UserException ex) {
//            mLogger.error(mLocalizer.x("VMT002: Failed to get view merge tree  records:{0}", ex.getMessage()), ex);
//            return SERVICE_LAYER_ERROR;
//        } catch (Exception ex) {
//            mLogger.error(mLocalizer.x("VMT003: Failed to get view merge tree  records:{0}", ex.getMessage()), ex);
//            return SERVICE_LAYER_ERROR;
//        }
            // modified exceptional handling logic
            }catch  (Exception ex) {
            if (ex instanceof ValidationException) {                
                mLogger.error(mLocalizer.x("VMT001: Failed to get view merge tree  records:{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("VMT002: Failed to get view merge tree  records:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("VMT003: Failed to get view merge tree  records:{0}"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            return SERVICE_LAYER_ERROR;
        }
 
        return SUCCESS;
    }

    public TextNode buildTree(MergeHistoryNode rootNode, TextNode textNode) throws ObjectException {
        if (textNode == null) {
            textNode = new TextNode(rootNode.getEUID(), rootNode.getEUID());
        }
        TextNode leftTextNode = new TextNode(rootNode.getSourceNode().getEUID(), rootNode.getSourceNode().getEUID());
        TextNode rightTextNode = new TextNode(rootNode.getDestinationNode().getEUID(), rootNode.getDestinationNode().getEUID());

        String transactionNumberRoot = rootNode.getTransactionObject().getTransactionNumber();
        String urlRoot = "transeuiddetails.jsf?transactionId=" + transactionNumberRoot + "&function=euidMerge";
        textNode.setHref(urlRoot);

        textNode.addNode(leftTextNode);
        textNode.addNode(rightTextNode);

        leftTextNode.setHref(urlRoot);
        rightTextNode.setHref(urlRoot);



        MergeHistoryNode rootNodeLeft = rootNode.getSourceNode();
        if (rootNodeLeft.getTransactionObject() != null) {
            buildTree(rootNodeLeft, leftTextNode);
        }
        MergeHistoryNode rootNodeRight = rootNode.getDestinationNode();
        if (rootNodeRight.getTransactionObject() != null) {
            buildTree(rootNodeRight, rightTextNode);
        }
        return textNode;
    }
}
