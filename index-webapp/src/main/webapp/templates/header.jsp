<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>

<f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
<%@ page isErrorPage="false" errorPage="../error500.jsp" %>

<%
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
if(session!=null && session.isNew()) {
	//System.out.println("SESSION INVALDATED CONDITION " + screenObject.getDisplayTitle());
%>
   <c:redirect url="login.jsf"/>
<%}
%>

<%
if(session.getAttribute("ScreenObject") == null  ) {
	//System.out.println("redirecting if screen object is null ");

%>
   <c:redirect url="login.jsf"/>
<%}
%>

<%
if(session.getAttribute("user") == null  ) {
%>
   <c:redirect url="login.jsf"/>
<%}
%>

<%
ConfigManager.init();


String uri = request.getRequestURI();
String requestPage = uri.substring(uri.lastIndexOf("/")+1,uri.length());
String recordDetailsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("record-details").getDisplayTitle();

String transactionsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("transactions").getDisplayTitle();
String duplicateRecordsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("duplicate-records").getDisplayTitle();
String assumeMatchesLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("assumed-matches").getDisplayTitle();
String sourceRecordsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("source-record").getDisplayTitle();
String reportsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("reports").getDisplayTitle();
String auditLogLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("audit-log").getDisplayTitle();
String dashBoardLabel = "Dashboard";
%>
<!-- 
  Author Sridhar Narsingh
  sridhar@ligaturesoftware.com
  http://www.ligaturesoftware.com
  Update Date: 12/16/2007  
  -->

      <!--Skip to main Content added-->
    <a href="#mainContent"><img src="images/spacer.gif" border="0" height="0" width= "0" alt="Skip to main Content"></a>


 <div id="menuDiv">
     <table width="100%" cellpadding="0" cellspacing="0" border="0" >
         <h:form>   
             <tr>
                 <td align="left">
                     <img src='images/EDM-logo.png' alt="Enterprise Data Manager">
                 </td>
                 <td align="right">
                     <% if (session.getAttribute("user") != null) {%>
                     <span class="greetingsmall"><nobr><%=session.getAttribute("user")%>&nbsp;</span>
                     <h:commandLink action="#{LoginHandler.signOutUser}" styleClass="greetingsmall">
                         <h:outputText  value="#{msgs.header_logout_prompt}" />
                     </h:commandLink>
                     
                     <%}%>             
                     <img src='images/sun-logo.png' alt="Sun Microsystems Logo">
                 </td>
             </tr>    
         </h:form>   
         <tr>
             <td colspan="2">
                 <div id="header">    
                     <h:form>   
                         <% if ("dashboard.jsp".equalsIgnoreCase(requestPage)) {%>                 
                         <h:commandLink styleClass="navbuttonselected" id="dashlink"  action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}"  action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}"  action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}"  action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}"  action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}"  action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span> </h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}"  action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span> </h:commandLink>    
                         
                         
                         <%} else if ("duplicaterecords.jsp".equalsIgnoreCase(requestPage) || "compareduplicates.jsp".equalsIgnoreCase(requestPage)) {%>
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("recorddetails.jsp".equalsIgnoreCase(requestPage) || "euiddetails.jsp".equalsIgnoreCase(requestPage) || "editmaineuid.jsp".equalsIgnoreCase(requestPage)) {%>
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("assumedmatches.jsp".equalsIgnoreCase(requestPage) || "ameuiddetails.jsp".equalsIgnoreCase(requestPage)) {%> 
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("transactions.jsp".equalsIgnoreCase(requestPage) || "transeuiddetails.jsp".equalsIgnoreCase(requestPage)) {%> 
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("reports.jsp".equalsIgnoreCase(requestPage)) {%> 
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}"   action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="replink" rendered="#{Operations.reports_View}"  action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}"  action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}"  action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("sourcerecords.jsp".equalsIgnoreCase(requestPage)) {%>
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if ("auditlog.jsp".equalsIgnoreCase(requestPage)) {%> 
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbuttonselected" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else if (("error404.jsp".equalsIgnoreCase(requestPage)) || ("error500.jsp".equalsIgnoreCase(requestPage))) {%>                         
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%} else {%>                                               
                         
                         <h:commandLink styleClass="navbutton" id="dashlink" action="#{NavigationHandler.toDashboard}"><span><%=dashBoardLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><span><%=duplicateRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><span><%=recordDetailsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><span><%=assumeMatchesLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><span><%=transactionsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><span><%=reportsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><span><%=sourceRecordsLabel%></span></h:commandLink>
                         <h:commandLink styleClass="navbutton" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><span><%=auditLogLabel%></span></h:commandLink>
                         
                         
                         <%}%>
                     </h:form>            
                 </div>                 
             </td>
         </tr>
         <tr>
             <td width="100%" colspan="2"><div class="blueline">&nbsp;</div></td>
         </tr>
     </table>    
  
</div>
