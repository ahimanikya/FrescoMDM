function testResults(){
    
    DomainScreenHandler.getSearchResultFields("Company",testResultsBack);
}

function testResultsBack(data){
    alert(data);
    for (var fieldGrp in data)  {
        for (var fields in data[fieldGrp])  {
          alert("Search Results"+fieldGrp + " : "+data[fieldGrp][fields].name);
        }
     }
    
}

function test() {
    DomainScreenHandler.getSummaryFields("Person",testCB);
}
function testCB(data) {
    alert(data);
     for (var fieldGrp in data)  {
        for (var fields in data[fieldGrp])  {
          alert(fieldGrp + " : "+data[fieldGrp][fields].name);
        }
     }
}
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
    _deleteChildWidgets(contentPaneId);
    contentPaneObj.setHref (byRelationshipMainPage);
    
}
function changeViewToByRecord(contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    _deleteChildWidgets(contentPaneId);
    contentPaneObj.setHref (byRecordMainPage);
}
function _deleteChildWidgets( contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    var childWidgets = contentPaneObj.getDescendants();
    for (i=0; i<childWidgets.length; i++) {
        childWidgets[i].destroyRecursive(false);
    }
}

function showSelectDialog() {
    var selectDialog = dijit.byId("byrel_select");
    if(selectDialog != null)
      selectDialog.show();
}
//function showByRelSelectDialog() {
//    var selectDialog = dijit.byId("byrel_select");
//    selectDialog.show();
//}

function hideByRelSelectDialog () {
    dijit.byId('byrel_select').hide();
}
function showByRelAddDialog(){
    var addDialog = dijit.byId("byrel_add");
    addDialog.show();
    //initializeAddDialog();
}

function initializeAddDialog() {
    //alert("add dialog ")
    var selectedSourceDomain =document.getElementById("select_sourceDomain").value;
    var selectedTargetDomain =document.getElementById("select_targetDomain").value;
    var selectedRelationshipDef =document.getElementById("select_relationshipDefs").value;

    document.getElementById("byrel_addSourceDomain").innerHTML= selectedSourceDomain;
    document.getElementById("byrel_addTargetDomain").innerHTML= selectedTargetDomain;
    document.getElementById("byrel_addRelationshipDef").innerHTML= selectedRelationshipDef;
    
    RelationshipDefHandler.getRelationshipDefByName(selectedRelationshipDef, selectedSourceDomain, selectedTargetDomain, populateAddRelationshipDefAttributes);
    // Populate search type drop down 
    loadAddSearchCriteria();
    //RelationshipDefHandler.getDomainSearchCriteria(selectedSourceDomain, addSourceDomainCriteria);
    //RelationshipDefHandler.getDomainSearchCriteria(selectedTargetDomain, addTargetDomainCriteria);
}

function populateAddRelationshipDefAttributes(data){
    var startDate =(getBoolean(data.startDate));
    var endDate =(getBoolean(data.endDate));
    var purgeDate =(getBoolean(data.purgeDate));
    var CustomrowCount = 0;
    var PredefinedrowCount = 0;  
   // document.getElementById('byrel_add_customAttributes').innerHTML="";
   // document.getElementById('byrel_add_predefinedAttributes').innerHTML="";
    //dwr.util.removeAllRows("byrel_add_customAttributes");
    dwr.util.removeAllRows("byrel_add_predefinedAttributes");
       if(data.extendedAttributes.length>0 ){
           createCustomAttributesSection ("byrel_add_customAttributes", data.extendedAttributes, "add_custom");
           /*
           var customHeading = document.getElementById('byrel_add_customAttributes').insertRow(CustomrowCount ++);
           customHeading.insertCell(0);
           customHeading.cells[0].innerHTML="Custom Attributes";
           var j=0;
           for(var i=0;i < data.extendedAttributes.length;i++){
             var customContent = document.getElementById('byrel_add_customAttributes').insertRow(CustomrowCount ++);
             customContent.insertCell(0);
             customContent.insertCell(1);
             customContent.cells[0].innerHTML=data.extendedAttributes[i].name;
             var field = document.createElement("input");
             field.type="text";
             field.id = "add_" + data.extendedAttributes[i].name;
             field.name= "add_" + data.extendedAttributes[i].name;
             field.size="5";
             field.style.width="100px";
             customContent.cells[1].appendChild(field);
           }
        */
           document.getElementById("add_Relationship_CustomAtrributes").style.visibility="visible"
           document.getElementById("add_Relationship_CustomAtrributes").style.display="";
       }
       else{
           
           document.getElementById("add_Relationship_CustomAtrributes").style.visibility="hidden"
           document.getElementById("add_Relationship_CustomAtrributes").style.display="none";
       }
       if(startDate==true || endDate==true || purgeDate == true ){ 
          createPredefinedAttributesSection ("byrel_add_predefinedAttributes", data, "add_predefined");
          document.getElementById("add_Relationship_PredefinedAtrributes").style.visibility="visible"
          document.getElementById("add_Relationship_PredefinedAtrributes").style.display="";
      } else{
          document.getElementById("add_Relationship_PredefinedAtrributes").style.visibility="hidden"
          document.getElementById("add_Relationship_PredefinedAtrributes").style.display="none";
      }
    /*
    if(startDate==true || endDate==true || startDate == true ){ // condition for Predefined Attributes
        var FirstRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(PredefinedrowCount ++);
        FirstRow.insertCell(0);
        FirstRow.cells[0].colSpan='4';
        FirstRow.cells[0].innerHTML="Predefined Attributes";
    }
    else{
        document.getElementById("add_Relationship_PredefinedAtrributes").style.visibility="hidden"
        document.getElementById("add_Relationship_PredefinedAtrributes").style.display="none";
    }
    
    if(startDate==true&& endDate==true){ // condition for Start and End dates of Predefined Attributes
        var SecondRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(PredefinedrowCount ++);
        SecondRow.insertCell(0);
        SecondRow.insertCell(1);
        SecondRow.insertCell(2);
        SecondRow.insertCell(3);
        SecondRow.insertCell(4);
        SecondRow.cells[0].innerHTML="Effective";
        SecondRow.cells[1].innerHTML="From";
        var startDate_textBox = document.createElement("input");
        startDate_textBox.type = "text";
        startDate_textBox.name = "startDate";
        startDate_textBox.id = "predefinedStartDate";
        startDate_textBox.size = "5";
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
        endDate_textBox.name = "endDate";
        endDate_textBox.id = "predefinedEndDate";
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
        
    } else if (startDate == true) { // condition for Start Date of Predefined Attributes
        var SecondRowStart = document.getElementById('byrel_add_predefinedAttributes').insertRow(PredefinedrowCount ++);
        SecondRowStart.insertCell(0);
        SecondRowStart.insertCell(1);
        SecondRowStart.insertCell(2);
        
        SecondRowStart.cells[0].innerHTML="Effective";
        SecondRowStart.cells[1].innerHTML="From";
        var start_date = document.createElement("input");
        start_date.type="text";
        start_date.name = "startDate";
        start_date.id = "predefinedStartDate";
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
        
    } else if(endDate == true) { // condition for End Date of Predefined Attributes
        
        var SecondRowEnd = document.getElementById('byrel_add_predefinedAttributes').insertRow(PredefinedrowCount ++);
        SecondRowEnd.insertCell(0);
        SecondRowEnd.insertCell(1);
        SecondRowEnd.insertCell(2);
        
        SecondRowEnd.cells[0].innerHTML="Effective";
        SecondRowEnd.cells[1].innerHTML="To";
        var end_date = document.createElement("input");
        end_date.type="text";
        end_date.name = "endDate";
        end_date.id = "predefinedEndDate";
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
    if(purgeDate==true ){ // condition for purge date of Predefined Attributes
        
        var ThirdRow = document.getElementById('byrel_add_predefinedAttributes').insertRow(PredefinedrowCount ++);
        ThirdRow.insertCell(0);
        ThirdRow.insertCell(1);
        ThirdRow.insertCell(2);
        ThirdRow.cells[0].innerHTML=" ";
        ThirdRow.cells[1].innerHTML="Purge Date";
        var Purge_date= document.createElement("input");
        Purge_date.type="text";
        Purge_date.name = "purgeDate";
        Purge_date.id = "predefinedPurgeDate";
        Purge_date.value = "true";
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
    */
    
}
function hideByRelAddDialog () {
    dijit.byId('byrel_add').hide();
}


// Method to refresh record listing table in "By Relationship""
function selectRecordRow (objSelRow) {
    // highlight the selected row
    var tempTBody = document.getElementById("relationshipsListing");
    for(i=0; i<tempTBody.rows.length; i++) {
        if(tempTBody.rows[i] == objSelRow) 
            objSelRow.style.backgroundColor='#EBF5FF'; // change to CSS class later
        else
            tempTBody.rows[i].style.backgroundColor='#FFFFFF'; // change to CSS class later
    }
}

// Function to create the UI for custom attributes section.
// Can be used for Add, select & edit relationship attributes screens   
function createCustomAttributesSection (tableId, attributesArray, prefixToUse) {
   // alert("CREATING custom attributes section for: " + tableId);
     var editCustomAttrFuncs = [
      function(data) { return data; },
      function(data) { return data; }
    ];
    dwr.util.removeAllRows(tableId);
    if(attributesArray != null && attributesArray.length > 0) {
      dwr.util.addRows(tableId, attributesArray, editCustomAttrFuncs, {
          rowCreator:function(options) {
            var row = document.createElement("tr");
            return row;
          },
          cellCreator:function(options) {
            var td = document.createElement("td");
            var tempData = options.data;
            if(options.cellNum == 1) {
                var field = document.createElement("input");
                field.type ="text";
                field.name = prefixToUse + "_" + tempData.name;
                field.id = prefixToUse + "_" + tempData.name;
                td.appendChild (field);
                /*var date_props = {
                  name: "date_field",
                  promptMessage: "mm/dd/yyyy",
                  invalidMessage: "Invalid date.",
                  width:"100px"
                }
                date_field = new dijit.form.DateTextBox(date_props, field);
                */
                options.data = null;
            } else {
                options.data = "<span class='label'>" + tempData.name +  "</span>";
            }            
            return td;
          },
          escapeHtml:false
        });
    } else {
    }
}

// Function to populate data in custom attributes section, used in Edit relationship attributes section.
function populateCustomAttributesValues (attributesArray, attributesValuesArray, prefixToUse) {
  //alert("POPULATING data for custom attributes");
  for(i=0; i<attributesValuesArray.length; i++) {
      //alert(attributesValuesArray[i].name + " : " + attributesValuesArray[i].value);
      var fieldObj = document.getElementById(prefixToUse + "_" + attributesValuesArray[i].name );
      if(fieldObj != null) {
          fieldObj.value = attributesValuesArray[i].value;
      }
  }
}
// Function to create Predefined attributes section
// Used in Add, Select & Edit Relationship Attributes screens
function createPredefinedAttributesSection (tableId, dataObj, prefixToUse) {
    //alert("CREATING Predefined attributes section : " + tableId);
    var startDate =(getBoolean(dataObj.startDate));
    var endDate =(getBoolean(dataObj.endDate));
    var purgeDate =(getBoolean(dataObj.purgeDate));
    
    //alert(startDate + "," + endDate +", " + purgeDate);
    dwr.util.removeAllRows(tableId);
    PredefinedrowCount = 0;
    var temp = document.getElementById(prefixToUse + "_startDate");
    var temp2 = dijit.byId(prefixToUse + "_startDate");
    alert(temp2);
    //alert(prefixToUse + "_startDate : " + temp);
    if(startDate==true&& endDate==true){ // condition for Start and End dates of Predefined Attributes
        var SecondRow = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRow.insertCell(0);
        SecondRow.insertCell(1);
        SecondRow.insertCell(2);
        SecondRow.insertCell(3);
        SecondRow.insertCell(4);
        SecondRow.cells[0].innerHTML="Effective";
        SecondRow.cells[1].innerHTML="From";
        var startDate_textBox = document.createElement("input");
        startDate_textBox.type = "text";
        startDate_textBox.name = prefixToUse + "_startDate";
        startDate_textBox.id = prefixToUse + "_startDate";
        startDate_textBox.size = "5";
        startDate_textBox.style.width="100px";
        SecondRow.cells[2].appendChild(startDate_textBox);
        //alert("fine till here");
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
        endDate_textBox.name = prefixToUse + "_endDate";
        endDate_textBox.id = prefixToUse + "_endDate";
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
        
    } else if (startDate == true) { // condition for Start Date of Predefined Attributes
        var SecondRowStart = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRowStart.insertCell(0);
        SecondRowStart.insertCell(1);
        SecondRowStart.insertCell(2);
        
        SecondRowStart.cells[0].innerHTML="Effective";
        SecondRowStart.cells[1].innerHTML="From";
        var start_date = document.createElement("input");
        start_date.type="text";
        start_date.name = prefixToUse + "_startDate";
        start_date.id = prefixToUse + "_startDate";
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
        
    } else if(endDate == true) { // condition for End Date of Predefined Attributes
        
        var SecondRowEnd = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRowEnd.insertCell(0);
        SecondRowEnd.insertCell(1);
        SecondRowEnd.insertCell(2);
        
        SecondRowEnd.cells[0].innerHTML="Effective";
        SecondRowEnd.cells[1].innerHTML="To";
        var end_date = document.createElement("input");
        end_date.type="text";
        end_date.name = prefixToUse + "_endDate";
        end_date.id = prefixToUse + "_endDate";
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
    if(purgeDate==true ){ // condition for purge date of Predefined Attributes
        
        var ThirdRow = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        ThirdRow.insertCell(0);
        ThirdRow.insertCell(1);
        ThirdRow.insertCell(2);
        ThirdRow.cells[1].innerHTML=" ";
        ThirdRow.cells[0].innerHTML="Purge Date";
        var Purge_date= document.createElement("input");
        Purge_date.type="text";
        Purge_date.name = prefixToUse + "_purgeDate";
        Purge_date.id = prefixToUse + "_purgeDate";
        Purge_date.value = "true";
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


function populatePredefinedAttributesValues(dataObj, dataObjValues, prefixToUse) {
    if(getBoolean(dataObj.startDate)) {
      var startDateObj = document.getElementById(prefixToUse + "_startDate" );
      if(startDateObj != null) {
          startDateObj.value = dataObjValues.startDate;
      }
    }
    if(getBoolean(dataObj.endDate)) {
      var endDateObj = document.getElementById(prefixToUse + "_endDate" );
      if(endDateObj != null) {
          endDateObj.value = dataObjValues.endDate;
      }
    }
    if(getBoolean(dataObj.purgeDate)) {
      var purgeDateObj = document.getElementById(prefixToUse + "_purgeDate" );
      if(purgeDateObj!=null) {
          purgeDateObj.value = dataObjValues.purgeDate;
      }
    }    
}