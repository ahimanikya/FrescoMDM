<%-- 
    Document   : logout
    Created on : Nov 14, 2008, 10:35:48 AM
    Author     : Narahari, cye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.sun.mdm.multidomain.presentation.beans.ApplicationHandler" %>
    
    <%
    System.out.println(" -----------1--------------"+request.getParameter("logout"));
    
    
    String Logout = request.getParameter("logout");
    boolean isLogout = (null == Logout?false:true);
    if(isLogout)
    {
        ApplicationHandler applicationhandler = (ApplicationHandler)session.getAttribute("applicationHandler");
        if (applicationhandler != null) {
            applicationhandler.logout();
        } else {
            session.invalidate();
        }
        response.sendRedirect("redirect.jsp");
    }
    
    %>
    