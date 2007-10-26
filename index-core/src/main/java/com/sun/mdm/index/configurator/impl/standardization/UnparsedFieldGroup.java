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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;

/**
 * Represents the configuration for a 'free-form texts to standardize' field group,
 * referred to in this class as a 'unparsed' field group as the standardization
 * process consists of two stages: parsing and normalizing. This group represents 
 * a structure which does not yet fit the standardized structure and therefore
 * needs parsing as well as normalizing.
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class UnparsedFieldGroup extends StandardizationFieldGroup {

    /** SystemObjectField instances that represent the source fields for standardization */    
    private ArrayList sourceFields;

    /**
     * Creates new UnparsedFieldGroup.
     *
     * @param aSourceFields The SystemObjectField instances defining the 
     * source fields belonging to one standardization type
     * to standardize.
     * @param aConcatenationDelimiter The concatenation delimiter to use if required.
     * @param aStandardizationTargets The fields to write the standardized structure to.
     * @param aStandardizationType The standardization engine specific standardization.
     * type identifying the entry to standardize to the standardization engine
     * @param aDomainSelector The default domain selector.
     * @throws ClassNotFoundException if a class is not found.
     * @throws InstantiationException if there is an InstantiationException.
     * @throws IllegalAccessException if there is an IllegalAccessException.
     */
    public UnparsedFieldGroup(ArrayList aSourceFields, 
                              String aConcatenationDelimiter, 
                              LinkedHashMap aStandardizationTargets, 
                              String aStandardizationType, 
                              String aDomainSelector) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        super(aConcatenationDelimiter, aStandardizationTargets, aStandardizationType, aDomainSelector);
        sourceFields = aSourceFields;
    }

    /**
     * Creates new UnparsedFieldGroup.
     *
     * @param aSourceFields The SystemObjectField instances defining the 
     * source fields belonging to one standardization type to standardize.
     * @param aConcatenationDelimiter The concatenation delimiter to use if required.
     * @param aStandardizationTargets The fields to write the standardized structure to.
     * @param aLocales the hash map containing the locale mappings.
     * @param aStandardizationType The standardization engine specific standardization.
     * @param aDomainSelector The default domain selector.
     * @param aLocaleField The field containing the locale information
     * type identifying the entry to standardize to the standardization engine.
     * @throws ClassNotFoundException if a class is not found.
     * @throws InstantiationException if there is an InstantiationException.
     * @throws IllegalAccessException if there is an IllegalAccessException.
     */
    public UnparsedFieldGroup(ArrayList aSourceFields, 
                              String aConcatenationDelimiter, 
                              LinkedHashMap aStandardizationTargets, 
                              HashMap aLocales, 
                              String aStandardizationType, 
                              String aDomainSelector, 
                              SystemObjectField aLocaleField)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        super(aConcatenationDelimiter, aStandardizationTargets, aLocales, aStandardizationType, 
                aDomainSelector, aLocaleField);
        sourceFields = aSourceFields;
    }

    /**
     * Getter for SourceFields attribute.
     *
     * @return an ArrayList of SystemObjectField instances defining the 
     * source fields belonging to one standardization type to standardize.
     */
    public ArrayList getSourceFields() {
        return sourceFields;
    }
}
