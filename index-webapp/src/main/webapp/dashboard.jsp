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
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="javax.servlet.http.HttpServletRequest"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DashboardHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%
   
   //ajax related variables
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));
   

%>

     
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:view>
    
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
           <script type="text/javascript" src="scripts/edm.js"></script>

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

			<script>
				//not used in dashboard, but since its require in the script we fake it
				var editIndexid = ""; 
 				var rand = "";
				var popUrl;

				function setRand(thisrand)  {
					rand = thisrand;
				}
			</script>

		</head>
        <title><h:outputText value="#{msgs.application_heading}"/></title>   
        <body onload="javascript:setFocusOnFirstField(QuickSearchForm);">
       <%@include file="./templates/header.jsp"%>
 <%
   Operations operations=new Operations();
   String summaryText = bundle.getString("dashboard_summary_table_text");
   String lookupText = bundle.getString("dashboard_lookup_euid_table_text");
   String reportsText = bundle.getString("dashboard_reports_table_text");
   String compareText = bundle.getString("dashboard_compareeuid_table_text");
%>
       
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
    <td valign="top" align="left" >
	<div id="duplicateIdsDiv" class="ajaxalert"></div>
 	<div id="messages" class="ajaxalert">
	      <%if (request.getParameter("na") != null) {%>
		        <ul>				  
				   <li><%=request.getParameter("na") %></li>
				</ul>
		  <% }%>
	</div></td>
   </tr>

  <tr>
    
   <%HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
     DashboardHandler dashboardHandler = new DashboardHandler();
	 int countPotentialDuplicates  = dashboardHandler.getCountPotentialDuplicates();
	 int countAssumedMatches       = dashboardHandler.getCountAssumedMatches();
	  
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
                <tr><td> 
				<h:commandLink  title="#{msgs.dashboard_summary_table_link1}"  rendered="#{Operations.potDup_SearchView}"
				action="#{NavigationHandler.toDuplicateRecords}"> 
				      <h:outputText value="#{msgs.dashboard_summary_table_link1}"/>
					  <f:param name="where" value="dashboard" />
			    </h:commandLink> 
				<h:outputText rendered="#{Operations.potDup_SearchView}"  value=":"/> 
				      <b><h:commandLink  rendered="#{Operations.potDup_SearchView}" action="#{NavigationHandler.toDuplicateRecords}">
					  <f:param name="where" value="dashboard" />
					  <%=countPotentialDuplicates%> </h:commandLink> </b></td></tr>
                <tr><td> <h:commandLink  title="#{msgs.dashboard_summary_table_link4}" 
				rendered="#{Operations.assumedMatch_SearchView}"
				action="#{NavigationHandler.toAssumedMatches}"> 
				    <h:outputText value="#{msgs.dashboard_summary_table_link4}"/>
					   <f:param name="where" value="DBassumematches" />
					</h:commandLink> <h:outputText rendered="#{Operations.assumedMatch_SearchView}"  value=":"/> <b><h:commandLink  rendered="#{Operations.assumedMatch_SearchView}" action="#{NavigationHandler.toAssumedMatches}"> <%=countAssumedMatches%> 
					<f:param name="where" value="DBassumematches" />
					</h:commandLink></b></td></tr>
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
                    <td> <h:inputText id="euidField" title="EUID" value="#{DashboardHandler.singleEuid}"  maxlength="#{SourceHandler.euidLength}" /> </td>
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
                           <a href="javascript:void(0)" 
							 onclick="javascript:getFormValues('QuickSearchForm');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?pageName=dashboard.jsf&pageFrom=dashboard&singleEuidSearch=true&random='+rand+'&'+queryStr,'duplicateIdsDiv','')"						  class="button" 
						     title="<h:outputText value="#{msgs.search_button_label}"/>" >
							 <span><h:outputText value="#{msgs.search_button_label}"/></span></a>

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
                            <a href="javascript:void(0)"   
                             onclick="javascript:getFormValues('compareform');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?collectEuids=true&pageFrom=dashboard&compareEuids=true&random='+rand+'&'+queryStr,'duplicateIdsDiv','')"
							 class="button" 
						     title="<h:outputText value="#{msgs.dashboard_compare_but_text}"/>" >   
							 <span><h:outputText value="#{msgs.dashboard_compare_but_text}"/></span>
                      </a>
                    </td>
                </tr>
            </table>
            <input type="hidden" id="duplicateLid" title="duplicateLid" /> 
            </h:form>
        </div>
	<%}%>
   </td>
  </tr>

</table>
</div> <!-- End Main Content -->
<!-- Added  on 27-09-2008, to change alert pop up window to information pop up window -->
<div id="activeDiv" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('activeDiv',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="activemessageDiv"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td>
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:window.location = popUrl;" class="button" >
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </a>
							  </td>
							 </tr>
							 </table>
					     </td>
                     </tr> 
                 </table>
             </form>
         </div>

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


<!-- Added on 22-aug-2008 to incorparte with the functionality of back button in euiddetails.jsp  -->
 <%
    Enumeration parameterNames = request.getParameterNames();
  %>
    <%if(request.getParameter("back")!=null ){%>
     <script>
    <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);
		//replace the wild character
        attributeValue  = attributeValue.replaceAll("~~","%");
		if(request.getParameter("singleEuidSearch")!=null) {
   %>
    populateContents('QuickSearchForm','<%=attributeName%>','<%=attributeValue%>');
    <%} else if(request.getParameter("compareEuids")!=null){%>
	 populateContents('compareform','<%=attributeName%>','<%=attributeValue%>');
	<%}%>
   <%}%>
   </script>
   <%
 
   %>
   <%}%>
    </body>
	<script type="text/javascript">
     makeDraggable("activeDiv");
    </script>

    </html>
</f:view>
