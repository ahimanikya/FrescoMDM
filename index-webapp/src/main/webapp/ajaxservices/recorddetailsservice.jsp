<%-- 
    Document   : Record details Ajax services
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Admin
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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DashboardHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext" %>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ArrayList"  %>
<f:view>
<!-- Modified on 22-aug-2008 for the incorparte with the functionality of back button in euiddetails.jsp  
    added String previousQueryStr = request.getQueryString();
    and appended the same with every href link
-->        
<%
 String previousQueryStr = null;
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
 previousQueryStr = request.getQueryString();
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
SourceHandler sourceHandler = new SourceHandler();
NavigationHandler navigationHandler = new NavigationHandler();

//Keep the record-details page screen object in session when the user comes from the dashboard
String pageFrom = request.getParameter("pageFrom");   
if(pageFrom != null && "dashboard".equalsIgnoreCase(pageFrom)) {
	  session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
}
  
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

MasterControllerService masterControllerService = new MasterControllerService();
Operations operations  = new Operations();
RecordDetailsHandler recordDetailsHandler = new RecordDetailsHandler();

String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

Enumeration parameterNames = request.getParameterNames();
MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

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
//Fix for 6700851 on 07-01-09 
ArrayList euidList = new ArrayList();
String euids = "";

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());

String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");
String active_euid_text = bundle.getString("active_euid_text") ;
ArrayList fullFieldNamesList  = new ArrayList();


//Variables required compare euids
String collectEuids = request.getParameter("collectEuids");
boolean iscollectEuids = (null == collectEuids?false:true);

//Variables required for compare euids
String collectedEuids = request.getParameter("collecteuid");

ArrayList collectedEuidsList = new ArrayList();
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
	 attributeValue = attributeValue.replaceAll("~~","%");
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
		    !("selectedSearchType".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
		     recordDetailsHandler.getParametersMap().put(attributeName,attributeValue);			
      }
   } 
  //set the selected search type here....
 recordDetailsHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));

%>


<% 
String euidValue  = (String) recordDetailsHandler.getParametersMap().get("EUID");
 String lid = (String) recordDetailsHandler.getParametersMap().get("LID");
  String systemCode = (String) recordDetailsHandler.getParametersMap().get("SystemCode");
%>

<%if(iscollectEuids) {%> <!-- if compare euids case -->
    
    <%if(pageFrom != null && "dashboard".equalsIgnoreCase(pageFrom)) {
     boolean validationFailed = false;
	 DashboardHandler dashboardHandler = new DashboardHandler();
     if( (request.getParameter("EUID 1") != null &&  request.getParameter("EUID 1").trim().length()  == 0 ) &&    
        (request.getParameter("EUID 2") != null &&  request.getParameter("EUID 2").trim().length()  == 0 ) &&    
        (request.getParameter("EUID 3") != null &&  request.getParameter("EUID 3").trim().length()  == 0 ) &&    
        (request.getParameter("EUID 4") != null &&  request.getParameter("EUID 4").trim().length()  == 0 ) ) {
        validationFailed = true;
     }

	 //Fix for #29 Starts

     HashMap duplicateEuidsSet = new HashMap();
     
	 boolean duplicateEuidFound = false;
	 ArrayList dupEuids = new ArrayList();

     duplicateEuidsSet.put("EUID 1",request.getParameter("EUID 1"));

	 if(request.getParameter("EUID 2").trim().length() > 0 && duplicateEuidsSet.containsValue(request.getParameter("EUID 2"))) {
	    dupEuids.add("EUID 2");
	 } else {
	    duplicateEuidsSet.put("EUID 2",request.getParameter("EUID 2"));
	 }
  
 
	 if(request.getParameter("EUID 3").trim().length() > 0 && duplicateEuidsSet.containsValue(request.getParameter("EUID 3"))) {
	    dupEuids.add("EUID 3");
	 } else {
	    duplicateEuidsSet.put("EUID 3",request.getParameter("EUID 3"));
	 }
 
 	 if(request.getParameter("EUID 4").trim().length() > 0 && duplicateEuidsSet.containsValue(request.getParameter("EUID 4"))) {
	    dupEuids.add("EUID 4");
	 } else {
        duplicateEuidsSet.put("EUID 4",request.getParameter("EUID 4"));
	 }
 
 	 //Fix for #29 Ends
 
	  
	  //Set EUID1, EUID2, EUID3 and EUID 4 here
      dashboardHandler.setEuid1(request.getParameter("EUID 1"));
      dashboardHandler.setEuid2(request.getParameter("EUID 2"));
      dashboardHandler.setEuid3(request.getParameter("EUID 3"));
      dashboardHandler.setEuid4(request.getParameter("EUID 4"));

	  session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
      boolean  duplicateFound = (request.getParameter("duplicateLid") != null && request.getParameter("duplicateLid").length() > 0 ) ? true : false;


	  %>
   <% 
     if(dupEuids.size() > 0 || validationFailed) { //If validation fails display the error message here  ***Fix for #29 Starts****

    %>
 	  <table>
		<tr>
		 <td>
 		  <%if(dupEuids.size() > 0 ) {
       	    String[] dupMesssages  = new String[dupEuids.size()];
	        StringBuffer msgs = new StringBuffer("<ul>");	
     	  %>
  		  <%for(int i = 0; i< dupEuids.size() ; i ++ ) {
		      dupMesssages[i]  = bundle.getString("duplicate_euid_text")+" '<b>" + request.getParameter(dupEuids.get(i).toString()) + "</b>' " +  bundle.getString("has_been_entered_text");
		  %>
			<%
			  msgs.append("<li>");
			  msgs.append(dupMesssages[i] + " (" + dupEuids.get(i).toString() +")");
			  msgs.append("</li>");
			 %>
	     <%} msgs.append("</ul>"); %>
           <script>document.getElementById('messages').innerHTML="<%=msgs%>";</script>
		  <%}else {      //Fix for #29 Ends%> 
			<script>document.getElementById('messages').innerHTML='<ul><li><%=bundle.getString("ERROR_one_of_many")%></li></ul>';</script> 
		  <%}%>
 		<td>
	  <tr>
	</table>
 <%} else {%>
<% ArrayList allEuids = new ArrayList();	
	%>
<%
  if( (request.getParameter("EUID 1") != null &&  request.getParameter("EUID 1").trim().length()  > 0 ) ) {
 	  allEuids.add(request.getParameter("EUID 1"));
   }
 %>
	
<%
  if( (request.getParameter("EUID 2") != null &&  request.getParameter("EUID 2").trim().length()  > 0 ) ) {
 	  allEuids.add(request.getParameter("EUID 2"));
   }
 %>
 	
<%
  if( (request.getParameter("EUID 3") != null &&  request.getParameter("EUID 3").trim().length()  > 0 ) ) {
 	  allEuids.add(request.getParameter("EUID 3"));
   }
 %>

<%
  if( (request.getParameter("EUID 4") != null &&  request.getParameter("EUID 4").trim().length()  > 0 ) ) {
 	  allEuids.add(request.getParameter("EUID 4"));
   }
 %>

 <%   String allEuidsString  = allEuids.toString();
      allEuidsString = allEuidsString.replace("[","");
      allEuidsString = allEuidsString.replace("]","");
      allEuidsString = allEuidsString.replace(" ","");
	  ArrayList compareEuidsList = dashboardHandler.performCompareEuidSearch();	
      Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
 %>
     <%if(compareEuidsList != null && compareEuidsList.size() > 0) { %>
		<table><tr><td>
  		<script>
           window.location = '/<%=URI%>/compareduplicates.jsf?euids=<%=allEuidsString%>&previousQueryStr=<%=previousQueryStr%>&fromUrl=dashboard.jsf';
 		</script>
 		</td></tr>
		</table>
   <%} else {
	      StringBuffer msgs = new StringBuffer("<ul>");	
	%>
    <table>
	<tr>
	 <td>
 	 <% 
		while (messagesIter.hasNext())   { %>
		<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
		<%msgs.append("<li>");
		  msgs.append(facesMessage.getSummary());
          msgs.append("</li>");
		 %>
	  <%}
		 msgs.append("</ul>");
	  %>
         <script>document.getElementById('messages').innerHTML="<%=msgs%>";</script>
		<td>
	<tr>
  </table>
   <%}%>

<%}%>


    <%} else {%> <!--If not from the dashboard -->
		<table><tr><td>
 		<script>
 		if (checkedItems.length >= 2)  { 
		  var euids = "";
		  for(var j=0;j<checkedItems.length;j++) {
			euids +=checkedItems[j] + ",";
		  }
		  
		  window.location = '/<%=URI%>/compareduplicates.jsf?euids='+checkedItems+'&<%=previousQueryStr%>&fromUrl=recorddetails.jsf';
	    } else {
	        var messages = document.getElementById("messages");
	        messages.innerHTML= "<ul><li>Please select at least two EUID to compare </li> </ul>";
	    } 

		</script>
 		</td></tr>
		</table>
<%}%>




<%}else if( euidValue != null && euidValue.length() > 0) {
	 //results = recordDetailsHandler.performSubmit();
	// EnterpriseObject eo = masterControllerService.getEnterpriseObject(euidValue);
     String megredEuid  = midmUtilityManager.getMergedEuid(euidValue);
	 Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	%> <!-- if only EUID is entered by the user is entered by the user-->
	<%if(megredEuid == null) {%>
<table>
  <tr><td>
  <script>
	   <%if("euiddetails.jsf".equalsIgnoreCase(request.getParameter("pageName"))) {%>
         window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=euidValue%>';
	   <%} else if("compareduplicates.jsf".equalsIgnoreCase(request.getParameter("pageName"))) {%>
         window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=euidValue%>';
	   <%} else if("dashboard.jsf".equalsIgnoreCase(request.getParameter("pageName"))) {%>
         window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=euidValue%>&<%=previousQueryStr%>&fromUrl=dashboard.jsf';
	   <%} else {%>
         window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=euidValue%>&<%=previousQueryStr%>&fromUrl=recorddetails.jsf';
	   <%}%>
  </script>
  </td>
  </tr>
  </table>
  <%}else if(megredEuid != null){%>
       <%if("Exception_Occured".equalsIgnoreCase(megredEuid)) {
	      StringBuffer msgs = new StringBuffer("<ul>");	
       %>
  <table>
	<tr>
     <td>
		<% while (messagesIter.hasNext())   { %>
		 <% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
		<% msgs.append("<li>");
		   msgs.append(facesMessage.getSummary());
		   msgs.append("</li>");
 	    %>
 	    <%}
		msgs.append("</ul>");
		%>
       <script>document.getElementById('messages').innerHTML="<%=msgs%>";</script>
	<td>
   <tr>
 </table>
	<%}else if("Invalid EUID".equalsIgnoreCase(megredEuid)) {%>
		<table>
	   <tr>
		 <td>
		  <% String messages = "EUID '" + euidValue + "' " + bundle.getString("euid_not_found_text"); %>     	 
			<script>
			  var messages = document.getElementById("messages");
			  messages.innerHTML= "<ul><li><%=messages%></li></ul>";
			  messages.style.visibility="visible";
		  </script>
	   </td>
	   </tr>
	 <table>
  <%}else{%>
   <table>
	 <tr><td>
	 <!-- Modified  on 27-09-2008, to change alert pop up window to information pop up window -->
	 <script>
		 window.location = "#top";
		 document.getElementById("activemessageDiv").innerHTML='<%=megredEuid%> <%=active_euid_text%> <%=euidValue%>.';
		 document.getElementById('activeDiv').style.visibility='visible';
		 document.getElementById('activeDiv').style.display='block';
		 <%if("euiddetails.jsf".equalsIgnoreCase(request.getParameter("pageName"))) {%>
			 popUrl = '/<%=URI%>/euiddetails.jsf?euid=<%=megredEuid%>';
		 <%} else if("dashboard.jsf".equalsIgnoreCase(request.getParameter("pageName"))) {%>
			 popUrl = '/<%=URI%>/euiddetails.jsf?euid=<%=megredEuid%>&<%=previousQueryStr%>&fromUrl=dashboard.jsf';
		 <%} else {%>
			 popUrl = '/<%=URI%>/euiddetails.jsf?euid=<%=megredEuid%>&<%=previousQueryStr%>&fromUrl=recorddetails.jsf';
		 <%}%>
	  
	  </script>
	 </td>
	 </tr>
	 </table>
	 

 <%}%>
<%}%>

<% } else if (recordDetailsHandler.getParametersMap().get("LID") != null && recordDetailsHandler.getParametersMap().get("SystemCode") != null) {%><!-- if only System Code/LID is entered by the user is entered by the user-->

 
<%
   boolean checkMasking = true;
   lid = (String) recordDetailsHandler.getParametersMap().get("LID");
   String lidmask = (String) recordDetailsHandler.getParametersMap().get("lidmask");
   // String systemCode = (String) recordDetailsHandler.getParametersMap().get("SystemCode");
   // valiadtions.put(basicFieldConfig.getDisplayName(),bundle.getString("lid_format_error_text") + " " +basicFieldConfig.getInputMask());
   checkMasking = recordDetailsHandler.checkMasking(lid,lidmask);

   %>
 <%if(checkMasking) {%>
  <%
   EnterpriseObject eo = null;

  lid = lid.replaceAll("-", "");
  SystemObject so = masterControllerService.getSystemObject(systemCode, lid);
  if(so != null ) {
     eo = masterControllerService.getEnterpriseObjectForSO(so);
  }
%>

<%if(eo != null) {%>
<table>
  <tr><td>
  <script>
        window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=eo.getEUID()%>&<%=previousQueryStr%>&fromUrl=recorddetails.jsf';
   </script>
  </td>
  </tr>
  </table>
  <%}else{%>
     <table>
	   <tr>
	     <td>
  <%
   String messages = "";
  if(lid.trim().length() == 0 && systemCode.trim().length() == 0 && euidValue.length()== 0 ){
         messages ="Please enter EUID or both System and" + " "+ localIdDesignation ;
  }
  if(systemCode.trim().length() != 0 && lid.trim().length() == 0 ){
  messages = bundle.getString("LID_SysCode") + " "+ localIdDesignation ;
  }

  if(systemCode.trim().length() != 0 && lid.trim().length() != 0 && eo == null ){
  messages = sourceHandler.getSystemCodeDescription(systemCode) + "/"+ (String) recordDetailsHandler.getParametersMap().get("LID") +  " Not Found. Please check the entered values.";
  }
 %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<ul><li><%=messages%></li></ul>";
		 messages.style.visibility="visible";
		 var formNameValue = document.forms['advancedformData'];
		 var lidField =  getDateFieldName(formNameValue.name,'LID');
 		 if(lidField != null) {
		  document.getElementById(lidField).focus();
		 }
	 </script>
	   </td>
	   </tr>
	 <table>
   <%}%>
 <%} else {%>
     <table>
	   <tr>
	     <td>
   <%
    localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
   String messages = localIdDesignation + " " + bundle.getString("lid_format_error_text") + " " +lidmask;
  %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<ul><li><%=messages%></li></ul>";
		 messages.style.visibility="visible";
		 var formNameValue = document.forms['advancedformData'];
		 var lidField =  getDateFieldName(formNameValue.name,'LID');
 		 if(lidField != null) {
		  document.getElementById(lidField).focus();
		 }
	 </script>
	   </td>
	   </tr>
	 <table>
 <%}%>


 <%} else {%>

<% if(request.getParameter("pageName") != null) { 
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
	%> <!-- From the euid details page-->
   <%if(request.getParameter("EUID") != null && request.getParameter("EUID").trim().length() == 0
	  ) 
   
    {%>
            <table>
       	   <tr>
       	     <td>
              <% String messages = bundle.getString("enter_euid_text"); %>     	 
 	            <script>
       		      var messages = document.getElementById("messages");
                  messages.innerHTML= "<ul><li><%=messages%></li></ul>";
       		      messages.style.visibility="visible";
       	      </script>
       	   </td>
       	   </tr>
       	 <table>
    <%}%>

<%} else {%>
<%

 results = recordDetailsHandler.performSubmit();

ArrayList resultConfigArray = recordDetailsHandler.getResultsConfigArray();
if (results != null)   {
   keys.add("CheckBox");
   labelsList.add("");
   fullFieldNamesList.add("CheckBox");

   keys.add("EUID");
   labelsList.add("EUID");
   fullFieldNamesList.add("EUID");

	for (int i=0;i<resultConfigArray.size();i++)    {
       	FieldConfig fieldConfig = (FieldConfig)resultConfigArray.get(i);
		//keys.add(fieldConfig.getDisplayName());
		keys.add(fieldConfig.getName());
		labelsList.add(fieldConfig.getDisplayName());
		fullFieldNamesList.add(fieldConfig.getFullFieldName());
    }
   //add weight fields here
   keys.add("Weight");
   labelsList.add("Weight");
   fullFieldNamesList.add("Weight");

	myColumnDefs.append("[");
    String value = new String();
	for(int ji=0;ji<keys.size();ji++) {
	    if ("CheckBox".equalsIgnoreCase((String)keys.toArray()[ji]))  {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",width:25,resizeable:true}";
	    } else if ("EUID".equalsIgnoreCase((String)keys.toArray()[ji]))  {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",width:150,sortable:true,resizeable:true}";
	    }  else {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:true,resizeable:true}";
	    }
       myColumnDefs.append(value);
       if(ji != keys.size()-1) myColumnDefs.append(",");
	}
    myColumnDefs.append("]");

%>
    

     <table border="0" cellpadding="0" cellspacing="0"> 
		 <tr>
			<td>
			   <% if (operations.isEO_Compare() && results.size() > 1)   {%>
				 <a  
					 href="javascript:ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?collectEuids=true&<%=previousQueryStr%>&fromUrl=recorddetails.jsf','messages','')" 
					 class="button" 
					 title="<h:outputText value="#{msgs.dashboard_compare_tab_button}"/>">
					 <span>
						  <h:outputText value="#{msgs.dashboard_compare_tab_button}"/>
					 </span>
				 </a>                                     
			   <% } %>
			</td>
            <td style="width:67%;text-align:right">
                         <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=results.size()%>&nbsp;
			</td>
			<td><nobr>
				<% if (results.size() > 0)   {%>
					<h:panelGrid rendered="#{Operations.EO_PrintSBR}" >
						<a title="<%=print_text%>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('advancedformData');openPrintWindow('/<%=URI%>/printservices/recorddetails.jsf?random='+rand+'&'+queryStr)"><span><%=print_text%></span></a>
					</h:panelGrid>             
			    <% } %>
 				</nobr>
            </td>
         </tr>
          
         <tr>
         <td colspan="3">

         <% if (results != null && results.size() > 0 )   {%>
		   <%request.setAttribute("eoResults",results);%>
             <div id="myMarkedUpContainer" class="reportresults">
                	<table id="myTable">
                  	   <thead>
                          <tr>
                     	   <% for (int i =0;i< keys.size();i++) { %>
                               <th><%=keys.toArray()[i]%></th>
                          <%}%>
                         </tr>
                	   </thead>
                  	   <tbody>
                       <% for (int i3 = 0; i3 < results.size(); i3++) { %>
                        <tr> 
                            <%
                            
                            HashMap valueMap = (HashMap) results.get(i3);
                             for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {
 								    String keyValue = (String) keys.toArray()[kc];
								    String fullfieldValue = (String) fullFieldNamesList.toArray()[kc];
									
				              %>
                                   <td>
								    <nobr>
								      <%if(keyValue.equalsIgnoreCase("CheckBox")) {%>
										  <%if(operations.isEO_Compare() ) {%>
											  <%if("active".equalsIgnoreCase( (String)valueMap.get("EOStatus") ) ) {%>
												<input type="checkbox" id="checkbox-<%=valueMap.get("EUID")%>"
													  onclick="javascript:getCheckedValues(this,'<%=valueMap.get(fullFieldNamesList.toArray()[kc])%>')"/>&nbsp;
											  <%} else {%>
												<input type="checkbox" title="<%=valueMap.get("EOStatus")%> EO " readonly="true" disabled="true" />&nbsp;
											  <%}%>
										  <%}%>
								   <%} else if(keyValue.equalsIgnoreCase("EUID")) {%>
								   <!-- Fix for 6700851 on 07-01-09  -->
									   <%
											euidList.add(valueMap.get(fullFieldNamesList.toArray()[kc]));
									   %>
                                                                          
		                          <a href="javascript:void(0);"
								  onclick="javascript:window.location = 'euiddetails.jsf?euid=<%=valueMap.get(fullFieldNamesList.toArray()[kc])%>&<%=previousQueryStr%>&fromUrl=recorddetails.jsf&euidList='+euidList;" >
                                           <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))%>
										</a>                                                                                
									<%}else if(!keyValue.equalsIgnoreCase("Weight") && fullfieldValue.indexOf(screenObject.getRootObj().getName()) != 0) {
                                  String minorObjectType = fullfieldValue.substring(0,fullfieldValue.indexOf("."));
                                  
								  %>
				                           <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))%> 
									    <a href="javascript:void(0)" title="<%=bundle.getString("more_details")%>" onclick="align(event,ajaxOutputdiv);javascript:ajaxURL('/<%=URI%>/ajaxservices/showeuidminorobjects.jsf?euid=<%=valueMap.get("EUID")%>&MOT=<%=minorObjectType%>','ajaxOutputdiv','')"><%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":"...")%></a>

									 <%}else {%>
                                        <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))%> 
									 <%}%>
								    </nobr>
                                   </td>
                             <%}%>
                       </tr>
                     <%}%>
					 <%
						euids=euidList.toString();
						euids = euids.replace("[","");
					    euids = euids.replace("]","");
					    euids = euids.replace(" ","");
					 %>
 	                 </tbody>
                    </table>
					<table><tr><td><script>euidList='<%=euids%>'</script></td></tr></table>
                </div>
		   <% } %>
           </td>
           </tr>
       </table>

      <% if (results != null && results.size() > 0 )   {%>
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
				})     
			};
			var myColumnDefs = <%=myColumnDefs.toString().length() == 0?"\""+ "\"":myColumnDefs.toString()%>;
			var myDataTable = new YAHOO.widget.DataTable("myMarkedUpContainer", myColumnDefs, myDataSource,myConfigs);
		</script>
      <% } %>
	 <script>
		//nullify any previous error messages
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";
		 messages.style.visibility="hidden";
	 </script>
<% } else { %> <!-- End results!= null -->
 <p style="text-align:left;color:red;">
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
			
          while (messagesIter.hasNext()) {
	        FacesMessage facesMessage = (FacesMessage) messagesIter.next();
	%>
 		<%
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %> 
	 <table><tr><td>
     <script>document.getElementById('messages').innerHTML="<%=msgs%>";</script>
	 </td></tr></table>
</p>
 <% } %>

<%}%>

<%}%> <!-- if not euid or systemcode/lid values entered -->

<%} %>  <!-- Session check -->

</f:view>
