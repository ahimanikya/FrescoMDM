
/*
 * API integration scripts for Manage - By Relationship Screens.
 */ 

dwr.engine.setErrorHandler(exceptionHandler);

function exceptionHandler(message) {
    alert("Exception: " + message);
}

/*
 * Scripts for Select/Search screen <START>
 */ 
  function loadDomainsForSearch () {
      DomainHandler.getDomains(updateSelectDomains);
  }
  function updateSelectDomains(data) {
      dwr.util.addOptions("select_sourceDomain", data, "name");
      dwr.util.setValue("select_sourceDomain", data[0].name); 
      dwr.util.addOptions("select_targetDomain", data, "name");
      dwr.util.setValue("select_targetDomain", data[1].name);       
      loadRelationshipDefs();
  }
  function loadRelationshipDefs() {
      var sourceDomain = document.getElementById("select_sourceDomain").value;
      var targetDomain = document.getElementById("select_targetDomain").value;
      RelationshipDefHandler.getRelationshipDefs(sourceDomain, targetDomain, updateRelationshipDefs);
  }
  
  function updateRelationshipDefs(data) {
      dwr.util.removeAllOptions("select_relationshipDefs");
      dwr.util.addOptions("select_relationshipDefs", data, "name");
      //dwr.util.setValue("select_relationshipDefs", data[0].name); 
  }
  
  function loadSearchCriteria(id)   {
      var sourceDomain = document.getElementById(id).value;
      RelationshipDefHandler.getDomainSearchCriteria(sourceDomain, sourceDomainCriteria);
  }
  
function sourceDomainCriteria(data)   {
   var searchTypes = new Array();
   var j = 0;
   var fgroups = new Array();
   for (var searchType in data)  {
      searchTypes[j++] = searchType;
   }
   var str = new String(searchTypes);
   fgroups = str.split(',');
   for(i=0;i<fgroups.length;i++){
        var opt =  document.createElement("option");
        opt.text = fgroups[i];
        opt.value = fgroups[i];
        document.getElementById('select_source_criteria').options.add(opt);
    }
}
  
  
/*
 * Scripts for Select/Search screen <END>
 */

  
  
/*
 * Scripts for Main (listing, details) screen <START>
 */
function searchRelationships() {
    var sourceDomain = document.getElementById("select_sourceDomain").value;
    var targetDomain = document.getElementById("select_targetDomain").value;
    var relationshipDef = document.getElementById("select_relationshipDefs").value;
    
    hideByRelSelectDialog(); // hide the select dialog...
    //alert(sourceDomain + "\n" + targetDomain + "\n" + relationshipDef);

    dijit.byId("selectedSourceDomain").setContent (sourceDomain);
    dijit.byId("selectedRelationshipDef").setContent (relationshipDef);
    dijit.byId("selectedTargetDomain").setContent (targetDomain);
    
    // make DWR call to search for relationships
    // [TBD] need to include search criteria attributes as well.
    var sourceDomainSearch = {name: sourceDomain}; 
    var targetDomainSearch = {name: targetDomain};
    var relationshipDefSearch = {name: relationshipDef};
    
    RelationshipHandler.searchRelationships (sourceDomainSearch, targetDomainSearch, relationshipDefSearch, searchResultsCallback);

}
function searchResultsCallback(data) {
    alert("search results " + data);
    /*for(i=0; i<data.length; i++) {
        alert(data[i].id + " : " + data[i].sourceEUID + " : "  + data[i].sourceHighLight);
    }*/
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
    }
    dwr.util.addRows("relationshipsListing", data, relationListingFuncs, {
        rowCreator:function(options) {
          var row = document.createElement("tr");
          row.onclick = function() { 
              selectRecordRow(this); 
              // Populate data in details section.
              //populateSourceRecordDetails (sourceEUID);
              //populateSourceRecordDetails ();
              alert("Populating record details now..");
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
}
var relationListingFuncs = [
    function(data) { return "<input type='checkbox' align='center' >"; },
    function(data) { return data.sourceHighLight; },
    function(data) { return data.targetHighLight; }

];
/*
 * Scripts for Main (listing, details) screen <END>
 */



/*
 * Scripts for Add Relationship screen <START>
 */ 
function loadAddSearchCriteria(){
    var selectedsourceDomain = document.getElementById('select_sourceDomain').value;
    var selectedTargetDomain = document.getElementById('select_sourceDomain').value;
    RelationshipDefHandler.getDomainSearchCriteria(selectedsourceDomain, addSourceDomainCriteria);
    RelationshipDefHandler.getDomainSearchCriteria(selectedTargetDomain, addTargetDomainCriteria);
    
}

function addSourceDomainCriteria(data){
    
    var searchTypes = new Array();
     var j = 0;
     var fgroups = new Array();
     document.getElementById('add_source_criteria').innerHTML='';
     for (var searchType in data)  {
        searchTypes[j++] = searchType;
     }
     var str = new String(searchTypes);
     fgroups = str.split(',');
     for(i=0;i<fgroups.length;i++){
          var opt =  document.createElement("option");
          opt.text = fgroups[i];
          opt.value = fgroups[i];
          document.getElementById('add_source_criteria').options.add(opt);
      }    
}

function addTargetDomainCriteria(data){
    
    var searchTypes = new Array();
     var j = 0;
     var fgroups = new Array();
     document.getElementById('add_target_criteria').innerHTML='';
     for (var searchType in data)  {
        searchTypes[j++] = searchType;
     }
     var str = new String(searchTypes);
     fgroups = str.split(',');
     for(i=0;i<fgroups.length;i++){
          var opt =  document.createElement("option");
          opt.text = fgroups[i];
          opt.value = fgroups[i];
          document.getElementById('add_target_criteria').options.add(opt);
      }    
}

function getSourceSearchFields(searchTypeId){
      var sourceDomain = document.getElementById('select_sourceDomain').value;
      var searchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(sourceDomain, searchType,sourceSearchTypeFields);
}
  
function sourceSearchTypeFields(data){
    var fieldGroups = new Array();
    var j = 0;
    var count = 0;
    document.getElementById('add_search_source_fields').innerHTML='';
     for (var fieldGrp in data)  {
        fieldGroups[j++] = fieldGrp;
        alert("fieldGrp  ");
         for (var fieldCfg in data[fieldGrp])  {
             alert("fields " + data[fieldGrp][fieldCfg].name);
             var row = document.getElementById('add_search_source_fields').insertRow(count++);
                 row.insertCell(0);
                 row.insertCell(1);
                 row.cells[0].innerHTML = data[fieldGrp][fieldCfg].name;
                 var field = document.createElement("input");
                 field.type="text";
                 field.size="5";
                 field.style.width="100px";
                 row.cells[1].appendChild(field);
         }
        
     }
}

function getTargetSearchFields(searchTypeId){
    
      var targetDomain = document.getElementById('select_targetDomain').value;
      var targetsearchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(targetDomain, targetsearchType,targetSearchTypeFields);
}

function targetSearchTypeFields(data){
    var fieldGroups = new Array();
    var j = 0;
    var count = 0;
    document.getElementById('add_search_target_fields').innerHTML='';
     for (var fieldGrp in data)  {
        fieldGroups[j++] = fieldGrp;
        alert("fieldGrp  ");
         for (var fieldCfg in data[fieldGrp])  {
             alert("fields " + data[fieldGrp][fieldCfg].name);
             var row = document.getElementById('add_search_target_fields').insertRow(count++);
                 row.insertCell(0);
                 row.insertCell(1);
                 row.cells[0].innerHTML = data[fieldGrp][fieldCfg].name;
                 var field = document.createElement("input");
                 field.type="text";
                 field.size="5";
                 field.style.width="100px";
                 row.cells[1].appendChild(field);
         }
     }
}

function addSourceDomainSearch() {
    alert('getting EUIDs');
    var selectedSourceDomain = document.getElementById("select_sourceDomain").value;
    var domainSearch = {name:selectedSourceDomain}; // need to add more parameters once done with search criteria section based on fieldconfig etc.,
    RelationshipHandler.searchEnterprises (domainSearch, addSourceSearchResults);
}

function addSourceSearchResults(data) {
    if(data == null) return;
    for(i =0;i<data.length;i++){
        if(i==0){
          var header = document.getElementById('AddSource_TableId').insertRow(i);
          header.insertCell(0);
          for(j=0; j<data[i].attributes.length; j++) {
            header.insertCell(j+1);   
            header.style.backgroundColor = '#f4f3ee';
            header.cells[j+1].innerHTML  = data[i].attributes[j].name;
          }
        }
          var dataRow = document.getElementById('AddSource_TableId').insertRow(i+1);
          dataRow.insertCell(0);
          var chkbox =  document.createElement("input");
          chkbox.type="checkbox";
          dataRow.cells[0].appendChild (chkbox);
          for(j=0; j<data[i].attributes.length; j++) {
            dataRow.insertCell(j+1);
            dataRow.cells[j+1].innerHTML = data[i].attributes[j].value;
        }
    } 
}

function addTargetDomainSearch() {
    alert('getting EUIDs');
    var selectedTargetDomain = document.getElementById("select_targetDomain").value;
    var domainSearch = {name:selectedTargetDomain}; // need to add more parameters once done with search criteria section based on fieldconfig etc.,
    RelationshipHandler.searchEnterprises (domainSearch, addTargeSearchResults);
}

function addTargeSearchResults(data) {
    if(data == null) return;
    for(i =0;i<data.length;i++){
        if(i==0){
          var header = document.getElementById('AddTarget_TableId').insertRow(i);
          header.insertCell(0);
          for(j=0; j<data[i].attributes.length; j++) {
            header.insertCell(j+1);   
            header.style.backgroundColor = '#f4f3ee';
            header.cells[j+1].innerHTML  = data[i].attributes[j].name;
          }
        }
          var dataRow = document.getElementById('AddTarget_TableId').insertRow(i+1);
          dataRow.insertCell(0);
          var chkbox =  document.createElement("input");
          chkbox.type="checkbox";
          dataRow.cells[0].appendChild (chkbox);
          for(j=0; j<data[i].attributes.length; j++) {
            dataRow.insertCell(j+1);
            dataRow.cells[j+1].innerHTML = data[i].attributes[j].value;
        }
    } 
}

/*
 * Scripts for Add Relationship screen <END>
 */



