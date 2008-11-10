<%-- 
    Document   : Manage Module
    Created on : Oct 16, 2008, 5:04:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
    <head>
<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen"/>
<script type='text/javascript' src='scripts/mdwm.js'></script>

<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>
<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>
</head>
<body class="mdwm">
    <%@ include file="/WEB-INF/jsp/header.jsp" %>
Manages goes here
<jsp:include page="/WEB-INF/jsp/manage/hierarchy_maintain.jsp" flush="true" />
</body>
</html>