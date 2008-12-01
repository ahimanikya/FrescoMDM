
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
    
    RelationshipDefHandler.getRelationshipDefByName(selectedRelationshipDef, selectedSourceDomain, selectedTargetDomain, populateAddRelationshipDefAttributes);
    
}

function populateAddRelationshipDefAttributes(data){
    var startDate =(getBoolean(data.startDate));
    var endDate =(getBoolean(data.endDate));
    var purgeDate =(getBoolean(data.purgeDate));
    var rowCount = 0;
    
    
    if(startDate==true || endDate==true || startDate == true ){ // condition for Predefined Attributes
        var FirstRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(rowCount ++);
        FirstRow.insertCell(0);
        FirstRow.cells[0].colSpan='4';
        FirstRow.cells[0].innerHTML="Predefined Attributes";
    }
    
    if(startDate==true&& endDate==true){ // condition for Start and End dates
        var SecondRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(rowCount ++);
        SecondRow.insertCell(0);
        SecondRow.insertCell(1);
        SecondRow.insertCell(2);
        SecondRow.insertCell(3);
        SecondRow.insertCell(4);
        SecondRow.cells[0].innerHTML="Effective";
        SecondRow.cells[1].innerHTML="From";
        var startDate_textBox = document.createElement("input");
        startDate_textBox.type="text";
        startDate_textBox.size="5";
        startDate_textBox.style.width="100px";
        SecondRow.cells[2].appendChild(startDate_textBox);
        var startProps = {
            name: "start_date_textbox",
            promptMessage: "mm/dd/yyyy",
            invalidMessage: "Invalid date.",
            width:"100px"
        }
        startDate_textBox = new dijit.form.DateTextBox(startProps, startDate_textBox);
        
        SecondRow.cells[3].innerHTML="To";
        var endDate_textBox = document.createElement("input");
        endDate_textBox.type="text";
        endDate_textBox.size="5";
        endDate_textBox.style.width="100px";
        SecondRow.cells[4].appendChild(endDate_textBox);
        var endProps = {
            name: "end_date_textbox",
            promptMessage: "mm/dd/yyyy",
            invalidMessage: "Invalid date.",
            width:"100px"
        }
        endDate_textBox = new dijit.form.DateTextBox(endProps, endDate_textBox);
        
    } else if (startDate == true) { // condition for Start Date
        var SecondRowStart = document.getElementById('byrel_add_predefinedAttributes').insertRow(rowCount ++);
        SecondRowStart.insertCell(0);
        SecondRowStart.insertCell(1);
        SecondRowStart.insertCell(2);
        
        SecondRowStart.cells[0].innerHTML="Effective";
        SecondRowStart.cells[1].innerHTML="From";
        var start_date = document.createElement("input");
        start_date.type="text";
        start_date.size="5";
        start_date.style.width="100px";
        SecondRowStart.cells[2].appendChild(start_date);
        var start_date_Props = {
            name: "start_date",
            promptMessage: "mm/dd/yyyy",
            invalidMessage: "Invalid date.",
            width:"100px"
        }
        start_date = new dijit.form.DateTextBox(start_date_Props, start_date);
        
    } else if(endDate == true) { // condition for End Date
        
        var SecondRowEnd = document.getElementById('byrel_add_predefinedAttributes').insertRow(rowCount ++);
        SecondRowEnd.insertCell(0);
        SecondRowEnd.insertCell(1);
        SecondRowEnd.insertCell(2);
        
        SecondRowEnd.cells[0].innerHTML="Effective";
        SecondRowEnd.cells[1].innerHTML="To";
        var end_date = document.createElement("input");
        end_date.type="text";
        end_date.size="5";
        end_date.style.width="100px";
        SecondRowEnd.cells[2].appendChild(end_date);
        var end_date_Props = {
            name: "end_date",
            promptMessage: "mm/dd/yyyy",
            invalidMessage: "Invalid date.",
            width:"100px"
        }
        end_date = new dijit.form.DateTextBox(end_date_Props, end_date);
        
    }
    if(purgeDate==true ){ // condition for purge date
        
        var ThirdRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(rowCount ++);
        ThirdRow.insertCell(0);
        ThirdRow.insertCell(1);
        ThirdRow.insertCell(2);
        ThirdRow.cells[0].innerHTML=" ";
        ThirdRow.cells[1].innerHTML="Purge Date";
        var Purge_date= document.createElement("input");
        Purge_date.type="text";
        Purge_date.size="5";
        Purge_date.style.width="100px";
        ThirdRow.cells[2].appendChild(Purge_date);
        var purge_date_Props = {
            name: "purge_date",
            promptMessage: "mm/dd/yyyy",
            invalidMessage: "Invalid date.",
            width:"100px"
        }
        Purge_date = new dijit.form.DateTextBox(purge_date_Props, Purge_date);
    }
    
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