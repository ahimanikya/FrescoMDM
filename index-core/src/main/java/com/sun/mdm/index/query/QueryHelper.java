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
package com.sun.mdm.index.query;
 
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.sun.mdm.index.objects.SystemObjectPK;
import com.sun.mdm.index.master.SystemDefinition;
import com.sun.mdm.index.master.ProcessingException;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.ops.DBAdapter;
import com.sun.mdm.index.util.Localizer;

/**
 * Performs basic straight JDBC calls for statically defined tables
 * @author Daniel
 */
public class QueryHelper {

    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of QueryHelper
     */
    public QueryHelper() {
    }

    private static final String GET_EUID = "SELECT e.EUID FROM SBYN_ENTERPRISE e, " 
                   + "SBYN_SYSTEMOBJECT s WHERE e.SYSTEMCODE=? AND e.LID=? AND " 
                   + "s.status=? AND s.SYSTEMCODE=e.SYSTEMCODE AND " 
                   + "s.LID=e.LID";

    private static final String GET_EUID_NO_STATUS = "SELECT e.EUID FROM SBYN_ENTERPRISE e, " 
                   + "SBYN_SYSTEMOBJECT s WHERE e.SYSTEMCODE=? AND e.LID=? AND " 
                   + "s.SYSTEMCODE=e.SYSTEMCODE AND s.LID=e.LID";    
    
    private static final String GET_SO_STATUS = "SELECT s.STATUS FROM  " 
                   + "SBYN_ENTERPRISE e, SBYN_SYSTEMOBJECT s WHERE e.SYSTEMCODE=? " 
                   + "AND e.LID=? AND s.SYSTEMCODE=e.SYSTEMCODE AND s.LID=e.LID";    

    // SQL command for retrieving system codes
    private static final String GET_SYSTEM_CODE = "SELECT SYSTEMCODE FROM SBYN_SYSTEMS";
    
    private static final String LOOKUP_SYSTEM_PK = "SELECT e.SYSTEMCODE, e.LID "
                    + "FROM SBYN_ENTERPRISE e, SBYN_SYSTEMOBJECT s WHERE e.EUID=? "
                    + "AND s.status=? AND s.SYSTEMCODE=e.SYSTEMCODE "
                    + "AND s.LID=e.LID";

    private static final String LOOKUP_CROSS_SYSTEM_PK = "SELECT e.SYSTEMCODE, e.LID "
                    + "FROM SBYN_ENTERPRISE e, SBYN_SYSTEMOBJECT s WHERE e.EUID=? "
                    + "AND s.status=? AND s.SYSTEMCODE=e.SYSTEMCODE "
                    + "AND s.LID=e.LID AND s.SYSTEMCODE=?";    
    
    private static final String LOOKUP_SYSTEM_DEFS = "SELECT SYSTEMCODE, DESCRIPTION, " 
            + "STATUS, ID_LENGTH, FORMAT, INPUT_MASK, VALUE_MASK, CREATE_DATE, "
            + "CREATE_USERID, UPDATE_DATE, UPDATE_USERID FROM SBYN_SYSTEMS ORDER BY SYSTEMCODE";

    // SQL command for looking up a description for a system code
    private static final String LOOKUP_SYSTEM_DEF = "SELECT SYSTEMCODE, DESCRIPTION, " 
            + "STATUS, ID_LENGTH, FORMAT, INPUT_MASK, VALUE_MASK, CREATE_DATE, "
            + "CREATE_USERID, UPDATE_DATE, UPDATE_USERID FROM SBYN_SYSTEMS "
            + "WHERE SYSTEMCODE=?";
    
    // retrieve a SBRrevision number for an EUID
    private static final String GET_REVISION_NUMBER = "select revisionnumber "
            + "from sbyn_systemsbr where euid = ?";
    
    /**
     * Given a system object key, return its status. If status is not found, 
     * return null.
     *
     * @param con Connection
     * @param key SystemObjectPK
     * @return status
     * @exception QMException QMException
     */ 
    public String getSOStatus(Connection con, SystemObjectPK key)
        throws QMException {
        String status = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(GET_SO_STATUS);
            ps.setString(1, key.systemCode);
            ps.setString(2, key.lID);

            rs = ps.executeQuery();

            if (rs.next()) {
                status = rs.getString(1);
            }

        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE517: Could not retreive the " +
                                            "SystemObject status:{0}", e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE518: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }
        return status;
    }

    /**
     * Given a system object key, lookup its EUID. If EUID not found, return
     * null.
     *
     * @param con Connection
     * @param key SystemObjectPK
     * @param status status
     * @return EUID
     * @exception QMException QMException
     */ 
    public String getEUID(Connection con, SystemObjectPK key, String status)
        throws QMException {
        String euid = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(GET_EUID);
            ps.setString(1, key.systemCode);
            ps.setString(2, key.lID);
            ps.setString(3, status);

            rs = ps.executeQuery();

            if (rs.next()) {
                euid = rs.getString(1);

                if (rs.next()) {
                    rs.close();
                    ps.close();
                    throw new QMException(mLocalizer.t("QUE519: More than one " +
                                            "EUID found for the system key: {0}", key.toString()));
                }
            }

        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE520: Could not retrieve the EUID" +
                                  "for the system key: {0}:{1}", key.toString(), e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE521: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }
        return euid;
    }

   /**
     * Given a system object key, lookup its EUID. If EUID not found, return
     * null. 
     *
     * @param con Connection
     * @param key SystemObjectPK
     * @return EUID
     * @exception QMException QMException
     */ 
    public String getEUID(Connection con, SystemObjectPK key)
        throws QMException {
        String euid = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(GET_EUID_NO_STATUS);
            ps.setString(1, key.systemCode);
            ps.setString(2, key.lID);

            rs = ps.executeQuery();

            if (rs.next()) {
                euid = rs.getString(1);

                if (rs.next()) {
                    rs.close();
                    ps.close();
                    throw new QMException(mLocalizer.t("QUE522: More than one " +
                                            "EUID found for the system key: {0}", key.toString()));
                }
            }

        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE523: Could not retrieve the EUID " +
                                  "for the system key: {0}:{1}", key.toString(), e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE524: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }
        return euid;
    }

    /**
     *return System codes
     * @param con Connection
     * @return String[] array of systemcodes 
     * @exception QMException QMException
     * 
     */
    public String[] lookupSystemCodes(Connection con)
        throws QMException {
        ArrayList retList = new ArrayList();
        String[] retArray = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT SYSTEMCODE FROM SBYN_SYSTEMS";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String systemCode = rs.getString(1);
                retList.add(systemCode);
            }

            int size = retList.size();

            if (size > 0) {
                retArray = new String[size];

                for (int i = 0; i < size; i++) {
                    retArray[i] = (String) retList.get(i);
                }
            }

        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE525: Could not lookup the " +
                                            "System Codes: {0}", e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE526: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return retArray;
    }

    /**
     *return System codes
     * @param con Connection
     * @return String[] array of systemcodes 
     * @exception QMException QMException
     * 
     */
    public SystemDefinition[] lookupSystemDefinitions(Connection con)
        throws QMException {
        ArrayList retList = new ArrayList();
        SystemDefinition[] retArray = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(LOOKUP_SYSTEM_DEFS);
            rs = ps.executeQuery();

            while (rs.next()) {
                SystemDefinition sysDef = new SystemDefinition();
                sysDef.setSystemCode(rs.getString(1));
                sysDef.setDescription(rs.getString(2));
                sysDef.setStatus(rs.getString(3));
                sysDef.setIdLength(rs.getInt(4));
                sysDef.setFormat(rs.getString(5));
                sysDef.setInputMask(rs.getString(6));
                sysDef.setValueMask(rs.getString(7));
                sysDef.setCreateDate(rs.getTimestamp(8));
                sysDef.setCreateUserId(rs.getString(9));
                sysDef.setUpdateDate(rs.getTimestamp(10));
                sysDef.setUpdateUserId(rs.getString(11));
                retList.add(sysDef);
            }

            int size = retList.size();

            if (size > 0) {
                retArray = new SystemDefinition[size];

                for (int i = 0; i < size; i++) {
                    retArray[i] = (SystemDefinition) retList.get(i);
                }
            }

        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE527: Could not lookup the " +
                                            "System Definitions: {0}", e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE528: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return retArray;
    }
    
    /**
     * Return system definition for a specific system code
     *
     * @param con Connection
     * @param systemCode System Code to look up
     * @return SystemDefinition  System definition
     * @exception QMException QMException
     * 
     */
    public SystemDefinition lookupSystemDefinition(Connection con, String systemCode)
        throws QMException {
        
        SystemDefinition sysDef = null;
        int count = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = con.prepareStatement(LOOKUP_SYSTEM_DEF);
            ps.setString(1, systemCode);
            rs = ps.executeQuery();

            while (rs.next()) {
                sysDef = new SystemDefinition();
                sysDef.setSystemCode(rs.getString(1));
                sysDef.setDescription(rs.getString(2));
                sysDef.setStatus(rs.getString(3));
                sysDef.setIdLength(rs.getInt(4));
                sysDef.setFormat(rs.getString(5));
                sysDef.setInputMask(rs.getString(6));
                sysDef.setValueMask(rs.getString(7));
                sysDef.setCreateDate(rs.getTimestamp(8));
                sysDef.setCreateUserId(rs.getString(9));
                sysDef.setUpdateDate(rs.getTimestamp(10));
                sysDef.setUpdateUserId(rs.getString(11)); 
                count++;
            }           
            if (count > 1) {
               throw new QMException(mLocalizer.t("QUE529: Found more than one " +
                                            "System with the same system code."));
            }
        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE530: Could not lookup " +
                                            "System Definition."));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE531: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return sysDef;
    }
    
    
    /**
     * Retrieve active SystemObject keys. If no keys found, return null.
     *
     * @param euid EUID
     * @param con Connection 
     * @return SystemObjectPK[]
     * @exception QMException QMException
     */
    public SystemObjectPK[] lookupSystemObjectKeys(Connection con, String euid)
        throws QMException {
        return lookupSystemObjectKeys(con, euid, "active");
    }


    /**
     * return SystemObjectKeys
     * @param euid EUID
     * @param status status
     * @param con Connection 
     * @return SystemObjectPK[]
     * @exception QMException QMException
     * 
     */
    public SystemObjectPK[] lookupSystemObjectKeys(Connection con, String euid,
            String status)
        throws QMException {
        ArrayList retList = new ArrayList();
        SystemObjectPK[] retArray = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = con.prepareStatement(LOOKUP_SYSTEM_PK);
            ps.setString(1, euid);
            ps.setString(2, status);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                String systemCode = rs.getString(1);
                String lid = rs.getString(2);
                SystemObjectPK key = new SystemObjectPK(systemCode, lid);
                retList.add(key);
            }

            int size = retList.size();

            if (size > 0) {
                retArray = new SystemObjectPK[size];

                for (int i = 0; i < size; i++) {
                    retArray[i] = (SystemObjectPK) retList.get(i);
                }
            }
        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE532: Could not lookup the " +
                                            "SystemObjectKeys: {0}", e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE533: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return retArray;
    }
    
    /**
     * Performs cross lookup
     * @param sourceSystem the source system
     * @param sourceLID the source local id
     * @param destSystem the destination system
     * @param status status of records in destination system to search for
     * @param con Connection 
     * @return array of system keys for given system or null if none found
     * @exception QMException QMException
     * 
     */
    public SystemObjectPK[] lookupSystemObjectKeys(Connection con, String sourceSystem,
            String sourceLID, String destSystem, String status)
        throws QMException {    
    
        SystemObjectPK sourceKey = new SystemObjectPK(sourceSystem, sourceLID);
        String euid =  getEUID(con, sourceKey);
        if (euid == null) {
            throw new QMException(mLocalizer.t("QUE534: No EUID found for System" +
                                            "={0}, Local ID={1}", sourceSystem, sourceLID));
        }

        ArrayList retList = new ArrayList();
        SystemObjectPK[] retArray = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(LOOKUP_CROSS_SYSTEM_PK);
            ps.setString(1, euid);
            ps.setString(2, status);
            ps.setString(3, destSystem);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                String systemCode = rs.getString(1);
                String lid = rs.getString(2);
                SystemObjectPK key = new SystemObjectPK(systemCode, lid);
                retList.add(key);
            }

            int size = retList.size();

            if (size > 0) {
                retArray = new SystemObjectPK[size];

                for (int i = 0; i < size; i++) {
                    retArray[i] = (SystemObjectPK) retList.get(i);
                }
            }
        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE535: Could not lookup" +
                                            "SystemObjectKeys: {0}", e));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE536: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return retArray;
    }
     
    /**
     * Retrieves array of immediate LIDs by a given LID to which they are merged
     * @param conn jdbc connection
     * @param lid LocalID
     * @return String[]
     */
    private String[] findMergedToLID(Connection conn, String lid) throws QMException {

        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        Timestamp maxMergeTimestamp = null;
        Timestamp maxUnmergeTimestamp = null;
        long mtnm = 0;
        long mtnu = 0;
        String ret[] = null;
        ResultSet rSet1 = null;
        ResultSet rSet2 = null;
        try {
                String sql = DBAdapter.getDBAdapterInstance().getQHelperMergeMaxTimestampStmt();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, lid);
                rSet1 = stmt.executeQuery();
                
                if (null != rSet1) {
                    ArrayList list = new ArrayList();
                   
                    while (rSet1.next()) {
                         String lidFound = rSet1.getString("LID2".toUpperCase());
                         if (rSet1.wasNull()) {
                           lidFound = null;
                         }
                         maxMergeTimestamp=rSet1.getTimestamp("MTNM".toUpperCase());
                         if (maxMergeTimestamp != null)
                              mtnm=maxMergeTimestamp.getTime();
                        
                         if (rSet1.wasNull()) {
                           mtnm = 0;
                         } 
                         if (mtnm != 0) {
                             sql = DBAdapter.getDBAdapterInstance().getQHelperUnmergeMaxTimestampStmt();
                             stmt1 = conn.prepareStatement(sql);
                             stmt1.setString(1, lid);
                             stmt1.setString(2, lidFound);
                             rSet2 = stmt1.executeQuery();
                             
                             while (rSet2.next()) {
                                maxUnmergeTimestamp=rSet2.getTimestamp("MTNU".toUpperCase());
                                 if (maxUnmergeTimestamp != null)
                                       mtnu = maxUnmergeTimestamp.getTime();
                                if (rSet2.wasNull()) {
                                  mtnu = 0;
                                }
                             }
                             try {
                                stmt1.close(); 
                             } catch (SQLException e1) {
                                 throw new QMException(mLocalizer.t("QUE537: Could not close the " +
                                            "SQL statement: {0}", e1));
                             }
                         }
                         if (mtnm != 0) {
                             if (mtnu == 0) {
                                 list.add(lidFound);
                             } else {
                                 if (mtnm > mtnu) {
                                    list.add(lidFound);
                                 }
                             }                                  
                         }
                   }
               

                    if (list.size() > 0) {
                        ret = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            ret[i] = (String) list.get(i);
                        }
                    }
                }
            
        } catch (Exception e) {
            throw new QMException(mLocalizer.t("QUE538: Could not find the " +
                                            "\"merged to\" Local ID: {0}", e));
        } finally {
            try {
                if (rSet1 != null) {
                    rSet1.close();
                }
                if (rSet2 != null) {
                    rSet2.close();
                }
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE539: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return ret;
    }
    
    

    /**
     * Deep searches merged LIDs to find the more than one level of graph
     * @param conn jdbc connection
     * @param mergedLIDs list of all LIDs merged with 
     * @param alist holds the entire graph but the first level
     */
    private void  deepSearchMergedLIDs(Connection conn, String[] mergedLIDs, ArrayList alist)
        throws QMException {  
        
        if (mergedLIDs == null)  {
            return;
        } else {         
            try {
                for (int i=0; i < mergedLIDs.length; i++) {
                    
                    String[] newMergedLIDs=findMergedToLID(conn, mergedLIDs[i]);
                    if (newMergedLIDs != null) {
                        for (int j=0; j < newMergedLIDs.length; j++)
                            alist.add(newMergedLIDs[j]);
                    }
                    deepSearchMergedLIDs(conn, newMergedLIDs, alist);
                }
            } catch (QMException e) {
                throw new QMException(mLocalizer.t("QUE540: findMergedToLID() failed " +
                                            "at deepSearchMergedLIDs(): {0}", e));
            } 
            return;
        }
 
    }
    
    /**
     * Deep searches merged LIDs to find the complete graph
     * @param conn jdbc connection
     * @param mergedLIDs list of all LIDs merged with     
     */
    public String[]  findAllMergedLIDs(Connection conn, String lid) throws QMException {  
        
        String ret[] = null;
        ArrayList alist=new ArrayList();
        String[] mergedLIDs=null;
        
        try {
          mergedLIDs=findMergedToLID(conn, lid);
        } catch (QMException e) {
            throw new QMException(mLocalizer.t("QUE541: findMergedToLID() failed " +
                                            "at findAllMergedLIDs(): {0}", e));
        } 
        if (mergedLIDs != null)  {
            for (int i=0; i < mergedLIDs.length; i++) {
                alist.add(mergedLIDs[i]);
            }
            try {
              deepSearchMergedLIDs(conn, mergedLIDs, alist);
            } catch (QMException e) {
                    throw new QMException(mLocalizer.t("QUE542: findMergedToLID() failed " +
                                            "at findAllMergedLIDs(): {0}", e));
                }   
            if (alist.size() > 0) {
                        ret = new String[alist.size()];
                        for (int i = 0; i < alist.size(); i++) {
                            ret[i] = (String) alist.get(i);
                        }
                }
                return ret;
        } else {
            return null;
        } 
    }
    /**
     * Retrieves the SBR revision number for an EUID
     *
     * @param conn jdbc connection
     * @param euid EUID to check
     * @return SBR revision number for the EUID
     * @throws QMException if more than one revision number is found.
     */
    public Integer getRevisionNumber(Connection con, String euid)
        throws QMException {
            
        if (euid == null) {
            return null;
        }

        Integer revisionNumber = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(GET_REVISION_NUMBER);
            ps.setString(1, euid);

            rs = ps.executeQuery();

            if (rs.next()) {
                revisionNumber = new Integer(rs.getInt(1));

                if (rs.next()) {
                    throw new QMException(mLocalizer.t("QUE543: More than one " +
                                            "revision number found for EUID: {0}", euid));
                }
            }
        } catch (SQLException e) {
            throw new QMException(mLocalizer.t("QUE544: Could not retrieve the " +
                                            "revision number found for EUID: {0}", euid));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new QMException(mLocalizer.t("QUE545: Could not close the " +
                                            "prepared statement or the result set: {0}", e));
            }
        }

        return revisionNumber;
    }
    
}
    
   
