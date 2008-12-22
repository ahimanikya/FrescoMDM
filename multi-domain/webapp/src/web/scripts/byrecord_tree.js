
/*
 * Scripts related to Tree in By Record screen
 */ 

var mainTree_Data = { identifier: 'id',
    label: 'name',
    items: []};
var mainTree_Store = null;

dojo.addOnLoad(function(){
    mainTree_Store = new dojo.data.ItemFileWriteStore({data: mainTree_Data});
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
    
    // 1. Populate store for tree
    // 1a. delete existing data items from store.
    mainTree_Store.fetch ({query:{},onComplete: deleteItemsFromStore, queryOptions: {deep:true}});
    
    // 1b. Create root element, i.e., Domain eg., Person, UK Patient.
    var rootDomainItem = {};
    rootDomainItem.id = "roodDomainID";
    rootDomainItem.name = data.domain;
    rootDomainItem.type = item_types.DOMAIN;
    
    // 1c. Create node for the main record. eg., George Karlin.
    var rootRecordItem = {};
    rootRecordItem.id = "roodRecordID";
    rootRecordItem.EUID = data.primaryObject.EUID;
    rootRecordItem.name = data.primaryObject.highLight;
    rootDomainItem.type = item_types.RECORD;
 
    var rootDomain = mainTree_Store.newItem( rootDomainItem , null);
    var rootRecord = mainTree_Store.newItem(rootRecordItem, {parent: rootDomain, attribute:"children"} );

    // 1d. add relationships for the root record.
    for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
        //alert(i + " " + tempRelObj.relationshipDefView.id + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
        var relationshipNode = {};
        relationshipNode.id = tempRelObj.relationshipDefView.id;
        relationshipNode.name = tempRelObj.relationshipDefView.name;
        relationshipNode.type = item_types.RELATIONSHIP;
        
        var rNodeItem = mainTree_Store.newItem(relationshipNode, {parent: rootRecord, attribute:"children"} );
        var relationships = tempRelObj.relationshipViews;
        for(j=0; j<relationships.length; j++) {
            //alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
    
    // 2. Save the items of store
    mainTree_Store.save();
    
    // 3. Create tree now
    createMainTree();
    return;
}

// Function to create the main tree. Store should be populated before calling this.
function createMainTree () {
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
        onClick: mainTreeClicked
    }, document.createElement("div"));
    mainTreeObj.startup();
    dojo.byId("mainTreeContainer").appendChild(mainTreeObj.domNode);
}

// function to delete items from main tree store.
function deleteItemsFromStore(items, req) {
    for(i=0; i<items.length; i++) {
        var status = mainTree_Store.deleteItem(items[i]);
        mainTree_Store.save();
    }
}

// Main tree click event is captured by this function
function mainTreeClicked(item, node) {
  //  alert("clicked")
}

