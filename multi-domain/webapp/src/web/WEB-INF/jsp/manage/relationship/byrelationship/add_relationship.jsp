<%-- 
    Document   : add_relationship
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
    dojo.require("dijit.GenericTitlePane");
    dojo.require("dojo.parser");
</script>



<body class="mdwm">
    <table border="0" width="100%">
        <tr>
            <td>
                <div class="MainBox">
                 <div class="TitleBar"><f:message key="search_for_records" /></div>
                    <div class="Content">

                <%@ include file="/WEB-INF/jsp/manage/relationship/domain_criteria_with_results.jsp" %>
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
            <td align="right"><input type="button" id="ByRelAddRelationshipButton" onclick="ByRelAddRelationship()" title="<f:message key="add_text" />" value="<f:message key="add_text" />"/>
                              <input type="button" onclick="hideByRelAddDialog()" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" />
            </td>
        </tr>
    </table>
</bodY>