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
    <p> <f:message key="login_failure_long" /> <f:message key="click_here" /> <a href="login.jsp"><f:message key="try_again" /></a></p>
    </body>
</html>
