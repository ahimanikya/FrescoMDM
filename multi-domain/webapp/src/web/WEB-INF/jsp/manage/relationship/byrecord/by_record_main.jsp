
<%-- 
    Document   : Main screen for Manage - By Relationship 
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" 
            minSize="50"  style="width:30%;border:0px;background-color:#F9F9F0;" >
           <div dojoType="dijit.layout.ContentPane">
               <input type="radio" value="by_relationship" name="m_relationship" onclick="changeViewToByRelationship('mRelatioshipTab');" > By Relationship <Br>
               <input type="radio" value="by_record" name="m_relationship"  checked> By Record
           </div>
           <hr>
           <div dojoType="dijit.layout.ContentPane"  parseOnLoad="true">Under construction...</div>
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" 
            style="border:0px;background-color:#F9F9F0;">
        Under construction...
        
    </div>
</div>