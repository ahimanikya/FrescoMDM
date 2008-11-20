

var selectOptions = new Array("string","date","int","float","char","boolean");

function NewAttribute(tableId, attributesArray, prefixToUse)
{
    var _this = this;
    // Create our row  
    this.Row = document.getElementById(tableId).insertRow(document.getElementById(tableId).rows.length);

    //this.Row.style.className="AttributesListing_dataItem";
    this.Row.insertCell(0);
    this.Row.insertCell(1);
    this.Row.insertCell(2);
    this.Row.insertCell(3);
    this.Row.insertCell(4);
    this.Row.insertCell(5);

    var currentLength = document.getElementById(tableId).rows.length;
    if((currentLength-1) %2)
        this.Row.style.backgroundColor = "#f4f2ed";
    else
        this.Row.style.backgroundColor = "#ffffff";

    this.Row.style.height ="22px";


    // ID field
    this.IdField = document.createElement("input");
    this.IdField.type = "checkbox";
    this.IdField.value = attributesArray.length;
    this.IdField.onclick = function () { refreshCustomAttributesButtonsPalette(eval(prefixToUse+"_attributesArray"),prefixToUse); }
    this.Row.cells[0].appendChild(this.IdField);

    this.AttributeNameField = document.createElement("input");
    this.AttributeNameField.type = "text";
    this.Row.cells[1].appendChild(this.AttributeNameField);

    // Select field for the attribute type
    this.AttributeTypeField = document.createElement("select");
    for(var i=0; i<selectOptions.length; i++) {
        var option = document.createElement("option");
        option.text = selectOptions[i];
        option.value =  selectOptions[i];

        this.AttributeTypeField.options[i] = option;
    }

    this.AttributeTypeField.onchange = function() {
        _this.TypeChanged();
    }
    this.Row.cells[2].appendChild(this.AttributeTypeField);

    // field for the default value
    this.DefaultValueField = document.createElement("input");
    this.DefaultValueField.type = "text";
    this.DefaultValueField.size = 10;
    this.Row.cells[3].appendChild(this.DefaultValueField);

    // Required field
    this.RequiredField = document.createElement("input");
    this.RequiredField.type = "checkbox";
    this.Row.cells[4].appendChild(this.RequiredField);
    this.Row.cells[4].style.textAlign="center";

    // Searchable field
    this.SearchableField = document.createElement("input");
    this.SearchableField.type = "checkbox";
    this.Row.cells[5].appendChild(this.SearchableField);
    this.Row.cells[5].style.textAlign="center";

    // Our delete method
    this.Delete = function()
    {
        // Remove the row from the table
        this.Row.parentNode.removeChild(this.Row);
    }

    this.TypeChanged = function () {
        //alert(' changed ' + this.AttributeTypeField.value);
        this.Row.cells[3].removeChild(this.Row.cells[3].firstChild);
        if ( this.AttributeTypeField.value == "date") {
                this.DefaultValueField = document.createElement("input");
                // This should be date field...
                this.DefaultValueField.type = "text";
                this.DefaultValueField.size = 5; 
                this.DefaultValueField.style.width = "100px";
                this.Row.cells[3].appendChild(this.DefaultValueField);
                var props = {
                      name: "date_attr",
                      promptMessage: "mm/dd/yyyy",
                      invalidMessage: "Invalid date."
                }; 
                var w = new dijit.form.DateTextBox(props, this.DefaultValueField);
        } else if( this.AttributeTypeField.value == "boolean") {
                this.DefaultValueField = document.createElement("select");
                var tOption = document.createElement ("option");
                tOption.text = "true"; tOption.value = "true";
                var fOption = document.createElement ("option");
                fOption.text = "false"; fOption.value = "false";
                this.DefaultValueField.options[0] = tOption;
                this.DefaultValueField.options[1] = fOption;
                this.Row.cells[3].appendChild(this.DefaultValueField);
        } else {
                //this.Row.cells[3].removeChild(this.Row.cells[3].firstChild);
                this.DefaultValueField = document.createElement("input");
                this.DefaultValueField.type = "text";
                this.DefaultValueField.size = 10;
                this.Row.cells[3].appendChild(this.DefaultValueField);
        } 
    }

}

function deleteAttributes(tableId, attributesArray) {
    var anythingChanged = false;
    for(i=0;i<attributesArray.length; i++) {
     attr = attributesArray[i];
        if(attr.IdField.checked) {
            attr.Delete();
            attributesArray.splice(i,1);
            i--;
            anythingChanged = true;
        }
    }
    // Refresh the view to replicate the array contents.
    if(anythingChanged) {
            refreshAttributesView(tableId, attributesArray);
    }
}

function showValues (attributesArray) {
    //alert(attributesArray);
    //alert(attributesArray.length + " attributes");
    var msg = attributesArray.length + " attributes\n\n";
    for(i=0;i<attributesArray.length; i++) {
            attr = attributesArray[i];
            //alert(i + " > " + attr.IdField.value + " " + attr.AttributeNameField.value + " : " +  attr.DefaultValueField.value);
            msg += i + " > " + attr.IdField.value + " " + attr.AttributeNameField.value + " : " +  attr.DefaultValueField.value + "\n";
    }
    alert(msg);
}



function MoveUp(tableId, attributesArray) {
    var anythingChanged = false;

    for(i=0;i<attributesArray.length; i++) {
        //alert(i + " : " + attributesArray[i].IdField + " check box ----------- " + attributesArray[i].IdField.checked);
        if(attributesArray[i].IdField.checked) {
            //alert("move this up " + attributesArray[i].AttributeNameField.value);
            if(i!=0 && attributesArray[i-1].IdField.checked!=true ) {
                    //alert(attributesArray[i].Row.innerHTML);

                    // swap in array
                    temp = attributesArray[i-1];
                    attributesArray[i-1] = attributesArray[i];
                    attributesArray[i] = temp;

                    anythingChanged = true;
            }
        }
    }

    // Refresh the view to replicate the array contents.
    if(anythingChanged) {
            refreshAttributesView(tableId, attributesArray);
    }
}

function MoveDown(tableId, attributesArray) {
    var anythingChanged = false;
    for(i=attributesArray.length-1;i>=0; i--) {
        if(attributesArray[i].IdField.checked) {
            //alert("move this up " + attr.AttributeNameField.value);
            if(i!=attributesArray.length-1 && attributesArray[i+1].IdField.checked!=true ) {
                    // swap in array
                    var temp = attributesArray[i+1];
                    attributesArray[i+1] = attributesArray[i];
                    attributesArray[i] = temp;
                    anythingChanged = true;
            }
        }
    }
    // Refresh the view to replicate the array contents.
    if(anythingChanged) {
           attributesArray = refreshAttributesView(tableId, attributesArray);
    }
}

// Refresh table view from attributes array, by using temporary array.
function refreshAttributesView (tableId, attributesArray) {
    //alert("Refreshing view now ... " + attributesArray);
    //var tempAttributesArray = new Array();
    var currentLength = attributesArray.length;
    var tempTBody = document.getElementById(tableId);
    // Remove existing rows.
    while(tempTBody.hasChildNodes() && tempTBody.lastChild) {
        tempTBody.removeChild(tempTBody.lastChild);
    }

    //alert("# of rows: " +attributesArray.length);
    for(i=0;i<currentLength; i++) {
        var attr = attributesArray[i];

        var IdFieldState = attr.IdField.checked;
        var RequiredFieldState = attr.RequiredField.checked;
        var SearchableFieldState = attr.SearchableField.checked;

        document.getElementById(tableId).appendChild(attr.Row);
        if(i%2)
          attr.Row.style.backgroundColor = "#f4f2ed";
        else
          attr.Row.style.backgroundColor = "#ffffff";
        attr.IdField.checked = IdFieldState;
        attr.RequiredField.checked = RequiredFieldState;
        attr.SearchableField.checked = SearchableFieldState;

        /*var tempAttr = new NewAttribute(tableId, tempAttributesArray);
        tempAttr.IdField.value = attr.IdField.value; // need to think abt this
        tempAttr.IdField.checked = attr.IdField.checked;
        tempAttr.AttributeNameField.value = attr.AttributeNameField.value;
        tempAttr.AttributeTypeField.selectedIndex = attr.AttributeTypeField.selectedIndex;
        tempAttr.TypeChanged();
        tempAttr.DefaultValueField.value = attr.DefaultValueField.value;
        tempAttr.RequiredField.checked = attr.RequiredField.checked;
        tempAttr.SearchableField.checked = attr.SearchableField.checked;

        tempAttributesArray.push( tempAttr );
        */
    }
    
    return true;
}


function selectAllCustomAttributes(attributesArray) {
    for(i=0;i<attributesArray.length; i++) {
      attr = attributesArray[i];
      attr.IdField.checked = true;
    }
}

function deselectAllCustomAttributes(attributesArray) {
    for(i=0;i<attributesArray.length; i++) {
      attr = attributesArray[i];
      attr.IdField.checked = false;
    }
}


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

function refreshCustomAttributesButtonsPalette (attributesArray, buttonIdPrefix) {
    //alert('refreshing...........' + buttonIdToRefresh);
    var selectedChoices = 0;
    var enableMoveUp = false, enableMoveDown = false;
    //var chkboxes = document.getElementsByName("chkRelationshipDef");
    for(i=0;i<attributesArray.length; i++) {
        var attr = attributesArray[i];
        if(attr.IdField.checked ) {
            selectedChoices ++;
            if(i>0) enableMoveUp = true;
            if(i<attributesArray.length-1) enableMoveDown = true;
        }
        
    }
   // alert("move up: " + enableMoveUp + " Move down: " + enableMoveDown);
    var imgDeleteAttributeObj = dojo.byId(buttonIdPrefix+"_imgDeleteCustAttr");
    if(imgDeleteAttributeObj != null) {
      if(selectedChoices > 0) imgDeleteAttributeObj.src =   deleteButtonEnabled.src;
      else imgDeleteAttributeObj.src =   deleteButtonDisabled.src;
    }
    
    var imgSelectAllButtonObj = dojo.byId(buttonIdPrefix+"_imgSelectAllAttr");
    if(imgSelectAllButtonObj != null ) {
        if(selectedChoices != attributesArray.length)
            imgSelectAllButtonObj.src =   selectAllButtonEnabled.src;
        else
            imgSelectAllButtonObj.src =   selectAllButtonDisabled.src;
    }
    var imgDeSelectAllButtonObj = dojo.byId(buttonIdPrefix+"_imgDeselectAllAttr");
    if(imgDeSelectAllButtonObj != null ) {
        if(selectedChoices > 0)
            imgDeSelectAllButtonObj.src =   deselectAllButtonEnabled.src;
        else
            imgDeSelectAllButtonObj.src =   deselectAllButtonDisabled.src;
    }
    
    // Enable/disable moveup & movedown buttons
    var buttonMoveUpObj = dojo.byId(buttonIdPrefix+"_moveUpButton");
    if(buttonMoveUpObj != null) {
      if(enableMoveUp) buttonMoveUpObj.disabled = false;
      else buttonMoveUpObj.disabled = true;
    }
    var buttonMoveDownObj = dojo.byId(buttonIdPrefix+"_moveDownButton");
    if(buttonMoveDownObj != null) {
      if(enableMoveDown) buttonMoveDownObj.disabled = false;
      else buttonMoveDownObj.disabled = true;
    }    
}

// function to create set of custom attributes entry from data set.
function createCustomAttributes (data, tableId, attributesArray, prefixToUse) {
    //alert("creating custom attributes now for " + prefixToUse + " table id " + tableId);
    attributesArray.splice(0, attributesArray.length); // Reset the array
    if(data.extendedAttributes != null) {
        for(i=0; i<data.extendedAttributes.length; i++) {
            var attrValue = data.extendedAttributes[i];
            //alert(i + " : " + attrValue.name + " : --- required:" + attrValue.required + " searchable:" + attrValue.searchable);
            var tempAttr = new NewAttribute(tableId,attributesArray, prefixToUse);
            attributesArray.push(tempAttr);
            // put values in fields
            tempAttr.AttributeNameField.value = attrValue.name;
            tempAttr.AttributeTypeField.value = attrValue.type;
            tempAttr.DefaultValueField.value = attrValue.defaultValue;
            tempAttr.RequiredField.value = attrValue.required;
            tempAttr.SearchableField.value = attrValue.searchable;
        }
    }
    refreshAttributesView(tableId, attributesArray);
}
