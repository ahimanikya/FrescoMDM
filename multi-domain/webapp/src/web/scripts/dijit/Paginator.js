dojo.provide("dijit.Paginator");

dojo.require("dijit._Templated");
dojo.require("dijit.layout.ContentPane");

dojo.declare("dijit.Paginator",
	[dijit.layout.ContentPane, dijit._Templated], 
{

	
	templateString: '<div class="${baseClass}">' +
			'		<div dojoAttachPoint="containerNode" class="Paginator"><table cellspacing="0" border="0"><tr>' +
			'			<td valign="top"><span dojoAttachEvent="onclick:firstClick" title="Go to First Page">' +
                        '                         <img dojoAttachPoint="firstPageIconNode" src="${_blankGif}" alt="" class="firstPageIcon_Disabled">' +
			'			</span>' +
			'			<span dojoAttachEvent="onclick:prevClick">' +
			'                         <img dojoAttachPoint="prevPageIconNode" src="${_blankGif}" alt="" class="prevPageIcon">' +
			'			</span>' +
			'			</td><td valign="top" class="infoDisplay">'+
                        '                       <b>Page: </b><input type="text" class="pageNumberTextField" dojoAttachPoint="currentPageNode" size="2"> <b>of</b>' +
                        '                       <span dojoAttachPoint="totalPageDisplayNode" class="TotalPageInfo"></span><input dojoAttachEvent="onclick:goToPage" type="button" value="Go" class="pageGoToButton"> ' +
                        '                       </td><td valign="top">' +
			'			<span dojoAttachEvent="onclick:nextClick">' +
			'                         <img dojoAttachPoint="nextPageIconNode" src="${_blankGif}" alt="" class="nextPageIcon">' +
			'			</span>' +
			'			<span dojoAttachEvent="onclick:lastClick">' +
			'                         <img dojoAttachPoint="lastPageIconNode" src="${_blankGif}" alt="" class="lastPageIcon">' +
			'			</span> ' +
			'		</td></tr></table></div>' +
			'	</div>',

	pagingNode:null,
	currentPage: 1,
	totalPages: 0,

        goToPage: function () {
            var tempNum = this.currentPageNode.value;
            if (! isNaN(tempNum)) {
              this.currentPage = Math.floor(tempNum);
            } 
            this.refresh();
            this.navigationFunction(this.currentPage);
        },
	firstClick: function () {
                if(this.currentPage <= 1) return;
		this.currentPage = 1;
		this.refresh();
		this.navigationFunction(this.currentPage);
	},

	prevClick : function() {
                if(this.currentPage <= 1) return;
		this.currentPage = this.currentPage - 1;
		this.refresh();
		this.navigationFunction(this.currentPage);
	},
	nextClick : function() {
                if(this.currentPage >= this.totalPages) return;
		this.currentPage = this.currentPage + 1;
		this.refresh();
		this.navigationFunction(this.currentPage);
	},	
	lastClick: function () {
                if(this.currentPage >= this.totalPages) return;
		this.currentPage = this.totalPages;
		this.refresh();
		this.navigationFunction(this.currentPage);
	},

	navigationFunction: function (currPage) {
		// User function called. passes the currentPage
	},

	refresh: function () {
		if(this.currentPage <= 0) this.currentPage =1;
                if(this.currentPage > this.totalPages) this.currentPage = this.totalPages;
		this.totalPageDisplayNode.innerHTML = ""+ this.totalPages;
                this.currentPageNode.value = this.currentPage;

		
		if(this.currentPage <= 1) {
			this.prevPageIconNode.className = "prevPageIcon_Disabled";
			this.firstPageIconNode.className = "firstPageIcon_Disabled";
                        this.prevPageIconNode.title = "";
                        this.firstPageIconNode.title = "";
		} else {
			this.prevPageIconNode.className = "prevPageIcon";
			this.firstPageIconNode.className = "firstPageIcon";
                        this.prevPageIconNode.title = "Go to Previous Page";
                        this.firstPageIconNode.title = "Go to First Page";
		}

		if(this.currentPage >= this.totalPages) {
			this.nextPageIconNode.className = "nextPageIcon_Disabled";
			this.lastPageIconNode.className = "lastPageIcon_Disabled";
                        this.nextPageIconNode.title = "";
                        this.lastPageIconNode.title = "";
		} else {
			this.nextPageIconNode.className = "nextPageIcon";
			this.lastPageIconNode.className = "lastPageIcon";
                        this.nextPageIconNode.title = "Go to Next Page";
                        this.lastPageIconNode.title = "Go to Last Page";
		}
		
	},
	
	postCreate: function(){
		this.refresh();
		this.inherited(arguments);
	}

});