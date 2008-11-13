<%-- 
    Document   : manage_relationships
    Created on : Nov 11, 2008, 8:04:59 PM
    Author     : Harish
--%>
<script>
    dojo.require("dijit.layout.BorderContainer");

</script>


<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" minSize="50"  style="width:30%;">
           Search box & search results
    </div>
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" style="padding:0px;border:0px;">
        <div dojoType="dijit.layout.BorderContainer" style="padding:0px;">
            <div dojoType="dijit.layout.ContentPane" region="top" minSize="50" splitter="true" style="height:60%;">
                    Record details
            </div>
            <div dojoType="dijit.layout.ContentPane" region="center" splitter="true" >
                    Relationship attributes update section
            </div>
        </div>
    </div>
</div>

