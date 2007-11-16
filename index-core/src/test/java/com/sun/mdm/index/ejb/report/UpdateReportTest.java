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
import com.sun.mdm.index.ejb.master.MasterController;
import com.sun.mdm.index.ejb.master.helper.ExecuteMatchHelper;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.util.Constants;
import com.sun.mdm.index.report.UpdateReportConfig;
import com.sun.mdm.index.report.UpdateReport;
import com.sun.mdm.index.report.UpdateReportRow;
import com.sun.mdm.index.ejb.report.ReportGenerator;
import com.sun.mdm.index.objects.EnterpriseObject;
import com.sun.mdm.index.objects.SystemObject;
import com.sun.mdm.index.objects.PersonObject;
import com.sun.mdm.index.objects.SystemObjectPK;

/** Test class for Person searches as defined in the
 * eIndex50.xml file
 * @author Dan Cidon
 */
public class UpdateReportTest extends TestCase {
    
    /** Creates new Tester
     * @see junit.framework.TestCase#
     * @param name required by JUnit
     */
    public UpdateReportTest(String name) {
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
    public void testUpdateReport() throws Exception {
        String args[] = {"fileName=UpdateEnterpriseObject3.txt", "fileType=generic"};
        ExecuteMatchHelper createHelper = new ExecuteMatchHelper();
        createHelper.clearDb();
        createHelper.run(args);
        MasterController controller = MCFactory.getMasterController();
        EnterpriseObject eo = controller.getEnterpriseObject(new SystemObjectPK("SiteA", "0001"));
        SystemObject sysObj = eo.getSystemObject("SiteA", "0001"); 
        PersonObject personNode = (PersonObject) sysObj.getObject();
        
        //Update record
        personNode.setFirstName("CHANGED");
        controller.updateEnterpriseObject(eo);

        //Update other record
        eo = controller.getEnterpriseObject(new SystemObjectPK("SiteB", "0001"));
        sysObj = eo.getSystemObject("SiteB", "0001"); 
        personNode = (PersonObject) sysObj.getObject();
        personNode.setFirstName("CHANGED");
        controller.updateEnterpriseObject(eo);
        
        UpdateReportConfig config = new UpdateReportConfig();
        config.addTransactionField(UpdateReport.EUID, "EUID", 20);
        config.addTransactionField(UpdateReport.SYSTEM_CODE, "System Code", 10);
        config.addTransactionField(UpdateReport.TRANSACTION_NUMBER, "TransactionNumber", 20);
        config.addObjectField("Person.FirstName", "First Name 1", "First Name 2", 20);
        config.addObjectField("Person.LastName", "Last Name 1", "Last Name 2", 20);
        config.addObjectField("Person.DOB", "DOB 1", "DOB Name 2", 20);
        config.addObjectField("Person.Address[@AddressType=H].AddressLine1", "AddressLine1 Before", "AddressLine1 After", 40);
        ReportGenerator repgen = ReportGeneratorFactory.getReportGenerator();
        UpdateReport report = repgen.execUpdateReport(config);
        report.outputDelimitedTextHeader(System.out);
        while (report.hasNext()) {
            UpdateReportRow reportRow = report.getNextReportRow();
            report.outputDelimitedTextRow(System.out, reportRow);
        }
    }
    
    /** Main entry point
     * @param args args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(UpdateReportTest.class));
    }
    
}
