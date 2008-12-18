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
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchScreenConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DuplicateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DeactivatedReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.MergeRecordHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UnmergedRecordsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UpdateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AuditLogHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
 

<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="java.text.SimpleDateFormat" %>


<script>
var editIndexid = "";
var thisForm;
var rand = "";
var outputDivId = "";

function setRand(thisrand)  {
	rand = thisrand;
}
</script>
<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   int pageSize = 10;
   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   SourceHandler sourceHandler = new SourceHandler();
   Date myDate  = new Date();
   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("yyyy");
   String dateField = simpleDateFormatFields.format(myDate);
   int intCurrentYear =  new Integer(dateField).intValue();
%>
<%
//set locale value
if(session!=null ){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
<f:view>
    <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
    
    <html>
       <head>
<title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="./css/yui/yahoo/yahoo-min.js"></script>
            <!-- Additional source files go here -->
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" >
            <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css">
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
            <!--there is no custom header content for this example-->
             <!--CSS file (default YUI Sam Skin) -->
            <link  type="text/css" rel="stylesheet" href="./css/yui/datatable/assets/skins/sam/datatable.css">
            <!-- Dependencies -->
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/datasource/datasource-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/json/json-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/calendar/calendar-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/connection/connection-min.js"></script>
            <!-- Source files -->
            <script type="text/javascript" src="./scripts/yui/datatable/datatable-beta-min.js"></script>
         </head>
<%
  // ResourceBundle bundle = //ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getV//iewRoot().getLocale());
  /* String mergeText = bundle.getString("Merged_Transaction_Report_Label");
   String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
   String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
   String updateText = bundle.getString("Updated_Record_Report_Label");
   String activityText = bundle.getString("Activity_Report_Label");
   String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
   String assumedText = bundle.getString("Assumed_Matches_Report_Label"); */
%>
        
        <body class="yui-skin-sam">
             <% Operations operations=new Operations();%>
             <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden;">                 
            <div id="reports">
                        <!--td><%=(String)request.getAttribute("tabName")%></td-->
                              <%

                               String mergeText = bundle.getString("Merged_Transaction_Report_Label");
                               String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
                               String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
                               String updateText = bundle.getString("Updated_Record_Report_Label");
                               String activityText = bundle.getString("Activity_Report_Label");
                               String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
                               String assumedText = bundle.getString("Assumed_Matches_Report_Label");
                                ScreenObject subScreenObj = null;

								//get the Sorted Screen objects
								 ScreenObject screenObjectObj = (ScreenObject) session.getAttribute("ScreenObject");
   								ReportHandler reportHandler = new ReportHandler();
                                ScreenObject[] orderdedScreens  = reportHandler.getOrderedScreenObjects();
                                Iterator messagesIter = FacesContext.getCurrentInstance().getMessages();
  								%>
                      <%
					  if(orderdedScreens != null ) {
                                String reportTabName = (request.getAttribute("reportTabName") != null)?(String) request.getAttribute("reportTabName"):orderdedScreens[0].getDisplayTitle();
								String tabName = "";
                                String clasName = "";
						   %>
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                        <td>
                            <div id="demo" class="yui-navset">                               
                                <ul class="yui-nav">
                                 <%   for(int i=0;i<orderdedScreens.length;i++){  
                                        subScreenObj = orderdedScreens[i];
                                        tabName =  subScreenObj.getDisplayTitle(); 
										//tabName = tabName.replaceAll("Report","");
                                        if (reportTabName.equalsIgnoreCase(tabName)) { %>
                                            <li class="selected" >
										        <a title="<%=tabName%>" href="#tab<%=i+1%>"> <em><%=tabName%></em></a>
										    </li>             
                                        <%} else {%>
                                            <li>
										        <a title="<%=tabName%>" href="#tab<%=i+1%>"> <em><%=tabName%></em></a>
										    </li>             
                                        <%}	%>
                                 <%

											}%>
                                </ul>            
                                
                           <div class="yui-content">

                                 <%   for(int i=0;i<orderdedScreens.length;i++){  
                                           ArrayList screenConfigList = orderdedScreens[i].getSearchScreensConfig();
									       SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) screenConfigList.get(0);
									       ArrayList  basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                                           ValueExpression basicSearchFGValueExpression = ExpressionFactory.newInstance().createValueExpression(basicSearchFieldConfigs, basicSearchFieldConfigs.getClass());
										   boolean isActivityBoolean = (activityText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) ? true :false; 
										   Boolean isActivityReport =  new Boolean(isActivityBoolean);
                                           ValueExpression isActivityReportValueExpression = ExpressionFactory.newInstance().createValueExpression(isActivityReport, isActivityReport.getClass());
                                           Integer integerValue = new Integer(i+1);
										   ValueExpression loopIndexVE = ExpressionFactory.newInstance().createValueExpression(integerValue, integerValue.getClass());
									 %>
									     <div id="tab<%=i+1%>">
										   <form id="form<%=i%>">
										             <input id='lidmask' type='hidden' name='lidmask' value='' />
											         <div class="reportYUISearch1" >
													 <%if(activityText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle()) ){%>
													 <script>
										               outputDivId = "results<%=i%>";
										             </script>
													 <%}%>
													 <h:panelGrid rendered="<%=isActivityReportValueExpression%>">
													   <h:panelGroup>
													    <span class="inlinefonts">
													     <h:outputText value="#{msgs.activity_type}"/>
														</span>&nbsp;
														<h:selectOneMenu title="activityType" styleClass="selectContent" id="viewreports" onchange="javascript:showActivityTypes(this.value,outputDivId);" value="#{ReportHandler.frequency}" >
                                                           <f:selectItems value="#{ReportHandler.activityReportTypes}"/>
                                                        </h:selectOneMenu>      
													   </h:panelGroup>
													   </h:panelGrid>
												  <input type="hidden" title="tabName" name="reportName" value="<%=orderdedScreens[i].getDisplayTitle()%>"/>
                                                  <%if(isActivityBoolean) {%>
												  <table cellspacing="4" cellpadding="4" >
												  <tr>
												    <td>
													  <div id="ActivitySearchCriteria">
												           <%    
															 String inputMaskVar  = dateFormat;
														     inputMaskVar = inputMaskVar.toUpperCase();
                                                             inputMaskVar = inputMaskVar.replace("MM","DD");
														     inputMaskVar = inputMaskVar.replace("DD","DD");
														     inputMaskVar = inputMaskVar.replace("YYYY","DDDDD");
															%>
															  <table>
															    <tr>
																<td>
																<span class="inlinefonts"><%=bundle.getString("fromdate_label_text")%></span>
																</td>
																<td>
																<input type="text" 
																	   id="WeeklyDate"
																	   title="StartDate"
 																	  onkeydown="javascript:qws_field_on_key_down(this, '<%=inputMaskVar%>')"
																		onkeyup="javascript:qws_field_on_key_up(this)"                         onblur="javascript:validate_date(this,'<%=dateFormat%>')">
																	  <a href="javascript:void(0);" 
																		 title="WeeklyDate"
																		 onclick="g_Calendar.show(event,
																			  'WeeklyDate',
																			  '<%=dateFormat%>',
																			  '<%=global_daysOfWeek%>',
																			  '<%=global_months%>',
																			  '<%=cal_prev_text%>',
																			  '<%=cal_next_text%>',
																			  '<%=cal_today_text%>',
																			  '<%=cal_month_text%>',
																			  '<%=cal_year_text%>')" 
																			  ><img  border="0"  title="WeeklyDate (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
																	  <font class="dateFormat">(<%=dateFormat%>)</font>
																	  </td>
																	  </tr>
																	  </table>
														  
													  </div>
													</td>
												  </tr>
												  </table>
												  <%} else {%>
												  <table>
												  <tr><td>&nbsp;</td></tr>
												   <%
													 ArrayList fieldGroupArrayList  = reportHandler.getFieldGroupList(objSearchScreenConfig);
													%>
												   <%for(int j = 0 ; j < fieldGroupArrayList.size() ; j++) {
													  ArrayList fieldConfigArrayList = (ArrayList) fieldGroupArrayList.get(j);
													%>
													<tr>
													<%for(int k = 0 ; k < fieldConfigArrayList.size() ; k++) {
													  FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayList.get(k);

														   String title = fieldConfig.getName();
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
															<span class="inlinefonts">
															<%=fieldConfig.getDisplayName()%>
															</span>
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
																	   id="<%=i%><%=title%>"
																	   title="<%=title%>"
																	   name="<%=fieldConfig.getName()%>" 
																	   maxlength="<%=maxlength%>" 
																	   size="<%=fieldConfig.getMaxLength()%>"
																		onkeydown="javascript:qws_field_on_key_down(this, '<%=(fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0)?fieldConfig.getInputMask():""%>')"
																		onkeyup="javascript:qws_field_on_key_up(this)"                                          onblur="javascript:validate_date(this,'<%=dateFormat%>')">
																	  <a href="javascript:void(0);" 
																		 title="<%=title%>"
																		 onclick="g_Calendar.show(event,
																			  '<%=i%><%=title%>',
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

												  </table>
												  <%}%>
<!-- Action Buttons -->   
													<table  cellpadding="0" cellspacing="0">
													 <tr>
													   <td>
													  <nobr>
													  <% if (operations.isReports_Duplicates() && potDupText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>   <!--  Potential  Duplicate Report -->										 
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"  href="javascript:void(0)" id = "deactivateReport" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
												
													 
													 <% } else if (operations.isReports_DeactivatedEUIDs() && deactiveText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Deactivated Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_MergedRecords() && mergeText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>  <!--  Merged Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "mergeText" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>

													 <% } else if (operations.isReports_UnmergedRecords() && unmergeText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  UnMerge Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_Updates() && updateText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Update Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_Activity() && activityText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Activity Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_AssumedMatches() && assumedText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>  <!--  Assumed Match Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } %>
													 </nobr>
												    </td>
												    <td>
													   <nobr>
														  <a class="button"  title="<h:outputText value="#{msgs.clear_button_label}"/>" href="javascript:void(0)" onclick="javascript:
                                                          document.getElementById('messages').innerHTML='';
														  if(document.getElementById('errormessages1')!=null){
														  document.getElementById('errormessages1').innerHTML='';
														  }
														  if(document.getElementById('errormessages')!=null){
														   document.getElementById('errormessages').innerHTML='';
														  }	
														  ClearContents('form<%=i%>')" >
															<span><h:outputText value="#{msgs.clear_button_label}"/> </span>
														  </a>
													   </nobr>
													 </td>
													</tr>
													<tr>
													  <td>&nbsp;
													  </td>
													</tr>
													<tr>
													   <td colspan="2" align="center"><div class="ajaxalert" id="messages"> </div></td>									
													</tr>

				  <% if (reportHandler.isOneOfGroupExists(orderdedScreens[i].getDisplayTitle()) ) {%>
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;" colspan="2">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
					<%}%>

					<% if (reportHandler.isRequiredExists(orderdedScreens[i].getDisplayTitle()) ) {%>			
					<tr>
						<td style="font-size:10px;" colspan="2">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
					<%}%>

												  </table>
			    <table>
				</table>

												  <input type="hidden" value="<%=i%>" name="layer" title="layer" />
<!-- End Action Buttons -->
										  </form>
<!-- Results Div -->
										</div>  
                                          <div class="reportresults" id="results<%=i%>"></div>
                                     </div>                                       
                                 <%}%>
<%} else {%> 

    <div class="ajaxalert" id="errormessages">
    <table cellpadding="0" cellspacing="0" border="0">	   
        <% int i=0;
           while (messagesIter.hasNext()) {
              FacesMessage facesMessage = (FacesMessage) messagesIter.next();
		%>
		    <% if (i == 0) {%>
			<tr> 
			  <td><ul><li>		         
                   <%=bundle.getString("CONFIG_ERROR")%>&nbsp;:&nbsp;<%=facesMessage.getSummary()%>
  	          </li></ul></td>
			</tr> 
			<tr>
			  <td>&nbsp;</td>
			</tr>
		   	 <tr> 
			  <td>
			<% } else { %>
			     <ul><li><%=facesMessage.getSummary()%></li></ul>
			<%} %>
			<%i++;%>
         <%}%>     	 
			   </td>
			 </tr> 
	 </table>
	 </div>
<%}%>

                            </div> <!-- End YUI content -->
							    
                            </div> <!-- demo end -->
                        </td>
                    </tr>
                </table>
            </div>      
			
		  <div id="<%=bundle.getString("WEEKLY_ACTIVITY")%>" style="visibiity:hidden;display:none;">
			   <%
				 String inputMaskVar  = dateFormat;
				 inputMaskVar = inputMaskVar.toUpperCase();
				 inputMaskVar = inputMaskVar.replace("MM","DD");
				 inputMaskVar = inputMaskVar.replace("DD","DD");
				 inputMaskVar = inputMaskVar.replace("YYYY","DDDDD");
				%>
				  <table>
					<tr>
						  <td>
						   <span class="inlinefonts">
						    <%=bundle.getString("fromdate_label_text")%>
						   </span>
						 </td>
					   <td>
					<input type="text" 
						   id="WeeklyDate"
						   title="StartDate"
						  onkeydown="javascript:qws_field_on_key_down(this, '<%=inputMaskVar%>')"
							onkeyup="javascript:qws_field_on_key_up(this)"                         onblur="javascript:validate_date(this,'<%=dateFormat%>')">
						  <a href="javascript:void(0);" 
							 title="WeeklyDate"
							 onclick="g_Calendar.show(event,
								  'WeeklyDate',
								  '<%=dateFormat%>',
								  '<%=global_daysOfWeek%>',
								  '<%=global_months%>',
								  '<%=cal_prev_text%>',
								  '<%=cal_next_text%>',
								  '<%=cal_today_text%>',
								  '<%=cal_month_text%>',
								  '<%=cal_year_text%>')" 
								  ><img  border="0"  title="WeeklyDate (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
						  <font class="dateFormat">(<%=dateFormat%>)</font>
						  </td>
						  </tr>
						  </table>
		  </div>
		  <div id="<%=bundle.getString("MONTHLY_ACTIVITY")%>" style="visibiity:hidden;display:none;">
				  <table>
					<tr>
					   <td>
						<span class="inlinefonts">
					    <%=bundle.getString("month_year_selection")%>
						</span>
					   </td>
					   <td>
						<select id="Month" title="Monthly_Month">	
							<option value="01"><%=bundle.getString("MONTH01")%></option>
							<option value="02"><%=bundle.getString("MONTH02")%></option>
							<option value="03"><%=bundle.getString("MONTH03")%></option>
							<option value="04"><%=bundle.getString("MONTH04")%></option>
							<option value="05"><%=bundle.getString("MONTH05")%></option>
							<option value="06"><%=bundle.getString("MONTH06")%></option>
							<option value="07"><%=bundle.getString("MONTH07")%></option>
							<option value="08"><%=bundle.getString("MONTH08")%></option>
							<option value="09"><%=bundle.getString("MONTH09")%></option>
							<option value="10"><%=bundle.getString("MONTH10")%></option>
							<option value="11"><%=bundle.getString("MONTH11")%></option>
							<option value="12"><%=bundle.getString("MONTH12")%></option>
						</select>
					  </td>
					  <td>
						<select id="Year" title="Monthly_Year">	
						   <%for(int year = intCurrentYear - 50  ; year < intCurrentYear + 10 ; year++) {%>
							<%if(year == intCurrentYear ) {%>
							  <option value="<%=year%>" selected="true"><%=year%></option> 
							<%} else {%>
							<option value="<%=year%>"><%=year%></option> 
							<%}%>
						   <%}%>
						</select>
					  </td>
					  </tr>
				 </table>
		  </div>
		  <div id="<%=bundle.getString("YEARLY_ACTIVITY")%>"  style="visibiity:hidden;display:none;">
				  <table>
					<tr>
					   <td>
						 <span class="inlinefonts">
					      <%=bundle.getString("year_selection")%>
						 </span>
					   </td>
					  <td>
						<select id="Year" title="Yearly_year">	
						   <%for(int year = intCurrentYear - 50  ; year < intCurrentYear + 10 ; year++) {%>
							<%if(year == intCurrentYear ) {%>
							  <option value="<%=year%>" selected="true"><%=year%></option> 
							<%} else {%>
							<option value="<%=year%>"><%=year%></option> 
							<%}%>
						   <%}%>
						</select>
					  </td>
					  </tr>
				 </table>
		   </div>

            <form id="lidform" name="lidform">
				 <input id='lidmask' type='hidden' name='lidmask' value='' />
			 </form>
            
            <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
            
            <script>
                (function() {
                var tabView = new YAHOO.widget.TabView('demo');                
                YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
                })();
            </script>
            
            <!--END SOURCE CODE FOR EXAMPLE =============================== -->
            </div>        <!-- End mainContent -->                
<!--BEGIN YUI Table =============================== -->
            
<script>
   (function() {
       var tabView = new YAHOO.widget.TabView('demo');                
       YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
     })();
</script>



        <%
		  SourceAddHandler sourceAddHandler = new SourceAddHandler();
          String[][] lidMaskingArray = sourceAddHandler.getAllSystemCodes();
          
        %>
        <script>
            var systemCodes = new Array();
            var lidMasks = new Array();
        </script>
        
        <%
        for(int i=0;i<lidMaskingArray.length;i++) {
            String[] innerArray = lidMaskingArray[i];
            for(int j=0;j<innerArray.length;j++) {
            
            if(i==0) {
         %>       
         <script>
           systemCodes['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>      
         <%       
            } else {
         %>
         <script>
           lidMasks ['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>
         <%       
            }
           }
           }
        %>
    <script>
         function setLidMaskValue(field) {
			var tokens = new Array();
			thisformName = field.name.split(':');
            formName = thisformName[0]; 
			thisForm = document.getElementById(formName); //update global formName for onblur
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            document.lidform.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }  
         
    </script>


        </body>
    </html>
</f:view>
