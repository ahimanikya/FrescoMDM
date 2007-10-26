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

/**
 * The <b>SystemObjectPK</b> class represents a system code and local ID pair.
 * You perform different types of searches, such as an enterprise object search,
 * a system object search, or an EUID search, using a SystemObjectPK object as
 * the search criteria.
 * @author gzheng
 */
public class SystemObjectPK implements java.io.Serializable {
    /**
     * System code
     */
    public String systemCode;
    /**
     * Local ID
     */
    public String lID;


    /**
     * Creates a new instance of the SystemObjectPK class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public SystemObjectPK() {
    }


    /**
     * Creates a new instance of the SystemObjectPK class.
     * <p>
     * @param systems The processing code of an external system.
     * @param lid A local ID in the specified system.
     * @include
     */
    public SystemObjectPK(String systems, String lid) {
        systemCode = systems;
        lID = lid;
    }


    /**
     * Compares two SystemObjectPK objects to determine whether the
     * system and local ID pair in each object match.
     * <p>
     * @param pk An object containing a local ID and system code.
     * @return <CODE>boolean</CODE> - An indicator of whether two instances of
     * SystemObjectPK match. This method returns <b>true</b> if the
     * objects match, or <b>false</b> if they do not.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public boolean equals(Object pk) {
        boolean bRet = false;

        if ((pk instanceof SystemObjectPK)
                && ((SystemObjectPK) pk).systemCode.equals(systemCode)
                && ((SystemObjectPK) pk).lID.equals(lID)) {
            bRet = true;
        }

        return bRet;
    }


    /**
     * @return int hash code
     */
    public int hashCode() {
        return (systemCode.hashCode() ^ lID.hashCode());
    }


    /**
     * Returns a string representation of the system code and local ID
     * in an instance of SystemObjectPK.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - A string representation of a SystemObjectPK object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return "[" + systemCode + "," + lID + "]";
    }
}
