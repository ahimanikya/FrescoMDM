<%-- 
    Document   : add_relationship_attributes
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>



<div class="MainBox">
<div class="TitleBar"><f:message key="relationship_attributes_text" /></div>
     <div class="Content">
      <table border="0">
        <tr>
            <td>
                <div class="RelationshipAttributes">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="2">
                                <f:message key="custom_text" /> <f:message key="attributes_text" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label">
                                &nbsp;&nbsp;<f:message key="number_visit_text" /><f:message key="colon_symbol" /> <f:message key="mandatory_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="25"></td>
                            <td>
                                <input name="Number Visits" title="<f:message key="number_visit_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                            </td>
                        </tr>
                    </table>
                    
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="RelationshipAttributes">
                    <table border="0">
                        <tr>
                            <td class="Heading" colspan="4">
                                <f:message key="predefined_text" /><f:message key="attributes_text" />
                            </td>
                        </tr>
                        <tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
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
                        <tr><td><img src="images/spacer.gif" height="4" width="1"></td></tr>
                        <tr>
                            <td class="label" valign="top">
                                &nbsp;&nbsp;<f:message key="purge_date_text" /><f:message key="colon_symbol" />
                            </td>
                            <td><img src="images/spacer.gif" height="1" width="20"></td>
                            <td class="label" valign="top">
                                <input name="Purge Date"  title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/>
                                <br>mm/dd/yyyy
                            </td>
                        </tr>
                    </table>
                    
                </div>
            </td>
        </tr>
     </table>
 </div>
 </div>