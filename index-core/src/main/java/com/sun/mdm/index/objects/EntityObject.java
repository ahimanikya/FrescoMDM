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
package com.sun.mdm.index.objects;


/*
      EntityObject.java
      
  */

import java.util.Collection;

/**
 * @author gzheng
 * @version
 */
public abstract interface EntityObject extends java.io.Serializable {

    /**
     * @return EntityObject entity object
     * @exception SystemObjectException system object exception
     * copy
     */
    EntityObject copy() throws SystemObjectException;

    /**
     * updates itself by values from input object
     *
     * @param obj object
     * @exception SystemObjectException SystemObjectException
     */
    void update(EntityObject obj) throws SystemObjectException;

    /**
     * toString
     *
     * @return String
     */
    String toString();

    /**
     * getter for object type
     *
     * @return String
     */
    String getType();

    /**
     * getter for ID
     *
     * @return String
     */
    String getId();

    /**
     * setter for ID
     *
     * @param id String
     */
    void setId(String id);

    /**
     * setter for signature
     *
     * @param signature byte[]
     */
    void setSignature(byte[] signature);

    /**
     * getter for signature
     *
     * @return byte[]
     */
    byte[] getSignature();

    /**
     * adds a child
     *
     * @param obj child EntityObject to be added
     * @exception SystemObjectException SystemObjectException
     */
    void addSecondaryObject(EntityObject obj)
        throws SystemObjectException;

    /**
     * removes a child
     *
     * @param obj child EntityObject to be dropped
     * @exception SystemObjectException SystemObjectException
     */
    void dropSecondaryObject(EntityObject obj)
        throws SystemObjectException;

    /**
     * getter for attribute metadata
     *
     * @return AttributeMetaData
     */
    AttributeMetaData getMetaData();

    /**
     * getter for field value
     *
     * @param fieldname String
     * @exception SystemObjectException system object exception
     * @return Object
     */
    java.lang.Object getField(String fieldname)
        throws SystemObjectException;

    /**
     * getter fro child object
     *
     * @param type String
     * @exception SystemObjectException system object exception
     * @return Collection of children objects
     */
    Collection getSecondaryObject(String type)
        throws SystemObjectException;

    /**
     * getter for child objects
     *
     * @param type int
     * @exception SystemObjectException SystemObjectException
     * @return Collection of children objects
     */
    Collection getSecondaryObject(int type)
        throws SystemObjectException;

    /**
     * getter for child objects
     *
     * @param type String
     * @param id String
     * @exception SystemObjectException SystemObjectException
     * @return EntityObject
     */
    EntityObject getSecondaryObject(String type, String id)
        throws SystemObjectException;

    /**
     * getter for child objects
     *
     * @param type int
     * @param id String
     * @exception SystemObjectException SystemObjectException
     * @return EntityObject
     */
    EntityObject getSecondaryObject(int type, String id)
        throws SystemObjectException;

    /**
     * setter for field value
     *
     * @param fieldname String
     * @param fieldvalue Object
     * @exception SystemObjectException SystemObjectException
     */
    void setField(String fieldname, java.lang.Object fieldvalue)
        throws SystemObjectException;
}
