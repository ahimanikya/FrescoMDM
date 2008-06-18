<%-- 
    Document   : reportservices
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Sridhar Narsingh
	This is an Ajax service never use this JSP directly
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ArrayList"  %>
<f:view>
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/printservices","");
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

<%ScreenObject  screenObject = (ScreenObject) session.getAttribute("ScreenObject");
String reportName = request.getParameter("tabName");
%>
<html>
<head>
 <title> <%=reportName%> </title>
 <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen, print">
</head>
<body>

<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
	


<%
ReportHandler reportHandler = new ReportHandler();

String divId = request.getParameter("layer");

Enumeration parameterNames = request.getParameterNames();

//Report Type
reportHandler.setReportType(reportName);

//Map to hold the validation Errors
HashMap valiadtions = new HashMap();

EDMValidation edmValidation = new EDMValidation();         

//Column Definitions for YUI Datatable
StringBuffer myColumnDefs = new StringBuffer();

//Labels for YUI Datatable
ArrayList labelsList  = new ArrayList();

//Keys for YUI Datatable
ArrayList keys = new ArrayList();

//List to hold the results
ArrayList results = new ArrayList();

ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
String mergeText = bundle.getString("Merged_Transaction_Report_Label");
String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
String updateText = bundle.getString("Updated_Record_Report_Label");
String activityText = bundle.getString("Activity_Report_Label");
String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
String assumedText = bundle.getString("Assumed_Matches_Report_Label");
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

int pageSize = 10;
ArrayList fullFieldNamesList  = new ArrayList();
ArrayList fcArrayList  = new ArrayList();

%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>



<table cellspacing="0" cellpadding="0" class="printresultssearch">
<tr>
<td>
 <span><b><%=reportName%></b>&nbsp;-&nbsp;<h:outputText value="#{msgs.SEARCH_CRITERIA}"/>:</span>
 </td>
<% //Build the request Map 
   HashMap keyDescriptionsMap = reportHandler.getKeyDescriptionsMap();
   HashMap newHashMap = new HashMap();
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
            reportHandler.getReportParameters().put(attributeName,attributeValue);
			newHashMap.put(attributeName,attributeValue);
 %>
 <% if (attributeValue.length() != 0) { %>
 <% } %>
<%
      }
   } 
%>
<%if(reportHandler.getReportParameters().get("selectedSearchType") != null) {%>
  <td>
	  <span><%=bundle.getString("search_Type")%>:&nbsp;<b><%=reportHandler.getReportParameters().get("selectedSearchType")%></b></span>
  </td>
<%}%>

<%if(reportHandler.getReportParameters().get("activityType") != null) {%>
  <td>
	  <span>Activity Type:&nbsp;<b><%=reportHandler.getReportParameters().get("activityType")%></b></span>
  </td>
<%}%>



<%
        ArrayList fgGroups = reportHandler.getSearchScreenFieldGroupArray(reportHandler.getSearchScreenConfigArray());
        for (int fg = 0; fg < fgGroups.size(); fg++) {
            FieldConfigGroup basicSearchFieldGroup = (FieldConfigGroup) fgGroups.get(fg);
            ArrayList screenConfigArrayLocal = basicSearchFieldGroup.getFieldConfigs();

  //ArrayList screenConfigArrayLocal = reportHandler.getScreenConfigArray();
%>
 <%
String strVal = new String();
  for (Iterator it = screenConfigArrayLocal.iterator(); it.hasNext();) {
	  %>
	  
	      <td>

	  <%
     FieldConfig fieldConfig = (FieldConfig) it.next();
 	   
 	 String   value = (fieldConfig.isRange()) ? (String) newHashMap.get(fieldConfig.getDisplayName()):(String) newHashMap.get(fieldConfig.getName());
	 %>

	 <%
     if(value != null && value.length() > 0) {
       if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0)) {
                        
        //SET THE VALUES WITH USER CODES AND VALUE LIST 
        if (fieldConfig.getUserCode() != null) {
               strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
        } else {
              strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
        }
    %>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=strVal%></b>&nbsp;</span>
       
   <%  } else {%>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=value%></b>&nbsp;</span>
   <% }%>
   <%}%>
   </td>
  <% }%>
  <% }%> <!-- FG LOOP-->
<tr>
</table>



 <%
	 if (mergeText.equalsIgnoreCase(reportName))    { 
        /**********************Merge Report************************/
		 results = reportHandler.mergeReport();
		 fcArrayList  = reportHandler.getMergedRecordsHandler().getResultsConfigArrayList();
     } else if (deactiveText.equalsIgnoreCase(reportName))  {
      /**********************Deactivated Report************************/
		 results = reportHandler.deactivatedReport();
		 fcArrayList  = reportHandler.getDeactivatedReport().getResultsConfigArrayList();
	 } else if (updateText.equalsIgnoreCase(reportName))  {
      /**********************Update Report************************/
		 results = reportHandler.updateReport();
		 fcArrayList  = reportHandler.getUpdateReport().getResultsConfigArrayList();
	 } else if (unmergeText.equalsIgnoreCase(reportName))  {
      /**********************UnMerge Report************************/
		 results = reportHandler.unMergeReport();
		 fcArrayList  = reportHandler.getUnmergedRecordsHandler().getResultsConfigArrayList();
	 } else if (activityText.equalsIgnoreCase(reportName))  {
      /**********************Activity Report************************/
		 results = reportHandler.activitiesReport();
	 } else if (assumedText.equalsIgnoreCase(reportName))  {
      /**********************Assume Match Report************************/
		 results = reportHandler.assumeMatchReport();
		 fcArrayList  = reportHandler.getAssumeMatchReport().getResultsConfigArrayList();
	 } else if (potDupText.equalsIgnoreCase(reportName))  {
      /**********************Potential Dupllicates Report************************/
		 results = reportHandler.duplicateReport();
		 fcArrayList  = reportHandler.getDuplicateReport().getResultsConfigArrayList();

	 }
%> 

<% if (results != null && results.size() > 0 )  { 
	if (activityText.equalsIgnoreCase(reportName))   {
        keys = (ArrayList)request.getAttribute("keys");
		labelsList = (ArrayList)request.getAttribute("labels");
        fullFieldNamesList = (ArrayList) request.getAttribute("keys");
	} else {
	     keys.add("EUID");
	     labelsList.add("EUID");
	     fullFieldNamesList.add("EUID");
     	
	    for(int ii=0;ii<fcArrayList.size();ii++) {
		     FieldConfig fieldConfig = (FieldConfig)fcArrayList.get(ii);
		     keys.add(fieldConfig.getName());
		     labelsList.add(fieldConfig.getDisplayName());
		     fullFieldNamesList.add(fieldConfig.getFullFieldName());
	    }		
    }
    
	
	myColumnDefs.append("[");
    String value = new String();
	for(int ji=0;ji<keys.size();ji++) {
	    if ("EUID".equalsIgnoreCase((String)keys.toArray()[ji]))  {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:true,resizeable:true,width:150}";
	    }  else {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:true,resizeable:true}";
	    }
       myColumnDefs.append(value);
       if(ji != keys.size()-1) myColumnDefs.append(",");
	}
    myColumnDefs.append("]");
} 
     %> 
         


<table class="printresultssummary">
<%if(results != null) {%>
<tr>

                     <td class="reportresults">
					   <em>
						   <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
						   &nbsp;
						   <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
						   &nbsp;			   

                         <h:outputText value="#{msgs.total_records_text}"/>&nbsp;
						 <%if (potDupText.equalsIgnoreCase(reportName))  {%>
						   <%=results.size()/2%>
						 <%}else{%>
						   <%=results.size()%>
						 <%}%>
                        </em>
				     </td>
</tr>
<%}%>
<tr>
<td>
<% 
 if(results != null && results.size() > 0 ) {%>

<div id="myMarkedUpContainer<%=divId%>" class="printresults" >
	<table id="myTable">
	   <thead>
            <tr>
               <% for (int i =0;i< labelsList.size();i++) { %>
                   <th><%=labelsList.toArray()[i]%></th>
               <%}%>
            </tr>
	   </thead>
	   <tbody>
<%       for (int i3 = 0; i3 < results.size(); i3++) { %>
              <tr>
                <%HashMap valueMap = (HashMap) results.get(i3);
                    for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {

				%>
                   <td>
                      <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"&nbsp;":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
                   </td>
                  <%}%>
				  <%}%>
              </tr>
	   </tbody>
	</table>
</div>
<%}%>
</td>
</tr>
</table>

<table class="printresultssummary">
<tr>
 <td>
   <em>
     <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
	  &nbsp;
	 <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
	   &nbsp;			   
   <h:outputText value="#{msgs.total_records_text}"/>
	<%if (potDupText.equalsIgnoreCase(reportName))  {%>
		<%=results.size()/2%>&nbsp;
	<%}else{%>
		<%=results.size()%>&nbsp;
	<%}%>
   </em>
 </td>
</tr>

</table>

<%} %>  <!-- Session check -->
</body>
</head>
</html>

</f:view>
