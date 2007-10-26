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

import com.sun.mdm.index.objects.epath.EPathAPI;

import java.util.Comparator;
import com.sun.mdm.index.util.LogUtil;
import com.sun.mdm.index.util.Logger;

/**
 * @author gzheng
 * General comparator of object nodes
 */
public class ObjectNodeComparator implements Comparator, java.io.Serializable {
    private final String mmFQFN;
    private final boolean mReverse;
    private final Logger mLogger = LogUtil.getLogger(this);
    /** Creates a new instance of ObjectNodeComparator
     * @param mFQFN fully qualified field name
     * @param reverse boolean 
     */
    public ObjectNodeComparator(String mFQFN, boolean reverse) {
        mmFQFN = mFQFN;
        mReverse = reverse;
    }

    /**
     * compare
     * @param obj1 Object
     * @param obj2 Object
     * @return int
     */
    public int compare(Object obj1, Object obj2) {
        ObjectNode objNode1 = (ObjectNode) obj1;
        ObjectNode objNode2 = (ObjectNode) obj2;

        try {
            Comparable val1 = (Comparable) EPathAPI.getFieldValue(mmFQFN,
                    objNode1);
            Comparable val2 = (Comparable) EPathAPI.getFieldValue(mmFQFN,
                    objNode2);

            if (val1 == null) {
                if ((val2 == null)) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                if (val2 == null) {
                    return -1;
                }
            }

            //Since id numbers are represented as strings, we must ensure that
            //they have the same length otherwise sort order might be 1,10,2,20,3
            //etc...   Padding is only performed for non numerics 
            if (isNumericString(val1) && isNumericString(val2)) {
                String stringVal1 = (String) val1;
                String stringVal2 = (String) val2;
                int length1 = stringVal1.length();
                int length2 = stringVal2.length();
                if (length1 == length2) {
                    ;
                    //Do Nothing.  No padding required
                } else if (length1 > length2) {
                    stringVal2 = padNumericString(stringVal2, length1);
                } else {
                    stringVal1 = padNumericString(stringVal1, length2);
                }
                val1 = stringVal1;
                val2 = stringVal2;
            }
            
            if (mReverse) {
                return val2.compareTo(val1);
            } else {
                return val1.compareTo(val2);
            }
        } catch (Exception e) {
            mLogger.error("Comparison Error", e);

            return 0;
        }
    }
        
    private String padNumericString(String val, int len) {
        StringBuffer sb = new StringBuffer();
        int lenToAppend = len - val.length();
        for (int i = 0; i < lenToAppend; i++) {
            sb.append('0');
        }
        sb.append(val);
        return sb.toString();
    }
    
    private boolean isNumericString(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        String string = (String) obj;
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
        
}
