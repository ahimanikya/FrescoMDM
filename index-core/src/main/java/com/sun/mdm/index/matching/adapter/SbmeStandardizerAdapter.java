/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * The contents of this file are subject to the terms of the Common 
 * Development and Distribution License ("CDDL")(the "License"). You 
 * may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://open-dm-mi.dev.java.net/cddl.html
 * or open-dm-mi/bootstrap/legal/license.txt. See the License for the 
 * specific language governing permissions and limitations under the  
 * License.  
 *
 * When distributing the Covered Code, include this CDDL Header Notice 
 * in each file and include the License file at
 * open-dm-mi/bootstrap/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the 
 * fields enclosed by brackets [] replaced by your own identifying 
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 */
package com.sun.mdm.index.matching.adapter;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.matching.StandardizationException;
import com.sun.mdm.index.matching.StandardizerEngineConfiguration;
import com.sun.mdm.index.matching.DomainSelector;
import com.sun.mdm.index.configurator.impl.standardization.SystemObjectStandardization;
import com.sun.mdm.index.configurator.impl.standardization.StandardizationFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.PreparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.UnparsedFieldGroup;
import com.sun.mdm.index.configurator.impl.standardization.SystemObjectField;
import com.sun.mdm.index.configurator.impl.standardization.PhoneticizeField;
import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.phonetic.Phoneticizer;
import com.sun.mdm.index.phonetic.PhoneticEncoderException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.stc.sbme.api.SbmeStandRecord;
import com.stc.sbme.api.SbmeStandardizationEngine;
import com.stc.sbme.api.SbmeStandRecordFactory;
import com.stc.sbme.api.SbmeStandardizationException;
import com.stc.sbme.api.SbmeMatchEngineException;
import com.stc.sbme.api.SbmeConfigFilesAccess;
import com.sun.mdm.index.util.Localizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * StandardizerAPI implementation that allows MEFA to communicate with the
 * Sbme standardizer.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public class SbmeStandardizerAdapter
        implements com.sun.mdm.index.matching.StandardizerAPI {

    /** 
     * The delimiter used to concatenate fields that are to be passed to the
     * standardizer
     */
    public static final String CONCAT_DELIMITER = "|";
    
    private SbmeStandRecordFactory standRecFactory; // To create SbmeStandRecords 
    private Phoneticizer phoneticizer;             // Access to phonetic encoders
    private SbmeStandardizationEngine standardizationEngine;
    
    /**  mLogger instance
     *
     */
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates new SbmeStandardizerAdapter */
    public SbmeStandardizerAdapter() {
    }

    /**
     * Standardize a SystemObject using the Sbme standardization engine
     * @param objToStandardize the SystemObject to standardize
     * @param metaData describes the standardization configuration for the given
     * SystemObject
     * @return the standardized SystemObject
     * @throws StandardizationException if standardization failed
     * @throws ObjectException if accessing the required data in the 
     * SystemObject failed
     */
    public SystemObject standardize(SystemObject objToStandardize,
                                    SystemObjectStandardization metaData) 
            throws StandardizationException, ObjectException {

        // Retrieve relevant info from the metadata
        ArrayList unparsedGroups = metaData.getUnParsedFieldGroups();
        ArrayList preparsedGroups = metaData.getPreParsedFieldGroups();
        ArrayList phoneticFields = metaData.getFieldsToPhoneticize();        

        try {        
            // Handle unparsed field groups 
            RecordsToStandardize unparsedRawRecords = getUnparsedRecords(objToStandardize, unparsedGroups);
            // Standardize all the values. It's possible that for one text record going in
            // there are multiple records coming back (i.e. the record contained multiple entities)
            // - this is considered an error condition though as the record
            // is meant to only contain one, and this will be logged. 
            String domains[] = unparsedRawRecords.getDomains();

            SbmeStandRecord[][] allUnparsedStandRecords = 
                    standardizationEngine.standardize(
                    unparsedRawRecords.getRecStandardizationTypes(), 
                    unparsedRawRecords.getRecords(),
                    domains);

            SbmeStandRecord[] unparsedStandRecords = 
                keepFirstStandardizedVersion(allUnparsedStandRecords, objToStandardize, 
                    unparsedRawRecords.getRecords(), unparsedRawRecords.getRecStandardizationTypes());

            // Handle pre-parsed field groups
            RecordsToNormalize parsedRawRecords = 
                getParsedRecords(objToStandardize, preparsedGroups);
            
            // Normalize all the values
            domains = parsedRawRecords.getDomains();
            // For now only allow one domain

            SbmeStandRecord[] preparsedStandRecords = 
                standardizationEngine.normalize(parsedRawRecords.getRecords(), domains);

            // Update the SystemObject with values from the standardized records
            objToStandardize = updateSystemObject(objToStandardize, 
                unparsedStandRecords, unparsedRawRecords.getRecFieldGroups());
            objToStandardize = updateSystemObject(objToStandardize, 
                preparsedStandRecords, parsedRawRecords.getRecFieldGroups()); 

            // Handle fields to phoneticize
            objToStandardize = 
                phoneticizeFields(objToStandardize, phoneticFields);
        } catch (EPathException ex) {
            throw new StandardizationException(mLocalizer.t("MAT530: Failed to " + 
                                            "standardize the configured fields in " + 
                                            "the SystemObject: {0}", ex));
        } catch (PhoneticEncoderException ex) {
            throw new StandardizationException(mLocalizer.t("MAT531:  Failed to " + 
                                            "create phonetic code for the configured " + 
                                            "fields in the SystemObject: {0}", ex));
        } catch (SbmeStandardizationException ex) {
            // This signifies a problem with the standardization engine
            // The normal result of it not being able to standardize a record would be to return
            // null for that record, not to throw an exception
            throw new StandardizationException(mLocalizer.t("MAT532: The Standardization engine " + 
                                            "could not standardize the given record: {0}", ex));
        } catch (SbmeMatchEngineException ex) {
            throw new StandardizationException(mLocalizer.t("MAT533: Failed to standardize. " + 
                                            "The standardization engine reported an error: {0}", ex));
        } catch (java.io.IOException ex) {
            throw new StandardizationException(mLocalizer.t("MAT534: The Standardization engine " + 
                                            "failed to standardize the given record: {0}", ex));
        }
        
        return objToStandardize;
    }

    /**
     * Keep only the first standardized version of a record.
     * The standardizer might find several entities in one input record (e.g. 2 people)
     * In which case it will return them as separate records. As a pre-condition to 
     * a record here is only expected to contain data related to one entity, therefore
     * only the first is kept and error handling is preformed.
     * @param allUnparsedStandRecords all the standardized records
     * @param objToStandardize the SystemObject that was standardized
     * @param inputRecords the input records to the standardizer that were used 
     * @param inputStandardizationTypes the input standardization types to the standardizer that were used 
     * @return the first standardized version of a record
     * @throws StandardizationException a problem inspecting and preparing the standardized values
     * @throws ObjectException accessing the SystemObject for logging purposes failed
     */
    protected SbmeStandRecord[] keepFirstStandardizedVersion(SbmeStandRecord[][] allUnparsedStandRecords, 
            SystemObject objToStandardize, String[] inputRecords, String[] inputStandardizationTypes) 
            throws StandardizationException, ObjectException {

        SbmeStandRecord[] unparsedStandRecords = new SbmeStandRecord[allUnparsedStandRecords.length];
        
        // Retrieve only the first standardized record for each ingoing record
        // Log if there is more than one.
        for (int recCount = 0; recCount < allUnparsedStandRecords.length; recCount++) {
            // Retrieve all the standardized records for an ingoing record
            SbmeStandRecord[] recEntries = allUnparsedStandRecords[recCount];
            if (recEntries != null && recEntries.length > 0 && recEntries[0] != null) {
                // Keep only the first standardized version
                unparsedStandRecords[recCount] = recEntries[0];
            } else {
                // Log that there is no standardized version of the ingoing record
                // TODO: put customer data logging (inputRecords) on a separate logging topic so it
                // can be switched off 
            }

            // Log if there are more then one standardized version for an incoming record
            int noOfRealEntries = 0;
            for (int entryCount = 0; entryCount < recEntries.length; entryCount++) {
                if (recEntries[entryCount] != null) {
                    noOfRealEntries++;
                }
            }

            if (noOfRealEntries > 1) {
                // Log that there is more than one standardized version of the ingoing record
                // TODO: put customer data logging (inputRecords) on a separate logging topic so it
                // can be switched off
            }
        }
        
        return unparsedStandRecords;
    }    
    
    /**
     * Initialize the Sbme standardization engine and the adapter
     * @param config the standardization engine configuration configured
     * @throws StandardizationException if initialization failed
     */
    public void initialize(StandardizerEngineConfiguration config) 
            throws StandardizationException {
        try {
            standRecFactory = new SbmeStandRecordFactory();
            phoneticizer = new Phoneticizer();
            
            if (config == null) {            
                throw new StandardizationException(mLocalizer.t("MAT535: No standardization engine " + 
                                        "configuration class is configured for this standardization " +
                                        "adapter, unable to initialize Standardization Engine. " +
                                        "{0} expected", SbmeStandardizerAdapterConfig.class.getName()));
            }
            if (!(config instanceof SbmeStandardizerAdapterConfig)) {
                throw new StandardizationException(mLocalizer.t("MAT536: The configured " + 
                                        "standardization engine configuration class is not " +
                                        "compatible with this standardization adapter. " +
                                        "{0} expected but configured {1}", 
                                        SbmeStandardizerAdapterConfig.class.getName(),
                                        config.getClass().getName()));
                                        
            }
            SbmeStandardizerAdapterConfig eViewConfig =
                (SbmeStandardizerAdapterConfig) config;

            SbmeConfigFilesAccess cfgFilesAccess = eViewConfig.getConfigFileAccess();
            
            standardizationEngine = new SbmeStandardizationEngine(cfgFilesAccess);
            standardizationEngine.initializeData(cfgFilesAccess);

        } catch (SbmeMatchEngineException ex) {
            throw new StandardizationException(
                "Failed to initialize standardizer adapter, standardization engine reports an error: " 
                    + ex.getMessage(), ex);                                    
        } catch (Exception ex) {
            throw new StandardizationException(mLocalizer.t("MAT537: Failed to " + 
                                        "initialize the standardizer adapter: {0}", ex));
        }
    }

    /**
     * Shutdown and release and relevant resources in the tigris standardization
     * engine and adapter
     * @throws StandardizationException if it failed to shut down
     */
    public void shutdown() throws StandardizationException {
        if (standardizationEngine != null) {
            try {
                // TODO: add shutdown call once engine provides one
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Shutting down the SBME standardization engine");
                }
                //standardizationEngine.shutdown();
            } catch (Exception ex) {
                throw new StandardizationException(mLocalizer.t("MAT538: Failed to " + 
                                        "shutdown standardization engine: {0}", ex));
            } finally {
                standardizationEngine = null;
            }
        }        
    }

    /**
     * Given the SystemObject and the configured standardization settings, 
     * retrieve the individual records (e.g. an address) that need to be 
     * standardized (free from text to standardize)
     * @param objToStandardize the SystemObject to standardize
     * @param unparsedGroups the unparsed groups (= free from text) 
     * to standardize configuration
     * @throws ObjectException if the retrieval of the configured Object 
     * values fails
     * @throws EPathException if the configured EPaths are invalid
     */
    RecordsToStandardize getUnparsedRecords(SystemObject objToStandardize, 
                                            ArrayList unparsedGroups) 
            throws ObjectException, EPathException {

        RecordsToStandardize recs = new RecordsToStandardize();
        
        // Build all the values to call standardizer
        ArrayList standardizationTypes = new ArrayList();
        ArrayList values = new ArrayList();        
        
        // A position-related arraylist that describes the field group of
        // a standardized record (i.e. standRecords)
        ArrayList correspondingFieldGroups = new ArrayList();
        ArrayList domains = new ArrayList();
        
        // Go through unparsed field groups        
        Iterator unparsedIter = unparsedGroups.iterator();
        while (unparsedIter.hasNext()) {
            UnparsedFieldGroup fieldGroup = 
                (UnparsedFieldGroup) unparsedIter.next();
            
            String standType = fieldGroup.getStandardizationType();
           
            // Concatenate all the values in the field group for each object 
            // node for parsing
            ArrayList allColumns = new ArrayList(); // List of column ArrayLists
            int maxRows = 0;
            ArrayList sourceFields = fieldGroup.getSourceFields();
            Iterator sourceFieldsIter = sourceFields.iterator();
            while (sourceFieldsIter.hasNext()) {
                SystemObjectField field = 
                    (SystemObjectField) sourceFieldsIter.next();
                String aFieldName = field.getQualifiedName();
                EPath aFieldEPath = field.getEPath();

                ArrayList outColumnValues = new ArrayList();
                EPathAPI.getFieldList(aFieldEPath, 0, 
                    objToStandardize.getObject(), outColumnValues);
                allColumns.add(outColumnValues);
                if (outColumnValues.size() > maxRows) {
                    maxRows = outColumnValues.size();
                }
            }
            DomainSelector domainSelector = fieldGroup.getDomainSelector();
            int noOfColumns = allColumns.size();
            if (noOfColumns > 0) {
                String[] sDomain = domainSelector.getDomains(objToStandardize, fieldGroup, allColumns);
                for (int i=0; i < sDomain.length; i++) {
                    domains.add(sDomain[i]);
                }
            }            
            
            for (int rowCount = 0; rowCount < maxRows; rowCount++) {
                StringBuffer concatValue = new StringBuffer();
                for (int colCount = 0; colCount < noOfColumns; colCount++) {
                    String aValue = null;
                    // Retrieve field value
                    ArrayList currCol = (ArrayList) allColumns.get(colCount);
                    if (currCol.size() > rowCount) {
                        Object anObj = currCol.get(rowCount);
                        if (anObj != null) {
                            // All values are currently converted to a String 
                            // via toString, no special conversion is done 
                            // for Dates etc.
                            aValue = anObj.toString();
                        } else {
                            aValue = "";
                        }
                    } else {
                        aValue = "";
                    }
                    
                    // Concatenate field value with previous values of a row
                    if (concatValue.length() != 0 && aValue.length() != 0) {
                        concatValue.append(CONCAT_DELIMITER);
                    }
                    concatValue.append(aValue);
                }
                // Add another free-from text string to standardize
                values.add(concatValue.toString());
                // Add another standardization type such as person name or 
                // person address            
                standardizationTypes.add(standType);
                // Add the corresponding field group of this record
                correspondingFieldGroups.add(fieldGroup);
            }
        }

        recs.setRecords((String[]) values.toArray(new String[values.size()]));
        recs.setRecStandardizationTypes((String[]) standardizationTypes.toArray(
            new String[standardizationTypes.size()]));
        recs.setRecFieldGroups((StandardizationFieldGroup[]) 
                correspondingFieldGroups.toArray(new 
                StandardizationFieldGroup[correspondingFieldGroups.size()]));
        recs.setDomains((String[]) domains.toArray(new String[domains.size()]));

        return recs;
    }

    /**
     * Given the SystemObject and the configured standardization settings, 
     * retrieve the individual records (e.g. an address) that need to be 
     * normalized (structures to normalize)
     * @param objToStandardize the SystemObject to standardize
     * @param parsedGroups the parsed groups (= structures) 
     * to normalize configuration
     * @throws ObjectException if the retrieval of the configured Object 
     * values fails
     * @throws EPathException if the configured EPaths are invalid
     * @throws StandardizationException if the standardization configuration 
     * is invalid
     */    
    RecordsToNormalize getParsedRecords(SystemObject objToStandardize, 
                                        ArrayList parsedGroups) 
            throws ObjectException, EPathException, StandardizationException {
        
        RecordsToNormalize recs = new RecordsToNormalize();

        // Build all the values to call standardization engine
        ArrayList standardizationTypes = new ArrayList();
        ArrayList values = new ArrayList();        
        ArrayList correspondingFieldGroups = new ArrayList(); 
        ArrayList domains = new ArrayList();
        
        // Go through pre-parsed field groups to normalize them
        Iterator parsedIter = parsedGroups.iterator();
        while (parsedIter.hasNext()) {
            PreparsedFieldGroup fieldGroup = 
                (PreparsedFieldGroup) parsedIter.next();
            String standType = fieldGroup.getStandardizationType();
            
            ArrayList allColumns = new ArrayList(); // List of Column ArrayLists
            ArrayList colsTargetStandObjectFieldID = new ArrayList();
            int maxRows = 0;
            
            LinkedHashMap sourceFieldsDirectMap = 
                fieldGroup.getSourceFieldsDirectMap();
            Iterator sourceFieldsIter = 
                sourceFieldsDirectMap.entrySet().iterator();
            while (sourceFieldsIter.hasNext()) {
                Entry entry = (Entry) sourceFieldsIter.next();
                SystemObjectField sourceField = 
                    (SystemObjectField) entry.getKey();
                String targetStandObjectFieldID = (String) entry.getValue();
                
                // Get the unnormalized (but parsed) value from the SystemObject
                String aFieldName = sourceField.getQualifiedName();
                EPath aFieldEPath = sourceField.getEPath();

                ArrayList outColumnValues = new ArrayList();
                EPathAPI.getFieldList(aFieldEPath, 0, 
                    objToStandardize.getObject(), outColumnValues);

                allColumns.add(outColumnValues);
                colsTargetStandObjectFieldID.add(targetStandObjectFieldID);
                if (outColumnValues.size() > maxRows) {
                    maxRows = outColumnValues.size();
                }
                if (allColumns.size() > 0) {
                    DomainSelector domainSelector = fieldGroup.getDomainSelector();
                    String[] sDomain = domainSelector.getDomains(objToStandardize, fieldGroup, allColumns);
                    for (int i=0; i < sDomain.length; i++) {
                        domains.add(sDomain[i]);
                    }
                }
            }
            
            int noOfColumns = allColumns.size();
            for (int rowCount = 0; rowCount < maxRows; rowCount++) {
                // Build a StandRecord from the unstandardized values 
                // for normalizing
                SbmeStandRecord standRec = null;
                try {
                    standRec = standRecFactory.getInstance(standType);
                } catch (IllegalArgumentException ex) {
                    throw new StandardizationException(mLocalizer.t("MAT539: The configured " + 
                                    "standardization type is invalid: {0}. " + 
                                    "The standardization engine threw an exception " +
                                    "while trying creating a record of that type: {1}", 
                                    standType, ex));
                }

                for (int colCount = 0; colCount < noOfColumns; colCount++) {
                    String aValue = null;
                    // Retrieve field value
                    ArrayList currCol = (ArrayList) allColumns.get(colCount);
                    if (currCol.size() > rowCount) {
                        Object anObj = currCol.get(rowCount);
                        if (anObj != null) {
                            // All values are currently converted to a String 
                            // via toString, no special conversion is done 
                            // for Dates etc.
                            aValue = anObj.toString();
                        } 
                    }  

                    // Fill the StandRecord with the value
                    standRec.setValue((String) 
                        colsTargetStandObjectFieldID.get(colCount), aValue);
                }
                // Add another record to normalize
                values.add(standRec);
                // Add another standardization type such as person name 
                // or person address
                standardizationTypes.add(standType);
                // Add the corresponding field group of the record
                correspondingFieldGroups.add(fieldGroup);
            }
        }

        recs.setRecords((SbmeStandRecord[]) 
            values.toArray(new SbmeStandRecord[values.size()]));
        recs.setRecStandardizationTypes((String[]) standardizationTypes.toArray(
            new String[standardizationTypes.size()]));
        recs.setRecFieldGroups((StandardizationFieldGroup[]) 
                correspondingFieldGroups.toArray(new 
                StandardizationFieldGroup[correspondingFieldGroups.size()]));
        recs.setDomains((String[]) domains.toArray(new String[domains.size()]));
        return recs;        
    }


    /**
     *  Add the standardized values to the SystemObject
     * @param objToStandardize the SystemObject to standardize and update
     * @param standRecords The standardized records returned from tigris
     * @param recordsFieldGroups the field group configuration corresponding to
     * each standRecords entry
     * @throws ObjectException if the retrieval/setting of the configured Object
     * values fails
     * @throws EPathException if the configured EPaths are invalid
     */
    SystemObject updateSystemObject(SystemObject objToStandardize, 
            SbmeStandRecord[] standRecords, 
            StandardizationFieldGroup[] recordsFieldGroups) 
            throws ObjectException, EPathException {

        ArrayList fieldGroupStandRecords = new ArrayList();
        
        // Process each standardized record
        int noOfStandRecords = 0;
        if (standRecords != null) {
            noOfStandRecords = standRecords.length;
        }
        for (int position = 0; position < noOfStandRecords; position++) {
            // Retrieve the standardized record for the fieldGroup
            SbmeStandRecord aStandRec = standRecords[position];            
            
            // Retrieve the fieldGroup corresponding to the record
            StandardizationFieldGroup fieldGroup = recordsFieldGroups[position];
            // Retrieve the fieldGroup of the next record if it exists
            StandardizationFieldGroup nextFieldGroup = null;
            if (recordsFieldGroups.length > position + 1) {
                nextFieldGroup = recordsFieldGroups[position + 1];
            }
            
            // Collect the standardized records for a field group            
            fieldGroupStandRecords.add(aStandRec);
            
            // If the next fieldGroup is different than the current one 
            // (or this is the last record),
            // process all the values for the field group. 
            // Otherwise, continue collecting values for the field group
            if (nextFieldGroup != fieldGroup) {

                // Process all the targets of a field group 
                // (e.g. standardized fields for firstname,  Address[*].street)
                LinkedHashMap targets = fieldGroup.getStandardizationTargets();
                Iterator targetsIter = targets.entrySet().iterator();
                String lastTargetFieldName = null;
                ArrayList lastStandFieldValues = null;
                while (targetsIter.hasNext()) {
                    Entry entry = (Entry) targetsIter.next();
                    String srcStandObjectFieldID = (String) entry.getKey();
                    SystemObjectField targetField = 
                        (SystemObjectField) entry.getValue();
                    String targetFieldName = 
                        (String) targetField.getQualifiedName();
                    EPath targetFieldEPath = targetField.getEPath();
                    int maxFieldSize = targetField.getFieldSize();
                    
                    // Special case handling if this target field is the same as the previous one
                    // Then we keep the first valid value found
                    boolean sameTargetField = false;
                    if (lastTargetFieldName != null && lastTargetFieldName.equals(targetFieldName)) {
                        sameTargetField = true;
                    } else {
                        lastStandFieldValues = null;
                    }

                    // Retrieve the standardized value(s) for one column 
                    // ('field') from all standardized records belonging to 
                    // the current fieldGroup
                    ArrayList standFieldValues = new ArrayList();
                    Iterator groupStandRecIter = fieldGroupStandRecords.iterator();
        
                    while (groupStandRecIter.hasNext()) {
                        SbmeStandRecord groupStandRec = (SbmeStandRecord) groupStandRecIter.next();

                        // Get standardized value from the standardized object
                        // The standardized object is null if the standardizer
                        // could not standardize it at all.
                        String standFieldValue = null;
                        if (groupStandRec != null) {
                            String srcVal = groupStandRec.getValue(srcStandObjectFieldID);
                            if (srcVal != null && srcVal.length() > 0) {
                                standFieldValue = srcVal;
                            }
                        }
                                                
                        // if the current target field is the same as the previous one, 
                        // keep the first valid value found for it.
                        if (sameTargetField) {
                            int valuePos = standFieldValues.size();
                            String prevStandFieldValue = null;
                            if (lastStandFieldValues != null && lastStandFieldValues.size() > valuePos) {
                                prevStandFieldValue = (String) lastStandFieldValues.get(valuePos);
                            }
                            if (prevStandFieldValue != null) {
                                standFieldValue = prevStandFieldValue;
                            } else {
                            }
                        }
                        
                        // Trim the standardized values to the maximum field size.
                        if (standFieldValue != null && maxFieldSize > 0 && standFieldValue.length() > maxFieldSize) {
                            String untrimmed = standFieldValue;
                            standFieldValue = standFieldValue.substring(0, maxFieldSize);
                        }
                        
                        standFieldValues.add(standFieldValue);
                    }

                    // Set the standardized field(s) in the systemobject 
                    // to the value(s)
                    EPathAPI.addObjectValues(targetFieldEPath, 0, 
                        objToStandardize.getObject(), standFieldValues);
                    
                    // Keep the values of this loop iteration for special case handling
                    // of the same target fields configured in sequence.
                    // - which means 'keep first valid value' for this target field
                    lastStandFieldValues = standFieldValues;
                    lastTargetFieldName = targetFieldName;
                }
                
                fieldGroupStandRecords.clear();    
            }
        }
       
        return objToStandardize;
    }
    
    /**
     * Phoneticize all the configured fields
     * This will change the passed in SystemObject
     * @param objToStandardize The SystemObject in which to phoneticize fields. 
     *        This method changes this passed in SystemObject directly and 
     *        returns a reference to it.
     * @param phoneticFields an ArrayList of all the fields to phoneticize, 
     *        contains PhoneticizeField elements.
     * @return the SystemObject with all the fields to phoneticize populated
     */
    SystemObject phoneticizeFields(SystemObject objToPhoneticize, 
                                   ArrayList phoneticFields) 
            throws ObjectException, EPathException, PhoneticEncoderException {
        // Go through all the fields to phoneticize
        Iterator fieldsIter = phoneticFields.iterator();
        while (fieldsIter.hasNext()) {
            PhoneticizeField phoneticField = 
                (PhoneticizeField) fieldsIter.next();
            SystemObjectField sourceField = phoneticField.getSourceField();
            SystemObjectField targetField = phoneticField.getTargetField();
            int maxFieldSize = targetField.getFieldSize();
            String encodingType = phoneticField.getEncodingType();

            // Retrieve the value(s) to phoneticize from the SystemObject
            String sourceFieldName = sourceField.getQualifiedName();

            EPath sourceFieldEPath = sourceField.getEPath();
            ArrayList inOutValues = new ArrayList();
            EPathAPI.getFieldList(sourceFieldEPath, 0, 
                objToPhoneticize.getObject(), inOutValues);

            String domain = null;
            String targetFieldName = targetField.getQualifiedName();
            ArrayList phoneticFieldValues = new ArrayList();

            Iterator valuesIter = inOutValues.iterator();
            // Phoneticize the field(s)
            while (valuesIter.hasNext()) {
                Object anObj = valuesIter.next();
                String aValue = null;
                String phoneticFieldValue = null;
                if (anObj != null) {
                    // All values are currently converted to a String via
                    // toString, no special conversion is done for Dates etc.
                    aValue = anObj.toString();

                    phoneticFieldValue = phoneticizer.encode(aValue, encodingType, domain);
                    // Trim the phoneticized values to the maximum field size.
                    if (phoneticFieldValue != null && maxFieldSize > 0 
                            && phoneticFieldValue.length() > maxFieldSize) {
                        String untrimmed = phoneticFieldValue;
                        phoneticFieldValue = phoneticFieldValue.substring(0, maxFieldSize);
                    }                    
                }
                phoneticFieldValues.add(phoneticFieldValue);
            }

            EPath targetFieldsEPath = targetField.getEPath(); 
            
            //? Should we remove all existing entries (sigma objects) 
            //? before adding all of these?
            // Set the phoneticized field(s) in the systemobject to the value(s)
            EPathAPI.addObjectValues(targetFieldsEPath, 0, 
                objToPhoneticize.getObject(), phoneticFieldValues);
        }
        
        return objToPhoneticize;
    }
}

/**
 * Utility class to hold the records to standardize
 */
class RecordsToStandardize {
    private String[] records;                   // The records to standardize
    private String[] recStandardizationTypes;   // The standardization type corresponding to each record
    private StandardizationFieldGroup[] recFieldGroups; // The field group corresponding to each record
    private String[] domains;
    /**
     * Accessor for Records
     * @return the Strings to standardize
     */    
    public String[] getRecords() {
        return records;
    }
        
    /**
     * Accessor for StandardizationTypes
     * @return The standardization type corresponding to each record
     */    
    public String[] getRecStandardizationTypes() {
        return recStandardizationTypes;
    }

    /**
     * Accessor for FieldGroup
     * @return the field group corresponding to each record
     */    
    public StandardizationFieldGroup[] getRecFieldGroups() {
        return recFieldGroups;
    }

    /**
     * Accessor for Records
     * @param records the Strings to standardize
     */
    public void setRecords(String[] records) {
        this.records = records;
    }    
    
    /**
     * Accessor for StandardizationTypes
     * @param recStandardizationTypes the standardization type corresponding 
     * to each record
     */
    public void setRecStandardizationTypes(String[] recStandardizationTypes) {
        this.recStandardizationTypes = recStandardizationTypes;
    }

    /**
     * Accessor for FieldGroup
     * @param recFieldGroups the field group corresponding to each record
     */
    public void setRecFieldGroups(StandardizationFieldGroup[] recFieldGroups) {
        this.recFieldGroups = recFieldGroups;
    }
    
    public void setDomains(String[] domains) {
        this.domains = domains;
    }
    
    public String[] getDomains() {
        return domains;
    }
}

/**
 * Utility class to hold the records to normalize
 */
class RecordsToNormalize {
    private SbmeStandRecord[] records;                   // The records to normalize
    private String[] recStandardizationTypes;           // The standardization type corresponding to each record
    private StandardizationFieldGroup[] recFieldGroups; // The field group corresponding to each record
    private String[] domains;
    
    /**
     * Accessor for Records
     * @return the records to normalize
     */
    public SbmeStandRecord[] getRecords() {
        return records;
    }
    
    /**
     * Accessor for StandardizationTypes
     * @return The standardization type corresponding to each record
     */    
    public String[] getRecStandardizationTypes() {
        return recStandardizationTypes;
    }

    /**
     * Accessor for FieldGroup
     * @return the field group corresponding to each record
     */
    public StandardizationFieldGroup[] getRecFieldGroups() {
        return recFieldGroups;
    }

    /**
     * Accessor for Records
     * @param records the records to normalize
     */    
    public void setRecords(SbmeStandRecord[] records) {
        this.records = records;
    }

    /**
     * Accessor for StandardizationTypes
     * @param recStandardizationTypes the standardization type corresponding 
     * to each record
     */
    public void setRecStandardizationTypes(String[] recStandardizationTypes) {
        this.recStandardizationTypes = recStandardizationTypes;
    }

    /**
     * Accessor for FieldGroup
     * @param recFieldGroups the field group corresponding to each record
     */    
    public void setRecFieldGroups(StandardizationFieldGroup[] recFieldGroups) {
        this.recFieldGroups = recFieldGroups;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }
    
    public String[] getDomains() {
        return domains;
    }
    
}
