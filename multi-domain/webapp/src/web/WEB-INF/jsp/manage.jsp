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
<script type='text/javascript' src='scripts/manage.js'></script>

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
    dojo.require("dijit.layout.BorderContainer");
    dojo.require("dijit.TitlePane");
    dojo.require("dijit.RecordDetailsTitlePane");
        dojo.require("dijit.form.TextBox");
    dojo.require("dijit.form.DateTextBox");  
    
</script>
</head>
<body class="mdwm">
<%@ include file="/WEB-INF/jsp/header.jsp" %>




<div id="mainManageContainer" dojoType="dijit.layout.TabContainer" style="width:98%;height:800px;">
            
    <div id="mHierarchyTab" dojoType="dijit.layout.ContentPane" title="Hierarchy"  selected="true" >
        Under construction!
    </div>
    <div id="mRelatioshipTab" dojoType="dijit.layout.ContentPane" title="Relationships" href="m_relationships.htm" parseOnLoad="true">
        
    </div>
    
</div>


</body>
</html>