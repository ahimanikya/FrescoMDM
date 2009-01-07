dojo.provide("dijit.TreeCustom");

dojo.require("dijit.Tree");

dojo.declare("dijit.TreeCustom", dijit.Tree, {
	
	lastFocusedNode: null, // To store the currently focused node in the tree.

	allFocusedNodes: [], // array of currently focused nodes. eg., user holds control and chooses multiple nodes.

	ctrlKeyPressed: false,

	postCreate: function(){
		this.inherited(arguments);
		//this.connect(this.domNode, "onkeyup", this._onKeyUp);
		//this.connect(this.domNode, "onkeydown", this._onKeyDown);
		this.onClick = this.onClickDummy;
		this.allFocusedNodes = [];
		this.lastFocusedNode = null;
	},

	focusNode: function(/* _tree.Node */ node){
		this.inherited(arguments); 
		
		this.lastFocusedNode = node;
		if(this.ctrlKeyPressed) {
			//this.allFocusedNodes.push(node);
		} else {
			//alert("blurring " +this.allFocusedNodes.length);
			for(i=0;i<this.allFocusedNodes.length; i++) {
				this._customBlurNode(this.allFocusedNodes[i]);
			}
			this.allFocusedNodes = [];
		}
		var isExists = false; // Flag to find out if this node already been selected
		for(i=0;i<this.allFocusedNodes.length; i++) {
			var temp = this.allFocusedNodes[i];
			if(temp.item.id == node.item.id) isExists = true;
		}
		if( ! isExists)
			this.allFocusedNodes.push(node);
		this.customOnClick (node.item, node, this.getSelectedItems() );
		this.ctrlKeyPressed = false;
	},

	blurNode: function(){
		// Not using, we've our own custom made blur method. See _customBlurNode
	},
	
	_onBlur:function () {
	},

	_onUpArrow: function(/*Object*/ message){
		this.inherited(arguments); 
		this._onEnterKey({ node: this.lastFocusedNode, item: this.lastFocusedNode.item } );
	},

	_onDownArrow: function(/*Object*/ message){
		this.inherited(arguments); 
		this._onEnterKey({ node: this.lastFocusedNode, item: this.lastFocusedNode.item } );
	},

	_onLeftArrow: function(/*Object*/ message){
		this.inherited(arguments); 
		this._onEnterKey({ node: this.lastFocusedNode, item: this.lastFocusedNode.item } );
	},
	
	_onRightArrow: function(/*Object*/ message){
		this.inherited(arguments); 
		this._onEnterKey({ node: this.lastFocusedNode, item: this.lastFocusedNode.item } );
	},

	_onClick: function(/*Event*/ e){
		if(e.ctrlKey) {
			this.ctrlKeyPressed = true;
		} else {
			this.ctrlKeyPressed = false;
		}
		this.inherited(arguments); 
		dojo.stopEvent(e);
	},

	_customBlurNode: function(node) {
		var labelNode = node.labelNode;
		dojo.removeClass(labelNode, "dijitTreeLabelFocused");
		labelNode.setAttribute("tabIndex", "-1");
		dijit.setWaiState(labelNode, "selected", false);
	},
	
	// Returns array of currently selected items.
	getSelectedItems: function() {
		var selectedItems = [];
		for(i=0;i<this.allFocusedNodes.length; i++) {
			var iNode = this.allFocusedNodes[i];
			selectedItems.push(iNode.item);
		}
		return selectedItems ;
	},


	onClickDummy: function(item, node) {
	},

	customOnClick: function () {
		alert("in custom on click");
	},

	_expandNode: function (node) {
		if(node.state == "UNCHECKED") {
			this.lazyLoadItems(node , function() {
				node.unmarkProcessing();
			});
		}
		this.inherited(arguments); 
	},
		
	lazyLoadItems: function(node, callback_function) {
		callback_function();
	}

});