<%-- 
    Document   : Administration Module
    Created on : Oct 16, 2008, 5:04:33 PM
    Author     : Sridhar Narsingh
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TabContainer Demo</title>
        <link rel="stylesheet" type="text/css" href="/css/styles.css" media="screen"/>
        <style type="text/css">
            @import "scripts/dojo-release-1.2.0/dijit/themes/tundra/tundra.css";
        </style>
        
        <script type="text/javascript" src="scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
        <script type="text/javascript">
            dojo.require("dojo.parser");
            dojo.require("dijit.layout.ContentPane");
            dojo.require("dijit.layout.TabContainer");
            dojo.require("dijit.form.Button");
            dojo.require("dojo.dnd.Moveable");
        </script>

    </head>
    <body class="tundra">
        <div id="overlayBackgroundDiv" class="overlayBackground"></div><!-- empty div for overal window -->
        
        <div id="mainTabContainer" dojoType="dijit.layout.TabContainer" style="width:80%;height:650px;">
            <div id="tab1" dojoType="dijit.layout.ContentPane" 
                 title="Groups"  >
                <%@ include file="administration/groups.jsp" %>
            </div>
            <div id="tab2" dojoType="dijit.layout.ContentPane"
                 title="Hierarchy">
                <%@ include file="administration/hierarchy.jsp" %>
            </div>
            <div id="tab3" dojoType="dijit.layout.ContentPane"
                 title="Relationships"  selected="true">
                <%@ include file="administration/relationships.jsp" %>
            </div>
        </div>
        
        <div id="overlayContent" class="confirmPreview"  dojoType="dojo.dnd.Moveable" style="visibility:hidden;display:none;"> overlay div here</div>
    
        <script>var dnd = new dojo.dnd.Moveable(dojo.byId("overlayContent"));</script>
    </body>
</html>