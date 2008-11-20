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
<div dojoType="dijit.TitlePane" class="EditAttributes"  title="<f:message key="relationship_attributes_text" /><f:message key="colon_symbol" />" jsId="pane1">
    <div class="TitleBar" title="<f:message key="relationship_attributes_text" />"><f:message key="relationship_attributes_text" /><f:message key="colon_symbol" /></div>
    
    
    <div class="Content">
      <table border="0" width="100%">
        <tr>
            <td>
                <div class="TopRow">
                    <table border="0">
                        <tr>
                            <td class="mainLabel">
                                <f:message key="custom_text" /> <f:message key="attributes_text" />
                            </td>
                        </tr>
                        <tr>
                            <td class="subLabel">
                                &nbsp;&nbsp;<f:message key="number_visit_text" /><f:message key="colon_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="25"></td>
                            <td>
                                <input name="Number Visits" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                            </td>
                        </tr>
                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="BottomRow">
                    <table border="0">
                        <tr>
                            <td class="mainLabel">
                                <f:message key="relationship_life_text" />
                            </td>
                        </tr>
                        <tr>
                            <td class="subLabel">
                                &nbsp;&nbsp;<f:message key="effective_text" /><f:message key="colon_symbol" />
                            </td>
                            <td class="subLabel">
                                <f:message key="from_text" /><f:message key="colon_symbol" />
                            </td>
                            <td>
                                <input name="EffectiveFrom"  title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                            </td>
                            <td class="subLabel">
                                <f:message key="to_text" /><f:message key="colon_symbol" />
                            </td>
                            <td>
                                <input name="EffectiveTo"  title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                            </td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td>mm/dd/yyyy</td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td>mm/dd/yyyy</td>
                        </tr>
                        <tr>
                            <td colspan="1" class="subLabel">
                                &nbsp;&nbsp;<f:message key="purge_date_text" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td colspan="1">
                                <input name="Purge Date"  title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                            </td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td>mm/dd/yyyy</td>
                        </tr>
                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td align="right" width="20%">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td><input type="button" title="<f:message key="save_text" />" value="<f:message key="save_text" />"/></td>
                        <td class="button_spacing"></td>
                        <td><input type="button" title="<f:message key="revert_text" />" value="<f:message key="revert_text" />" /></td>
                    </tr>
                </table>                            
            </td>
        </tr>
     </table>
   </div>
 </div>
 
 
 
</body>