<%-- 
    Document   : reportservices
    Created on : May 5, 2008, 7:59:17 AM
	This is an Ajax service never use this JSP directly
--%>
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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

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
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
	<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />  
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
String dateFormat  = ConfigManager.getDateFormat();
ReportHandler reportHandler = new ReportHandler();

String reportName = request.getParameter("tabName");
String formName = request.getParameter("form");
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

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
String mergeText = bundle.getString("Merged_Transaction_Report_Label");
String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
String updateText = bundle.getString("Updated_Record_Report_Label");
String activityText = bundle.getString("Activity_Report_Label");
String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
String assumedText = bundle.getString("Assumed_Matches_Report_Label");
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
String sortOption = "false";

int pageSize = 10;
ArrayList fullFieldNamesList  = new ArrayList();
ArrayList fcArrayList  = new ArrayList();

%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>

<% //Build the request Map 
  
   HashMap activityReportMap = new HashMap(); // Fix for 6701596 

   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("tabName".equalsIgnoreCase(attributeName)) && 
		    !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) && 
			!("form".equalsIgnoreCase(attributeName)) && 
			!("layer".equalsIgnoreCase(attributeName))  ) {
		    // Fix for 6701596 Start
			if (activityText.equalsIgnoreCase(reportName))  {
 		       activityReportMap.put(attributeName,attributeValue);			
			} else {
 		       reportHandler.getReportParameters().put(attributeName,attributeValue);			
			}
			// Fix for 6701596 Ends
      }
   } 
   // Fix for 6701596 Start
   if (activityText.equalsIgnoreCase(reportName))  {
       reportHandler.getReportParameters().put("activityType",activityReportMap.get("activityType"));			
	   if(activityReportMap.get("activityType").toString().equalsIgnoreCase(bundle.getString("WEEKLY_ACTIVITY")) ) {
          reportHandler.getReportParameters().put("StartDate",activityReportMap.get("StartDate"));			
	   } else if(activityReportMap.get("activityType").toString().equalsIgnoreCase(bundle.getString("MONTHLY_ACTIVITY")) ) {
		  
		  dateFormat = dateFormat.toUpperCase();
          dateFormat = dateFormat.replace("DD","01");
          dateFormat = dateFormat.replace("MM",activityReportMap.get("Monthly_Month").toString());
          dateFormat = dateFormat.replace("YYYY",activityReportMap.get("Monthly_Year").toString());

          String dateField = dateFormat;
          reportHandler.getReportParameters().put("StartDate",dateField);			
	   } else if(activityReportMap.get("activityType").toString().equalsIgnoreCase(bundle.getString("YEARLY_ACTIVITY")) ) {

		  dateFormat = dateFormat.toUpperCase();
          dateFormat = dateFormat.replace("DD","01");
          dateFormat = dateFormat.replace("MM","01");
          dateFormat = dateFormat.replace("YYYY",activityReportMap.get("Yearly_year").toString());

          String dateField = dateFormat;

		  reportHandler.getReportParameters().put("StartDate",dateField);			
	   }
   }
   // Fix for 6701596 Ends
%>

 <%
	 if (mergeText.equalsIgnoreCase(reportName))    { 
        /**********************Merge Report************************/
 		 results = reportHandler.mergeReport();
		 fcArrayList  = reportHandler.getMergedRecordsHandler().getResultsConfigArrayList();
     } else if (deactiveText.equalsIgnoreCase(reportName))  {
      /**********************Deactivated Report************************/
		 sortOption = "true";
		 results = reportHandler.deactivatedReport();
		 fcArrayList  = reportHandler.getDeactivatedReport().getResultsConfigArrayList();
	 } else if (updateText.equalsIgnoreCase(reportName))  {
      /**********************Update Report************************/
		 sortOption = "true";
		 results = reportHandler.updateReport();
		 fcArrayList  = reportHandler.getUpdateReport().getResultsConfigArrayList();
	 } else if (unmergeText.equalsIgnoreCase(reportName))  {
      /**********************UnMerge Report************************/
		 sortOption = "true";
		 results = reportHandler.unMergeReport();
		 fcArrayList  = reportHandler.getUnmergedRecordsHandler().getResultsConfigArrayList();
	 } else if (activityText.equalsIgnoreCase(reportName))  {
      /**********************Activity Report************************/
		 sortOption = "true";
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
	    
		//Fix for 54 start here
		if (potDupText.equalsIgnoreCase(reportName))  {     	
	        keys.add("Status");
    	    labelsList.add("Status");
	        fullFieldNamesList.add("Status");
		}
		//Fix for 54 ends here
		if (assumedText.equalsIgnoreCase(reportName))  {     	
	        keys.add("LID");
    	    labelsList.add("LID");
	        fullFieldNamesList.add("LID");

	        keys.add("SystemCode");
	        labelsList.add("SystemCode");
	        fullFieldNamesList.add("SystemCode");

	        keys.add("Weight");
	        labelsList.add("Weight");
	        fullFieldNamesList.add("Weight");
		}

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
          if (assumedText.equalsIgnoreCase(reportName)) { 
				 sortOption = "true";
		  }
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:"+sortOption+",resizeable:true,width:150}";
	    }  else {
          if (assumedText.equalsIgnoreCase(reportName)) { 
				 sortOption = "false";
		  }
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:"+sortOption+",resizeable:true}";
	    }
       myColumnDefs.append(value);
       if(ji != keys.size()-1) myColumnDefs.append(",");
	}
    myColumnDefs.append("]");
} 
    Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
     %> 
	 <%if(results == null ) {%>
     <div class="ajaxalert" id="errormessages1">
         <table>
             <tr>
             <td>
             <ul> 
                 <% while (messagesIter.hasNext()) {%>
                 <li>
                     <% FacesMessage facesMessage = (FacesMessage) messagesIter.next();%>
                     <%= facesMessage.getSummary()%>
                 </li>
                 <% }%>
             </ul>
             <td>
             <tr>
         </table>
     </div>


	 <%}%>
         
<% 
String reportType = (String)request.getAttribute("frequency");
%>
 
<table border="0">
 <tr><td style="width:55%" style="text-decoration:underline;">
     <b>
     <% if(reportType != null && reportType.equals(bundle.getString("YEARLY_ACTIVITY"))){%>
      <h:outputText value="#{msgs.Summary_Report_year}"/>&nbsp; <%=reportHandler.getActivityReport().getYearValue()%>
     <%} else  if (reportType != null && reportType.equals( bundle.getString("MONTHLY_ACTIVITY")) ){%>
      <h:outputText value="#{msgs.Summary_Report_month}"/>&nbsp;<%=reportHandler.getActivityReport().getLocaleMonthName()%>&nbsp; <%=reportHandler.getActivityReport().getYearValue()%>
     <%}%> 
     </b>
  </td>
  <td>&nbsp;</td>
 </tr>
   
 </table>
 
 
 
<table border="0">
<%if(results != null) {%>
<tr>
                     <td style="width:85%">
                         <h:outputText value="#{msgs.total_records_text}"/>
						   <%=results.size()%>
				     </td>
					 <td>
					 <%if(results.size() > 0){%>
                       <h:panelGrid>
                          <a title="<%=print_text%>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('<%=formName%>');openPrintWindow('/<%=URI%>/printservices/reportsprint.jsf?random='+rand+'&'+queryStr)"><span><%=print_text%></span></a>
					  </h:panelGrid> 
					<%}%>
                     </td>
</tr>
<%}%>
<tr>
<td colspan="2">
<%  if(results != null && results.size() > 0 ) {	%>
 
             <div id="myMarkedUpContainer<%=divId%>" class="reportresults">
                	<table id="myTable" border="0">
                  	   <thead>
                          <tr>
                     	   <% for (int i =0;i< keys.size();i++) { %>
                               <th><nobr><%=keys.toArray()[i]%></nobr></th>
                          <%}%>
                         </tr>
                	   </thead>
                  	   <tbody>
                       <% for (int i = 0; i < results.size(); i++) { //Outer Arraylist 
			              ArrayList valueList = (ArrayList) results.get(i);
						  int length=10;
						  boolean euidPrinted = false;
			           %>
						<tr> 		
							                 <%for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {%>
											   <td valign="top">
											      <table style="border:none">
                                                  <% for (int j = 0; j < valueList.size(); j++) { //The values itself can be an array %>
										                <%HashMap valueMap = (HashMap) valueList.get(j);%>
												    <tr>
												       <td  style="border:none">	
													     <%if (assumedText.equalsIgnoreCase(reportName) && ("EUID").equalsIgnoreCase((String)fullFieldNamesList.toArray	()[kc])) { %>
														     <% if (j ==0 ) { %>
																 <nobr>
																	 <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
																 </nobr>
															 <% } else  { %>
															    &nbsp;
															 <% } %> 
														<%} else {%>
																 <nobr>
																	 <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
																 </nobr>
														<%}%>
												     </td>
													 </tr>												 
  									             <%}%><!-- end value list loop -->
												 </table>
												</td>
							                 <%}%> <!-- end Full field loop -->
							</tr>
		                  <% } %>
	                 </tbody>
                 </table>
              </div>

<%}%>
</td>
</tr>
</table>
<%  if(results != null ) {	%>
		<script>
			var fieldsArray = new Array();
		</script>
			<% for(int index=0;index<keys.size();index++) {%>
					<script>
						fieldsArray[<%=index%>] = '<%=keys.toArray()[index]%>';
					</script>
			<%}%>
		<script type="text/javascript">
			var myDataSource = new YAHOO.util.DataSource(YAHOO.util.Dom.get("myTable"));
			myDataSource.responseType = YAHOO.util.DataSource.TYPE_HTMLTABLE;
			myDataSource.responseSchema = {
				fields: fieldsArray
			};
			var myConfigs = {
				paginator : new YAHOO.widget.Paginator({
					rowsPerPage    : 10, // REQUIRED
					totalRecords   : <%=results.size()%> //, // OPTIONAL
					//template       : "{PageLinks} Show {RowsPerPageDropdown} per page"
				}),
				resizable : true     
			};
			var myColumnDefs = <%=myColumnDefs.toString().length() == 0?"\""+ "\"":myColumnDefs.toString()%>;
			var myDataTable = new YAHOO.widget.DataTable("myMarkedUpContainer<%=divId%>", myColumnDefs, myDataSource,myConfigs);
		</script>
<%}%>

<% for(int index=0;index<keys.size();index++) {%>
        <script>
            fieldsArray[<%=index%>] = '<%=keys.toArray()[index]%>';
        </script>
<%}%>

  <%} %>  <!-- Session check -->
</f:view>
