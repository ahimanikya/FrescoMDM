<%-- 
    Document   : serviceaccessfailure
    Created on : Oct 10, 2008, 3:57:05 PM
    Author     : cye
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.sun.mdm.multidomain.services.core.ServiceException"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><f:message key="service.access.failure" /></title>  
        <link rel="stylesheet" type="text/css" href=".../../css/styles.css" media="screen"/>        
    </head>
    <body>
    <div id="head">&nbsp;</div>
        <br clear="all">
        <br>        
        <h2>Under Construction!</h2>
    <br>    
    <%
        Exception ex = (Exception) request.getAttribute("exception");
    %>

    <h2>Service access failure: <%= ex.getMessage() %></h2>
    <p>
    <%
    ex.printStackTrace(new java.io.PrintWriter(out));
    %>
    <p>
    <br>
    <A href="<c:url value="/index.htm"/>">Home</A>    
    </body>
</html>
