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
<div class="MainBox">
    <div class="TitleBar"><f:message key="records_in_relationship_text" /></div>
    <div >
           <table border="0">
               <tr>
                   <td class="mainLabel" colspan="2">
                       <f:message key="source_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="mainLabel" colspan="2">
                       Comapnay
                   </td>
               </tr>
               <tr>
                   <td class="mainLabel" colspan="2">
                      <f:message key="relationship" /><f:message key="colon_symbol" />
                   </td>
                   <td class="mainLabel" colspan="2">
                       Tracks
                   </td>
               </tr>
               <tr>
                   <td class="mainLabel" colspan="2">
                       <f:message key="target_domain_text" /><f:message key="colon_symbol" />
                   </td>
                   <td class="mainLabel" colspan="2">
                       UKPatient
                   </td>
               </tr>
               <tr>
                   <td colspan="5"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td colspan="5">
                       Select Relationship to View Details
                   </td>
               </tr>
               <tr>
                   <td colspan="5"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td>
                      <table cellspacing="0" cellpadding="0" border="0">
                           <tr>
                            <td><a href="javascript:void(0);" title="<f:message key="select_all_text" />"><img src="images/icons/select_multiple.png" class="palleteButton"  border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" title="<f:message key="de_select_all_text" />" ><img src="images/icons/deselect_multiple.png" border="0"></a></td>
                            <td><img src="images/icons/actions_separator.gif" border="0"></td>
                            <td><a href="javascript:void(0);" title="<f:message key="add_text" />..."><img src="images/icons/add_button.png" border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" title="<f:message key="delete_text" />"><img src="images/icons/delete_button.png" border="0"></a></td>
                          </tr>
                      </table>
                   </td>
               </tr>
               <tr>
                   <td colspan="5"><img src="images/spacer.gif" height="1" width="1"></td>
               </tr>
               <tr>
                   <td>
                      <table cellspacing="0" cellpadding="0" border="0">
                           <tr>
                            <td><img src="images/spacer.gif" height="1" width="30"></td>
                            <td><img src="images/icons/actions_separator.gif" border="0" width="10" height="30"></td>
                            <td>Source Record</td>
                            <td><img src="images/icons/actions_separator.gif" border="0" width="10" height="30"></td>
                            <td>Target Record</td>
                          </tr>
                      </table>
                   </td>
               </tr>
               <tr>
                   <td class="mainLabel" colspan="5">
                       <div class="Details" style="height:300px;width:300px;">
                           Grid goes here...............
                       </div>
                   </td>
               </tr>
           </table>
     </div>
</div>
</body>