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
<%@ page import="java.util.ArrayList"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<f:loadBundle basename="com.sun.mdm.index.edm.presentation.messages.Edm" var="msgs" />
<%@ page isErrorPage="false" errorPage="../error500.jsp" %>

<%
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

// fix for 6679172,6684209
if(session!=null && session.isNew()) {	
	
	session.invalidate();
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
  				     <img src='images/EDM-logo.png' alt="Master Index Data Manager" title="Master Index Data Manager" />
                 </td>
                 <td align="right">
                     <% if (session.getAttribute("user") != null) {%>
                     <span class="greetingsmall"><nobr><%=session.getAttribute("user")%>&nbsp;</span>
                     <h:commandLink action="#{LoginHandler.signOutUser}" styleClass="greetingsmall">
                         <h:outputText  value="#{msgs.header_logout_prompt}" />
                     </h:commandLink>
                     
                     <%}%>             
                     <img src='images/sun-logo.png' alt="Sun Microsystems Logo" title="Sun Microsystems Logo"/>
                 </td>
             </tr>    
         </h:form>   
		 <!--Start dynamic header content code here -->
		 <%
         ArrayList headerTabsLabelsList = ConfigManager.getInstance().getAllScreenObjects(); 
		 Object[] headerTabsLabelsListObj = headerTabsLabelsList.toArray();
		 ScreenObject allScreensArrayOrdered[] = new ScreenObject[headerTabsLabelsListObj.length];
         
		 String headerClassName = "";

		 //System.out.println("newArrayListOrdered (size) ===> " + newArrayListOrdered.size());

		 for(int aIndex = 0 ;aIndex <headerTabsLabelsListObj.length; aIndex++) {
		    ScreenObject screenObjectLocal = (ScreenObject) headerTabsLabelsListObj[aIndex];
            allScreensArrayOrdered[screenObjectLocal.getDisplayOrder()] = screenObjectLocal;
		 }
		 %>
          
		  <tr>
             <td colspan="2">
                 <div id="header">
                                 <%   for(int i=0;i<allScreensArrayOrdered.length;i++){  
                                  %> 
                                  <% 
                                    ValueExpression screenID = ExpressionFactory.newInstance().createValueExpression(allScreensArrayOrdered[i].getID(), allScreensArrayOrdered[i].getID().getClass());
                                  %>

                                  <h:form>
								  <%if(screenObject.getDisplayTitle().equalsIgnoreCase(allScreensArrayOrdered[i].getDisplayTitle())) {%>
                                       <h:commandLink  styleClass ="navbuttonselected" 
                                                  actionListener="#{NavigationHandler.setHeaderByTabName}" > 
                                          <f:attribute name="screenId" value="<%=screenID%>"/>
                                          <span><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
                                      </h:commandLink>
								 <%} else {%>
                                       <h:commandLink  styleClass ="navbutton" 
                                                  actionListener="#{NavigationHandler.setHeaderByTabName}" > 
                                          <f:attribute name="screenId" value="<%=screenID%>"/>
                                          <span><%=allScreensArrayOrdered[i].getDisplayTitle()%></span>
                                      </h:commandLink>
								 <%}%>
                                 </h:form>
                                 <%}%>

				 </div>
		     </td>
		 </tr>
         <tr>
             <td width="100%" colspan="2"><div class="blueline">&nbsp;</div></td>
         </tr>
     </table>    
  
</div>
