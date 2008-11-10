<%-- 
    Document   : ManageRelationshipType
    Created on : Oct 6, 2008, 3:15:28 PM
    Author     : cye
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" type="text/css" href="css/administration.css" media="screen"/>

<script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
<script type='text/javascript' src='dwr/interface/RelationshipDefHandler.js'></script>    
<script type='text/javascript' src='dwr/engine.js'></script>    
<script type='text/javascript' src='dwr/util.js'></script>    
<script type='text/javascript' src='dwr/local.js'></script>

<script type='text/javascript' src='scripts/mdwm.js'></script>

<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.dnd.Moveable");
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>
<script type='text/javascript'>
    var relationshiptype;
    dwr.engine.setErrorHandler(exceptionHandler);
    function exceptionHandler(message) {
        alert("Exception: " + message);
    }
    function loadSourceDomains() {                
        var domain=document.getElementById("selectSourceDomain").value;
        DomainHandler.getDomains( updateSourceDomains);
    }
    function updateSourceDomains(data) {
        dwr.util.addOptions("selectSourceDomain", data, "name");        
        dwr.util.setValue("selectSourceDomain", data[0].name);   
    }

    function loadTargetDomains() {
        var domain=document.getElementById("selectTargetDomain").value;                
        DomainHandler.getDomains( updateTargetDomains);
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
        RelationshipDefHandler.getRelationshipTypes(sourceDomain, targetDomain, updateRelationshipTypes);
    }                         
    function updateRelationshipTypes(data) {
        dwr.util.removeAllRows("types");
        dwr.util.addRows("types", data, cellfuncs, {escapeHtml:false});
    }
    function clickAdd() {
        relationshiptype = {name:"foo", displayName:"foo", sourceDomain:"Person", targetDomain:"Product"};
        alert("add new relationshiptype: {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
        RelationshipDefHandler.addRelationshipType(relationshiptype, clickAddCB);
    }
    function clickAddCB(data) {
        alert("added relationshiptype id=" + data);
        loadRelationshipTypes();
    }
    function clickDelete() {
        relationshiptype = {name:"foo", displayName:"foo", sourceDomain:"Person", targetDomain:"Product"};
        alert("delete a relationshiptype: {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
        RelationshipDefHandler.deleteRelationshipType(relationshiptype, clickDeleteCB);
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
        RelationshipDefHandler.getRelationshipTypeCount(domain, clickThrowCB);
    }
    function clickThrowCB(data){
    }
    var cellfuncs = [
        function(data) { return data.name; },
        function(data) { return data.displayName; },
        function(data) { return data.sourceDomain; },
        function(data) { return data.targetDomain; },
        function(data) { return "<input id='edit' type='button' value='Edit' onclick='clickEdit(this.id)' />"; },
        function(data) { return "<input id='clone' type='button' value='Clone' onclick='clickClone(this.id)' />"; }              
    ];
    
    function tellValue() {
        var a = document.getElementById("textid");
        alert(a.value);
    }
</script>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <!--<tr>
        <td width="80%">&nbsp;</td>
        <td align="right" width="20%">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td><input type="button" value="<f:message key="save_text" />"/></td>
                    <td class="button_spacing"></td>
                    <td><input type="button" value="<f:message key="revert_text" />" /></td>
                </tr>
            </table>                            
        </td>
    </tr>-->
    <tr>
        <td>
            <!-- domain drop downs starts -->
            <table cellpadding="0" cellspacing="0" border="0">
                <form name="domainSelectionForm">
                    <tr>
                        <td class="mainLabel"><f:message key="source_domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;</td>
                        <td>
                            <select id="selectSourceDomain"></select>
                        </td>
                        <td>&nbsp;</td>
                        <td class="mainLabel"><f:message key="target_domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;</td>
                        <td>
                            <select id="selectTargetDomain" onchange="selectDomain()"></select>
                        </td>
                    </tr>
                </form>
            </table>
        </td>
        <td>&nbsp;</td>
        
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    <tr>
        <td>
            <div dojoType="dijit.layout.ContentPane" class="Box" hasShadow="true">
                <div class="TitleBar" title="<f:message key="relationship_text" />"><f:message key="relationship_text" /></div>
                <div dojoType="dijit.layout.ContentPane" class="Content" hasShadow="false">
                <table width="100%">
                    <form name="relationshipListingForm">
                    <tr>
                        <td>
                            <input type="button" onclick="dijit.byId('addrelationship').show();" value="<f:message key="add_text" />..."  />&nbsp;
                            <input type="button" value="<f:message key="delete_text" />" onclick="deleteRecords(this.form);" />                                                        
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <!-- <START> List of available Relationships shown between the selected Source & Target Domains -->
                                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="RelationshipsListing"  id="relListingTable">
                                    <tr class="header">
                                        <td width="5%"  valign="bottom">
                                            <table width="100%"cellspacing="0" cellpadding="0" border="0"><tr><td width="100%" class="label">&nbsp;</td>
                                            <td align="right" class="hSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                            </table>
                                        </td>
                                        
                                        <td width="25%" valign="bottom">
                                            <table width="100%"cellspacing="0" cellpadding="0" border="0"><tr><td width="100%" class="label">Name</td>
                                            <td align="right" class="hSpacing" style="cursor:e-resize;"><img src="images/spacer.gif" height="1" width="2"></td></tr>
                                            </table>
                                        </td>
                                        
                                        <td width="10%" valign="bottom">
                                            <table width="100%"cellspacing="0" cellpadding="0" border="0"><tr><td width="100%" class="label">Direction</td>
                                            <td align="right" class="hSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                            </table>
                                        </td>
                                        
                                        <td width="25%" valign="bottom">
                                            <table width="100%"cellspacing="0" cellpadding="0" border="0"><tr><td width="100%" class="label">Plugin</td>
                                            <td align="right" class="hSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                            </table>
                                        </td>
                                        
                                        <td width="25%" valign="bottom">
                                            <table width="100%"cellspacing="0" cellpadding="0" border="0"><tr><td width="100%" class="label">Attributes</td>
                                            <td align="right" class="hSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                            </table>
                                        </td> 
                                        
                                        <td width="10%">&nbsp;</td>
                                        
                                    </tr>
                                    <tr><td colspan="6" class="vSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                    <tr class="dataRow">
                                        <td align="center" ><input type="checkbox" name="relationshipId"></td>
                                        <td class="textdata">Associate</td>
                                        <td class="textdata">--></td>
                                        <td class="textdata">RelOtherPlugin</td>
                                        <td class="textdata">2 Predefined | 1 Custom</td> 
                                        <td><input type="button" value="Edit..." class="editButton" onclick="dijit.byId('editrelationship').show();"></td>
                                    </tr>
                                    <tr><td colspan="6" class="vSpacing"><img src="images/spacer.gif" height="1" width="1"></td></tr>
                                    <tr  class="dataRow">
                                        <td align="center" ><input type="checkbox" name="relationshipId"></td>
                                        <td class="textdata">Employed By</td>
                                        <td class="textdata"><--></td>
                                        <td class="textdata">RelOtherPlugin</td>
                                        <td class="textdata">2 Predefined | 1 Custom</td> 
                                        <td class="textdata"><input type="button" value="Edit..." class="editButton" onclick="dijit.byId('editrelationship').show();"></td>
                                    </tr>

                                </table>
                            <!-- <END> List of available Relationships shown between the selected Source & Target Domains -->
                        </td>
                    </tr>
                    <tr><td><img src="images/spacer.gif" height="10" width="1"></td></tr>
                </form>
                </table>
                </div>
            </div>
            
        </td>
        <td>&nbsp;</td>
    </tr>
</table>


<!-- Content for Add relationship -->
<div id="addrelationship" dojoType="dijit.Dialog" title="Add Relationship" style="display:none;width:700px;">
    <jsp:include page="/WEB-INF/jsp/administration/add_relationship.jsp" flush="true" />
</div>

<!-- Content for Edit relationship -->
<div id="editrelationship" dojoType="dijit.Dialog" title="Edit Relationship" style="display:none;width:700px;">
    <jsp:include page="/WEB-INF/jsp/administration/edit_relationship.jsp" flush="true" />
</div>

<script>
    loadSourceDomains("selectSourceDomain");
    loadTargetDomains("selectTargetDomain");
</script>    
<script>
    
function addRowToTable()
{
    var x=document.getElementById('relListingTable').insertRow(5);
    var y=x.insertCell(0);
    var z=x.insertCell(1);
    x.insertCell(2).innerHTML="dddddddddd";
    y.innerHTML="";
    z.innerHTML="xyzzzz";
}
function deleteRecords (objForm) {
    alert('Not yet implemented.');
}     

</script>
