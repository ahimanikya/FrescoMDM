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

import java.util.NoSuchElementException;


/** Used by parsers.
 *
 * @author  dcidon
 */
class StringTokenizer extends Object {
    private char[] inputCharArray;
    private int stringLength;
    private char delimiter;
    private int position;
    private String nextToken = null;
    private boolean returnNullString;

    /** Creates new StringTokenizer */
    StringTokenizer(String inputString, char delimiter) {
        this.inputCharArray = inputString.toCharArray();
        this.delimiter = delimiter;
        this.stringLength = inputString.length();
        this.position = 0;
        returnNullString = false;
    }

    StringTokenizer(String inputString, char delimiter, boolean returnNullString) {
        this(inputString, delimiter);
        this.returnNullString = returnNullString;
    }

    private void getNextToken() {
        int tokenStartPosition = position;

        if (position < stringLength) {
            while (inputCharArray[position] != delimiter) {
                position++;

                if (position == stringLength) {
                    break;
                }
            }

            int tokenLength = position - tokenStartPosition;

            if (tokenLength == 0) {
                nextToken = null;
            } else {
                nextToken = new String(inputCharArray, tokenStartPosition,
                        tokenLength);
            }
        } else {
            nextToken = null;
        }

        position++;
    }

    public boolean hasMoreElements() {
        return (position <= stringLength);
    }

    public String nextToken() throws NoSuchElementException {
        if (!hasMoreElements()) {
            throw new NoSuchElementException("No more elements in token: " 
                + new String(inputCharArray) + "\nToken Delimeter: " + delimiter);
        }

        getNextToken();

        if ((nextToken == null) && returnNullString) {
            nextToken = new String("");
        }

        return nextToken;
    }
}
