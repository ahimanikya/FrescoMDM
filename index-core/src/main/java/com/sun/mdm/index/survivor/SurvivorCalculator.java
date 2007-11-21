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
package com.sun.mdm.index.survivor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.factory.SimpleFactory;

import com.sun.mdm.index.objects.epath.EPathAPI;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.objects.epath.EPathParser;
import com.sun.mdm.index.matching.MatchEngineController;
import com.sun.mdm.index.util.Localizer;
import com.sun.mdm.index.matching.Standardizer;

/** Survivor calculator class
 * 
 * @version $Revision: 1.1 $
 */
public class SurvivorCalculator implements java.io.Serializable {
    /** helper object reference
     */
    private AbstractSurvivorHelper mHelper;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    private MatchEngineController mMatch;
    private Standardizer mStandardizer = null;
    
    /** Creates new SurvivorCalculator using default helper object
     * @param match MatchEngineController
     * @throws HelperCreationException error during helper creation
     */
    public SurvivorCalculator(MatchEngineController match) throws HelperCreationException {
        mHelper = HelperFactory.createSurvivorHelper();
        mMatch = match;
    }
    
    public SurvivorCalculator(Standardizer standardizer) throws HelperCreationException {
    	mHelper = HelperFactory.createSurvivorHelper();
    	mStandardizer = standardizer;
        
    }

    /** Creates a new SurvivorCalculator, using the passed in helper object
     * @param match MatchEngineController
     * @param h helper object reference
     */
    public SurvivorCalculator(MatchEngineController match, AbstractSurvivorHelper h) {
        mHelper = h;
        mMatch = match;
    }

    /** determines the survivor for each field and updates the SBR record
     * @param omega EnterpriseObject
     * @throws SystemObjectException error accessing object node
     * @throws ObjectException object exception
     * @throws SurvivorCalculationException calculation failed
     */
    public void determineSurvivor(EnterpriseObject omega)
        throws SurvivorCalculationException, SystemObjectException, 
            ObjectException {
  
       
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Input EnterpriseObject: " + omega);
        }
        
        // do survivor calculation IFF there are more than 1 active systems
        if (countActiveSystems(omega) > 0) {
            
            SBR sbr = omega.getSBR();
            ObjectNode ogSbrObj = sbr.getObject();
            
            // Remove all child objects that were added to the SBR.  If the user 
            // tries to add a Child Object to the SBR from a java client, it will cause
            // problems.  When the SBR tries to update itself with the update()
            // method, this new Child SBR Object will not be found in the incoming
            // child list, so it is marked for Removal.  This information is stored
            // in the Transaction Log delta.  Later, when viewing this transaction,
            // the "Before" state will be created using this delta; it will do the 
            // inverse operation (i.e. Add) and add the Child SBR Object to the 
            // "Before" image, which is incorrect.  By phyiscally removing the
            // Child SBR Object, it will not be marked for Removal, thereby
            // ensuring that it does not end up in the "Before" image.  The 
            // correct way to add a Child Object to the SBR is with an SBROverWrite
            // object.
            // This is also true for updates done in the same way.
            
            ArrayList sbrChildObjects = ogSbrObj.pGetChildren();
            if (sbrChildObjects != null) {
                Iterator sbrChildIter = sbrChildObjects.iterator();
                while (sbrChildIter.hasNext()) {
                    ObjectNode childObj = (ObjectNode) sbrChildIter.next();
                    if (childObj.isAdded() == true || childObj.isUpdated() == true) {
                        ogSbrObj.removeChild(childObj);
                    }
                }
            }
            
            String sbrObjType = ogSbrObj.pGetType();
            
            // recreate a new entity object to update the SBR
            ObjectNode sbrObj = SimpleFactory.create(sbrObjType);
            
            if (sbrObj != null) {
                // use helper to get match fields on SBR object
                java.util.Collection candidateIds = mHelper.getCandidateFieldNames();
                
                java.util.Iterator iter = candidateIds.iterator();
                
                while (iter.hasNext()) {
                    // current candidate field
                    String candidateId = (String) iter.next();
                    
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Processing candidate: " + candidateId);
                    }                
                    
                    // get fields used for calculation
                    SystemFieldListMap sysFields = mHelper.getSystemFields(omega,
                        candidateId);
                    
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("System fields returned for candidate " + candidateId 
                            + ": " + sysFields);
                    }
                    
                    // call survival strategy for each set of system fields using candidate id
                    SystemField value = mHelper.executeStrategy(candidateId,
                        sysFields);
                    
                    if (value != null) {
                        if (value.getValue() != null) {
                            // set the field value on SBR
                            if (mLogger.isLoggable(Level.FINE)) {
                                mLogger.fine("Candidate values returned from strategy: " + value);
                            }
                            mHelper.setCandidateFieldValue(sbrObj, value);
                        }
                    }
                }
            } else {
                throw new SurvivorCalculationException(mLocalizer.t("SUR518: SBR " +
                                                    "entity object is null."));
            }
            
            // now apply the override values
            ArrayList overs = sbr.getOverWrites();
            if (overs != null) {
                for (int i = 0; i < overs.size(); i++) {
                    SBROverWrite over = (SBROverWrite) overs.get(i);
                    if (!over.isRemoved()) {
                        String path = over.getEPath();
                        Object data = over.getData();

                        try {
                            EPathAPI.addObjectValue(EPathParser.parse(path), sbrObj, data);
                        } catch (EPathException eex) {
                            throw new SurvivorCalculationException(mLocalizer.t("SUR519: Error " +
                                                "applying overwrite value: EPath error: {0}", eex));
                        }
                    }
                }
            }
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Newly created SBR Object: " + sbrObj);
            }
            // clear the object ID of all child objects
            ArrayList childList = sbrObj.pGetChildren();
            if (childList != null) {
                Iterator childIter = childList.iterator();
                while(childIter.hasNext()) {
                    ObjectNode childObj = (ObjectNode) childIter.next();
                    String tag = childObj.pGetTag();
                    ObjectField field = childObj.getField(childObj.pGetTag() + "Id");
                    field.setValue(null);
                }
            }
            //Standardize SBR object
            SBR stndSBR = new SBR();
            stndSBR.setChildType(sbr.getChildType());
            stndSBR.setObject(sbrObj);
            try {
            	if (mMatch != null) {
                stndSBR = (SBR) mMatch.standardize(stndSBR);
            	} else if (mStandardizer != null) {
            		mStandardizer.standardize(stndSBR);
            	}
            } catch (Exception e) {
                throw new SurvivorCalculationException(mLocalizer.t("SUR520: Standardizing " +
                                                "SBR failed: {0}", e));
            }
            // ckuo: added to use update
            ogSbrObj.update(stndSBR.getObject(), true, true);
        }  else {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("EnterpriseObject has less than 1 active system, so survivor calculation is skipped.");
            }
        }
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Returning SBR: " + omega.getSBR());
        }
        
    }

    /** sets the helper object
     * @param h helper object reference
     */
    public void setHelper(AbstractSurvivorHelper h) {
        mHelper = h;
    }

    /**
     * @param eo
     * @return  */
    private int countActiveSystems(EnterpriseObject eo)
    throws ObjectException {
        
        Collection systems = eo.getSystemObjects();
        
        if (systems == null) {
            // so count is 0
            return 0;
        }
        
        Iterator iter = systems.iterator();
        
        int count = 0;
        
        while (iter.hasNext()) {
            SystemObject so = (SystemObject) iter.next();
            
            if (so.getStatus().equals(SystemObject.STATUS_ACTIVE)
            && !so.isRemoved()) {
                count++;
            }
        }
        
        return count;
    }

    
}
