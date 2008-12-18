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
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.sql.Timestamp"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Enumeration"%>

<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>


<f:view>    
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
<html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
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
function setRand(thisrand)  {
	rand = thisrand;
 }
</script>

       </head>

    <body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
     <div id="mainContent" >   
<%
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));
   FacesContext facesContext = FacesContext.getCurrentInstance(); 
   String from = (String)facesContext.getExternalContext().getRequestParameterMap().get("where");
  Operations operations=new Operations();
  Enumeration parameterNames = request.getParameterNames();

   HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   AssumeMatchHandler  assumeMatchHandler = (facesSession.getAttribute("AssumeMatchHandler") != null ) ? (AssumeMatchHandler) facesSession.getAttribute("AssumeMatchHandler") : new AssumeMatchHandler();
   SourceHandler sourceHandler= new SourceHandler();

%>
    <div id ="assumedmatches " class="duplicaterecords">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{AssumeMatchHandler.searchType}" valueChangeListener="#{AssumeMatchHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AssumeMatchHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{AssumeMatchHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
		           <tr>
			     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{AssumeMatchHandler.instructionLine}" /></td>
			       </tr>

                    <tr>
                        <td>
						   <%
						   int countFc = 0;
						   ArrayList sList  = assumeMatchHandler.getSearchScreenFieldGroupArray();
						   for(int i=0;i<sList.size();i++) {
							    FieldConfigGroup fcg = (FieldConfigGroup)  sList.get(i);
								countFc = fcg.getFieldConfigs().size();
						   }
                           %>
                            
                            <input id='lidmask' type='hidden' title='lidmask' name='lidmask' value='DDD-DDD-DDDD' />
							<div id="SearchCriteria">
							<table width="100%" cellpadding="2" cellspacing="2">
							<%
							  for(int i = 0 ; i < assumeMatchHandler.getSearchScreenFieldGroupArray().size(); i++) {
                                 FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) assumeMatchHandler.getSearchScreenFieldGroupArray().get(i);

							%>
							   <tr><td>&nbsp;</td></tr>
							   <%if(basicSearchFieldGroup.getDescription() != null ) {%>
							   <tr>
							     <td>
							      <font style="color:blue"><%=basicSearchFieldGroup.getDescription()%></font>
							     </td>
							   </tr>
							   <%}%>
                               <%
								 ArrayList fieldGroupArrayList  = (ArrayList)assumeMatchHandler.getSearchScreenHashMap().get(basicSearchFieldGroup.getDescription());
								%>
							   <%for(int j = 0 ; j < fieldGroupArrayList.size() ; j++) {
								  ArrayList fieldConfigArrayList = (ArrayList) fieldGroupArrayList.get(j);
								  ValueExpression fieldConfigArrayListVar = ExpressionFactory.newInstance().createValueExpression( fieldConfigArrayList,  fieldConfigArrayList.getClass()); 	

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
							</table>
							</div>
                             <table  cellpadding="0" cellspacing="0">
								 <tr>
								  <td>
								  <nobr>
								  <% if(operations.isAssumedMatch_SearchView()){%>	
								   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"  id = "deactivateReport" href="javascript:void(0)"
								   onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=rand'+'&'+queryStr,'outputdiv','')">
										 <span><h:outputText value="#{msgs.search_button_label}"/></span>
									</a>
								 <%}%>
								</nobr>
								   <nobr>
									  <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:
               						  document.getElementById('messages').innerHTML='';
									  ClearContents('advancedformData')" >
										<span><h:outputText value="#{msgs.clear_button_label}"/></span>
									  </h:outputLink>
								   </nobr>									
							  </td>
								</tr>
                             </table>
                        </td>
                    </tr>
					<%if(countFc == 0) {%>
					<tr>
					  <td> 
					     <h:outputText value="#{msgs.search_config_error}" />
					  </td>
					</tr>
					<%}%><tr><td>&nbsp</td></tr>
					<tr><td><div id="messages" class="ajaxalert"> </div></td></tr>
                </table>
                <h:inputHidden id="enteredFieldValues" value="#{AssumeMatchHandler.enteredFieldValues}"/>
            </h:form>
			<h:panelGrid>
               <h:panelGroup rendered="#{AssumeMatchHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			   <h:panelGroup rendered="#{AssumeMatchHandler.requiredExists}">
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
  </div>
</body>
        <%
          String[][] lidMaskingArray = assumeMatchHandler.getAllSystemCodes();          
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
         //var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
         //document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
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
     
<%
   if ("DBassumematches".equalsIgnoreCase(from))   {
   Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
   //currentTime = new java.util.Date();
   String queryStr ="";
   Long currentTime = new java.util.Date().getTime();
   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String startDateField = simpleDateFormatFields.format(currentTime);
   queryStr = "create_end_date="+startDateField;

   SimpleDateFormat simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String startTimeField = simpletimeFormatFields.format(currentTime);
   queryStr += "&create_end_time="+startTimeField;
   long milliSecsInADay = 86400000L; //24 hours back
   Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
   Date dt24HrsBack = new Date(currentTime - milliSecsInADay);

   simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String endDateField = simpleDateFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_date="+endDateField;

   simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String  endTimeField = simpletimeFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_time="+endTimeField;
%>

    <script>
	   var last24hours = "";
	     populateContents('advancedformData','create_start_date','<%=endDateField%>');
	     populateContents('advancedformData','create_start_time','<%=startTimeField%>');
	     populateContents('advancedformData','create_end_date','<%=startDateField%>');
	     populateContents('advancedformData','create_end_time','<%=endTimeField%>');
 	     ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=<%=rand%>&<%=queryStr%>','outputdiv','')
     </script>
<% }  %>
<!-- Added  on 22-aug-2008 to incorparte with the functionality of back button in ameuiddetails.jsp  -->
    <%
    
    if(request.getParameter("back")!=null){%>
    <script>
         var queryStr = '<%=request.getQueryString()%>';
         setRand(Math.random());
		 ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');
   
   <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);
   %>
    populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
   <%}%>
   </script>
   <%}%>
</html>
</f:view>              
