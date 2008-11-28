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
                   <td class="generalTextBold" width="30%">
                       <f:message key="source_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold" width="70%">
                       <div id="selectedSourceDomain" dojoType="dijit.layout.ContentPane">&nbsp;</div>
                   </td>
               </tr>
               <tr>
                   <td class="generalTextBold">
                      <f:message key="relationship" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold">
                       <div id="selectedRelationshipDef" dojoType="dijit.layout.ContentPane">&nbsp;</div>
                   </td>
               </tr>
               <tr>
                   <td class="generalTextBold">
                       <f:message key="target_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="generalTextBold">
                       <div id="selectedTargetDomain" dojoType="dijit.layout.ContentPane"></div>
                   </td>
               </tr>
               <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
               <tr>
                   <td colspan="2" class="generalText">
                       Select Relationship to View Details
                   </td>
               </tr>
               <tr>
                   <td colspan="2"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td colspan="2">
                      <table cellspacing="0" cellpadding="0" border="0">
                           <tr>
                            <td><a href="javascript:void(0);" title="<f:message key="select_all_text" />"><img src="images/icons/select_multiple.png" class="palleteButton"  border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" title="<f:message key="de_select_all_text" />" ><img src="images/icons/deselect_multiple.png" border="0"></a></td>
                            <td><img src="images/icons/actions_separator.gif" border="0"></td>
                            <td><a href="javascript:void(0);" onclick="test()" title="<f:message key="add_text" />..."><img src="images/icons/add_button.png" border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" title="<f:message key="delete_text" />"><img src="images/icons/delete_button.png" border="0"></a></td>
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
                                    <td>&nbsp;&nbsp;</td>
                                    <td class="generalTextBold">Source Record</td>
                                    <td class="generalTextBold">Target Record</td>
                                </tr>
                            </thead>
                            <tbody id="recordListing">
                                <!--<tr><td class="textdata" colspan="3" align="center"> Loading... </td></tr>-->
                                <tr onclick="selectRecordRow(this);">
                                    <td><input type="checkbox"></td>
                                    <td class="generalTextBold">George Karlin</td>
                                    <td class="generalTextBold">Center Point</td>
                                </tr>
                                <tr onclick="selectRecordRow(this);">
                                    <td><input type="checkbox"></td>
                                    <td class="generalTextBold">George Karlin</td>
                                    <td class="generalTextBold">Center Point</td>
                                </tr>
                                <tr onclick="selectRecordRow(this);">
                                    <td><input type="checkbox"></td>
                                    <td class="generalTextBold">George Karlin</td>
                                    <td class="generalTextBold">Center Point</td>
                                </tr>
                                <tr onclick="selectRecordRow(this);">
                                    <td><input type="checkbox"></td>
                                    <td class="generalTextBold">George Karlin</td>
                                    <td class="generalTextBold">Center Point</td>
                                </tr>
                            </tbody>
                      </table>
                      </div>
                   </td>
               </tr>

           </table>
     </div>
</div>
</body>