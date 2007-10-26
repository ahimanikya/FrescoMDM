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
package com.sun.mdm.index.phonetic;

import java.util.HashMap;
import java.util.Iterator;

import com.sun.mdm.index.configurator.ConfigurationService;
import com.sun.mdm.index.configurator.impl.PhoneticEncodersConfig;

/**
 * Manages all Phonetic Encoder instances and provides access to their
 * functionality by name
 *
 * @author  aegloff
 * @version 
 */
public class Phoneticizer {

    private HashMap encoders;    // A map from the encoding type to the PhoneticEncoder instance
    
    /** 
     * Creates new Phoneticizer 
     * @throws PhoneticEncoderException if the configured encoders can not be registred
     */
    public Phoneticizer() 
            throws PhoneticEncoderException {
        registerPhoneticEncoders();
    }

    /**
     * Encode a string phoneticaly with the relevant phonetic encoder
     * for the specified encodingType
     *
     * @param strToEncode The string to encode
     * @param encodingType The phonetic encoding type, which maps onto a responsible encoder
     * @param domain A domain marking the country/language context. An optional feature 
     *        for an encoder to implement.
     * @return the encoded version of the strToEncode
     * @throws PhoneticEncoderException if no phonetic encoder is present for the 
     *        specified encodingType or the encoding by the PhoneticEncoder fails.
     */
    public String encode(String strToEncode, String encodingType, String domain) 
            throws PhoneticEncoderException {
        
        PhoneticEncoder searchEncoder = (PhoneticEncoder) encoders.get(encodingType);
        
        if (searchEncoder == null) {
            throw new PhoneticEncoderException("Unknown phonetic encoding type: " + encodingType);
        }
        
        String encodedStr = searchEncoder.encode(strToEncode, domain);
        
        return encodedStr;
    }

    /**
     * Read the available encoders from a Configuration, instantiate them
     * and populate the internal map of encoding type to PhoneticEncoder
     * @throws PhoneticEncoderException if the encoders could not be loaded or registered
     */
    void registerPhoneticEncoders() 
            throws PhoneticEncoderException {
        
        encoders = new java.util.HashMap();
        PhoneticEncodersConfig encodersConfig = null;

        // Use Configuration factory to retrieve phonetic encoder configuration
        try {
            ConfigurationService cfgFactory = ConfigurationService.getInstance();
            encodersConfig = 
                (PhoneticEncodersConfig) cfgFactory.getConfiguration(PhoneticEncodersConfig.PHONETIC_ENCODERS);
        } catch (InstantiationException ex) {
            throw new PhoneticEncoderException (
                    "Failed to instantiate configuration classes to obtain config for phonetic encoders. " 
                    + ex.getMessage(), ex);
        }

        
        // Go through all the configured encoders and instantiate each one, populating the encoders HashMap
        HashMap encodersClasses = encodersConfig.getEncodersClasses();
        Iterator encodingTypesIter = encodersClasses.keySet().iterator();
        while (encodingTypesIter.hasNext()) {
            PhoneticEncoder encoderInstance = null;            
            String encodingType = (String) encodingTypesIter.next();
            String encoderClassName = (String) encodersClasses.get(encodingType);
            try {
                Class encoderClass = Class.forName(encoderClassName);
                encoderInstance = (PhoneticEncoder) encoderClass.newInstance(); 
            } catch (ClassNotFoundException ex) {
                throw new PhoneticEncoderException ("Failed to load configured phonetic encoder class: " 
                        + encoderClassName + " msg: " + ex.getMessage(), ex);
            } catch (Exception ex) {
                throw new PhoneticEncoderException ("Failed to instantiate the configured phonetic encoder class: " 
                        + encoderClassName + " msg: " + ex.getMessage(), ex);
            }
            encoders.put(encodingType, encoderInstance);
        }
    }

}
