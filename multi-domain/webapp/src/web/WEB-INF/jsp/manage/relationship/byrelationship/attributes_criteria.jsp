<%-- 
    Document   : attributes_criteria
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>


<div class="MainBox">
  <div class="TitleBar"><f:message key="select_text" /> <f:message key="relationship" /></div>
  <div class="Content">
  <table border="0" width="100%">
    <tr>
        <td class="generalTextBold" colspan="3">
            &nbsp;<f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;&nbsp;
            <select id="select_relationshipDefs" name="select_relationshipDefs" title="<f:message key='name_text' />" onchange="getSelectRelationshipAttributes()">
                <!--<option value="">Select Relationship</option>-->
            </select>
        </td>
    </tr>
    <tr><td><img src="images/spacer.gif" height="3" width="1"></td></tr>
    <tr>
        <td>
            
           <div dojoType="dijit.GenericTitlePane"  title="<f:message key="search_criteria_for_relationhship" /> <f:message key="attributes_text" />" class="MainBox" jsId="pane1">

            <div class="Content">
               <table border="0" width="100%">
                   <tr>
                       <td>
                          <div class="RelationshipAttributes" id="select_Relationship_CustomAtrributes" style="display:none;">
                            <table border="0" >
                               <tr>
                                    <td class="Heading" colspan="5">
                                        <f:message key="custom_text" /> <f:message key="attributes_text" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Heading" colspan="5">
                                        <table border="0" >
                                            <tbody id="byrel_select_customAttributes"></tbody>
                                        </table>
                                    </td>
                                </tr>
                                 <!--<tr>
                                    <td class="label">
                                        &nbsp;&nbsp;<f:message key="number_visit_text" /><f:message key="colon_symbol" />
                                    </td>
                                    <td><img src="images/spacer.gif" height="1" width="15"></td>
                                    <td class="label"><f:message key="between_text" /></td>
                                    <td>
                                        <input name="Between" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:100px"/> 
                                    </td>
                                    <td class="label"><f:message key="to_text" /></td>
                                    <td> <input name="To" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:100px"/> </td>
                                </tr> -->
                            </table>
                          </div>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div class="RelationshipAttributes" id="select_Relationship_PredefinedAtrributes" style="display:none;">
                                <table border="0" >
                                   <tr>
                                        <td class="Heading">
                                            <f:message key="predefined_text" /> <f:message key="attributes_text" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="label">
                                            <table border="0" >
                                                <tbody id="byrel_select_predefinedAttributes"></tbody>
                                            </table>
                                        </td>
                                    </tr>
                                <!--     <tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
                                    <tr>
                                        <td class="label" valign="top">
                                            &nbsp;&nbsp;<f:message key="effective_text" /><f:message key="colon_symbol" />
                                        </td>
                                        <td class="label" valign="top">
                                            <f:message key="from_text" /><f:message key="colon_symbol" />
                                        </td>
                                        <td class="label" valign="top">
                                            <input name="EffectiveFrom"  title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                            <br>mm/dd/yyyy
                                        </td>
                                        <td class="label" valign="top">
                                            <f:message key="to_text" /><f:message key="colon_symbol" />
                                        </td>
                                        <td class="label" valign="top">
                                            <input name="EffectiveTo"  title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                            <br>mm/dd/yyyy
                                        </td>
                                    </tr>
                                    <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                                    <tr>
                                        <td class="label" valign="top">
                                            &nbsp;&nbsp;<f:message key="purge_date_text" /><f:message key="colon_symbol" />
                                        </td>
                                        <td></td>
                                        <td class="label" valign="top">
                                            <input name="Purge Date"  title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                            <br>mm/dd/yyyy
                                        </td>
                                    </tr> -->
                                </table>
                           </div>
                       </td>
                   </tr>
               </table>
            </div>
           </div>
        </td>
    </tr>
</table>
</div>
