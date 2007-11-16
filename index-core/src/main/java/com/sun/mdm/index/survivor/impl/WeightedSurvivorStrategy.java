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
package com.sun.mdm.index.survivor.impl;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.WeightedCalculatorConfig;

import com.sun.mdm.index.survivor.SurvivorStrategyInterface;
import com.sun.mdm.index.survivor.SurvivorCalculationException;
import com.sun.mdm.index.survivor.StrategyCreationException;
import com.sun.mdm.index.survivor.SystemField;
import com.sun.mdm.index.survivor.SystemFieldList;
import com.sun.mdm.index.survivor.SystemFieldListMap;
import com.sun.mdm.index.survivor.StrategyParameter;
import com.sun.mdm.index.util.Localizer;



/** Strategy for determing survivor. Calculates utility based on preference
 * and quality.
 *
 
 * @version $Revision: 1.1 $
 */
public class WeightedSurvivorStrategy implements SurvivorStrategyInterface {
    
    /** last modified constant */
    public static final String FLD_LAST_MODIFIED = "LastModified";
    
    private transient static final Logger mLogger 
                = Logger.getLogger("com.sun.mdm.index.survivor.impl.WeightedSurvivorStrategy");
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** configuration object */
    private WeightedCalculatorConfig mConfig;
    
    /** Creates new MySurvivalStrategy */
    public WeightedSurvivorStrategy() {
    }
    
    /** returns the value for the specified candidate field name from a list of system fields
     * @param fieldName field name
     * @param systemFields SystemFieldList of fields
     * @return field value
     */
    private Object getCandidateValue(String fieldName, SystemFieldList systemFields) {
        SystemField f = systemFields.get(fieldName);
        
        return (f == null) ? null : f.getValue();
    }
    
    /** return a SystemField object of candidate field name
     * @param fieldName candidate field name
     * @param systemFields SystemFieldLust of system fields
     * @return SystemField object
     */
    private SystemField getCandidateField(String fieldName, SystemFieldList systemFields) {
        return systemFields.get(fieldName);
    }
    
    /** determine the utility for the SourceSystem quality
     * @param q quality configuration from user defined XML
     * @param scores reference to the scores MaxHeap
     * @param candidateId field name of the set of values to be compared.  Ignored for this quality
     * @param fields set of fields for each system
     * @throws SurvivorCalculationException preferred system id does not exist in actual field list map parameter
     */
    protected void processSourceSystem(SurvivorQuality q, Map scores, String candidateId, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        
        String preferredSystem = q.getPreference();
        
        // see if it's in the source system
        // return the subset of systemFieldLists that has is from the preferred
        // source system
        Set keySet = fields.keySet(preferredSystem);
        Iterator iter = keySet.iterator();
        while (iter.hasNext()) {
            SystemFieldListMap.SystemKey systemKey = (SystemFieldListMap.SystemKey) iter.next();
            SystemFieldList sfl = fields.get(systemKey);
            
            Double currentScore = (Double) scores.get(systemKey);
            if ((currentScore != null) && (sfl != null)) {
                // check the value of the candidate field, don't consider the system if value == null
                if (getCandidateValue(candidateId, sfl) != null) {
                    synchronized (scores) {
                        double d = currentScore.doubleValue();
                        d += q.getUtility();
                        scores.put(systemKey, new Double(d));
                    }
                }
            }
        }
    }
    
    /**
     * @param q quality configuration from user defined XML
     * @param scores reference to the scores MaxHeap
     * @param candidateId field name of the set of values to be compared.  Ignored for this quality
     * @param fields set of fields for each system
     * @throws SurvivorCalculationException if a expect value is not present
     */
    protected void processMostRecentModified(SurvivorQuality q, Map scores,
        String candidateId, SystemFieldListMap fields)
        throws SurvivorCalculationException {
        
        Set keys = fields.keySet();
        Iterator dateIter = keys.iterator();
        TreeMap dateRank = new TreeMap();
        
        SystemFieldListMap.SystemKey systemKey = null;
        
        // sort system id by last modified Date value
        // if two systems have the same last modified date, all records with same
        // date get the score increased
        while (dateIter.hasNext()) {
            systemKey = (SystemFieldListMap.SystemKey) dateIter.next();
            
            SystemFieldList systemFields = fields.get(systemKey);
            Date date = (Date) getCandidateValue(WeightedSurvivorStrategy.FLD_LAST_MODIFIED,
                systemFields);
            
            if (date == null) {
                throw new SurvivorCalculationException(mLocalizer.t("SUR528: Last " +
                                            "modified date does not exist in " + 
                                            "actual field values for candidateID={0}, " +
                                            "systemKey={1}", candidateId, systemKey.toString()));
            }
            
            // check the value of the candidate field, don't consider the system if value == null
            if (getCandidateValue(candidateId, systemFields) != null) {
                ArrayList list = (ArrayList) dateRank.get(date);
                if (list == null) {
                    list = new ArrayList();
                    dateRank.put(date, list);
                }
                list.add(systemKey);
            }
        }
        
        if (!dateRank.isEmpty()) {
            // get the system id(s) of the highest modified date
            Object lastKey = dateRank.lastKey();
            ArrayList list = (ArrayList) dateRank.get(lastKey);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Processing MostRecentModified - highest ranking systems:\n" + list); 
            }
            Iterator i = list.iterator();
            
            // increment the utility of the system(s)
            synchronized (scores) {
                while (i.hasNext()) {
                    systemKey = (SystemFieldListMap.SystemKey) i.next();
                    Double currentScore = (Double) scores.get(systemKey);
                    double d = currentScore.doubleValue();
                    d += q.getUtility();
                    scores.put(systemKey, new Double(d));
                }
            }
        }
    }
    
    /**Given m source systems and n aggrements.  The resulting score is n/m of
     * the utility score specified in the configuration file
     * @param q quality configuration from user defined XML
     * @param scores reference to the scores MaxHeap
     * @param candidateId field name of the set of values to be compared.  Ignored for this quality
     * @param fields set of fields for each system
     * @throws SurvivorCalculationException unexpected values
     */
    protected void processSystemAgreement(SurvivorQuality q, Map scores,
    String candidateId, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        
        Set keySet = fields.keySet();
        int keyCount = keySet.size();
        
        Map values = new HashMap();
        
        // get only the relavent set of fields
        Set entries = fields.entrySet();
        Iterator iter = entries.iterator();
        
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            
            SystemFieldList sfl = (SystemFieldList) e.getValue();
            SystemField sf = sfl.get(candidateId);
            
            if ((sf != null) && (sf.getValue() != null)) {
                values.put(e.getKey(), sf);
            }
        }
        
        // for each item in the set, remove all candidate fields that have the same value
        while (values.size() > 0) {
            // count the items that are the same
            entries = values.entrySet();
            
            Map.Entry[] ea = (Map.Entry[]) entries.toArray(new Map.Entry[] {});
            
            SystemField f = (SystemField) ea[0].getValue();
            ArrayList list = new ArrayList();
            
            for (int i = 0; i < ea.length; i++) {
                Map.Entry e = ea[i];
                
                if (e != null) {
                    SystemField nf = (SystemField) e.getValue();
                    
                    if (nf.getValue().equals(f.getValue())) {
                        list.add(e.getKey());
                        ea[i] = null;
                        entries.remove(e);
                    }
                }
            }
            
            // adjust scores for those systems
            if (list.size() > 0) {
                double fraction = list.size() / (double) keyCount;
                
                for (int i = 0; i < list.size(); i++) {
                    SystemFieldListMap.SystemKey systemKey = 
                        (SystemFieldListMap.SystemKey) list.get(i);
                    
                    synchronized (scores) {
                        Double currentScore = (Double) scores.get(systemKey);
                        double d = currentScore.doubleValue();
                        d += (q.getUtility() * fraction);
                        scores.put(systemKey, new Double(d));
                    }
                }
            }
        }
    }
    
    /** Calculates the survivor value by using a MaxHeap to accrue utility scores.
     * User defines qualities,  preferences and utility scores for each quality.  Based
     * on the user defined values, return the SystemField that has the highest utility
     * after processing all of the qualities.
     * @param candidateId current candidate field to work with
     * @param fields set of fields associated with each system
     * @throws SurvivorCalculationException no rule has been defined for this candidate field
     * value not found for this candidate field
     * @return SystemField object holding the surviving value.
     */
    public SystemField selectField(String candidateId, SystemFieldListMap fields)
    throws SurvivorCalculationException {
        Collection rules = mConfig.getRules(candidateId);
        
        if (rules == null) {
            rules = mConfig.getDefaultRules();
            if (rules == null) {
                throw new SurvivorCalculationException(mLocalizer.t("SUR529: Both " +
                                            "custom and default rules are not " + 
                                            "defined for candidate field: {0}", candidateId));
            }
        }
        
        Map scores = Collections.synchronizedMap(new LinkedHashMap());
        
        // initialize all scores to 0.0
        Set systemKeys = fields.keySet();
        Iterator iter = systemKeys.iterator();
        
        
            while (iter.hasNext()) {
                SystemFieldListMap.SystemKey key = (SystemFieldListMap.SystemKey) iter.next();
                scores.put(key, new Double(0.0));
            }
        
        
        // process through all the rules
        iter = rules.iterator();
        
        while (iter.hasNext()) {
            SurvivorQuality q = (SurvivorQuality) iter.next();
            
            if (q.getQuality().equals("SourceSystem")) {
                processSourceSystem(q, scores, candidateId, fields);
            } else if (q.getQuality().equals("MostRecentModified")) {
                processMostRecentModified(q, scores, candidateId, fields);
            } else if (q.getQuality().equals("SystemAgreement")) {
                processSystemAgreement(q, scores, candidateId, fields);
            }
        }
        
        // finished processing all the rules
        // return the candidate field value for the system with the highest utility
        // if more then one system has the same utility score, return the last one inserted
        TreeMap heap = new TreeMap();
        Collection scoreSysKeys = scores.keySet();
        Iterator sysIter = scoreSysKeys.iterator();
        
        while (sysIter.hasNext()) {
            SystemFieldListMap.SystemKey key = (SystemFieldListMap.SystemKey) sysIter.next();
            Double score = (Double) scores.get(key);
            heap.put(score, key);
        }
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("total scores:\n" + heap);
        }
        
        if (!heap.isEmpty()) {
            Double maxScore = (Double) heap.lastKey();
            SystemFieldListMap.SystemKey maxSystemKey = 
                (SystemFieldListMap.SystemKey) heap.get(maxScore);
            return getCandidateField(candidateId, fields.get(maxSystemKey));
        } else {
            return null;
        }
    }
    
    /** returns a clone of the object
     * @return cloned object
     * @throws CloneNotSupportedException cloning not supported
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * {@inheritDoc}
     * @param parameters list of parameters used to initialize the strategy
     * @throws StrategyCreationException missing required parameter
     */
    public void init(java.util.Collection parameters)
    throws StrategyCreationException {
        String configModName = null;
        
        if ((parameters != null) && (parameters.size() > 0)) {
            Iterator iter = parameters.iterator();
            
            while (iter.hasNext()) {
                StrategyParameter p = (StrategyParameter) iter.next();
                
                if (p.getName().equals("ConfigurationModuleName")) {
                    configModName = p.getValue();
                }
            }
        }
        
        if ((configModName == null) || configModName.equals("")) {
            throw new StrategyCreationException(mLocalizer.t("SUR530: Configuration " +
                                            "module name is empty."));
        }
        
        try {
            ConfigurationService c = ConfigurationService.getInstance();
            mConfig = (WeightedCalculatorConfig) c.getConfiguration(configModName);
            
            if (mConfig == null) {
                throw new StrategyCreationException(mLocalizer.t("SUR531: Configuration " +
                                            "for module {0} cannot be null.", 
                                            WeightedCalculatorConfig.MODULE_NAME));
            }
        } catch (InstantiationException iex) {
            throw new StrategyCreationException(mLocalizer.t("SUR532: Unable " +
                                            "to retrieve configuration mBean instance: {0}", 
                                            iex));
        }
    }
}
