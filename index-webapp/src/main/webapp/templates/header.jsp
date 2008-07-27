<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<script>
 var tabTitles=[];
</script>
<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
<%@ page isErrorPage="false" errorPage="../error500.jsp" %>

<%
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
Operations operationsGlobal = null;
HttpSession sessionFacesGlobal = null;
sessionFacesGlobal = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
operationsGlobal = new Operations();
sessionFacesGlobal.setAttribute("Operations",operationsGlobal);
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

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

<%
ConfigManager.init();

String uri = request.getRequestURI();
String requestPage = uri.substring(uri.lastIndexOf("/")+1,uri.length());
//Added by Sridhar to handle the null pointer Exception bug#66
NavigationHandler navigationHandler = new NavigationHandler();                                 
if(requestPage.equalsIgnoreCase("recorddetails.jsp")) { 
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
} else if(requestPage.equalsIgnoreCase("assumedmatches.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("assumed-matches"));	
}else if(requestPage.equalsIgnoreCase("transactions.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("transactions"));	
}else if(requestPage.equalsIgnoreCase("duplicaterecords.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("duplicate-records"));	
}else if(requestPage.equalsIgnoreCase("reports.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("reports"));	
}else if(requestPage.equalsIgnoreCase("sourcerecords.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("source-record"));	
}else if(requestPage.equalsIgnoreCase("auditlog.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("audit-log"));	
}else if(requestPage.equalsIgnoreCase("dashboard.jsp"))  {
    session.setAttribute("ScreenObject", navigationHandler.getScreenObject("dashboard"));	
}
//Get the updated Session Object based on the user browser navigation
screenObject = (ScreenObject) session.getAttribute("ScreenObject");
%>
<!-- 
  Author Sridhar Narsingh
  sridhar@ligaturesoftware.com
  http://www.ligaturesoftware.com
  Update Date: 12/16/2007  
  -->
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
                     <span class="greetingsmall"><nobr><%=session.getAttribute("user")%>&nbsp;</span>
                     <h:commandLink title="#{msgs.header_logout_prompt}" action="#{LoginHandler.signOutUser}" styleClass="greetingsmall">
                         <h:outputText  value="#{msgs.header_logout_prompt}" />
                     </h:commandLink>                     
                     <%}%>             
                     <img src='images/sun-logo.png' alt="Sun Microsystems" title="Sun Microsystems"/>
                 </td>
             </tr>    
         </h:form>   
		 <!--Start dynamic header content code here -->
		 <%
         ArrayList headerTabsLabelsList = ConfigManager.getInstance().getAllScreenObjects();                    
		 Object[] headerTabsLabelsListObj = headerTabsLabelsList.toArray();
		 ScreenObject allScreensArrayOrdered[] = new ScreenObject[headerTabsLabelsListObj.length];         
		 String headerClassName = "";		
                   // modfied by Anil , fix for MIDM security Isssue
		 for(int aIndex = 0 ;aIndex <headerTabsLabelsListObj.length; aIndex++) {
		    ScreenObject screenObjectLocal = (ScreenObject) headerTabsLabelsListObj[aIndex];
                    
                    if (screenObjectLocal!=null){
                     String screenName = ConfigManager.getInstance().getScreenObjectTagName(screenObjectLocal.getID().toString());
                 
                     Operations ops = new Operations();
                     
                     if (screenName.equalsIgnoreCase("assumed-matches")){
                         if(!ops.isAssumedMatch_SearchView()){
                             screenObjectLocal = null;
                             
                         }
                     }
                      if (screenName.equalsIgnoreCase("transactions")){
                         if(!ops.isTransLog_SearchView()){
                             screenObjectLocal = null;
                             
                         }
                     }
                     if (screenName.equalsIgnoreCase("audit-log")){
                         if(!ops.isAuditLog_SearchView()){
                             screenObjectLocal = null;
                             
                         }
                     }
                     if (screenName.equalsIgnoreCase("record-details")){
                         if(!ops.isEO_SearchViewSBR()){
                             screenObjectLocal = null;
                             
                         }
                     }
                      if (screenName.equalsIgnoreCase("duplicate-records")){
                         if(!ops.isPotDup_SearchView()){
                             screenObjectLocal = null;
                             
                         }
                     }
					 if (screenName.equalsIgnoreCase("reports")){
                         if(!ops.isReports_MergedRecords()&& !ops.isReports_DeactivatedEUIDs() &&
							 !ops.isReports_UnmergedRecords() && !ops.isReports_Updates() && !ops.isReports_Activity() && !ops.isReports_Duplicates()&& !ops.isReports_AssumedMatches()){
                             screenObjectLocal = null;
                             
                         }
                     }
					 if (screenName.equalsIgnoreCase("source-record")){
                         if(!ops.isSO_SearchView() && !ops.isSO_Add() && !ops.isSO_Merge()){
                             screenObjectLocal = null;
                             
                         }
                     }
                    }
                
                if (screenObjectLocal!=null){
                     allScreensArrayOrdered[screenObjectLocal.getDisplayOrder()] = screenObjectLocal;
                }
		 }
		 %>
          
		  <tr>
             <td colspan="2">
                 <div id="header">
                                  <h:form id="tabsForm" title="tabsForm">
                                 <%                                  
                                 for(int i=0;i<allScreensArrayOrdered.length;i++){  
                                  %> 
								  <script>
									  tabTitles.push("<%=allScreensArrayOrdered[i].getDisplayTitle()%>");
								  </script>
                                  <% 
                                  
                                  if (allScreensArrayOrdered[i]!=null){
                                    ValueExpression screenID = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getID(), allScreensArrayOrdered[i].getID().getClass());
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
                                          <span id="<%=allScreensArrayOrdered[i].getDisplayTitle()%>" title="<%=screenObject.getDisplayTitle()%>"><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
                                      </h:commandLink>
								 <%}%>
                                 <%}}%>
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

/*function highlighTabs(formName,thisEvent)   {
	alert(thisEvent.target);
  var src = e.srcelement? e.srcelement : e.target; 
  var clickedTabTitle = thisEvent.srcElement.parentElement.title;  
  for(i=0; i< tabTitles.length; i++)   {      
			if(tabTitles[i] == clickedTabTitle ) {
				alert(thisEvent.srcElement);
				thisEvent.srcElement.parentElement.style.className = "navbuttonselected"
				var selectedId = document.getElementById(tabTitles[i]).parentElement;
				selectedId.className = "navbuttonselected"
	    	} else {
				var notSelectedId = document.getElementById(tabTitles[i]).parentElement;
				alert(thisEvent.srcElement);
				notSelectedId.className = "navbutton";
			}
    }
}
*/
</script>
</div>
