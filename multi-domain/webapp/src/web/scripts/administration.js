
// Administration screen related scripts

// Populate fields of predefined attributes with the given values. used in Edit screens
function populatePredefinedAttributeField (attributeObj, attributeRequiredObj, attributeValue, attributeRequiredValue) {

    //alert(attributeObj + " : "  + attributeRequiredObj + "\n\n\n" + attributeValue + " : " + attributeRequiredValue);
    if(attributeObj == null || attributeRequiredObj==null) return;

    if(attributeValue == true) {
        attributeObj.setChecked (true);
        attributeRequiredObj.setDisabled(false);
    }  else {
        attributeObj.setChecked (false);
        attributeRequiredObj.setDisabled(true);
    }
    if(attributeRequiredValue == true) attributeRequiredObj.setChecked (true);
    else attributeRequiredObj.setChecked (false);
}
    
function clearPredefinedAttributeField(attributeField, attributeRequiredField) {
    attributeField.setChecked (false);
    attributeRequiredField.setChecked(false);
    attributeRequiredField.setDisabled(true);
}

// Refresh direction icons in Add/Edit relationship def screens
var oneDirectionImgOff = new Image();
oneDirectionImgOff.src="images/icons/relationship-button_right.png";
var oneDirectionImgOn = new Image();
oneDirectionImgOn.src="images/icons/relationship-button_right_tog.png";    

var biDirectionImgOff = new Image();
biDirectionImgOff.src="images/icons/relationship-button_both.png";
var biDirectionImgOn = new Image();
biDirectionImgOn.src="images/icons/relationship-button_both_tog.png";    



function refreshDirection (directionState, hiddenFieldId, oneDirectionImgId, biDirectionImgId) {
  dojo.byId(hiddenFieldId).value = directionState;
  if(directionState == true) {
     dojo.byId(oneDirectionImgId).src = oneDirectionImgOff.src;
     dojo.byId(biDirectionImgId).src = biDirectionImgOn.src;
  } else {
     dojo.byId(oneDirectionImgId).src = oneDirectionImgOn.src;
     dojo.byId(biDirectionImgId).src = biDirectionImgOff.src;
  }
}
    
// Script for Add Relationship Def STARTS
var addRelationshipPrefix = "addrelationship";
var addrelationship_attributesArray = new Array();
function validateRelationshipForm() {

    var relationshipDefName = dojo.byId("relationship_add_name").value;
    var direction = dojo.byId("relationship_add_direction").value;
    var pluginName = dojo.byId("relationship_add_plugin").value;
    var description = dojo.byId("relationship_add_description").value;
    if(relationshipDefName == '') {
       alert("Please Enter the Name.");
       dojo.byId('relationship_add_name').focus();
       return false;
    }

    if(direction == "") {
       alert("Please Select the Direction.");
       dojo.byId('relationship_add_direction').focus();
       return false;
    }

    if(pluginName == '') {
       alert("Please Select the Plugin.");
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
    //showValues(customAttributesArray);

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
        
        alert("administration.js: type => " + tempAttr.dataType + " value => " + tempAttr.defaultValue);
    }

    relationshipdef.extendedAttributes = customAttributes;
    //alert("extended attr: " + relationshipdef.extendedAttributes);

    RelationshipDefHandler.addRelationshipDef(relationshipdef, loadRelationshipDefs );

    // Close this dialog
    clearAddRelationshipForm () ;
    dijit.byId('addrelationship').hide();

    return true;
}
function clearAddRelationshipForm () {
    //alert("clearing fields of add hierarchy form");
    dojo.byId('relationship_add_name').value = "";
    dojo.byId('relationship_add_direction').value = "";
    dojo.byId('relationship_add_plugin').value = "";
    dojo.byId('relationship_add_description').value = "";
    dojo.byId('addRelationship_oneDirectionImg').src = "images/icons/relationship-button_right.png";
    dojo.byId('addRelationship_biDirectionImg').src = "images/icons/relationship-button_both.png";
    
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
// Script for Add Relationship Def ENDS

// Script for Add Hierarchy Def STARTS
var addHierarchyPrefix = "addhierarchy";    
var addhierarchy_attributesArray = new Array();
function validateHierarchyForm() {
    var hierarchyDefName = dojo.byId('hierarchy_add_name').value;
    var plugin = dojo.byId('hierarchy_add_plugin').value;
    var hierarchyEffectiveFrom = dojo.byId('hierarchy_add_effectiveFrom').value;
    var hierarchyEffectiveTo = dojo.byId('hierarchy_add_effectiveTo').value;
    var description =  dojo.byId('hierarchy_add_description').value;
   
     /* narahari
      * dijit.byId("hierarchy_add_description").attr("value");
      * */
    if(dojo.byId('hierarchy_add_name').value=='') {
       alert("Please Enter the Name.");
       dojo.byId('hierarchy_add_name').focus();
       return false;
    }

    if(dojo.byId('hierarchy_add_plugin').value=='') {
       alert("Please Select the Plugin.");
       dojo.byId('hierarchy_add_plugin').focus();
       return false;
    }
       
       // Validation done. make DWR call to add hierarchy Def
    var domain = dojo.byId("domain").value;
    
    var hierarchydef = {name:hierarchyDefName };
    hierarchydef.domain = domain;
    hierarchydef.plugin = plugin;
    hierarchydef.description = description;

    // fixed attributes
    var effectiveFrom = dojo.byId(addHierarchyPrefix + "_EffectiveFrom").checked;
    var effectiveFromRequired = dojo.byId(addHierarchyPrefix + "_EffectiveFromRequired").checked;
    var effectiveTo = dojo.byId(addHierarchyPrefix + "_EffectiveTo").checked;
    var effectiveToRequired = dojo.byId(addHierarchyPrefix + "_EffectiveToRequired").checked;
    var purgeDate = dojo.byId(addHierarchyPrefix + "_PurgeDate").checked;
    var purgeDateRequired = dojo.byId(addHierarchyPrefix + "_PurgeDateRequired").checked;

    hierarchydef.startDate = effectiveFrom;
    if(effectiveFrom) hierarchydef.startDateRequired = effectiveFromRequired; else hierarchydef.startDateRequired = false;
    hierarchydef.endDate = effectiveTo;
    if(effectiveTo) hierarchydef.endDateRequired = effectiveToRequired; else hierarchydef.endDateRequired = false;
    hierarchydef.purgeDate = purgeDate;
    if(purgeDate) hierarchydef.purgeDateRequired = purgeDateRequired; else hierarchydef.purgeDateRequired = false;
    
    //alert("Start date sending is : " + hierarchydef.startDate + "\n" + " -- required is: " + hierarchydef.startDateRequired);
    //alert("end date sending is : " + hierarchydef.endDate + "\n" + " -- required is: " + hierarchydef.endDateRequired);
    //alert("purge date sending is : " + hierarchydef.purgeDate + "\n" + " -- required is: " + hierarchydef.purgeDateRequired);
    
    // Custom attributes
    var customAttributesArray = eval(addHierarchyPrefix + "_attributesArray");
    //showValues(customAttributesArray);
    
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

    hierarchydef.extendedAttributes = customAttributes;
    //alert("extended attr: " + hierarchydef.extendedAttributes);
    
    HierarchyDefHandler.addHierarchyDef(hierarchydef, loadHierarchyDefs );

    // Close this dialog
    dijit.byId('addhierarchy').hide();
    clearAddHierarchyForm();
    return true;       
}
function clearAddHierarchyForm () {
    //alert("clearing fields of add hierarchy form");
    dojo.byId('hierarchy_add_name').value = "";
    dojo.byId('hierarchy_add_plugin').value = "";
    dojo.byId('hierarchy_add_effectiveFrom').value = "";
    dojo.byId('hierarchy_add_effectiveTo').value = "";
    dojo.byId('hierarchy_add_description').value = "";
    
    // clear predefined attributes fields
    clearPredefinedAttributeField(dijit.byId(addHierarchyPrefix+"_EffectiveFrom"), 
            dijit.byId(addHierarchyPrefix+"_EffectiveFromRequired"));
    clearPredefinedAttributeField(dijit.byId(addHierarchyPrefix+"_EffectiveTo"), 
            dijit.byId(addHierarchyPrefix+"_EffectiveToRequired"));
    clearPredefinedAttributeField(dijit.byId(addHierarchyPrefix+"_PurgeDate"), 
            dijit.byId(addHierarchyPrefix+"_PurgeDateRequired"));
            
    // clear custom attributes
    selectAllCustomAttributes( eval(addHierarchyPrefix+'_attributesArray') );
    deleteAttributes( addHierarchyPrefix+'_customAttributesTable', eval(addHierarchyPrefix+'_attributesArray') );
}
// Script for Add Hierarchy Def ENDS


var editHierarchyPrefix = "edithierarchy";    
var edithierarchy_attributesArray = new Array();
function validateEditHierarchyForm() {
    var hierarchyDefName = dojo.byId('hierarchy_edit_name').value;
    var plugin = dojo.byId('hierarchy_edit_plugin').value;
    var hierarchyEffectiveFrom = dojo.byId('hierarchy_edit_effectiveFrom').value;
    var hierarchyEffectiveTo = dojo.byId('hierarchy_edit_effectiveTo').value;
    var description =  dojo.byId('hierarchy_edit_description').value;
        
     /* narahari
      *var description=dijit.byId("hierarchy_edit_description").attr("value"); */ 
    
      if(dojo.byId('hierarchy_edit_name').value=='') 
       {
           alert("Please Enter the Name.");
           dojo.byId('hierarchy_edit_name').focus();
           return false;
       }
       
       if(dojo.byId('hierarchy_edit_plugin').value=='') 
       {
           alert("Please Select the Plugin.");
           dojo.byId('hierarchy_edit_plugin').focus();
           return false;
       }
       // Yet to be implemented. Make DWR call
    var domain = dojo.byId("domain").value;
    
    var hierarchydef = {name:hierarchyDefName };
    hierarchydef.domain = domain;
    hierarchydef.plugin = plugin;
    hierarchydef.description = description;

    // fixed attributes
    var effectiveFrom = dojo.byId(editHierarchyPrefix + "_EffectiveFrom").checked;
    var effectiveFromRequired = dojo.byId(editHierarchyPrefix + "_EffectiveFromRequired").checked;
    var effectiveTo = dojo.byId(editHierarchyPrefix + "_EffectiveTo").checked;
    var effectiveToRequired = dojo.byId(editHierarchyPrefix + "_EffectiveToRequired").checked;
    var purgeDate = dojo.byId(editHierarchyPrefix + "_PurgeDate").checked;
    var purgeDateRequired = dojo.byId(editHierarchyPrefix + "_PurgeDateRequired").checked;

    hierarchydef.startDate = effectiveFrom;
    if(effectiveFrom) hierarchydef.startDateRequired = effectiveFromRequired; else hierarchydef.startDateRequired = false;
    hierarchydef.endDate = effectiveTo;
    if(effectiveTo) hierarchydef.endDateRequired = effectiveToRequired; else hierarchydef.endDateRequired = false;
    hierarchydef.purgeDate = purgeDate;
    if(purgeDate) hierarchydef.purgeDateRequired = purgeDateRequired; else hierarchydef.purgeDateRequired = false;
    
    //alert("Start date sending is : " + hierarchydef.startDate + "\n" + " -- required is: " + hierarchydef.startDateRequired);
    //alert("end date sending is : " + hierarchydef.endDate + "\n" + " -- required is: " + hierarchydef.endDateRequired);
    //alert("purge date sending is : " + hierarchydef.purgeDate + "\n" + " -- required is: " + hierarchydef.purgeDateRequired);
    
    // Custom attributes
    var customAttributesArray = eval(editHierarchyPrefix + "_attributesArray");
    //showValues(customAttributesArray);
    
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
        //alert("tempAttr.isRequired="+tempAttr.isRequired+" tempAttr.searchable"+tempAttr.searchable);
        tempAttr.id = attr.IdField.value;
        customAttributes.push(tempAttr);
    }

    hierarchydef.extendedAttributes = customAttributes;
    //alert("extended attr: " + hierarchydef.extendedAttributes);
    
    HierarchyDefHandler.updateHierarchyDef(hierarchydef, loadHierarchyDefs );

    // Close this dialog
    dijit.byId('edithierarchy').hide();       
}

function populateEditHierarchyDefForm(data) {
    //alert("editing " + data);
    showHierarchyDialog('edithierarchy');      
    if(data != null) {      
        dojo.byId("hierarchy_edit_name").value = data.name;
        dojo.byId("hierarchy_edit_plugin").value = data.plugin;
        dojo.byId("hierarchy_edit_description").value = data.description;
        
           
       /* narahari
         dijit.byId("hierarchy_edit_description").attr("value", ""); 
        dijit.byId("hierarchy_edit_description").attr("value", data.description);
       */             
         
        
        //alert("Start date got is: " + data.startDate + " Required: " + data.startDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_EffectiveFrom"), 
            dijit.byId(editHierarchyPrefix+"_EffectiveFromRequired"), getBoolean(data.startDate), getBoolean(data.startDateRequired) );

        //alert("end date got is: " + data.endDate + " Required: " + data.endDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_EffectiveTo"), 
            dijit.byId(editHierarchyPrefix+"_EffectiveToRequired"), getBoolean(data.endDate), getBoolean(data.endDateRequired));

        //alert("Purge date got is: " + data.purgeDate + " Required: " + data.purgeDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_PurgeDate"), 
            dijit.byId(editHierarchyPrefix+"_PurgeDateRequired"), getBoolean(data.purgeDate), getBoolean(data.purgeDateRequired) );
        
        // custom atributes populating
        createCustomAttributes (data, editHierarchyPrefix+'_customAttributesTable', eval(editHierarchyPrefix+'_attributesArray'), editHierarchyPrefix );
        refreshCustomAttributesButtonsPalette(eval(editHierarchyPrefix+'_attributesArray'), editHierarchyPrefix );
    }

}
