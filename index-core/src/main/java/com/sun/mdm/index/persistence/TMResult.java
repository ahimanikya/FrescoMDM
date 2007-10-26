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
package com.sun.mdm.index.persistence;

import java.io.Serializable;


/**
 * The <b>TMResult</b> class represents the results of the Transaction Manager.
 * Use this class to retrieve the transaction number when using
 * <b>updateEnterpriseDupRecalc</b>.
 *
 * @author gzheng
 */
public class TMResult implements Serializable {
    private String mEUID;
    private String mTMID;

    public static final TMResult NO_CHANGE_TRANSACTION = new TMResult();

    /**
     * Creates a new instance of the TMResult class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.
     * <DT><B>Returns:</B><DD> The results of the Transaction Manager.
     * <DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public TMResult() {
    }


    /**
     * Creates a new instance of the TMResult class.
     * <p>
     * @param euid The EUID for the transaction.
     * @param tmid The transaction number for the transaction.
     * <DT><B>Returns:</B><DD> The results of the Transaction Manager.
     * <DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public TMResult(String euid, String tmid) {
        mEUID = euid;
        mTMID = tmid;
    }


    /**
     * Retrieves the EUID from an instance of TMResult.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The EUID for the transaction.
     * <DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public String getEUID() {
        return mEUID;
    }

    /**
     * Retrieves the transaction number from an instance of TMResult.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The transaction number for the transaction.
     * <DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public String getTMID() {
        return mTMID;
    }
}
