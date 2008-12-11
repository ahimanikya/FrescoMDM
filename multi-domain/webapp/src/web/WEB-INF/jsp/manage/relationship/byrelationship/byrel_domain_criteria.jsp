<%-- 
    Document   : byrel_domain_criteria
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish,Narahari
--%>


<div class="MainBox">
   <div class="TitleBar"><f:message key="select_text" /> <f:message key="source_domain_text" /></div>
   <div class="Content">
    <table border="0"  width="100%">
    <tr>
        <td class="generalTextBold">
            &nbsp;<f:message key="source_domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;&nbsp;
            <select id="select_sourceDomain" name="select_sourceDomain" title="<f:message key='source_domain_text' />"  onchange="loadRelationshipDefs();loadSelectSourceSearchTypes(this.id)">
            </select>
        </td>
    </tr>
    <tr><td><img src="images/spacer.gif" height="3" width="1"></td></tr>
    <tr>
        <td>
            
           <div dojoType="dijit.GenericTitlePane" title="<f:message key="search_criteria_for_records" />" class="MainBox" jsId="pane1">
            <div class="Content">
               <table border="0">
                   <tr>
                       <td class="generalTextBold">
                           <f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />
                       </td>
                   </tr>
                   <tr>
                       <td>
                          <select id="select_source_searchtypes" name="Source Domain" title="<f:message key='select_text' /> <f:message key='the_text' /> <f:message key='search_text' /> <f:message key='type_text' />" onchange="selectSourceSearchFields(this.id);">
                          </select>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div>
                               <table border="0">
                                   <tbody id="select_search_source_fields"></tbody>
                               </table>
                           </div>
                       </td>
                    </tr>
               <!--    <tr>
                       <td class="generalTextBold">
                           &nbsp;<f:message key="alpha_search_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold">
                           &nbsp;&nbsp;<f:message key="name_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                           <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold">
                           &nbsp;&nbsp;<f:message key="duns_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                       </td>
                   </tr>-->
               </table>
            </div>
           </div>

        </td>
    </tr>
</table>
</div>
</div>

<div style="padding:3px;"></div>

  <div class="MainBox">
    <div class="TitleBar"><f:message key="select_text" /> <f:message key="target_domain_text" /></div>
    <div class="Content">
    <table border="0" width="100%">
    <tr>
        <td class="generalTextBold">
            &nbsp;<f:message key="target_domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;&nbsp;
            <select id="select_targetDomain" name="select_targetDomain" title="<f:message key='target_domain_text' />" onchange="loadRelationshipDefs();loadSelectTargetSearchTypes(this.id);">
            </select>
        </td>
    </tr>
    <tr><td><img src="images/spacer.gif" height="3" width="1"></td></tr>
    <tr>
        <td>
            
           <div dojoType="dijit.GenericTitlePane" title="<f:message key="search_criteria_for_records" />" class="MainBox" jsId="pane1">

            <div class="Content">
               <table border="0">
                   <tr>
                       <td class="generalTextBold">
                           <f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />
                       </td>
                   </tr>
                   <tr>
                       <td>
                          <select id="select_target_searchtypes"  name="Source Domain" title="<f:message key='select_text' /> <f:message key='the_text' /> <f:message key='search_text' /> <f:message key='type_text' />" onchange="selectTargetSearchFields(this.id);">
                          </select>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div>
                               <table >
                                   <tbody id="select_search_target_fields"></tbody>
                               </table>
                           </div>
                       </td>
                    </tr>
                <!--   <tr>
                       <td class="generalTextBold">
                           &nbsp;<f:message key="alpha_search_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold">
                           &nbsp;&nbsp;<f:message key="name_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="company_text" /> <f:message key="name_text" /><f:message key="colon_symbol" />&nbsp;
                           <input name="Company Name" title="<f:message key="company_text" /> <f:message key="name_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                       </td>
                   </tr>
                   <tr>
                       <td class="generalTextBold">
                           &nbsp;&nbsp;<f:message key="duns_text" />
                       </td>
                   </tr>
                   <tr>
                       <td class="generalText">
                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<f:message key="duns_text" /> <f:message key="number_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                           <input name="Duns Number" title="<f:message key="duns_text" /> <f:message key="number_text" />" dojoType="dijit.form.TextBox" style="width:150px"/> 
                       </td>
                   </tr> -->
               </table>
            </div>
           </div>
        </td>
    </tr>
</table>
</div>
</div>