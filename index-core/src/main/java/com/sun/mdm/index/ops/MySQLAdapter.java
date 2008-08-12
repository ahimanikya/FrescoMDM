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
import com.sun.mdm.index.util.Localizer;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * MySQL database adapter class.
 */
public class MySQLAdapter extends DBAdapter {

    private static MySQLAdapter mySQLAdapterInstance = null;  // singleton instance

    private static ObjectPersistenceService mOps = null;        // OPS instance

    private transient final Localizer mLocalizer = Localizer.get();

    /**  Return the MySQLAdapter singleton.
     *
     * @throws OPSException if any exceptions are encountered
     * @return  MySQLAdapter singleton
     */
    public static MySQLAdapter getInstance() throws OPSException {
        if (mySQLAdapterInstance == null) {
            mySQLAdapterInstance = new MySQLAdapter();
            mOps = new ObjectPersistenceService();
        }
        return mySQLAdapterInstance;
    }

    // TransactionObjectDB
    /** Select statement for TransactionObjectDB.
     *
     * @return  Select statement for TransactionObjectDB.
     */
    String getTransObjSelectStmt() {
        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" + 
                "    euid2, \n" +
                "    function, \n" + 
                "    systemuser, \n" + 
                "    timestamp, \n" + 
                "    delta, \n" + 
                "    systemcode, \n" +
                "    lid, \n" + 
                "    euid \n" +
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" +
                "    transactionnumber = ? \n" + 
                "order by \n" + 
                "    transactionnumber desc \n");

    }

    /** Generates the first part of a general select clause to find
     * a set of transaction objects.
     *
     * @return  First part of a general select clause for TransactionObjectDB.
     */
    String getTransObjCreateSelectStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" + 
                "    timestamp, \n" + 
                "    systemcode, \n" + 
                "    lid, \n" + 
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n");

    }

    /** Insert statement for TransactionObjectDB.
     *
     * @return  Insert SQL statement for TransactionObjectDB.
     */
    String getTransObjInsertStmt() {

        return ("insert into sbyn_transaction \n" + 
                "( \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" +
                "    delta, \n" + 
                "    systemcode, \n" + 
                "    lid, \n" + 
                "    euid \n" + 
                ") \n" +
                "values \n" + 
                "( \n" +
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?, \n" + 
                "    ?  \n" + 
                ") \n");

    }

    /** Find by system code SQL statement for TransactionObjectDB.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjBySysCodeLIDStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" +
                "    euid1, \n" + 
                "    euid2, \n" +
                "    function, \n" + 
                "    systemuser, \n" + 
                "    timestamp, \n" + 
                "    systemcode, \n" +
                "    lid, \n" + 
                "    euid \n" + "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    systemcode = ? and \n" + 
                "    lid = ? \n" + 
                "order by \n" + 
                "    transactionnumber desc \n");
    }

    /** Find by system code SQL statement for TransactionObjectDB and after
     * a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjBySysCodeLIDAfterTimeStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" + 
                "    timestamp, \n" + 
                "    systemcode, \n" + 
                "    lid, \n" + 
                "    euid \n" + 
                "from \n" +
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    systemcode = ? and \n" +
                "    lid = ? and \n" + 
                "    timestamp >= ? \n" + 
                "order by \n" + 
                "    transactionnumber desc \n");
    }

    /** Find by system code SQL statement for TransactionObjectDB and before
     * a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB
     */
    String getTransObjBySysCodeLIDBeforeTimeStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" + 
                "    systemcode, \n" + 
                "    lid, \n" +
                "    euid \n" +
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    systemcode = ? and \n" +
                "    lid = ? and \n" +
                "    timestamp <= ? \n" + 
                "order by \n" + 
                "    transactionnumber desc \n");
    }

    /** Find by system code SQL statement for TransactionObjectDB and between
     * two specified times.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjBySysCodeLIDBetweenTimesStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" +
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" + 
                "    systemcode, \n" + 
                "    lid, \n" + 
                "    euid \n" + "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" +
                "    systemcode = ? and \n" + 
                "    lid = ? and \n" + 
                "    timestamp >= ? and \n" + 
                "    timestamp <= ? \n" + 
                "order by \n" + 
                "    transactionnumber desc \n");
    }

    /** Find by EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjByEUIDStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" + 
                "    systemcode, \n" +
                "    lid, \n" + 
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ?\n" +
                "order by \n" + 
                "    transactionnumber desc \n");
    }

    /** Find by EUID and after a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjByEUIDAfterTimeStmt() {

        return ("select \n" +
                "    transactionnumber, \n" +
                "    lid1, \n" + 
                "    lid2, \n" +
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" +
                "    systemcode, \n" + 
                "    lid, \n" +
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" +
                "where \n" +
                "    timestamp >= ? and \n" +
                "    (sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ?) \n" +
                "order by \n" + "    transactionnumber desc \n");
    }

    /** Find by EUID and before a specified time.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjByEUIDBeforeTimeStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" + 
                "    lid2, \n" + 
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" +
                "    systemuser, \n" +
                "    timestamp, \n" + 
                "    systemcode, \n" +
                "    lid, \n" +
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" +
                "where \n" + 
                "    timestamp <= ? and \n" +
                "    (sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ?)\n" + 
                "order by \n" + "    transactionnumber desc \n");
    }

    /** Find by EUID and between two specified times.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjByEUIDBetweenTimesStmt() {

        return ("select \n" +
                "    transactionnumber, \n" +
                "    lid1, \n" +
                "    lid2, \n" + 
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" +
                "    systemuser, \n" + 
                "    timestamp, \n" + 
                "    systemcode, \n" +
                "    lid, \n" + 
                "    euid \n" + 
                "from \n" +
                "    sbyn_transaction \n" + 
                "where \n" +
                "    timestamp >= ? and \n" +
                "    timestamp <= ? and \n" + 
                "    (sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ?) \n" +
                "order by \n" + "    transactionnumber desc \n");
    }

    /** Find by EUID and after a specified time (non-merged records only).
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public String getTransObjByEUIDAfterTimeNonMergedStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" + 
                "    lid1, \n" +
                "    lid2, \n" + 
                "    euid1, \n" +
                "    euid2, \n" + 
                "    function, \n" + 
                "    systemuser, \n" +
                "    timestamp, \n" +
                "    systemcode, \n" +
                "    lid, \n" +
                "    euid \n" +
                "from \n" +
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    timestamp >= ? and \n" +
                "    sbyn_transaction.euid = ? \n" + 
                "order by \n" +
                "    transactionnumber desc \n");
    }

    /** Find by EUID and after a specified timestamp for recovery.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    String getTransObjForRecoveryStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" +
                "    lid1, \n" +
                "    lid2, \n" + 
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" +
                "    systemuser, \n" + 
                "    timestamp, \n" +
                "    systemcode, \n" +
                "    lid, \n" + 
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    timestamp >= ? and \n" +
                "    (sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ?) \n" + 
                "order by \n" + 
                "    timestamp, transactionnumber \n");
    }

    /** Find the next Transaction Object for an EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public String getTransObjNextTOByEUIDStmt() {

        return ("select \n" + 
                "    transactionnumber, \n" +
                "    lid1, \n" +
                "    lid2, \n" +
                "    euid1, \n" + 
                "    euid2, \n" + 
                "    function, \n" +
                "    systemuser, \n" +
                "    timestamp, \n" +
                "    systemcode, \n" + 
                "    lid, \n" +
                "    euid \n" + 
                "from \n" + 
                "    sbyn_transaction \n" +
                "where \n" +
                "    transactionnumber = (select min(transactionnumber) \n" + "                             from sbyn_transaction \n" + " 		                      where \n" + "                                 transactionnumber  > ? and \n" + "                                 ( sbyn_transaction.euid = ? or sbyn_transaction.euid2 = ? ) ) \n");
    }

    /** Find the delta by EUID.
     *
     * @return  Select SQL statement for TransactionObjectDB.
     */
    public String getDeltaByTransIDStmt() {

        return ("select \n" + 
                "    delta \n" + 
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    transactionnumber = ? \n");
    }

    //  AuditManager
    /** Select statement for AuditManager.
     *
     * @return  Select SQL statement for AuditManager.
     */
    public String getAuditMgrSelectStmt() {

        return ("select \n" +
                "    a.audit_id, \n" +
                "    a.primary_object_type, \n" + 
                "    a.euid, \n" +
                "    a.euid_aux, \n" +
                "    a.function, \n" +
                "    a.detail, \n" +
                "    a.create_date, \n" + 
                "    a.create_by \n" +
                "from sbyn_audit a \n");
    }

    /** Insert statement for AuditManager.
     *
     * @return  Insert SQL statement for AuditManager.
     */
    public String getAuditMgrInsertStmt() {

        return ("insert into sbyn_audit \n" +
                "( \n" + 
                "    audit_id, \n" +
                "    primary_object_type, \n" + 
                "    euid, \n" +
                "    euid_aux, \n" +
                "    function, \n" + 
                "    detail, \n" +
                "    create_date, \n" +
                "    create_by\n" +
                ") \n" +
                "values \n" +
                "(\n" +
                "    ?,\n" + 
                "    ?,\n" + 
                "    ?,\n" + 
                "    ?,\n" +
                "    ?,\n" +
                "    ?,\n" + 
                "    ?,\n" + 
                "    ?\n" + ")\n");
    }

    /** Return Operation column name for AuditManager.
     *
     * @return  AuditManager table Column name.
     */
    public String getAuditMgrOperationColumnName() {

        return ("    a.function = ?");
    }

    // QueryHelper
    /** Select LID by maximum timestamp for QueryHelper.
     *
     * @return  Select SQL statement for QueryHelper.
     */
    public String getQHelperMergeMaxTimestampStmt() {

        return ("select \n" + 
                "    lid2, \n" +
                "    max(timestamp) mtnm \n" + 
                "from \n" + 
                "    sbyn_transaction \n" + 
                "where \n" + 
                "    lid1 = ? and \n" + 
                "    function = 'lidMerge' \n" +
                "group \n" + 
                "by lid2");
    }

    /** Select maximum timestamp for unmerging two LIDs for QueryHelper.
     *
     * @return  Select SQL statement for QueryHelper.
     */
    public String getQHelperUnmergeMaxTimestampStmt() {

        return ("select \n" +
                "max(timestamp) mtnu \n" +
                "from \n" +
                "    sbyn_transaction \n" + 
                "where \n" +
                "    lid1 = ? and \n" + 
                "    function = 'lidUnMerge' and \n" + 
                "    lid2 = ?\n");
    }

    // KeyStatisticsDB
    /** Count transactions of a specific operation between two specified times.
     *
     * @return  Select SQL statement for KeyStatisticsDB.
     */
    public String getKeyStatDBCountBetweenTimesStmt() {

        return ("select \n" + 
                "    count(*) \n" + 
                "from \n" +
                "    sbyn_transaction \n" +
                "where \n" + 
                "    function = ? and \n" + 
                "    timestamp >= ? and \n" +
                "    timestamp < ? + 1 \n");
    }

    // Format conversion
    /** Convert a string (varchar2) to a number.
     *
     * @param val   Value to convert.
     * @return  Conversion function.
     */
    public String getVarcharToNumberConversion(String val) {
        return ("cast(" + val + " as decimal(5,2))");
    }

    /**
     * Convert Date to formatted String that is used in SQL query.
     * @param date Date to convert.
     * @return formatted String representing the converted date.
     */
    public String getDateFormattedString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ObjectPersistenceService.DATEFORMAT);
        return " '" + sdf.format(date) + "' ";
    }

    // General methods
    /** Set the delta column value.
     *
     * @param delta Value of the delta.
     * @param deltaParams  Parameters used for setting the delta.
     * @throws OPSException if any exceptions are encountered.
     * @return boolean true if no problems were encountered, false otherwise.
     */
    public boolean setDelta(Object delta,
            DeltaParameters deltaParams)
            throws OPSException {

        mOps.setParamBlob(deltaParams.getPreparedStatement(),
                deltaParams.getColumnIndex(),
                delta);
        return true;
    }

    /** Return the name of the operation column.
     *
     * @return  name of the operation column.
     */
    public String getOperationColumnName() {
        return "FUNCTION";
    }

    /** Creates a TransactionObjectDB instance.
     *
     * @param con   Database connection.
     * @param tObj  TransactionObject instance to persist.
     * @throws OPSException if any exceptions are encountered.
     */
    public void createTransactionObjectDB(Connection con,
            TransactionObject tObj)
            throws OPSException {

        Calendar cal = Calendar.getInstance();
        char decSep = new DecimalFormatSymbols().getDecimalSeparator();
        String pattern = "yyyy-MM-dd HH:mm:ss" + decSep + "SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        // executes insert SQL statement
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            stmt = mOps.getStatement(getTransObjInsertStmt(), con);
            mOps.setParam(stmt, 1, "String", tObj.getTransactionNumber());
            mOps.setParam(stmt, 2, "String", tObj.getLID1());
            mOps.setParam(stmt, 3, "String", tObj.getLID2());
            mOps.setParam(stmt, 4, "String", tObj.getEUID1());
            mOps.setParam(stmt, 5, "String", tObj.getEUID2());
            mOps.setParam(stmt, 6, "String", tObj.getFunction());
            mOps.setParam(stmt, 7, "String", tObj.getSystemUser());
            mOps.setParam(stmt, 8, "String", sdf.format(cal.getTime()));
            mOps.setParam(stmt, 10, "String", tObj.getSystemCode());
            mOps.setParam(stmt, 11, "String", tObj.getLID());
            mOps.setParam(stmt, 12, "String", tObj.getEUID());

           Object delta = tObj.getDelta();
            
            // column name is not needed for MySQL
            DeltaParameters deltaParams = new DeltaParameters();
            deltaParams.setColumnIndex(9);
            deltaParams.setColumnName(null);
            deltaParams.setPreparedStatement(stmt);
            if (DBAdapter.getDBAdapterInstance().setDelta(delta, deltaParams) == true) {
                stmt.executeUpdate();  
            }
        } catch (ObjectException e) {
            throw new OPSException(mLocalizer.t("OPS700: Could not create " +
                    "a TransactionObjectDB instance: {0}", e));
        } catch (SQLException e) {
            String sqlErr = e.getMessage();
            ArrayList params = new ArrayList();

            try {
                params = mOps.addobject(params, tObj.getTransactionNumber());
                params = mOps.addobject(params, tObj.getLID1());
                params = mOps.addobject(params, tObj.getLID2());
                params = mOps.addobject(params, tObj.getEUID1());
                params = mOps.addobject(params, tObj.getEUID2());
                params = mOps.addobject(params, tObj.getFunction());
                params = mOps.addobject(params, tObj.getSystemUser());
                params = mOps.addobject(params, sdf.format(cal.getTime()));
                params = mOps.addobject(params, tObj.getDelta());
                params = mOps.addobject(params, tObj.getSystemCode());
                params = mOps.addobject(params, tObj.getLID());
                params = mOps.addobject(params, tObj.getEUID());

                String sql = mOps.sql2str(getTransObjInsertStmt(), params);
                throw new OPSException(mLocalizer.t("OPS701: Could not create " +
                        "a TransactionObjectDB instance " +
                        "with this SQL statement: {0}: {1}",
                        sql, e));
            } catch (ObjectException oe) {
                throw new OPSException(mLocalizer.t("OPS702: Could not create " +
                        "a TransactionObjectDB instance " +
                        "due to an SQL error: {0}: {1}",
                        e, oe));
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException e) {
                throw new OPSException(mLocalizer.t("OPS703: Could not close " +
                        "an SQL statement: {0}", e));
            }
        }
    }
}
