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
package com.sun.mdm.index.ops;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import com.sun.mdm.index.idgen.SEQException;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.MergeObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.objects.epath.EPathException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.ops.exception.DataModifiedException;
import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.util.Localizer;

/**
* Transaction Manager implementation
*
* @author gzheng
*/
public class TransactionMgrImpl implements TransactionMgr {
    
    // HashMap of entity OPS handles, keyed by object type
    private HashMap mOPSMap;

    // enterprise ops handle
    private EnterpriseDB mEnterpriseDB;

    // systemobject ops handle
    private SystemObjectDB mSystemObjectDB;

    // systemsbr ops handle
    private SystemSBRDB mSystemSBRDB;

    // transaction ops handle
    private TransactionObjectDB mTransactionObjectDB;

    // merge ops handle
    private MergeObjectDB mMergeObjectDB;
   
    // use LockManager to lock EOs before any update transaction.
    private LockManager mLockManager;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
   
    private boolean isCUIDMgrInitialized = false;
   
    /** 
     * Constructor to create transaction manager
     */
    public TransactionMgrImpl() throws OPSException {
        initialize();
    }
   
    /**
     * Initializes this class.
     */
    private void initialize() throws OPSException {
       
        try {       
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Loading entity OPS map.");
            }
            Class opsClass = Class.forName("com.sun.mdm.index.ops.OPSInitHelper");
            OPSLoad ops = (OPSLoad) opsClass.newInstance();
            mOPSMap = ops.loadOPS();
            mEnterpriseDB = new EnterpriseDB();
            mSystemSBRDB = new SystemSBRDB();
            mSystemObjectDB = new SystemObjectDB();
            mTransactionObjectDB = new TransactionObjectDB();
            mMergeObjectDB = new MergeObjectDB();
            mLockManager = new LockManager();
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS640: Could not initialize " + 
                                            "TransactionMgrImpl: {0}", ex));
        }
    } 
   
    /**
     * Initialize CUID Manager. This cannot be done in the initialize() method
     * because the JDBC connection must be passed into the CUIDManager method, 
     * but the connection is not available at initializtion time . This method 
     * needs to be called at the moment CUIDManager is used to see if it has 
     * been initialized.
     *
     * @param con JDBC connection
     * @throws SEQException if an Sequence exception is encountered.
     */
    private void initCUIDManager(Connection con) throws SEQException {
        if (isCUIDMgrInitialized == false) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Initializing CUIDManager");
                String id = com.sun.mdm.index.idgen.CUIDManager.getNextUID(con, "TRANSACTIONNUMBER");   
                isCUIDMgrInitialized = true;
            }
        }
    }

    /**
     * Invokes Enterprise OPS to retrieve an EnterpriseObject by its EUID: 
     * 1.Get list of SystemCode-LID-EUID associations from SBYN_Enterprise table.
     * 2. For the EUID, retrieve the SystemSBR record.
     * 3. For each SystemCode-LID pair, retrieve the SystemObject record 
     * 4. Construct the EnterpriseObject.
     * 5. Return the EnterpriseObject.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @throws OPSException if an error is encountered.
     * @return EnterpriseObject.
     */
    public EnterpriseObject getEnterpriseObject(Connection conn, String euid)
        throws OPSException {

        EnterpriseObject eo = null;
        try {
            eo = mEnterpriseDB.get(conn, mOPSMap, euid, mSystemObjectDB, 
                                   mSystemSBRDB);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS641: Could not retrieve " + 
                                            "the EnterpriseObject record with " + 
                                            "EUID={0}: {1}", euid, ex));
        }
        return eo;
    }

    /**
     * Retrieves an EnterpriseObject by its EUID and set
     * of Epaths. The set of Epaths specify what objects are retrieved to compose EO.
     *
     * @param conn JDBC connection.
     * @param euid EUID
     * @patam epaths list of Epaths
     * @throws OPSException if an error is encountered.
     * @return EnterpriseObject
     */
    public EnterpriseObject getEnterpriseObject(Connection conn, String euid, String[] epaths)
        throws OPSException {
        EnterpriseObject eo = null;
     
        try {
            if (epaths == null) {
                eo = getEnterpriseObject(conn, euid);
            } else {
                EORetriever eoRetriever = new EORetriever();
                eo = eoRetriever.getEO(conn, euid, epaths);
            }           
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS642: Could not retrieve " + 
                                            "the EnterpriseObject record with " + 
                                            "EUID={0}: {1}", euid, ex));
        } 
        return eo; 
    }   
    
    /**
     * Invokes SystemObject OPS to retrieve a SystemObject by its SystemCode
     * and LocalID.
     *
     * @param conn JDBC connection.
     * @param systemcode System code.
     * @param lid Local ID.
     * @throws OPSException if an error is encountered.
     * @return SystemObject
     */
    public SystemObject getSystemObject(Connection conn, 
                                        String systemcode,
                                        String lid) throws OPSException {
       SystemObject so = null;

       try {
           so = mSystemObjectDB.get(conn, mOPSMap, systemcode, lid);
       } catch (OPSException ex) {
           throw ex;
       }

       return so;
   }

    /**
     * Invokes SystemSBR OPS to retrieve a SystemSBR by its EUID.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @throws OPSException if an error is encountered.
     * @return SystemSBR.
     */
    public SBR getSystemSBR(Connection conn, String euid)
            throws OPSException {
                
        SBR sbr = null;

        try {
            sbr = mSystemSBRDB.get(conn, mOPSMap, euid);
        } catch (OPSException ex) {
            throw ex;
        }
        return sbr;
    }

    /**
     * Invokes Enterprise OPS to add a new association of EUID, SystemCode, and
     * LocalID.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param systemcode System code.
     * @param lid Local ID.
     * @throws OPSException if an error is encountered.
     */
    private void pAddEnterprise(Connection conn, String euid, String systemcode,
                                String lid) throws OPSException {
        try {
            mEnterpriseDB.create(conn, euid, systemcode, lid);
        } catch (OPSException ex) {
            throw ex;
        }
    }

    /**
     * Invokes SystemObject OPS to insert a new SystemObject record.
     *
     * @param conn JDBC connection.
     * @param sysObj SystemObject
     * @throws OPSException if an error is encountered.
     */
    private void pAddSystemObject(Connection conn, SystemObject sysObj)
            throws OPSException {
        try {
            mSystemObjectDB.create(conn, mOPSMap, sysObj);
        } catch (OPSException ex) {
            throw ex;
        }
    }

    /**
     * Persists a new EnterpriseObject into database:
     * 1. Persists SystemSBR 
     * 2. For each SystemObject: 
     * - persists into database 
     * - adds EUID-SystemCode-LocalID association
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject.
     * @throws OPSException if an error is encountered.
     * @return TMResult object containing the results of the transaction.
     */
    public TMResult addEnterpriseObject(Connection conn, EnterpriseObject eo)
            throws OPSException {
                
        TMResult ret = null;

        try {
            SBR sbr = eo.getSBR();
            String euid = pAddSystemSBR(conn, sbr);

            ArrayList objs = (ArrayList) eo.getSystemObjects();
            StringBuffer systemCodes = new StringBuffer(50);
            StringBuffer lids = new StringBuffer(50);
           
            SystemObject obj = (SystemObject) objs.get(0);
            if (obj != null) {
                String sysCode = obj.getSystemCode();
                String lid = obj.getLID();

                // create systemobject rows
                pAddSystemObject(conn, obj);

                // create enterprise row
                pAddEnterprise(conn, euid, sysCode, lid);

                systemCodes.append(sysCode);
                lids.append(lid);
            }
            for (int i = 1; i < objs.size(); i++) {
                // create systemobject rows
                obj = (SystemObject) objs.get(i);
                pAddSystemObject(conn, obj);
               
                String sysCode = obj.getSystemCode();
                String lid = obj.getLID();
               
                // create enterprise row
                pAddEnterprise(conn, euid, sysCode, lid);
               
                // add the sysCode and lid to the list of SystemObjects
                systemCodes.append('|');
                systemCodes.append(sysCode);
                lids.append('|');
                lids.append(lid);
            }

            // log 'addEnterpriseObject'
            initCUIDManager(conn);           
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                    "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("Add");
            tObj.setEUID(euid);
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemCode(systemCodes.toString());
            tObj.setLID(lids.toString());
            tObj.setSystemUser(sbr.getCreateUser());
            mTransactionObjectDB.create(conn, tObj);
            ret = new TMResult(euid, transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS643: Could not add " + 
                                            "an EnterpriseObject record: {0}", ex));
        }

        return ret;
    }

    /**
     * Adds a new SystemObject to a known EUID:
     * 1. Persists the new SystemObject.
     * 2. Adds the new EUID-SystemCode-LocalID association.
     *
     * @param conn JDBC connection.
     * @param euid EUID of the EnterpriseObject to which the SystemObject is to
     * be added.
     * @param sysObj SystemObject to add.
     * @throws OPSException if an error is encountered.
     */
    private void addSystemObject(Connection conn, String euid,
                                 SystemObject sysObj) throws OPSException {
        try {
            pAddSystemObject(conn, sysObj);
            pAddEnterprise(conn, euid, sysObj.getSystemCode(), sysObj.getLID());
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                       "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("Add");
            tObj.setEUID(euid);
            tObj.setSystemCode(sysObj.getSystemCode());
            tObj.setLID(sysObj.getLID());
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(sysObj.getCreateUser());
            mTransactionObjectDB.create(conn, tObj);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS644: Could not add " + 
                                            "a SystemObject record: {0}", ex));
        } 
    }

    /**
     * Invokes SystemSBR OPS to persist a new SystemSBR record into database.
     *
     * @param conn JDBC connection.
     * @param sbr SystemSBR to add.
     * @throws OPSException if an error is encountered.
     * @return EUID to which the SystemSBR has been added.
     */
    private String addSystemSBR(Connection conn, SBR sbr)
        throws OPSException {
        String euid;
 
        try {
            euid = pAddSystemSBR(conn, sbr);
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                        "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("Add");
            tObj.setEUID(euid);
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(sbr.getCreateUser());
            mTransactionObjectDB.create(conn, tObj);
        } catch (Exception e) {
            throw new OPSException(mLocalizer.t("OPS645: Could not add " + 
                                            "a SystemSBR record: {0}", e));
        }
 
        return euid;
    }

    /**
     * Invokes SystemSBR OPS to persist a new SystemSBR record into database.
     *
     * @param conn JDBC connection.
     * @param sbr SystemSBR to add.
     * @throws OPSException if an error is encountered.
     * @return EUID to which the SystemSBR has been added.
     */
    private String pAddSystemSBR(Connection conn, SBR sbr)
        throws OPSException {
        String euid;
 
        try {
            initCUIDManager(conn);
            euid = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "EUID");
            mSystemSBRDB.create(conn, mOPSMap, euid, sbr);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS646: Could not add " + 
                                            "a SystemSBR record: {0}", ex));
        }
 
        return euid;
    }
   
    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
    private void pUpdateEnterpriseObject(Connection conn, EnterpriseObject eo)
            throws OPSException, DataModifiedException {
           
        pUpdateEnterpriseObject(conn,  eo, true);  
    }

    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to update.
     * @param lock true/false to lock the EO object.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
    private void pUpdateEnterpriseObject(Connection conn, 
                                        EnterpriseObject eo, 
                                        boolean lock)
            throws OPSException, DataModifiedException {
                
        try {
            boolean eoToBeDeleted = eo.isRemoved();
            SBR sbr = eo.getSBR();
            boolean sbrToBeDeleted = sbr.isRemoved();
            String euid = eo.getEUID();
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Remove flag for EO with EUID (" + euid + "): " + eoToBeDeleted);
                mLogger.fine("Remove flag for SBR with EUID (" + euid + "): " + sbrToBeDeleted);
            }
                    
            try {
                mLockManager.lock(conn, eo, lock);
            } catch (DataModifiedException e) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Unable to lock record for update: " + e.getMessage());
                }
                throw e;
            }
             
 
            if (sbrToBeDeleted || eoToBeDeleted) {
                if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Removing SBR and SO's of EO with EUID(" + euid + ") due to "
                                      + " the value of the removal flag for EO or SBR");
                }
 
                ArrayList o = (ArrayList) eo.getSystemObjects();
 
                if (o != null) {
                    for (int i = 0; i < o.size(); i++) {
                        SystemObject s = (SystemObject) o.get(i);
                        pDeleteEnterpriseObject(conn, eo.getEUID(),
                                                s.getSystemCode(), s.getLID());
                    }
                }
 
                pDeleteSystemSBR(conn, eo.getEUID(), sbr);
            } else {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Removal flag of EO and SBR false, updating SBR("
                                  + eo.getEUID() + ")");
                }
                pUpdateSystemSBR(conn, eo.getEUID(), sbr);
            }
 
            ArrayList objs = (ArrayList) eo.getSystemObjects();
 
            int count = 0;
            for (int i = 0; objs != null && i < objs.size(); i++) {
                SystemObject obj = (SystemObject) objs.get(i);
 
                if (obj.isRemoved() || eoToBeDeleted) {
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Removal flag of EO or SO enabled, removing SO " 
                                      + "for system code: " + obj.getSystemCode() 
                                      + ", LID: " + obj.getLID());
                    }
 
                    pDeleteEnterpriseObject(conn, eo.getEUID(), 
                                            obj.getSystemCode(), obj.getLID());
                    pDeleteSystemObject(conn, obj);
                } else if (obj.isAdded()) {
                    count++;
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Adding SO with system code: " 
                                      + obj.getSystemCode() + ", LID: " 
                                      + obj.getLID());
                    }
 
                    pAddSystemObject(conn, obj);
                    pAddEnterprise(conn, eo.getEUID(), obj.getSystemCode(),
                        obj.getLID());
                } else {
                    count++;
                    if (mLogger.isLoggable(Level.FINE)) {
                        mLogger.fine("Updating SO for system code: " 
                                      + obj.getSystemCode() + ", LID: " 
                                      + obj.getLID());
                    }
 
                    pUpdateSystemObject(conn, obj);
                }
            }
                
            if ((count == 0 && !sbr.isRemoved()) || eoToBeDeleted) {
                eo.setRemoveFlag(true);
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("System object count: " + count + ", " 
                                  + ", removing SBR for EUID: " + eo.getEUID());
                }
                pDeleteSystemSBR(conn, eo.getEUID(), sbr);
            }
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS647: Could not update " + 
                                            "an EnterpriseObject record: {0}", ex));
        }
    }
    
    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to update.
     * @param sysCode System code of the SystemObject causing the update.
     * @param lid Local ID of the SystemObject causing the update.
     * @param function This is the update function: "Update", "euidActivate", 
     * "euidDeactivate", "lidActivate", or "lidDeactivate".
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     * @return TMResult object containing the results of the transaction.
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
                                           String sysCode, String lid, String function)
            throws OPSException, DataModifiedException {
        
        return updateEnterpriseObject(conn, eo, sysCode, lid, null, function);
    }
    
    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to update.
     * @param sysCode System code of the SystemObject causing the update.
     * @param lid Local ID of the SystemObject causing the update.
     * @param revisionNumber  The revision number of the SBR of the associated EO.
     * @param function This is the update function: "Update", "euidActivate", 
     * "euidDeactivate", "lidActivate", or "lidDeactivate".
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     * @return TMResult object containing the results of the transaction.
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
                                           String sysCode, String lid, 
                                           String revisionNumber, String function)
        throws OPSException, DataModifiedException {
            
        TMResult ret = null;

        try {
            try {
                mLockManager.lock(conn, eo, revisionNumber, true);
            } catch (DataModifiedException e) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Unable to lock record for update: " + e.getMessage());
                }
                throw e;
            }
            // don't lock eo again.
            pUpdateEnterpriseObject(conn, eo, false);
            SBR sbr = eo.getSBR();
            initCUIDManager(conn);            
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction(function);
            tObj.setEUID(eo.getEUID());
            tObj.setSystemCode(sysCode);
            tObj.setLID(lid);
            tObj.setDelta(TransactionLog.getLogs("Enterprise", eo));
            tObj.setSystemUser(sbr.getUpdateUser());
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Update Delta: " + tObj.getDelta());
            }

            tObj.setTimeStamp(new java.util.Date());
            mTransactionObjectDB.create(conn, tObj);

            ret = new TMResult(eo.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS648: Could not update " + 
                                            "an EnterpriseObject record: {0}", ex));
        }

        return ret;
   }

    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to update.
     * @param function This is the update function: "Update", "euidActivate", 
     * "euidDeactivate", "lidActivate", or "lidDeactivate".
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     * @return TMResult object containing the results of the transaction.
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo,
                                           String function)
            throws OPSException, DataModifiedException {
        return updateEnterpriseObject(conn, eo, null, function);        
    }
    
    /**
     * Updates an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to update.
     * @param revisionNumber  The revision number of the SBR of the associated EO.
     * @param function This is the update function: "Update", "euidActivate", 
     * "euidDeactivate", "lidActivate", or "lidDeactivate".
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     * @return TMResult object containing the results of the transaction.
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo,
                                           String revisionNumber, String function)
        throws OPSException, DataModifiedException {
        
        TMResult ret = null;

        try {
            try {
                mLockManager.lock(conn, eo, revisionNumber, true);
            } catch (DataModifiedException e) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Unable to lock record for update: " + e.getMessage());
                }
                throw e;
            }
            // don't lock eo again.
            pUpdateEnterpriseObject(conn, eo, false);

            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction(function);
            tObj.setEUID(eo.getEUID());
            tObj.setDelta(TransactionLog.getLogs("Enterprise", eo));

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Update Delta: " + tObj.getDelta());
            }

            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(eo.getSBR().getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);

            ret = new TMResult(eo.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS649: Could not update " + 
                                            "an EnterpriseObject record: {0}", ex));
        }

        return ret;
    }

    /**
     * Deletes a SystemObject from the database.
     *
     * @param conn JDBC connection.
     * @param so SystemObject to delete.
     * @throws OPSException if an error is encountered.
     */
    private void pDeleteSystemObject(Connection conn, SystemObject so)
            throws OPSException {
        try {
            mSystemObjectDB.remove(conn, mOPSMap, so);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS650: Could not delete " + 
                                            "a SystemObject record: {0}", ex));
        }
    }

    /**
     * Deletes an EnterpriseObject from database
     *
     * @param conn JDBC connection.
     * @param euid EUID to delete.
     * @param systemcode System code of the associated SystemObject.
     * @param lid Local ID of the associated SystemObject.
     * @throws OPSException if an error is encountered.
     */
    private void pDeleteEnterpriseObject(Connection conn, String euid,
        String systemcode, String lid) throws OPSException {
        try {
            mEnterpriseDB.remove(conn, euid, systemcode, lid);
        } catch (OPSException ex) {
            throw ex;
        }
    }

    /**
     * Updates a SystemObject in the database.
     *
     * @param conn JDBC connection.
     * @param so SystemObject to update.
     * @throws OPSException if an error is encountered.
     */
    private void pUpdateSystemObject(Connection conn, SystemObject so)
            throws OPSException {
        try {
            mSystemObjectDB.update(conn, mOPSMap, so);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS651: Could not delete " + 
                                            "a SystemObject record: {0}", ex));
        }
    }

    /**
     * Updates a SystemObject in the database.
     *
     * @param conn JDBC connection.
     * @param so SystemObject to update.
     * @param euid EUID associated with the SystemObject to update.
     * @throws OPSException if an error is encountered.
     */
    private void updateSystemObject(Connection conn, String euid, SystemObject so)
            throws OPSException {
        try {
            pUpdateSystemObject(conn, so);
 
            initCUIDManager(conn);           
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                        "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("Update");
            tObj.setEUID(euid);
            tObj.setSystemCode(so.getSystemCode());
            tObj.setLID(so.getLID());
            tObj.setDelta(TransactionLog.getLogs("Enterprise", so));
            tObj.setTimeStamp(new java.util.Date());
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Update Delta: " + tObj.getDelta());
            }
            tObj.setSystemUser(so.getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
        } catch (Exception e) {
            throw new OPSException(mLocalizer.t("OPS652: Could not update " + 
                                            "a SystemObject record: {0}", e));
        }
    }

    /**
     * Updates a SystemSBR in the database.
     *
     * @param conn JDBC connection.
     * @param euid EUID associated with the SystemSBR.
     * @param sbr SystemSBR to update.
     * @throws OPSException if an error is encountered.
     */
    private void pUpdateSystemSBR(Connection conn, String euid, SBR sbr)
            throws OPSException {
                
        try {
            mSystemSBRDB.update(conn, mOPSMap, euid, sbr);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS653: Could not update " + 
                                            "a SystemSBR record: {0}", ex));
        }
    }

    /**
     * Updates a SystemSBR in the database.
     *
     * @param conn JDBC connection.
     * @param euid EUID
     * @param sbr SystemSBR
     * @throws OPSException if an error is encountered.
     */
    private void updateSystemSBR(Connection conn, String euid, SBR sbr)
            throws OPSException {
        try {
            pUpdateSystemSBR(conn, euid, sbr);
 
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                        "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("Update");
            tObj.setEUID(euid);
            tObj.setDelta(TransactionLog.getLogs("Enterprise", sbr));
            tObj.setTimeStamp(new java.util.Date());
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Update Delta: " + tObj.getDelta());
            }
            tObj.setSystemUser(sbr.getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
        } catch (Exception e) {
            throw new OPSException(mLocalizer.t("OPS654: Could not update " + 
                                            "a SystemSBR record: {0}", e));
        }
    }

    /**
     * Find transaction logs by EUID.  If the starting time or ending time
     * are specified, they are added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @return array of TransactionLogs
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   String euid, 
                                                   Date beginTS, 
                                                   Date endTS) 
            throws OPSException {
                
        TransactionObject[] ret = null;

        try {
            ret = mTransactionObjectDB.findTransactionObject(conn, euid, beginTS, endTS);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS655: Could not retrieve " + 
                                            "Transaction Logs for EUID={0}: {1}", 
                                            euid, ex));
        }
 
        return ret;
    }

    /**
     * Find transaction logs by EUID.  If the starting time is specified, it 
     * is added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param euid EUID
     * @param beginTS start timestamp
     * @param endTS end timestamp
     * @return array of TransactionLogs
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   String euid, 
                                                   Date beginTS) 
            throws OPSException {
                
        TransactionObject[] ret = null;

        try {
            ret = mTransactionObjectDB.findTransactionObject(conn, euid, beginTS);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS656: Could not retrieve " + 
                                            "Transaction Logs for EUID={0}: {1}", 
                                            euid, ex));
        }
 
        return ret;
    }

    /**
     * Find transaction logs by TransactionObject ID.
     * @param conn JDBC connection.
     * @param transId Transaction ID.
     * @return TransactionLog instance.
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject findTransactionLog(Connection conn, String transId)
            throws OPSException {
                
        TransactionObject ret = null;
        try {
            ret = mTransactionObjectDB.get(conn, transId);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS657: Could not retrieve " + 
                                            "Transaction Logs for Transaction " + 
                                            "ID={0}: {1}", 
                                            transId, ex));
        }
        return ret;
    }

    /**
     * Find transaction objects by SystemCode, Local ID, starting time and
     * ending time.
     *
     * @param conn JDBC connection.
     * @param systemcode System code.
     * @param lid Local ID.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @return array of TransactionObject instances..
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   String systemcode, 
                                                   String lid, 
                                                   Date beginTS, 
                                                   Date endTS)
            throws OPSException {
                
        TransactionObject[] ret = null;
 
        try {
            ret = mTransactionObjectDB.findTransactionObject(conn, systemcode,
                                                             lid, beginTS, 
                                                             endTS);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS658: Could not retrieve " + 
                                            "Transaction Logs for SystemCode={0}, " + 
                                            "Local ID={1}: {2}", 
                                            systemcode, lid, ex));
        }
        return ret;
    }

    /**
     * Find transaction objects by TransactionObject, starting time and
     * ending time.
     * @param conn JDBC connection.
     * @param tObj TransactionObject.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @return array of TransactionObject instances.
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   TransactionObject tObj, 
                                                   Date beginTS, 
                                                   Date endTS)
            throws OPSException {
                
        TransactionObject[] ret = null;
 
        try {
            ret = mTransactionObjectDB.findTransactionObject(conn, tObj,
                    beginTS, endTS);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS659: Could not retrieve " + 
                                            "Transaction Logs: {0}", ex));
        }
        return ret;
    }

    /**
     * Find transaction objects by TransactionObject, starting time and
     * ending time.  Ordering can be specified.
     * @param conn JDBC connection.
     * @param tObj TransactionObject.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @param orderBy Order by clause.
     * @return array of TransactionObject instances.
     * @throws OPSException if an error is encountered.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   TransactionObject tObj, 
                                                   Date beginTS, 
                                                   Date endTS, 
                                                   String orderBy)
            throws OPSException {
                
        TransactionObject[] ret = null;
 
        try {
            ret = mTransactionObjectDB.findTransactionObject(conn, tObj,
                    beginTS, endTS, orderBy);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS660: Could not retrieve " + 
                                            "Transaction Logs: {0}", ex));
        }
 
        return ret;
    }    
   
    /**
     * Deletes SystemSBR from database
     *
     * @param conn JDBC connection.
     * @param euid EUID associated with the SystemSBR.
     * @param sbr SystemSBR to be deleted.
     * @throws OPSException if an error is encountered.
     */
    private void pDeleteSystemSBR(Connection conn, String euid, SBR sbr)
            throws OPSException {
        try {
            mSystemSBRDB.remove(conn, mOPSMap, euid, sbr);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS661: Could not delete " + 
                                            "SystemSBR record from the database: {0}",
                                             ex));
        }
    }

    /**
     * Merge two EnterpriseObjects in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @return ret Transaction Manager result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
    public TMResult euidMerge(Connection conn, EnterpriseObject eo1,
                              EnterpriseObject eo2) 
            throws OPSException, DataModifiedException {
                
        TMResult ret = null;
 
        try {
            try {
                mLockManager.lock(conn, eo1, eo2);
            } catch (DataModifiedException e) {
                if (mLogger.isLoggable(Level.FINE)) {
                    mLogger.fine("Unable to lock record for update: " + e.getMessage());
                }
                throw e;
            }
            // don't lock eo1, eo2 again.
            pUpdateEnterpriseObject(conn, eo2, false);
            pUpdateEnterpriseObject(conn, eo1, false);
 
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                        "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("euidMerge");
            tObj.setEUID(eo1.getEUID());
            tObj.setEUID2(eo2.getEUID());
 
            Object[] delta = new Object[2];
            delta[0] = TransactionLog.getLogs("Enterprise", eo1);
            delta[1] = TransactionLog.getLogs("Enterprise", eo2);
            tObj.setDelta(delta);
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(eo1.getSBR().getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
 
            String mergeid = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn,
                                                        "MERGE");
            MergeObject mObj = new MergeObject();
            mObj.setMergeID(mergeid);
            mObj.setKeptEUID(eo1.getEUID());
            mObj.setMergedEUID(eo2.getEUID());
            mObj.setMergeTransactionNumber(transnum);
            
            mMergeObjectDB.create(conn, mObj);
            ret = new TMResult(eo1.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS662: Could not merge two " + 
                                            "EnterpriseObject records in the database: {0}", ex));
        }
 
        return ret;
    }

    /**
     * Merge two EnterpriseObjects in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param srcRevisionNumber  The SBR revision number of the surviving EnterpriseObject.
     * @param destRevisionNumber The SBR revision number of the merged EnterpriseObject.
     * @return ret Transaction Manager result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
     public TMResult euidMerge(Connection conn, EnterpriseObject eo1,
                               EnterpriseObject eo2, String srcRevisionNumber, 
                               String destRevisionNumber) 
            throws OPSException, DataModifiedException {
             
         TMResult ret = null;
 
         try {
             try {
                 mLockManager.lock(conn, eo1, eo2, srcRevisionNumber, destRevisionNumber);
             } catch (DataModifiedException e) {
                 throw new DataModifiedException(mLocalizer.t("OPS663: Could not merge two " + 
                                            "EnterpriseObject records in the database. " +
                                            "The records could not be locked for update: {0}", e));
             }
             // don't lock eo1, eo2 again.
             pUpdateEnterpriseObject(conn, eo2, false);
             pUpdateEnterpriseObject(conn, eo1, false);
 
             initCUIDManager(conn);            
             String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                        "TRANSACTIONNUMBER");
             TransactionObject tObj = new TransactionObject();
             tObj.setTransactionNumber(transnum);
             tObj.setFunction("euidMerge");
             tObj.setEUID(eo1.getEUID());
             tObj.setEUID2(eo2.getEUID());
 
             Object[] delta = new Object[2];
             delta[0] = TransactionLog.getLogs("Enterprise", eo1);
             delta[1] = TransactionLog.getLogs("Enterprise", eo2);
             tObj.setDelta(delta);
             tObj.setTimeStamp(new java.util.Date());
             tObj.setSystemUser(eo1.getSBR().getUpdateUser());
             mTransactionObjectDB.create(conn, tObj);
 
             String mergeid = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                        "MERGE");
             MergeObject mObj = new MergeObject();
             mObj.setMergeID(mergeid);
             mObj.setKeptEUID(eo1.getEUID());
             mObj.setMergedEUID(eo2.getEUID());
             mObj.setMergeTransactionNumber(transnum);
            
             mMergeObjectDB.create(conn, mObj);
            
             ret = new TMResult(eo1.getEUID(), transnum);
         } catch (Exception ex) {
             throw new OPSException(mLocalizer.t("OPS664: Could not merge two " + 
                                            "EnterpriseObject records in the database."));
         }
 
         return ret;
     }

   
    /**
     * Unmerge an EnterpriseObject in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @return ret Transaction Manager result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
     public TMResult euidUnMerge(Connection conn, EnterpriseObject eo1,
                                 EnterpriseObject eo2) 
            throws OPSException, DataModifiedException {
        TMResult ret = null;
 
        try {
            ret = euidUnMerge(conn, null, eo1, eo2); 
        } catch (OPSException ex) {
             throw new OPSException(mLocalizer.t("OPS665: Could not unmerge " + 
                                            "EnterpriseObject records in the database."));
        }
        return ret;
    }

    /**
     * Unmerge two EnterpriseObjects in the database.
     *
     * @param conn JDBC connection.
     * @param transactionID Transaction number of the original merge operation.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @return ret Transaction Manager result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
     public TMResult euidUnMerge(Connection conn, String transactionID, 
                                EnterpriseObject eo1, EnterpriseObject eo2) 
             throws OPSException, DataModifiedException {
                
         TMResult ret = null;
 
         try {        
             pUpdateEnterpriseObject(conn, eo1);
             // there is no entry for eo2 in systemsbr table, so it can't be locked.
             pUpdateEnterpriseObject(conn, eo2, false);
 
             initCUIDManager(conn);
             String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                        "TRANSACTIONNUMBER");
             TransactionObject tObj = new TransactionObject();
             tObj.setTransactionNumber(transnum);
             tObj.setFunction("euidUnMerge");
             tObj.setEUID(eo1.getEUID());
             tObj.setEUID2(eo2.getEUID());
             tObj.setSystemUser(eo1.getSBR().getUpdateUser());
 
             Object[] delta = new Object[2];
             delta[0] = TransactionLog.getLogs("Enterprise", eo1);
             delta[1] = TransactionLog.getLogs("Enterprise", eo2);
             tObj.setDelta(delta);
             tObj.setTimeStamp(new java.util.Date());
             mTransactionObjectDB.create(conn, tObj);
 
             if (eo2 == null || eo2.getEUID() == null)  {
                 mMergeObjectDB.update(conn, transnum, eo1.getEUID(), null);
             } else  {
                 mMergeObjectDB.update(conn, transactionID, transnum, 
                                       eo1.getEUID(), eo2.getEUID());
             }
 
             ret = new TMResult(eo1.getEUID(), transnum);
         } catch (Exception ex) {
             throw new OPSException(mLocalizer.t("OPS667: Could not unmerge " + 
                                            "EnterpriseObject records with the " + 
                                            "Transaction ID={0}: {1}", 
                                            transactionID, ex)); 
         }
         return ret;
     }

    /**
     * Merge two system objects in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Surviving SystemObject.
     * @param lid2 Merged SystemObject.
     * @return ret Transaction Manager Result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
    public TMResult lidMerge(Connection conn, EnterpriseObject eo1,
                             EnterpriseObject eo2, String systemcode, 
                             String lid1, String lid2)
            throws OPSException, DataModifiedException {
                
        TMResult ret = null;
 
        try {
            try {
                mLockManager.lock(conn, eo1, eo2);
            } catch (DataModifiedException e) {
                 throw new OPSException(mLocalizer.t("OPS668: Could not lock " + 
                                                "SystemObject records for a merge (" + 
                                                "System Code={0}, Local ID1={1}, " + 
                                                "Local ID2={2}): {3}", 
                                                systemcode, lid1, lid2, e)); 
            }
            
            if (eo2 != null) {
                pUpdateEnterpriseObject(conn, eo2, false);
            }
 
            pUpdateEnterpriseObject(conn, eo1, false);
 
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                    "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("lidMerge");
            tObj.setEUID(eo1.getEUID());
 
            if (eo2 != null) {
                tObj.setEUID2(eo2.getEUID());
            }
 
            tObj.setSystemCode(systemcode);
            tObj.setLID(lid1);
            tObj.setLID1(lid1);
            tObj.setLID2(lid2);
 
            if (eo2 != null) {
                Object[] delta = new Object[2];
                delta[0] = TransactionLog.getLogs("Enterprise", eo1);
                delta[1] = TransactionLog.getLogs("Enterprise", eo2);
                tObj.setDelta(delta);
            } else {
                Object[] delta = new Object[1];
                delta[0] = TransactionLog.getLogs("Enterprise", eo1);
                tObj.setDelta(delta);
            }
 
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(eo1.getSBR().getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
 
            String mergeid = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                    "MERGE");
            MergeObject mObj = new MergeObject();
            mObj.setMergeID(mergeid);
            mObj.setKeptEUID(eo1.getEUID());
            if (eo2 != null) {
                mObj.setMergedEUID(eo2.getEUID());
            }
            mObj.setMergeTransactionNumber(transnum);
            mMergeObjectDB.create(conn, mObj);
 
            ret = new TMResult(eo1.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS669: Could not merge " + 
                                                "SystemObject records (" + 
                                                "System Code={0}, Local ID1={1}, " + 
                                                "Local ID2={2}): {3}", 
                                                systemcode, lid1, lid2, ex)); 
        }
 
        return ret;
    }
 
    /**
     * Unmerge two system objects in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Surviving SystemObject.
     * @param lid2 Merged SystemObject.
     * @return ret Transaction Manager Result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
     public TMResult lidUnMerge(Connection conn, EnterpriseObject eo1,
                               EnterpriseObject eo2, String systemcode, 
                               String lid1, String lid2)
             throws OPSException, DataModifiedException {
                 
         TMResult ret = null;
         
         try {
             ret = lidUnMerge(conn, eo1,eo2, null, systemcode, lid1, lid2);
        } catch (OPSException ex) {
            throw new OPSException(mLocalizer.t("OPS670: Could not unmerge " + 
                                                "SystemObject records (" + 
                                                "System Code={0}, Local ID1={1}, " + 
                                                "Local ID2={2}): {3}", 
                                                systemcode, lid1, lid2, ex)); 
        } 
        return ret;
    }
 
    /**
     * LID unmerge: update unmerged EnterpriseObject in database and log
     * the action in transaction facility
     *
     * Unmerge two system objects in the database.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param transactionID Transaction ID of the original merge operation.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Surviving SystemObject.
     * @param lid2 Merged SystemObject.
     * @return ret Transaction Manager Result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
     public TMResult lidUnMerge(Connection conn, EnterpriseObject eo1,
                                EnterpriseObject eo2, String transactionID, 
                                String systemcode, String lid1, String lid2)
            throws OPSException, DataModifiedException {
                
        TMResult ret = null;
      
        try {
            pUpdateEnterpriseObject(conn, eo1);
            if (eo2 != null) {
                pUpdateEnterpriseObject(conn, eo2, false);
            }
 
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                    "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("lidUnMerge");
            tObj.setEUID(eo1.getEUID());
 
            if (eo2 != null) {
                tObj.setEUID2(eo2.getEUID());
            }
 
            tObj.setSystemCode(systemcode);
            tObj.setLID(lid1);
            tObj.setLID1(lid1);
            tObj.setLID2(lid2);
 
            if (eo2 != null) {
                Object[] delta = new Object[2];
                delta[0] = TransactionLog.getLogs("Enterprise", eo1);
                delta[1] = TransactionLog.getLogs("Enterprise", eo2);
                tObj.setDelta(delta);
            } else {
                Object[] delta = new Object[1];
                delta[0] = TransactionLog.getLogs("Enterprise", eo1);
                tObj.setDelta(delta);
            }
 
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(eo1.getSBR().getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
 
            if (eo2 == null)  {
                mMergeObjectDB.update(conn, transactionID, transnum, 
                                      eo1.getEUID(), null);
            }  else  {
                mMergeObjectDB.update(conn, transnum, eo1.getEUID(), 
                                      eo2.getEUID());
            }
            
            ret = new TMResult(eo1.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS671: Could not unmerge " + 
                                                "SystemObject records (" + 
                                                "System Code={0}, Local ID1={1}, " + 
                                                "Local ID2={2}, Transaction ID={3}): {4}", 
                                                systemcode, lid1, lid2, transactionID, ex)); 
        }
 
        return ret;
    }

    /**
     * Transfer a SystemObject from one EnterpriseObject to another.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param transactionID Transaction ID of the original merge operation.
     * @param systemcode System code for both SystemObjects.
     * @param lid Local ID of the transferred SystemObject.
     * @return ret Transaction Manager Result.
     * @throws OPSException if an error other than a DataModifiedException 
     * is encountered. 
     * @throws DataModifiedException if the record has been modified by 
     * another user.
     */
    public TMResult lidTransfer(Connection conn, EnterpriseObject eo1,
                                EnterpriseObject eo2, String systemcode, 
                                String lid)
            throws OPSException, DataModifiedException {
        TMResult ret = null;
  
        try {
            mLockManager.lock(conn, eo1, eo2);
        } catch (DataModifiedException e) {
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Unable to lock record for update: " + e.getMessage());
            }
            throw new OPSException(mLocalizer.t("OPS672: Could not lock " + 
                                                "EnterpriseObject records for " +
                                                "transferring SystemObjects (" + 
                                                "System Code={0}, Local ID={1}): {2}", 
                                                systemcode, lid, e)); 
        }
        try {
            pUpdateEnterpriseObject(conn, eo2, false);
            pUpdateEnterpriseObject(conn, eo1, false);
 
            initCUIDManager(conn);
            String transnum = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                    "TRANSACTIONNUMBER");
            TransactionObject tObj = new TransactionObject();
            tObj.setTransactionNumber(transnum);
            tObj.setFunction("lidTransfer");
            tObj.setEUID(eo1.getEUID());
            tObj.setEUID2(eo2.getEUID());
            tObj.setSystemCode(systemcode);
            tObj.setLID(lid);
            tObj.setLID2(lid);
  
            Object[] delta = new Object[2];
            delta[0] = TransactionLog.getLogs("Enterprise", eo1);
            delta[1] = TransactionLog.getLogs("Enterprise", eo2);
            tObj.setDelta(delta);
            tObj.setTimeStamp(new java.util.Date());
            tObj.setSystemUser(eo1.getSBR().getUpdateUser());
            mTransactionObjectDB.create(conn, tObj);
  
            String mergeid = com.sun.mdm.index.idgen.CUIDManager.getNextUID(conn, 
                                                    "MERGE");
            MergeObject mObj = new MergeObject();
            mObj.setMergeID(mergeid);
            mObj.setKeptEUID(eo1.getEUID());
            mObj.setMergedEUID(eo2.getEUID());
            mObj.setMergeTransactionNumber(transnum);
            
            mMergeObjectDB.create(conn, mObj);
 
            ret = new TMResult(eo1.getEUID(), transnum);
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS673: Could not transfer " + 
                                                "SystemObject records (" + 
                                                "System Code={0}, Local ID={1}): {2}", 
                                                systemcode, lid, ex)); 
        }
  
        return ret;
    }

    /**
     * Get the child node of an ObjectNode as specified by the path.
     *
     * @param path Path of the child node.
     * @param node Node.
     */
    private ObjectNode getChildNode(String path, ObjectNode node) {
        StringTokenizer token = new StringTokenizer(path, ".");
        ObjectNode currentNode = null;
 
        try {
            currentNode = node;
 
            int lastnodepos = path.lastIndexOf(".");
            String root = node.pGetTag();
            token.nextToken();
 
            while (token.hasMoreTokens()) {
                String t = token.nextToken();
                int pos = -1;
 
                if ((t.indexOf("[") > 0) && (t.indexOf("]") > 0)) {
                    int startBracket = t.indexOf('[');
                    int endBracket = t.indexOf(']');
                    String index = t.substring(startBracket + 1, endBracket);
                    pos = (new Integer(index)).intValue();
                    t = t.substring(0, startBracket);
                } else {
                    pos = 0;
                }
 
                ArrayList childList = currentNode.pGetChildren(t);
                currentNode = (ObjectNode) childList.get(pos);
            }
        } catch (Exception ex) {
            mLogger.warn(mLocalizer.x("OPS017: TransactionMgr could not retrieve " 
                                      + "a child node: {0}", ex.getMessage()));
        }
 
        return currentNode;
    }

    /**
     * Apply a delta to an EnterpriseObject to obtain the image
     * before the update was made.
     *
     * @param eo EnterpriseObject to which the delta is applied.
     * @throws OPSException if an error is encountered. 
     * @return image of the EnterpriseObject before the update was made.
     */
    public EnterpriseObject getBeforeImage(EnterpriseObject eo) 
            throws OPSException{
        EnterpriseObject beforeEO = null;
        Object delta = TransactionLog.getLogs("Enterprise", eo);
        try{
            beforeEO = applyDelta(eo, delta);
        } catch (Exception ex){
            throw new OPSException(mLocalizer.t("OPS674: Could not retrieve " + 
                                                "the \"before\" image for an " + 
                                                "EnterpriseObject: {0}", ex)); 
        }
        return beforeEO;

    }
   
    /**
     * Apply the delta to an EnterpriseObject to obtain the image
     * before the update was made.
     *
     * @param eo EnterpriseObject to which the delta is applied.
     * @param delta Delta object listing the updates that were made.
     * @throws OPSException if an error other than an EPath error is encountered. 
     * @throws EPathException if an EPath error is encountered. 
     * @return image of the EnterpriseObject before the update was made.
     */
    private EnterpriseObject applyDelta(EnterpriseObject eo, Object delta) 
            throws ObjectException, EPathException {
        return TransactionLog.applyDelta(eo, delta);
    }

    /**
     * Clean up matched merge and unmerge operations from the array of 
     * TransactionObjects.
     * 
     * @param tobjs Array of TransactionObject to clean up.
     * @throws OPSException if an error is encountered. 
     * @return array of cleaned up TransactionObjects.
     */
    private TransactionObject[] matchMerges(TransactionObject[] tObjs) 
            throws ObjectException {
        ArrayList toRemove = null;
        TransactionObject[] ret = null;
 
        if (tObjs != null && tObjs.length > 0) {        
            for (int i = 0, j= i + 1; i < tObjs.length - 1 && j < tObjs.length - 1; i++, j = i + 1) {
                if (tObjs[i].getFunction().equals("euidUnMerge")) {                        
                        if (tObjs[j].getEUID().equals(tObjs[i].getEUID())
                            && tObjs[j].getEUID2() != null 
                            && tObjs[j].getEUID2().equals(tObjs[i].getEUID2())
                            && tObjs[j].getFunction().equals("euidMerge")) {
                                if (toRemove == null) {
                                    toRemove = new ArrayList();
                                }
                                toRemove.add(new Integer(i));
                                toRemove.add(new Integer(j));
                        }
                } 
                if (tObjs[i].getFunction().equals("lidUnMerge")) { 
                    return tObjs;
                }
                if (tObjs[i].getFunction().equals("lidMerge")) { 
                    return tObjs;
                }
                
                if (tObjs[i].getFunction().equals("lidUnMerge")) {
                    if (tObjs[j].getEUID().equals(tObjs[i].getEUID())
                            && tObjs[j].getEUID2() != null 
                            && tObjs[j].getEUID2().equals(tObjs[i].getEUID2())
                            && tObjs[j].getLID() != null
                            && tObjs[j].getLID().equals(tObjs[i].getLID())
                            && tObjs[j].getLID2() != null
                            && tObjs[j].getLID2().equals(tObjs[i].getLID2())
                            && tObjs[j].getFunction().equals("lidMerge")) {
                        if (toRemove == null) {
                            toRemove = new ArrayList();
                        }
                        toRemove.add(new Integer(i));
                        toRemove.add(new Integer(j));
                    }
                } 
            }
        }
        
        if (toRemove != null) {        
            ret = new TransactionObject[tObjs.length - toRemove.size()];
 
            for (int i = 0, j = 0; i < tObjs.length; i++) {
                if (!toRemove.contains(new Integer(i))) {
                    ret[j++] = tObjs[i];
                }
            }
        } else {
            ret = tObjs;
        }
        return ret;
    }
 
    /**
     * Recover object by TransactionObject.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to recover.
     * @param mergedeo Merged EnterpriseObject.
     * @param tObj TransactionObject.
     * @param calcAfter This is true if the "after" image is to be calculated.
     * @throws  ObjectException if an ObjectException is encountered. 
     * @throws  EPathException if an EPathException is encountered. 
     * @throws OPSException if any other type of error is encountered. 
     * @return RecreateResult object containing the results of the operation.
     */
    private RecreateResult recoverObject(Connection conn, 
                                         EnterpriseObject eo, 
                                         EnterpriseObject mergedeo, 
                                         TransactionObject tranObject, 
                                         boolean calcAfter) 
            throws ObjectException, EPathException, OPSException {
                
        RecreateResult ret = null;

        try {
            EnterpriseObject beforeEO1 = null;
            EnterpriseObject beforeEO2 = null;            
            
            EnterpriseObject afterEO = null;
            EnterpriseObject afterEO2 = null;
            
            String function = tranObject.getFunction();
            String euid = tranObject.getEUID();
            String lid = tranObject.getLID();
            String euid2 = tranObject.getEUID2();  
            String systemcode = tranObject.getSystemCode();
            
            if (function.equals("Add")) {
                afterEO = eo;                
                ret = new RecreateResult(afterEO, null, null, null);
                return ret;
            }
            
            TransactionObject tObjWithBlob 
                    = mTransactionObjectDB.get(conn, tranObject.getTransactionNumber());
                       
            if (function.equals("Update")
                    || function.equals("euidActivate")
                    || function.equals("euidDeactivate")) {
                
                if ( calcAfter && eo != null ) {
                    afterEO = (EnterpriseObject)eo.copy();    
                }
                
                beforeEO1 = eo;
                Object delta = tObjWithBlob.getDelta();
                beforeEO1 = applyDelta(beforeEO1, delta);
                               
                ret = new RecreateResult(afterEO, null, beforeEO1, null); 
                
                return ret;
                
            } 
            
            if (function.equals("euidUnMerge")) {
                if ( calcAfter ) {
                    if ( eo != null ) {
                        afterEO = (EnterpriseObject)eo.copy();
                    }
                    afterEO2 = (EnterpriseObject)mergedeo;                            
                }
 
                beforeEO1 = eo;
                Object[] deltaList = (Object[]) tObjWithBlob.getDelta();
                Object survivingDelta = deltaList[0];
                
                if (survivingDelta != null) {
                    beforeEO1 = applyDelta(beforeEO1, survivingDelta);
                }
                ret = new RecreateResult(afterEO, afterEO2, beforeEO1, null);
               
               return ret;
            }
            
            if (function.equals("lidUnMerge") 
                    || function.equals("euidMerge") 
                    || function.equals("lidMerge") 
                    || function.equals("lidTransfer")) {
                
                if ( calcAfter && eo != null ) {
                    afterEO = (EnterpriseObject) eo.copy();
                }                    
                
                if ( !tranObject.getEUID().equals(tranObject.getEUID2()) ) {
                    if ( mergedeo != null ) {
                        if ( calcAfter ) {
                            afterEO2 = (EnterpriseObject)mergedeo.copy();
                        }                    
                        beforeEO2 = (EnterpriseObject)mergedeo;                        
                    }
                }
                
                beforeEO1 = eo;
                Object[] deltaList = (Object[]) tObjWithBlob.getDelta();
                Object survivingDelta = deltaList[0];
                Object mergedDelta = null;
               
               if (deltaList.length > 1) {
                   mergedDelta = deltaList[1];
                }
                
                if (survivingDelta != null) {
                    beforeEO1 = applyDelta(beforeEO1, survivingDelta);
                }
                
                if (mergedDelta != null) {
                    ArrayList list = (ArrayList) mergedDelta;
                    TransactionLog log = (TransactionLog) list.get(0);
                    Object tmpEo = log.getValue();
                    
                    //  If the transaction log value (tmpEo) is not an EnterpriseObject, then
                    //  retrieve beforeEO2 from the database.  This can occur if: eo1 has so1 and so2;
                    //  eo2 has so2.  eo1.so2 is merged with eo2.so2.  The mergedDelta would then
                    //  be a java.sql.Timestamp because it looks like an update even though the
                    //  function is a merge/unmerge.
                    
                    if (beforeEO2 == null) {
                        
                        beforeEO2 = applyDelta(beforeEO2, mergedDelta);
                        SBR sbrTmp = beforeEO2.getSBR();
                        sbrTmp.setAddFlag(true);
                        ArrayList soTmps = (ArrayList) beforeEO2.getSystemObjects();
                        for (int i = 0; i < soTmps.size(); i++) {
                            SystemObject soTmp = (SystemObject) soTmps.get(i);
                            soTmp.setAddFlag(true);
                        }
                        
                    } else {
                        beforeEO2 = applyDelta(beforeEO2, mergedDelta);
                    }
                }        
                
                ret = new RecreateResult(afterEO, afterEO2, beforeEO1, beforeEO2);
            }
        } catch (ObjectException ex) {
            throw new OPSException(mLocalizer.t("OPS675: Transaction Manager could " + 
                                                "not recover a transaction object: {0}",
                                                ex)); 
        }    
        return ret;
    }


    /**
     * Recreate an object by transaction ID.
     *
     * @param conn JDBC connection.
     * @param transactionnumber Transaction ID.
     * @throws OPSException if an error is encountered. 
     * @return RecreateResult object containing the results of the operation.
     */
    public RecreateResult recreateObject(Connection conn,
                                         String transactionnumber) 
            throws OPSException {

        TransactionObject tranObject = null;             
        TransactionObject[] tranObjects = null;        
        TransactionObjectList tranObjectList = null;       

        try {        
            tranObject = mTransactionObjectDB.get(conn, transactionnumber);
            tranObjects 
                = mTransactionObjectDB.findTransactionObjectForRecovery(conn, 
                                                                        tranObject.getEUID(), 
                                                                        tranObject.getTimeStamp());
            tranObjectList 
                = new TransactionObjectList(tranObject.getEUID(), 
                                            transactionnumber, 
                                            tranObjects);
            
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS676: Transaction Manager could " + 
                                                "not recreate a transaction object: {0}",
                                                ex)); 
        }         
        Map tranMap = new HashMap();
        return recreateObject(conn, tranObjectList, true, tranMap);
    }
   
    /**
     * Recreate object by a list of TransactionObject instances.
     *
     * @param conn JDBC connection.
     * @param tranObjectList TransactionObject list.
     * @param calcAfter This is true if the "after" image is to be calculated.
     * @param tranMap Map of TransactionSummary objects with the 
     * transaction ID as the key.
     * @throws  EPathException if an EPathException is encountered. 
     * @throws OPSException if any other type of error is encountered. 
     * @return RecreateResult object containing the results of the operation.
     */
    private RecreateResult recreateObject(Connection conn,
                                          TransactionObjectList tranObjectList, 
                                          boolean calcAfter, 
                                          Map tranMap) 
            throws OPSException {
             
        RecreateResult ret = null;

        String euid = null;
        String transactionNumber = null;
    
        TransactionObject survivorTranObject = null;
        TransactionObject mergedTranObject = null;
        RecreateResult survivorResult = null;
        RecreateResult mergedResult = null;
        EnterpriseObject survivorObject = null;
        EnterpriseObject mergedObject = null;
        TransactionObjectList survivorTranObjectList = null;           
        TransactionObjectList mergedTranObjectList = null;           
   
        TransactionObject currentTranObject = null;
       
        try {            
            currentTranObject = tranObjectList.getCurrentTransaction();
            transactionNumber = currentTranObject.getTransactionNumber();
           
            // Survivor Object Related Logic 
            euid = currentTranObject.getEUID();
           
            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("Recreating transaction for Transaction ID: " + transactionNumber);
            }
                          
            if ( currentTranObject.getRecoverObject().equals(currentTranObject.RECOVER_SURVIVOR)  ) { 
                survivorTranObjectList = tranObjectList;    
            }
            else { 
                TransactionObject[] tranObjects  
                    = mTransactionObjectDB.findTransactionObjectForRecovery(conn,
                                                                            euid,
                                                                            currentTranObject.getTimeStamp());
                survivorTranObjectList = new TransactionObjectList(euid, 
                                                                   transactionNumber, 
                                                                   tranObjects);                                
            }
           
            survivorTranObjectList.moveNext();
            survivorTranObject = survivorTranObjectList.getCurrentTransaction(); 
           
            if ( survivorTranObject == null ) { 
                survivorObject = getEnterpriseObject(conn, euid);
            }
            else { 
            // Check if the result is already existing in the cache 
            TransactionSummary ts 
                = (TransactionSummary)tranMap.get(survivorTranObject.getTransactionNumber());

            if ( ts == null ) {
                survivorResult = recreateObject(conn, survivorTranObjectList, true, tranMap );                                 
                ts = new TransactionSummary(survivorTranObject);
                ts.setEnterpriseObjectHistory(new EnterpriseObjectHistory(survivorResult));
                ts.setValidTransaction(true);
                tranMap.put(survivorTranObject.getTransactionNumber(),ts);    
            } 
            else {                        
                survivorResult  = new RecreateResult();
                survivorResult.setBeforeEO1(ts.getEnterpriseObjectHistory().getBeforeEO1());
                survivorResult.setBeforeEO2(ts.getEnterpriseObjectHistory().getBeforeEO2());        
                survivorResult.setAfterEO(ts.getEnterpriseObjectHistory().getAfterEO());        
                survivorResult.setAfterEO2(ts.getEnterpriseObjectHistory().getAfterEO2());
            }
            
               if ( euid.equals(survivorTranObject.getEUID())) {
                   survivorObject = survivorResult.getBeforeEO1();
               }
               else {
                   survivorObject = survivorResult.getBeforeEO2();
               }
            }
           
           // Merged Object Related Logic 
           if (currentTranObject.getEUID2() != null 
                && !currentTranObject.getEUID().equals(currentTranObject.getEUID2()) 
                && ("euidUnMerge".equals(currentTranObject.getFunction()) 
                    || "lidUnMerge".equals(currentTranObject.getFunction())  
                    || "lidMerge".equals(currentTranObject.getFunction())    
                    || "lidTransfer".equals(currentTranObject.getFunction()))) {
               
            euid = currentTranObject.getEUID2();
               
            if (!currentTranObject.getRecoverObject().equals(currentTranObject.RECOVER_SURVIVOR)) { 
                mergedTranObjectList = tranObjectList;    
            }
            else { 
                TransactionObject[] tranObjects  
                    = mTransactionObjectDB.findTransactionObjectForRecovery(conn, 
                                                                            euid, 
                                                                            currentTranObject.getTimeStamp());
                mergedTranObjectList = new TransactionObjectList(euid, 
                                                                 transactionNumber, 
                                                                 tranObjects);                                
            }

            mergedTranObjectList.moveNext();
            mergedTranObject = mergedTranObjectList.getCurrentTransaction();
               
            if ( mergedTranObject == null ) {                       
                   // In case of a LID Merge and LID Transfer, it is possible 
                   // that the source did not survived if it was the last 
                   // system record in the eo.  
                   mergedObject = getEnterpriseObject(conn, euid);
                   
                   if ( mergedObject == null ) {
                       // Should only happen in case of lidMerge and lid Transfer
                       if (!( "lidMerge".equals(currentTranObject.getFunction()) 
                              || "lidTransfer".equals(currentTranObject.getFunction()))) {
                           throw new OPSException(mLocalizer.t("OPS677: TMerged Object " + 
                                                "must exist for a unmerge operation.")); 
                       }
                   }
               }
            else { 
                // Check if the result is already existing in the cache 
                TransactionSummary ts 
                    = (TransactionSummary)tranMap.get(mergedTranObject.getTransactionNumber());

                if ( ts == null ) {
                    mergedResult = recreateObject(conn, mergedTranObjectList, true, tranMap);
                    ts = new TransactionSummary(mergedTranObject);
                    ts.setEnterpriseObjectHistory(new EnterpriseObjectHistory(mergedResult));
                    ts.setValidTransaction(true);
                    tranMap.put(mergedTranObject.getTransactionNumber(),ts);
                } 
                else {
                    mergedResult = new RecreateResult();
                    mergedResult.setBeforeEO1(ts.getEnterpriseObjectHistory().getBeforeEO1());
                    mergedResult.setBeforeEO2(ts.getEnterpriseObjectHistory().getBeforeEO2());        
                    mergedResult.setAfterEO(ts.getEnterpriseObjectHistory().getAfterEO());        
                    mergedResult.setAfterEO2(ts.getEnterpriseObjectHistory().getAfterEO2());
                }
                   
                if ( euid.equals(mergedTranObject.getEUID())) {
                    mergedObject = mergedResult.getBeforeEO1();
                }
                else {
                    mergedObject = mergedResult.getBeforeEO2();
                }
            }
        }           
        // Now call the recoverObject on the current transaction            
        ret =  recoverObject( conn, survivorObject, mergedObject, currentTranObject, calcAfter );
        
        } catch (Exception ex) {
            throw new OPSException(mLocalizer.t("OPS678: Transaction Manager could " + 
                                                "not recreate a transaction object: {0}",
                                                ex)); 
        }            
        return ret;
   } 
}
