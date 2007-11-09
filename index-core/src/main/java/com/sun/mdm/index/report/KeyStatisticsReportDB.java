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
package com.sun.mdm.index.report;

import com.sun.mdm.index.ops.ObjectPersistenceService;
import com.sun.mdm.index.ops.DBAdapter;
import com.sun.mdm.index.ops.exception.OPSException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Key Statistics Report DB class for retrieving statistic data from database
 */
public class KeyStatisticsReportDB extends ObjectPersistenceService {	

    private static String mGetCountByDatesNFunction;
    private static String mGetCountByDates;
    private static String mGetUnresolvedPotDupCountByDates;
    private static String mGetResolvedPotDupCountByDates;

    
    static {
       mGetCountByDatesNFunction = null;
       
       mGetCountByDates = 
              "       select \n" 
            + "               count(*) \n" 
            + "       from \n" 
            + "               sbyn_transaction \n" 
            + "       where \n" 
            + "               timestamp >= ? and \n" 
            + "               timestamp < ? + 1 \n";
            
       mGetUnresolvedPotDupCountByDates = 
              "       select \n" 
            + "               count(*) \n" 
            + "       from \n" 
            + "               sbyn_potentialduplicates a, sbyn_transaction b \n" 
            + "       where \n" 
            + "               a.transactionnumber = b.transactionnumber and \n"
            + "               a.status = 'U' and \n"           
            + "               b.timestamp >= ? and \n" 
            + "               b.timestamp < ? + 1 \n";
            
       mGetResolvedPotDupCountByDates = 
              "       select \n" 
            + "               count(*) \n" 
            + "       from \n" 
            + "               sbyn_potentialduplicates a, sbyn_transaction b \n" 
            + "       where \n" 
            + "               a.transactionnumber = b.transactionnumber and \n"
            + "               a.status != 'U' and \n"           
            + "               b.timestamp >= ? and \n" 
            + "               b.timestamp < ? + 1 \n";                        
    }

    /**
     * default constructor
     */
    public KeyStatisticsReportDB() throws OPSException {
        super();
    }

    /**
     * Retrieves transaction count by Function and transaction date.
     *
     * @param conn JDBC connection handle.
     * @param transDate Date of transaction.
     * @param function Function of transaction.
     * @throws OPSException if an error is encountered.
     * @return Number of transactions specified by function and timestamp.
     */
    public int getDailyCountByFunction(Connection conn, Date transDate, String function)
            throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            if (mGetCountByDatesNFunction == null) {
                mGetCountByDatesNFunction 
                        = DBAdapter.getDBAdapterInstance().getKeyStatDBCountBetweenTimesStmt();
            }
            stmt = getStatement(mGetCountByDatesNFunction, conn);
            stmt.setString(1, function);
            setParam(stmt, 2, "Timestamp", transDate);
            setParam(stmt, 3, "Timestamp", transDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(function);
            params.add(transDate);

            if (mGetCountByDatesNFunction == null) {
                mGetCountByDatesNFunction 
                        = DBAdapter.getDBAdapterInstance().getKeyStatDBCountBetweenTimesStmt();
            }
            String sql = sql2str(mGetCountByDatesNFunction, params);
            throw new OPSException(sql + e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }
    
    /**
     * Retrieves transaction count by transaction date.
     *
     * @param conn JDBC connection handle.
     * @param transDate Date of transaction.
     * @throws OPSException if an error is encountered.
     * @return Number of transactions specified by timestamp.
     */
    public int getDailyCount(Connection conn, Date transDate)
        throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            stmt = getStatement(mGetCountByDates, conn);
            setParam(stmt, 1, "Timestamp", transDate);
            setParam(stmt, 2, "Timestamp", transDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(transDate);

            String sql = sql2str(mGetCountByDates, params);
            throw new OPSException(sql + e.getMessage());
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }    

    /**
     * Retrieves transaction count by transaction begin and end dates and function
     *
     * @param conn JDBC connection handle.
     * @param startDate Begin date of transaction.
     * @param endDate End date of transaction.
     * @param function Function of transaction.     
     * @throws OPSException if an error is encountered.
     * @return number of transactions specified by function, begin and end dates.
     */
    public int getCountByDatesNFunction(Connection conn, Date startDate, 
                                        Date endDate, String function)
        throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            if (mGetCountByDatesNFunction == null) {
                mGetCountByDatesNFunction 
                        = DBAdapter.getDBAdapterInstance().getKeyStatDBCountBetweenTimesStmt();
            }
            stmt = getStatement(mGetCountByDatesNFunction, conn);
            stmt.setString(1, function);
            setParam(stmt, 2, "Timestamp", startDate);
            setParam(stmt, 3, "Timestamp", endDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(function);
            params.add(startDate);
            params.add(endDate);

            if (mGetCountByDatesNFunction == null) {
                mGetCountByDatesNFunction 
                    = DBAdapter.getDBAdapterInstance().getKeyStatDBCountBetweenTimesStmt();
            }
            String sql = sql2str(mGetCountByDatesNFunction, params);
            throw new OPSException(sql + e.getMessage());
        } catch (OPSException e) {
            throw e;
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }
    
    /**
     * Retrieves transaction count by transaction begin and end dates, inclusive
     *
     * @param conn JDBC connection handle.
     * @param startDate Begin date of transaction.
     * @param endDate End date of transaction.
     * @throws OPSException if an error is encountered.
     * @return number of transactions specified by begin and end dates.
     */
    public int getCountByDates(Connection conn, Date startDate, Date endDate)
        throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            stmt = getStatement(mGetCountByDates, conn);
            setParam(stmt, 1, "Timestamp", startDate);
            setParam(stmt, 2, "Timestamp", endDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(startDate);
            params.add(endDate);

            String sql = sql2str(mGetCountByDates, params);
            throw new OPSException(sql + e.getMessage());
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }
    
    /**
     * Retrieves unresolved potential duplicate count by transaction begin and 
     * end dates,inclusive.
     *
     * @param conn JDBC connection handle.
     * @param startDate Begin date of transaction.
     * @param endDate End date of transaction.
     * @throws OPSException if an error is encountered.
     * @return number of unresolved potential duplicates specified by begin and 
     * end dates, inclusive.
     */
    public int getUnresolvedPotDupByDates(Connection conn, Date startDate, 
                                          Date endDate)
            throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            stmt = getStatement(mGetUnresolvedPotDupCountByDates, conn);
            setParam(stmt, 1, "Timestamp", startDate);
            setParam(stmt, 2, "Timestamp", endDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(startDate);
            params.add(endDate);

            String sql = sql2str(mGetUnresolvedPotDupCountByDates, params);
            throw new OPSException(sql + e.getMessage());
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }
    
    /**
     * Retrieves resolved potential duplicate count by transaction begin and  
     * end dates, inclusive.
     *
     * @param conn JDBC connection handle.
     * @param startDate Begin date of transaction.
     * @param endDate End date of transaction.     
     * @throws OPSException if an error is encountered.
     * @return number of resolved potential duplicates specified by begin and 
     * end dates.
     */
    public int getResolvedPotDupByDates(Connection conn, Date startDate, 
                                        Date endDate)
            throws OPSException {

        PreparedStatement stmt = null;
        ResultSet rSet = null;
        int ret = 0;
        
        try {
            stmt = getStatement(mGetResolvedPotDupCountByDates, conn);
            setParam(stmt, 1, "Timestamp", startDate);
            setParam(stmt, 2, "Timestamp", endDate);

            rSet = stmt.executeQuery();

            if (null != rSet && rSet.next()) {
                ret = rSet.getInt(1);
            }
        } catch (SQLException e) {
            ArrayList params = new ArrayList();
            params.add(startDate);
            params.add(endDate);

            String sql = sql2str(mGetResolvedPotDupCountByDates, params);
            throw new OPSException(sql + e.getMessage());
        } finally {
        	try {
        	    if (rSet != null) {
        	        rSet.close();
        	    }
        	    
                    if (stmt != null) {
                        stmt.close();
            	}
            } catch (SQLException e) {
            	throw new OPSException("failed to close statement");
            }
        }

        return ret;
    }            
    
}
