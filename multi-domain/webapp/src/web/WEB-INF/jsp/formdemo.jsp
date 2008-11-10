<html>
<head>
<title>Dojo example</title>
<style type="text/css">
  @import "scripts/dojo-release-1.2.0/dijit/themes/nihilo/nihilo.css";
</style>
<style type="text/css">
#dndOne {
  width: 100px;
  height: 100px;
  padding: 10px;
  border: 1px solid #000;
  background: red;
}

#dndArea {
  height: 200px;
  padding: 10px;
  border: 1px solid #000;
}
</style>
<script type="text/javascript" src="scripts/dojo-release-1.2.0/dojo/dojo.js" djConfig="parseOnLoad:true, isDebug: true"></script>
<script type="text/javascript">
dojo.require("dijit.form.Button"); // this we only require to make the demo look fancy
dojo.require("dojo.dnd.Moveable");

function makeMoveable(node){
  var dnd = new dojo.dnd.Moveable(dojo.byId(node));
}
</script>
<body class="nihilo">
<div id="dndArea">
  <div id="dndOne">Hi, I am moveable when you want to.</div>
</div>
<p><button dojoType="dijit.form.Button" onClick="makeMoveable('dndOne')">Make moveable</button>
</body>
</html>