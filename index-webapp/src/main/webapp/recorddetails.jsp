<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler"  %>
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
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>

<f:view>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            
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
<script>
//not used in transactions, but since its require in the script we fake it
var editIndexid = ""; 
var thisForm;
var rand = "";
var popUrl;
// Fix for 6700851 on 07-01-09 
var euidList = ""; 
function setRand(thisrand)  {
	rand = thisrand;
}

var checkedItems = new Array();
function getCheckedValues(a,v)   {
   if (a.checked == true)  {
      checkedItems.push(v);
   } else {
     for(i=0;i<checkedItems.length;i++){
       if(v == checkedItems[i]) checkedItems.splice(i, 1);
     } 
  }
}

function align(thisevent,divID) {
	divID.style.visibility= 'visible';
	divID.style.top = thisevent.clientY-180;
	divID.style.left= thisevent.clientX;
}
</script>


</head>
<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Enumeration parameterNames = request.getParameterNames();
   Operations operations=new Operations();
   HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   RecordDetailsHandler  recordDetailsHandler = (facesSession.getAttribute("RecordDetailsHandler") != null ) ? (RecordDetailsHandler) facesSession.getAttribute("RecordDetailsHandler") : new RecordDetailsHandler();
   SourceHandler sourceHandler= new SourceHandler();
%>

<title><h:outputText value="#{msgs.application_heading}"/></title>  
<body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
  <div id="mainContent">
	  <table border="0">
	   <tr>
		<td> 
			<div class="duplicaterecords" style="visibility:visible;display:block">
				<table border="0">
				<tr>
				    <td>&nbsp;</td>
				     <td>
 							 <table border="0" cellpadding="0" cellspacing="0" align="right"  width="100px">
								<form id="searchTypeForm" >
									<tr>
										<td>
											 <h:outputText  rendered="#{RecordDetailsHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
													   <select id="searchTypeList" title="<%=bundle.getString("search_Type")%>" 
													   onchange="javascript:document.getElementById('messages').innerHTML='';
													   document.getElementById('outputdiv').innerHTML ='';document.getElementById('SearchCriteria').innerHTML ='';getRecordDetailsFormValues('searchTypeForm');checkedItems = new Array();setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchscreenservices.jsf?random='+rand+'&'+queryStr,'SearchCriteria','')" style="width:220px;">	
													  <%ArrayList   searchListItemArray = recordDetailsHandler.getPossilbeSearchTypes();%>
														<%for(int p = 0; p <searchListItemArray.size();p++) {
																SelectItem selectItem = (SelectItem) searchListItemArray.get(p);
															%>
															<%if(request.getParameter("selectedSearchType") != null && request.getParameter("selectedSearchType").equalsIgnoreCase(selectItem.getLabel())) {%>
															  <option value="<%=selectItem.getValue()%>" selected="true"><%=selectItem.getLabel()%></option>
															<%} else {%>
															<option value="<%=selectItem.getValue()%>"><%=selectItem.getLabel()%></option>
															<%}%>
														<%}%>
													 </select>
										</td>
									</tr>
								</form>
						  </table>

					 </td>
 				<tr>
				   <td colspan="2">
						<h:form id="advancedformData">
 							<table border="0" cellpadding="0" cellspacing="0">
							<input id='lidmask' type='hidden' title='lidmask' name='lidmask' value='' />
							<input type="hidden" id="selectedSearchType" title='selectedSearchType' 
							value='<%=recordDetailsHandler.getSelectedSearchType()%>' />
								<tr>
									<td colspan="2">
									   <div id="SearchCriteria">
 										<table width="100%" cellpadding="0" cellspacing="0">
										<%if(recordDetailsHandler.getInstructionLine() != null ) {%>
 										   <tr><td colspan="2"><%=recordDetailsHandler.getInstructionLine()%></td></tr>
 									   <%}%>
										<%
										  for(int i = 0 ; i < recordDetailsHandler.getSearchScreenFieldGroupArray().size(); i++) {
											 FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) recordDetailsHandler.getSearchScreenFieldGroupArray().get(i);

										%>
										   <%if( basicSearchFieldGroup.getDescription() != null ) { %>
										   <tr>
											 <td colspan="2">
											  <font style="color:blue"><%=basicSearchFieldGroup.getDescription()%></font>
											 </td>
										   </tr>
										   <%}%>
										   <%
											 ArrayList fieldGroupArrayList  = (ArrayList)recordDetailsHandler.getSearchScreenHashMap().get(basicSearchFieldGroup.getDescription());
											%>
										   <%for(int j = 0 ; j < fieldGroupArrayList.size() ; j++) {
											  ArrayList fieldConfigArrayList = (ArrayList) fieldGroupArrayList.get(j);
											  ValueExpression fieldConfigArrayListVar = ExpressionFactory.newInstance().createValueExpression( fieldConfigArrayList,  fieldConfigArrayList.getClass()); 	

											%>
											<tr>
											<%for(int k = 0 ; k < fieldConfigArrayList.size() ; k++) {
											  FieldConfig fieldConfig = (FieldConfig) fieldConfigArrayList.get(k);
												   String title = (fieldConfig.isRange()) ? fieldConfig.getDisplayName(): fieldConfig.getName();
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
									   </div> <!-- Search div ends here -->
										<table  cellpadding="0" cellspacing="0" style="	border:0px red solid;padding-left:20px">
											<tr>
												<td align="left">
													<nobr>
													<% if(operations.isEO_SearchViewSBR()){%>	
													   <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:
													   document.getElementById('messages').innerHTML='';
													   getRecordDetailsFormValues('advancedformData');checkedItems = new Array();setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
														   <span>
															 <h:outputText value="#{msgs.search_button_label}"/>
														   </span>
													   </a>
													 <%}%>
													</nobr>
													<nobr>
														<h:outputLink  title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:
														document.getElementById('messages').innerHTML='';
														ClearContents('advancedformData')">
															<span><h:outputText value="#{msgs.clear_button_label}"/></span>
														</h:outputLink>
													</nobr>                                        
												</td>
											</tr>
										</table>
									</td>
								</tr><tr><td>&nbsp</td></tr>
							   <tr>
								 <td><div id="messages" class="ajaxalert" valign="bottom"></div></td>
							   </tr>

							</table>
						</h:form>

					</td>
				 </tr>
				</table>
						<h:panelGrid>
						   <h:panelGroup rendered="#{RecordDetailsHandler.oneOfGroupExists}">
								<tr> <!-- inline style required to override the class defined in CSS -->
									<td style="font-size:10px;">
									   <hr/>
										<nobr>
											 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
										</nobr>
									</td>
								</tr>
						   </h:panelGroup>

						   <h:panelGroup rendered="#{RecordDetailsHandler.requiredExists}">
								<tr>
									<td style="font-size:10px;">
										<nobr>
											 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
										</nobr>
									</td>
								</tr>
						   </h:panelGroup>

						</h:panelGrid>
					   <div class="reportresults" id="outputdiv"></div>
			</div>
       </td>
	  </tr>
	 </table>
  </div><!--  id="mainContent" -->
			<form id="collectEuidsForm">
			      <input type="hidden" id="collectEuids" title='collectEuids' />   
            </form>

</body>
<div class="alert" id="ajaxOutputdiv"></div> 

        <%
		  
          String[][] lidMaskingArray = recordDetailsHandler.getAllSystemCodes();
          
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
        
		 function setLidMaskValue(field,formName) {
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');
			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).readOnly = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
         var selectedSearchValue = document.getElementById("searchTypeList").options[document.getElementById("searchTypeList").selectedIndex].value;
         document.getElementById("selectedSearchType").value = selectedSearchValue;
                  

	if( document.advancedformData.elements[0]!=null) {
		var i;
		var max = document.advancedformData.length;
		for( i = 0; i < max; i++ ) {
			if( document.advancedformData.elements[ i ].type != "hidden" &&
				!document.advancedformData.elements[ i ].disabled &&
				!document.advancedformData.elements[ i ].readOnly ) {
				document.advancedformData.elements[ i ].focus();
				break;
			}
		}
     }         


</script>
<!-- Added  on 22-aug-2008 to incorparte with the functionality of back button in euiddetails.jsp  -->
    <%if(request.getParameter("back")!=null){%>
  <script>
		 <% 
		  String qryString  = request.getQueryString();
	      qryString  = qryString.replaceAll("collectEuids=true","");
		%>
		 setRand(Math.random());
         var queryStrVar = '<%=qryString%>';
   
   <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);
		//replace the wild character
        attributeValue  = attributeValue.replaceAll("~~","%");
   %>
    populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
   <%}%>
   </script>
   <%
	recordDetailsHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));   
	recordDetailsHandler.setSearchType(request.getParameter("selectedSearchType")); 
	
	facesSession.setAttribute("RecordDetailsHandler",recordDetailsHandler);

   %>
   <%}%>
      
    <script type="text/javascript">
     function closeDiv()    {                            
        document.getElementById('ajaxOutputdiv').style.visibility='hidden';
        document.getElementById('ajaxOutputdiv').style.display='none';
     }
</script>
<!-- Added  on 27-09-2008, to change alert pop up window to information pop up window -->
<div id="activeDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="activemessageDiv"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td>
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:window.location = popUrl;" class="button" >
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </a>
							  </td>
							 </tr>
							 </table>
					     </td>
                     </tr> 
                 </table>
             </form>
         </div>
</html>
<script type="text/javascript">
    makeDraggable("activeDiv");
  </script>
       <%if(request.getParameter("back")!=null){%>
	   	<% 
		  String qryString  = request.getQueryString();
	      qryString  = qryString.replaceAll("collectEuids=true","");
		%>

    <script>
            var queryStrVar = '<%=qryString%>';
		    getRecordDetailsFormValues('searchTypeForm');
			ajaxURL('/<%=URI%>/ajaxservices/searchscreenservices.jsf?random='+rand+'&'+queryStr+'&myStrVar='+queryStrVar,'SearchCriteria','');
   </script>
   <%}%>

</f:view>












