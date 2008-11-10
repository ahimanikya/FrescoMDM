<%-- 
    Document   : grid
    Created on : Oct 18, 2008, 10:34:47 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style type="text/css">
            @import "scripts/dojo-release-1.2.0/dijit/themes/tundra/tundra.css";
            @import "scripts/dojo-release-1.2.0/dojox/grid/_grid/Grid.css";
            @import "scripts/dojo-release-1.2.0/dojox/grid/_grid/nihiloGrid.css";
        </style>
        <script type="text/javascript" src="scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>        
        <script type="text/javascript">
            dojo.require("dojox.grid.Grid");
            dojo.require("dojox.grid.cells._base");
            dojo.require("dojox.grid._data.model");
            dojo.require("dojox.grid._data.dijitEditors");
            dojo.require("dojox.grid.DataGrid");
           
            dojo.require("dojo.parser");
        </script>
        
        <script type="text/javascript">
            // ==========================================================================
            // Create a data model
            // ==========================================================================

            s = (new Date()).getTime();
            data = [
                [ "normal", false, "new", 'But are not followed by two hexadecimal', 29.91, 10, false, s],
                [ "important", false, "new", 'Because a % sign always indicates', 9.33, -5, false, s ],
                [ "important", false, "read", 'Signs can be selectively', 19.34, 0, true, s ],
                [ "note", false, "read", 'However the reserved characters', 15.63, 0, true, s ],
                [ "normal", false, "replied", 'It is therefore necessary', 24.22, 5.50, true, s ],
                [ "important", false, "replied", 'To problems of corruption by', 9.12, -3, true, s ],
                [ "note", false, "replied", 'Which would simply be awkward in', 12.15, -4, false, s ]
            ];
            /*/var rows = 10;
            for(var i=0, l=data.length; i<rows; i++){
                data.push(data[i%l].slice(0));
            }*/
            model = new dojox.grid.data.Table(null, data);
            // ==========================================================================
            // Tie some UI to the data model
            // ==========================================================================
            model.observer(this);
            modelChange = function(){
                dojo.byId("rowCount").innerHTML = 'Row count: ' + model.count;
            }
            /*
    modelInsertion = modelDatumChange = function(a1, a2, a3){
      console.debug(a1, a2, a3);
    }
             */
            // ==========================================================================
            // Custom formatters
            // ==========================================================================
            formatCurrency = function(inDatum){
                return isNaN(inDatum) ? '...' : dojo.currency.format(inDatum, this.constraint);
            }
            formatDate = function(inDatum){
                return dojo.date.locale.format(new Date(inDatum), this.constraint);
            }
            // ==========================================================================
            // Grid structure
            // ==========================================================================
            statusCell = {
                field: 2, name: 'Status',
                styles: 'text-align: center;',
                editor: dojox.grid.editors.Select,
                options: [ "new", "read", "replied" ]
            };
            
            //statusCell = new dojox.grid.cells.AlwaysEdit();

            gridLayout = [{
                    type: 'dojox.GridRowView', width: '20px'
                },{
                    defaultCell: { width: 8, editor: dojox.grid.editors.Input, styles: 'text-align: right;'  },
                    rows: [[
                            { name: 'Id',
                                
                                get: function(inRowIndex) { return inRowIndex+1;},
                                editor: dojox.grid.editors.Dijit,
                                editorClass: "dijit.form.NumberSpinner" },
                            { name: 'Date', width: 10, field: 7,
                                editor: dojox.grid.editors.DateTextBox,
                                formatter: formatDate,
                                constraint: {formatLength: 'long', selector: "date"}},
                            { name: 'Priority', styles: 'text-align: center;', field: 0,
                                editor: dojox.grid.editors.ComboBox,                                
                                options: ["normal", "note", "important"], width: 10},
                            { name: 'Mark', width: 3, styles: 'text-align: center;',
                                type: dojox.grid.cells.AlwaysEdit, alwaysEditing:true,
                                editor:dojox.grid.editors.CheckBox
                                },
                            statusCell,
                            { name: 'Message', styles: '', width: '100%',
                                editor: dojox.grid.editors.Editor, editorToolbar: true },
                            { name: 'Amount', formatter: formatCurrency, constraint: {currency: 'EUR'},
                                editor: dojox.grid.editors.Dijit, editorClass: "dijit.form.CurrencyTextBox" },
                            { name: 'Amount', field: 4, formatter: formatCurrency, constraint: {currency: 'EUR'},
                                editor: dojox.grid.editors.Dijit, editorClass: "dijit.form.HorizontalSlider", width: 10}
                        ]]
                }];
            // ==========================================================================
            // UI Action
            // ==========================================================================
            addRow = function(){
                grid2.addRow([
                    "normal", false, "new",
                    'Now is the time for all good men to come to the aid of their party.',
                    99.99, 9.99, false
                ]);
            }
        </script>
        
    </head>
    <body class="tundra">
        <h2>Grid Layout!</h2>
        <div id="controls">
            <button dojoType="dijit.form.Button" onclick="grid2.refresh()">Refresh</button>
            <button dojoType="dijit.form.Button" onclick="grid2.edit.focusEditor()">Focus Editor</button>
            <button dojoType="dijit.form.Button" onclick="grid2.focus.next()">Next Focus</button>
            <button dojoType="dijit.form.Button" onclick="addRow()">Add Row</button>
            <button dojoType="dijit.form.Button" onclick="grid2.removeSelectedRows()">Remove</button>
            <button dojoType="dijit.form.Button" onclick="grid2.edit.apply()">Apply</button>
            <button dojoType="dijit.form.Button" onclick="grid2.edit.cancel()">Cancel</button>
            <button dojoType="dijit.form.Button" onclick="grid2.singleClickEdit = !grid.singleClickEdit">Toggle singleClickEdit</button>
        </div>
        <br />
        <div id="grid2" jsId="grid2" dojoType="dojox.Grid" model="model" structure="gridLayout"
             style="width:650px;height:350px;border: 1px solid silver;"></div>
        <br />
        <div id="rowCount"></div>
        
       

    </body>
</html>




