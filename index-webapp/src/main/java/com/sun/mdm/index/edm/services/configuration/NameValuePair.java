/*
 * BEGIN_HEADER - DO NOT EDIT
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://open-esb.dev.java.net/public/CDDLv1.0.html.
 * If applicable add the following below this CDDL HEADER,
 * with the fields enclosed by brackets "[]" replaced with
 * your own identifying information: Portions Copyright
 * [year] [name of copyright owner]
 */

/*
 * @(#)NameValuePair.java
 * Copyright 2004-2007 Sun Microsystems, Inc. All Rights Reserved.
 *
 * END_HEADER - DO NOT EDIT
 */
package com.sun.mdm.index.edm.services.configuration;

import java.util.Iterator;
import java.util.ArrayList;


/**
 * Name value pairs
 *
 * @author rtam
 * @created July 27, 2007
 */
public class NameValuePair implements java.io.Serializable {
    
    private String mName;    // Name component for the pair 
    private String mValue;   // Value component for the pair
    

    /**
     * Creates a new instance of the NameValuePair class
     *
     * @param name  The Name component of this NameValue pair.
     * @param value  The Value component of this NameValue pair.
     */
    public NameValuePair(String name, String value) {
                    
        mName = name;
        mValue = value;
    }
    
    /**
     * Getter for the mName attribute
     *
     * @return The Name component of this Name/Value pair.
     */
    public String getDisplayName() {
        return mName;
    }
    
     /**
     * Getter for the mValue attribute
     *
     * @return The Value component of this Name/Value pair.
     */
    public String getValue() {
        return mValue;
    }
    
}