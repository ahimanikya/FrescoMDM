<%-- 
    Document   : record_details
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>

<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>
<script type='text/javascript' src='scripts/manage.js'></script>
<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
<script type="text/javascript">
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
    dojo.require("dijit.dijit");
    dojo.require("dijit.TitlePane");
    dojo.require("dojo.parser");
    dojo.require("dijit.RecordDetailsTitlePane");
     
</script>

<div dojoType="dijit.RecordDetailsTitlePane" title="Dr. George Karlin" class="MainBox" 
        onSummaryClick="showRecordFullDetails('recordSummary','recordDetails',false);" onDetailsClick="showRecordFullDetails('recordSummary','recordDetails',true);" parseOnLoad="true">

    <!-- sumarry content -->
    <div class="Content">
        
        <div id="recordSummary">summary</div>
        
        <div id="recordDetails" style="display:none;">details</div>
        
        
            
    <table border="0" class="RecordDetails">
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">First Name</td>
            <td><img src="../images/spacer.gif" height="1" width="60"></td>
            <td class="data">George</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Middle Name</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">Denise</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Last Name</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">Karlin</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Gender</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">Male</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Date of Birth</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">04/07/1965</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Profession</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">Manager</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Phone Home</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">(020)123-4567</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Phone Work</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">(020)123-4445</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="label">Address Home</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">234 Fifth Street</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td></td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="data">City Town,State 99999</td>
        </tr>
        <!--  detail content displays after summary -->
        <tr>
            <td colspan="4">
                <div id="DetailContent" style="display:none;">
                    <table>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">First Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">George</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Middle Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">Denise</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Last Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">Karlin</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Gender</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">Male</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Date of Birth</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">04/07/1965</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Profession</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">Manager</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="label">Phone Home</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="data">(020)123-4567</td>
                        </tr>
                        
                    </table>
                </div>
            </td>
        </tr>
    </table>
    </div>
</div>
   
  
<script>

</script>  