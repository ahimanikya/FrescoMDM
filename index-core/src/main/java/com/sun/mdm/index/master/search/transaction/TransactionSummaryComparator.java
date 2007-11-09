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

import java.util.Comparator;
import java.util.logging.Level;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNodeComparator;
import com.sun.mdm.index.util.Localizer;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * The <b>TransactionSummaryComparator</b> class is used to sort
 * and compare TransactionSummary objects in a transaction iterator.
 */
public class TransactionSummaryComparator implements Comparator, java.io.Serializable {

    private final Comparator mComparator;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();

    /**
     * Creates a new instance of the TransactionSummaryComparator class,
     * and sorts the transaction summaries by the specified field.
     * <p>
     * @param field The name of the field to use as sorting criteria. The
     * following field names can be used.
     * <UL>
	 * <LI>LID1
	 * <LI>LID2
	 * <LI>EUID1
	 * <LI>EUID2
	 * <LI>Function
	 * <LI>SystemUser
	 * <LI>TimeStamp
	 * <LI>SystemCode
	 * <LI>LID
	 * <LI>EUID
	 * </UL>
     * @param reverse An indicator of whether to sort in ascending or
     * descending order. Specify <b>true</b> to sort in descending
     * order, or specify <b>false</b> to sort in ascending order.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public TransactionSummaryComparator(String field, boolean reverse) {
        mComparator = new ObjectNodeComparator(field, reverse);
    }


    /**
     * Compares the field by which the transaction summaries in the
     * transaction iterator are sorted to determine the order in which
     * they will be displayed.
     * <p>
     * @param obj1 The first transaction summary to compare.
     * @param obj2 The second transaction summary to compare.
     * @return <CODE>int</CODE> - An integer representing the
     * results of the comparison. Possible results are:
     * <UL>
     * <LI>Negative integer - The value of the 'sort by' field in the
     * first object is less than that of the second object.
     * <LI>Zero (0) - The 'sort by' field in the two objects are equal.
     * <LI>Positive integer - The value of the 'sort by' field in the
     * first object is greater than that of the second object.
     * </UL>
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public int compare(Object obj1, Object obj2) {
        try {
            TransactionSummary rec1 = (TransactionSummary) obj1;
            TransactionSummary rec2 = (TransactionSummary) obj2;
            ObjectNode objNode1 = rec1.getTransactionObject();
            ObjectNode objNode2 = rec2.getTransactionObject();
            return mComparator.compare(objNode1, objNode2);
        } catch (Exception e) {
            mLogger.warn(mLocalizer.x("MAS002: TransactionSummaryComparator encountered an exception {0}", e.getMessage()));
            return 0;
        }
    }
}
