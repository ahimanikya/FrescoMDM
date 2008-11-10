<%-- 
    Document   : managehierarchytype.jsp
    Created on : Oct 9, 2008, 4:56:08 PM
    Author     : cye
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<link rel="stylesheet" type="text/css" href="css/administration.css" media="screen"/>

<script type='text/javascript' src='dwr/interface/DomainHandler.js'></script>    
<script type='text/javascript' src='dwr/interface/HierarchyDefHandler.js'></script>    
<script type='text/javascript' src='dwr/engine.js'></script>    
<script type='text/javascript' src='dwr/util.js'></script>    
<script type='text/javascript' src='dwr/local.js'></script>     

<script type='text/javascript' src='scripts/mdwm.js'></script>

<script type="text/javascript" src="../scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>                
<script type="text/javascript">
    dojo.require("dojo.dnd.Moveable");
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <!--<tr>
        <td width="80%">&nbsp;</td>
        <td align="right"  width="20%">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td><input type="button" value="<f:message key="save_text" />"/></td>
                    <td class="button_spacing"></td>
                    <td><input type="button" value="<f:message key="revert_text" />" /></td>
                </tr>
            </table>                            
        </td>
    </tr>-->
    <tr>
        <td>
            <!-- domain drop downs starts -->
            <table cellpadding="0" cellspacing="0" border="0">
                <form name="domainSelection">
                    <tr>
                        <td class="mainLabel"><f:message key="domain_text" /><f:message key="colon_symbol" />&nbsp;<f:message key="mandatory_symbol" />&nbsp;</td>
                        <td>
                            <select id="selectDomain" name="Domain" title="<f:message key="domain_text" />">
                               <option value="Company"><f:message key="company_text" /></option>
                               <option value="Product"><f:message key="product_text" /></option>
                               <option value="Customer"><f:message key="customer_text" /></option>
                            </select>        
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                </form>
            </table>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr><td colspan="2"><img src="images/spacer.gif" height="10" width="1"></td></tr>
    <tr>
        <td>
            <div dojoType="dijit.layout.ContentPane" class="Box" hasShadow="true">
                <div class="TitleBar" title="<f:message key="hierarchy_text" />"><f:message key="hierarchy_text" /></div>
                <div dojoType="dijit.layout.ContentPane" class="Content" hasShadow="false">
                <table width="100%">
                    <tr>
                        <td>
                            <input type="button" onclick="dijit.byId('addhierarchy').show();" value="<f:message key="add_text" />..."  />&nbsp;
                            <input type="button" value="<f:message key="delete_text" />"  />                                                        
                        </td>
                    </tr>
                    <tr>
                        <td>grid content goes here 
                        
                        
                        </td>
                    </tr>
                </table>
                </div>
            </div>
            
        </td>
        <td>&nbsp;</td>
    </tr>
</table>
<!-- Content for Add Hierarchy -->
<div id="addhierarchy" dojoType="dijit.Dialog" title="Add Hierarchy" style="display:none;width:700px;">
    
     <%@ include file="/WEB-INF/jsp/administration/add_hierarchy.jsp" %>
    
</div>

<!-- Content for Edit Hierarchy -->
<div id="edithierarchy" dojoType="dijit.Dialog" title="Edit Hierarchy" style="display:none;width:700px;">
    <%@ include file="/WEB-INF/jsp/administration/edit_hierarchy.jsp" %>
</div>