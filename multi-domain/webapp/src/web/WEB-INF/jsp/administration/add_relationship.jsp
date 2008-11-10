<%-- 
    Document   : add_relationship
    Created on : Nov 3, 2008, 10:20:58 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.CheckBox");
  dojo.require("dijit.form.ValidationTextBox");
  dojo.require("dijit.Dialog");
  dojo.require("dojo.parser");
  dojo.require("dijit.form.FilteringSelect");

</script>


<form action="#" method="GET" name="addRelationshipForm" onSubmit="return validateRelationshipForm(this);">
<table border="0" width="100%" cellpadding="0" cellspacing="0">
    
    <tr>
        <td class="formLabel"><f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" /></td> 
        <td>
            <table  cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                       <input  type="text" name="Name" value="" title="<f:message key="name_text" />" style="width:150px"/>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                    <f:message key="direction_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select  name="Direction" title="<f:message key="direction_text" />"  style="width:75px">
                            <option value=""></option>
                            <option value="->">-></option>
                            <option value="<->"><-></option>
                         </select>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                        <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select  name="Plugin" title="<f:message key="plugin_text" />"  style="width:150px">
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
        <td> <textarea dojoType="dijit.form.Textarea"  style="height:50px;width:575px;" title="<f:message key="desctription_text" />"></textarea></td> 
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="2">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true" style="width:100%;">
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
            <input type="submit" name="submit_add_relations" title="<f:message key="save_text" />" value="<f:message key="save_text" />" />
            <input type="button" name="cancel_add_relations" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('addrelationship').hide();"/>
        </td>
    </tr>
    
</table> 
</form>
<!--
<div id="errorDialog" class="MainBox" dojoType="dijit.Dialog" title="Error Message" style="display:none;width:500px;">
    <table>
        <tr>
            <td>
                <b>Please Enter the Name</b>
            </td>
        </tr>
        <tr>
            <td align="center">
               <input type="button" value="ok" onclick="dijit.byId('errorDialog').hide();"/>
            </td>
        </tr>
    </table>
</div>
-->

<script language="javascript" 
  type="text/javascript">
      
function validateRelationshipForm(objForm) {
    if(""==objForm.Name.value) {
        alert("Please Enter the Name.");
        return false;
    }
    if(""==objForm.Direction.value) {
        alert("Please Select the Direction.");
        return false;
    }
    if(""==objForm.Plugin.value) {
        alert("Please Select the Plugin.");
        return false;
    }
    return true;
}
</script>


