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


/** @todo should be added as inner class to WeightedSurvivorStrategy
 *

 * @version $Revision: 1.1 $
 */
public class SurvivorQuality implements java.io.Serializable {
    /** quality name */    
    String mQuality;
    /** prefered value */    
    String mPreference;
    /** source field name */    
    String mSourceField;
    /** utility score */    
    double mUtility;

    /** Creates new UtilityParameter */
    public SurvivorQuality() {
    }

    /**constructs a SurvivorQuality object
     * @param quality quality value
     * @param preference preference value
     * @param source source field value
     * @param utility utility value
     * @throws NumberFormatException utility value is not a number
     */
    public SurvivorQuality(String quality, String preference, String source,
        String utility) throws NumberFormatException {
        mQuality = quality;
        mPreference = preference;
        mSourceField = source;
        mUtility = Double.parseDouble(utility);
    }

    /** getter for quality
     * @return getter for quality
     */
    public String getQuality() {
        return mQuality;
    }

    /** getter for preference
     * @return preference value
     */
    public String getPreference() {
        return mPreference;
    }

    /** getter for utility value
     * @return utility value
     */
    public double getUtility() {
        return mUtility;
    }

    /** getter for source field
     * @return source field name
     */
    public String getSource() {
        return mSourceField;
    }
}
