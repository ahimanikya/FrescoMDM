/*
 * RecordDetailsHandler.java
 *
 * Created on November 4, 2007, 6:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.sun.mdm.index.edm.presentation.handlers;

import com.sun.mdm.index.edm.presentation.managers.MidmUtilityManager;
import com.sun.mdm.index.edm.presentation.security.Operations;
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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import com.sun.mdm.index.edm.services.configuration.ValidationService;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
public class RecordDetailsHandler extends ScreenConfiguration {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler");
    private static transient final Localizer mLocalizer = Localizer.get();
    private static final String SEARCH_PATIENT_DETAILS = "Record Details";
    private static final String SEARCH_EUID_DETAILS = "EUID Details";
    public static final String CONCURRENT_MOD_ERROR = "CONCURRENT_MOD_ERROR";
    
    //result array list for the out put
    private ArrayList resultArrayList = new ArrayList();
    private String[] euidCheckValues;
    private boolean euidCheckboolean;
    private static final String VALIDATION_ERROR = "validationfailed";
    private static final String SERVICE_LAYER_ERROR = "servicelayererror";
    //private PatientDetails[] patientDetailsVO = null;
    private String resolveType  = "AutoResolve";
    private String potentialDuplicateId;
    /**
     * Variable to hold the results defaulted to negative
     */
    private int resultsSize = -1;
    private String singleEUID;
    private String compareEUID;
    private String euid1 ;
    private String euid2 ;
    private String euid3 ;
    private String euid4 ;
    
    private String compareEuids = new String();

    //private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.RecordDetailsHandler");

    private String mergeEuids = new String();
    private String destnEuid  = new String();
    private String selectedMergeFields = new String();
    /*
    * Map to hold the parameters
    **/
    private HashMap parametersMap  = new HashMap();

    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    MidmUtilityManager midmUtilityManager = new MidmUtilityManager();
    
    Operations operations = new Operations();
    
    /**HashMap used for the updated root node values for the surviving EOduring merge process**/
    private HashMap destnRootNodeHashMap = new HashMap();
    /**ArrayList used for the updated minor object values for the surviving EO during merge process**/
    private ArrayList destnMinorobjectsList = new ArrayList();
    /** Creates a new instance of RecordDetailsHandler */
    
    public RecordDetailsHandler() {

    }
    /**
    @return String
    @throws com.sun.mdm.index.master.ProcessingException 
    @throws com.sun.mdm.index.master.UserException 
     */
    public ArrayList performSubmit() throws ProcessingException, UserException {
        //get the hidden fields search type from the form usin the facesContext
        // get the array list as per the search
        String errorMessage = null;
        Date date = null;
        try {
            super.setUpdateableFeildsMap(parametersMap);
            //set the search type as per the user choice
            super.setSearchType(super.getSelectedSearchType());
            //check one of many condtion here
            if (super.checkOneOfManyCondition()) {
                errorMessage = bundle.getString("ERROR_one_of_many");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));                
                mLogger.info(mLocalizer.x("RDH001: Failed Get the Screen Object:{0} ", errorMessage));
                return null;
            }
            
            //Check if all the required values in the group are entered by the user
            HashMap oneOfErrors = super.checkOneOfGroupCondition();
            if (oneOfErrors.size() > 0 ) {
                Iterator iter = oneOfErrors.keySet().iterator();
                while (iter.hasNext())   {
                    String key = (String)iter.next();
                    String message = bundle.getString("ERROR_ONE_OF_GROUP_TEXT1") + (key == null? " ":" "+key+" ") + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message , message));
                    ArrayList fieldsInGroup = (ArrayList)oneOfErrors.get(key);
                    for (int i = 0; i < fieldsInGroup.size(); i++) {
                        String fields = (String) fieldsInGroup.get(i);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                    }
                }                
              return null;
            }
            
            //Check if all required values are entered by the user
            ArrayList requiredErrorsList = super.isRequiredCondition();
            if (requiredErrorsList.size() > 0 ) {                                
                for (int i = 0; i < requiredErrorsList.size(); i++) {
                     String fields = (String) requiredErrorsList.get(i);
                     fields += " " + bundle.getString("ERROR_ONE_OF_GROUP_TEXT2");
                     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fields, fields));
                }
                return null;
            }

            //Check the input masking for the enterted values
            HashMap inputMaskErrors = super.checkInputMasking();
            if (!inputMaskErrors.isEmpty()) {
                Object[] keysValidations = inputMaskErrors.keySet().toArray();
                for (int i = 0; i < keysValidations.length; i++) {
                    String value = (String) keysValidations[i] + " " + (String) inputMaskErrors.get((String) keysValidations[i]);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, value, value));
                }
                return null;
            }

            
            
            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = bundle.getString("LID_only");   //"Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("RDH002: {0}",errorMessage));
                return null;
            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
                errorMessage = bundle.getString("enter_LID");//"Please Enter LID Value";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
               mLogger.info(mLocalizer.x("RDH003: {0}",errorMessage));
                return null;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString("enter_LID");// "Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("RDH004: {0}",errorMessage));
                    return null;

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
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage, errorMessage));
                             mLogger.info(mLocalizer.x("RDH005: {0} ", errorMessage));
                            return null;
                        }
                    } catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                            mLogger.error(mLocalizer.x("RDH006: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("RDH007: Encountered the UserException:{0} ", ex.getMessage()), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("RDH008: Error  occurred"), ex);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                        } else if (ex instanceof ProcessingException) {
                            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                            if (exceptionMessage.indexOf("stack trace") != -1) {
                                String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                                if (exceptionMessage.indexOf("message=") != -1) {
                                    parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                                }
                                mLogger.error(mLocalizer.x("RDH009: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                            } else {
                                mLogger.error(mLocalizer.x("RDH010: Error  occurred"), ex);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                            }
                            return null;
                        }

                    }

                }

            }

            //Validate all date fields entered by the user
            if (super.validateDateFields().size() > 0) {
                Object[] messObjs = super.validateDateFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(">>");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.info(mLocalizer.x("RDH011: Validation failed :{0}:{1} ", fieldErrors[0],fieldErrors[1]));
                    return null;
                }

            }

            //Validate all time fields entered by the user
            if (super.validateTimeFields().size() > 0) {
                Object[] messObjs = super.validateTimeFields().toArray();
                for (int i = 0; i < messObjs.length; i++) {
                    String obj = (String) messObjs[i];
                    String[] fieldErrors = obj.split(">>");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, fieldErrors[0] + " : " + fieldErrors[1], fieldErrors[1]));
                    mLogger.info(mLocalizer.x("RDH012: Validation failed :{0}:{1} ", fieldErrors[0],fieldErrors[1]));
                    return null;
                }

            }


            EOSearchResultIterator eoSearchResultIterator = null;

            ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();

            EPathArrayList ePathArrayList = midmUtilityManager.retrieveEpathResultsFields(arlResultsConfig);
            ArrayList sResultsConfigArrayList = screenObject.getSearchResultsConfig();

            EPathArrayList resultFields = new EPathArrayList();

            Iterator srcalIterator = sResultsConfigArrayList.iterator();
            ArrayList results = new ArrayList();
            //if only euid is entered by the user.
            if (super.getUpdateableFeildsMap().get("EUID") != null) {
                String euid = (String) super.getUpdateableFeildsMap().get("EUID");
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
                if (eo != null) {
                    HashMap eoHashMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                    results.add(eoHashMap);
                    //httpRequest.setAttribute("comapreEuidsArrayList", results);
                    return results;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "EUID: " + euid + " not found", "EUID: " + euid + " not found"));
                    return null;
                }

            } else if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {//if only LID/System Code is entered by the user is entered by the user.
            
                String lid = (String) super.getUpdateableFeildsMap().get("LID");
                lid = lid.replaceAll("-", "");
                String systemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                SystemObject so = masterControllerService.getSystemObject(systemCode, lid);

                EnterpriseObject eo = masterControllerService.getEnterpriseObjectForSO(so);
                HashMap eoHashMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                results.add(eoHashMap);
                //httpRequest.setAttribute("comapreEuidsArrayList", results);
                return results;

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
               boolean weightedSearch = false;
                while (searchScreenArrayIter.hasNext()) {
                    searchScreenConfig = (SearchScreenConfig) searchScreenArrayIter.next(); 
                   
                    if (searchScreenConfig.getScreenTitle().equalsIgnoreCase(super.getSelectedSearchType())) {
                        //get the EO search option from the midm.xml file here as per the search type                      
                        eoSearchOptionQueryBuilder = searchScreenConfig.getOptions().getQueryBuilder();
                        //set the weighted searched paramater here.
                        if (searchScreenConfig.getOptions().getIsWeighted()) {
                            httpRequest.setAttribute("WeightedSearch", "true");
                            weightedSearch = true;
                        }
                    }
                }

                resultFields.add("Enterprise.SystemSBR." + objectRef + ".EUID");

                EOSearchOptions eoSearchOptions = new EOSearchOptions(eoSearchOptionQueryBuilder, resultFields);
                
                
                if(weightedSearch) {
                   eoSearchOptions.setWeighted(weightedSearch);
                   
                }
              

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

                    //Get the Field Value as per the field config range type
                    if (objectFieldConfig.isRange()) {
                        feildValue = (String) super.getUpdateableFeildsMap().get(objectFieldConfig.getDisplayName());
                    } else {
                        feildValue = (String) super.getUpdateableFeildsMap().get(objectFieldConfig.getName());
                    }
                    if (feildValue != null && feildValue.trim().length() > 0) {
                        //Remove all masking fields from the field valued if any like SSN,LID...etc
                        feildValue = objectFieldConfig.demask(feildValue);
                        
                         if (objectFieldConfig.isRange() && objectFieldConfig.getDisplayName().endsWith("From")) {
                            gSearchCriteriaFromDOB.put(objectFieldConfig.getFullFieldName(), feildValue);
                        } else if (objectFieldConfig.isRange() && objectFieldConfig.getDisplayName().endsWith("To")) {
                            gSearchCriteriaToDOB.put(objectFieldConfig.getFullFieldName(), feildValue);
                        } else {
                            gSearchCriteria.put(objectFieldConfig.getFullFieldName(), feildValue);
                        }

                    }

                }               
                String objRef = objectRef;
                // following code is from buildObjectNodeFromSearchCriteria()
                //ObjectNode topNode = SimpleFactory.create(objRef);

                SystemObject sysobj = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteria);
                SystemObject sysobj2 = null;
                SystemObject sysobj3 = null;              
                
                if (!gSearchCriteriaFromDOB.isEmpty()) {
                    sysobj2 = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaFromDOB);
                    
                     
                    eoSearchCriteria.setSystemObject2(sysobj2); // for dob from
                }
                if (!gSearchCriteriaToDOB.isEmpty()) {
                    sysobj3 = buildObjectNodeFromSearchCriteria(objectRef, gSearchCriteriaToDOB);
                    eoSearchCriteria.setSystemObject3(sysobj3); // for dob to
                }
          
                eoSearchCriteria.setSystemObject(sysobj);  // for all search attributes other than dob range

                eoSearchResultIterator = masterControllerService.searchEnterpriseObject(eoSearchCriteria, eoSearchOptions);
                SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());


                String dateField = new String();
                ArrayList resultsConfigArray = super.getResultsConfigArray();
                String strVal = new String();
                ConfigManager.init();

                while (eoSearchResultIterator!=null && eoSearchResultIterator.hasNext()) {
                    EOSearchResultRecord eoSearchResultRecord = eoSearchResultIterator.next();
                    
                    ObjectNode objectNode = eoSearchResultRecord.getObject();
                    //check if the EO has sensitive data for ex: VIP, EMPOLYEE data
                    //Check if the object-sensitive-plug-in-class exists in the midm.xml file and check for the object senstitve data
                    boolean hasSensitiveData = (ConfigManager.getInstance().getSecurityPlugIn() != null) ? ConfigManager.getInstance().getSecurityPlugIn().isDataSensitive(objectNode) : true;
                   
                    HashMap fieldvalues = new HashMap();

                    //set the comparision score here
                    if (weightedSearch) {
                        fieldvalues.put("Weight", eoSearchResultRecord.getComparisonScore());
                    }
                    
                    for (int m = 0; m < ePathArrayList.size(); m++) {
                        FieldConfig fieldConfig  = (FieldConfig) resultsConfigArray.get(m);
                        EPath ePath = ePathArrayList.get(m);
                        try {
                            Object value = EPathAPI.getFieldValue(ePath, objectNode);
                            
                           if(value instanceof java.util.Date) {
                               dateField = simpleDateFormatFields.format(value);
                                
                               if (value != null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
                                    fieldvalues.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                               } else {
                                    fieldvalues.put(fieldConfig.getFullFieldName(), dateField);
                                }
                                
                           } else {
                               if (value != null && hasSensitiveData && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
                                    fieldvalues.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                               } else {
                                   if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0) && value != null) {
                                       //value for the fields with VALUE LIST
                                       strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
                                       fieldvalues.put(fieldConfig.getFullFieldName(), strVal);
                                   } else if ((fieldConfig.getInputMask() != null && fieldConfig.getInputMask().length() > 0) && value != null) {
                                       //Mask the field values accordingly
                                       strVal = fieldConfig.mask(value.toString());
                                       fieldvalues.put(fieldConfig.getFullFieldName(), strVal);
                                   } else if ((fieldConfig.getUserCode() != null && fieldConfig.getUserCode().length() > 0) && value != null) {
                                       //get the value if the user code is present for the fields
                                       strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
                                       fieldvalues.put(fieldConfig.getFullFieldName(), strVal);
                                       
                                   } else {
                                       fieldvalues.put(fieldConfig.getFullFieldName(), value);
                                   }
                               }
                                
                                
                            }
                        } catch (Exception npe) {
		          npe.printStackTrace();
                        }
                    }
					String euid = eoSearchResultRecord.getEUID();
                    EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
                    fieldvalues.put("EUID", eoSearchResultRecord.getEUID());
                    fieldvalues.put("EOStatus", eo.getStatus());

                    resultArrayList.add(fieldvalues);
                
            }
                //httpRequest.setAttribute("resultArrayListReq", resultArrayList);
                return resultArrayList;
                //setResultsSize(getPatientDetailsVO().length);

            }
        // End here            
        // SambaG
        } catch (Exception ex) {
            String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH013: Service Layer Validation Exception has occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH014: Service Layer User Exception occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH015: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH016: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH017: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }
            }
            return null;
        }
    }
 
     /** 
     * Added  on 07/06/2008
     * 
     * This method is called from the ajax services for resolving the potential duplicate.
     *
     * @param potentialDuplicateIdArg - resolving potential duplicate id.
     *          resolveTypeArg - resolve type (AutoResolve/Resolve)
     *                 
     * @return ArrayList of potential duplicates with modified status (A/R) OR
     *         null if resolve operation fails or any exception occurs.
     * 
     */

    public ArrayList resolvePotentialDuplicate(String potentialDuplicateIdArg,String resolveTypeArg) {
        
        ArrayList modifiedArrayList = new ArrayList();
        try {
            
            //resolve the potential duplicate as per resolve type
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(resolveTypeArg)) ? false : true;
            String resolveString = ("AutoResolve".equalsIgnoreCase(resolveTypeArg)) ? "R": "A";

            //flag=false incase of autoresolve
            //flag = true incase of permanant resolve
            masterControllerService.setAsDifferentPerson(potentialDuplicateIdArg, resolveBoolean);
             
            ArrayList eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
            
            //reset the status and set it back in session
            for (int i = 0; i < eoArrayList.size(); i++) {
                HashMap objectHashMap = (HashMap) eoArrayList.get(i);
                //set the resolve type for the selected potential duplicate
                 if(potentialDuplicateIdArg.equalsIgnoreCase((String)objectHashMap.get("PotDupId"))) {
                  objectHashMap.put("Status", resolveString);
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH018: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH019: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH020: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH021: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH022: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        return modifiedArrayList;
    }   
    
    
    /** 
     * 
     * Added  on 07/06/2008
     * 
     * This method is called from the ajax services for unresolving the potential duplicate.
     *
     * @param potentialDuplicateIdArg - unresolving potential duplicate id.
     *          
     *                 
     * @return ArrayList of potential duplicates with modified status (A/R) OR
     *         null if unresolve operation fails or any exception occurs.
     * 
     */
    public ArrayList unresolvePotentialDuplicate(String potentialDuplicateIdArg) {
        ArrayList modifiedArrayList = new ArrayList();
        try {
            ArrayList eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
             
            //un resolve the potential duplicate as per resolve type
            masterControllerService.unresolvePotentialDuplicate(potentialDuplicateIdArg);
   
            //reset the status and set it back in session
            for (int i = 0; i < eoArrayList.size(); i++) {
                HashMap objectHashMap = (HashMap) eoArrayList.get(i);
                 //set the resolve type to "U" (UnResolve)for the selected potential duplicate
                if(potentialDuplicateIdArg.equalsIgnoreCase((String)objectHashMap.get("PotDupId"))) {
                  objectHashMap.put("Status", "U");
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH023: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH024: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH025: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH026: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH027: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        return modifiedArrayList;
    }

     /** 
     * Added  on 07/06/2008<br>
     * 
     * This method is used to get the preview of the surviving enterprise object. <br>
     * This method is called from the ajax services for merging the enterprise object.<br>
     *
     * @param srcDestnEuids String array of source and destination euids.<br>
     *                 srcDestnEuids[0] will be the surviving euid and rest of them are source euids in the multi merge operation.<br>
     *                 
     * @return HashMap of final surviving EO if merge preview is successfull.<br>
     *         null if merge fails or any exception occurs.<br>
     * 
     */

    public HashMap previewMultiMergeEnterpriseObject(String[] srcDestnEuids) {
        HashMap eoMultiMergePreview = null;
        try {
            // modified as fix for bug #202, on 23-10-08
            String mergeEuid  = midmUtilityManager.getMergedEuid(srcDestnEuids[0]);            
            if(mergeEuid != null && mergeEuid.length() > 0) {
                HashMap retHashMap  = new HashMap();
                retHashMap.put("Merged_EUID_Message", mergeEuid+" "+ bundle.getString("active_euid_text") + " " +srcDestnEuids[0]);
                retHashMap.put("Merged_EUID", mergeEuid );
                return retHashMap;
            }            
            
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
            

            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, RecordDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
                destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
                
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());

                HashMap retHashMap  = new HashMap();
                retHashMap.put(RecordDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                return retHashMap;
            }


            
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();

            ArrayList srcsList = new ArrayList();
            for (int i = 0; i < srcDestnEuids.length; i++) {
                if (i != 0) {
                    srcsList.add(srcDestnEuids[i]);
                }
            }

            Object[] sourceEUIDObjs = srcsList.toArray();

            String[] sourceEUIDs = new String[srcsList.size()];

            String[] srcRevisionNumbers = new String[sourceEUIDs.length];
            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            //httpRequest.setAttribute("sourceEUIDs", sourceEUIDs);

//            httpRequest.setAttribute("destnEuid", destnEuid);

            EnterpriseObject resulteo = masterControllerService.getPostMergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);

            eoMultiMergePreview = midmUtilityManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);

//            httpRequest.setAttribute("eoMultiMergePreview", eoMultiMergePreview);


        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH028: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH029: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH030: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH031: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH032: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }

        //Insert Audit logs 
        try {
            //String userName, String euid1, String euid2, String function, int screeneID, String detail
            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                    destnEuid,
                    "",
                    "EUID Merge Confirm",
                    new Integer(screenObject.getID()).intValue(),
                    "View two selected EUIDs of the merge confirm page");
        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH033: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH034: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH035: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH036: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH037: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }


        return eoMultiMergePreview;
    }
    /** 
     * Added  on 07/06/2008 <br>
     * 
     * This method is called from the ajax services for merging the enterprise object.<br>
     *
     * @param srcDestnEuids  String array of source and destination euids.<br>
     *                 srcDestnEuids[0] will be the surviving euid and rest of them are source euids in the multi merge operation.<br>
     *                 
     * @return ArrayList of final surviving EO if merge preview is successfull.<br>
     *         null if merge fails or any exception occurs.
     * 
     */
        
    public ArrayList multiMergeEnterpriseObject(String[] srcDestnEuids) {
        ArrayList finalMergeList = new ArrayList();
        try {
 
            //modified as fix for bug #202 on 23-10-08
            String mergeEuid  = midmUtilityManager.getMergedEuid(srcDestnEuids[0]);
            
            if(mergeEuid != null && mergeEuid.length() > 0) {
                HashMap retHashMap  = new HashMap();
                retHashMap.put("Merged_EUID_Message", mergeEuid+" "+ bundle.getString("active_euid_text") + " " +srcDestnEuids[0]);
                retHashMap.put("Merged_EUID", mergeEuid );
                ArrayList retList  = new ArrayList();
                retList.add(retHashMap);
                return retList;
            }
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
 
           //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, RecordDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
                destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
                
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());
                HashMap retHashMap  = new HashMap();
                retHashMap.put(RecordDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                ArrayList retList  = new ArrayList();

                retList.add(retHashMap);
                return retList;
            }

            HashMap eoHashMap = eoHashMap = (HashMap) midmUtilityManager.getEnterpriseObjectAsHashMap(destinationEO, screenObject).get("ENTERPRISE_OBJECT_CODES");
 
            String[] selectedFieldsValue = this.selectedMergeFields.split(">>");

            HashMap updatedEoMap = new HashMap();

            //when user modifies the person fields the only  update the enterprise object
            if (selectedFieldsValue.length > 1) {
                updatedEoMap.put(MasterControllerService.HASH_MAP_TYPE, eoHashMap.get(MasterControllerService.HASH_MAP_TYPE));
                updatedEoMap.put(MasterControllerService.MINOR_OBJECT_ID, eoHashMap.get(MasterControllerService.MINOR_OBJECT_ID));
                //Modify destination EO values with selected values 
                for (int i = 0; i < selectedFieldsValue.length; i++) {
                    String[] sourceEuidFull = selectedFieldsValue[i].split("##");
                    //if blank value is entered overwrite the value with null
                    if (sourceEuidFull[1] != null && "null".equalsIgnoreCase(sourceEuidFull[1])) {
                        updatedEoMap.put(sourceEuidFull[0], null);
                    } else {
                        updatedEoMap.put(sourceEuidFull[0], sourceEuidFull[1]);
                    }

                }
                //Modify CHANGED sbr values here
                masterControllerService.modifySBR(destinationEO.getSBR(), updatedEoMap);

                masterControllerService.updateEnterpriseObject(destinationEO);

                //get the modifed EO and merge it
                destinationEO = masterControllerService.getEnterpriseObject(destnEuid);
            }
 
            this.destnRootNodeHashMap = (HashMap)session.getAttribute("destnRootNodeHashMap");
            
                     
            if(this.destnRootNodeHashMap != null && this.destnRootNodeHashMap.keySet().size() > 0 ) {
                destnRootNodeHashMap.put(MasterControllerService.HASH_MAP_TYPE, eoHashMap.get(MasterControllerService.HASH_MAP_TYPE));
                destnRootNodeHashMap.put(MasterControllerService.MINOR_OBJECT_ID, eoHashMap.get(MasterControllerService.MINOR_OBJECT_ID));
                 //Modify CHANGED sbr values here
                masterControllerService.modifySBR(destinationEO.getSBR(), destnRootNodeHashMap);

                masterControllerService.updateEnterpriseObject(destinationEO);

                //get the modifed EO and merge it
                destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
                
            }
            
            String destRevisionNumber = new Integer(destinationEO.getSBR().getRevisionNumber()).toString();
 
            ArrayList srcsList = new ArrayList();
            for (int i = 0; i < srcDestnEuids.length; i++) {
                if (i != 0) {
                    srcsList.add(srcDestnEuids[i]);
                }
            }

            Object[] sourceEUIDObjs = srcsList.toArray();

            String[] sourceEUIDs = new String[srcsList.size()];

            String[] srcRevisionNumbers = new String[sourceEUIDs.length];

            for (int i = 0; i < sourceEUIDObjs.length; i++) {
                String sourceEuid = (String) sourceEUIDObjs[i];
                sourceEUIDs[i] = sourceEuid;
                srcRevisionNumbers[i] = new Integer(masterControllerService.getEnterpriseObject(sourceEuid).getSBR().getRevisionNumber()).toString();
            }

            EnterpriseObject resulteo = masterControllerService.mergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);
            session.removeAttribute("comapreEuidsArrayList");

            HashMap eoMultiMergePreview = midmUtilityManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);


            finalMergeList.add(eoMultiMergePreview);

            session.setAttribute("comapreEuidsArrayList", finalMergeList);

        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH038: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH039: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH040: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH041: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH042: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        //Insert Audit logs 
        try {
            //String userName, String euid1, String euid2, String function, int screeneID, String detail
            masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                                    destnEuid,
                                                    "",
                                                    "EUID Multi Merge Confirm",
                                                    new Integer(screenObject.getID()).intValue(),
                                                    "View two selected EUIDs of the merge confirm page");
        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH043: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH044: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH045: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH046: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH047: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }

        return finalMergeList;
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
            //if EO is found
            if(eo != null) {
			HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
			euidsMapList.add(eoMap);
           //String userName, String euid1, String euid2, String function, int screeneID, String detail
           masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               eo.getEUID(), 
                                               "",
                                               "EO View/Edit",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View/Edit detail of enterprise object");
			} else {
                                String mergeEuid = midmUtilityManager.getMergedEuid(euid);
                                if ("Invalid EUID".equalsIgnoreCase(mergeEuid)) { //if EO is not found
                                     String errorMessage = euid + " : ";
                                    errorMessage += bundle.getString("enterprise_object_not_found_error_message");
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                                return null;
                             } else if (mergeEuid != null) { //if its merged
				String errorMessage = euid +" : ";
				errorMessage +=	bundle.getString("enterprise_object_not_found_error_message");
                                 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
                                 //Set the merged euid for the user to search in the duplicates screen

                                //Insert audit log here for merged EUID search here
                                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                    mergeEuid,
                                    euid,
                                    "EO View/Edit",
                                    new Integer(screenObject.getID()).intValue(),
                                    "View/Edit detail of enterprise object");
                                    //If merged EUID found
                                   HashMap mergedMap = new HashMap();
                                   mergedMap.put("Merged_EUID_Message", mergeEuid  + " "+ bundle.getString("active_euid_text") + " " + euid);
                                   mergedMap.put("Merged_EUID", mergeEuid );
                                   euidsMapList.add(mergedMap);
                                   return euidsMapList;
			}
            } 
        } // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH048: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH049: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH050: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH051: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH052: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
    return euidsMapList;        
  }

    public ArrayList buildCompareEuids(String[] euids) {
        ArrayList euidsMapList = new ArrayList();
		String message = new String();
        try {
            for (int i = 0; i < euids.length; i++) {
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids[i]);
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);

                if ("active".equalsIgnoreCase(eo.getStatus())) {
                    euidsMapList.add(eoMap);
                    //String userName, String euid1, String euid2, String function, int screeneID, String detail
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            eo.getEUID(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                } else {  
					message += eo.getEUID() + " : is" + eo.getStatus();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
				}
            }

            session.setAttribute("eocomparision", "yes");

        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH053: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH054: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH055: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH056: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH057: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        return euidsMapList;
    }

    public String getPotentialDuplicateId() {
        return potentialDuplicateId;
    }

    public void setPotentialDuplicateId(String potentialDuplicateId) {
        this.potentialDuplicateId = potentialDuplicateId;
    }

    public HashMap getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(HashMap parametersMap) {
        this.parametersMap = parametersMap;
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
     * Added  on 8/10/2008
     * 
     * This method is used to get the Activate the EO
     * 
     * 
     * @param euid  
      * @return ArrayList 
     * 
     */

 
    public ArrayList activateEO(String euid) {
        ArrayList euidsMapList = new ArrayList();
        try {
            masterControllerService.activateEnterpriseObject(euid);

            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);

            if (eo != null) {
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                euidsMapList.add(eoMap);
                //String userName, String euid1, String euid2, String function, int screeneID, String detail
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                        eo.getEUID(),
                        "",
                        "EO View/Edit",
                        new Integer(screenObject.getID()).intValue(),
                        "View/Edit detail of enterprise object");
            } else {
                String errorMessage = euid + " : ";
                errorMessage += bundle.getString("enterprise_object_not_found_error_message");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            }
        } catch (Exception ex) {// modified exceptional handling logic
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("RDH058: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("RDH059: Encountered the UserException:{0} ", ex.getMessage()), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("RDH060: Error  occurred"), ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            } else if (ex instanceof ProcessingException) {
                String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                if (exceptionMessage.indexOf("stack trace") != -1) {
                    String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                    if (exceptionMessage.indexOf("message=") != -1) {
                        parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                    }
                    mLogger.error(mLocalizer.x("RDH061: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                } else {
                    mLogger.error(mLocalizer.x("RDH062: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                }

            }
            return null;
        }
        return euidsMapList;
    }
    /** 
     * Added    on 8/10/2008
     * 
     * This method is used to get the Deactivate the EO
     * 
     * 
     * @param euid  
      * @return ArrayList 
     * 
     */

        public ArrayList deactivateEO(String euid) {
        ArrayList euidsMapList = new ArrayList();
        try {
            masterControllerService.deactivateEnterpriseObject(euid);

            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);

            if (eo != null) {
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                euidsMapList.add(eoMap);
                //String userName, String euid1, String euid2, String function, int screeneID, String detail
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                        eo.getEUID(),
                        "",
                        "EO View/Edit",
                        new Integer(screenObject.getID()).intValue(),
                        "View/Edit detail of enterprise object");
            } else {
                String errorMessage = euid + " : ";
                errorMessage += bundle.getString("enterprise_object_not_found_error_message");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
            }
            } catch (Exception ex) {// modified exceptional handling logic
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("RDH063: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("RDH064: Encountered the UserException:{0} ", ex.getMessage()), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("RDH065: Error  occurred"), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
                } else if (ex instanceof ProcessingException) {
                    String exceptionMessage = QwsUtil.getRootCause(ex).getMessage();
                    if (exceptionMessage.indexOf("stack trace") != -1) {
                        String parsedString = exceptionMessage.substring(0, exceptionMessage.indexOf("stack trace"));
                        if (exceptionMessage.indexOf("message=") != -1) {
                            parsedString = parsedString.substring(exceptionMessage.indexOf("message=") + 8, parsedString.length());
                        }
                        mLogger.error(mLocalizer.x("RDH066: Error  occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, parsedString, exceptionMessaage));
                    } else {
                        mLogger.error(mLocalizer.x("RDH067: Error  occurred"), ex);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessage, exceptionMessaage));
                    }

                }
                return null;
            }
        return euidsMapList;
    }
    
    /** 
     * Added  on 8/10/2008
     * 
     * This method is used to get the Merged Records for the EO
     * 
     * 
     * @param euid  
     * @param transactionNumber 
     * @return ArrayList 
     * 
     */
     
    public ArrayList viewMergedRecords(String euid,String transactionNumber) throws ObjectException {

           try {
            EnterpriseObjectHistory viewMergehist = masterControllerService.viewMergeRecords(transactionNumber);
            ArrayList mergeEOList = new ArrayList();

            if (viewMergehist.getBeforeEO1() != null) {
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO1(), screenObject);
                mergeEOList.add(eoMap);
             }
            if (viewMergehist.getBeforeEO2() != null) {
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO2(), screenObject);
                mergeEOList.add(eoMap);
             }
 
            if (viewMergehist.getAfterEO2() != null) {
                HashMap eoMap = midmUtilityManager.getEnterpriseObjectAsHashMap(viewMergehist.getAfterEO2(), screenObject);
                mergeEOList.add(eoMap);
             }
 
            return mergeEOList;
 
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("RDH068: Failed to get merge records ", ex.getMessage()),ex);
             return null;
        }
    }
        
}


