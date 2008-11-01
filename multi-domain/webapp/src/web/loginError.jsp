<%-- 
    Document   : loginerror
    Created on : Oct 29, 2008, 3:00:44 PM
    Author     : cye
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page session="false" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.sun.mdm.multidomain.presentation.beans.UserLocale"  %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="./css/styles.css" media="screen"/>
        <title><f:message key="webpage.title" /></title>
        <% 
            UserLocale userLocale = (UserLocale)request.getAttribute("userLocale");
            request.setAttribute("logout", "LoggedOut");
            request.getSession().invalidate();       
        %>            
    </head>
    <body> 
    
    <f:setLocale value="${userLocale.name}" scope="application" /> 
    <f:setBundle basename="com.sun.mdm.multidomain.presentation.errors" scope="page"/> 
     
    <c:url var="url" value="/index.jsp"/>
    <h2><f:message key="login_failure_message" /></h2>
    <p>Please enter a user name or password that is authorized to access this application.  
  For this application, this means a user that has been created 
in the <code>file</code> realm and has been assigned to the <em>group</em> of <code>user</
code>.  Click here to <a href="${url}">Try Again</a></h2>
    </body>
</html>
