<%-- 
    Document   : tree
    Created on : Oct 17, 2008, 12:04:19 PM
    Author     : Sridhar Narsingh
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Drag and Drop Example</title>
        <style type="text/css">
            @import "scripts/dojo-release-1.2.0/dijit/themes/nihilo/nihilo.css";
        </style>        
<%@page contentType="text/html" pageEncoding="UTF-8"%>
   
<script type="text/javascript" src="scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>           
<script language="JavaScript" type="text/javascript">
  dojo.require("dojo.data.ItemFileReadStore");
  dojo.require("dijit.Tree");
  dojo.require("dijit.Menu");
  dojo.require("dojo.parser");  // scan page for widgets and instantiate them
</script>
</head>
<body class="nihilo">
<h2>A rootless tree (no "continents" node) with context menus</h2>

<!--
  Define the data source that reads from a JSON file.
-->
<div dojoType="dojo.data.ItemFileReadStore" jsId="continentStore"
  url="scripts/countries.json"></div>
  
<!--
  Define the menu that will pop up
-->

<ul dojoType="dijit.Menu" id="tree_menu" style="display: none;">
  <li dojoType="dijit.MenuItem" onClick="alert('Hello world');">Enabled Item</li>
  <li dojoType="dijit.MenuItem" disabled="true">Disabled Item</li>
  <li dojoType="dijit.MenuItem" iconClass="dijitEditorIcon dijitEditorIconCut"
    onClick="alert('not actually cutting anything, just a test!')">Cut</li>
  <li dojoType="dijit.MenuItem" iconClass="dijitEditorIcon dijitEditorIconCopy"
    onClick="alert('not actually copying anything, just a test!')">Copy</li>
  <li dojoType="dijit.MenuItem" iconClass="dijitEditorIcon dijitEditorIconPaste"
    onClick="alert('not actually pasting anything, just a test!')">Paste</li>
  <li dojoType="dijit.PopupMenuItem">
    <span>Enabled Submenu</span>
    <ul dojoType="dijit.Menu" id="submenu2">
      <li dojoType="dijit.MenuItem" onClick="alert('Submenu 1!')">Submenu Item One</li>
      <li dojoType="dijit.MenuItem" onClick="alert('Submenu 2!')">Submenu Item Two</li>
      <li dojoType="dijit.PopupMenuItem">
        <span>Deeper Submenu</span>
        <ul dojoType="dijit.Menu" id="submenu4">
          <li dojoType="dijit.MenuItem" onClick="alert('Sub-submenu 1!')">Sub-sub-menu Item One</li>
          <li dojoType="dijit.MenuItem" onClick="alert('Sub-submenu 2!')">Sub-sub-menu Item Two</li>
        </ul>
      </li>
    </ul>
  </li>
  <li dojoType="dijit.PopupMenuItem" disabled="true">
    <span>Disabled Submenu</span>
    <ul dojoType="dijit.Menu" id="submenu3" style="display: none;">
      <li dojoType="dijit.MenuItem" onClick="alert('Submenu 1!')">Submenu Item One</li>
      <li dojoType="dijit.MenuItem" onClick="alert('Submenu 2!')">Submenu Item Two</li>
    </ul>
  </li>
</ul>

<!--
  Define the tree, tell it to populate itself from the data store 'continentStore'
-->
<div dojoType="dijit.Tree" id="tree2" store="continentStore" query="{type:'continent'}">
  <script type="dojo/connect">
    var menu = dijit.byId("tree_menu");
    // when we right-click anywhere on the tree, make sure we open the menu
    menu.bindDomNode(this.domNode);

    dojo.connect(menu, "_openMyself", this, function(e){
      // get a hold of, and log out, the tree node that was the source of this open event
      var tn = this._domElement2TreeNode(e.target);
      console.debug(tn);

      // now inspect the data store item that backs the tree node:
      console.debug(tn.item);

      // contrived condition: if this tree node doesn't have any children, disable all of the menu items
      menu.getChildren().forEach(function(i){ i.setDisabled(!tn.item.children); });

      // IMPLEMENT CUSTOM MENU BEHAVIOR HERE
    });
  </script>
</div>
</body>
</html>