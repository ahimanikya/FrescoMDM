<%-- 
    Document   : edit_relationship
    Created on : Nov 6, 2008, 4:34:25 PM
    Author     : Narahari
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dojo.parser");
  dojo.require("dijit.form.FilteringSelect");

</script>



<table border="0" width="100%" cellpadding="0" cellspacing="0">
    <form>
    <tr>
        <td class="formLabel"><f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" /></td> 
        <td>
            <table  cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                       <input  name="name" value="" dojoType="dijit.form.TextBox" style="width:150px"/>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                    <f:message key="direction_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select  dojoType="dijit.form.FilteringSelect" name="Direction" title="<f:message key="direction_text" />" hasDownArrow="true" style="width:75px">
                            <option value="->">-></option>
                            <option value="<-"><-</option>
                            <option value="<->"><-></option>
                         </select>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                        <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select  dojoType="dijit.form.FilteringSelect" name="Plugin" title="<f:message key="plugin_text" />" hasDownArrow="true" style="width:150px">
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
                <div class="TitleBar">RelationShip Attributes</div>
                <div class="Content">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td>
                                <!-- Pre-defined attributes section -->
                                
                                <jsp:include page="/WEB-INF/jsp/administration/predefined_attributes.jsp?prefix=editrelationship" flush="true" />
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                        <tr>
                            <td>
                                <!-- Custom attributes section -->
                                <jsp:include  page="/WEB-INF/jsp/administration/custom_attributes.jsp?prefix=editrelationship" flush="true" />
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
            <input type="button" value="<f:message key="save_text" />"/>
            <input type="button" value="<f:message key="cancel_text" />" onclick="dijit.byId('editrelationship').hide();"/>
        </td>
    </tr>
    </form>
</table> 


