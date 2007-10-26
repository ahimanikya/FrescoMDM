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
 * Each field in an object node has control flags represented by a number. The
 * <b>InvalidKeyTypeException</b> class represents an exception thrown
 * when a field's control flag number is not recognized as a known flag.
 * @author gzheng
 */
public class InvalidKeyTypeException extends ObjectException {

    /**
     * Creates a new instance of the InvalidKeyTypeException class without
     * an error message.
     * <p>
     * <DL><DT><B>Parameters:</B><DD>None.</DL>
     * <DL><DT><B>Throws:</B><DD>None.</DL>
     * @include
     */
    public InvalidKeyTypeException() {
        super("invalid key type");
    }


    /**
     * Creates a new instance of the InvalidKeyTypeException class with
     * an error message.
     * <p>
     * @param msg The error message to display for the type of error
     * that occurred.
     * <DT><B>Throws:</B><DD>None.
     * @include
     */
    public InvalidKeyTypeException(String msg) {
        super(msg);
    }
}
