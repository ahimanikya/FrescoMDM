<%-- 
    Document   : select_record
    Created on : Dec 19, 2008, 10:10:45 AM
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
    
    <table border="0" cellspacing="0" cellpadding="0" width="100%" ><tr><td width="100%">
            
    <div class="MainBox">
       <div class="Content">
            <table border="0"  width="100%">
            <tr>
                <td class="generalTextBold">
                    &nbsp;<f:message key="select_domain_text" /><f:message key="colon_symbol" />
                    <select id="byRecord_SelectDomain" name="byRecordSelectDomain" title="<f:message key="select_domain_text" />"  onchange="loadByRecordSelectSearchTypes(this.id)">
                    </select>
                </td>
            </tr>
            <tr><td><img src="images/spacer.gif" height="3" width="1"></td></tr>
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
                                  <select id="byRecord_select_searchtypes" name="<f:message key="select_the_search_type" />" title="<f:message key="select_the_search_type" />" onchange="byRecordSelectSearchFields(this.id);">
                                  </select>
                               </td>
                           </tr>
                           <tr>
                               <td>
                                   <div>
                                       <form name="byRecordSelectSearchFieldsForm">
                                           <table border="0">
                                               <tbody id="byRecord_select_search_fields" class="DomainSearchField"></tbody>
                                           </table>
                                       </form>
                                   </div>
                               </td>
                            </tr>
                            <tr>
                               <td align="right" colspan="2"><input type="button" onclick="byRecordSelectRecordSearch()" title="<f:message key='search_text' />" value="<f:message key='search_text' />"/> <input type="button" onclick="document.byRecordSelectSearchFieldsForm.reset();" title="<f:message key='clear_text' />" value="<f:message key='clear_text' />" /></td>
                           </tr>
                       </table>
                    </div>
                   </div>
                </td>
            </tr>
        </table>


              <table>
               <tr>
                   <td class="generalText"><div id="byRecordSelectSearchResultsSuccess" style="display:none;"><f:message key="select_record_text" /></div></td>
                   <td class="generalText"><div id="byRecordSelectSearchResultsFailure" style="display:none;"><f:message key='no_records_found' /></div></td>
               </tr>
               <tr>
                <td>
                    <div id="byRecord_Select_SearchResultDiv" style="display:none;">
                        <table border="0" class="DomainSearchResults" >
                            <tbody id="byRecord_Select_SearchResults_tableId"></tbody>
                        </table>
                        <div dojoType="dijit.Paginator" id="byRecordSelectSearchResultsPaginator"
                            totalPages="0" currentPage="0" navigationFunction="byRecordSelectSearchResults_Display_Refresh"></div>
                    </div>
                </td>
              </tr>
           </table>


    </div>
    </div>


</td></tr>
<tr><td>
    <table border="0" cellspacing="5" align="right">
      <tr>
        <td align="right" colspan="2"><input type="button"  id="byRecordSelectRecordButton" disabled = "true" title="<f:message key='select_record_text' />" value="<f:message key='select_record_text' />"/> <input type="button" onclick="hideByRecordSelectDialog();" title="<f:message key='cancel_text' />" value="<f:message key='cancel_text' />" /></td>
      </tr>
    </table>

</td></tr>
</table>
</body>