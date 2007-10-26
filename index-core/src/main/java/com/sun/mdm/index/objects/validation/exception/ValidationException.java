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
package com.sun.mdm.index.objects.validation.exception;



/**
 * @author jwu
 */
public class ValidationException extends com.sun.mdm.index.master.UserException {

    private String systemCode = null;   // system code
    private String id = null;           // ID of object to validate
    private String format = null;       // expected format
    private String systemDesc = null;   // system description
    
    /**
     * Creates a new instance of ValidationException without detail
     * message.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */

    public ValidationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ValidationException</code> with the
     * specified detail message.
     *
     * @param systemCode the system code.
     * @param systemDesc the system description.
     * @param format the expected format.
     * @param id the ID to validate.
     * @param msg the detail message.
     */

    public ValidationException(String systemCode, String systemDesc, String format, String id, String msg) {
        super(msg);
        this.systemCode=systemCode;
        this.systemDesc=systemDesc;
        this.format=format;
        this.id=id;
    }

    /**
     * @param msg the detail message.
     * @param t thrown exception
     */
    public ValidationException(String msg, Throwable t) {
        super(msg, t);
    }


    /**
     * @param t thrown exception
     */
    public ValidationException(Throwable t) {
        super(t);
    }

    /**
     *  Getter for the systemCode.
     *
     * @return  the system code
     */
    public String getSystemCode() {     
        return systemCode;
    }
    
    /**
     *  Getter for the systemDescription.
     *
     * @return  the system description
     */
    public String getSystemDescription() {     
        return systemDesc;
    }
    
    /**
     *  Getter for the format.
     *
     * @return  the expected format
     */
    public String getFormat() {     
        return format;
    }
    
    /**
     *  Getter for the ID.
     *
     * @return  the ID
     */
    public String getId() {     
        return id;
    }

}
