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
package com.sun.mdm.index.master.search.transaction;


import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.objects.EnterpriseObjectHistory;


/**
 * The <b>TransactionSummary</b> class represents an object containing a
 * transaction summary returned by a call to <b>lookupTransaction</b>. A
 * transaction history search returns an iterator of TransactionSummary
 * objects.
 */
public class TransactionSummary implements java.io.Serializable {
    private EnterpriseObjectHistory enterpriseHistory = null;
    private TransactionObject transObj = null;
    private boolean mValidTransaction = true;   // indicates if this is a valid transaction

    /**
     * Creates a new instance of the TransactionSummary class.
     *<p>
     * @param obj A transaction object (class TransactionObject).
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TransactionSummary(TransactionObject obj) {
        transObj = obj;
    }


    /**
     * Retrieves the enteprise object history (class EnterpriseObjectHistory)
     * in an instance of TransactionSummary.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObjectHistory</CODE> - A history of an
     * enterprise object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObjectHistory getEnterpriseObjectHistory() {
        return enterpriseHistory;
    }


    /**
     * Retrieves the transaction object (class TransactionObject)
     * in an instance of TransactionSummary.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>TransactionObject</CODE> - A transaction object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TransactionObject getTransactionObject() {
        return transObj;
    }

    /**
     * Returns the valid transaction flag.  
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>boolean</CODE> - true if the transaction is valid, false otherwise.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public boolean getValidTransaction() {
        return mValidTransaction;
    }
    
    /**
     * Sets the enterprise object history (class EnterpriseObjectHistory)
     * in an instance of TransactionSummary.
     * <p>
     * @param objHist An EnterpriseObjectHistory object.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setEnterpriseObjectHistory(EnterpriseObjectHistory objHist) {
        enterpriseHistory = objHist;
    }

    /**
     * Sets the valid transaction flag
     * <p>
     * @param val  A boolean indicating if the transaction is valid.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setValidTransaction(boolean val) {
        mValidTransaction = val;
    }

    /**
     * Retrieves a string representation of a TransactionSummary object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The value of the TransactionSummary
     * elements.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        String str = "";

        str += "Object: TransactionSummary\n";
        if (mValidTransaction) {
            str += "Valid Transaction\n";
        } else {
            str += "Invalid Transaction\n";
        }
        str += transObj.toString();
        if (enterpriseHistory != null) {
            if (enterpriseHistory.getAfterEO() != null) {
                str += "After EO:\n";
                str += enterpriseHistory.getAfterEO().toString();
            }
            if (enterpriseHistory.getBeforeEO1() != null) {
                str += "Before EO1:\n";
                str += enterpriseHistory.getBeforeEO1().toString();
            }
            if (enterpriseHistory.getBeforeEO2() != null) {
                str += "Before EO2:\n";
                str += enterpriseHistory.getBeforeEO2().toString();
            }
        }
        return str;
    }
}
