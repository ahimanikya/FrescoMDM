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

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.persistence.TMResult;


/**
 * The <b>UpdateResult</b> class contains the return values for a call to
 * <b>updateEnterpriseDupRecalc</b>.
 */
public class UpdateResult
    implements java.io.Serializable {

    /** result from TransactionManager */
    private TMResult mTransactionResult;

    /** affected EnterpriseObject1 */
    private EnterpriseObject mEnterpriseObj1;

    /** affected EnterpriseObject2 */
    private EnterpriseObject mEnterpriseObj2;

    /* indicates if a match field has changed */
    private boolean mMatchFieldChanged;

    /** default constructor */
    protected UpdateResult() {
    }

    /**
     * Creates a new instance of the UpdateResult class.
     * <p>
     * @param tm The result returned by the Transaction Manager for the
     * update transaction.
     * @param eo The enterprise object that was updated.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public UpdateResult(TMResult tm, EnterpriseObject eo) {
        mTransactionResult = tm;
        mEnterpriseObj1 = eo;
        mEnterpriseObj2 = eo;
    }

    /**
     * Creates a new instance of the UpdateResult class containing two
     * enterprise objects.
     * <p>
     * @param tm The result returned by the Transaction Manager for the
     * update transaction.
     * @param eo1 The initial enterprise object involved in the update process.
     * @param eo2 The second enterprise object involved in the update process
     * (if a second object was involved).
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public UpdateResult(TMResult tm, EnterpriseObject eo1, EnterpriseObject eo2) {
        mTransactionResult = tm;
        mEnterpriseObj1 = eo1;
        mEnterpriseObj2 = eo2;
        mMatchFieldChanged = false;

    }

    /**
     * Creates a new instance of the UpdateResult class that indicates whether
     * a match field was updated as a result of the update transaction.
     * <p>
     * @param tm The result returned by the Transaction Manager for the
     * update transaction.
     * @param eo1 The first enterprise object involved in the update process.
     * @param eo2 The second enterprise object involved in the update process
     * (if a second object was involved).
     * @param matchFieldChanged  A Boolean indicator of whether a match field
     * was changed during the update transaction. <B>True</B> indicates a match
     * field has changed; <B>false</B> indicates a match field did not change.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public UpdateResult(TMResult tm, EnterpriseObject eo1, EnterpriseObject eo2,
                        boolean matchFieldChanged) {
        mTransactionResult = tm;
        mEnterpriseObj1 = eo1;
        mEnterpriseObj2 = eo2;
        mMatchFieldChanged = matchFieldChanged;

    }

    /**
     * Retrieves an indicator of whether a match field was updated during
     * the update transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>Boolean</CODE> - Boolean indicator of whether a match
     * field was changed during the update transaction. <B>True</B> indicates
     * a match field changed; <B>false</B> indicates a match field did not
     * change.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public boolean getMatchFieldChanged()  {
        return mMatchFieldChanged;
    }

    /**
     * Sets the indicator of whether a match field was updated.
     * <p>
     * @param matchFieldChanged Boolean indicator of whether a match field
     * changed during the update transaction. <B>True</B> indicates a match
     * field changed; <B>false</B> indicates a match field did not change.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setMatchFieldChanged(boolean matchFieldChanged)  {
        mMatchFieldChanged = matchFieldChanged;
    }

    /**
     * Retrieves the result returned by the Transaction Manager for the
     * update transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>TNResult</CODE> - The Transaction Manager result for
     * the update transaction.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TMResult getTransactionResult() {
        return mTransactionResult;
    }

    /**
     * Retrieves the initial enterprise object involved in the update transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The initial enterprise object
     * involved in the transaction.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getEnterpriseObject1() {
        return mEnterpriseObj1;
    }

    /**
     * Retrieves the second enterprise object involved in the update transaction
     * (if a second object was involved).
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The second enterprise object
     * involved in the transaction.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getEnterpriseObject2() {
        return mEnterpriseObj2;
    }
}
