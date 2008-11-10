<%-- 
    Document   : hierarchy_maintain
    Created on : Nov 9, 2008, 8:42:49 PM
    Author     : Narahari
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>

<script type='text/javascript' src='scripts/mdwm.js'></script>
<script type="text/javascript" src="../scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>

<table>
    <tr>
        <td>
            <input type="button" onclick="dijit.byId('SelectManageHierarchy').show();" value="<f:message key="select_text" />..."  />                               
        </td>
    </tr>
</table>

<div id="SelectManageHierarchy" dojoType="dijit.Dialog" title="Select Hierarchy" style="display:none;width:700px;">
    <jsp:include page="/WEB-INF/jsp/manage/select_hierarchy.jsp" flush="true" />
</div>