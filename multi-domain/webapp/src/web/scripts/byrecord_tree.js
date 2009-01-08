
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


function byRecord_initMainTree () {

	var selectedDomain = "Person";
	var selectedEUID = "000-000-555";
	if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
	if(byRecord_CurrentWorking_EUID != null) selectedEUID = byRecord_CurrentWorking_EUID;

    var domainSearchObj = {name:selectedDomain, attributes:[{EUID: selectedEUID}]};
    
    RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, byRecord_initMainTree_CB);
    return;
	
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
			  relationshipView.targetHighLight = "T_Highlight_" + j;
			  relationshipView.sourceHighLight = primaryObj.highLight ;
            } else {
              relationshipView.sourceEUID = "S000-000-" + j;
			  relationshipView.sourceHighLight = "S_Highlight_" + j;
			  relationshipView.targetHighLight = primaryObj.highLight ;
              relationshipView.targetEUID = selectedEUID;
            }
            
            
            relationshipViews.push(relationshipView);
        }
        tempRelationshipObj.relationshipViews = relationshipViews;
        relationshipObjs.push (tempRelationshipObj);
    }
    data.relationshipsObjects = relationshipObjs;
    byRecord_initMainTree_CB (data);
    // For testing, simulate data and call callback method <END>
}

function byRecord_initMainTree_CB (data) {
    
    // 1. Populate store for tree
    // 1a. delete existing data items from store.
    mainTree_Store.fetch ({query:{},onComplete: deleteItemsFromStore, queryOptions: {deep:true}});

    // 1b. Create root element, i.e., Domain eg., Person, UK Patient.
    var rootDomainItem = {};
    rootDomainItem.id = "rootDomainID";
    rootDomainItem.name = data.domain;
    rootDomainItem.type = item_types.DOMAIN;
	rootDomainItem.isRoot = true;
    
    // 1c. Create node for the main record. eg., George Karlin.
    var rootRecordItem = {};
    rootRecordItem.id = "rootRecordID";
    rootRecordItem.EUID = data.primaryObject.EUID;
    rootRecordItem.name = data.primaryObject.highLight;
	rootRecordItem.parentDomain = data.domain;
    rootRecordItem.type = item_types.RECORD;
	rootRecordItem.isRoot = true;
	
 //alert("rootRecordItem : " + rootRecordItem);
    var rootDomain = mainTree_Store.newItem( rootDomainItem , null);
    var rootRecord = mainTree_Store.newItem(rootRecordItem, {parent: rootDomain, attribute:"children"} );
//alert("data.relationshipsObjects.length : " +data.relationshipsObjects.length );
    // 1d. add relationships for the root record.

    for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
		var useSourceDomain = false;
		if(tempRelObj.relationshipDefView.sourceDomain != byRecord_CurrentWorking_Domain)
			useSourceDomain = true; 
		else useSourceDomain = false;

//alert(i + "  useSourceDomain=" + useSourceDomain + " " +  tempRelObj.relationshipDefView.id + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
        var relationshipNode = {};
        relationshipNode.id = i + "_" + tempRelObj.relationshipDefView.id;
		relationshipNode.relationshipDefId = tempRelObj.relationshipDefView.id; 
        relationshipNode.name = tempRelObj.relationshipDefView.name;
        relationshipNode.biDirection = tempRelObj.relationshipDefView.biDirection;
		if(useSourceDomain)
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.targetDomain;
		
		relationshipNode.parentDomain = rootRecordItem.parentDomain;
		relationshipNode.parentRecordEUID = rootRecordItem.EUID;
		
        relationshipNode.type = item_types.RELATIONSHIP;
        var rNodeItem = mainTree_Store.newItem(relationshipNode, {parent: rootRecord, attribute:"children"} );

        var relationshipDomain = {};
        relationshipDomain.id = i + "_" + relationshipNode.id + "_" + tempRelObj.relationshipDefView.sourceDomain;
		if(useSourceDomain)
			relationshipDomain.name = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipDomain.name = tempRelObj.relationshipDefView.targetDomain;
        relationshipDomain.parentRelationshipDefName = relationshipNode.name;
		relationshipDomain.parentRelationshipDefId = relationshipNode.relationshipDefId;
		relationshipDomain.parentDomain = relationshipNode.parentDomain;
		relationshipDomain.parentRecordEUID = rootRecordItem.EUID;
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
			recordNode.parentDomain = relationshipDomain.name;
			recordNode.relationshipFromDomain = rootDomainItem.name;
			recordNode.relationshipToDomain = relationshipDomain.name;

			recordNode.fromRecordHighLight = relationships[j].sourceHighLight;
			recordNode.toRecordHighLight = relationships[j].targetHighLight;

			recordNode.parentRelationshipDefName = relationshipNode.name;
			recordNode.parentRelationshipDefId = relationshipNode.relationshipDefId;
			recordNode.parentRelationshipId = relationships[j].id;
            recordNode.type = item_types.RECORD;
			recordNode.isStub = true;
            var recordNodeItem = mainTree_Store.newItem(recordNode, {parent: rDomainItem, attribute:"children"} );
            //alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
    
    // 2. Save the items of store
    mainTree_Store.save();
    
    // 3. Create tree now
    byRecord_createMainTree();
	displayDiv("mainTreeContainer", true);
	
	// 4. Reset button pallete (enable/disable add,delete, etc., buttons)
	byRecord_resetView ();
	byRecord_refreshMainTreeButtonsPallete();
	byRecord_refreshRearrangeTreeButtonsPallete();
    return;
}


// Function to create the main tree. Store should be populated before calling this.
function byRecord_createMainTree () {
    var newModel  = new dijit.tree.TreeStoreModel({
        store: mainTree_Store,
        query: {id:'rootDomainID'},
        childrenAttrs: ["children"],
		mayHaveChildren: customMainTreeMayHaveChildren
    });
    var mainTreeObj = dijit.byId("mainTree");
    if (dijit.byId("mainTree")) {dijit.byId("mainTree").destroy()}
    mainTreeObj = new dijit.TreeCustom({
        id: "mainTree",
        model: newModel,
        customOnClick: mainTreeClicked,
		//onClose: mainTreeNodeClosed,
        getIconClass: mainTreeGetIconClass,
		lazyLoadItems: lazyLoad_MainTreeRelationships,			
		dndController:"dijit._tree.dndSource",
		checkAcceptance: mainTreeDnDCheckAcceptance
    }, document.createElement("div"));
    mainTreeObj.startup();
    dojo.byId("mainTreeContainer").appendChild(mainTreeObj.domNode);
}

function customMainTreeMayHaveChildren(item) {
	return true;
}

function mainTreeDnDCheckAcceptance(source, nodes)  {
	//Dont accept if the source is the same tree
	if(this.tree == source.tree) return false;	
	else return true;
}

// Function called when tree (MAIN Tree) is trying load the childrens for expanded Record.
function lazyLoad_MainTreeRelationships(node, callback_function) {
	var parentItem = node.item;
	if(parentItem == null ) {
		callback_function();
		return;
	}
	var parentType = mainTree_Store.getValue (parentItem, "type");
	if(parentType != item_types.RECORD) {
		callback_function();
		return;
	}
	var isStillStub = mainTree_Store.getValue (parentItem, "isStub");
	var parentName = mainTree_Store.getValue (parentItem, "name");
	var parentEUID = mainTree_Store.getValue (parentItem, "EUID");
	var parentDomain = mainTree_Store.getValue (parentItem, "parentDomain");
	//alert("isStillStub : " + isStillStub + " : " + parentName + ", parentEUID is: " + parentEUID + "parentDomain : " + parentDomain);

	if(isStillStub ) {
		var domainSearchObj = {name:parentDomain, attributes:[{EUID: parentEUID}]};
		 
		RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, { callback:function(dataFromServer) {
			mainTree_loadRelationshipsForRecord(dataFromServer, node, callback_function); }
		});
		// make the stub marking as false.
		mainTree_Store.setValue(parentItem, "isStub", false);
	}
	//callback_function();
}

// To load next level of relationships. Loaded only when user expands the record. 
function mainTree_loadRelationshipsForRecord(data, node, callback_function) {
	if(data == null) {
		callback_function();
		return;
	}
	var parentItem = node.item;
	var parentDomain = mainTree_Store.getValue (parentItem, "parentDomain");
	var parentItemID = mainTree_Store.getValue (parentItem, "id");
	var parentItemEUID = mainTree_Store.getValue (parentItem, "EUID");
	var parentDomain = mainTree_Store.getValue (parentItem, "parentDomain");

	for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
		var useSourceDomain = false;
		if(tempRelObj.relationshipDefView.sourceDomain != parentDomain) // TBD: need to relook @ this logic
			useSourceDomain = true; 
		else useSourceDomain = false;

//alert(i + "  useSourceDomain=" + useSourceDomain + " " +  tempRelObj.relationshipDefView.id + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
		
        var relationshipNode = {};
        relationshipNode.id = parentItemID + "_" + i + "_" + tempRelObj.relationshipDefView.id;
		relationshipNode.relationshipDefId = tempRelObj.relationshipDefView.id; 
        relationshipNode.name = tempRelObj.relationshipDefView.name;
        relationshipNode.biDirection = tempRelObj.relationshipDefView.biDirection;
		if(useSourceDomain)
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.targetDomain;
		
		relationshipNode.parentDomain = parentDomain;
		relationshipNode.parentRecordEUID = parentItemEUID;
		
        relationshipNode.type = item_types.RELATIONSHIP;
        var rNodeItem = mainTree_Store.newItem(relationshipNode, {parent: parentItem, attribute:"children"} );

        var relationshipDomain = {};
        relationshipDomain.id = i + "_" + relationshipNode.id + "_" + tempRelObj.relationshipDefView.sourceDomain;
		if(useSourceDomain)
			relationshipDomain.name = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipDomain.name = tempRelObj.relationshipDefView.targetDomain;
        relationshipDomain.parentRelationshipDefName = relationshipNode.name;
		relationshipDomain.parentRelationshipDefId = relationshipNode.relationshipDefId;
		relationshipDomain.parentDomain = relationshipNode.parentDomain;
		relationshipDomain.parentRecordEUID = parentItemEUID;
		
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
			recordNode.parentDomain = relationshipDomain.name;
			recordNode.relationshipFromDomain = byRecord_CurrentWorking_Domain;
			recordNode.relationshipToDomain = relationshipDomain.name;

			recordNode.fromRecordHighLight = relationships[j].sourceHighLight;
			recordNode.toRecordHighLight = relationships[j].targetHighLight;

			recordNode.parentRelationshipDefName = relationshipNode.name;
			recordNode.parentRelationshipDefId = relationshipNode.relationshipDefId;
			recordNode.parentRelationshipId = relationships[j].id;
            recordNode.type = item_types.RECORD;
            var recordNodeItem = mainTree_Store.newItem(recordNode, {parent: rDomainItem, attribute:"children"} );
            //alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
	callback_function();
}


// function to delete items from main tree store.
function deleteItemsFromStore(items, req) {
    for(i=0; i<items.length; i++) {
        var status = mainTree_Store.deleteItem(items[i]);
        mainTree_Store.save();
    }
}

var mainTree_isAddPossible = false;
var mainTree_isDeletePossible = false;
var mainTree_isFindPossible = false;
var mainTree_isMovePossible = false;

// Main tree click event is captured by this function
function mainTreeClicked(item, node, allSelectedItems ) {
//alert('allSelectedItems ' + allSelectedItems.length);
	byRecord_Selected_Relationship = null;
	byRecord_Selected_Record = null;
	
	mainTree_isAddPossible = false; 
	mainTree_isDeletePossible = true;
	mainTree_isFindPossible = false;
	mainTree_isMovePossible = false;
	
	var recFromSameDomainCount = 0, prevRecDomainName = null;
	var isRootDomainSelected = false, isRootRecordSelected = false;
	
	for(i=0; i<allSelectedItems.length; i++) {
		var tempItem = allSelectedItems[i];
		var itemType = mainTree_Store.getValue(tempItem, "type");
		var itemName = mainTree_Store.getValue(tempItem, "name");	
		//alert(itemType + " : " + itemName);
		var isRoot = mainTree_Store.getValue(tempItem, "isRoot");
		
		switch (itemType) {
			case item_types.DOMAIN:
				//if(itemName == byRecord_CurrentWorking_Domain) {
				if(isRoot !=null && isRoot) {
					mainTree_isDeletePossible = false;
					isRootDomainSelected = true;
					continue;
				}
				var parentRelationshipDefName = mainTree_Store.getValue(tempItem, "parentRelationshipDefName");
				byRecord_CurrentSelected_RelationshipDefName = parentRelationshipDefName; // Used for Add
				byRecord_CurrentSelected_TargetDomain = itemName; // User for add
				mainTree_isAddPossible = true;
				mainTree_isFindPossible = true;
				mainTree_isMovePossible = true;
				//alert("its a domain ");
				break;
			case item_types.RELATIONSHIP:
				mainTree_isAddPossible = false;
				mainTree_isMovePossible = true;
				//alert("its a relationship");
				break;
			case item_types.RECORD:
				var itemEUID = mainTree_Store.getValue(tempItem, "EUID");
				var tempDomain = mainTree_Store.getValue(tempItem, "parentDomain");
				//alert(itemEUID + " : " + byRecord_CurrentWorking_EUID);
				
				// logic to figure out if all the selected records belong to same domain or not. (required to enable/disable add/delete/etc., operations)
				if(prevRecDomainName == null) prevRecDomainName = tempDomain;
				if(tempDomain == prevRecDomainName) 
					recFromSameDomainCount ++;
				else
					recFromSameDomainCount --;
				prevRecDomainName = tempDomain;
				
				if(isRoot !=null && isRoot) {
					byRecord_Selected_Record = {};
					byRecord_Selected_Record["EUID"] = itemEUID;
					
					var tempName = mainTree_Store.getValue(item, "name");
				    byRecord_Selected_Record["domain"] = tempDomain;
					byRecord_Selected_Record["sourceRecordHighLight"] = tempName;
					isRootRecordSelected = true;
				} else {
                    //alert("not root");                
					var tempRelationshipId = mainTree_Store.getValue(tempItem, "parentRelationshipId");
					
					byRecord_Selected_Relationship = {};
					byRecord_Selected_Relationship["relationshipId"] = mainTree_Store.getValue(tempItem, "parentRelationshipId");
					byRecord_Selected_Relationship["sourceDomain"] = mainTree_Store.getValue(tempItem, "relationshipFromDomain");
					byRecord_Selected_Relationship["targetDomain"] = mainTree_Store.getValue(tempItem, "relationshipToDomain");
					byRecord_Selected_Relationship["relationshipDefName"] = mainTree_Store.getValue(tempItem, "parentRelationshipDefName");
					
					byRecord_Selected_Relationship["sourceRecordHighLight"] = mainTree_Store.getValue(tempItem, "fromRecordHighLight");
					byRecord_Selected_Relationship["targetRecordHighLight"] = mainTree_Store.getValue(tempItem, "toRecordHighLight");
					
				}
				//alert("its a record");
				break;
		}
	}
	// If multi-selected, then no details can be shown.
	if(allSelectedItems.length > 1) {
		byRecord_Selected_Relationship = null;
		byRecord_Selected_Record = null;
		
		mainTree_isAddPossible = false;
		mainTree_isFindPossible = false;
		mainTree_isMovePossible = false;
	}
	// If all selected items are records & from same domain, then enable add,find buttons
	if(recFromSameDomainCount == allSelectedItems.length) {
		mainTree_isAddPossible = true;
		mainTree_isFindPossible = true;
		mainTree_isMovePossible = true;
	}
	
	// If root nodes (either root-domain or root-record) are selected, disable add, move, find buttons
	if(isRootRecordSelected || isRootDomainSelected) {
		mainTree_isAddPossible = false;
		mainTree_isMovePossible = false;
		mainTree_isFindPossible = false;
	}

	byRecord_refreshMainTreeButtonsPallete();
	byRecord_refreshRearrangeTreeButtonsPallete();
	
	// Show details for selected record/relationship (main tree).
	byRecord_ShowDetails();
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
	byRecord_refreshMainTreeButtonsPallete();
}

// Function to get the data for rearrange tree.
function byRecord_initRearrangeTree () {
	var selectedDomain = "Person";
	var selectedEUID = "000-000-555";
	var domainSearchObj = {name:selectedDomain};
	if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
	if(byRecord_CurrentWorking_EUID != null) selectedEUID = byRecord_CurrentWorking_EUID;
	
	RelationshipHandler.getEnterprises(domainSearchObj, byRecord_initRearrangeTree_CB);
    return;
    // For testing, simulate data and call callback method 
	var data = {};
    data.domain = selectedDomain;
	
	var recordObjs = []; // array of relationship objects
    for(i=0; i<4; i++) {
		var tempRecordObj = {};
		tempRecordObj.EUID = "000-000-"+i;
		tempRecordObj.highLight = "Record Name " + i;
		recordObjs.push (tempRecordObj);
	}
	
	data.domainRecords = recordObjs;
	
	byRecord_initRearrangeTree_CB(data);
}

// Callback function: for getting data for re-arrange tree
function byRecord_initRearrangeTree_CB (data) {
    // 1. Populate store for tree
    // 1a. delete existing data items from store.
    rearrangeTree_Store.fetch ({query:{},onComplete: deleteItemsFromRearrangeTreeStore, queryOptions: {deep:true}});

	
	// 1b. Create root element, i.e., Domain eg., Person, UK Patient.
    var rootDomainItem = {};
    rootDomainItem.id = "rootDom";
    rootDomainItem.name = byRecord_CurrentWorking_Domain;
    rootDomainItem.type = item_types.DOMAIN;
	rootDomainItem.isRoot = true;
	
	var rootDomain = rearrangeTree_Store.newItem( rootDomainItem , null);
	
	var domainRecords = data;
	
	for(i=0; i<domainRecords.length; i++) {
		var tempRec = domainRecords[i];
		
		var recordNode = {};
        recordNode.id = i + "_" + tempRec.EUID;
		recordNode.EUID = tempRec.EUID;
        recordNode.name = tempRec.highLight;
		recordNode.type = item_types.RECORD;
		recordNode.parentDomain = rootDomainItem.name;
		recordNode.isStub = true;
		recordNode.isRoot = true;

        recordNode.type = item_types.RECORD;
        var recordNodeItem = rearrangeTree_Store.newItem(recordNode, {parent: rootDomain, attribute:"children"} );
	}
	
	// 2. Save the items of store
    rearrangeTree_Store.save();

    // 3. Create tree now
    byRecord_createRearrangeTree();
	
	displayDiv("rearrangeTreeContainer", true);
	// 4. Reset button pallete (enable/disable add,delete, etc., buttons)
	byRecord_resetView ();
	byRecord_refreshMainTreeButtonsPallete();
	byRecord_refreshRearrangeTreeButtonsPallete();
}

// Custom function, for deciding if a node may have children or not. (For Rearrange tree)
function customRearrangeTreeMayHaveChildren(item) {
	var node = item.node;
	return true;
}

// Function to create the Rearrange tree. Store should be populated before calling this.
function byRecord_createRearrangeTree () {
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
        getIconClass: rearrangeTreeGetIconClass,
		lazyLoadItems: lazyLoadRearrangeTreeRelationships
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

// Function called when tree (rearrange Tree) is trying load the childrens for expanded node.
function lazyLoadRearrangeTreeRelationships(node, callback_function) {
	var parentItem = node.item;
	if(parentItem == null ) {
		callback_function();
		return;
	}
	var parentType = rearrangeTree_Store.getValue (parentItem, "type");
	if(parentType != item_types.RECORD) {
		callback_function();
		return;
	}
	var isStillStub = rearrangeTree_Store.getValue (parentItem, "isStub");
	var parentName = rearrangeTree_Store.getValue (parentItem, "name");
	var parentEUID = rearrangeTree_Store.getValue (parentItem, "EUID");
	//alert(parentName + " parentEUID is: " + parentEUID);
	
	if(isStillStub ) {
		var selectedDomain = "Person";
		var selectedEUID = parentEUID;
		var domainSearchObj = {name:selectedDomain};
		if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
		
		RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, { callback:function(dataFromServer) {
			rearrangeTree_loadRelationshipsForRecord(dataFromServer, node, callback_function); }
		});
		// make the stub marking as false.
		rearrangeTree_Store.setValue(parentItem, "isStub", false);
	}
	//callback_function();
}

// To load relationships for rearrange tree, when record is expanded.
function rearrangeTree_loadRelationshipsForRecord(data, node, callback_function) {
	var parentItem = node.item;
	var parentDomain = rearrangeTree_Store.getValue (parentItem, "parentDomain");
	var parentItemID = rearrangeTree_Store.getValue (parentItem, "id");
	var parentItemEUID = rearrangeTree_Store.getValue (parentItem, "EUID");
	
	for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
		var useSourceDomain = false;
		if(tempRelObj.relationshipDefView.sourceDomain != byRecord_CurrentWorking_Domain)
			useSourceDomain = true; 
		else useSourceDomain = false;

//alert(i + "  useSourceDomain=" + useSourceDomain + " " +  tempRelObj.relationshipDefView.id + " -- " + tempRelObj.relationshipDefView.sourceDomain + " : "+ tempRelObj.relationshipDefView.targetDomain);
		
        var relationshipNode = {};
        relationshipNode.id = parentItemID + "_" + i + "_" + tempRelObj.relationshipDefView.id;
		relationshipNode.relationshipDefId = tempRelObj.relationshipDefView.id; 
        relationshipNode.name = tempRelObj.relationshipDefView.name;
        relationshipNode.biDirection = tempRelObj.relationshipDefView.biDirection;
		if(useSourceDomain)
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipNode.otherDomain = tempRelObj.relationshipDefView.targetDomain;
		
		relationshipNode.parentDomain = parentDomain;
		relationshipNode.parentRecordEUID = parentItemEUID;
		
        relationshipNode.type = item_types.RELATIONSHIP;
        var rNodeItem = rearrangeTree_Store.newItem(relationshipNode, {parent: parentItem, attribute:"children"} );

        var relationshipDomain = {};
        relationshipDomain.id = i + "_" + relationshipNode.id + "_" + tempRelObj.relationshipDefView.sourceDomain;
		if(useSourceDomain)
			relationshipDomain.name = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipDomain.name = tempRelObj.relationshipDefView.targetDomain;
        relationshipDomain.parentRelationshipDefName = relationshipNode.name;
		relationshipDomain.parentRelationshipDefId = relationshipNode.relationshipDefId;
		relationshipDomain.parentDomain = relationshipNode.parentDomain;
		relationshipDomain.parentRecordEUID = parentItemEUID;
		
        relationshipDomain.type = item_types.DOMAIN;
        var rDomainItem = rearrangeTree_Store.newItem(relationshipDomain, {parent: rNodeItem, attribute:"children"} );
        
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
			recordNode.parentDomain = relationshipDomain.name;
			recordNode.relationshipFromDomain = byRecord_CurrentWorking_Domain;
			recordNode.relationshipToDomain = relationshipDomain.name;

			recordNode.fromRecordHighLight = relationships[j].sourceHighLight;
			recordNode.toRecordHighLight = relationships[j].targetHighLight;

			recordNode.parentRelationshipDefName = relationshipNode.name;
			recordNode.parentRelationshipDefId = relationshipNode.relationshipDefId;
			recordNode.parentRelationshipId = relationships[j].id;
            recordNode.type = item_types.RECORD;
            var recordNodeItem = rearrangeTree_Store.newItem(recordNode, {parent: rDomainItem, attribute:"children"} );
            //alert(j + " " + relationships[j].sourceEUID + " :: " + relationships[j].targetEUID);
        }
    }
	
	callback_function();
}

var rearrangeTree_isAddPossible = false;
var rearrangeTree_isDeletePossible = false;
var rearrangeTree_isFindPossible = false;
var rearrangeTree_isMovePossible = false;

function rearrangeTreeClicked(item, node, allSelectedItems ) {
	//alert("rearrange tree clicked");
	byRecord_rearrangeTree_Selected_Relationship = null; 
	byRecord_rearrangeTree_Selected_Record = null; 

	rearrangeTree_isAddPossible = false; 
	rearrangeTree_isDeletePossible = true;
	rearrangeTree_isFindPossible = false;
	rearrangeTree_isMovePossible = false;
	
	var recFromSameDomainCount = 0, prevRecDomainName = null;
	var isRootDomainSelected = false, isRootRecordSelected = false;
	
	for(i=0; i<allSelectedItems.length; i++) {
		var tempItem = allSelectedItems[i];
		var itemType = rearrangeTree_Store.getValue(tempItem, "type");
		var itemName = rearrangeTree_Store.getValue(tempItem, "name");	
		//alert(itemType + " : " + itemName);
		var isRoot = rearrangeTree_Store.getValue(tempItem, "isRoot");
		
		switch (itemType) {
			case item_types.DOMAIN:
				if(itemName == byRecord_CurrentWorking_Domain) {
					rearrangeTree_isDeletePossible = false;
					rearrangeTree_isFindPossible = true;
					isRootDomainSelected = true;
					continue;
				}
				rearrangeTree_isAddPossible = true;
				rearrangeTree_isFindPossible = true;
				rearrangeTree_isMovePossible = true;
				var parentRelationshipDefName = rearrangeTree_Store.getValue(tempItem, "parentRelationshipDefName");
				//alert("its a domain ");
				break;
			case item_types.RELATIONSHIP:
				rearrangeTree_isAddPossible = false;
				rearrangeTree_isMovePossible = true;
				//alert("its a relationship");
				break;
			case item_types.RECORD:
				var itemEUID = rearrangeTree_Store.getValue(tempItem, "EUID");
				var tempDomain = rearrangeTree_Store.getValue(tempItem, "parentDomain");
				//alert(itemEUID + " : " + byRecord_CurrentWorking_EUID);
				
				// logic to figure out if all the selected records belong to same domain or not. (required to enable/disable add/delete/etc., operations)
				if(prevRecDomainName == null) prevRecDomainName = tempDomain;
				if(tempDomain == prevRecDomainName) 
					recFromSameDomainCount ++;
				else
					recFromSameDomainCount --;
				prevRecDomainName = tempDomain;
				
				if(isRoot !=null && isRoot) {
					byRecord_rearrangeTree_Selected_Record = {};
					byRecord_rearrangeTree_Selected_Record["EUID"] = itemEUID;
					
					var tempName = rearrangeTree_Store.getValue(item, "name");
				    byRecord_rearrangeTree_Selected_Record["domain"] = tempDomain;
					byRecord_rearrangeTree_Selected_Record["sourceRecordHighLight"] = tempName;
					isRootRecordSelected = true;
				} else {
                    //alert("not root");                
					var tempRelationshipId = rearrangeTree_Store.getValue(tempItem, "parentRelationshipId");
					
					byRecord_rearrangeTree_Selected_Relationship = {};
					byRecord_rearrangeTree_Selected_Relationship["relationshipId"] = rearrangeTree_Store.getValue(tempItem, "parentRelationshipId");
					byRecord_rearrangeTree_Selected_Relationship["sourceDomain"] = rearrangeTree_Store.getValue(tempItem, "relationshipFromDomain");
					byRecord_rearrangeTree_Selected_Relationship["targetDomain"] = rearrangeTree_Store.getValue(tempItem, "relationshipToDomain");
					byRecord_rearrangeTree_Selected_Relationship["relationshipDefName"] = rearrangeTree_Store.getValue(tempItem, "parentRelationshipDefName");
					
					byRecord_rearrangeTree_Selected_Relationship["sourceRecordHighLight"] = rearrangeTree_Store.getValue(tempItem, "fromRecordHighLight");
					byRecord_rearrangeTree_Selected_Relationship["targetRecordHighLight"] = rearrangeTree_Store.getValue(tempItem, "toRecordHighLight");
					
				}
				//alert("its a record");
				break;
		}
	}
		// If multi-selected, then no details can be shown.
	if(allSelectedItems.length > 1) {
		byRecord_rearrangeTree_Selected_Relationship = null;
		byRecord_rearrangeTree_Selected_Record = null;
		
		rearrangeTree_isAddPossible = false;
		rearrangeTree_isFindPossible = false;
		rearrangeTree_isMovePossible = false;
	}
	// If all selected items are records & from same domain, then enable add,find buttons
	if(recFromSameDomainCount == allSelectedItems.length) {
		rearrangeTree_isAddPossible = true;
		rearrangeTree_isFindPossible = true;
		rearrangeTree_isMovePossible = true;
	}

	// If root nodes (either root-domain or root-record) are selected, disable add, move, find buttons
	if(isRootRecordSelected || isRootDomainSelected) {
		rearrangeTree_isAddPossible = false;
		rearrangeTree_isMovePossible = false;
		//rearrangeTree_isFindPossible = false;
	}
	
	byRecord_refreshMainTreeButtonsPallete();
	byRecord_refreshRearrangeTreeButtonsPallete();
	
	// Show details section for the selected record/relationship (rearrange tree)
	byRecord_rearrangeTree_ShowDetails();
}


// function to find out if move RIGHT is possible (From Main tree to rearrange tree)
function getIsMoveRightPossible() {
	// If rearrange tree is not shown, then move is not possible.
	if(! isRearrangeTreeShown) return false;

	// TODO: return proper value based on node selections in both trees.
	var mainTreeObj = dijit.byId("mainTree");
	var rearrangeTreeObj = dijit.byId("rearrangeTree");
	
	if(mainTreeObj == null || rearrangeTreeObj == null) {
		return false;
	}
	
	var isValidMove = byRecord_isMoveValid(mainTreeObj, rearrangeTreeObj);
	//alert("right move valid : " + isValidMove);
	return isValidMove;
}

// function to find out if move LEFT is possible (From Rearrange tree to main tree)
function getIsMoveLeftPossible() {
	// If rearrange tree is not shown, then move is not possible.
	if(! isRearrangeTreeShown) return false;

	// TODO: return proper value based on node selections in both trees.
	var mainTreeObj = dijit.byId("mainTree");
	var rearrangeTreeObj = dijit.byId("rearrangeTree");
	
	if(mainTreeObj == null || rearrangeTreeObj == null) {
		return false;
	}
	
	var isValidMove = byRecord_isMoveValid( rearrangeTreeObj, mainTreeObj);
	//alert("left move valid : " + isValidMove);
	return isValidMove;
}

// Common function to find if the selected item(s) in sourceTreeObj can be moved to selected item in targetTreeObj.
function byRecord_isMoveValid (sourceTreeObj, targetTreeObj) {
	if(sourceTreeObj == null || targetTreeObj == null) return false;
	
	var tempSourceStore = sourceTreeObj.model.store;
	var tempTargetStore = targetTreeObj.model.store;
	
	if(tempSourceStore == null || tempSourceStore == null) return false;
	
	var sourceSelectedItems = sourceTreeObj.getSelectedItems();
	var targetSelectedItems = targetTreeObj.getSelectedItems();
	
	if(sourceSelectedItems == null || sourceSelectedItems.length <= 0)
		return false;
	if(targetSelectedItems == null || targetSelectedItems.length <= 0)
		return false;
	
	if(targetSelectedItems.length > 1 ) {
		// While moving, only SINGLE item must be selected in target tree.
		return false;
	}
	
	var sourceKeyItem = null, targetKeyItem = null;
	
	targetKeyItem = targetSelectedItems[0];
	
	if(sourceSelectedItems.length == 1) {
		sourceKeyItem = sourceSelectedItems[0];
	} else {
		var recFromSameLevel = 0, prevRecDomainName = null, prevRecRelatioshipDefId = null;
		for(i=0; i<sourceSelectedItems.length; i++) {
			//if multiple nodes are selected, check if all are RECORDs and belong to same domain. If not, move is not possible.
			var tempItem = sourceSelectedItems[i];
			var tempType = tempSourceStore.getValue(tempItem, "type");
			
			if(tempType == item_types.RECORD) {
				var tempParentDomain = tempSourceStore.getValue(tempItem, "parentDomain");
				var tempParentRelationshipDefId = tempSourceStore.getValue(tempItem, "parentRelationshipDefId");	
				
				// logic to figure out if all the selected records belong to same domain or not. 
				if(prevRecDomainName == null) prevRecDomainName = tempParentDomain;
				if(prevRecRelatioshipDefId == null) prevRecRelatioshipDefId = tempParentRelationshipDefId;
				if(tempParentDomain == prevRecDomainName && prevRecRelatioshipDefId == tempParentRelationshipDefId) recFromSameLevel ++;
				else recFromSameLevel --;

				prevRecDomainName = tempParentDomain;
				prevRecRelatioshipDefId = tempParentRelationshipDefId;
			}
		}

		if(recFromSameLevel != sourceSelectedItems.length) {
			// Multiple selected items doesn't belong to same level/domain.
			return false;
		} else {
			sourceKeyItem = sourceSelectedItems[0];
		}
	}

	// Check if the move is valid between sourceKeyItem & targetKeyItem.
	var sourceItemType = tempSourceStore.getValue(sourceKeyItem, "type");
	var targetItemType = tempTargetStore.getValue(targetKeyItem, "type");
	
//alert(sourceItemType + " : " + targetItemType);
	
	switch (sourceItemType) {
		case item_types.RELATIONSHIP: 
			// Source item type is RELATIONSHIP, check for the valid types for target...
			if(targetItemType == item_types.DOMAIN) {
				// Relationship type cannot be moved to Domain type, so not a valid Move. return false.
				return false;
			} else if(targetItemType == item_types.RELATIONSHIP) {
				// Both source & target item types are Relationships,. 
				// Check if both relationship Def Id's match? If yes, move is valid.
				var sourceRelationshipDefId = tempSourceStore.getValue(sourceKeyItem, "relationshipDefId");
				var targetRelationshipDefId = tempTargetStore.getValue(targetKeyItem, "relationshipDefId");
				if(sourceRelationshipDefId == targetRelationshipDefId)
					return true;
			} else if(targetItemType == item_types.RECORD) {
				// target type is RECORD, check if record's parentDomain is same as parentDomain of source relationshipdef
				var sourceParentDomain = tempSourceStore.getValue(sourceKeyItem, "parentDomain");
				var targetParentDomain = tempTargetStore.getValue(targetKeyItem, "parentDomain");
				// If parent domain's match, move is valid. return true.
				if(sourceParentDomain == targetParentDomain)
					return true;
			}
			break;
		case item_types.DOMAIN:
			// Source item type is DOMAIN, check for the valid types for target...
			var isRootDomain = tempSourceStore.getValue(sourceKeyItem, "isRoot");
			
			if(isRootDomain) { 
				// Root domain cannot be moved.
				return false;
			}
			if(targetItemType == item_types.DOMAIN) {
				// If both source & target are Domains.
				var isTargetRootDomain = tempTargetStore.getValue(targetKeyItem, "isRoot");
				if(isTargetRootDomain) return false;
				// Check if both has same relationshipDef as parent.
				
				var sourceParentRelationshipDef = tempSourceStore.getValue(sourceKeyItem, "parentRelationshipDefId");
				var targetParentRelationshipDef = tempTargetStore.getValue(targetKeyItem, "parentRelationshipDefId");	
				if(sourceParentRelationshipDef == targetParentRelationshipDef)
					return true;
				
			} else if(targetItemType == item_types.RELATIONSHIP) {
				// target type is Relationship, check if sourceDomain's parent domain & targetRelationship's parent domain match?
				var sourceParentDomain = tempSourceStore.getValue(sourceKeyItem, "parentDomain");
				var targetParentDomain = tempTargetStore.getValue(targetKeyItem, "parentDomain");
				
				var sourceParentRelationshipDef = tempSourceStore.getValue(sourceKeyItem, "parentRelationshipDefId");
				var targetRelationshipDefId = tempTargetStore.getValue(targetKeyItem, "relationshipDefId");
				
				var domainsMatch = (sourceParentDomain == targetParentDomain);
				var relationshipDefsMatch = (sourceParentRelationshipDef == targetRelationshipDefId);
				
				if(domainsMatch && relationshipDefsMatch)
					return true;
				
			} else if(targetItemType == item_types.RECORD) {
				// Target type is RECORD.
				// Domain type cannot be moved to record type. return false
				return false;
			}
			break;
		case item_types.RECORD:
			// Source item type is RECORD, check for the valid types for target...
			var isRootRecord = tempSourceStore.getValue(sourceKeyItem, "isRoot");
			if(isRootRecord) return false; // cannot move Primary record.
				
			if(targetItemType == item_types.DOMAIN) {
				// target type is domain, check if target domain & source record's parent domain is same.
				
				var isTargetRootDomain = tempTargetStore.getValue(targetKeyItem, "isRoot");
				if(isTargetRootDomain) return false; // Cannot be moved to primary domain.
					
				var sourceParentDomain = tempSourceStore.getValue(sourceKeyItem, "parentDomain");
				var targetDomain = tempTargetStore.getValue(targetKeyItem, "name");
				
				var sourceParentRelationshipDef = tempSourceStore.getValue(sourceKeyItem, "parentRelationshipDefId");
				var targetDomainParentRelationshipDef = tempTargetStore.getValue(targetKeyItem, "parentRelationshipDefId");
				
				var domainsMatch = (sourceParentDomain == targetDomain);
				var relationshipDefsMatch = (sourceParentRelationshipDef == targetDomainParentRelationshipDef);
				
				if(domainsMatch && relationshipDefsMatch)
					return true;
				
			} else if(targetItemType == item_types.RELATIONSHIP) {
				// Cannot move Record type to Relationship type.
				return false;
			} else if(targetItemType == item_types.RECORD) {
				// Both are of type Record. check if parent domain's of both match & parent relationshipDef id's match.
				var sourceParentDomain = tempSourceStore.getValue(sourceKeyItem, "parentDomain");
				var targetParentDomain = tempTargetStore.getValue(targetKeyItem, "parentDomain");
				
				var sourceParentRelationshipDef = tempSourceStore.getValue(sourceKeyItem, "parentRelationshipDefId");
				var targetParentRelationshipDef = tempTargetStore.getValue(targetKeyItem, "parentRelationshipDefId");	
				
				var domainsMatch = (sourceParentDomain == targetParentDomain);
				var relationshipDefsMatch = (sourceParentRelationshipDef == targetParentRelationshipDef);
				if(domainsMatch && relationshipDefsMatch)
					return true;
			}
			break;
		default:
			return false;
	}

	return false;
}


// function to Add operation, for MAIN tree
function byRecord_mainTree_addOperation () {
	if(!mainTree_isAddPossible) return;
	
	var mainTreeObj = dijit.byId("mainTree");
	if(mainTreeObj == null) return;
	
	var tempStore = mainTreeObj.model.store;
	
	var mainTreeSelectedNodes = mainTreeObj.getSelectedNodes();
	var mainTreeSelectedItems = mainTreeObj.getSelectedItems();
	
	if(mainTreeSelectedItems == null || mainTreeSelectedItems.length <= 0)
		return false;
	
	var keyItem = null; // The key item to be used as base for Add operation. 
		
	keyItem = mainTreeSelectedItems [0];
	keyNode = mainTreeSelectedNodes [0];
	
	var keyItemType = tempStore.getValue(keyItem, "type");
	
	if(keyItemType == item_types.RELATIONSHIP) return; // if key item is of relationship type, add operation is not possible.

	if(keyItemType == item_types.RECORD) {
		// If item type is of Record, get it's parent domain, and make it the key item.
		var tempNode = keyNode.getParent();
		if(tempNode != null)
			keyItem = tempNode.item;
	}
	
	byRecord_CurrentSelected_TargetDomain = tempStore.getValue(keyItem, "name");
	byRecord_CurrentSelected_RelationshipDefName = tempStore.getValue(keyItem, "parentRelationshipDefName");
	byRecord_CurrentSelected_SourceDomain = tempStore.getValue(keyItem, "parentDomain");
	byRecord_CurrentSelected_SourceEUID = tempStore.getValue(keyItem, "parentRecordEUID");
	
	//alert(byRecord_CurrentSelected_TargetDomain + " : " + byRecord_CurrentSelected_RelationshipDefName +  " : " + byRecord_CurrentSelected_SourceDomain + " : " + byRecord_CurrentSelected_SourceEUID);
	
	//byRecord_CurrentSelected_TargetDomain = "Company";
    //byRecord_CurrentSelected_RelationshipDefName = "EmployedBy";
	//byRecord_CurrentSelected_SourceDomain = "Person";
	//byRecord_CurrentSelected_SourceEUID = "000001";

	showByRecordAddDialog();
}

// function to Add operation, for REARRANGE tree
function byRecord_rearrangeTree_addOperation () {
	if(!rearrangeTree_isAddPossible) return;
	
	var rearrangeTreeObj = dijit.byId("rearrangeTree");
	if(rearrangeTreeObj == null) return;
	
	var tempStore = rearrangeTreeObj.model.store;
	
	var rearrangeTreeSelectedNodes = rearrangeTreeObj.getSelectedNodes();
	var rearrangeTreeSelectedItems = rearrangeTreeObj.getSelectedItems();
	
	if(rearrangeTreeSelectedItems == null || rearrangeTreeSelectedItems.length <= 0)
		return false;
	
	var keyItem = null; // The key item to be used as base for Add operation. 
		
	keyItem = rearrangeTreeSelectedItems [0];
	keyNode = rearrangeTreeSelectedNodes [0];
	
	var keyItemType = tempStore.getValue(keyItem, "type");
	
	if(keyItemType == item_types.RELATIONSHIP) return; // if key item is of relationship type, add operation is not possible.

	if(keyItemType == item_types.RECORD) {
		// If item type is of Record, get it's parent domain, and make it the key item.
		var tempNode = keyNode.getParent();
		if(tempNode != null)
			keyItem = tempNode.item;
	}
	
	byRecord_CurrentSelected_TargetDomain = tempStore.getValue(keyItem, "name");
	byRecord_CurrentSelected_RelationshipDefName = tempStore.getValue(keyItem, "parentRelationshipDefName");
	byRecord_CurrentSelected_SourceDomain = tempStore.getValue(keyItem, "parentDomain");
	byRecord_CurrentSelected_SourceEUID = tempStore.getValue(keyItem, "parentRecordEUID");
	
	//alert(byRecord_CurrentSelected_TargetDomain + " : " + byRecord_CurrentSelected_RelationshipDefName +  " : " + byRecord_CurrentSelected_SourceDomain + " : " + byRecord_CurrentSelected_SourceEUID);

	showByRecordAddDialog();
}

// function to Delete operation, for MAIN tree
function byRecord_mainTree_deleteOperation () {
	if(!mainTree_isDeletePossible) return;
	alert("Not yet implemented");
}

// function to Delete operation, for REARRANGE tree
function byRecord_rearrangeTree_deleteOperation () {
	if(!rearrangeTree_isDeletePossible) return;
	alert("Not yet implemented");
}

// function to Move (Right) operation, for MAIN tree
function byRecord_mainTree_moveOperation() {
	if(!mainTree_isMovePossible || !getIsMoveRightPossible()) return;
	alert("Not yet implemented");
}

// function to Move (Left) operation, for REARRANGE tree
function byRecord_rearrangeTree_moveOperation() {
	if(!rearrangeTree_isMovePossible || !getIsMoveLeftPossible()) return;
	alert("Not yet implemented");
}

// function to Find operation, for MAIN tree
function byRecord_mainTree_findOperation() {
	if(!mainTree_isFindPossible) return;
	alert("Not yet Implemented");
}

// function to Find operation, for REARRANGE tree
function byRecord_rearrangeTree_findOperation() {
	if(!rearrangeTree_isFindPossible) return;
	alert("Not yet Implemented");
}

//function to refresh buttons pallete for main tree
function byRecord_refreshMainTreeButtonsPallete () {
	//alert("add : " + mainTree_isAddPossible +"\n delete:"+ mainTree_isDeletePossible);
	
	var imgDeleteButtonObj = dojo.byId("imgMainTreeDeleteButton");
    if(imgDeleteButtonObj != null) {
        if(mainTree_isDeletePossible)
            imgDeleteButtonObj.src =   deleteButtonEnabled.src;
        else
            imgDeleteButtonObj.src =   deleteButtonDisabled.src;
    }
	
	var imgAddButtonObj = dojo.byId("imgMainTreeAddButton");
    if(imgAddButtonObj != null) {
        if(mainTree_isAddPossible)
            imgAddButtonObj.src =   addButtonEnabled.src;
        else
            imgAddButtonObj.src =   addButtonDisabled.src;
    }
	var imgFindButtonObj = dojo.byId("imgMainTreeFindButton");
    if(imgFindButtonObj != null) {
        if(mainTree_isFindPossible)
            imgFindButtonObj.src =   findButtonEnabled.src;
        else
            imgFindButtonObj.src =   findButtonDisabled.src;
    }

	
	var imgMoveButtonObj = dojo.byId("imgMainTreeMoveButton");
    if(imgMoveButtonObj != null) {
        if(mainTree_isMovePossible && getIsMoveRightPossible())
            imgMoveButtonObj.src =   moveRightButtonEnabled.src;
        else
            imgMoveButtonObj.src =   moveRightButtonDisabled.src;
    }
	
}

//function to refresh buttons pallete for Rearrange tree
function byRecord_refreshRearrangeTreeButtonsPallete () {
	
	//alert("add : " + mainTree_isAddPossible +"\n delete:"+ mainTree_isDeletePossible);

	var imgDeleteButtonObj = dojo.byId("imgRearrangeTreeDeleteButton");
    if(imgDeleteButtonObj != null) {
        if(rearrangeTree_isDeletePossible)
            imgDeleteButtonObj.src =   deleteButtonEnabled.src;
        else
            imgDeleteButtonObj.src =   deleteButtonDisabled.src;
    }
	
	var imgAddButtonObj = dojo.byId("imgRearrangeTreeAddButton");
    if(imgAddButtonObj != null) {
        if(rearrangeTree_isAddPossible)
            imgAddButtonObj.src =   addButtonEnabled.src;
        else
            imgAddButtonObj.src =   addButtonDisabled.src;
    }
	var imgFindButtonObj = dojo.byId("imgRearrangeTreeFindButton");
    if(imgFindButtonObj != null) {
        if(rearrangeTree_isFindPossible)
            imgFindButtonObj.src =   findButtonEnabled.src;
        else
            imgFindButtonObj.src =   findButtonDisabled.src;
    }

	
	var imgMoveButtonObj = dojo.byId("imgRearrangeTreeMoveButton");
    if(imgMoveButtonObj != null) {
        if(rearrangeTree_isMovePossible && getIsMoveLeftPossible())
            imgMoveButtonObj.src =   moveLeftButtonEnabled.src;
        else
            imgMoveButtonObj.src =   moveLeftButtonDisabled.src;
    }
	
}

// Reset the view. reset flags used for enabling/disabling buttons pallete, for both Main Tree & rearrange Tree
// clear details section (both main & rearrange details)
function byRecord_resetView () {
	mainTree_isAddPossible = false;
	mainTree_isDeletePossible = false;
	mainTree_isFindPossible = false;
	mainTree_isMovePossible = false;
	
	rearrangeTree_isAddPossible = false;
	rearrangeTree_isDeletePossible = false;
	rearrangeTree_isFindPossible = false;
	rearrangeTree_isMovePossible = false;
}