<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpServletRequest"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:view>
    <f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title>   
        <body>
                <%@include file="./templates/header.jsp"%>
                
<div id="mainContent">  <!-- Main content -->
<table border="0" cellpadding="3" cellspacing="1">
  <tr>
   <td> 
   <%HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();%>
   
   <div class="dashboardHeadMessage"> <h:outputLabel for="#{msgs.dashboard_summary_table_text}" value="#{msgs.dashboard_summary_table_text}"/> </div>
         <div id="dashboardSummary" class="dashboardSummary"> 
          <h:form>
            <table border="0" cellspacing="10" cellpadding="4">
                <!--Caption included-->                
                <!--caption class="euidHeadMessage">Summary</caption-->
                <tr><td><h:outputText value="#{msgs.dashboard_summary_table_link1}"/> : <h:commandLink  action="#{NavigationHandler.toDuplicateRecords}"><h:outputText value="#{DashboardHandler.countPotentialDuplicates} " /></h:commandLink></td></tr>
                <tr><td><h:outputText value="#{msgs.dashboard_summary_table_link4}"/> : <h:commandLink  action="#{NavigationHandler.toAssumedMatches}"><h:outputText value="#{DashboardHandler.countAssumedMatches} " /></h:commandLink></td></tr>
                <!--
                <tr><td><h:outputText value="#{msgs.dashboard_summary_table_link2}"/></td></tr>
                <tr><td><h:outputText value="#{msgs.dashboard_summary_table_link3}"/></td></tr>
                -->
            </table>
          </h:form>
         </div>
   </td>
   <td> 
       <div class="dashboardHeadMessage"><h:outputLabel for="#{msgs.dashboard_quicksearch_table_text}" value="#{msgs.dashboard_quicksearch_table_text}"/></div> 
        <div id="qs" class="dashboardSummaryQS"> 
            <table border="0" cellspacing="0" cellpadding="4">
                <!--Caption included-->
                <!--Caption class = "euidHeadMessage">Quick Search</Caption-->
                <h:form id="QuickSearchForm">
                <tr><td> &nbsp;</td></tr>
                <tr>
                    <td> 
                        <span><h:outputText value="#{msgs.dashboard_quick_search_text}" /></span>
                    </td>
                    <td> <h:inputText id="euidField" value="#{PatientDetailsHandler.singleEUID}"  maxlength="10"/> </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>

                <tr>
                      <td>
                          <h:commandLink  action="#{PatientDetailsHandler.singleEuidSearch}">
                              <h:outputText value="#{msgs.dashboard_search_but_text}" />
                          </h:commandLink> 
                      </td>
                      <td align="right">
                       <h:commandLink  id="patlink" 
                                       rendered="#{Operations.EO_SearchViewSBR}"  
                                       action="#{NavigationHandler.toPatientDetails}">
                             <h:outputText value="#{msgs.dashboard_advsearch_but_text}" />
                       </h:commandLink>                               

                      </td>
                </tr>
               </h:form>                

            </table>
        </div>
   </td>
  </tr>

  <tr>
   <td> 
   <div class="dashboardHeadMessage"><h:outputLabel for="#{msgs.dashboard_reports_table_text}" value="#{msgs.dashboard_reports_table_text}"/></div>
        <div id="dashboardSummary" class="dashboardSummary">
            <h:form>
            <table border="0" cellspacing="0" cellpadding="4"> 
            <!--Caption Included-->            
                <!--Caption class = "euidHeadMessage"> Reports </caption-->
           <tr>
               <td>
                   <%
                     String mergeTab = "MERGE_RECORDS";
                     ValueExpression mergeValueExpression = ExpressionFactory.newInstance().createValueExpression(mergeTab, mergeTab.getClass());
                   %>
                 <h:commandLink  action="#{NavigationHandler.toReports}"
                            actionListener="#{ReportHandler.setReportsTabName}" > 
                            
                   <f:attribute name="tabName" value="<%=mergeValueExpression%>"/>
                    <h:outputText value="#{msgs.dashboard_reports_table_link1}"/>
                   </h:commandLink>
              </td>
         </tr>
                   <%
                       String deactTab = "DEACTIVATED_REPORT";
                       ValueExpression deactValueExpression = ExpressionFactory.newInstance().createValueExpression(deactTab, deactTab.getClass());
                    %>
              <tr>
                  <td>
                      <h:commandLink   action="#{NavigationHandler.toReports}"
                                    actionListener="#{ReportHandler.setReportsTabName}" >  
                        <f:attribute name="tabName" value="<%=deactValueExpression%>"/>
                        <h:outputText value="#{msgs.dashboard_reports_table_link2}"/>
                     </h:commandLink>
                  </td>
              </tr>                                     
            <%
                  String unmergedTab = "UNMERGED_RECORDS";
                   ValueExpression unmergedValueExpression = ExpressionFactory.newInstance().createValueExpression(unmergedTab, unmergedTab.getClass());
            %>
                
                <tr>
                    <td>
                    <h:commandLink  
                           action="#{NavigationHandler.toReports}"
                           actionListener="#{ReportHandler.setReportsTabName}" >  
                          <f:attribute name="tabName" value="<%=unmergedValueExpression%>"/>
                         <h:outputText value="#{msgs.dashboard_reports_table_link3}"/>
                    </h:commandLink>
                    </td>
                </tr>
            <tr><td><h:commandLink  action="#{NavigationHandler.toAuditLog}"><h:outputText value="#{msgs.dashboard_reports_table_link4}"/></h:commandLink></td></tr>
            </table>
           </h:form>
        </div>
   </td>
                
   <td> 
       <div class="dashboardHeadMessage">
           <h:outputLabel for="#{msgs.dashboard_compareeuid_table_text}" value="#{msgs.dashboard_compareeuid_table_text}"/>
       </div>
        
        <div id="qs" class="dashboardSummaryQS"> 
            <h:form id="compareform">
            <table border="0" cellspacing="0" cellpadding="4"> 
            <!--Caption included-->
                <!--Caption class = "euidHeadMessage"> Compare EUID's </caption-->                
                <tr>
                    <td><h:inputText  id="euid1Field" value="#{PatientDetailsHandler.euid1}" maxlength="10"/></td>
                    <td>
                        <nobr>
                            <h:commandLink  action="#{PatientDetailsHandler.lookupEuid1}">  
                                <h:outputText value="Lookup EUID1"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr><td><h:inputText  id="euid2Field" value="#{PatientDetailsHandler.euid2}" maxlength="10"/>
                    <td>
                        <nobr>
                            <h:commandLink   action="#{PatientDetailsHandler.lookupEuid2}">  
                                <h:outputText value="Lookup EUID2"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr><td><h:inputText  id="euid3Field" value="#{PatientDetailsHandler.euid3}" maxlength="10"/></td>
                    <td>
                        <nobr>
                            <h:commandLink  action="#{PatientDetailsHandler.lookupEuid3}">  
                                <h:outputText value="Lookup EUID3"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr>
                    <td><h:inputText  id="euid4Field" value="#{PatientDetailsHandler.euid4}" maxlength="10"/></td>
                    <td align="right" width="70px">
                        <nobr>
                            <h:commandLink  action="#{PatientDetailsHandler.lookupEuid4}">  
                                <h:outputText value="Lookup EUID4"/>
                            </h:commandLink>
                        </nobr>
                    </td>
                    <td align="right" width="70px">
                        <nobr>
                            <h:commandLink  action="#{PatientDetailsHandler.compareEuidSearch}">  
                                <h:outputText value="#{msgs.dashboard_compare_but_text}"/>
                           </h:commandLink>
                        </nobr>
                    </td>
                </tr>
            </table>
                        <table>
                            <tr>
                                <td valign="top" align="left" width="50%">
                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                </td>
                            </tr>
                        </table>   
            
            </h:form>
        </div>
   </td>
  </tr>
  
</table>
</div> <!-- End Main Content -->
                
        </body>
    </html>
</f:view>