
/*
 * API integration scripts for Manage - By Relationship Screens.
 */ 

dwr.engine.setErrorHandler(exceptionHandler);
var addSourceResultsLength ;
var addTargetResultsLength ;
function exceptionHandler(message) {
    alert(getMessageForI18N("exception")+ " " + message);
}

var isSelectSourceDomainCriteriaApplied = false;
var isSelectTargetDomainCriteriaApplied = false;
var isSelectRelationshipDefAttributeCriteriaApplied = false;

var currentSelectedSourceDomain = null; // Holds the current selected source domain
var currentSelectedTargetDomain = null; // Holds the current selected target domain. 
var currentSelectedRelationshipDef = null; //Holds the current selected relationshipDef.
   
var cachedRelationshipDefs = {}; // To hold relationshipDef data for relationshipDef Name.

var cachedSearchResults = new Array(); // Store the search results (relationships);
var intRelationshipItemsPerPage = 10;
var intRelationshipMaxResultSize = 100;

var intDomainSearchResultsItemsPerPage = 5; // Number of items to be shown in search results (Add dialog)

var summaryFields = {}; // To hold summary fields for domains.
var searchResultsFields = {}; // To hold the list of search results fields to be used, per domain.
/*
 * Scripts for Select/Search screen <START>
 */ 
  function loadDomainsForSearch () {
      isByRelSelectDialogLoaded = true;
      DomainHandler.getDomains(updateSelectDomains);
  }
  function updateSelectDomains(data) {
      dwr.util.removeAllOptions("select_sourceDomain");
      dwr.util.removeAllOptions("select_targetDomain");
      dwr.util.addOptions("select_sourceDomain", data, "name");
      dwr.util.setValue("select_sourceDomain", data[0].name); 
      dwr.util.addOptions("select_targetDomain", data, "name");
      dwr.util.setValue("select_targetDomain", data[1].name);       
      loadRelationshipDefs();
      
      // Load search criteria for source/target domains.

      var SourceDomain =document.getElementById("select_sourceDomain").value;
      var TargetDomain =document.getElementById("select_targetDomain").value;
      //var RelationshipDef =document.getElementById("select_relationshipDefs").value;

      RelationshipDefHandler.getDomainSearchCriteria(SourceDomain, selectSourceSearchTypes);
      RelationshipDefHandler.getDomainSearchCriteria(TargetDomain, selectTargetSearchTypes);      
  }
  function loadRelationshipDefs() {
      var sourceDomain = document.getElementById("select_sourceDomain").value;
      var targetDomain = document.getElementById("select_targetDomain").value;
      RelationshipDefHandler.getRelationshipDefs(sourceDomain, targetDomain, updateRelationshipDefs);
  }
  
  function updateRelationshipDefs(data) {
      dwr.util.removeAllOptions("select_relationshipDefs");
      if(data == null || data == "") return;
      dwr.util.addOptions("select_relationshipDefs", data, "name");
      //dwr.util.setValue("select_relationshipDefs", data[0].name); 
      var selectSourceDomain = document.getElementById("select_sourceDomain").value;
      var selectTargetDomain = document.getElementById("select_targetDomain").value;
      var selectRelationshipDef = document.getElementById("select_relationshipDefs").value;
      RelationshipDefHandler.getRelationshipDefByName(selectRelationshipDef, selectSourceDomain, selectTargetDomain, populateSelectRelationshipDefAttributes);
  }
  
  function getSelectRelationshipAttributes(){
     var selectSourceDomain = document.getElementById("select_sourceDomain").value;
     var selectTargetDomain = document.getElementById("select_targetDomain").value;
     var selectRelationshipDef = document.getElementById("select_relationshipDefs").value;
     RelationshipDefHandler.getRelationshipDefByName(selectRelationshipDef, selectSourceDomain, selectTargetDomain, populateSelectRelationshipDefAttributes);
}
  

function populateSelectRelationshipDefAttributes(data){
    var startDate =(getBoolean(data.startDate));
    var endDate =(getBoolean(data.endDate));
    var purgeDate =(getBoolean(data.purgeDate));
    var CustomrowCount = 0;
    var PredefinedrowCount = 0;  
    cachedRelationshipDefs[data.name] = data;
    //document.getElementById('byrel_select_customAttributes').innerHTML="";
    //document.getElementById('byrel_select_predefinedAttributes').innerHTML="";
    dwr.util.removeAllRows("byrel_select_customAttributes");
    dwr.util.removeAllRows("byrel_select_predefinedAttributes");
    if(data.extendedAttributes.length>0 ){
          var searchableCustomAttributes = [] ;
          for(i=0; i<data.extendedAttributes.length; i++) {
              if( getBoolean(data.extendedAttributes[i].searchable) ) {
                  searchableCustomAttributes.push(data.extendedAttributes[i]);
              }
          }
           createCustomAttributesSection ("byrel_select_customAttributes", searchableCustomAttributes, "select_custom", false);
           document.getElementById("select_Relationship_CustomAtrributes").style.visibility="visible"
           document.getElementById("select_Relationship_CustomAtrributes").style.display="block";
       }
       else{
           
           document.getElementById("select_Relationship_CustomAtrributes").style.visibility="hidden"
           document.getElementById("select_Relationship_CustomAtrributes").style.display="none";
       }
       if(startDate==true || endDate==true || purgeDate == true ){ 
          createPredefinedAttributesSection ("byrel_select_predefinedAttributes", data, "select_predefined", false);
          document.getElementById("select_Relationship_PredefinedAtrributes").style.visibility="visible"
          document.getElementById("select_Relationship_PredefinedAtrributes").style.display="";
      } else{
          document.getElementById("select_Relationship_PredefinedAtrributes").style.visibility="hidden"
          document.getElementById("select_Relationship_PredefinedAtrributes").style.display="none";
      }
}
  
  function loadSelectSourceSearchTypes(id)   {
      var sourceDomain = document.getElementById(id).value;
      RelationshipDefHandler.getDomainSearchCriteria(sourceDomain, selectSourceSearchTypes);
  }
  
function selectSourceSearchTypes(data)   {
   dwr.util.removeAllOptions("select_source_searchtypes"); 
   for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
        document.getElementById('select_source_searchtypes').options.add(opt);
    }
    selectSourceSearchFields('select_source_searchtypes');
}

function loadSelectTargetSearchTypes(id)   {
      var targetDomain = document.getElementById(id).value;
      RelationshipDefHandler.getDomainSearchCriteria(targetDomain, selectTargetSearchTypes);
  }
  
function selectTargetSearchTypes(data)   {
   dwr.util.removeAllOptions("select_target_searchtypes"); 
   for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
        document.getElementById('select_target_searchtypes').options.add(opt);
    }
    selectTargetSearchFields('select_target_searchtypes');
}

function selectSourceSearchFields(searchTypeId){
    var sourceDomain = document.getElementById('select_sourceDomain').value;
      var searchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(sourceDomain, searchType,selectSourceSearchTypeFields);
    
}

function selectSourceSearchTypeFields(data){
    dwr.util.removeAllRows("select_search_source_fields"); 
    var count = 0;
    // create a hidden field, put the querybuilder value in it. hidden field name=selectSourceQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "selectSourceQueryBuilder";
    hiddenField.name = "selectSourceQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('select_search_source_fields').appendChild(hiddenField);
    var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('select_search_source_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
             var field; // = document.createElement("input");
             try{
                 field = document.createElement('<input type="text" name="selectSourceSearchFieldName" />');
             }catch(err){
                 field = document.createElement("input");
             }
             field.type="text";
             field.size="5";
             field.name="selectSourceSearchFieldName";
             field.domainFieldName = fieldCfg.name;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name;   
             row.cells[1].appendChild(field);   
         }
        
     }
}

function selectTargetSearchFields(searchTypeId){
    var taragetDomain = document.getElementById('select_targetDomain').value;
    var searchType = document.getElementById(searchTypeId).value;
    RelationshipDefHandler.getSearchTypeCriteria(taragetDomain, searchType,selectTargetSearchTypeFields);
    
}

function selectTargetSearchTypeFields(data){
    dwr.util.removeAllRows("select_search_target_fields"); 
    var count = 0;
    // create a hidden field, put the querybuilder value in it. hidden field name=selectTargetQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "selectTargetQueryBuilder";
    hiddenField.name = "selectTargetQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('select_search_target_fields').appendChild(hiddenField);
    var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('select_search_target_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
             var field; // = document.createElement("input");
             try{
                 field = document.createElement('<input type="text" name="selectTargetSearchFieldName" />');
             }catch(err){
                 field = document.createElement("input");
             }
             field.type="text";
             field.size="5";
             field.name="selectTargetSearchFieldName";
             field.domainFieldName = fieldCfg.name;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name;   
             row.cells[1].appendChild(field);   
         }
     }
}

  
/*
 * Scripts for Select/Search screen <END>
 */

  
  
/*
 * Scripts for Main (listing, details) screen <START>
 */


function searchRelationships() {
    isSelectTargetDomainCriteriaApplied = false;
    isSelectSourceDomainCriteriaApplied = false;
    isSelectRelationshipDefAttributeCriteriaApplied = false;
    var sourceDomain = document.getElementById("select_sourceDomain").value;
    var targetDomain = document.getElementById("select_targetDomain").value;
    var relationshipDef = document.getElementById("select_relationshipDefs").value;
    
   if(sourceDomain == null || sourceDomain == "") {
       alert(getMessageForI18N("selectsourcedomain")+ " " +getMessageForI18N("fromthelist"));
       return false;
   }
   if(targetDomain == null || targetDomain == "") {
       alert(getMessageForI18N("selecttargetdomain")+ " " +getMessageForI18N("fromthelist"));
       return false;
   }   
   if(relationshipDef == null || relationshipDef == "") {
       alert(getMessageForI18N("selectarelationshipDef")+ " " +getMessageForI18N("fromthelist"));
       return false;
   }
   currentSelectedSourceDomain = sourceDomain ;
   currentSelectedTargetDomain = targetDomain ;
   currentSelectedRelationshipDef = relationshipDef;
    var selectSourceSearchFieldNames = document.getElementsByName('selectSourceSearchFieldName');
    var selectTargetSearchFieldNames = document.getElementsByName('selectTargetSearchFieldName');

    var relationshipDefObj = cachedRelationshipDefs[relationshipDef];
    for(c =0; c < relationshipDefObj.extendedAttributes.length; c++) {
      var attributeName = relationshipDefObj.extendedAttributes[c].name;
      if(document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[c].name) == null) continue;
      var attributeId = document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[c].name);
      var attributeValue =  document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[c].name).value;
      var attributeType = relationshipDefObj.extendedAttributes[c].dataType;
      if( ! isValidCustomAttribute( attributeType, attributeValue) ) {
          alert(attributeValue + " " + getMessageForI18N("isnotavalidvaluefor")+ " " + attributeName + " " + getMessageForI18N("attribute")+getMessageForI18N("period"));
          attributeId.focus(); 
          return;
      }
     }
     
    var sourceDomainSearch = {name: sourceDomain}; 
    var targetDomainSearch = {name: targetDomain};
    var relationshipDefSearch = {name: relationshipDef, sourceDomain: sourceDomain, targetDomain: targetDomain};
    
    // Get search criteria fieds value for Source domain
    var sourceqBuilder = document.getElementById("selectSourceQueryBuilder").value;
    sourceDomainSearch["type"] = sourceqBuilder; // put search type.
    var sourceDomainAttributes = [];
    for(i=0;i<selectSourceSearchFieldNames.length;i++){
        var sourceTempFieldName = selectSourceSearchFieldNames[i].domainFieldName;
        var sourceTempFieldValue = selectSourceSearchFieldNames[i].value ;
        var tempMap = {} ;
        tempMap[sourceTempFieldName] = sourceTempFieldValue;
        sourceDomainAttributes.push( tempMap );
        if(!isEmpty(sourceTempFieldValue)) isSelectSourceDomainCriteriaApplied = true;
    }
    sourceDomainSearch.attributes = sourceDomainAttributes;
    
    // Get search criteria fields value for Target domain
    var taragetqBuilder = document.getElementById("selectTargetQueryBuilder").value;
    targetDomainSearch["type"] = taragetqBuilder; // put search type.
    var targetDomainAttributes = [];
    for(i=0;i<selectTargetSearchFieldNames.length;i++){
        
        var targetTempFieldName = selectTargetSearchFieldNames[i].domainFieldName;
        var targetTempFieldValue = selectTargetSearchFieldNames[i].value ;
        var targetTempMap = {} ;
        tempMap[targetTempFieldName] = targetTempFieldValue;
        targetDomainAttributes.push( targetTempMap );
        if(!isEmpty(targetTempFieldValue)) isSelectTargetDomainCriteriaApplied = true;
    }
    targetDomainSearch.attributes = targetDomainAttributes;
   // relationshipdef attributes for relationshipDefSearch
   var searchCustomAttributes = [];
   
    for(cc =0; cc < relationshipDefObj.extendedAttributes.length; cc++) {
      var CustomAttributeName = relationshipDefObj.extendedAttributes[cc].name;
      if(document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[cc].name) == null) continue;
      var CustomAttributeId = document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[cc].name);
      var CustomAttributeValue =  document.getElementById("select_custom_" + relationshipDefObj.extendedAttributes[cc].name).value;
      var CustomAttributeType = relationshipDefObj.extendedAttributes[cc].dataType;
      if( ! isValidCustomAttribute( CustomAttributeType, CustomAttributeValue) ) {
          alert(CustomAttributeValue + " " +getMessageForI18N("isnotavalidvaluefor")+ " " + CustomAttributeName + " " + getMessageForI18N("attribute"));
          CustomAttributeId.focus();
          return;
      }
      if(!isEmpty(CustomAttributeValue)) isSelectRelationshipDefAttributeCriteriaApplied = true;
      searchCustomAttributes.push( {CustomAttributeName : CustomAttributeValue} );
     }
   var startDateField = document.getElementById('select_predefined_startDate');
   var endDateField = document.getElementById('select_predefined_endDate');
   var purgeDateField = document.getElementById('select_predefined_purgeDate');
   var startDate,endDate,purgeDate;
   if(startDateField != null)
     {
           startDate =  document.getElementById('select_predefined_startDate').value;
           if(!isEmpty(startDate)) isSelectRelationshipDefAttributeCriteriaApplied = true;
     }
     if(endDateField != null)
     {
           endDate =  document.getElementById('select_predefined_endDate').value;
           if(!isEmpty(endDate)) isSelectRelationshipDefAttributeCriteriaApplied = true;
     }
     if(purgeDateField != null)
     {
           purgeDate =  document.getElementById('select_predefined_purgeDate').value;
           if(!isEmpty(purgeDate)) isSelectRelationshipDefAttributeCriteriaApplied = true;
     }
    relationshipDefSearch.startDate =startDate;
    relationshipDefSearch.endDate = endDate;
    relationshipDefSearch.purgeDate = purgeDate;
    relationshipDefSearch.attributes = searchCustomAttributes;

    hideByRelSelectDialog(); // hide the select dialog...
    
    displayDiv("selectSourceDomainFilterImg", isSelectSourceDomainCriteriaApplied);
    displayDiv("selectTargetDomainFilterImg", isSelectTargetDomainCriteriaApplied);
    displayDiv("selectRelationshipDefFilterImg", isSelectRelationshipDefAttributeCriteriaApplied);


    // Load fields for summary display for source & target domain
    DomainScreenHandler.getSummaryFields(sourceDomain, { callback:function(dataFromServer) {
      loadSummaryFields(dataFromServer, sourceDomain); }
    });
    DomainScreenHandler.getSummaryFields(targetDomain, { callback:function(dataFromServer) {
      loadSummaryFields(dataFromServer, targetDomain); }
    });
    
    // Load fields for search results display for source & target domain, and store it for future use.
    DomainScreenHandler.getSearchResultFields(sourceDomain, { callback:function(dataFromServer) {
      loadSearchResultFields(dataFromServer, sourceDomain); }
    });    
    DomainScreenHandler.getSearchResultFields(targetDomain, { callback:function(dataFromServer) {
      loadSearchResultFields(dataFromServer, targetDomain); }
    });   
    
    // Load relationship definition data, and store it for future use.
    RelationshipDefHandler.getRelationshipDefByName(
      relationshipDef, sourceDomain, targetDomain, { callback:function(dataFromServer) {
      cacheRelationshipDefAttributes(dataFromServer, relationshipDef);}
    });
    
    document.getElementById("selectedSourceDomain").innerHTML = sourceDomain;
    document.getElementById("selectedRelationshipDef").innerHTML = relationshipDef;
    document.getElementById("selectedTargetDomain").innerHTML = targetDomain;
    
   RelationshipHandler.searchRelationships (sourceDomainSearch, targetDomainSearch, relationshipDefSearch, searchResultsCallback);

}
function loadSummaryFields(data, domainName) {
    if(domainName == null) return;
    var fieldsList = []; var i=0;
    for (var fieldGrp in data)  {
        for (var fields in data[fieldGrp])  {
          //alert(fieldGrp + " : "+data[fieldGrp][fields].name);
          fieldsList[i ++] = data[fieldGrp][fields].name;
        }
     }
     //alert(fieldsList);
     summaryFields[domainName] = fieldsList;
}
function loadSearchResultFields(data, domainName) {
    if(domainName == null) return;
    var fieldsList = []; var i=0;
    for (var fieldGrp in data)  {
        for (var fields in data[fieldGrp])  {
          //alert(fieldGrp + " : "+data[fieldGrp][fields].name);
          fieldsList[i ++] = data[fieldGrp][fields].name;
        }
     }
     //alert(fieldsList);
     searchResultsFields[domainName] = fieldsList;    
   //  alert("fields loaded: " + searchResultsFields[domainName]);
}
function cacheRelationshipDefAttributes (data, relationshipDefName) {
    //alert("relationship def: " + data);
    cachedRelationshipDefs[relationshipDefName] = data;

    // Update the view with relationship Name along with direction icon.
    var strHTML = "<table cellspacing='0' cellpadding='0'><tr><td valign='middle'>" + relationshipDefName;
    strHTML += "</td><td valign='middle'>";
    if(getBoolean (data.biDirection) ) {
        strHTML += "<img src='images/icons/relationship-both.png' hspace='3'>";
    } else {
        strHTML += "<img src='images/icons/relationship-right.png' hspace='3'>";
    }
    strHTML +="</td></tr></table>";
    document.getElementById("selectedRelationshipDef").innerHTML = strHTML;
}

function searchResultsCallback(data) {
    cachedSearchResults = []; // clear the cache array.
    // Cache the search results.
    for(i=0; i<data.length; i++) {
        cachedSearchResults.push(data[i]);
        // for testing pagination, adding more records ....
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
        cachedSearchResults.push(data[i]);cachedSearchResults.push(data[i]);
    }
    //alert("cached search results length " + cachedSearchResults.length);
    displaySearchResults(1, intRelationshipItemsPerPage); 
    // Show pagination
    var paginator = dijit.byId("relationshipsSearchResultPaginator");
    paginator.currentPage = 1;
    paginator.totalPages = Math.ceil(cachedSearchResults.length / intRelationshipItemsPerPage) ;
    paginator.refresh();  
}
function searchResultsRefresh(currentPage) {
    displaySearchResults(currentPage, 10);
}
function displaySearchResults ( currentPage, itemsPerPage) {
    data = cachedSearchResults;
    var relationListingFuncs = [
      function(data) { return "<input type='checkbox' align='center' id='relationship_id' name='relationship_id' value='"+data.id+"' onclick='refreshRelationshipsListingButtonsPalette();' >" ; },
      function(data) { return data.sourceHighLight; },
      function(data) { return data.targetHighLight; }
    ];
    dwr.util.removeAllRows("relationshipsListing");
    if(data == null || data.length<=0) {
        //alert("no relationship definitions found");
        dwr.util.addRows("relationshipsListing", [''], [function(data){return "No Relationships found.";}], {
              cellCreator:function(options) {
                var td = document.createElement("td");
                td.colSpan="7"; td.align="center";
                return td;
              },
              escapeHtml:false
        });
        return;
    }
    // show only records that should go in current page.
    var resultsToShow = new Array();
    var itemsFrom = 0, itemsTill = 10;
    itemsFrom = (currentPage-1) * itemsPerPage;
    if(itemsFrom > data.length) itemsFrom = 0;
    itemsTill = itemsFrom + itemsPerPage;
    
    if(itemsTill > data.length) itemsTill = data.length;
   // alert(itemsFrom + " : " + itemsTill);
    for(i=itemsFrom; i<itemsTill; i++) {
        resultsToShow.push(data[i]);
    }
    var uniqueIndexId = itemsFrom;
    dwr.util.addRows("relationshipsListing", resultsToShow, relationListingFuncs, {
        rowCreator:function(options) {
          var row = document.createElement("tr");
          //row.indexId = options.rowIndex;
          row.indexId = uniqueIndexId;
          uniqueIndexId ++;

          row.onclick = function() { 
              selectRecordRow(this); 
              // Populate data in details section.
              //alert("Populating record details now.." + this.indexId);
              var relationships = document.getElementsByName("relationship_id");
              if(relationships != null) {
                   //alert("relationship id " + relationships[this.indexId].value);
                   populateRelationshipDetails (cachedSearchResults[this.indexId].id, this.indexId);
              }
          };
          return row;
        },
        cellCreator:function(options) {
          var td = document.createElement("td");
          if(options.cellNum==0) td.align="center";// alert(options.cellNum);
          return td;
        },
        escapeHtml:false
      });  
      refreshRelationshipsListingButtonsPalette ();      
}



function populateRelationshipDetails(relationshipId, indexNum) {
    //alert("populating relationship details for relationship id: " + relationshipId);
    var sourceDomain = cachedSearchResults[indexNum].sourceDomain;
    var targetDomain = cachedSearchResults[indexNum].targetDomain;
    var relationshipDefName = cachedSearchResults[indexNum].name;
    
    var sourcePane = dijit.byId("sourceRecordDetailsTitlePane"); 
    var targetPane = dijit.byId("targetRecordDetailsTitlePane");
    if(cachedSearchResults != null && cachedSearchResults[indexNum]!= null) {
        sourcePane.attr("title",cachedSearchResults[indexNum].sourceHighLight);
        targetPane.attr("title",cachedSearchResults[indexNum].targetHighLight);
        var relationshipDef = cachedRelationshipDefs[relationshipDefName];
    
        var relationshipRecordPane = dijit.byId("relationshipRecordDetailsPane"); 
        var strRecordPaneTitleHTML = "<table cellspacing='0' cellpadding='0'><tr><td>Relationship Attributes: ";
        strRecordPaneTitleHTML += cachedSearchResults[indexNum].sourceHighLight;
        strRecordPaneTitleHTML += " " + relationshipDef.name ;
        strRecordPaneTitleHTML += "</td><td>" + getRelationshipDefDirectionIcon(relationshipDef.biDirection) + "</td><td>";
        strRecordPaneTitleHTML += " " + cachedSearchResults[indexNum].targetHighLight;
        strRecordPaneTitleHTML += "</td></tr></table>"
        relationshipRecordPane.attr("title", strRecordPaneTitleHTML);
    }

    var relationshipView = {name:relationshipDefName, id:relationshipId, sourceDomain:sourceDomain, targetDomain:targetDomain}; 
    RelationshipHandler.getRelationship (relationshipView, populateRelationshipDetails_Callback);
}

var cachedRelationshipRecordDetails ;
function populateRelationshipDetails_Callback (data) {
    var summaryFieldCount = 0;
    //alert("Source summary fields: " + data.sourceRecord.name + " : " + summaryFields[data.sourceRecord.name]); 
    //alert("Targer summary fields: " + data.targetRecord.name + " : " + summaryFields[data.targetRecord.name]); 
    // For testing, hardcoding..
    summaryFields[data.sourceRecord.name].push("FirstName");
    summaryFields[data.targetRecord.name].push("FirstName");
    
    // Cache the relationship record details for further use.
    cachedRelationshipRecordDetails = data;
    var fieldName, fieldValue;
     var recordFieldRow,isSummaryField;
    // Populate source record details
    var sourceRecordDetails =  data.sourceRecord.attributes;
    dwr.util.removeAllRows("sourceRecordInSummary");    
    dwr.util.removeAllRows("sourceRecordInDetail");
    summaryFieldCount = 0;
    for(i=0; i<sourceRecordDetails.length; i++) {
        fieldName = sourceRecordDetails[i].name;
        fieldValue = sourceRecordDetails[i].value;
        
        //alert(i + " : " +  fieldName + " : " + fieldValue );
        var sourceSummaryTable = document.getElementById('sourceRecordInSummary');
        var sourceDetailTable = document.getElementById('sourceRecordInDetail');
        
        recordFieldRow = sourceDetailTable.insertRow(i);
        recordFieldRow.insertCell(0);recordFieldRow.cells[0].className = "label";
        recordFieldRow.insertCell(1);recordFieldRow.cells[1].className = "data";
        recordFieldRow.cells[0].innerHTML = fieldName + ": ";
        recordFieldRow.cells[1].innerHTML = fieldValue;

        //alert(summaryFields[data.sourceRecord.name].contains(fieldName));
        isSummaryField = summaryFields[data.sourceRecord.name].contains(fieldName);
        if( isSummaryField ) {
          recordFieldRow= sourceSummaryTable.insertRow(summaryFieldCount);
          recordFieldRow.insertCell(0);recordFieldRow.cells[0].className = "label";
          recordFieldRow.insertCell(1);recordFieldRow.cells[1].className = "data";
          recordFieldRow.cells[0].innerHTML = fieldName + ": ";
          recordFieldRow.cells[1].innerHTML = fieldValue;
          summaryFieldCount ++;
        }
    }
    
    // Populate target record Details
    var targetRecordDetails =  data.targetRecord.attributes;
    dwr.util.removeAllRows("targetRecordInSummary");    
    dwr.util.removeAllRows("targetRecordInDetail");
    summaryFieldCount = 0;
    for(i=0; i<targetRecordDetails.length; i++) {
        fieldName = targetRecordDetails[i].name;
        fieldValue = targetRecordDetails[i].value;
        
        //alert(i + " : " +  fieldName + " : " + fieldValue );
        var targetSummaryTable = document.getElementById('targetRecordInSummary');
        var targetDetailTable = document.getElementById('targetRecordInDetail');
        
        recordFieldRow = targetDetailTable.insertRow(i);
        recordFieldRow.insertCell(0);recordFieldRow.cells[0].className = "label";
        recordFieldRow.insertCell(1);recordFieldRow.cells[1].className = "data";
        recordFieldRow.cells[0].innerHTML = fieldName + ": ";
        recordFieldRow.cells[1].innerHTML = fieldValue;

        //alert(summaryFields[data.sourceRecord.name].contains(fieldName));
        isSummaryField = summaryFields[data.targetRecord.name].contains(fieldName);
        if( isSummaryField ) {
          recordFieldRow= targetSummaryTable.insertRow(summaryFieldCount);
          recordFieldRow.insertCell(0);recordFieldRow.cells[0].className = "label";
          recordFieldRow.insertCell(1);recordFieldRow.cells[1].className = "data";
          recordFieldRow.cells[0].innerHTML = fieldName + ": ";
          recordFieldRow.cells[1].innerHTML = fieldValue;
          summaryFieldCount ++;
        }
    }
    // Populate relationship attributes.
    var relationshipDef = cachedRelationshipDefs[data.relationshipRecord.name];

    
    var startDate = getBoolean(relationshipDef.startDate);
    var endDate = getBoolean(relationshipDef.endDate);
    var purgeDate = getBoolean(relationshipDef.purgeDate);
    var customAttributes = relationshipDef.extendedAttributes;
    var recordCustomAttributes = data.relationshipRecord.attributes;
    
    if(customAttributes != null && customAttributes.length > 0) {
        createCustomAttributesSection ("editCustomAttributesTable", customAttributes, "edit_custom", true);
        populateCustomAttributesValues (customAttributes, recordCustomAttributes, "edit_custom");
        document.getElementById("editCustomAttributesDiv").style.display = "";
    } else {
        document.getElementById("editCustomAttributesDiv").style.display = "none";
    }
    //data.relationshipRecord.endDate = "12/20/2008";
    //data.relationshipRecord.purgeDate = "12/30/2008";
    if(startDate==true || endDate==true || purgeDate == true ){ 
        createPredefinedAttributesSection ("editPredefinedAttributesTable", relationshipDef,"edit_predefined", true);
        populatePredefinedAttributesValues (relationshipDef, data.relationshipRecord, "edit_predefined");
        document.getElementById("editPredefinedAttributesDiv").style.visibility="visible"
        document.getElementById("editPredefinedAttributesDiv").style.display="";
    } else{
        document.getElementById("editPredefinedAttributesDiv").style.visibility="hidden"
        document.getElementById("editPredefinedAttributesDiv").style.display="none";
    }
    
    //alert(document.getElementById("edit_salary"));
    return;
}
function deleteRelationships() {
    var relationships = document.getElementsByName("relationship_id")
    
    if(relationships == null || relationships.length <=0 ) return;
    for(i=0; i<relationships.length; i++) {
        if(relationships[i].checked) {
            alert("deleting relationship with id: " + relationships[i].value );
            // Make DWR call to delete relationship
            RelationshipHandler.deleteRelationship({id: relationships[i].value}, deleteRelationshipCB);
        }
    }
    
}
function deleteRelationshipCB (data){ 
    //alert("Deleted " + data);
}
/*
 * Scripts for Main (listing, details) screen <END>
 */



/*
 * Scripts for Add Relationship screen <START>
 */ 
function loadAddSearchCriteria(){
    var selectedsourceDomain = currentSelectedSourceDomain;
    var selectedTargetDomain = currentSelectedTargetDomain;
    RelationshipDefHandler.getDomainSearchCriteria(selectedsourceDomain, addSourceDomainCriteria);
    RelationshipDefHandler.getDomainSearchCriteria(selectedTargetDomain, addTargetDomainCriteria);
    
}

function addSourceDomainCriteria(data){
     dwr.util.removeAllOptions("add_source_criteria");
     for (var searchType in data)  {
          var opt =  document.createElement("option");
          opt.text = searchType;
          opt.value = searchType;
          document.getElementById('add_source_criteria').options.add(opt);
      }
     var tempSourceSearchType = document.getElementById('add_source_criteria').value;
     var tempSourceDomain = currentSelectedSourceDomain;
     RelationshipDefHandler.getSearchTypeCriteria(tempSourceDomain,tempSourceSearchType,sourceSearchTypeFields);
}

function addTargetDomainCriteria(data){
     dwr.util.removeAllOptions("add_target_criteria");
     for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
        document.getElementById('add_target_criteria').options.add(opt);
     }
    var tempTargetSearchType = document.getElementById('add_target_criteria').value;
    var tempTargetDomain = currentSelectedTargetDomain;
    RelationshipDefHandler.getSearchTypeCriteria(tempTargetDomain,tempTargetSearchType,targetSearchTypeFields);  
}

function getSourceSearchFields(searchTypeId){
      var sourceDomain = currentSelectedSourceDomain;
      var searchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(sourceDomain, searchType,sourceSearchTypeFields);
}
  
function sourceSearchTypeFields(data){
    dwr.util.removeAllRows("add_search_source_fields");
    // create a hidden field, put the querybuilder value in it. hidden field name=addSourceQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "addSourceQueryBuilder";
    hiddenField.name = "addSourceQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('add_search_source_fields').appendChild(hiddenField);
    var count = 0;
     var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('add_search_source_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
             var field ;//= document.createElement("input");
             try{
                 field = document.createElement('<input type="text" name="addSourceSearchFieldName" />');
              }catch(err){
                 field = document.createElement("input");
              }
             field.type="text";
             field.size="5";
             field.name="addSourceSearchFieldName";
             field.domainFieldName = fieldCfg.name;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name; 
             row.cells[1].appendChild(field); 
         }
        
     }
}

function getTargetSearchFields(searchTypeId){
    
      var targetDomain = currentSelectedTargetDomain;
      var targetsearchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(targetDomain, targetsearchType,targetSearchTypeFields);
}

function targetSearchTypeFields(data){
    var count = 0;
    dwr.util.removeAllRows("add_search_target_fields");
    // create a hidden field, put the querybuilder value in it. hidden field name=addTargetQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "addTargetQueryBuilder";
    hiddenField.name = "addTargetQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('add_search_target_fields').appendChild(hiddenField);
    
    var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('add_search_target_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1); 
             var field ; //= document.createElement("input");
             try{
                  field = document.createElement('<input type="text" name="addTargetSearchFieldName" />');
             }catch(err){
                  field = document.createElement("input");
             }
             field.type="text";
             field.size="5";
             field.name="addTargetSearchFieldName";
             field.domainFieldName = fieldCfg.name ;
             field.style.width="100px";
             row.cells[0].innerHTML = fieldCfg.name; 
             row.cells[1].appendChild(field); 
         }
     }
}

function addSourceDomainSearch() {
    var selectedSourceDomain = currentSelectedSourceDomain;
    var domainSearch = {name:selectedSourceDomain}; 
    var addSourceSearchFieldNames = document.getElementsByName('addSourceSearchFieldName');
    var qBuilder = document.getElementById("addSourceQueryBuilder").value;
    domainSearch["type"] = qBuilder; // put search type.
    var domainAttributes = [];
    for(i=0;i<addSourceSearchFieldNames.length;i++){
        var tempFieldName = addSourceSearchFieldNames[i].domainFieldName;
        var tempFieldValue = addSourceSearchFieldNames[i].value ;
        var tempMap = {} ;
        tempMap[tempFieldName] = tempFieldValue;
        //alert(tempFieldName);
        domainAttributes.push( tempMap );
    }
    domainSearch.attributes = domainAttributes;
    RelationshipHandler.searchEnterprises (domainSearch, addSourceSearchResults);
}

var cachedSourceDomainSearchResults = null; // to cache the search results for SOURCE domain (Add dialog)
var cachedTaretDomainSearchResults = null; // to cache the search results for TARGET domain (Add dialog)

function addSourceSearchResults(data) {
    cachedSourceDomainSearchResults = []; // clear the cache array.
    for(i=0; i<data.length; i++) {
        cachedSourceDomainSearchResults.push(data[i]);
        // For testing pagination, adding more records 
        cachedSourceDomainSearchResults.push(data[i]);cachedSourceDomainSearchResults.push(data[i]);
        cachedSourceDomainSearchResults.push(data[i]);cachedSourceDomainSearchResults.push(data[i]);
        cachedSourceDomainSearchResults.push(data[i]);cachedSourceDomainSearchResults.push(data[i]);
    }
    if(data == null){
       document.getElementById('sourceResultsFailure').style.visibility='visible'; 
       document.getElementById('sourceResultsFailure').style.display='block'; 
       document.getElementById('byrel_addSourceResults').style.visibility='hidden'; 
       document.getElementById('byrel_addSourceResults').style.display='none';   
    }else{
       document.getElementById('sourceResultsSuccess').style.visibility='visible'; 
       document.getElementById('sourceResultsSuccess').style.display='block'; 
       document.getElementById('byrel_addSourceResults').style.visibility='visible'; 
       document.getElementById('byrel_addSourceResults').style.display='block';   
       
        addSourceSearchResults_Display (1, intDomainSearchResultsItemsPerPage);
        var paginator = dijit.byId("add_sourceDomainSearchPaginator");
        paginator.currentPage = 1;
        paginator.totalPages = Math.ceil(cachedSourceDomainSearchResults.length / intDomainSearchResultsItemsPerPage) ;
        paginator.refresh();  
    
       addSourceResultsLength = document.getElementsByName("addSourceCheckBox").length;
   }
   enableByRelAddButton();
}
function addSourceSearchResults_Display_Refresh (currentPage) {
    addSourceSearchResults_Display(currentPage, intDomainSearchResultsItemsPerPage);
}
function addSourceSearchResults_Display (currentPage, itemsPerPage) {
    data = cachedSourceDomainSearchResults;
    dwr.util.removeAllRows("AddSource_TableId");
    var fieldsToShowInSearchResults = searchResultsFields[currentSelectedSourceDomain];
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
          var header = document.getElementById('AddSource_TableId').insertRow(i);
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
      var dataRow = document.getElementById('AddSource_TableId').insertRow(i+1);
      dataRow.insertCell(columnCount);
      var chkbox ; //=  document.createElement("input");
      try{
          chkbox = document.createElement('<input type="checkbox" name="addSourceCheckBox" />');
      }catch(err){
          chkbox = document.createElement("input");
      }
      chkbox.type = "checkbox";
      chkbox.name = "addSourceCheckBox";
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

function addTargetDomainSearch() {
    var selectedTargetDomain = currentSelectedTargetDomain;
    var domainSearch = {name:selectedTargetDomain}; // need to add more parameters once done with search criteria section based on fieldconfig etc.,
    var addTargetSearchFieldNames = document.getElementsByName('addTargetSearchFieldName');
    var qBuilder = document.getElementById("addTargetQueryBuilder").value;
    domainSearch["type"] = qBuilder;
    var domainAttributes = [];
    for(i=0;i<addTargetSearchFieldNames.length;i++){
        var tempFieldName = addTargetSearchFieldNames[i].domainFieldName;
        var tempFieldValue = addTargetSearchFieldNames[i].value ;
        var tempMap = {} ;
        tempMap[tempFieldName] = tempFieldValue;
        //alert(tempFieldName);
        domainAttributes.push( tempMap );
    }
    domainSearch.attributes = domainAttributes;
    RelationshipHandler.searchEnterprises (domainSearch, addTargeSearchResults);
}

function addTargeSearchResults(data) {
    cachedTaretDomainSearchResults = [];
    for(i=0; i<data.length; i++) {
        cachedTaretDomainSearchResults.push(data[i]);
        // For testing pagination, adding more records 
        cachedTaretDomainSearchResults.push(data[i]);cachedTaretDomainSearchResults.push(data[i]);
    }
    if(data == null){
       document.getElementById('targetResultsFailure').style.visibility='visible'; 
       document.getElementById('targetResultsFailure').style.display='block'; 
       document.getElementById('byrel_addTargetResults').style.visibility='hidden'; 
       document.getElementById('byrel_addTargetResults').style.display='none';
    }else{
       document.getElementById('targetResultsSuccess').style.visibility='visible'; 
       document.getElementById('targetResultsSuccess').style.display='block';
       document.getElementById('byrel_addTargetResults').style.visibility='visible'; 
       document.getElementById('byrel_addTargetResults').style.display='block';      
       
      addTargeSearchResults_Display(1, intDomainSearchResultsItemsPerPage);
      var paginator = dijit.byId("add_targetDomainSearchPaginator");
      paginator.currentPage = 1;
      paginator.totalPages = Math.ceil(cachedTaretDomainSearchResults.length / intDomainSearchResultsItemsPerPage) ;
      paginator.refresh();  

      addTargetResultsLength = document.getElementsByName("addTargetCheckBox").length;
   }
   enableByRelAddButton();
}
function addTargeSearchResults_Display_Refresh (currentPage) {
    addTargeSearchResults_Display (currentPage, intDomainSearchResultsItemsPerPage);
}
function addTargeSearchResults_Display(currentPage, itemsPerPage) {
    data = cachedTaretDomainSearchResults;
    dwr.util.removeAllRows("AddTarget_TableId");     
    var fieldsToShowInSearchResults = searchResultsFields[currentSelectedTargetDomain];   
    fieldsToShowInSearchResults.push("LastName"); // For testing purpose only. need to be removed later.
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
    for(i =0;i<resultsToShow.length;i++){
        if(i==0){
          columnCount = 0;
          var header = document.getElementById('AddTarget_TableId').insertRow(i);
          header.className = "header";
          header.insertCell(columnCount++);

          for(j=0; j<resultsToShow[i].attributes.length; j++) {
            if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
            header.insertCell(columnCount);   
            header.cells[columnCount].className = "label";
            header.cells[columnCount].innerHTML  = resultsToShow[i].attributes[j].name;
            columnCount ++;
          }
        }
        columnCount = 0;
        var dataRow = document.getElementById('AddTarget_TableId').insertRow(i+1);
        dataRow.insertCell(columnCount);
        var chkbox ; //=  document.createElement("input");
        try{
              chkbox = document.createElement('<input type="checkbox" name="addTargetCheckBox" />');
         }catch(err){
              chkbox = document.createElement("input");
         }

        chkbox.type = "checkbox";
        chkbox.name = "addTargetCheckBox";
        chkbox.value = resultsToShow[i].EUID;
        dataRow.cells[columnCount].appendChild (chkbox);
        columnCount ++;
        for(j=0; j<resultsToShow[i].attributes.length; j++) {
          if(! fieldsToShowInSearchResults.contains (resultsToShow[i].attributes[j].name)) continue;
          dataRow.insertCell(columnCount);
          dataRow.cells[columnCount].innerHTML = resultsToShow[i].attributes[j].value;
          columnCount++;
       }
    }    
}

function ByRelAddRelationship(){
    
    var startDateField = document.getElementById('add_predefined_startDate');
    var endDateField = document.getElementById('add_predefined_endDate');
    var purgeDateField = document.getElementById('add_predefined_purgeDate');
    var startDate,endDate,purgeDate;
    
   var SourceDomain = currentSelectedSourceDomain;
   var TargetDomain = currentSelectedTargetDomain;
   var RelationshipDefName = currentSelectedRelationshipDef;
   var relationshipCustomAttributes = [];
   var relationshipDef = cachedRelationshipDefs[RelationshipDefName];
    
   var startDateRequireField = (getBoolean(relationshipDef.startDateRequired)); 
   var endDateRequireField = (getBoolean(relationshipDef.endDateRequired)); 
   var purgeDateRequireField = (getBoolean(relationshipDef.purgeDateRequired));
   var s=0;
   var t=0;
    
    for(cc =0; cc < relationshipDef.extendedAttributes.length; cc++) {
      var attributeName = relationshipDef.extendedAttributes[cc].name;
      var attributeId = document.getElementById("add_custom_" + relationshipDef.extendedAttributes[cc].name);
      var attributeValue =  document.getElementById("add_custom_" + relationshipDef.extendedAttributes[cc].name).value;
      var attributeType = relationshipDef.extendedAttributes[cc].dataType;
        if(getBoolean(relationshipDef.extendedAttributes[cc].isRequired)) {
          if( isEmpty (attributeValue) ) {
              alert(getMessageForI18N("enterValueFor") + " " + attributeName +getMessageForI18N("period") );
              attributeId.focus();
              return ;
          }
      }
      if( ! isValidCustomAttribute( attributeType, attributeValue) ) {
          alert(attributeValue + " " +getMessageForI18N("isnotavalidvaluefor")+ " " +attributeName + " " +getMessageForI18N("attribute") +getMessageForI18N("period"));
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
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("effectiveFrom")+getMessageForI18N("period"));
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
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("effectiveTo")+getMessageForI18N("period"));
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
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("purgeDate")+getMessageForI18N("period"));
              purgeDateField.focus();
              return ;
           } 
          }    
     }
   var sourceCheckBox = false; 
   var targerCheckBox = false;
   var sourceCheckedArray = document.getElementsByName("addSourceCheckBox"); 
   var targetCheckedArray = document.getElementsByName("addTargetCheckBox");
   
    for(i=0;i<sourceCheckedArray.length; i++) {
        if(sourceCheckedArray[i].checked) {
            s++;
            sourceCheckBox = true;
            var sourceRecordEUID = sourceCheckedArray[i].value;
            for(j=0;j<targetCheckedArray.length; j++) {
              if(targetCheckedArray[j].checked) {
                  t++;
                targerCheckBox = true;
                var targetRecordEUID = targetCheckedArray[j].value;
                var newRelationshipRecord = {};
                newRelationshipRecord.name = RelationshipDefName;
                newRelationshipRecord.sourceDomain = SourceDomain;
                newRelationshipRecord.sourceEUID = sourceRecordEUID;
                newRelationshipRecord.targetDomain = TargetDomain;
                newRelationshipRecord.targetEUID = targetRecordEUID;
                
                newRelationshipRecord.startDate = startDate;
                newRelationshipRecord.endDate = endDate;
                newRelationshipRecord.purgeDate = purgeDate;
                newRelationshipRecord.attributes = relationshipCustomAttributes ; 
                RelationshipHandler.addRelationship(newRelationshipRecord,addRelationshipCB);
              }    
            }     
        }       
   } 
    if(s == 0) {
       alert(getMessageForI18N("SelectAtleastOneRecord") + " " + getMessageForI18N("fromSourceDomain"));    
    }else if(t == 0 ){
        alert(getMessageForI18N("SelectAtleastOneRecord") + " " + getMessageForI18N("fromTargetDomain"));    
    }        
}
function addRelationshipCB(data) {
    alert("New relationship record added, id is : " + data);
    hideByRelAddDialog();
    
    // refresh the relationship listing 
    searchRelationships();
}


function updateRelationship () {
    
    var updateRelaltionshipRecord = {};
    var updateRelationshipCustomAttributes = [];
    
    var sourceDomain = cachedRelationshipRecordDetails.sourceRecord.name;
    var sourceEUID = cachedRelationshipRecordDetails.sourceRecord.EUID;
    var targetDomain = cachedRelationshipRecordDetails.targetRecord.name;
    var targetEUID = cachedRelationshipRecordDetails.targetRecord.EUID;
    var relationshipDef = cachedRelationshipRecordDetails.relationshipRecord.name;
    var relationshipRecordId = cachedRelationshipRecordDetails.relationshipRecord.id;
    var relationshipPredefined = cachedRelationshipDefs[relationshipDef];
    var relationshipDefAttributes = cachedRelationshipDefs[relationshipDef].extendedAttributes;
    
    // Get attributes. Predefined & Custom from edit screen
    var startDateField = document.getElementById('edit_predefined_startDate');
    var endDateField = document.getElementById('edit_predefined_endDate');
    var purgeDateField = document.getElementById('edit_predefined_purgeDate');
    var startDate,endDate,purgeDate;
    
    // Get Predefined Required Fileds
    var startDateRequire = (getBoolean(relationshipPredefined.startDateRequired));
    var endDateRequire = (getBoolean(relationshipPredefined.endDateRequired));
    var purgeDateRequire = (getBoolean(relationshipPredefined.purgeDateRequired));
    
    for(i =0; i < relationshipDefAttributes.length; i++) {
      var attributeName = relationshipDefAttributes[i].name;
      var attributeId = document.getElementById("edit_custom_" + relationshipDefAttributes[i].name);
      var attributeValue =  document.getElementById("edit_custom_" + relationshipDefAttributes[i].name).value;
      var attributeType = relationshipDefAttributes[i].dataType;
      
      if(getBoolean(relationshipDefAttributes[i].isRequired)) {
          if( isEmpty (attributeValue) ) {
              alert(getMessageForI18N("enterValueFor") + " " + attributeName +getMessageForI18N("period"));
              attributeId.focus();
              return ;
          }
      }
      if( ! isValidCustomAttribute( attributeType, attributeValue) ) {
          alert(attributeValue + " " +getMessageForI18N("isnotavalidvaluefor")+ " " +attributeName + " " +getMessageForI18N("attribute") +getMessageForI18N("period"));
          attributeId.focus();
          return;
      }
      updateRelationshipCustomAttributes.push( {attributeName : attributeValue} );
     }
     
    if(startDateField != null)
     {   
           startDate =  document.getElementById('edit_predefined_startDate').value;
           if(startDateRequire == true){
               if( isEmpty (startDate) ) {
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("effectiveFrom")+getMessageForI18N("period"));
              startDateField.focus();
              return ;
           }
         }    
     }
     if(endDateField != null)
     {   
           endDate =  document.getElementById('edit_predefined_endDate').value;
           if(endDateRequire == true){
               if( isEmpty (endDate) ) {
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("effectiveTo")+getMessageForI18N("period"));
              endDateField.focus();
              return ;
            }
         }      
     }
     if(purgeDateField != null)
     {
           purgeDate =  document.getElementById('edit_predefined_purgeDate').value;
           if(purgeDateRequire == true){
               if( isEmpty (purgeDate) ) {
              alert(getMessageForI18N("enterValueFor") + " " + getMessageForI18N("purgeDate")+getMessageForI18N("period"));
              purgeDateField.focus();
              return ;
            }
          }      
     }
    // Make DWR API Call to updateRelationship
      updateRelaltionshipRecord.name = relationshipDef;
      updateRelaltionshipRecord.id = relationshipRecordId; 
      updateRelaltionshipRecord.sourceDomain = sourceDomain;
      updateRelaltionshipRecord.sourceEUID = sourceEUID;
      updateRelaltionshipRecord.targetDomain = targetDomain;
      updateRelaltionshipRecord.targetEUID = targetEUID;

      updateRelaltionshipRecord.startDate = startDate;
      updateRelaltionshipRecord.endDate = endDate;
      updateRelaltionshipRecord.purgeDate = purgeDate;
      updateRelaltionshipRecord.attributes = updateRelationshipCustomAttributes ;

      RelationshipHandler.updateRelationship(updateRelaltionshipRecord,updateRelationshipCB);

}

function updateRelationshipCB(data){
    alert("Relationship Attributes Updated......   "+data)
}

function isValidCustomAttribute(attributeType, attributeValue) {
    // Implement validation based on type.
    if(isEmpty(attributeValue)) return true;
     if(attributeType == "date" ||attributeType == "boolean" ){
         return true;
      }else if(attributeType == "int" ) {
          if( ! isInteger(attributeValue)){
              return false;
          }
      } else if(attributeType == "float" ) {
         if( isNaN(attributeValue)){
              return false;
          }          
      }else if(attributeType == "char"){
          if(attributeValue.length == 1){
              return true;
          }
          else{
              return false;
          }
      } else { 
          return true; 
      }
      return true;
}
 
function enableByRelAddButton(){
    if(addSourceResultsLength > 0 && addTargetResultsLength >0 ){
          document.getElementById("ByRelAddRelationshipButton").disabled = false;
    }
}

/*
 * Scripts for Add Relationship screen <END>
 */