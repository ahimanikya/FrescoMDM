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
package com.sun.mdm.multidomain.services.util;

import java.util.regex.Pattern;
import net.java.hulp.i18n.LocalizationSupport;

/**
 * This class supports localization.
 * @author cye
 */
public class Localizer extends LocalizationSupport {
    private static final String DEFAULT_PATTERN = "([A-Z][A-Z][A-Z]\\d\\d\\d)(:)(.*)";
    private static final String DEFAULT_PREFIX = "MDM-MD-";
    private static final String DEFAULT_BUNDLENAME = "messages";
    private static Localizer instance; 
    
    /**
     * Localizer Constructor 
     * @param idpattern		the text pattern of messages.
     * @param prefix		the prefix of messages.
     * @param bundlename	the name of resource bundle.
     */
    public Localizer(Pattern idpattern, String prefix, String bundlename) {
        super(idpattern, prefix, bundlename);
    }
    
    /**  
     * Get an instance of Localizer.
     * 
     * @return a Localizer instance.
     */
    public static Localizer getInstance() {
        if (instance == null) {
            Pattern pattern = Pattern.compile(DEFAULT_PATTERN);
            instance = new Localizer(pattern, DEFAULT_PREFIX, DEFAULT_BUNDLENAME);
        }
        return instance;
    } 
}
