<%-- 
    Document   : managehierarchytype.jsp
    Created on : Oct 9, 2008, 4:56:08 PM
    Author     : cye
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><f:message key="webpage.title" /></title>
        
        <link rel="stylesheet" type="text/css" href=".../../css/styles.css" media="screen"/>
                  
        <script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
        <script type='text/javascript' src='dwr/interface/HierarchyDefHandler.js'></script>    
        <script type='text/javascript' src='dwr/engine.js'></script>    
        <script type='text/javascript' src='dwr/util.js'></script>
        
        <script type='text/javascript'>
        dwr.engine.setErrorHandler(exceptionHandler);
        function exceptionHandler(message) {
            alert(message);
        }    
        function getHierarchyTypes(domain) {
            alert("requesting hierarchyTypes for {domain=" + domain +"}");
              HierarchyDefHandler.getHierarchyDefs(domain,getHierarchyTypesCB);  
        }    
        function getHierarchyTypesCB(data) {
            alert("received hierarchyTypes[0]:{domain= " + data[0].domain + ",name=" +data[0].name +"}");
        }
        </script>    
    </head>
    <body>
        <div id="head">&nbsp;</div>
        <br clear="all">
        <br><br>        
        <h2>Under Construction!</h2>
        <br><br>
        <table border="0">              
            <tr>
                <td>
                <input type="button" value="Get HierarchyTypes for FOO1 domain" onclick="getHierarchyTypes('FOO1')"/>           
                </td>
            </tr>
            <tr>
                <td>
                <input type="button" value="Get HierarchyTypes for FOO2 domain" onclick="getHierarchyTypes('FOO2')"/>           
                </td>
            </tr>
                  <tr>
                <td>
                <input type="button" value="Get HierarchyTypes for UNKNOWN domain" onclick="getHierarchyTypes('UNKNOWN')"/>           
                </td>
            </tr>           
        </table>            
    </body>
</html>
