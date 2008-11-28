<%-- 
    Document   : domain_criteria_with_results
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

    <div>
    <table border="0" width="100%">
        <tr>
            <td>
            <div dojoType="dijit.GenericTitlePane" class="MainBox" title="<f:message key="source_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" />">
            <div class="Content">
            <table border="0" >
                <tr>
                    <td class="generalTextBold" colspan="2">
                        <f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />
                    </td>
                </tr>
                 <tr>
                       <td colspan="2">
                          <select name="Source Domain">
                           <option value="">Company (Alpha Search)</option>
                          </select>
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;<f:message key="alpha_search_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;&nbsp;<f:message key="name_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                           
                       </td>
                       <td>
                          <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/>  
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;&nbsp;<f:message key="duns_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />
                           
                       </td>
                       <td>
                          <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/>  
                       </td>
                   </tr>
                   <tr>
                       <td colspan="2"><img src="images/spacer.gif" height="1" width="6"></td>
                   </tr>
                   <tr>
                       <td align="right" colspan="2"><input type="button" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/> <input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                   </tr>
                 </table>
               <div> 
           </td>
       </tr>
        <tr>
            <td class="generalText"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
        </tr>
        <tr>
            <td>
                <div>
                    <table border="1">
                        <tr>
                            <th><img src="images/spacer.gif" height="1" width="1"></th>
                            <th class="generalTextBold"><f:message key="company_text" /> <f:message key="name_text" /></th>
                            <th class="generalTextBold"><f:message key="duns_text" /> <f:message key="number_text" /></th>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="generalText">AIG</td>
                            <td class="generalText">123456789</td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="generalText">Good Ins</td>
                            <td class="generalText">222456789</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>
</div>
<br>
<div>  
  <table>
      <tr>
        <td>
        <div dojoType="dijit.GenericTitlePane" class="MainBox" title="<f:message key="target_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" />">
        <div class="Content">
            <table border="0">
                <tr>
                    <td class="generalTextBold" colspan="2">
                        <f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />
                    </td>
                </tr>
                 <tr>
                       <td colspan="2">
                          <select name="Source Domain">
                           <option value="">Company (Alpha Search)</option>
                          </select>
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;<f:message key="alpha_search_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;&nbsp;<f:message key="name_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                           
                       </td>
                       <td>
                          <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/>  
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold" colspan="2">
                           &nbsp;&nbsp;<f:message key="duns_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />
                           
                       </td>
                       <td>
                          <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/>  
                       </td>
                   </tr>
                   <tr>
                       <td colspan="2"><img src="images/spacer.gif" height="1" width="1"></td>
                   </tr>
                   <tr>
                       <td align="right" colspan="2"><input type="button" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/> <input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                   </tr>
                 </table>
               <div>
          </td>
        </tr>
        <tr>
            <td class="generalText"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
        </tr>
        <tr>
            <td>
                <div>
                    <table border="1">
                        <tr>
                            <th><img src="images/spacer.gif" height="1" width="1"></th>
                            <th class="generalTextBold">First Name</th>
                            <th class="generalTextBold">Last Name</th>
                            <th class="generalTextBold">SSN</th>
                            <th class="generalTextBold">Date of Birth</th>
                            <th class="generalTextBold">Gender</th>
                        </tr>
                        <tr>
                            <td ><input type="checkbox"></td>
                            <td class="generalText">John</td>
                            <td class="generalText">Smith</td>
                            <td class="generalText">123456789</td>
                            <td class="generalText">10/10/1970</td>
                            <td class="generalText">Male</td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="generalText">Andrea</td>
                            <td class="generalText">Ken</td>
                            <td class="generalText">223456789</td>
                            <td class="generalText">10/10/1960</td>
                            <td class="generalText">Female</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>
<br>
