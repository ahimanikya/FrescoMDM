<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <h1>JSP Page</h1>
    
    <table>
        <tr>
            <td>
            This is the results page from the patient details when submit button was clicked.                
            <% 
                   PotentialDuplicateSummary potentialDuplicateSummary = null;
                   PotentialDuplicateIterator pdPageIter = (PotentialDuplicateIterator) session.getAttribute("pdPageIter");                
                   PotentialDuplicateIterator asPdIter ;
             %>      
            PD iterator in jsp page<%=pdPageIter%>
            
            </td>
        </tr>
    </table>    

    
    </body>
</html>
