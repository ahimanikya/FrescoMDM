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
        function populatePreviewDiv(contentDiv,divIndex)   {
                 alert("div Index (countMain) " + divIndex)
                 //document.getElementById("previewEuidDiv"+divIndex).innerHTML =  document.getElementById("mainEuidContentDiv"+divIndex).innerHTML;
                 document.getElementById(contentDiv+divIndex).innerHTML =  document.getElementById(contentDiv+divIndex).innerHTML;

        }


        /*
         *  Function : showExtraFields
         *  Purpose  : Function to display or hide duplicate records extra fields.
         *
         *
         *
         */ 
        function showExtraFields(mainEuidDivIdIndex,assDupCount,mainDupCount,optionHideDisplay) {
              var height;

              if(optionHideDisplay == 'visible') {
                  height = "100%";
                  // Show/hide up/down images
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivIdIndex).style.visibility = "hidden";              
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivIdIndex).style.height = "0px";              

                  // Show/hide up/down images
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivIdIndex).style.height = height;              
 
              } else {
                  height = "0px";
                  // Show/hide up/down images
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivIdIndex).style.visibility = "visible";              
                  document.getElementById("showPreviewButtonDiv"+mainEuidDivIdIndex).style.height = "100%";              

                  // Show/hide up/down images
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
                  document.getElementById("hidePreviewButtonDiv"+mainEuidDivIdIndex).style.height = height;              
              } 

              alert(mainEuidDivIdIndex+"<=main==ass count==>"+assDupCount+"<<<<<<<"+optionHideDisplay+">>>>"+height);
              
              // Show/hide Heading Div id
              document.getElementById("dynamicContent"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicContent"+mainEuidDivIdIndex).style.height = height;              

              // Show/hide Main Div id
              document.getElementById("dynamicMainEuidContent"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicMainEuidContent"+mainEuidDivIdIndex).style.height = height;

              // Show/hide Main Div buttons id
              document.getElementById("dynamicMainEuidButtons"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicMainEuidButtons"+mainEuidDivIdIndex).style.height = height;
              //alert(document.getElementById("dynamicMainEuidContent"+mainEuidDivIdIndex).innerHTML);

              // Show/hide Associated Div Id
              for(var i=0;i<assDupCount;i++) {

                     document.getElementById("dynamicAssEuidContent"+mainEuidDivIdIndex+i).style.visibility = optionHideDisplay;              
                     document.getElementById("dynamicAssEuidContent"+mainEuidDivIdIndex+i).style.height = height;              

                     if(optionHideDisplay == 'visible') {
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivIdIndex+i).style.visibility = "hidden";              
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivIdIndex+i).style.height = "0px";              
                    } else {
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivIdIndex+i).style.visibility = "visible";              
                       document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivIdIndex+i).style.height = "25px";              
                    } 

              }

              // Show/hide Preview Div id
              document.getElementById("dynamicPreviewEuidButtonsDiv"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicPreviewEuidButtonsDiv"+mainEuidDivIdIndex).style.height = height;              

              document.getElementById("dynamicPreviewEuidDiv"+mainEuidDivIdIndex).style.visibility = optionHideDisplay;              
              document.getElementById("dynamicPreviewEuidDiv"+mainEuidDivIdIndex).style.height = height;              


        }
