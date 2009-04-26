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

import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.Localizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * @author gzheng
 * OPS class for TransactionLog objects
 */
public final class TransactionObjectDB extends ObjectPersistenceService {

    private static String mSelectString = null;     // Select string

    private static String mFindByEUID = null;       // Find by EUID

    private static String mFindByEUID1 = null;      // Find by EUID and after a specified time

    private static String mFindByEUID2 = null;      // Find by EUID and before a specified time

    private static String mFindByEUID3 = null;      // Find by EUID and between two specified times

    private static String mFindByEUID4 = null;      // Find by EUID and after a specified time 
    // (non-merged records only)

    private static String mFindByEUID5 = null;      // Find by EUID and after a specified timestamp 
    // for recovery.

    private static String mFindNextTranByEUID = null;   // Find the next Transaction Object for an EUID

    private static String mFindBySystemCodeLID = null;  // Find by system code SQL statement 
    // for TransactionObjectDB

    private static String mFindBySystemCodeLID1 = null; // Find by system code SQL statement for 
    // TransactionObjectDB and after a 
    // specified time.

    private static String mFindBySystemCodeLID2 = null; // Find by system code SQL statement for 
    // TransactionObjectDB and before a 
    // specified time.

    private static String mFindBySystemCodeLID3 = null; // Find by system code SQL statement for 
    // TransactionObjectDB and between two  
    // specified times.

    private static String mSelectDelta = null;          // Find the delta by EUID

    private static String mOperationColumnName = null;  // Name of the operation column.

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * default constructor
     *
     * @throws OPSException if an error is encountered.
     */
    public TransactionObjectDB() throws OPSException {
        if (mOperationColumnName == null) {
            mOperationColumnName = DBAdapter.getDBAdapterInstance().getOperationColumnName();
        }
        if (mSelectString == null) {
            mSelectString =
                    DBAdapter.getDBAdapterInstance().getTransObjSelectStmt();
        }
        if (mFindNextTranByEUID == null) {
            mFindNextTranByEUID =
                    DBAdapter.getDBAdapterInstance().getTransObjNextTOByEUIDStmt();
        }
        if (mFindBySystemCodeLID == null) {
            mFindBySystemCodeLID =
                    DBAdapter.getDBAdapterInstance().getTransObjBySysCodeLIDStmt();
        }
        if (mFindBySystemCodeLID1 == null) {
            mFindBySystemCodeLID1 =
                    DBAdapter.getDBAdapterInstance().getTransObjBySysCodeLIDAfterTimeStmt();
        }
        if (mFindBySystemCodeLID2 == null) {
            mFindBySystemCodeLID2 =
                    DBAdapter.getDBAdapterInstance().getTransObjBySysCodeLIDBeforeTimeStmt();
        }
        if (mFindBySystemCodeLID3 == null) {
            mFindBySystemCodeLID3 =
                    DBAdapter.getDBAdapterInstance().getTransObjBySysCodeLIDBetweenTimesStmt();
        }
        if (mFindByEUID == null) {
            mFindByEUID =
                    DBAdapter.getDBAdapterInstance().getTransObjByEUIDStmt();
        }
        if (mFindByEUID1 == null) {
            mFindByEUID1 =
                    DBAdapter.getDBAdapterInstance().getTransObjByEUIDAfterTimeStmt();
        }
        if (mFindByEUID2 == null) {
            mFindByEUID2 =
                    DBAdapter.getDBAdapterInstance().getTransObjByEUIDBeforeTimeStmt();
        }
        if (mFindByEUID3 == null) {
            mFindByEUID3 =
                    DBAdapter.getDBAdapterInstance().getTransObjByEUIDBetweenTimesStmt();
        }
        if (mFindByEUID4 == null) {
            mFindByEUID4 =
                    DBAdapter.getDBAdapterInstance().getTransObjByEUIDAfterTimeNonMergedStmt();
        }
        if (mFindByEUID5 == null) {
            mFindByEUID5 =
                    DBAdapter.getDBAdapterInstance().getTransObjForRecoveryStmt();
        }

    }

    /**
     * Retrieves TransactionObject by TransactionNumber.
     *
     * @param conn JDBC connection.
     * @param transactionnumber TransactionNumber.
     * @throws OPSException if an error is encountered.
     * @return TransactionObject.
     */
    public TransactionObject get(Connection conn, String transactionnumber)
            throws OPSException {

        TransactionObject ret = null;

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {

            stmt = getStatement(mSelectString, conn);
            stmt.setString(1, transactionnumber);

            rSet = stmt.executeQuery();

            if (null != rSet) {
                ret = new TransactionObject();

                while (rSet.next()) {
                    ret.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBER", "String"));
                    ret.setLID1(getValue(rSet, "LID1", "String"));
                    ret.setLID2(getValue(rSet, "LID2", "String"));
                    ret.setEUID1(getValue(rSet, "EUID1", "String"));
                    ret.setEUID2(getValue(rSet, "EUID2", "String"));
                    ret.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    ret.setFunctionColumnName(mOperationColumnName);
                    ret.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    ret.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    ret.setDelta(getValue(rSet, "DELTA", "Blob"));
                    ret.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    ret.setLID(getValue(rSet, "LID", "String"));
                    ret.setEUID(getValue(rSet, "EUID", "String"));
                }
                ret.reset();
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(transactionnumber);
            String sql = sql2str(mSelectString, params);
            throw new OPSException(mLocalizer.t("OPS679: Could not retrieve " +
                    "a TransactionObject from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e.getMessage()));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS680: Could not retrieve " +
                    "a TransactionObject from the database : {0}", e.getMessage()));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS681: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return ret;
    }

    /**
     * Retrieves the next TransactionObject for an EUID.
     *
     * @param conn JDBC connection.
     * @param transactionnumber TransactionNumber.
     * @throws OPSException if an error is encountered.
     * @return TransactionObject.
     */
    public TransactionObject getNext(Connection conn,
            String transactionnumber,
            String EUID)
            throws OPSException {

        TransactionObject tObj = null;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {

            stmt = getStatement(mFindNextTranByEUID, conn);
            stmt.setString(1, transactionnumber);
            stmt.setString(2, EUID);
            stmt.setString(3, EUID);
            rSet = stmt.executeQuery();
            tObj = null;

            if (null != rSet) {
                while (rSet.next()) {
                    tObj = new TransactionObject();

                    tObj.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBER", "String"));
                    tObj.setLID1(getValue(rSet, "LID1", "String"));
                    tObj.setLID2(getValue(rSet, "LID2", "String"));
                    tObj.setEUID1(getValue(rSet, "EUID1", "String"));
                    tObj.setEUID2(getValue(rSet, "EUID2", "String"));
                    tObj.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    tObj.setFunctionColumnName(mOperationColumnName);
                    tObj.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    tObj.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    tObj.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    tObj.setLID(getValue(rSet, "LID", "String"));
                    tObj.setEUID(getValue(rSet, "EUID", "String"));
                }
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(transactionnumber);
            params.add(EUID);
            params.add(EUID);
            String sql = sql2str(mFindNextTranByEUID, params);
            throw new OPSException(mLocalizer.t("OPS682: Could not retrieve the " +
                    "next TransactionObject from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS683: Could not retrieve the " +
                    "next TransactionObject from the database: {0}",
                    e));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS684: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return tObj;
    }

    /**
     * Persists a TransactionObject into the database.
     *
     * @param conn JDBC connection.
     * @param tObj TransactionObject
     * @throws OPSException if an error is encountered.
     */
    public void create(Connection con, TransactionObject tObj)
            throws OPSException {
        DBAdapter.getDBAdapterInstance().createTransactionObjectDB(con, tObj);
    }

    /**
     * Retrieves an array of TransactionObjects by SystemCode and LocalID.
     * If starting or ending times are specified, they are added to the
     * search criteria.
     *
     * @param conn JDBC connection.
     * @param systemcode System code.
     * @param lid Local ID.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObject(Connection conn,
            String systemcode,
            String lid,
            Date beginTS,
            Date endTS)
            throws OPSException {

        TransactionObject[] ret = null;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            if ((beginTS == null) && (endTS == null)) {

                stmt = getStatement(mFindBySystemCodeLID, conn);
                setParam(stmt, 1, "String", systemcode);
                setParam(stmt, 2, "String", lid);
                rSet = stmt.executeQuery();
            } else if ((beginTS != null) && (endTS == null)) {
                stmt = getStatement(mFindBySystemCodeLID1, conn);
                setParam(stmt, 1, "String", systemcode);
                setParam(stmt, 2, "String", lid);
                setParam(stmt, 3, "Timestamp", beginTS);
                rSet = stmt.executeQuery();
            } else if ((beginTS == null) && (endTS != null)) {
                stmt = getStatement(mFindBySystemCodeLID2, conn);
                setParam(stmt, 1, "String", systemcode);
                setParam(stmt, 2, "String", lid);
                setParam(stmt, 3, "Timestamp", endTS);
                rSet = stmt.executeQuery();
            } else {
                stmt = getStatement(mFindBySystemCodeLID3, conn);
                setParam(stmt, 1, "String", systemcode);
                setParam(stmt, 2, "String", lid);
                setParam(stmt, 3, "Timestamp", beginTS);
                setParam(stmt, 4, "Timestamp", endTS);
                rSet = stmt.executeQuery();
            }

            if (null != rSet) {
                ArrayList list = new ArrayList();
                while (rSet.next()) {
                    TransactionObject tObj = new TransactionObject();
                    tObj.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBER", "String"));
                    tObj.setLID1(getValue(rSet, "LID1", "String"));
                    tObj.setLID2(getValue(rSet, "LID2", "String"));
                    tObj.setEUID1(getValue(rSet, "EUID1", "String"));
                    tObj.setEUID2(getValue(rSet, "EUID2", "String"));
                    tObj.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    tObj.setFunctionColumnName(mOperationColumnName);
                    tObj.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    tObj.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    tObj.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    tObj.setLID(getValue(rSet, "LID", "String"));
                    tObj.setEUID(getValue(rSet, "EUID", "String"));
                    tObj.reset();
                    list.add(tObj);
                }

                if (list.size() > 0) {
                    ret = new TransactionObject[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        ret[i] = (TransactionObject) list.get(i);
                    }
                }
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(systemcode);
            params.add(lid);
            String sql = null;

            if ((beginTS == null) && (endTS == null)) {
                sql = sql2str(mFindBySystemCodeLID, params);
            } else if ((beginTS != null) && (endTS == null)) {
                params.add(beginTS);
                sql = sql2str(mFindBySystemCodeLID1, params);
            } else if ((beginTS == null) && (endTS != null)) {
                params.add(endTS);
                sql = sql2str(mFindBySystemCodeLID2, params);
            } else {
                params.add(beginTS);
                params.add(endTS);
                sql = sql2str(mFindBySystemCodeLID3, params);
            }
            throw new OPSException(mLocalizer.t("OPS685: Could not retrieve the " +
                    "TransactionObjects from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS686: Could not retrieve the " +
                    "TransactionObjects from the database : {0}",
                    e));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS687: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return ret;
    }

    /**
     * Retrieves an array of TransactionObjects by EUID. If starting or 
     * ending times are specified, they are added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObject(Connection conn,
            String euid,
            Date beginTS,
            Date endTS)
            throws OPSException {

        TransactionObject[] ret = null;

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                ArrayList params = new ArrayList();
                String sql = null;

                if ((beginTS == null) && (endTS == null)) {
                    sql = sql2str(mFindByEUID, params);
                } else if ((beginTS != null) && (endTS == null)) {
                    params.add(beginTS);
                    sql = sql2str(mFindByEUID1, params);
                } else if ((beginTS == null) && (endTS != null)) {
                    params.add(endTS);
                    sql = sql2str(mFindByEUID2, params);
                } else {
                    params.add(beginTS);
                    params.add(endTS);
                    sql = sql2str(mFindByEUID3, params);
                }
                params.add(euid);
                mLogger.fine("Query String for findTransactionObject(): " + sql);
            }

            stmt = null;

            if ((beginTS == null) && (endTS == null)) {
                stmt = getStatement(mFindByEUID, conn);
                setParam(stmt, 1, "String", euid);
                setParam(stmt, 2, "String", euid);
                rSet = stmt.executeQuery();
            } else if ((beginTS != null) && (endTS == null)) {
                stmt = getStatement(mFindByEUID1, conn);
                setParam(stmt, 1, "Timestamp", beginTS);
                setParam(stmt, 2, "String", euid);
                setParam(stmt, 3, "String", euid);
                rSet = stmt.executeQuery();
            } else if ((beginTS == null) && (endTS != null)) {
                stmt = getStatement(mFindByEUID2, conn);
                setParam(stmt, 1, "Timestamp", endTS);
                setParam(stmt, 2, "String", euid);
                setParam(stmt, 3, "String", euid);
                rSet = stmt.executeQuery();
            } else {
                stmt = getStatement(mFindByEUID3, conn);
                setParam(stmt, 1, "Timestamp", beginTS);
                setParam(stmt, 2, "Timestamp", endTS);
                setParam(stmt, 3, "String", euid);
                setParam(stmt, 4, "String", euid);
                rSet = stmt.executeQuery();
            }

            if (null != rSet) {
                ArrayList list = new ArrayList();

                while (rSet.next()) {
                    TransactionObject tObj = new TransactionObject();
                    tObj.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBER", "String"));
                    tObj.setLID1(getValue(rSet, "LID1", "String"));
                    tObj.setLID2(getValue(rSet, "LID2", "String"));
                    tObj.setEUID1(getValue(rSet, "EUID1", "String"));
                    tObj.setEUID2(getValue(rSet, "EUID2", "String"));
                    tObj.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    tObj.setFunctionColumnName(mOperationColumnName);
                    tObj.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    tObj.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    tObj.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    tObj.setLID(getValue(rSet, "LID", "String"));
                    tObj.setEUID(getValue(rSet, "EUID", "String"));
                    if (euid.equals(tObj.getEUID())) {
                        tObj.setRecoverObject(tObj.RECOVER_SURVIVOR);
                    } else {
                        tObj.setRecoverObject(tObj.RECOVER_MERGED);
                    }
                    tObj.reset();
                    list.add(tObj);
                }

                if (list.size() > 0) {
                    ret = new TransactionObject[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        ret[i] = (TransactionObject) list.get(i);
                    }
                }
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(euid);

            String sql = null;

            if ((beginTS == null) && (endTS == null)) {
                sql = sql2str(mFindByEUID, params);
            } else if ((beginTS != null) && (endTS == null)) {
                params.add(beginTS);
                sql = sql2str(mFindByEUID1, params);
            } else if ((beginTS == null) && (endTS != null)) {
                params.add(endTS);
                sql = sql2str(mFindByEUID2, params);
            } else {
                params.add(beginTS);
                params.add(endTS);
                sql = sql2str(mFindByEUID3, params);
            }
            throw new OPSException(mLocalizer.t("OPS688: Could not retrieve the " +
                    "TransactionObjects from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e.getMessage()));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS689: Could not retrieve the " +
                    "TransactionObjects from the database : {0}",
                    e.getMessage()));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS690: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return ret;
    }

    /**
     * Retrieves an array of TransactionObjects by EUID.  If the starting time
     * is specified, it is added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param beginTS Starting timestamp.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObject(Connection conn,
            String euid,
            Date beginTS)
            throws OPSException {

        TransactionObject[] ret = null;
        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
            if (mLogger.isLoggable(Level.FINE)) {
                ArrayList params = new ArrayList();
                params.add(beginTS);
                String sql = sql2str(mFindByEUID4, params);
                params.add(euid);
                mLogger.fine("Query String for findTransactionObject(): " + sql);
            }
            stmt = getStatement(mFindByEUID4, conn);
            setParam(stmt, 1, "Timestamp", beginTS);
            setParam(stmt, 2, "String", euid);
            rSet = stmt.executeQuery();

            if (null != rSet) {
                ArrayList list = new ArrayList();

                while (rSet.next()) {
                    TransactionObject tObj = new TransactionObject();
                    tObj.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBEr", "String"));
                    tObj.setLID1(getValue(rSet, "LID1", "String"));
                    tObj.setLID2(getValue(rSet, "LID2", "String"));
                    tObj.setEUID1(getValue(rSet, "EUID1", "String"));
                    tObj.setEUID2(getValue(rSet, "EUID2", "String"));
                    tObj.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    tObj.setFunctionColumnName(mOperationColumnName);
                    tObj.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    tObj.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    tObj.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    tObj.setLID(getValue(rSet, "LID", "String"));
                    tObj.setEUID(getValue(rSet, "EUID", "String"));
                    if (euid.equals(tObj.getEUID())) {
                        tObj.setRecoverObject(tObj.RECOVER_SURVIVOR);
                    } else {
                        tObj.setRecoverObject(tObj.RECOVER_MERGED);
                    }
                    tObj.reset();
                    list.add(tObj);
                }

                if (list.size() > 0) {
                    ret = new TransactionObject[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        ret[i] = (TransactionObject) list.get(i);
                    }
                }
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(euid);
            params.add(beginTS);
            String sql = sql2str(mFindByEUID4, params);
            throw new OPSException(mLocalizer.t("OPS691: Could not retrieve the " +
                    "TransactionObjects from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e.getMessage()));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS692: Could not retrieve the " +
                    "TransactionObjects from the database : {0}",
                    e.getMessage()));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS693: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return ret;
    }

    /**
     * Retrieves an array of TransactionObjects by TransactionObject. A dynamic
     * Statement is going to be created according to what's populated in 
     * the TransactionObject. The only operator supported is '='.  If starting or 
     * ending times are specified, they are added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param tObj TransactionObject.
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObject(Connection conn,
            TransactionObject tObj,
            Date beginTS,
            Date endTS)
            throws OPSException {

        return findTransactionObject(conn, tObj, beginTS, endTS, "timestamp desc");
    }

    /**
     * Retrieves array of TransactionObjects by TransactionObject. A dynamic 
     * Statement is going to be created according to what's populated in 
     * the TransactionObject. The only operator supported is '='. If starting or
     * ending times are specified, they are added to the search criteria.
     *
     * @param conn JDBC connection.
     * @param tObj TransactionObject
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @param orderBy Sort order.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObject(Connection conn,
            TransactionObject tObj,
            Date beginTS,
            Date endTS,
            String orderBy)
            throws OPSException {

        TransactionObject[] ret = null;
        String queryStr = null;
        Statement queryStmt = null;
        ResultSet rSet = null;

        try {
            boolean first = true;
            DBAdapter db = DBAdapter.getDBAdapterInstance();
            queryStr =
                    db.getTransObjCreateSelectStmt();

            if ((beginTS != null) && (endTS == null)) {
                first = false;
                queryStr += ("               timestamp >= " +
                        db.getDateFormattedString(beginTS));
            } else if ((beginTS == null) && (endTS != null)) {
                first = false;
                queryStr += ("               timestamp <= " +
                        db.getDateFormattedString(endTS));
            } else if ((beginTS != null) && (endTS != null)) {
                first = false;
                queryStr += ("               timestamp >= " + db.getDateFormattedString(beginTS));
                queryStr += (" and \n               timestamp <= " + db.getDateFormattedString(endTS));
            }

            ArrayList fields = tObj.pGetFieldNames();
            if (fields != null) {
                for (int i = 0; i < fields.size(); i++) {
                    String fieldName = (String) fields.get(i);

                    if (fieldName.equals("Delta")) {
                        continue;
                    }
                    if (!tObj.isNull(fieldName)) {
                        if (first) {
                            first = false;
                        } else {
                            queryStr += " and \n         ";
                        }
                        // If it is EUID, then we query from both EUID and EUID2.
                        if (fieldName.equals("EUID")) {
                            String euid = (String) tObj.getValue(fieldName);
                            queryStr += " ( EUID = '" + euid + "'  OR  EUID2 = '" + euid + "'  )";
                        } else {
                            String column = fieldName;
                            if (fieldName.equalsIgnoreCase("FUNCTION")) {
                                column = mOperationColumnName;
                            }
                            queryStr += ("               " + column + " = '" + tObj.getValue(fieldName) + "'");
                        }
                    }
                }
            }

            queryStr += "\n       order by \n" + "               " + orderBy + " \n";

            if (mLogger.isLoggable(Level.FINE)) {
                mLogger.fine("TransactionObjectDB: the transaction log query " + "string is: \n" + queryStr);
            }

            queryStmt = conn.createStatement();
            rSet = queryStmt.executeQuery(queryStr);

            if (null != rSet) {
                ArrayList list = new ArrayList();

                while (rSet.next()) {
                    TransactionObject t = new TransactionObject();
                    t.setTransactionNumber(getValue(rSet, "TRANSACTIONNUMBER", "String"));
                    t.setLID1(getValue(rSet, "LID1", "String"));
                    t.setLID2(getValue(rSet, "LID2", "String"));
                    t.setEUID1(getValue(rSet, "EUID1", "String"));
                    t.setEUID2(getValue(rSet, "EUID2", "String"));
                    t.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    t.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    t.setTimeStamp(getValue(rSet, "TIMESTAMP", "Timestamp"));
                    t.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    t.setLID(getValue(rSet, "LID", "String"));
                    t.setEUID(getValue(rSet, "EUID", "String"));
                    t.reset();
                    list.add(t);
                }

                if (list.size() > 0) {
                    ret = new TransactionObject[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        ret[i] = (TransactionObject) list.get(i);
                    }
                }
            }
        } catch (Exception e) {
            throw new OPSException(mLocalizer.t("OPS695: Could not retrieve the " +
                    "TransactionObjects from the database: {0}",
                    e.getMessage()));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (queryStmt != null) {
                    queryStmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS696: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }

        return ret;
    }

    /**
     * Retrieves array of TransactionObjects by by EUID and after a specified 
     * timestamp for recovery. 
     *
     * @param conn JDBC connection.
     * @param tObj TransactionObject
     * @param beginTS Starting timestamp.
     * @param endTS Ending timestamp.
     * @param orderBy Sort order.
     * @throws OPSException if an error is encountered.
     * @return an array of TransactionObject instances.
     */
    public TransactionObject[] findTransactionObjectForRecovery(Connection conn,
            String euid,
            Date beginTS)
            throws OPSException {

        TransactionObject[] ret = null;

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        try {
		    ArrayList<String> params = DBAdapter.getDBAdapterInstance().getTransObjForRecoveryParameters(euid, beginTS);
            if (mLogger.isLoggable(Level.FINE)) {
                String sql = sql2str(mFindByEUID5, params);
                mLogger.fine("Query String for findTransactionObjectForRecovery(): " + sql);
            }

            stmt = getStatement(mFindByEUID5, conn);
			for (int i=0; i < params.size(); i++) {
			    setParam(stmt, i+1, "String", params.get(i));
			}
            rSet = stmt.executeQuery();

            if (null != rSet) {
                ArrayList list = new ArrayList();

                while (rSet.next()) {
                    TransactionObject tObj = new TransactionObject();
                    tObj.setTransactionNumber(getValue(rSet,
                            "TRANSACTIONNUMBER", "String"));
                    tObj.setLID1(getValue(rSet, "LID1", "String"));
                    tObj.setLID2(getValue(rSet, "LID2", "String"));
                    tObj.setEUID1(getValue(rSet, "EUID1", "String"));
                    tObj.setEUID2(getValue(rSet, "EUID2", "String"));
                    tObj.setSystemUser(getValue(rSet, "SYSTEMUSER", "String"));
                    tObj.setFunctionColumnName(mOperationColumnName);
                    tObj.setFunction(getValue(rSet, mOperationColumnName, "String"));
                    tObj.setTimeStamp(rSet.getTimestamp("TIMESTAMP"));
                    tObj.setSystemCode(getValue(rSet, "SYSTEMCODE", "String"));
                    tObj.setLID(getValue(rSet, "LID", "String"));
                    tObj.setEUID(getValue(rSet, "EUID", "String"));
                    if (euid.equals(tObj.getEUID())) {
                        tObj.setRecoverObject(tObj.RECOVER_SURVIVOR);
                    } else {
                        tObj.setRecoverObject(tObj.RECOVER_MERGED);
                    }
                    tObj.reset();
                    list.add(tObj);
                }

                if (list.size() > 0) {
                    ret = new TransactionObject[list.size()];

                    for (int i = 0; i < list.size(); i++) {
                        ret[i] = (TransactionObject) list.get(i);
                    }
                }
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            char decSep = new DecimalFormatSymbols().getDecimalSeparator();
            String pattern = "yyyy-MM-dd HH:mm:ss" + decSep + "SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String ts = sdf.format(beginTS);
            params.add(ts);
            params.add(euid);
            params.add(euid);
            String sql = sql2str(mFindByEUID5, params);
            throw new OPSException(mLocalizer.t("OPS697: Could not retrieve the " +
                    "TransactionObjects from the database " +
                    "with this SQL statement: {0}: {1}",
                    sql, e.getMessage()));
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS698: Could not retrieve the " +
                    "TransactionObjects from the database: {0}",
                    e.getMessage()));
        } finally {
            try {
                if (rSet != null) {
                    rSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS699: Could not close " +
                        "an SQL statement or result set: {0}",
                        e.getMessage()));
            }
        }
        return ret;
    }
}
