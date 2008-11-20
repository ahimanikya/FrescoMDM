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

<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
<script type="text/javascript">
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
    dojo.require("dijit.dijit");
    dojo.require("dijit.TitlePane");
    dojo.require("dojo.parser");

</script>


<body class="mdwm">
<div dojoType="dijit.TitlePane" title="Title Pane #1" class="Details" jsId="pane1">
    
    <div class="TitleBar">
       <table border="0" width="100%" class="TitleBar">
           <tr>
               <td align="left">
                    <img src="images/icons/document_icon.png" border="0">&nbsp;Dr. George Karlin
                </td>
                <td align="right">
                    <a href="javascript:void(0);" onclick="RefreshRecordDetails('summary_deatails_hidden_id','summaryOn','detailsOff')"><img id="summaryOn" src="images/icons/summary-button-down.gif" border="0" title="<f:message key="summary_text" />" alt="<f:message key="summary_text" />"></a>
                    <a href="javascript:void(0);" onclick="RefreshRecordDetails('summary_deatails_hidden_id','summaryOn','detailsOff')"><img id="detailsOff" src="images/icons/details-button.gif" border="0" title="<f:message key="details_text" />" alt="<f:message key="details_text" />" ></a>
                    <input type="hidden" value="false" id="summary_deatails_hidden_id">
               </td>
           </tr>
       </table>
    </div>
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
                <div id="DetailsAndSummary" style="display:none;">
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
</body>

 <!-- <script>        dojo.byId("nari").title = dojo.byId('titleContent').innerHTML;
     </script>
  -->
   
   
   
   
  
<script>
      // Refresh summary and details icons in record details screens
       
       var summaryOn=new Image();
           summaryOn.src="images/icons/summary-button-down.gif";
       var summaryOff=new Image();
           summaryOff.src="images/icons/summary-button.gif";
       var DetailOn=new Image();
           DetailOn.src="images/icons/details-button-down.gif";
       var DetailsOff=new Image();
           DetailsOff.src="images/icons/details-button.gif";
       
       
       
       function RefreshRecordDetails(hiddenId,summary_on_id,details_off_id){
           
           if(dojo.byId(hiddenId).value=='false'){
               document.getElementById('DetailsAndSummary').style.visibility='hidden';
              document.getElementById('DetailsAndSummary').style.display='none';
               dojo.byId(summary_on_id).src=summaryOn.src;
               dojo.byId(details_off_id).src=DetailsOff.src;
               dojo.byId(hiddenId).value='true';
           }
           else{
              document.getElementById('DetailsAndSummary').style.visibility='visible';
              document.getElementById('DetailsAndSummary').style.display='block';
              dojo.byId(summary_on_id).src=summaryOff.src;
              dojo.byId(details_off_id).src=DetailOn.src;
              dojo.byId(hiddenId).value='false';
           }
           
           
       }
       
   </script>  