
/*
 * API integration scripts for Manage - Hierarchy Screens.
 */ 


function loadSelectHierarchyDomains() {
    DomainHandler.getDomains( updateDomains);
}
function updateDomains(data) {
    dwr.util.addOptions("select_hierarchy_domain", data, "name");        
    dwr.util.setValue("select_hierarchy_domain", data[0].name);     
    //loadSelectHierarchyDefs();
	loadSelectHierarchySearch("select_hierarchy_domain");
}
/*
function loadSelectHierarchyDefs() {
    var selectedDomain=document.getElementById("select_hierarchy_domain").value;
    HierarchyDefHandler.getHierarchyDefs(selectedDomain, updateSelectHierarchyDefs);
}
function updateSelectHierarchyDefs (data) {
    dwr.util.addOptions("load_select_hierarchies", data, "name");        
    dwr.util.setValue("load_select_hierarchies", data.name);
       // refreshHierarchyDefsButtonsPalette();
} */
function loadSelectHierarchySearch(id){
	var currentDomain = document.getElementById(id).value;
    RelationshipDefHandler.getDomainSearchCriteria(currentDomain, loadSelectHierarchySearchTypes);
}
function loadSelectHierarchySearchTypes(data){
	dwr.util.removeAllOptions("select_hierarchy_searchtypes"); 
    for (var searchType in data)  {
        var opt =  document.createElement("option");
        opt.text = searchType;
        opt.value = searchType;
		opt.title = searchType;
        document.getElementById('select_hierarchy_searchtypes').options.add(opt);
     }
     selectHierarchySearchFields('select_hierarchy_searchtypes');
}
function selectHierarchySearchFields(searchTypeId){
    var selectDomain = document.getElementById('select_hierarchy_domain').value;
    var searchType = document.getElementById(searchTypeId).value;
    RelationshipDefHandler.getSearchTypeCriteria(selectDomain, searchType,showSelectHierarchySearchFields);
}
function showSelectHierarchySearchFields(data){
	dwr.util.removeAllRows("select_hierarchy_search_fields");
    var count = 0;
	var guiTypeStr;
    // create a hidden field, put the querybuilder value in it. hidden field name=selectHierarchyQueryBuilder.
    hiddenField = document.createElement("input");
    hiddenField.type = "hidden";
    hiddenField.id = "selectHierarchyQueryBuilder";
    hiddenField.name = "selectHierarchyQueryBuilder";
    hiddenField.value = data.queryBuilder;
    document.getElementById('select_hierarchy_search_fields').appendChild(hiddenField);
    var fieldGroups = data.fieldGroups;
     for (var fieldGrp in fieldGroups)  {
		var descriptionRow = document.getElementById('select_hierarchy_search_fields').insertRow(count++);
		descriptionRow.insertCell(0);
		descriptionRow.cells[0].innerHTML = "<b>"+ fieldGrp + "</b>";
        for(i=0; i<fieldGroups[fieldGrp].length; i++) {
             fieldCfg = fieldGroups[fieldGrp][i];
             var row = document.getElementById('select_hierarchy_search_fields').insertRow(count++);
             row.insertCell(0);
             row.insertCell(1);
			 guiTypeStr = fieldCfg.guiType;
			 if(guiTypeStr.toLowerCase() == "textbox"){
             var field;
             try{
                 field = document.createElement('<input type="text" name="selectHierarchySearchFieldName" />');
             }catch(err){
                 field = document.createElement("input");
             }
             field.type="text";
             field.size = "20";
			 field.maxlength = fieldCfg.maxLength;
             field.name="selectHierarchySearchFieldName";
			 field.title =  fieldCfg.displayName;
             field.domainFieldName = fieldCfg.displayName;
             row.cells[0].innerHTML = fieldCfg.displayName;
             row.cells[1].appendChild(field);
			 }
         }
     }
}
 function m_hierarchy_showSelectDialog () {
	 //alert("Not yet implemented");
	 var selectDialog = dijit.byId("select_hierarchy");
    if(selectDialog != null)
      selectDialog.show();
 }

function hideSelectHierarchyDialog () {
    dijit.byId('select_hierarchy').hide();
}