<%-- 
    Document   : edit_relationship_attributes
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" type="text/css" href="css/manage.css" />

<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>

<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
<script>
    
    dojo.require("dijit.TitlePane"); 
    dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.DateTextBox");
    dojo.require("dojo.parser");

</script>
<body class="mdwm">
<div dojoType="dijit.GenericTitlePane" class="MainBox"  id="relationshipRecordDetailsPane"
    title="<f:message key="relationship_attributes_text" /><f:message key="colon_symbol" />">
    
    <div class="Content" id="editAttributesDiv" style="display:none;">
        <form name="editRelationshiAttributesForm">
      <table border="0" width="100%">
        <tr>
            <td>
                <div class="RelationshipAttributes" id="editCustomAttributesDiv" style="display:none;">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="2">
                                <f:message key="custom_attributes" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table border="0"  cellspacing="0">
                                    <tbody id="editCustomAttributesTable"></tbody>
                                </table>
                            </td>
                        </tr>
                        <!--
                        <tr>
                            <td class="label">
                                &nbsp;&nbsp;<f:message key="number_visit_text" /><f:message key="colon_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="25"></td>
                            <td>
                                <input name="Number Visits" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                            </td>
                        </tr>-->
                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="RelationshipAttributes" id="editPredefinedAttributesDiv" style="display:none;">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="4">
                                <f:message key="predefined_attributes" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label" colspan="4">
                                <table border="0" id="editPredefinedAttributesTable"></table>
                            </td>
                        </tr>
                        <!--
                        <tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
                        <tr>
                            <td class="label" valign="top">
                                &nbsp;&nbsp;<f:message key="effective_text" /><f:message key="colon_symbol" />
                            </td>
                            <td class="label" valign="top">
                                <f:message key="from_text" /><f:message key="colon_symbol" />
                            </td>
                            <td valign="top" class="label">
                                <input name="EffectiveFrom"  title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                            <td class="label" valign="top">
                                <f:message key="to_text" /><f:message key="colon_symbol" />
                            </td>
                            <td valign="top" class="label">
                                <input name="EffectiveTo"  title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>

                        <tr>
                            <td class="label" valign="top">
                                &nbsp;&nbsp;<f:message key="purge_date_text" /><f:message key="colon_symbol" />
                            </td>
                            <td></td>
                            <td valign="top" class="label">
                                <input name="Purge Date"  title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                        </tr>
                        -->

                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td align="right" width="20%">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td><input type="button" title="<f:message key='save_text' />" value="<f:message key='save_text' />" onclick="updateRelationship();"></td>
                        <td class="button_spacing"></td>
                        <td><input type="button" onclick="document.editRelationshiAttributesForm.reset();" title="<f:message key='revert_text' />" value="<f:message key='revert_text' />" /></td>
                    </tr>
                </table>                            
            </td>
        </tr>
     </table>
     </form>  
   </div>
 </div>
 
 
 
</body>