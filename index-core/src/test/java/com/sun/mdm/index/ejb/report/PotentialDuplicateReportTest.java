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
package com.sun.mdm.index.ejb.report;

import junit.framework.TestCase; 
import junit.framework.TestSuite;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.report.PotentialDuplicateReportConfig;
import com.sun.mdm.index.report.PotentialDuplicateReport;
import com.sun.mdm.index.report.PotentialDuplicateReportRow;
import com.sun.mdm.index.ejb.report.ReportGenerator;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class PotentialDuplicateReportTest extends TestCase {
    
    /** Creates new Tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public PotentialDuplicateReportTest(String name) {
        super(name);
    }
    
    /** Set up the unit test.  In order to create potential duplicates,
     * SameSystemMatch rule must be enabled (or OneExactMatch).
     * @see junit.framework.TestCase#
     * @throws Exception An error occured
     */
    protected void setUp() throws Exception {
    }
    
    /** Test the potential duplicate lookup
     * @throws Exception an error occured
     */
    public void testPotentialDuplicateReport() throws Exception {
        String args[] = {"fileName=LookupPotentialDuplicates1.txt", "fileType=eiEvent"};
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.clearDb();
        createHelper.run(args);
        PotentialDuplicateReportConfig config = new PotentialDuplicateReportConfig();
        config.addTransactionField(PotentialDuplicateReport.EUID1, "EUID 1", 20);
        config.addTransactionField(PotentialDuplicateReport.EUID2, "EUID 2", 20);
        config.addTransactionField(PotentialDuplicateReport.WEIGHT, "Weight", 10);
        config.addTransactionField(PotentialDuplicateReport.SYSTEM_CODE, "System Code", 10);
        config.addObjectField("Person.FirstName", "First Name 1", "First Name 2", 20);
        config.addObjectField("Person.LastName", "Last Name 1", "Last Name 2", 20);
        config.addObjectField("Person.DOB", "DOB 1", "DOB Name 2", 20);
        ReportGenerator repgen = ReportGeneratorFactory.getReportGenerator();
        PotentialDuplicateReport report = repgen.execPotentialDuplicateReport(config);
        report.outputDelimitedTextHeader(System.out);
        int potentialDuplicateRecCount = 0;     // number of potential duplicates 
                                                // to display in a report
        while (report.hasNext()) {
            PotentialDuplicateReportRow reportRow = report.getNextReportRow();
            report.outputDelimitedTextRow(System.out, reportRow);
            potentialDuplicateRecCount++;
        }
        // for QAI 107530
        assertTrue(potentialDuplicateRecCount == 36);
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(PotentialDuplicateReportTest.class));
    }
    
}
