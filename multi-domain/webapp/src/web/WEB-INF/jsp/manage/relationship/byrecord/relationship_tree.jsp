<%-- 
    Document   : Relaionship_tree
    Created on : Dec 19, 2008, 10:10:45 AM
    Author     : Harish
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>


<body class="mdwm">
<div class="MainBox" dojoType="dijit.layout.ContentPane">
	<div class="TitleBar">Relationship Tree</div>
	<div class="Content" style="padding-left:10px;padding-right:5px;">

		<table cellspacing="0" cellpadding="0" border="0" width="100%">
			<tr><td colspan="3" style="padding-top:10px;padding-bottom:10px;">
				<div class="generalTextBold">Select a Record to view details </div>
				<div class="generalText" style="padding-top:7px;">Drag nodes to rearrange.</div>
			</td></tr>
			<tr><td valign="top" id="mainTreeSection"  width="100%">
				<!-- main tree -->
				<table width="100%">
					<tr><td width="100%" align="left"  valign="top">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" style="height:25px;">
                           <tr>
                            <td><a href="javascript:void(0);" onclick="byRecord_mainTree_addOperation();" title="<f:message key="add_text" />..."><img id="imgMainTreeAddButton" src="images/icons/add_button_faded.png" border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_mainTree_moveOperation();" title="Move"><img id="imgMainTreeMoveButton" src="images/icons/move-right_button_faded.png" border="0"></a></td>
							<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_mainTree_deleteOperation();" title="<f:message key="delete_text" />"><img id="imgMainTreeDeleteButton" src="images/icons/delete_button_faded.png" border="0"></a></td>
							<!--<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="" title="Replace"><img id="imgMainTreeReplaceButton" src="images/icons/replace_button_faded.png" border="0"></a></td>
							-->
							<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_mainTree_findOperation();" title="Find"><img id="imgMainTreeFindButton" src="images/icons/find_button_faded.png" border="0"></a></td>
							<!--<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="" title="Filter"><img id="imgMainTreeFilterButton" src="images/icons/filter_button_faded.png" border="0"></a></td>
							-->
							<td width="100%" align="right"><input type="button" value="Rearrange &gt;&gt;" title="Show Rearrange tree" onclick="showRearrangeTree(this);" style="height:25px;"></td>
                          </tr>
                      </table>
					</td></tr>
					<tr><td width="100%" valign="top">
						<div id="mainTreeContainer" dojoType="dijit.layout.ContentPane"
						style="height:400px;border:1px solid #000000;background-color:#ffffff;"></div>
					</td></tr>
				</table>
				
			</td>
			<td>&nbsp;</td>
			<td valign="top" id="rearrangeTreeSection" style="display:none;" width="100%">
				<!-- rearrange tree -->
				<table width="100%" border="0">
					<tr><td width="100%" align="left"  valign="top">
						<table cellspacing="0" cellpadding="0" border="0" style="height:25px;">
                           <tr>
                            <td><a href="javascript:void(0);" onclick="byRecord_rearrangeTree_addOperation();" title="<f:message key="add_text" />..."><img id="imgRearrangeTreeAddButton" src="images/icons/add_button_faded.png" border="0"></a></td>
                            <td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_rearrangeTree_moveOperation();" title="Move"><img id="imgRearrangeTreeMoveButton" src="images/icons/move-left_button_faded.png" border="0"></a></td>
							<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_rearrangeTree_deleteOperation();" title="<f:message key="delete_text" />"><img id="imgRearrangeTreeDeleteButton" src="images/icons/delete_button_faded.png" border="0"></a></td>
							<!--<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="" title="Replace"><img id="imgRearrangeTreeReplaceButton" src="images/icons/replace-button-b-to-a_faded.png" border="0"></a></td>
							-->
							<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="byRecord_rearrangeTree_findOperation();" title="Find"><img id="imgRearrangeTreeFindButton" src="images/icons/find_button_faded.png" border="0"></a></td>
							<!--<td><img src="images/spacer.gif" height="1" width="6"></td>
                            <td><a href="javascript:void(0);" onclick="" title="Filter"><img id="imgRearrangeTreeFilterButton" src="images/icons/filter_button_faded.png" border="0"></a></td>
							-->
                          </tr>
						</table>
					</td></tr>
					<tr><td width="100%" valign="top">
						<div id="rearrangeTreeContainer" dojoType="dijit.layout.ContentPane"
						style="height:400px;border:1px solid #000000;background-color:#ffffff;"></div>
					</td></tr>
				</table>				
				
			</td>
			</tr>
		</table>
		<br><br>
	</div>
	
	
</div>


</body>