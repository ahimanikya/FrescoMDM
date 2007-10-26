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
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.query.QueryResults;

/**
 * The interface a match engine adapter has to implement to enable communication
 * between the framework and the specific match engine.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public interface MatcherAPI {

    /**
     * Find all the match weights comparing the 
     * bases systemobject to the 'block' records retrieved from the DB
     * @param base the system object to match against
     * @param block the block of DB records to compare with
     * @param matchOptions controls any matcher options such as minimum score
     * @throws MatchingException if the Matching failed
     * @return  An ArrayList containing the ScoreElements with the weights 
     * associated with an EUID.
     */    
    ArrayList findWeights(SystemObject base, QueryResults block, MatchOptions matchOptions) 
        throws MatchingException;
    
    /**
     * Initialize the match engine and the adapter
     * called once upon startup for each adapter instance
     * @param config the match engine configuration configured
     * @throws MatchingException if the initialization failed
     */    
    void initialize(MatchEngineConfiguration config) 
        throws MatchingException;
    
    /**
     * Shutdown and release any resources associated with the match engine 
     * and the adapter
     * Called once per adapter instance before the adapter is discarded by the
     * framework
     * @throws MatchingException if the shutdown failed
     */        
    void shutdown() 
        throws MatchingException;
    
}
