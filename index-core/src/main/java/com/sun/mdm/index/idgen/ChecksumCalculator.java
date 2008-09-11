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

package com.sun.mdm.index.idgen;

/**
 * Interface to calculate checksums
 * @author gw194542
 */
public interface ChecksumCalculator {

    /** Parameters of the checksum calculator
     * file are set using this method.
     *
     * @param parameterName parameter
     * @param value parameter value
     * @exception SEQException An error occurred
     */
    void setParameter(String parameterName, Object value)
        throws SEQException;


    /** Calculate the checksum for the supplied data.
     *
     * A return value of null indicates that no checksum is valid for the supplied data.
     * This could be used, for example, where a modulo 11 checksum algorithm is used, and
     * the algorithm specifies that if the remainder is 10, the original number is invalid.
     * @param baseData data for which the checksum is to be calculated
     * @return calculated checksum or <b>null</b> if no checksum is valid
     * @exception SEQException An error occurred
     */
    String calcChecksum(String baseData)
        throws SEQException;

}
