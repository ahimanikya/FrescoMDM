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
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.util.Localizer;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Date;

/**
 * @author rtam
 * Database adapter class
 */
public abstract class DBAdapter {

    private final static String ORACLE = "Oracle";          // Oracle database  

    private final static String SQLSERVER = "SQL Server";   // SQL Server database  

    private final static String MYSQL = "MySQL";            // MySQL database  

    // TODO: Add support for new database vendors as necessary
    private static String mDatabaseType = null;     // database type

    private static DBAdapter mDBAdapterRef = null;  // database adapter

    private transient static final Localizer mLocalizer = Localizer.get();

    /**  Return the DBAdapter singleton.
     *
     * @throws OPSException if any exceptions are encountered.
     * @return  DBAdapter singleton
     */
    public static DBAdapter getDBAdapterInstance() throws OPSException {
        if (mDBAdapterRef == null) {
			synchronized(DBAdapter.class) {
				// determine and set the database type
				if (mDatabaseType == null) {
					mDatabaseType = ObjectFactory.getDatabase();

					if (mDatabaseType.compareToIgnoreCase(ORACLE) == 0) {
						mDBAdapterRef = OracleAdapter.getInstance();
					} else if (mDatabaseType.compareToIgnoreCase(SQLSERVER) == 0) {
						mDBAdapterRef = SQLServerAdapter.getInstance();
					} else if (mDatabaseType.compareToIgnoreCase(MYSQL) == 0) {
						mDBAdapterRef = MySQLAdapter.getInstance();
					} else {    // TODO: Add support for new database vendors as necessary

						throw new OPSException(mLocalizer.t("OPS500: Could not retrieve " +
								"a database adapter instance.  This is an " +
								"unsupported database type: {0}", mDatabaseType));
					}
				}
			}
        }
        return mDBAdapterRef;
    }

    // TransactionObjectDB
    /** Select statement for TransactionObjectDB.
     *
     * @return  Select statement for TransactionObjectDB.
     */
    abstract String getTransObjSelectStmt();

    /** Generates the first part of a general select clause to find
     * a set of transaction objects.
     *
     * @return  First part of a general select clause for TransactionObjectDB.
     */
    abstract String getTransObjCreateSelectStmt();

    /** Insert statement for TransactionObjectDB.
     *
     * @return  Insert SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjInsertStmt();

    /** Find by system code SQL statement for TransactionObjectDB.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjBySysCodeLIDStmt();

    /** Find by system code SQL statement for TransactionObjectDB and after
     * a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjBySysCodeLIDAfterTimeStmt();

    /** Find by system code SQL statement for TransactionObjectDB and before
     * a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjBySysCodeLIDBeforeTimeStmt();

    /** Find by system code SQL statement for TransactionObjectDB and between
     * two specified times.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjBySysCodeLIDBetweenTimesStmt();

    /** Find by EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjByEUIDStmt();

    /** Find by EUID and after a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjByEUIDAfterTimeStmt();

    /** Find by EUID and before a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjByEUIDBeforeTimeStmt();

    /** Find by EUID and between two specified times.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjByEUIDBetweenTimesStmt();

    /** Find by EUID and after a specified time (non-merged records only).
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public abstract String getTransObjByEUIDAfterTimeNonMergedStmt();

    /** Find by EUID and after a specified timestamp for recovery.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    abstract String getTransObjForRecoveryStmt();

    /** Find the next Transaction Object for an EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public abstract String getTransObjNextTOByEUIDStmt();

    /** Find the delta by EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public abstract String getDeltaByTransIDStmt();

    //  AuditManager
    /** Select statement for AuditManager.
     *
     * @return  Select SQL statement for AuditManager.
     */
    public abstract String getAuditMgrSelectStmt();

    /** Insert statement for AuditManager.
     *
     * @return  Insert SQL statement for AuditManager.
     */
    public abstract String getAuditMgrInsertStmt();

    /** Return Operation column name for AuditManager.
     *
     * @return  AuditManager table Column name.
     */
    public abstract String getAuditMgrOperationColumnName();

    // QueryHelper
    /** Select LID by maximum timestamp for QueryHelper.
     *
     * @return  Select SQL statement for QueryHelper.
     */
    public abstract String getQHelperMergeMaxTimestampStmt();

    /** Select maximum timestamp for unmerging two LIDs for QueryHelper.
     *
     * @return  Select SQL statement for QueryHelper.
     */
    public abstract String getQHelperUnmergeMaxTimestampStmt();

    // KeyStatisticsDB
    /** Count transactions of a specific operation between two specified times.
     *
     * @return  Select SQL statement for KeyStatisticsDB.
     */
    public abstract String getKeyStatDBCountBetweenTimesStmt();

    // Format conversion
    /** Convert a string (varchar) to a number.
     *
     * @return  Conversion function.
     */
    public abstract String getVarcharToNumberConversion(String val);

    // General methods
    /** Return the name of the operation column.
     *
     * @return  name of the operation column.
     */
    public abstract String getOperationColumnName();

    /**
     * Convert Date to formatted String that is used in SQL query.
     * @param date Date to convert.
     * @return formatted String representing the formatted date.
     */
    abstract String getDateFormattedString(Date date);

    /** Set the delta column value.
     *
     * @param ps PreparedStatement for setting the delta value.
     * @param delta Value of the delta.
     * @param columnName Name of the delta column.
     * @param deltaColumnIndex Delta index in the prepared statement.
     * @throws OPSException if any exceptions are encountered.
     * @return boolean true if no problems were encountered, false otherwise.
     */
    public abstract boolean setDelta(Object delta,
            DeltaParameters deltaParams)
            throws OPSException;

    /** Creates a TransactionObjectDB and persist to the database.
     *
     * @param con Database connection.
     * @param tObj TransactionObject to persist.
     * @throws OPSException if any exceptions are encountered
     */
    public abstract void createTransactionObjectDB(Connection con,
            TransactionObject tObj)
            throws OPSException;
}
