<%-- 
    Document   : Main screen for Manage - By Relationship 
    Created on : Nov 19, 2008, 10:10:45 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" 
            minSize="50"  style="width:30%;border:0px;background-color:#F9F9F0;" >
           <div dojoType="dijit.layout.ContentPane" class="generalText">
               <table>
                   <tr>
                       <td>
                           <input type="radio" value="<f:message key='by_relationship' />" title="<f:message key='by_relationship' />" name="m_relationship" checked> By Relationship <Br>
                           <input type="radio" value="<f:message key='by_record' />" title="<f:message key='by_record' />" name="m_relationship" onclick="changeViewToByRecord('mRelatioshipTab');"> By Record
                       </td>
                       <td><input type="button" value="<f:message key='select_text' />..." title="<f:message key='select_text' />" onclick="showSelectDialog();"></td>
                   </tr>
               </table>
               
           </div>
           <hr>
           <div dojoType="dijit.layout.ContentPane" href="m_byrel_relationships_listing.htm" parseOnLoad="true"></div>
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" 
            style="border:0px;background-color:#F9F9F0;" >
           <div dojoType="dijit.layout.ContentPane" class="MainBox"> 

               <div dojoType="dijit.layout.ContentPane" href="m_byrel_details_section.htm" parseOnLoad="true" >
                 details section
               </div>
               
            </div>
    </div>
</div>

<div id="byrel_select" dojoType="dijit.Dialog" title="<f:message key='search_relationship' />" style="display:none;width:750px;height:500px;padding:0px;"
        href="m_byrel_select_relationship.htm" parseOnLoad="true" onLoad="loadDomainsForSearch();">
</div>
<!--
<div id="byrel_select" dojoType="dijit.Dialog" title="Search Relationships" style="display:none;"  >
    <div dojoType="dijit.layout.ContentPane" style="width:700px;height:500px;padding:0px;"
        href="m_byrel_select_relationship.htm" parseOnLoad="true" onLoad="loadDomainsForSearch();"></div>
</div>
-->

<div id="byrel_add" dojoType="dijit.Dialog" title="<f:message key='add_relationship' />"  style="display:none;width:750px;height:500px;padding:0px;"
        href="m_byrel_add_relationship.htm" parseOnLoad="true" onLoad="initializeAddDialog();">
</div>
<!--
<div id="byrel_add" dojoType="dijit.Dialog" title="Add Relationship" style="display:none;"  >
    <div dojoType="dijit.layout.ContentPane" style="width:700px;height:500px;padding:0px;"
        href="m_byrel_add_relationship.htm" parseOnLoad="true"  onLoad="initializeAddDialog();"></div>
</div>
-->
