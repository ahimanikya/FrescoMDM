
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
	var selectedEUID = "000-000-555";
	if(byRecord_CurrentSelected_Domain != null) selectedDomain = byRecord_CurrentSelected_Domain;
	if(byRecord_CurrentSelected_EUID != null) selectedEUID = byRecord_CurrentSelected_EUID;

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
    rootRecordItem.type = item_types.RECORD;
 
    var rootDomain = mainTree_Store.newItem( rootDomainItem , null);
    var rootRecord = mainTree_Store.newItem(rootRecordItem, {parent: rootDomain, attribute:"children"} );

    // 1d. add relationships for the root record.
    for(i=0; i<data.relationshipsObjects.length; i++) {
        var tempRelObj = data.relationshipsObjects[i];
		var useSourceDomain = false;
		if(tempRelObj.relationshipDefView.sourceDomain != byRecord_CurrentSelected_Domain)
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
        relationshipDomain.underRelationship = relationshipNode.name;
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
			recordNode.underDomain = relationshipDomain.name;
			recordNode.underRelationship = relationshipNode.name;
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
	isAddButtonEnabled = false, isDeleteButtonEnabled = false;
	targetDomain = null, addToRelationship = null;
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
var isAddButtonEnabled = false;
var isDeleteButtonEnabled = false;
var targetDomain = null, addToRelationship = null;

// Main tree click event is captured by this function
function mainTreeClicked(item, node, allSelectedItems ) {
	//alert('allSelectedItems ' + allSelectedItems.length);
	isAddButtonEnabled = false, isDeleteButtonEnabled = false;
	targetDomain = null, addToRelationship = null;
	
	for(i=0; i<allSelectedItems.length; i++) {
		var tempItem = allSelectedItems[i];
		
		var itemType = mainTree_Store.getValue(tempItem, "type");
		var itemName = mainTree_Store.getValue(tempItem, "name");	
		//alert(itemType + " : " + itemName);
		
		switch (itemType) {
			case item_types.DOMAIN:
				if(itemName == byRecord_CurrentSelected_Domain) {
					continue;
				}
				//alert("its a domain ");
				break;
			case item_types.RELATIONSHIP:
				//alert("its a relationship");
				break;
			case item_types.RECORD:
				var itemEUID = mainTree_Store.getValue(item, "EUID");
				if(itemEUID == byRecord_CurrentSelected_EUID) 
					continue;
				//alert("its a record");
				break;
		}
	}
	
	return;
	
	var itemType = mainTree_Store.getValue(item, "type");
	var itemName = mainTree_Store.getValue(item, "name");

	if(itemType == item_types.DOMAIN) {
		if(itemName != byRecord_CurrentSelected_Domain) {
			isAddButtonEnabled = true;
			isDeleteButtonEnabled = true;
			targetDomain = itemName;
			addToRelationship = mainTree_Store.getValue(item, "underRelationship");
		}

	} else if(itemType == item_types.RELATIONSHIP) {
		isAddButtonEnabled = true;
		isDeleteButtonEnabled = true;
		targetDomain = mainTree_Store.getValue(item, "otherDomain");
		addToRelationship = itemName;

	} else  if(itemType == item_types.RECORD) {
		var itemEUID = mainTree_Store.getValue(item, "EUID");
		if(itemEUID != byRecord_CurrentSelected_EUID) {
			isAddButtonEnabled = true;
			targetDomain = itemName;
			
			targetDomain = mainTree_Store.getValue(item, "underDomain");
			addToRelationship = mainTree_Store.getValue(item, "underRelationship");
		}
		isDeleteButtonEnabled = true;
	}
}

function mainTreeNodeClosed(item, node) {
	isAddButtonEnabled = false, isDeleteButtonEnabled = false;
	targetDomain = null, addToRelationship = null;

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

function chkstatus() {
	var res = "Add allowed: " + isAddButtonEnabled;
	res += "\nDelete allowed: " + isDeleteButtonEnabled;
	res += "\n target domain: " + targetDomain ;
	res += "\n add to relationship def : " + addToRelationship;
	alert(res);
}


var isRearrangeTreeShown = false;
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