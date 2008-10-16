<%-- 
    Document   : ManageRelationshipType
    Created on : Oct 6, 2008, 3:15:28 PM
    Author     : cye
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
    <head>
        <title><f:message key="webpage.title" /></title>
        
        <link rel="stylesheet" type="text/css" href=".../../css/styles.css" media="screen"/>
                  
        <script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
        <script type='text/javascript' src='dwr/interface/RelationshipTypeHandler.js'></script>    
        <script type='text/javascript' src='dwr/engine.js'></script>    
        <script type='text/javascript' src='dwr/util.js'></script>    
               
        <script type='text/javascript'>
            var relationshiptype;
            dwr.engine.setErrorHandler(exceptionHandler);
            function exceptionHandler(message) {
                alert(message);
            }
            function loadSourceDomains() {
                var domain=document.getElementById("selectSourceDomain").value;
                DomainHandler.getDomains(updateSourceDomains);
            }
            function updateSourceDomains(data) {
                dwr.util.addOptions("selectSourceDomain", data, "name");        
                dwr.util.setValue("selectSourceDomain", data[0].name);   
            }
            function loadTargetDomains() {
                var domain=document.getElementById("selectTargetDomain").value;                
                DomainHandler.getDomains(updateTargetDomains);
            }
            function updateTargetDomains(data) {
                dwr.util.addOptions("selectTargetDomain", data, "name");        
                dwr.util.setValue("selectTargetDomain", data[1].name);   
            }                
            function selectDomain() {
                loadRelationshipTypes();
            }
            function loadRelationshipTypes() {
                var sourceDomain=document.getElementById("selectSourceDomain").value;
                var targetDomain=document.getElementById("selectTargetDomain").value;
                RelationshipTypeHandler.getRelationshipTypes(sourceDomain, targetDomain, updateRelationshipTypes);
            }                         
            function updateRelationshipTypes(data) {
                dwr.util.removeAllRows("types");
                dwr.util.addRows("types", data, cellfuncs, {escapeHtml:false});
            }
            function clickAdd() {
                relationshiptype = {name:"foo", sourceDomain:"Person", targetDomain:"Product", attributes:[{name:"foo"}]};
                alert("add new relationshiptype: {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
                RelationshipTypeHandler.addRelationshipType(relationshiptype, clickAddCB);
            }
            function clickAddCB(data) {
                 alert("added relationshiptype id=" + data);
                 loadRelationshipTypes();
            }
            function clickDelete() {
               relationshiptype = {name:"foo", sourceDomain:"Person", targetDomain:"Product"};
               alert("delete a relationshiptype: {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
               RelationshipTypeHandler.deleteRelationshipType(relationshiptype, clickDeleteCB);
            }           
            function clickDeleteCB() {
                loadRelationshipTypes();
            }
            function clickEdit(id) {
                alert("edit this " + id + " now!");
            }
            function clickClone(id) {
                alert("clone this " + id + " now!");
            }            
            function clickThrow(){
                alert("throw an exception now!");
                var domain = "foo";
                RelationshipTypeHandler.getRelationshipTypeCount(domain, clickThrowCB);
            }
            function clickThrowCB(data){
            }
            var cellfuncs = [
              function(data) { return data.name; },
              function(data) { return data.sourceDomain; },
              function(data) { return data.targetDomain; },
              function(data) { return data.attributes[0].name; },              
              function(data) { return "<input id='edit' type='button' value='Edit' onclick='clickEdit(this.id)' />"; },
              function(data) { return "<input id='clone' type='button' value='Clone' onclick='clickClone(this.id)' />"; }              
            ];
        </script>
    </head>
    <body>
        <div id="head">&nbsp;</div>
        <br clear="all">
        <br>
        <br>    
        <h2>MultiDomain Relationship Management Web Application</h2>        
        <h3><i><c:out value="${tab}"/></i></h3>
        <table border="0">
        <form name="manageRelationshipType">
        <tr>
            <td>Source Domain</td>
            <td>
            <select id="selectSourceDomain">
            </select>
            </td>
        </tr>            
        <tr>
            <td>Target Domain</td>
            <td>
            <select id="selectTargetDomain" onchange="selectDomain()">
            </select>
            </td>
        </tr>            
        </form>
        </table>
              
        <table border="0">              
        <tr>
            <td>
            <input type="button" value="Add a new relationship type" onclick="clickAdd()"/>         
            </td>
            <td>
            <input type="button" value="Delete selected relationship type(s)" onclick="clickDelete()"/>           
            </td>
            <td>
            <input type="button" value="Show to throw an exception" onclick="clickThrow()"/>           
            </td>           
        </tr>    
        </table>
    
        <div id="List of RelationshipTypes">
        <table class="relationshiptypetable" border="1" width="100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>SourceDomain</th>
                <th>TargetDomain</th>
                <th>Attribute</th>                
                <th>Action</th>
                <th>Action</th>                
            </tr>
        </thead>
        <tbody id="types">
        </tbody>
        </table>
        </div>        
        <script>
            loadSourceDomains("selectSourceDomain");
            loadTargetDomains("selectTargetDomain");
        </script>    
    </body>
</html>
