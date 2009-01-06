
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
    
    RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, getByRecordDataCB);
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

        relationshipNode.type = item_types.RELATIONSHIP;
        var rNodeItem = mainTree_Store.newItem(relationshipNode, {parent: rootRecord, attribute:"children"} );

        var relationshipDomain = {};
        relationshipDomain.id = i + "_" + relationshipNode.id + "_" + tempRelObj.relationshipDefView.sourceDomain;
		if(useSourceDomain)
			relationshipDomain.name = tempRelObj.relationshipDefView.sourceDomain;
		else
			relationshipDomain.name = tempRelObj.relationshipDefView.targetDomain;
        relationshipDomain.parentRelationshipDefName = relationshipNode.name;
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
			recordNode.parentRelationshipId = relationships[j].id;
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
        getIconClass: mainTreeGetIconClass,
		dndController:"dijit._tree.dndSource",
		checkAcceptance: mainTreeDnDCheckAcceptance
    }, document.createElement("div"));
    mainTreeObj.startup();
    dojo.byId("mainTreeContainer").appendChild(mainTreeObj.domNode);
}
function mainTreeDnDCheckAcceptance(source, nodes)  {
	//Dont accept if the source is the same tree
	if(this.tree == source.tree) return false;	
	else return true;
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
	mainTree_isMovePossible = true;
	
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
				//alert("its a domain ");
				break;
			case item_types.RELATIONSHIP:
				mainTree_isAddPossible = false;
				//alert("its a relationship");
				break;
			case item_types.RECORD:
				var itemEUID = mainTree_Store.getValue(tempItem, "EUID");
				var tempDomain = mainTree_Store.getValue(tempItem, "parentDomain");
				//alert(itemEUID + " : " + byRecord_CurrentWorking_EUID);
				
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
		mainTree_isFindPossible = true;
	}
	// If all selected items are records & from same domain, then enable add,find button.s
	if(recFromSameDomainCount == allSelectedItems.length) {
		mainTree_isAddPossible = true;
		mainTree_isFindPossible = true;
	}
	
	// If root nodes (either root-domain or root-record) are selected, disable add, move, find buttons
	if(isRootRecordSelected || isRootDomainSelected) {
		mainTree_isAddPossible = false;
		mainTree_isMovePossible = false;
		mainTree_isFindPossible = false;
	}

	byRecord_refreshMainTreeButtonsPallete();
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
	byRecord_refreshMainTreeButtonsPallete();
}

// Function to get the data for rearrange tree.
function getRearrangeTreeData () {
	var selectedDomain = "Person";
	var selectedEUID = "000-000-555";
	var domainSearchObj = {name:selectedDomain};
	if(byRecord_CurrentWorking_Domain != null) selectedDomain = byRecord_CurrentWorking_Domain;
	if(byRecord_CurrentWorking_EUID != null) selectedEUID = byRecord_CurrentWorking_EUID;
	
	RelationshipHandler.getEnterprises(domainSearchObj, getRearrangeTreeData_CB);
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
    rootDomainItem.name = byRecord_CurrentWorking_Domain;
    rootDomainItem.type = item_types.DOMAIN;
	
	var rootDomain = rearrangeTree_Store.newItem( rootDomainItem , null);
	
	var domainRecords = data;
	
	for(i=0; i<domainRecords.length; i++) {
		var tempRec = domainRecords[i];
		
		var recordNode = {};
        recordNode.id = tempRec.EUID;
        recordNode.name = tempRec.highLight;

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


// function to find out if move Right is possible (From Main tree to rearrange tree)
function getIsMoveRightPossible() {
	if(! isRearrangeTreeShown) return false;

	
	// TODO: return proper value based on node selections in both trees.
	
	return true;
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
