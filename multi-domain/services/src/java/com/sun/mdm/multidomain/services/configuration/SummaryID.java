/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2003-2008 Sun Microsystems, Inc. All Rights Reserved.
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
package com.sun.mdm.multidomain.services.configuration;

import java.util.ArrayList;
import java.util.Iterator;

public class SummaryID {
	String mPrefix;			// user-supplied prefix.
	String mSuffix;			// user-supplied suffix
	ArrayList<FieldConfig> mFieldConfigs;	// FieldConfig objects from which record-specific values are retrieved
	ArrayList<String> mDelimiters;		// ArrayList of Strings

    public SummaryID() {
    }
    
	String getPrefix() {			// retrieves the prefix for the ID label
	    return mPrefix;
	}

	void setPrefix(String prefix) {		// sets the prefix for the ID label
	    mPrefix = prefix;
	}

	String getSuffix() {			// retrieves the suffix for the ID label
	    return mSuffix;
	}

	void setSuffix(String suffix) {		// sets the suffix for the ID label
	    mSuffix = suffix;
	}

	ArrayList<FieldConfig> getFieldConfigs() {		// retrieves an ArrayList of FieldConfig objects
	    return mFieldConfigs;
	}

	void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) {	// sets the fieldConfigs 
	    mFieldConfigs = fieldConfigs;
	}

	ArrayList<String> getDelimiters() {		// retrieves an ArrayList of String objects
	    return mDelimiters;
	}

	void setDelimiters(ArrayList<String> delimiters) {	// sets the delimiter 
	    if (delimiters.size() != mFieldConfigs.size()) {
	        // testing--raymond tam
	        // RESUME HERE
	        // throw exception--they must be the same size
	    }
	    mDelimiters = delimiters;
	}

    // ID label has the following format:
    // mPrefix + mFieldConfigs[i] + mDelimiters[i] + mSuffix
	String getIDLabel() {			// retrieves the ID label
	    if (mDelimiters.size() != mFieldConfigs.size()) {
	        // testing--raymond tam
	        // RESUME HERE
	        // throw exception--they must be the same size
	    }
	    StringBuffer str = new StringBuffer();
	    str.append(mPrefix);
	    for (int i = 0; i < mDelimiters.size(); i++ ) {
	        str.append(mFieldConfigs.get(i));
	        str.append(mDelimiters.get(i));
	    }
	    str.append(mSuffix);
	    return str.toString();
	}

}
