dojo._hasResource["dijit.RecordDetailsTitlePane"]=true;
dojo.provide("dijit.RecordDetailsTitlePane");

var summaryOn = new Image();
summaryOn.src="images/icons/summary-button-down.gif";
var summaryOff=new Image();
summaryOff.src="images/icons/summary-button.gif";
var detailsOn=new Image();
detailsOn.src="images/icons/details-button-down.gif";
var detailsOff=new Image();
detailsOff.src="images/icons/details-button.gif";
           
dojo.declare("dijit.RecordDetailsTitlePane", dijit.TitlePane, {
    detailsShownFlag : false,
    
    templateString: '<div class="dijitTitlePane">' +
            '    <div class="RecordDetailsTitleBar" dojoAttachPoint="titleBarNode" style="cursor: default;height:22px;">' +
            '        <div dojoAttachEvent="onclick:toggle,onkeypress: _onTitleKey,onfocus:_handleFocus,onblur:_handleFocus" tabindex="0"' +
            '                waiRole="button" dojoAttachPoint="focusNode,arrowNode" class="dijitInline dijitArrowNode" style="cursor: pointer;float:left;"><span dojoAttachPoint="arrowNodeInner" class="dijitArrowNodeInner"></span></div>' +
            '        <img src="images/icons/document_icon.png" border="0" align="left" hspace="2"><span dojoAttachPoint="titleNode" class="dijitTitlePaneTextNode" style="float:left"></span>'+
            '       <span style="float:right;" class="dijitTitlePaneTextNode"><a href="javascript:void(0);"  dojoAttachEvent="onclick:toggleSummaryIcon"><img dojoAttachPoint="summaryIconNode" id="summaryIcon" src="images/icons/summary-button-down.gif" border="0" title="Summary" alt="Summary"></a>&nbsp;<a href="javascript:void(0);" dojoAttachEvent="onclick:toggleDetailsIcon"><img dojoAttachPoint="detailsIconNode" id="detailsIcon" src="images/icons/details-button.gif" border="0" title="Details" alt="Details" ></a></span>' +
            '    </div>' +
            '    <div class="dijitTitlePaneContentOuter" dojoAttachPoint="hideNode">' +
            '        <div class="dijitReset" dojoAttachPoint="wipeNode">' +
            '            <div class="dijitTitlePaneContentInner" dojoAttachPoint="containerNode" waiRole="region" tabindex="-1">' +
            '            </div>' +
            '        </div>' +
            '    </div>' +
            '</div>',

    toggleSummaryIcon : function () {
        this.detailsShownFlag = false;
        this.summaryIconNode.src = summaryOn.src;
        this.detailsIconNode.src = detailsOff.src;        
        this. onSummaryClick(); 
    },
    
    toggleDetailsIcon : function() {
        this.detailsShownFlag = true;
        this.summaryIconNode.src = summaryOff.src;
        this.detailsIconNode.src = detailsOn.src;
        this. onDetailsClick(); 
    },
    
    onSummaryClick : function() {
        //alert("default summary clicked ");
    },

    onDetailsClick : function() {
      //alert(" default details clicked ");
    }
    

       
    
});