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
               var domainSearch1 = {name:"domain1", type:"simple", attributes:[{name:"Person.FirstName", value:"Foo"}, {name:"Person.Address.City", value:"Foo"}]};
               var domainSearch2 = {name:"domain2", type:"simple", attributes:[{name:"Person.FirstName", value:"NotFoo"}, {name:"Person.Address.City", value:"NotFoo"}]};
               var relationshipSearch = {name:"IsFoo",  attributes:[{name:"Title", value:"NotFoo"}]};
               RelationshipHandler.searchRelationships(domainSearch1, domainSearch2, relationshipSearch, searchByTypeCB);  
            }
            
            function searchByTypeCB(data) {
                 alert("received relationshp[0]: " + data[0].sourceDomain + ":" +data[0].targetDomain);
                 alert("received relationshp[1]: " + data[1].sourceDomain + ":" +data[1].targetDomain);
            }
            
            function getDetail() {
               var relationshipView = {name:"FooView", sourceDomain:"sourceDomain", targetDomain:"targetDomain"};
               RelationshipHandler.getRelationship(relationshipView, getDetailCB);  
  
            }
            function getDetailCB(data) {
                alert("source = >" + data.sourceRecord.name + ":" + data.sourceRecord.attributes[0].name +  ":" + data.sourceRecord.attributes[0].value);
                alert("target = >" + data.targetRecord.name + ":" + data.targetRecord.attributes[0].name +  ":" + data.targetRecord.attributes[0].value);
                alert("relationship = >" + data.relationshipRecord.name + ":" + data.relationshipRecord.attributes[0].name +  ":" + data.relationshipRecord.attributes[0].value);  
            }
            
            function searchByRecord() {
                alert("search by record!");
            }            

            function addRelationship() {
                alert("Add a relationship"); 
            } 
            function addRelationshipCB() {
                
            }
            function deleteRelationship() {
                alert("Delete a relationship");
            } 
            function deleteRelationshipCB() {       
            }
            function Search() {
            }

            function clear() {
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
                <input type="button" value="Get a record detail" onclick="getDetail()"/>           
            </td>
            <td>
            <input type="button" value="Search relationship(s) by record" onclick="searchByRecord()"/>           
            </td>
        </tr> 
         <tr>
            <td>
            <input type="button" value="Add a relationship" onclick="addRelationship()"/>           
            </td>
        </tr> 
        <tr>
            <td>
            <input type="button" value="Delete a relationship" onclick="deleteRelationship()"/>           
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
      <input type="button" value="Search" onclick="Search()"/>
      <input type="button" value="Cancel" onclick="clear()"/>
   </td>
  </tr>
</table>
        
    </body>
</html>
