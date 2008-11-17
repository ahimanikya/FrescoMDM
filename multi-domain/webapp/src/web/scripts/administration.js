
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
