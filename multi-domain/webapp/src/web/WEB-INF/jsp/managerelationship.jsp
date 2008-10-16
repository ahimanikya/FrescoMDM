<%-- 
    Document   : managerelationship
    Created on : Oct 9, 2008, 4:51:37 PM
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
        <script type='text/javascript' src='dwr/interface/RelationshipHandler.js'></script>    
        <script type='text/javascript' src='dwr/engine.js'></script>    
        <script type='text/javascript' src='dwr/util.js'></script>
        
        <script type='text/javascript'>  
            <!-- engine options -->
            dwr.engine.setAsync(true);
            
            function searchByType() {
                alert("search by type!");
            }
            function searchByRecord() {
                alert("search by record!");
            }            

function writePerson() {
}

function clearPerson() {
}
    
        </script>
        
    </head>
    <body>
        <div id="head">&nbsp;</div>
        <br clear="all">
        <br>        
        <table border="0">              
        <tr>
            <td>
            <input type="button" value="Search relationship(s) by relationship type" onclick="searchByType()"/>         
            </td>
            <td>
            <input type="button" value="Search relationship(s) by record" onclick="searchByRecord()"/>           
            </td>
        </tr>    
        </table>
        
<br>        
<table class="plain">
  <tr>
    <td>Person</td>
  </tr>    
  <tr>
    <td>First Name:</td>
    <td><input id="firstName" type="text" size="30"/></td>
  </tr>
  <tr>
    <td>Last Name:</td>
    <td><input id="lastName" type="text" size="30"/></td>
  </tr>  
  <tr>
    <td>SSN:</td>
    <td><input id="ssn" type="text" size="20"/></td>
  </tr>
  <tr>
    <td>Person.Address</td>
  </tr>      
  <tr>
    <td>Address Line1:</td>
    <td><input type="text" id="address" size="40"/></td>
  </tr>
  <tr>
    <td>City:</td>
    <td><input type="text" id="city" size="40"/></td>
  </tr>  

  <tr>
    <br>  
    <td>Company</td>
  </tr>    
  <tr>
    <td>CompanyName:</td>
    <td><input id="firstName" type="text" size="30"/></td>
  </tr>
  <tr>
    <td>StockSymbol:</td>
    <td><input id="lastName" type="text" size="30"/></td>
  </tr>  
  <tr>
    <td>TaxPayerId:</td>
    <td><input id="ssn" type="text" size="20"/></td>
  </tr>
  <tr>
    <td>Company.Address</td>
  </tr>      
  <tr>
    <td>Address Line1:</td>
    <td><input type="text" id="address" size="40"/></td>
  </tr>
  <tr>
    <td>City:</td>
    <td><input type="text" id="city" size="40"/></td>
  </tr>  
  
  <tr>
    <br>  
    <td>Relationship Attributes</td>
  </tr>  
  <tr>
    <td>From:</td>
    <td><input type="text" id="address" size="40"/></td>
  </tr>
  <tr>
    <td>To:</td>
    <td><input type="text" id="city" size="40"/></td>
  </tr>  
    
  <tr>
    <td colspan="2" align="right">
      <input type="button" value="Search" onclick="writePerson()"/>
      <input type="button" value="Cancel" onclick="clearPerson()"/>
   </td>
  </tr>
</table>
        
    </body>
</html>
