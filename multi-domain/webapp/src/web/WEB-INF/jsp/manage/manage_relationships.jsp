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
<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" minSize="50"  style="width:30%;">
           Search box & search results
    </div>
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" style="padding:0px;border:0px;">
        <div dojoType="dijit.layout.BorderContainer" style="padding:0px;">
            <div dojoType="dijit.layout.ContentPane" region="top" minSize="50" splitter="true" style="height:60%;" href="m_record_details.htm" parseOnLoad="true" >
                    <!--Record details-->
            </div>
            <div dojoType="dijit.layout.ContentPane" region="center" splitter="true" >
                    Relationship attributes update section
            </div>
        </div>
    </div>
</div>

</body>
