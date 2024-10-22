<%-- 
    Document   : load_js_messages
    Created on : Dec 11, 2008, 3:20:31 PM
    Author     : Harish
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
   
<!--
JSP files loads all the messages from message.properties, which will be used in javascripts.
-->
<script>
    
    //var message_effectiveFroms = "Effective From";
    //var message_validation_enterValueFor = "Enter value for";
    
    function getMessageForI18N (strKey) {
        if(strKey ==null && messages[strKey] == null)
            return "";
        else return messages [strKey];
    }

    var messages = {};
    
    //General messages
    messages ["attribute"] = '<f:message key="lower_attribute_text" />';
    messages ["is"] = '<f:message key="is_text" />';
    messages ["add_Relationship"] = '<f:message key="add_relationship_text" />';
    messages ["effective"] = '<f:message key="effective_text" />';
    messages ["from"] = '<f:message key="from_text" />';
    messages ["to"] = '<f:message key="to_text" />';
    messages ["purgeDate"] = '<f:message key="purge_date_text" />';
    messages ["mandatorySymbol"] = '<f:message key="mandatory_symbol" />';
    messages ["period"] = '<f:message key="period_symbol" />';
    messages ["colon"] = '<f:message key="colon_symbol" />';
    messages ["relationship_Attributes"] = '<f:message key="relationship_attributes_text" />';
    messages ["no_Relationships_found"] = '<f:message key="no_relatonship_found" />';
    messages ["no_hierarchy_definition_found"] = '<f:message key="no_hierarchy_definistion_found" />';
    messages ["no_relationship_definition_found"] = '<f:message key="no_relationship_definistion_found" />';
    messages ["selected_hierarchy_delete_confirmation"] = '<f:message key="selected_hierarchy_delete_confirmation" />';
    messages ["selcted_relationship_delete_confirmation"] = '<f:message key="selcted_relationship_delete_confirmation" />';
    
    
    //Validation prompts for Manage Screens
    messages ["enterValueFor_effectiveFrom"] = '<f:message key="enter_value_for_effective_from" />';
    messages ["enter_ValueFor"] = '<f:message key="enter_value_for_text" />';
    messages ["enterValueFor_effectiveTo"] = '<f:message key="enter_value_for_effective_to" />';
    messages ["enterValueFor_purgeDate"] = '<f:message key="enter_value_for_purgedate" />';
    messages ["isnotavalidvaluefor"] = '<f:message key="is_not_valid_value_for" />';
    messages ["attributeTypeFor"] = '<f:message key="attribute_type_for" />';
    messages ["select_source_domain"] = '<f:message key="select_source_domain" />';
    messages ["select_destination_domain"] = '<f:message key="select_destination_domain" />';
    messages ["select_relationshipDef"] = '<f:message key="select_relationshipDef" />';
    messages ["select_atleast_one_source_record"] = '<f:message key="select_atleast_one_source_record" />';
    messages ["select_atleast_one_destination_record"] = '<f:message key="select_atleast_one_destination_record" />';
	messages ["select_one_record_from_the_results"] = '<f:message key="select_one_record_from_the_results" />';
    
    
   // Validation prompts for Administer Screens
   
    messages ["enter_name"] = '<f:message key="enter_name" />';
    messages ["select_direction"] = '<f:message key="select_direction" />';
    messages ["select_plugin"] = '<f:message key="select_plugin" />';
    messages ["enter_attribute_name"] = '<f:message key="enter_attribute_name" />';
    
   messages ["invalid_date"] = '<f:message key="invalid_date" />';
   messages ["true_text"] = '<f:message key="true_text" />';
   messages ["false_text"] = '<f:message key="false_text" />';
    // Exception messages
    messages ["exception"] = '<f:message key="exception_text" />';

</script>
