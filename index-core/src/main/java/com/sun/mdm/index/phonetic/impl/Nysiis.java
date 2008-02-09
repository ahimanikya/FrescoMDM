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
import com.sun.mdm.matcher.util.DiacriticalMarks;
import java.util.logging.Level;
import net.java.hulp.i18n.Logger;

/**
 * Encode a string using the NYSIIS algorithm.
 * @version $Revision: 1.1 $
 */
public class Nysiis implements PhoneticEncoder {

    /** Informative String about the encoding type this encoder does */
    public static final String ENCODING_TYPE = "NYSIIS";
    StringBuffer word = null;

    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    
    
    /** Creates new Nysiis */
    public Nysiis() {
    }

    /**
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

    /**
     * Encode the value using DoubleMetaphone.
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
     * Encode a string phoneticaly using NYSIIS
     *
     * @param originalWord The string to encode
     * @param domain A domain marking the country/language context. An optional feature
     *       for an encoder to implement.
     *       This encoder does not currently support domains.
     * @return the NYSIIS encoded version of the strToEncode
     * @throws PhoneticEncoderException if the encoding fails.
     */
    public String encode(String originalWord, String domain)
        throws PhoneticEncoderException {
        
        char ch;
        
        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Creating NYSIIS encoding for: " + originalWord);
        }
        
        if (originalWord == null) {
            return null;
        }
        
        int len = originalWord.length();

        if (len == 0) {
            return originalWord;
        }
               
        word = new StringBuffer(originalWord.toUpperCase());

        for (int i = 0; i < len; i++) {      
            ch = word.charAt(i);
                
            if (!Character.isLetterOrDigit(ch)) {                
                word.deleteCharAt(i);
                len--;
                i--;
                continue;                 
            }            
            word.setCharAt(i, DiacriticalMarks.removeDiacriticalMark(word.charAt(i)));          
        }       

        char first;

        // strip any trailing S or Zs
        while (word.toString().endsWith("S") || word.toString().endsWith("Z")) {
            word.deleteCharAt(word.length() - 1);
        }

        replaceFront("MAC", "MC");
        replaceFront("PF", "F");
        replaceEnd("IX", "IC");
        replaceEnd("EX", "EC");

        replaceEnd("YE", "Y");
        replaceEnd("EE", "Y");
        replaceEnd("IE", "Y");

        replaceEnd("DT", "D");
        replaceEnd("RT", "D");
        replaceEnd("RD", "D");

        replaceEnd("NT", "N");
        replaceEnd("ND", "N");

        // .EV => .EF
        replaceAll("EV", "EF", 1);

        if (word.length() == 0) {
            return "";
        }
        
        first = word.charAt(0);

        // replace all vowels with 'A'
        // word = replaceAll(   word, "A",  "A" );
        replaceAll("E", "A");
        replaceAll("I", "A");
        replaceAll("O", "A");
        replaceAll("U", "A");

        // remove any 'W' that follows a vowel
        replaceAll("AW", "A");

        replaceAll("GHT", "GT");
        replaceAll("DG", "G");
        replaceAll("PH", "F");

        replaceAll("AH", "A", 1);
        replaceAll("HA", "A", 1);

        replaceAll("KN", "N");
        replaceAll("K", "C");

        replaceAll("M", "N", 1);
        replaceAll("Q", "G", 1);

        replaceAll("SH", "S");
        replaceAll("SCH", "S");

        replaceAll("YW", "Y");

        replaceAll("Y", "A", 1, word.length() - 2);

        replaceAll("WR", "R");

        replaceAll("Z", "S", 1);

        replaceEnd("AY", "Y");

        while (word.toString().endsWith("A")) {
            word.deleteCharAt(word.length() - 1);
        }

        reduceDuplicates();

        if (('A' == first) || ('E' == first) || ('I' == first) 
                || ('O' == first) || ('U' == first)) {
            if (word.length() > 0) {
                word.deleteCharAt(0);
            }
            word.insert(0, first);
        }

        if (mLogger.isLoggable(Level.FINE)) {
            mLogger.fine("Created NYSIIS encoding: " + word);
        }
        
        return word.toString();
    }

    /**
     * Reduce duplicates by iterating through characters in the word
     * and only appending unique characters to new word.
     *
     */
    private void reduceDuplicates() {
        char lastChar;
        StringBuffer newWord = new StringBuffer();

        if (word.length() == 0) {
            return;
        }

        lastChar = word.charAt(0);
        newWord.append(lastChar);

        for (int i = 1; i < word.length(); ++i) {
            if (lastChar != word.charAt(i)) {
                newWord.append(word.charAt(i));
            }

            lastChar = word.charAt(i);
        }

        word = newWord;
    }

    /**
     * Replace each part of the word string matching a certain pattern
     * with another string, looking in the whole word
     * @param find the string pattern to find (and replace)
     * @param repl the string to replace the string pattern with
     */
    private void replaceAll(String find, String repl) {
        replaceAll(find, repl, 0, -1);
    }

    /**
     * Replace each part of the word string matching a certain pattern
     * with another string, looking from a given start position to the end of the word
     * @param find the string pattern to find (and replace)
     * @param repl the string to replace the string pattern with
     * @param startPos the starting position in the word to start replacing
     */    
    private void replaceAll(String find, String repl, int startPos) {
        replaceAll(find, repl, startPos, -1);
    }

    /**
     * Replace each part of the word string matching a certain pattern
     * with another string, looking from a given start position to a given end position
     * @param find the string pattern to find (and replace)
     * @param repl the string to replace the string pattern with
     * @param startPos the starting position in the word to start replacing
     * @param endPos the end position in the word to stop replacing
     */    
    private void replaceAll(String find, String repl, int startPos, int endPos) {
        int pos = word.toString().indexOf(find, startPos);

        if (-1 == endPos) {
            endPos = word.length() - 1;
        }

        while (-1 != pos) {
            if ((-1 != endPos) && (pos > endPos)) {
                break;
            }

            word.delete(pos, pos + find.length());

            word.insert(pos, repl);

            pos = word.toString().indexOf(find, startPos);
        }
    }

    /**
     * Replace the front of the word string matching a certain pattern
     * with another string, if it starts with the 'find' String
     * @param find the string pattern to find (and replace)
     * @param repl the string to replace the string pattern with
     */    
    private void replaceFront(String find, String repl) {
        if (word.toString().startsWith(find)) {
            word.delete(0, find.length());
            word.insert(0, repl);
        }
    }

    /**
     * Replace the end of the word string matching a certain pattern
     * with another string, if it ends with the 'find' String
     * @param find the string pattern to find (and replace)
     * @param repl the string to replace the string pattern with
     */        
    private void replaceEnd(String find, String repl) {
        if (word.toString().endsWith(find)) {
            word.delete(word.length() - find.length(), word.length());
            word.append(repl);
        }
    }

}
