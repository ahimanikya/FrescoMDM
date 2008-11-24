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
    dojo.require("dojo.parser");
</script>



<body class="mdwm">
  <div class="MainTitleBar" style="width:917px;"><f:message key="search_for_records" /></div>
    <table border="0" width="75%" class="abc">
        <tr>
            <td>
                <%@ include file="/WEB-INF/jsp/manage/relationship/domain_criteria_with_results.jsp" %>
            </td>
        </tr>
        <tr>
            <td>
                <%@ include file="/WEB-INF/jsp/manage/relationship/add_relationship_attributes.jsp" %>
            </td>
        </tr>
        <tr>
            <td align="right"><input type="button" title="<f:message key="select_text" />" value="<f:message key="select_text" />"/>
                              <input type="button" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" />
            </td>
        </tr>
    </table>
</bodY>