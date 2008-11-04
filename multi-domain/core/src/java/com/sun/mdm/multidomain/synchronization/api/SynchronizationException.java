/*
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
*
* Copyright 1997-2007 Sun Microsystems, Inc. All Rights Reserved.
*
* The contents of this file are subject to the terms of the Common
* Development and Distribution License ("CDDL")(the "License"). You
* may not use this file except in compliance with the License.
*
* You can obtain a copy of the License at
* https://open-esb.dev.java.net/public/CDDLv1.0.html
* or mural/license.txt. See the License for the specific language
* governing permissions and limitations under the License. *
* When distributing Covered Code, include this CDDL Header Notice
* in each file and include the License file at mural/license.txt.
* If applicable, add the following below the CDDL Header, with the
* fields enclosed by brackets [] replaced by your own identifying
* information: "Portions Copyrighted [year] [name of copyright owner]"
*/ 

package com.sun.mdm.multidomain.synchronization.api;

import net.java.hulp.i18n.LocalizedString;

/**
 * The base class that handles exceptions related to the standardization engine
 * 
 */
public class SynchronizationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6009470345372096165L;

    /**
     * Constructs an <code>MdmStandardizationException</code> with no
     * detail message.
     * 
     * @param t
     *            a throwable
     */
    public SynchronizationException(final Throwable t) {
        super(t);
    }

    /**
     * Constructs an <code>MdmStandardizationException</code> with the
     * specified detail message.
     * 
     * @param s
     *            the detail message
     * @param t
     *            a throwable
     * 
     */
    public SynchronizationException(final String s, final Throwable t) {
        super(s, t);
    }

    /**
     * Constructs an <code>MdmStandardizationException</code> with the
     * specified detail message.
     * 
     * @param s
     *            the detail message.
     */
    public SynchronizationException(final String s) {
        super(s);
    }
    
    /**
     * Constructs an <code>MdmStandardizationException</code> with the
     * specified detail message.
     * 
     * @param s
     *            the detail message.
     */
    public SynchronizationException(final LocalizedString s) {
    	super(s.toString());
    }
}
