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
import com.sun.mdm.index.decision.DecisionMakerStruct;


/**
 * Return class for DecisionMaker.process()
 * @author Dan Cidon
 */
public class DecisionMakerResult {

    private final DecisionMakerStruct mAssumedMatch;
    private final DecisionMakerStruct[] mPotentialDuplicates;
    private boolean mAllowAssumedMatch;


    /** Creates a new instance of DecisionMakerResult
     *
     * @param assumedMatch record that is assumed match or null
     * @param potentialDuplicates array of potential duplicates or null
     */
    public DecisionMakerResult(DecisionMakerStruct assumedMatch, DecisionMakerStruct[] potentialDuplicates) {
        mAssumedMatch = assumedMatch;
        mPotentialDuplicates = potentialDuplicates;
	mAllowAssumedMatch = true;
    }


    /** Return assumed match record if exists, otherwise null
     * @return assumed match records
     */
    public DecisionMakerStruct getAssumedMatch() {
        return (mAllowAssumedMatch ? mAssumedMatch : null);
    }


    /** Return potential duplicates if exists, otherwise null
     * @return array of duplicates or null
     */
    public DecisionMakerStruct[] getPotentialDuplicates() {
	if (mAllowAssumedMatch || mAssumedMatch == null) {
	    return mPotentialDuplicates;
	}
	// Add the rejected assumed match to the pot dup list
	int nDups = 0;
	if (mPotentialDuplicates != null) {
	    nDups = mPotentialDuplicates.length;
	}
	DecisionMakerStruct[] dms = new DecisionMakerStruct[nDups+1];
	dms[0] = mAssumedMatch;
	for (int i=0; i<nDups; i++) {
	    dms[i+1] = mPotentialDuplicates[i];
	}
	return dms;
    }

    /** Flag the assumed match, if any, as being rejected
     */
    public void rejectAssumedMatch() {
	mAllowAssumedMatch = false;
    }

    /** Flag the assumed match, if any, as being accepted
     */
    public void allowAssumedMatch() {
	mAllowAssumedMatch = true;
    }
}
