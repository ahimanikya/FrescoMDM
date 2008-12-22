<%-- 
    Document   : Manage Module
    Created on : Oct 16, 2008, 5:04:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
<head>
    <title><f:message key="manage_text" /></title>
    <script type='text/javascript' src='dwr/engine.js'></script>    
    <script type='text/javascript' src='dwr/util.js'></script>    
    <script type='text/javascript' src='dwr/local.js'></script>
    <script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
    <script type='text/javascript' src='dwr/interface/RelationshipDefHandler.js'></script> 
    <script type='text/javascript' src='dwr/interface/RelationshipHandler.js'></script>
    <script type="text/javascript" src="scripts/lang_utils.js" ></script>
    <script type='text/javascript' src='dwr/interface/DomainScreenHandler.js'></script> 
    <link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="css/style.css" media="screen"/>
    <script type='text/javascript' src='scripts/mdwm.js'></script>
    <script type='text/javascript' src='scripts/manage.js'></script>
    <script type='text/javascript' src='scripts/byrel_api.js'></script>
    <script type='text/javascript' src='scripts/byrecord_api.js'></script>
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
        dojo.require("dijit.GenericTitlePane");

        dojo.require("dijit.form.TextBox");
        dojo.require("dijit.form.DateTextBox");  
        dojo.require("dijit.Paginator");
    </script>
    <script type="text/javascript">
        dwr.engine.setAsync(true);
        dwr.engine.setOrdered(true);
        dwr.engine.setErrorHandler(exceptionHandler);
        function exceptionHandler(message) {
            alert("invocation exception: " + message);
        }
    </script>
</head>
<body class="mdwm">
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div id="mainManageContainer" dojoType="dijit.layout.TabContainer" style="width:98%;height:800px;">
            
    <div id="mHierarchyTab" dojoType="dijit.layout.ContentPane" title="<f:message key='hierarchy_tab_text' />"  selected="true" >
        Under construction!
    </div>
    <div id="mRelatioshipTab" dojoType="dijit.layout.ContentPane" title="<f:message key='relationship_text' />" 
            href="m_byrelationship_main.htm" parseOnLoad="true" <!--onLoad="showSelectDialog();"--> >
        
    </div>
    
</div>


</body>
</html>