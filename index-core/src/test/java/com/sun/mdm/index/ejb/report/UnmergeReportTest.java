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
import com.sun.mdm.index.ejb.master.helper.CreateEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.MergeEnterpriseObjectHelper;
import com.sun.mdm.index.ejb.master.helper.UnmergeEnterpriseObjectHelper;
import com.sun.mdm.index.master.MergeResult;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.report.UnmergeReportConfig;
import com.sun.mdm.index.report.UnmergeReport;
import com.sun.mdm.index.report.UnmergeReportRow;
import com.sun.mdm.index.ejb.report.ReportGenerator;
import com.sun.mdm.index.objects.EnterpriseObject;
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class UnmergeReportTest extends TestCase {
    
    /** Creates new Tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UnmergeReportTest(String name) {
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
    public void testUnmergeReport() throws Exception {
        CreateEnterpriseObjectHelper cHelper = new CreateEnterpriseObjectHelper();
        cHelper.clearDb();
        List eoList = cHelper.run(new String[] {"fileName=MergeEnterpriseObject3.txt", "fileType=generic"});
        EnterpriseObject mEntObjSource = (EnterpriseObject) eoList.get(1);
        EnterpriseObject mEntObjDest = (EnterpriseObject) eoList.get(0);
        //Merge the records
        MergeEnterpriseObjectHelper helper = new MergeEnterpriseObjectHelper();
        String sourceEUID = mEntObjSource.getEUID();
        String destEUID = mEntObjDest.getEUID();
        String calcOnly = "false";
        String[] mergeCommand = {"euidSource=" + sourceEUID, "euidDest=" + destEUID, "calcOnly=" + calcOnly}; 
        MergeResult mr = helper.run(mergeCommand);
        
        //Unmerge the records
        UnmergeEnterpriseObjectHelper helper2 = new UnmergeEnterpriseObjectHelper();
        String euid = mEntObjDest.getEUID();
        String[] unmergeCommand = {"euid=" + euid, "calcOnly=" + calcOnly}; 
        mr = helper2.run(unmergeCommand);
        
        UnmergeReportConfig config = new UnmergeReportConfig();
        config.addTransactionField(UnmergeReport.EUID1, "EUID1", 20);
        config.addTransactionField(UnmergeReport.EUID2, "EUID2", 20);
        config.addTransactionField(UnmergeReport.TRANSACTION_NUMBER, "TransactionNumber", 20);
        config.addObjectField("Person.FirstName", "First Name 1", "First Name 2", 20);
        config.addObjectField("Person.LastName", "Last Name 1", "Last Name 2", 20);
        config.addObjectField("Person.Address.AddressLine1", "Address 1", "Address 2", 20);
        ReportGenerator repgen = ReportGeneratorFactory.getReportGenerator();
        UnmergeReport report = repgen.execUnmergeReport(config);
        report.outputDelimitedTextHeader(System.out);
        while (report.hasNext()) {
            UnmergeReportRow reportRow = report.getNextReportRow();
            report.outputDelimitedTextRow(System.out, reportRow);
        }
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(UnmergeReportTest.class));
    }
    
}
