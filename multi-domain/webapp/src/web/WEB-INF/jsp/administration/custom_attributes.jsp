<%-- 
    Document   : custom_attributes
    Created on : Nov 4, 2008, 6:47:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
String prefixId = (String) request.getParameter("prefix");
%>
<div dojoType="dijit.layout.ContentPane" class="InnerBox" hasShadow="true" style="width:100%;height:150px;">
    <div class="TitleBar"><f:message key="define_custom_attribute_text" /></div>

    <div class="Content">
        <input type="button" value='<f:message key="add_text" />' onclick="attributesArray.push( new NewAttribute('<%=prefixId%>_customAttributesTable') );" />
        <input type="button" value="<f:message key="delete_text" />"  onclick="deleteAttributes();"/> <br> 
        <form name="customAttributesForm">
        <table border="1" cellspacing="5" width="100%">
       
       <tr><td width="90%" valign="top">
        <table id="<%=prefixId%>_customAttributesTable" class="AttributesListing" border="0" cellpadding="3" cellspacing="0" width="100%">
            <thead class="header">
                <th>&nbsp;</th>
                <th align="left">Attribute</th>
                <th align="left">Type</th>
                <th align="left" nowrap>Default Value</th>
                <th align="left">Required</th>
                <th align="left">Selectable</th>
            </thead>
                 
        </table>
    </td>
    <td width="10%"><input type="button" value="Move Up" onclick="MoveUp('<%=prefixId%>_customAttributesTable');"><br><input type="button" value="Move Down"  onclick="MoveDown('<%=prefixId%>_customAttributesTable');">
    </td></tr>
    
    </table></form>
        <br>

    </div>
</div>

       
        
