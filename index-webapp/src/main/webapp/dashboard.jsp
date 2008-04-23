<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpServletRequest"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DashboardHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>


<%
   ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
   String summaryText = bundle.getString("dashboard_summary_table_text");
   String lookupText = bundle.getString("dashboard_lookup_euid_table_text");
   String reportsText = bundle.getString("dashboard_reports_table_text");
   String compareText = bundle.getString("dashboard_compareeuid_table_text");
%>
    
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
        <body onload="javascript:setFocusOnFirstField(QuickSearchForm);">
       <%@include file="./templates/header.jsp"%>
       
<div id="mainContent">  <!-- Main content -->
<table border="0" cellpadding="3" cellspacing="1">
  <tr>
    
   <%HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     DashboardHandler dashboardHandler = new DashboardHandler();
	 System.out.println("=====>" + dashboardHandler + "dashboardHandler.showSubscreenTab(summaryText)" + dashboardHandler.showSubscreenTab(summaryText));
         
        System.out.println("=====>" + dashboardHandler + "dashboardHandler.showSubscreenTab(summaryText)" + dashboardHandler.showSubscreenTab(summaryText));
        
        
        boolean check1=dashboardHandler.showSubscreenTab(summaryText);
        boolean check2=dashboardHandler.showSubscreenTab(lookupText);
        boolean check3=dashboardHandler.showSubscreenTab(reportsText);
        boolean check4=dashboardHandler.showSubscreenTab(compareText);
        
       
      
        int dashboadComponentDisplayCount = 0;
        
	 //Summary
   %>
   <%if(dashboardHandler.showSubscreenTab(summaryText)) {%>
   <td>
    <div class="dashboardHeadMessage"> <h:outputLabel for="#{msgs.dashboard_summary_table_text}" value="#{msgs.dashboard_summary_table_text}"/> </div>
         <div id="dashboardSummary" class="dashboardSummary"> 
          <h:form>
            <table border="0" cellspacing="10" cellpadding="4">
                <!--Caption included-->                
                <!--caption class="euidHeadMessage">Summary</caption-->
                <tr><td> <h:commandLink  action="#{NavigationHandler.toDuplicateRecords}"> <h:outputText value="#{msgs.dashboard_summary_table_link1}"/></h:commandLink> : <b><h:commandLink  action="#{NavigationHandler.toDuplicateRecords}"><h:outputText value="#{DashboardHandler.countPotentialDuplicates} " /> </h:commandLink> </b></td></tr>
                <tr><td> <h:commandLink  action="#{NavigationHandler.toAssumedMatches}"> <h:outputText value="#{msgs.dashboard_summary_table_link4}"/></h:commandLink> : <b><h:commandLink  action="#{NavigationHandler.toAssumedMatches}"> <h:outputText value="#{DashboardHandler.countAssumedMatches} " /> </h:commandLink></b></td></tr>
            </table>
          </h:form>
         </div>
        </td>
	<%
        dashboadComponentDisplayCount++;
        if (dashboadComponentDisplayCount%2 == 0){
            %>
                </tr> <tr>
            <%
        }
    }%>
   
   
   
   <%if(dashboardHandler.showSubscreenTab(lookupText)) {%>
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
                    <td> <h:inputText id="euidField" value="#{DashboardHandler.euid1}"  maxlength="#{SourceHandler.euidLength}" /> </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>

                <tr>
                      <td>
                          <h:commandLink  action="#{DashboardHandler.lookupEuid1}">
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
        <%
        dashboadComponentDisplayCount++;
        if (dashboadComponentDisplayCount%2 == 0){
            %>
                </tr> <tr>
            <%
        }
        }%>
   
   
   
   
  
      
   
     

   <%if(dashboardHandler.showSubscreenTab(reportsText)) {%>
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
        <%
        dashboadComponentDisplayCount++;
        if (dashboadComponentDisplayCount%2 == 0){
            %>
                </tr> <tr>
            <%
        }
        }%>
   
                
   <td> 
   <%if(dashboardHandler.showSubscreenTab(compareText)) {%>
       <div class="dashboardHeadMessage">
           <h:outputLabel for="#{msgs.dashboard_compareeuid_table_text}" value="#{msgs.dashboard_compareeuid_table_text}"/>
       </div>
        
        <div id="qs" class="dashboardSummaryQS"> 
            <h:form id="compareform">
            <table border="0" cellspacing="0" cellpadding="4"> 
            <!--Caption included-->
                <!--Caption class = "euidHeadMessage"> Compare EUID's </caption-->                
                <tr>
                    <td><h:inputText  id="euid1Field" value="#{DashboardHandler.euid1}" maxlength="#{SourceHandler.euidLength}" /></td>
                    <td>
                        <nobr>
                            <h:commandLink  action="#{DashboardHandler.lookupEuid1}">  
                                 <h:outputText value="#{msgs.dashboard_lookup_euid1}"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr><td><h:inputText  id="euid2Field" value="#{DashboardHandler.euid2}" maxlength="#{SourceHandler.euidLength}" />
                    <td>
                        <nobr>
                            <h:commandLink   action="#{DashboardHandler.lookupEuid2}">  
                                <h:outputText value="#{msgs.dashboard_lookup_euid2}"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr><td><h:inputText  id="euid3Field" value="#{DashboardHandler.euid3}" maxlength="#{SourceHandler.euidLength}" /></td>
                    <td>
                        <nobr>
                            <h:commandLink  action="#{DashboardHandler.lookupEuid3}">  
                                 <h:outputText value="#{msgs.dashboard_lookup_euid3}"/> 
                            </h:commandLink>                        
                        </nobr>
                    </td>
                </tr>
                <tr>
                    <td><h:inputText  id="euid4Field" value="#{DashboardHandler.euid4}" maxlength="#{SourceHandler.euidLength}" /></td>
                    <td align="right" width="66px">
                        <nobr>
                            <h:commandLink  action="#{DashboardHandler.lookupEuid4}">  
                                  <h:outputText value="#{msgs.dashboard_lookup_euid4}"/> 
                            </h:commandLink>
                        </nobr>
                    </td>
                    <td align="right" width="70px">
                        <nobr>
                            <h:commandLink  action="#{DashboardHandler.compareEuidSearch}">  
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
	<%}%>
   </td>
  </tr>
  
</table>
</div> <!-- End Main Content -->
<script>
    function setFocusOnFirstField(formName) {
         if( formName.elements[0]!=null) {
		var i;
		var max = formName.length;
		for( i = 0; i < max; i++ ) {
			if( formName.elements[ i ].type != "hidden" &&
				!formName.elements[ i ].disabled &&
				!formName.elements[ i ].readOnly ) {
				formName.elements[ i ].focus();
				break;
			}
		}
      }         
    }
</script>                
        </body>
    </html>
</f:view>
