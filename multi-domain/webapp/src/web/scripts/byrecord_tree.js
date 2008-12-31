
/*
 * Scripts related to Tree in By Record screen
 */ 

var mainTree_Data = { identifier: 'id',
    label: 'name',
    items: []};
var mainTree_Store = null;

var rearrangeTree_Data = { identifier: 'id',
    label: 'name',
    items: []};
var rearrangeTree_Store = null;

dojo.addOnLoad(function(){
    mainTree_Store = new dojo.data.ItemFileWriteStore({data: mainTree_Data});
	rearrangeTree_Store = new dojo.data.ItemFileWriteStore({data: rearrangeTree_Data});
});


var item_types = {DOMAIN:"domain", RELATIONSHIP:"relationship", RECORD:"record"};

var isRearrangeTreeShown = false;


function getByRecordData () {

	var selectedDomain = "Person";
	var selectedEUID = "000-000-555";
	if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
	if(byRecord_CurrentWorking_EUID != null) selectedEUID = byRecord_CurrentWorking_EUID;

    var domainSearchObj = {name:selectedDomain, attributes:[{EUID: selectedEUID}]};
    
    //RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, getByRecordDataCB);
    
    // For testing, simulate data and call callback method <START>
    var data = {};
    data.domain = domainSearchObj.name;
    
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
          relationshipDefView.targetDomain = "Hospital"+i;
          relationshipDefView.biDirection = true;
        } else {
          relationshipDefView.sourceDomain = "Hospital";
          relationshipDefView.targetDomain = selectedDomain;
          relationshipDefView.biDirection = false;
        }
        tempRelationshipObj.relationshipDefView = relationshipDefView;
        
        var relationshipViews = [];
        for(j=0; j<2; j++) {
            // put some records for this relationship view
            var relationshipView = {};
            relationshipView.id = "relId"+j;
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
    rootDomainItem.id = "rootDomainID";
    rootDomainItem.name = data.domain;
    rootDomainItem.type = item_types.DOMAIN;
    
    // 1c. Create node for the main record. eg., George Karlin.
    var rootRecordItem = {};
    rootRecordItem.id = "rootRecordID";
    rootRecordItem.EUID = data.primaryObject.EUID;
    rootRecordItem.name = data.primaryObject.highLight;
	rootRecordItem.underDomain = data.domain;
    rootRecordItem.type = item_types.RECORD;
 
    var rootDomain = mainTree_Store.newItem( rootDomainItem , null);
    var rootRecord = mainTree_Store.newItem(rootRecordItem, {parent: rootDomain, attribute:"children"} );

    // 1d. add relationships for the root record.
    for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
		var useSourceDomain = false;
		if(tempRelObj.relationshipDefView.sourceDomain != byRecord_CurrentWorking_Domain)
			useSourceDomain = true; 
		else useSourceDomain = false;

        //alert(i + " " + tempRelObj.relationshipDefView.id + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
        var relationshipNode = {};
        relationshipNode.id = tempRelObj.relationshipDefView.id;
        relationshipNode.name = tempRelObj.relationshipDefView.name;
        relationshipNode.biDirection = tempRelObj.relationshipDefView.biDirection;
		if(useSourceDomain)
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.targetDomain;

        relationshipNode.type = item_types.RELATIONSHIP;
        var rNodeItem = mainTree_Store.newItem(relationshipNode, {parent: rootRecord, attribute:"children"} );

        var relationshipDomain = {};
        relationshipDomain.id = relationshipNode.id + "_" + tempRelObj.relationshipDefView.sourceDomain;
		if(useSourceDomain)
			relationshipDomain.name = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipDomain.name = tempRelObj.relationshipDefView.targetDomain;
        relationshipDomain.underRelationshipDefName = relationshipNode.name;
        relationshipDomain.type = item_types.DOMAIN;
        var rDomainItem = mainTree_Store.newItem(relationshipDomain, {parent: rNodeItem, attribute:"children"} );
        
        var relationships = tempRelObj.relationshipViews;
        for(j=0; j<relationships.length; j++) {
            var recordNode = {};
			if(useSourceDomain) {
				recordNode.id = j +  relationshipNode.id + "_" + relationships[j].sourceEUID;
				recordNode.EUID = relationships[j].sourceEUID;
				recordNode.name = relationships[j].sourceHighLight;
			} else {
				recordNode.id = j+  relationshipNode.id + "_" + relationships[j].targetEUID;
				recordNode.EUID = relationships[j].targetEUID;
				recordNode.name = relationships[j].targetHighLight;
			}
			recordNode.relationshipFromDomain = rootDomainItem.name;
			recordNode.relationshipToDomain = relationshipDomain.name;
			recordNode.underRelationshipDefName = relationshipNode.name;
			recordNode.underRelationshipId = relationships[j].id;
            recordNode.type = item_types.RECORD;
            var recordNodeItem = mainTree_Store.newItem(recordNode, {parent: rDomainItem, attribute:"children"} );
            //alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
    
    // 2. Save the items of store
    mainTree_Store.save();
    
    // 3. Create tree now
    createMainTree();
	displayDiv("mainTreeContainer", true);
	
	// 4. Reset button pallete (enable/disable add,delete, etc., buttons)

    return;
}


// Function to create the main tree. Store should be populated before calling this.
function createMainTree () {
    var newModel  = new dijit.tree.TreeStoreModel({
        store: mainTree_Store,
        query: {id:'rootDomainID'},
        childrenAttrs: ["children"]    
    });
    var mainTreeObj = dijit.byId("mainTree");
    if (dijit.byId("mainTree")) {dijit.byId("mainTree").destroy()}
    mainTreeObj = new dijit.TreeCustom({
        id: "mainTree",
        model: newModel,
        customOnClick: mainTreeClicked,
		//onClose: mainTreeNodeClosed,
        getIconClass: mainTreeGetIconClass
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
function mainTreeClicked(item, node, allSelectedItems ) {
	//alert('allSelectedItems ' + allSelectedItems.length);
	byRecord_Selected_Relationship = null;
	byRecord_Selected_Record = null;
	for(i=0; i<allSelectedItems.length; i++) {
		var tempItem = allSelectedItems[i];
		
		var itemType = mainTree_Store.getValue(tempItem, "type");
		var itemName = mainTree_Store.getValue(tempItem, "name");	
		//alert(itemType + " : " + itemName);

		switch (itemType) {
			case item_types.DOMAIN:
				if(itemName == byRecord_CurrentWorking_Domain) {
					continue;
				}
				//alert("its a domain ");
				break;
			case item_types.RELATIONSHIP:
				//alert("its a relationship");
				break;
			case item_types.RECORD:
				var itemEUID = mainTree_Store.getValue(item, "EUID");
				
				if(itemEUID == byRecord_CurrentWorking_EUID) {
					byRecord_Selected_Record = {};
					byRecord_Selected_Record["EUID"] = itemEUID;
					var tempDomain = mainTree_Store.getValue(item, "underDomain");
					byRecord_Selected_Record["domain"] = tempDomain;
				} else {
					var tempRelationshipId = mainTree_Store.getValue(item, "underRelationshipId");
					
					byRecord_Selected_Relationship = {};
					byRecord_Selected_Relationship["relationshipId"] = mainTree_Store.getValue(item, "underRelationshipId");
					byRecord_Selected_Relationship["sourceDomain"] = mainTree_Store.getValue(item, "relationshipFromDomain");
					byRecord_Selected_Relationship["targetDomain"] = mainTree_Store.getValue(item, "relationshipToDomain");
					byRecord_Selected_Relationship["relationshipDefName"] = mainTree_Store.getValue(item, "underRelationshipDefName");
					
				}
				//alert("its a record");
				break;
		}
	}
	// If multi-selected, then no details can be shown.
	if(allSelectedItems.length > 1) {
		byRecord_Selected_Relationship = null;
		byRecord_Selected_Record = null;
	}
	
	return;
}

function mainTreeNodeClosed(item, node) {

	var itemName = mainTree_Store.getValue(item, "name"); //alert(itemName);
}

// Custom icons for our main tree (different icons for domain, relationship & record)
function mainTreeGetIconClass (item, opened) {
    if(item != null) {
		var itemType = mainTree_Store.getValue(item, "type");
        //alert(mainTree_Store.getValue(item, "id") + " : " +itemType);
        if(itemType == item_types.DOMAIN) {
            return "domainIcon";
        } else if(itemType == item_types.RELATIONSHIP ) {
            var relBiDirection = mainTree_Store.getValue(item, "biDirection");
            if(relBiDirection!=null && getBoolean(relBiDirection)) {
                return "relationshipBiDirectionIcon"
            } else return "relationshipOneDirectionIcon";
        } else return "recordIcon";
    }
    return "recordIcon";    
}
// Custom icons for our Rearrange tree (different icons for domain, relationship & record)
function rearrangeTreeGetIconClass (item, opened) {
    if(item != null) {
		var itemType = rearrangeTree_Store.getValue(item, "type");
        //alert(mainTree_Store.getValue(item, "id") + " : " +itemType);
        if(itemType == item_types.DOMAIN) {
            return "domainIcon";
        } else if(itemType == item_types.RELATIONSHIP ) {
            var relBiDirection = rearrangeTree_Store.getValue(item, "biDirection");
            if(relBiDirection!=null && getBoolean(relBiDirection)) {
                return "relationshipBiDirectionIcon"
            } else return "relationshipOneDirectionIcon";
        } else return "recordIcon";
    }
    return "recordIcon";    
}

function chkstatus() {
	var res = "";
	//res +=	"Add allowed: " + isAddButtonEnabled;
	//res += "\nDelete allowed: " + isDeleteButtonEnabled;
	//res += "\n target domain: " + targetDomain ;
	//res += "\n add to relationship def : " + addToRelationship;
	alert(res);
}



function showRearrangeTree(rearrangeButtonObj) {
	if(isRearrangeTreeShown) {
		isRearrangeTreeShown = false;
		rearrangeButtonObj.value = "Rearrange >>";
		rearrangeButtonObj.title = "Show Rearrange tree";
		document.getElementById("mainTreeSection").style.width = "100%";
		displayDiv("rearrangeTreeSection", false);
	} else {
		isRearrangeTreeShown = true;
		rearrangeButtonObj.value = "<< Rearrange";
		rearrangeButtonObj.title = "Hide Rearrange tree";
		document.getElementById("mainTreeSection").style.width = "50%";
		displayDiv("rearrangeTreeSection", true);
	}

}

// Function to get the data for rearrange tree.
function getRearrangeTreeData () {
	var selectedDomain = "Person";
	var selectedEUID = "000-000-555";
	if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
	if(byRecord_CurrentWorking_EUID != null) selectedEUID = byRecord_CurrentWorking_EUID;
	
	//RelationshipHandler.xyz(domainSearchObj, getRearrangeTreeData_CB);
    
    // For testing, simulate data and call callback method 
	var data = {};
    data.domain = selectedDomain;
	
	var recordObjs = []; // array of relationship objects
    for(i=0; i<4; i++) {
		var tempRecordObj = {};
		tempRecordObj.EUID = "000-000-"+i;
		tempRecordObj.recordHighLight = "Record Name " + i;
		recordObjs.push (tempRecordObj);
	}
	
	data.domainRecords = recordObjs;
	
	getRearrangeTreeData_CB(data);
}

// Callback function: for getting data for re-arrange tree
function getRearrangeTreeData_CB (data) {
    // 1. Populate store for tree
    // 1a. delete existing data items from store.
    rearrangeTree_Store.fetch ({query:{},onComplete: deleteItemsFromRearrangeTreeStore, queryOptions: {deep:true}});
	
	// 1b. Create root element, i.e., Domain eg., Person, UK Patient.
    var rootDomainItem = {};
    rootDomainItem.id = "rootDom";
    rootDomainItem.name = data.domain;
    rootDomainItem.type = item_types.DOMAIN;
	
	var rootDomain = rearrangeTree_Store.newItem( rootDomainItem , null);
	for(i=0; i<data.domainRecords.length; i++) {
		var tempRec = data.domainRecords[i];
		var recordNode = {};
        recordNode.id = tempRec.EUID;
        recordNode.name = tempRec.recordHighLight;

        recordNode.type = item_types.RECORD;
        var recordNodeItem = rearrangeTree_Store.newItem(recordNode, {parent: rootDomain, attribute:"children"} );
	}
	
	// 2. Save the items of store
    rearrangeTree_Store.save();

		
    // 3. Create tree now
    createRearrangeTree();
	displayDiv("rearrangeTreeContainer", true);
}

// Custom function, for deciding if a node may have children or not. (For Rearrange tree)
function customRearrangeTreeMayHaveChildren(item) {
	var node = item.node;
	return true;
}

// Function to create the Rearrange tree. Store should be populated before calling this.
function createRearrangeTree () {
    var rearrangeTreeModel  = new dijit.tree.TreeStoreModel({
        store: rearrangeTree_Store,
        query: {id:'rootDom'},
        childrenAttrs: ["children"],
		mayHaveChildren: customRearrangeTreeMayHaveChildren
    });
    var rearrangeTreeObj = dijit.byId("rearrangeTree");
    if (dijit.byId("rearrangeTree")) {dijit.byId("rearrangeTree").destroy()}

    rearrangeTreeObj = new dijit.TreeCustom({
        id: "rearrangeTree",
        model: rearrangeTreeModel,
        customOnClick: rearrangeTreeClicked,
        getIconClass: rearrangeTreeGetIconClass
    }, document.createElement("div"));
    rearrangeTreeObj.startup();
    dojo.byId("rearrangeTreeContainer").appendChild(rearrangeTreeObj.domNode);
}
// function to delete items from rearrange tree store.
function deleteItemsFromRearrangeTreeStore(items, req) {
    for(i=0; i<items.length; i++) {
        var status = rearrangeTree_Store.deleteItem(items[i]);
        rearrangeTree_Store.save();
    }
}

function rearrangeTreeClicked(item, node, allSelectedItems ) {
	//alert("rearrange tree clicked");
}
