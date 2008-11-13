<%-- 
    Document   : predefined_attributes
    Created on : Nov 4, 2008, 6:49:40 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
String prefixId = (String) request.getParameter("prefix");
%>
<div dojoType="dijit.layout.ContentPane" class="InnerBox" hasShadow="true">
    <div class="TitleBar"><f:message key="include_these_predefine_attribute_text" /></div>

    <div class="Content">
    <table cellspacing="5" cellpadding="0">
        <tr>
            <td class="formLabel"><input type="checkbox"  onclick="updateRequiredField(this, '<%=prefixId %>_EffectiveFromRequired');" dojoType="dijit.form.CheckBox" name="EffectiveFrom" id="<%=prefixId %>_EffectiveFrom"><f:message key="effective_from_text" /></td> 
            <td><img src="images/spacer.gif" height="1" width="10"></td>
            <td class="formLabel"><input type="checkbox" dojoType="dijit.form.CheckBox" disabled="disabled" name="<%=prefixId %>_EffectiveFromRequired" id="<%=prefixId %>_EffectiveFromRequired"><f:message key="require_text" /></td>
        </tr>
        <tr>
            <td class="formLabel"><input type="checkbox" onclick="updateRequiredField(this, '<%=prefixId %>_EffectiveToRequired');" dojoType="dijit.form.CheckBox" name="EffectiveTo" id="<%=prefixId %>_EffectiveTo"><f:message key="effective_to_text" /></td> 
            <td><img src="images/spacer.gif" height="1" width="10"></td>
            <td class="formLabel"><input  type="checkbox" dojoType="dijit.form.CheckBox" disabled="disabled" name="<%=prefixId %>_EffectiveToRequired" id="<%=prefixId %>_EffectiveToRequired"><f:message key="require_text" /></td>
        </tr>
        <tr>
            <td class="formLabel"><input type="checkbox" onclick="updateRequiredField(this, '<%=prefixId %>_PurgeDateRequired');" dojoType="dijit.form.CheckBox" name="PurgeDate" id="<%=prefixId %>_PurgeDate"><f:message key="purge_date_text" /></td>                                         
            <td><img src="images/spacer.gif" height="1" width="10"></td>
            <td class="formLabel"><input type="checkbox" dojoType="dijit.form.CheckBox" disabled="disabled" name="<%=prefixId %>_PurgeDateRequired" id="<%=prefixId %>_PurgeDateRequired" ><f:message key="require_text" /></td>
        </tr>
    </table>
    </div>
</div>



<script>
    function updateRequiredField( currentSel, requiredFieldId) {
        var requiredFieldObj = dijit.byId(requiredFieldId);
        if(currentSel.checked) {
            requiredFieldObj.setDisabled(false);
        } else {
            requiredFieldObj.setDisabled(true);
        }

    }
</script>
  