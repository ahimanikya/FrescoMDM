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
package com.sun.mdm.index.webservice;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.exception.ObjectException;

/**
 * EnterpriseObjectBean value object
 * @author jlong
 */
public interface EnterpriseObjectBean {
 
    /**
     * Getter for EUID attribute of the EnterpriseObject object
     *
     * @exception ObjectException if error occures
     * @return euid of EnterpriseObject object
     */
    public String getEUID() throws ObjectException;

    /**
     * Getter for SBR attribute of the EnterprisePerson object
     *
     * @return SBR object bean
     */
    public SBRObjectBean pGetSBRObjectBean();

    /**
     * Getter for Status attribute of the EnterpriseObject object
     *
     * @exception ObjectException if error occures
     * @return status of EnterpriseObject object
     */
    public String getStatus() throws ObjectException;


    /**
     * Getter for SystemObject attribute of the EnterpriseObject object
     *
     * @param system SystemCode
     * @param lid LocalID
     * @exception ObjectException if error occures
     * @return System ObjectBean
     */
    public SystemObjectBean pGetSystemObjectBean(String system, String lid)
        throws ObjectException;

    /**
     * Getter for SystemObjects attribute of the EnterpriseObject object
     *
     * @return Collection of SystemObject(s)
     */
    public SystemObjectBean[] pGetSystemObjectBean();
    
    /**
     * Getter for ith SystemObjectBean
     * @param index index of SystemObjectBean
     * @return SystemObjectBean
     */
    public SystemObjectBean pGetSystemObjectBean(int index);
    
    /**
     * Setter for EUID attribute of the EnterpriseObject object
     *
     * @param euid EUID
     * @exception ObjectException if error occures
     */
    public void setEUID(String euid) throws ObjectException;

    /**
     * Getter for Status attribute of the EnterpriseObject object
     *
     * @param status Enterprise Object status
     * @exception ObjectException if error occures
     */
    public void setStatus(String status) throws ObjectException;    

    /**
     * Removes a SystemObject from EnterpriseObject
     *
     * @param system System Code
     * @param lid Local ID
     * @exception ObjectException if error occures
     */
    public void removeSystemObjectBean(String system, String lid) throws ObjectException;

    /**
     * mark a SystemObject from EnterpriseObject for deletion
     *
     * @param system System Code
     * @param lid Local ID
     * @exception ObjectException if error occures
     */
    public void deleteSystemObjectBean(String system, String lid) throws ObjectException;
    
    /** 
     * Getter for enterprise object
     * @return enterprise object
     */
    public EnterpriseObject pGetEnterpriseObject();
}
