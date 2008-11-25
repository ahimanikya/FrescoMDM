<%-- 
    Document   : Details section (showing record details & relationship attributes edit area)
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;">
    
    <div dojoType="dijit.layout.ContentPane" region="center" gutters="false" splitter="true" >
            <div class="MainBox">
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
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="bottom"   splitter="true" style="padding:0px;border:0px;">
        
        <div dojoType="dijit.layout.ContentPane" href="m_byrel_edit_attributes.htm" parseOnLoad="true" >
                edit attributes
        </div>
    </div>
</div>

        

    


