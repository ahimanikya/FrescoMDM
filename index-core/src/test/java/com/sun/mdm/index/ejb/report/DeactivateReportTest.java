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
import com.sun.mdm.index.ejb.master.helper.DeactivateEnterpriseObjectHelper;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.report.DeactivateReportConfig;
import com.sun.mdm.index.report.DeactivateReport;
import com.sun.mdm.index.report.DeactivateReportRow;
import com.sun.mdm.index.ejb.report.ReportGenerator;
import com.sun.mdm.index.objects.EnterpriseObject;
import java.util.List;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class DeactivateReportTest extends TestCase {
    
    /** Creates new Tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public DeactivateReportTest(String name) {
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
    public void testDeactivateReport() throws Exception {

        //Insert Data
        CreateEnterpriseObjectHelper createHelper = new CreateEnterpriseObjectHelper();
        String[] command = {"fileName=DeactivateEnterpriseObject1.txt", "fileType=generic"};
        createHelper.clearDb();
        List eoList = createHelper.run(command);
        EnterpriseObject eo = (EnterpriseObject) eoList.get(0);
        
        //Deactivate EO
        DeactivateEnterpriseObjectHelper helper = new DeactivateEnterpriseObjectHelper();
        command = new String[] {"euid=" + eo.getEUID()}; 
        helper.run(command);

        DeactivateReportConfig config = new DeactivateReportConfig();
        config.addTransactionField(DeactivateReport.EUID, "EUID", 20);
        config.addTransactionField(DeactivateReport.TRANSACTION_NUMBER, "TransactionNumber", 20);
        config.addObjectField("Person.FirstName", "First Name 1", 20);
        config.addObjectField("Person.LastName", "Last Name 1", 20);
        ReportGenerator repgen = ReportGeneratorFactory.getReportGenerator();
        DeactivateReport report = repgen.execDeactivateReport(config);
        report.outputDelimitedTextHeader(System.out);
        while (report.hasNext()) {
            DeactivateReportRow reportRow = report.getNextReportRow();
            report.outputDelimitedTextRow(System.out, reportRow);
        }
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(DeactivateReportTest.class));
    }
    
}
