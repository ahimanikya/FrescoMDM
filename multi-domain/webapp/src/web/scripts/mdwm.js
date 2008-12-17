/* Javascript for MDWM */

// Function to validate a custom attribute
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
