<%-- 
    Document   : manage_relationships
    Created on : Nov 11, 2008, 8:04:59 PM
    Author     : Harish
--%>
<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen"/>
<style type="text/css">
            @import "scripts/dijit/themes/mdwm/mdwm.css";
</style>
<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
    dojo.require("dijit.layout.TabContainer");

    dojo.require("dijit.layout.BorderContainer");

</script>

<body class="mdwm">
    <!-- by default loading By Relationship screen -->
    <jsp:include page="/WEB-INF/jsp/manage/relationship/byrelationship/by_relationship_main.jsp" flush="true" />
    
</body>
