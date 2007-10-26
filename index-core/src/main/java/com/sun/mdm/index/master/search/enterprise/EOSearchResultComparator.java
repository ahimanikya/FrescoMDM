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
package com.sun.mdm.index.master.search.enterprise;

import java.util.Comparator;
import com.sun.mdm.index.objects.ObjectNode;
import com.sun.mdm.index.objects.ObjectNodeComparator;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * Comparator for EOSearchResultRecords
 */
public class EOSearchResultComparator 
    implements Comparator, java.io.Serializable {

    /** Full qualified field name to compare
     */        
    private final String mFQFN;
    /** True if compare in reverse order
     */    
    private final boolean mReverse;

    private final Logger mLogger = LogUtil.getLogger(this);
    
    
    /** Creates a new instance of EOSearchResultComparator
     *
     * @param fqfn Fully qualified field name
     * @param reverse set true for reverse comparison
     */
    public EOSearchResultComparator(String fqfn, boolean reverse) {
        mFQFN = fqfn;
        mReverse = reverse;
    }


    /** Compare this object with one in parameter field
     * @return Standard comparator output
     * @param obj1 Object 1
     * @param obj2 Object 2
     */
    public int compare(Object obj1, Object obj2) {
        try {
            EOSearchResultRecord rec1 = (EOSearchResultRecord) obj1;
            EOSearchResultRecord rec2 = (EOSearchResultRecord) obj2;
            if (mFQFN.equalsIgnoreCase("SCORE")) {
                float f1 = rec1.getComparisonScore();
                float f2 = rec2.getComparisonScore();
                if (mReverse) {
                    return Float.compare(f2, f1);
                } else {
                    return Float.compare(f1, f2);
                }
            } else {
                ObjectNode objNode1 = rec1.getObject();
                ObjectNode objNode2 = rec2.getObject();
                String objName = objNode1.pGetTag();
                int beginIndex = mFQFN.indexOf(objName) + objName.length() + 1;
                String relativePath = mFQFN.substring(beginIndex);
                if (relativePath.equals("EUID")) {
                    String euid1 = rec1.getEUID();
                    String euid2 = rec2.getEUID();
                    if (mReverse) {
                        return euid2.compareTo(euid1);
                    } else {
                        return euid1.compareTo(euid2);
                    }
                } else {
                    String shortFieldName = objName + "." + relativePath;
                    ObjectNodeComparator c = 
                        new ObjectNodeComparator(shortFieldName, mReverse);
                    return c.compare(objNode1, objNode2);
                }
            }
        } catch (Exception e) {
            mLogger.error("Exception", e);
            return 0;
        }
    }


    /** String representation
     * @return String representation
     */
    public String toString() {
        return "EOSearchResultComparator: " + mFQFN + " reverse: " + mReverse;
    }

}
