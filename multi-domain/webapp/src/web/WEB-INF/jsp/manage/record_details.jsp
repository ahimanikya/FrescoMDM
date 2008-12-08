<%-- 
    Document   : record_details
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page import="java.util.Date" %>
<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
<%
String prefix = (String) request.getParameter("prefix");
Date d = new Date();

if(prefix == null) prefix = "pre" + d.getTime();
%>
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

<div dojoType="dijit.RecordDetailsTitlePane" title="Dr. George Karlin" id ="<%=prefix%>RecordDetailsTitlePane" class="MainBox" 
        onSummaryClick="showRecordFullDetails('<%=prefix%>RecordSummaryDiv','<%=prefix%>RecordDetailDiv',false);" onDetailsClick="showRecordFullDetails('<%=prefix%>RecordSummaryDiv','<%=prefix%>RecordDetailDiv',true);" parseOnLoad="true">

    <!-- sumarry content -->
    <div class="Content" style="padding-left:5px;">
        <div id="<%=prefix%>RecordSummaryDiv" cellpadding="2">
            <table border="0" class="RecordDetails" id="<%=prefix%>RecordInSummary">
                <tr>
                    <td class="label">First Name</td>
                    <td class="data">George</td>
                </tr>
                <tr>
                    <td class="label">Middle Name</td>
                    <td class="data">Denise</td>
                </tr>
                <tr>
                    <td class="label">Last Name</td>
                    <td class="data">Karlin</td>
                </tr>
                <tr>
                    <td class="label">Gender</td>
                    <td class="data">Male</td>
                </tr>
                <tr>
                    <td class="label">Date of Birth</td>
                    <td class="data">04/07/1965</td>
                </tr>
                <tr>
                    <td class="label">Profession</td>
                    <td class="data">Manager</td>
                </tr>
                <tr>
                    <td class="label">Phone Home</td>
                    <td class="data">(020)123-4567</td>
                </tr>
                <tr>
                    <td class="label">Phone Work</td>
                    <td class="data">(020)123-4445</td>
                </tr>
                <tr>
                    <td class="label">Address Home</td>
                    <td class="data">234 Fifth Street, City Town,State 99999</td>
                </tr>
          </table>
      </div>
      <div id="<%=prefix%>RecordDetailDiv" class="RecordDetails" style="display:none;">
              <table border="0" class="RecordDetails" id="<%=prefix%>RecordInDetail">
                <tr>
                    <td class="label">First Name</td>
                    <td class="data">George</td>
                </tr>
                <tr>
                    <td class="label">Middle Name</td>
                    <td class="data">Denise</td>
                </tr>
                <tr>
                    <td class="label">Last Name</td>
                    <td class="data">Karlin</td>
                </tr>
                <tr>
                    <td class="label">Gender</td>
                    <td class="data">Male</td>
                </tr>
                <tr>
                    <td class="label">Date of Birth</td>
                    <td class="data">04/07/1965</td>
                </tr>
                <tr>
                    <td class="label">Profession</td>
                    <td class="data">Manager</td>
                </tr>
                <tr>
                    <td class="label">Phone Home</td>
                    <td class="data">(020)123-4567</td>
                </tr>
                <tr>
                    <td class="label">Phone Work</td>
                    <td class="data">(020)123-4445</td>
                </tr>
                <tr>
                    <td class="label">Address Home</td>
                    <td class="data">234 Fifth Street, City Town,State 99999</td>
                </tr>
                <tr>
                    <td class="label">Address Work</td>
                    <td class="data">123 Fourth Street, City Town,State 99999</td>
                </tr>
                <tr>
                    <td class="label">Alias Name</td>
                    <td class="data">George</td>
                </tr>
                <tr>
                    <td class="label">Email ID</td>
                    <td class="data">George@gmail.com</td>
                </tr>
                <tr>
                    <td class="label">SSN</td>
                    <td class="data">123456789</td>
                </tr>
                <tr>
                    <td class="label">Father Name</td>
                    <td class="data">Karlin</td>
                </tr>
                <tr>
                    <td class="label">Mother Name</td>
                    <td class="data">Flavia</td>
                </tr>
           </table>
        </div>
    </div>
</div>
   
  
<script>

</script>  