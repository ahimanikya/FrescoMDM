
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
    document.getElementById("advancedSearch").style.visibility = "hidden";
    document.getElementById("advancedSearch").style.display= "none";
    
    document.getElementById("basicSearch").style.visibility = "visible";
    document.getElementById("basicSearch").style.display = "block";
    
}
function showAdvSearch()  {
    document.getElementById("basicSearch").style.visibility = "hidden";
    document.getElementById("basicSearch").style.display= "none";
    
    document.getElementById("advancedSearch").style.visibility = "visible";
    document.getElementById("advancedSearch").style.display= "block";
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

function getPosition(e) {
    e = e || window.event;
    var cursor = {x:0, y:0};
    if (e.pageX || e.pageY) {
        cursor.x = e.pageX;
        cursor.y = e.pageY;
    } 
    else {
        var de = document.documentElement;
        var b = document.body;
        cursor.x = e.clientX + 
            (de.scrollLeft || b.scrollLeft) - (de.clientLeft || 0);
        cursor.y = e.clientY + 
            (de.scrollTop || b.scrollTop) - (de.clientTop || 0);
    }
    return cursor;
}

function showConfirm(divId,thisEvent)  {
    //alert(thisEvent);
    var y;
    var x;      
    //alert(document.getElementById(divId).style.visibility);
if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        var cursor = getPosition(thisEvent);
        //document.getElementById(divId).style.top = cursor.x;
        //document.getElementById(divId).style.left = cursor.y;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}

function showExtraDivs(divId,thisEvent)  {
    //alert(thisEvent);
    var y;
    var x;      
    //alert(document.getElementById(divId).style.visibility);
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
        if (thisEvent.pageX || thisEvent.pageY) {
            x = thisEvent.pageX;
            y = thisEvent.pageY;
        } else if (thisEvent.clientX || thisEvent.clientY) {
        x = thisEvent.clientX + document.body.scrollLeft;
        y = thisEvent.clientY + document.body.scrollTop;
    }
    
    document.getElementById(divId).style.top = (y-150);
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}

function togglePDDivs(divId,x,y)  {
    //alert(document.getElementById(divId).style.visibility);
    if(document.getElementById(divId).style.visibility == 'hidden') {
        document.getElementById(divId).style.visibility = "visible";
        document.getElementById(divId).style.display = "block";
    //alert(x+"---"+y);
    document.getElementById(divId).style.top = y;
    document.getElementById(divId).style.left = x;
} else {
   document.getElementById(divId).style.visibility = "hidden";
   document.getElementById(divId).style.display = "none";
}

}


function closeExtraDivs(divId,closeId)  {
    //alert(document.getElementById(divId).style.visibility);
       document.getElementById(divId).style.visibility = "hidden";
       document.getElementById(divId).style.display = "none";
       document.getElementById(divId).innerHTML = "";

       document.getElementById(closeId).style.visibility = "hidden";
       document.getElementById(closeId).style.display = "none";
       

}

function populateExtraDivs(sourceDivId,destnDivId,rootDiv,closeDivId)  {
        //alert(destnDivId+" destn  <====== > source "+sourceDivId);
        //alert(" source "+document.getElementById(sourceDivId).innerHTML);
        
        document.getElementById(destnDivId).innerHTML += document.getElementById(sourceDivId).innerHTML;
        //alert(document.getElementById(destnDivId).innerHTML);
        document.getElementById(destnDivId).style.visibility = "visible";
        document.getElementById(destnDivId).style.display = "block";
        //close the div after populating
        document.getElementById(rootDiv).style.visibility = "hidden";
        document.getElementById(rootDiv).style.display = "none";

        document.getElementById(closeDivId).style.visibility = "visible";
        document.getElementById(closeDivId).style.display = "block";
        
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
    
    var mainEuidDiv;
    var contentDiv;
    var extraContentDiv;
    
    var previewEuidDiv;
    mainEuidDiv     = "mainEuidContentDiv"+previewDivIndex;
    contentDiv      = "mainEuidDataContent"+previewDivIndex;
    extraContentDiv = "dynamicMainEuidContent"+previewDivIndex;
    
    alert(document.getElementById("showActiveButtonDiv").innerHTML);
    // Activate the compare screen buttons
    document.getElementById("showActiveButtonDiv").style.visibility = "visible";
    document.getElementById("showActiveButtonDiv").style.display = "block";
    
    // hide read only compare screen buttons
    document.getElementById("showReadonlyButtonDiv").style.visibility = "hidden";
    document.getElementById("showReadonlyButtonDiv").style.display = "none";
    
    
    // Change the class name for the divs
    //document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
    //document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
    //alert(document.getElementById(contentDiv).innerHTML);
    //alert(document.getElementById("previewEuidContentDiv"+previewDivIndex).innerHTML);
    
    //document.getElementById("previewEuidContentDivAbove"+previewDivIndex).innerHTML = document.getElementById(contentDiv).innerHTML;
    //document.getElementById("previewEuidContentDivExtra"+previewDivIndex).innerHTML = document.getElementById(extraContentDiv).innerHTML;
    
}

/*
*  Function : populateAssociatePreviewDiv
*  Purpose  : Function to preview the euid fields in preview layer.
*
*
*
*/
function populateAssociatePreviewDiv(assoDivIndex,previewDivIndex)   {
    
    var mainEuidDiv;
    var contentDiv;
    var extraContentDiv;
    
    var previewEuidDiv;
    mainEuidDiv     = "assEuidDataContent"+assoDivIndex;
    contentDiv      = "assEuidDataContentAbove"+assoDivIndex;
    extraContentDiv = "dynamicAssEuidContent"+assoDivIndex;
    
    // Change the class name for the divs
    document.getElementById(mainEuidDiv).className = 'dynaprevieww169';
    document.getElementById("previewEuidContentDiv"+previewDivIndex).className = 'dynaprevieww169';
    alert(document.getElementById(contentDiv).innerHTML);
    alert(document.getElementById("previewEuidContentDiv"+previewDivIndex).innerHTML);
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

function showExtraFields(mainEuidDivId,assDupCount,optionHideDisplay,mainDupCount) {
    var height;
    var displayOption;
    
    if(optionHideDisplay == 'visible') {
        height = "100%";
        displayOption = "block";
        
        // Show/hide up/down images
        document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.visibility = "hidden";
        //document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.height = "0px";
        document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.display = "none";
        
        // Show/hide up/down images
        document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";
        //document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;
        document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
        
        document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.visibility = "visible";
        document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.display = "block";
        
        document.getElementById("previewEuidDiv"+mainEuidDivId).style.visibility = "hidden";
        document.getElementById("previewEuidDiv"+mainEuidDivId).style.display = "none";
        
        document.getElementById("previewEuidContentDiv"+mainEuidDivId).className = 'dynaprevieww169';
        
    } else {
    height = "0px";
    displayOption = "none";
    // Show/hide up/down images
    document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.visibility = "visible";
    //document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.height = "100%";
    document.getElementById("showCompareButtonDiv"+mainEuidDivId).style.display = "block";
    
    // Show/hide up/down images
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility = "visible";
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.height = "100%";
    
    
    // Show/hide up/down images
    document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.visibility = "hidden";
    //document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.height = height;
    document.getElementById("hidePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
    
    // Show/hide preview
    document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.visibility = "hidden";
    document.getElementById("previewEuidDivBlue"+mainEuidDivId).style.display = "none";
    
    document.getElementById("previewEuidDiv"+mainEuidDivId).style.visibility = "visible";
    document.getElementById("previewEuidDiv"+mainEuidDivId).style.display = "block";
    
    document.getElementById("previewEuidContentDiv"+mainEuidDivId).className = 'dynaw169';
    
}




//alert(mainEuidDivId+"<=main==ass count==>"+assDupCount+"<<<<<<<"+optionHideDisplay);

// Show/hide Heading Div id
document.getElementById("dynamicContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicContent"+mainEuidDivId).style.display = displayOption;

var  contentDiv = "mainEuidDataContent"+mainEuidDivId;
var  contentExtraDiv = "dynamicMainEuidContent"+mainEuidDivId;

// Show/hide Main Div id
//document.getElementById(contentDiv).style.visibility = optionHideDisplay;
//document.getElementById(contentDiv).style.height = height;

document.getElementById(contentExtraDiv).style.visibility = optionHideDisplay;
//document.getElementById(contentExtraDiv).style.height = height;
document.getElementById(contentExtraDiv).style.display = displayOption;

// Show/hide Main Div id
document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicMainEuidButtonContent"+mainEuidDivId).style.display = displayOption;

//alert(document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.visibility);

document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.height = height;
document.getElementById("dynamicMainEuidContent"+mainEuidDivId).style.display = displayOption;

//alert(document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.visibility);

//document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.visibility = optionHideDisplay;
//document.getElementById("previewEuidContentDivExtra"+mainEuidDivId).style.height = height;

// Show/hide Associated Div Id
for(var i=0;i<assDupCount;i++) {
    //alert("======1=====");
    document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;
    //document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.height = height;
    document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).style.display = displayOption;
    
    document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.visibility = optionHideDisplay;
    //document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.height = height;
    document.getElementById("dynamicAssEuidButtonContent"+mainEuidDivId+i).style.display = displayOption;
    
    //alert(document.getElementById("dynamicAssEuidContent"+mainEuidDivId+i).innerHTML);
    if(optionHideDisplay == 'visible') {
        document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "hidden";
        //document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "0px";
        document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.display = "none";
    } else {
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.visibility = "visible";
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.height = "25px";
    document.getElementById("dynamicAssAddressEuidDiv"+mainEuidDivId+i).style.display = "block";
    
}

}
//alert(document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility);
if(document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility == 'visible')  {
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).className = 'dynaprevieww169';
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.visibility = optionHideDisplay;;
    //document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.height = height;
    document.getElementById("showActivePreviewButtonDiv"+mainEuidDivId).style.display = displayOption;
}


// Hide other main divs if any of them is ex
for(var i=0;i<mainDupCount;i++) {
    //alert("=======1====="+mainDupCount+"==========="+i);
    if(optionHideDisplay == 'visible') {
        if(i == mainEuidDivId) {
            //alert("===showing>>>====1====="+mainEuidDivId+"==========="+i);
            document.getElementById("mainEuidDiv"+mainEuidDivId).style.visibility = "visible";
            document.getElementById("mainEuidDiv"+i).style.display = "block";
            document.getElementById("separator"+i).style.display = "block";
        }  else {
        //alert("===hidinig>>>====1====="+mainEuidDivId+"==========="+i);
        document.getElementById("mainEuidDiv"+i).style.visibility = "hidden";
        document.getElementById("mainEuidDiv"+i).style.display = "none";
        document.getElementById("separator"+i).style.display = "none";
    }
} else {
document.getElementById("mainEuidDiv"+i).style.visibility = "visible";
document.getElementById("mainEuidDiv"+i).style.display = "block";
document.getElementById("separator"+i).style.display = "block";
}
}
}









/***
// Transparent Background on the different Person
**/

function toggleDifferentPerson(divToAnimate,showHide,AssocDiv,ToggleFlagId,nRows) {
    //alert ("Div to Animate -> " + divToAnimate);
    //alert ("Showhide -> " + showHide);   
    //alert(nRows);
    
    // toggle the visibility of Different Person
    var toggleHide = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'H'," + "'assEuidDataContent" + ToggleFlagId + "','" + ToggleFlagId + "'";
    var toggleShow = "'" + "Anim"  + ToggleFlagId + "'" + "," + "'S'," + "'assEuidDataContent" + ToggleFlagId + "','" + ToggleFlagId + "'";
    var divContent = document.getElementById(AssocDiv).innerHTML;
    
    if ( divContent.indexOf(toggleHide) > 0 )   {
        var attribShowRight = {
            width: { from: 165, to:0}
        };
        var animShowRight = new YAHOO.util.Anim(divToAnimate, attribShowRight, 2, YAHOO.util.Easing.easeNone);
        animShowRight.animate();
        
        /* Change the styling of the different person*/
        
        
        //Close Animation div
        // alert(divContent);
        if (nRows == 2)  {
            document.getElementById(AssocDiv).style.height= '400px';
        } else {
        document.getElementById(AssocDiv).style.height= '200px';
    }
    document.getElementById(AssocDiv).style.display='block';
    //alert("hello");
    document.getElementById(divToAnimate).style.height = '0px';
    document.getElementById(divToAnimate).style.display = 'none';
    
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
if (nRows == 2)  {
    document.getElementById(divToAnimate).style.height = '400px';
}  else {
document.getElementById(divToAnimate).style.height = '200px';
}
document.getElementById(divToAnimate).style.display = 'block';
//alert(divToAnimate);
animShowLeft.animate();
}

var changeFlag = document.getElementById(AssocDiv).innerHTML;
if ( divContent.indexOf(toggleHide) > 0 )   {
    //alert("1");
    changeFlag = changeFlag.replace(toggleHide,toggleShow);
} else {
//alert(changeFlag);
//alert(toggleShow + " -- " +toggleHide);
changeFlag = divContent.replace(toggleShow,toggleHide);
//alert(changeFlag);
}
document.getElementById(AssocDiv).innerHTML = changeFlag ;

//alert(changeFlag);
document.getElementById(divToAnimate).innerHTML = '<div class="diffperson">' + divContent+ '</div>';
}


/*
*  Function : activePreviewButtons
*  Purpose  : Function to preview the euid fields in preview layer.
*
*
*
*/
function activePreviewButtons(totalAssociates,assoDivIndex,previewDivIndex)   {
    //alert(document.getElementById("showCompareButtonDiv"+previewDivIndex).style.visibility);
    // Change the class name for the divs
    if(document.getElementById("showCompareButtonDiv"+previewDivIndex).style.visibility == 'hidden') {
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).className = 'dynaprevieww169';
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).style.visibility = "visible";
        document.getElementById("showActivePreviewButtonDiv"+previewDivIndex).style.display = "block";
    }
    // Change the class name for the divs
    document.getElementById("hidePreviewButtonDiv"+previewDivIndex).style.visibility = "hidden";
    document.getElementById("hidePreviewButtonDiv"+previewDivIndex).style.display = "none"
    for(var i=0;i<totalAssociates;i++) {
        if(previewDivIndex+i == assoDivIndex) {
            document.getElementById("assEuidDataContent"+previewDivIndex+i).className = 'dynaprevieww169';
        }
    }
    for(var i=0;i<totalAssociates;i++) {
        if(previewDivIndex+i != assoDivIndex) {
            document.getElementById("assEuidDataContent"+previewDivIndex+i).className = 'dynaw169';
        }
    }
    
}




function showViewSources(mainDupSources,count,countEnt,totalColumns) {
//    alert(mainDupSources+"++++++"+count+"====="+countEnt);
  // alert(document.getElementById("previewPane").style.visibility);
    var divLayerMain;
    //hideOther(mainDupSources,count,countEnt);
    for(var i=0;i<count;i++) {
    divLayerMain = document.getElementById(mainDupSources+countEnt+i);
    //alert(mainDupSources+countEnt+i);
    if (divLayerMain.style.display=="none" || divLayerMain.style.display=="") {
        divLayerMain.style.visibility="visible";
        divLayerMain.style.display="block";
       if( mainDupSources == 'mainDupSources') {
            divLayerMain = document.getElementById("mainDupHistory"+countEnt+i);
            divLayerMain.style.visibility="hidden";
            divLayerMain.style.display="none";
        } else if( mainDupSources == 'mainDupHistory' ) {
           divLayerMain = document.getElementById("mainDupSources"+countEnt+i);
           divLayerMain.style.visibility="hidden";
           divLayerMain.style.display="none";
       }
       //display or hide preview pane
       document.getElementById("previewPane").style.visibility="hidden";
       document.getElementById("previewPane").style.display="none";

       //display/hide other divs
       for(var c=0;c<totalColumns;c++) {
          //alert("=H/D=="+c +"totalColumns"+totalColumns+ "==" +countEnt+"====="+document.getElementById("outerMainContentDivid"+c).style.visibility);
          if(c  == countEnt) {
            //alert("=EQUAL=="+c + "==" +countEnt+"====="+document.getElementById("outerMainContentDivid"+c).style.visibility);
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
         } else {
            //alert("=NE=="+c + "==" +countEnt+"====="+document.getElementById("outerMainContentDivid"+c).style.visibility);
           document.getElementById("outerMainContentDivid"+c).style.visibility = "hidden";
           document.getElementById("outerMainContentDivid"+c).style.display = "none";
         }
       }
    } else if (divLayerMain.style.display=="block") {
       divLayerMain.style.visibility="hidden";
       divLayerMain.style.display="none";
 
       //display or hide preview pane
       document.getElementById("previewPane").style.visibility="visible";
       document.getElementById("previewPane").style.display="block";
       //display main and other duplicate divs
       for(var c=0;c<totalColumns;c++) {
            //alert("=H/D=HIHIHIH="+c +"totalColumns"+totalColumns+ "==" +countEnt+"====="+document.getElementById("outerMainContentDivid"+c).style.visibility);
            document.getElementById("outerMainContentDivid"+c).style.visibility = "visible";
            document.getElementById("outerMainContentDivid"+c).style.display = "block";
      }
       
  }
}
} 
function hideOther(mainDupSources,count,countEnt) {
    alert(mainDupSources+"+++HIDING+++"+count+"====="+countEnt);
    var divLayerMain;
    //alert(divLayerMain.style.visibility);
    for(var i=0;i<count;i++) {
       if( showViewSources == 'mainDupSources') {
            divLayerMain = document.getElementById("mainDupHistory"+countEnt+i);
            divLayerMain.style.visibility="hidden";
            divLayerMain.style.display="none";
        } else if( showViewSources == 'mainDupHistory' ) {
           divLayerMain = document.getElementById("mainDupSources"+countEnt+i);
           divLayerMain.style.visibility="hidden";
           divLayerMain.style.display="none";
       }
    }   
}


function showAssViewSources(countAss) {
    var divLayerAssoc = document.getElementById("assDupSources"+countAss);
    if (divLayerAssoc.style.display=="none" || divLayerAssoc.style.display=="") {
        divLayerAssoc.style.visibility="visible";
        divLayerAssoc.style.display="block";
    } else if (divLayerAssoc.style.display=="block"){
    divLayerAssoc.style.visibility="hidden";
    divLayerAssoc.style.display="none";
}
}

function getDateFieldName(formName,idName)  { 
   var thisFrm = document.forms[formName];
   for(i=0; i< thisFrm.elements.length; i++)   {      
     if (thisFrm.elements[i].name.indexOf(idName) != -1)   {
          return thisFrm.elements[i].name;            
        }
    }
    return 'null';
} 


function ClearContents(thisForm)  { 
    thisFrm = document.forms[thisForm];
    for(i=0; i< thisFrm.elements.length; i++)   {      
        if (!thisFrm.elements[i].name.indexOf(thisForm+':'))   {
            thisFrm.elements[i].value = "";
        }
    }
    return;
} 

function  confirmResolve(countDupId){
    
    //var confirm_action = confirm("Select the type of resolve<BR> Resolve:<BR> AutoResolve");
    alert(countDupId + "<=== countDupId");
    countDupId = 1;
    document.getElementById("potentialDuplicate"+countDupId).style.visibility = "visible";
    document.getElementById("potentialDuplicate"+countDupId).style.display = "block";

    document.getElementById("differentPerson"+countDupId).style.visibility = "hidden";
    document.getElementById("differentPerson"+countDupId).style.display= "none";

    var newwindow=window.open('./resolvepopup.jsp','Different Person','height=200,width=300,left=1200,top=1400,resizable=no,scrollbars=no,toolbar=no,status=no,statusbar=no');
    if (window.focus) {newwindow.focus()}
}

function  markDuplicate(){
    var countDupId = 1;
    document.getElementById("differentPerson1").style.visibility = "hidden";
    document.getElementById("differentPerson1").style.display= "none";
    
    document.getElementById("potentialDuplicate1").style.visibility = "visible";
    document.getElementById("potentialDuplicate1").style.display = "block";
    
}

function disp_gnameover(divId) {
    document.getElementById(divId).style.visibility = "visible";
    document.getElementById(divId).style.display = "block";
	
}

function hide_gnameover(divId) {
    document.getElementById(divId).style.visibility = "hidden";
    document.getElementById(divId).style.display = "none";
	
}


//* AJAX Services 

var is_ie = (navigator.userAgent.indexOf('MSIE') >= 0) ? 1 : 0; 
var is_ie5 = (navigator.appVersion.indexOf("MSIE 5.5")!=-1) ? 1 : 0; 
var is_opera = ((navigator.userAgent.indexOf("Opera6")!=-1)||(navigator.userAgent.indexOf("Opera/6")!=-1)) ? 1 : 0; 

//netscape, safari, mozilla behave the same??? 
var is_netscape = (navigator.userAgent.indexOf('Netscape') >= 0) ? 1 : 0; 

//XML HttpRequest Handle
var xhr; 
// call the URL
var innerHtmlDiv = '';
var thisEvent;
function ajaxURL(url,thisInnerHtmlDivName,e)    {
    innerHtmlDiv = thisInnerHtmlDivName;

    //alert(innerHtmlDiv);
    //alert(e);    
    thisEvent = e;
    document.getElementById(innerHtmlDiv).style.visibility='visible';
    xhr = getXmlHttpObject(ajaxstateChangeHandler); 
    //Send the xmlHttp get to the specified url 
    xmlHttpGet(xhr, url); 
} 


    function ajaxstateChangeHandler()     { 
        //readyState of 4 or 'complete' represents that data has been returned 
        if (xhr.readyState == 4 || xhr.readyState == 'complete')        { 
            //Gather the results from the callback 
           var str = xhr.responseText; 
           //alert(str);
           //var closeButton ="<a align='right' onclick='javascript:document.getElementById('tree').style.visibility = 'hidden'>Close</a>"; 
           document.getElementById(innerHtmlDiv).innerHTML = xhr.responseText;
           document.getElementById(innerHtmlDiv).style.display = 'block';
           document.getElementById(innerHtmlDiv).style.visibility = 'visible';
           var xpos = thisEvent.layerX? thisEvent.layerX : thisEvent.offsetX? thisEvent.offsetX : 0;
           var ypos = thisEvent.layerY? thisEvent.layerY : thisEvent.offsetY? thisEvent.offsetY : 0;
           //alert(xpos+'---'+ypos);
           var divID = document.getElementById(innerHtmlDiv);
           var x = divID.getElementsByTagName("script");    
           for(var i=0;i<x.length;i++)   {       
                eval(x[i].text);   
           }
           //get values
         } else   {
            document.getElementById(innerHtmlDiv).innerHTML =  "<img src='./images/loading.gif' border='0'> <p>Loading ...</p>";
         }
           //document.getElementById(innerHtmlDiv).style.top = ypos;
           //document.getElementById(innerHtmlDiv).style.left = xpos;
           //alert(xpos+'---'+ypos);
    } 

    // XMLHttp send GET request 
    function xmlHttpGet(xmlhttp, url) { 
        xmlhttp.open('GET', url, true); 
        xmlhttp.send(null); 
    } 


    function getXmlHttpObject(handler) 
    { 
        var objXmlHttp = null;    //Holds the local xmlHTTP object instance 

        //Depending on the browser, try to create the xmlHttp object 
        if (is_ie){ 
            //The object to create depends on version of IE 
            //If it isn't ie5, then default to the Msxml2.XMLHTTP object 
            var strObjName = (is_ie5) ? 'Microsoft.XMLHTTP' : 'Msxml2.XMLHTTP'; 
             
            //Attempt to create the object 
            try{ 
                objXmlHttp = new ActiveXObject(strObjName); 
                objXmlHttp.onreadystatechange = handler; 
            } 
            catch(e){ 
            //Object creation errored 
                alert('IE detected, but object could not be created. Verify that active scripting and activeX controls are enabled'); 
                return; 
            } 
        } 
        else if (is_opera){ 
            //Opera has some issues with xmlHttp object functionality 
            alert('Opera detected. The page may not behave as expected.'); 
            return; 
        } 
        else{ 
            // Mozilla | Netscape | Safari 
            objXmlHttp = new XMLHttpRequest(); 
            objXmlHttp.onload = handler; 
            objXmlHttp.onerror = handler; 
        } 
         
        //Return the instantiated object 
        return objXmlHttp; 
    } 
    
    function getFormData(form) {
    var dataString = "";

    function addParam(name, value) {
        dataString += (dataString.length > 0 ? "&" : "")
            + escape(name).replace(/\+/g, "%2B") + "="
            + escape(value ? value : "").replace(/\+/g, "%2B");
    }

    var elemArray = form.elements;
    for (var i = 0; i < elemArray.length; i++) {
        var element = elemArray[i];
        var elemType = element.type.toUpperCase();
        var elemName = element.name;
        if (elemName) {
            if (elemType == "TEXT"
                    || elemType == "TEXTAREA"
                    || elemType == "PASSWORD"
                    || elemType == "HIDDEN")
                addParam(elemName, element.value);
            else if (elemType == "CHECKBOX" && element.checked)
                addParam(elemName, element.value ? element.value : "On");
            else if (elemType == "RADIO" && element.checked)
                addParam(elemName, element.value);
            else if (elemType.indexOf("SELECT") != -1)
                for (var j = 0; j < element.options.length; j++) {
                    var option = element.options[j];
                    if (option.selected)
                        addParam(elemName,
                            option.value ? option.value : option.text);
                }
        }
    }
    return dataString;
}

function submitFormData(form, thisInnerHtmlDivName) {
    innerHtmlDiv = thisInnerHtmlDivName;
    if (window.ActiveXObject)
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest)
        xhr = new XMLHttpRequest();
    else
        return null;
    getXmlHttpObject(handler);    
    var method = form.method ? form.method.toUpperCase() : "GET";
    var action = form.action ? form.action : document.URL;
    var data = getFormData(form);

    var url = action;
    alert(action);
    
    function submitCallback() {
        if (xhr.readyState == 4 && xhr.status != 200) {
            alert("Successssss!!!");
            alert(xhr.responseText);
            alert("Auto-Save Error: "
                + xhr.status + " " + xhr.statusText);
        }
    }
    xhr.onreadystatechange = submitCallback;

    xhr.setRequestHeader("Ajax-Request", "Auto-Save");
    if (method == "POST") {
        xhr.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded");
        xhr.send(data);
    } else
        xhr.send(null);
    
    return xhr;
}
