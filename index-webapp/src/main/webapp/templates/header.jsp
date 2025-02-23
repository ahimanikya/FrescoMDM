<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<!-- 
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
  -->
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="javax.faces.application.FacesMessage"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>


<script>
 var enter_date="";
 var format_text = "";
 var not_supported = "";
 var date_text = "";
 var invalid_day_of_month = "";
 var invalid_month = "";
 var tabTitles=[];
 var unsavedEditMinorObjectType = '';
 var unsavedRootNodeValues='';
 var unsavedEuidEditMinorObjectType='';
 var unsavedEuidEditObjectType='';
	// setting values when putting edit euid minor objects in to edit mode
	function setEuidEditMinorObjectAddressType(MinorObjectType,objectType){
		unsavedEuidEditMinorObjectType=MinorObjectType;
		unsavedEuidEditObjectType=objectType;
	}

	//unsaved root node fileds (sourcerecords edit) alert
	function showUnSavedRootNodeTabAlert(thisEvent){
			document.getElementById("unsavedMessageTopTabDiv").innerHTML = "<h:outputText value="#{msgs.unsaved_root_node_message}"/>";
		    showExtraTopTabDivs("unsavedTopTabDiv",thisEvent);
	}
	// added on 08-12-08 as fix of 221 and 219	//unsaved edit euid minorobject alert
	function showUnSavedEditEuidTopTabAlert(thisEvent,unsavedEuidEditMinorObjectType,unsavedEuidEditObjectType){
		 if(unsavedEuidEditObjectType.length == 0) {
			document.getElementById("unsavedMessageTopTabDiv").innerHTML = '<h:outputText value="#{msgs.unsaved_message_part_I}"/> \''+unsavedEuidEditMinorObjectType+'\'<h:outputText value="#{msgs.unsaved_message_part_III}"/>';
		     showExtraTopTabDivs("unsavedTopTabDiv",thisEvent);

		 } else {
			document.getElementById("unsavedMessageTopTabDiv").innerHTML = '<h:outputText value="#{msgs.unsaved_message_part_I}"/> \' '+unsavedEuidEditMinorObjectType+' \' <h:outputText value="#{msgs.unsaved_message_part_II}"/> ('+unsavedEuidEditObjectType+') <h:outputText value="#{msgs.unsaved_message_part_III}"/>';
 		    showExtraTopTabDivs("unsavedTopTabDiv",thisEvent);
		}
	}
	
	//unsaved source record edit/add minor object alert
	function showUnSavedTopTabAlert(thisEvent,unsavedEditMinorObjectType){
		document.getElementById("unsavedMessageTopTabDiv").innerHTML = "<h:outputText value="#{msgs.unsaved_message_part_I}"/> '"+unsavedEditMinorObjectType+"' <h:outputText value="#{msgs.unsaved_message_part_III}"/>";
		showExtraTopTabDivs("unsavedTopTabDiv",thisEvent);
	 }
	
	// added on 08-12-08 as fix of 221 and 219
	function showExtraTopTabDivs(divId,thisEvent)  {
		var y;
		var x;   
		if(document.getElementById(divId).style.visibility == 'hidden') {
			document.getElementById(divId).style.visibility = "visible";
			document.getElementById(divId).style.display = "block";
			if (thisEvent.pageX || thisEvent.pageY) {
				x = thisEvent.pageX;
				y = thisEvent.pageY;
			} else if (thisEvent.clientX || thisEvent.clientY) {
			   x = thisEvent.clientX + document.body.scrollLeft;
			   y = thisEvent.clientY + document.body.scrollTop;
			}
			 document.getElementById(divId).style.top = y+30;
			 document.getElementById(divId).style.left = x;
			} else {
		   document.getElementById(divId).style.visibility = "hidden";
		   document.getElementById(divId).style.display = "none";
		  }
	}

</script>
<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
<%@ page isErrorPage="false" errorPage="../error500.jsp" %>

<%NavigationHandler navigationHandler = new NavigationHandler();%>

<%
Operations operationSecurityCheck = new Operations();
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
Operations operationsGlobal = null;
HttpSession sessionFacesGlobal = null;
sessionFacesGlobal = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
operationsGlobal = new Operations();
sessionFacesGlobal.setAttribute("Operations",operationsGlobal);
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
boolean allowed = false;
// Permissions Map:This map defines the relationship between the MIDM xml tag names and the 
// files associated with the functionality of the tag
// The permissions are based on the MIDM tag name
HashMap tagNameScreenNameMap = new HashMap();
tagNameScreenNameMap.put("dashboard","dashboard.jsp");
tagNameScreenNameMap.put("duplicate-records","duplicaterecords.jsp,compareduplicates.jsp");
tagNameScreenNameMap.put("record-details","recorddetails.jsp,euiddetails.jsp,compareduplicates.jsp");
tagNameScreenNameMap.put("assumed-matches","assumedmatches.jsp,ameuiddetails.jsp");
tagNameScreenNameMap.put("source-record","sourcerecords.jsp");
tagNameScreenNameMap.put("reports","reports.jsp");
tagNameScreenNameMap.put("transactions","transactions.jsp,transeuiddetails.jsp");
tagNameScreenNameMap.put("audit-log","auditlog.jsp");
String refererPage = request.getHeader("referer");

// fix for 6679172,6684209
if(session!=null && session.isNew()) {	
	//session.invalidate();
%>
   <c:redirect url="login.jsf"/>
<%}%>

<%if(session.getAttribute("ScreenObject") == null  ) {%>
   <c:redirect url="login.jsf"/>
<%}%>

<%if(session.getAttribute("user") == null  ) {%>
   <c:redirect url="login.jsf"/>
<%}%>
<table><table><tr><td><script>
	enter_date ="<%=bundle.getString("enter_date")%>";	
	format_text="<%=bundle.getString("format")%>";
	not_supported = "<%=bundle.getString("not_supported")%>";
    date_text = "<%=bundle.getString("date")%>";
    invalid_day_of_month = "<%=bundle.getString("invalid_day_of_month")%>";
    invalid_month = "<%=bundle.getString("invalid_month")%>";

 </script></td></tr></table></table>
<%
ConfigManager.init();

String uri = request.getRequestURI();
String requestPage = uri.substring(uri.lastIndexOf("/")+1,uri.length());
         ArrayList headerTabsLabelsList = ConfigManager.getInstance().getAllScreenObjects();                    
		 Object[] headerTabsLabelsListObj = headerTabsLabelsList.toArray();
		 ScreenObject allScreensArrayOrdered[] = new ScreenObject[headerTabsLabelsListObj.length];         
		 String headerClassName = "";		
                   // modfied by Anil , fix for MIDM security Isssue
		 for(int aIndex = 0 ;aIndex <headerTabsLabelsListObj.length; aIndex++) {
		    ScreenObject screenObjectLocal = (ScreenObject) headerTabsLabelsListObj[aIndex];                   
                    if (screenObjectLocal!=null){
                     String screenName = ConfigManager.getInstance().getScreenObjectTagName(screenObjectLocal.getID().toString());
                     if (screenName.equalsIgnoreCase("assumed-matches")){
                         if(!operationSecurityCheck.isAssumedMatch_SearchView()){
                             screenObjectLocal = null;
                         }
                     }
                      if (screenName.equalsIgnoreCase("transactions")){
                         if(!operationSecurityCheck.isTransLog_SearchView()){
                             screenObjectLocal = null;
                         }
                     }
                     if (screenName.equalsIgnoreCase("audit-log")){
                         if(!operationSecurityCheck.isAuditLog_SearchView()){
                             screenObjectLocal = null;
                         }
                     }
                     if (screenName.equalsIgnoreCase("record-details")){
                         if(!operationSecurityCheck.isEO_SearchViewSBR()){
                             screenObjectLocal = null;
                         }
                     }
                      if (screenName.equalsIgnoreCase("duplicate-records")){
                         if(!operationSecurityCheck.isPotDup_SearchView()){
                             screenObjectLocal = null;
                         }
                     }
					 if (screenName.equalsIgnoreCase("reports")){
                         if(!operationSecurityCheck.isReports_MergedRecords()&& !operationSecurityCheck.isReports_DeactivatedEUIDs() &&
							 !operationSecurityCheck.isReports_UnmergedRecords() && !operationSecurityCheck.isReports_Updates() && !operationSecurityCheck.isReports_Activity() && !operationSecurityCheck.isReports_Duplicates()&& !operationSecurityCheck.isReports_AssumedMatches()){
                             screenObjectLocal = null;
                         }
                     }
					 if (screenName.equalsIgnoreCase("source-record")){
                         if(!operationSecurityCheck.isSO_SearchView() && !operationSecurityCheck.isSO_Add() && !operationSecurityCheck.isSO_Merge()){
                             screenObjectLocal = null;
                         }
                     }
                    }                
                if (screenObjectLocal!=null){
                     allScreensArrayOrdered[screenObjectLocal.getDisplayOrder()] = screenObjectLocal;
                }
		 }
//System.out.println("Accessable tabs --> " + allScreensArrayOrdered.length );

//to handle the null pointer Exception bug#66
//Added logic to check the URL hacking to fix the bug # 17
//**Build the list of permitted screens
ArrayList permittedScreens = new ArrayList();
for (int i=0;i<allScreensArrayOrdered.length;i++)    {
   if (allScreensArrayOrdered[i] != null)  {
       permittedScreens.add(allScreensArrayOrdered[i]);
   }
}
   //System.out.println(" requestPage " + requestPage);
for (int i=0;i<permittedScreens.size();i++)    {
	String permittedTagName = navigationHandler.getTagNameByScreenId(((ScreenObject)permittedScreens.get(i)).getID());
	String permittedScreenName[] = ((String)tagNameScreenNameMap.get(permittedTagName)).split(",");
	//Check if the requested page is within the permitted tabs
	for (int j = 0; j<permittedScreenName.length;j++ )    {
            //System.out.println("permittedTagName  ->>" + permittedTagName  + " permittedScreenName " + permittedScreenName[j]);	   
	     if (permittedScreenName[j].equalsIgnoreCase(requestPage))    {		   
		        allowed = true;  // Access allowed (valid request)
			    session.setAttribute("ScreenObject", navigationHandler.getScreenObject(permittedTagName));
	     }
         if(allowed) break;
	 }
}

//User has no permission to any tabs,No access to MIDM
if(permittedScreens.size() == 0 ) {  
    FacesContext.getCurrentInstance().getExternalContext().redirect("loginerror.jsf?error="+bundle.getString("no_midm_access"));
} 

%>

<%   
    boolean initialScreenAllowed = false;	
 	if (!allowed)  {
        ScreenObject initialScreenObject = ConfigManager.getInstance().getInitialScreen();		
		String initialTagName  = navigationHandler.getTagNameByScreenId(((ScreenObject)initialScreenObject).getID());
		for (int i=0;i<permittedScreens.size();i++)    {
			String thisTagName  = navigationHandler.getTagNameByScreenId(((ScreenObject)permittedScreens.get(i)).getID());
			if (thisTagName.equalsIgnoreCase(initialTagName))    {
				initialScreenAllowed = true; //Initial screen configuration is fine				
				break;
			}
		} 
%>
 
<%      
	  
       //Check if the initial configuration is wrongly configured
	    if (!initialScreenAllowed )    {
		    if((null != refererPage && refererPage.endsWith("login.jsf")))    {
				   //If he has come from the login screen, take him to dashboard with a configuration message
                   ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("dashboard");
                   session.setAttribute("ScreenObject", screenObjectDashboard);			
			       FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.jsf?na="+bundle.getString("initial_screen_error"));
			} else {
				    //user has hacked the URL, however update the correct screen object 
					//so that any delay in redirect should not cause Null Pointer Exception
			        if ("recorddetails.jsp".equalsIgnoreCase(requestPage) || 
						"euiddetails.jsp".equalsIgnoreCase(requestPage))    {
                         session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));			
 					}  else if ("dashboard.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("dashboard");
                         session.setAttribute("ScreenObject", screenObjectDashboard);			
                    }  else if ("duplicaterecords.jsp".equalsIgnoreCase(requestPage) )    {
						  session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
					}  else if ("compareduplicates.jsp".equalsIgnoreCase(requestPage))    {
                          if(request.getParameter("euids") != null) {
                            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));			
						 } else {
                            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
						 }

					}  else if ("assumedmatches.jsp".equalsIgnoreCase(requestPage) || 
						        "ameuiddetails.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("assumed-matches");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("sourcerecords.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("source-record");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("reports.jsp".equalsIgnoreCase(requestPage))    {						
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("reports");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("transeuiddetails.jsp".equalsIgnoreCase(requestPage) || 
						        "transactions.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("transactions");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("auditlog.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("audit-log");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}
			        // BOOOOOOOOOOOOOO!! throw em' out
				
                    FacesContext.getCurrentInstance().getExternalContext().redirect("loginerror.jsf?na="+bundle.getString("not_authorized"));
			}
       }  else {
				    //user has hacked the URL, however update the correct screen object 
					//so that any delay in redirect should not cause Null Pointer Exception
			        if ("recorddetails.jsp".equalsIgnoreCase(requestPage) || 
						"euiddetails.jsp".equalsIgnoreCase(requestPage) )    {
                          ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("record-details");
                         session.setAttribute("ScreenObject", screenObjectDashboard);			
					}  else if ("dashboard.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("dashboard");
                         session.setAttribute("ScreenObject", screenObjectDashboard);			
					}  else if ("duplicaterecords.jsp".equalsIgnoreCase(requestPage) )    {
						  session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
					}  else if ("compareduplicates.jsp".equalsIgnoreCase(requestPage))    {
                          if(request.getParameter("euids") != null) {
                            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));			
						 } else {
                             session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
						 }
					}  else if ("assumedmatches.jsp".equalsIgnoreCase(requestPage) || 
						        "ameuiddetails.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("assumed-matches");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("sourcerecords.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("source-record");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("reports.jsp".equalsIgnoreCase(requestPage))    {						
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("reports");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("transeuiddetails.jsp".equalsIgnoreCase(requestPage) || 
						        "transactions.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("transactions");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("auditlog.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("audit-log");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}
			        // BOOOOOOOOOOOOOO!! throw em' out
				
                    FacesContext.getCurrentInstance().getExternalContext().redirect("loginerror.jsf?na="+bundle.getString("not_authorized"));
	   }
   } else {

	   			     if ("recorddetails.jsp".equalsIgnoreCase(requestPage) || 
						"euiddetails.jsp".equalsIgnoreCase(requestPage) )    {
                          ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("record-details");
                         session.setAttribute("ScreenObject", screenObjectDashboard);			
					}  else if ("dashboard.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("dashboard");
                         session.setAttribute("ScreenObject", screenObjectDashboard);			
					}  else if ("duplicaterecords.jsp".equalsIgnoreCase(requestPage))    {
                            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
 					}  else if ("compareduplicates.jsp".equalsIgnoreCase(requestPage))    {
                          if(request.getParameter("euids") != null) {
                             session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));			
						 } else {
                             session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));			
						 }
					}  else if ("assumedmatches.jsp".equalsIgnoreCase(requestPage) || 
						        "ameuiddetails.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("assumed-matches");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("sourcerecords.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("source-record");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("reports.jsp".equalsIgnoreCase(requestPage))    {						
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("reports");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("transeuiddetails.jsp".equalsIgnoreCase(requestPage) || 
						        "transactions.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("transactions");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}  else if ("auditlog.jsp".equalsIgnoreCase(requestPage))    {
                         ScreenObject  screenObjectDashboard = navigationHandler.getScreenObject("audit-log");
                         session.setAttribute("ScreenObject", screenObjectDashboard);									
					}

   }
   
   //Get the updated Session Object based on the user browser navigation
   screenObject = (ScreenObject) session.getAttribute("ScreenObject");

   
   %>
 
<%
String global_daysOfWeek  = bundle.getString("global_daysOfWeek");
String global_months = bundle.getString("global_months");
String cal_prev_text = bundle.getString("cal_prev_text");
String cal_next_text = bundle.getString("cal_next_text");
String cal_today_text = bundle.getString("cal_today_text");
String cal_month_text = bundle.getString("cal_month_text");
String cal_year_text = bundle.getString("cal_year_text");
String  dateFormat = ConfigManager.getDateFormat();
%>
      <!--Skip to main Content added-->
    <a href="#mainContent"><img src="images/spacer.gif" border="0" height="0" width= "0" alt="Skip to main Content"></a>


 <div id="menuDiv">
     <table width="100%" cellpadding="0" cellspacing="0" border="0" >
         <h:form>   
             <tr>
                 <td align="left">
  				     <img src='images/EDM-logo.png' alt="Master Index Data Manager" title="Master Index Data Manager" />
                 </td>
                 <td align="right">
                     <% if (session.getAttribute("user") != null) {%>
                     <span class="greetingsmall"><font color="blue"><nobr><%=session.getAttribute("user")%></font>&nbsp;</span>
                     <h:commandLink title="#{msgs.header_logout_prompt}" action="#{LoginHandler.signOutUser}" styleClass="greetingsmall">
                         <font color="blue"><h:outputText  value="#{msgs.header_logout_prompt}" /></font>
                     </h:commandLink>                     
                     <%}%>             
                     <img src='images/sun-logo.png' alt="Sun Microsystems" title="Sun Microsystems"/>
                 </td>
             </tr>    
         </h:form>   
		 <!--Start dynamic header content code here -->
          
		  <tr>
             <td colspan="2">
                 <div id="header">
                                  <h:form id="tabsForm" title="tabsForm">
								 <div id="activeHeaders" style="visibility:visible;display:block">
                                 <%                                  
                                 for(int i=0;i<allScreensArrayOrdered.length;i++){  
                                  %> 
                                  <% 
                                  if (allScreensArrayOrdered[i]!=null){%>
								  <script>
									  tabTitles.push("<%=allScreensArrayOrdered[i].getDisplayTitle()%>");
								  </script>

                                    <%ValueExpression screenID = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getID(), allScreensArrayOrdered[i].getID().getClass());
                                    ValueExpression displayTitleVE = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getDisplayTitle(), allScreensArrayOrdered[i].getDisplayTitle().getClass());                                    
                                  %>
									  <%if(screenObject.getDisplayTitle().equalsIgnoreCase(allScreensArrayOrdered[i].getDisplayTitle())) {%>

										   <h:commandLink title="<%=displayTitleVE%>" styleClass ="navbuttonselected" 
										   onclick="javascript:highlighTabs('tabsForm',event)"
													  actionListener="#{NavigationHandler.setHeaderByTabName}" > 
											  <f:attribute name="screenId" value="<%=screenID%>"/>
											  <span id="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" title="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" ><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
										  </h:commandLink>
									 <%} else {%>
										   <h:commandLink title="<%=displayTitleVE%>" styleClass ="navbutton" 
										    onclick="javascript:highlighTabs('tabsForm',event)"
													  actionListener="#{NavigationHandler.setHeaderByTabName}" > 
											  <f:attribute name="screenId" value="<%=screenID%>"/>
											  <span id="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" title="<%=allScreensArrayOrdered[i].getDisplayTitle()%>"><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
										  </h:commandLink>
									 <%}%>
                                 <%}%>
								<%}%>
								 </div>
								 <div id="inactiveHeaders" style="visibility:hidden;display:none">
                                 <%                                  
                                 for(int i=0;i<allScreensArrayOrdered.length;i++){  
                                  %> 
                                  <% 
                                  if (allScreensArrayOrdered[i]!=null){%>
								  <script>
									  tabTitles.push("<%=allScreensArrayOrdered[i].getDisplayTitle()%>");
								  </script>

                                    <%ValueExpression screenID = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getID(), allScreensArrayOrdered[i].getID().getClass());
                                    ValueExpression displayTitleVE = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getDisplayTitle(), allScreensArrayOrdered[i].getDisplayTitle().getClass());                                    
                                  %>
									<%if(screenObject.getDisplayTitle().equalsIgnoreCase(allScreensArrayOrdered[i].getDisplayTitle())) {%>
										 <a href="#" title="<%=displayTitleVE%>" class ="navbuttonselected" 
											   onclick="javascript:if(unsavedEditMinorObjectType.length>0){
											                         showUnSavedTopTabAlert(event,unsavedEditMinorObjectType);}
																   else if(unsavedEuidEditMinorObjectType.length>0){
																       showUnSavedEditEuidTopTabAlert(thisEvent,unsavedEuidEditMinorObjectType,unsavedEuidEditObjectType);}
																    else if(unsavedRootNodeValues.length>0){
												                       showUnSavedRootNodeTabAlert(event);}"> 
 												  <span id="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" title="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" ><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
										</a>
									<%}else{%>
										   <a href="#" class ="navbutton" title = "<%=allScreensArrayOrdered[i].getDisplayTitle()%>"
											   onclick="javascript:if(unsavedEditMinorObjectType.length>0){
											                         showUnSavedTopTabAlert(event,unsavedEditMinorObjectType);}
											                       else if(unsavedEuidEditMinorObjectType.length>0){
																       showUnSavedEditEuidTopTabAlert(thisEvent,unsavedEuidEditMinorObjectType,unsavedEuidEditObjectType);}
																   else if(unsavedRootNodeValues.length>0){
												                       showUnSavedRootNodeTabAlert(event);}"> 
 											  <span id="<%=allScreensArrayOrdered[i].getDisplayTitle()%>">
											  <%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
										  </a>
									<%}%>
								  <%}%>
								<%}%>
								 </div>
                                  </h:form>

				 </div>
		     </td>
		 </tr>
         <tr>
             <td width="100%" colspan="2"><div class="blueline">&nbsp;</div></td>
         </tr>
     </table>    

<script>

function highlighTabs(formName,thisEvent)   {

  var clickedTabTitle = (thisEvent.target != null?thisEvent.target.parentNode.title:thisEvent.srcElement.parentElement.title);  
	//alert("ssss" + clickedTabTitle);

  for(i=0; i< tabTitles.length; i++)   {      
			if(tabTitles[i] == clickedTabTitle ) {
				//thisEvent.target.parentElement.style.className = "navbuttonselected"
				if (document.getElementById(tabTitles[i]).parentNode != null)	 {
					selectedId  = document.getElementById(tabTitles[i]).parentNode
				} else {
					selectedId = document.getElementById(tabTitles[i]).parentElement;
				}				
				selectedId.className = "navbuttonselected"
	    	} else {
				var notSelectedId = document.getElementById(tabTitles[i]).parentNode;
				notSelectedId.className = "navbutton";
			}
    }
}
var yui_prev_var  = "<%=bundle.getString("yui_dt_prev_text")%>";
var yui_next_var  = "<%=bundle.getString("yui_dt_next_text")%>";
var yui_first_var  = "<%=bundle.getString("yui_dt_first_text")%>";
var yui_last_var  = "<%=bundle.getString("yui_dt_last_text")%>";
makeDraggable("unsavedTopTabDiv");
</script>
</div>
  		 <div id="unsavedTopTabDiv" class="confirmPreview" style="top:400px;left:500px;visibility:hidden;display:none;">
               <form id="unsavedDivForm">
                <table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<th align="center" title="<%=bundle.getString("move")%>"><%=bundle.getString("popup_information_text")%></th>
				<th>
				<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedTopTabDiv',event);"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>

                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('unsavedTopTabDiv',event);"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
				</th>
				</tr>
                    <tr><td colspan="2">&nbsp;</td></tr>    
					<tr>
						<td colspan="2">
							<b><div id="unsavedMessageTopTabDiv"></div></b>
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>    
					<tr id="actions">
					  <td colspan="2" align="center">
					    <table align="center">
							<tr>
								<td>
									<a  class="button"  href="javascript:void(0)" title="<h:outputText value="#{msgs.ok_text_button}" />" onclick="javascript:showExtraDivs('unsavedTopTabDiv',event);">                          
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

	    <div id="validate_date_div" class="confirmPreview" style="top:175px;left:400px;visibility:hidden;display:none;">
             <form id="activeMerge" name="activeMerge" >
                 <table cellspacing="0" cellpadding="0" border="0">
 					 <tr>
					     <th title="<%=bundle.getString("move")%>">&nbsp;<h:outputText value="#{msgs.popup_information_text}"/></th> 
					     <th>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('validate_date_div',event)"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
							<a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:showExtraDivs('validate_date_div',event)"><img src="images/close.gif" border="0" alt="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>"/></a>
						</th>
					  </tr>
					 <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" ><b><div id="validate_date_message_div"></div></b></td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr id="actions">
                         <td colspan="2" border="2"  align="right" valign="top" >
                            <table align="center">
						      <tr>
							 <td>&nbsp;</td>
							 <td>
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)" class="button"
                                onclick="javascript:showExtraDivs('validate_date_div',event);">
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
  makeDraggable("validate_date_div");
</script>