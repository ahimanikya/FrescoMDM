<%-- 
    Document   : header
    Created on : Oct 7, 2008, 3:53:48 PM
    Author     : cye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.sun.mdm.multidomain.services.security.ACL" %>
<%@ page import="com.sun.mdm.multidomain.services.security.UserProfile" %>

<%@ include file="/WEB-INF/jsp/include.jsp" %>

<link rel="stylesheet" type="text/css" href="css/styles.css" media="screen"/>

<% 

   
   String LoggedUser =(String) session.getAttribute("user");
       
   String currentUri = request.getRequestURI();

   boolean isAdministerScreen = true; 

  if(currentUri.contains("manage")) {
    isAdministerScreen = false;
  }
%>



<table cellpadding="3" width="100%" border="0">
    <tr>
        <td align="left"><img src='images/banner-logo.png' alt="MultiDomain Web Manager" title="MultiDomain Web Manager" /> </td>
        <td class="greetingsmall"><f:message key="logged_in_as_text" /> <%=LoggedUser%></td>
        <td style="width:130px;" title="<f:message key="logout_text" />"><a href="logout.jsp?logout=success" class="logoutButton" ><span> <f:message key="logout_text" /></span></a></td>
        <td align="right" style="width:120px;"><img src='images/sun-logo.png' alt="Sun Microsystems" title="Sun Microsystems"/></td>
    </tr>
    <tr>
         <td colspan="4">
             <% if (isAdministerScreen) { // Current screen is Adminster %>
             <a href="administration.htm" class="navbuttonselected" title="<f:message key="administration_text" />"><span><f:message key="administration_text" /></span></a> 
             <a href="manage.htm" class="navbutton" title="<f:message key="manage_text" />"><span><f:message key="manage_text" /></span></a>
             <% } else { // Current screen is Manage %>
             <a href="administration.htm" class="navbutton" title="<f:message key="administration_text" />"><span><f:message key="administration_text" /></span></a> 
             <a href="manage.htm" class="navbuttonselected" title="<f:message key="manage_text" />"><span><f:message key="manage_text" /></span></a>
             <% } %>
         </td>
    </tr>
    <tr>
           <td colspan="7"><div class="blueline">&nbsp;</div></td>
    </tr>
</table> 

    

    
