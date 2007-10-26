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

import com.stc.sbme.util.EmeUtil;

/**
 * Encode a string using a version of French Soundex algorithm (Soundex2 by .
 *
 * @version $Revision: 1.1 $
 */
public class SoundexFR {
    
    /** Informative String about the encoding type this encoder does */
    public static final String ENCODING_TYPE = "SoundEx_FR";
    /**  */
    private static final String[] GROUP1_INPUT = {"GUI", "GUE", "GA", "GO", "GU", "CA", "CO", "CU", "Q", "CC", "CK"};
    private static final String[] GROUP1_OUTPUT = {"KI", "KE", "KA", "KO", "K", "KA", "KO", "KU", "K", "K", "K"};
    private static final String[] GROUP2_INPUT = {"MAC", "ASA", "KN", "PF", "SCH", "PH"};
    private static final String[] GROUP2_OUTPUT = {"MCC", "AZA", "NN", "FF", "SSS", "FF"};
    
    private static final char[] VOWELS = {'E', 'I', 'O', 'U'};
    
    /**
     * Creates a new Soundex encoder
     */
    public SoundexFR() {
    }

    /**
     * Encode a string phoneticaly with SoundEx.
     *
     * This algorithm is sampled from the code-snippers on
     * http://www.sourceforge.net/
     *
     * @param str The string to encode
     * @return the encoded version of the strToEncode
     * @throws PhoneticEncoderException if the encoding fails.
     */
    public String encode(String str) {
        
        // Trim the token from trailing spaces
        str.trim();
        int size = str.length();
        char ch;
        int i;
        
        // If the token is empty, return an empty 4 character-long string
        if (size == 0) {
            return "    ";
        }
        
        // Convert all charatcers to uppercase
        str = str.toUpperCase();
        
        // If the token is one character long, return the character and 3 spaces
        if (size == 1) {
            return str + "   ";
        }
        
        // Wrap the String into a Stringbuffer
        StringBuffer word = new StringBuffer(str);  
                      
        // Remove all diacretical marks
        for (i = 0; i < size; i++) { 
            ch = str.charAt(i);  

            // Remove all special characters and numbers
            if (!Character.isLetter(ch)) {
                word.deleteCharAt(i);
                size--;
                i--;
                continue;
            }
            
            if ((int) ch > 191) {                         
                word.setCharAt(i, EmeUtil.removeDiacriticalMark(ch));
            }
        }               
        
        // Replace the group1 of substrings with their corresponding outputs
        size = GROUP1_INPUT.length;
        str = word.toString();
        
        for (i = 0; i < size; i++) {
            str = str.replaceAll(GROUP1_INPUT[i], GROUP1_OUTPUT[i]);
        }
        
        // Replace all vowels (E,I,O,U) with A (excluding the fist character?)
        size = str.length();

        for (i = 1; i < size; i++) {
            ch = str.charAt(i);
            if (isVowel(ch)) {
                str = str.replace(ch, 'A');
            }
        }
        
        // Replace the group2 of substrings with their corresponding outputs
        size = GROUP2_INPUT.length;
        
        for (i = 0; i < size; i++) {
            str = str.replaceAll(GROUP2_INPUT[i], GROUP2_OUTPUT[i]);
        }
                
        // Remove all Hs if they are not preceeded by C or S
        size = str.length();
        word.setLength(0);
        word.append(str);

        for (i = 0; i < size; i++) {            
            if (word.charAt(i) == 'H') {
                if (!(i > 0 && (word.charAt(i - 1) == 'C' || word.charAt(i - 1) == 'S'))) {
                    word.deleteCharAt(i);
                    size--;
                    i--;
                }                
            }
        }
            
        // Remove all Ys, unless they are preceeded by As
        for (i = 0; i < size; i++) {            
            if (word.charAt(i) == 'Y') {
                if (!(i > 0 && word.charAt(i - 1) == 'A')) {
                    word.deleteCharAt(i);
                    size--;
                    i--;
                }                
            }
        }
                
        // Remove the following characters (A, T, D, S) if they are the last character
        ch = word.charAt(size - 1);
        if (ch == 'A' || ch == 'T' || ch == 'D' || ch == 'S') {
            word.deleteCharAt(size - 1);
            size--;
        }
        
        // Remove all As. Keep it if it is the first character of the token
        for (i = 1; i < size; i++) {            
            if (word.charAt(i) == 'A') {
                word.deleteCharAt(i);
                size--;
                i--;
            }                         
        } 
               
        // Remove all subsets of similar successive characters
        removeSimilarGroupChars(word);
        
        // Keep only the first 4 characters (if the token is smaller, add spaces).
        size = word.length();
        
        if (word.length() >= 4) {
            word.setLength(4);
        } else {
            while (size++ < 4) {
                word.append(" ");
            }
        }
        
        return word.toString();
    }

    /**
     * Tests if the character is a vowel.
     * @param ch the character to test
     * @return true if character is a vowel, otherwise, false
     */
    private boolean isVowel(char ch) {
        int size = VOWELS.length;
        
        for (int i = 0; i < size; i++) {
            if (ch == VOWELS[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes duplicate characters in a word if they are in succession.
     * @param word the string buffer containing the word to check and modify
     */
    private void removeSimilarGroupChars(StringBuffer word) {
        int size = word.length();
        
        for (int i = 0; i < size; i++) {
            if (i < size - 1 && word.charAt(i) == word.charAt(i + 1)) {
                word.deleteCharAt(i);
                size--;
                i--;
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
