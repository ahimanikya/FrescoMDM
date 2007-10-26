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

/**
 * Represents the configuration of a field to phoneticize.
 * @author aegloff
 * @version $Revision: 1.1 $
 */
public class PhoneticizeField {
    private String encodingType;
    private String phoneticizerFieldID;

    private SystemObjectField sourceField;
    private SystemObjectField targetField;

    /**
     * Creates new PhoeneticizeField.
     *
     * @param aEncodingType The encoding type corresponding to an available 
     * phonetic encoder.
     * @param aPhoneticizerFieldID An optional encoder specific ID for the 
     * field if the encoder supports it.
     * @param aSourceField The field from which to get the value to encode.
     * @param aTargetField The field to which to write the encoded version.
     */
    public PhoneticizeField(String aEncodingType, 
                            String aPhoneticizerFieldID, 
                            SystemObjectField aSourceField, 
                            SystemObjectField aTargetField) {
        encodingType = aEncodingType;
        phoneticizerFieldID = aPhoneticizerFieldID;
        sourceField = aSourceField;
        targetField = aTargetField;
    }

    /**
     * Getter for EncodingType.
     *
     * @return The encoding type corresponding to an available phonetic encoder.
     */
    public String getEncodingType() {
        return encodingType;
    }


    /**
     * Getter for PhoneticizerFieldID.
     * 
     * @return an optional encoder specific ID for the field if the encoder 
     * supports it.
     */
    public String getPhoneticizerFieldID() {
        return phoneticizerFieldID;
    }


    /**
     * Getter for SourceField.
     *
     * @return the definition of the field from which to get the value to encode.
     */
    public SystemObjectField getSourceField() {
        return sourceField;
    }


    /**
     * Getter for TargetField.
     *
     * @return the definition of the field to which to write the encoded version.   
     */
    public SystemObjectField getTargetField() {
        return targetField;
    }

}
