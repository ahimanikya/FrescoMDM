<%-- 
    Document   : loginerror
    Created on : Oct 29, 2008, 3:00:44 PM
    Author     : cye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
       <c:url var="url" value="/index.jsp"/>
  <h2>Invalid user name or password.</h2>
  <p>Please enter a user name or password that is authorized to access this application.  
  For this application, this means a user that has been created 
in the <code>file</code> realm and has been assigned to the <em>group</em> of <code>user</
code>.  Click here to <a href="${url}">Try Again</a></h2>
    </body>
</html>
