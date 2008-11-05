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

import com.sun.mdm.index.util.Localizer;
import java.util.logging.Level;
import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class SummaryLabel {
    
    private static transient final Logger mLogger = Logger.getLogger("com.sun.mdm.multidomain.services.configuration.SummaryLabel");
    private static transient final Localizer mLocalizer = Localizer.get();
    
    ArrayList<FieldConfig> mFieldConfigs;   // FieldConfig objects from which record-specific values are retrieved
    ArrayList<String> mDelimiters;          // ArrayList of Strings

    public SummaryLabel() {
    }
    
    // retrieves an ArrayList of FieldConfig objects
    
    ArrayList<FieldConfig> getFieldConfigs() {      
        return mFieldConfigs;
    }

    // sets the fieldConfigs 
    
    void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) { 
        mFieldConfigs = fieldConfigs;
    }

    // retrieves an ArrayList of String objects
    ArrayList<String> getDelimiters() {     
        return mDelimiters;
    }

    // sets the delimiters
     
    void setDelimiters(ArrayList<String> delimiters) throws Exception { 
        if (delimiters.size() != mFieldConfigs.size() + 1) {
            throw new Exception(mLocalizer.t("CFG501: The number of delimiters ({0}) " +
                                             "must be one greater than number of fields({1}).", 
                                             delimiters.size(), mFieldConfigs.size()));
        }
        mDelimiters = delimiters;
    }

    // retrieves the ID label
    // ID label has the following format:
    // mFieldConfigs[0] + mDelimiters[0] + ...  + mFieldConfigs[i] + mDelimiters[i+1] 
    
    String getIDLabel() throws Exception {          
        if (mDelimiters.size() != mFieldConfigs.size()) {
            throw new Exception(mLocalizer.t("CFG502: The number of delimiters ({0}) " +
                                             "does not match the number of fields({1}).", 
                                             mDelimiters.size(), mFieldConfigs.size()));
        }
        StringBuffer str = new StringBuffer();
        int maxSize = 0;
        for (int i = 0; i < mFieldConfigs.size(); i++ ) {
            str.append(mDelimiters.get(i));
            str.append(mFieldConfigs.get(i));
            maxSize++;
        }
        str.append(mDelimiters.get(maxSize + 1));
        return str.toString();
    }

}
