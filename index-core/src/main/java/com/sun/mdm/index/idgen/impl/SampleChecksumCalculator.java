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

package com.sun.mdm.index.idgen.impl;

import java.util.logging.Level;

import com.sun.mdm.index.idgen.ChecksumCalculator;
import com.sun.mdm.index.idgen.SEQException;
import com.sun.mdm.index.util.Localizer;

import net.java.hulp.i18n.LocalizationSupport;
import net.java.hulp.i18n.Logger;

/**
 * Default checksum calculator
 * @author gw194542
 */
public class SampleChecksumCalculator implements ChecksumCalculator {
    
    private transient final Logger mLogger = Logger.getLogger(this.getClass().getName());
    private transient final Localizer mLocalizer = Localizer.get();
    private int mChecksumLength;

    /** Default constructor for DefaultChecksumCalculator
     * @throws SEQException An exception occurred
     */
    public SampleChecksumCalculator() throws SEQException {
    }

    /** Parameters of the checksum calculator
     *
     * @param parameterName parameter
     * @param value parameter value
     * @exception SEQException An error occurred
     */
    public void setParameter(String parameterName, Object value)
            throws SEQException {
        if (parameterName.equals("ChecksumLength")) {
            mChecksumLength = ((Integer) value).intValue();
            if (mChecksumLength != 1) {
                throw new SEQException(mLocalizer.t("IDG510: Checksum Length for SampleChecksumCalculator must be 1"));
            }
        } else {
            throw new SEQException(mLocalizer.t("IDG509: Unknown parameter: (0}",
                    parameterName));
        }
    }

    /** Calculate the checksum for the supplied data.
     *
     * This calculator uses a variation of the ISBN-10 checksum calculation.
     * As it uses modulo 11, if the checkdigit is ten it will return null,
     * effectively asking for a new base number to be passed to it.
     *
     * @param baseData data for which the checksum is to be calculated
     * @return calculated checksum or <b>null</b> if no checksum can be calculated.
     * @exception SEQException An error occurred
     */
    public String calcChecksum(String baseData)
        throws SEQException {

        String retVal = null;
        
        // Derive the check digits
        long sum = 0;
        for (int i = 0; i < baseData.length(); i++) {
            int x = Integer.parseInt(String.valueOf(baseData.charAt(i)));
            sum = sum + x * (i+1);
        }
        int ck = (int) (sum % 11);
        if (ck == 10) {
            // invalid number
            return null;
        }
        return String.valueOf(ck);
    }
}
