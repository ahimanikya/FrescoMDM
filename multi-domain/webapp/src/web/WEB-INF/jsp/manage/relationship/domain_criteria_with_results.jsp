<%-- 
    Document   : domain_criteria_with_results
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>


 <br>
    <div class="MainTitleBar" style="width:900px;"><f:message key="source_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" /></div>
    <table border="0" class="DomainDiv" style="width:920px;">
        <tr>
            <td>
                <div dojoType="dijit.TitlePane" style="width:370px;" title="<f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />">
                   <table border="0" bgcolor="#fefee4" width="100%">
                       <tr>
                           <td>
                              <select name="Source Domain">
                               <option value="">Company (Alpha Search)</option>
                              </select>
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;<f:message key="alpha_search_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;&nbsp;<f:message key="name_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="subLabel" colspan="2">
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                               <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;&nbsp;<f:message key="duns_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="subLabel" colspan="2">
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;&nbsp;&nbsp;
                               <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                           </td>
                       </tr>
                       <tr>
                           <td><img src="images/spacer.gif" height="1" width="6"></td>
                       </tr>
                       <tr>
                           <td align="right"><input type="button" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/></td>
                           <td class="button_spacing"></td>
                           <td align="right"><input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                       </tr>
                   </table>
                </div>
            </td>
        </tr>
        <tr>
            <td class="mainLabel"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
        </tr>
        <tr>
            <td>
                <div>
                    <table border="1">
                        <tr>
                            <th><img src="images/spacer.gif" height="1" width="1"></th>
                            <th class="mainLabel"><f:message key="company_text" /> <f:message key="name_text" /></th>
                            <th class="mainLabel"><f:message key="duns_text" /> <f:message key="number_text" /></th>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="sample">AIG</td>
                            <td class="sample">123456789</td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="sample">Good Ins</td>
                            <td class="sample">222456789</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
    <br>
    <div class="MainTitleBar" style="width:900px;"><f:message key="target_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" /></div>
    <table border="0" class="DomainDiv" style="width:920px;">
        <tr>
            <td>
                <div dojoType="dijit.TitlePane" style="width:370px;" title="<f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />">
                   <table border="0"  bgcolor="#fefee4" width="100%">
                       <tr>
                           <td>
                              <select name="Source Domain">
                               <option value="">Company (Alpha Search)</option>
                              </select>
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;<f:message key="alpha_search_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;&nbsp;<f:message key="name_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="subLabel" colspan="2">
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                               <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                           </td>
                       </tr>
                       <tr>
                           <td class="mainLabel" colspan="2">
                               &nbsp;&nbsp;<f:message key="duns_text" />
                           </td>
                       </tr>
                       <tr>
                           <td class="subLabel" colspan="2">
                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;&nbsp;&nbsp;
                               <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                           </td>
                       </tr>
                       <tr>
                           <td><img src="images/spacer.gif" height="1" width="6"></td>
                       </tr>
                       <tr>
                           <td align="right"><input type="button" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/></td>
                           <td class="button_spacing"></td>
                           <td align="right"><input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                       </tr>
                   </table>
                </div>
            </td>
        </tr>
        <tr>
            <td class="mainLabel"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
        </tr>
        <tr>
            <td>
                <div>
                    <table border="1">
                        <tr>
                            <th><img src="images/spacer.gif" height="1" width="1"></th>
                            <th class="mainLabel">First Name</th>
                            <th class="mainLabel">Last Name</th>
                            <th class="mainLabel">SSN</th>
                            <th class="mainLabel">Date of Birth</th>
                            <th class="mainLabel">Gender</th>
                        </tr>
                        <tr>
                            <td ><input type="checkbox"></td>
                            <td class="sample">John</td>
                            <td class="sample">Smith</td>
                            <td class="sample">123456789</td>
                            <td class="sample">10/10/1970</td>
                            <td class="sample">Male</td>
                        </tr>
                        <tr>
                            <td><input type="checkbox"></td>
                            <td class="sample">Andrea</td>
                            <td class="sample">Ken</td>
                            <td class="sample">223456789</td>
                            <td class="sample">10/10/1960</td>
                            <td class="sample">Female</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</div>
<br>