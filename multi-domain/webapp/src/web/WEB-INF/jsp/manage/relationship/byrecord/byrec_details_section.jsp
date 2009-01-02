<%-- 
    Document   : byrec_details_section
    Created on : Dec 30, 2008, 10:10:45 AM
    Author     : Harish,Narahari
--%>

<div dojoType="dijit.layout.ContentPane">

        <div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">

           <div dojoType="dijit.layout.ContentPane" region="top" minSize="30" maxSize="30" style="height:24px;padding:0px;border:0px;" >
                <div dojoType="dijit.layout.ContentPane"  class="DetailsTitleBar">&nbsp;Details</div>
           </div>

           <div dojoType="dijit.layout.ContentPane" region="center" gutters="true" minSize="50" splitter="true" style="border:0px;background-color:#F9F9F0;">
                <div dojoType="dijit.layout.ContentPane" id="byRecord_SourceRecordDetails" href="m_record_details.htm?prefix=source" parseOnLoad="true" >
                </div>
               <div style="padding:4px;"></div>
                <div dojoType="dijit.layout.ContentPane" id="byRecord_TargetRecordDetails" href="m_record_details.htm?prefix=target" parseOnLoad="true" >
                </div>
           </div>
         
           <div dojoType="dijit.layout.ContentPane" gutters="true" region="bottom"  minSize="100"  splitter="true" style="height:300px;border:0px;background-color:#F9F9F0;">
               <div dojoType="dijit.layout.ContentPane" id="byRecord_editAttributes" href="m_byrecord_edit_attributes.htm" parseOnLoad="true" >
                </div>
           </div> 
        </div> 

</div>