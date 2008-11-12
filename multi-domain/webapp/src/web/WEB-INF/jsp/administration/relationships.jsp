<%-- 
    Document   : ManageRelationshipDefinition
    Created on : Oct 6, 2008, 3:15:28 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" type="text/css" href="css/administration.css" media="screen"/>

<script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
<script type='text/javascript' src='dwr/interface/RelationshipDefHandler.js'></script>    
<script type='text/javascript' src='dwr/engine.js'></script>    
<script type='text/javascript' src='dwr/util.js'></script>    
<script type='text/javascript' src='dwr/local.js'></script>

<script type='text/javascript' src='scripts/mdwm.js'></script>
<script type='text/javascript' src='scripts/attributes.js'></script>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.dnd.Moveable");
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>
<script type='text/javascript'>
    var relationshipdef;
    var isSourceDomainsLoaded = false;
    var isTargetDomainsLoaded = false;
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
        isSourceDomainsLoaded = true;
        if(isSourceDomainsLoaded && isTargetDomainsLoaded)
            selectDomain();        
    }

    function loadTargetDomains() {
        var domain=document.getElementById("selectTargetDomain").value;                
        DomainHandler.getDomains( updateTargetDomains);
    }
    function updateTargetDomains(data) {
        dwr.util.addOptions("selectTargetDomain", data, "name");        
        dwr.util.setValue("selectTargetDomain", data[1].name);  
        isTargetDomainsLoaded = true;
        if(isSourceDomainsLoaded && isTargetDomainsLoaded)
            selectDomain();
    }                
    function selectDomain() {
        loadRelationshipDefs();
    }
    function loadRelationshipDefs() {
        var sourceDomain=document.getElementById("selectSourceDomain").value;
        var targetDomain=document.getElementById("selectTargetDomain").value;
        RelationshipDefHandler.getRelationshipDefs(sourceDomain, targetDomain, updateRelationshipDefs);
    }                          
    function updateRelationshipDefs(data) {
        dwr.util.removeAllRows("relListing");
        if(data == null || data.length<=0) {
            //alert("no relationship definitions found");
            dwr.util.addRows("relListing", [''], [function(data){return "No Relationship definitions found.";}], {
                  cellCreator:function(options) {
                    var td = document.createElement("td");
                    td.colSpan="6"; td.align="center";
                    return td;
                  },
                  escapeHtml:false
            });
        }
        dwr.util.addRows("relListing", data, relationListingDataFuncs, {
              rowCreator:function(options) {
                var row = document.createElement("tr");
                return row;
              },
              cellCreator:function(options) {
                var td = document.createElement("td");
                if(options.cellNum==0) td.align="center";// alert(options.cellNum);
                return td;
              },
              escapeHtml:false
            });
    }
    
    
    function clickAdd() {
        relationshipdef = {name:"foo", displayName:"foo", sourceDomain:"Person", targetDomain:"Product"};
        alert("add new relationshipdef: {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
        RelationshipDefHandler.addRelationshipDef(relationshipdef, clickAddCB);
    }
    function clickAddCB(data) {
        alert("added relationshipdef id=" + data);
        loadRelationshipDefs();
    }
    function clickDelete() {
        relationshipdef = {name:"foo", displayName:"foo", sourceDomain:"Person", targetDomain:"Product"};
        alert("delete a relationshipdef {name:'foo', sourceDomain:'Person', targetDomain:'Product'}");
        RelationshipDefHandler.deleteRelationshipDef(relationshipdef, clickDeleteCB);
    }           
    function clickDeleteCB() {
        loadRelationshipDefs();
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
        RelationshipDefHandler.getRelationshipDefCount(domain, clickThrowCB);
    }
    function clickThrowCB(data){
    }

    
    var relationListingDataFuncs = [
        function(data) { return "<input type='checkbox' align='center' name='chkRelationshipDef' value='"+data.name+"'>"; },
        function(data) { return data.name; },
        function(data) { if(data.biDirection) return "<-->"; else return "-->" ; },
        function(data) { return data.plugin; },
        function(data) { 
            var fixedAttributesCount = 0;
            if(data.startDate) fixedAttributesCount ++;
            if(data.endDate) fixedAttributesCount ++;
            if(data.purgeDate) fixedAttributesCount ++;
            var output = "";
            output += fixedAttributesCount + " Predefined | " + data.extendedAttributes.length + " Custom"; 
            return output; //return data.attributes; 
        },
        function(data) { return '<input type="button" value="Edit..." class="editButton" onclick="showRelationshipDialog(\'editrelationship\');">'; },
        //function(data) { return "<input id='clone' type='button' value='Clone' onclick='clickClone(this.id)' />"; }              
    ];


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
            <div dojoType="dijit.layout.ContentPane" class="Box" hasShadow="true" style="height:400px;">
                <div class="TitleBar" title="<f:message key="relationship_text" />"><f:message key="relationship_text" /></div>
                <div dojoType="dijit.layout.ContentPane" class="Content" hasShadow="false">
                <table width="100%">
                    <form name="relationshipListingForm">
                    <tr>
                        <td valign="bottom">
                            <input type="button" id="selectallbutton" onclick="selectAllRelationshipDefs(this.form);" title="Select all"  />&nbsp;
                            <input type="button" id="deselectallbutton" title="De-select all" onclick="deselectAllRelationshipDefs(this.form);" /><img src="images/icons/actions_separator.gif" >
                            <input type="button" id="addbutton" onclick="showRelationshipDialog('addrelationship');" title="<f:message key="add_text" />..."  />&nbsp;
                            <input type="button" id="deletebutton" title="<f:message key="delete_text" />" onclick="deleteRelationshipDefs(this.form);" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <!-- <START> List of available RelationshipDefs between the selected Source & Target Domains -->
                                <table cellspacing="0" cellpadding="0" border="0" width="100%" class="DefsListing"  >
                                    <thead class="header">
                                        <tr>
                                        <th width="5%"  valign="bottom" class="label">
                                            &nbsp;
                                        </th>
                                        
                                        <th width="25%" valign="bottom" class="label">
                                            Name
                                        </th>
                                        
                                        <th width="10%" valign="bottom" class="label">
                                            Direction
                                        </th>
                                        
                                        <th width="25%" valign="bottom" class="label">
                                            Plugin 
                                        </th>
                                        
                                        <th width="25%" valign="bottom" class="label">
                                            Attributes
                                        </th> 
                                        
                                        <th width="10%">&nbsp;</th>
                                        
                                        </tr>
                                    </thead>
                                    <tbody id="relListing">
                                     <!--
                                    <tr class="dataRow">
                                        <td align="center" ><input type="checkbox" name="relationshipId"></td>
                                        <td class="textdata">Associate</td>
                                        <td class="textdata">-></td>
                                        <td class="textdata">RelOtherPlugin</td>
                                        <td class="textdata">2 Predefined | 1 Custom</td> 
                                        <td><input type="button" value="Edit..." class="editButton" onclick="dijit.byId('editrelationship').show();"></td>
                                    </tr>

                                    <tr class="dataRow">
                                        <td align="center" ><input type="checkbox" name="relationshipId"></td>
                                        <td class="textdata">Employed By</td>
                                        <td class="textdata"><-></td>
                                        <td class="textdata">RelOtherPlugin</td>
                                        <td class="textdata">2 Predefined | 1 Custom</td> 
                                        <td class="textdata"><input type="button" value="Edit..." class="editButton" onclick="dijit.byId('editrelationship').show();"></td>
                                    </tr>
                                    
                                    -->
                                    <tr><td class="textdata" colspan="6" align="center"> Loading... </td></tr>
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
function selectAllRelationshipDefs (objForm) {
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = true;
    }
}
function deselectAllRelationshipDefs (objForm) {
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = false;
    }
}
function deleteRelationshipDefs (objForm) {
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked) {
            alert('deleting ' + chkboxes[i].value);
            // Make DWR call to delete
        }
    }
}

function showRelationshipDialog (dialogId) {
    var relationshipDialog = dijit.byId(dialogId);
    var strTitle = relationshipDialog.title;
    var sourceDomain=document.getElementById("selectSourceDomain").value;
    var targetDomain=document.getElementById("selectTargetDomain").value;
    if(sourceDomain != null && targetDomain != null)
        strTitle += " - " + sourceDomain + " and " + targetDomain;
        
    relationshipDialog.titleNode.innerHTML = strTitle;
    relationshipDialog.show();
}
</script>
