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
 * Report class for Potential Duplicates
 * @author  dcidon
 */
public class PotentialDuplicateReport extends MultirowReport2 {
    
    public static final String ID = "ID";
    public static final String EUID1 = "EUID1";
    public static final String EUID2 = "EUID2";
    public static final String WEIGHT = "Weight";
    public static final String STATUS = "Status";
    public static final String REASON = "Reason";
    public static final String CREATE_DATE = "CreateDate";
    public static final String CREATE_USER = "CreateUser";
    public static final String RESOLVED_DATE = "ResolvedDate";
    public static final String RESOLVED_USER = "ResolvedUser";
    public static final String RESOLVED_COMMENT = "ResolvedComment";
    public static final String SYSTEM_CODE = "SystemCode";
    
    /** Creates a new instance of PotentialDuplicateReport.
     *
     * @param config  report configuration.
     */
    public PotentialDuplicateReport(MultirowReportConfig2 config) {
        super(config);
    }
    
    /** Gets the next report row.
     *
     * @return The next report row.
     */
    public PotentialDuplicateReportRow getNextReportRow() {
        return (PotentialDuplicateReportRow) getNextRow();
    }
    
}
