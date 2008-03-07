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
package com.sun.mdm.index.webservice;

import com.sun.mdm.index.master.MatchResult;

/**
 * Return value for webservice executeMatch
 */

public class MatchColResult {
    
    String mEUID;
    int mResultCode;
    public MatchColResult(){
    }
    
    public MatchColResult(MatchResult matchResult){
        mEUID = matchResult.getEUID();
        mResultCode = matchResult.getResultCode();
    }
    
    /** Getter for EUID attribute of the MatchResult object
     * @return EUID
     */
    public String getEUID(){
        return mEUID;
    }
    
    /** 
     * Setter EUID
     * @param euid
     */
    public void setEUID(String euid){
        mEUID = euid;
    } 
    
    /** Getter for ResultCode
     * @return resultCode
     */
    public int getResultCode(){
        return mResultCode;
    }
    
    /** 
     * Setter for ResultCode
     * @param resultCode
     */
    public void setResultCode(int resultCode){
        mResultCode =resultCode;
    }
    
    
}
