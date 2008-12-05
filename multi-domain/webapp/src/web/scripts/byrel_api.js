
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
    document.getElementById('byrel_select_customAttributes').innerHTML="";
    document.getElementById('byrel_select_predefinedAttributes').innerHTML="";
    
    if(data.extendedAttributes.length>0 ){
           var customHeading = document.getElementById('byrel_select_customAttributes').insertRow(CustomrowCount ++);
           customHeading.insertCell(0);
           customHeading.cells[0].innerHTML="Custom Attributes";
           var j=0;
           for(var i=0;i < data.extendedAttributes.length;i++){
             var customContent = document.getElementById('byrel_select_customAttributes').insertRow(CustomrowCount ++);
             customContent.insertCell(0);
             customContent.insertCell(1);
             customContent.cells[0].innerHTML=data.extendedAttributes[i].name;
             var field = document.createElement("input");
             field.type="text";
             field.size="5";
             field.style.width="100px";
             customContent.cells[1].appendChild(field);
           }
           document.getElementById("select_Relationship_CustomAtrributes").style.visibility="visible"
           document.getElementById("select_Relationship_CustomAtrributes").style.display="block";
       }
       else{
           
           document.getElementById("select_Relationship_CustomAtrributes").style.visibility="hidden"
           document.getElementById("select_Relationship_CustomAtrributes").style.display="none";
       }
    if(startDate==true || endDate==true || startDate == true ){ // condition for Predefined Attributes
        var FirstRow = document.getElementById('byrel_select_predefinedAttributes').insertRow(PredefinedrowCount ++);
        FirstRow.insertCell(0);
        FirstRow.cells[0].colSpan='4';
        FirstRow.cells[0].innerHTML="Predefined Attributes";
        document.getElementById("select_Relationship_PredefinedAtrributes").style.visibility="visible"
        document.getElementById("select_Relationship_PredefinedAtrributes").style.display="block";
    }
    else{
        document.getElementById("select_Relationship_PredefinedAtrributes").style.visibility="hidden"
        document.getElementById("select_Relationship_PredefinedAtrributes").style.display="none";
    }  
    if(startDate==true&& endDate==true){ // condition for Start and End dates of Predefined Attributes
        var SecondRow = document.getElementById('byrel_select_predefinedAttributes').insertRow(PredefinedrowCount ++);
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
        
    } else if (startDate == true) { // condition for Start Date of Predefined Attributes
        var SecondRowStart = document.getElementById('byrel_select_predefinedAttributes').insertRow(PredefinedrowCount ++);
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
        
    } else if(endDate == true) { // condition for End Date of Predefined Attributes
        
        var SecondRowEnd = document.getElementById('byrel_select_predefinedAttributes').insertRow(PredefinedrowCount ++);
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
    if(purgeDate==true ){ // condition for purge date of Predefined Attributes
        
        var ThirdRow = document.getElementById('byrel_select_predefinedAttributes').insertRow(PredefinedrowCount ++);
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
  
  function loadSelectSourceSearchTypes(id)   {
      var sourceDomain = document.getElementById(id).value;
      RelationshipDefHandler.getDomainSearchCriteria(sourceDomain, selectSourceSearchTypes);
  }
  
function selectSourceSearchTypes(data)   {
   var searchTypes = new Array();
   var j = 0;
   var fgroups = new Array();
   document.getElementById('select_source_searchtypes').innerHTML='';
   for (var searchType in data)  {
      searchTypes[j++] = searchType;
   }
   var str = new String(searchTypes);
   fgroups = str.split(',');
   for(i=0;i<fgroups.length;i++){
        var opt =  document.createElement("option");
        opt.text = fgroups[i];
        opt.value = fgroups[i];
        document.getElementById('select_source_searchtypes').options.add(opt);
    }
    selectSourceSearchFields('select_source_searchtypes');
}

function loadSelectTargetSearchTypes(id)   {
      var targetDomain = document.getElementById(id).value;
      RelationshipDefHandler.getDomainSearchCriteria(targetDomain, selectTargetSearchTypes);
  }
  
function selectTargetSearchTypes(data)   {
   var searchTypes = new Array();
   var j = 0;
   var fgroups = new Array();
   document.getElementById('select_target_searchtypes').innerHTML='';
   for (var searchType in data)  {
      searchTypes[j++] = searchType;
   }
   var str = new String(searchTypes);
   fgroups = str.split(',');
   for(i=0;i<fgroups.length;i++){
        var opt =  document.createElement("option");
        opt.text = fgroups[i];
        opt.value = fgroups[i];
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
    var fieldGroups = new Array();
    var j = 0;
    var count = 0;
    document.getElementById('select_search_source_fields').innerHTML='';
     for (var fieldGrp in data)  {
        fieldGroups[j++] = fieldGrp;
         for (var fieldCfg in data[fieldGrp])  {
             var row = document.getElementById('select_search_source_fields').insertRow(count++);
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

function selectTargetSearchFields(searchTypeId){
    var taragetDomain = document.getElementById('select_targetDomain').value;
      var searchType = document.getElementById(searchTypeId).value;
      RelationshipDefHandler.getSearchTypeCriteria(taragetDomain, searchType,selectTargetSearchTypeFields);
    
}

function selectTargetSearchTypeFields(data){
    var fieldGroups = new Array();
    var j = 0;
    var count = 0;
    document.getElementById('select_search_target_fields').innerHTML='';
     for (var fieldGrp in data)  {
        fieldGroups[j++] = fieldGrp;
         for (var fieldCfg in data[fieldGrp])  {
             var row = document.getElementById('select_search_target_fields').insertRow(count++);
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
         for (var fieldCfg in data[fieldGrp])  {
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



