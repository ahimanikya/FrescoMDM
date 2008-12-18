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
<%@ page import="com.sun.mdm.index.master.search.audit.AuditDataObject"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditSearchObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AuditLogHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.common.PullDownListItem"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>

<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Operations operations=new Operations();
%>

<f:view>
    
    
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
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>  
        <%@include file="./templates/header.jsp"%>
		<%
   HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   AuditLogHandler  auditLogHandler = (facesSession.getAttribute("AuditLogHandler") != null ) ? (AuditLogHandler) facesSession.getAttribute("AuditLogHandler") : new AuditLogHandler();
   SourceHandler sourceHandler= new SourceHandler();
		
		%>
    <body class="yui-skin-sam">
	  <table width="100%">
	    <tr>
		  <td>
             <div id="mainContent" style="width:100%">     
              <div id ="auditlog " class="duplicaterecords">
                        <table border="0" cellpadding="" cellspacing="0">
                      <h:form id="searchTypeForm" >
						<%if(auditLogHandler.getPossilbeSearchTypesCount() > 1) { %>
                            <tr>
                                <td>
                                    <h:outputText rendered="#{AuditLogHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu  id="searchType" title="#{msgs.search_Type}"
                                                      rendered="#{AuditLogHandler.possilbeSearchTypesCount gt 1}" 
                                                      onchange="submit();" 
													   style="width:220px;" 
                                                      value="#{AuditLogHandler.searchType}" 
                                                      valueChangeListener="#{AuditLogHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AuditLogHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                        </table>
						<%}%>
                    </h:form>
                    <h:form id="advancedformData">
                        <h:inputHidden id="selectedSearchType" value="#{AuditLogHandler.selectedSearchType}"/>
                        <table border="0" cellpadding="4" cellspacing="2" >
		       	           <tr>
			    			     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{AuditLogHandler.instructionLine}" /></td>
				           </tr>
						   <tr>
                                <td>
                                    <input id='lidmask' title='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />
							<div id="SearchCriteria">
							<table width="100%" cellpadding="2" cellspacing="2">
							<%
							  for(int i = 0 ; i < auditLogHandler.getSearchScreenFieldGroupArray().size(); i++) {
                                 FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) auditLogHandler.getSearchScreenFieldGroupArray().get(i);

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
								 ArrayList fieldGroupArrayList  = (ArrayList)auditLogHandler.getSearchScreenHashMap().get(basicSearchFieldGroup.getDescription());
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
                                   <table>
                                        <tr>
                                            <td>
                                                <nobr>
												<% if(operations.isAuditLog_SearchView()){%>	
                                                        <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" href="javascript:void(0)" onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/auditlogservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
                                                            <span>
                                                                <h:outputText value="#{msgs.search_button_label}"/>
                                                            </span>
                                                        </a>                                     
												<% } %>
                                                </nobr>
                                                <nobr>
                                                    <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:document.getElementById('messages').innerHTML='';ClearContents('advancedformData')">
                                                        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                    </h:outputLink>
                                                </nobr>                                                
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
							<tr><td><div id="messages" class="ajaxalert"></div></td><tr>
                        </table>
                        <h:inputHidden id="enteredFieldValues" value="#{AuditLogHandler.enteredFieldValues}"/>
                    </h:form>
					<!--div class="reportresults" id="outputdiv"></div-->
			<h:panelGrid>
               <h:panelGroup rendered="#{AuditLogHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			   <h:panelGroup rendered="#{AuditLogHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			</h:panelGrid>
<div class="reportresults" style="visibility:hidden"" id="outputdiv"></div>

                </div> 
            </div>  
		  </td>
		  </tr>
		  </table>
   </body>     
        <%
           String[][] lidMaskingArray = auditLogHandler.getAllSystemCodes();
          
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
    </script>
    </html>
</f:view>













