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
package com.sun.mdm.index.matching;

import java.util.ArrayList;

import com.sun.mdm.index.query.QueryResults;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Impl class for Matcher. Loads and forwards requests to the
 * adapter interface implementation configured.
 * 
 * @author aegloff
 * $Revision: 1.1 $
 */

public class MatcherImpl implements Matcher {
    
    private MatcherAPI matcherAPIImpl;
    private MatchEngineConfiguration matchEngineConfig;
    private Logger mLogger = LogUtil.getLogger(this);
    
    /**
     * No argument constructor.
     */
    public MatcherImpl() throws Exception {   	
       initialize();       
    }
    

    /**
     * Load and initialize the configured matcher API implementation
     * @throws MatchingException Initializing the configured API implementation failed
     * @throws InstantiationException instantiating the configured API implementation
     * class failed
     * @throws ClassNotFoundException The class for the configured API implementation
     * class could not be found 
     * @throws IllegalAccessException The current security settings do not allow
     * loading and instantiating the class configured for API implementation
     */
    private void initialize() 
            throws MatchingException, InstantiationException, ClassNotFoundException, IllegalAccessException {
        mLogger.debug("initialize()");
        try {
            // use MatcherAPIHelper as factory        
            matcherAPIImpl = new MatcherAPIHelper().getMatcherAPIImpl();
            if (matcherAPIImpl == null) {
                throw new MatchingException("No MatcherAPI implementation configured.");
            }
            matchEngineConfig = new MatcherAPIHelper().getMatchEngineConfigImpl();
            if (matchEngineConfig == null) {
                mLogger.debug("No MatchEngineConfig implementation configured.");
            }
            
            matcherAPIImpl.initialize(matchEngineConfig);
        } catch (MatchingException ex) {
            mLogger.error("Initializing the match engine failed.", ex);
            throw ex;            
        } catch (InstantiationException ex) {
            mLogger.error("Instantiating the user API implementation class failed.", ex);
            throw ex;            
        } catch (ClassNotFoundException ex) {
            mLogger.error("Loading the user API implementation class failed.", ex);
            throw ex;            
        } catch (IllegalAccessException ex) {
            mLogger.error("Accessing the user API implementation class failed.", ex);
            throw ex;            
        } catch (RuntimeException ex) {
            mLogger.error("Initialize failed.", ex);
            throw ex;            
        }
    }    
    
    
    public void finalizer() {       
        try {    	
            super.finalize();
        	shutdown();
        	
        } catch (Throwable ex) {
            mLogger.error("Shutting down the match engine failed.", ex);              
        }
    }
    
   public void shutdown() throws Exception {    	 
    	if (matcherAPIImpl != null) {
            matcherAPIImpl.shutdown();
    		matcherAPIImpl = null;
        }   
    }

    /**
     * Return a reference to the loaded and instantiated matcher API implementation
     * instance
     */
    MatcherAPI getMatcherAPIImpl() {
        return matcherAPIImpl;
    }
    
    
    /**
     * Evaluate how closely the passed in SystemObject matches the database
     * records in the passed in block by calculating the weights
     * @param base the SystemObject to match
     * @param block the database block of records to match against
     * @param matchOptions the options controlling the matching process
     * @throws MatchingException the matching process failed
     * @return an ArrayList of ScoreElements containing the EUID and associated
     * match weights
     */    
    public ArrayList findWeights(SystemObject base, QueryResults block, MatchOptions matchOptions) 
            throws MatchingException {
        ArrayList weights = getMatcherAPIImpl().findWeights(base, block, matchOptions);
        return weights;
    }     
}
