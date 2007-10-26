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
package com.sun.mdm.index.objects.validation;



/**
 * @author jwu
 */
public class FieldType {

   
    static final int UNKNOWN = -1;

    /** Integer             */
    public static final int INTEGER = 0;
    /** Boolean     */
    public static final int BOOLEAN = 1;
    /** String      */
    public static final int STRING = 2;
    /** Byte                 */
    static final int BYTE = 3;
    /** Long                 */
    public static final int LONG = 4;
    /** Raw                 */
    static final int RAW = 5;
    /** Float                */
    public static final int FLOAT = 7;
    /** Date                     */
    public static final int DATE = 6;
    /** Timestamp               */
    public static final int TIMESTAMP = 8;
    /** Character               */
    public static final int CHAR = 9;

    static final int MAX_TYPES = 9;

    static final String[] DESCRIPTION = {
            "Integer", "Boolean", "String", "Byte",
            "Long", "Blob", "Float", "Date", "Timestamp",
            "Character"
            };

    /**
     * Creates a new instance of FieldType
     */
    public FieldType() {
    }

}
