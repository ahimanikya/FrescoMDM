
<%-- 
    Document   : Main screen for Manage - By Record 
    Created on : Nov 18, 2008, 10:10:45 AM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" 
            minSize="50"  style="width:50%;border:0px;background-color:#F9F9F0;" >
           <div dojoType="dijit.layout.ContentPane" class="generalText">
               <table>
                   <tr>
                       <td>
                           <input type="radio" value="<f:message key='by_relationship' />" title="<f:message key='by_relationship' />" name="m_relationship"  onclick="changeViewToByRelationship('mRelatioshipTab');" > By Relationship <Br>
                           <input type="radio" value="<f:message key='by_record' />" title="<f:message key='by_record' />" name="m_relationship" checked> By Record
                       </td>
                       <td><input type="button" value="<f:message key='select_text' />..." title="<f:message key='select_text' />" onclick="showSelectDialog();"></td>
                   </tr>
               </table>
           </div>
           <hr>
           <div dojoType="dijit.layout.ContentPane"  href="m_byrecord_tree_section.htm"  parseOnLoad="true">
               tree section
				
           </div>
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" 
            style="border:0px;background-color:#F9F9F0;">
			<div dojoType="dijit.layout.ContentPane" class="MainBox"> 

               <div dojoType="dijit.layout.ContentPane" href="m_byrecord_details_section.htm" parseOnLoad="true" onLoad="byRecord_clearDetailsSection();" >
                 details section
               </div>
               
            </div>
    </div>
</div>


<div id="byrecord_select" dojoType="dijit.Dialog" title="<f:message key='select_record_text' />" style="display:none;width:750px;height:500px;padding:0px;"
        href="m_byrecord_select_record.htm" parseOnLoad="true" onLoad="loadDomainsForSelectRecord();">
</div>

<div id="byrecord_add" dojoType="dijit.Dialog" title="<f:message key='add_records_text' />"  style="display:none;width:750px;height:500px;padding:0px;"
        href="m_byrecord_add_relationship.htm" parseOnLoad="true" onLoad="initializeByRecordAddDialog();">
</div>