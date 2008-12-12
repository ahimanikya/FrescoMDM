<%-- 
    Document   : add_relationship_attributes
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish, Narahari
--%>

<div class="MainBox">
<div class="TitleBar">
    <table>
        <tr>
            <td>
             <f:message key="relationship_attributes_text" /> <f:message key="colon_symbol" />
           </td>
           <td id="byrel_addRelationshipDef"></td>
        </tr>
   </table> 
</div>
     <div class="Content">
      <table border="0" width="100%">
        <tr>
            <td>
                <div id="add_Relationship_CustomAtrributes" class="RelationshipAttributes">
                    <table border="0" width="100%" >
                       <tr>
                            <td class="Heading" colspan="2">
                                <f:message key="custom_text" /> <f:message key="attributes_text" />
                            </td>
                        </tr>
                        <tr>
                            <td class="Heading" colspan="2">
                                <table border="0"  >
                                    <tbody id="byrel_add_customAttributes"></tbody>
                                </table>
                            </td>
                        </tr>
                       <!-- <tr>
                            <td class="label">
                                &nbsp;&nbsp;<f:message key="number_visit_text" /><f:message key="colon_symbol" /> <f:message key="mandatory_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="25"></td>
                            <td>
                                <input id="byrel_add_numbervisits" name="NumberVisits" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                            </td>
                        </tr> -->
                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div id="add_Relationship_PredefinedAtrributes" class="RelationshipAttributes">
                    <table border="0" width="100%">
                        <tr>
                            <td class="Heading">
                                <f:message key="predefined_text" /><f:message key="attributes_text" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label" >
                                <table border="0" >
                                    <tbody id="byrel_add_predefinedAttributes"></tbody>
                                </table>
                            </td>
                        </tr>
                        <!--<tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
                        <tr>
                            <td class="label" valign="top">
                                &nbsp;&nbsp;<f:message key="effective_text" /><f:message key="colon_symbol" />
                            </td>
                            <td class="label" valign="top">
                                <f:message key="from_text" /><f:message key="colon_symbol" />
                            </td>
                            <td class="label" valign="top">
                                <input id="byrel_add_effectiveFrom" name="EffectiveFrom"  title="<f:message key="effective_from_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                            <td class="label" valign="top">
                                <f:message key="to_text" /><f:message key="colon_symbol" />
                            </td>
                            <td class="label" valign="top">
                                <input id="byrel_add_effectiveTo"  name="EffectiveTo"  title="<f:message key="effective_to_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
                        <tr>
                            <td class="label" valign="top">
                                &nbsp;&nbsp;<f:message key="purge_date_text" /><f:message key="colon_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td class="label" valign="top">
                                <input id="byrel_add_purgeDate" name="PurgeDate"  title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
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