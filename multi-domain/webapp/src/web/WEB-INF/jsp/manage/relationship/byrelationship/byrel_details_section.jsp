<%-- 
    Document   : Details section (showing record details & relationship attributes edit area)
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<div dojoType="dijit.layout.ContentPane" >

        <div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">

           <div dojoType="dijit.layout.ContentPane"  region="top" minSize="30" maxSize="30" style="height:24px;padding:0px;border:0px;" >
                <div dojoType="dijit.layout.ContentPane"  class="DetailsTitleBar">&nbsp;Details</div>
           </div>

           <div dojoType="dijit.layout.ContentPane" region="center" gutters="true" minSize="50" splitter="true" style="border:0px;background-color:#F9F9F0;">
                <div dojoType="dijit.layout.ContentPane" href="m_record_details.htm" parseOnLoad="true" >
                    source record details
                </div>
                <div style="padding:4px;"></div>
                <div dojoType="dijit.layout.ContentPane" href="m_record_details.htm" parseOnLoad="true" >
                    target record details
                </div>
           </div>

           <div dojoType="dijit.layout.ContentPane" gutters="true" region="bottom"  minSize="100"  splitter="true" style="height:300px;border:0px;background-color:#F9F9F0;">
               <div dojoType="dijit.layout.ContentPane" href="m_byrel_edit_attributes.htm" parseOnLoad="true" >
                    edit attributes
                </div>
           </div>
        </div> 

</div>


<!--
<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;">
    
    <div dojoType="dijit.layout.ContentPane" region="center" gutters="true" minSize="50" splitter="true" style="padding:0px;border:0px;">
            <div class="MainBox" dojoType="dijit.layout.ContentPane">
                <div class="TitleBar" >Details</div>
                <div style="padding:4px;">
                    <div dojoType="dijit.layout.ContentPane" href="m_record_details.htm" parseOnLoad="true" >
                        source record details
                    </div><br>
                    <div dojoType="dijit.layout.ContentPane" href="m_record_details.htm" parseOnLoad="true" >
                        target record details
                    </div>
                 </div>
             </div>
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="true" region="bottom"  minSize="100"  splitter="true" style="height:300px;padding:0px;border:0px;">
        
        <div dojoType="dijit.layout.ContentPane" href="m_byrel_edit_attributes.htm" parseOnLoad="true" >
                edit attributes
        </div>
    </div>
</div>
-->
        

    


