<%-- 
    Document   : relationships_listing
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>

<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>

<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
<script type="text/javascript">
    dojo.require("dijit.layout.ContentPane");
    dojo.require("dijit.dijit");
    dojo.require("dijit.TitlePane");
    dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.DateTextBox");
    dojo.require("dojo.parser");
    
                          
</script>

<body class="mdwm">
    
<div class="MainBox" dojoType="dijit.layout.ContentPane">
    <div class="TitleBar"><f:message key="records_in_relationship_text" /></div>
    <div class="Content">
           <table border="0" width="100%" >
               <tr>
                   <td class="generalTextBold" width="30%" >
                       <f:message key="source_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold" width="70%">
                       <table cellspacing="0" cellpadding="0"  width="100%" >
                           <tr>
                               <td class="selectedDataBox" width="70%"><div id="selectedSourceDomain" >&nbsp;</div></td>
                               <td align="left"><div id="selectSourceDomainFilterImg" style="display:none;"><img src="images/icons/filter.png" title="<f:message key='search_criteria_applied' />"></div></td>
                           </tr>
                       </table>
                   </td>
               </tr>
               <tr>
                   <td class="generalTextBold">
                      <f:message key="relationship" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold">
                       <table cellspacing="0" cellpadding="0" width="100%">
                           <tr>
                               <td class="selectedDataBox" width="70%"><div id="selectedRelationshipDef" >&nbsp;</div></td>
                               <td align="left"><div id="selectRelationshipDefFilterImg" style="display:none;"><img src="images/icons/filter.png" title="<f:message key='search_criteria_applied' />"></div></td>
                           </tr>
                       </table>                       
                   </td>
               </tr>
               <tr>
                   <td class="generalTextBold">
                       <f:message key="target_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold">
                       <table cellspacing="0" cellpadding="0" width="100%">
                           <tr>
                               <td class="selectedDataBox" width="70%"><div id="selectedTargetDomain" >&nbsp;</div></td>
                               <td align="left"><div id="selectTargetDomainFilterImg" style="display:none;"><img src="images/icons/filter.png" title="Search Criteria Applied"></div></td>
                           </tr>
                       </table>
                   </td>
               </tr>
               <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
               <tr>
                   <td colspan="2" class="generalText">
                       <f:message key="select_relationship_to_view_details" />
                   </td>
               </tr>
               <tr>
                   <td colspan="2"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td colspan="2">
                      <table cellspacing="0" cellpadding="0" border="0">
                           <tr>
                            <td><a href="javascript:void(0);" onclick="selectAllRelationships();" title="<f:message key="select_all_text" />"><img id="imgSelectAllRelationshipListing" src="images/icons/select_multiple.png" class="palleteButton"  border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="deselectAllRelationships();" title="<f:message key="de_select_all_text" />" ><img id="imgDeselectAllRelationshipListing" src="images/icons/deselect_multiple.png" border="0"></a></td>
                            <td><img src="images/icons/actions_separator.gif" border="0"></td>
                            <td><a href="javascript:void(0);" onclick="showByRelAddDialog();" title="<f:message key="add_text" />..."><img id="imgAddRelationshipListing" src="images/icons/add_button_faded.png" border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="deleteRelationships();" title="<f:message key="delete_text" />"><img id="imgDeleteRelationshipListing" src="images/icons/delete_button.png" border="0"></a></td>
                          </tr>
                      </table>
                   </td>
               </tr>
               <tr>
                   <td colspan="2"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td colspan="2">
                       <div style="height:400px;">
                      <table cellspacing="0" cellpadding="0" border="0" width="100%" class="RecordsListing">
                            <thead class="header">
                                <tr>
                                    <th>&nbsp;&nbsp;</th>
                                    <th class="generalTextBold"><f:message key="source_record" /></th>
                                    <th class="generalTextBold"><f:message key="target_record" /></th>
                                </tr>
                            </thead>
                            <tbody id="relationshipsListing">
                                <!--<tr><td class="textdata" colspan="3" align="center"> Loading... </td></tr>-->
                               <!-- <tr onclick="selectRecordRow(this);">
                                    <td><input type="checkbox"></td>
                                    <td class="generalTextBold">George Karlin</td>
                                    <td class="generalTextBold">Center Point</td>
                                </tr>-->

                            </tbody>
                      </table>
                      <div dojoType="dijit.Paginator" id="relationshipsSearchResultPaginator"
                        totalPages="0" currentPage="0" navigationFunction="searchResultsRefresh"></div>
                      </div>
                   </td>
               </tr>

           </table>
     </div>
</div>
</body>