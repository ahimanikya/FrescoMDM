<%-- 
    Document   : managehierarchytype.jsp
    Created on : Oct 9, 2008, 4:56:08 PM
    Author     : cye
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<link rel="stylesheet" type="text/css" href="css/administration.css" media="screen"/>

<script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
<script type='text/javascript' src='dwr/interface/HierarchyDefHandler.js'></script>    
<script type='text/javascript' src='dwr/engine.js'></script>    
<script type='text/javascript' src='dwr/util.js'></script>    
<script type='text/javascript' src='dwr/local.js'></script>     

<script type='text/javascript' src='scripts/mdwm.js'></script>

<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>                
<script type="text/javascript">
    dojo.require("dojo.dnd.Moveable");
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>

<script type="text/javascript">
    
    function loadDomains() {
        DomainHandler.getDomains( updateDomains);
    }
    function updateDomains(data) {
        dwr.util.addOptions("domain", data, "name");        
        dwr.util.setValue("domain", data[0].name);     
        loadHierarchyDefs();        
    }


    function loadHierarchyDefs() {
        var domain=document.getElementById("domain").value;
        HierarchyDefHandler.getHierarchyDefs(domain, updateHierarchyDefs);
    }
    function updateHierarchyDefs (data) {
        dwr.util.removeAllRows("hierachyListing");
        if(data == null || data.length<=0) {
            //alert("no relationship definitions found");
            dwr.util.addRows("hierachyListing", [''], [function(data){return "No Hierarchy definitions found.";}], {
                  cellCreator:function(options) {
                    var td = document.createElement("td");
                    td.colSpan="6"; td.align="center";
                    return td;
                  },
                  escapeHtml:false
            });
        }
        dwr.util.addRows("hierachyListing", data, hierarchyListingDataFuncs, {
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
        refreshHierarchyDefsButtonsPalette();
    }
    
    var hierarchyListingDataFuncs = [
        function(data) { return "<input type='checkbox' align='center' name='chkHierarchyDef' value='"+data.name+"' onclick='refreshHierarchyDefsButtonsPalette();'>"; },
        function(data) { return data.name; },
        function(data) { return data.description;  },
        function(data) { return data.plugin; },
        function(data) { 
            var fixedAttributesCount = 0; 
            if(getBoolean(data.startDate)) fixedAttributesCount ++;
            if(getBoolean(data.endDate)) fixedAttributesCount ++;
            if(getBoolean(data.purgeDate)) fixedAttributesCount ++;
            var output = "";
            output += fixedAttributesCount + " Predefined | " + data.extendedAttributes.length + " Custom"; 
            return output; //return data.attributes; 
        },
        function(data) { return '<input type="button" value="Edit..." class="editButton" onclick="prepareEditHierachyDef(\''+data.name+'\'); " >'; },
        //function(data) { return "<input id='clone' type='button' value='Clone' onclick='clickClone(this.id)' />"; }              
    ];
</script>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <!--<tr>
        <td width="80%">&nbsp;</td>
        <td align="right"  width="20%">
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
                <form name="domainSelection">
                    <tr>
                        <td class="mainLabel"><f:message key="domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;</td>
                        <td>
                            <select id="domain" name="Domain" title="<f:message key="domain_text" />" onchange="loadHierarchyDefs();">
                            </select>        
                        </td>
                        <td>&nbsp;</td>
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
                <div class="TitleBar" title="<f:message key="hierarchy_text" />"><f:message key="hierarchy_text" /></div>
                <div dojoType="dijit.layout.ContentPane" class="Content" hasShadow="false">
                <table width="100%" border="0">
                    <tr>
                        <td valign="bottom">
                            <table cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                    <td><a href="javascript:void(0);" title="Select all" onclick="selectAllHierarchyDefs();" ><img id="imgSelectAllHierarchyDef" src="images/icons/select_multiple.gif" class="palleteButton"  border="0"></a></td>
                                    <td><img src="images/spacer.gif" height="1" width="4"></td>
                                    <td><a href="javascript:void(0);" title="De-select all" onclick="deselectAllHierarchyDefs();"><img id="imgDeselectAllHierarchyDef" src="images/icons/deselect_multiple.gif" border="0"></a></td>
                                    <td><img src="images/icons/actions_separator.gif" border="0"></td>
                                    <td><a href="javascript:void(0);" onclick="showHierarchyDialog('addhierarchy');" title="<f:message key="add_text" />..."><img id="imgAddHierarchyDef" src="images/icons/add_button.png" border="0"></a></td>
                                    <td><img src="images/spacer.gif" height="1" width="7"></td>
                                    <td><a href="javascript:void(0);" title="<f:message key="delete_text" />" onclick="deleteHierarchyDefs();" ><img id="imgDeleteHierarchyDef" src="images/icons/delete_button.png" border="0"></a></td>
                                </tr>
                            </table>
                            <!--<input type="button" id="selectallbutton" onclick="selectAllHierarchyDefs(this.form);" title="Select all"  />&nbsp;
                            <input type="button" id="deselectallbutton" title="De-select all" onclick="deselectAllHierarchyDefs(this.form);" /><img src="images/icons/actions_separator.gif" >
                            <input type="button" id="addbutton" onclick="showHierarchyDialog('addhierarchy');" title="<f:message key="add_text" />..."  />&nbsp;
                            <input type="button" id="deletebutton" title="<f:message key="delete_text" />" onclick="deleteHierarchyDefs(this.form);" />
                            -->
                        </td>
                    </tr>
                    <tr>
                        <td>
                            
                            <table cellspacing="0" cellpadding="0" border="0" width="100%" class="DefsListing"  >
                                <thead class="header">
                                    <tr>
                                    <th width="5%"  valign="bottom" class="label">
                                        &nbsp;
                                    </th>

                                    <th width="25%" valign="bottom" class="label">
                                        Name
                                    </th>

                                    <th width="25%" valign="bottom" class="label">
                                        Description
                                    </th>

                                    <th width="15%" valign="bottom" class="label">
                                        Plugin 
                                    </th>

                                    <th width="20%" valign="bottom" class="label">
                                        Attributes
                                    </th> 

                                    <th width="10%">&nbsp;</th>

                                    </tr>
                                </thead>
                                <tbody id="hierachyListing">
                                 
                                <!--<tr><td class="textdata" colspan="6" align="center"> Loading... </td></tr>-->
                            </table>
                        
                        
                        </td>
                    </tr>
                </table>
                </div>
            </div>
            
        </td>
        <td>&nbsp;</td>
    </tr>
</table>
<!-- Content for Add Hierarchy -->
<div id="addhierarchy" dojoType="dijit.Dialog" title="Add Hierarchy" style="display:none;width:700px;">
    <jsp:include page="/WEB-INF/jsp/administration/add_hierarchy.jsp" flush="true" />
</div>

<!-- Content for Edit Hierarchy -->
<div id="edithierarchy" dojoType="dijit.Dialog" title="Edit Hierarchy" style="display:none;width:700px;">
    <jsp:include page="/WEB-INF/jsp/administration/edit_hierarchy.jsp" flush="true" />
</div>

<script>
    loadDomains();
</script>  
<script>
var deleteButtonEnabled = new Image();
deleteButtonEnabled.src = "images/icons/delete_button.png";
var deleteButtonDisabled = new Image();
deleteButtonDisabled.src = "images/icons/delete_button_faded.png";

function refreshHierarchyDefsButtonsPalette () {
    var anySelected = false;
    var chkboxes = document.getElementsByName("chkHierarchyDef");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked )
            anySelected = true;
    }
    var imgDeleteButtonObj = dojo.byId("imgDeleteHierarchyDef");
    if(imgDeleteButtonObj != null ) {
        if(anySelected)
            imgDeleteButtonObj.src =   deleteButtonEnabled.src;
        else
            imgDeleteButtonObj.src =   deleteButtonDisabled.src;
    }

}

    
    
function selectAllHierarchyDefs () {
    var chkboxes = document.getElementsByName("chkHierarchyDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = true;
    }
    refreshHierarchyDefsButtonsPalette();
}
function deselectAllHierarchyDefs () {
    var chkboxes = document.getElementsByName("chkHierarchyDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = false;
    }
    refreshHierarchyDefsButtonsPalette();
}
function deleteHierarchyDefs () {
    var anySelected = false;
    var chkboxes = document.getElementsByName("chkHierarchyDef");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked) anySelected = true;
    }
    if(!anySelected) return;
    if(! confirm("Are you sure you want to delete the selected Hierarchy Definitions?")) {
        return ;
    }
    var domain=document.getElementById("domain").value;
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked) {
            //alert('deleting ' + chkboxes[i].value);
            // Make DWR call to delete
            hierarchydef = {name:chkboxes[i].value, domain:domain};
            HierarchyDefHandler.deleteHierarchyDef(hierarchydef, loadHierarchyDefs);
        }
    }
    refreshHierarchyDefsButtonsPalette();
}

function showHierarchyDialog (dialogId) {
    var hierarchyDialog = dijit.byId(dialogId);
    var strTitle = hierarchyDialog.title;
    var domain = document.getElementById("domain").value;
    if(domain != null )
        strTitle += " - " + domain ;
    hierarchyDialog.titleNode.innerHTML = strTitle;
    hierarchyDialog.show();
}

function prepareEditHierachyDef(hierarchyDefName) {
    //alert("editing for  ---------- " + relationshipDefName);
    var domain = document.getElementById("domain").value;
    HierarchyDefHandler.getHierarchyDefByName(hierarchyDefName, domain, populateEditHierarchyDefForm);   
}

    
refreshHierarchyDefsButtonsPalette ();
</script>
