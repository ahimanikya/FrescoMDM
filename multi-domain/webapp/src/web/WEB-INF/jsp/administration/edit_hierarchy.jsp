<%-- 
    Document   : edit_hierarchy
    Created on : Nov 6, 2008, 4:35:22 PM
    Author     : Narahari
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
    String prefixToUse = "edithierarchy";
%>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.DateTextBox");
  dojo.require("dijit.form.Textarea");
  dojo.require("dojo.parser");
  dojo.require("dijit.form.FilteringSelect");

</script>



<table border="0" width="100%" cellpadding="0" cellspacing="0">
    
    <tr>
        <td class="formLabel" width="200px">
            <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
        </td>
        <td>
            <input id="hierarchy_edit_name" name="Name" value="" title="<f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
        </td> 
        <td class="formLabel">
           <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
        </td>
        <td>
           <select id="hierarchy_edit_plugin" name="Plugin" title="<f:message key="plugin_text" />" hasDownArrow="true" style="width:150px">
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
            <input  name="name" value="" title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
        </td> 
        <td class="formLabel">
           <f:message key="effective_to_text" /><f:message key="colon_symbol" />
       </td>
        <td>
           <input  name="name" value="" title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
         </td>
    </tr>
    <tr><td colspan="4"><img src="images/spacer.gif" height="5" width="1"></td></tr>
    <tr> 
        <td valign="top" class="formLabel"><f:message key="desctription_text" /><f:message key="colon_symbol" /></td> 
        <td colspan="3"> <textarea id="hierarchy_edit_description" dojoType="dijit.form.Textarea" style="height:50px;width:575px;" title="<f:message key="desctription_text" />"></textarea></td> 
    </tr>
    <tr><td colspan="4"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="4">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true" style="width:100%;">
                <div class="TitleBar"><f:message key="node_attribute_text" /></div>
                <div class="Content">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td>
                                <!-- Pre-defined attributes section -->
                                <jsp:include page="/WEB-INF/jsp/administration/predefined_attributes.jsp?prefix=edithierarchy" flush="true" />
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                        <tr>
                            <td>
                                <!-- Custom attributes section -->
                                <jsp:include page="/WEB-INF/jsp/administration/custom_attributes.jsp?prefix=edithierarchy" flush="true" />
                            
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
            <input type="submit" name="save_edit_hierarchy" onclick="return validateEditHierarchyForm();" title="<f:message key="save_text" />" value="<f:message key="save_text" />"/>
            <input type="button" name="cancel_edit_hierarchy" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('edithierarchy').hide();"/>
        </td>
    </tr>
</table> 


<script language="javascript" 
  type="text/javascript">
var editHierarchyPrefix = "<%=prefixToUse%>";    
function validateEditHierarchyForm() {
    
      if(dojo.byId('hierarchy_edit_name').value=='') 
       {
           alert("Please Enter the Name.");
           dojo.byId('hierarchy_edit_name').focus();
           return false;
       }
       
       if(dojo.byId('hierarchy_edit_plugin').value=='') 
       {
           alert("Please Select the Plugin.");
           dojo.byId('hierarchy_edit_plugin').focus();
           return false;
       }
}

function populateEditHierarchyDefForm(data) {
    //alert("editing " + data);
    if(data != null) {      
        dojo.byId("hierarchy_edit_name").value = data.name;
        dojo.byId("hierarchy_edit_plugin").value = data.plugin;
        dijit.byId("hierarchy_edit_description").attr("value", ""); 
        dijit.byId("hierarchy_edit_description").attr("value", data.description); 
        
        //alert("Start date got is: " + data.startDate + " Required: " + data.startDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_EffectiveFrom"), 
            dijit.byId(editHierarchyPrefix+"_EffectiveFromRequired"), getBoolean(data.startDate), getBoolean(data.startDateRequired) );

        //alert("end date got is: " + data.endDate + " Required: " + data.endDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_EffectiveTo"), 
            dijit.byId(editHierarchyPrefix+"_EffectiveToRequired"), getBoolean(data.endDate), getBoolean(data.endDateRequired));

        //alert("Purge date got is: " + data.purgeDate + " Required: " + data.purgeDateRequired);
        populatePredefinedAttributeField(dijit.byId(editHierarchyPrefix+"_PurgeDate"), 
            dijit.byId(editHierarchyPrefix+"_PurgeDateRequired"), getBoolean(data.purgeDate), getBoolean(data.purgeDateRequired) );
        
        // custom atributes populating
        createCustomAttributes (data, editHierarchyPrefix+'_customAttributesTable', eval(editHierarchyPrefix+'_attributesArray'), editHierarchyPrefix );
        refreshCustomAttributesButtonsPalette(eval(editHierarchyPrefix+'_attributesArray'), editHierarchyPrefix );
    }
    showHierarchyDialog('edithierarchy');  
}
</script>
