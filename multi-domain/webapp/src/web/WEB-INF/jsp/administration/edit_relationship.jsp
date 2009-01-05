<%-- r
    Document   : edit_relationship
    Created on : Nov 6, 2008, 4:34:25 PM
    Author     : Narahari
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
    String prefixToUse = "editrelationship";
%>

<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.CheckBox");
  dojo.require("dijit.Dialog");
  dojo.require("dojo.parser");
  dojo.require("dijit.form.FilteringSelect");
</script>

<table border="0" width="100%" cellpadding="0" cellspacing="0">
    
    <tr>
        <td class="formLabel"><f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" /></td> 
        <td>
            <table  cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                       <input id="relationship_edit_id" name="id"  style="display:none;width:0px"/>
                   </td>
                   <td>
                       <input id="relationship_edit_name" name="<f:message key="name_text" />" value="" dojoType="dijit.form.TextBox" style="width:150px"/>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                    <f:message key="direction_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />

                        <a href="javascript:void(0);" title="<f:message key='one_direction_text' />" onclick="refreshDirection(false,'relationship_edit_direction','editRelationship_oneDirectionImg','editRelationship_biDirectionImg');"><img id="editRelationship_oneDirectionImg" src="images/icons/relationship-button_right.png" alt="<f:message key='one_direction_text' />" border="0"></a>
                        <a href="javascript:void(0);" title="<f:message key='bi_direction_text' />" onclick="refreshDirection(true,'relationship_edit_direction','editRelationship_oneDirectionImg','editRelationship_biDirectionImg');"><img id="editRelationship_biDirectionImg" src="images/icons/relationship-button_both.png" alt="<f:message key='bi_direction_text' />" border="0"></a>

                         <input type="hidden" value="false" id="relationship_edit_direction">
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                        <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select id="relationship_edit_plugin" name="<f:message key="plugin_text" />" title="<f:message key="plugin_text" />" hasDownArrow="true" style="width:150px">
                           <option value=""></option>
                           <option value="RelSpecialistPlugin">RelSpecialistPlugin</option>
                         </select>
                    </td>
               </tr>
            </table>
        </td>
    </tr>
    
    <tr><td colspan="2"><img src="images/spacer.gif" height="5" width="1"></td></tr>
    <tr> 
        <td valign="top" class="formLabel"><f:message key="desctription_text" /><f:message key="colon_symbol" /></td> 
        <td> <textarea id="relationship_edit_description" name="<f:message key="desctription_text" />" rows="3" cols="70" title="<f:message key="desctription_text" />"></textarea></td> 
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="2">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true" style="width:100%;">
                <div class="TitleBar"><f:message key="relationship_attributes_text" /></div>
                <div class="Content">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td>
                                <!-- Pre-defined attributes section -->                               
                                <jsp:include page="/WEB-INF/jsp/administration/predefined_attributes.jsp" flush="true" >
                                    <jsp:param name="prefix" value="editrelationship" />
                                </jsp:include>  
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                        <tr>
                            <td>
                                <!-- Custom attributes section -->
                                <jsp:include  page="/WEB-INF/jsp/administration/custom_attributes.jsp" flush="true" >
                                     <jsp:param name="prefix" value="editrelationship" />
                                </jsp:include>  
                            </td>
                        </tr>
                       
                    </table>
                </div>
              </div>
        </td>
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    <tr>
        <td align="right" colspan="2">
            <input type="submit" name="<f:message key="save_text" />" onclick="return validateEditRelationshipForm();" title="<f:message key="save_text" />" value="<f:message key="save_text" />"/>
            <input type="button" name="<f:message key="cancel_text" />" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('editrelationship').hide();"/>
        </td>
    </tr>
    
</table>

<script language="javascript" 
  type="text/javascript">
var editRelationshipPrefix = "<%=prefixToUse%>";

function validateEditRelationshipForm() {
  var relationshipDefId = dojo.byId("relationship_edit_id").value;  
  var relationshipDefName = dojo.byId("relationship_edit_name").value;
  var direction = dojo.byId("relationship_edit_direction").value;
  var pluginName = dojo.byId("relationship_edit_plugin").value;
  var description = dojo.byId("relationship_edit_description").value;
   
  if(dojo.byId('relationship_edit_name').value=='') 
   {
       alert(getMessageForI18N("select_name"));
       dojo.byId('relationship_edit_name').focus();
       return false;
   }

   if(dojo.byId('relationship_edit_direction').value=='') 
   {
       alert(getMessageForI18N("select_direction"));
       dojo.byId('relationship_edit_direction').focus();
       return false;
   }

   if(dojo.byId('relationship_edit_plugin').value=='') 
   {
       alert(getMessageForI18N("select_plugin"));
       dojo.byId('relationship_edit_plugin').focus();
       return false;
   }
    // Validation fine. Make DWR call to save this info.
    var sourceDomain = dojo.byId("selectSourceDomain").value;
    var targetDomain = dojo.byId("selectTargetDomain").value;
    relationshipdef = {name:relationshipDefName };
    relationshipdef.id = relationshipDefId;
    relationshipdef.sourceDomain = sourceDomain;
    relationshipdef.targetDomain = targetDomain;
    relationshipdef.plugin = pluginName;
    relationshipdef.biDirection = direction;
    relationshipdef.description = description;

    var effectiveFrom = dojo.byId(editRelationshipPrefix + "_EffectiveFrom").checked;
    var effectiveFromRequired = dojo.byId(editRelationshipPrefix + "_EffectiveFromRequired").checked;
    var effectiveTo = dojo.byId(editRelationshipPrefix + "_EffectiveTo").checked;
    var effectiveToRequired = dojo.byId(editRelationshipPrefix + "_EffectiveToRequired").checked;
    var purgeDate = dojo.byId(editRelationshipPrefix + "_PurgeDate").checked;
    var purgeDateRequired = dojo.byId(editRelationshipPrefix + "_PurgeDateRequired").checked;
    
    relationshipdef.startDate = effectiveFrom;
    if(effectiveFrom) relationshipdef.startDateRequired = effectiveFromRequired; else relationshipdef.startDateRequired = false;
    relationshipdef.endDate = effectiveTo;
    if(effectiveTo) relationshipdef.endDateRequired = effectiveToRequired; else relationshipdef.endDateRequired = false;
    relationshipdef.purgeDate = purgeDate;
    if(purgeDate) relationshipdef.purgeDateRequired = purgeDateRequired; else relationshipdef.purgeDateRequired = false;
    
    var customAttributesArray = eval(editRelationshipPrefix + "_attributesArray");
    //alert("array to use is : " + customAttributesArray);
    
    var customAttributes = [];
    for(i=0;i<customAttributesArray.length; i++) {
        var attr = customAttributesArray[i];
        //alert(attr.IdField.value + " " + attr.AttributeNameField.value + " : " +  attr.DefaultValueField.value);
        var  tempAttr = {};
        tempAttr.name = attr.AttributeNameField.value;
        tempAttr.dataType = attr.AttributeTypeField.value;
        tempAttr.isRequired = attr.RequiredField.checked;
        tempAttr.searchable = attr.SearchableField.checked;
        tempAttr.id = attr.IdField.value;
        if (tempAttr.dataType == "date") {
            tempAttr.defaultValue = attr.DefaultValueField.getDisplayedValue();
        } else {
            tempAttr.defaultValue = attr.DefaultValueField.value;
        }
        if(isEmpty (tempAttr.name)) {
            alert(getMessageForI18N("enter_attribute_name"));
            attr.AttributeNameField.focus();
            return false;
        }
        if( !isValidCustomAttribute (tempAttr.dataType, tempAttr.defaultValue) ) {
            alert(tempAttr.defaultValue + " " + getMessageForI18N("isnotavalidvaluefor")+ " " + tempAttr.name + " " + getMessageForI18N("attribute")+ " " +getMessageForI18N("attributeTypeFor")+ " " + tempAttr.name + " " +getMessageForI18N("is")+ " " +"'"+tempAttr.dataType+"'");
            return false;
        }
        customAttributes.push(tempAttr);
    }
    relationshipdef.extendedAttributes = customAttributes;
    //alert("Updating data now ............. extended attr: " + relationshipdef.extendedAttributes.length);
    
    RelationshipDefHandler.updateRelationshipDef(relationshipdef, loadRelationshipDefs );

    // Close this dialog
    dijit.byId('editrelationship').hide();
       

}


function populateEditRelationshipDefForm(data) {
    if(data != null) {     
        dojo.byId("relationship_edit_id").value = data.id;
        dojo.byId("relationship_edit_name").value = data.name;
        dojo.byId("relationship_edit_direction").value = data.biDirection;
        
        refreshDirection(getBoolean(data.biDirection),'relationship_edit_direction','editRelationship_oneDirectionImg','editRelationship_biDirectionImg');
        
        dojo.byId("relationship_edit_plugin").value = data.plugin;
        dojo.byId("relationship_edit_description").value=data.description;
        
        /* narahari
        dijit.byId("relationship_edit_description").attr("value", ""); 
        dijit.byId("relationship_edit_description").attr("value", data.description); 
        */

        //alert("Effective From: " + data.startDate + " Required: " + data.startDateRequired);
        populatePredefinedAttributeField(dijit.byId(editRelationshipPrefix+"_EffectiveFrom"), 
            dijit.byId(editRelationshipPrefix+"_EffectiveFromRequired"), getBoolean(data.startDate), getBoolean(data.startDateRequired) );

        //alert("Effective To: " + data.endDate + " Required: " + data.endDateRequired);
        populatePredefinedAttributeField(dijit.byId(editRelationshipPrefix+"_EffectiveTo"), 
            dijit.byId(editRelationshipPrefix+"_EffectiveToRequired"), getBoolean(data.endDate), getBoolean(data.endDateRequired) );

        //alert("Purge date: " + data.purgeDate + " Required: " + data.purgeDatRequired);
        populatePredefinedAttributeField(dijit.byId(editRelationshipPrefix+"_PurgeDate"), 
            dijit.byId(editRelationshipPrefix+"_PurgeDateRequired"), getBoolean(data.purgeDate), getBoolean(data.purgeDateRequired) );

        // Populate custom attributes;
        //eval(editRelationshipPrefix+'_attributesArray').splice(0, 0);
    
        createCustomAttributes (data, editRelationshipPrefix+'_customAttributesTable', eval(editRelationshipPrefix+'_attributesArray'), editRelationshipPrefix);
        refreshCustomAttributesButtonsPalette(eval(editRelationshipPrefix+'_attributesArray'), editRelationshipPrefix );
    }
    showRelationshipDialog('editrelationship');  
}

</script>
