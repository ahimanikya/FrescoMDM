<%-- 
    Document   : login
    Created on : Oct 29, 2008, 2:47:11 PM
    Author     : cye
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MultiDomain Web Manager</title>
    </head>
    <body>
        <h2>MultiDomain Web Manager login(This page will be redesigned soon):!</h2>
        <br>
        <form action="j_security_check" method=post>
            <p><strong>Please Enter Your User Name: </strong>
            <input type="text" name="j_username" size="25">
            <p><p><strong>Please Enter Your Password: </strong>
            <input type="password" size="15" name="j_password">
            <p><p>
            <input type="submit" value="Submit">
            <input type="reset" value="Reset">
        </form> 
    </body>
</html>
