<%-- 
    Document   : add_relationship
    Created on : Nov 3, 2008, 10:20:58 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
    String prefixToUse = "addrelationship";
%>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.CheckBox");
  dojo.require("dijit.form.ValidationTextBox");
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
                       <input id="relationship_add_name" type="text" name="Name" value="" title="<f:message key="name_text" />" style="width:150px"/>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                    <f:message key="direction_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                       <!--  <select id="temp" name="Direction" title="<f:message key="direction_text" />"  style="width:75px">
                            <option value=""></option>
                            <option value="false">-></option>
                            <option value="true"><-></option>
                         </select>
                       -->
                        <a href="javascript:void(0);" onclick="refreshDirection(false,'relationship_add_direction','addRelationship_oneDirectionImg','addRelationship_biDirectionImg');"><img id="addRelationship_oneDirectionImg" src="images/icons/relationship-button_right.png" border="0"></a>
                        <a href="javascript:void(0);" onclick="refreshDirection(true,'relationship_add_direction','addRelationship_oneDirectionImg','addRelationship_biDirectionImg');"><img id="addRelationship_biDirectionImg" src="images/icons/relationship-button_both.png" border="0"></a>
                         
                         <input type="hidden" value="false" id="relationship_add_direction" >
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                        <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select id="relationship_add_plugin"  name="Plugin" title="<f:message key="plugin_text" />"  style="width:150px">
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
        <td><textarea id="relationship_add_description" name="Description" rows="3" cols="70" title="<f:message key="desctription_text" />"></textarea></td> 
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="2">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true">
                <div class="TitleBar"><f:message key="relationship_attributes_text" /></div>
                <div class="Content">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td>
                                <!-- Pre-defined attributes section -->
                                
                                <jsp:include page="/WEB-INF/jsp/administration/predefined_attributes.jsp?prefix=addrelationship" flush="true" />
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                        <tr>
                            <td>
                                <!-- Custom attributes section -->
                                <jsp:include  page="/WEB-INF/jsp/administration/custom_attributes.jsp?prefix=addrelationship" flush="true" />
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
            <input type="submit" name="save_add_relations" onclick=" return validateRelationshipForm()" title="<f:message key="save_text" />" value="<f:message key="save_text" />" />
            <input type="button" name="cancel_add_relations" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('addrelationship').hide(); clearAddRelationshipForm (); "/>
        </td>
    </tr>
    
</table>





<script language="javascript" 
  type="text/javascript">

var addRelationshipPrefix = "<%=prefixToUse%>";
function validateRelationshipForm() {

    var relationshipDefName = dojo.byId("relationship_add_name").value;
    var direction = dojo.byId("relationship_add_direction").value;
    var pluginName = dojo.byId("relationship_add_plugin").value;
    var description = dojo.byId("relationship_add_description").value;
    if(relationshipDefName == '') {
       alert(getMessageForI18N("enter_name"));
       dojo.byId('relationship_add_name').focus();
       return false;
    }

    if(direction == '') {
       alert(getMessageForI18N("select_direction"));
       dojo.byId('relationship_add_direction').focus();
       return false;
    }

    if(pluginName == '') {
       alert(getMessageForI18N("select_plugin"));
       dojo.byId('relationship_add_plugin').focus();
       return false;
    }           
    
    // Validation fine. Make DWR call to save this info.
    var sourceDomain = dojo.byId("selectSourceDomain").value;
    var targetDomain = dojo.byId("selectTargetDomain").value;
    relationshipdef = {name:relationshipDefName };
    relationshipdef.sourceDomain = sourceDomain;
    relationshipdef.targetDomain = targetDomain;
    relationshipdef.plugin = pluginName;
    relationshipdef.biDirection = direction;
    relationshipdef.description = description;    

    var effectiveFrom = dojo.byId(addRelationshipPrefix + "_EffectiveFrom").checked;
    var effectiveFromRequired = dojo.byId(addRelationshipPrefix + "_EffectiveFromRequired").checked;
    var effectiveTo = dojo.byId(addRelationshipPrefix + "_EffectiveTo").checked;
    var effectiveToRequired = dojo.byId(addRelationshipPrefix + "_EffectiveToRequired").checked;
    var purgeDate = dojo.byId(addRelationshipPrefix + "_PurgeDate").checked;
    var purgeDateRequired = dojo.byId(addRelationshipPrefix + "_PurgeDateRequired").checked;
    
    relationshipdef.startDate = effectiveFrom;
    if(effectiveFrom) relationshipdef.startDateRequired = effectiveFromRequired; else relationshipdef.startDateRequired = false;
    relationshipdef.endDate = effectiveTo;
    if(effectiveTo) relationshipdef.endDateRequired = effectiveToRequired; else relationshipdef.endDateRequired = false;
    relationshipdef.purgeDate = purgeDate;
    if(purgeDate) relationshipdef.purgeDateRequired = purgeDateRequired; else relationshipdef.purgeDateRequired = false;
    
    //alert("Start date sending is : " + relationshipdef.startDate + "\n" + "required is: " + relationshipdef.startDateRequired);
    //alert("end date sending is : " + relationshipdef.endDate + "\n" + "required is: " + relationshipdef.endDateRequired);
    //alert("purge date sending is : " + relationshipdef.purgeDate + "\n" + "required is: " + relationshipdef.purgeDateRequired);
    
    var customAttributesArray = eval(addRelationshipPrefix + "_attributesArray");
    //alert("array to use is : " + customAttributesArray);
    
    var customAttributes = [];
    for(i=0;i<customAttributesArray.length; i++) {
        var attr = customAttributesArray[i];
        //alert(attr.IdField.value + " " + attr.AttributeNameField.value + " : " +  attr.DefaultValueField.value);
        var  tempAttr = {};
        tempAttr.name = attr.AttributeNameField.value;
        tempAttr.dataType = attr.AttributeTypeField.value;
        tempAttr.defaultValue = attr.DefaultValueField.value;
        tempAttr.isRequired = attr.RequiredField.checked;
        tempAttr.searchable = attr.SearchableField.checked;
        tempAttr.id = attr.IdField.value;
        customAttributes.push(tempAttr);
    }

    relationshipdef.extendedAttributes = customAttributes;
    //alert("extended attr: " + relationshipdef.extendedAttributes);
    
    RelationshipDefHandler.addRelationshipDef(relationshipdef, loadRelationshipDefs );

    // Close this dialog
    dijit.byId('addrelationship').hide();
    clearAddRelationshipForm () ;
    return true;
}
function clearAddRelationshipForm () {
    //alert("clearing fields of add hierarchy form");
    dojo.byId('relationship_add_name').value = "";
    dojo.byId('relationship_add_direction').value = "";
    dojo.byId('relationship_add_plugin').value = "";
    dojo.byId('relationship_add_description').value = "";
    dojo.byId('addRightDirection').src ="images/icons/relationship-button_right.png";
    dojo.byId('addBothDirection').src ="images/icons/relationship-button_both.png";
    
    // clear predefined attributes fields
    clearPredefinedAttributeField(dijit.byId(addRelationshipPrefix+"_EffectiveFrom"), 
            dijit.byId(addRelationshipPrefix+"_EffectiveFromRequired"));
    clearPredefinedAttributeField(dijit.byId(addRelationshipPrefix+"_EffectiveTo"), 
            dijit.byId(addRelationshipPrefix+"_EffectiveToRequired"));
    clearPredefinedAttributeField(dijit.byId(addRelationshipPrefix+"_PurgeDate"), 
            dijit.byId(addRelationshipPrefix+"_PurgeDateRequired"));
            
    // clear custom attributes
    selectAllCustomAttributes( eval(addRelationshipPrefix+'_attributesArray') );
    deleteAttributes( addRelationshipPrefix+'_customAttributesTable', eval(addRelationshipPrefix+'_attributesArray') );
}


</script>


