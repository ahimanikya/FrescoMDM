<%-- 
    Document   : select_hierarchy
    Created on : Jan 15, 2008, 10:10:45 AM
    Author     : Narahari
--%>

<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<link rel="stylesheet" type="text/css" href="css/manage.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen"/>

<script type='text/javascript' src='scripts/mdwm.js'></script>
<script type="text/javascript" src="scripts/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: false"></script>                
<script type="text/javascript">
    dojo.require("dojo.parser");
    dojo.require("dijit.form.DateTextBox");
	dojo.require("dijit.GenericTitlePane");
    dojo.require("dijit.Dialog");
</script>


<body class="mdwm">
   <div class="MainBox">
   <div style="padding-left:5px;padding-top:5px;padding-right:5px;padding-bottom:5px;">
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
	   <tr>
          <td class="generalTextBold">&nbsp;<f:message key="domain_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <select id="select_hierarchy_domain" name="Domain" title="<f:message key='domain_text'/>" onchange="loadSelectHierarchySearch(this.id)"  style="width:150px"></select>
	      </td>
       </tr>
       <tr><td colspan="2"><img src="images/spacer.gif" height="5" width="1"></td></tr>
       <tr>
	      <td class="generalTextBold">&nbsp;<f:message key="hierarchy_tab_text" /><f:message key="colon_symbol" />&nbsp;&nbsp;
		      <select id="load_select_hierarchies" name="Hierarchy" title="<f:message key='hierarchy_tab_text'/>"  style="width:150px">
			        <option value="Organization Chart">Organization Chart</option>
              </select>
		  </td>
       </tr>
	   <tr><td colspan="2"><img src="images/spacer.gif" height="5" width="1"></td></tr>
	   <tr>
	     <td>
		   <div style="border: 1px solid #000000;padding-left:10px;padding-top:5px;padding-right:5px;padding-bottom:5px;">
		      <table border="0" cellspacing="0" cellpadding="0" width="100%">
			     <tr><td class="generalTextBold">&nbsp;<f:message key="initial_record_text" /></td></tr>
				 <tr>
				   <td class="generalTextBold">
				    <input type="radio" value="<f:message key='root_node_text' />" title="<f:message key='root_node_text' />" name="Root Node" >Root Node <Br>
                    <input type="radio" value="<f:message key='Root Node' />" title="<f:message key='Root Node' />" name="Specific Record">Specific Record
				   </td>
				 </tr>
				 <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
				 <tr>
	               <td>
				      <div class="MainBox" style="border: 1px solid #000000;padding-left:5px;padding-top:5px;padding-right:5px;padding-bottom:5px;">
					  <div dojoType="dijit.GenericTitlePane" title="<f:message key='search_text' />" class="MainBox" jsId="pane1">
						 <table class="MainBox" border="0" cellspacing="0" cellpadding="0" width="100%">
						   <tr>
						      <td>
							    <div style="padding-left:5px;padding-top:5px;padding-right:5px;padding-bottom:5px;">
								     <%@ include file="/WEB-INF/jsp/manage/hierarchy/domain_criteria.jsp" %>
								</div>
							   </td>
						   </tr>
						   <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
						   <tr>
						      <td>
							     <div style="padding-left:5px;padding-top:5px;padding-right:5px;padding-bottom:5px;">
								     <%@ include file="/WEB-INF/jsp/manage/hierarchy/node_attributes_criteria.jsp" %>
								 </div>
							  </td>
						   </tr>
						   <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
						   <tr>
						      <td align="right">
							      <input type="button" class="button_spacing" title="<f:message key='search_text' />" value="<f:message key='search_text' />"/> 
								  <input type="button" class="button_spacing" onclick="document.selectHierarchySearchFieldsForm.reset();" title="<f:message key='clear_text' />" value="<f:message key='clear_text' />" />&nbsp;
							  </td>
						   </tr>
						   <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
						 </table>
					  </div>
					     <table>
						     <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
							 <tr><td>search criteria results goes here....</td></tr>
							 <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
						 </table>
					  </div>
	                </td>
	            </tr>
			  </table>
		   </div>
	     </td>
	  </tr>
	  <tr><td><img src="images/spacer.gif" height="5" width="1"></td></tr>
      <tr>
        <td align="right" colspan="2"><input type="button" disabled = "true" title="<f:message key='select_text' />" value="<f:message key='select_text' />" onclick=""/> <input type="button" onclick="hideSelectHierarchyDialog();" title="<f:message key='cancel_text' />" value="<f:message key='cancel_text' />" />
	    </td>
	  </tr>
   </table>
   </div>
   </div>
</body>


<!--
         
        <form name="HierarchyMaintain" class="Box">
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="formLabel">
                        <f:message key="domain_text" /><f:message key="colon_symbol" />
                    </td>
                    <td>
                          <select  name="Domain" title="<f:message key="domain_text" />"  style="width:90px">
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
                    </td>
                    <td class="formLabel">
                         <select  name="Domain" title="<f:message key="hierarchy_text" />"  style="width:150px">
                              <option value="Organization Chart">Organization Chart</option>
                         </select>
                    </td>
                </tr>
                <tr><td colspan="2"><img src="images/spacer.gif" height="2" width="1"></td></tr>
                <tr>
                    <td class="xyz" colspan="2">
                        <table  cellspacing="0" cellpadding="0" width="100%">
                            <tr>
                                <td class="formLabel"><f:message key="initial_record_text" /></td>
                           </tr>
                           <tr>
                                <td><img src="images/spacer.gif" height="1" width="15"></td>
                           </tr>
                           <tr>
                                <td class="formLabel">&nbsp;<input type="radio" name="Root Node">&nbsp;<f:message key="root_node_text" /></td>
                           </tr>
                           <tr>
                                <td><img src="images/spacer.gif" height="1" width="15"></td>
                           </tr>
                           <tr>
                                <td class="formLabel">&nbsp;<input type="radio" name="Specific Record">&nbsp;<f:message key="specific_record_text" /></td>
                           </tr>
                           <tr>
                                <td class="xyz">
                                    <table width="100%">
                                        <tr>
                                            <td>
                                               <div dojoType="dijit.layout.ContentPane" class="MainBox" hasShadow="true" style="width:100%;">
                                                   <div class="TitleBar"><a onclick="showDiv('alpha');showDiv('attribute');"><img src="images/down-chevron-button.png" title="downArrow"></a>&nbsp; <f:message key="search_text" /></div>
                                                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                                        <tr>
                                                            <td>
                                                                <div class="Content" id="alpha" style="display:none;">
                                                                    <table border="0">
                                                                        <tr>
                                                                            <td colspan="2" class="formLabel">
                                                                                <f:message key="alpha_search_text" />
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td colspan="2" class="formLabel">
                                                                                &nbsp;<f:message key="main_text" />
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class="formLabel">
                                                                                &nbsp;&nbsp;<f:message key="first_text" /> &nbsp;<f:message key="name_text" /><f:message key="colon_symbol" />
                                                                            </td>
                                                                            <td>
                                                                                <input type="text" name="firstName" title="<f:message key="first_text" /><f:message key="name_text" />">
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class="formLabel">
                                                                                &nbsp;&nbsp;<f:message key="last_text" /> &nbsp;<f:message key="name_text" /><f:message key="colon_symbol" />
                                                                            </td>
                                                                            <td>
                                                                                <input type="text" name="lastName" title="<f:message key="last_text" /> &nbsp;<f:message key="name_text" />">
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td colspan="2" class="formLabel">
                                                                                &nbsp;<f:message key="id_text" />
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class="formLabel">
                                                                                &nbsp;&nbsp;<f:message key="group_text" /><f:message key="colon_symbol" />
                                                                            </td>
                                                                            <td>
                                                                                <input type="text" name="firstName" title="<f:message key="group_text" />">
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr><td><img src="images/spacer.gif" height="6" width="1"></td></tr>
                                                        <tr>
                                                            <td>
                                                                <div class="Content" id="attribute" style="display:none;">
                                                                    <table width="100%">
                                                                        <tr>
                                                                            <td class="formLabel">
                                                                                <f:message key="hierarchy_text" /><f:message key="node_attribute_text" />
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <div class="custom">
                                                                                    <table>
                                                                                        <tr>
                                                                                            <td class="formLabel">
                                                                                                <f:message key="custom_text" /><f:message key="attributes_text" />
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="formLabel">
                                                                                                <f:message key="hired_text" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <input  name="Hired" value="" title="<f:message key="hired_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/><img src="images/cal.gif" title="calender">
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <div class="predefined">
                                                                                    <table width="100%" border="0">
                                                                                        <tr>
                                                                                            <td class="formLabel" colspan="3">
                                                                                                <f:message key="predefined_text" /><f:message key="attributes_text" />
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="formLabel">
                                                                                                <f:message key="Effective_text" />
                                                                                            </td>
                                                                                            <td class="formLabel">
                                                                                                <f:message key="From_text" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <input  name="EffectiveFrom" value="" title="<f:message key="From_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/><img src="images/cal.gif" title="calender">
                                                                                            </td> 
                                                                                            <td class="formLabel">
                                                                                               <f:message key="To_text" />
                                                                                           </td>
                                                                                            <td>
                                                                                               <input  name="EffectiveTo" value="" title="<f:message key="To_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/><img src="images/cal.gif" title="calender">
                                                                                             </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td class="formLabel">
                                                                                                <f:message key="purge_date_text" />
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                            </td>
                                                                                            <td>
                                                                                                <input  name="PurgeDate" value="" title="<f:message key="purge_date_text" />" dojoType="dijit.form.DateTextBox" style="width:150px"/><img src="images/cal.gif" title="calender">
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                            </td>
                                                                                            <td>
                                                                                                
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </div>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <input type="button" class="button" name="search" title="<f:message key="search_text" />" value="<f:message key="search_text" />" />
                                                                <input type="button" class="button" name="clear" title="<f:message key="clear_text" />" value="<f:message key="clear_text" />" />
                                                            </td>
                                                        </tr>

                                                    </table> 
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td >
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
-->
