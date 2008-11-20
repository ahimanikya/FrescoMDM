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

        <table>
        <tr>
        <td>
        <input type="button" onclick="dijit.byId('SelectManageHierarchy').show();" value="<f:message key="select_text" />..."  />                               
        </td>
        </tr>
        </table>
        <br><br>
        hierarchy tree here
        
    </div>
    <div dojoType="dijit.layout.ContentPane" gutters="false" region="center" splitter="true" style="padding:0px;border:0px;width:70%;">
        <div dojoType="dijit.layout.BorderContainer" style="padding:0px;">
            <div dojoType="dijit.layout.ContentPane" region="top" minSize="50" splitter="true" style="height:60%;">
                    Record details
            </div>
            <div dojoType="dijit.layout.ContentPane" region="center" splitter="true" >
                    hierarchy node attributes update section
            </div>
        </div>
    </div>
</div>


<div id="SelectManageHierarchy" dojoType="dijit.Dialog" title="Select Hierarchy" style="display:none;width:900px;">
    <jsp:include page="/WEB-INF/jsp/manage/hierarchy/select_hierarchy.jsp" flush="true" />
</div>
