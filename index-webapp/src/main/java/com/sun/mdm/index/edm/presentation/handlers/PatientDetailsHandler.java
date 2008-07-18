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
import com.sun.mdm.index.edm.services.configuration.ValidationService;
import com.sun.mdm.index.util.LogUtil;
//import com.sun.mdm.index.util.Logger;

import com.sun.mdm.index.edm.presentation.util.Localizer;
import com.sun.mdm.index.edm.presentation.util.Logger;
import com.sun.mdm.index.edm.services.configuration.ConfigManager;
import com.sun.mdm.index.edm.services.masterController.MasterControllerService;
import net.java.hulp.i18n.LocalizationSupport;
/**
 * @author Rajani
 */
public class PatientDetailsHandler extends ScreenConfiguration {

    private transient static final Logger mLogger = Logger.getLogger("com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler");
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

    //private static final Logger mLogger = LogUtil.getLogger("com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler");

    private String mergeEuids = new String();
    private String destnEuid  = new String();
    private String selectedMergeFields = new String();
    /*
    * Map to hold the parameters
    **/
    private HashMap parametersMap  = new HashMap();

    String exceptionMessaage =bundle.getString("EXCEPTION_MSG");
    CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
    
    Operations operations = new Operations();
    
    /**HashMap used for the updated root node values for the surviving EOduring merge process**/
    private HashMap destnRootNodeHashMap = new HashMap();
    /**ArrayList used for the updated minor object values for the surviving EO during merge process**/
    private ArrayList destnMinorobjectsList = new ArrayList();
    /** Creates a new instance of PatientDetailsHandler */
    
    public PatientDetailsHandler() {

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
                mLogger.info(mLocalizer.x("PDH001: Failed Get the Screen Object:{0} ", errorMessage));
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

            //if user enters LID ONLY 
            if ((super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("LID").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("SystemCode") == null) {
                errorMessage = bundle.getString("LID_only");   //"Please Enter System Code";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                mLogger.info(mLocalizer.x("PDH002: {0}",errorMessage));
                return null;
            }
            //if user enters SYSTEMCODE ONLY 
            if ((super.getUpdateableFeildsMap().get("SystemCode") != null && super.getUpdateableFeildsMap().get("SystemCode").toString().trim().length() > 0) && super.getUpdateableFeildsMap().get("LID") == null) {
                errorMessage = bundle.getString("enter_LID");//"Please Enter LID Value";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
               mLogger.info(mLocalizer.x("PDH003: {0}",errorMessage));
                return null;

            }
            //if user enters LID and SystemCode Validate the LID 
            if (super.getUpdateableFeildsMap().get("LID") != null && super.getUpdateableFeildsMap().get("SystemCode") != null) {
                String LID = (String) super.getUpdateableFeildsMap().get("LID");
                String SystemCode = (String) super.getUpdateableFeildsMap().get("SystemCode");
                if (SystemCode.trim().length() > 0 && LID.trim().length() == 0) {
                    errorMessage = bundle.getString("enter_LID");// "Please Enter LID Value";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
                    mLogger.info(mLocalizer.x("PDH003: {0}",errorMessage));
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
                             mLogger.info(mLocalizer.x("PDH004: {0} ", errorMessage));
                            return null;
                        }
                    }                    
                    catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                             mLogger.error(mLocalizer.x("PDH005: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("PDH006: Encountered the UserException:{0} ", ex.getMessage()),ex);
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("PDH035: Error  occurred"), ex);
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
                    mLogger.info(mLocalizer.x("PDH007: Validation failed :{0}:{1} ", fieldErrors[0],fieldErrors[1]));
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
                    mLogger.info(mLocalizer.x("PDH008: Validation failed :{0}:{1} ", fieldErrors[0],fieldErrors[1]));
                    return null;
                }

            }


            EOSearchResultIterator eoSearchResultIterator = null;

            ArrayList arlResultsConfig = screenObject.getSearchResultsConfig();

            EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEpathResultsFields(arlResultsConfig);
            ArrayList sResultsConfigArrayList = screenObject.getSearchResultsConfig();

            EPathArrayList resultFields = new EPathArrayList();

            Iterator srcalIterator = sResultsConfigArrayList.iterator();
            ArrayList results = new ArrayList();
            //if only euid is entered by the user.
            if (super.getUpdateableFeildsMap().get("EUID") != null) {
                String euid = (String) super.getUpdateableFeildsMap().get("EUID");
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(euid);
                if (eo != null) {
                    HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
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
                HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
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
                        //get the EO search option from the EDM.xml file here as per the search type                      
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
                while (eoSearchResultIterator.hasNext()) {
                    EOSearchResultRecord eoSearchResultRecord = eoSearchResultIterator.next();
                    
                    ObjectNode objectNode = eoSearchResultRecord.getObject();
                   
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
                                
                               if (value != null && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
                                    fieldvalues.put(fieldConfig.getFullFieldName(), bundle.getString("SENSITIVE_FIELD_MASKING"));
                               } else {
                                    fieldvalues.put(fieldConfig.getFullFieldName(), dateField);
                                }
                                
                           } else {
                               if (value != null  && fieldConfig.isSensitive() && !operations.isField_VIP()) { //if the field is senstive then mask the value accordingly
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
           mLogger.error(mLocalizer.x("PDH009: Exception has occured  :{0} ", ex.getMessage()),ex);
            return null;
        }
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
            boolean resolveBoolean = ("AutoResolve".equalsIgnoreCase(this.getResolveType())) ? false : true;
            String resolveString = ("AutoResolve".equalsIgnoreCase(this.getResolveType())) ? "R": "A";

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
                  objectHashMap.put("Status", resolveString);
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } 
        
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("PDH010: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH011: Encountered the UserException:{0} ", ex.getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH036: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }

        return "Compare Duplicates";
    }

    /** 
     * Added By Rajani Kanth  on 07/06/2008
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

        } 
        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("PDH040: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH041: Encountered the UserException:{0} ", ex.getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH042: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
        return modifiedArrayList;
    }   
    
    
    /** 
     * 
     * Added By Rajani Kanth  on 07/06/2008
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

        }

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH012: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH013: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH037: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
        return modifiedArrayList;
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
                //set the resolve type to "U" (UnResolve)for the selected potential duplicate
                if(potDupId.equals((String)objectHashMap.get("PotDupId"))) {
                  objectHashMap.put("Status", "U");
                }
                modifiedArrayList.add(objectHashMap);
            }
            session.setAttribute("comapreEuidsArrayList",modifiedArrayList);

        } 

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH051: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH052: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH053: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
        }

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH014: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH015: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH096: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
        } 

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("PDH016: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH066: Encountered the UserException:{0} ", ex.getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH067: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }

    }
    /** 
     * Added By Rajani Kanth  on 07/06/2008<br>
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
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
            
/*
            //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            System.out.println("sessionRevisionNumber--> " + sessionRevisionNumber + "---> dbRevisionNumber " + dbRevisionNumber);
            
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PatientDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
                destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
                
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());

                HashMap retHashMap  = new HashMap();
                retHashMap.put(PatientDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                return retHashMap;
            }
            */


            
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

            eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);

//            httpRequest.setAttribute("eoMultiMergePreview", eoMultiMergePreview);


        } 

        // modified exceptional handling logic
                    catch (Exception ex) {
                        if (ex instanceof ValidationException) {
                             mLogger.error(mLocalizer.x("PDH077: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
                        } else if (ex instanceof UserException) {
                            mLogger.error(mLocalizer.x("PDH078: Encountered the UserException:{0} ", ex.getMessage()),ex);
                        } else if (!(ex instanceof ProcessingException)) {
                            mLogger.error(mLocalizer.x("PDH79: Error  occurred"), ex);
                        }
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
        } 
        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                 mLogger.error(mLocalizer.x("PDH019: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH020: Encountered the UserException:{0} ", ex.getMessage()),ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH021: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            return null;
        }


        return eoMultiMergePreview;
    }
    /** 
     * Added By Rajani Kanth  on 07/06/2008 <br>
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

            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
 /*
           //get the revision number from the session and which is available in DB
            Integer sessionRevisionNumber  =(Integer) session.getAttribute("SBR_REVISION_NUMBER"+destinationEO.getEUID());
            Integer dbRevisionNumber  = destinationEO.getSBR().getRevisionNumber();
            System.out.println("sessionRevisionNumber--> " + sessionRevisionNumber + "---> dbRevisionNumber " + dbRevisionNumber);
            if (dbRevisionNumber.intValue() != sessionRevisionNumber.intValue()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PatientDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text")));
                destinationEO = masterControllerService.getEnterpriseObject(srcDestnEuids[0]);
                
                //reset the SBR revision number here in session
                session.setAttribute("SBR_REVISION_NUMBER" + destinationEO.getEUID(), destinationEO.getSBR().getRevisionNumber());
                HashMap retHashMap  = new HashMap();
                retHashMap.put(PatientDetailsHandler.CONCURRENT_MOD_ERROR,
                        "'" + destinationEO.getEUID() + "' " + bundle.getString("concurrent_mod_text") + " " + bundle.getString("login_try_again_text"));
                ArrayList retList  = new ArrayList();

                retList.add(retHashMap);
                return retList;
            }
*/
            HashMap eoHashMap = eoHashMap = (HashMap) compareDuplicateManager.getEnterpriseObjectAsHashMap(destinationEO, screenObject).get("ENTERPRISE_OBJECT_CODES");
 
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

            HashMap eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);


            finalMergeList.add(eoMultiMergePreview);

            session.setAttribute("comapreEuidsArrayList", finalMergeList);

        } 

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH082: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH083: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH084: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
        } 
        catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH025: Encountered the UserException :{0} ", ex.getMessage()), ex);
            return null;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH026: Encountered the ObjectException :{0} ", ex.getMessage()), ex);
            return null;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, exceptionMessaage, ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH027: Encountered the Exception :{0} ", ex.getMessage()), ex);
            return null;
        }

        return finalMergeList;
    }

        
        public String previewPostMultiMergedEnterpriseObject() {
        try {
            //httpRequest.setAttribute("comapreEuidsArrayList", httpRequest.getAttribute("comapreEuidsArrayList"));
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

//            httpRequest.setAttribute("sourceEUIDs", sourceEUIDs);

//            httpRequest.setAttribute("destnEuid", destnEuid);
            
            EnterpriseObject resulteo = masterControllerService.getPostMergeMultipleEnterpriseObjects(sourceEUIDs, destinationEO, srcRevisionNumbers, destRevisionNumber);

            HashMap eoMultiMergePreview = compareDuplicateManager.getEnterpriseObjectAsHashMap(resulteo, screenObject);

//            httpRequest.setAttribute("eoMultiMergePreview", eoMultiMergePreview);


        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
             mLogger.error(mLocalizer.x("PDH017: Encountered the  ProcessingException :{0} ", ex.getMessage()),ex);
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,exceptionMessaage));
            mLogger.error(mLocalizer.x("PDH018: Encountered the  UserException :{0} ", ex.getMessage()),ex);
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
        } catch (UserException ex) {   
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("PDH069: Encountered the  UserException :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
          mLogger.error(mLocalizer.x("PDH070: Encountered the  ObjectException :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("PDH071: Encountered the Exception :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        }
        
        return this.SEARCH_PATIENT_DETAILS;
}
        
        
        
  public String performMultiMergedEnterpriseObject() {
        try {
         
            EnterpriseObject destinationEO = masterControllerService.getEnterpriseObject(destnEuid);


            HashMap eoHashMap = eoHashMap = (HashMap) compareDuplicateManager.getEnterpriseObjectAsHashMap(destinationEO, screenObject).get("ENTERPRISE_OBJECT_CODES");
      
           
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
                    if(sourceEuidFull[1] != null && "null".equalsIgnoreCase(sourceEuidFull[1])) {
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
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("PDH022: Encountered the ObjectException :{0} ", ex.getMessage()),ex);
        } catch (ValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("PDH023: Encountered the  ValidationException :{0} ", ex.getMessage()),ex);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
              mLogger.error(mLocalizer.x("PDH024: Encountered the Exception :{0} ", ex.getMessage()),ex);
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
        }
       catch (UserException ex) {   
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("PDH025: Encountered the UserException :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
   mLogger.error(mLocalizer.x("PDH026: Encountered the ObjectException :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH027: Encountered the Exception :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
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
//            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getMessage(),ex.getMessage()));
             mLogger.error(mLocalizer.x("PDH028: Encountered the ProcessingException :{0} ", ex.getMessage()));
            return this.SERVICE_LAYER_ERROR;
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH029: Encountered the UserException :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        }
        
        
      //Insert Audit logs after adding the new System Object
       try {
       //String userName, String euid1, String euid2, String function, int screeneID, String detail
        masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               this.getSingleEUID(), 
                                               "",
                                               "EO View/Edit",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View/Edit detail of enterprise object");
        } catch (UserException ex) {   
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
            mLogger.error(mLocalizer.x("PDH032: Failed to insert Audit log :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (ObjectException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
         mLogger.error(mLocalizer.x("PDH030: Failed to insert Audit log :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
           mLogger.error(mLocalizer.x("PDH031: Failed to insert Audit log :{0} ", ex.getMessage()),ex);
            return this.SERVICE_LAYER_ERROR;
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
                    String msg1 = bundle.getString("EUID1");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  msg1+errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);

                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid1(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                }
            }
            if (this.getEuid2() != null && !"EUID 2".equalsIgnoreCase(this.getEuid2())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid2());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
//                    session.removeAttribute("comapreEuidsArrayList");
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    String msg2 = bundle.getString("EUID2");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg2 + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid2(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                }
            }
            if (this.getEuid3() != null && !"EUID 3".equalsIgnoreCase(this.getEuid3())) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid3());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    String msg3 = bundle.getString("EUID3");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg3 + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid3(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                    
                }
            }
            if (this.getEuid4() != null && this.getEuid4().length() > 0 ) {
                enterpriseObject = masterControllerService.getEnterpriseObject(this.getEuid4());
                //Throw exception if EO is found null.
                if (enterpriseObject == null) {
                    String errorMessage = bundle.getString("enterprise_object_not_found_error_message");
                    String msg4 = bundle.getString("EUID4");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg4 + errorMessage, errorMessage));
                } else {
                    eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
                    newArrayList.add(eoMap);
                    //Insert audit log here for EUID search
                    masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                            this.getEuid4(),
                            "",
                            "EO View/Edit",
                            new Integer(screenObject.getID()).intValue(),
                            "View/Edit detail of enterprise object");
                    
                }
            }
           
            session.setAttribute("comapreEuidsArrayList", newArrayList);
        } catch (ProcessingException ex) {
           mLogger.error(mLocalizer.x("PDH033: Encountered  ProcessingException:{0} ", ex.getMessage()),ex);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
        } catch (UserException ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("PDH034: Encountered  UserException:{0} ", ex.getMessage()),ex);
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
            String euid = (String) event.getComponent().getAttributes().get("eoValueExpression");

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(euid);

            //Activate the enterprise object
            masterControllerService.activateEnterpriseObject(enterpriseObject.getEUID());

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());

            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(updatedEnterpriseObject, screenObject);

            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(eoMap);

            //Keep the updated SO in the session again
            httpRequest.setAttribute("comapreEuidsArrayList", updatedEOList);

        } 

        // modified exceptional handling logic
    catch (Exception ex) {
        if (ex instanceof ValidationException) {
             mLogger.error(mLocalizer.x("PDH043: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
        } else if (ex instanceof UserException) {
            mLogger.error(mLocalizer.x("PDH044: Encountered the UserException:{0} ", ex.getMessage()),ex);
        } else if (!(ex instanceof ProcessingException)) {
            mLogger.error(mLocalizer.x("PDH096: Error  occurred"), ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
    }

    }

    /**
     * 
     * @param event
     */
    public void deactivateEO(ActionEvent event) {
        try {

            String euid = (String) event.getComponent().getAttributes().get("eoValueExpression");

            EnterpriseObject enterpriseObject = masterControllerService.getEnterpriseObject(euid);


            //Deactivate the enterprise object
            masterControllerService.deactivateEnterpriseObject(enterpriseObject.getEUID());

            EnterpriseObject updatedEnterpriseObject = masterControllerService.getEnterpriseObject(enterpriseObject.getEUID());

            HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(updatedEnterpriseObject, screenObject);

            ArrayList updatedEOList = new ArrayList();
            updatedEOList.add(eoMap);

            //Keep the updated SO in the session again
            httpRequest.setAttribute("comapreEuidsArrayList", updatedEOList);

        } 

        // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH045: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH046: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH097: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
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

        } 

        // modified exceptional handling logic
            catch (Exception ex) {
                if (ex instanceof ValidationException) {
                     mLogger.error(mLocalizer.x("PDH047: Encountered the ValidationException :{0} ", ex.getMessage()),ex);
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("PDH048: Encountered the UserException:{0} ", ex.getMessage()),ex);
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("PDH099: Error  occurred"), ex);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            }
    }

    /**
     * 
     * @param event
     * @throws com.sun.mdm.index.objects.exception.ObjectException 
     */
    public void unmergeEnterpriseObject(ActionEvent event) throws ObjectException {

            //ArrayList newArrayList = new ArrayList();
            String euidUnmerge = (String) event.getComponent().getAttributes().get("unMergeEuidVE");

            //HashMap unmergeEO = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject, screenObject);
            //newArrayList.add(unmergeEO);
        try {
            MergeResult unmerge = masterControllerService.unMerge(euidUnmerge);
            ArrayList newArrayList = new ArrayList();
            EnterpriseObject eo = masterControllerService.getEnterpriseObject(euidUnmerge);
            HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
            newArrayList.add(eoHashMap);
            httpRequest.setAttribute("comapreEuidsArrayList", newArrayList);
            if (unmerge.getDestinationEO() != null && unmerge.getSourceEO() != null) {
                //Insert audit log here for EUID UNMERGE
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                                        unmerge.getDestinationEO().getEUID(),
                                                        unmerge.getSourceEO().getEUID(),
                                                        "EUID Unmerge",
                                                        new Integer(screenObject.getID()).intValue(),
                                                        "Unmerge two enterprise objects");
            }


        } 
     // modified exceptional handling logic
            catch (Exception ex) {
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("PDH049: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("PDH050: Encountered the UserException:{0} ", ex.getMessage()), ex);
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("PDH091: Error  occurred"), ex);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
            }

    }

    public void viewMergedRecords(ActionEvent event) throws ObjectException {

        //HashMap unmergedHashMapValueExpression = (HashMap) event.getComponent().getAttributes().get("unmergedEOValueExpression");
        String euid = (String) event.getComponent().getAttributes().get("euidValueExpression");
        String transactionNumber = (String) event.getComponent().getAttributes().get("tranNoValueExpressionviewmerge");
        ArrayList eoArrayList = (ArrayList) event.getComponent().getAttributes().get("eoArrayList");
        httpRequest.setAttribute("comapreEuidsArrayList", eoArrayList);
        try {
            EnterpriseObjectHistory viewMergehist = masterControllerService.viewMergeRecords(transactionNumber);
            ArrayList mergeEOList = new ArrayList();

            if (viewMergehist.getBeforeEO1() != null) {
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO1(), screenObject);
                mergeEOList.add(eoMap);
                //mergeEOList.add(viewMergehist.getBeforeEO1());
            }
            if (viewMergehist.getBeforeEO2() != null) {
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getBeforeEO2(), screenObject);
                mergeEOList.add(eoMap);
                //mergeEOList.add(viewMergehist.getBeforeEO2());
            }
//          if(viewMergehist.getAfterEO() !=null){
//              mergeEOList.add(viewMergehist.getAfterEO());
//          }
            if (viewMergehist.getAfterEO2() != null) {
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(viewMergehist.getAfterEO2(), screenObject);
                mergeEOList.add(eoMap);
                //mergeEOList.add(viewMergehist.getAfterEO2());
            }


            httpRequest.setAttribute("mergeEOList"+euid, mergeEOList);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("PDH092: Failed to get merge records ", ex.getMessage()),ex);
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
                
                HashMap objectHistMapUpdated  = new HashMap();
                if(objectHistMap.get(key) != null) {
                    eoHist = (EnterpriseObject) objectHistMap.get(key);
                    objectHistMapUpdated.put(key, compareDuplicateManager.getEnterpriseObjectAsHashMap(eoHist, screenObject));
                    newArrayListHistory.add(objectHistMapUpdated);
                }                
                               
            }
         
            httpRequest.setAttribute("eoHistory" + euid, newArrayListHistory);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
              mLogger.error(mLocalizer.x("PDH093: Failed to get history ", ex.getMessage()),ex);
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

            httpRequest.setAttribute("eoSources"+enterpriseObject.getEUID(), newArrayList);

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,exceptionMessaage,ex.getMessage()));
             mLogger.error(mLocalizer.x("PDH054: Failed to view source ", ex.getMessage()),ex);
        }
    }

    public void removeSource(ActionEvent event) throws ObjectException {
        EnterpriseObject enterpriseObject = (EnterpriseObject) event.getComponent().getAttributes().get("eoValueExpression");
        httpRequest.removeAttribute("eoSources");
    }

//    public PatientDetails[] getPatientDetailsVO() {
//        patientDetailsVO = new PatientDetails[this.resultArrayList.size()];
//        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat("MM/dd/yyyy");
//        int size = this.resultArrayList.size();
//
//        HashMap values = new HashMap();
//        for (int i = 0; i < size; i++) {
//            patientDetailsVO[i] = new PatientDetails();
//            values = (HashMap) resultArrayList.get(i);
//            String dateField = ((Date) values.get("Person.DOB") ).toString();
//            patientDetailsVO[i].setEuid((String) values.get("EUID"));
//            patientDetailsVO[i].setFirstName((String) values.get("Person.FirstName"));
//            patientDetailsVO[i].setLastName((String) values.get("Person.LastName"));
//            patientDetailsVO[i].setDob(dateField);
//            patientDetailsVO[i].setSsn((String) values.get("Person.SSN"));
//            patientDetailsVO[i].setAddressLine1((String) values.get("Person.Address.AddressLine1"));
//        }
//
//        return patientDetailsVO;
//    }
//
//    public void setPatientDetailsVO(PatientDetails[] patientDetailsVO) {
//        this.patientDetailsVO = patientDetailsVO;
//    }

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

        String[] euids = this.compareEuids.split("##");
        for (int i = 0; i < euids.length; i++) {
            try {
                String sourceEuid = euids[i];
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(sourceEuid);
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
                euidsMapList.add(eoMap);
                //Insert audit log here for EUID search
                masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                        sourceEuid,
                        "",
                        "EO View/Edit",
                        new Integer(screenObject.getID()).intValue(),
                        "View/Edit detail of enterprise object");

            }

        // modified exceptional handling logic
            catch (Exception ex) {
                if (ex instanceof ValidationException) {
                    mLogger.error(mLocalizer.x("PDH055: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
                } else if (ex instanceof UserException) {
                    mLogger.error(mLocalizer.x("PDH056: Encountered the UserException:{0} ", ex.getMessage()), ex);
                } else if (!(ex instanceof ProcessingException)) {
                    mLogger.error(mLocalizer.x("PDH057: Error  occurred"), ex);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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
            if(eo != null) {
			HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);
			euidsMapList.add(eoMap);
           //String userName, String euid1, String euid2, String function, int screeneID, String detail
           masterControllerService.insertAuditLog((String) session.getAttribute("user"),
                                               eo.getEUID(), 
                                               "",
                                               "EO View/Edit",
                                               new Integer(screenObject.getID()).intValue(),
                                               "View/Edit detail of enterprise object");
			} else {
				String errorMessage = euid +" : ";
				errorMessage +=	bundle.getString("enterprise_object_not_found_error_message");
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMessage,errorMessage));
			}
    } 
     // modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH058: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH059: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH060: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
        }
    return euidsMapList;        
  }

    public ArrayList buildCompareEuids(String[] euids) {
        ArrayList euidsMapList = new ArrayList();
		String message = new String();
        try {
            for (int i = 0; i < euids.length; i++) {
                EnterpriseObject eo = masterControllerService.getEnterpriseObject(euids[i]);
                HashMap eoMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(eo, screenObject);

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

        } 

        
// modified exceptional handling logic
        catch (Exception ex) {
            if (ex instanceof ValidationException) {
                mLogger.error(mLocalizer.x("PDH087: Encountered the ValidationException :{0} ", ex.getMessage()), ex);
            } else if (ex instanceof UserException) {
                mLogger.error(mLocalizer.x("PDH088: Encountered the UserException:{0} ", ex.getMessage()), ex);
            } else if (!(ex instanceof ProcessingException)) {
                mLogger.error(mLocalizer.x("PDH089: Error  occurred"), ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, QwsUtil.getRootCause(ex).getMessage(), exceptionMessaage));
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

}


   








