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
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:view>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

     <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
<%
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
double rand = java.lang.Math.random();
boolean isSessionActive = true;
%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>

<%if (isSessionActive)  {%>
<%
        ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String undoAssumedMatchId = (String)request.getParameter("undoAssumedMatchId");
        AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
		String newEuid = assumeMatchHandler.undoMatch(undoAssumedMatchId);
		String mainEUIDAssume= request.getParameter("mainEUID");
%>
											       <table border="0" cellpadding="0" cellspacing="0" >
											         <tr><th title="<%=bundle.getString("move")%>"><%=bundle.getString("popup_information_text")%></th>
													 <th>
														<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:window.location.reload();"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

														<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:window.location.reload();"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
													</th>
													<tr><td colspan="2">&nbsp;</td></tr>
													 </tr>
											         <tr>
													       <td colspan="2"><nobr>&nbsp;<%=bundle.getString("merge_result")%>&nbsp;<%=mainEUIDAssume%>&nbsp;<%=bundle.getString("merge_result1")%></nobr>
														   </td>
													 </tr>
													 <tr>
													    <td colspan="2"><nobr><%=bundle.getString("merge_result2")%>&nbsp;<%=newEuid%>&nbsp;<%=bundle.getString("merge_result3")%><nobr>
														</td>
												     </tr>
													 <tr><td colspan="2"><nobr><%=bundle.getString("view_new_EO")%></nobr></td></tr>
													 <tr><td colspan="2">&nbsp;</td></tr>
											         <tr id="actions">
											            <td colspan="2">
														  <table align="center">
														    <tr>
															 <td>&nbsp;</td>
															 <td>
                                                                <a class="button" title="<h:outputText value="#{msgs.ok_text_button}" />" href="/<%=URI%>/euiddetails.jsf?euid=<%=newEuid%>"> <span><%=bundle.getString("ok_text_button")%></span></a>
														     </td>
														     <td>
                                                                <a class="button" title="<h:outputText value="#{msgs.cancel_but_text}" />"  href='javascript:void(0)' onclick="javascript:window.location.reload();"><span><%=bundle.getString("login_cancel_button_prompt")%></span></a>
															</td>
														   </tr>
														 </table>
											           </td>
											         </tr>
											        </table>

<%}%> <!-- End session check if condition -->



</f:view>
