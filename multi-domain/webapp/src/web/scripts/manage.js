
// Manage screen related scripts

 function showRecordFullDetails(contentId, showDetailsFlag){
     if(! showDetailsFlag){
         document.getElementById(contentId).style.visibility='hidden';
        document.getElementById(contentId).style.display='none';
     }
     else{
        document.getElementById(contentId).style.visibility='visible';
        document.getElementById(contentId).style.display='block';
     }
 }
