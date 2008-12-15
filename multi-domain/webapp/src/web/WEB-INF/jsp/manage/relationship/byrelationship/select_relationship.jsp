<%-- 
    Document   : select_relationship
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
<div>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
            <td>
                <%@ include file="/WEB-INF/jsp/manage/relationship/byrelationship/byrel_domain_criteria.jsp" %>
            </td>
        </tr>
        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
        <tr>
            <td>
                <%@ include file="/WEB-INF/jsp/manage/relationship/byrelationship/attributes_criteria.jsp" %>
            </td>
        </tr>
        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
        <tr>
            <td align="right"><input type="button" value="<f:message key="select_text" />" onclick="searchRelationships();" /> 
            <input type="reset" value="<f:message key="cancel_text" />" onclick="hideByRelSelectDialog();refreshRelationshipsListingButtonsPalette();" /></td>
        </tr>
    </table>
</div>
</bodY>