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
package com.sun.mdm.index.ejb.master.helper;


/**
 * String formatting helper class
 * @author Dan Cidon
 */
public class StringUtil {
    private static final char[] CHAR_MAP = new char[256];
    private static final char[] CHAR_MAP_UPPER = new char[256];
    /** Format enumeration */
    public static final int STRIP_DIACRITICAL_MARKS = 0;
    /** Format enumeration */
    public static final int STRIP_DIACRITICAL_MARKS_STRIP_PUNCTUATION = 1;
    /** Format enumeration */
    public static final int STRIP_DIACRITICAL_MARKS_STRIP_PUNCTUATION_KEEP_WILDCARD = 2;

    static {
        initializeCharMap();
    }

    /** Creates new StringUtil */
    public StringUtil() {
    }

    private static void initializeCharMap() {
        for (char c = 0; c < 256; c++) {
            if (c < 192) {
                CHAR_MAP[c] = c;
            } else if ((c >= 192) && (c <= 197)) {
                CHAR_MAP[c] = 'A';
            } else if (c == 199) {
                CHAR_MAP[c] = 'C';
            } else if ((c >= 200) && (c <= 203)) {
                CHAR_MAP[c] = 'E';
            } else if ((c >= 204) && (c <= 207)) {
                CHAR_MAP[c] = 'I';
            } else if (c == 208) {
                CHAR_MAP[c] = 'D';
            } else if (c == 209) {
                CHAR_MAP[c] = 'N';
            } else if (((c >= 210) && (c <= 214)) || (c == 216)) {
                CHAR_MAP[c] = 'O';
            } else if ((c >= 217) && (c <= 220)) {
                CHAR_MAP[c] = 'U';
            } else if (c == 221) {
                CHAR_MAP[c] = 'Y';
            } else if ((c >= 224) && (c <= 229)) {
                CHAR_MAP[c] = 'a';
            } else if (c == 231) {
                CHAR_MAP[c] = 'c';
            } else if ((c >= 232) && (c <= 235)) {
                CHAR_MAP[c] = 'e';
            } else if ((c >= 236) && (c <= 239)) {
                CHAR_MAP[c] = 'i';
            } else if (c == 240) {
                CHAR_MAP[c] = 'd';
            } else if (c == 241) {
                CHAR_MAP[c] = 'n';
            } else if (((c >= 242) && (c <= 246)) || (c == 248)) {
                CHAR_MAP[c] = 'o';
            } else if ((c >= 249) && (c <= 252)) {
                CHAR_MAP[c] = 'u';
            } else if ((c == 253) || (c == 255)) {
                CHAR_MAP[c] = 'y';
            } else {
                CHAR_MAP[c] = c;
            }
        }

        for (char c = 0; c < 256; c++) {
            if (Character.isLetter(c)) {
                CHAR_MAP_UPPER[c] = Character.toUpperCase(CHAR_MAP[c]);
            } else {
                CHAR_MAP_UPPER[c] = c;
            }
        }
    }

    /** Format string
     * @param s string
     * @param format format
     * @param CHAR_MAP character map
     * @return formatted string
     */    
    private static String formatString(String s, int format, char[] charMap) {
        if (s == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        char[] charArray = s.toCharArray();

        switch (format) {
        case STRIP_DIACRITICAL_MARKS:

            for (int i = 0; i < charArray.length; i++) {
                sb.append(charMap[charArray[i]]);
            }

            break;

        case STRIP_DIACRITICAL_MARKS_STRIP_PUNCTUATION:

            for (int i = 0; i < charArray.length; i++) {
                char ch = charArray[i];

                if (Character.isLetterOrDigit(ch)) {
                    sb.append(charMap[ch]);
                }
            }

            break;

        case STRIP_DIACRITICAL_MARKS_STRIP_PUNCTUATION_KEEP_WILDCARD:

            for (int i = 0; i < charArray.length; i++) {
                char ch = charArray[i];

                if (Character.isLetterOrDigit(ch) || (ch == '%') || (ch == '_')) {
                    sb.append(charMap[ch]);
                }
            }

            break;

        default:
            throw new RuntimeException("Unknown format enumeration: " + format);
        }

        return sb.toString();
    }

    /** Format string
     * @param s string
     * @param format format
     * @return formatted string
     */   
    public static String formatString(String s, int format) {
        return formatString(s, format, CHAR_MAP);
    }

    /** Change string to upper case
     * @param s string
     * @param format format
     * @return formatted string
     */
    public static String formatStringToUpper(String s, int format) {
        return formatString(s, format, CHAR_MAP_UPPER);
    }

    /** Strip character from string
     * @param s string
     * @param stripChar character to be stripped
     * @return modified string
     */
    public static String stripChar(String s, char stripChar) {
        if (s == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        char[] charArray = s.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];

            if (ch != stripChar) {
                sb.append(CHAR_MAP[ch]);
            }
        }

        return sb.toString();
    }
}
