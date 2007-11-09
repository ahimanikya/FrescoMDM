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
package com.sun.mdm.index.phonetic.impl;

import com.sun.mdm.index.phonetic.PhoneticEncoder;
import com.sun.mdm.index.phonetic.PhoneticEncoderException;
import com.stc.sbme.util.EmeUtil;

import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * Encode a string using the Soundex algorithm.
 *
 * @version $Revision: 1.1 $
 */
public class Soundex implements PhoneticEncoder {
    
    /** Informative String about the encoding type this encoder does */
    public static final String ENCODING_TYPE = "SoundEx";
    
    /** The default soundex mapping used */
    public static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();    

    private char[] soundexMapping;
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
    
    /**
     * Creates a new Soundex encoder
     */
    public Soundex() {
        this(US_ENGLISH_MAPPING);
    }

    /**
     * Creates a new Soundex encoder using a given soundex mapping
     * @param mapping the soundex mapping to use
     */    
    public Soundex(char[] mapping) {
        this.soundexMapping = mapping;
    }

    /**
     * Encode the value using Soundex.
     *
     * @param value String to encode
     * @return An encoded String
     * @throws PhoneticEncoderException thrown if there is an exception during
     *         the encoding process.
     */
    public String encode(String value) 
        throws PhoneticEncoderException {
        return encode(value, "US");   
    }

    /**
     * Encode a string phoneticaly with SoundEx.
     *
     * This algorithm is sampled from the code-snippers on
     * http://www.sourceforge.net/
     *
     * @param str The string to encode
     * @param domain A domain marking the country/language context. An optional feature
     *       for an encoder to implement.
     *       This encoder does not currently support domains.
     * @return the encoded version of the strToEncode
     * @throws PhoneticEncoderException if the encoding fails.
     */
    public String encode(String str, String domain)
        throws PhoneticEncoderException {
        char[] out = {'0', '0', '0', '0'};
        char last;
        char mapped;
        int incount = 1;
        int count = 1;
        int len = str.length();
        char ch;
        StringBuffer word;
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Creating Soundex encoding for: " + str);
        }
        
        if (str == null || len == 0) {
            return null;
        }

        word = new StringBuffer(str);

        for (int i = 0; i < len; i++) { 
            ch = str.charAt(i);  
            
            if ((int) ch > 191) {                         
                word.setCharAt(i, EmeUtil.removeDiacriticalMark(ch));
            }
        }        
        
        out[0] = Character.toUpperCase(word.charAt(0));
        last = getMappingCode(word.charAt(0));
        
        while ((incount < len) 
                && ((mapped = getMappingCode(word.charAt(incount++))) != 0) 
                && (count < 4)) {
            if ((mapped != '0') && (mapped != last)) {
                out[count++] = mapped;
            }

            last = mapped;
        }

        String word1 = new String(out);

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Created Soundex encoding: " + word1);
        }        
        
        return word1;
    }

    /**
     * Used internally by the SoundEx algorithm.
     * @param c the character to encode
     * @return char the character was mapped to
     */
    private char getMappingCode(char c) {
        if (!Character.isLetter(c)) {
            return 0;
        } else {
            int i = Character.toUpperCase(c) - 'A';

            if ((i >= 0) && (i < soundexMapping.length)) {
                return soundexMapping[i];
            } else {
                return 0;
            }
        }
    }

    /**
     * Getter for the encoding type attribute
     * @return the encoding type the Phonetic Encoder implements
     * (such as NYSIIS or Soundex)
     * This value is for information purposes and is not enforced to be of the
     * same value as the 'encoding type' listed in the PhoneticEncodersConfiguration
     * for the encoder class instance. As a result it should not be used for calls
     * to the Phoneticizer.
     */
    public String getEncodingType() {
        return ENCODING_TYPE;
    } 
    
}
