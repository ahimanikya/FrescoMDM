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
package com.sun.mdm.index.report;


/** Report Exception
 *
 * @version
 */
public class ReportException extends com.sun.mdm.index.master.ProcessingException {

    // Creates new ReportException 
    public ReportException() {
    }

    /**
     * Constructs an <code>ReportException</code> with the specified 
     * detail message,
     * @param msg the detailed message
     */
    public ReportException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>ReportException</code> with the specified 
     * cause.
     * @param cause the cause
     */ 
    public ReportException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an <code>ReportException</code> with the specified 
     * detail message and cause.
     * @param msg the detailed message
     * @param cause the cause
     */
    public ReportException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
