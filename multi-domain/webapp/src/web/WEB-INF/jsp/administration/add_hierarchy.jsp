<%-- 
    Document   : add_hierarchy
    Created on : Nov 5, 2008, 2:26:50 PM
    Author     : Narahari
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
    String prefixToUse = "addhierarchy";
%>
<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <tr>
        <td class="formLabel" >
            <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
        </td>
        <td>
            <input id="hierarchy_add_name"  name="Name" value="" title="<f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
        </td> 
        <td class="formLabel">
           <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
        </td>
        <td>
           <select id="hierarchy_add_plugin" name="Plugin" title="<f:message key="plugin_text" />" hasDownArrow="true" style="width:150px">
               <option value=""></option>
                <option value="HierPrunePlugin">HierPrunePlugin</option>
           </select>
         </td>
    <tr><td colspan="4"><img src="images/spacer.gif" height="5" width="1"></td></tr>
    <tr>
        <td class="formLabel">
            <f:message key="effective_from_text" /><f:message key="colon_symbol" />
        </td>
        <td>
            <input id="hierarchy_add_effectiveFrom"  name="EffectiveFrom" value="" title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
        </td> 
        <td class="formLabel">
           <f:message key="effective_to_text" /><f:message key="colon_symbol" />
       </td>
        <td>
           <input id="hierarchy_add_effectiveTo"  name="EffectiveTo" value="" title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
         </td>
    </tr>
    <tr><td colspan="4"><img src="images/spacer.gif" height="5" width="1"></td></tr>
    <tr> 
        <td valign="top" class="formLabel"><f:message key="desctription_text" /><f:message key="colon_symbol" /></td> 
        <td colspan="3"><textarea id="hierarchy_add_description" name="Description" rows="3" cols="70" title="<f:message key="desctription_text" />"></textarea></td>
    </tr>
    <tr><td colspan="4"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="4">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true">
                <div class="TitleBar"><f:message key="node_attribute_text" /></div>
                <div class="Content">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td>
                                <!-- Pre-defined attributes section -->
                                <jsp:include page="/WEB-INF/jsp/administration/predefined_attributes.jsp?prefix=addhierarchy" flush="true" />
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                        <tr>
                            <td>
                                <!-- Custom attributes section -->
                                <jsp:include page="/WEB-INF/jsp/administration/custom_attributes.jsp?prefix=addhierarchy" flush="true" />
                            
                            </td>
                        </tr>
                       
                    </table>
                </div>
              </div>
        </td>
    </tr>
    <tr><td colspan="4"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    <tr>
        <td align="right" colspan="4">
            <input type="submit" name="save_add_hierarchy" onclick="return validateHierarchyForm();" title="<f:message key="save_text" />" value="<f:message key="save_text" />"/>
            <input type="button" name="cancel_add_hierarchy" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('addhierarchy').hide();clearAddHierarchyForm();"/>
        </td>
    </tr>
</table> 
