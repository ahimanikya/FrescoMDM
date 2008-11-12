<%-- 
    Document   : Manage Module
    Created on : Oct 16, 2008, 5:04:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
    <head>
        <title>Manage</title>
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
    dojo.require("dijit.layout.TabContainer");
    dojo.require("dijit.form.Button");
</script>
</head>
<body class="mdwm">
<%@ include file="/WEB-INF/jsp/header.jsp" %>




<div id="mainManageContainer" dojoType="dijit.layout.TabContainer" style="width:98%;height:580px;">
            
    <div id="tab1" dojoType="dijit.layout.ContentPane" title="Hierarchy"  selected="true" >
        <%@ include file="manage/manage_hierarchy.jsp" %>
    </div>
    <div id="tab2" dojoType="dijit.layout.ContentPane" title="Relationships">
        <%@ include file="manage/manage_relationships.jsp" %>
    </div>
    
</div>


</body>
</html>