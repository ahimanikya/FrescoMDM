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
package com.sun.mdm.index.ops;

import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.TransactionObject;
import com.sun.mdm.index.ops.exception.OPSException;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * 
 * @author sanjay.sharma
 */
public class TransactionObjectList  {
    private String euid = null; 
    private String startTranNumber = null; 
    private TransactionObject [] tranObjects = null;
    private int currentIndex = 0;
    
    /** Constructor.
     * @param euid EUID of the EnterpriseObject for which the
     * TransactionObjects are to be constructed.
     * @param startTranNumber All transactions from this transaction number.
     * @param transObjects Array of TransactionObject instances.
     */
    public TransactionObjectList(String euid, String startTranNumber, 
                                 TransactionObject [] tranObjects ) {
    	this.euid = euid;    	
    	this.startTranNumber = startTranNumber;    	
    	this.tranObjects = tranObjects;
    }


    /** Return the euid attribute.
     *
     * @return euid attribute.
     */
    public String getEUID() {
        return euid;
    }

    /** Return the startTranNumber attribute.
     *
     * @return startTranNumber attribute.
     */
    public String getStartTransactionNumber() {
        return startTranNumber;
    }

    /** Gets the current transaction.
     *
     * @return current TransactionObject.
     */
    public TransactionObject getCurrentTransaction() throws OPSException {

        if ((tranObjects == null) || (currentIndex >= tranObjects.length))  { 
        	return null;
        }
        
    	return tranObjects[currentIndex];	        
    }

    /** Moves to the next transaction.
     *
     */
    public void moveNext() {
    	currentIndex++;
    }

}
