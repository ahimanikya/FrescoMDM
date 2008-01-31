        function showdatadiv() {
              document.getElementById("advSearch").style.visibility = "hidden"; 
              document.getElementById("datadiv").style.visibility = "visible";    
          
              var id ;
              for(var i=1;i<5;i++)  {
                id = "datadiv"+i;
                document.getElementById(id).style.visibility="hidden"; 
                document.getElementById("preview").style.visibility  = "hidden";
               }                
              
        }
        function showDuplicatesdiv() {
              document.getElementById("datadiv").style.visibility = "visible";              
        }
        function showEoDetails() {
              //alert(document.getElementById("datadiv").innerHTML);
              //alert(document.getElementById("eoDatadiv").innerHTML);
              document.getElementById("allduprecords").style.visibility = "hidden";              
            
              document.getElementById("eoDatadiv").innerHTML = document.getElementById("datadiv").innerHTML;   
              document.getElementById("eoDatadiv").style.visibility = "visible";              
              //alert(document.getElementById("eoDatadiv").innerHTML);
               
        }
        function showBasicSearch()  {
              document.getElementById("advSearch").style.visibility = "hidden";             
              document.getElementById("advSearch").innerHTML= "";             

              document.getElementById("basicSearch").style.visibility = "visible";  
              if(document.getElementById("basicSearch").innerHTML =="") {
                document.getElementById("basicSearch").innerHTML =   document.getElementById("basicSearchData").innerHTML;
              }
              
        }
        function showAdvSearch()  {
              document.getElementById("basicSearch").style.visibility = "hidden";                            
              document.getElementById("basicSearch").innerHTML= "";             

              document.getElementById("advSearch").style.visibility = "visible";
              document.getElementById("advSearch").style.background = "#f3f3f3";  
              document.getElementById("advSearch").innerHTML =   document.getElementById("advSearchData").innerHTML;
        }
        function simple()  {
              var id ;
              for(var i=1;i<5;i++)  {
                id = "datadiv"+i;
                document.getElementById(id).style.visibility=(document.getElementById(id).style.visibility =='hidden')? 'visible' :'hidden'; 
                document.getElementById("preview").style.visibility  = "hidden";
               }                
        }

        function makeDiffPerson(divId,visibility,diffPerId)   {
              document.getElementById(divId).style.background = "#efefef";
        }
        
        function makeDiffPerson1(divId,visibility)   {
              document.getElementById(divId).style.background = "#efefef";
        }

        function populatePreview(index,euid)   {
                 var divId = "eodata"+index;
                 document.getElementById("preview").innerHTML =  document.getElementById(divId).innerHTML;
                 for(var i=0;i<4;i++) {
                      if(index == i) {
                         document.getElementById("eodata"+i).style.background = "#cecff9";
                      } else {
                         document.getElementById("eodata"+i).style.background = "#f7f8d5";
                      }
                 }
                 document.getElementById("preview").style.background = "#cecff9";
                 document.getElementById("preview").style.visibility = "visible";
        }
        function populateDuplicatesPreview(divId,previewId)   {
                 alert(document.getElementById(divId).innerHTML);
                 document.getElementById(previewId).innerHTML =  document.getElementById(divId).innerHTML;
                 document.getElementById(previewId).style.background = "#cecff9";
        }

        function selectOption(num) {
	        var selObj = document.getElementById('selSeaShells1');
	        selObj.selectedIndex = num;
        }

        function showSourceDivs(divId)  {
                ////alert(document.getElementById(divId).innerHTML);
                document.getElementById("mainDiv").innerHTML = document.getElementById(divId).innerHTML;            
        }

        function showReportDivs(divId)  {
                if (divId=="activityReport"){
                   document.getElementById("activityReportTab").style.visibility = "hidden";
                   document.getElementById("extraTab").innerHTML = document.getElementById("extraTabData").innerHTML;
                   document.getElementById("extraTab").style.visibility = "visible";
                } else if(divId=="assumedMatches" || divId=="auditLog"){
                   document.getElementById("extraTab").innerHTML = "";
                   document.getElementById("activityReportTab").style.visibility = "visible";
                   document.getElementById("extraTab").style.visibility = "hidden";
                }  

        }
        function showMoreAddr()  {
             document.getElementById("addDiv").innerHTML =   document.getElementById("addAddr").innerHTML; 
        }
        function showMorePnos()  {
             document.getElementById("phoneDiv").innerHTML =   document.getElementById("addPnosDiv").innerHTML;
        }
        function showSourceEditSearch()  {
                 document.getElementById("editSearch").innerHTML = document.getElementById("editSearchData").innerHTML ;
        }
        function showSourceEditLid() {
                  document.getElementById("editLid").innerHTML = document.getElementById("editLidData").innerHTML ;  
                  document.getElementById("source").style.visibility = "hidden";  
                  document.getElementById("editSearch").style.visibility = "hidden";  
        }
        function showSourceEditLidSearch() {
                 document.getElementById("edit2").innerHTML = document.getElementById("edit2Data").innerHTML ;
        }


        /*
         *  Function : populatePreviewDiv
         *  Purpose  : Function to preview the euid fields in preview layer.
         *
         *
         *
         */ 
        function populatePreviewDiv(previewDivIndex)   {
                 var previewEuidDiv;
                 var mainEuidDiv     = "mainEuidContentDiv"+previewDivIndex;
                 var contentDiv      = "mainEuidDataContent"+previewDivIndex;
                 var extraContentDiv = "dynamicMainEuidContent"+previewDivIndex;

                 // Change the class name for the divs
                 document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
                 document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
                 //alert(document.getElementById(contentDiv).innerHTML);
                 //alert(document.getElementById("previewEuidContentDiv"+previewDivIndex).innerHTML);

                 document.getElementById("previewEuidContentDivAbove"+previewDivIndex).innerHTML = document.getElementById(contentDiv).innerHTML;
                 document.getElementById("previewEuidContentDivExtra"+previewDivIndex).innerHTML = document.getElementById(extraContentDiv).innerHTML;

        }

        /*
         *  Function : populateAssociatePreviewDiv
         *  Purpose  : Function to preview the euid fields in preview layer.
         *
         *
         *
         */ 
        function populateAssociatePreviewDiv(assoDivIndex,previewDivIndex)   {

                 var previewEuidDiv;
                 var mainEuidDiv     = "assEuidDataContent"+assoDivIndex;
                 var contentDiv      = "assEuidDataContentAbove"+assoDivIndex;
                 var extraContentDiv = "dynamicAssEuidContent"+assoDivIndex;

                 // Change the class name for the divs
                 document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
                 document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
                 //alert(document.getElementById(contentDiv).innerHTML);
                 //alert(document.getElementById("previewEuidContentDiv"+previewDivIndex).innerHTML);

                 document.getElementById("previewEuidContentDivAbove"+previewDivIndex).innerHTML = document.getElementById(contentDiv).innerHTML;
                 document.getElementById("previewEuidContentDivExtra"+previewDivIndex).innerHTML = document.getElementById(extraContentDiv).innerHTML;

        }



        /*
         *  Function : showExtraFields
         *  Purpose  : Function to display or hide duplicate records extra fields.
         *
         *
         *
         */ 

         function showExtraFields(mainEuidDivId,assDupCount,optionHideDisplay) {
              var height;
              if(optionHideDisplay == 'visible') {
                  height = "100%";
                  // Show/hide up/down images
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivId).style.visibility = "hidden";              
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivId).style.height = "0px";              

                  // Show/hide up/down images
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";              
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;              
              } else {
                  height = "0px";
                  // Show/hide up/down images
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";              
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivId).style.height = "100%";              

                  // Show/hide up/down images
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "hidden";              
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;              

              } 

              //alert(mainEuidDivId+"<=main==ass count==>"+assDupCount+"<<<<<<<"+optionHideDisplay);
              
              // Show/hide Heading Div id
              document.getElementById("dynamicContent"+mainEuidDivId).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicContent"+mainEuidDivId).style.height = height;              

              var  contentDiv = "mainEuidDataContent"+mainEuidDivId;
              var  contentExtraDiv = "dynamicMainEuidContent"+mainEuidDivId;

              // Show/hide Main Div id
              //document.getElementById(contentDiv).style.visibility = optionHideDisplay;              
              //document.getElementById(contentDiv).style.height = height;

              document.getElementById(contentExtraDiv).style.visibility = optionHideDisplay;              
              document.getElementById(contentExtraDiv).style.height = height;

              // Show/hide Main Div id
              document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.height = height;

              
              //alert(document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.visibility);

              document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.height = height;

              //alert(document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.visibility);

              document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.visibility = optionHideDisplay;              
              document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.height = height;

              // Show/hide Associated Div Id
              for(var i=0;i<assDupCount;i++) {
                     document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;              
                     document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.height = height;              


                     document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;              
                     document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.height = height;

                     //alert(document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).innerHTML);
                     if(optionHideDisplay == 'visible') {
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "hidden";              
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "0px";              
                    } else {
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "visible";              
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "25px";              
                    } 

              }

              // Show/hide Preview Div id
              document.getElementById("dynamicPreviewEuidDiv"+mainEuidDivId).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicPreviewEuidDiv"+mainEuidDivId).style.height = height;              
        }        









/***
// Transparent Background on the different Person
**/

function toggleDifferentPerson(divToAnimate,showHide,AssocDiv,ToggleFlagId) { 
    alert ("Div to Animate -> " + divToAnimate);
    alert ("Showhide -> " + showHide);

   // toggle the visibility of Different Person
   var toggleHide = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'H'," + "'assEuidContentDiv" + ToggleFlagId + "','" + ToggleFlagId + "'";
   var toggleShow = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'S'," + "'assEuidContentDiv" + ToggleFlagId + "','" + ToggleFlagId + "'";
   var divContent = document.getElementById(AssocDiv).innerHTML;
   if ( divContent.indexOf(toggleHide) > 0 )   {
            //alert("Hide");

            var attribShowRight = {
                width: { from: 165, to:0}
            };

            var animShowRight = new YAHOO.util.Anim(divToAnimate, attribShowRight, 6, YAHOO.util.Easing.easeNone);

/* Change the styling of the different person*/         
            divContent = document.getElementById(AssocDiv).innerHTML;
            divContent = divContent.replace(/fnt6Hidden/g,'fntdup6');
            divContent = divContent.replace(/btnHidden/g,'dupbtn');
            divContent = divContent.replace(/dupHidden/g,'dupfirst');
            divContent = divContent.replace(/fnt9aHidden/g,'fntdup9a');
            divContent = divContent.replace(/fnt7aHidden/g,'fntdup7a');
            alert("hello");
            animShowRight.animate();
//Close Animation div
            document.getElementById(divToAnimate).style.height = '0px';
            //document.getElementById(divToAnimate).style.display = 'none';
            document.getElementById(AssocDiv).style.height='243px';
            document.getElementById(AssocDiv).style.display='block';

   } else {
            var attribShowLeft = {
                width: { from: 2, to:165}
            };
            //alert("Show");
//Close Content div
            var animShowLeft = new YAHOO.util.Anim(divToAnimate, attribShowLeft, 2, YAHOO.util.Easing.easeNone);
            document.getElementById(AssocDiv).style.height='0px';
            document.getElementById(AssocDiv).style.display='none';

            document.getElementById(divToAnimate).style.visibility='visible';
            document.getElementById(divToAnimate).style.height = '234px';
            document.getElementById(divToAnimate).style.display = 'block';

            // Change the styling to match the different person
            //divContent = divContent.replace(/fntdup6/g,'fnt6Hidden');
            divContent = divContent.replace(/dupbtn/g,'btnHidden');
            //divContent = divContent.replace(/dupfirst/g,'dupHidden');
            //divContent = divContent.replace(/fntdup9a/g,'fnt9aHidden');
            //divContent = divContent.replace(/fntdup7a/g,'fnt7aHidden');
            document.getElementById(divToAnimate).className = 'dynayui169';
            
            //alert(divContent);
            animShowLeft.animate();        
   }
    //alert("Hide" + divContent.indexOf(toggleHide));
    //alert("Show" + divContent.indexOf(toggleShow));

   var changeFlag = document.getElementById(AssocDiv).innerHTML;
   if ( divContent.indexOf(toggleHide) > 0 )   {
      changeFlag = changeFlag.replace(toggleHide,toggleShow);
   } else {
      changeFlag = divContent.replace(toggleShow,toggleHide);      
   }
   document.getElementById(AssocDiv).innerHTML = changeFlag ;

   //alert(divContent);
   document.getElementById(divToAnimate).innerHTML = '<div class="diffperson">' + divContent+ '</div>';
}
