
// Manage screen related scripts

 function showRecordFullDetails(summaryId, detailsId, showDetailsFlag){
     if(! showDetailsFlag){
        document.getElementById(detailsId).style.visibility='hidden';
        document.getElementById(detailsId).style.display='none';
        
        document.getElementById(summaryId).style.visibility='visible';
        document.getElementById(summaryId).style.display='block';
     }
     else{
        document.getElementById(detailsId).style.visibility='visible';
        document.getElementById(detailsId).style.display='block';
        
        document.getElementById(summaryId).style.visibility='hidden';
        document.getElementById(summaryId).style.display='none';         
     }
 }

var byRelationshipMainPage = "m_byrelationship_main.htm";
var byRecordMainPage = "m_byrecord_main.htm";
function changeViewToByRelationship(contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    contentPaneObj.destroyDescendants(false);
    contentPaneObj.setHref (byRelationshipMainPage);
    // Show the Select overlay dialog
    
}
function changeViewToByRecord(contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    contentPaneObj.destroyDescendants(false);
    contentPaneObj.setHref (byRecordMainPage);
}

function showByRelSelectDialog() {

    var selectDialog = dijit.byId("byrel_select");
    selectDialog.show();        

}
function hideByRelSelectDialog () {
    dijit.byId('byrel_select').hide();
}
function showByRelAddDialog(){
    
    var addDialog = dijit.byId("byrel_add");
    var selectedSourceDomain =document.getElementById("select_sourceDomain").value;
    var selectedTargetDomain =document.getElementById("select_targetDomain").value;
    var selectedRelationshipDef =document.getElementById("select_relationshipDefs").value;
    addDialog.show();
    alert("--sourceDomain--"+selectedSourceDomain);
    document.getElementById("byrel_addSourceDomain").innerHTML= selectedSourceDomain;
    document.getElementById("byrel_addTargetDomain").innerHTML= selectedTargetDomain;
    document.getElementById("byrel_addRelationshipDef").innerHTML= selectedRelationshipDef;
    
}
function hideByRelAddDialog () {
    dijit.byId('byrel_add').hide();
}


// Method to refresh record listing table in "By Relationship""
function selectRecordRow (objSelRow) {
    var tempTBody = document.getElementById("recordListing");
    for(i=0; i<tempTBody.rows.length; i++) {
        if(tempTBody.rows[i] == objSelRow) 
            objSelRow.style.backgroundColor='#EBF5FF'; // change to CSS class later
        else
            tempTBody.rows[i].style.backgroundColor='#FFFFFF'; // change to CSS class later
    }
    
}