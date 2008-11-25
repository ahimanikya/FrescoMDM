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

<div dojoType="dijit.RecordDetailsTitlePane" title="Dr. George Karlin" class="Details" 
        onSummaryClick="showRecordFullDetails('DetailContent',false);" onDetailsClick="showRecordFullDetails('DetailContent',true);" parseOnLoad="true">

    <!-- sumarry content -->
    <div class="Content">
    <table border="0">
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">First Name</td>
            <td><img src="../images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">George</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Middle Name</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">Denise</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Last Name</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">Karlin</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Gender</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">Male</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Date of Birth</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">04/07/1965</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Profession</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">Manager</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Phone Home</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">(020)123-4567</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Phone Work</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">(020)123-4445</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td class="LeftPane">Address Home</td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">234 Fifth Street</td>
        </tr>
        <tr>
            <td><img src="images/spacer.gif" height="1" width="3"></td>
            <td></td>
            <td><img src="images/spacer.gif" height="1" width="60"></td>
            <td class="RightPane">City Town,State 99999</td>
        </tr>
        <!--  detail content displays after summary -->
        <tr>
            <td colspan="4">
                <div id="DetailContent" style="display:none;">
                    <table>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">First Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">George</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Middle Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">Denise</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Last Name</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">Karlin</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Gender</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">Male</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Date of Birth</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">04/07/1965</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Profession</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">Manager</td>
                        </tr>
                        <tr>
                            <td><img src="images/spacer.gif" height="1" width="3"></td>
                            <td class="LeftPane">Phone Home</td>
                            <td><img src="images/spacer.gif" height="1" width="60"></td>
                            <td class="RightPane">(020)123-4567</td>
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