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
import java.util.Date;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SBR;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.ops.exception.OPSException;
import com.sun.mdm.index.ops.exception.DataModifiedException;
import com.sun.mdm.index.persistence.TMResult;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.ops.RecreateResult;


/**
 * The interface of Transaction Manager
 *
 * @author gzheng
 */
public interface TransactionMgr{

    /**
     * Persists a new EnterpriseObject into database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to persist.
     * @throws OPSException if an error occurred.
     * @return result of the transaction.
     */
    TMResult addEnterpriseObject(Connection conn, EnterpriseObject eo)
        throws OPSException;


    /**
     * Updates an EnterpriseObject in database
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to persist.
     * @param function Update, euidActivate, euidDeactivate, lidActivate, lidDeactivate
     * @throws OPSException if an error occurred.
     * @throws DataModifiedException if the Enterprise Object has been modified
     * by another process or user.
     * @return TMResult tm result
     */
    TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
                                    String function)
        throws OPSException, DataModifiedException;

    /**
     * Updates an EnterpriseObject in database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to persist.
     * @param revisionNumber Revision number.
     * @param function Update, euidActivate, euidDeactivate, lidActivate, lidDeactivate
     * @throws OPSException if an error occurred.
     * @throws DataModifiedException if the Enterprise Object has been modified
     * by another process or user.
     * @return result of the transaction.
     */
    TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
                                    String revisionNumber, String function)
            throws OPSException, DataModifiedException;

    /**
     * Updates an EnterpriseObject in database
     *
     * @param conn jdbc connection
     * @param eo EnterpriseObject
     * @param sysCode System code of the SystemObject causing the change
     * @param lid lid of the SystemObject causing the change
     * @param function Update, euidActivate, euidDeactivate, lidActivate, lidDeactivate
     * @exception OPSException ops exception OPSException
     * @exception DataModifiedException Concurrent modification by other user
     * @return result of the transaction.
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
    String sysCode, String lid, String function)
        throws OPSException, DataModifiedException;

    /**
     * Updates an EnterpriseObject in database.
     *
     * @param conn JDBC connection.
     * @param eo EnterpriseObject to persist.
     * @param sysCode System code of the SystemObject causing the change.
     * @param lid Local ID of the SystemObject causing the change.
     * @param revisionNumber Revision number.
     * @param function Update, euidActivate, euidDeactivate, lidActivate, lidDeactivate
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException Concurrent modification by other user
     * @return TMResult tm result
     */
    public TMResult updateEnterpriseObject(Connection conn, EnterpriseObject eo, 
                                           String sysCode, String lid, 
                                           String revisionNumber, String function)
        throws OPSException, DataModifiedException;


    /**
     * Retrieves an EnterpriseObject from database by its EUID
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @throws OPSException if an OPSException occurred.
     * @return EnterpriseObject
     */
    EnterpriseObject getEnterpriseObject(Connection conn, String euid)
        throws OPSException;
    
    /**
     * Retrieves an EnterpriseObject by its EUID and set of Epaths. The set 
     * of Epaths specify what objects are retrieved to compose EO.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param epaths list of Epaths
     * @throws OPSException if an OPSException occurred.
     * @return EnterpriseObject
     */
    public EnterpriseObject getEnterpriseObject(Connection conn, String euid,
                                                String[] epaths)
        throws OPSException;

    /**
     * Retrieves SystemSBR from database by its EUID.
     *
     * @param conn JDBC connection.
     * @param euid EUID.
     * @throws OPSException if an OPSException occurred.
     * @return SystemSBR
     */
    SBR getSystemSBR(Connection conn, String euid)
        throws OPSException;


    /**
     * Retrieves a SystemObject from database by its SystemCode and LocalID.
     *
     * @param conn JDBC connection.
     * @param systemcode System code.
     * @param lid Local ID.
     * @throws OPSException if an OPSException occurred.
     * @return SystemObject
     */
    SystemObject getSystemObject(Connection conn, String systemcode, String lid)
        throws OPSException;


    /**
     * Finds transaction logs by EUID and timestamp range indicated by beginning
     * and ending timestamp.
     * @param conn JDBC connection.
     * @param euid EUID.
     * @param beginTS Beginning timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an OPSException occurred.
     * @return ret TransactionObject array.
     */
    TransactionObject[] findTransactionLogs(Connection conn, String euid, 
                                            Date beginTS, Date endTS)
        throws OPSException;

        
    /**
     * Finds transaction logs by System Code, Local ID, and timestamp range 
     * indicated by beginning and ending timestamp.
     * @param conn JDBC connection.
     * @param systemcode System code.
     * @param lid Local ID.
     * @param beginTS Beginning timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an OPSException occurred.
     * @return ret TransactionObject array.
     */
    TransactionObject[] findTransactionLogs(Connection conn, String systemcode,
                                            String lid, Date beginTS, Date endTS)
        throws OPSException;


    /**
     * Find transaction logs by TransactionObject and timestamp range 
     * indicated by beginning and ending timestamp.
     * @param conn JDBC connection.
     * @param tobj TransactionObject
     * @param beginTS Beginning timestamp.
     * @param endTS Ending timestamp.
     * @throws OPSException if an OPSException occurred.
     * @return ret TransactionObject array.
     */
    TransactionObject[] findTransactionLogs(Connection conn, 
                                            TransactionObject tobj, 
                                            Date beginTS, 
                                            Date endTS)
        throws OPSException;
        
    /**
     * Find transaction logs by TransactionObject.  Ordering can be specified.
     * @param conn JDBC connection.
     * @param tobj TransactionObject
     * @param beginTS Beginning timestamp.
     * @param endTS Ending timestamp.
     * @param orderBy Order by clause
     * @throws OPSException if an OPSException occurred.
     * @return ret TransactionObject array.
     */
    public TransactionObject[] findTransactionLogs(Connection conn,
                                                   TransactionObject tObj, 
                                                   Date beginTS, 
                                                   Date endTS, 
                                                   String orderBy)    
        throws OPSException;
    
    /**
     * Find transaction logs by TransactionObject ID.
     * @param conn JDBC connection.
     * @param transId Transaction ID
     * @throws OPSException if an OPSException occurred.
     * @return ret TransactionObject array.
     */    
    TransactionObject findTransactionLog(Connection conn, String transId) 
        throws OPSException;    


    /**
     * EUID merge: update both enterprise objects in the database, and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult euidMerge(Connection conn, EnterpriseObject eo1, EnterpriseObject eo2)
        throws OPSException, DataModifiedException ;

    /**
     * EUID merge: update both enterprise objects in the database, and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param srcRevisionNumber  The SBR revision number or the surviving 
     * EnterpriseObject.
     * @param destRevisionNumber The SBR revision number or the merged 
     * EnterpriseObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult euidMerge(Connection conn, EnterpriseObject eo1, EnterpriseObject eo2,
                       String srcRevisionNumber, String destRevisionNumber)
        throws OPSException, DataModifiedException ;

   
    /**
     * EUID unmerge: update both enterprise objects in the database, and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult euidUnMerge(Connection conn, EnterpriseObject eo1, EnterpriseObject eo2)
        throws OPSException, DataModifiedException;

    /**
     * EUID unmerge: update both enterprise objects in the database, and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param transactionID Transaction number of the original merge operation.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult euidUnMerge(Connection conn, String transactionID, 
                         EnterpriseObject eo1, EnterpriseObject eo2)
        throws OPSException, DataModifiedException;

    /**
     * LID merge: update merged EnterpriseObject in database and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Local ID of the surviving SystemObject.
     * @param lid2 Local ID of the merged SystemObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult lidMerge(Connection conn, EnterpriseObject eo1, 
                      EnterpriseObject eo2, String systemcode, 
                      String lid1, String lid2)
        throws OPSException, DataModifiedException;


    /**
     * LID unmerge: update merged EnterpriseObject in database and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Local ID of the surviving SystemObject.
     * @param lid2 Local ID of the merged SystemObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult lidUnMerge(Connection conn, EnterpriseObject eo1, 
                        EnterpriseObject eo2, String systemcode, 
                        String lid1, String lid2)
        throws OPSException, DataModifiedException;

    /**
     * LID unmerge: update merged EnterpriseObject in database and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param transactionID Transaction number of the original merge operation.
     * @param systemcode System code for both SystemObjects.
     * @param lid1 Local ID of the surviving SystemObject.
     * @param lid2 Local ID of the merged SystemObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult lidUnMerge(Connection conn, EnterpriseObject eo1, EnterpriseObject eo2, 
                        String transactionID, String systemcode, String lid1, 
                        String lid2)
        throws OPSException, DataModifiedException;

    /**
     * LID transfer : update enterprise objects involved in transfer and log
     * the action in transaction facility.
     *
     * @param conn JDBC connection.
     * @param eo1 Surviving EnterpriseObject.
     * @param eo2 Merged EnterpriseObject.
     * @param systemcode System code of the transferred SystemObject.
     * @param lid1 Local ID of the transferred SystemObject.
     * @throws OPSException if an OPSException occurred.
     * @throws DataModifiedException if the record has been modified by 
     * another process.
     * @return ret Transaction Manager result
     */
    TMResult lidTransfer(Connection conn, EnterpriseObject eo1, 
                         EnterpriseObject eo2, String systemcode, String lid)
        throws OPSException, DataModifiedException;


    /**
     * Recreate object.
     *
     * @param conn JDBC connection.
     * @param transactionnumber Transaction number for recreating the object.
     * @throws OPSException if an OPSException occurred.
     * @return RecreateResult object.
     */
    RecreateResult recreateObject(Connection conn, String transactionnumber)
        throws OPSException;
    
    /**
     * Apply delta to an EnterpriseObject.
     *
     * @param eo EnterpriseObject to which the delta is applied.
     * @return updated EnterpriseObject.
     * @throws OPSException if an OPSException occurred.
     */
    EnterpriseObject getBeforeImage(EnterpriseObject eo)
        throws OPSException;
    
}
