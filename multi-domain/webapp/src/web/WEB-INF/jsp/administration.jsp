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
        <script type="text/javascript" src="scripts/lang_utils.js" ></script>
        <script type="text/javascript" src="scripts/administration.js" ></script>
        <script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>
        <script type="text/javascript">
            dojo.require("dojo.parser");
            dojo.require("dijit.layout.ContentPane");
            dojo.require("dijit.layout.TabContainer");
            dojo.require("dijit.form.Button");
            
        </script>
        <script>
            // initialize icons
            var deleteButtonEnabled = new Image();
            deleteButtonEnabled.src = "images/icons/delete_button.png";
            var deleteButtonDisabled = new Image();
            deleteButtonDisabled.src = "images/icons/delete_button_faded.png";

            var selectAllButtonEnabled = new Image();
            selectAllButtonEnabled.src = "images/icons/select_multiple.png";
            var selectAllButtonDisabled = new Image();
            selectAllButtonDisabled.src = "images/icons/select_multiple_faded.png";

            var deselectAllButtonEnabled = new Image();
            deselectAllButtonEnabled.src = "images/icons/deselect_multiple.png";
            var deselectAllButtonDisabled = new Image();
            deselectAllButtonDisabled.src = "images/icons/deselect_multiple_faded.png";            
        </script>
        
    </head>
    <body class="mdwm" onLoad="showMainContent();">
        <%@ include file="/WEB-INF/jsp/header.jsp" %>
        
        <div id="mainTabContainer" dojoType="dijit.layout.TabContainer"  style="width:100%;height:580px;">
            
            <div id="tab1" dojoType="dijit.layout.ContentPane" title="Hierarchy"  selected="true">
                <div id="hierarchyTabContent" style="display:none;">
                    <%@ include file="administration/hierarchy.jsp" %>
                </div>
            </div>
            <div id="tab2" dojoType="dijit.layout.ContentPane" title="Relationship" >
                <div id="relationshipTabContent" style="display:none;">
                    <%@ include file="administration/relationships.jsp" %>
                 </div>
            </div>
        </div>
        
    </body>
</html>
<script>
    function showMainContent() {
       // alert('loaded.. showing content now...');
        dojo.byId("hierarchyTabContent").style.display = "block";
        dojo.byId("relationshipTabContent").style.display = "block";
    }
    

    
</script>