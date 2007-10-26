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

import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.exception.ObjectException;
import java.util.Date;

/**
 * Interface for system object bean
 */
public interface SystemObjectBean {
 
    /**
     * Getter for local id
     * @exception ObjectException if error occures
     * @return String lid
     */
    public String getLocalId() throws ObjectException;

    /**
     * Getter for system code
     * @exception ObjectException if error occures
     * @return String system code
     */
    public String getSystemCode() throws ObjectException;

    /**
     * Getter for ObjectBean
     * @return ObjectBean 
     */
    public ObjectBean pGetObjectBean();
    
    /**
     * Getter for status
     * @return status of SystemObjectBean
     * @exception ObjectException if error occures
     */
    public String getStatus() throws ObjectException;


    /**
     * Getter for create date time
     * @return Date create date time
     * @exception ObjectException if error occures
     */
    public Date getCreateDateTime() throws ObjectException;

    /**
     * Getter for create function
     * @return String create function
     * @exception ObjectException if error occures
     */
    public String getCreateFunction() throws ObjectException;

    /**
     * Getter for create user
     * @return String create user
     * @exception ObjectException if error occures
     */
    public String getCreateUser() throws ObjectException;

    /**
     * Setter for local id
     * @param lid LID
     * @exception ObjectException if error occures
     */
    public void setLocalId(String lid) throws ObjectException;

    /**
     * Setter for system code
     * @param system system code
     * @exception ObjectException if error occures
     */
    public void setSystemCode(String system) throws ObjectException;

    /**
     * Setter for status
     * @param status status
     * @exception ObjectException if error occures
     */
    public void setStatus(String status) throws ObjectException;

    /**
     * Setter for create date time
     * @param createdatetime create date time
     * @exception ObjectException if error occures
     */
    public void setCreateDateTime(Date createdatetime)
        throws ObjectException;

    /**
     * Setter for create function
     * @param createfunction create function
     * @exception ObjectException if error occures
     */
    public void setCreateFunction(String createfunction)
        throws ObjectException;

    /**
     * Setter for create user
     * @param createuser create user
     * @exception ObjectException if error occures
     */
    public void setCreateUser(String createuser) throws ObjectException;

    /**
     * Setter for update user
     * @param updateuser update user
     * @exception ObjectException if error occures
     */
    public void setUpdateUser(String updateuser) throws ObjectException;
    
    /** 
     * Getter for system object
     * @return system object
     */
    public SystemObject pGetSystemObject();
}
