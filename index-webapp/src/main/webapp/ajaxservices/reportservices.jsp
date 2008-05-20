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
ReportHandler reportHandler = new ReportHandler();

String reportName = request.getParameter("tabName");

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

<% //Build the request Map 
  

   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
   	   //System.out.println(attributeName + "->" + attributeValue);
       if ( !("tabName".equalsIgnoreCase(attributeName)) && 
		    !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) && 
			!("layer".equalsIgnoreCase(attributeName))  ) {
		     reportHandler.getReportParameters().put(attributeName,attributeValue);			
      }
   } 
%>
<%
//System.out.println("reportHandler.getReportParameters() --> " + reportHandler.getReportParameters());
%>


 <%
	 if (mergeText.equalsIgnoreCase(reportName))    { 
        /**********************Merge Report************************/
		 results = reportHandler.mergeReport();
		 fcArrayList  = reportHandler.getMergedRecordsHandler().getResultsConfigArrayList();
		 //System.out.println("Merge Report --> " + results);
     } else if (deactiveText.equalsIgnoreCase(reportName))  {
      /**********************Deactivated Report************************/
		 results = reportHandler.deactivatedReport();
		 fcArrayList  = reportHandler.getDeactivatedReport().getResultsConfigArrayList();
		 //System.out.println("Deactivated Report --> " + results);
	 } else if (updateText.equalsIgnoreCase(reportName))  {
      /**********************Update Report************************/
		 results = reportHandler.updateReport();
		 fcArrayList  = reportHandler.getUpdateReport().getResultsConfigArrayList();
		// System.out.println("Update Report --> " + results);
	 } else if (unmergeText.equalsIgnoreCase(reportName))  {
      /**********************UnMerge Report************************/
		 results = reportHandler.unMergeReport();
		 fcArrayList  = reportHandler.getUnmergedRecordsHandler().getResultsConfigArrayList();
		// System.out.println("UnMerge Report --> " + results);
	 } else if (activityText.equalsIgnoreCase(reportName))  {
      /**********************Activity Report************************/
		 results = reportHandler.activitiesReport();
		// System.out.println("activity Report --> " + results);
	 } else if (assumedText.equalsIgnoreCase(reportName))  {
      /**********************Assume Match Report************************/
		 results = reportHandler.assumeMatchReport();
		 fcArrayList  = reportHandler.getAssumeMatchReport().getResultsConfigArrayList();
		// System.out.println("assumeMatchReport--> " + results);
	 } else if (potDupText.equalsIgnoreCase(reportName))  {
      /**********************Potential Dupllicates Report************************/
		 results = reportHandler.duplicateReport();
		 fcArrayList  = reportHandler.getDuplicateReport().getResultsConfigArrayList();

		// System.out.println("Potential Duplicate Report --> " + results);
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
    Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
     %> 
	 <%if(results == null ) {%>
     <div class="ajaxalert">
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
//System.out.println("myColumnDefs " + myColumnDefs);
%>
<table>
<%if(results != null) {%>
<tr>
                     <td style="width:93%">
                         <h:outputText value="#{msgs.total_records_text}"/>
						 <%if (potDupText.equalsIgnoreCase(reportName))  {%>
						   <%=results.size()/2%>&nbsp;&nbsp;
						 <%}else{%>
						   <%=results.size()%>&nbsp;&nbsp;
						 <%}%>

				     </td>
					 <td>
                         <h:outputLink styleClass="button" 
                                       rendered="#{Operations.EO_PrintSBR }"  
                                       value="JavaScript:window.print();">
                             <span><h:outputText value="#{msgs.print_text}"/>  </span>
                         </h:outputLink>              
                         
                     </td>
</tr>
<%}%>
<tr>
<td colspan="2">
<% //System.out.println("-------------before printing-----------");  
 if(results != null && results.size() > 0 ) {%>

<div id="myMarkedUpContainer<%=divId%>">
	<table id="myTable">
	   <thead>
            <tr>
	   <% for (int i =0;i< keys.size();i++) { %>
                <th><%=keys.toArray()[i]%></th>
	   <%}%>
            </tr>
	   </thead>
	   <tbody>
<%       for (int i3 = 0; i3 < results.size(); i3++) { %>
              <tr>
                <%HashMap valueMap = (HashMap) results.get(i3);
                    for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {
                      //System.out.println("-------------xcvxxcv------------------------" + valueMap.get(fullFieldNamesList.toArray()[kc]));
				%>
                   <td>
                      <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
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
        rowsPerPage    : <%=reportHandler.getRecordsPerPage(reportName)%>, //, // REQUIRED
        totalRecords   : <%=(results != null && results.size() > 0 )?results.size():"0"%>//, // OPTIONAL
        //template       : "{PageLinks} Show {RowsPerPageDropdown} per page"
    })     
};

var myDataTable = new YAHOO.widget.DataTable("myContainer",
    myColumnDefs, myDataSource, myConfigs);


var myColumnDefs = <%=myColumnDefs.toString().length() == 0?"\""+ "\"":myColumnDefs.toString()%>;


var myDataTable = new YAHOO.widget.DataTable("myMarkedUpContainer<%=divId%>", myColumnDefs, myDataSource,myConfigs);
</script>
  <%} %>  <!-- Session check -->
</f:view>
