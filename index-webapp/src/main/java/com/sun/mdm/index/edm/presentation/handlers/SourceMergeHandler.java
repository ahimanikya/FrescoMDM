/*
 * SourceMergeHandler.java
 *
 * Created on September 12, 2007, 6:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;   

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.model.SelectItem;
import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.java.hulp.i18n.LocalizationSupport;
/**
 *
 * @author admin
 */
public class SourceMergeHandler {
    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.SourceMergeHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    
     private String lid1;
     
     private String lid2;
     
     private String lid3;
     
     private String lid4;
     
     private String formlids;
     
     private String lidsource;

     
     private String source;
     
     private String viewLids="Merge_View_Lids";
     
    //Adding the following variable for getting the select options if the FieldConfig type is "Menu List"
    private ArrayList<SelectItem> selectOptions = new ArrayList();
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    ScreenObject  screenObject  = (ScreenObject) session.getAttribute("ScreenObject");
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    MasterControllerService masterControllerService = new MasterControllerService();

    private ArrayList mergeLidsList  = new ArrayList();
    String[][] allSystemCodes = masterControllerService.getSystemCodes();
         
    private String lidMask = allSystemCodes[1][0];

    private int lidMaskLength  = allSystemCodes[1][0].length();


    //private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.SourceMergeHandler");
     
    
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    String enterLidsMessaage =bundle.getString("lid_enter_more_lids");
    SourceHandler sourceHandler = new SourceHandler();
    Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
    Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
    
    EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();
    private ArrayList soArrayList = new ArrayList();
    private HashMap systemObjectHashMap;
    /** Selected merge fields */
    private String selectedMergeFields = new String();

    
    /** Creates a new instance of SourceMergeHandler */
    public SourceMergeHandler() {
    }

    public String getLid1() {
        return lid1;
    }

    public void setLid1(String lid1) {
        this.lid1 = lid1;
    }

    public String getLid2() {
        return lid2;
    }

    public void setLid2(String lid2) {
        this.lid2 = lid2;
    }

    public String getLid3() {
        return lid3;
    }

    public void setLid3(String lid3) {
        this.lid3 = lid3;
    }

    public String getLid4() {
        return lid4;
    }

    public void setLid4(String lid4) {
        this.lid4 = lid4;
    }

    public String getViewLids() {
        return viewLids;
    }

    public void setViewLids(String viewLids) {
        this.viewLids = viewLids;
    }

    public ArrayList<SelectItem> getSelectOptions() {
        MasterControllerService masterControllerService  = new MasterControllerService();
        String[][] systemCodes = masterControllerService.getSystemCodes();
        HashMap  SystemCodeDesc = new HashMap ();
        String[] pullDownListItems = systemCodes[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
           // SelectItem selectItem = new SelectItem();
            
            String sysDesc = masterControllerService.getSystemDescription(pullDownListItems[i]);
            String  sysCode  = pullDownListItems[i];
            SystemCodeDesc.put(sysCode, sysDesc);
           
           
            //System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
           // selectItem.setLabel(masterControllerService.getSystemDescription(pullDownListItems[i]));
           // selectItem.setValue(pullDownListItems[i]);
           // newArrayList.add(selectItem);
        }
        HashMap  sortedSyscode = getSortedMap(SystemCodeDesc);
        
            Set  sysCodeSet =  sortedSyscode.keySet();
            Iterator it = sysCodeSet.iterator();
            while(it.hasNext()){
            SelectItem selectItem = new SelectItem();
            String  sysCode  = (String)it.next();
            String sysDesc = masterControllerService.getSystemDescription(sysCode);
            SystemCodeDesc.put(sysCode, sysDesc);
            System.out.println("Adding Select item label" + sysDesc + "Value" + sysCode);
            selectItem.setLabel(sysDesc);
            selectItem.setValue(sysCode);
            newArrayList.add(selectItem);
        }
           selectOptions = newArrayList;
           return selectOptions;
    }

    public void setSelectOptions(ArrayList<SelectItem> selectOptions) {
        this.selectOptions = selectOptions;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public String performPreviewLID() {

        String[] lids = this.formlids.split(":");
        //System.out.println(" Request " +   request);
        String sourceLid = lids[0];
        String destnLid = lids[1];
        request.setAttribute("lids", lids);
        request.setAttribute("lidsource", this.lidsource);

        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        String sourceEuid = new String();
        String destnEuid = new String();

        try {

            sourceEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, sourceLid)).getEUID();
            destnEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, destnLid)).getEUID();

            SystemObject finalMergredDestnSOPreview = masterControllerService.getPostMergeSystemObject(this.lidsource, sourceLid, destnLid);
            request.setAttribute("mergedSOMap", compareDuplicateManager.getSystemObjectAsHashMap(finalMergredDestnSOPreview, screenObject));
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage, ex.getMessage()));
           mLogger.error(mLocalizer.x("SRC045: Failed to generate LID preview {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            //mLogger.error("UserException ex : " + ex.toString());
            mLogger.error(mLocalizer.x("SRC046: Failed to generate LID preview {0}",ex.getMessage()),ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            //mLogger.error("Exception ex : " + ex.toString());
            mLogger.error(mLocalizer.x("SRC047: Failed to generate LID preview {0}",ex.getMessage()),ex);
        }
        
        try {
            //Insert audit Log for LID Merge
            masterControllerService.insertAuditLog((String) session.getAttribute("user"), sourceEuid, destnEuid, "LID Merge - Selection", new Integer(screenObject.getID()).intValue(), "View two selected EUIDs of the LID merge confirm page");
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SRC048: Failed to insert audit Log for LID Merge {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            mLogger.error(mLocalizer.x("SRC049: Failed to insert audit Log for LID Merge {0}",ex.getMessage()),ex);
        }

        return ""; //reload the same page

    }
  
  public String performMergeLIDs () {
    
         String[] lids = this.formlids.split(":");
         //System.out.println(" Request " +   request);
         String sourceLid = lids[0];
         String destnLid = lids[1];
         request.setAttribute("lids", lids);
        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        try{
     
            SystemObject finalMergredDestnSOPreview  = masterControllerService.getPostMergeSystemObject(this.lidsource, sourceLid, destnLid);
            request.setAttribute("mergedSOMap", compareDuplicateManager.getSystemObjectAsHashMap(finalMergredDestnSOPreview,screenObject));
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            //mLogger.error("ProcessingException ex : " + ex.toString());
            mLogger.error(mLocalizer.x("SRC050: Failed to Merge LID {0}",ex.getMessage()),ex); 
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            //mLogger.error("UserException ex : " + ex.toString());
             mLogger.error(mLocalizer.x("SRC051: Failed to Merge LID {0}",ex.getMessage()),ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            //mLogger.error("Exception ex : " + ex.toString());
             mLogger.error(mLocalizer.x("SRC052: Failed to Merge LID {0}",ex.getMessage()),ex);
        }
         return ""; //reload the same page
     }
  
  
  
     public String performLidMergeSearch () {
        session.removeAttribute("soHashMapArrayList");
        session.setAttribute("tabName","Merge");
        try {
            String errorMessage = bundle.getString("system_object_not_found_error_message");					  
            errorMessage += this.source ; 
            SystemObject systemObjectLID = null;
            ArrayList newArrayList  = new ArrayList();
	    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

            boolean validateSystemCode = false;
            if (this.getLid1() != null && this.getLid1().trim().length()>0 ) {
                this.setLid1(this.getLid1().replaceAll("-", ""));
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid1);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    errorMessage +=  "," + this.getLid1();
                    validateSystemCode = true; 
                 } else {
                    //display the message if the user is searching for either inactive/merged system objects
                    if ("merged".equalsIgnoreCase(systemObjectLID.getStatus()) || "inactive".equalsIgnoreCase(systemObjectLID.getStatus())) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + systemObjectLID.getStatus(), this.getLid4() + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                    }
                    
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(systemObjectLID.getStatus())) newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid2() != null && this.getLid2().trim().length()>0) {
                this.setLid2(this.getLid2().replaceAll("-", ""));
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid2);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    errorMessage += "," + this.getLid2();
                    validateSystemCode = true; 
                } else {
                    //display the message if the user is searching for either inactive/merged system objects
                    if ("merged".equalsIgnoreCase(systemObjectLID.getStatus()) || "inactive".equalsIgnoreCase(systemObjectLID.getStatus())) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + systemObjectLID.getStatus(), this.getLid4() + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(systemObjectLID.getStatus())) newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid3() != null && this.getLid3().trim().length()>0) {
                 this.setLid3(this.getLid3().replaceAll("-", ""));
                 systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid3);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                   errorMessage += "," + this.getLid3();
                   validateSystemCode = true; 
                } else {
                    //display the message if the user is searching for either inactive/merged system objects
                    if ("merged".equalsIgnoreCase(systemObjectLID.getStatus()) || "inactive".equalsIgnoreCase(systemObjectLID.getStatus())) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + systemObjectLID.getStatus(), this.getLid4() + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(systemObjectLID.getStatus())) newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid4() != null && this.getLid4().trim().length()>0) {
                this.setLid4(this.getLid4().replaceAll("-", ""));
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid4);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    errorMessage += "," +  this.getLid4();
                    validateSystemCode = true; 
                } else {
                    //display the message if the user is searching for either inactive/merged system objects
                    if ("merged".equalsIgnoreCase(systemObjectLID.getStatus()) || "inactive".equalsIgnoreCase(systemObjectLID.getStatus())) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + systemObjectLID.getStatus(), this.getLid4() + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(systemObjectLID.getStatus())) newArrayList.add(systemObjectLID);
                }
            }
            if(validateSystemCode) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            }

            ArrayList newSoArrayList  = new ArrayList();
            for (int i = 0; i < newArrayList.size(); i++) {
                SystemObject systemObject = (SystemObject) newArrayList.get(i);
                //HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                HashMap editSystemObjectHashMap = (HashMap) compareDuplicateManager.getSystemObjectAsHashMap(systemObject, screenObject);
                newSoArrayList.add(editSystemObjectHashMap);
            }
    
            if(newSoArrayList.size() > 0) {
                setSoArrayList(newSoArrayList);
                session.setAttribute("soHashMapArrayList", newSoArrayList);
                if(newSoArrayList.size() ==1 ) { 
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,enterLidsMessaage,enterLidsMessaage));
                }
            }
            
       } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC053: Failed to search  LIDMerge {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("SRC054: Failed to search LIDMerge {0}",ex.getMessage()),ex);
        }
        return "LID Details";
    }

    public ArrayList getSoArrayList() {
        return soArrayList;
    }

    public void setSoArrayList(ArrayList soArrayList) {
        this.soArrayList = soArrayList;
    }

     /**
     * 
     * @param event
     */
    public void keepLidsAction(ActionEvent event) {
        //String lid = (String) event.getComponent().getAttributes().get("lidDiff");
        String sourceLID = (String) event.getComponent().getAttributes().get("sourceLID");
        //System.out.println("IN keepLidsAction ==: > " + sourceLID);
        this.mergeLidsList.add(sourceLID);
        session.setAttribute("mergeLidsList", mergeLidsList);
    }

      /**
     * 
     * @param event
     */
    public void setPreviewSystemObjectValues(ActionEvent event) {
        
        String fnameExpression = (String) event.getComponent().getAttributes().get("fnameExpression");
        Object fvalueValueExpression = (Object) event.getComponent().getAttributes().get("fvalueValueExpression");

        //System.out.println("IN fnameExpression " + fnameExpression + "fvalueValueExpression" + fvalueValueExpression);

        HashMap fieldValuesMerge = (HashMap)request.getAttribute("mergedSOMap");
        //System.out.println("fieldValuesMerge ==> " + fieldValuesMerge);
        if (fieldValuesMerge != null) {
            //System.out.println("Before Changing the hashmap for " + fnameExpression + "with" + fieldValuesMerge.get(fnameExpression));
            fieldValuesMerge.put(fnameExpression, fvalueValueExpression); //set the value for the preview section
            request.setAttribute("mergedSOMap", fieldValuesMerge);  //restore the session object again.
            //System.out.println("After Changing the hashmap for " + fnameExpression + "with" + fieldValuesMerge.get(fnameExpression));

        }

    }
     
      /**
     * 
     * @param event
     */
    public void postMergePreviewSystemObject(ActionEvent event) {
       
        String srcLIDValueExpression = (String) event.getComponent().getAttributes().get("mainEOValueExpression");
        String destnLIDValueExpression = (String) event.getComponent().getAttributes().get("duplicateEOValueExpression");

        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        HashMap mergedHashMapValueExpression = (HashMap) event.getComponent().getAttributes().get("mergedEOValueExpression");

      
        
        String sbrLID =  (String) mergedHashMapValueExpression.get("LID");
        String destnId = (sbrLID.equalsIgnoreCase(srcLIDValueExpression))?destnLIDValueExpression:srcLIDValueExpression;
        
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        try{
            SystemObject sourceSO = masterControllerService.getSystemObject(this.source,sbrLID);
            SystemObject destinationSO = masterControllerService.getSystemObject(this.source,destnId);
            
             String[] selectedFieldsValue = this.getSelectedMergeFields().split(">>");

             HashMap soHashMap = (HashMap)compareDuplicateManager.getSystemObjectAsHashMap(destinationSO, screenObject).get("SYSTEM_OBJECT_EDIT");

            //when user modifies the person fields the only  update the enterprise object
            if (selectedFieldsValue.length > 1) {
                //Modify destination SO values with selected values 
                for (int i = 0; i < selectedFieldsValue.length; i++) {
                    String[] sourceEuidFull = selectedFieldsValue[i].split("##");
                    soHashMap.put(sourceEuidFull[0], sourceEuidFull[1]);
                  
                }
                //Modify CHANGED SYSTEM OBJECT values here
                masterControllerService.modifySystemObject(destinationSO, soHashMap);
            }
           
            
            SystemObject finalMergredDestnSOPreview  = masterControllerService.getPostMergeSystemObject(this.source, sbrLID, destnId);
            
            request.setAttribute("mergedSOMap", getSystemObjectAsHashMap(finalMergredDestnSOPreview));
            
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC055: Failed to get System object preview {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC056: Failed to get System object preview {0}",ex.getMessage()),ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC057: Failed to get System object preview {0}",ex.getMessage()),ex);
        }
        //System.out.println("=====1====" + mergredHashMapVaueExpression.get("Person.FirstName"));
      }
/**
     * 
     * @param event
     */

    public void mergePreviewSystemObject(ActionEvent event) {
        try {


            String[] lids = this.formlids.split(":");
            String sourceLid = lids[0];
            String destnLid = lids[1];

        
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            HashMap mergredHashMapVaueExpression = (HashMap) event.getComponent().getAttributes().get("mergedEOValueExpression");

            //System.out.println("=====IN mergePreviewEnterpriseObject ====" + mergredHashMapVaueExpression);
            String sbrLID = (String) mergredHashMapVaueExpression.get("LID");
            String destnId = (sbrLID.equalsIgnoreCase(sourceLid)) ? destnLid : sourceLid;

            String sourceEuid = new String();
            String destnEuid = new String();

            try {
                sourceEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, sbrLID)).getEUID();
                destnEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, destnLid)).getEUID();

                SystemObject finalMergredDestnSO = masterControllerService.mergeSystemObject(this.source, sbrLID, destnId, mergredHashMapVaueExpression);
                ArrayList finalMergredDestnEOArrayList = new ArrayList();
                finalMergredDestnEOArrayList.add(finalMergredDestnSO);
                session.removeAttribute("soHashMapArrayList");
		
                request.setAttribute("mergedSOMap", finalMergredDestnEOArrayList);
            } catch (ProcessingException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
                mLogger.error(mLocalizer.x("SRC058: Failed to get merge System object preview {0}",ex.getMessage()),ex);
            } catch (UserException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
                mLogger.error(mLocalizer.x("SRC059: Failed to get merge System object preview {0}",ex.getMessage()),ex);
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage, ex.getMessage()));
               mLogger.error(mLocalizer.x("SRC060: Failed to get merge System object preview {0}",ex.getMessage()),ex);
            }
            //Insert audit Log for LID Mer
            masterControllerService.insertAuditLog((String) session.getAttribute("user"), sourceEuid, destnEuid, "LID Merge Confirm", new Integer(screenObject.getID()).intValue(), "View two selected EUIDs of the merge confirm page");
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC061: Failed to insert audit Log for LID Merge {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("SRC062: Failed to insert audit Log for LID Merge {0}",ex.getMessage()),ex);
        }
      }

    public String mergePreviewSystemObject() {

        String[] lids = this.formlids.split(":");
        String sourceLid = lids[0];
        String destnLid = lids[1];
        try{
           SystemObject so = masterControllerService.getSystemObject(this.lidsource,  destnLid);
             
           CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        
            HashMap destnMap  = (HashMap) compareDuplicateManager.getSystemObjectAsHashMap(so, screenObject).get("SYSTEM_OBJECT_EDIT");
           
            String[] selectedFieldsValue = this.getSelectedMergeFields().split(">>");

            //when user modifies the person fields the only  update the enterprise object
            if (selectedFieldsValue.length > 1) {
                //Modify destination SO values with selected values 
                for (int i = 0; i < selectedFieldsValue.length; i++) {
                    String[] sourceEuidFull = selectedFieldsValue[i].split("##");
                    destnMap.put(sourceEuidFull[0], sourceEuidFull[1]);
                }
            }
            
            SystemObject finalMergredDestnSO  = masterControllerService.mergeSystemObject(this.lidsource, sourceLid, destnLid, destnMap);
            ArrayList finalMergredDestnEOArrayList = new ArrayList();
            
            finalMergredDestnEOArrayList.add(compareDuplicateManager.getSystemObjectAsHashMap(finalMergredDestnSO, screenObject));
            
            session.removeAttribute("soHashMapArrayList");
            
            session.setAttribute("soHashMapArrayList",finalMergredDestnEOArrayList);            
            request.setAttribute("lids", lids);
            request.setAttribute("lidsource", this.lidsource);
            request.setAttribute("mergeComplete", "mergeComplete");			

        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC063: Failed to get merge System object preview {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("SRC064: Failed to get merge System object preview {0}",ex.getMessage()),ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("SRC065: Failed to get merge System object preview {0}",ex.getMessage()),ex);
        }

        return "";
      }

     private HashMap getSystemObjectAsHashMap(SystemObject systemObject) {
                

        try {

            //System.out.println("==> :  LID " + systemObject.getLID() + "===> : Code " + systemObject.getSystemCode());
            
            HashMap systemObjectHashMap = new HashMap();
            //add SystemCode and LID value to the new Hash Map
            systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID()); 
            systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode()); 
            systemObjectHashMap.put("Status", systemObject.getStatus()); // set Status here
            HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);

            //add SystemCode and LID value to the new Hash Map
            systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap); // Set the edit SystemObject here
            //set address array list of hasmap for editing
            ArrayList addressMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Address"), "Address", MasterControllerService.MINOR_OBJECT_UPDATE);

            if (addressMapSOArrayList.size() > 0) {
                systemObjectHashMap.put("SOAddressList", addressMapSOArrayList); 
            }

            //set phone array list of hasmap for editing
            ArrayList phoneMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone", MasterControllerService.MINOR_OBJECT_UPDATE);

            if (phoneMapSOArrayList.size() > 0) {
                systemObjectHashMap.put("SOPhoneList", phoneMapSOArrayList); // set SO phones as arraylist here
            }

            //set alias array list of hasmap for editing
            ArrayList aliasMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias", MasterControllerService.MINOR_OBJECT_UPDATE);

            if (aliasMapSOArrayList.size() > 0) {
                systemObjectHashMap.put("SOAliasList", aliasMapSOArrayList); // set SO alias as arraylist here
            }
        } catch (EPathException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("SRC066: Failed to get  System object  {0}",ex.getMessage()),ex);
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("SRC067: Failed to get  System object  {0}",ex.getMessage()),ex);
        }
         return systemObjectHashMap;   
     
     }

    public ArrayList getMergeLidsList() {
        return mergeLidsList;
    }

    public void setMergeLidsList(ArrayList mergeLidsList) {
        this.mergeLidsList = mergeLidsList;
    }

    public String getFormlids() {
        return formlids;
    }

    public void setFormlids(String formlids) {
        this.formlids = formlids;
    }
         /*
     * Method used to set the lid masking when user picks the system code from the select options.
     *
     * Triggered when value is changed using ValueChangeListener.
     * @param event
     */
    public void setLidMaskValue(ValueChangeEvent event) {
        session.setAttribute("tabName","Merge");
       // get the event with the changed values
        String systemCodeSelected = (String) event.getNewValue();
        String lidMaskValue  = getMaskedValue(systemCodeSelected);
     
        //set mask and its length
        setLidMask(lidMaskValue);
        setLidMaskLength(lidMaskValue.length());
       
        
    }

    private String getMaskedValue(String systemCodeSelected)  {
        String lidMaskValue = new String();
        //System.out.println("systemCodeSelected ==> : " +  systemCodeSelected);
        String[][] lidMaskingArray = masterControllerService.getSystemCodes();

        for (int i = 0; i < lidMaskingArray.length; i++) {
            String[] strings = lidMaskingArray[i];
            //System.out.println("Outer Loop ==> : " +  strings);
            //Get the lid masking values here
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if(systemCodeSelected.equalsIgnoreCase(string)) {
//                     System.out.println( systemCodeSelected + "<=== [" +i + "]"  + "[" +j + "]" + "Inner Loop ==> : ");
                     lidMaskValue = lidMaskingArray[i+1][j];
                }
                
            }
        }
        
        return lidMaskValue;
    }
    public int getLidMaskLength() {
        return lidMaskLength;
    }

    public void setLidMaskLength(int lidMaskLength) {
        this.lidMaskLength = lidMaskLength;
    }

    public String getLidMask() {
        return lidMask;
    }

    public void setLidMask(String lidMask) {
        this.lidMask = lidMask;
    }

    public String getLidsource() {
        return lidsource;
    }

    public void setLidsource(String lidsource) {
        this.lidsource = lidsource;
    }
    /**
     * Method used to cancel the merge operation by the user.
     * Clears the session memory.
     *
     * @return String
     * 
     * @param event
     */
    public String  cancelMergeOperation(){
        session.removeAttribute("soHashMapArrayList");
        return "source-record";
        
    }
    /**
     * 
     * @param event
     */
    
    public void viewEUID(ActionEvent event){
        try {

          
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            HashMap systemObjectMap = (HashMap) event.getComponent().getAttributes().get("soValueExpressionMerge");
          
            SystemObject systemObject = masterControllerService.getSystemObject((String) systemObjectMap.get(MasterControllerService.SYSTEM_CODE), (String) systemObjectMap.get(MasterControllerService.LID));
         

            EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(systemObject);
            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            ArrayList newEOArrayList = new ArrayList();
            newEOArrayList.add(eoMap);

            session.setAttribute("comapreEuidsArrayList", newEOArrayList);
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SRC068: Failed to view EUID {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
             mLogger.error(mLocalizer.x("SRC069: Failed to view EUID {0}",ex.getMessage()),ex);
        }
   }

    public String getSelectedMergeFields() {
        return selectedMergeFields;
    }

    public void setSelectedMergeFields(String selectedMergeFields) {
        this.selectedMergeFields = selectedMergeFields;
    }

public HashMap getSortedMap(HashMap hmap)
	{
		HashMap map = new LinkedHashMap();
		List mapKeys = new ArrayList(hmap.keySet());
		List mapValues = new ArrayList(hmap.values());
		hmap.clear();
		TreeSet sortedSet = new TreeSet(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
//		a) Ascending sort
 		for (int i=0; i<size; i++)
		{
 
		map.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]);
     
		}
		return map;
	}

     
}
