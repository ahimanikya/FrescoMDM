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
package com.sun.mdm.index.report.client;

/**
 *
 * @author  rtam
 */
public class StringFormatter {
    
    /** Creates a new instance of StringFormatter */
    public StringFormatter() {
    }
    /**  Right pads a string with the assigned character
     *
     * @param str  string to pad
     * @param length  desired length of string (starting from 1)
     * @param c  character for padding
     * @return padded string
     */
    public String rightPadString(String str, int length, char c) {
        if (str == null || length < 0)  {
            return null;
        }
        int i = str.length();
        //  truncate string if desired length is less than the current length
        if (i >= length)  {
            return str.substring(0, length);
        }
        StringBuffer sbuf = new StringBuffer(str);
        for(; i < length; i++)  {
            sbuf.append(c);
        }
        return sbuf.toString();
    }

    /**  Left pads a string with the assigned character
     *
     * @param str  string to pad
     * @param length  desired length of string (starting from 1)
     * @param c  character for padding
     * @return padded string
     */
    public String leftPadString(String str, int length, char c) {
        if (str == null || length < 0)  {
            return null;
        }
        int i = str.length();
        //  truncate string if desired length is less than the current length
        if (i >= length)  {
            return str.substring(0, length);
        }
        StringBuffer sbuf = new StringBuffer(length);
        for(i = 0; i < length - str.length(); i++)  {
            sbuf.append(c);
        }
        sbuf.append(str);
        return sbuf.toString();
    }
    
    /**  Center a string and pad with the assigned character
     *
     * @param str  string to center and pad
     * @param length  desired length of string (starting from 1)
     * @param c  character for padding
     * @return padded string
     */
    public String centerNPadString(String str, int length, char c) {
        if (str == null || length < 0)  {
            return null;
        }
        int stringLen = str.length();
        //  truncate string if desired length is less than the current length
        if (stringLen >= length)  {
            return str.substring(0, length);
        }
        String firstHalf = str.substring(0, stringLen/2);
        String lastHalf = str.substring(stringLen/2, stringLen);
        StringBuffer sbuf = new StringBuffer(length);

        sbuf.append(leftPadString(firstHalf, length/2, c));
        sbuf.append(rightPadString(lastHalf, length/2, c));
        
        return sbuf.toString();
    }
    
    public static void main(String args[]) {
    
        String s1 = "EUID Merge";
        int width = 14;
        StringFormatter formatter = new StringFormatter();
        
        System.out.println("Center string: " + s1);
        System.out.println("<" + formatter.centerNPadString(s1, width, ' ') + ">\n");	
    }
}
