<%-- 
    Document   : by_record_add_domain_criteria
    Created on : Dec 19, 2008, 10:10:45 AM
    Author     : Harish, Narahari
--%>

  
 <div class="MainBox"> 
 <div class="TitleBar">
     <table>
        <tr>
            <td>
             <f:message key="target_records_from_domain" /><f:message key="colon_symbol" />
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
                        <f:message key="select_the_search_type" />
                    </td>
                </tr>
                 <tr>
                       <td colspan="2">
                          <select name="<f:message key='target_domain_text' />" id="add_target_criteria" onchange="getTargetSearchFields('add_target_criteria')" title="<f:message key="select_text" /> <f:message key="the_text" /> <f:message key="search_text" /> <f:message key="type_text" />">
                          </select>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <div>
                               <form name="addTargetSearchFieldsForm">
                              <table >
                                  <tbody id="add_search_target_fields" class="DomainSearchField"></tbody>
                              </table>
                              
                              </form>
                           </div>
                       </td>
                   </tr>
                   <tr>
                       <td align="right" colspan="2"><input type="button" onclick="addTargetDomainSearch()" title="<f:message key='search_text' />" value="<f:message key='search_text' />"/> <input type="button" onclick="document.addTargetSearchFieldsForm.reset();" title="<f:message key='clear_text' />" value="<f:message key='clear_text' />" /></td>
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
               <td class="generalText"><div id="targetResultsSuccess" style="display:none;"><f:message key="select_records_to_include_in_the_relationship_text" /></div></td>
               <td class="generalText"><div id="targetResultsFailure" style="display:none;"><f:message key='no_records_found' /></div></td>
           </tr>
           <tr>
            <td>
                <div id="byrel_addTargetResults" style="display:none;">
                    <table border="0" class="DomainSearchResults">
                        <tbody id="AddTarget_TableId"></tbody>
                    </table>
                    <div dojoType="dijit.Paginator" id="add_targetDomainSearchPaginator"
                        totalPages="0" currentPage="0" navigationFunction="addTargeSearchResults_Display_Refresh"></div>
                </div>
            </td>
          </tr>
       </table>
    </div>
  </div>  
</div>
