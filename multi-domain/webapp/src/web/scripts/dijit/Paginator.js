dojo.provide("dijit.Paginator");

dojo.require("dijit._Templated");
dojo.require("dijit.layout.ContentPane");

dojo.declare("dijit.Paginator",
	[dijit.layout.ContentPane, dijit._Templated], 
{

	
	templateString: '<div class="${baseClass}">' +
			'		<div dojoAttachPoint="containerNode" class="Paginator"><table cellspacing="0" border="0"><tr>' +
			'			<td valign="top"><span dojoAttachEvent="onclick:firstClick" title="Go to First Page" vspace="2">' +
                        '                         <img dojoAttachPoint="firstPageIconNode" src="${_blankGif}" alt="" class="firstPageIcon_Disabled" vspace="2">' +
			'			</span>' +
			'			<span dojoAttachEvent="onclick:prevClick">' +
			'                         <img dojoAttachPoint="prevPageIconNode" src="${_blankGif}" alt="" class="prevPageIcon_Disabled" vspace="2">' +
			'			</span>' +
			'			</td><td valign="top" class="infoDisplay">'+
                        '                       <b>Page: </b><input type="text" class="pageNumberTextField" dojoAttachPoint="currentPageNode" size="2"> <b>of</b>' +
                        '                       <span dojoAttachPoint="totalPageDisplayNode" class="TotalPageInfo"></span><input dojoAttachEvent="onclick:goToPage" type="button" value="Go" class="pageGoToButton"> ' +
                        '                       </td><td valign="top">' +
			'			<span dojoAttachEvent="onclick:nextClick">' +
			'                         <img dojoAttachPoint="nextPageIconNode" src="${_blankGif}" alt="" class="nextPageIcon" vspace="2">' +
			'			</span>' +
			'			<span dojoAttachEvent="onclick:lastClick">' +
			'                         <img dojoAttachPoint="lastPageIconNode" src="${_blankGif}" alt="" class="lastPageIcon" vspace="2">' +
			'			</span> ' +
			'		</td></tr></table></div>' +
			'	</div>',

	pagingNode:null,
	currentPage: 1,
	totalPages: 0,
        
        firstPageTooltip: "Go to First Page",
        previousPageTooltip: "Go to Previous Page",
        nextPageTooltip: "Go to Next Page",
        lastPageTooltip: "Go to Last Page",
        
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
		} else {
			this.prevPageIconNode.className = "prevPageIcon";
			this.firstPageIconNode.className = "firstPageIcon";
		}

		if(this.currentPage >= this.totalPages) {
			this.nextPageIconNode.className = "nextPageIcon_Disabled";
			this.lastPageIconNode.className = "lastPageIcon_Disabled";
		} else {
			this.nextPageIconNode.className = "nextPageIcon";
			this.lastPageIconNode.className = "lastPageIcon";
		}
		
	},
	
	postCreate: function(){
            this.refresh();
                
            this.firstPageIconNode.title = this.firstPageTooltip;
            this.prevPageIconNode.title = this.previousPageTooltip;
            this.nextPageIconNode.title = this.nextPageTooltip;
            this.lastPageIconNode.title = this.lastPageTooltip;
            this.inherited(arguments);
	}

});