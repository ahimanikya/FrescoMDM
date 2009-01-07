<%-- 
    Document   : edit_relationship_attributes
    Created on : Dec 30, 2008, 10:10:45 AM
    Author     : Harish,Narahari
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page import="java.util.Date" %>
<link rel="stylesheet" type="text/css" href="css/manage.css" />

<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>
<%
String prefix = (String) request.getParameter("prefix");
Date d = new Date();
if(prefix == null) prefix = "pre" + d.getTime();
%>

<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
<script>
    
    dojo.require("dijit.TitlePane"); 
    dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.DateTextBox");
    dojo.require("dojo.parser");

</script>
<body class="mdwm">
<div dojoType="dijit.GenericTitlePane" class="MainBox"  id="<%=prefix%>_relationshipRecordDetailsPane"
    title="<f:message key="relationship_attributes_text" /><f:message key="colon_symbol" />">
    
    <div class="Content" id="<%=prefix%>_byRecordEditAttributesDiv" style="display:none;">
        <form name="<%=prefix%>byRecordEditRelationshiAttributesForm">
      <table border="0" width="100%">
        <tr>
            <td>
                <div class="RelationshipAttributes" id="<%=prefix%>_byRecordEditCustomAttributesDiv" style="display:none;">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="2">
                                <f:message key="custom_attributes" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table border="0"  cellspacing="0">
                                    <tbody id="<%=prefix%>_byRecordEditCustomAttributesTable"></tbody>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="RelationshipAttributes" id="<%=prefix%>_byRecordEditPredefinedAttributesDiv" style="display:none;">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="4">
                                <f:message key="predefined_attributes" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label" colspan="4">
                                <table border="0" id="<%=prefix%>_byRecordEditPredefinedAttributesTable"></table>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td align="right" width="20%">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td><input type="button" title="<f:message key='save_text' />" value="<f:message key='save_text' />"></td>
                        <td class="button_spacing"></td>
                        <td><input type="button" onclick="document.<%=prefix%>byRecordEditRelationshiAttributesForm.reset();" title="<f:message key='revert_text' />" value="<f:message key='revert_text' />" /></td>
                    </tr>
                </table>                            
            </td>
        </tr>
     </table>
     </form>  
   </div>
 </div>
 
 
 
</body>