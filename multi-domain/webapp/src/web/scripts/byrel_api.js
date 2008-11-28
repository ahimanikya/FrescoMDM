
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
function test() {
    alert('getting EUIDs');
    var domainSearch = {name:"Person"}; // need to add more parameters once done with search criteria section based on fieldconfig etc.,
    RelationshipHandler.searchEnterprises (domainSearch, testCallback);
}
function testCallback(data) {
    if(data == null) return;
    for(i =0;i<data.length;i++) 
        alert(i + " EUID: " + data[i].EUID + " : " + data[i].attributes[0].name + " : " + data[i].attributes[0].value);
}

/*
 * Scripts for Add Relationship screen <END>
 */



