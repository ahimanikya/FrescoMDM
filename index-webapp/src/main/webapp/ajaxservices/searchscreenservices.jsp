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
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ScreenConfiguration"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>



<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchCriteria"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchOptions"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathAPI"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.model.SelectItem" %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%
//
//This page is an Ajax Service, never to be used directly from the Faces-confg.
//This will render a datatable of minor object to be rendered on the calling JSP.
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Merge Tree</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
     </head>
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
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
double rand = java.lang.Math.random();

HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
ScreenConfiguration  screenConfiguration = (facesSession.getAttribute("ScreenConfiguration") != null ) ? (ScreenConfiguration) facesSession.getAttribute("ScreenConfiguration") : new ScreenConfiguration();
SourceHandler  sourceHandler   = new SourceHandler();
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
NavigationHandler navigationHandler = new NavigationHandler();
String selectedSearchType = request.getParameter(bundle.getString("search_Type"));

ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
String tagName  = navigationHandler.getTagNameByScreenId(objScreenObject.getID()) ;
ArrayList searchScreenGroupsList  = screenConfiguration.getFieldGroupList(selectedSearchType);

String global_daysOfWeek  = bundle.getString("global_daysOfWeek");
String global_months = bundle.getString("global_months");
String cal_prev_text = bundle.getString("cal_prev_text");
String cal_next_text = bundle.getString("cal_next_text");
String cal_today_text = bundle.getString("cal_today_text");
String cal_month_text = bundle.getString("cal_month_text");
String cal_year_text = bundle.getString("cal_year_text");
String  dateFormat = ConfigManager.getDateFormat();
%>
<table>
  <tr>
    <td>
	   <script>
	           document.getElementById("selectedSearchType").value = "<%=selectedSearchType%>";
       </script>
	</td>
  </tr>
</table>
		<table width="100%" cellpadding="0" cellspacing="0">
					 <tr><td colspan="2">&nbsp;</td></tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                         <% if(screenConfiguration.getInstructionLine() != null) {%>
						   <tr>
							 <td colspan="2" align="left"><p><nobr><%=screenConfiguration.getInstructionLine()%></nobr></p>
							 </td>
						   </tr>
						   <%}%>
							<%
							  for(int i = 0 ; i < searchScreenGroupsList.size(); i++) {
                                 FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) searchScreenGroupsList.get(i);

							%>
							   <tr><td>&nbsp;</td></tr>
							   <%if( basicSearchFieldGroup.getDescription() != null ) { %>
							   <tr>
							     <td>
							      <font style="color:blue"><%=basicSearchFieldGroup.getDescription()%></font>
							     </td>
							   </tr>
							   <%}%>
                               <%
								 ArrayList fieldGroupArrayList  = (ArrayList)screenConfiguration.getSearchScreenHashMap().get(basicSearchFieldGroup.getDescription());
								%>
							   <%for(int j = 0 ; j < fieldGroupArrayList.size() ; j++) {
								  ArrayList fieldConfigArrayList = (ArrayList) fieldGroupArrayList.get(j);
								  ValueExpression fieldConfigArrayListVar = ExpressionFactory.newInstance().createValueExpression( fieldConfigArrayList,  fieldConfigArrayList.getClass()); 	

								%>
								<tr>
								<%for(int k = 0 ; k < fieldConfigArrayList.size() ; k++) {
								  FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayList.get(k);
									   String title = (tagName.equalsIgnoreCase("record-details") && fieldConfig.isRange()) ? fieldConfig.getDisplayName(): fieldConfig.getName();
									   int maxlength = (fieldConfig.getName().equalsIgnoreCase("EUID")) ? sourceHandler.getEuidLength(): fieldConfig.getMaxSize();
								%>
							     <td>
									<nobr>											
										<%if(fieldConfig.isOneOfTheseRequired()) {%>
											 <span style="font-size:9px;color:blue;verticle-align:top">&dagger;&nbsp;</span>
										<%}%>
										<%if(fieldConfig.isRequired()) {%>
											 <span style="font-size:9px;color:red;verticle-align:top">*&nbsp;</span>
										<%}%>
										<%=fieldConfig.getDisplayName()%>
									</nobr>
								 
								 </td>
							      <td>
								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("MenuList")) {%>
								             <% if( "SystemCode".equalsIgnoreCase(fieldConfig.getName()))  {%>
                                                <select title="<%=fieldConfig.getName()%>"
												        name="<%=fieldConfig.getName()%>" 
                                                        onchange="javascript:setLidMaskValue(this,'advancedformData')"
												        id="SystemCode">	
											 <%} else {%>
                                               <select title="<%=fieldConfig.getName()%>"
												     name="<%=fieldConfig.getName()%>" >	
											<%}%>
                                              <%PullDownListItem[]   pullDownListItemArray = fieldConfig.getPossibleValues();%>
											    <option value=""></option>
											    <%for(int p = 0; p <pullDownListItemArray.length;p++) {%>
											     	<option value="<%=pullDownListItemArray[p].getName()%>"><%=pullDownListItemArray[p].getDescription()%></option>
												<%}%>
											</select>

								  <%}%>
								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("TextArea")) {%>
                                      <textarea 
									            id="<%=fieldConfig.getName()%>" 
												title="<%=title%>"
												name="<%=fieldConfig.getName()%>" ></textarea>
                                <%}%>

								  <%if(fieldConfig.getGuiType().equalsIgnoreCase("TextBox")) {
									  %>
									  <%if(fieldConfig.getName().equalsIgnoreCase("LID")) {%>
                                                 <input type="text" 
												       id="LID"
												       title="<%=title%>"
												       name="<%=fieldConfig.getName()%>" 
													   maxlength="<%=maxlength%>" 
                                                       readonly="true"
													   onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                       onkeyup="javascript:qws_field_on_key_up(this)"
                                                       onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"/>
 
									  <%} else {%>
									   <%if(fieldConfig.getValueType() == 6 ) {%>
                                          <nobr>
                                            <input type="text" 
												   id="<%=title%>"
												   title="<%=title%>"
												   name="<%=fieldConfig.getName()%>" 
												   maxlength="<%=maxlength%>" 
 												   size="<%=fieldConfig.getMaxLength()%>"
													onkeydown="javascript:qws_field_on_key_down(this, '<%=(fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0)?fieldConfig.getInputMask():""%>')"
													onkeyup="javascript:qws_field_on_key_up(this)"                                          onblur="javascript:validate_date(this,'<%=dateFormat%>')">
                                                  <a href="javascript:void(0);" 
												     title="<%=title%>"
                                                     onclick="g_Calendar.show(event,
												          '<%=title%>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<%=title%> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                          </nobr>
                                       <%} else {%>

                                                <input type="text" 
												       title="<%=title%>"
												       name="<%=fieldConfig.getName()%>" 
													   maxlength="<%=maxlength%>" 
 													   size="<%=fieldConfig.getMaxLength()%>"
													   onkeydown="javascript:qws_field_on_key_down(this, '<%=(fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0)?fieldConfig.getInputMask():""%>')"
													   onkeyup="javascript:qws_field_on_key_up(this)" />

                                       <%}%>

									  <%}%>
 								  <%}%>
								  </td>
  							   <%}%>
							   </tr>
							   <%}%>

							<%}%>
														 <tr><td colspan="2">&nbsp;</td></tr>
							</table>
							<%if(request.getParameter("myStrVar") != null ) {%>
	   	<% 
		  String qryString  = request.getQueryString();
	      qryString  = qryString.replaceAll("collectEuids=true","");
          Enumeration parameterNames = request.getParameterNames();
		%>

							   <table>
							     <tr>
								   <td>
							     <script>
								   <% while(parameterNames.hasMoreElements())   { 
										String attributeName = (String) parameterNames.nextElement();
										String attributeValue = (String) request.getParameter(attributeName);
										//replace the wild character
										attributeValue  = attributeValue.replaceAll("~~","%");
								   %>
									populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
								   <%}%>
								     var queryStrVar = "<%=qryString%>";
									 <%if(tagName.equalsIgnoreCase("record-details") ) {%>
							          ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?random='+rand+'&'+queryStrVar,'outputdiv','');
									 <%} else if(tagName.equalsIgnoreCase("assumed-matches") ) {%>
                                        ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=rand'+'&'+queryStr,'outputdiv','')
									 <%} else if(tagName.equalsIgnoreCase("duplicate-records") ) {%>
										ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStrVar,'outputdiv','');
									 <%} else if(tagName.equalsIgnoreCase("audit-log") ) {%>
										ajaxURL('/<%=URI%>/ajaxservices/auditlogservice.jsf?random='+rand+'&'+queryStrVar,'outputdiv','');
									 <%} else if(tagName.equalsIgnoreCase("transactions") ) {%>
										ajaxURL('/<%=URI%>/ajaxservices/transactionservice.jsf?random='+rand+'&'+queryStrVar,'outputdiv','');
									 <%}%>
							     </script>
								 </td></tr></table>
 							<%}%>
<%} %>  <!-- Session check -->
</html>
