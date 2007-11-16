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

package com.sun.mdm.index.update.impl;
import com.sun.mdm.index.objects.AliasObject;

/**
 * A container for aliases that implements hashCode and equals methods
 * @author  dcidon
 */
public  class AliasContainer {
    
    private final AliasObject mAlias;
    private final String mFirstName;
    private final String mLastName;
    private final String mMiddleName;
    private final String mStdFirstName;
    private final String mStdLastName;
    private final String mStdMiddleName;
    
    /**
     * Constructor
     * @param obj alias object
     */
    public AliasContainer(AliasObject obj) {
        try {
            mAlias = obj;
            mFirstName = mAlias.getFirstName();
            mLastName = mAlias.getLastName();
            mMiddleName = mAlias.getMiddleName();
            mStdFirstName = mAlias.getStdFirstName();
            mStdLastName = mAlias.getStdLastName();
            mStdMiddleName = mAlias.getStdMiddleName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Constructor
     * @param firstName first name
     * @param lastName last name
     * @param middleName middle name
     * @param firstNamePhonetic first name phonetic
     * @param lastNamePhonetic last name phonetic
     * @param middleNamePhonetic middle name phonetic
     * @param stdFirstName standardized first name
     * @param stdLastName standardized last name
     * @param stdMiddleName standardized middle name
     */
    public AliasContainer(String firstName, String lastName, String middleName,
    String firstNamePhonetic, String lastNamePhonetic, String middleNamePhonetic,
    String stdFirstName, String stdLastName, String stdMiddleName) {
        try {
            mAlias = new AliasObject();
            mAlias.setFirstName(firstName);
            mAlias.setLastName(lastName);
            mAlias.setMiddleName(middleName);
            mAlias.setFnamePhoneticCode(firstNamePhonetic);
            mAlias.setLnamePhoneticCode(lastNamePhonetic);
            mAlias.setMnamePhoneticCode(middleNamePhonetic);
            mAlias.setStdFirstName(stdFirstName);
            mAlias.setStdLastName(stdLastName);
            mAlias.setStdMiddleName(stdMiddleName);
            mFirstName = firstName;
            mLastName = lastName;
            mMiddleName = middleName;
            mStdFirstName = stdFirstName;
            mStdLastName = stdLastName;
            mStdMiddleName = stdMiddleName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Hashcode of first, last and middle names
     * @return hash code
     */
    public int hashCode() {
        return (mFirstName + mLastName + mMiddleName).hashCode();
    }
    
    /**
     * return true if equals
     * @param obj other alias
     * @return true if equals
     */
    public boolean equals(Object obj) {
        try {
            AliasContainer aliasContainer = (AliasContainer) obj;
            AliasObject alias2 = aliasContainer.mAlias;
            String firstName = alias2.getFirstName();
            String lastName = alias2.getLastName();
            String middleName = alias2.getMiddleName();
            boolean retVal = fieldEquals(mFirstName, firstName)
            && fieldEquals(mLastName, lastName) && fieldEquals(mMiddleName, middleName);
            return retVal;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get alias
     * @return alias
     */
    public AliasObject getAlias() {
        return mAlias;
    }
    
    private boolean fieldEquals(String str1, String str2) {
        if (str1 == null || str1.equals("")) {
          return (str2 == null || str2.equals(""));
        } else {
            if (str2 == null || str2.equals("")) {
                return false;
            } else {
                return str1.equals(str2);
            }
        }
    }
}
