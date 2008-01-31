<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>
<% Integer size = 0; %>

<f:view>
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    
    <html>
       <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
            <!-- Additional source files go here -->
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
            <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
            <!--there is no custom header content for this example-->
       </head>
        
        <body class="yui-skin-sam">
             <%@include file="./templates/header.jsp"%>
            <div id="mainContent">                 
            <div id="reports">
                <table border="0" cellspacing="0" cellpadding="0">
                    <% Operations operations=new Operations();%>
                    <tr> 
                        <!--td><%=(String)request.getAttribute("tabName")%></td-->
                        <td>
                            <div id="demo" class="yui-navset">                               
                                <ul class="yui-nav">
                                  <% if ("MERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))   {%>
                                    <% if(operations.isReports_MergedRecords()){%>
                                        <li class="selected"><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <%} else if ("DEACTIVATED_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>                                  
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li  class="selected"><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% } else if ("UNMERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li  class="selected"><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% } else if ("UPDATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li  class="selected"><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% } else if ("ACTIVITY_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li  class="selected"><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% } else if ("DUPLICATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li  class="selected"><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% } else if ("ASSUME_MATCH".equalsIgnoreCase((String)request.getAttribute("tabName"))) { %>
                                     <% if(operations.isReports_MergedRecords()){%>
                                        <li><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li  class="selected"><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <%} else {%>
                                    <% if(operations.isReports_MergedRecords()){%>
                                        <li  class="selected"><a href="#tab1"><em>${msgs.report_submenu1}</em></a></li>
                                    <%}%>
                                    <% if(operations.isReports_DeactivatedEUIDs()){%>
                                           <li><a href="#tab2"><em>${msgs.report_submenu2}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_UnmergedRecords()){%>
                                          <li><a href="#tab3"><em>${msgs.report_submenu3}</em></a></li>
                                    <%}%>    
                                    <% if(operations.isReports_Updates()){%>
                                         <li><a href="#tab4"><em>${msgs.report_submenu4}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_Activity()){%>   
                                         <li><a href="#tab5"><em>${msgs.report_submenu5}</em></a></li>
                                    <%}%>   
                                    <% if(operations.isReports_Duplicates()){%>  
                                         <li><a href="#tab6"><em>${msgs.report_submenu6}</em></a></li>
                                    <%}%>  
                                    <% if(operations.isReports_AssumedMatches()){%>     
                                         <li><a href="#tab7"><em>${msgs.report_submenu7}</em></a></li>
                                    <%}%>     
                                  <% }%>
                                  
                                </ul>            
                                
                           <div class="yui-content">
                                <% if(operations.isReports_MergedRecords()){%>   
                                    <div id="tab1">
                                       <div id ="mergedrecords" class="basicSearch">
                                        <h:form id="MergedReportsSearch">
                                            <table border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td valign="top">
                                                        <table border="0" cellpadding="0" cellspacing="0">
                                                            <tr>  
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                   <nobr> <h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'MergedReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A>
                                                                   </nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'MergedReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg1" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeToField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeToField"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>                                       
                                                            <tr>
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('MergedReportsSearch')">
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                    <!--a class="button" href="javascript:alert('Apologies, Service Layer Work in progress..');">&nbsp;<!--h:outputText value="Search"/>&nbsp;</a-->

                                                                    <h:commandLink  id="submitSearch" styleClass="button" rendered="#{Operations.reports_MergedRecords}"
                                                                                    action="#{ReportHandler.mergeReport}"> 
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                                                
                                                    <td valign="top">
                                                        <% if ("MERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                            <h:messages  styleClass="reportErrorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table> 
                                        </h:form>  
                                        </div>                                        
                                       <% if ("MERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))   {%>
                                       <br/>                                       
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>                                                           
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                        <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                         <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_MergedRecords && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>                                       
                                       <div class="reportYUISearch">
                                            <yui:datatable var="mergedRecords" value="#{ReportHandler.mergedRecordsVO}"
                                                           paginator="true" 
                                                           id="mergedRecords"
                                                           rowClasses="even,odd"
                                                           rows="50"                                     
                                                           width="1024px">                                                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="euidArray" value="#{mergedRecords.euid}">
                                                            <h:column>
                                                                <h:outputText value="#{euidArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="firstNameArray" value="#{mergedRecords.firstName}">
                                                            <h:column>
                                                                <h:outputText value="#{firstNameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="lastNameArray" value="#{mergedRecords.lastName}">
                                                            <h:column>
                                                                <h:outputText value="#{lastNameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="ssnArray" value="#{mergedRecords.ssn}">
                                                            <h:column>
                                                                <h:outputText value="#{ssnArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_DOB_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="dobArray" value="#{mergedRecords.dob}">
                                                            <h:column>
                                                                <h:outputText value="#{dobArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="addressLine1Array" value="#{mergedRecords.addressLine1}">
                                                            <h:column>
                                                                <h:outputText value="#{addressLine1Array}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                    
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_date_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{mergedRecords.mergedtime}" />
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_description_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{mergedRecords.description}" />
                                                </yui:column>
                                            </yui:datatable>
                                        </div>         
                                       <%}%>
                                    </div>
                                <%}%>
                                <% if(operations.isReports_DeactivatedEUIDs()){%>
                                    <div id="tab2">
                                        <div id ="deactivatedreport" class="basicSearch">
                                        <h:form id="DeactivatedReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'DeactivatedReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg2" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'DeactivatedReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg3" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>     
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <nobr><h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                    </nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>                                       
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('DeactivatedReportsSearch')">
                                                                    <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span></a>
                                                                    <h:commandLink  id="submitSearch" styleClass="button" rendered="#{Operations.reports_DeactivatedEUIDs}"
                                                                                    action="#{ReportHandler.deactivatedReport}">  
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("DEACTIVATED_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table> 
                                        </h:form>   
                                       </div>                                        
                                       <% if ("DEACTIVATED_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                       <br/>                                       
                                        <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                        </td>
                                                        <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                         </td>
                                                         <td>   
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_DeactivatedEUIDs && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                                        <div class="reportYUISearch">
                                            <yui:datatable var="deactivateRecords" value="#{ReportHandler.deactivatedRecordsVO}"
                                                           paginator="true" 
                                                           id="deactivateRecords"
                                                           rows="50" 
                                                           rendered="#{ReportHandler.resultsSize gt 0}"
                                                           rowClasses="even,odd"
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                       <h:outputText  value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.euid}" />
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.firstName}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.lastName}"/>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.ssn}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_DOB_text}"  />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.dob}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.addressLine1}" />
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_date_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.deactivatedDate}" />
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_description_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{deactivateRecords.description}" />
                                                </yui:column>
                                            </yui:datatable>
                                        </div>
                                       <%}%>                                        
                                  </div>
                                <%}%>  
                                <% if(operations.isReports_UnmergedRecords()){%> 
                                    <div id="tab3">
                                        <div id ="unmergeReport" class="basicSearch">
                                        <h:form id="UnmergedReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                   <nobr> <h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'UnmergedReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg4" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                                <td>
                                                                   <nobr> <h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                   <nobr> <h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'UnmergedReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg5" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>                                       
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('UnmergedReportsSearch')">
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                    <!--a class="button" href="javascript:alert('Apologies, Service Layer Work in progress..');">&nbsp;<!--h:outputText value="Search"/>&nbsp;</a-->

                                                                    <h:commandLink  id="submitSearch" styleClass="button" rendered="#{Operations.reports_UnmergedRecords}"
                                                                                    action="#{ReportHandler.unMergeReport}">  
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("UNMERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <% } %>
                                                    </td>
                                                </tr>
                                            </table> 
                                        </h:form>   
                                        </div>
                                       <% if ("UNMERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                       <br/>                                       
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td> 
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                        </td>
                                                        <td>   
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_UnmergedRecords && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                                        <div class="reportYUISearch">
                                           <yui:datatable var="unmergedRecords" value="#{ReportHandler.unmergedRecordsVO}"
                                                           paginator="true" 
                                                           id="unmergedRecords"
                                                           rows="50"
                                                           rowClasses="even,odd"                                                           
                                                           rendered="#{ReportHandler.resultsSize gt 0}"                                                           
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>                                                    
                                                     <div id="multicolumn">
                                                        <h:dataTable var="euidArray" value="#{unmergedRecords.euid}">
                                                            <h:column>
                                                                <h:outputText value="#{euidArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                    
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="firstNameArray" value="#{unmergedRecords.firstName}">
                                                            <h:column>
                                                                <h:outputText value="#{firstnameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                    
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="lastNameArray" value="#{unmergedRecords.lastName}">
                                                            <h:column>
                                                                <h:outputText value="#{lastNameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                   
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                      <div id="multicolumn">
                                                        <h:dataTable var="ssnArray" value="#{unmergedRecords.ssn}">
                                                            <h:column>
                                                                <h:outputText value="#{ssnArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                    
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_DOB_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="dobArray" value="#{unmergedRecords.dob}">
                                                            <h:column>
                                                                <h:outputText value="#{dobArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                  
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                     <div id="multicolumn">
                                                        <h:dataTable var="addline1Array" value="#{unmergedRecords.addressLine1}">
                                                            <h:column>
                                                                <h:outputText value="#{addline1Array}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                                                                   
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_date_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{unmergedRecords.unmergedDate}" />
                                                </yui:column>

                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_description_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{unmergedRecords.description}" />
                                                </yui:column>
                                            </yui:datatable>
                                        </div>
                                       <%}%>                                        
                                    </div>
                                <%}%>   
                                <% if(operations.isReports_Updates()){%>   
                                    <div id="tab4">
                                        <div id ="UpdateReportsSearch" class="basicSearch">
                                        <h:form id="UpdateReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                   <nobr> <h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'UpdateReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg6"
                                                                                        alt="calendar Image"
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>
                                                                    </A></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'UpdateReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg7" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>                                       
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                        <a class="button" href="javascript:ClearContents('UpdateReportsSearch')">
                                                                            <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                                                                        </a>
                                                                    <!--a class="button" href="javascript:alert('Apologies, Service Layer Work in progress..');">&nbsp;<!--h:outputText value="Search"/>&nbsp;</a-->
                                                                    <h:commandLink  id="submitSearch"  styleClass="button" rendered="#{Operations.reports_Updates}"
                                                                                    action="#{ReportHandler.updateReport}">  
                                                                                    <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("UPDATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table> 
                                       </h:form>
                                        </div>
                                      
                                       <% if ("UPDATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>                                       <br/>                                       
                                        <br>                                      
                                        <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>     
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                       </td>
                                                       <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_Updates && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                              <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                           </table>
                                        </div>
                                        <div class="reportYUISearch">
                                            <yui:datatable var="updateRecords" value="#{ReportHandler.updateRecordsVO}"
                                                           id="updateRecords"
                                                           paginator="true" 
                                                           rows="50"
                                                           rendered="#{ReportHandler.resultsSize gt 0}"
                                                           rowClasses="even,odd"                                                           
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                     <h:outputText  value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>                                                    
                                                    <div id="multicolumn">
                                                        <h:dataTable var="euidArray" value="#{updateRecords.euid}">
                                                            <h:column>
                                                                <h:outputText value="#{euidArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                    
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                      <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                      <div id="multicolumn">
                                                        <h:dataTable var="firstNameArray" value="#{updateRecords.firstName}">
                                                            <h:column>
                                                                <h:outputText value="#{firstNameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                                      
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                       <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                     <div id="multicolumn">
                                                        <h:dataTable var="lastNameArray" value="#{updateRecords.lastName}">
                                                            <h:column>
                                                                <h:outputText value="#{lastNameArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                         
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                      <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                     <div id="multicolumn">
                                                        <h:dataTable var="ssnArray" value="#{updateRecords.ssn}">
                                                            <h:column>
                                                                <h:outputText value="#{ssnArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>    
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                     <h:outputText value="#{msgs.datatable_DOB_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="dobArray" value="#{updateRecords.dob}">
                                                            <h:column>
                                                                <h:outputText value="#{dobArray}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                     
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                       <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                     <div id="multicolumn">
                                                        <h:dataTable var="addLine1Array" value="#{updateRecords.addressLine1}">
                                                            <h:column>
                                                                <h:outputText value="#{addLine1Array}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>                                                     
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                      <h:outputText value="#{msgs.datatable_date_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{updateRecords.updateDate}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                       <h:outputText value="#{msgs.datatable_time_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{updateRecords.updateTime}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                      <h:outputText value="#{msgs.datatable_description_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{updateRecords.description}" />
                                                </yui:column>
                                            </yui:datatable>
                                        </div>
                                       <%}%>                                                                                
                                    </div>
                                <%}%>     
                                <% if(operations.isReports_Activity()){%> 
                                    <div id="tab5">
                                        <div id ="activities" class="basicSearch">
                                        <h:form id="ActivityReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <nobr> <h:outputLabel for="viewreports" value="#{msgs.activity_rep_freq}"/></nobr>
                                                        <div class="selectContent">
                                                        <h:selectOneRadio style="selectContent" id="viewreports" value="#{ReportHandler.frequency}" >
                                                            <f:selectItem itemValue="Weekly" itemLabel="Weekly"/>
                                                            <f:selectItem itemValue="Monthly" itemLabel="Monthly"/>
                                                            <f:selectItem itemValue="Yearly" itemLabel="Yearly"/>
                                                        </h:selectOneRadio>
                                                        </div>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                   <nobr> <h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'ActivityReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg8" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A>
                                                                    </nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'ActivityReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg9" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <nobr><h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <nobr><h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/></nobr>
                                                                </td>
                                                            </tr>                                       
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('ActivityReportsSearch')">
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                    <h:commandLink  id="submitSearch"  styleClass="button" rendered="#{Operations.reports_Activity}"
                                                                                    action="#{ReportHandler.activitiesReport}">  
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("ACTIVITY_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table> 
                                       </h:form> 
                                       </div>
                                       <% if ("ACTIVITY_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                        <% if (request.getAttribute("frequency") != null && "Weekly".equalsIgnoreCase((String)request.getAttribute("frequency")) ){ %> 
                                          <div class="reportYUISearch">
                                              <yui:datatable var="activityRecords" value="#{ReportHandler.activityRecordsVO}"
                                                           id="weeklyactivity"
                                                           paginator="true" 
                                                           rendered="#{ReportHandler.resultsSize gt 0}"
                                                           rowClasses="even,odd"
                                                           rows="50"
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_day_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.day}" />
                                                </yui:column>
                                                
                                                 <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.reports_data_table_date_column}" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.activityDate}" />
                                                </yui:column>
                                               
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_add_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.addTransactions}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_euiddeactivate_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.euidDeactivateTrans}" />
                                                </yui:column>
                                                  <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_euidmerge_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.euidMergedTrans}" />
                                                </yui:column>
                                                
                                                 <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_euidunmerge_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.euidUnmergedTrans}" />
                                                </yui:column>
                                                
                                                 <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_lidmerge_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.lidMergedTrans}" />
                                                </yui:column>
                                               
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_lidunmerge_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.lidUnMergedTrans}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_lidtransfer_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.lidTransfer}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.activity_rep_update_text }" />
                                                    </f:facet>
                                                    <h:outputText value="#{activityRecords.updateCount}" />
                                                </yui:column>
                                                
                                              </yui:datatable>
                                          </div>  
                                        <%} else {%>
                                           <div class="reportYUISearch">                                        
                                               <yui:datatable var="activityRecords" value="#{ReportHandler.activityRecordsVO}"
                                                       id="monthlyactivity"
                                                       paginator="true"
                                                       rendered="#{ReportHandler.resultsSize gt 0}"
                                                       rows="50"                                     
                                                       rowClasses="even,odd"
                                                       width="1024px">                                                                            
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                       <h:outputText value="#{msgs.activity_rep_add_text}" />
                                                     </f:facet>
                                                     <h:outputText value="#{activityRecords.addTransactions}" />
                                                   </yui:column>
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                       <h:outputText value="#{msgs.activity_rep_euiddeactivate_text}" />
                                                     </f:facet>
                                                       <h:outputText value="#{activityRecords.euidDeactivateTrans}"/>
                                                   </yui:column>
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_euidmerge_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.euidMergedTrans}" />
                                                   </yui:column>
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_euidunmerge_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.euidUnmergedTrans}"/>
                                                   </yui:column>
                                            
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_lidmerge_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.lidMergedTrans}" />
                                                   </yui:column>
                                            
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_lidunmerge_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.lidUnMergedTrans}" />
                                                   </yui:column>
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_unresolvedpd_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.unresolvedPotentialDup}" />
                                                   </yui:column>
                                            
                                                   <yui:column sortable="true" resizeable="true">
                                                     <f:facet name="header">
                                                      <h:outputText value="#{msgs.activity_rep_resolvedpd_text }" />
                                                     </f:facet>
                                                      <h:outputText value="#{activityRecords.unresolvedPotentialDup}" />
                                                   </yui:column>
                                            
                                            </yui:datatable>                                        
                                           </div>  
                                        <% } %>
                                    <%}%>                                         
                                    </div>
                                <%}%>  
                                <% if(operations.isReports_Duplicates())  {%>   
                                    <div id="tab6">
                                        <div id ="duplicatereport" class="basicSearch">
                                        <h:form id="DuplicateReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr>
                                                                      <h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'DuplicateReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg10" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A>
                                                                    </nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'DuplicateReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg11" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A>
                                                                    </nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                        
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>    
                                                            <tr>
                                                                <td><nobr><h:outputLabel for="function" value="#{msgs.transaction_function}" /></nobr></td>
                                                                <td>
                                                                    <h:selectOneMenu   label="#{DuplicateReportHandler.source}" value="#{DuplicateReportHandler.source}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{TransactionHandler.selectOptions}" />
                                                                    </h:selectOneMenu>
                                                                  
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>    
                                                                    <nobr><h:outputLabel for="reportSize" value="#{msgs.reports_reportsize_text}" /></nobr>
                                                                </td>
                                                                <td>
                                                                    <h:inputText id="reportSize" size= "6" maxlength="6" value="#{ReportHandler.reportSize}"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('DuplicateReportsSearch')">
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button1}"/></span></a>
                                                                    <h:commandLink  id="submitSearch"  styleClass="button" rendered="#{Operations.reports_Duplicates}"
                                                                                    action="#{ReportHandler.duplicateReport}">  
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("DUPLICATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table>
                                        </h:form>
                                        </div>
                                       <% if ("DUPLICATE_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))    { %>
                                        <br/>
                                        <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                               <h:form>
                                                   <tr>
                                                       <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                        </td>
                                                        <td>   
                                                            <h:outputLink styleClass="button" rendered="#{Operations.reports_Duplicates && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                              <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                               </h:form>
                                           </table>
                                        </div>
                                        <div class="reportYUISearch">
                                            <yui:datatable var="duplicateRecords" value="#{ReportHandler.duplicateRecordsVO}"
                                                           id="dupRecords"
                                                           paginator="true" 
                                                           rendered="#{ReportHandler.resultsSize gt 0}"
                                                           rowClasses="even,odd"
                                                           rows="50"                                     
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                       <h:outputText value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.euid}" />
                                                </yui:column>
                                                
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.firstName}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.lastName}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.ssn}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_DOB_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.dob}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{duplicateRecords.addressLine1}" />
                                                </yui:column>
                                            </yui:datatable>
                                        </div>  
                                       <%}%>                                                                                                                        
                                    </div>
                                <%}%>     
                                <% if(operations.isReports_AssumedMatches())   {   %> 
                                    <div id="tab7"> 
                                        <div id ="assumedmatchreport" class="basicSearch">
                                        <h:form id="AssumeReportsSearch">
                                            <table border="0">
                                                <tr>
                                                    <td>
                                                        <table border="0">
                                                            <tr>  
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStDateField"   value="#{msgs.source_edit_fromDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createStDateField"
                                                                        value="#{ReportHandler.createStartDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'AssumeReportsSearch:createStDateField')">
                                                                        <h:graphicImage id="calImg12" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndDateField"   value="#{msgs.source_edit_toDatePrompt}"/>:</nobr>
                                                                </td>
                                                                <td align="left">    
                                                                    <nobr><h:inputText 
                                                                        id="createEndDateField"
                                                                        value="#{ReportHandler.createEndDate}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="10"/>
                                                                    <A HREF="javascript:void(0);"
                                                                       onclick="g_Calendar.show(event, 'AssumeReportsSearch:createEndDateField')">
                                                                        <h:graphicImage id="calImg13" 
                                                                                        alt="calendar Image"  
                                                                                        url="./images/cal.gif"
                                                                                        styleClass="imgClass"/>               
                                                                    </A></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr> 
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeField"   value="#{msgs.source_edit_fromTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <nobr><h:inputText 
                                                                        id="createStartTimeField"
                                                                        value="#{ReportHandler.createStartTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createEndTimeField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <nobr><h:inputText 
                                                                        id="createEndTimeField"
                                                                        value="#{ReportHandler.createEndTime}"
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        size="12"
                                                                        maxlength="8"/></nobr>
                                                                </td>
                                                            </tr>    
                                                            <tr>
                                                                <td>    
                                                                    <nobr><h:outputLabel for="reportSize" value="#{msgs.reports_reportsize_text}"/></nobr>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:inputText id="reportSize"  maxlength="6" size= "6" value="#{ReportHandler.reportSize}"/></nobr>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                
                                                                <td colspan="4" align="center">                                    
                                                                    <a class="button" href="javascript:ClearContents('AssumeReportsSearch')"> 
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span></a>
                                                                    <h:commandLink  id="submitSearch"  styleClass="button" rendered="#{Operations.reports_AssumedMatches}"
                                                                                    action="#{ReportHandler.assumeMatchReport}">  
                                                                        <span><h:outputText  value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>                                            
                                                            </tr>
                                                        </table>
                                                    </td>
                                                    <td>
                                                        <% if ("ASSUME_MATCH".equalsIgnoreCase((String)request.getAttribute("tabName")))      {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table>
                                        </h:form>
                                        </div>
                                       <% if ("ASSUME_MATCH".equalsIgnoreCase((String)request.getAttribute("tabName")))      { %>
                                       <br/>                                       
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <h:outputText rendered="#{ReportHandler.resultsSize gt -1}" value="#{ReportHandler.resultsSize}"/>&nbsp;&nbsp;
                                                       </td>
                                                       <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_AssumedMatches && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                              <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                                        <div class="reportYUISearch">
                                             <yui:datatable var="assumeMatchesRecords" value="#{ReportHandler.assumematchesRecordsVO}"
                                                           paginator="true" 
                                                           rendered="#{ReportHandler.resultsSize gt 0}"
                                                           id="assumeMatch"
                                                           rowClasses="even,odd"
                                                           rows="50"                                     
                                                           width="1024px">                                                                            
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_euidno_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{assumeMatchesRecords.euid}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_localid_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{assumeMatchesRecords.localId}" />
                                                </yui:column>
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText  value="#{msgs.potential_dup_table_system_column}" />
                                                    </f:facet>
                                                    <h:outputText value="#{assumeMatchesRecords.systemCode}" />
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_firstname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="fname" value="#{assumeMatchesRecords.firstName}">
                                                            <h:column>
                                                                <h:outputText value="#{fname}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_lastname_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="lname" value="#{assumeMatchesRecords.lastName}">
                                                            <h:column>
                                                                <h:outputText value="#{lname}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_ssn_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="social" value="#{assumeMatchesRecords.ssn}">
                                                            <h:column>
                                                                <h:outputText value="#{social}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_DOB_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="birth" value="#{assumeMatchesRecords.dob}">
                                                            <h:column>
                                                                <h:outputText value="#{birth}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_addressline1_text}" />
                                                    </f:facet>
                                                    <div id="multicolumn">
                                                        <h:dataTable var="line1" value="#{assumeMatchesRecords.addressLine1}">
                                                            <h:column>
                                                                <h:outputText value="#{line1}" />
                                                            </h:column>
                                                        </h:dataTable>
                                                    </div>
                                                </yui:column>
                                                <yui:column sortable="true" resizeable="true">
                                                    <f:facet name="header">
                                                        <h:outputText value="#{msgs.datatable_weight_text}" />
                                                    </f:facet>
                                                    <h:outputText value="#{assumeMatchesRecords.weight}" />
                                                </yui:column>
                                               
                                        </yui:datatable>                             
                                        </div>  
                                       <%}%>                                        
                                    </div>
                                <%}%>    
                            </div> <!-- End YUI content -->
                            </div> <!-- demo end -->
                        </td>
                    </tr>
                </table>
            </div>        
            
            <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
            
            <script>
                (function() {
                var tabView = new YAHOO.widget.TabView('demo');                
                YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
                })();
            </script>
            
            <!--END SOURCE CODE FOR EXAMPLE =============================== -->
            </div>        <!-- End mainContent -->                
        </body>
    </html>
</f:view>