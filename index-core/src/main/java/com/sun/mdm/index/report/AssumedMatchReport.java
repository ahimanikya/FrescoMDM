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

/**
 * Report class for assumed matches
 * @author  dcidon
 */
public class AssumedMatchReport extends MultirowReport2 {
    
    public static final String ID = "ID";
    public static final String EUID = "EUID";
    public static final String WEIGHT = "Weight";
    public static final String CREATE_DATE = "CreateDate";
    public static final String CREATE_USER = "CreateUser";
    public static final String SYSTEM_CODE = "SystemCode";
    public static final String LID = "LID";
    public static final String TRANSACTION_NUMBER = "TransactionNumber";
    
    /** Creates a new instance of AssumedMatchReport.
     *
     * @param config  report configuration.
     */
    public AssumedMatchReport(MultirowReportConfig2 config) {
        super(config);
    }
   
    /** Gets the next report row.
     *
     * @return The next report row.
     */
    public AssumedMatchReportRow getNextReportRow() {
        return (AssumedMatchReportRow) getNextRow();
    }
    
}
