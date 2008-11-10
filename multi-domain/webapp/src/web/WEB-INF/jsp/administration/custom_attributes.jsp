<%-- 
    Document   : custom_attributes
    Created on : Nov 4, 2008, 6:47:33 PM
    Author     : Harish
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%
String prefixId = (String) request.getParameter("prefix");
%>
<div dojoType="dijit.layout.ContentPane" class="InnerBox" hasShadow="true" style="width:100%;">
    <div class="TitleBar"><f:message key="define_custom_attribute_text" /></div>

    <div class="Content">
        <input type="button" value='<f:message key="add_text" />' onclick="addNewAttribute('<%=prefixId%>_customAttributesTable');" />
        <input type="button" value="<f:message key="delete_text" />"  /> <br> 
        <form name="customAttributesForm">
        <table border="1" cellspacing="5" width="100%">
       
       <tr><td width="90%" valign="top">
        <table id="<%=prefixId%>_customAttributesTable" border="0" cellpadding="3" cellspacing="0" width="100%">
            <thead>
                <th><input type="checkbox"></th>
                <th align="left">Attribute</th>
                <th align="left">Type</th>
                <th align="left" nowrap>Default Value</th>
                <th align="left">Required</th>
                <th align="left">Selectable</th>
            </thead>
            
            <script>
            var validIds = new Array();
            var selectOptions = new Array("string","date","int","float","char","boolean");
            function addNewAttribute(tableId) {
               
               
                rtable = document.getElementById(tableId);
              
                var lastRow = rtable.rows.length;
                // if there's no header row in the table, then iteration = lastRow + 1
                var row = rtable.insertRow(lastRow);

                var column1 = row.insertCell(0);
                column1.align = "center";
                // check box for the name
                var attribute_id_checkbox = document.createElement("input");
                attribute_id_checkbox.type = "checkbox";
                attribute_id_checkbox.name = "attribute_id";
                column1.appendChild(attribute_id_checkbox);

                var column2 = row.insertCell(1);
                // text field for the name
                var name_text = document.createElement("input");
                name_text.type = "text";
                name_text.name = "attribute_name";
                column2.appendChild(name_text);
                
                var column3 = row.insertCell(2);
                // text field for the name
                var type_select = document.createElement("select");
                type_select.name = "attribute_type";
                
                for(var i=0; i<selectOptions.length; i++)
                {
                    var option = document.createElement("option");
                    option.text = selectOptions[i];
                    option.value =selectOptions[i];

                    type_select.options[i] = option;
                }
                column3.appendChild(type_select);
                
                var column4 = row.insertCell(3);
                var default_value_text = document.createElement("input");
                default_value_text.type = "text";
                default_value_text.size = "10";
                default_value_text.name = "default_value";
                column4.appendChild(default_value_text);
                
                
                var column5 = row.insertCell(4);
                column5.align = "center";
                var required_checkbox = document.createElement("input");
                required_checkbox.type = "checkbox";
                required_checkbox.name = "required";
                column5.appendChild(required_checkbox);
                
                var column6 = row.insertCell(5);
                column6.align = "center";
                var searchable_checkbox = document.createElement("input");
                searchable_checkbox.type = "checkbox";
                searchable_checkbox.name = "searchable";
                column6.appendChild(searchable_checkbox);
    
            }
           
            </script>
            <!--<tr>
                <td align="center"><input type="checkbox"></td>
                <td><input type="text"></td>
                <td><select>
                    <option value="string">string</option>
                        <option value="string">date</option>
                        <option value="string">int</option>
                        <option value="string">float</option>
                        <option value="string">char</option>
                        <option value="string">boolean</option>
                </select></td>
                <td><input type="text"></td>
                <td align="center"><input type="checkbox"></td>
                <td align="center"><input type="checkbox"></td>
            </tr>
            <tr>
                <td align="center"><input type="checkbox"></td>
                <td><input type="text"></td>
                <td><select>
                    <option value="string">string</option>
                        <option value="string">date</option>
                        <option value="string">int</option>
                        <option value="string">float</option>
                        <option value="string">char</option>
                        <option value="string">boolean</option>
                </select></td>
                <td><input type="text"></td>
                <td align="center"><input type="checkbox"></td>
                <td align="center"><input type="checkbox"></td>
            </tr>  -->          
        </table>
    </td>
    <td width="10%"><input type="button" value="Move Up" onclick="moveUp(this.form);"><br><input type="button" value="Move Down">
    </td></tr>
    
    </table></form>
        <br>

    </div>
</div>
<script>
    addNewAttribute('<%=prefixId%>_customAttributesTable');
    //addNewAttribute('<%=prefixId%>_customAttributesTable');
    
    function moveUp(objForm){ 

        alert('moving up...' + objForm.default_value.length);
    
    }
</script>
