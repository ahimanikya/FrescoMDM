/*
 * SourceMergeHandler.java
 *
 * Created on September 12, 2007, 6:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.sun.mdm.index.edm.presentation.handlers;   

import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.model.SelectItem;
import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.util.QwsUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.ValidationException;
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
     
    /**Adding the following variable for getting the select options if the FieldConfig type is "Menu List"**/
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
    String exceptionMessaage = bundle.getString("EXCEPTION_MSG");
    String enterLidsMessaage = bundle.getString("lid_enter_more_lids") + " " + ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
    SourceHandler sourceHandler = new SourceHandler();
    Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
    Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
    
    EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();
    private ArrayList soArrayList = new ArrayList();
    
    /**HashMap used for the updated root node values for the surviving system object during merge process**/
    private HashMap destnRootNodeHashMap = new HashMap();
    /**ArrayList used for the updated minor object values for the surviving system object during merge process**/
    private ArrayList destnMinorobjectsList = new ArrayList();
    
    /**HashMap used for the prewview of the surviving system object during merge process**/
    private HashMap soMergePreviewMap = new HashMap();
    
    /** Selected merge fields */
    private String selectedMergeFields = new String();

    /** local Id Designation as per midm.xml file*/
    String localIdDesignation =	 ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
    
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
        
        }
        HashMap  sortedSyscode = getSortedMap(SystemCodeDesc);
        
            Set  sysCodeSet =  sortedSyscode.keySet();
            Iterator it = sysCodeSet.iterator();
            while(it.hasNext()){
            SelectItem selectItem = new SelectItem();
            String  sysCode  = (String)it.next();
            String sysDesc = masterControllerService.getSystemDescription(sysCode);
            SystemCodeDesc.put(sysCode, sysDesc);
             
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
    
     /** 
     * Added on 25/06/2008
     * 
     * This method is used to get the preview for the surviving system object. This method is called from the ajax services.
     *
     * @return HashMap of final surviving system object as hashmap if preview merge is successfull.
     *         null if getting merge preview fails or any exception occurs.
     * 
     */

  
  public HashMap previewLIDMerge() {

        String[] lids = this.formlids.split(":");
        String sourceLid = lids[0];
        String destnLid = lids[1];
        //request.setAttribute("lids", lids);
        //request.setAttribute("lidsource", this.lidsource);

        MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
        String sourceEuid = new String();
        String destnEuid = new String();

        try {
            sourceEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, sourceLid)).getEUID();
            destnEuid = masterControllerService.getEnterpriseObjectForSO(masterControllerService.getSystemObject(this.lidsource, destnLid)).getEUID();
            SystemObject finalMergredDestnSOPreview = masterControllerService.getPostMergeSystemObject(this.lidsource, sourceLid, destnLid);
            //request.setAttribute("mergedSOMap", midmUtilityManager.getSystemObjectAsHashMap(finalMergredDestnSOPreview, screenObject));
            setSoMergePreviewMap(midmUtilityManager.getSystemObjectAsHashMap(finalMergredDestnSOPreview, screenObject));
            
            // keep the src and destn lids and the source selected in the 
            getSoMergePreviewMap().put("SRC_DESTN_LIDS", lids);
            getSoMergePreviewMap().put("LID_SOURCE", this.lidsource);

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SRCMRG045: Failed to generate LID preview {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SRCMRG046: Failed to generate LID preview {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SRCMRG047: Failed to generate LID preview {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SRCHND004: Error  occurred Failed to generate LID preview"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SRCHND005: Error  occurred Failed to generate LID preview"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
          
          return null; 
      }

      try {
          //Insert audit Log for LID Merge
          masterControllerService.insertAuditLog((String) session.getAttribute("user"), sourceEuid, destnEuid, "LID Merge - Selection", new Integer(screenObject.getID()).intValue(), "View two selected EUIDs of the LID merge confirm page");

      } catch (Exception ex) {
          if (ex instanceof ValidationException) {
              mLogger.error(mLocalizer.x("SRCMRG048: Failed to insert audit Log for LID Merge {0}", ex.getMessage()), ex);
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
          } else if (ex instanceof UserException) {
              mLogger.error(mLocalizer.x("SRCMRG049: Failed to insert audit Log for LID Merge {0}", ex.getMessage()), ex);
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
          } else if (!(ex instanceof ProcessingException)) {
              mLogger.error(mLocalizer.x("SRCMRG090: Failed to insert audit Log for LID Merge {0}", ex.getMessage()), ex);
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
          } else if (ex instanceof ProcessingException) {
              String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
              if (exceptionMessage.indexOf("stack trace") != -1) {
                  String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                  if (exceptionMessage.indexOf("message=") != -1) {
                      parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                  }
                  mLogger.error(mLocalizer.x("SRCHND004: Error  occurred"), ex);
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
              } else {
                  mLogger.error(mLocalizer.x("SRCHND005: Error  occurred"), ex);
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
              }
          }
          return null;
      }

        return getSoMergePreviewMap(); 

    }
  
  
     public String performLidMergeSearch () {
        session.removeAttribute("soHashMapArrayList");
        session.setAttribute("tabName","Merge");
        String eoStatus = new String();
        try {
            String errorMessage = bundle.getString("system_object_not_found_error_message");					  
            errorMessage += this.source ; 
            SystemObject systemObjectLID = null;
            ArrayList newArrayList  = new ArrayList();
	    MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source)  + "/" + this.getLid1() + " is " + systemObjectLID.getStatus()));
                    }
                    //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + eoStatus));
                    } 
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) { 
                        newArrayList.add(systemObjectLID);
                    }
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + systemObjectLID.getStatus()));
                    }
                    //get the status of the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + eoStatus));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                         newArrayList.add(systemObjectLID);
                    }
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + systemObjectLID.getStatus()));
                    }
                     //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
             
                     //get the status of the EO
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + eoStatus));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                            newArrayList.add(systemObjectLID);
                    }
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                    }
                     //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    //get the status of the EO
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + eoStatus));
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                            newArrayList.add(systemObjectLID);
                    }
                }
            }
            if(validateSystemCode) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            }

            ArrayList newSoArrayList  = new ArrayList();
            for (int i = 0; i < newArrayList.size(); i++) {
                SystemObject systemObject = (SystemObject) newArrayList.get(i);
                //HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                HashMap editSystemObjectHashMap = (HashMap) midmUtilityManager.getSystemObjectAsHashMap(systemObject, screenObject);
                newSoArrayList.add(editSystemObjectHashMap);
            }
    
            if(newSoArrayList.size() > 0) {
                setSoArrayList(newSoArrayList);
                session.setAttribute("soHashMapArrayList", newSoArrayList);
                if(newSoArrayList.size() ==1 ) { 
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,enterLidsMessaage,enterLidsMessaage));
                }
            }            

        } catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SRCMRG153: Failed to search  LIDMerge {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SRCMRG154: Failed to search  LIDMerge {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SRCMRG155: Failed to search  LIDMerge {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SRCHND004: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SRCHND005: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
                }
        return "LID Details";
    }
     public ArrayList sourcerecordMergeSearch () {
        session.removeAttribute("soHashMapArrayList");
        session.setAttribute("tabName","Merge");
        String eoStatus = new String();
        ArrayList newArrayList  = new ArrayList();
        ArrayList newSoArrayList  = new ArrayList();
        try {
            String errorMessage = bundle.getString("system_object_not_found_error_message");					  
            errorMessage += sourceHandler.getSystemCodeDescription(this.source) ; 
            SystemObject systemObjectLID = null;
	    MidmUtilityManager midmUtilityManager = new MidmUtilityManager();

            boolean validateSystemCode = false;
            if (this.getLid1() != null && this.getLid1().trim().length()>0 ) {
                
                //check the lid masking here.
                String sysMasking = getMaskedValue(this.source);
                boolean isMaskValid = true;

                isMaskValid = sourceHandler.checkMasking(this.getLid1(), sysMasking);
                if (!isMaskValid) {
                    errorMessage = localIdDesignation +" 1 " + bundle.getString("lid_format_error_text") + sysMasking;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    return null;
                }          
                                
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source)  + "/" + this.getLid1() + " is " + systemObjectLID.getStatus()));
                        return null;
                    }
                    //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid1() + " is " + eoStatus));
                        return null;
                    } 
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) { 
                        newArrayList.add(systemObjectLID);
                    }
                }
            }
            if (this.getLid2() != null && this.getLid2().trim().length()>0) {
                //check the lid masking here.
                String sysMasking = getMaskedValue(this.source);
                boolean isMaskValid = true;

                isMaskValid = sourceHandler.checkMasking(this.getLid2(), sysMasking);
                if (!isMaskValid) {
                    errorMessage = localIdDesignation +" 2 " + bundle.getString("lid_format_error_text") + sysMasking;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    return null;
                }                 
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + systemObjectLID.getStatus()));
                        return null;
                    }
                    //get the status of the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid2() + " is " + eoStatus));
                        return null;
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                         newArrayList.add(systemObjectLID);
                    }
                }
            }
            if (this.getLid3() != null && this.getLid3().trim().length()>0) {
                //check the lid masking here.
                String sysMasking = getMaskedValue(this.source);
                boolean isMaskValid = true;

                isMaskValid = sourceHandler.checkMasking(this.getLid3(), sysMasking);
                if (!isMaskValid) {
                    errorMessage = localIdDesignation +" 3 " + bundle.getString("lid_format_error_text") + sysMasking;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    return null;
                }                 
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + systemObjectLID.getStatus()));
                        return null;
                    }
                     //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
             
                     //get the status of the EO
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid3() + " is " + eoStatus));
                        return null;
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                            newArrayList.add(systemObjectLID);
                    }
                }
            }
            if (this.getLid4() != null && this.getLid4().trim().length()>0) {
                //check the lid masking here.
                String sysMasking = getMaskedValue(this.source);
                boolean isMaskValid = true;

                isMaskValid = sourceHandler.checkMasking(this.getLid4(), sysMasking);
                if (!isMaskValid) {
                    errorMessage = localIdDesignation +" 4 " + bundle.getString("lid_format_error_text") + sysMasking;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    return null;
                }                 
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + systemObjectLID.getStatus(), sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + systemObjectLID.getStatus()));
                        return null;
                    }
                     //get the status if the EO
                    eoStatus = midmUtilityManager.getEnterpriseObjectStatusForSO(systemObjectLID);
                    //get the status of the EO
                    if("inactive".equalsIgnoreCase(eoStatus) ) {
                           FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "EO for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + eoStatus, "Enterprise Object for " + sourceHandler.getSystemCodeDescription(this.source) + "/" + this.getLid4() + " is " + eoStatus));
                          return null;
                    }
                    //Add only active system objects to the array list
                    if("active".equalsIgnoreCase(eoStatus) && "active".equalsIgnoreCase(systemObjectLID.getStatus())) {
                            newArrayList.add(systemObjectLID);
                    }
                }
            }
            if(validateSystemCode) {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
               return null;
             }

            for (int i = 0; i < newArrayList.size(); i++) {
                SystemObject systemObject = (SystemObject) newArrayList.get(i);
                //HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                HashMap editSystemObjectHashMap = (HashMap) midmUtilityManager.getSystemObjectAsHashMap(systemObject, screenObject);
                newSoArrayList.add(editSystemObjectHashMap);
            }
    
            if(newSoArrayList.size() > 0) {
                setSoArrayList(newSoArrayList);
                session.setAttribute("soHashMapArrayList", newSoArrayList);
                if(newSoArrayList.size() ==1 ) { 
                    FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,enterLidsMessaage,enterLidsMessaage));
                    return null;
                }
            }            
        }catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("SRCMRG053: Failed to search  LIDMerge {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("SRCMRG054: Failed to search LIDMerge {0}", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("SRCMRG055: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("SRCHND004: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("SRCHND005: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
          }
        return newSoArrayList;
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
        HashMap fieldValuesMerge = (HashMap)request.getAttribute("mergedSOMap");
           if (fieldValuesMerge != null) {
             fieldValuesMerge.put(fnameExpression, fvalueValueExpression); //set the value for the preview section
            //request.setAttribute("mergedSOMap", fieldValuesMerge);  //restore the session object again.
          
        }

    }
     
    /** 
     * Added on 25/06/2008
     * 
     * This method is used to Merge the system object. This method is called from the ajax services for merging the system object.
     *
     * @return ArrayList of final surviving system object if merge is successfull.
     *         null if merge fails or any exception occurs.
     * 
     */
    
     public ArrayList mergeSystemObject() {
        ArrayList finalMergredDestnEOArrayList = new ArrayList();
 
        String[] lids = this.formlids.split(":");
        String sourceLid = lids[0];
        String destnLid = lids[1];
        try{
           SystemObject so = masterControllerService.getSystemObject(this.lidsource,  destnLid);
             
           MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
        
            HashMap destnMap  = (HashMap) midmUtilityManager.getSystemObjectAsHashMap(so, screenObject).get("SYSTEM_OBJECT_EDIT");
           
            String[] selectedFieldsValue = this.getSelectedMergeFields().split(">>");

            //when user modifies the person fields the only  update the enterprise object
            if (selectedFieldsValue.length > 1) {
                //Modify destination SO values with selected values 
                for (int i = 0; i < selectedFieldsValue.length; i++) {
                    String[] sourceEuidFull = selectedFieldsValue[i].split("##");
                    destnMap.put(sourceEuidFull[0], sourceEuidFull[1]);
                }
            }
            SystemObject finalMergredDestnSO  = masterControllerService.mergeSystemObject(this.lidsource, 
                                                                                          sourceLid, 
                                                                                          destnLid, 
                                                                                          this.destnRootNodeHashMap,
                                                                                          this.destnMinorobjectsList);
 
            finalMergredDestnEOArrayList.add(midmUtilityManager.getSystemObjectAsHashMap(finalMergredDestnSO, screenObject));
            
            session.removeAttribute("soHashMapArrayList");
            
            session.setAttribute("soHashMapArrayList",finalMergredDestnEOArrayList);            
            //request.setAttribute("lids", lids);
            //request.setAttribute("lidsource", this.lidsource);
            //request.setAttribute("mergeComplete", "mergeComplete");			


         } catch (Exception ex) {
             if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("SRCMRG063: Failed to get merge System object preview {0}", ex.getMessage()), ex);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
             } else if (ex instanceof UserException) {
                 mLogger.error(mLocalizer.x("SRCMRG064: Failed to get merge System object preview {0}", ex.getMessage()), ex);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
             } else if (!(ex instanceof ProcessingException)) {
                 mLogger.error(mLocalizer.x("SRCMRG065: Failed to get merge System object preview {0}", ex.getMessage()), ex);
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
             } else if (ex instanceof ProcessingException) {
                 String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                 if (exceptionMessage.indexOf("stack trace") != -1) {
                     String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                     if (exceptionMessage.indexOf("message=") != -1) {
                         parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                     }
                     mLogger.error(mLocalizer.x("SRCHND004: Error  occurred"), ex);
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                 } else {
                     mLogger.error(mLocalizer.x("SRCHND005: Error  occurred"), ex);
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                 }
             }
             return null;
         }
        return finalMergredDestnEOArrayList;
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

          
            MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
            HashMap systemObjectMap = (HashMap) event.getComponent().getAttributes().get("soValueExpressionMerge");
          
            SystemObject systemObject = masterControllerService.getSystemObject((String) systemObjectMap.get(MasterControllerService.SYSTEM_CODE), (String) systemObjectMap.get(MasterControllerService.LID));
         

            EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(systemObject);
            HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            ArrayList newEOArrayList = new ArrayList();
            newEOArrayList.add(eoMap);

            session.setAttribute("comapreEuidsArrayList", newEOArrayList);
        } catch (ProcessingException ex) {
            mLogger.error(mLocalizer.x("SRCMRG068: Failed to view EUID {0}",ex.getMessage()),ex);
        } catch (UserException ex) {
             mLogger.error(mLocalizer.x("SRCMRG069: Failed to view EUID {0}",ex.getMessage()),ex);
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

    public HashMap getDestnRootNodeHashMap() {
        return destnRootNodeHashMap;
    }

    public void setDestnRootNodeHashMap(HashMap destnRootNodeHashMap) {
        this.destnRootNodeHashMap = destnRootNodeHashMap;
    }

    public ArrayList getDestnMinorobjectsList() {
        return destnMinorobjectsList;
    }

    public void setDestnMinorobjectsList(ArrayList destnMinorobjectsList) {
        this.destnMinorobjectsList = destnMinorobjectsList;
    }
    
    /**
     *
     * <b>Purpose:</b>
     * Method used to check whether the selected minor object type is avaiable in the preview. <br>
     * 
     * @since 02-July-2008<b>
     *
     * @return <b>true</b> if the minorobject type not found in the preview <br>
     *         <b>false</b> if minorobject type already found in the preview
     * 
     * @param minorObjectsListPreview   Arraylist of Minor objects available in preview 
     * @param minorObjectHashMap        Minor objects HashMap for comparision
     * @param keyType                   Key type of the minor object value for checking  
      * 
     */
    
    public boolean isNotAvailableInPreview(ArrayList minorObjectsListPreview, HashMap minorObjectHashMap, String keyType) {
        HashMap returnHashMap = new HashMap();
        if (minorObjectsListPreview.size() == 0) {
            return true;
        }
        boolean retValue = true;
        for (int mo = 0; mo < minorObjectsListPreview.size(); mo++) {
            HashMap previewMinorMap = (HashMap) minorObjectsListPreview.get(mo);
            String previewMinorType = (String) previewMinorMap.get(keyType);
            //Build the hashmap with the preview key types for each minor object type
            returnHashMap.put(previewMinorType, previewMinorType);
        }
         
        String minorObjectKeyType = (String) minorObjectHashMap.get(keyType);
        
         
        //Minor object with key type already exists
        if (returnHashMap.get(minorObjectKeyType) != null) {
            retValue = false;
        } else {
            retValue = true;
        }
        return retValue;
    }

    

    /**HashMap used for the prewview of the surviving system object during merge process**/
    public HashMap getSoMergePreviewMap() {
        return soMergePreviewMap;
    }

    public void setSoMergePreviewMap(HashMap soMergePreviewMap) {
        this.soMergePreviewMap = soMergePreviewMap;
    }
    
    private String getMaskedValue(String systemCodeSelected) {
        String lidMaskValue = new String();
        String[][] lidMaskingArray = masterControllerService.getSystemCodes();

        for (int i = 0; i < lidMaskingArray.length; i++) {
            String[] strings = lidMaskingArray[i];
            //Get the lid masking values here
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j];
                if (systemCodeSelected.equalsIgnoreCase(string)) {
                    lidMaskValue = lidMaskingArray[i + 1][j];
                }

            }
        }

        return lidMaskValue;
    }
       
}
