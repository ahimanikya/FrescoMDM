<%-- 
    Document   : logout
    Created on : Nov 14, 2008, 10:35:48 AM
    Author     : Narahari
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
   <%@ page import="com.sun.mdm.multidomain.presentation.beans.ApplicationHandler" %>
    
    <%
    System.out.println(" -----------1--------------"+request.getParameter("logout"));
    
    
    String Logout = request.getParameter("logout");
    boolean isLogout = (null == Logout?false:true);
    if(isLogout)
    {
        ApplicationHandler applicationhandler=new ApplicationHandler();
        applicationhandler.logout();
        response.sendRedirect("login.htm");
        
    }
    
    %>
    