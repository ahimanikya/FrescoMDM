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
    messages ["effectiveFrom"] = "Effective From";
    messages ["enterValueFor"] = "Enter value for";    
    
    


</script>
