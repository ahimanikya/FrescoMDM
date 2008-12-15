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
    messages ["effectiveFrom"] = '<f:message key="effective_from_text" />';
    messages ["enterValueFor"] = '<f:message key="enter_value_for_text" />';
    messages ["effectiveTo"] = '<f:message key="effective_to_text" />';
    messages ["purgeDate"] = '<f:message key="purge_date_text" />';
    messages ["isnotavalidvaluefor"] = '<f:message key="valid_value_text" />';
    messages ["attribute"] = '<f:message key="lower_attribute_text" />';
    messages ["SelectAtleastOneRecord"] = '<f:message key="select_atleast_one_record_text" />';
    messages ["fromSourceDomain"] = '<f:message key="lower_from_text" />' + '<f:message key="source_domain_text" />' + '<f:message key="period_symbol" />';
    messages ["fromTargetDomain"] = '<f:message key="lower_from_text" />' + '<f:message key="target_domain_text" />' + '<f:message key="period_symbol" />';
    messages ["selectsourcedomain"] = '<f:message key="select_text" />' + '<f:message key="source_domain_text" />' + '<f:message key="period_symbol" />';
    messages ["selecttargetdomain"] = '<f:message key="select_text" />' + '<f:message key="target_domain_text" />' + '<f:message key="period_symbol" />';
    messages ["selectarelationshipDef"] = '<f:message key="select_relationshipDef_text" />';
    messages ["fromthelist"] = '<f:message key="from_the_list_text" />' + '<f:message key="period_symbol" />';
    messages ["effective"] = '<f:message key="effective_text" />';
    messages ["from"] = '<f:message key="from_text" />';
    messages ["to"] = '<f:message key="to_text" />';
    messages ["mandatorySymbol"] = '<f:message key="mandatory_symbol" />';
    messages ["period"] = '<f:message key="period_symbol" />';
    
    
   messages ["exception"] = '<f:message key="exception_text" />' + '<f:message key="colon_symbol" />';

</script>
