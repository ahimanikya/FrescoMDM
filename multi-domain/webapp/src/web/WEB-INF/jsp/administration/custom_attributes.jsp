<%-- 
    Document   : custom_attributes
    Created on : Nov 4, 2008, 6:47:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
String prefixId = (String) request.getParameter("prefix");
%>
<div dojoType="dijit.layout.ContentPane" class="InnerBox" hasShadow="true">
    <div class="TitleBar"><f:message key="define_custom_attribute_text" /></div>

    <div class="Content">
        &nbsp;&nbsp;<a href="#" title='<f:message key="add_text" />' onclick="attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable') );" ><img src="images/icons/add_button.png" border="0" alt='<f:message key="add_text" />'></a>
        &nbsp;<a href="#" title='<f:message key="delete_text" />' onclick="deleteAttributes();" ><img src="images/icons/delete_button.png" border="0" alt='<f:message key="delete_text" />'></a> 
       <!-- <input type="button" value='<f:message key="add_text" />' onclick="attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable') );" />
        <input type="button" value="<f:message key="delete_text" />"  onclick="deleteAttributes();"/> <br> -->
        <table border="0" cellspacing="5" width="100%">
         <tr><td width="90%" valign="top">
        <div class="ScrollablePane">   
        <table id="<%=prefixId%>_customAttributesTable" class="AttributesListing" border="0" cellpadding="3" cellspacing="0" width="100%">
            <thead class="header">
                <tr>
                <th>&nbsp;</th>
                <th align="left" class="formLabel">Attribute</th>
                <th align="left" class="formLabel">Type</th>
                <th align="left" nowrap class="formLabel">Default Value</th>
                <th align="left" class="formLabel">Required</th>
                <th align="left" class="formLabel">Selectable</th>
            </tr>
            </thead>
            <tbody >
            </tbody>
        </table>
    </div>
    </td>
    <td width="10%"><input type="button" value="Move Up" style="width:80px;" onclick="MoveUp('<%=prefixId%>_customAttributesTable');"><br><br><input type="button" value="Move Down" style="width:80px;" onclick="MoveDown('<%=prefixId%>_customAttributesTable');">
    </td></tr>
    </table>
    </div>
</div>

       
        
