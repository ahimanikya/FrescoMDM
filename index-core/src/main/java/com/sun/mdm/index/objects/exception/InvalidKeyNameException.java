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
package com.sun.mdm.index.objects.exception;



/**
 * @author gzheng
 */
public class InvalidKeyNameException extends ObjectException {

    /**
     * Creates a new instance of <code>InvalidKeyNameException</code> without
     * detail message.
     */
    public InvalidKeyNameException() {
        super("Invalid key name array");
    }


    /**
     * Constructs an instance of <code>InvalidKeyNameException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidKeyNameException(String msg) {
        super(msg);
    }
}
