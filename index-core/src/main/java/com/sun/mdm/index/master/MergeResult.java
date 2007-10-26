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
package com.sun.mdm.index.master;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.TransactionObject;


/**
 *
 * The <b>MergeResult</b> class contains the return values for a
 * call to the merge-related functions of the MasterController
 * class. These include <b>mergeEnterpriseObject</b>,
 * <b>unmergeEnterpriseObject</b>, <b>mergeSystemObject</b>, and
 * <b>unmergeSystemObject</b>. A <b>MergeResult</b> object includes
 * a source and destination enterprise object and a transaction
 * object.
 */
public class MergeResult implements Externalizable {
	
    public static final int version = 1;
    
    /** Destination enterprise object
     */
    private EnterpriseObject mDestinationEO;

    /** Source enterprise object
     */
    private EnterpriseObject mSourceEO;
    
    /** Transaction object
     */
    private TransactionObject mTransactionObject;


    /**
     * Creates a new instance of the MergeResult class.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @include
     */
    public MergeResult() {
    }


    /**
     * Retrieves the destination enterprise object in a MatchResult object.
     * This is the enterprise object whose EUID is retained after the
     * merge transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The destination enterprise
     * object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getDestinationEO() {
        return mDestinationEO;
    }


    /**
     * Retrieves the source enterprise object in a MatchResult object.
     * This is the enterprise object whose EUID is not retained after the
     * merge transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>EnterpriseObject</CODE> - The source enterprise
     * object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public EnterpriseObject getSourceEO() {
        return mSourceEO;
    }


    /**
     * Retrieves the transaction object (class TransactionObject) for a
     * merge or unmerge transaction.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>TransactionObject</CODE> - The transaction object.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TransactionObject getTransactionObject() {
        return mTransactionObject;
    }


    /**
     * Sets the destination enterprise object in a MergeResult object.
     * This is the enterprise object whose EUID is retained after a
     * merge transaction.
     * <p>
     * @param destinationEO The destination enterprise object from a merge
     * transaction.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setDestinationEO(EnterpriseObject destinationEO) {
        mDestinationEO = destinationEO;
    }


    /**
     * Sets the source enterprise object in a MergeResult object.
     * This is the enterprise object whose EUID is not retained after a
     * merge transaction.
     * <p>
     * @param sourceEO The source enterprise object from a merge
     * transaction.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setSourceEO(EnterpriseObject sourceEO) {
        mSourceEO = sourceEO;
    }


    /**
     * Sets the transaction object in a MergeResult object.
     * <p>
     * @param transactionObject The transaction object from a merge
     * or unmerge transaction.
     * <DT><B>Returns:</B><DD> <CODE>void</CODE> - None.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public void setTransactionObject(TransactionObject transactionObject) {
        mTransactionObject = transactionObject;
    }


    /** String representation of result
     * Retrieves a string representation of the MergeResult object.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * @return <CODE>String</CODE> - The values contained in the
     * MergeResult attributes.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public String toString() {
        return "*************** SourceEO ***************\n" + mSourceEO
        + "*************** DestinationEO ***************\n" + mDestinationEO
        + "*************** TransactionObject ***************\n"
        + mTransactionObject;
    }

    private void objectWriter(ObjectOutput out, Object obj) throws java.io.IOException {
        if (obj != null) {
            out.writeInt(1);
            out.writeObject(obj);
        } else {
            out.writeInt(0);
        }
     }

     private Object objectReader(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
         int f = in.readInt();
         return (f == 1) ? in.readObject() : null;
     }

    private final class ExternalizableVersion1 {
        public ExternalizableVersion1() {};

        void writeExternal(ObjectOutput out) throws java.io.IOException {
            objectWriter(out, mDestinationEO);
            objectWriter(out, mSourceEO);
            objectWriter(out, mTransactionObject);
        }

        void readExternal(ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            mDestinationEO = (EnterpriseObject) objectReader(in);
            mSourceEO = (EnterpriseObject) objectReader(in);
            mTransactionObject = (TransactionObject) objectReader(in);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        ExternalizableVersion1 ev = new ExternalizableVersion1();
        out.writeInt(version);
        ev.writeExternal(out);
    }

    public void readExternal(ObjectInput in)
    throws IOException, java.lang.ClassNotFoundException {
        int version = in.readInt();
        if (version == 1) {
            ExternalizableVersion1 ev = new ExternalizableVersion1();
            ev.readExternal(in);
        } else {
            throw new RuntimeException("Unsupported version = " + version);
        }
    }
}
