<%-- 
    Document   : custom_attributes
    Created on : Nov 4, 2008, 6:47:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script type='text/javascript' src='scripts/attributes.js'></script>
<%
String prefixId = (String) request.getParameter("prefix");
%>
<script>
    var <%=prefixId%>_attributesArray = new Array(); // array for holding attributes list
    
</script>
<div dojoType="dijit.layout.ContentPane" class="InnerBox" hasShadow="true">
    <div class="TitleBar"><f:message key="define_custom_attribute_text" /></div>

    <div class="Content">
        <table border="0" cellspacing="5" width="100%">
        <tr><td colspan="2" valign="bottom">
            <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td><a href="javascript:void(0);" title="<f:message key='select_all_text' />" onclick="selectAllCustomAttributes(<%=prefixId%>_attributesArray);refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');"><img id="<%=prefixId%>_imgSelectAllAttr" src="images/icons/select_multiple.png" alt="<f:message key='select_all_text' />" border="0"></a></td>
                    <td><img src="images/spacer.gif" height="1" width="6"></td>
                    <td><a href="javascript:void(0);" title="<f:message key='de_select_all_text' />" onclick="deselectAllCustomAttributes(<%=prefixId%>_attributesArray);refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');"><img id="<%=prefixId%>_imgDeselectAllAttr" src="images/icons/deselect_multiple.png" alt="<f:message key='de_select_all_text' />" border="0"></a></td>
                    <td><img src="images/icons/actions_separator.gif" border="0"></td>
                    <td><a href="javascript:void(0);" title="<f:message key='add_text' />..."  onclick="<%=prefixId%>_attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray, '<%=prefixId%>') ); refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');"><img src="images/icons/add_button.png" alt="<f:message key='add_text' />" border="0"></a></td>
                    <td><img src="images/spacer.gif" height="1" width="6"></td>
                    <td><a href="javascript:void(0);" title="<f:message key='delete_text' />" onclick="deleteAttributes('<%=prefixId%>_customAttributesTable',<%=prefixId%>_attributesArray); refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');" ><img id="<%=prefixId%>_imgDeleteCustAttr" src="images/icons/delete_button.png" alt="<f:message key='delete_text' />" border="0"></a></td>
                </tr>
            </table>
           <!-- <input type="button" id="selectallbutton"  onclick="selectAllCustomAttributes(<%=prefixId%>_attributesArray);" title="Select all"  />&nbsp;
            <input type="button" id="deselectallbutton" title="De-select all" onclick="deselectAllCustomAttributes(<%=prefixId%>_attributesArray);" /><img src="images/icons/actions_separator.gif" >
            <input type="button" id="addbutton" title="<f:message key="add_text" />..."  onclick="<%=prefixId%>_attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray) );" />&nbsp;
            <input type="button" id="deletebutton" title="<f:message key="delete_text" />" onclick="deleteAttributes('<%=prefixId%>_customAttributesTable',<%=prefixId%>_attributesArray);" />
            -->
        </td></tr>
        <tr><td width="90%" valign="top">
        <div class="ScrollablePane">   
        <table  class="AttributesListing" border="0" cellpadding="3" cellspacing="0" width="100%">
            <thead class="header">
                <tr>
                <th width="7%">&nbsp;</th>
                <th width="30%" align="left" title="<f:message key='attribute_text' />" class="formLabel"><f:message key='attribute_text' /></th>
                <th width="20%" align="left" title="<f:message key='type_text' />" class="formLabel"><f:message key='type_text' /></th>
                <th width="20%" align="left" title="<f:message key='default_value_text' />" nowrap class="formLabel"><f:message key='default_value_text' /></th>
                <th width="11%" align="left" title="<f:message key='require_text' />" class="formLabel"><f:message key='require_text' /></th>
                <th width="12%" align="left" title="<f:message key='searchable_text' />" class="formLabel"><f:message key='searchable_text' /></th>
            </tr>
            </thead>
            <tbody id="<%=prefixId%>_customAttributesTable">
            </tbody>
        </table>
    </div>
    </td>
    <td width="10%"><input type="button" title="<f:message key='move_up_text' />" value="<f:message key='move_up_text' />" id ="<%=prefixId%>_moveUpButton" style="width:80px;" onclick="MoveUp('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray); refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');">
    <br><br><input type="button" title="<f:message key='move_down_text' />" value="<f:message key='move_down_text' />" id ="<%=prefixId%>_moveDownButton" style="width:80px;" onclick="MoveDown('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray); refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, '<%=prefixId%>');">
    </td></tr>
    </table>
    </div>
</div>

       
<script>
    
    refreshCustomAttributesButtonsPalette (<%=prefixId%>_attributesArray, "<%=prefixId%>");
</script>