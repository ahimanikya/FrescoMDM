<%-- 
    Document   : custom_attributes
    Created on : Nov 4, 2008, 6:47:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
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
            <input type="button" id="selectallbutton" onclick="selectAllCustomAttributes(<%=prefixId%>_attributesArray);" title="Select all"  />&nbsp;
            <input type="button" id="deselectallbutton" title="De-select all" onclick="deselectAllCustomAttributes(<%=prefixId%>_attributesArray);" /><img src="images/icons/actions_separator.gif" >
            <input type="button" id="addbutton" title="<f:message key="add_text" />..."  onclick="<%=prefixId%>_attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray) );" />&nbsp;
            <input type="button" id="deletebutton" title="<f:message key="delete_text" />" onclick="deleteAttributes('<%=prefixId%>_customAttributesTable',<%=prefixId%>_attributesArray);" />
        </td></tr>
        <tr><td width="90%" valign="top">
        <div class="ScrollablePane">   
        <table  class="AttributesListing" border="0" cellpadding="3" cellspacing="0" width="100%">
            <thead class="header">
                <tr>
                <th>&nbsp;</th>
                <th align="left" class="formLabel">Attribute</th>
                <th align="left" class="formLabel">Type</th>
                <th align="left" nowrap class="formLabel">Default Value</th>
                <th align="left" class="formLabel">Required</th>
                <th align="left" class="formLabel">Searchable</th>
            </tr>
            </thead>
            <tbody id="<%=prefixId%>_customAttributesTable">
            </tbody>
        </table>
    </div>
    </td>
    <td width="10%"><input type="button" value="Move Up" style="width:80px;" onclick="MoveUp('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray);">
    <br><br><input type="button" value="Move Down" style="width:80px;" onclick="MoveDown('<%=prefixId%>_customAttributesTable', <%=prefixId%>_attributesArray);">
    </td></tr>
    </table>
    </div>
</div>

       
        
