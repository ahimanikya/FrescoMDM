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
package com.sun.mdm.index.decision;

import com.sun.mdm.index.ejb.master.MasterControllerCore;
import java.util.ArrayList;
import java.sql.Connection;
import com.sun.mdm.index.objects.SystemObject;


/**
 * Interface for logic that determines which records are potential duplicates
 * and which record may be an assumed match.
 * @author Dan Cidon
 */
public abstract class DecisionMaker {
    private MasterControllerCore mMasterController;


    /** Get the handle of the MC
     * @return master controller handle
     */
    public final MasterControllerCore getMasterController() {
        return mMasterController;
    }


    /**
     * Allows a handle to the MC to be passed in
     *
     * @param mc master controller
     */
    public final void setMasterController(MasterControllerCore mc) {
        this.mMasterController = mc;
    }


    /** Parameters of the decision maker represented in the configuration XML
     * file are set using this method.
     *
     * @param parameterName parameter
     * @param value parameter value
     * @exception DecisionMakerException An error occurred
     */
    public abstract void setParameter(String parameterName, Object value)
        throws DecisionMakerException;

    /** Given a list of ScoreElements, populate a
     * DecisionMakerResult struct to indicate which records are potential
     * duplicates and which record may be an assumed match.
     *
     * @param con database connection
     * @param list input list of ScoreElements
     * @param so system object being evaluated
     * @exception DecisionMakerException an error occurred
     * @return array of decision maker results
     */
    public abstract DecisionMakerResult process(Connection con, ArrayList list, SystemObject so)
        throws DecisionMakerException;
    
    /** Return the potential duplicate threshold.  This is a minimum comparison
     * score cutoff such that the MasterController will not send any ScoreElements
     * to the decision maker process function with a score lower than this.
     *
     * @exception DecisionMakerException an error occurred
     * @return duplicate threshold
     */
    public abstract float getDuplicateThreshold() throws DecisionMakerException;

    /** Return the assumed match threshold.  This is a minimum comparison
     * score cutoff such that the MasterController will consider any records
     * as Assumed Matches.
     *
     * @exception DecisionMakerException an error occured
     * @return assumed match threshold
     */
    public abstract float getMatchThreshold() throws DecisionMakerException;
}
