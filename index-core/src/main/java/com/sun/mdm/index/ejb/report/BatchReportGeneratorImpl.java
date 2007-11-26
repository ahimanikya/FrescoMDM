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
package com.sun.mdm.index.ejb.report;

import com.sun.mdm.index.ops.TransactionMgr;
import com.sun.mdm.index.query.AssembleDescriptor;
import com.sun.mdm.index.query.Condition;
import com.sun.mdm.index.query.EOSearchResultAssembler;
import com.sun.mdm.index.query.QMIterator;
import com.sun.mdm.index.query.QueryManager;
import com.sun.mdm.index.query.QueryObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSearchObject;
import com.sun.mdm.index.master.search.assumedmatch.AssumedMatchSummary;
import com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject;
import com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary;
import com.sun.mdm.index.master.search.transaction.TransactionSearchObject;
import com.sun.mdm.index.master.search.transaction.TransactionSummary;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.epath.EPath;
import com.sun.mdm.index.objects.epath.EPathArrayList;
import com.sun.mdm.index.objects.metadata.ObjectFactory;
import com.sun.mdm.index.ops.RecreateResult;
import com.sun.mdm.index.ops.DBAdapter;
import com.sun.mdm.index.ops.exception.OPSException;

import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.AssumedMatchReportRow;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.DeactivateReportRow;
import com.sun.mdm.index.report.KeyStatisticsReport;
import com.sun.mdm.index.report.KeyStatisticsReportConfig;
import com.sun.mdm.index.report.KeyStatisticsReportDB;
import com.sun.mdm.index.report.MergeReport;
import com.sun.mdm.index.report.MergeReportConfig;
import com.sun.mdm.index.report.MergeReportRow;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReportRow;
import com.sun.mdm.index.report.ReportException;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UnmergeReportRow;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.UpdateReportRow;
import com.sun.mdm.index.util.ConnectionUtil;
import com.sun.mdm.index.util.DateUtil;
import com.sun.mdm.index.util.Localizer;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import javax.ejb.SessionContext;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;


/**
 * Stateful Session bean for batch reports
 *
 */
public class BatchReportGeneratorImpl{
    private static final int NUM_DAYS_IN_WEEK = 7;
    private SessionContext context;
    
    private QueryManager mQuery;
    private TransactionMgr mTrans;

    private UpdateReport mUpdateReport;
    private UpdateReportConfig mUpdateReportConfig = null;

    private MergeReport mMergeReport = null;
    private MergeReportConfig mMergeReportConfig = null;
    private UnmergeReport mUnmergeReport = null;
    private UnmergeReportConfig mUnmergeReportConfig = null;
    
    private DeactivateReport mDeactivateReport = null;
    private DeactivateReportConfig mDeactivateReportConfig = null;

    private AssumedMatchReport mAssumedMatchReport = null;
    private AssumedMatchReportConfig mAssumedMatchReportConfig = null;
    private AssumedMatchSearchObject mAssumedMatchSearchObject = null;
    
    private PotentialDuplicateReport mPotentialDuplicateReport = null;
    private PotentialDuplicateReportConfig mPotentialDuplicateReportConfig = null;
    private PotentialDuplicateSearchObject mPotentialDuplicateSearchObject = null;
    
    /* default page size to 50 if not supplied by the report definition */
    private int DEFAULT_PAGE_SIZE = 50;
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DBDATEFORMAT = "yyyy-MM-dd hh24:MI:ss";
     // Assumed Matches Base Query
    private final String BASE_SELECT_CLAUSE = "SELECT " + "a.ASSUMEDMATCHID, "
        + "a.WEIGHT, " + "a.EUID, " + "b.SYSTEMUSER, " + "b.TIMESTAMP, "
        + "a.SYSTEMCODE, " + "a.LID, " + "a.TRANSACTIONNUMBER "
        + "FROM SBYN_ASSUMEDMATCH a, SBYN_TRANSACTION b "
        + "WHERE a.TRANSACTIONNUMBER=b.TRANSACTIONNUMBER";

    // Potential Duplicates Base Query
    private final String BASE_POT = "select distinct "
        + "a.POTENTIALDUPLICATEID, " + "a.WEIGHT, " + "a.DESCRIPTION, "
        + "a.STATUS, " + "a.RESOLVEDUSER, " + "a.RESOLVEDDATE, "
        + "a.RESOLVEDCOMMENT, " + "a.EUID2, " + "a.EUID1, " + "b.SYSTEMUSER, "
        + "b.TIMESTAMP, " + "b.SYSTEMCODE "
        + "from SBYN_POTENTIALDUPLICATES a, SBYN_TRANSACTION b "
        + "where a.TRANSACTIONNUMBER = b.TRANSACTIONNUMBER ";

    private final String TRANSACTION_SELECT_CLAUSE = "SELECT transactionnumber, "
        + "lid1, lid2, euid1, euid2, " 
        + "function, systemuser, timestamp, systemcode, lid, euid " 
        + "from sbyn_transaction " 
        + "where ";   
        
    private static String mNumberConversion = null;     // convert a string to a number
    

    private ResultSet mResultSetUpdate = null;
    private ResultSet mResultSetPotentialDuplicate = null;
    private ResultSet mResultSetAssumedMatch = null;
    private ResultSet mResultSetMerge = null;
    private ResultSet mResultSetUnmerge = null;
    private ResultSet mResultSetDeactivate = null;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    
    /** Path to root object
     */    
    private String mObjectPath;  

    /**
     * Creates a new instance of ReportGeneratorImpl
     */
    public BatchReportGeneratorImpl() {
        try {
            mQuery = com.sun.mdm.index.query.QueryManagerFactory.getInstance();
            mTrans = com.sun.mdm.index.ops.TransactionMgrFactory.getInstance();
        } catch (Exception ex) {
            mLogger.severe(mLocalizer.x("RPE002: Could not create an instance of BatchReportGeneratorImpl: {0}", ex.getMessage()));
        }
    }
    


    /**
     * To get potential duplicate base fields from the database. Added for
     * @param obj - Potential Duplicate Search Object
     * @param conn Connection
     * @return potential duplicate records
     * @throws  ReportException an error occurs
     */
    private ResultSet lookupPotDupRecords(PotentialDuplicateSearchObject obj, Connection conn)
        throws ReportException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer(BASE_POT);
        ArrayList parameters = new ArrayList();

        if (obj.getCreateStartDate() != null) {
            sb.append(" and b.TIMESTAMP >= ?");
            parameters.add(obj.getCreateStartDate());
        }

        if (obj.getCreateEndDate() != null) {
            sb.append(" and b.TIMESTAMP <= ?");
            parameters.add(obj.getCreateEndDate());
        }

        if (obj.getStatus() != null) {
            sb.append(" and a.STATUS = ?");
            parameters.add(obj.getStatus());
        }
        try {
            if (mNumberConversion == null) {
                mNumberConversion = 
                    DBAdapter.getDBAdapterInstance().getVarcharToNumberConversion("a.WEIGHT");
            }
        } catch (OPSException e) {
            throw new ReportException(mLocalizer.t("RPE501: Failed to lookup Potential Duplicate records: {0}", e));
        }
        sb.append(" order by "
                  + mNumberConversion
                  + " desc, a.EUID1 asc");

        try {
            ps = conn.prepareStatement(sb.toString());
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ps.setMaxRows(obj.getMaxElements());
            rs = ps.executeQuery();
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE502: Failed to lookup Potential Duplicate records: {0}", e));
        }
        return rs;
    }

    private ArrayList loadPotDupRecords(ResultSet rs, int count) 
                throws ReportException {

        ArrayList list = new ArrayList();
        int idx = 0;

        try {
            while (idx < count) {
                if (rs.next()) {
                    String id = rs.getString(1);
                    String weight = rs.getString(2);
                    String description = rs.getString(3);
                    String status = rs.getString(4);
                    String resolvedUser = rs.getString(5);
                    Date resolvedDate = rs.getTimestamp(6);
                    String resolvedComment = rs.getString(7);
                    String euid2 = rs.getString(8);
                    String euid1 = rs.getString(9);
                    String createUser = rs.getString(10);
                    Date createDate = rs.getTimestamp(11);
                    String systemCode = rs.getString(12);
                    PotentialDuplicateSummary dupSum 
                        = new PotentialDuplicateSummary(mPotentialDuplicateSearchObject,
                            id, euid1, euid2, status, description,
                            Float.parseFloat(weight), systemCode, createUser,
                            createDate, resolvedUser, resolvedDate, resolvedComment);
                    list.add(dupSum);
                } else {
                    break;
                }
                idx++;
            }

        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE503: Failed to load Potential Duplicate records: {0}", e));
        }
        return list;
     }
      
    private String getObjectPath(String[] fieldList) throws ReportException {
        if (mObjectPath == null) {
            for (int i = 0; i < fieldList.length; i++) {
                if (fieldList[i].endsWith("EUID")) {
                    mObjectPath = 
                        fieldList[i].substring(0, fieldList[i].length() - 5);
                    return mObjectPath;
                }
            }
            throw new ReportException(mLocalizer.t("RPE504: getObjectPath() requires EUID to be selected."));
        }
        return mObjectPath;
    }
    
    
    /**
     * To get the range of Potential Duplicate records
     *
     * @return PotentialDuplicateReport - Returns the object of next potential
     *         duplicate records
     * @throws ReportException an error occured
     */
    public PotentialDuplicateReport getNextPotDupRecords() throws ReportException {

        if (mResultSetPotentialDuplicate == null) {
            throw new ReportException(mLocalizer.t("RPE506: Potential Duplicate Report has not been generated."));
        }
        int pageSize = mPotentialDuplicateReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mPotentialDuplicateReport != null) {
                mPotentialDuplicateReport.clear();
            }            
            Connection con = mResultSetPotentialDuplicate.getStatement().getConnection();
            ArrayList transList = loadPotDupRecords(mResultSetPotentialDuplicate, pageSize);

            EPathArrayList fieldArrayList = mPotentialDuplicateSearchObject.getFieldsToRetrieve();
            String[] fields = null;
            if (fieldArrayList != null) {
                fields = fieldArrayList.toStringArray();
            }
            QueryObject queryObject = new QueryObject();
            queryObject.setSelect(fields);

            HashMap euidsToLoad = new HashMap();
            ObjectNode dummyNode = new ObjectNode();

            String objPath = getObjectPath(fields);
            String euidPath = objPath + "." + "EUID";

            for (int i = 0; i < transList.size(); i++) {
                PotentialDuplicateSummary pos = (PotentialDuplicateSummary) transList.get(i);
                String euid1 = pos.getEUID1();
                String euid2 = pos.getEUID2();
                euidsToLoad.put(euid1, dummyNode);
                euidsToLoad.put(euid2, dummyNode);
            }

            Object[] keyArray = euidsToLoad.keySet().toArray();

            String[] euidArray = new String[keyArray.length];

            for (int i = 0; i < keyArray.length; i++) {
                euidArray[i] = (String) keyArray[i];
            }

            Condition c = new Condition(euidPath, "IN", euidArray);
            queryObject.addCondition(c);

            AssembleDescriptor descriptor = new AssembleDescriptor();
            EOSearchResultAssembler factory = new EOSearchResultAssembler();
            descriptor.setAssembler(factory);
            descriptor.setResultValueObjectType(factory.getValueMetaNode(
                    objPath));
            queryObject.setAssembleDescriptor(descriptor);

            QMIterator iterator = mQuery.executeAssemble(queryObject);

            while (iterator.hasNext()) {
                EOSearchResultRecord rec = (EOSearchResultRecord) iterator.next();
                euidsToLoad.put(rec.getEUID(), rec.getObject());
            }

            iterator.close();

            for (int i = 0; i < transList.size(); i++) {
                PotentialDuplicateSummary pos 
                        = (PotentialDuplicateSummary) transList.get(i);
                ObjectNode obj = (ObjectNode) euidsToLoad.get(pos.getEUID1());
                pos.setObject1(obj);
                obj = (ObjectNode) euidsToLoad.get(pos.getEUID2());
                pos.setObject2(obj);
                if (pos.getObject1()!= null && pos.getObject2() != null){
                     PotentialDuplicateReportRow reportRow 
                        = new PotentialDuplicateReportRow(pos, mPotentialDuplicateReportConfig);
                     mPotentialDuplicateReport.addRow(reportRow);
                }
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetPotentialDuplicate.getStatement();
                stmt.close();
                mResultSetPotentialDuplicate = null;
                mPotentialDuplicateReport.setNoMore();
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE507: Could not retrieve the " + 
                                        "next set of Potential Duplicate records: {0}", e));
        }
        return mPotentialDuplicateReport;
    }

    /**
     * To get the assumed match records summary from the databse for the give
     * search object criteria.
     *
     * @param searchObj - Assumed Match Search Object
     * @return Assumed Match Records
     * @throws ReportException an error occured
     */
    private ResultSet lookupAssumedMatchRecords(AssumedMatchSearchObject searchObj, Connection conn)
            throws ReportException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer(BASE_SELECT_CLAUSE);
        ArrayList parameters = new ArrayList();

        if (searchObj.getCreateStartDate() != null) {
            sb.append(" and b.TIMESTAMP >= ?");
            parameters.add(searchObj.getCreateStartDate());
        }

        if (searchObj.getCreateEndDate() != null) {
            sb.append(" and b.TIMESTAMP <= ?");
            parameters.add(searchObj.getCreateEndDate());
        }
        try {
            if (mNumberConversion == null) {
                mNumberConversion = 
                    DBAdapter.getDBAdapterInstance().getVarcharToNumberConversion("a.WEIGHT");
            }
        } catch (OPSException e) {
            throw new ReportException(mLocalizer.t("RPE508: Could not retrieve the " + 
                                        "SQL statement for converting the Weight column: {0}", e));
        }
        sb.append(" order by "
                  + mNumberConversion 
                  + " desc, a.EUID asc");

        try {
            ps = conn.prepareStatement(sb.toString());
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ps.setMaxRows(searchObj.getMaxElements());
            rs = ps.executeQuery();
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE509: Could not retrieve " + 
                                                   "Assumed Match records: {0}", e));
        }
        return rs;            
    }
            
    private ArrayList loadAssumedMatchRecords(ResultSet rs, int count) 
                throws ReportException {
        
        ArrayList list = new ArrayList();
        int idx = 0;
        try {
            while (idx < count) {
                if (rs.next()) {
                    String id = rs.getString(1);
                    String weight = rs.getString(2);
                    String euid = rs.getString(3);
                    String createUser = rs.getString(4);
                    Date createDate = rs.getTimestamp(5);
                    String systemCode = rs.getString(6);
                    String lid = rs.getString(7);
                    String transactionNumber = rs.getString(8);
                    AssumedMatchSummary dupSum = new AssumedMatchSummary(id, euid,
                            systemCode, lid, createUser, createDate,
                            transactionNumber);
                    dupSum.setWeight(Float.parseFloat(weight));
                    list.add(dupSum);
                } else {
                    break;
                }
                idx++;
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE510: Could not load " + 
                                                   "Assumed Match records: {0}", e));
        }
        return list;
    }

    /**
     * To get the Assumed Match records page wise
     *
     * @return Next Assumed Match Records
     * @throws ReportException an error occured
     */
    public AssumedMatchReport getNextAssumedMatchRecords()
                throws ReportException {

        if (mResultSetAssumedMatch == null) {
            throw new ReportException(mLocalizer.t("RPE511: Assumed Match Report has not been generated."));
        }
        int pageSize = mAssumedMatchReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mAssumedMatchReport != null) {
                mAssumedMatchReport.clear();
            }
            Connection con = mResultSetAssumedMatch.getStatement().getConnection();
            ArrayList transList = loadAssumedMatchRecords(mResultSetAssumedMatch, pageSize);

            for (int i = 0; i < transList.size(); i++) {

                AssumedMatchSummary summary = (AssumedMatchSummary) transList.get(i);
                String transNum = summary.getTransactionNumber();
                RecreateResult history = mTrans.recreateObject(con, transNum);
                EnterpriseObject beforeEO = history.getBeforeEO1();
                EnterpriseObject afterEO = history.getAfterEO();
                SystemObject so = afterEO.getSystemObject(summary.getSystemCode(), 
                        summary.getLID());
                summary.setBeforeEO(beforeEO);
                summary.setAfterEO(afterEO);
                summary.setNewSO(so);

                AssumedMatchReportRow reportRow = new AssumedMatchReportRow(summary,
                            mAssumedMatchReportConfig);
                mAssumedMatchReport.addRow(reportRow);
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetAssumedMatch.getStatement();
                stmt.close();
                mResultSetAssumedMatch = null;
                mAssumedMatchReport.setNoMore();
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE512: Could not load the " + 
                                                   "next set of Assumed Match records: {0}", e));
        }
        return mAssumedMatchReport;
    }

    /**
     * To close the connection
     *
     * @param con Database Connection
     */
    private void releaseConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            mLogger.warn(mLocalizer.x("RPE003: BatchReportGeneratorImpl.releaseConnection(): " +
                                      "could not close JDBC connection: {0}", e.getMessage()));
        }
    }

    /**
     * Execute the assumed match report
     *
     * @param config assumed match report configuration
     * @param conn Database connection
     * @return AssumedMatchReport assumed match report
     * @throws ReportException an error occured
     */
    public AssumedMatchReport execAssumedMatchReport(AssumedMatchReportConfig config, Connection conn)
            throws ReportException {

        this.mAssumedMatchReportConfig = config;
        mAssumedMatchReport = new AssumedMatchReport(config);
        mAssumedMatchSearchObject = new AssumedMatchSearchObject();

        try {
            if (config.getMaxResultSize() != null) {
                mAssumedMatchSearchObject.setMaxElements(config.getMaxResultSize().intValue());
            }

            if (config.getStartDate() != null) {
                mAssumedMatchSearchObject.setCreateStartDate(new Timestamp(config.getStartDate().getTime()));
            }
            if (config.getEndDate() != null) {
                mAssumedMatchSearchObject.setCreateEndDate(new Timestamp(config.getEndDate().getTime()));
            }
 
            if (config.getSystemCode() != null) {
                mAssumedMatchSearchObject.setSystemCode(config.getSystemCode());
            }

            mResultSetAssumedMatch = lookupAssumedMatchRecords(mAssumedMatchSearchObject, conn);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE513: Could not generate the " + 
                                                   "Assumed Match report: {0}", e));
        }
        return getNextAssumedMatchRecords();
    }

    /**
     * Execute the potential duplicate report
     *
     * @param config potential duplicate report configuration
     * @param conn Database connection
     * @return potential duplicate report
     * @throws ReportException an error occured
     */
    public PotentialDuplicateReport execPotentialDuplicateReport(
        PotentialDuplicateReportConfig config, Connection conn)
            throws ReportException {
        
        this.mPotentialDuplicateReportConfig = config;
        mPotentialDuplicateReport = new PotentialDuplicateReport(config);
        mPotentialDuplicateSearchObject = new PotentialDuplicateSearchObject();

        try {

            if (config.getMaxResultSize() != null) {
                mPotentialDuplicateSearchObject.setMaxElements(config.getMaxResultSize().intValue());
            }

            if (config.getStartDate() != null) {
                mPotentialDuplicateSearchObject.setCreateStartDate(new Timestamp(
                        config.getStartDate().getTime()));
            }

            if (config.getEndDate() != null) {
                mPotentialDuplicateSearchObject.setCreateEndDate(new Timestamp(
                        config.getEndDate().getTime()));
            }

            if (config.getSystemCode() != null) {
                mPotentialDuplicateSearchObject.setSystemCode(config.getSystemCode());
            }

            if (config.getStatus() != null) {
                mPotentialDuplicateSearchObject.setStatus(config.getStatus());
            }

            if (config.getObjectFields() != null) {
                EPathArrayList fields = config.getObjectFields();
                EPathArrayList modFields = new EPathArrayList();
                String objName = ObjectFactory.getObjectDef().getName();
                modFields.add("Enterprise.SystemSBR." + objName + "." + objName
                    + "Id");
                modFields.add("Enterprise.SystemSBR." + objName + ".EUID");

                EPath[] epaths = fields.toArray();

                for (int i = 0; i < epaths.length; i++) {
                    modFields.add("Enterprise.SystemSBR."
                        + epaths[i].toString());
                }

                mPotentialDuplicateSearchObject.setFieldsToRetrieve(modFields);
            }

            mResultSetPotentialDuplicate = lookupPotDupRecords(mPotentialDuplicateSearchObject, conn);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE514: Could not generate the " + 
                                                   "Potential Duplicate report: {0}", e));
        }
        return getNextPotDupRecords();
    }

    /**
     * Execute the merge report
     *
     * @param config merge report configuration
     * @param conn database connection
     * @return merge Report
     * @throws ReportException an error occured
     */
    public MergeReport execMergeReport(MergeReportConfig config, Connection conn)
            throws ReportException {

        this.mMergeReportConfig = config;
        mMergeReport = new MergeReport(config);

        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            searchObj.setFunction("euidMerge");

            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }

            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            mResultSetMerge = lookupTransactions(searchObj, conn);
       } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE515: Could not generate the " + 
                                                   "Merge report: {0}", e));
        }
        return getNextMergeRecords();        
    }

    /**
     * To get the Assumed Match records page wise
     * @return Next Merge Records
     * @throws ReportException an error occured
     */
    public MergeReport getNextMergeRecords()
        throws ReportException {

        if (mResultSetMerge == null) {
            throw new ReportException(mLocalizer.t("RPE516: Merge Report has not been generated."));
        }
        int pageSize = mMergeReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mMergeReport != null) {
                mMergeReport.clear();
            }
            
            Connection con = mResultSetMerge.getStatement().getConnection();
            ArrayList transList = loadTransactionObjects(mResultSetMerge, pageSize);
            // Create TransactionSummary for each transaction object
            for (int i = 0; i < transList.size(); i++) {
                TransactionObject transObj = (TransactionObject) transList.get(i);
                TransactionSummary transSummary = new TransactionSummary(transObj);
                String transNo = transObj.getTransactionNumber();
                RecreateResult history = null;
                history = mTrans.recreateObject(con, transNo);

                transSummary.setEnterpriseObjectHistory(new EnterpriseObjectHistory(history));

                MergeReportRow reportRow = new MergeReportRow(transSummary, mMergeReportConfig);
                mMergeReport.addRow(reportRow);
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetMerge.getStatement();
                stmt.close();
                mResultSetMerge = null;
                mMergeReport.setNoMore();
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE517: Could not retrieve the " + 
                                        "next set of Merge records: {0}", e));
        }
        return mMergeReport;
    }

    /**
     * Execute the unmerge report
     * @param config Unmerge Report Configuration
     * @param conn Database connection
     * @return Unmerge Report
     * @throws ReportException an error occured
     */
    public UnmergeReport execUnmergeReport(UnmergeReportConfig config, Connection conn)
            throws ReportException{

        this.mUnmergeReportConfig = config;
        mUnmergeReport = new UnmergeReport(config);

        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            searchObj.setFunction("euidUnMerge");

            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }

            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            mResultSetUnmerge = lookupTransactions(searchObj, conn);
       } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE518: Could not generate the " + 
                                                   "Unmerge report: {0}", e));
        }
        return getNextUnmergeRecords();        
    }

    /**
     * To get the UnmergeReport records page wise
     * @return Next Unmerge Records
     * @throws ReportException an error occured
     */

    public UnmergeReport getNextUnmergeRecords()
        throws ReportException {
        
        if (mResultSetUnmerge == null) {
            throw new ReportException(mLocalizer.t("RPE519: Unmerge Report has not been generated."));
        }
        int pageSize = mUnmergeReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mUnmergeReport != null) {
                mUnmergeReport.clear();
            }
            
            Connection con = mResultSetUnmerge.getStatement().getConnection();
            ArrayList transList = loadTransactionObjects(mResultSetUnmerge, pageSize);
            // Create TransactionSummary for each transaction object
            for (int i = 0; i < transList.size(); i++) {
                TransactionObject transObj = (TransactionObject) transList.get(i);
                TransactionSummary transSummary = new TransactionSummary(transObj);
                String transNo = transObj.getTransactionNumber();
                RecreateResult history = null;
                history = mTrans.recreateObject(con, transNo);

                transSummary.setEnterpriseObjectHistory(new EnterpriseObjectHistory(history));

                UnmergeReportRow reportRow = new UnmergeReportRow(transSummary, mUnmergeReportConfig);
                mUnmergeReport.addRow(reportRow);
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetUnmerge.getStatement();
                stmt.close();
                mResultSetUnmerge = null;
                mUnmergeReport.setNoMore();
                //releaseConnection(con);
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE530: Could not retrieve the " + 
                                        "next set of Unmerge records: {0}", e));
        }

        return mUnmergeReport;
    }

    /**
     * Execute the deactivate report
     * @param config Deactivate Report Configuration
     * @param conn Database connection
     * @return Deactivate Report
     * @throws ReportException an error occured
     */
    public DeactivateReport execDeactivateReport(DeactivateReportConfig config, Connection conn)
            throws ReportException {

        this.mDeactivateReportConfig = config;
        mDeactivateReport= new DeactivateReport(config);

        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }

            searchObj.setFunction("euidDeactivate");

            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }

            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            mResultSetDeactivate = lookupTransactions(searchObj, conn);
       } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE520: Could not generate the " + 
                                                   "Deactivate report: {0}", e));
        }
        return getNextDeactivateRecords();        
    }

    /**
     * Execute the deactivate report
     * @return Next Deactivate Records
     * @throws ReportException an error occured
     */
    public DeactivateReport getNextDeactivateRecords()
        throws ReportException {
        
        if (mResultSetDeactivate == null) {
            throw new ReportException(mLocalizer.t("RPE521: Deactivate Report has not been generated."));
        }
        int pageSize = mDeactivateReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mDeactivateReport != null) {
                mDeactivateReport.clear();
            }
            
            Connection con = mResultSetDeactivate.getStatement().getConnection();
            ArrayList transList = loadTransactionObjects(mResultSetDeactivate, pageSize);
            // Create TransactionSummary for each transaction object
            for (int i = 0; i < transList.size(); i++) {
                TransactionObject transObj = (TransactionObject) transList.get(i);
                TransactionSummary transSummary = new TransactionSummary(transObj);
                String transNo = transObj.getTransactionNumber();
                RecreateResult history = null;
                history = mTrans.recreateObject(con, transNo);

                transSummary.setEnterpriseObjectHistory(new EnterpriseObjectHistory(history));

                DeactivateReportRow reportRow = new DeactivateReportRow(transSummary, mDeactivateReportConfig);
                mDeactivateReport.addRow(reportRow);
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetDeactivate.getStatement();
                stmt.close();
                mResultSetDeactivate = null;
                mDeactivateReport.setNoMore();
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE522: Could not retrieve the " + 
                                        "next set of Deactivate records: {0}", e));
        }

        return mDeactivateReport;
    }

    private ResultSet lookupTransactions(TransactionSearchObject searchObj, Connection conn)
		throws ReportException {
        ResultSet rs = null;
        
        StringBuffer queryStr = new StringBuffer(TRANSACTION_SELECT_CLAUSE);
        Date fromDate = searchObj.getStartDate();
        Date toDate = searchObj.getEndDate();
        
        if ((fromDate != null) && (toDate == null)) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
            queryStr.append(" timestamp >= to_date('").append(sdf.format(fromDate));
            queryStr.append("', '").append(DBDATEFORMAT).append("')");
        } else if ((fromDate == null) && (toDate != null)) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
            queryStr.append(" timestamp <= to_date('").append(sdf.format(toDate));
            queryStr.append("', '").append(DBDATEFORMAT).append("')");
        } else if ((fromDate != null) && (toDate != null)) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
            queryStr.append(" timestamp >= to_date('").append(sdf.format(fromDate));
            queryStr.append("', '").append(DBDATEFORMAT).append("')");
            queryStr.append(" and timestamp <= to_date('").append(sdf.format(toDate));
            queryStr.append("', '").append(DBDATEFORMAT).append("')");
        }
        try {
            String function = searchObj.getFunction();
            if (function != null) {
                queryStr.append(" and function = '").append(function).append("'");
            }
            String systemCode = searchObj.getSystemCode();
            if (systemCode != null) {
                queryStr.append(" and systemcode = '").append(systemCode).append("'");
            }
         } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE523: Could not retrieve Transaction records: {0}", e));
        }
        queryStr.append(" order by euid, timestamp");
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("BatchReportGeneratorImpl.lookupTransactions() SQL " +
                         "string is: " + queryStr.toString());
        }
        
        try {
            Statement ps = conn.createStatement();
            ps.setMaxRows(searchObj.getMaxElements());
            rs = ps.executeQuery(queryStr.toString());
         } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE524: Could not retrieve Transaction records: {0}", e));
        }
        return rs;
   }
    
    /**
     * Get the next part of the Update Report
     * @return Next Update Records
     * @throws ReportException an error occured 
     */
    public UpdateReport getNextUpdateRecords()
                throws ReportException {

        if (mResultSetUpdate == null) {
            throw new ReportException(mLocalizer.t("RPE525: Update Report has not been generated."));
        }
        int pageSize = mUpdateReportConfig.getPageSize().intValue();
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        
        try {
            if (mUpdateReport != null) {
                mUpdateReport.clear();
            }
            Connection con = mResultSetUpdate.getStatement().getConnection();
            ArrayList transList = loadTransactionObjects(mResultSetUpdate, pageSize);

                
            // Create TransactionSummary for each transaction object
            for (int i = 0; i < transList.size(); i++) {
                TransactionObject transObj = (TransactionObject) transList.get(i);
                TransactionSummary transSummary = new TransactionSummary(transObj);
                String transNo = transObj.getTransactionNumber();
                RecreateResult history = null;
                history = mTrans.recreateObject(con, transNo);

                transSummary.setEnterpriseObjectHistory(new EnterpriseObjectHistory(history));

                UpdateReportRow reportRow = new UpdateReportRow(transSummary, mUpdateReportConfig);
                mUpdateReport.addRow(reportRow);
            }
            if (transList.size() < pageSize) {
                Statement stmt = mResultSetUpdate.getStatement();
                stmt.close();
                mResultSetUpdate = null;
                mUpdateReport.setNoMore();
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE526: Could not retrieve the " + 
                                        "next set of Update records: {0}", e));
        }

        return mUpdateReport;
    }

    private ArrayList loadTransactionObjects(ResultSet rSet, int count) 
                throws ReportException {
        
        ArrayList list = new ArrayList();
        int idx = 0;
        try {
            while (rSet.next() && idx < count) {
                TransactionObject tObj = new TransactionObject();
                
                tObj.setTransactionNumber(rSet.getString("TransactionNumber"));
                tObj.setLID1(rSet.getString("LID1"));
                tObj.setLID2(rSet.getString("LID2"));
                tObj.setEUID1(rSet.getString("EUID1"));
                tObj.setEUID2(rSet.getString("EUID2"));
                tObj.setSystemUser(rSet.getString("SystemUser"));
                tObj.setFunction(rSet.getString("Function"));
                tObj.setTimeStamp(rSet.getTimestamp("TimeStamp"));
                tObj.setSystemCode(rSet.getString("SystemCode"));
                tObj.setLID(rSet.getString("LID"));
                tObj.setEUID(rSet.getString("EUID"));
                tObj.reset();
                list.add(tObj);
                idx++;
            }
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE527: Could not load the " + 
                                        "Transaction objects: {0}", e));
        }
        return list;
    }

    /**
     * Execute the update report
     * @param config Update Report Configuration
     * @param conn Database connection
     * @return update Report
     * @throws ReportException an error occured
     */
    public UpdateReport execUpdateReport(UpdateReportConfig config, Connection conn)
            throws ReportException {
        this.mUpdateReportConfig = config;
        mUpdateReport = new UpdateReport(config);

        try {
            TransactionSearchObject searchObj = new TransactionSearchObject();

            if (config.getMaxResultSize() != null) {
                searchObj.setMaxElements(config.getMaxResultSize().intValue());
            }
            searchObj.setFunction("Update");
            if (config.getStartDate() != null) {
                searchObj.setStartDate(config.getStartDate());
            }
            if (config.getEndDate() != null) {
                searchObj.setEndDate(config.getEndDate());
            }
            if (config.getSystemCode() != null) {
                searchObj.setSystemCode(config.getSystemCode());
            }
            mResultSetUpdate = lookupTransactions(searchObj, conn);
       } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE528: Could not generate the " + 
                                                   "Update report: {0}", e));
        }
        return getNextUpdateRecords();        
    }

    /**
     * Execute the weekly key statistics report
     *
     * @param config Key Statistics Report Configuration
     * @param conn Database connection
     * @return Weekly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execWeeklyKeyStatisticsReport(
        KeyStatisticsReportConfig config, Connection conn)
        throws ReportException {
        KeyStatisticsReport report = new KeyStatisticsReport(config);

        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;

            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
                throw new ReportException(mLocalizer.t("RPE529: Start or end " + 
                                "date must be specified for the Weekly Statistics Report."));
            }

            // Determine first date of week, Sunday's date, based on specified date
            Date startDate = DateUtil.getFirstDateOfWeek(specDate);
            report.setStartDate(startDate);

            String function = KeyStatisticsReport.ADD_FUNCTION;
            report.setAddCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.UPDATE_FUNCTION;
            report.setUpdateCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.DEACTIVATE_FUNCTION;
            report.setDeactivateCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.EUID_MERGE_FUNCTION;
            report.setEuidMergeCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.EUID_UNMERGE_FUNCTION;
            report.setEuidUnmergeCountsForWeek(getWeeklyCount(startDate,
                    function, conn));

            function = KeyStatisticsReport.LID_MERGE_FUNCTION;
            report.setLidMergeCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.LID_UNMERGE_FUNCTION;
            report.setLidUnmergeCountsForWeek(getWeeklyCount(startDate, function, conn));

            function = KeyStatisticsReport.LID_TRANSFER_FUNCTION;
            report.setLidTransferCountsForWeek(getWeeklyCount(startDate,
                    function, conn));

            report.setDailyTotalsForWeek(getWeeklyCount(startDate, conn));
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE531: Could not generate the " + 
                                                   "Weekly Statistics report: {0}", e));
        }

        return report;
    }

    /**
     * Execute the monthly key statistics report
     *
     * @param config Key Statistics Report Configuration
     * @param conn Database connection
     * @return Monthly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execMonthlyKeyStatisticsReport(
        KeyStatisticsReportConfig config, Connection conn)
        throws ReportException {
        KeyStatisticsReport report = new KeyStatisticsReport(config);

        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;

            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
                throw new ReportException(mLocalizer.t("RPE532: Start or end date must be specified " + 
                                            "for the Monthly Statistics report."));
            }

            // Determine first date of month based on specified date
            Date startDate = DateUtil.getFirstDateOfMonth(specDate);
            report.setStartDate(startDate);

            // Determine last date of month based on specified date
            Date endDate = DateUtil.getLastDateOfMonth(specDate);

            // Get key stats totals
            report = getKeyStatsReportTotals(report, startDate, endDate, conn);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE533: Could not generate the " + 
                                                   "Monthly Statistics report: {0}", e));
        }

        return report;
    }

    /**
     * Execute the yearly key statistics report
     *
     * @param config Key Statistics Report Configuration
     * @param conn Database connection
     * @return Yearly key statistics report
     * @throws ReportException an error occured
     */
    public KeyStatisticsReport execYearlyKeyStatisticsReport(
        KeyStatisticsReportConfig config, Connection conn)
        throws ReportException {
        KeyStatisticsReport report = new KeyStatisticsReport(config);

        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            Date specDate = null;

            if (config.getStartDate() != null) {
                specDate = config.getStartDate();
            } else if (config.getEndDate() != null) {
                specDate = config.getEndDate();
            } else {
                throw new ReportException(mLocalizer.t("RPE534: Start or end date must be specified " + 
                                            "for the Yearly Statistics report."));
            }

            // Determine first date of year based on specified date
            Date startDate = DateUtil.getFirstDateOfYear(specDate);
            report.setStartDate(startDate);

            // Determine last date of year based on specified date
            Date endDate = DateUtil.getLastDateOfYear(specDate);

            // Get key stats totals
            report = getKeyStatsReportTotals(report, startDate, endDate, conn);
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE535: Could not generate the " + 
                                                   "Yearly Statistics report: {0}", e));
        }

        return report;
    }

    /**
     * Get JDBC connection
     *
     * @return JDBC connection from pool.
     * @throws ReportException An error occured.
     */
    private Connection getConnection() throws ReportException {
        try {
            Connection con = ConnectionUtil.getConnection();
            return con;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE536: Failed to get JDBC connection: {0}", e));
        }
    }

    /**
     * Get key statistics report information for specified dates
     *
     * @param report Key statistics report to set values for
     * @param startDate start date of report
     * @param endDate end date of report
     * @return Key Statistics Report for specified dates
     */
    private KeyStatisticsReport getKeyStatsReportTotals(
        KeyStatisticsReport report, Date startDate, Date endDate, Connection conn)
        throws ReportException {
        String function = KeyStatisticsReport.ADD_FUNCTION;
        report.setAddCount(getCount(startDate, endDate, function, conn));

        function = KeyStatisticsReport.DEACTIVATE_FUNCTION;
        report.setDeactivateCount(getCount(startDate, endDate, function, conn));

        function = KeyStatisticsReport.EUID_MERGE_FUNCTION;
        report.setEuidMergeCount(getCount(startDate, endDate, function, conn));

        function = KeyStatisticsReport.EUID_UNMERGE_FUNCTION;
        report.setEuidUnmergeCount(getCount(startDate, endDate, function, conn));

        function = KeyStatisticsReport.LID_MERGE_FUNCTION;
        report.setLidMergeCount(getCount(startDate, endDate, function, conn));

        function = KeyStatisticsReport.LID_UNMERGE_FUNCTION;
        report.setLidUnmergeCount(getCount(startDate, endDate, function, conn));

        boolean resolved = false;
        report.setUnresolvedPotDupCount(getCount(startDate, endDate, resolved, conn));

        resolved = true;
        report.setResolvedPotDupCount(getCount(startDate, endDate, resolved, conn));

        return report;
    }

    // Return an ArrayList of counts for each day of the week of function
    private ArrayList getWeeklyCount(Date startDate, String function, Connection conn)
        throws ReportException {
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            int weeklyTotal = 0;
            Date dateN = startDate;

            for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
                count = statsDb.getDailyCountByFunction(conn, dateN, function);
                weeklyCount.add(new Integer(count));

                // Add up weekly total
                weeklyTotal += count;

                // Set next date
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dateN);
                cal.add(Calendar.DATE, 1);
                dateN = cal.getTime();
            }

            // Add weekly total to ArrayList
            weeklyCount.add(new Integer(weeklyTotal));
            return weeklyCount;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE537: Error getting weekly count for function : {0}", function));
        }
    }

    // Return an ArrayList of counts for each day of the week
    private ArrayList getWeeklyCount(Date startDate, Connection conn) throws ReportException {
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;
            int weeklyTotal = 0;
            Date dateN = startDate;

            for (int i = 0; i < NUM_DAYS_IN_WEEK; i++) {
                count = statsDb.getDailyCount(conn, dateN);
                weeklyCount.add(new Integer(count));

                // Add up weekly total
                weeklyTotal += count;

                // Set next date
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dateN);
                cal.add(Calendar.DATE, 1);
                dateN = cal.getTime();
            }

            // Add weekly total to ArrayList
            weeklyCount.add(new Integer(weeklyTotal));
            
            return weeklyCount;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE538: Error getting weekly count: {0}", e));
        }
    }

    // Return count for specified start and end dates and function
    private int getCount(Date startDate, Date endDate, String function, Connection conn)
        throws ReportException {
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;

            count = statsDb.getCountByDatesNFunction(conn, startDate, endDate,
                    function);
            
            return count;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE539: Error getting count for function : {0}", function));
        }
    }

    /*
     * Return count for potential duplicates, resolved or unresolved
     * specified by boolean and start and end dates
     */
    private int getCount(Date startDate, Date endDate, boolean resolved, Connection conn)
        throws ReportException {
        try {
            KeyStatisticsReportDB statsDb = new KeyStatisticsReportDB();
            ArrayList weeklyCount = new ArrayList();
            int count = 0;

            if (resolved) {
                count = statsDb.getResolvedPotDupByDates(conn, startDate,
                        endDate);
            } else {
                count = statsDb.getUnresolvedPotDupByDates(conn, startDate,
                        endDate);
            }

            return count;
        } catch (Exception e) {
            throw new ReportException(mLocalizer.t("RPE540: Error getting the " + 
                                "potential duplicate count for function : {0}", 
                                (resolved ? "resolved" : "unresolved")));
        }
    }
}
