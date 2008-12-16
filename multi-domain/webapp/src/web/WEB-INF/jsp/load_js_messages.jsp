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
    messages ["enterValueFor_effectiveFrom"] = '<f:message key="enter_value_for_effective_from" />';
    messages ["enter_ValueFor"] = '<f:message key="enter_value_for_text" />';
    messages ["enterValueFor_effectiveTo"] = '<f:message key="enter_value_for_effective_to" />';
    messages ["enterValueFor_purgeDate"] = '<f:message key="enter_value_for_purgedate" />';
    messages ["isnotavalidvaluefor"] = '<f:message key="is_not_valid_value_for" />';
    messages ["attribute"] = '<f:message key="lower_attribute_text" />';
    messages ["is"] = '<f:message key="is_text" />';
    messages ["attributeTypeFor"] = '<f:message key="attribute_type_for" />';
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
    messages ["enter_name"] = '<f:message key="enter_name" />';
    messages ["select_direction"] = '<f:message key="select_direction" />';
    messages ["select_plugin"] = '<f:message key="select_plugin" />';
    messages ["select_source_domain"] = '<f:message key="select_source_domain" />';
    messages ["select_destination_domain"] = '<f:message key="select_destination_domain" />';
    messages ["select_relationshipDef"] = '<f:message key="select_relationshipDef" />';
    messages ["select_atleast_one_source_record"] = '<f:message key="select_atleast_one_source_record" />';
    messages ["select_atleast_one_destination_record"] = '<f:message key="select_atleast_one_destination_record" />';
    
   messages ["exception"] = '<f:message key="exception_text" />';

</script>
