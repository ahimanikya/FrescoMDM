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
    dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.CheckBox");
    dojo.require("dijit.form.FilteringSelect");   

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
            dwr.util.addRows("relListing", [''], [function(data){return getMessageForI18N("no_relationship_definition_found");}], {
                  cellCreator:function(options) {
                    var td = document.createElement("td");
                    td.colSpan="7"; td.align="center";
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
        refreshRelationshipDefsButtonsPalette(); // Refresh the buttons palette to enable/disable the buttons.
    }

    
    var relationListingDataFuncs = [
        function(data) { return "<input type='checkbox' align='center' name='chkRelationshipDef' value='"+data.id+"' onclick='refreshRelationshipDefsButtonsPalette();'>"; },
        function(data) { return data.name; },
        function(data) { 
            //data.description ="testing only this is only testing, testing only this is only testing, testing only this is only testing";
            var desc =  data.description; 
            if(desc == null) return "";
            if(desc.length > 30) {
                desc = firstNCharacters (desc, 30,true);
                desc += "...";
            }
            var descHTML = "<span title='"+data.description+"'> "+ desc +"</span>"
            return descHTML;  
        },
        function(data) { if(getBoolean(data.biDirection)) {return "<img src='images/icons/relationship-both.png'>"; }else {return "<img src='images/icons/relationship-right.png'>" ;}  },
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
        function(data) { return '<img src="images/icons/edit-button.png" alt="<f:message key='edit_text' />" title="<f:message key='edit_text' />..." class="palleteButton" onclick="prepareEditRelationshipDef(\''+data.name+'\'); " >'; },
             
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
                            <select id="selectSourceDomain" onchange="selectDomain()"></select>
                        </td>
                        <td><img src="images/spacer.gif" height="1" width="20"></td>
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
                    <tr>
                        <td valign="bottom">
                            <table cellspacing="0" cellpadding="0" border="0">
                                <tr>
                                    <td><a href="javascript:void(0);" title="<f:message key='select_all_text' />" onclick="selectAllRelationshipDefs();" ><img id="imgSelectAllRelationshipDef" src="images/icons/select_multiple.png" class="palleteButton" alt="<f:message key='select_all_text' />"  border="0"></a></td>
                                    <td><img src="images/spacer.gif" height="1" width="6"></td>
                                    <td><a href="javascript:void(0);" title="<f:message key='de_select_all_text' />" onclick="deselectAllRelationshipDefs();"><img id="imgDeselectAllRelationshipDef" src="images/icons/deselect_multiple.png" alt="<f:message key='de_select_all_text' />" border="0"></a></td>
                                    <td><img src="images/icons/actions_separator.gif" border="0"></td>
                                    <td><a href="javascript:void(0);" onclick="showRelationshipDialog('addrelationship');" title="<f:message key="add_text" />..."><img id="imgAddRelationshipDef" src="images/icons/add_button.png" alt="<f:message key="add_text" />" border="0"></a></td>
                                    <td><img src="images/spacer.gif" height="1" width="6"></td>
                                    <td><a href="javascript:void(0);" title="<f:message key="delete_text" />" onclick="deleteRelationshipDefs();" ><img id="imgDeleteRelationshipDef" src="images/icons/delete_button.png" alt="<f:message key="delete_text" />" border="0"></a></td>
                                </tr>
                            </table>
                           <!--
                            <input type="button" id="selectallbutton" onclick="selectAllRelationshipDefs();" title="Select all"  />&nbsp;
                            <input type="button" id="deselectallbutton" title="De-select all" onclick="deselectAllRelationshipDefs();" /><img src="images/icons/actions_separator.gif" >
                            <input type="button" id="addbutton" onclick="showRelationshipDialog('addrelationship');" title="<f:message key="add_text" />..."  />&nbsp;
                            <input type="button" id="deletebutton" title="<f:message key="delete_text" />" onclick="deleteRelationshipDefs();" />
                            -->
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
                                        <th width="20%" valign="bottom" class="label">
                                            <f:message key="name_text" />
                                        </th>
                                        <th width="20%" valign="bottom" class="label">
                                            <f:message key="desctription_text" />
                                        </th>                                        
                                        <th width="10%" valign="bottom" class="label">
                                            <f:message key="direction_text" />
                                        </th>                                        
                                        <th width="10%" valign="bottom" class="label">
                                            <f:message key="plugin_text" />  
                                        </th>                                        
                                        <th width="25%" valign="bottom" class="label">
                                            <f:message key="attributes_text" />
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
                                    <tr><td class="textdata" colspan="6" align="center"><f:message key="loading_text" /> </td></tr>
                                </table>
                            <!-- <END> List of available Relationships shown between the selected Source & Target Domains -->
                        </td>
                    </tr>
                    <tr><td><img src="images/spacer.gif" height="10" width="1"></td></tr>
                </table>
                </div>
            </div>
            
        </td>
        <td>&nbsp;</td>
    </tr>
</table>


<!-- Content for Add relationship -->
<div id="addrelationship" dojoType="dijit.Dialog" title="<f:message key="add_relationship_text" />" style="display:none;" >
    <div dojoType="dijit.layout.ContentPane" style="width:820px;padding:5px;"
        href="administer_add_relationshipdef.htm" onload="refreshCustomAttributesButtonsPalette (addrelationship_attributesArray, 'addrelationship');">
    </div>
</div>

<!-- Content for Edit relationship -->
<div id="editrelationship" dojoType="dijit.Dialog" title="<f:message key="edit_relationship_text" />" style="display:none;" >
    <div dojoType="dijit.layout.ContentPane" style="width:820px;padding:5px;">
        <jsp:include page="/WEB-INF/jsp/administration/edit_relationship.jsp" flush="true" />
    </div>      
</div>

<script>
    loadSourceDomains("selectSourceDomain");
    loadTargetDomains("selectTargetDomain");
</script>    
<script>

function refreshRelationshipDefsButtonsPalette () {
    var selectedChoices = 0;
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked )
            selectedChoices ++;
    }

    var imgDeleteButtonObj = dojo.byId("imgDeleteRelationshipDef");
    if(imgDeleteButtonObj != null) {
        if(selectedChoices > 0)
            imgDeleteButtonObj.src =   deleteButtonEnabled.src;
        else
            imgDeleteButtonObj.src =   deleteButtonDisabled.src;
    }
    var imgSelectAllButtonObj = dojo.byId("imgSelectAllRelationshipDef");
    if(imgSelectAllButtonObj != null ) {
        if(selectedChoices != chkboxes.length)
            imgSelectAllButtonObj.src =   selectAllButtonEnabled.src;
        else
            imgSelectAllButtonObj.src =   selectAllButtonDisabled.src;
    }
    var imgDeSelectAllButtonObj = dojo.byId("imgDeselectAllRelationshipDef");
    if(imgDeSelectAllButtonObj != null ) {
        if(selectedChoices > 0)
            imgDeSelectAllButtonObj.src =   deselectAllButtonEnabled.src;
        else
            imgDeSelectAllButtonObj.src =   deselectAllButtonDisabled.src;
    }

}

function selectAllRelationshipDefs () {
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = true;
    }
    refreshRelationshipDefsButtonsPalette();
}
function deselectAllRelationshipDefs () {
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = false;
    }
    refreshRelationshipDefsButtonsPalette();
}
function deleteRelationshipDefs () {
    var anySelected = false;
    var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked) anySelected = true;
    }
    if(!anySelected) return;
    if(! confirm(getMessageForI18N("selcted_relationship_delete_confirmation"))) {
        return ;
    }
    
    var sourceDomain=document.getElementById("selectSourceDomain").value;
    var targetDomain=document.getElementById("selectTargetDomain").value;
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked) {
            // Make DWR call to delete
            relationshipdef = {id:chkboxes[i].value};
            RelationshipDefHandler.deleteRelationshipDef(relationshipdef, loadRelationshipDefs);
        }
    }
    refreshRelationshipDefsButtonsPalette();
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

function prepareEditRelationshipDef(relationshipDefName) {
    //alert("editing for  ---------- " + relationshipDefName);
    var sourceDomain=document.getElementById("selectSourceDomain").value;
    var targetDomain=document.getElementById("selectTargetDomain").value;    
    RelationshipDefHandler.getRelationshipDefByName(relationshipDefName, sourceDomain, targetDomain, populateEditRelationshipDefForm);   
}


refreshRelationshipDefsButtonsPalette();

</script>
