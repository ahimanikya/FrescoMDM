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
if(session.getAttribute("user") == null  ) {
   FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
}
%>

<%
ConfigManager.init();

ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
String uri = request.getRequestURI();
String requestPage = uri.substring(uri.lastIndexOf("/")+1,uri.length());
String recordDetailsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("record-details").getDisplayTitle();

String transactionsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("transactions").getDisplayTitle();
String duplicateRecordsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("duplicate-records").getDisplayTitle();
String assumeMatchesLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("assumed-matches").getDisplayTitle();
String sourceRecordsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("source-record").getDisplayTitle();
String reportsLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("reports").getDisplayTitle();
String auditLogLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("audit-log").getDisplayTitle();
String dashBoardLabel = ConfigManager.getInstance().getScreenObjectFromScreenName("dashboard").getDisplayTitle();
%>
<!-- 
  Author Sridhar Narsingh
  sridhar@ligaturesoftware.com
  http://www.ligaturesoftware.com
  Update Date: 12/16/2007  
  -->
  <h:form>   

      <!--Skip to main Content added-->
    <a href="#mainContent"><img src="images/spacer.gif" border="0" height="0" width= "0" alt="Skip to main Content"></a>

      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
              <td align="left">
                  <img src='images/EDM-logo.png' alt="Enterprise Data Manager">
              </td>
              <td align="right">
                  <% if(session.getAttribute("user") != null) {%>
                  <span class="greetingsmall"><nobr><%=session.getAttribute("user")%>&nbsp;</span>
                      <h:commandLink action="#{LoginHandler.signOutUser}" styleClass="greetingsmall">
                              <h:outputText  value="#{msgs.header_logout_prompt}" />
                          </h:commandLink>
                      
                   <%}%>              
                  <img src='images/sun-logo.png' alt="Sun Microsystems Logo">
              </td>
          </tr>    
      </table>
  </h:form>   

  
     <div id="menuDiv">
    <div id="header">    
    <h:form>   
    <% if("dashboard.jsp".equalsIgnoreCase(requestPage)) {%>                   
    <nobr><h:commandLink styleClass="selmenu" id="dashlink"  action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}"  action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}"  action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}"  action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}"  action="#{NavigationHandler.toReports}"><%=reportsLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}"  action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%> </h:commandLink></nobr>
    <nobr><h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}"  action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%> </h:commandLink></nobr>    
    <div class="blueline">&nbsp;</div>
    
    <%} else if("duplicaterecords.jsp".equalsIgnoreCase(requestPage) || "compareduplicates.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                            
    <%}else if("patientdetails.jsp".equalsIgnoreCase(requestPage)  || "euiddetails.jsp".equalsIgnoreCase(requestPage)|| "editmaineuid.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%}else if("assumedmatches.jsp".equalsIgnoreCase(requestPage)   || "ameuiddetails.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%}else if("transactions.jsp".equalsIgnoreCase(requestPage)   || "transeuiddetails.jsp".equalsIgnoreCase(requestPage))  {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%} else if("reports.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}"   action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="replink" rendered="#{Operations.reports_View}"  action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}"  action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}"  action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%} else if("sourcerecords.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%}else if("auditlog.jsp".equalsIgnoreCase(requestPage)) {%> 
                            
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="selmenu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=screenObject.getDisplayTitle()%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%}else if (("error404.jsp".equalsIgnoreCase(requestPage))||("error500.jsp".equalsIgnoreCase(requestPage))) {%>                         
                             
                                <h:commandLink styleClass="menu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                                                    
    <%} else { %>                                               
                            
                                <h:commandLink styleClass="selmenu" id="dashlink" action="#{NavigationHandler.toDashboard}"><%=dashBoardLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="duplicaterecordslink" rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><%=duplicateRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="patlink" rendered="#{Operations.EO_SearchViewSBR}" action="#{NavigationHandler.toPatientDetails}"><%=recordDetailsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="amlink" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"><%=assumeMatchesLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="tralink"  rendered="#{Operations.transLog_SearchView}" action="#{NavigationHandler.toTransactions}"><%=transactionsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="replink" rendered="#{Operations.reports_View}" action="#{NavigationHandler.toReports}"><%=reportsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="sourcelink" rendered="#{Operations.SO_SearchView}" action="#{NavigationHandler.toSourceRecords}"><%=sourceRecordsLabel%></h:commandLink>
                                <h:commandLink styleClass="menu" id="allink" rendered="#{Operations.auditLog_SearchView}" action="#{NavigationHandler.toAuditLog}"><%=auditLogLabel%></h:commandLink>
                                <div class="blueline">&nbsp;</div>
                        
    <%}%>

</div>
</div>
</h:form>                             
