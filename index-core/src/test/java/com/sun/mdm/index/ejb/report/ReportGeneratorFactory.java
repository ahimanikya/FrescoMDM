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

import java.rmi.RemoteException;  
import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.sun.mdm.index.ejb.master.helper.TestConstants;
import com.sun.mdm.index.ejb.master.helper.MCFactory;
import com.sun.mdm.index.util.JNDINames;


/** Factor for ReportGenerator objects and connections
 * @author dcidon
 */
public class ReportGeneratorFactory {
    private static ReportGenerator mReportGenerator;

    /** Get master controller based on server parameter
     * @return Master controller handle
     * @throws RemoteException An error occured
     * @throws NamingException An error occured
     * @throws CreateException An error occured
     */
    public static ReportGenerator getReportGenerator()
                                                throws RemoteException, 
                                                       NamingException, 
                                                       CreateException {
        if (mReportGenerator == null) {
            Context jndiContext = MCFactory.getContext();
            String jndiName = null;

            if (TestConstants.USE_EJB_PROXY) {
                jndiName = JNDINames.EJB_REF_REPORTGENERATOR;
            } else {
                jndiName = "ejb/ReportGenerator";
            }
            mReportGenerator = (ReportGenerator)jndiContext.lookup(jndiName);
        }

        return mReportGenerator;
    }


    /** Test method
     * @param args none
     */    
    public static void main(String[] args) {
        try {
            //test getting connection
            System.out.println("Getting connection");
            getReportGenerator();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
