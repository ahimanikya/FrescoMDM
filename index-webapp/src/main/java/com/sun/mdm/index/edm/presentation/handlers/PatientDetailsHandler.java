/*
 * PatientDetailsHandler.java
 *
 * Created on November 4, 2007, 6:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager;
import com.sun.mdm.index.edm.services.configuration.FieldConfig;
import com.sun.mdm.index.edm.services.configuration.SearchResultsConfig;
import com.sun.mdm.index.edm.services.configuration.SearchScreenConfig;
import com.sun.mdm.index.edm.util.QwsUtil;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.factory.SimpleFactory;
import com.sun.mdm.index.objects.validation.exception.ValidationException;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import com.sun.mdm.index.edm.presentation.valueobjects.PatientDetails;

/**
 * @author Rajani
 */
public class PatientDetailsHandler extends ScreenConfiguration {

    private static final String SEARCH_PATIENT_DETAILS = "Record Details";
    private static final String SEARCH_EUID_DETAILS = "EUID Details";

    //result array list for the out put
    private ArrayList resultArrayList = new ArrayList();
    private String[] euidCheckValues;
    private boolean euidCheckboolean;
    private static final String VALIDATION_ERROR = "validationfailed";
    private PatientDetails[] patientDetailsVO = null;
    private String resolveType  = "AutoResolve";
    private String potentialDuplicateId;
    /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;
    private String singleEUID;
    private String compareEUID;
    private String euid1 = "EUID 1";
    private String euid2 = "EUID 2";
    private String euid3 = "EUID 3";
    private String euid4 = "EUID 4";
    
    private String compareEuids = new String();

    private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler");

    private String mergeEuids = new String();
    private String destnEuid  = new String();
    private String selectedMergeFields = new String();

    
    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
    
    /** Creates a new instance of PatientDetailsHandler */
    
    public PatientDetailsHandler() {

    }
    /**
    @return String
    @throws com.sun.mdm.index.master.ProcessingException 
    @throws com.sun.mdm.index.master.UserException 
     */
    public String performSubmit() throws ProcessingException, UserException {
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
        String errorMessage = null;
        Date date = null;

        try {
            HashMap newFieldValuesMap = new HashMap();

            //System.out.println("---------------1-------------------" + super.getEnteredFieldValues());
            if (super.getEnteredFieldValues() != null && super.getEnteredFieldValues().length() > 0) {
                String[] fieldNameValues = super.getEnteredFieldValues().split(">>");
                for (int i = 0; i < fieldNameValues.length; i++) {
                    String string = fieldNameValues[i];
                    String[] keyValues = string.split("##");
                    if(keyValues.length ==2) {
                      //System.out.println("Key " + keyValues[0] + "Value ==> : " + keyValues[1]);
                      newFieldValuesMap.put(keyValues[0], keyValues[1]);
                    }
                }
            }

            super.setUpdateableFeildsMap(newFieldValuesMap);

            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            
            
            
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "One of Many :: " + errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "One of Many :: " + errorMessage);
                return VALIDATION_ERROR;
            }

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = "Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System Code/LID Validation  :: " + errorMessage, errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                return VALIDATION_ERROR;

            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
                errorMessage = "Please Enter LID Value";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System Code/LID Validation :: " + errorMessage, errorMessage));
                mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                return VALIDATION_ERROR;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = "Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SystemCode Validation :: " + errorMessage, errorMessage));
                    mLogger.error("Validation failed. Message displayed to the user: " + "LID/SystemCode Validation :: " + errorMessage);
                    return VALIDATION_ERROR;

                }
            }


            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (LID.trim().length() > 0 && SystemCode.trim().length() > 0) {
                    try {
                        //remove masking for LID field
                        LID = LID.replaceAll("-", "");
                        SystemObject so = masterControllerService.getSystemObject(SystemCode, LID);
                        if (so == null) {
                            errorMessage = bundle.getString("system_object_not_found_error_message");
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LID/SYSTEM CODE :: " + errorMessage, errorMessage));
                            mLogger.error("Validation failed. Message displayed to the user: " + "LID/SYSTEM CODE :: " + errorMessage);
                            return VALIDATION_ERROR;
                        }
                    } catch (ProcessingException ex) {
                        mLogger.error("ProcessingException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("ProcessingException ex : " + ex.toString());
                        return VALIDATION_ERROR;
                    } catch (UserException ex) {
                        mLogger.error("UserException : " + QwsUtil.getRootCause(ex).getMessage());
                        mLogger.error("UserException ex : " + ex.toString());
                        return VALIDATION_ERROR;
                    }

                }

            }

            //Validate all date fields entered by the user
            if (super.validateDateFields().size() > 0) {
                Object[] messObjs = super.validateDateFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(":");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    return VALIDATION_ERROR;
                }

            }

            //Validate all time fields entered by the user
            if (super.validateTimeFields().size() > 0) {
                Object[] messObjs = super.validateTimeFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(":");
                    //////System.out.println("===> Field" + fieldErrors[0] + "===> Message" + fieldErrors[1]);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.error("Validation failed. Message displayed to the user: " + fieldErrors[0] + " : " + fieldErrors[1]);
                    return VALIDATION_ERROR;
                }

            }


//            //System.out.println("eoSearchCriteria ==>: " + eoSearchCriteria);
            EOSearchResultIterator eoSearchResultIterator = null;

            ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();

            EPathArrayList ePathArrayList = compareDuplicateManager.retrievePatientResultsFields(arlResultsConfig);
            ArrayList sResultsConfigArrayList = screenObject.getSearchResultsConfig();

            EPathArrayList resultFields = new EPathArrayList();

            Iterator srcalIterator = sResultsConfigArrayList.iterator();
            ArrayList newEoArrayList = new ArrayList();
            if (super.getUpdateableFeildsMap().get("Person.EUID") != null) {
                String euid = (String) super.getUpdateableFeildsMap().get("Person.EUID");
                //System.out.println("ONLYYYY EUID ==> ; " + euid);
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
                HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                newEoArrayList.add(eoHashMap);
                httpRequest.setAttribute("comapreEuidsArrayList", newEoArrayList);
                return this.SEARCH_EUID_DETAILS;

            } else if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String lid = (String) super.getUpdateableFeildsMap().get("LID");
                lid = lid.replaceAll("-", "");
                String systemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                //System.out.println("ONLYYYY LID AND SYSTEM CODE ==> ; " + lid + "/" + systemCode);
                SystemObject so = masterControllerService.getSystemObject(systemCode, lid);

                EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                newEoArrayList.add(eoHashMap);
                httpRequest.setAttribute("comapreEuidsArrayList", newEoArrayList);
                return this.SEARCH_EUID_DETAILS;

            } else {
                // SambaG
                // Start here
                String objectRef = null;
                while (srcalIterator.hasNext()) {
                    SearchResultsConfig srConfig = (SearchResultsConfig) srcalIterator.next();
                    ArrayList epaths = srConfig.getEPaths();
                    Iterator ePathsIterator = epaths.iterator();
                    while (ePathsIterator.hasNext()) {
                        String ePathStr = (String) ePathsIterator.next();
                        resultFields.add("Enterprise.SystemSBR." + ePathStr);
                        if (objectRef == null) {
                            int index = ePathStr.indexOf(".");
                            objectRef = ePathStr.substring(0, index);
                        }
                    }
                }

                ArrayList searchScreenArray = super.screenObject.getSearchScreensConfig();
                Iterator searchScreenArrayIter = searchScreenArray.iterator();
                String eoSearchOptionQueryBuilder = new String();
                while (searchScreenArrayIter.hasNext()) {
                    searchScreenConfig = (SearchScreenConfig) searchScreenArrayIter.next();
                    if (searchScreenConfig.getScreenTitle().equalsIgnoreCase(super.getSelectedSearchType())) {
                        //get the EO search option from the EDM.xml file here as per the search type
                        eoSearchOptionQueryBuilder = searchScreenConfig.getOptions().getQueryBuilder();
                    }
                }

                resultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");

                EOSearchOptions eoSearchOptions = new EOSearchOptions(eoSearchOptionQueryBuilder, resultFields);
                ////System.out.println("===1=====seoSearchOptions ==>: " + eoSearchOptions);

                EOSearchCriteria eoSearchCriteria = new EOSearchCriteria();

                HashMap gSearchCriteria = new HashMap();
                HashMap gSearchCriteriaFromDOB = new HashMap();
                HashMap gSearchCriteriaToDOB = new HashMap();

                String feildValue = new String();

                Object[] fcObjects = getScreenConfigArray().toArray();

                //build the search criteria as per the user inputs from the form   
                for (int i = 0; i < fcObjects.length; i++) {
                    FieldConfig objectFieldConfig = (FieldConfig) fcObjects[i];
                    String rootNode = objectFieldConfig.getRootObj();
                    //System.out.println(objectFieldConfig.isRange() + "==>: " + objectFieldConfig.getDisplayName() + "name==> " + objectFieldConfig.getName() + "Value name ==> " + super.getUpdateableFeildsMap().get(objectFieldConfig.getFullFieldName()) + "Value dp ==> " + super.getUpdateableFeildsMap().get(objectFieldConfig.getDisplayName()));

                    //Get the Field Value as per the field config range type
                    if (objectFieldConfig.isRange()) {
                        feildValue = (String) super.getUpdateableFeildsMap().get(objectFieldConfig.getDisplayName());
                    } else {
                        feildValue = (String) super.getUpdateableFeildsMap().get(objectFieldConfig.getFullFieldName());
                    }
                    if (feildValue != null && feildValue.trim().length() > 0) {
                        //Remove all masking fields from the field valued if any like SSN,LID...etc
                        //System.out.println("DOB FROM Putting ==>: BEFORE feildValue==> " + feildValue);
                        feildValue = feildValue.replaceAll("-", "");
                        //System.out.println("DOB FROM Putting ==>: " + objectFieldConfig.getDisplayName() + "feildValue==> " + feildValue);
                        if (objectFieldConfig.getDisplayName().equalsIgnoreCase("DOB From")) {
                            gSearchCriteriaFromDOB.put(objectFieldConfig.getFullFieldName(), feildValue);
                        } else if (objectFieldConfig.getDisplayName().equalsIgnoreCase("DOB To")) {
                            ////System.out.println("DOB TO Putting ==>: " + objectFieldConfig.getDisplayName() + "feildValue==> " +feildValue);
                            gSearchCriteriaToDOB.put(objectFieldConfig.getFullFieldName(), feildValue);
                        } else {
                            ////System.out.println("OTHER Putting ==>: " + objectFieldConfig.getDisplayName() + "feildValue==> " +feildValue);
                            gSearchCriteria.put(objectFieldConfig.getFullFieldName(), feildValue);
                        }

                    }

                }

//            //System.out.println("gSearchCriteria==>: " + gSearchCriteria);
//            //System.out.println("gSearchCriteriaToDOB: " + gSearchCriteriaToDOB);
//            //System.out.println("gSearchCriteriaFromDOB==>: " + gSearchCriteriaFromDOB);

                String objRef = objectRef;
                // following code is from buildObjectNodeFromSearchCriteria()
                //ObjectNode topNode = SimpleFactory.create(objRef);

                SystemObject sysobj = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteria);
                SystemObject sysobj2 = null;
                SystemObject sysobj3 = null;
                if (!gSearchCriteriaFromDOB.isEmpty()) {
                    sysobj2 = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaFromDOB);
                    eoSearchCriteria.setSystemObject2(sysobj2); // for dob from
//                //System.out.println("<<== dob from set....");
                }
                if (!gSearchCriteriaToDOB.isEmpty()) {
                    sysobj3 = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaToDOB);
                    eoSearchCriteria.setSystemObject3(sysobj3); // for dob to
//                //System.out.println("<<== dob t set....");
                }
                eoSearchCriteria.setSystemObject(sysobj);  // for all search attributes other than dob range
                //System.out.println("OTHERRRRSSS ==> ; ");
                eoSearchResultIterator = masterControllerService.searchEnterpriseObject(eoSearchCriteria, eoSearchOptions);

                while (eoSearchResultIterator.hasNext()) {
                    EOSearchResultRecord eoSearchResultRecord = eoSearchResultIterator.next();
                    ObjectNode objectNode = eoSearchResultRecord.getObject();
                    HashMap fieldvalues = new HashMap();

                    for (int m = 0; m < ePathArrayList.size(); m++) {
                        EPath ePath = ePathArrayList.get(m);
                        try {
                            Object value = EPathAPI.getFieldValue(ePath, objectNode);
                            fieldvalues.put(ePath.getName(), value);
                        } catch (Exception npe) {
                        // THIS SHOULD BE FIXED
                        // npe.printStackTrace();
                        }
                    }
                    fieldvalues.put("EUID", eoSearchResultRecord.getEUID());
                    resultArrayList.add(fieldvalues);
                }
                httpRequest.setAttribute("resultArrayListReq", resultArrayList);
                setResultsSize(getPatientDetailsVO().length);

            }
        // End here            
        // SambaG

        } catch (Exception ex) {
            mLogger.error("Exception : " + QwsUtil.getRootCause(ex).getMessage());
            mLogger.error("Exception ex : " + ex.toString());
            return this.VALIDATION_ERROR;
        }
        return this.SEARCH_PATIENT_DETAILS;
    }

    /**
     * 
     * @param event
     */
    public void setPreviewEnterpriseObjectValues(ActionEvent event) {

        String fnameExpression = (String) event.getComponent().getAttributes().get("fnameExpression");
        Object fvalueVaueExpression = (Object) event.getComponent().getAttributes().get("fvalueVaueExpression");
        HashMap eoMultiMergePreviewMap = (HashMap) httpRequest.getAttribute("eoMultiMergePreview");
        
        HashMap mergePersonfieldValuesMapEO = (HashMap) eoMultiMergePreviewMap.get("ENTERPRISE_OBJECT");
        

       // masterControllerService.modifySBR(sbr, hm);

        HashMap fieldValuesMerge = (HashMap) session.getAttribute("mergedEOMap");

        if (fieldValuesMerge != null) {
            fieldValuesMerge.put(fnameExpression, fvalueVaueExpression); //set the value for the preview section
            session.setAttribute("mergedEOMap", fieldValuesMerge);  //restore the session object again.
        }

    }

    /**
     * 
     * @param event
     */
    public String makeDifferentPersonAction() {
        try {
            
            //resolve the potential duplicate as per resolve type
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(this.getResolveType())) ? true : false;

            //flag=false incase of autoresolve
            //flag = true incase of permanant resolve

            masterControllerService.setAsDifferentPerson(this.getPotentialDuplicateId(), resolveBoolean);
            ArrayList modifiedArrayList = new ArrayList();

            ArrayList eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
            //reset the status and set it back in session
            for (int i = 0; i < eoArrayList.size(); i++) {
                HashMap objectHashMap = (HashMap) eoArrayList.get(i);
                //set the resolve type for the selected potential duplicate
                if(this.getPotentialDuplicateId().equals((String)objectHashMap.get("PotDupId"))) {
                  objectHashMap.put("Status", "R");
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        return "Compare Duplicates";
    }

    /**
     * 
     * @param event
     */
    public void unresolvePotentialDuplicateAction(ActionEvent event) {
        try {
//            String sourceEUID = (String) event.getComponent().getAttributes().get("sourceEUID");
//            String destinationEUID = (String) event.getComponent().getAttributes().get("destinationEUID");

            //get potential duplicate ID
            String potDupId = (String) event.getComponent().getAttributes().get("potDupId");
            ArrayList eoArrayList = (ArrayList) event.getComponent().getAttributes().get("eoArrayList");

            session.removeAttribute("comapreEuidsArrayList");

            //un resolve the potential duplicate as per resolve type
            masterControllerService.unresolvePotentialDuplicate(potDupId);
            ArrayList modifiedArrayList = new ArrayList();
            //reset the status and set it back in session
            for (int i = 0; i < eoArrayList.size(); i++) {
                HashMap objectHashMap = (HashMap) eoArrayList.get(i);
                ////System.out.println("IN UN RESOLVEEE" + potDupId + "==="+ objectHashMap.get("PotDupId") + "==> ; " + objectHashMap.get("Status") + objectHashMap.keySet());
                //set the resolve type to "U" (UnResolve)for the selected potential duplicate
                if(potDupId.equals((String)objectHashMap.get("PotDupId"))) {
                  objectHashMap.put("Status", "U");
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 
     * @param event
     */
    public void getEUIDDetails(ActionEvent event) {
        try {
            String euid = (String) event.getComponent().getAttributes().get("euid");

            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
            ArrayList newEOArrayList = new ArrayList();
            newEOArrayList.add(eo);

            session.setAttribute("enterpriseArrayList", newEOArrayList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 
     * @param event
     */
    public void keepEuidsAction(ActionEvent event) {
        String sourceEUID = (String) event.getComponent().getAttributes().get("sourceEUID");
        String destinationEUID = (String) event.getComponent().getAttributes().get("destinationEUID");

        //set both source and destination euids in the session
        session.setAttribute("sourceEUIDSessionObj", sourceEUID);
        session.setAttribute("destinationEUIDSessionObj", destinationEUID);
    }

    /**
     * 
     * @param event 
     * 
     */
    public void getMergedEnterpriseObject(ActionEvent event) {
        try {
            String sourceEUID = (String) event.getComponent().getAttributes().get("finalSourceEuid");
            String destinationEUID = (String) event.getComponent().getAttributes().get("finalDestinationEuid");

            EnterpriseObject sourceEnterpriseObject = masterControllerService.getEnterpriseObject(sourceEUID);//get source EO
            EnterpriseObject destinationEnterpriseObject = masterControllerService.getEnterpriseObject(destinationEUID);//get destination EO


            //get the merged result for the source and destination using master master controller.
            EnterpriseObject mergeResultEO = masterControllerService.getPostMergeEO(sourceEnterpriseObject, destinationEnterpriseObject);

            
            HashMap fieldValuesMergeMap = compareDuplicateManager.getEOFieldValues(mergeResultEO, screenObject, super.getResultsConfigArray().toArray());
            //Add the EUID to the hashmap
            fieldValuesMergeMap.put("EUID", mergeResultEO.getEUID());

            //set the merged Enterprise object in the session for displaying the preview in compare duplicates screen
            session.setAttribute("mergedEO", mergeResultEO);
            session.setAttribute("mergedEOMap", fieldValuesMergeMap);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

        public String previewPostMultiMergedEnterpriseObject() {
        try {
            //httpRequest.setAttribute("comapreEuidsArrayList", httpRequest.getAttribute("comapreEuidsArrayList"));
            ////System.out.println("===> " + mergeEuids);
            
            ////System.out.println("===> " + destnEuid);

            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnEuid);
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();


            String[] allEUIDs = mergeEuids.split("##");
            
            
            ArrayList srcsList  = new ArrayList();
            for (int i = 0; i < allEUIDs.length; i++) {
                if(i !=0 ) {
                    srcsList.add(allEUIDs[i]);
                }
            }    
            
            Object[] sourceEUIDObjs =  srcsList.toArray();
            
            String[] sourceEUIDs  = new String[srcsList.size()];
            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            httpRequest.setAttribute("sourceEUIDs", sourceEUIDs);

            httpRequest.setAttribute("destnEuid", destnEuid);
            
            EnterpriseObject resulteo = masterControllerService.getPostMergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);

            HashMap eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);

            httpRequest.setAttribute("eoMultiMergePreview", eoMultiMergePreview);


        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.SEARCH_PATIENT_DETAILS;
}
        
        
        
  public String performMultiMergedEnterpriseObject() {
        try {
         
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnEuid);


            HashMap eoHashMap = eoHashMap = (HashMap) compareDuplicateManager.getEnterpriseObjectAsHashMap(destinationEO, screenObject).get("ENTERPRISE_OBJECT");
      
           
            String[] selectedFieldsValue = this.selectedMergeFields.split(">>");


            //when user modifies the person fields the only  update the enterprise object
            if (selectedFieldsValue.length > 1) {
                //Modify destination EO values with selected values 
                for (int i = 0; i < selectedFieldsValue.length; i++) {
                    String[] sourceEuidFull = selectedFieldsValue[i].split("##");
                    eoHashMap.put(sourceEuidFull[0], sourceEuidFull[1]);
//                    //System.out.println(sourceEuidFull[0] + "====" + sourceEuidFull[1]);
                    
                }
                //Modify CHANGED sbr values here
                masterControllerService.modifySBR(destinationEO.getSBR(), eoHashMap);

                masterControllerService.updateEnterpriseObject(destinationEO);

                //get the modifed EO and merge it
                destinationEO = masterControllerService.getEnterpriseObject(destnEuid);
            }
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();
            
            String[] allEUIDs = mergeEuids.split("##");
            
            ArrayList srcsList  = new ArrayList();
            for (int i = 0; i < allEUIDs.length; i++) {
                if(i !=0 ) {
                    srcsList.add(allEUIDs[i]);
                }
            }    
            
            Object[] sourceEUIDObjs =  srcsList.toArray();
            
            String[] sourceEUIDs  = new String[srcsList.size()];
            
            String[] srcRevisionNumbers = new String[sourceEUIDs.length];
            
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            EnterpriseObject resulteo = masterControllerService.mergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);              
            session.removeAttribute("comapreEuidsArrayList");
            
           HashMap eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);

           ArrayList finalMergeList  = new ArrayList();
           finalMergeList.add(eoMultiMergePreview);

            session.setAttribute("comapreEuidsArrayList",finalMergeList);
            
        } catch (ObjectException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.SEARCH_PATIENT_DETAILS;
}        

  
  public void undoMultiMerge(ActionEvent event) {
      httpRequest.removeAttribute("eoMultiMergePreview");
  }
    
    
    
    
    
    public String singleEuidSearch() {
        NavigationHandler navigationHandler = new NavigationHandler();
        session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));

        try {
            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(this.getSingleEUID());
            //Throw exception if EO is found null.
            if (enterpriseObject == null) {
                session.removeAttribute("enterpriseArrayList");
                String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            } else {
                eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                newArrayList.add(eoMap);
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
    } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }

    public String compareEuidSearch() {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 1 :" + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
//                    session.removeAttribute("comapreEuidsArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 2 :" + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 3 :" + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            if (this.getEuid4() != null && !"EUID 4".equalsIgnoreCase(this.getEuid4())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID 4 :" + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
           
//            //System.out.println("===> : " + newArrayList);
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Compare Duplicates";
    }

    public String lookupEuid1() {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
            
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid1() != null && !"EUID 1".equalsIgnoreCase(this.getEuid1())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid1());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }

    public String lookupEuid2() {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }

    public String lookupEuid3() {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }

    public String lookupEuid4() {
        try {
            NavigationHandler navigationHandler = new NavigationHandler();
            session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
            EnterpriseObject enterpriseObject = null;

            ArrayList newArrayList = new ArrayList();
            HashMap eoMap = new HashMap();

            if (this.getEuid4() != null && !"EUID 4".equalsIgnoreCase(this.getEuid4())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    session.removeAttribute("enterpriseArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                }
            }
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "EUID Details";
    }


    public String[] getStringEUIDs(String euids) {

        StringTokenizer stringTokenizer = new StringTokenizer(euids, ",");
        String[] euidsArray = new String[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            euidsArray[i] = new String(stringTokenizer.nextElement().toString());
            i++;
        }
        return euidsArray;
    }

    /**
     * 
     * @param event
     */
    public void activateEO(ActionEvent event) {
        try {
            String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(euid);

            //Activate the enterprise object
            masterControllerService.activateEnterpriseObject(enterpriseObject.getEUID());

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());

            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(updatedEnterpriseObject, screenObject);

            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(eoMap);

            //Keep the updated SO in the session again
            httpRequest.setAttribute("comapreEuidsArrayList", updatedEOList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    /**
     * 
     * @param event
     */
    public void deactivateEO(ActionEvent event) {
        try {

            String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(euid);


            //Deactivate the enterprise object
            masterControllerService.deactivateEnterpriseObject(enterpriseObject.getEUID());

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());

            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(updatedEnterpriseObject, screenObject);

            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(eoMap);

            //Keep the updated SO in the session again
            httpRequest.setAttribute("comapreEuidsArrayList", updatedEOList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    //Keep the updated SO in the session again
    }
    //get and setters for resultsArray
    public ArrayList getResultArrayList() {
        return resultArrayList;
    }

    public void setResultArrayList(ArrayList resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

    public String[] getEuidCheckValues() {
        return euidCheckValues;
    }

    public void setEuidCheckValues(String[] euidCheckValues) {
        this.euidCheckValues = euidCheckValues;
    }

    public boolean getEuidCheckboolean() {
        return euidCheckboolean;
    }

    public void setEuidCheckboolean(boolean euidCheckboolean) {
        this.euidCheckboolean = euidCheckboolean;
    }

    private SystemObject buildObjectNodeFromSearchCriteria(String objectRef, HashMap gSearchCriteria) throws ObjectException, ValidationException {
        ObjectNode topNode = SimpleFactory.create(objectRef);

        for (Iterator i = gSearchCriteria.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) gSearchCriteria.get(key);
            if ((value != null) && (value.trim().length() > 0)) {
                int index = key.indexOf(".");

                if (index > -1) {
                    String tmpRef = key.substring(0, index);
                    String fieldName = key.substring(index + 1);

                    if (tmpRef.equalsIgnoreCase(objectRef)) {
                        QwsUtil.setObjectNodeFieldValue(topNode, fieldName, value);
                    } else {
                        ArrayList childNodes = topNode.pGetChildren(tmpRef);
                        ObjectNode node = null;

                        if (childNodes == null) {
                            node = SimpleFactory.create(tmpRef);
                            topNode.addChild(node);
                        } else {
                            node = (ObjectNode) childNodes.get(0);
                        }
                        QwsUtil.setObjectNodeFieldValue(node, fieldName, value);
                    }
                }
            }
        }
        SystemObject sysobj = (SystemObject) SimpleFactory.create("SystemObject");
        sysobj.setValue("ChildType", objectRef);
        sysobj.setObject(topNode);
        return sysobj;
    }

    /**
     * 
     * @param event
     */
    public void mergePreviewEnterpriseObject(ActionEvent event) {

        String srcEUIDVaueExpression = (String) event.getComponent().getAttributes().get("mainEOVaueExpression");
        String destnEUIDVaueExpression = (String) event.getComponent().getAttributes().get("duplicateEOVaueExpression");
        HashMap mergredHashMapVaueExpression = (HashMap) event.getComponent().getAttributes().get("mergedEOValueExpression");
        String sbrEUID = (String) mergredHashMapVaueExpression.get("EUID");
        String destnId = (sbrEUID.equalsIgnoreCase(srcEUIDVaueExpression)) ? destnEUIDVaueExpression : srcEUIDVaueExpression;
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        SBR finalSBR = compareDuplicateManager.getEnterpriseObject(sbrEUID).getSBR();
        //Object[] resultsConfigFeilds  = getSearchResultsScreenConfigArray().toArray();
        SourceHandler sourceHandler = new SourceHandler();
        Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();

        try {
            for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                FieldConfig fieldConfig = (FieldConfig) personConfigFeilds[ifc];

                Object strValue = mergredHashMapVaueExpression.get(fieldConfig.getFullFieldName());
                String dateField;
                String fieldName = fieldConfig.getFullFieldName().substring(fieldConfig.getFullFieldName().indexOf(".") + 1, fieldConfig.getFullFieldName().length());
                if (strValue != null) {
                    if (fieldConfig.getValueType() == 6) {
                        dateField = simpleDateFormatFields.format(mergredHashMapVaueExpression.get(fieldConfig.getFullFieldName()));
                        QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, dateField, finalSBR);
                    } else {
                        QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, strValue.toString(), finalSBR);
                    }
                } else {
                    QwsUtil.setObjectNodeFieldValue(finalSBR.getObject(), fieldName, null, finalSBR);
                }
            }
            EnterpriseObject sourceEO = masterControllerService.getEnterpriseObject(sbrEUID);
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnId);

            MergeResult mergeResult = masterControllerService.mergeEnterpriseObject(sourceEO, destinationEO);
            EnterpriseObject finalMergredDestnEO = mergeResult.getDestinationEO();
            ArrayList finalMergredDestnEOArrayList = new ArrayList();
            finalMergredDestnEOArrayList.add(finalMergredDestnEO);
            session.removeAttribute("mergedEOMap");
            session.removeAttribute("enterpriseArrayList");
            session.removeAttribute("finalArrayList");

            session.setAttribute("enterpriseArrayList", finalMergredDestnEOArrayList);

        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param event
     * @throws com.sun.mdm.index.objects.exception.ObjectException 
     */
    public void unmergeEnterpriseObject(ActionEvent event) throws ObjectException {

        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpressionunmerge");
        try {
            MergeResult unMerge = masterControllerService.unMerge(enterpriseObject.getEUID());
        //httpRequest.setAttribute("unMerged", "unmerge");
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void viewMergedRecords(ActionEvent event) throws ObjectException {

        //HashMap unmergedHashMapValueExpression = (HashMap) event.getComponent().getAttributes().get("unmergedEOValueExpression");
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewmerge");

        try {
            EnterpriseObjectHistory viewMergehist = masterControllerService.viewMergeRecords(transactionNumber);
            ArrayList mergeEOList = new ArrayList();

            if (viewMergehist.getBeforeEO1() != null) {
                mergeEOList.add(viewMergehist.getBeforeEO1());
            }
            if (viewMergehist.getBeforeEO2() != null) {
                mergeEOList.add(viewMergehist.getBeforeEO2());
            }
//          if(viewMergehist.getAfterEO() !=null){
//              mergeEOList.add(viewMergehist.getAfterEO());
//          }
            if (viewMergehist.getAfterEO2() != null) {
                mergeEOList.add(viewMergehist.getAfterEO2());
            }


            httpRequest.setAttribute("mergeEOList", mergeEOList);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void viewHistory(ActionEvent event) throws ObjectException {
        // session.removeAttribute("eoHistory");  

        String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");
        
        ArrayList newArrayListHistory = new ArrayList();
        EnterpriseObject eoHist = null;
        try {
            ArrayList viewHistoryEOList = masterControllerService.viewHistory(euid);
            for (int i = 0; i < viewHistoryEOList.size(); i++) {
                HashMap objectHistMap  = (HashMap) viewHistoryEOList.get(i);
                String key = (String) objectHistMap.keySet().toArray()[0];
                
                ////System.out.println(i + "  <==>keysSet " + key + "==> : objectHistMap");
                HashMap objectHistMapUpdated  = new HashMap();
                if(objectHistMap.get(key) != null) {
                    eoHist = (EnterpriseObject) objectHistMap.get(key);
                    objectHistMapUpdated.put(key, compareDuplicateManager.getEnterpriseObjectAsHashMap(eoHist, screenObject));
                    newArrayListHistory.add(objectHistMapUpdated);
                }                
                               
            }
         
            ////System.out.println("FINAL HISTORY LIST" + newArrayListHistory.size());
            httpRequest.setAttribute("eoHistory" + euid, newArrayListHistory);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void removeHistory(ActionEvent event) throws ObjectException {
        String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");
        httpRequest.removeAttribute("eoHistory" + euid);
    }

    public void viewSource(ActionEvent event) throws ObjectException {
        String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");
        
        ArrayList newArrayList = new ArrayList();
        try {
            EnterpriseObject enterpriseObject  = masterControllerService.getEnterpriseObject(euid);
            Collection itemsSource = enterpriseObject.getSystemObjects();
            Iterator iterSources  = itemsSource.iterator();
            while (iterSources.hasNext()) {
                SystemObject systemObject =(SystemObject)iterSources.next();
                newArrayList.add(compareDuplicateManager.getSystemObjectAsHashMap(systemObject, screenObject));
           }

            ////System.out.println("newArrayList" + newArrayList.size());
            httpRequest.setAttribute("eoSources"+enterpriseObject.getEUID(), newArrayList);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void removeSource(ActionEvent event) throws ObjectException {
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
        httpRequest.removeAttribute("eoSources");
    }

    public PatientDetails[] getPatientDetailsVO() {
        patientDetailsVO = new PatientDetails[this.resultArrayList.size()];
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
        int size = this.resultArrayList.size();
    //    //System.out.println("this.resultArrayList ===> ; "  + this.resultArrayList);

        HashMap values = new HashMap();
        for (int i = 0; i < size; i++) {
            patientDetailsVO[i] = new PatientDetails();
            values = (HashMap) resultArrayList.get(i);
            String dateField = ((Date) values.get("Person.DOB") ).toString();
            patientDetailsVO[i].setEuid((String) values.get("EUID"));
            patientDetailsVO[i].setFirstName((String) values.get("Person.FirstName"));
            patientDetailsVO[i].setLastName((String) values.get("Person.LastName"));
            patientDetailsVO[i].setDob(dateField);
            patientDetailsVO[i].setSsn((String) values.get("Person.SSN"));
            patientDetailsVO[i].setAddressLine1((String) values.get("Person.Address.AddressLine1"));
        }

        return patientDetailsVO;
    }

    public void setPatientDetailsVO(PatientDetails[] patientDetailsVO) {
        this.patientDetailsVO = patientDetailsVO;
    }

    public String getSingleEUID() {
        return singleEUID;
    }

    public void setSingleEUID(String singleEUID) {
        this.singleEUID = singleEUID;
    }

    public String getCompareEUID() {
        return compareEUID;
    }

    public void setCompareEUID(String compareEUID) {
        this.compareEUID = compareEUID;
    }

    public String getEuid1() {
        return euid1;
    }

    public void setEuid1(String euid1) {
        this.euid1 = euid1;
    }

    public String getEuid2() {
        return euid2;
    }

    public void setEuid2(String euid2) {
        this.euid2 = euid2;
    }

    public String getEuid3() {
        return euid3;
    }

    public void setEuid3(String euid3) {
        this.euid3 = euid3;
    }

    public String getEuid4() {
        return euid4;
    }

    public void setEuid4(String euid4) {
        this.euid4 = euid4;
    }

    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public String getMergeEuids() {
        return mergeEuids;
    }

    public void setMergeEuids(String mergeEuids) {
        this.mergeEuids = mergeEuids;
    }

    public String getDestnEuid() {
        return destnEuid;
    }

    public void setDestnEuid(String destnEuid) {
        this.destnEuid = destnEuid;
    }

    public String getCompareEuids() {
        return compareEuids;
    }

    public void setCompareEuids(String compareEuids) {
        this.compareEuids = compareEuids;
    }

    /**
     * 
     * @return String
     */
    public String buildCompareEuids() {
        ArrayList euidsMapList = new ArrayList();
        ////System.out.println("===> EUIDS " + this.compareEuids);

        String[] euids = this.compareEuids.split("##");
        for (int i = 0; i < euids.length; i++) {
            try {
                String sourceEuid = euids[i];
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(sourceEuid);
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                euidsMapList.add(eoMap);

             ////System.out.println("===> " + sourceEuid + "srcRevisionNumbers" + eo.getEUID());
            } catch (ProcessingException ex) {
                java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UserException ex) {
                java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        session.setAttribute("comapreEuidsArrayList", euidsMapList);
        return "Compare Duplicates";

    }

    public String getSelectedMergeFields() {
        return selectedMergeFields;
    }

    public void setSelectedMergeFields(String selectedMergeFields) {
        this.selectedMergeFields = selectedMergeFields;
    }
    

    public ArrayList buildEuids(String euid) {
        ArrayList euidsMapList = new ArrayList();
        try {
            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            euidsMapList.add(eoMap);
        } catch (ProcessingException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserException ex) {
            java.util.logging.Logger.getLogger(PatientDetailsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return euidsMapList;        
  }

    public String getPotentialDuplicateId() {
        return potentialDuplicateId;
    }

    public void setPotentialDuplicateId(String potentialDuplicateId) {
        this.potentialDuplicateId = potentialDuplicateId;
    }

}


   

