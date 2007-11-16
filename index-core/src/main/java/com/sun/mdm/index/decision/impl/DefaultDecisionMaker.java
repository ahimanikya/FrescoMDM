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
package com.sun.mdm.index.decision.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.sql.Connection;
import com.sun.mdm.index.decision.DecisionMaker;
import com.sun.mdm.index.decision.DecisionMakerException;
import com.sun.mdm.index.decision.DecisionMakerResult;
import com.sun.mdm.index.decision.DecisionMakerStruct;
import com.sun.mdm.index.matching.ScoreElement;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.ejb.master.MasterControllerCore;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Sample decision maker which chooses the highest weighted record above a
 * match threshold to be the assumed match record.
 * @author Dan Cidon
 */
public class DefaultDecisionMaker extends DecisionMaker {

    private boolean mOneExactMatch = false;
    private boolean mSameSystemMatch = false;
    private float mDuplicateThreshold;
    private float mMatchThreshold;
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of DefaultDecisionMaker. Should only be called by
     * DecisionMakerConfiguration.
     */
    public DefaultDecisionMaker() {
    }


    /** Get match threshold
     * @return match threshold
     */
    public float getMatchThreshold() {
        return mMatchThreshold;
    }


    /** Return true if one exact match enabled
     * @return true or false
     */
    public boolean isOneExactMatchEnabled() {
        return mOneExactMatch;
    }


    /** Return true if same system match enabled
     * @return true or false
     */
    public boolean isSameSystemMatchEnabled() {
        return mSameSystemMatch;
    }


    /** See DecisionMaker
     * @param parameterName See DecisionMaker
     * @param value See DecisionMaker
     * @exception DecisionMakerException See DecisionMaker
     * @see com.sun.mdm.index.decision.DecisionMaker#setParameter(String, Object)
     */
    public void setParameter(String parameterName, Object value)
        throws DecisionMakerException {
        mLogger.info(mLocalizer.x("DEC001: DefaultDecisionMaker setting " +
                                  "parameter {0} to {1}", parameterName, value));
        if (parameterName.equals("OneExactMatch")) {
            mOneExactMatch = ((Boolean) value).booleanValue();
        } else if (parameterName.equals("SameSystemMatch")) {
            mSameSystemMatch = ((Boolean) value).booleanValue();
        } else if (parameterName.equals("MatchThreshold")) {
            mMatchThreshold = ((Float) value).floatValue();
        } else if (parameterName.equals("DuplicateThreshold")) {
            mDuplicateThreshold = ((Float) value).floatValue();
        } else {
            throw new DecisionMakerException(mLocalizer.t("DEC501: Unknown parameter: {0}", parameterName));
        }
    }


    /** Choose the highest weighted record above a match threshold to be the
     * assumed match record. If more than one record meets this criteria and
     * OneExactMatch is enabled, then do not select an assumed match.
     *
     * @param list See DecisionMaker
     * @param so See DecisionMaker
     * @exception DecisionMakerException See DecisionMaker
     * @return See DecisionMaker
     */
    public DecisionMakerResult process(Connection con, ArrayList list, SystemObject so)
        throws DecisionMakerException {
        try {
            Object scoreElementArray[] = list.toArray();
            ArrayList dmArrayList = new ArrayList();
            for (int i = 0; i < scoreElementArray.length; i++) {
                ScoreElement se = (ScoreElement) scoreElementArray[i];
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Score element: " + se);
                }
                float probability = (float) se.getWeight();
                String euid = se.getEUID();
                //TODO: set comment???
                String comment = "";
                dmArrayList.add(new DecisionMakerStruct(euid, probability, comment));
            }
            Object[] objArray = dmArrayList.toArray();
            DecisionMakerStruct[] potentialDuplicates;
            DecisionMakerStruct assumedMatch = null;
            Arrays.sort(objArray);

            // if the first record is above match threshold
            if (objArray.length > 0 && ((DecisionMakerStruct) objArray[0]).weight >= mMatchThreshold) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("process(): first element is above or equal to match threshold: " + 
                                  ((DecisionMakerStruct) objArray[0]).weight + " >= " + mMatchThreshold);
                }
                // if in one exact match mode and there are more than 2 in list above dup threshold
                if (mOneExactMatch && objArray.length > 1) {
                    // assume match if only 1 records is above match threshold (i.e. 2nd on list is less)
                    if (((DecisionMakerStruct) objArray[1]).weight < mMatchThreshold) {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("process(): one exact match enabled and only one record above match threshold.\nAssumed match found");
                        }
                        assumedMatch = (DecisionMakerStruct) objArray[0];
                    } else {
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("process(): one exact match rule violated");
                        }
                        ((DecisionMakerStruct) objArray[0]).comment = "One Exact Match Rule Violated";
                    }
                } else {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("process(): one exact match disabled, selecting first record as assumed match");
                    }
                    assumedMatch = (DecisionMakerStruct) objArray[0];
                }
            }

            //Check if this is a same system match
            if (assumedMatch != null && mSameSystemMatch) {
                MasterControllerCore mc = getMasterController();
                SystemObjectPK[] systemObjectPKs = mc.lookupSystemObjectPKs(con, assumedMatch.euid);
                String systemCode = so.getSystemCode();
                for (int i = 0; i < systemObjectPKs.length; i++) {
                    if (systemObjectPKs[i].systemCode.equals(systemCode)) {
                        assumedMatch = null;
                        if (mLogger.isLoggable(Level.FINE)) {
                            mLogger.fine("process(): same system match rule violated");
                        }
                        ((DecisionMakerStruct) objArray[0]).comment = "Same System Match Rule Violated";
                        break;
                    }
                }
            }

            int startIndex = 0;
            if (assumedMatch != null) {
                startIndex = 1;
            }
            potentialDuplicates = new DecisionMakerStruct[objArray.length - startIndex];
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("process(): potential duplicate count " + potentialDuplicates.length);
            }
            for (int i = 0; i < potentialDuplicates.length; i++) {
                potentialDuplicates[i] = (DecisionMakerStruct) objArray[startIndex + i];
            }
            return new DecisionMakerResult(assumedMatch, potentialDuplicates);
        } catch (Exception e) {
            throw new DecisionMakerException(mLocalizer.t("DEC502: DefaultDecisionMaker " + 
                                    "encountered an error while processing records: {0}", e));
        }
    }

    /** @see com.sun.mdm.index.decision.DecisionMaker#getDuplicateThrehsold()
     */
    public float getDuplicateThreshold() throws DecisionMakerException {
        return mDuplicateThreshold;
    }
    
}
