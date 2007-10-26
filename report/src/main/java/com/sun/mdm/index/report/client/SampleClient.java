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
package com.sun.mdm.index.report.client;
import com.sun.mdm.index.ejb.report.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import com.sun.mdm.index.report.AssumedMatchReportConfig;
import com.sun.mdm.index.report.AssumedMatchReport;
import com.sun.mdm.index.report.AssumedMatchReportRow;

/**
 *
 * @author  dcidon
 */
public class SampleClient {
    
    /** Creates a new instance of SampleClient */
    public SampleClient() {
    }
    
    static InitialContext getJndiContext() {
        //For SeeBeyond IS
        String server = "java://localhost:18005"; 
        InitialContext jndiContext = null;
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.stc.is.naming.NamingContextFactory");
            //For SeeBeyond IS
            env.put(Context.PROVIDER_URL, server);
            jndiContext = new InitialContext( env );

	} catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " + e.toString());
            throw new RuntimeException(e.getMessage());
	}
        return jndiContext; 
    }
   
    static BatchReportGenerator getReportGenerator(Context jndiContext) {
        BatchReportGenerator reportGen = null;
        try {
            Object obj = jndiContext.lookup( "ejb/PersonBatchReportGenerator" );
            reportGen = (BatchReportGenerator)obj;
            //BatchReportGeneratorHome home = (BatchReportGeneratorHome)PortableRemoteObject.narrow( obj, BatchReportGeneratorHome.class );
            //reportGen = home.create();
        } catch (Exception ne) {
            ne.printStackTrace();
        } 
        return reportGen;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AssumedMatchReportConfig config = new AssumedMatchReportConfig();
            config.addTransactionField(AssumedMatchReport.EUID, "EUID", 20);
            config.addTransactionField(AssumedMatchReport.WEIGHT, "Weight", 10);
            config.addTransactionField(AssumedMatchReport.SYSTEM_CODE, "System Code", 10);
            config.addTransactionField(AssumedMatchReport.LID, "Local Id", 20);
            config.addObjectField("Person.FirstName", "First Name 1", "First Name 2", 20);
            config.addObjectField("Person.LastName", "Last Name 1", "Last Name 2", 20);
            int pageSize = 500;
            config.setPageSize(new Integer(pageSize));
            
            BatchReportGenerator repgen = getReportGenerator(getJndiContext());

            repgen.execAssumedMatchReport(config);

            AssumedMatchReport report = repgen.getNextAssumedMatchRecords();

            report.outputDelimitedTextHeader(System.out);
            while(true) {
                while (report.hasNext()) {
                    AssumedMatchReportRow reportRow = report.getNextReportRow();
                    report.outputDelimitedTextRow(System.out, reportRow);
                }
                if (!report.hasMore()) {
                    break ;
                }
                report = repgen.getNextAssumedMatchRecords();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
