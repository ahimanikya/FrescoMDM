
/*
 * API integration scripts for Manage - By Record Screens.
 */ 

dwr.engine.setErrorHandler(exceptionHandler);

function exceptionHandler(message) {
    alert(getMessageForI18N("exception")+ " " + message);
}


/*
 * Scripts for Select Record screen <START>
 */ 

function loadDomainsForSelectRecord() {
    isByRecordSelectDialogLoaded = true;
    alert("loading domains for select record dialog...");
}

/*
 * Scripts for Select/Search screen <END>
 */


  
  
/*
 * Scripts for Main (tree, details) screen <START>
 */


/*
 * Scripts for Main (listing, details) screen <END>
 */





/*
 * Scripts for Add Relationship screen <START>
 */ 
/*
 * Scripts for Add Relationship screen <END>
 */