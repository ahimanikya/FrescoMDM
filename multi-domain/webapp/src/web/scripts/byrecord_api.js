
/*
 * API integration scripts for Manage - By Record Screens.
 */ 

dwr.engine.setErrorHandler(exceptionHandler);
var byRecordSelectSearchResultsLength = null; // Holds the select record search results length

function exceptionHandler(message) {
    alert(getMessageForI18N("exception")+ " " + message);
}


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
    
    // Load search criteria for select domains.
    var selectedDomain =document.getElementById("byRecord_SelectDomain").value;    
    RelationshipDefHandler.getDomainSearchCriteria(selectedDomain, byRecordSelectSearchTypes);
}
function loadByRecordSelectSearchTypes(id){
    var currentDomain = document.getElementById(id).value;
    RelationshipDefHandler.getDomainSearchCriteria(currentDomain, byRecordSelectSearchTypes);
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
    
    DomainScreenHandler.getSearchResultFields(selectedDomain, { callback:function(dataFromServer) {
      loadSearchResultFields(dataFromServer, selectedDomain); }
    });
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
 
       byRecordSelectSearchResultsLength = document.getElementsByName("byRecordSelectResultsCheckBox").length;        
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
          chkbox = document.createElement('<input type="radio" name="byRecordSelectResultsCheckBox" />');
      }catch(err){
          chkbox = document.createElement("input");
      }
      chkbox.type = "radio";
      chkbox.name = "byRecordSelectResultsCheckBox";
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
    if(byRecordSelectSearchResultsLength > 0 && byRecordSelectSearchResultsLength >0 ){
          document.getElementById("byRecordSelectRecordButton").disabled = false;
    }
}
/*
 * Scripts for Select/Search screen <END>
 */


  
  
/*
 * Scripts for Main (tree, details) screen <START>
 */


/*
 * Scripts for Main (listing, details) screen <END>
 */





/*
 * Scripts for Add Relationship screen <START>
 */ 
/*
 * Scripts for Add Relationship screen <END>
 */