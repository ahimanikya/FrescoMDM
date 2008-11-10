<%-- 
    Document   : select_hierarchy
    Created on : Nov 10, 2008, 10:10:45 AM
    Author     : Narahari
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>

<script type='text/javascript' src='scripts/mdwm.js'></script>
<script type="text/javascript" src="../scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.Dialog");
    dojo.require("dijit.layout.ContentPane");
</script>



        <form name="HierarchyMaintain" class="Box">
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="formLabel">
                                <f:message key="domain_text" /><f:message key="colon_symbol" />
                                     <select  name="Domain" title="<f:message key="domain_text" />"  style="width:75px">
                                       <option value="Company"><f:message key="company_text" /></option>
                                       <option value="Product"><f:message key="product_text" /></option>
                                       <option value="Customer"><f:message key="customer_text" /></option>
                                     </select>
                    </td>
                </tr>
                <tr><td colspan="2"><img src="images/spacer.gif" height="5" width="1"></td></tr>
                <tr>
                    <td class="formLabel">
                                <f:message key="hierarchy_text" /><f:message key="colon_symbol" />
                                     <select  name="Domain" title="<f:message key="hierarchy_text" />"  style="width:75px">
                                       <option value="Organization Chart">Organization Chart</option>
                                     </select>
                    </td>
                </tr>
                <tr><td colspan="2"><img src="images/spacer.gif" height="2" width="1"></td></tr>
                <tr>
                    <td>
                        <table  cellspacing="0" cellpadding="0">
                            <tr>
                                <td><f:message key="initial_record_text" /></td>
                           </tr>
                           <tr>
                                <td><img src="images/spacer.gif" height="1" width="15"></td>
                           </tr>
                           <tr>
                                <td>&nbsp;<input type="radio" name="Root Node">&nbsp;<f:message key="root_node_text" /></td>
                           </tr>
                           <tr>
                                <td><img src="images/spacer.gif" height="1" width="15"></td>
                           </tr>
                           <tr>
                                <td>&nbsp;<input type="radio" name="Specific Record">&nbsp;<f:message key="specific_record_text" /></td>
                           </tr>
                           <tr>
                                <td class="xyz">
                                    <table>
                                        <tr>
                                            <td>
                                               <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true" style="width:100%;">
                                                <div class="TitleBar"><f:message key="search_text" /></div>
                                                <div class="Content">
                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                                        <tr>
                                                            <td>
                                                                

                                                               alpha search
                                                            </td>
                                                        </tr>
                                                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                                                        <tr>
                                                            <td>
                                                               Hierarchy Node Attributes
                                                                
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <input type="button" name="search" title="<f:message key="search_text" />" value="<f:message key="search_text" />" />
                                                                <input type="button" name="clear" title="<f:message key="clear_text" />" value="<f:message key="clear_text" />" />
                                                            </td>
                                                        </tr>

                                                    </table> 
                                                </div>
                                              </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Grid goes here
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                           </tr>
                               
                        </table>
                    </td>
                </tr>

                <tr><td colspan="2"><img src="images/spacer.gif" height="5" width="1"></td></tr>
                <tr>
                    <td align="right" colspan="2">
                        <input type="submit" name="select" title="<f:message key="select_text" />" value="<f:message key="select_text" />" />
                        <input type="button" name="cancel" title="<f:message key="cancel_text" />" value="<f:message key="cancel_text" />" onclick="dijit.byId('SelectManageHierarchy').hide();"/>
                    </td>
                </tr>

            </table> 
</form>