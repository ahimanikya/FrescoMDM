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
import com.sun.mdm.index.master.UserException;
import com.sun.mdm.index.objects.SystemObjectException;
import com.sun.mdm.index.objects.exception.ObjectException;
import com.sun.mdm.index.ops.exception.DataModifiedException;
import com.sun.mdm.index.survivor.SurvivorCalculationException;

public interface UpdateManager{
    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#createEnterpriseObject
     */
    public com.sun.mdm.index.update.UpdateResult createEnterpriseObject(
        Connection con, com.sun.mdm.index.objects.SystemObject so)
        throws SystemObjectException, UpdateException, 
            ObjectException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#createEnterpriseObject
     */
    public com.sun.mdm.index.update.UpdateResult createEnterpriseObject(
                Connection con, com.sun.mdm.index.objects.SystemObject so,
                String euid)
        throws SystemObjectException, UpdateException,
               ObjectException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#createEnterpriseObject
     */
    public com.sun.mdm.index.update.UpdateResult createEnterpriseObject(
        Connection con, com.sun.mdm.index.objects.SystemObject[] so)
        throws SystemObjectException, UpdateException, 
            SurvivorCalculationException, 
            ObjectException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#updateEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
        Connection con, com.sun.mdm.index.objects.SystemObject so,
        com.sun.mdm.index.objects.EnterpriseObject eo, int flags, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

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
            Connection con, com.sun.mdm.index.objects.SystemObject so,
            com.sun.mdm.index.objects.EnterpriseObject eo, 
            String revisionNumber, int flags, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#updateEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult updateEnterprise(
        Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, int flags, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

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
            Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, 
            String revisionNumber, int flags, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#transferSystem
     */
    public com.sun.mdm.index.update.UpdateResult transferSystem(
        Connection con, com.sun.mdm.index.objects.EnterpriseObject srcEO,
        com.sun.mdm.index.objects.EnterpriseObject destEO,
        java.lang.String system, java.lang.String lid, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException, DataModifiedException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#removeSystem
     */
    public com.sun.mdm.index.update.UpdateResult removeSystem(Connection con,
        com.sun.mdm.index.objects.EnterpriseObject srcEO, java.lang.String system,
        java.lang.String lid, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException, DataModifiedException; 

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#mergeSystem
     */
    public com.sun.mdm.index.update.UpdateResult mergeSystem(Connection con,
        com.sun.mdm.index.objects.EnterpriseObject srcEO,
        com.sun.mdm.index.objects.EnterpriseObject destEO, String system,
        String lid, com.sun.mdm.index.objects.SystemObject newSO, int flags, String user)
        throws SurvivorCalculationException, SystemObjectException, 
            ObjectException, UpdateException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#mergeSystem
     */
    public com.sun.mdm.index.update.UpdateResult mergeSystem(Connection con,
        com.sun.mdm.index.objects.EnterpriseObject srcEO, String system,
        String lid, com.sun.mdm.index.objects.SystemObject newSO, int flags, String user)
        throws SurvivorCalculationException, SystemObjectException, 
            ObjectException, UpdateException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#mergeEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult mergeEnterprise(
        Connection con, com.sun.mdm.index.objects.EnterpriseObject srcEO,
        com.sun.mdm.index.objects.EnterpriseObject destEO, int flags, String user)
        throws SurvivorCalculationException, SystemObjectException, 
            ObjectException, UpdateException,
            DataModifiedException, UserException;

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
        throws SurvivorCalculationException, SystemObjectException, 
            ObjectException, UpdateException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#splitSystem
     */
    public com.sun.mdm.index.update.UpdateResult splitSystem(Connection con,
        String system, String lid, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#splitSystem
     */
    public com.sun.mdm.index.update.UpdateResult splitSystem(Connection con,
        com.sun.mdm.index.objects.SystemObject so,
        com.sun.mdm.index.objects.EnterpriseObject eo, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException, 
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#unmergeSystem
     */
    public com.sun.mdm.index.update.UpdateResult unmergeSystem(
        java.sql.Connection con, String transactionID, int flag, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;

    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#unmergeEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult unmergeEnterprise(
        java.sql.Connection con, String transactionID, int flag, String user)
        throws UpdateException, SurvivorCalculationException, 
            SystemObjectException, ObjectException,
            DataModifiedException, UserException;
    
    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#deactivateEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult deactivateEnterprise(
        Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
        throws UpdateException, ObjectException, DataModifiedException;
    
    /**
     * @see com.sun.mdm.index.update.UpdateManagerEJB#activateEnterprise
     */
    public com.sun.mdm.index.update.UpdateResult activateEnterprise(
        Connection con, com.sun.mdm.index.objects.EnterpriseObject eo, String user)
        throws UpdateException, ObjectException, DataModifiedException;    
    
}
