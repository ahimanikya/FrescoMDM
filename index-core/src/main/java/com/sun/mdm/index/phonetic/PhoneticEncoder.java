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

/**
 * Interface a phonetic encoder should implement so the framework can use it.
 * @author  aegloff
 * @version $Revision: 1.1 $
 */
public interface PhoneticEncoder {
    
    /**
     * Encode a string phoneticaly
     *
     * @param strToEncode The string to encode
     * @param domain A domain marking the country/language context. An optional feature 
     *        for an encoder to implement.
     * @return the encoded version of the strToEncode
     * @throws PhoneticEncoderException if the encoding fails.
     */
    public String encode(String strToEncode, String domain) throws PhoneticEncoderException;
 

    /**
     * Encode a string phoneticaly
     *
     * @param strToEncode The string to encode
     * @return the encoded version of the strToEncode
     * @throws PhoneticEncoderException if the encoding fails.
     */
    public String encode(String strToEncode) throws PhoneticEncoderException;
   
    /**
     * Getter for the encoding type
     * @return the encoding type the Phonetic Encoder implements 
     * (such as NYSIIS or Soundex)
     * This value is for information purposes and is not enforced to be of the 
     * same value as the 'encoding type' listed in the PhoneticEncodersConfiguration 
     * for the encoder class instance. As a result it should not be used for calls
     * to the Phoneticizer.
     */
    public String getEncodingType();
}
