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
package com.sun.mdm.index.update;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;
import net.java.hulp.i18n.LocalizationSupport;

import com.sun.mdm.index.query.QueryHelper;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.ObjectKey;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SBROverWrite;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.objects.ObjectField;
import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.ops.TransactionMgrFactory;
import com.sun.mdm.index.ops.RecreateResult;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.ops.exception.DataModifiedException;
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.matching.MatchEngineController;
import com.sun.mdm.index.matching.MatchEngineControllerFactory;
import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.survivor.SurvivorCalculationException;
import com.sun.mdm.index.survivor.SurvivorCalculator;
import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.UpdateManagerConfig;
import com.sun.mdm.index.query.QMException;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.util.Localizer;

public class UpdateManagerImpl implements UpdateManager {
    static final String CHANGES_NEW = "new";
    static final String CHANGES_REMOVED = "removed";
    private UpdateHelper mHelper;
    private SurvivorCalculator mCalculator;
    private TransactionMgr mTransaction;
    private MatchEngineController mMatch;
    private QueryHelper mQueryHelper;  // handle to query helper
    
    //private HashMap mPolicies;
    private UpdatePolicy mEMergePolicy;
    private UpdatePolicy mEUnmergePolicy;
    private UpdatePolicy mECreatePolicy;
    private UpdatePolicy mEUpdatePolicy;
    private UpdatePolicy mSMergePolicy;
    private UpdatePolicy mSUnmergePolicy;
    private UpdatePolicy mUndoAssumePolicy;
    
    private boolean mSkipUpdateIfNoChange;
    
    private transient Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient Localizer mLocalizer = Localizer.get();
       
    /**
     * Constructor to create update manager.
     * @throws Exception if error occurs
     */
    public UpdateManagerImpl() throws Exception {
        initialize();
    }
     
    private void initialize() throws Exception {
        mHelper = new UpdateHelper();
        mTransaction = TransactionMgrFactory.getInstance();
        mMatch = MatchEngineControllerFactory.getInstance();
        mCalculator = new SurvivorCalculator(mMatch); 
        UpdateManagerConfig config = (UpdateManagerConfig) ConfigurationService
            .getInstance().getConfiguration("UpdateManager");
        
        String s = config.getEnterpriseCreatePolicy();
        mECreatePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD001: Creating EnterpriseCreatePolicy: {0}", s));
        
        s = config.getEnterpriseMergePolicy();
        mEMergePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD002: EnterpriseMergePolicy: {0}", s));

        s = config.getEnterpriseUnmergePolicy();
        mEUnmergePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD003: EnterpriseUnmergePolicy: {0}", s));
            
        s = config.getEnterpriseUpdatePolicy();
        mEUpdatePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD004: EnterpriseUpdatePolicy: {0}", s));
            
        s = config.getSystemMergePolicy();
        mSMergePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD005: SystemMergePolicy: {0}", s));
            
        s = config.getSystemUnmergePolicy();
        mSUnmergePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD006: SystemUnmergePolicy: {0}", s));
            
        s = config.getUndoAssumePolicy();
        mUndoAssumePolicy = createPolicy(s);
        mLogger.info(mLocalizer.x("UPD007: UndoAssumePolicy: {0}", s));    
           
        mSkipUpdateIfNoChange = config.getSkipUpdateIfNoChange();
        mQueryHelper = new QueryHelper();
    }
    
    private UpdatePolicy createPolicy(String s)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (s != null) {
            Object o = Class.forName(s).newInstance();
            return (UpdatePolicy) o;
        }
        return null;
    }
    
    /** creates an EnterpriseObject given a SystemObject.
     * Populates the SBR with values from the initial object and persists it in the
     * database
     * @param con connection  database connection
     * @param so <code>SystemObject</code> used to create <code>EnterpriseObject</code>
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID. 
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * <code>SystemObject</code> access exception
     * @throws UpdateException error updating
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws UserException general user exception     
     */
    public com.sun.mdm.index.update.UpdateResult createEnterpriseObject(
                    Connection con, com.sun.mdm.index.objects.SystemObject so)
            throws SystemObjectException, UpdateException,
                   ObjectException, UserException {
        try {
            Date date = new Date();
            if (so.getCreateDateTime() == null) {
                so.setCreateDateTime(date);
            }
            if (so.getUpdateDateTime() == null) {
                so.setUpdateDateTime(date);
            }
            so.setCreateFunction(SystemObject.ACTION_ADD);
            so.setUpdateFunction(SystemObject.ACTION_ADD);
            so.setCreateUser(so.getUpdateUser());
            EnterpriseObject newEO = mHelper.createEO(so);        
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Created EnterpriseObject in memory :" + newEO);
            }
            
            if (mECreatePolicy != null) {
                newEO = mECreatePolicy.applyUpdatePolicy(null,  newEO);
            }           
            TMResult tresult = mTransaction.addEnterpriseObject(con, newEO);

            // add newly allocated EUID
            if (tresult != null) {
                newEO.setEUID(tresult.getEUID());
            } else {
                throw new UpdateException(mLocalizer.t("UPD501: Enterprise Object " +
                                                "creation failed."));
            }
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("EnterpriseObject before returning " 
                             + "createEnterpriseObject(so): " + newEO);
            }
            
            return new UpdateResult(tresult, newEO);
        } catch (OPSException ex) {
            throw new UpdateException(mLocalizer.t("UPD502: Could not create an " +
                                            "Enterprise Object: {0}", ex));
        }
    }
    
    /** creates an EnterpriseObject given a SystemObject.
     * Populates the SBR with values from the initial object and persists it in the
     * database     
     * @param con connection
     * @param so array of <code>SystemObject</code> used to create the <code>EnterpriseObject</code>
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws UpdateException error updating 
     * @throws SurvivorCalculationException error calculating SBR
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws UserException general user exception
      
     */
    public com.sun.mdm.index.update.UpdateResult createEnterpriseObject(
                    Connection con, com.sun.mdm.index.objects.SystemObject[] so)
            throws SystemObjectException, UpdateException,
                   SurvivorCalculationException,
                   ObjectException, UserException {
        
        if (so.length == 0) {
            throw new UpdateException(mLocalizer.t("UPD503: At least one " +
                                            "system object required."));
        } else if (so.length == 1) {
            return createEnterpriseObject(con, so[0]);
        } else {
            
            EnterpriseObject newEO = mHelper.createEO(so[0]);
            Date date = new Date();
            
            for (int i = 1; i < so.length; i++) {
                if (so[i].getCreateDateTime() == null) {
                   so[i].setCreateDateTime(date);
                }
                if (so[i].getUpdateDateTime() == null) {
                   so[i].setUpdateDateTime(date);
                }
                so[i].setCreateFunction(SystemObject.ACTION_ADD);
                so[i].setUpdateFunction(SystemObject.ACTION_ADD);
                so[i].setCreateUser(so[i].getUpdateUser());
                newEO.addSystemObject(so[i]); 
            }
            
            // calculate SBR
            mCalculator.determineSurvivor(newEO);
            
            // Standardize SBR
            try {
                SBR sbr = newEO.getSBR();
                SBR newSBR = (SBR) mMatch.standardize(sbr);
                sbr.updateIfNotNull(newSBR, true, true);
            } catch (Exception e) {
                throw new UpdateException(mLocalizer.t("UPD504: SBR " +
                                            "could not be standardized: {0}", e));
            }
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Created EnterpriseObject in memory :" + newEO);
            }
            if (mECreatePolicy != null) {
                newEO = mECreatePolicy.applyUpdatePolicy(null,  newEO);
            }
            
            com.sun.mdm.index.persistence.TMResult tresult = null;
            try {
                // call persist to create DB
                tresult = mTransaction.addEnterpriseObject(con, newEO);
            } catch (OPSException rex) {
                throw new UpdateException(mLocalizer.t("UPD505: Could not " +
                                            "add an EnterpriseObject to the " +
                                            "database: {0}", rex));
            }
            
            // add newly allocated EUID
            if (tresult != null) {
                newEO.setEUID(tresult.getEUID());
            } else {
                throw new UpdateException(mLocalizer.t("UPD506: Could not " +
                                            "create an EnterpriseObject in the " +
                                            "database."));
            }
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("EnterpriseObject before returning " 
                             + "createEnterpriseObject(so[]): " + newEO);
            }
            
            return new UpdateResult(tresult, newEO);
        }
    }
    
    /** Updates a particular EnterpriseObject with values from the specified
     * SystemObject
     * @param con connection
     * @param so up-to-date <code>SystemObject</code> image    
     * @param eo <code>EnterpriseObject</code> that the SystemObject belongs to
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.     
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
                    Connection con, SystemObject so,
                    EnterpriseObject eo, int flags, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
                    
        return updateEnterprise(con, so, eo, null, flags, user);                    
    }
        
    /** Updates a particular EnterpriseObject with values from the specified
     * SystemObject     
     * @param con connection
     * @param so up-to-date <code>SystemObject</code> image
     * @param eo <code>EnterpriseObject</code> that the SystemObject belongs to    
     * @param revisionNumber  The revision number of the SBR of the associated SO.
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
                    Connection con, SystemObject so, 
                    EnterpriseObject eo, String revisionNumber, 
                    int flags, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
        
        EnterpriseObject ogEO = null;
        if (mEUpdatePolicy != null) {
            ogEO = (EnterpriseObject) eo.copy();
	}
        Date date = new Date();
        
        String sysCode = so.getSystemCode();
        String lid = so.getLID();
        String euid = eo.getEUID();
        
        // assert inputs are not null
        if ((eo != null) && (so != null)) {
            
            try {
                
                boolean recordChanged = false;
                boolean copyflag = true;
                boolean replaceSO = (0 != (flags & Constants.FLAG_UM_REPLACE_SO)) ? true : false;
                
                // Update the existing SO, or add it
                if (mHelper.updateSO(so, eo, copyflag, replaceSO) == null) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Specified SystemObject not found for " 
                        + "EnterpriseObject, adding it to EnterpriseObject.");
                    }
                    if (so.getUpdateDateTime() == null) {
                        so.setUpdateDateTime(date);
                    }
                    if (so.getCreateDateTime() == null) {
                        so.setCreateDateTime(date);
                    }
                    so.setUpdateFunction(SystemObject.ACTION_ADD);
                    so.setCreateFunction(SystemObject.ACTION_ADD);
                    so.setCreateUser(user);
                    eo.addSystemObject(so);
                    recordChanged = true;
                } else {
                    // Set the timestamp (UpdateHelper only modifies child object,
                    // so this has to be done separately)
                    Date updateTimestamp = so.getUpdateDateTime();
                    Date createTimestamp = so.getCreateDateTime();
                    if (updateTimestamp == null) {
                        updateTimestamp = date;
                    }
                    so = eo.getSystemObject(sysCode, lid);
                    so.setUpdateFunction(SystemObject.ACTION_UPDATE);
                    so.setUpdateDateTime(updateTimestamp);
                    // Only modify timestamp if user has provided a non null timestamp
                    if (createTimestamp != null) {
                        so.setCreateDateTime(createTimestamp);
                    }
                }               
                so.setUpdateUser(user);
                
                //check how many active SO's are left, if 0 then deactivate EO
                if (countActiveSystems(eo) < 1) {
                    // no active systems
                    // deactivate source enterprise object
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("No active SystemObjects left, " 
                                     + "deactivating EnterpriseObject.");
                    }
                    mHelper.deactivateEO(eo, date);
                } else {
                    // calculate SBR
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Recalculating SBR for EnterpriseObject.");
                    }
                    mCalculator.determineSurvivor(eo);
                }
                
                if (mEUpdatePolicy != null) {
                    eo = mEUpdatePolicy.applyUpdatePolicy(ogEO, eo);
                }
                
                // Check if SBR changed by SC or update policy
                SBR sbr = eo.getSBR(); 
                if (isObjectChanged(sbr.getObject())) {
                    sbr.setUpdateDateTime(date);
                    sbr.setUpdateFunction(SystemObject.ACTION_UPDATE);
                    sbr.setUpdateUser(user);
                    recordChanged = true;
                }

                ArrayList overwrites = sbr.getOverWrites();
                if (overwrites != null) {
                    Iterator iter = overwrites.iterator();
                    while (iter.hasNext()) {
                        SBROverWrite ow = (SBROverWrite) iter.next();
                        if (isObjectChanged(ow)) {
                            sbr.setUpdateDateTime(date);
                            sbr.setUpdateFunction(SystemObject.ACTION_UPDATE);
                            sbr.setUpdateUser(user);
                            recordChanged = true;
                            break;
                        }
                    }
                }

                // Check if SO's changed
                Collection sysobjs = eo.getSystemObjects();
                if (!recordChanged && sysobjs != null) {
                    Iterator i = sysobjs.iterator();
                    while (i.hasNext()) {
                        SystemObject sysObj = (SystemObject) i.next();
                        if (isObjectChanged(sysObj.getObject())) {
                            recordChanged = true;
                            break;
                        }
                    }
                }
                        
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("EnterpriseObject before persisting : " + eo);
                }
                
                if (mSkipUpdateIfNoChange && !recordChanged) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("SkipUpdateIfNoChange is true, no updates for EUID: " + euid);
                    }
                    return new UpdateResult(TMResult.NO_CHANGE_TRANSACTION, eo);   
                }
                
                // persist newEo
                // pass in the sysCode and lid that caused the change in EO
                TMResult tmr = mTransaction.updateEnterpriseObject(con, eo, sysCode, lid, revisionNumber, "Update");
                
                return new UpdateResult(tmr, eo);
            } catch (OPSException rex) {
                throw new UpdateException(mLocalizer.t("UPD507: Could not " +
                                            "update an EnterpriseObject in the " +
                                            "database: {0}", rex));
            }
        }
        
        throw new UpdateException(mLocalizer.t("UPD508: EnterpriseObject " +
                                            "and SystemObject cannot be null."));
    }
    
    
    /** Updates a particular EnterpriseObject     
     * @param con connection
     * @param eo <code>EnterpriseObject</code> to be updated
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.    
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode     
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
                    Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, int flags, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
                    
        return updateEnterprise(con, eo, null, flags, user);                    
    }    
    /** Updates a particular EnterpriseObject
     * @param con connection
     * @param eo <code>EnterpriseObject</code> to be updated
     * @param revisionNumber  The revision number of the SBR of the associated SO.
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
                    Connection con, EnterpriseObject eo, 
                    String revisionNumber, int flags, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
        if (eo != null) {
            boolean recordChanged = false;
            Date date = new Date();
            String euid = eo.getEUID();
            Collection sysobjs = eo.getSystemObjects();
            Iterator i = sysobjs.iterator();
            while (i.hasNext()) {
                SystemObject so = (SystemObject) i.next();
                if (!so.isAdded() && isObjectChanged(so)) {
                    so.setUpdateFunction(SystemObject.ACTION_UPDATE);
                    so.setUpdateUser(user);
                    // Timestamp can not be null for SC to work
                    if (so.getUpdateDateTime() == null) {
                        so.setUpdateDateTime(date);
                    }
                    recordChanged = true;
                }
                
                if (so.isAdded()) {
                    if (so.getCreateDateTime() == null) {
                        so.setCreateDateTime(date);
                    }
                    // Timestamp can not be null for SC to work
                    if (so.getUpdateDateTime() == null) {
                        so.setUpdateDateTime(date);
                    }
                    so.setUpdateFunction(SystemObject.ACTION_ADD);
                    so.setCreateFunction(SystemObject.ACTION_ADD);
                    so.setCreateUser(user);
                    recordChanged = true;
                }
            }
            
            EnterpriseObject ogEO = null;
            if (mEUpdatePolicy != null) {
                ogEO = (EnterpriseObject) eo.copy();
                //Use transaction logs to restore passed in image to what it was 
                //before caller modified it (do not remove this please - it actually
                //has a purpose)
                try{
                    ogEO = mTransaction.getBeforeImage(ogEO);
                } catch (OPSException oex) {
                    throw new UpdateException(mLocalizer.t("UPD509: Could not " +
                                            "update an EnterpriseObject.  The " +
                                            "\"before\" image could not be "+ 
                                            "retrieved: {0}", oex));
                }
	    }

            //check how many active SO's are left, if 0 then deactivate EO
            if (countActiveSystems(eo) < 1) {
                // no active systems
                // deactivate source enterprise object
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("No active SystemObjects left, deactivating EntepriseObject");
                }
                mHelper.deactivateEO(eo, date);
            } else {
                // calculate SBR
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Recalculating SBR for EntepriseObject");
                }
                mCalculator.determineSurvivor(eo);
            }
                        
            if (mEUpdatePolicy != null) {
                eo = mEUpdatePolicy.applyUpdatePolicy(ogEO, eo);
            }
            
            // Check if SBR changed by SC or update policy
            SBR sbr = eo.getSBR();
            if (isObjectChanged(sbr.getObject())) {
               sbr.setUpdateDateTime(date);
               sbr.setUpdateFunction(SystemObject.ACTION_UPDATE);
               sbr.setUpdateUser(user);
               recordChanged = true;
            }
            
            ArrayList overwrites = sbr.getOverWrites();
            if (overwrites != null) {
                Iterator iter = overwrites.iterator();
                while (iter.hasNext()) {
                    SBROverWrite ow = (SBROverWrite) iter.next();
                    if (isObjectChanged(ow)) {
                        sbr.setUpdateDateTime(date);
                        sbr.setUpdateFunction(SystemObject.ACTION_UPDATE);
                        sbr.setUpdateUser(user);
                        recordChanged = true;
                        break;
                    }
                }
            }
            
            // Check if SO's changed
            if (!recordChanged && sysobjs != null) {
                i = sysobjs.iterator();
                while (i.hasNext()) {
                    SystemObject sysObj = (SystemObject) i.next();
                    if (isObjectChanged(sysObj.getObject())) {
                        recordChanged = true;
                        break;
                    }
                }
            }
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("EntepriseObject before persisting: " + eo);
            }
            
            if (mSkipUpdateIfNoChange && !recordChanged) {
                if (mLogger.isLoggable(Level.FINE)) {
                	mLogger.fine("SkipUpdateIfNoChange is true, no updates for EUID: " + euid);
                }
                return new UpdateResult(TMResult.NO_CHANGE_TRANSACTION, eo);   
            }
            
            try {
                TMResult tmr = null;
                
                // persist newEo
                tmr = mTransaction.updateEnterpriseObject(con, eo, revisionNumber, "Update");
                
                return new UpdateResult(tmr, eo);
            } catch (OPSException oex) {
                throw new UpdateException(mLocalizer.t("UPD510: Could not " +
                                            "update an EnterpriseObject: {0}", oex));
            }
        }
        
        throw new UpdateException("EnterpriseObject can not be null");
    }
    
    /** Deactivates a particular EnterpriseObject     
     * @param con connection
     * @param eo <code>EnterpriseObject</code> to be deactivated
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     */
    public com.sun.mdm.index.update.UpdateResult deactivateEnterprise(
                    Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
            throws UpdateException,
                   ObjectException,
                   DataModifiedException{
        if (eo != null) {
            try {
            	Date date = new Date();
                mHelper.deactivateEO(eo, date);
                eo.getSBR().setUpdateUser(user);
                TMResult tmr = mTransaction.updateEnterpriseObject(con, eo, "euidDeactivate");
                return new UpdateResult(tmr, eo);
            } catch (OPSException oex) {
                throw new UpdateException(mLocalizer.t("UPD511: Could not " +
                                            "deactivate an EnterpriseObject: {0}", oex));
            }
        }
        
        throw new UpdateException(mLocalizer.t("UPD512: EnterpriseObject " +
                                            "cannot be null. "));
    }
    
    /** Activates a particular EnterpriseObject
     * @param con connection
     * @param eo <code>EnterpriseObject</code> to be deactivated
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     */
    public com.sun.mdm.index.update.UpdateResult activateEnterprise(
    Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
    throws UpdateException,
    ObjectException,
    DataModifiedException {
        if (eo != null) {
        	Date date = new Date();
            try {
                mHelper.activateEO(eo, date);
                eo.getSBR().setUpdateUser(user);
                TMResult tmr = mTransaction.updateEnterpriseObject(con, eo, "euidActivate");
                return new UpdateResult(tmr, eo);
            } catch (OPSException oex) {
                throw new UpdateException(mLocalizer.t("UPD513: Could not " +
                                         "activate an EnterpriseObject: {0}", oex));
            }
        }
        
        throw new UpdateException(mLocalizer.t("UPD514: EnterpriseObject " +
                                            "cannot be null."));
    }
    
    /**
     * Transfer system object from source Enterprise Object to
     * destination Enterprise Object
     *
     * @param con connection
     * @param srcEO source of the transfer
     * @param destEO destination of the transfer
     * @param system system code of the <code>SystemObject</code> to transfer
     * @param lid local ID of the <code>SystemObject</code> to transfer
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     */
    public com.sun.mdm.index.update.UpdateResult transferSystem(
    Connection con, com.sun.mdm.index.objects.EnterpriseObject srcEO,
    com.sun.mdm.index.objects.EnterpriseObject destEO,
    java.lang.String system, java.lang.String lid, String user)
    throws UpdateException,
    SurvivorCalculationException,
    SystemObjectException,
    ObjectException,
    DataModifiedException {
        try {
            mHelper.transferSO(system, lid, srcEO, destEO);
            Date date = new Date();
            // check how many active system objects are left
            // not counting QWS
            if (countActiveSystems(srcEO) < 1) {
                if ( countInActiveSystems(srcEO) > 0 ) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Only inactive SystemObject(s) left, " 
                                     + "deactivating EnterpriseObject");
                    }
                    mHelper.deactivateEO(srcEO, date);                 
                } else {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("No active or inactive SystemObject " 
                                     + "left, remove EnterpriseObject");
                    }
                    srcEO.setRemoveFlag(true);
                }
            } else {
                // more then one active system object on enterprise object
                // call survivor calculation on src enterprise object
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Recalculating SBR for source EnterpriseObject");
                }
                mCalculator.determineSurvivor(srcEO);
                
                srcEO.getSBR().setUpdateDateTime(date);
                srcEO.getSBR().setUpdateFunction(SystemObject.ACTION_TRANSFER);
            }
            srcEO.getSBR().setUpdateUser(user);
            
            // call survivor calculation on dest enterprise object
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Recalculating SBR for destination EnterpriseObject");
            }
            EnterpriseObject retEO = destEO;
            mCalculator.determineSurvivor(retEO);
            
            retEO.getSBR().setUpdateDateTime(date);
            retEO.getSBR().setUpdateFunction(SystemObject.ACTION_TRANSFER);
            retEO.getSBR().setUpdateUser(user);
            
            // persist dest enterprise object
            TMResult r = mTransaction.lidTransfer(con, retEO, srcEO, system, lid);
            
            return new UpdateResult(r, retEO, srcEO);
        } catch (OPSException oex) {
            throw new UpdateException(mLocalizer.t("UPD515: Could not transfer " +
                                            "SystemObjects:{0}", oex));
        }
    }
    
    /**Removes a System Object from a Enterprise Object
     *
     * @param con connection
     * @param srcEO source EnterpriseObject
     * @param system SystemCode of the SystemObject to be removed
     * @param lid local ID of the SystemObject to be removed
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     */
    public com.sun.mdm.index.update.UpdateResult removeSystem(Connection con,
    com.sun.mdm.index.objects.EnterpriseObject srcEO, java.lang.String system,
    java.lang.String lid, String user)
    throws UpdateException,
    SurvivorCalculationException,
    SystemObjectException,
    ObjectException,
    DataModifiedException {
        
        try {
            mHelper.removeSO(system, lid, srcEO);
            Date date = new Date();
            // check how many active system objects are left
            // not counting QWS
            if (countActiveSystems(srcEO) < 1) {
                if ( countInActiveSystems(srcEO) > 0 ) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Only inactive SystemObject(s) left, deactivating EnterpriseObject");
                    }
                    mHelper.deactivateEO(srcEO, date);                 
                } else {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("No active or inactive SystemObject left, removing EnterpriseObject");
                    }
                    srcEO.setRemoveFlag(true);
                }
            } else {
                // more then one active system object on enterprise object
                // call survivor calculation on src enterprise object
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Recalculating SBR for EnterpriseObject");
                }
                mCalculator.determineSurvivor(srcEO);
                
                srcEO.getSBR().setUpdateDateTime(date);
                srcEO.getSBR().setUpdateFunction(SystemObject.ACTION_REMOVE);
            }
            srcEO.getSBR().setUpdateUser(user);
            
            // persist source enterprise object
            TMResult tmr = mTransaction.updateEnterpriseObject(con, srcEO, system, lid, "Update");
            
            return new UpdateResult(tmr, srcEO);
        } catch (OPSException rex) {
            throw new UpdateException(mLocalizer.t("UPD516: Could not remove " +
                                            "a SystemObject: {0}", rex));
        }
    }
    
    /** merge two LID within the same EnterpriseObject and update the existing
     * SystemObject with values from newSO
     *
     * @param con connection
     * @param srcEO source of the transfer
     * @param system SystemCode of the SystemObject to be removed
     * @param lid local ID of the SystemObject to be removed
     * @param newSO merged image of the SystemObject
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws UpdateException error updating     
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult mergeSystem(Connection con,
                    com.sun.mdm.index.objects.EnterpriseObject srcEO, String system,
                    String lid, com.sun.mdm.index.objects.SystemObject newSO, int flags, String user)
            throws SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   UpdateException,
                   DataModifiedException,
                   UserException {
        EnterpriseObject ogEO = null;
        if (mSMergePolicy != null) {
            ogEO = (EnterpriseObject) srcEO.copy();
	}
        
        Date date = new Date();
        newSO.setUpdateDateTime(date);
        newSO.setUpdateUser(user);
        
        SystemObject mergedSO = srcEO.getSystemObject(system, lid);
        
        if (mergedSO == null) {
            throw new UpdateException(mLocalizer.t("UPD517: SystemObject to be " +
                            "merged does not exist in specified EnterpriseObject."));
        }
        
        mergedSO.setValue("Status", SystemObject.STATUS_MERGED);
        
        // Clear the object ID of Added child object (the source SO)
        // Clear the object ID of Updated child object (the destination SO)
        // This will force the OPS level to assign object ID's to added
        // objects.
        ObjectNode topLevelObject = newSO.getObject();
        ArrayList childList = topLevelObject.pGetChildren();
        if (childList != null) {
            Iterator childIter = childList.iterator();
            while(childIter.hasNext()) {
                ObjectNode childObj = (ObjectNode) childIter.next();
                String tag = childObj.pGetTag();
                if (childObj.isAdded()) {
                    ObjectField field = childObj.getField(childObj.pGetTag() + "Id");
                    field.setValue(null);
                }
            }
        }

        EnterpriseObject newEO;
        boolean replaceSO = true;
        newEO = mHelper.updateSO(newSO, srcEO, true, replaceSO);
        
        if (null == newEO) {
            throw new UpdateException(mLocalizer.t("UPD518: EnterpriseObject does not " +
                                                "contain the specified SystemObject."));
        }
        
        // SBROverride code starts here - SambaG
        removeSBROverrideLinks(srcEO,system, lid);
        removeSBROverrideLinks(newEO,system, lid);
        // SBROverride coce ends here - SambaG
        //because its same EO lid merge, there will always be at least 1 SO left on the EO
        //so no need to count the active SOs
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("EnterpriseObject before survivor calculation: " + newEO);
        }
        mCalculator.determineSurvivor(newEO);
        newEO.getSBR().setUpdateDateTime(date);
        newEO.getSBR().setUpdateFunction(SystemObject.ACTION_MERGE);
        newEO.getSBR().setUpdateUser(user);
        
        // Clear the object ID of Added child object in the SBR.  The object ID
        // are derived from the source object, so they have no relation to the
        // object ID's in the SBR tables.  For example, using the Most Recently 
        // Modified strategy, suppose there is an existing Address SBR record 
        // with an object ID of 004 was generated from an Address record 
        // with an object ID of 005(aka as Address005).  Address005 was added after
        // Address004 (object ID 004) was added.  In an LID Merge, suppose 
        // Address005 was merged into Address 004.  Address004 would be added to 
        // the SBR through the Survivor Calculator.  However, the new Address SBR
        // record's object ID would be obtained from Address004, which is 004. 
        // This conflicts with the existing Address SBR's object ID, which is 
        // also 004.
        topLevelObject = newEO.getSBR().getObject();
        childList = topLevelObject.pGetChildren();
        if (childList != null) {
            Iterator childIter = childList.iterator();
            while(childIter.hasNext()) {
                ObjectNode childObj = (ObjectNode) childIter.next();
                String tag = childObj.pGetTag();
                if (childObj.isAdded()) {
                    ObjectField field = childObj.getField(childObj.pGetTag() + "Id");
                    field.setValue(null);
                }
            }
        }

        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("EnterpriseObject after survivor calculation: " + newEO);
        }
        
        if (mSMergePolicy != null) {
            newEO = mSMergePolicy.applyUpdatePolicy(ogEO, newEO);
        }
        
        try {
            TMResult tmr = null;
            if ((flags & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                // persist ret
                tmr = mTransaction.lidMerge(con, newEO, null, system, newSO.getLID(), lid);
            }
            return new UpdateResult(tmr, newEO);
        } catch (OPSException rex) {
            throw new UpdateException(mLocalizer.t("UPD519: Could not " +
                                                "merge SystemObjects: {0}", rex));
        }
    }
    
    /** Merge two system objects from two EnterpriseObjects.  Transfers over the specified
     * SystemObject, deactivates it, and update remaining SystemObject with values from
     * newSO     
     * @param con connection
     * @param srcEO source of the transfer
     * @param destEO destination of the transfer
     * @param system SystemCode of the <code>SystemObject</code> to transfer
     * @param lid local id of the <code>SystemObject</code> to transfer
     * @param newSO merged image of the SystemObject
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.* @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws UpdateException error updating     
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult mergeSystem(Connection con,
                    com.sun.mdm.index.objects.EnterpriseObject srcEO,
                    com.sun.mdm.index.objects.EnterpriseObject destEO, String system,
                    String lid, com.sun.mdm.index.objects.SystemObject newSO, int flags, String user)
            throws SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   UpdateException,
                   DataModifiedException,
                   UserException {

        if (srcEO.equals(destEO)) {
            //same EO, throw exception
            throw new UpdateException(mLocalizer.t("UPD520: Source EnterpriseObject " +
                                                "and destination EnterpriseObject " +
                                                "cannot be the same for cross-EO " +
                                                "SystemObject merge."));
        }
        
        Date date = new Date();
        newSO.setUpdateDateTime(date);
        newSO.setUpdateUser(user);
        EnterpriseObject ogEO = null;
        if (mSMergePolicy != null) { 
            ogEO = (EnterpriseObject) destEO.copy();
	}
        
        try {
            SystemObject mergedSO = srcEO.getSystemObject(system, lid);
            
            // setStatus() causes an error message saying to use master controller
            // for now, use setValue()
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Set system object to merged status" + mergedSO);
            }
            
            mergedSO = (SystemObject) mergedSO.copy();
            mergedSO.setValue("Status", SystemObject.STATUS_MERGED);
            srcEO.deleteSystemObject(system, lid);
            destEO.addSystemObject(mergedSO);
            
            // clear the object ID of all Added child objects
            ObjectNode topLevelObject = newSO.getObject();
            ArrayList childList = topLevelObject.pGetChildren();
            if (childList != null) {
                Iterator childIter = childList.iterator();
                while(childIter.hasNext()) {
                    ObjectNode childObj = (ObjectNode) childIter.next();
                    String tag = childObj.pGetTag();
                    if (childObj.isAdded()) {
                        ObjectField field = childObj.getField(childObj.pGetTag() + "Id");
                        field.setValue(null);
                    }
                }
            }
            // SBROverride code starts here - SambaG
            removeSBROverrideLinks(srcEO, system, lid);
            removeSBROverrideLinks(destEO, system, lid);
            // SBROverride coce ends here - SambaG
            // update the so with the passed in value
            boolean replaceSO = true;
            EnterpriseObject newEO = mHelper.updateSO(newSO, destEO, true, replaceSO);
            if (newEO == null) {
                throw new UpdateException(mLocalizer.t("UPD521: EnterpriseObject " +
                                                "does not contain the " +
                                                "specified SystemObject."));
            }
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject before invoking " 
                             + " survivor calculation: " + newEO);
            }
            
            // check how many active system objects are left
            // not including QWS
            if (countActiveSystems(srcEO) < 1) {
                // no active systems
                // deactivate source enterprise object
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("No active SystemObject left, deactivating EnterpriseObject");
                }
                try {
                    String[] mlids=mQueryHelper.findAllMergedLIDs(con, lid);
                    if ((mlids != null) && (!srcEO.getEUID().equals(destEO.getEUID()))) {
                        for (int i=0; i < mlids.length; i++) {
                            SystemObject mSO = srcEO.getSystemObject(system, mlids[i]);
                            SystemObject mcSO = (SystemObject) mSO.copy();
                            srcEO.deleteSystemObject(system, mlids[i]);
                            destEO.addSystemObject(mcSO);
                        }
                    }
                } catch(Exception e1) {
                    throw new UpdateException(mLocalizer.t("UPD522: Could not " +
                                            "retrieve all merged SystemObjects: {0}", e1));
                }
                
                if ( countInActiveSystems(srcEO) > 0 ) {
                    mHelper.deactivateEO(srcEO, date);                    
                } else {
                    srcEO.setRemoveFlag(true);
                }
                
            } else {
                // more then one active system object on enterprise object
                // call survivor calculation on src enterprise object
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Recalculating SBR for source EnterpriseObject");
                }
                mCalculator.determineSurvivor(srcEO);
                srcEO.getSBR().setUpdateDateTime(date);
                srcEO.getSBR().setUpdateFunction(SystemObject.ACTION_MERGE);
            }
            srcEO.getSBR().setUpdateUser(user);
            
            // call SC on the new merged EO
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Recalculating SBR for destination EnterpriseObject");
            }
            mCalculator.determineSurvivor(newEO);
            newEO.getSBR().setUpdateDateTime(date);
            newEO.getSBR().setUpdateFunction(SystemObject.ACTION_MERGE);
            newEO.getSBR().setUpdateUser(user);
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject after survivor " 
                             + "calculation:" + newEO);
                mLogger.fine("Merged EnterpriseObject: " + srcEO);
            }
            
            if (mSMergePolicy != null) {
                newEO = mSMergePolicy.applyUpdatePolicy(ogEO, newEO);
            }
            
            // persist both srcEO and destEO
            TMResult tmr = null;
            if ((flags & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                // persist ret
                tmr = mTransaction.lidMerge(con, newEO, srcEO, system,
                newSO.getLID(), lid);
            }
            return new UpdateResult(tmr, newEO, srcEO);
        } catch (OPSException rex) {
            throw new UpdateException(mLocalizer.t("UPD523: Could not " +
                                           "merge SystemObjects: {0}", rex));
        }
    }
    
    /** Merges two <code>EnterpriseObject</code> by transfering all of the
     * <code>SystemObject</code> from the source to the destination.
     * @param con connection
     * @param srcEO the source of the transfers
     * @param destEO the destination of the transfers
     * @param flags optional flags
     * @param user user
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult mergeEnterprise(
                    Connection con, com.sun.mdm.index.objects.EnterpriseObject srcEO,
                    com.sun.mdm.index.objects.EnterpriseObject destEO, int flags, String user)
            throws SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   UpdateException,
                   DataModifiedException,
                   UserException {

        Date date = new Date();
        EnterpriseObject ogEO = null;
        if (mEMergePolicy != null) {
            ogEO = (EnterpriseObject) destEO.copy();
	}
        
        try {
            // for each system, move over
            Collection xferSystems = srcEO.getSystemObjects();
            Iterator xferIter = xferSystems.iterator();
            
            while (xferIter.hasNext()) {
                SystemObject sysObj = (SystemObject) xferIter.next();
                
                if (!sysObj.getSystemCode().equals(Constants.QWS_SYSTEM_CODE)) {
                    // do not move QWS over
                    SystemObject copySysObj = (SystemObject) sysObj.copy();
                    // not deleting SO individually, delete the entire EO
                    //srcEO.deleteChild(sysObj.getType(), sysObj.getKey());
                    destEO.addSystemObject(copySysObj);
                }
            }
            
            srcEO.setRemoveFlag(true);
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject before invoking " 
                             + "survivor calculation :" + destEO);
            }
            
            EnterpriseObject retEO = destEO;
            mCalculator.determineSurvivor(retEO);
            retEO.getSBR().setUpdateDateTime(date);
            retEO.getSBR().setUpdateFunction(SystemObject.ACTION_MERGE);
            retEO.getSBR().setUpdateUser(user);

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject after survivor calculation :" 
                             + retEO);
            }
            if (mEMergePolicy != null) {
                retEO = mEMergePolicy.applyUpdatePolicy(ogEO, retEO);
            }
            
            // SBROverride code starts here - SambaG
           removeSBROverrideLinks(srcEO);
           removeSBROverrideLinks(retEO); 
            // SBROverride code ends here - SambaG
            TMResult tmr = null;
            if ((flags & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                tmr = mTransaction.euidMerge(con, retEO, srcEO);
            }
            return new UpdateResult(tmr, retEO, srcEO);
        } catch (OPSException rex) {
            throw new UpdateException(mLocalizer.t("UPD524: Could not " +
                                            "merge EnterpriseObjects: {0}", rex));
        }
    }

    /** Merges two <code>EnterpriseObject</code> by transfering all of the
     * <code>SystemObject</code> from the source to the destination.
     * @param con connection
     * @param srcEO the source of the transfers
     * @param destEO the destination of the transfers
     * @param srcRevisionNumber  The SBR revision number or the surviving EnterpriseObject
     * @param destRevisionNumber The SBR revision number or the merged EnterpriseObject
     * @param flags optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws UpdateException error updating
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult mergeEnterprise(
                    Connection con, com.sun.mdm.index.objects.EnterpriseObject srcEO,
                    com.sun.mdm.index.objects.EnterpriseObject destEO, 
                    String srcRevisionNumber, String destRevisionNumber, 
                    int flags, String user)
            throws SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   UpdateException,
                   DataModifiedException,
                   UserException {
        
        Date date = new Date();
        EnterpriseObject ogEO = null;
        if (mEMergePolicy != null) {
            ogEO = (EnterpriseObject) destEO.copy();
        }
        
        try {
            // for each system, move over
            Collection xferSystems = srcEO.getSystemObjects();
            Iterator xferIter = xferSystems.iterator();
            
            while (xferIter.hasNext()) {
                SystemObject sysObj = (SystemObject) xferIter.next();
                
                if (!sysObj.getSystemCode().equals(Constants.QWS_SYSTEM_CODE)) {
                    // do not move QWS over
                    SystemObject copySysObj = (SystemObject) sysObj.copy();
                    // not deleting SO individually, delete the entire EO
                    destEO.addSystemObject(copySysObj);
                }
            }
            
            srcEO.setRemoveFlag(true);
            
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject before invoking " 
                             + "survivor calculation :" + destEO);
            }
            
            EnterpriseObject retEO = destEO;
            mCalculator.determineSurvivor(retEO);
            retEO.getSBR().setUpdateDateTime(date);
            retEO.getSBR().setUpdateFunction(SystemObject.ACTION_MERGE);
            retEO.getSBR().setUpdateUser(user);

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Surviving EnterpriseObject after invoking " 
                             + "survivor calculation :" + retEO);
            }
            if (mEMergePolicy != null) {
                retEO = mEMergePolicy.applyUpdatePolicy(ogEO, retEO);
            }
            // SBROverwrite code starts here
            removeSBROverrideLinks(srcEO);
            removeSBROverrideLinks(retEO);
            // SBROverwrite code ends here
            
            TMResult tmr = null;
            if ((flags & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                tmr = mTransaction.euidMerge(con, retEO, srcEO, 
                                             srcRevisionNumber, destRevisionNumber);
            }
            return new UpdateResult(tmr, retEO, srcEO);
        } catch (OPSException rex) {
            throw new UpdateException(mLocalizer.t("UPD525: Could not " +
                                            "merge EnterpriseObjects: {0}", rex));
        }
    }

    private int countActiveSystems(com.sun.mdm.index.objects.EnterpriseObject eo)
    throws com.sun.mdm.index.objects.exception.ObjectException {
        
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
    
    /** Splits a SystemObject from its current EnterpriseObject and assign it
     * to a new EnterpriseObject
     * @param con connection
     * @param system SystemCode
     * @param lid local ID
     * @param eo EnterpriseObject to removed the SystemObject from.
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception     
     */
    public com.sun.mdm.index.update.UpdateResult splitSystem(Connection con,
                    String system, String lid, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {

        SystemObject so = eo.getSystemObject(system, lid);
        
        removeSystem(con, eo, system, lid, user);
        
        // Clear all object ID's in the SO.  This will force new object ID's to
        // be assigned by OPS.  Otherwise, the object ID's of the child objects in 
        // in the SBR will be duplicates of the original ones (in the SO) that were 
        // copied from the original EO
       
        clearObjectIDs(so);
        
        // create new eo
        UpdateResult ur = createEnterpriseObject(con, so);
        EnterpriseObject newEO = ur.getEnterpriseObject1();
        return new UpdateResult(ur.getTransactionResult(), eo, newEO);
    }
    
    /** Splits a SystemObject from its current EnterpriseObject and assign it
     * to a new EnterpriseObject
     * @param con connection
     * @param so system objec to be split out
     * @param eo enterprise object to split from
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult splitSystem(Connection con,
                    com.sun.mdm.index.objects.SystemObject so,
                    com.sun.mdm.index.objects.EnterpriseObject eo, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
        
        UpdateResult ur = splitSystem(con, so.getSystemCode(), so.getLID(), eo, user);
        return ur;
    }
    
    /** Unmerges a SystemObject merge.
     * @param con connection
     * @param transactionID transaction ID of the SystemObject merge
     * @param flag optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    
    public UpdateResult unmergeSystem(
        Connection con, String transactionID, int flag, String user)
        throws UpdateException, SurvivorCalculationException,
               SystemObjectException, ObjectException, DataModifiedException,
               UserException {
        
        Date date = new Date();
        try {
            RecreateResult rr = mTransaction.recreateObject(con, transactionID);
            
            TransactionObject transObj = mTransaction.findTransactionLog(con, transactionID);
            
            EnterpriseObject beforeDestEO = rr.getBeforeEO1();
            EnterpriseObject beforeSrcEO = rr.getBeforeEO2();
            EnterpriseObject afterDestEO = rr.getAfterEO();
            
            String mergedSysCode = transObj.getSystemCode();
            String mergedLid = transObj.getLID2();
            String winLid = transObj.getLID1();
            
            EnterpriseObject curDestEO = null;
            EnterpriseObject curSrcEO = null;
            
            String destEuid = beforeDestEO.getEUID();
            curDestEO = mTransaction.getEnterpriseObject(con,
            destEuid);
            // check if current dest eo has been deleted, there for null
            if (curDestEO == null) {
                throw new UpdateException(mLocalizer.t("UPD531: The EnterpriseObject " +
                                            "has been removed: {0}", destEuid));
            }
            EnterpriseObject ogDestEO = null;
            if (mSUnmergePolicy != null) {
                ogDestEO = (EnterpriseObject) curDestEO.copy();
	    }
            
            String removeSysCode = null;
            String removeLid = null;

            if (beforeSrcEO == null) {
                // src eo was null, meaning same EO lid merge.
                curSrcEO = null;
                
                // restore the merged SO to active status
                SystemObject so = curDestEO.getSystemObject(mergedSysCode,
                mergedLid);
                if (so.getStatus().equals(SystemObject.STATUS_MERGED)) {
                    so.setValue("Status", SystemObject.STATUS_ACTIVE);
                } else {
                    throw new UpdateException(mLocalizer.t("UPD526: The SystemObject " +
                                            "with SystemCode={0}, Local ID={1} " +
                                            "does not have merged status.", 
                                            mergedSysCode, mergedLid));
                }
                
                // restore the winning SO to its previous image
                so = beforeDestEO.getSystemObject(mergedSysCode, winLid);
                SystemObject curSo = curDestEO.getSystemObject(mergedSysCode, winLid);
                curSo.getObject().updateIfNotEqual( so.getObject(), true, true );
                
                // For unkeyed objects, the survivor calculator will attempt to update 
                // the SBR with the values in the system object.  However, because 
                // they are unkeyed, it cannot update them (it cannot locate them) and 
                // will append them instead.  This will lead to duplicate  unkeyed objects.  
                // curDestEO's SBR records are in the database, so they need to be 
                // deleted (i.e. set the Remove flag to true).            

                eliminateSBRUnkeyedChildObjects(curDestEO, true);
                
                // call survivor calculator
                mCalculator.determineSurvivor(curDestEO);
                curDestEO.getSBR().setUpdateFunction(SystemObject.ACTION_UNMERGE);
                curDestEO.getSBR().setUpdateUser(user);
                
            } else {
                                   
                curSrcEO = mTransaction.getEnterpriseObject(con, beforeSrcEO.getEUID());
                SystemObject so;
                // restore the merged SO which was deleted
                so = beforeSrcEO.getSystemObject(mergedSysCode, mergedLid);
               
                // for backwards compatibility with records merged in 5.0.1
                if (so.getStatus().equals(SystemObject.STATUS_MERGED)) {
                    so.setValue("Status", SystemObject.STATUS_ACTIVE);
                }
                if (curSrcEO == null) {
                    // if the source EO does not exist in this system anymore then either
                    // 1) the system merge caused the src eo to be deactivated
                    // 2) operation post the merge caused the eo to be removed
                    curSrcEO = beforeSrcEO;
                    curSrcEO.setAddFlag(true);
                } else {
                    try {
                        curSrcEO.addSystemObject(so);
                    } catch (Exception e) {
                        SystemObjectPK systemKey = 
                            new SystemObjectPK(so.getSystemCode(), so.getLID());
                        String status = null;
                        try{
                            status = mQueryHelper.getSOStatus(con, systemKey);
                        }catch(QMException qex){
                            throw new UpdateException(mLocalizer.t("UPD527: Could not " +
                                            "retrieve the status for the SystemObject " +
                                            "with this SystemKey: {0}", 
                                            systemKey.toString()));
                        }
                        if (status == null) {
                            throw new UpdateException(mLocalizer.t("UPD528: Source system " +
                                            "record could not be found with this SystemKey: {0}",
                                            systemKey.toString()));
                        }
                        if (status.compareToIgnoreCase(SystemObject.STATUS_MERGED) != 0) {
                            throw new UpdateException(mLocalizer.t("UPD529: Record has " +
                                            "been modified by another user. System record " +
                                            "with SystemKey={0} has already been unmerged.", 
                                            systemKey.toString()));
                        } else {
                            throw new UpdateException(mLocalizer.t("UPD530: Record has " +
                                            "been modified by another user. System record " +
                                            "with SystemKey={0} has this status: {1}", 
                                            systemKey.toString(), status));
                        }
                    }
                }
                mHelper.activateEO(curSrcEO, date);
                
                
                //Delete the SO that was transferred due to merge
                // Do not remove this at this time as the OPS layer need it 
                // so that this record can be deleted from data database. 
                // after ops layer has done its operation, it should be removed
                // ( physically ) from the EnterpriseObject so that EO reflect the
                // correct values when EDM display the EO ( EDM will need the EO 
                // for displaying purpose only if calcOnly flag is true. 
                
                curDestEO.deleteSystemObject(mergedSysCode, mergedLid);
                removeSysCode = mergedSysCode;
                removeLid = mergedLid;
                
                // restore the winning SO to its previous image
                so = beforeDestEO.getSystemObject(mergedSysCode, winLid);               
                SystemObject curSo = curDestEO.getSystemObject(mergedSysCode, winLid);
                curSo.getObject().updateIfNotEqual( so.getObject(), true, true );
                                
                // For unkeyed objects, the survivor calculator will attempt to update 
                // the SBR with the values in the system object.  However, because 
                // they are unkeyed, it cannot update them (it cannot locate them) and 
                // will append them instead.  This will lead to duplicate  unkeyed objects.  
                // curDestEO�s SBR records are in the database, so they need to be 
                // deleted (i.e. set the Remove flag to true).            
                // If curSrcEO has been added, it was deactivated, such as in the 
                // case where it had 1 SO, which was merge to another EO.  The record
                // is in memory.  Thus, the SBR records  need to be removed.  Otherwise, 
                // the EO has been retrieved from the database and the SBR records 
                // must be deleted (i.e. set the Remove flag to true).
                
                if (curSrcEO.isAdded() == true) {
                    eliminateSBRUnkeyedChildObjects(curSrcEO, false);
                } else {
                    eliminateSBRUnkeyedChildObjects(curSrcEO, true);
                }
                eliminateSBRUnkeyedChildObjects(curDestEO, true);

                // call survivor calculator
                mCalculator.determineSurvivor(curSrcEO);
                mCalculator.determineSurvivor(curDestEO);
                curSrcEO.getSBR().setUpdateFunction(SystemObject.ACTION_UNMERGE);
                curDestEO.getSBR().setUpdateFunction(SystemObject.ACTION_UNMERGE);
                curSrcEO.getSBR().setUpdateUser(user);
                curDestEO.getSBR().setUpdateUser(user);
                
            }
            
            if (mSUnmergePolicy != null) {
                curDestEO = mSUnmergePolicy.applyUpdatePolicy(ogDestEO, curDestEO);
            }
            
            String[] mlids=mQueryHelper.findAllMergedLIDs(con, mergedLid);
            if (mlids != null) {
                for (int i=0; i < mlids.length; i++) {                    
                   if (curSrcEO != null && !curDestEO.getEUID().equals(curSrcEO.getEUID())) {
                        curDestEO.deleteSystemObject(mergedSysCode, mlids[i]);
                    }
                }
            }

            // SBROverriding code starts here - SamabG
            removeSBROverrideLinks(curDestEO, removeSysCode, removeLid);
            removeSBROverrideLinks(curSrcEO, mergedSysCode, mergedLid);
            // SBROverriding code ends here - SamabG
            // call TM to presist beforeDestEO
            TMResult tmr = null;
            if ((flag & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                tmr = mTransaction.lidUnMerge(con, curDestEO, curSrcEO, 
                                              transactionID, mergedSysCode, 
                                              winLid, mergedLid);
            }

            // Now since the OPS has down its persistence operation, we can now 
            // remove the merged system object from the destination object permanently.            
            if (mlids != null) {
                for (int i=0; i < mlids.length; i++) {
                    if (curSrcEO != null && !curDestEO.getEUID().equals(curSrcEO.getEUID()))
                        curDestEO.removeSystemObject(mergedSysCode, mlids[i]);
                }
            }         

            if (beforeSrcEO != null) {
            	curDestEO.removeSystemObject(removeSysCode, removeLid);
            }

            return new UpdateResult(tmr, curDestEO, curSrcEO);
        } catch (Exception oex) {
            throw new UpdateException(mLocalizer.t("UPD532: Could not merge " +
                                            "SystemObjects: {0}", oex));
        }
    }
    
    /** Helper method for unmerging enterprise objects.  Using a diff map, reconstruct
     * the EO by keeping the changes made after the merge
     * @param beforeEO image of the EnterpriseObject before the merge
     * @param curEO image of the EnterpriseObject as of now
     * @param changes diff map of changes
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     */
    public void unmergeHelper(EnterpriseObject beforeEO,
    EnterpriseObject curEO, Map changes) throws ObjectException {
        
        Set s = changes.entrySet();
        Iterator changeIter = s.iterator();
        
        while (changeIter.hasNext()) {
            Map.Entry e = (Map.Entry) changeIter.next();
            String value = (String) e.getValue();
            ObjectKey key = (ObjectKey) e.getKey();
            
            // if a system object exist in current EO, but not in before EO,
            // that means it was added after the merge.
            if (CHANGES_NEW.equals(value)) {
                // SO was added after the merge, now copy it to reconstructed EO
                SystemObject tmpSO = curEO.getSystemObject(key.getKeyValue(
                "SystemCode").toString(), key.getKeyValue("LocalID").toString());
                
                if (tmpSO != null) {
                    beforeEO.addSystemObject((SystemObject) tmpSO.copy());
                } 
            } else if (CHANGES_REMOVED.equals(value)) {
                // SO was removed after the merge, now remove it from the reconstructed EO
                SystemObject tmpSO = beforeEO.getSystemObject(key.getKeyValue(
                "SystemCode").toString(), key.getKeyValue("LocalID").toString());
                
                if (tmpSO != null) {
                    beforeEO.deleteSystemObject(key.getKeyValue("SystemCode").toString(),
                        key.getKeyValue("LocalID").toString());
                } 
            } else {
                // should never reach here. error
                //assert false : "Entered an unexpected execution branch";
            }
        }
    }

    /** Helper method for unmerging enterprise objects.  Using a diff map, reconstruct
     * the EO by keeping the changes made after the merge
     * @param beforeEO image of the EnterpriseObject before the merge
     * @param curEO image of the EnterpriseObject as of now
     * @param changes diff map of changes
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     */
    public void addHelper(EnterpriseObject beforeEO,
    EnterpriseObject curEO, Map changes) throws ObjectException {
        
        Set s = changes.entrySet();
        Iterator changeIter = s.iterator();
        
        while (changeIter.hasNext()) {
            Map.Entry e = (Map.Entry) changeIter.next();
            String value = (String) e.getValue();
            ObjectKey key = (ObjectKey) e.getKey();
            
            // if a system object exist in current EO, but not in before EO,
            // that means it was added after the merge.
            if (CHANGES_NEW.equals(value)) {
                // SO was added after the merge, now copy it to reconstructed EO
                SystemObject tmpSO = curEO.getSystemObject(key.getKeyValue(
                "SystemCode").toString(), key.getKeyValue("LocalID").toString());
                
                if (tmpSO != null) {
                    beforeEO.addSystemObject((SystemObject) tmpSO.copy());
                } 
            }
        }
    }

    /** Helper method for unmerging enterprise objects.  Using a diff map, reconstruct
     * the EO by keeping the changes made after the merge
     * @param beforeEO image of the EnterpriseObject before the merge
     * @param changes diff map of changes
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     */
    public void removeHelper(EnterpriseObject beforeEO, Map changes) throws ObjectException {
        Set s = changes.entrySet();
        Iterator changeIter = s.iterator();
        
        while (changeIter.hasNext()) {
            Map.Entry e = (Map.Entry) changeIter.next();
            String value = (String) e.getValue();
            ObjectKey key = (ObjectKey) e.getKey();
            
            if (CHANGES_REMOVED.equals(value)) {
                // SO was removed after the merge, now remove it from the reconstructed EO
                SystemObject tmpSO = beforeEO.getSystemObject(key.getKeyValue(
                "SystemCode").toString(), key.getKeyValue("LocalID").toString());
                
                if (tmpSO != null) {
                    beforeEO.deleteSystemObject(key.getKeyValue("SystemCode").toString(), 
                    	key.getKeyValue("LocalID").toString());
                } 
            }
        }
    }
    
    /** compares eo1 and eo2, returns a map of SO difference of eo2 to eo1.
     * @param eo1 Enterprise Object
     * @param eo2 Enterprise Object
     * @return map of SO difference of eo2 to eo1.
     * @throws ObjectException ObjectNode access exception.
     */
    private Map diffEO(EnterpriseObject eo1, EnterpriseObject eo2)
    throws ObjectException {
        Map<ObjectKey,String> changes = new HashMap<ObjectKey,String>();
        
        ArrayList eo1NodeList = eo1.pGetChildren();
        for (int i = 0; i < eo1NodeList.size(); i++) {
            ObjectNode node = (ObjectNode) eo1NodeList.get(i);
            String type = node.pGetTag();
            ObjectKey key = node.pGetKey();
            
            ObjectNode node2 = eo2.getChild(type, key);
            
            // is in eo1, but not in eo2
            if (node2 == null) {
                changes.put(key, CHANGES_REMOVED);
            }
        }
        
        ArrayList eo2NodeList = eo2.pGetChildren();
        for (int i = 0; i < eo2NodeList.size(); i++) {
            ObjectNode node = (ObjectNode) eo2NodeList.get(i);
            String tag = node.pGetTag();
            ObjectKey key = node.pGetKey();
            
            ObjectNode node2 = eo1.getChild(tag, key);
            
            // it is in eo2, but not eo1.
            if (node2 == null) {
                changes.put(key, CHANGES_NEW);
            }
        }
        
        return changes;
    }
    
    /** identify which SO's are common to both EO's
     * @param eo1 Enterprise Object
     * @param eo2 Enterprise Object
     * @return map of SO common to eo2 and eo1.
     * @throws ObjectException ObjectNode access exception.
     */
    public Map sameEO(EnterpriseObject eo1, EnterpriseObject eo2) 
        throws ObjectException {
        Map<ObjectKey,String> same = new HashMap<ObjectKey,String>();
        
        Collection list = eo1.getSystemObjects();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SystemObject so = (SystemObject) iter.next();
            
            SystemObject so2 = eo2.getSystemObject(so.getSystemCode(), so.getLID());
            
            if (so2 != null) {
                same.put(so.pGetKey(), CHANGES_REMOVED);
            }
        }
        return same;
    }
    
    /** unmerges an EnterpriseObject merge transaction
     * @param con connection
     * @param transactionID transaction ID to unmerge
     * @param flag optional flags
     * @param user user
     * @return an <code>UpdateResult</code> containing the affected EnterpriseObjects
     * and transaction ID.
     * @throws UpdateException error updating
     * @throws SurvivorCalculationException error calculating SBR
     * @throws SystemObjectException <code>SystemObject</code> access exception
     * @throws ObjectException ObjectNode access exception.
     *      Please @see com.sun.mdm.index.objects.ObjectNode
     * @throws DataModifiedException data modification by concurrent user
     * @throws UserException general user exception
     */
    public com.sun.mdm.index.update.UpdateResult unmergeEnterprise(
                    java.sql.Connection con, java.lang.String transactionID, int flag, String user)
            throws UpdateException,
                   SurvivorCalculationException,
                   SystemObjectException,
                   ObjectException,
                   DataModifiedException,
                   UserException {
        
        try {
            Date date = new Date();
            
            RecreateResult rr = mTransaction.recreateObject(con, transactionID);
            
            EnterpriseObject beforeDestEO = rr.getBeforeEO1();
            EnterpriseObject beforeSrcEO = rr.getBeforeEO2();
            
            EnterpriseObject afterDestEO = rr.getAfterEO();
            
            // don't need the current src/merged EO, just activate the old one regardless
            String destEuid = beforeDestEO.getEUID();
            EnterpriseObject curDestEO = mTransaction.getEnterpriseObject(con, destEuid);
            
            if (curDestEO == null) {
                throw new UpdateException(mLocalizer.t("UPD533: The EnterpriseObject " +
                                            "with EUID={0} has been removed.", destEuid));
            }
            
            EnterpriseObject ogDestEO = null;
            if (mEUnmergePolicy != null) {
                ogDestEO = (EnterpriseObject) curDestEO.copy();
	    }
            EnterpriseObject newSrcEO = (EnterpriseObject) beforeSrcEO.copy();
            
            // first mark all the ones in current that came from the src eo as removed
            Map removeChanges = sameEO(curDestEO, beforeSrcEO);
            removeHelper(curDestEO, removeChanges);
            
            // remove old system objects from srcEO
            newSrcEO.removeChildren("SystemObject");
            // add system objects from cur dest EO to src eo
            Map addChanges = diffEO(beforeDestEO, afterDestEO);
            addHelper(newSrcEO, curDestEO, addChanges);
            
            // activate the src EO
            setAllFlags(newSrcEO, true, false, false);
            
            curDestEO.getSBR().setUpdateDateTime(date);
            newSrcEO.getSBR().setUpdateDateTime(date);
            
            newSrcEO.getSBR().setUpdateFunction(SystemObject.ACTION_UNMERGE);
            curDestEO.getSBR().setUpdateFunction(SystemObject.ACTION_UNMERGE);
            newSrcEO.getSBR().setUpdateUser(user);
            curDestEO.getSBR().setUpdateUser(user);
            
            // For unkeyed objects, the survivor calculator will attempt to update 
            // the SBR with the values in the system object.  However, because 
            // they are unkeyed, it cannot update them (it cannot locate them) and 
            // will append them instead.  This will lead to duplicate  unkeyed objects.  
            // newSrcEO�s SBR records are not in the database  because they were 
            // constructed from the deltas.  Thus, they need to be removed.  
            // curDestEO�s SBR records are in the database, so they need to be 
            // deleted (i.e. set the Remove flag to true).            
            
            eliminateSBRUnkeyedChildObjects(curDestEO, true);
            eliminateSBRUnkeyedChildObjects(newSrcEO, false);
            // SBROverride code starts here - SambaG
            removeSBROverrideLinks(curDestEO);
            removeSBROverrideLinks(newSrcEO);
            // SBROverride coce ends here - SambaG
            
            mCalculator.determineSurvivor(curDestEO);
            mCalculator.determineSurvivor(newSrcEO);
            
            if (mEUnmergePolicy != null) {
                curDestEO = mEUnmergePolicy.applyUpdatePolicy(beforeDestEO, curDestEO);
                newSrcEO = mEUnmergePolicy.applyUpdatePolicy(beforeSrcEO, newSrcEO);
            }
            
            //call TM to persist curDestEO and newSrcEO.
            TMResult tmr = null;
            if ((flag & Constants.FLAG_UM_CALC_ONLY) != Constants.FLAG_UM_CALC_ONLY) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Current destination EnterpriseObject before persisting: " + curDestEO);
                    mLogger.fine("New source EnterpriseObject before persisting" + newSrcEO);
                }
                tmr = mTransaction.euidUnMerge(con, transactionID, curDestEO, newSrcEO);
            }
            
            return new UpdateResult(tmr, curDestEO, newSrcEO);
        } catch (OPSException oex) {
            throw new UpdateException(mLocalizer.t("UPD534: EnterpriseObjects " +
                                            "could not be unmerged for this " +
                                            "Transaction ID {0}: {1}", transactionID, oex));
        }
    }
    
    /** Eliminate all unkeyed child objects from the SBR.  If the deleteFlag argument
     * is set to true, then the specified SBR child objects are marked for removal from 
     * the database.  Otherwise, they are present only in the memory image; they 
     * must be removed from the image only.
     * @param eo Enterprise Object to modify
     * @param deleteFlag indicates if the record should be deleted (i.e. marked 
     * for removal from the database) or removed from memory.  
     * @throws ObjectException ObjectNode access exception.
     *
     */
    private void eliminateSBRUnkeyedChildObjects(EnterpriseObject eo, boolean deleteFlag) 
            throws ObjectException {
                
        if (eo == null) {
            return;
        }
        
        SBR sbr = eo.getSBR();
        
        //  Retrieve all child tags.  Iterate through them if found
        
        ObjectNode objectNode = sbr.getObject();
        ArrayList allChildTags = objectNode.pGetChildTags();
        if (allChildTags == null) {
            return;
        }
        Iterator tagIterator = allChildTags.iterator();
        
        //  retrieve children of a specific tag
        while (tagIterator.hasNext()) {
            //  check if it is keyed
            ObjectNode child = null;
            ObjectKey childKey = null;
            String type = (String) tagIterator.next();
            ArrayList aChildren = objectNode.getChildrenForType(type, false);
            if (aChildren != null) {
                for (int i = 0; i < aChildren.size(); i++) {
                    child = (ObjectNode) aChildren.get(i);
                    childKey = child.pGetKey();
                    //  delete or remove unkeyed child objects
                    if (childKey == null) {
                        if (deleteFlag) {
                            objectNode.deleteChild(type, child.getObjectId());
                        } else {
                            objectNode.removeChild(type, childKey);
                        }
                    } else {
                        break;  // else continue checking children with other tags.
                    }
                }
            }
        }
    }

    /** Clear all object ID's in the SO.  This will force new object ID's to
     * be assigned by OPS.  Otherwise, the object ID's of the child objects in 
     * in the SBR will be duplicates of the original ones (in the SO) that were 
     * copied from the original EO
     * @param node object node to modify
     * @throws ObjectException ObjectNode access exception.
     *
     */
    private void clearObjectIDs(ObjectNode node) throws ObjectException {
        if (node == null) {
            return;
        }
        
        ArrayList childNodes = node.pGetChildren();
        
        if (childNodes == null) {
            return;
        }
        
        Iterator childNodesIter = childNodes.iterator();
        while (childNodesIter.hasNext()) {
            ObjectNode childObj = (ObjectNode) childNodesIter.next();
            ObjectField field = childObj.getField(childObj.pGetTag() + "Id");
            field.setValue(null);
            clearObjectIDs(childObj);
        }
    }
       
    /** sets the flags for all EO, SO and SBR
     * @param eo EnterpriseObject to operate on
     * @param add true if add flag should be set
     * @param update true if update flag should be set
     * @param remove true if remove flag should be set
     */
    private void setAllFlags(EnterpriseObject eo, boolean add, boolean update, boolean remove) {
        //do EO
        eo.setAddFlag(add);
        eo.setUpdateFlag(update);
        eo.setRemoveFlag(remove);
        
        //do SBR
        SBR sbr = eo.getSBR();
        sbr.setAddFlag(add);
        sbr.setUpdateFlag(update);
        sbr.setRemoveFlag(remove);
        
        //do all system objects
        Collection c = eo.getSystemObjects();
        if (c != null) {
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                ObjectNode n = (ObjectNode) iter.next();
                n.setAddFlag(add);
                n.setUpdateFlag(update);
                n.setRemoveFlag(remove);
            }
        }
    }
    
    private boolean isObjectChanged(ObjectNode obj) {
        if (obj.isUpdated() || obj.isAdded() || obj.isRemoved()) {
            return true;
        } else {
            ArrayList children = obj.pGetChildren();
            if (children == null) {
                return false;
            } else {
                for (int i = 0; i < children.size(); i++) {
                    ObjectNode n = (ObjectNode) children.get(i);
                    if (isObjectChanged(n)) {
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    private int countInActiveSystems(com.sun.mdm.index.objects.EnterpriseObject eo)
        throws com.sun.mdm.index.objects.exception.ObjectException {

        Collection systems = eo.getSystemObjects();
        if (systems == null) {
            return 0;
        }
        Iterator iter = systems.iterator();
        int count = 0;
        while (iter.hasNext()) {
            SystemObject so = (SystemObject) iter.next();
            
            if ( !(so.getStatus().equals(SystemObject.STATUS_ACTIVE)) && !so.isRemoved()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * To delete the LINK information for specified EO
     * @param eo Enterprise Object that need to delete all the corresponding LINK information
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     */
    private void removeSBROverrideLinks(EnterpriseObject eo) throws ObjectException{
        if (eo==null){
            mLogger.fine("EO is null, So no LINKs can be removed!");
            return;
}
         SBR sbr = eo.getSBR();
            ArrayList overWrites = sbr.getOverWrites();
            if (overWrites != null) {
                Iterator overWritesItr = overWrites.iterator();
                while( overWritesItr.hasNext() ){
                    SBROverWrite sbrOverWrite = (SBROverWrite) overWritesItr.next();               
                    Object fieldValue = sbrOverWrite.getData();
                    if(fieldValue instanceof java.lang.String){                        
                         String value = (String) fieldValue;
                         if(value!=null && value.length()>0){
                            if (value.charAt(0) == '[' && value.charAt( value.length()-1) == ']'){
                                sbrOverWrite.setRemoveFlag(true);
                                if (mLogger.isLoggable(Level.FINE)) {
                                    mLogger.fine("<<== removing override link info for : " + sbrOverWrite);
                                }
                            }
                         }
                    }
                }
            }
    }
    /**
     * To delete the LINK information for specified EO
     * @param eo eo Enterprise Object that need to delete all the corresponding LINK information
     * @param system SystemCode for which SO the LINKs should be deleted
     * @param lid LID for which SO the LINKs should be deleted
     * @throws com.sun.mdm.index.objects.exception.ObjectException
     */
    private void removeSBROverrideLinks(EnterpriseObject eo, String system, String lid) throws ObjectException{
            if (eo==null){
                mLogger.fine("EO is null, So no LINKs can be removed!");
                return;
            }
            SBR sbr = eo.getSBR();
            String linkTobeRemoved = "["+system+":"+lid+"]";
            ArrayList overWrites = sbr.getOverWrites();
            if (overWrites != null) {
                Iterator overWritesItr = overWrites.iterator();
                while( overWritesItr.hasNext() ){
                    SBROverWrite sbrOverWrite = (SBROverWrite) overWritesItr.next();               
                    Object fieldValue = sbrOverWrite.getData();
                    if(fieldValue instanceof java.lang.String){
                        String value = (String) fieldValue;
                        if(value!=null && value.length()>0){
                            if (value.charAt(0) == '[' && value.charAt( value.length()-1) == ']'){
                                if ( value.equals(linkTobeRemoved) ){
                                    sbrOverWrite.setRemoveFlag(true);
                                    if (mLogger.isLoggable(Level.FINE)) {
                                        mLogger.fine("<<== removing override link info for : " + sbrOverWrite);
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}
