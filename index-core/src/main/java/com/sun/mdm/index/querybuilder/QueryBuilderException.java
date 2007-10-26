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
package com.sun.mdm.index.querybuilder;

/**
 * Query builder exception
 * @author dcidon
 */
public class QueryBuilderException extends com.sun.mdm.index.master.ProcessingException {

    /**
     * Creates new ProcessException
     */
    public QueryBuilderException() {
        super();
    }


    /** Constructor
     * @param msg Error message
     */
    public QueryBuilderException(String msg) {
        super(msg);
    }


    /** Constructor
     * @param msg Error message
     * @param t Chained exception
     */
    public QueryBuilderException(String msg, Throwable t) {
        super(msg, t);
    }


    /** Constructor
     * @param t Chained exception
     */
    public QueryBuilderException(Throwable t) {
        super(t);
    }
}
