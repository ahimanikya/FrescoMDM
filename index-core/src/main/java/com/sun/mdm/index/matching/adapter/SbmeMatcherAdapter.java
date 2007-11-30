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
import java.util.ArrayList;
import java.util.HashMap;
import java.text.ParseException;
import com.sun.mdm.index.matching.MatchingException;
import com.sun.mdm.index.matching.MatchEngineConfiguration;
import com.sun.mdm.index.matching.ScoreElement;
import com.sun.mdm.index.matching.MatchOptions;
import com.sun.mdm.index.matching.converter.MatchTupleConverter;
import com.sun.mdm.index.matching.converter.MatchTuples;
import com.sun.mdm.index.query.QueryResults;
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.query.MatchTuple;
import com.sun.mdm.index.query.MatchTupleAssembler;
import com.sun.mdm.index.query.QMException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.exception.ObjectException;

import com.sun.mdm.matcher.api.MatchingEngine;
import com.sun.mdm.matcher.api.MatcherException;
import com.sun.mdm.matcher.api.ConfigFilesAccess;

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * MatcherAPI implementation that allows MEFA to communicate with the
 * Sbme match engine.
 * @author  pkar
 * @version $Revision: 1.1 $
 */
public class SbmeMatcherAdapter 
        implements com.sun.mdm.index.matching.MatcherAPI {
        
    private MatchTupleConverter tupleConverter;
    private AssembleDescriptor tupleDescriptor;
    private MatchingEngine matchEngine;
    
    /**  mLogger instance
     *
     */
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Creates new SbmeMatcherAdapter */
    public SbmeMatcherAdapter() {
    }

    /**
     * Uses the sbme match engine to find all the match weights comparing the 
     * bases systemobject to the 'block' records retrieved from the DB
     * @param base the system object to match against
     * @param block the block of DB records to compare with
     * @param matchOptions controls any matcher options such as minimum score
     * @throws MatchingException if the Matching failed
     * @return  An ArrayList containing the ScoreElements with the weights 
     * associated with an EUID.
     */
    public ArrayList findWeights(SystemObject base, 
                                 QueryResults block, 
                                 MatchOptions matchOptions) 
                                 throws MatchingException {
        long[] time = null;
        double minimumScore = matchOptions.getMinimumWeight();
        // Do not sort the weights in the adapter - it would be an unnecessary
        // performance hit as the calling EJB will take care of it if necessary
        // boolean sortWeights = matchOptions.getSortWeightResults();
        
        ArrayList resultWeights = new ArrayList();
        HashMap mapped = new HashMap();
        
        QMIterator tupleIter = null;

        try {
            
            MatchTuples matchTuples = tupleConverter.convert(base);
            String[][] objToMatchTuples = matchTuples.getTuples();
            String[] fieldIDs = matchTuples.getColumnMatchTypes();
            
            block.setAssembleDescriptor(tupleDescriptor);

            tupleIter = block.assemble();
            
            int pageSize = 1000; 

            String[][] blockTuples = new String[pageSize][];
            String[] euids = new String[pageSize];
            
            while (tupleIter.hasNext()) {
                int noOfBlockRecords = 0;
                
                // build up a 'page' of data to match against
                for (int itemCount = 0; itemCount < pageSize 
                        && tupleIter.hasNext(); itemCount++) {
                    MatchTuple matchTuple = (MatchTuple) tupleIter.next();
                    euids[itemCount] = matchTuple.getEuid();
                    blockTuples[itemCount] = matchTuple.getData();
                    noOfBlockRecords++;
                }
                
                // If the arrays are too big, reduce their size. 
                // Arrays instead of collections are used for performance 
                // reasons.
                if (pageSize > noOfBlockRecords) {
                    String[][] reducedTuples = new String[noOfBlockRecords][];
                    String[] reducedEUIDs = new String[noOfBlockRecords];
                    System.arraycopy(blockTuples, 0, reducedTuples, 0, 
                        noOfBlockRecords);
                    System.arraycopy(euids, 0, reducedEUIDs, 0, 
                        noOfBlockRecords);
                    blockTuples = reducedTuples;
                    euids = reducedEUIDs;
                }
                
                // match all the records for the candidate object against the 
                // records for the block
                double[][] someWeights = matchEngine.matchWeight(fieldIDs, 
                    objToMatchTuples, blockTuples); 
                
                // The weights are in a 'rectangular' array of arrays, 
                // therefore we only need to get this length once.
                int noOfBlockRecWeights = 0;
                if (someWeights.length > 0) {
                    noOfBlockRecWeights = someWeights[0].length;
                }

                String lastEUID = null;
                double lastHighest = Double.NEGATIVE_INFINITY;
                
                // Go through the results for each 'block' record.
                for (int blockRecCount = 0; blockRecCount < noOfBlockRecWeights;
                        blockRecCount++) {
                    String currEUID = euids[blockRecCount];
                    
                    // For each block record, go trough all the different 
                    // weights comparing it to the candidate records
                    for (int objToMatchCount = 0; objToMatchCount 
                            < someWeights.length; objToMatchCount++) {
                        double currWeight = 
                            someWeights[objToMatchCount][blockRecCount];
                        
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("Retrieved EUID: "+ currEUID + " with weight:" + currWeight);
                        }
                        
                        // Keep the highest weight for each EUID.
                        // If the EUID changed, save the score. 
                        // Notice that for the last EUID in weights array,
                        // this will not save anything, that is why it adds it 
                        // outside of these loops.
                        if (lastEUID != null && !lastEUID.equals(currEUID)) {
                            if (lastHighest >= minimumScore) {
                                ScoreElement exists = (ScoreElement)mapped.get(lastEUID);
                                if (exists == null) {
                                    resultWeights.add(
                                        new ScoreElement(lastEUID, lastHighest));
                                    mapped.put(lastEUID, new ScoreElement(lastEUID, lastHighest));
                                } else {
                                    if (exists.getEUID().equals(lastEUID)) {
                                        if (exists.getWeight() < lastHighest) {
                                           mapped.remove(lastEUID);
                                           mapped.put(lastEUID, new ScoreElement(lastEUID, lastHighest));
                                           resultWeights.remove(exists);
                                           resultWeights.add(new ScoreElement(lastEUID, lastHighest)); 
                                        }
                                    }
                                }
                            }
                            lastHighest = Double.NEGATIVE_INFINITY;
                        } 
                        lastEUID = currEUID;
                        if (lastHighest < currWeight) {
                            lastHighest = currWeight;
                        }
                    }
                }
                // Save the score for the last EUID if applicable
                if (lastEUID != null) {
                    if (lastHighest >= minimumScore) {
                        ScoreElement exists = (ScoreElement)mapped.get(lastEUID);
                        if (exists == null) {
                                    resultWeights.add(
                                        new ScoreElement(lastEUID, lastHighest));
                                    mapped.put(lastEUID, new ScoreElement(lastEUID, lastHighest));
                                } else {
                                    if (exists.getEUID().equals(lastEUID)) {
                                        if (exists.getWeight() < lastHighest) {
                                           mapped.remove(lastEUID);
                                           mapped.put(lastEUID, new ScoreElement(lastEUID, lastHighest));
                                           resultWeights.remove(exists);
                                           resultWeights.add(new ScoreElement(lastEUID, lastHighest)); 
                                        }
                                    }
                                }
                       }
                }
                
            }
        } catch (MatcherException ex) {
            throw new MatchingException(mLocalizer.t("MAT516: SBME adapter encountered a matching exception: {0}", ex));
        } catch (ParseException ex) {
            throw new MatchingException(mLocalizer.t("MAT518: SBME adapter encountered a parsing exception: {0}", ex));
        } catch (EPathException ex) {
            throw new MatchingException(mLocalizer.t("MAT519: SBME adapter failed to match the configured " + 
                                                     "fields in a SystemObject: {0}", ex));
        } catch (QMException ex) {
            throw new MatchingException(mLocalizer.t("MAT520: SBME adapter encountered a QMException " + 
                                                     "and failed to retrieve candidate " + 
                                                     "records from database: {0}", ex));
        } catch (ObjectException ex) {
            throw new MatchingException(mLocalizer.t("MAT521: SBME adapter failed to to convert " + 
                                              "a SystemObject to candidate format for matching: {0}", ex));
        } finally {
            // clean-up QMIterator
            if (tupleIter != null) {
                try {
                    tupleIter.close();
                } catch (QMException ex) {
                    throw new MatchingException(mLocalizer.t("MAT522: QMException: " + 
                                                    "Failed to close QMIterator: {0}", ex));
                }
            }            
        }
        
        return resultWeights;        
    }

    /**
     * Initialize the Sbme match engine and the adapter
     * @param config the match engine configuration configured
     * @throws MatchingException if the initialization failed
     */
    public void initialize(MatchEngineConfiguration config) 
            throws MatchingException {
        try {
            tupleConverter = new MatchTupleConverter();
            
            tupleDescriptor = new AssembleDescriptor();
            MatchTupleAssembler assembler = new MatchTupleAssembler();
            tupleDescriptor.setAssembler(assembler);
            
            // Handle the configuration passed in.
            if (config == null) {            
                throw new MatchingException(mLocalizer.t("MAT523: No match engine configuration " 
                        + "class is configured for this match adapter.  The " 
                        + "match engine cannot be initialized: {0} expected", 
                        SbmeMatcherAdapterConfig.class.getName()));
            }
            if (!(config instanceof SbmeMatcherAdapterConfig)) {
                throw new MatchingException(mLocalizer.t("MAT524: The configured match engine " 
                        + "configuration class is not compatible with this " 
                        + "match adapter: {0} expected, but configured: {1} ",
                        SbmeMatcherAdapterConfig.class.getName(),
                        config.getClass().getName()));
            }
            SbmeMatcherAdapterConfig eViewConfig = 
                (SbmeMatcherAdapterConfig) config;
            
            // TODO: where to get this from? Shouldn't it be initialized for all?
            String domain = "US";
            String configFileName = eViewConfig.getConfigurationFileName();
            ConfigFilesAccess cfgFilesAccess = eViewConfig.getConfigFileAccess();
            matchEngine = new MatchingEngine(cfgFilesAccess);
            matchEngine.initializeData(cfgFilesAccess);
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Setting configuration file:" + configFileName + 
                             " for domain: " + domain);
            }
            matchEngine.upLoadConfigFile(domain);
        } catch (MatcherException ex) {
            throw new MatchingException(mLocalizer.t("MAT525: Failed to initialize the " + 
                                        "SBME match adapter: {0}", ex));
        } catch (Exception ex) {
            throw new MatchingException(mLocalizer.t("MAT526: Failed to initialize the " + 
                                        "SBME match adapter: {0}", ex));
        }
    }

    /**
     * Shutdown and release any resources associated with the Sbme match 
     * engine and the adapter
     * @throws MatchingException if the shutdown failed
     */    
    public void shutdown() 
            throws MatchingException {
        if (matchEngine != null) {
            try {
                matchEngine.shutdown();
            } catch (Exception ex) {
                throw new MatchingException(mLocalizer.t("MAT527: Failed to shut down match " + 
                                            "engine: {0}", ex));
            } finally {
                matchEngine = null;
            }
        }
    }
    
}
