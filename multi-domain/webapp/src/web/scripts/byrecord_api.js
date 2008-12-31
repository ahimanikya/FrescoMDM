
/*
 * API integration scripts for Manage - By Record Screens.
 */ 

dwr.engine.setErrorHandler(exceptionHandler);
var byRecordSelectSearchResultsLength = null; // Holds the select record search results length
var byRecordAddSearchResultsLength = null; // Holds the add record search results length

function exceptionHandler(message) {
    alert(getMessageForI18N("exception")+ " " + message);
}


var byRecord_CurrentWorking_Domain = null; // Current working domain
var byRecord_CurrentWorking_EUID = null; // Current working record EUID.

var byRecord_CurrentSelected_RelationshipDefName = null; // Populated when any operation happens in Tree
var byRecord_CurrentSelected_TargetDomain = null; // Populated when any operation happens in Tree

var byRecord_Selected_Relationship = null; //Relationship Object - Populated when anything clicked in tree
var byRecord_Selected_Record = null; // Record Object - Populated when anything clicked in tree

var byRecord_CachedRelationshipDefs = {};

/*
 * Scripts for Select Record screen <START>
 */ 

function loadDomainsForSelectRecord() {
    isByRecordSelectDialogLoaded = true;
    DomainHandler.getDomains(updateByRecordSelectDomains);
}
function updateByRecordSelectDomains(data){
    dwr.util.removeAllOptions("byRecord_SelectDomain");
    dwr.util.addOptions("byRecord_SelectDomain", data, "name");
    dwr.util.setValue("byRecord_SelectDomain", data[0].name); 
    
    loadByRecordSelectSearchTypes("byRecord_SelectDomain");
}
function loadByRecordSelectSearchTypes(id){
    var currentDomain = document.getElementById(id).value;
    RelationshipDefHandler.getDomainSearchCriteria(currentDomain, byRecordSelectSearchTypes);
    
    // Cache the search fields for this domain
    DomainScreenHandler.getSearchResultFields(currentDomain, { callback:function(dataFromServer) {
      loadSearchResultFields(dataFromServer, currentDomain); }
    });
}
function byRecordSelectSearchTypes(data){
    
    dwr.util.removeAllOptions("byRecord_select_searchtypes"); 
    for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
        document.getElementById('byRecord_select_searchtypes').options.add(opt);
     }
     byRecordSelectSearchFields('byRecord_select_searchtypes');
}
function byRecordSelectSearchFields(searchTypeId){
    var selectDomain = document.getElementById('byRecord_SelectDomain').value;
    var searchType = document.getElementById(searchTypeId).value;
    RelationshipDefHandler.getSearchTypeCriteria(selectDomain, searchType,showByRecordSelectSearchFields);
}
function showByRecordSelectSearchFields(data){
    dwr.util.removeAllRows("byRecord_select_search_fields");
    var count = 0;
    // create a hidden field, put the querybuilder value in it. hidden field name=byRecordSelectQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "byRecordSelectQueryBuilder";
    hiddenField.name = "byRecordSelectQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('byRecord_select_search_fields').appendChild(hiddenField);
    var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('byRecord_select_search_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
             var field;
             try{
                 field = document.createElement('<input type="text" name="byRecordSelectSearchFieldName" />');
             }catch(err){
                 field = document.createElement("input");
             }
             field.type="text";
             field.size="5";
             field.name="byRecordSelectSearchFieldName";
             field.domainFieldName = fieldCfg.name;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name;   
             row.cells[1].appendChild(field);   
         }
     }
}
function byRecordSelectRecordSearch(){
    var selectedDomain =document.getElementById("byRecord_SelectDomain").value;
    var domainSearch = {name:selectedDomain}; 
    var byRecordSelectSearchFieldNames = document.getElementsByName('byRecordSelectSearchFieldName');
    var byRecord_qBuilder = document.getElementById("byRecordSelectQueryBuilder").value;
    domainSearch["type"] = byRecord_qBuilder; // put search type.
    var domainAttributes = [];
    for(i=0;i<byRecordSelectSearchFieldNames.length;i++){
        var tempFieldName = byRecordSelectSearchFieldNames[i].domainFieldName;
        var tempFieldValue = byRecordSelectSearchFieldNames[i].value ;
        var tempMap = {} ;
        tempMap[tempFieldName] = tempFieldValue;
        //alert(tempFieldName);
        domainAttributes.push( tempMap );
    }
    domainSearch.attributes = domainAttributes;
    RelationshipHandler.searchEnterprises (domainSearch, byRecordSelectSearchResults);
    

}
var cachedByRecordSelectSearchResults = null; // to cache the search results for select record (Select dialog)

function byRecordSelectSearchResults(data){
    cachedByRecordSelectSearchResults = []; // clear the cache array.
    for(i=0; i<data.length; i++) {
        cachedByRecordSelectSearchResults.push(data[i]);
        // For testing pagination, adding more records 
        cachedByRecordSelectSearchResults.push(data[i]);cachedByRecordSelectSearchResults.push(data[i]);
        cachedByRecordSelectSearchResults.push(data[i]);cachedByRecordSelectSearchResults.push(data[i]);
        cachedByRecordSelectSearchResults.push(data[i]);cachedByRecordSelectSearchResults.push(data[i]);
    }
    if(data == null){ 
       displayDiv("byRecordSelectSearchResultsFailure", true);
       displayDiv("byRecordSelectSearchResultsSuccess", false);
       displayDiv("byRecord_Select_SearchResultDiv", false);
    }else{
       displayDiv("byRecordSelectSearchResultsSuccess", true);
       displayDiv("byRecordSelectSearchResultsFailure", false);
       displayDiv("byRecord_Select_SearchResultDiv", true);
       
       byRecordSelectSearchResults_Display (1, intDomainSearchResultsItemsPerPage); 
       var byRecordSelectpaginator = dijit.byId("byRecordSelectSearchResultsPaginator");
       byRecordSelectpaginator.currentPage = 1;
       byRecordSelectpaginator.totalPages = Math.ceil(cachedByRecordSelectSearchResults.length / intDomainSearchResultsItemsPerPage) ;
       byRecordSelectpaginator.refresh();
 
       byRecordSelectSearchResultsLength = document.getElementsByName("byRecordSelectResultsRadio").length;        
   }
   enableByRecordSelectRecordButton();
}
function byRecordSelectSearchResults_Display_Refresh(currentPage){
    byRecordSelectSearchResults_Display(currentPage,intDomainSearchResultsItemsPerPage);
}
function byRecordSelectSearchResults_Display(currentPage,itemsPerPage){
    data = cachedByRecordSelectSearchResults;
    var selectedDomain =document.getElementById("byRecord_SelectDomain").value;
    dwr.util.removeAllRows("byRecord_Select_SearchResults_tableId");
    var fieldsToShowInSearchResults = searchResultsFields[selectedDomain];
    fieldsToShowInSearchResults.push("LastName"); fieldsToShowInSearchResults.push("FirstName"); // For testing purpose only.
   
    // show only records that should go in current page.
    var resultsToShow = new Array();
    var itemsFrom = 0, itemsTill = 10;
    itemsFrom = (currentPage-1) * itemsPerPage;
    if(itemsFrom > data.length) itemsFrom = 0;
    itemsTill = itemsFrom + itemsPerPage;
    
    if(itemsTill > data.length) itemsTill = data.length;
   // alert("records from " + itemsFrom + " : " + itemsTill);
    for(i=itemsFrom; i<itemsTill; i++) {
        resultsToShow.push(data[i]);
    }
    var columnCount = 0;
    for(i =0;i<resultsToShow.length;i++) {
      if(i==0){
          columnCount = 0;
          var header = document.getElementById('byRecord_Select_SearchResults_tableId').insertRow(i);
          header.className = "header";
          header.insertCell(columnCount++);
          for(j=0; j<resultsToShow[i].attributes.length; j++) {
            if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
            header.insertCell(columnCount);
            header.cells[columnCount].className = "label";
            header.cells[columnCount].innerHTML  = resultsToShow[i].attributes[j].name;
            columnCount++;
          }
       }
      columnCount = 0;
      var dataRow = document.getElementById('byRecord_Select_SearchResults_tableId').insertRow(i+1);
      dataRow.insertCell(columnCount);
      var chkbox ; //=  document.createElement("input");
      try{
          chkbox = document.createElement('<input type="radio" name="byRecordSelectResultsRadio" />');
      }catch(err){
          chkbox = document.createElement("input");
      }
      chkbox.type = "radio";
      chkbox.name = "byRecordSelectResultsRadio";
      chkbox.value = resultsToShow[i].EUID;
      dataRow.cells[columnCount].appendChild (chkbox);
      columnCount++;
      for(j=0; j<resultsToShow[i].attributes.length; j++) {
          if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
          dataRow.insertCell(columnCount);
          dataRow.cells[columnCount].innerHTML = resultsToShow[i].attributes[j].value;
          columnCount++;
      }
    }
}
function enableByRecordSelectRecordButton(){
    if(byRecordSelectSearchResultsLength > 0 ){
          document.getElementById("byRecordSelectRecordButton").disabled = false;
    }
}
/*
 * Scripts for Select/Search screen <END>
 */


  
  
/*
 * Scripts for Main (tree, details) screen <START>
 */

function byRecordSelectRecord() {
    var selectedDomain = document.getElementById("byRecord_SelectDomain").value;
    var selectedRecordEUID = null;
    
    var chkBoxes = document.getElementsByName("byRecordSelectResultsRadio");
    for(i=0; i<chkBoxes.length; i++) {
        if(chkBoxes[i].checked) {
            selectedRecordEUID = chkBoxes[i].value;
        }
    }
    
    byRecord_CurrentWorking_Domain = selectedDomain;
    byRecord_CurrentWorking_EUID = selectedRecordEUID;

    // Hard code assigning values, to be used for Add dialog
    byRecord_CurrentSelected_TargetDomain = "Company";
    byRecord_CurrentSelected_RelationshipDefName = "worksfor";
    
    RelationshipDefHandler.getRelationshipDefByName(byRecord_CurrentSelected_RelationshipDefName, byRecord_CurrentWorking_Domain, 
        byRecord_CurrentSelected_TargetDomain, cacheRelationshipDef);
        
    hideByRecordSelectDialog();

}
// function to cache the realtionship def details.
function cacheRelationshipDef(data) {
    byRecord_CachedRelationshipDefs [data.name] = data;
}

// function to show right section details...
function byRecord_ShowDetails () {

	if(byRecord_Selected_Relationship != null) {
		alert("Showing details for relationship: " + byRecord_Selected_Relationship);
		alert(byRecord_Selected_Relationship.relationshipId + " : " + byRecord_Selected_Relationship.sourceDomain + " : " 
			+ byRecord_Selected_Relationship.targetDomain + " :"  + byRecord_Selected_Relationship.relationshipDefName);
	} else if(byRecord_Selected_Record != null) {
		alert("showing details for  record  : " + byRecord_Selected_Record);
		alert(byRecord_Selected_Record.EUID );
	} else {
		alert("details section show nothing. clear the currently shown details. ");
	}
	
}


/*
 * Scripts for Main (listing, details) screen <END>
 */





/*
 * Scripts for Add Relationship screen <START>
 */ 

function byRecord_prepareAdd () {
    var relationshipDefObj = byRecord_CachedRelationshipDefs[byRecord_CurrentSelected_RelationshipDefName] ;
    
   // alert("Soruce domain " + byRecord_CurrentWorking_Domain );
   // alert("target domain " + byRecord_CurrentSelected_TargetDomain);
   // alert("Source EUID " + byRecord_CurrentWorking_EUID);
   // alert("relationship def " + relationshipDefObj.name);
    // create search criteria section for target domain
    RelationshipDefHandler.getDomainSearchCriteria(byRecord_CurrentSelected_TargetDomain, showByRecordAddSearchTypes);
    // create attributes section for relationship def 
    
    populateAddRelationshipDefAttributes( relationshipDefObj );
    //RelationshipDefHandler.getRelationshipDefByName(relationshipDefName, byRecord_CurrentWorking_Domain, byRecord_CurrentSelected_TargetDomain, populateAddRelationshipDefAttributes);
    // Cache the search fields for this domain
    DomainScreenHandler.getSearchResultFields(byRecord_CurrentSelected_TargetDomain, { callback:function(dataFromServer) {
      loadSearchResultFields(dataFromServer, byRecord_CurrentSelected_TargetDomain); }
    });
}
function showByRecordAddSearchTypes(data){
    dwr.util.removeAllOptions("byRecord_add_searchtypes");
    for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
        document.getElementById('byRecord_add_searchtypes').options.add(opt);
    }
    var selectedSearchType = document.getElementById('byRecord_add_searchtypes').value;
    RelationshipDefHandler.getSearchTypeCriteria(byRecord_CurrentSelected_TargetDomain,selectedSearchType,showByRecordAddSearchFields);
}
function loadByRecordAddSearchFields(id){
    var selectedSearchType = document.getElementById(id).value;
    RelationshipDefHandler.getSearchTypeCriteria(byRecord_CurrentSelected_TargetDomain,selectedSearchType,showByRecordAddSearchFields);
    
}
function showByRecordAddSearchFields(data){
    dwr.util.removeAllRows("byRecord_add_search_fields");
    // create a hidden field, put the querybuilder value in it. hidden field name=byRecordAddQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "byRecordAddQueryBuilder";
    hiddenField.name = "byRecordAddQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('byRecord_add_search_fields').appendChild(hiddenField);
    var count = 0;
     var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('byRecord_add_search_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
             var field ;
             try{
                 field = document.createElement('<input type="text" name="byRecordAddSearchFieldName" />');
              }catch(err){
                 field = document.createElement("input");
              }
             field.type="text";
             field.size="5";
             field.name="byRecordAddSearchFieldName";
             field.domainFieldName = fieldCfg.name;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name; 
             row.cells[1].appendChild(field); 
         }
    }
}
function showByRecordAddRecordSearch()
{
    var selectedTargetDomain = byRecord_CurrentSelected_TargetDomain;
    var domainSearch = {name:selectedTargetDomain}; 
    var addSourceSearchFieldNames = document.getElementsByName('byRecordAddSearchFieldName');
    var byRecordAddqBuilder = document.getElementById("byRecordAddQueryBuilder").value;
    domainSearch["type"] = byRecordAddqBuilder; // put search type.
    var domainAttributes = [];
    for(i=0;i<addSourceSearchFieldNames.length;i++){
        var tempFieldName = addSourceSearchFieldNames[i].domainFieldName;
        var tempFieldValue = addSourceSearchFieldNames[i].value ;
        var tempMap = {} ;
        tempMap[tempFieldName] = tempFieldValue
        domainAttributes.push( tempMap );
    }
    domainSearch.attributes = domainAttributes;
    RelationshipHandler.searchEnterprises (domainSearch, showByRecordAddRecordSearchResults);
}
var cachedByRecordAddSearchResults = null; // to cache the search results for Add record (Add dialog)
function showByRecordAddRecordSearchResults(data){
    cachedByRecordAddSearchResults = []; // clear the cache array.
    for(i=0; i<data.length; i++) {
        cachedByRecordAddSearchResults.push(data[i]);
        // For testing pagination, adding more records 
        cachedByRecordAddSearchResults.push(data[i]);cachedByRecordAddSearchResults.push(data[i]);
        cachedByRecordAddSearchResults.push(data[i]);cachedByRecordAddSearchResults.push(data[i]);
        cachedByRecordAddSearchResults.push(data[i]);cachedByRecordAddSearchResults.push(data[i]);
    }
    if(data == null){ 
       displayDiv("byRecordAddSearchResultsFailure", true);
       displayDiv("byRecordAddSearchResultsSuccess", false);
       displayDiv("byRecord_Add_SearchResultDiv", false);
    }else{
       displayDiv("byRecordAddSearchResultsSuccess", true);
       displayDiv("byRecordAddSearchResultsFailure", false);
       displayDiv("byRecord_Add_SearchResultDiv", true);
       
       byRecordAddSearchResults_Display (1, intDomainSearchResultsItemsPerPage); 
       var byRecordAddpaginator = dijit.byId("byRecordAddSearchResultsPaginator");
       byRecordAddpaginator.currentPage = 1;
       byRecordAddpaginator.totalPages = Math.ceil(cachedByRecordAddSearchResults.length / intDomainSearchResultsItemsPerPage) ;
       byRecordAddpaginator.refresh();
       byRecordAddSearchResultsLength = document.getElementsByName("byRecordAddResultsCheckBox").length;        
    }
    enableByRecordAddRecordButton();
}
function byRecordAddSearchResults_Display_Refresh(currentPage){
    byRecordAddSearchResults_Display(currentPage,intDomainSearchResultsItemsPerPage); 
}
function byRecordAddSearchResults_Display(currentPage,itemsPerPage){
    data = cachedByRecordAddSearchResults;
    dwr.util.removeAllRows("byRecord_Add_SearchResults_tableId");
    var fieldsToShowInSearchResults = searchResultsFields[byRecord_CurrentSelected_TargetDomain];
    fieldsToShowInSearchResults.push("LastName"); fieldsToShowInSearchResults.push("FirstName"); // For testing purpose only.

    // show only records that should go in current page.
    var resultsToShow = new Array();
    var itemsFrom = 0, itemsTill = 10;
    itemsFrom = (currentPage-1) * itemsPerPage;
    if(itemsFrom > data.length) itemsFrom = 0;
    itemsTill = itemsFrom + itemsPerPage;

    if(itemsTill > data.length) itemsTill = data.length;
    // alert("records from " + itemsFrom + " : " + itemsTill);
    for(i=itemsFrom; i<itemsTill; i++) {
      resultsToShow.push(data[i]);
    }
    var columnCount = 0;
    for(i =0;i<resultsToShow.length;i++) {
     if(i==0){
        columnCount = 0;
        var header = document.getElementById('byRecord_Add_SearchResults_tableId').insertRow(i);
        header.className = "header";
        header.insertCell(columnCount++);
        for(j=0; j<resultsToShow[i].attributes.length; j++) {
          if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
          header.insertCell(columnCount);
          header.cells[columnCount].className = "label";
          header.cells[columnCount].innerHTML  = resultsToShow[i].attributes[j].name;
          columnCount++;
        }
     }
     columnCount = 0;
     var dataRow = document.getElementById('byRecord_Add_SearchResults_tableId').insertRow(i+1);
     dataRow.insertCell(columnCount);
     var chkbox ; //=  document.createElement("input");
     try{
        chkbox = document.createElement('<input type="checkbox" name="byRecordAddResultsCheckBox" />');
     }catch(err){
        chkbox = document.createElement("input");
     }
     chkbox.type = "checkbox";
     chkbox.name = "byRecordAddResultsCheckBox";
     chkbox.value = resultsToShow[i].EUID;
     dataRow.cells[columnCount].appendChild (chkbox);
     columnCount++;
     for(j=0; j<resultsToShow[i].attributes.length; j++) {
        if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
        dataRow.insertCell(columnCount);
        dataRow.cells[columnCount].innerHTML = resultsToShow[i].attributes[j].value;
        columnCount++;
     }
   }
}
function enableByRecordAddRecordButton(){
   if(byRecordAddSearchResultsLength > 0 ){
        document.getElementById("byRecordAddRecordButton").disabled = false;
   }
}
function byRecordAddRecord(){
    var startDateField = document.getElementById('add_predefined_startDate');
    var endDateField = document.getElementById('add_predefined_endDate');
    var purgeDateField = document.getElementById('add_predefined_purgeDate');
    var startDate,endDate,purgeDate;
    var SourceDomain = byRecord_CurrentWorking_Domain;
    var TargetDomain = byRecord_CurrentSelected_TargetDomain;
    var relationshipDefObj = byRecord_CachedRelationshipDefs[byRecord_CurrentSelected_RelationshipDefName] ;
    var RelationshipDefName = relationshipDefObj.name;
    var relationshipCustomAttributes = [];
    
    var startDateRequireField = (getBoolean(relationshipDefObj.startDateRequired)); 
    var endDateRequireField = (getBoolean(relationshipDefObj.endDateRequired)); 
    var purgeDateRequireField = (getBoolean(relationshipDefObj.purgeDateRequired));
    var selectedCheckBoxLength = 0;
    
    for(cc =0; cc < relationshipDefObj.extendedAttributes.length; cc++) {
     var attributeName = relationshipDefObj.extendedAttributes[cc].name;
     var attributeId = document.getElementById("add_custom_" + relationshipDefObj.extendedAttributes[cc].name);
     var attributeValue =  document.getElementById("add_custom_" + relationshipDefObj.extendedAttributes[cc].name).value;
     var attributeType = relationshipDefObj.extendedAttributes[cc].dataType;
      if(getBoolean(relationshipDefObj.extendedAttributes[cc].isRequired)) {
        if( isEmpty (attributeValue) ) {
            alert(getMessageForI18N("enter_ValueFor") + " " + attributeName +getMessageForI18N("period") );
            attributeId.focus();
            return ;
        }
     }
     if( ! isValidCustomAttribute( attributeType, attributeValue) ) {
        alert(attributeValue + " " +getMessageForI18N("isnotavalidvaluefor")+ " " +attributeName + " " +getMessageForI18N("attribute")+ " " +getMessageForI18N("attributeTypeFor")+ " " +attributeValue + " " +getMessageForI18N("is")+ " " +"'"+attributeType+"'");
        attributeId.focus();
        return;
    }

     relationshipCustomAttributes.push( {attributeName : attributeValue} );
    }
    if(startDateField != null)
    {
         startDate =  document.getElementById('add_predefined_startDate').value;
         if(startDateRequireField == true){
            if( isEmpty (startDate) ) {
            alert(getMessageForI18N("enterValueFor_effectiveFrom"));
            startDateField.focus();
            return ;
          } 
        }     
    }
    if(endDateField != null)
    {
         endDate =  document.getElementById('add_predefined_endDate').value;
         if(endDateRequireField == true){
           if( isEmpty (endDate) ) {
            alert(getMessageForI18N("enterValueFor_effectiveTo"));
            endDateField.focus();
            return ;
         }  
        }     
    }
    if(purgeDateField != null)
    {
         purgeDate =  document.getElementById('add_predefined_purgeDate').value;
         if(purgeDateRequireField == true){
            if( isEmpty (purgeDate) ) {
            alert(getMessageForI18N("enterValueFor_purgeDate"));
            purgeDateField.focus();
            return ;
         } 
        }    
    }
    
    var targetCheckBox = false;
    var targetCheckedArray = document.getElementsByName("byRecordAddResultsCheckBox"); 
    for(i=0;i<targetCheckedArray.length; i++) {
      if(targetCheckedArray[i].checked) {
          selectedCheckBoxLength++;
          targetCheckBox = true;
          var targetRecordEUID = targetCheckedArray[i].value;
          var newRelationshipRecord = {};
          newRelationshipRecord.name = RelationshipDefName;
          newRelationshipRecord.sourceDomain = SourceDomain;
          newRelationshipRecord.targetDomain = TargetDomain;
          newRelationshipRecord.sourceEUID = byRecord_CurrentWorking_EUID;
          newRelationshipRecord.targetEUID = targetRecordEUID;

          newRelationshipRecord.startDate = startDate;
          newRelationshipRecord.endDate = endDate;
          newRelationshipRecord.purgeDate = purgeDate;
          newRelationshipRecord.attributes = relationshipCustomAttributes ; 
          RelationshipHandler.addRelationship(newRelationshipRecord,byRecordAddRecordCB);
      }       
    }
    if(selectedCheckBoxLength == 0) alert(getMessageForI18N("select_atleast_one_destination_record"));
}
function byRecordAddRecordCB(data){
    alert("Add Records relationship is added " +data);
    hideByRecordAddDialog();
}
/*
 * Scripts for Add Relationship screen <END>
 */