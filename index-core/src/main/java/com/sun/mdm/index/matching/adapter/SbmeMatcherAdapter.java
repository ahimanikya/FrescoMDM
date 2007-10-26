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

import com.stc.sbme.api.SbmeMatchingEngine;
import com.stc.sbme.api.SbmeMatchingException;
import com.stc.sbme.api.SbmeMatchEngineException;
import com.stc.sbme.api.SbmeConfigFilesAccess;

import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

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
    private SbmeMatchingEngine matchEngine;
    
    /**  mLogger instance
     *
     */
    private final Logger mLogger = LogUtil.getLogger(this);
    
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
                        
                        if (mLogger.isDebugEnabled()) {                       
                            mLogger.debug("EUID: "+ currEUID + ", weight:" + currWeight);
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
        } catch (SbmeMatchingException ex) {
            mLogger.error("SbmeMatchingException: " + ex.getMessage());
            throw new MatchingException("SbmeMatchingException: " + ex.getMessage());
        } catch (SbmeMatchEngineException ex) {
            mLogger.error("SbmeMatchEngineException: " + ex.getMessage());
            throw new MatchingException("SbmeMatchEngineException: " + ex.getMessage());
        } catch (ParseException ex) {
            mLogger.error("ParseException: " + ex.getMessage());
            throw new MatchingException("ParseException: " + ex.getMessage());
        } catch (EPathException ex) {
            mLogger.error("EPathException: Failed to match the configured fields in the SystemObject: " 
                    + ex.getMessage());
            throw new MatchingException(
                "Failed to match the configured fields in the SystemObject: " 
                    + ex.getMessage(), ex);
        } catch (QMException ex) {
            mLogger.error("QMException: Failed to retrieve candidate(s) from database. " 
                    + ex.getMessage());
            throw new MatchingException(
                "Failed to retrieve candidate(s) from database. " 
                    + ex.getMessage(), ex);
        } catch (ObjectException ex) {
            mLogger.error("ObjectException: Failed to convert SystemObject to candidate format for matching. " 
                    + ex.getMessage());
            throw new MatchingException(
                "Failed to convert SystemObject to candidate format for matching. " 
                    + ex.getMessage(), ex);
        } finally {
            // clean-up QMIterator
            if (tupleIter != null) {
                try {
                    tupleIter.close();
                } catch (QMException ex) {
                    mLogger.error("QMException: Failed to close QMIterator. " 
                        + ex.getMessage());
                    throw new MatchingException("Failed to close QMIterator. " 
                        + ex.getMessage(), ex);
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
                mLogger.error("No match engine configuration " 
                        + "class is configured for this match adapter, unable " 
                        + "to initialize Match Engine. " 
                        + SbmeMatcherAdapterConfig.class.getName() 
                        + " expected.");    
                throw new MatchingException("No match engine configuration " 
                        + "class is configured for this match adapter, unable " 
                        + "to initialize Match Engine. " 
                        + SbmeMatcherAdapterConfig.class.getName() 
                        + " expected.");                
            }
            if (!(config instanceof SbmeMatcherAdapterConfig)) {
                mLogger.error("The configured match engine " 
                        + "configuration class is not compatible with this " 
                        + "match adapter. " 
                        + SbmeMatcherAdapterConfig.class.getName() 
                        + " expected, configured: " 
                        + config.getClass().getName());
                throw new MatchingException("The configured match engine " 
                        + "configuration class is not compatible with this " 
                        + "match adapter. " 
                        + SbmeMatcherAdapterConfig.class.getName() 
                        + " expected, configured: " 
                        + config.getClass().getName());
            }
            SbmeMatcherAdapterConfig eViewConfig = 
                (SbmeMatcherAdapterConfig) config;
            
            // TODO: where to get this from? Shouldn't it be initialized for all?
            String domain = "US";
            String configFileName = eViewConfig.getConfigurationFileName();
            SbmeConfigFilesAccess cfgFilesAccess = eViewConfig.getConfigFileAccess();
            matchEngine = new SbmeMatchingEngine(cfgFilesAccess);
            matchEngine.initializeData(cfgFilesAccess);
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("Setting configuration file:" + configFileName + " for domain: " + domain);
            }
            matchEngine.upLoadConfigFile(domain);
        } catch (SbmeMatchEngineException ex) {
            mLogger.error("SbmeMatchEngineException: Failed initialize match adapter: " + ex.getMessage());
            throw new MatchingException(
                "Failed initialize match adapter, match engine reports an error: " 
                    + ex.getMessage(), ex);                        
        } catch (Exception ex) {
            mLogger.error("Exception: Failed to initialize match adapter: " + ex.getMessage());
            throw new MatchingException("Failed to initialize match adapter" 
                + ex.getMessage(), ex);
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
                mLogger.error("Exception: Failed to shutdown match engine: " + ex.getMessage());
                throw new MatchingException(
                    "Failed to shutdown match engine: " + ex.getMessage(), ex);            
            } finally {
                matchEngine = null;
            }
        }
    }
    
}
