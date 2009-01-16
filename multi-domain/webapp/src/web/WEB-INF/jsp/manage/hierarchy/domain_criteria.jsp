<%-- 
    Document   : domain_criteria
    Created on : Jan 15, 2009, 11:10:45 AM
    Author     : Harish, Narahari
--%>

<div class="MainBox">
   <div class="Content">
    <table border="0"  width="100%">
	 <tr>
        <td class="generalTextBold"><f:message key="select_the_search_type" /></td>
     </tr>
     <tr>
        <td>
           <select id="select_hierarchy_searchtypes" name="<f:message key='select_the_search_type' />" title="<f:message key='select_the_search_type' />" onchange="selectHierarchySearchFields(this.id);">
           </select>
        </td>
     </tr>
    <tr>
        <td>
		   <div>
		      <form name="selectHierarchySearchFieldsForm">
               <table border="0">
                  <tbody id="select_hierarchy_search_fields" class="DomainSearchField"></tbody>
               </table>
              </form>
            </div>
         </td>
    </tr>
    </table>
   </div>
</div>