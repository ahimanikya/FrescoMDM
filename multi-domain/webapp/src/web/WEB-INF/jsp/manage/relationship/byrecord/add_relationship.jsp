<%-- 
    Document   : add_relationship
    Created on : Dec 22, 2008, 10:10:45 AM
    Author     : Harish,Narahari
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen"/>

<script type='text/javascript' src='scripts/mdwm.js'></script>
<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.form.DateTextBox");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
    dojo.require("dijit.Paginator");
</script>

<body class="mdwm">
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr>
        <td width="100%">
        <div class="MainBox">
          <div class="TitleBar"><f:message key="target_records_from_domain" /><f:message key="colon_symbol" /></div>  
           <div class="Content">
             <table border="0"  width="100%">
                <tr>
                    <td>
                       <div dojoType="dijit.GenericTitlePane" title="<f:message key='enter_search_criteria' />" class="MainBox" jsId="pane1">
                        <div class="Content">
                           <table border="0">
                               <tr>
                                   <td class="generalTextBold">
                                       <f:message key="select_the_search_type" />
                                   </td>
                               </tr>
                               <tr>
                                   <td>
                                      <select id="byRecord_add_searchtypes" name="<f:message key="select_the_search_type" />" title="<f:message key="select_the_search_type" />" onchange="loadByRecordAddSearchFields(this.id);">
                                      </select>
                                   </td>
                               </tr>
                               <tr>
                                   <td>
                                       <div>
                                           <form name="byRecordAddSearchFieldsForm">
                                               <table border="0">
                                                   <tbody id="byRecord_add_search_fields" class="DomainSearchField"></tbody>
                                               </table>
                                           </form>
                                       </div>
                                   </td>
                                </tr>
                                <tr>
                                   <td align="right" colspan="2"><input type="button" onclick="showByRecordAddRecordSearch()" title="<f:message key='search_text' />" value="<f:message key='search_text' />"/> <input type="button" onclick="document.byRecordAddSearchFieldsForm.reset();" title="<f:message key='clear_text' />" value="<f:message key='clear_text' />" /></td>
                               </tr>
                           </table>
                        </div>
                       </div>
                    </td>
                </tr>
            </table>
            <table>
               <tr>
                   <td class="generalText"><div id="byRecordAddSearchResultsSuccess" style="display:none;"><f:message key="select_record_text" /></div></td>
                   <td class="generalText"><div id="byRecordAddSearchResultsFailure" style="display:none;"><f:message key='no_records_found' /></div></td>
               </tr>
               <tr>
                <td>
                    <div id="byRecord_Add_SearchResultDiv" style="display:none;">
                        <table border="0" class="DomainSearchResults" >
                            <tbody id="byRecord_Add_SearchResults_tableId"></tbody>
                        </table>
                        <div dojoType="dijit.Paginator" id="byRecordAddSearchResultsPaginator"
                            totalPages="0" currentPage="0" navigationFunction="byRecordAddSearchResults_Display_Refresh"></div>
                    </div>
                </td>
              </tr>
           </table>
         </div>
      </div>
    </td>
  </tr>
  <tr>
      <td>
          <%@ include file="/WEB-INF/jsp/manage/relationship/add_relationship_attributes.jsp" %>
      </td>
  </tr>
  <tr>
    <td>
     <table border="0" cellspacing="5" align="right">
      <tr>
        <td align="right"><input type="button" id="byRecordAddRecordButton" disabled = "true" onclick="byRecordAddRecord();" title="<f:message key="add_text" />" value="<f:message key="add_text" />"/>
                          <input type="button" onclick="hideByRecordAddDialog()" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" />
        </td>
      </tr>
     </table>
    </td>
  </tr>
</table>
</body>



