// initialize icons
var deleteButtonEnabled = new Image();
deleteButtonEnabled.src = "images/icons/delete_button.png";
var deleteButtonDisabled = new Image();
deleteButtonDisabled.src = "images/icons/delete_button_faded.png";

var selectAllButtonEnabled = new Image();
selectAllButtonEnabled.src = "images/icons/select_multiple.png";
var selectAllButtonDisabled = new Image();
selectAllButtonDisabled.src = "images/icons/select_multiple_faded.png";

var deselectAllButtonEnabled = new Image();
deselectAllButtonEnabled.src = "images/icons/deselect_multiple.png";
var deselectAllButtonDisabled = new Image();
deselectAllButtonDisabled.src = "images/icons/deselect_multiple_faded.png";   

var addButtonEnabled = new Image();
addButtonEnabled.src = "images/icons/add_button.png";
var addButtonDisabled = new Image();
addButtonDisabled.src = "images/icons/add_button_faded.png";   

var isByRelSelectDialogLoaded = false; // Global flag to find out if Select dialog is already loaded for ByRelationship screen
var isByRelAddDialogLoaded = false; // Global flag to find out if Add dialog is already loaded for ByRelationship screen

var isByRecordSelectDialogLoaded = false;
var isByRecordAddDialogLoaded = false;

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

// General functions for Manage Relationship Screens

// function to get HTML view of the relationship def name, along with direction icon.
function getRelationshipDefDirectionIcon (isBiDirection) {
    var strHTML = "";
    if(getBoolean (isBiDirection) ) {
        strHTML += "<img src='images/icons/relationship-both.png' hspace='3'>";
    } else {
        strHTML += "<img src='images/icons/relationship-right.png' hspace='3'>";
    }
    return strHTML;
}

// Manage screen related scripts

 function showRecordFullDetails(summaryId, detailsId, showDetailsFlag){
     if(! showDetailsFlag){
        displayDiv (detailsId, false);
        displayDiv (summaryId, true);
     }else{  
        displayDiv (detailsId, true);
        displayDiv (summaryId, false);        
     }
 }

var byRelationshipMainPage = "m_byrelationship_main.htm";
var byRecordMainPage = "m_byrecord_main.htm";
var currentView = "by_rel";
function changeViewToByRelationship(contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    _deleteChildWidgets(contentPaneId);
    contentPaneObj.setHref (byRelationshipMainPage);
    currentView = "by_rel";    
    initializeByRelationshipScreen ();
}
function changeViewToByRecord(contentPaneId) {
    var contentPaneObj = dijit.byId(contentPaneId);
    _deleteChildWidgets(contentPaneId);
    contentPaneObj.setHref (byRecordMainPage);
    currentView = "by_rec";
    initializeByRecordScreen ();
}
function initializeByRecordScreen() {  
    isByRecordSelectDialogLoaded = false;
    isByRecordAddDialogLoaded = false;
}
function initializeByRelationshipScreen() {
    currentSelectedSourceDomain = null; 
    currentSelectedTargetDomain = null; 
    currentSelectedRelationshipDef = null; 
    isByRelSelectDialogLoaded = false; 
    isByRelAddDialogLoaded = false; 
}
function _deleteChildWidgets( parentId) {
    var parentObj = dijit.byId(parentId);
    if(parentObj == null) return;
    var childWidgets = parentObj.getDescendants();
    for (i=0; i<childWidgets.length; i++) {
        childWidgets[i].destroyRecursive(false);
    }
}

function showSelectDialog() {
    if(currentView != null && currentView == "by_rel")
      showByRelSelectDialog ();
    else showByRecordSelectDialog();
}
function showByRelSelectDialog() {
    var selectDialog = dijit.byId("byrel_select");
    if(selectDialog != null)
      selectDialog.show();
  
    if(isByRelSelectDialogLoaded) loadDomainsForSearch();
}

function hideByRelSelectDialog () {
    dijit.byId('byrel_select').hide();
}

function showByRecordSelectDialog() {
    var selectDialog = dijit.byId("byrecord_select");
    if(selectDialog != null)
      selectDialog.show();
    if(isByRecordSelectDialogLoaded) loadDomainsForSelectRecord();
}
function hideByRecordSelectDialog () {
    dijit.byId('byrecord_select').hide();
}
function showByRecordAddDialog() {
    var addDialog = dijit.byId("byrecord_add");
    if(addDialog != null)
        addDialog.show();
    if(isByRecordAddDialogLoaded) initializeByRecordAddDialog();    
}
function initializeByRecordAddDialog() {
    isByRecordAddDialogLoaded = true;
    alert("By record add dialog Loaded...");
    byRecord_prepareAdd();
}

function showByRelAddDialog(){
    if(currentSelectedSourceDomain == null || currentSelectedTargetDomain==null || currentSelectedRelationshipDef==null)
        return;
    var addDialog = dijit.byId("byrel_add");
    addDialog.show();
    if(isByRelAddDialogLoaded) initializeAddDialog();
}

function initializeAddDialog() {
    isByRelAddDialogLoaded = true;
    //alert("add dialog ")
    var selectedSourceDomain = currentSelectedSourceDomain; //document.getElementById("select_sourceDomain").value;
    var selectedTargetDomain = currentSelectedTargetDomain; //document.getElementById("select_targetDomain").value;
    var selectedRelationshipDef = currentSelectedRelationshipDef; //document.getElementById("select_relationshipDefs").value;

    document.getElementById("byrel_addSourceDomain").innerHTML= selectedSourceDomain;
    document.getElementById("byrel_addTargetDomain").innerHTML= selectedTargetDomain;
    document.getElementById("relationship_add_RelationshipDefName").innerHTML= selectedRelationshipDef;
    
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
   // document.getElementById('relationship_add_customAttributes').innerHTML="";
   // document.getElementById('relationship_add_predefinedAttributes').innerHTML="";
    //dwr.util.removeAllRows("relationship_add_customAttributes");
    dwr.util.removeAllRows("relationship_add_predefinedAttributes");
       if(data.extendedAttributes.length>0 ){
           createCustomAttributesSection ("relationship_add_customAttributes", data.extendedAttributes, "add_custom", true,false);
           displayDiv ("add_Relationship_CustomAtrributes", true);
       }else{
           displayDiv ("add_Relationship_CustomAtrributes", false);
       }
       if(startDate==true || endDate==true || purgeDate == true ){ 
          createPredefinedAttributesSection ("relationship_add_predefinedAttributes", data, "add_predefined", true);
          displayDiv ("add_Relationship_PredefinedAtrributes", true);
      } else{
          displayDiv ("add_Relationship_PredefinedAtrributes", false);
      }    
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
function createCustomAttributesSection (tableId, attributesArray, prefixToUse, showRequiredSymbol, showByRange ) {
   // alert("CREATING custom attributes section for: " + tableId);
     var editCustomAttrFuncs = [
      function(data) { return data; },
      function(data) { return data; },
      function(data) { return data; },
      function(data) { return data; }
    ];
    if(showRequiredSymbol == null) showRequiredSymbol = true;
    if(showByRange == null) showByRange = false;
    // Destroy previously created widgets.
    if(attributesArray != null && attributesArray.length > 0) {
        for(i=0; i<attributesArray.length; i++) {            
            var fName =  prefixToUse + "_" + attributesArray[i].name;
            if(dijit.byId(fName) != undefined && dijit.byId(fName) != null ) {
               dijit.byId(fName).destroy();
            }
            var fName_To =  prefixToUse + "_" + attributesArray[i].name + "_TO";
            if(dijit.byId(fName_To) != undefined && dijit.byId(fName_To) != null ) {
                dijit.byId(fName_To).destroy();
            }
        }
    }
    
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
            var byRangeDiv = null;
            var byRangeCheckbox = null;
            var labelByRange = null;
            var field_To = null;
            if(showByRange) {
                byRangeDiv = document.createElement("span");
                byRangeDiv.id = prefixToUse + "_" + tempData.name + "_TO_DIV";
                byRangeDiv.style.display = "none";
                var labelTo = document.createElement ("label");
                labelTo.id = "label_TO"; labelTo.innerHTML ="&nbsp; to ";
                byRangeDiv.appendChild (labelTo);
                field_To = document.createElement("input");
                field_To.type ="text";
                field_To.name = prefixToUse + "_" + tempData.name + "_TO";
                field_To.id = prefixToUse + "_" + tempData.name + "_TO";
                byRangeDiv.appendChild (field_To);

                byRangeCheckbox = document.createElement("input");
                byRangeCheckbox.type = "checkbox";
                byRangeCheckbox.name = "showbyRangeChk";
                byRangeCheckbox.value = byRangeDiv.id;
                byRangeCheckbox.onclick = function () {
                    displayDiv(this.value, this.checked);
                }
                labelByRange = document.createElement ("label");
                labelByRange.innerHTML = "By Range";
            }
            if(options.cellNum == 1) {
                if(tempData.dataType=="date"){
                    var datefield = document.createElement("input");
                    // This should be date field...
                    datefield.type = "text";
                    datefield.name = prefixToUse + "_" + tempData.name;
                    datefield.id = prefixToUse + "_" + tempData.name;
                    datefield.size = 5; 
                    datefield.style.width = "100px";
                    td.appendChild(datefield);
                    var props = {
                          name: "custom_date_attr",
                          promptMessage: getMessageForI18N("date_Format"),
                          invalidMessage: getMessageForI18N("invalid_date"),
                          //constraints: "{selector:'date', datePattern:'mm/dd/yyyy'}",
                          width:"150px"
                    }; 
                    datefield = new dijit.form.DateTextBox(props, datefield);
                }else if(tempData.dataType == "boolean"){
                    var booleanField = document.createElement("select");
                    var tOption = document.createElement ("option");
                    tOption.text = "true"; 
                    tOption.value = "true";
                    var fOption = document.createElement ("option");
                    fOption.text = "false"; 
                    fOption.value = "false";
                    booleanField.options[0] = tOption;
                    booleanField.options[1] = fOption;
                    booleanField.name = prefixToUse + "_" + tempData.name;
                    booleanField.id = prefixToUse + "_" + tempData.name;
                    td.appendChild(booleanField);
               } else {
                    var field = document.createElement("input");
                    field.type ="text";
                    field.name = prefixToUse + "_" + tempData.name;
                    field.id = prefixToUse + "_" + tempData.name;
                    td.appendChild (field);
               }
               td.className = "label";
               options.data = null;
            } else if(options.cellNum == 0) {
                if(getBoolean(tempData.isRequired) == true && showRequiredSymbol){
                   options.data = "<span class='label'>" + tempData.name + getMessageForI18N("mandatorySymbol") +  "</span>"; 
                } else {
                   options.data = "<span class='label'>" + tempData.name +  "</span>";
                }
                td.innerHTML = options.data;
            } else if(options.cellNum == 2) {
                // create by Range field.
                if(showByRange) { 
                    if(tempData.dataType=="date"){
                      field_To.type = "text";
                      field_To.name = prefixToUse + "_" + tempData.name + "_TO";
                      field_To.id = prefixToUse + "_" + tempData.name + "_TO";
                      field_To.size = 5; 
                      field_To.style.width = "100px";
                      var dateProps = {
                          name: "custom_date_attr",
                          promptMessage: getMessageForI18N("date_Format"),
                          invalidMessage: getMessageForI18N("invalid_date"),
                          width:"150px"
                      }; 
                      field_To = new dijit.form.DateTextBox(dateProps, field_To);
                      td.appendChild(byRangeDiv);
                    } else if(tempData.dataType == "boolean"){
                    } else {
                      td.appendChild(byRangeDiv);  
                    }
                }
                options.data = null;
                td.className = "label";
            } else if(options.cellNum == 3) {
                if(showByRange) {                 
                  if(tempData.dataType == "boolean") {
                  } else {
                    td.appendChild(byRangeCheckbox);
                    td.appendChild(labelByRange);
                  }
                }
                options.data = null;
                td.className = "label";
            }
            else {}
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
function createPredefinedAttributesSection (tableId, dataObj, prefixToUse, showRequiredSymbol) {
    //alert("CREATING Predefined attributes section : " + tableId);
    if(showRequiredSymbol == null) showRequiredSymbol = true;
    var startDate =(getBoolean(dataObj.startDate));
    var endDate =(getBoolean(dataObj.endDate));
    var purgeDate =(getBoolean(dataObj.purgeDate));
    var startDateRequired = (getBoolean(dataObj.startDateRequired));
    var endDateRequired = (getBoolean(dataObj.endDateRequired));
    var purgeDateRequired = (getBoolean(dataObj.purgeDateRequired));
    
    //alert(startDate + "," + endDate +", " + purgeDate);

    // Destroy the datetextbox widgets if already registered
    if(dijit.byId(prefixToUse + "_startDate") != undefined && dijit.byId(prefixToUse + "_startDate") != null ) {
      dijit.byId(prefixToUse + "_startDate").destroy();
    }
    if(dijit.byId(prefixToUse + "_endDate") != undefined && dijit.byId(prefixToUse + "_endDate") != null ) {
      dijit.byId(prefixToUse + "_endDate").destroy();
    }
    if(dijit.byId(prefixToUse + "_purgeDate") != undefined && dijit.byId(prefixToUse + "_purgeDate") != null ) {
      dijit.byId(prefixToUse + "_purgeDate").destroy();
    }    

    dwr.util.removeAllRows(tableId);
    PredefinedrowCount = 0;
    if(startDate==true&& endDate==true){ // condition for Start and End dates of Predefined Attributes
        var SecondRow = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRow.insertCell(0);
        SecondRow.insertCell(1);
        SecondRow.insertCell(2);
        SecondRow.insertCell(3);
        SecondRow.insertCell(4);
        SecondRow.cells[0].innerHTML=getMessageForI18N("effective");
        if(startDateRequired == true && showRequiredSymbol){
          SecondRow.cells[1].innerHTML=getMessageForI18N("from") + " " + getMessageForI18N("mandatorySymbol");   
        }else{
          SecondRow.cells[1].innerHTML=getMessageForI18N("from");      
        }
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
            promptMessage: getMessageForI18N("date_Format"),
            invalidMessage: getMessageForI18N("invalid_date"),
            width:"100px"
        }
        startDate_textBox = new dijit.form.DateTextBox(startProps, startDate_textBox);
        if(endDateRequired == true && showRequiredSymbol){
          SecondRow.cells[3].innerHTML=getMessageForI18N("to") + " " + getMessageForI18N("mandatorySymbol");   
        }else{
          SecondRow.cells[3].innerHTML=getMessageForI18N("to");      
        }
        
        var endDate_textBox = document.createElement("input");
        endDate_textBox.type="text";
        endDate_textBox.name = prefixToUse + "_endDate";
        endDate_textBox.id = prefixToUse + "_endDate";
        endDate_textBox.size="5";
        endDate_textBox.style.width="100px";
        SecondRow.cells[4].appendChild(endDate_textBox);
        var endProps = {
            name: "end_date_textbox",
            promptMessage: getMessageForI18N("date_Format"),
            invalidMessage: getMessageForI18N("invalid_date"),
            width:"100px"
        }
        endDate_textBox = new dijit.form.DateTextBox(endProps, endDate_textBox); 
    } else if (startDate == true) { // condition for Start Date of Predefined Attributes
        var SecondRowStart = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRowStart.insertCell(0);
        SecondRowStart.insertCell(1);
        SecondRowStart.insertCell(2);
        
        SecondRowStart.cells[0].innerHTML=getMessageForI18N("effective");
        if(startDateRequired == true && showRequiredSymbol){
          SecondRowStart.cells[1].innerHTML=getMessageForI18N("from") + " " + getMessageForI18N("mandatorySymbol");   
        }else{
          SecondRowStart.cells[1].innerHTML=getMessageForI18N("from");      
        }
        var start_date = document.createElement("input");
        start_date.type="text";
        start_date.name = prefixToUse + "_startDate";
        start_date.id = prefixToUse + "_startDate";
        start_date.size="5";
        start_date.style.width="100px";
        SecondRowStart.cells[2].appendChild(start_date);
        var start_date_Props = {
            name: "start_date",
            promptMessage: getMessageForI18N("date_Format"),
            invalidMessage: getMessageForI18N("invalid_date"),
            width:"100px"
        }
        start_date = new dijit.form.DateTextBox(start_date_Props, start_date);
        
    } else if(endDate == true) { // condition for End Date of Predefined Attributes
        
        var SecondRowEnd = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        SecondRowEnd.insertCell(0);
        SecondRowEnd.insertCell(1);
        SecondRowEnd.insertCell(2);
        
        SecondRowEnd.cells[0].innerHTML=getMessageForI18N("effective");
        if(endDateRequired == true && showRequiredSymbol){
          SecondRowEnd.cells[1].innerHTML=getMessageForI18N("to") + " " + getMessageForI18N("mandatorySymbol");      
        }else{
          SecondRowEnd.cells[1].innerHTML=getMessageForI18N("to");      
        }
        var end_date = document.createElement("input");
        end_date.type="text";
        end_date.name = prefixToUse + "_endDate";
        end_date.id = prefixToUse + "_endDate";
        end_date.size="5";
        end_date.style.width="100px";
        SecondRowEnd.cells[2].appendChild(end_date);
        var end_date_Props = {
            name: "end_date",
            promptMessage: getMessageForI18N("date_Format"),
            invalidMessage: getMessageForI18N("invalid_date"),
            width:"100px"
        }
        end_date = new dijit.form.DateTextBox(end_date_Props, end_date);
        
    }
    if(purgeDate==true ){ // condition for purge date of Predefined Attributes
        
        var ThirdRow = document.getElementById(tableId).insertRow(PredefinedrowCount ++);
        ThirdRow.insertCell(0);
        ThirdRow.insertCell(1);
        ThirdRow.insertCell(2);
        if(purgeDateRequired == true && showRequiredSymbol){
          ThirdRow.cells[0].innerHTML=getMessageForI18N("purgeDate") + " " + getMessageForI18N("mandatorySymbol");
        }else{
          ThirdRow.cells[0].innerHTML=getMessageForI18N("purgeDate");
        }
        ThirdRow.cells[1].innerHTML=" ";
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
            promptMessage: getMessageForI18N("date_Format"),
            invalidMessage: getMessageForI18N("invalid_date"),
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



function selectAllRelationships () {
    var chkboxes = document.getElementsByName("relationship_id");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = true;
    }
    refreshRelationshipsListingButtonsPalette();
}
function deselectAllRelationships () {
    var chkboxes = document.getElementsByName("relationship_id");
    for(i=0;i<chkboxes.length; i++) {
        chkboxes[i].checked = false;
    }
    refreshRelationshipsListingButtonsPalette();
}

function refreshRelationshipsListingButtonsPalette () {
    var selectedChoices = 0;
    
    var chkboxes = document.getElementsByName("relationship_id");
    for(i=0;i<chkboxes.length; i++) {
        if(chkboxes[i].checked )
            selectedChoices ++;
    }

    var imgDeleteButtonObj = dojo.byId("imgDeleteRelationshipListing");
    if(imgDeleteButtonObj != null) {
        if(selectedChoices > 0)
            imgDeleteButtonObj.src =   deleteButtonEnabled.src;
        else
            imgDeleteButtonObj.src =   deleteButtonDisabled.src;
    }
    var imgSelectAllButtonObj = dojo.byId("imgSelectAllRelationshipListing");
    if(imgSelectAllButtonObj != null ) {
        if(selectedChoices != chkboxes.length)
            imgSelectAllButtonObj.src =   selectAllButtonEnabled.src;
        else
            imgSelectAllButtonObj.src =   selectAllButtonDisabled.src;
    }
    var imgDeSelectAllButtonObj = dojo.byId("imgDeselectAllRelationshipListing");
    if(imgDeSelectAllButtonObj != null ) {
        if(selectedChoices > 0)
            imgDeSelectAllButtonObj.src =   deselectAllButtonEnabled.src;
        else
            imgDeSelectAllButtonObj.src =   deselectAllButtonDisabled.src;
    }

    var imgAddButtonObj = dojo.byId("imgAddRelationshipListing");
    if(imgAddButtonObj != null) {
        if(currentSelectedSourceDomain != null && currentSelectedTargetDomain!=null && currentSelectedRelationshipDef!=null) {
            imgAddButtonObj.src =   addButtonEnabled.src;
        } else {
            imgAddButtonObj.src =   addButtonDisabled.src;
        }
    }
}
