   
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