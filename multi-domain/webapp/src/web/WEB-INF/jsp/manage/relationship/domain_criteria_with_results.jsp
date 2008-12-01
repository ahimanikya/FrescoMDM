<%-- 
    Document   : domain_criteria_with_results
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish, Narahari
--%>

  

<div class="MainBox">
    <div class="TitleBar">
        <table>
            <tr>
                <td>
                 <f:message key="source_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" />
               </td>
               <td id="byrel_addSourceDomain"></td>
            </tr>
        </table> 
   </div>
   <div class="Content">
    <table border="0" width="100%">
        <tr>
            <td>
             <div dojoType="dijit.GenericTitlePane" class="MainBox" title="<f:message key="search_criteria_for_records" />">
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
                           <td align="right" colspan="2"><input type="button" onclick="addSourceDomainSearch();" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/> <input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                       </tr>
                     </table>
                   <div> 
              </div>
           </td>
         </tr>
        </table>
      
      <div class="Content">
          <table>
           <tr>
             <td class="generalText"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
           </tr>
           <tr>
            <td>
                <div id="byrel_addSourceResults" >
                    <table border="1" id="AddSource_TableId">
                    </table>
                </div>
            </td>
          </tr>
       </table>
    </div>
  </div>
 </div>
 
 
 <br>
     
 <div class="MainBox"> 
 <div class="TitleBar">
     <table>
        <tr>
            <td>
             <f:message key="target_records_text" /> <f:message key="lower_from_text" /><f:message key="domain_text" /><f:message key="colon_symbol" />
           </td>
           <td id="byrel_addTargetDomain"></td>
        </tr>
     </table> 
  </div>
   <div class="Content">
    <table border="0" width="100%">
        <tr>
            <td>
            <div dojoType="dijit.GenericTitlePane" class="MainBox" title="<f:message key="search_criteria_for_records" />">    
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
                       <td align="right" colspan="2"><input type="button" onclick="addTargetDomainSearch()" title="<f:message key="search_text" />" value="<f:message key="search_text" />"/> <input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" /></td>
                   </tr>
                 </table>
               <div>
               </div>
           </td>
         </tr>
        </table>
      <div class="Content">
          <table>
           <tr>
             <td class="generalText"><f:message key="select_records_to_include_in_the_relationship_text" /></td>
           </tr>
           <tr>
            <td>
                <div id="byrel_addTargetResults">
                    <table border="1" id="AddTarget_TableId">
                    </table>
                </div>
            </td>
          </tr>
       </table>
    </div>
  </div>  
</div>
