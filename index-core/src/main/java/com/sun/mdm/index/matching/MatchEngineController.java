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

import com.sun.mdm.index.master.UserException;
import java.util.ArrayList;
import java.sql.Connection;

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.master.search.enterprise.EOSearchCriteria;
import com.sun.mdm.index.master.search.enterprise.EOSearchOptions;
import com.sun.mdm.index.matching.MatchingException;
import com.sun.mdm.index.matching.StandardizationException;
import com.sun.mdm.index.matching.MatchOptions;

/**
 * The interface of the match engine controller.
 * 
 * @author aegloff
 * $Revision: 1.1 $
 */
public interface MatchEngineController {

    /**
     * Standardize a SystemObject
     * @param objToStandardize the SystemObject to standardize
     * @return the standardized SystemObject
     * @throws StandardizationException if the standardization failed
     * @throws ObjectException if accessing/setting relevant SystemObject data failed
     * @throws InstantiationException if creating framework components failed
     */
    public SystemObject standardize(SystemObject objToStandardize) 
            throws StandardizationException, ObjectException, InstantiationException;
    
    /**
     * Find records in the database that match the passed in search criteria, 
     * e.g. a SystemObject
     * @param crit the criteria to match against
     * @param opts the options controlling the desired search result
     * @param matchOptions the options controlling the matching process
     * @throws MatchingException the matching process failed
     * @throws SystemObjectException Accessing/manipulating the system object failed
     * @throws ObjectException Accessing/manipulating the value object failed
     * @throws java.sql.SQLException querying the database failed
     * @throws EPathException using the configured epaths failed
     * @throws InstantiationException instantiating a framework component failed
     * @throws ClassNotFoundException the configured calss for a framework component
     * could not be found 
     * @throws IllegalAccessException the security setting do not allow for loading
     * the configured class for a framework component
     * @throws com.sun.mdm.index.master.UserException 
     * @return an ArrayList of ScoreElements containing the EUID and associated
     * match weights
     */
    public ArrayList findMatch(EOSearchCriteria crit, EOSearchOptions opts, MatchOptions matchOptions) 
            throws MatchingException, SystemObjectException, ObjectException, java.sql.SQLException, 
            EPathException, InstantiationException, ClassNotFoundException, IllegalAccessException,
            UserException;
    
    
    /**
     * Attempt to find a matching object by calculating the probabilities an object matches. 
     *
     * @param con  The database connection.
     * @param crit the criteria to find the match for, contains the 
     * (already standardized) SystemObject to find match for.
     * @param opts defines what enterprise objects to match against in the database
     * @param matchOptions the options to control the matching and the results to return
     * @return An ArrayList of com.sun.mdm.index.matching.ScoreElement, containing
     * information such as EUID and weight, indicating how well the the object matches.
     * The matchOptions can influence exactly what is
     * returned, i.e. whether it is in sorted order and whether weights
     * below a certain threshold are removed before returning.
     * By default the order is from the heighest weigth
     * (= best match) first, to the lowest last - and all results are returned.
     * @throws MatchingException matching failed
     * @throws SystemObjectException accessing the configured fields required for standardization 
     * or matching on the passed in SystemObject failed
     * @throws ObjectException accessing the configured fields required for standardization 
     * or matching on the passed in objects failed
     * @throws java.sql.SQLException retrieving data from the database for matching failed
     * @throws EPathException a configured ePath is invalid
     * @throws InstantiationException a configured implementation class for a component
     * could not be instantiated
     * @throws ClassNotFoundException a configured implementation class for a component
     * could not be found
     * @throws IllegalAccessException a configured implementation class for a component
     * refused access
     * @throws com.sun.mdm.index.master.UserException 
     */    
    public ArrayList findMatch(Connection con, EOSearchCriteria crit, 
                                         EOSearchOptions opts, MatchOptions matchOptions)
            throws MatchingException, SystemObjectException, ObjectException, java.sql.SQLException, EPathException, 
            InstantiationException, ClassNotFoundException, IllegalAccessException,
            UserException;     
}
