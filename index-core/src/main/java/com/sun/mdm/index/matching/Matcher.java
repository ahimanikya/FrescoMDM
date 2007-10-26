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
import com.sun.mdm.index.matching.MatchOptions;
import com.sun.mdm.index.matching.MatchingException;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.query.QueryResults;

/**
 * Loads and forwards requests to the
 * adapter interface implementation configured.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public interface Matcher{
    
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
            throws MatchingException;
    
}
