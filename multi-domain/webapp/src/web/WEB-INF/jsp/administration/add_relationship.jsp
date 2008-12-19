<%-- 
    Document   : add_relationship
    Created on : Nov 3, 2008, 10:20:58 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
    String prefixToUse = "addrelationship";
    String dateFormat =  (String)session.getAttribute("mdwm_date_format");
    String dateInputMask = (String)session.getAttribute("mdwm_date_input_mask");
%>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
  dojo.require("dijit.form.TextBox");
  dojo.require("dijit.form.CheckBox");
  dojo.require("dijit.form.ValidationTextBox");
  dojo.require("dijit.Dialog");
  dojo.require("dojo.parser");
  dojo.require("dijit.form.FilteringSelect");
 

</script>

<table border="0" width="100%" cellpadding="0" cellspacing="0">
   
    <tr>
        <td class="formLabel"><f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" /></td> 
        <td>
            <table  cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                       <input id="relationship_add_name" type="text" name="<f:message key="name_text" />" value="" title="<f:message key="name_text" />" style="width:150px"/>
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                    <f:message key="direction_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                       <!--  <select id="temp" name="Direction" title="<f:message key="direction_text" />"  style="width:75px">
                            <option value=""></option>
                            <option value="false">-></option>
                            <option value="true"><-></option>
                         </select>
                       -->
                        <a href="javascript:void(0);" title="<f:message key="one_direction_text" />" onclick="refreshDirection(false,'relationship_add_direction','addRelationship_oneDirectionImg','addRelationship_biDirectionImg');"><img id="addRelationship_oneDirectionImg" src="images/icons/relationship-button_right.png" alt="<f:message key="one_direction_text" />" border="0"></a>
                        <a href="javascript:void(0);" title="<f:message key="bi_direction_text" />" onclick="refreshDirection(true,'relationship_add_direction','addRelationship_oneDirectionImg','addRelationship_biDirectionImg');"><img id="addRelationship_biDirectionImg" src="images/icons/relationship-button_both.png" alt="<f:message key="bi_direction_text" />" border="0"></a>
                         
                         <input type="hidden" value="false" id="relationship_add_direction" >
                   </td>
                   <td><img src="images/spacer.gif" height="1" width="15"></td>
                   <td class="formLabel">
                        <f:message key="plugin_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />
                         <select id="relationship_add_plugin"  name="<f:message key="plugin_text" />" title="<f:message key="plugin_text" />"  style="width:150px">
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
        <td><textarea id="relationship_add_description" name="<f:message key="desctription_text" />" rows="3" cols="70" title="<f:message key="desctription_text" />"></textarea></td> 
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    
    <tr>
        <td colspan="2">
            <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true">
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
                                <jsp:include  page=
                                    "/WEB-INF/jsp/administration/custom_attributes.jsp?prefix=addrelationship&date_format=<%=dateFormat%>&date_input_mask=<%=dateInputMask%>"  
                                  flush="true" />
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
            <input type="submit" name="<f:message key="save_text" />" onclick=" return validateRelationshipForm()" title="<f:message key="save_text" />" value="<f:message key="save_text" />" />
            <input type="button" name="<f:message key="cancel_text" />" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('addrelationship').hide(); clearAddRelationshipForm (); "/>
        </td>
    </tr>
    
</table>





<script language="javascript" 
  type="text/javascript">


</script>


