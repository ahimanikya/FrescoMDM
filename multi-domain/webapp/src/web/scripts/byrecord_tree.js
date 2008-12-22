
/*
 * Scripts related to Tree in By Record screen
 */ 

var mainTree_Data = { identifier: 'id',
    label: 'name',
    items: []};
var mainTree_Store = null;
var mainTree_Model = null;

dojo.addOnLoad(function(){
    mainTree_Store = new dojo.data.ItemFileWriteStore({data: mainTree_Data});
    mainTree_Model = new dijit.tree.TreeStoreModel({
              store: mainTree_Store,
              query: {id:'roodDomainID'},
              childrenAttrs: ["children"]
      });
});


var item_types = {DOMAIN:"domain", RELATIONSHIP:"relationship", RECORD:"record"};



function getByRecordData () {
    var selectedDomain = "Person";
    var selectedEUID = "000-000-055";
    var domainSearchObj = {name:selectedDomain, attributes:[{EUID: selectedEUID}]};
    
    //RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, getByRecordDataCB);
    
    // For testing, simulate data and call callback method <START>
    var data = {};
    data.domain = "Person";
    
    var primaryObj = {};
    primaryObj.EUID = domainSearchObj.attributes[0].EUID;
    primaryObj.highLight = "George Karlin";
    data.primaryObject = primaryObj;
    
    var relationshipObjs = []; // array of relationship objects
    for(i=0; i<4; i++) {
        var tempRelationshipObj = {};
        var relationshipDefView = {};
        relationshipDefView.name = "Employed By" + i;
        relationshipDefView.id = "" + i;
        if(i%2==0) {
          relationshipDefView.sourceDomain = selectedDomain;
          relationshipDefView.targetDomain = "Tdomain " + i;
        } else {
          relationshipDefView.sourceDomain = "SDomain" + i;
          relationshipDefView.targetDomain = selectedDomain;
        }
        relationshipDefView.biDirection = true;
        tempRelationshipObj.relationshipDefView = relationshipDefView;
        
        var relationshipViews = [];
        for(j=0; j<2; j++) {
            // put some records for this relationship view
            var relationshipView = {};
            relationshipView.id = j;
            if(i%2==0) {
              relationshipView.sourceEUID = selectedEUID;
              relationshipView.targetEUID = "T000-000-" + j;
            } else {
              relationshipView.sourceEUID = "S000-000-" + j;
              relationshipView.targetEUID = selectedEUID;
            }
            relationshipView.sourceHighLight = "S_Highlight_" + j;
            relationshipView.targetHighLight = "T_Highlight_" + j;
            relationshipViews.push(relationshipView);
        }
        tempRelationshipObj.relationshipViews = relationshipViews;
        relationshipObjs.push (tempRelationshipObj);
    }
    data.relationshipsObjects = relationshipObjs;
    getByRecordDataCB (data);
    // For testing, simulate data and call callback method <END>
}

function getByRecordDataCB (data) {
    var rootDomainItem = {};
    rootDomainItem.id = "roodDomainID";
    rootDomainItem.name = data.domain;
    rootDomainItem.type = item_types.DOMAIN;
    
    var rootRecordItem = {};
    rootRecordItem.id = "roodRecordID";
    rootRecordItem.EUID = data.primaryObject.EUID;
    rootRecordItem.name = data.primaryObject.highLight;

    var rootRecordItem2 = {};
    rootRecordItem2.id = "roodRecordID2";
    rootRecordItem2.EUID = data.primaryObject.EUID;
    rootRecordItem2.name = data.primaryObject.highLight;
    
    mainTree_Store.fetch ({query:{},onComplete: deleteItems,queryOptions: {deep:true}});

    var rootDomain = mainTree_Store.newItem( rootDomainItem , null);
    var rootRecord = mainTree_Store.newItem(rootRecordItem, {parent: rootDomain, attribute:"children"} );
    mainTree_Store.newItem(rootRecordItem2, {parent: rootDomain, attribute:"children"} );
    mainTree_Store.save({onComplete:saved});

   // var newStore = new dojo.data.ItemFileWriteStore({data: mainTree_Data});
    
     var newModel  = new dijit.tree.TreeStoreModel({
              store: mainTree_Store,
              query: {id:'roodDomainID'},
              childrenAttrs: ["children"]
      });
      
    var mainTreeObj = dijit.byId("mainTree");
    if (dijit.byId("mainTree")) {dijit.byId("mainTree").destroy()}

    mainTreeObj = new dijit.Tree({
        id: "mainTree",
        model: newModel,
        onClick: mytreeclicked
    }, document.createElement("div"));
    mainTreeObj.startup();
    dojo.byId("mainTreeContainer").appendChild(mainTreeObj.domNode);

    return;
    alert(data.domain + " : " + data.primaryObject.highLight + " : " + data.relationshipsObjects.length);

    for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
        alert(i + " " + tempRelObj.relationshipDefView.name + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
        var relationships = tempRelObj.relationshipViews;
        for(j=0; j<relationships.length; j++) {
            alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
}

function deleteItems(items, req) {
    //alert(items + " : " + items.length);
    for(i=0; i<items.length; i++) {
        var s = mainTree_Store.deleteItem(items[i]);
       // alert(i + " : " + s);
        mainTree_Store.save({onComplete:saved});
    }
}
function saved() {
    //alert("saved");
}

function mytreeclicked(item, node) {
    alert("clicked")
}

