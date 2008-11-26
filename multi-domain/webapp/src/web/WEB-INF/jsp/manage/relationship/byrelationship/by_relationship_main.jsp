<%-- 
    Document   : Main screen for Manage - By Relationship 
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" 
            minSize="50"  style="width:30%;border:0px;" href="m_byrel_relationships_listing.htm" parseOnLoad="true">
           <!--Search box & search results-->
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" 
            style="border:0px;">
        <jsp:include page="/WEB-INF/jsp/manage/relationship/byrelationship/byrel_details_section.jsp" flush="true" />
    </div>
</div>
