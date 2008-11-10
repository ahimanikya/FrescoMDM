<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css/administration.css" media="screen"/>        
        <title>Drag and Drop Example</title>
        <style type="text/css">
            @import "scripts/dojo-release-1.2.0/dijit/themes/nihilo/nihilo.css";
        </style>
        <% int i = 1;%>
        <% int j = 2;%>
        <% String myStr = "Sridhar";%>
        <style type="text/css">
            .dndContainer {
                width: 100px;
                background-color:#EBEBEB;
                border-right: 1px solid #2564c0;
                border-top:1px solid #2564c0;
                border-left: 1px solid #2564c0;
                border-bottom: 1px solid #2564c0;
                display: block;
            }            
            .dndContainer div {
                border-bottom-style:dotted;
                border-bottom-width: 1px;
                border-bottom-color:#2564c0;
                
             }
            
            .clear {
                clear: both;
            }
        </style>
        
        <script type="text/javascript" src="scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>
        <script type="text/javascript">
            dojo.require("dojo.dnd.Container");
            dojo.require("dojo.dnd.Manager");
            dojo.require("dojo.dnd.Source");

            function initDND(){                
                var c1;
                c1 = new dojo.dnd.Source("container1");
                c1.insertNodes(false, [<%=i%>, '<%=myStr%>',[1, 2, 3]]);

                    // example subscribe to events
                    dojo.subscribe("/dnd/start", function(source){
                        console.debug("Starting the move", source);
                        //alert("Starting the move" + source);
                    });
                    dojo.subscribe("/dnd/drop/before", function(source, nodes, copy, target){
                        if(target == c1){
                            console.debug(copy ? "Copying from" : "Moving from", source, "to", target, "before", target.before);
                        }
                        //alert(copy ? "Copying from" : "Moving from", source, "to", target, "before", target.before);
                    });
                    dojo.subscribe("/dnd/drop", function(source, nodes, copy, target){
                        if(target == c1){
                            console.debug(copy ? "Copying from" : "Moving from", source, "to", target, "before", target.before);
                        }
                        //alert(copy + "Copying from : " + "Moving from " +  source, "to"+ target+ "before"+ target.before);
                    });
                    dojo.connect(c4, "onDndDrop", function(source, nodes, copy, target){
                        //alert(copy + "Copying from" + "Moving from"+ source);
                        if(target == c4){
                            console.debug(copy ? "Copying from" : "Moving from", source);
                        }
                    });
                };

                dojo.addOnLoad(initDND);
        </script>
    </head>
    <body class="nihilo">
        <div id="dragLists">
            <div style="float: left; margin: 5px;">
                <h3>programaticaly <br/>generated content</h3>
                <div id="container1" class="dndContainer"></div>
            </div>
            <div style="float: left; margin: 5px;">
                <h3>Drag me!</h3>
                <div dojoType="dojo.dnd.Source" jsId="c2" class="dndContainer">
                    <div class="dojoDndItem">Item <strong>X</strong></div>
                    <div class="dojoDndItem">Item <strong>Y</strong></div>
                    <div class="dojoDndItem">Item <strong>Z</strong></div>
                </div>
            </div>
            <div style="float: left; margin: 5px;">
                <h3>Source 3</h3>
                <div dojoType="dojo.dnd.Source" jsId="c3" class="dndContainer">Source 3
                    <script type="dojo/method" event="creator" args="item, hint">
                            // this is custom creator, which changes the avatar representation
                            var node = dojo.doc.createElement("div"), s = String(item);
                            node.id = dojo.dnd.getUniqueId();
                            node.className = "dojoDndItem";
                            node.innerHTML = (hint != "avatar" || s.indexOf("Item") < 0) ?
                                s : "<strong style='color: darkred'>Special</strong> " + s;
                            return {node: node, data: item, type: ["text"]};
                    </script>
                    <div class="dojoDndItem">Item <strong>Alpha</strong></div>
                    <div class="dojoDndItem">Item <strong>Beta</strong></div>
                    <div class="dojoDndItem">Item <strong>Gamma</strong></div>
                    <div class="dojoDndItem">Item <strong>Delta</strong></div>
                </div>
            </div>
            <div style="float: left; margin: 5px;">
                <h3>Pure Target 4</h3>
                <div dojoType="dojo.dnd.Target" jsId="c4" class="dndContainer">
                    <div class="dojoDndItem">I'm the Target</div>
                    <div class="dojoDndItem">You can only </div>
                    <div class="dojoDndItem">drop on me</div>
                </div>
            </div>
            <div class="clear"></div>
        </div>
        
        <script>var dnd = new dojo.dnd.Moveable(dojo.byId("informationLayer"));</script>
    </body>
</html>