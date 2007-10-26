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

import javax.naming.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.DataModifiedException;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;
import com.sun.mdm.index.util.ConnectionUtil;

/**
 *
 * @author  sdua
 */
 class LockManager {
     
    private final Logger mLogger = LogUtil.getLogger(this);
    
    /**
     * For SQL Server, with clause will be used for locking.
     */
    private String mWithClause = "";

    /**
     * For Oracle, for update clause will be used for locking.
     */
    private String mForUpdateClause = "";
    
    /**
     * Flag to indicate if mWithClause/mForUpdateClause has been initialized.
     */
    private boolean mIsInitialized = false;

    /**
     * SQL statement for locking SBR using two EO.
     */
    private String mDoubleLockStmt = null;

    /**
     * SQL statement for locking SBR using an EO.
     */
    private String mSingleLockStmt = null;
    
    /** Creates a new instance of LockManager */
    LockManager() {
    }
    
    /**
     * Lock the EnterpriseObject. Lock is on the SystemSBR table for 
     * the given EUID.
     *
     * @param con JDBC connection handle.
     * @param eo EnterpriseObject to lock.
     * @param lock Lock flag.
     * @throws DataModifiedException if the record could not be locked.
     */
    void lock(Connection con, EnterpriseObject eo, boolean lock)
            throws DataModifiedException {
        try {
            if ((!lock) || (eo == null) || (con.getAutoCommit())) {
                return; // no locking, no revision update
            }
            SBR sbr = eo.getSBR();
            String euid = eo.getEUID();
            Integer revisionNumber = sbr.getRevisionNumber();
            lockSBR(con, euid, revisionNumber);
            sbr.setRevisionNumber(sbr.getRevisionNumber().intValue() + 1);
        } catch (ObjectException ex) {
            throw new DataModifiedException(ex.getMessage());
        } catch (SQLException se) {
            throw new DataModifiedException(se.getMessage());
        } 
    }
    
    /**
     * Lock the EnterpriseObject. Lock is on the SystemSBR table for the given EO.
     *
     * @param con JDBC connection handle.
     * @param eo Enterprise Object to lock.
     * @param revisionNumber SBR revision number.
     * @param lock Lock flag
     * @throws DataModifiedException if the record could not be locked.
     */
    void lock(Connection con, EnterpriseObject eo, String revisionNumber,
              boolean lock) throws DataModifiedException {
        try {
            if ((!lock) || (eo == null) || (con.getAutoCommit())) {
                return; // no locking, no revision update
            }
            SBR sbr = eo.getSBR();
            String euid = eo.getEUID();
            if (revisionNumber != null) {
                lockSBR(con, euid, revisionNumber);
            } else {
                Integer curRevisionNumber = sbr.getRevisionNumber();
                lockSBR(con, euid, curRevisionNumber);
            }
            sbr.setRevisionNumber(sbr.getRevisionNumber().intValue() + 1);
        } catch (ObjectException ex) {
            throw new DataModifiedException(ex.getMessage());
        } catch (SQLException se) {
            throw new DataModifiedException(se.getMessage());
        } 
    }
    
    /**
     * Lock two EnterpriseObjects. Lock is on the SystemSBR table for the given EO.
     *
     * @param con JDBC connection handle.
     * @param eo1 First Enterprise Object to lock.
     * @param eo2 Second Enterprise Object to lock.
     * @param srcRevisionNumber SBR revision number of the source (eo1).
     * @param destRevisionNumber SBR revision number of the destination (eo2).
     * @param lock Lock flag.
     * @throws DataModifiedException if the record could not be locked.
     */
    void lock(Connection con, EnterpriseObject eo1, EnterpriseObject eo2,
              String srcRevisionNumber, String destRevisionNumber) 
            throws DataModifiedException {
        try {
            if ((eo1 == null && eo2 == null) || (con.getAutoCommit())) {
                return;
            } else if (eo1 == null) {
                lock(con, eo2, srcRevisionNumber, true);    
                return;
            } else if (eo2 == null) {
                lock(con, eo1, destRevisionNumber, true);
                return;
            }
            SBR sbr1 = eo1.getSBR();
            String euid1 = eo1.getEUID();
            SBR sbr2 = eo2.getSBR();
            String euid2 = eo2.getEUID();

            if (srcRevisionNumber != null && destRevisionNumber != null) {
                lockSBR(con, euid1, destRevisionNumber, euid2,
                        srcRevisionNumber);
                sbr1.setRevisionNumber(Integer.parseInt(destRevisionNumber) + 1);
                sbr2.setRevisionNumber(Integer.parseInt(srcRevisionNumber) + 1);
            } else {
                Integer revisionNumber1 = sbr1.getRevisionNumber();
                sbr2 = eo2.getSBR();
                euid2 = eo2.getEUID();
                Integer revisionNumber2 = sbr2.getRevisionNumber();
                lockSBR(con, euid1, revisionNumber1, euid2, revisionNumber2);
                sbr1.setRevisionNumber(sbr1.getRevisionNumber().intValue() + 1);
                sbr2.setRevisionNumber(sbr2.getRevisionNumber().intValue() + 1);
            }
        } catch (ObjectException ex) {
            throw new DataModifiedException(ex.getMessage());
        } catch (SQLException se) {
            throw new DataModifiedException(se.getMessage());
        } 
    }
    
    
    /**
     * Lock two EnterpriseObjects. Lock is on the SystemSBR table for the given EO.
     *
     * @param con JDBC connection handle.
     * @param eo1 First Enterprise Object to lock.
     * @param eo2 Second Enterprise Object to lock.
     * @throws DataModifiedException if the record could not be locked.
     */
    void lock(Connection con, EnterpriseObject eo1, EnterpriseObject eo2) 
            throws DataModifiedException {
        try {
            if ((eo1 == null && eo2 == null) || (con.getAutoCommit())) {
                return;
            } else if (eo1 == null) {
                lock(con, eo2, true);
                return;
            } else if (eo2 == null) {
                lock(con, eo1, true);
                return;
            }
            SBR sbr1 = eo1.getSBR();
            String euid1 = eo1.getEUID();
            Integer revisionNumber1 = sbr1.getRevisionNumber();
            SBR sbr2 = eo2.getSBR();
            String euid2 = eo2.getEUID();
            Integer revisionNumber2 = sbr2.getRevisionNumber();
            lockSBR(con, euid1, revisionNumber1, euid2, revisionNumber2);
            sbr1.setRevisionNumber(sbr1.getRevisionNumber().intValue() + 1);
            sbr2.setRevisionNumber(sbr2.getRevisionNumber().intValue() + 1);
        } catch (ObjectException ex) {
           throw new DataModifiedException(ex.getMessage());
        } catch (SQLException se) {
           throw new DataModifiedException(se.getMessage());
        } 
    }
    
    /**
     * Lock an EnterpriseObject. Lock is on the SystemSBR table for the given euid.
     *
     * @param con JDBC connection handle.
     * @param euid EUID of the Enterprise Object to lock.
     * @param revisionNumber SBR revision number. 
     * @throws DataModifiedException if the record could not be locked.
     */
    private void lockSBR(Connection con, String euid, String revisionNumber)
            throws DataModifiedException {    
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (mLogger.isDebugEnabled()) {
            mLogger.debug("locking SystemSBR euid:" + euid + " rv#:"
                          + revisionNumber);
        }
        try {
            ps = con.prepareStatement(getSingleLockSBRStmt());
            ps.setString(1, euid);
            ps.setInt(2, Integer.parseInt(revisionNumber));
                   
            rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {     
                count++;
            }
            rs.close();
            ps.close();
            if (count != 1) {
                throw new DataModifiedException("Record with EUID: " + euid 
                                                + " has been modified by another user.");
            }
        } catch (SQLException e) {
            throw new DataModifiedException(e.getMessage());
        }
    }
    
    
    /**
     * Lock an EnterpriseObject. Lock is on the SystemSBR table for the given euid.
     *
     * @param con JDBC connection handle.
     * @param euid EUID of the Enterprise Object to lock.
     * @throws DataModifiedException if the record could not be locked.
     */
    private void lockSBR(Connection con, String euid, Integer revisionNumber)
            throws DataModifiedException {    
    
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (mLogger.isDebugEnabled()) {
            mLogger.debug("locking SystemSBR euid:" + euid + " rv#:"
                    + revisionNumber);
        }
        try {
                
            ps = con.prepareStatement(getSingleLockSBRStmt());
            ps.setString(1, euid);
            ps.setInt(2, revisionNumber.intValue());
                   
            rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {     
                count++;
            }
            rs.close();
            ps.close();
            if (count != 1) {
                throw new DataModifiedException("Record with EUID: " + euid 
                                                + " has been modified by another user.");
            }
        } catch (SQLException e) {
            throw new DataModifiedException(e.getMessage());
        }
    }
    
    /**
     * Lock two EnterpriseObjects. Lock is on the SystemSBR table for the given EUID's.
     *
     * @param con  JDBC connection handle.
     * @param euid1  EUID of the first Enterprise Object to lock.
     * @param revisionNumber1 SBR revision number of the first Enterprise Object.
     * @param euid2  EUID of the Enterprise Object to lock.
     * @param revisionNumber2 SBR revision number of the second Enterprise Object.
     * @throws DataModifiedException if the record could not be locked.
     */
    private void lockSBR(Connection con, String euid1, String revisionNumber1,
                         String euid2, String revisionNumber2) 
            throws DataModifiedException {
    
        try {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("LM:locking SystemSBR euid1:" + euid1 + " rv#1:"
                        + revisionNumber1 + "and euid2:" + euid2 + " rv#2:"
                        + revisionNumber2);
            }

            PreparedStatement ps = con.prepareStatement(getDoubleLockSBRStmt());
            ps.setString(1, euid1);
            ps.setInt(2, Integer.parseInt(revisionNumber1));
            ps.setString(3, euid2);
            ps.setInt(4, Integer.parseInt(revisionNumber2));
            
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {     
                count++;
            }

            rs.close();
            ps.close();
            if (count != 2) {
                throw new DataModifiedException(
                        "Record has been modified by another user.");
            }
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("LM:locking successful");
            }
        } catch (SQLException e) {
             throw new DataModifiedException(e.getMessage());
        }

    }
     
    /**
     * Lock two EnterpriseObjects. Lock is on the SystemSBR table for the given EUID's.
     *
     * @param con  JDBC connection handle.
     * @param euid1  EUID of the first Enterprise Object to lock.
     * @param revisionNumber1 SBR revision number of the first Enterprise Object.
     * @param euid2  EUID of the Enterprise Object to lock.
     * @param revisionNumber2 SBR revision number of the second Enterprise Object.
     * @throws DataModifiedException if the record could not be locked.
     */
     private void lockSBR(Connection con, String euid1, Integer revisionNumber1,
                          String euid2, Integer revisionNumber2) 
            throws DataModifiedException {
    
        try {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("LM:locking SystemSBR euid1:" + euid1 + " rv#1:"
                        + revisionNumber1 + "and euid2:" + euid2 + " rv#2:"
                        + revisionNumber2);
            }

            PreparedStatement ps = con.prepareStatement(getDoubleLockSBRStmt());
            ps.setString(1, euid1);
            ps.setInt(2, revisionNumber1.intValue());
            ps.setString(3, euid2);
            ps.setInt(4, revisionNumber2.intValue());
            
            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {     
                count++;
            }

            rs.close();
            ps.close();
            if (count != 2) {
                throw new DataModifiedException(
                        "Record has been modified by another user.");
            }
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("LM:locking successful");
            }
        } catch (SQLException e) {
             throw new DataModifiedException(e.getMessage());
        }
    }
     
    
    /**
     * Get the SQL statment for locking SystemSBR.
     * 
     * @return a SQL statement in String
     */
    private String getSingleLockSBRStmt() {

        if (null == mSingleLockStmt) {
            initClauses();
            mSingleLockStmt = "SELECT s.euid FROM SBYN_SystemSBR s "
                    + mWithClause
                    + " WHERE s.EUID = ? AND s.RevisionNumber = ? "
                    + mForUpdateClause;
        }

        return mSingleLockStmt;
    }

    /**
     * Get the SQL statment for locking SystemSBR. It uses 2 sets of EUID and
     * RevisionNumbers
     * 
     * @return a SQL statement in String
     */
    private String getDoubleLockSBRStmt() {

        if (null == mDoubleLockStmt) {
            initClauses();
            mDoubleLockStmt = "SELECT s.euid FROM SBYN_SystemSBR s "
                    + mWithClause
                    + " WHERE ( s.EUID = ? AND s.RevisionNumber = ?) OR ( s.EUID = ? AND s.RevisionNumber = ?)"
                    + mForUpdateClause;
        }

        return mDoubleLockStmt;
    }

    /**
     * Depending on the database, different clauses are used to lock the record.
     * Oracle use "FOR UPDATE" clause while SQL Server use WITH (ROWLOCK,
     * UPDLOCK) clause.
     * 
     * It cannot be called in the constructor since TransactionMgrBean has not
     * established the connection when it instantiates LockManager.
     */
    private void initClauses() {
        if (!mIsInitialized) {
            if (ConnectionUtil.getDBProductID() == ConnectionUtil.DB_ORACLE) {
                mForUpdateClause = " FOR UPDATE ";
            } else if (ConnectionUtil.getDBProductID() == ConnectionUtil.DB_SQLSERVER) {
                mWithClause = " WITH (ROWLOCK, UPDLOCK) ";
            }
            mIsInitialized = true;
        }

    }
}
