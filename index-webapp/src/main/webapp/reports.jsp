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
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DuplicateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DeactivatedReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.MergeRecordHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UnmergedRecordsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UpdateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchReportHandler"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.HashMap"  %>

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
        
        <body class="yui-skin-sam">
             <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden;">                 
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                                <td>
                                                                    <nobr><h:outputLabel for="createStartTimeToField"   value="#{msgs.source_edit_toTimePrompt}"/>:</nobr>
                                                                </td>
                                                                <td>    
                                                                    <h:inputText 
                                                                        id="createStartTimeToField"
                                                                        value="#{ReportHandler.createEndTime}" 
                                                                        onkeydown="javascript:qws_field_on_key_down(this, 'DD:DD:DD')" 
                                                                        onkeyup="javascript:qws_field_on_key_up(this)"
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                                            <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                            </table> 
                                        </h:form>  
                                        </div>                                        
                                       <% if ("MERGED_RECORDS".equalsIgnoreCase((String)request.getAttribute("tabName")))   {%>
                                      <%
                                        MergeRecordHandler mergeRecordHandler=new MergeRecordHandler(); 
                                        ArrayList fcArrayList  = mergeRecordHandler.getResultsConfigArrayList();
                                          ArrayList mergeResultArrayList = new ArrayList();
                                          if(request.getAttribute("mergeReportList") != null) {
                                             request.setAttribute("mergeReportList", request.getAttribute("mergeReportList") );  
                                             mergeResultArrayList = (ArrayList) request.getAttribute("mergeReportList"); 
                                              }
                                     %>
                                    <br/>
                                   <%
                                         if (request.getAttribute("mergeReportList") != null) {
                                    %>
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=mergeResultArrayList.size()/2%>&nbsp;&nbsp;
                                                        </td>
                                                        <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_MergedRecords && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div> 
                                          <%}%>                                        
                                        
   
   <%
         if (mergeResultArrayList != null && mergeResultArrayList.size() > 0) {
    %>
         <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
           value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
           myColumnDefs.append(value);
           if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (mergeResultArrayList != null && mergeResultArrayList.size() > 0) {
                for (int i = 0; i < mergeResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) mergeResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != mergeResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                         <%
                                        DeactivatedReportHandler deactivateReportHandler=new DeactivatedReportHandler(); 
                                        ArrayList fcArrayList  = deactivateReportHandler.getResultsConfigArrayList();
                                         ArrayList deactivateResultArrayList = new ArrayList();
                                          if(request.getAttribute("deactivatedReportList") != null) {
                                             request.setAttribute("deactivatedReportList", request.getAttribute("deactivatedReportList") );  
                                             deactivateResultArrayList = (ArrayList) request.getAttribute("deactivatedReportList"); 
                                             
                                             }
                                     %>
                                        <br/>
                                   <%
                                         if (request.getAttribute("deactivatedReportList") != null) {
                                    %>
                                           <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=deactivateResultArrayList.size()%>&nbsp;&nbsp;
                                                        </td>
                                                         <td>   
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_DeactivatedEUIDs && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                                          <%}%>                                        
                                        
   
   <%
         if (deactivateResultArrayList != null && deactivateResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var deactivatefieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList deacivatekeysList  = new ArrayList();
        ArrayList deactivatelabelsList  = new ArrayList();
        ArrayList deactivatefullFieldNamesList  = new ArrayList();
        
        deacivatekeysList.add("EUID");
        deactivatelabelsList.add("EUID");
        deactivatefullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            deacivatekeysList.add(fieldConfig.getName());
            deactivatelabelsList.add(fieldConfig.getDisplayName());
            deactivatefullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[deacivatekeysList.size()];
        String[] labels = new String[deactivatelabelsList.size()];
        String[] fullFieldNames = new String[deactivatefullFieldNamesList.size()];
        
        for(int i=0;i<deacivatekeysList.size();i++) {
            keys[i] = (String) deacivatekeysList.get(i);
            labels[i] = (String) deactivatelabelsList.get(i);
            fullFieldNames[i] = (String) deactivatefullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<deacivatekeysList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (deactivateResultArrayList != null && deactivateResultArrayList.size() > 0) {
                for (int i = 0; i < deactivateResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) deactivateResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != deactivateResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<deacivatekeysList.size();i++) {
        %> 
        <script>
            deactivatefieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var deactivatedataArray = new Array();
     deactivatedataArray  = <%=sbr.toString()%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: deactivatedataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: deactivatefieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : deactivatedataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
                                        
                                        
                                        
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                        <%
        UnmergedRecordsHandler unmergeReportHandler = new UnmergedRecordsHandler();
        ArrayList fcArrayList  = unmergeReportHandler.getResultsConfigArrayList();
         ArrayList unmergeResultArrayList = new ArrayList();
          if(request.getAttribute("unmergeReportList") != null) {
             request.setAttribute("unmergeReportList", request.getAttribute("unmergeReportList") );  
             unmergeResultArrayList = (ArrayList) request.getAttribute("unmergeReportList"); 
         }
   %>
                                        <br/>
   <%
         if (request.getAttribute("unmergeReportList") != null) {
    %>
                                  
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=unmergeResultArrayList.size()/2%>&nbsp;&nbsp;
                                                        </td>
                                                        <td>   
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_UnmergedRecords && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                               <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>                                                           
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                           <%}%>                                        
                                        
   
   <%
         if (unmergeResultArrayList != null && unmergeResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (unmergeResultArrayList != null && unmergeResultArrayList.size() > 0) {
                for (int i = 0; i < unmergeResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) unmergeResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != unmergeResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
                                        
                                        
                                        
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                     <%
                                        UpdateReportHandler updateReportHandler=new UpdateReportHandler(); 
                                        ArrayList fcArrayList  = updateReportHandler.getResultsConfigArrayList();
                                          ArrayList updateResultArrayList = new ArrayList();
                                          if(request.getAttribute("updateReportList") != null) {
                                             request.setAttribute("updateReportList", request.getAttribute("updateReportList") );  
                                             updateResultArrayList = (ArrayList) request.getAttribute("updateReportList"); 
                                         }
                                     %>
                                    <br/>
                                   <%
                                         if (request.getAttribute("updateReportList") != null) {
                                    %>
                                       <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=updateResultArrayList.size()%>&nbsp;&nbsp;
                                                        </td>
                                                       <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_Updates && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                              <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                           </table>
                                        </div>
                                   <%}%>                                        
                                        
   
   <%
         if (updateResultArrayList != null && updateResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (updateResultArrayList != null && updateResultArrayList.size() > 0) {
                for (int i = 0; i < updateResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) updateResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != updateResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
                                        
                                        
                                        
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
                                                        <h:selectOneRadio styleClass="selectContent" id="viewreports" value="#{ReportHandler.frequency}" >
                                                            <f:selectItems value="#{ReportHandler.activityReportTypes}"/>
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                       <% if ("ACTIVITY_REPORT".equalsIgnoreCase((String)request.getAttribute("tabName")))      {

       
        ArrayList keyList = new ArrayList();
            if (request.getAttribute("frequency") != null && "Weekly Activity".equalsIgnoreCase((String)request.getAttribute("frequency")) ){ 
              keyList.add("ActivityDate");
            }
            keyList.add("Add");
            keyList.add("EUIDDeactivate");
            keyList.add("EUIDMerge");
            keyList.add("EUIDUnmerge");
            keyList.add("LIDMerge");
            keyList.add("LIDUnMerge");
            keyList.add("UnresolvedDuplicate");
            keyList.add("ResolvedDuplicate");
            
            ArrayList labelList = new ArrayList();
            if (request.getAttribute("frequency") != null && "Weekly Activity".equalsIgnoreCase((String)request.getAttribute("frequency")) ){ 
              labelList.add("Activity Date");
            }
            labelList.add("Add");
            labelList.add("EUID Deactivate");
            labelList.add("EUID Merge");
            labelList.add("EUID Unmerge");
            labelList.add("LID Merge");
            labelList.add("LID UnMerge");
            labelList.add("Unresolved Duplicate");
            labelList.add("Resolved Duplicate");
        
        //set EUID values here
        String[] keys = new String[keyList.size()];
        String[] labels = new String[labelList.size()];
        
        for(int i=0;i<keyList.size();i++) {
            keys[i] = (String) keyList.get(i);
            labels[i] = (String) labelList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keyList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        ArrayList activityResultArrayList  = (ArrayList) request.getAttribute("activityOutputList");
        if (activityResultArrayList != null && activityResultArrayList.size() > 0) {
                for (int i = 0; i < activityResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) activityResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < keys.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(keys[kc]) != null)?valueMap.get(keys[kc]):"0") + "\"");
                        if (kc != keys.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != activityResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]"); 
%>




        <script>
            var fieldsArray = new Array();
        </script>





   <%
         if (activityResultArrayList  != null && activityResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      

     <%
        for(int i=0;i<keys.length;i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;
</script>

<script>

    YAHOO.example.Data = {
    outputActivityValues: dataArray
    }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputActivityValues);

        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;

        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : 10, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource);
            
    };
});
</script>
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
                                                                        size="12"
                                                                        maxlength="8"/>
                                                                </td>
                                                            </tr>    
                                                            <tr>
                                                                <td><nobr><h:outputLabel for="function" value="#{msgs.potential_dup_table_system_column}" /></nobr></td>
                                                                <td>
                                                                    <h:selectOneMenu   label="#{ReportHandler.reportFunction}" value="#{ReportHandler.reportFunction}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{ReportHandler.selectOptions}" />
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
    <%
        DuplicateReportHandler duplicateReportHandler = new DuplicateReportHandler();
        ArrayList fcArrayList  = duplicateReportHandler.getResultsConfigArrayList();
         ArrayList duplicateResultArrayList = new ArrayList();
          if(request.getAttribute("duplicateReportList") != null) {
             request.setAttribute("duplicateReportList", request.getAttribute("duplicateReportList") );  
             duplicateResultArrayList = (ArrayList) request.getAttribute("duplicateReportList"); 
         }
   %>
                                        <br/>
   <%
         if (request.getAttribute("duplicateReportList") != null) {
    %>
                                        <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                               <h:form>
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=duplicateResultArrayList.size()%>&nbsp;&nbsp;
                                                        </td>
                                                        <td>   
                                                            <h:outputLink styleClass="button" rendered="#{Operations.reports_Duplicates }" value="javaScript:window.print();">
                                                              <span><h:outputText  value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                               </h:form>
                                           </table>
                                        </div>
     <%}%>                                        
                                        
   
   <%
         if (duplicateResultArrayList != null && duplicateResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");




        StringBuffer sbr  = new StringBuffer();
        if (duplicateResultArrayList != null && duplicateResultArrayList.size() > 0) {
                sbr.append("[");
                for (int j = 0; j < duplicateResultArrayList.size(); j++) {
                 ArrayList valueMapArrayList = (ArrayList) duplicateResultArrayList.get(j);
                //sbr.append("[");
                for (int i = 0; i < valueMapArrayList.size(); i++) {
                        StringBuffer valueBuffer = new StringBuffer();
                        HashMap valueMap = (HashMap) valueMapArrayList.get(i);
                        valueBuffer.append("{");
                        for (int kc = 0; kc < fullFieldNames.length; kc++) {
                            valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null) ? valueMap.get(fullFieldNames[kc]) : "") + "\"");
                            if (kc != fullFieldNames.length - 1) {
                                valueBuffer.append(",");
                            }
                        }
                        valueBuffer.append("}");
                        sbr.append(valueBuffer.toString() +",");
                }
				//sbr.append("],");

                }
                sbr.append("]");

            }
 
        String finalStringOutput  = sbr.toString();
        finalStringOutput  = finalStringOutput.replaceAll(",]", "]");

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=finalStringOutput%>;

</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
                                        
                                        
                                        
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_date(this,'MM/dd/yyyy')"
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
                                                                        onblur="javascript:validate_time(this,'createStartTimeField')"
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
                                                                        onblur="javascript:validate_time(this,'createEndTimeField')"
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
                                        <%
        AssumeMatchReportHandler assumeMatchReportHandler = new AssumeMatchReportHandler();
        ArrayList fcArrayList  = assumeMatchReportHandler.getResultsConfigArrayList();
         ArrayList assumeMatchResultArrayList = new ArrayList();
          if(request.getAttribute("assumeMatchReportList") != null) {
             request.setAttribute("assumeMatchReportList", request.getAttribute("assumeMatchReportList") );  
             assumeMatchResultArrayList = (ArrayList) request.getAttribute("assumeMatchReportList"); 
         }
   %>
                                        <br/>
   <%
         if (assumeMatchResultArrayList != null) {
    %>
                                        <div class="printClass">
                                           <table cellpadding="0" cellspacing="0" border="0">
                                                   <tr>
                                                       <td>
                                                           <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
                                                       </td>
                                                       <td>    
                                                           <%=assumeMatchResultArrayList.size()%>&nbsp;&nbsp;
                                                        </td>
                                                       <td>
                                                           <h:outputLink styleClass="button" rendered="#{Operations.reports_AssumedMatches && ReportHandler.resultsSize gt 0}" value="javaScript:window.print();">
                                                              <span><h:outputText rendered="#{ReportHandler.resultsSize gt 0}" value="#{msgs.print_text}"/>  </span>
                                                           </h:outputLink>
                                                       </td>
                                                   </tr>
                                           </table>
                                       </div>
                           <%}%>                                        
                                        
   
   <%
         if (assumeMatchResultArrayList != null && assumeMatchResultArrayList.size() > 0) {
    %>

              <div class="reportYUISearch" >
                <div id="outputdiv"></div>
             </div>  
      <%}%>
      
        <script>
            var fieldsArray = new Array();
        </script>
         <%
            
        //SearchResultsConfig searchResultsConfig = (SearchResultsConfig) screenObject.getSearchResultsConfig().toArray()[0];

        int pageSize = 10;
        
        
        ArrayList keysList  = new ArrayList();
        ArrayList labelsList  = new ArrayList();
        ArrayList fullFieldNamesList  = new ArrayList();
        /*
        keysList.add("EUID");
        labelsList.add("EUID");
        fullFieldNamesList.add("EUID");
        keysList.add("SystemCode");
        labelsList.add("SystemCode");
        fullFieldNamesList.add("SystemCode");
        keysList.add("LID");
        labelsList.add("LID");
        fullFieldNamesList.add("LID");
        keysList.add("Weight");
        labelsList.add("Weight");
        fullFieldNamesList.add("Weight");
        */        
        
        for(int i=0;i<fcArrayList.size();i++) {
            FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(i);
            keysList.add(fieldConfig.getName());
            labelsList.add(fieldConfig.getDisplayName());
            fullFieldNamesList.add(fieldConfig.getFullFieldName());
        }
        
        //set EUID values here
        String[] keys = new String[keysList.size()];
        String[] labels = new String[labelsList.size()];
        String[] fullFieldNames = new String[fullFieldNamesList.size()];
        
        for(int i=0;i<keysList.size();i++) {
            keys[i] = (String) keysList.get(i);
            labels[i] = (String) labelsList.get(i);
            fullFieldNames[i] = (String) fullFieldNamesList.get(i);
        }
        
        
        
        StringBuffer myColumnDefs = new StringBuffer();

        myColumnDefs.append("[");
        String value = new String();
        for(int i=0;i<keysList.size();i++) {
          value = "{key:" + "\"" + keys[i]+  "\"" + ", label: " + "\"" + labels[i]+"\"" +  ",sortable:true,resizeable:true}";
          myColumnDefs.append(value);
          if(i != keys.length -1) myColumnDefs.append(",");
         }   
         myColumnDefs.append("]");

         
        StringBuffer sbr  = new StringBuffer();
        sbr.append("[");
        if (assumeMatchResultArrayList != null && assumeMatchResultArrayList.size() > 0) {
                for (int i = 0; i < assumeMatchResultArrayList.size(); i++) {
                    HashMap valueMap = (HashMap) assumeMatchResultArrayList.get(i);
                    StringBuffer valueBuffer = new StringBuffer();
                    valueBuffer.append("{");  
                    for (int kc = 0; kc < fullFieldNames.length; kc++) {
                        valueBuffer.append(keys[kc] + ":" + "\"" + ((valueMap.get(fullFieldNames[kc]) != null)?valueMap.get(fullFieldNames[kc]):"") + "\"");
                        if (kc != fullFieldNames.length - 1) {
                            valueBuffer.append(",");
                        }
                    }
                    valueBuffer.append("}");                   

                    sbr.append(valueBuffer.toString());

                    if (i != assumeMatchResultArrayList.size() - 1) {
                        sbr.append(",");
                    }

                }
            }
        sbr.append("]");           

        for(int i=0;i<keysList.size();i++) {
        %> 
        <script>
            fieldsArray[<%=i%>] = '<%=keys[i]%>';
        </script>
        <%}%>
        

<script>
     var dataArray = new Array();
     dataArray  = <%=sbr.toString()%>;

	</script>

<script>

    YAHOO.example.Data = {
    outputValues: dataArray
 }
</script>

<script type="text/javascript">
YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.CustomSort = new function() {
        var myColumnDefs = <%=myColumnDefs.toString()%>;
        this.myDataSource = new YAHOO.util.DataSource(YAHOO.example.Data.outputValues);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
        this.myDataSource.responseSchema = {
            fields: fieldsArray
        };


var myConfigs = {
    paginator : new YAHOO.widget.Paginator({
        rowsPerPage    : <%=pageSize%>, // REQUIRED
        totalRecords   : dataArray.length // OPTIONAL

        // use an existing container element
        //containers : 'sort',

        // use a custom layout for pagination controls
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page",

        // show all links
        //pageLinks : YAHOO.widget.Paginator.VALUE_UNLIMITED,

        // use these in the rows-per-page dropdown
        //rowsPerPageOptions : [25,50,100],

        // use custom page link labels
        //pageLabelBuilder : function (page,paginator) {
          //  var recs = paginator.getPageRecords(page);
           //return (recs[0] + 1) + ' - ' + (recs[1] + 1);
        //}
    })
     

};
        this.myDataTable = new YAHOO.widget.DataTable("outputdiv", myColumnDefs,
                this.myDataSource, myConfigs);
            
    };
});
</script>
                                        
                                        
                                        
                                        
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
