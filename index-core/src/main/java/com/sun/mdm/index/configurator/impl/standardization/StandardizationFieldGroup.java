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
package com.sun.mdm.index.configurator.impl.standardization;

import java.util.LinkedHashMap;
import java.util.HashMap;
import com.sun.mdm.index.matching.DomainSelector;

/**
 * Defines the configuration settings for the common elements to a 
 * standardization field group, whether for a structure to normalize (pre-parsed) 
 * or free-form text to standardize  (unparsed).
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class StandardizationFieldGroup {

    private String concatenationDelimiter;
    private LinkedHashMap standardizationTargets;
    private String standardizationType;
    private DomainSelector domainSelector;
    private HashMap locales;
    private SystemObjectField localeField;
    
    /**
     * Creates new StandardizationFieldGroup.
     *
     * @param aConcatenationDelimiter the concatenation delimiter to use if required.
     * @param aStandardizationTargets the fields to write the standardized structure to.
     * @param aStandardizationType the standardization engine specific standardization
     * type identifying the entry to standardize to the standardization engine.
     * @param aDomainSelector The default domain selector.
     * @throws ClassNotFoundException if a class is not found.
     * @throws InstantiationException if there is an InstantiationException.
     * @throws IllegalAccessException if there is an IllegalAccessException.
     */
    public StandardizationFieldGroup(String aConcatenationDelimiter, 
                                     LinkedHashMap aStandardizationTargets, 
                                     String aStandardizationType,
                                     String aDomainSelectorClassName) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        concatenationDelimiter = aConcatenationDelimiter;
        standardizationTargets = aStandardizationTargets;
        standardizationType = aStandardizationType;
        if (aDomainSelectorClassName == null || aDomainSelectorClassName.length() == 0 ) {
            aDomainSelectorClassName = "com.sun.mdm.index.matching.impl.SingleDomainSelectorUS";
        }
        domainSelector = (DomainSelector) Class.forName(aDomainSelectorClassName).newInstance();
    }

    /**
     * Creates new StandardizationFieldGroup.
     *
     * @param aConcatenationDelimiter the concatenation delimiter to use if required.
     * @param aStandardizationTargets the fields to write the standardized structure to.
     * @param aLocales the hash map containing the locale information.
     * @param aStandardizationType the standardization engine specific standardization.
     * @param aDomainSelectorClassName the default domain selector.
     * @param aLocaleField the field containing the locale information
     * type identifying the entry to standardize to the standardization engine.
     * @throws ClassNotFoundException if a class is not found.
     * @throws InstantiationException if there is an InstantiationException.
     * @throws IllegalAccessException if there is an IllegalAccessException.
     */
    public StandardizationFieldGroup(String aConcatenationDelimiter, 
                                     LinkedHashMap aStandardizationTargets, 
                                     HashMap aLocales, 
                                     String aStandardizationType, 
                                     String aDomainSelectorClassName, 
                                     SystemObjectField aLocaleField) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        concatenationDelimiter = aConcatenationDelimiter;
        standardizationTargets = aStandardizationTargets;
        standardizationType = aStandardizationType;
        if (aDomainSelectorClassName == null || aDomainSelectorClassName.length() == 0 ) {
            aDomainSelectorClassName = "com.sun.mdm.index.matching.impl.SingleDomainSelectorUS";
        }
        domainSelector = (DomainSelector) Class.forName(aDomainSelectorClassName).newInstance();
        this.locales = aLocales;
        this.localeField = aLocaleField;
    }

    /**
     * Getter for ConcatenationDelimiter attribute.
     *
     * @return the concatenation delimiter to use if required.
     */
    public String getConcatenationDelimiter() {
        return concatenationDelimiter;
    }

    /**
     * Getter for StandardizationTargets attribute.
     *
     * @return the fields to write the standardized structure to.
     */
    public LinkedHashMap getStandardizationTargets() {
        return standardizationTargets;
    }

    /**
     * Getter for StandardizationType attribute.
     *
     * @return the standardization engine specific standardization
     * type identifying the entry to standardize to the standardization engine.
     */
    public String getStandardizationType() {
        return standardizationType;
    }

    /**
     * Getter for domain selector attribute.
     *
     * @return the domain selector attribute.
     */
    public DomainSelector getDomainSelector() {
        return domainSelector;
    }
    
    /**
     * Getter for locales attribute.
     *
     * @return the hash map where the locale information is stored.
     */
    public HashMap getLocales() {
        return locales;
    }
    
    /**
     * Getter for locale field attribute.
     *
     * @return the hash map where the locale information is stored.
     */
    public SystemObjectField getLocaleField() {
        return localeField;
    }
}
