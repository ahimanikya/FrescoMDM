<%-- 
    Document   : Administration Module
    Created on : Oct 16, 2008, 5:04:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administration</title>
        <link rel="stylesheet" type="text/css" href="/css/administration.css" media="screen"/>

        <style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
        </style>
        <script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
        <script type="text/javascript">
            dojo.require("dojo.parser");
            dojo.require("dijit.layout.ContentPane");
            dojo.require("dijit.layout.TabContainer");
            dojo.require("dijit.form.Button");
            
        </script>
        
    </head>
    <body class="mdwm">
        <%@ include file="/WEB-INF/jsp/header.jsp" %>
        
        <div id="mainTabContainer" dojoType="dijit.layout.TabContainer" style="width:80%;height:580px;">
            
            <div id="tab1" dojoType="dijit.layout.ContentPane" title="Hierarchy" >
     
                <%@ include file="administration/hierarchy.jsp" %>
            </div>
            <div id="tab2" dojoType="dijit.layout.ContentPane" title="Relationships"  selected="true">
                <%@ include file="administration/relationships.jsp" %>
            </div>
        </div>
        
    </body>
</html>