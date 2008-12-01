
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
}
/*
 * Scripts for Main (listing, details) screen <END>
 */



/*
 * Scripts for Add Relationship screen <START>
 */ 

function addSourceDomainSearch() {
    alert('getting EUIDs');
    var selectedSourceDomain = document.getElementById("select_sourceDomain").value;
    var domainSearch = {name:selectedSourceDomain}; // need to add more parameters once done with search criteria section based on fieldconfig etc.,
    RelationshipHandler.searchEnterprises (domainSearch, addSourceDomainSearchCallback);
}
function addSourceDomainSearchCallback(data) {
    if(data == null) return;
    for(i =0;i<data.length;i++){
      alert(i + " EUID: " + data[i].EUID + " : " + data[i].attributes[0].name + " : " + data[i].attributes[0].value);
        
        if(i==0){
            alert("hello");
          // Create header.
          var header = document.getElementById('AddSource_TableId').insertRow(i);
          header.insertCell(0);
          for(j=0; j<data[i].attributes.length; j++) {
            header.insertCell(j+1);   
            header.style.backgroundColor = '#f4f3ee';
            header.cells[j+1].innerHTML  = data[i].attributes[j].name;
          }
          
        }
          // create data row & columns
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
    RelationshipHandler.searchEnterprises (domainSearch, addTargetDomainSearchCallback);
}
function addTargetDomainSearchCallback(data) {
    if(data == null) return;
    for(i =0;i<data.length;i++){
        alert(i + " EUID: " + data[i].EUID + " : " + data[i].attributes[0].name + " : " + data[i].attributes[0].value);
        if(i==0){
            alert("hello");
          // Create header.
          var header = document.getElementById('AddTarget_TableId').insertRow(i);
          header.insertCell(0);
          for(j=0; j<data[i].attributes.length; j++) {
            header.insertCell(j+1);   
            header.style.backgroundColor = '#f4f3ee';
            header.cells[j+1].innerHTML  = data[i].attributes[j].name;
          }
          
        }
          // create data row & columns
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



