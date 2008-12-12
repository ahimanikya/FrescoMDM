   
// Generic function, to get a boolean value for string,number input.
function getBoolean(variable)    {

    var vtype;
    var toReturn;
    if(variable != null && variable!=undefined)    {
        switch(typeof(variable))    {
            case 'boolean':    
                vtype = "boolean";
                return variable;
                break;
            case 'number':
                vtype = "number";
                if(variable == 0)    
                    toReturn = false;
                else toReturn = true;
                break;
            case 'string':
                variable = variable.toLowerCase();
                vtype = "string";
                if(variable == "true")    
                    toReturn = true;
                else if(variable == "false")
                    toReturn = false;
                else if(variable.length > 0)
                    toReturn = true;
                else if(variable.length == 0)
                    toReturn = false;                
                break;
        }
        return toReturn;        

    } else return false;
    return false; 
}
    
                
// returns first N characters from a string. If blnTrimAtWordEnd is true, output is trimmed to last word end.
function firstNCharacters(strData, intNChars, blnTrimAtWordEnd) {
    //alert(strData + " \n\n" + intNChars + " characters only");
    if(strData == null || strData==undefined) return null;
    if(strData.length <= intNChars) return strData;
    if(blnTrimAtWordEnd == null) blnTrimAtWordEnd = true;
        
    var returnValue = strData.substring(0,intNChars);
    //alert("returnValue :" + returnValue + ":");
    if(blnTrimAtWordEnd) {
        returnValue = returnValue.substring(0,returnValue.lastIndexOf(" "));
    }
    //alert("returning :" + returnValue + ":");
    return returnValue;
}

// Extending Array to add contains method.    
Array.prototype.contains = function (element) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == element) {
      return true;
    }
  }
  return false;
}
    

function isEmpty (value) {
    if(value == null || value == "") return true;
    return false;
}
function isInteger(value) {
    if(value == null) return true;
    return  (value.toString().search(/^-?[0-9]+$/) == 0);
}

function showDiv (divId) {
    var divObj = document.getElementById(divId);
    if(divObj != null) {
        divObj.style.visibility="visible";
        divObj.style.display = "block";
    }
}
function hideDiv (divId) {
    var divObj = document.getElementById(divId);
    if(divObj != null) {
        divObj.style.visibility="hidden";
        divObj.style.display = "none";
    }    
}
function displayDiv (divId, flagShow) {
    if(flagShow) showDiv (divId);
    else hideDiv(divId);
}