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
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.FieldConfigGroup;
import com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig;
import com.sun.mdm.index.edm.services.configuration.ScreenObject;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.validation.exception.ValidationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.model.SelectItem;
/**
 *
 * @author admin
 */
public class SourceMergeHandler {
    
     private String lid1;
     
     private String lid2;
     
     private String lid3;
     
     private String lid4;
     
     private String source;
     
     private String viewLids="Merge_View_Lids";
     
    //Adding the following variable for getting the select options if the FieldConfig type is "Menu List"
    private ArrayList<SelectItem> selectOptions = new ArrayList();
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    MasterControllerService masterControllerService = new MasterControllerService();
    
    ResourceBundle bundle = ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.Edm",FacesContext.getCurrentInstance().getViewRoot().getLocale());
 
    SourceHandler sourceHandler = new SourceHandler();
    Object[] resultsConfigFeilds = sourceHandler.getAllFieldConfigs().toArray();
    Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
    Object[] addressConfigFeilds = sourceHandler.getAddressFieldConfigs().toArray();
    Object[] aliasConfigFeilds = sourceHandler.getAliasFieldConfigs().toArray();
    Object[] phoneConfigFeilds = sourceHandler.getPhoneFieldConfigs().toArray();
    Object[] auxidConfigFeilds = sourceHandler.getAuxIdFieldConfigs().toArray();
    Object[] commentConfigFeilds = sourceHandler.getCommentFieldConfigs().toArray();
    
    EPathArrayList personEPathArrayList = sourceHandler.buildPersonEpaths();
    private ArrayList soArrayList = new ArrayList();
    private HashMap systemObjectHashMap;

    private ArrayList mergeLidsList  = new ArrayList();
         
    
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
        String[] pullDownListItems = systemCodes[0];
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < pullDownListItems.length; i++) {
            SelectItem selectItem = new SelectItem();
            System.out.println("Adding Select item label" + pullDownListItems[i] + "Value" + pullDownListItems[i]);
            selectItem.setLabel(pullDownListItems[i]);
            selectItem.setValue(pullDownListItems[i]);
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
    

     public String performLidMergeSearch () {
         session.setAttribute("tabName","Merge");
        try {
            //System.out.println("LID1  + =====> " + this.getLid1());
            //System.out.println("LID2  + =====> " + this.getLid2());
            //System.out.println("LID3  + =====> " + this.getLid3());
            //System.out.println("LID4  + =====> " + this.getLid4());
            
            SystemObject systemObjectLID = null;
            ArrayList newArrayList  = new ArrayList();
            if (this.getLid1() != null && this.getLid1().trim().length()>0 ) {
                String lid1 = this.getLid1().replaceAll("-", "");
                this.setLid1(lid1);
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid1);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    String errorMessage = bundle.getString("system_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                 } else {
                    newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid2() != null && this.getLid2().trim().length()>0) {
                String lid2 = this.getLid2().replaceAll("-", ""); 
                this.setLid2(lid2);
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid2);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    String errorMessage = bundle.getString("system_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid3() != null && this.getLid3().trim().length()>0) {
                 String lid3 = this.getLid3().replaceAll("-", ""); 
                 this.setLid3(lid3);
                 systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid3);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                   String errorMessage = bundle.getString("system_object_not_found_error_message");
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(systemObjectLID);
                }
            }
            if (this.getLid4() != null && this.getLid4().trim().length()>0) {
                String lid4 = this.getLid4().replaceAll("-", "");
                this.setLid4(lid4);
                systemObjectLID = masterControllerService.getSystemObject(this.source, this.lid4);
                //Throw exception if SO is found null.
                if (systemObjectLID == null) {
                    String errorMessage = bundle.getString("system_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    newArrayList.add(systemObjectLID);
                }
            }
            ArrayList newSoArrayList  = new ArrayList();
        for (int i = 0; i < newArrayList.size(); i++) {
                SystemObject systemObject = (SystemObject) newArrayList.get(i);
                //System.out.println(i + "==> :  LID " + systemObject.getLID() + "===> : Code " + systemObject.getSystemCode());
                //HashMap systemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                HashMap systemObjectHashMap = new HashMap();
                //add SystemCode and LID value to the new Hash Map
                systemObjectHashMap.put(MasterControllerService.LID, systemObject.getLID());// set LID here 
                systemObjectHashMap.put(MasterControllerService.SYSTEM_CODE, systemObject.getSystemCode());// set System code here 
                systemObjectHashMap.put("Status", systemObject.getStatus());// set Status here 
                
                HashMap editSystemObjectHashMap = masterControllerService.getSystemObjectAsHashMap(systemObject, personEPathArrayList);
                
                //add SystemCode and LID value to the new Hash Map
                systemObjectHashMap.put("SYSTEM_OBJECT", editSystemObjectHashMap);// Set the edit SystemObject here

                //set address array list of hasmap for editing
                ArrayList addressMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Address"), "Address", MasterControllerService.MINOR_OBJECT_UPDATE);
            
                    systemObjectHashMap.put("SOAddressList", addressMapSOArrayList);// set SO addresses as arraylist here    

                //set phone array list of hasmap for editing
                ArrayList phoneMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Phone"), "Phone", MasterControllerService.MINOR_OBJECT_UPDATE);
                
                    systemObjectHashMap.put("SOPhoneList", phoneMapSOArrayList);// set SO phones as arraylist here    

                //set alias array list of hasmap for editing
                ArrayList aliasMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Alias"), "Alias", MasterControllerService.MINOR_OBJECT_UPDATE);
                
                    systemObjectHashMap.put("SOAliasList", aliasMapSOArrayList);// set SO alias as arraylist here
                //set auxid array list of hasmap for editing
                ArrayList auxIdMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId", MasterControllerService.MINOR_OBJECT_UPDATE);
                
                    systemObjectHashMap.put("SOAuxIdList", auxIdMapSOArrayList);// set SO auxId as arraylist here
                //set comment array list of hasmap for editing
                ArrayList commentMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment", MasterControllerService.MINOR_OBJECT_UPDATE);
                
                    systemObjectHashMap.put("SOCommentList", commentMapSOArrayList);// set SO Comment as arraylist here

                //build the system object hashmap for editing 
                newSoArrayList.add(systemObjectHashMap);
                
                //System.out.println("IN ACTION EVENT ===> : this.editSOMinorObjectsHashMapArrayList" + this.editSOMinorObjectsHashMapArrayList);
            }
            
            if(newSoArrayList.size() > 0) {
                setSoArrayList(newSoArrayList);
            }
            //System.out.println("IN ACTION EVENT ===> : this.soArrayList" + this.soArrayList);
            //System.out.println("IN ACTION EVENT ===> : this.newSoArrayList" + newSoArrayList);
            session.setAttribute("soHashMapArrayList", newSoArrayList);
            
       } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
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

        HashMap fieldValuesMerge = (HashMap)session.getAttribute("mergedSOMap");
        //System.out.println("fieldValuesMerge ==> " + fieldValuesMerge);
        if (fieldValuesMerge != null) {
            //System.out.println("Before Changing the hashmap for " + fnameExpression + "with" + fieldValuesMerge.get(fnameExpression));
            fieldValuesMerge.put(fnameExpression, fvalueValueExpression); //set the value for the preview section
            session.setAttribute("mergedSOMap", fieldValuesMerge);  //restore the session object again.
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

        //System.out.println("=====IN mergePreviewEnterpriseObject ====" + mergedHashMapValueExpression);
        
        String sbrLID =  (String) mergedHashMapValueExpression.get("LID");
        String destnId = (sbrLID.equalsIgnoreCase(srcLIDValueExpression))?destnLIDValueExpression:srcLIDValueExpression;
        
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        try{
            SystemObject sourceSO = masterControllerService.getSystemObject(this.source,sbrLID);
            SystemObject destinationSO = masterControllerService.getSystemObject(this.source,destnId);
            
            SystemObject finalMergredDestnSOPreview  = masterControllerService.getPostMergeSystemObject(this.source, sbrLID, destnId, mergedHashMapValueExpression);
            
            request.setAttribute("mergedSOMap", getSystemObjectAsHashMap(finalMergredDestnSOPreview));
            
        } catch (ProcessingException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("=====1====" + mergredHashMapVaueExpression.get("Person.FirstName"));
      }
/**
     * 
     * @param event
     */

    public void mergePreviewSystemObject(ActionEvent event) {
       
        String srcLIDValueExpression = (String) event.getComponent().getAttributes().get("mainEOValueExpression");
        String destnLIDValueExpression = (String) event.getComponent().getAttributes().get("duplicateEOValueExpression");

        CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
        HashMap mergredHashMapVaueExpression = (HashMap) event.getComponent().getAttributes().get("mergedEOValueExpression");

        //System.out.println("=====IN mergePreviewEnterpriseObject ====" + mergredHashMapVaueExpression);
        
        String sbrLID =  (String) mergredHashMapVaueExpression.get("LID");
        String destnId = (sbrLID.equalsIgnoreCase(srcLIDValueExpression))?destnLIDValueExpression:srcLIDValueExpression;
        
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        try{
            SystemObject sourceSO = masterControllerService.getSystemObject(this.source,sbrLID);
            SystemObject destinationSO = masterControllerService.getSystemObject(this.source,destnId);
            
            SystemObject finalMergredDestnSO  = masterControllerService.mergeSystemObject(this.source, sbrLID, destnId, mergredHashMapVaueExpression);
            ArrayList finalMergredDestnEOArrayList = new ArrayList();
            finalMergredDestnEOArrayList.add(finalMergredDestnSO);
            session.removeAttribute("soHashMapArrayList");
            
            session.setAttribute("mergedSOMap", finalMergredDestnEOArrayList);
            
        } catch (ProcessingException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("=====1====" + mergredHashMapVaueExpression.get("Person.FirstName"));
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
            //set auxid array list of hasmap for editing
            ArrayList auxIdMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("AuxId"), "AuxId", MasterControllerService.MINOR_OBJECT_UPDATE);

            if (auxIdMapSOArrayList.size() > 0) {
                systemObjectHashMap.put("SOAuxIdList", auxIdMapSOArrayList); // set SO auxId as arraylist here
            }
            //set comment array list of hasmap for editing
            ArrayList commentMapSOArrayList = masterControllerService.getSystemObjectChildrenArrayList(systemObject, sourceHandler.buildSystemObjectEpaths("Comment"), "Comment", MasterControllerService.MINOR_OBJECT_UPDATE);

            if (commentMapSOArrayList.size() > 0) {
                systemObjectHashMap.put("SOCommentList", commentMapSOArrayList); // set SO Comment as arraylist here
            }
        } catch (EPathException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ObjectException ex) {
            Logger.getLogger(SourceMergeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         return systemObjectHashMap;   
     
     }

    public ArrayList getMergeLidsList() {
        return mergeLidsList;
    }

    public void setMergeLidsList(ArrayList mergeLidsList) {
        this.mergeLidsList = mergeLidsList;
    }
     
}