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
import com.sun.mdm.index.matching.DomainSelector;

/**
 * Represents the configuration for a 'standardize system object' entry,
 * defining how to standardize a given system object.
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class SystemObjectStandardization {
    /** ArrayList of PhoneticizeField instances */
    private ArrayList phoneticize;
    /** ArrayList of PreparsedFieldGroup instances */
    private ArrayList preParsed;
    /** ArrayList of UnparsedFieldGroup instances */
    private ArrayList unParsed;
    
    private String qualifiedName;

    /**
     * Creates new SystemObjectStandardization.
     *
     * @param aPhoneticize The PhoneticizeField definitions of what to phoneticize.
     * @param aPreParsed The PreparsedFieldGroup definitions of what to normalize.
     * @param aUnParsed The UnparsedFieldGroup definitions of what to standardize.
     * @param aQualifiedName The qualified name of the SystemObject this configuration
     * relates to.
     */
    public SystemObjectStandardization(ArrayList aPhoneticize, 
                                       ArrayList aPreParsed, 
                                       ArrayList aUnParsed, 
                                       String aQualifiedName) {
        phoneticize = aPhoneticize;
        preParsed = aPreParsed;
        unParsed = aUnParsed;
        qualifiedName = aQualifiedName;
    }

    /**
     * Getter for FieldsToPhoneticize.
     *
     * @return an ArrayList of PhoneticizeField instances defining which fields
     * need to be phoneticized and how.
     */
    public ArrayList getFieldsToPhoneticize() {
        return phoneticize;
    }

    /**
     * Getter for PreParsedFieldGroups.
     *
     * @return an ArrayList of PreparsedFieldGroup instances defining which fields
     * need to normalized and how.
     */
    public ArrayList getPreParsedFieldGroups() {
        return preParsed;
    }

    /**
     * Getter for UnParsedFieldGroups.
     *
     * @return an ArrayList of UnparsedFieldGroup instances defining which fields
     * need to standardized and how.
     */
    public ArrayList getUnParsedFieldGroups() {
        return unParsed;
    }    

    /**
     * Getter for QualifiedName.
     *
     * @return the qualified name of the SystemObject this configuration relates to.
     */
    public String getQualifiedName() {
        return qualifiedName;
    }
    
}
