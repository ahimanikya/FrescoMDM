<%-- 
    Document   : manage hierarchy main page.
    Created on : Jan 15, 2009
    Author     : Harish
--%>


<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div dojoType="dijit.layout.BorderContainer" splitter="true" style="width:100%; height:100%;padding:0px;border:0px;">
    <div dojoType="dijit.layout.ContentPane" region="left" gutters="false" splitter="true" 
            minSize="50"  style="width:50%;border:0px;background-color:#F9F9F0;" >
           <div dojoType="dijit.layout.ContentPane" class="generalText">
               <table>
                   <tr>
                       <td><input type="button" value="<f:message key='select_text' />..." title="<f:message key='select_text' />" onclick="m_hierarchy_showSelectDialog();"></td>
                   </tr>
               </table>
           </div>
           <hr>
           <div dojoType="dijit.layout.ContentPane"   parseOnLoad="true">
               tree section
				
           </div>
    </div>
    
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" 
            style="border:0px;background-color:#F9F9F0;">

               <div dojoType="dijit.layout.ContentPane" >
                 details section
               </div>
    </div>
</div>
