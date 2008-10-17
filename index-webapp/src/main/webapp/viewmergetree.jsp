<%--
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

--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="javax.faces.context.FacesContext"  %>


 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Merge Tree</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<!-- Required CSS> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/yui4jsfResources.jsf?name=tree.css&folder=treeview&fromAssets=true"--> 
	<link type="text/css" rel="stylesheet" href="css/yui/treeview/assets/skins/sam/treeview-skin.css"> 
       </head>
<%
 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
//set locale value
if(session!=null ){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
<f:view>
 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       
<body>	
     <table  width="100%">
         <tr>
             <th align="left" title="<%=bundle.getString("move")%>"><h:outputText value="#{msgs.merge_tree_text}"/></th>
             <th align="right">
                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:closeTree();"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:closeTree();"><img src="images/close.gif" width="12px" height="12px" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
             </th>
         </tr>

         <tr>
             <td colspan="2" align="left">
                     <yui:treeView id="treeView" value="#{ViewMergeTreeHandler.htmlNodeTreeDataModel}" expandAnim="FADE_IN" collapseAnim="fade_out"></yui:treeView>
             </td>
         </tr>
     </table>
 </body>
</f:view>
</html>
