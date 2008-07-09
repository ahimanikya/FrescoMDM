<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpServletRequest"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DashboardHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>

<%
   Operations operations=new Operations();
   ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
   String summaryText = bundle.getString("dashboard_summary_table_text");
   String lookupText = bundle.getString("dashboard_lookup_euid_table_text");
   String reportsText = bundle.getString("dashboard_reports_table_text");
   String compareText = bundle.getString("dashboard_compareeuid_table_text");
%>
     
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:view>
    
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
           <script type="text/javascript" src="scripts/edm.js"></script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title>   
        <body onload="javascript:setFocusOnFirstField(QuickSearchForm);">
       <%@include file="./templates/header.jsp"%>
       
<div id="mainContent">  <!-- Main content -->
<table border="0" cellpadding="5" cellspacing="5">
 <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top" align="left" > 
       <h:messages warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" /> 
 	</td>
  </tr>
   <tr>
    <td valign="top">&nbsp;</td>
    <td valign="top" align="left" ><div id="duplicateIdsDiv" class="ajaxalert"></div></td>
   </tr>

  <tr>
    
   <%HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     DashboardHandler dashboardHandler = new DashboardHandler();
	  
        int dashboadComponentDisplayCount = 0;
        
	 //Summary
   %>
   <%if(dashboardHandler.showSubscreenTab(summaryText) && (operations.isPotDup_SearchView() || operations.isAssumedMatch_SearchView())) {%>
   <td>
    <div class="dashboardHeadMessage"> <h:outputLabel for="#{msgs.dashboard_summary_table_text}" value="#{msgs.dashboard_summary_table_text}"/> </div>
         <div id="dashboardSummary" class="dashboardSummary"> 
          <h:form>
            <table border="0" cellspacing="10" cellpadding="4">
                <!--Caption included-->                
                <!--caption class="euidHeadMessage">Summary</caption-->
                <tr><td> <h:commandLink  title="#{msgs.dashboard_summary_table_link1}"  rendered="#{Operations.potDup_SearchView}"
				action="#{NavigationHandler.toDuplicateRecords}"> <h:outputText value="#{msgs.dashboard_summary_table_link1}"/></h:commandLink> <h:outputText rendered="#{Operations.potDup_SearchView}"  value=":"/> <b><h:commandLink  title="#{DashboardHandler.countPotentialDuplicates}"  rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}"><h:outputText value="#{DashboardHandler.countPotentialDuplicates} " /> </h:commandLink> </b></td></tr>
                <tr><td> <h:commandLink  title="#{msgs.dashboard_summary_table_link4}" 
				rendered="#{Operations.assumedMatch_SearchView}"
				action="#{NavigationHandler.toAssumedMatches}"> <h:outputText value="#{msgs.dashboard_summary_table_link4}"/></h:commandLink> <h:outputText rendered="#{Operations.assumedMatch_SearchView}"  value=":"/> <b><h:commandLink  title="#{DashboardHandler.countAssumedMatches}" rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"> <h:outputText value="#{DashboardHandler.countAssumedMatches} " /> </h:commandLink></b></td></tr>
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
            <table border="0" cellspacing="4" cellpadding="4" width="100%">
                <!--Caption included-->
                <!--Caption class = "euidHeadMessage">Quick Search</Caption-->
                <h:form id="QuickSearchForm">
                <tr><td colspan="2"> &nbsp;</td></tr>

                <tr>
                    <td> 
                        <h:outputText value="#{msgs.dashboard_quick_search_text}" />
                    </td>
                    <td> <h:inputText id="euidField" value="#{DashboardHandler.singleEuid}"  maxlength="#{SourceHandler.euidLength}" /> </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                      <td>
                       <h:commandLink  id="patlink"
					                   title="#{msgs.dashboard_advsearch_but_text}"
                                       rendered="#{Operations.EO_SearchViewSBR}"  
                                       action="#{NavigationHandler.toPatientDetails}">
                             <h:outputText value="#{msgs.dashboard_advsearch_but_text}" />
                       </h:commandLink>                               

                      </td>
                      <td>
                          <h:commandLink title="#{msgs.dashboard_search_but_text}" styleClass="button" action="#{DashboardHandler.singleEuidSearch}">
                              <span><h:outputText value="#{msgs.dashboard_search_but_text}" /></span>
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
   
   
   
   
  
      
   
     

   <%if(dashboardHandler.showSubscreenTab(reportsText) && ((operations.isReports_MergedRecords() || 
                operations.isReports_DeactivatedEUIDs() ||
                operations.isReports_UnmergedRecords() ||
                operations.isAuditLog_SearchView()
                ))) {%>
   <td>
    <div class="dashboardHeadMessage"><h:outputLabel for="#{msgs.dashboard_reports_table_text}" value="#{msgs.dashboard_reports_table_text}"/></div>
        <div id="dashboardSummary" class="dashboardSummary">
            <h:form>
            <table border="0" cellspacing="4" cellpadding="4"> 
            <!--Caption Included-->            
                <!--Caption class = "euidHeadMessage"> Reports </caption-->
           <tr>
               <td>
                   <%
                     String mergeTab = "MERGE_RECORDS";
                     ValueExpression mergeValueExpression = ExpressionFactory.newInstance().createValueExpression(mergeTab, mergeTab.getClass());
                   %>
                   <h:commandLink title="#{msgs.dashboard_reports_table_link1}" action="#{NavigationHandler.toReports}"  
                                  rendered="#{Operations.reports_MergedRecords}" 
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
                      <h:commandLink   title="#{msgs.dashboard_reports_table_link2}" action="#{NavigationHandler.toReports}" 
                                    rendered="#{Operations.reports_DeactivatedEUIDs}"
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
                    <h:commandLink title="#{msgs.dashboard_reports_table_link3}" 
                           action="#{NavigationHandler.toReports}"
                           rendered="#{Operations.reports_UnmergedRecords}"
                           actionListener="#{ReportHandler.setReportsTabName}" >  
                          <f:attribute name="tabName" value="<%=unmergedValueExpression%>"/>
                         <h:outputText value="#{msgs.dashboard_reports_table_link3}"/>
                    </h:commandLink>
                    </td>
                </tr>
            <tr><td><h:commandLink  title="#{msgs.dashboard_reports_table_link4}" action="#{NavigationHandler.toAuditLog}" 
									rendered="#{Operations.auditLog_SearchView}">
									<h:outputText value="#{msgs.dashboard_reports_table_link4}"/></h:commandLink></td></tr>
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
            <table border="0" cellspacing="4" cellpadding="4" width="100%"> 
            <!--Caption included-->
                <!--Caption class = "euidHeadMessage"> Compare EUID's </caption-->                
                <tr>
                     <td><h:outputText value="#{msgs.EUID1}"/></td>
                    <td><h:inputText  id="euid1Field" title="EUID 1" onblur="javascript:checkDuplicateFileds('compareform',this,'#{msgs.already_found_error_text}')" value="#{DashboardHandler.euid1}" maxlength="#{SourceHandler.euidLength}" /></td>
                     <td>&nbsp;</td>
               </tr>
                <tr>
                     <td><h:outputText value="#{msgs.EUID2}"/></td>
				    <td><h:inputText  id="euid2Field" title="EUID 2" onblur="javascript:checkDuplicateFileds('compareform',this,'#{msgs.already_found_error_text}')" value="#{DashboardHandler.euid2}" maxlength="#{SourceHandler.euidLength}" />
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><h:outputText value="#{msgs.EUID3}"/></td>
				    <td><h:inputText  id="euid3Field" title="EUID 3" onblur="javascript:checkDuplicateFileds('compareform',this,'#{msgs.already_found_error_text}')" value="#{DashboardHandler.euid3}" maxlength="#{SourceHandler.euidLength}" /></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><h:outputText value="#{msgs.EUID4}"/></td>
                    <td><h:inputText  id="euid4Field"  title="EUID 4"  onblur="javascript:checkDuplicateFileds('compareform',this,'#{msgs.already_found_error_text}')" value="#{DashboardHandler.euid4}" maxlength="#{SourceHandler.euidLength}" /></td>
                    <td align="left">
                             <h:commandLink  title="#{msgs.dashboard_compare_but_text}" action="#{DashboardHandler.compareEuidSearch}" styleClass="button">  
                                <span><h:outputText value="#{msgs.dashboard_compare_but_text}"/></span>
                           </h:commandLink>
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
