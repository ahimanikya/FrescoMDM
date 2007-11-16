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
package com.sun.mdm.index.util.ejbproxy;

import java.util.ListResourceBundle;
import com.sun.mdm.index.util.JNDINames;

/**
 * JNDI name mappings for the ProxyInitialContext
 * @author  aegloff
 * @version $Revision: 1.6 $
 */
public class NamingMappings extends ListResourceBundle {

    /** Creates new NamingMappings */
    public NamingMappings() {
    }

    /**
     * Returns the ListResourceBundle contents, which 
     * maps the JNDI name to the home, remote and impl class names
     * @return the ListResourceBundle contents
     */    
    public Object[][] getContents() {
          return CONTENTS;
    }

    /**
     * Map the JNDI name to the home, remote and EJB implementation class names.
     * For using the local interfaces, map the local JNDI name to the localhome,
     * local and EJB implementation class names.
     */
    static final Object[][] CONTENTS = {
        
        {JNDINames.EJB_REF_MASTER, 
            new String[] {"com.sun.mdm.index.ejb.master.MasterControllerRemote", 
                          "com.sun.mdm.index.ejb.master.MasterControllerEJB"}},
        
        {JNDINames.EJB_LOCAL_REF_MASTER, 
            new String[] {"com.sun.mdm.index.ejb.master.MasterControllerLocal", 
                          "com.sun.mdm.index.ejb.master.MasterControllerEJB"}},

        {"com.sun.mdm.index.ejb.master.MasterControllerLocal",
            new String[] {"com.sun.mdm.index.ejb.master.MasterControllerLocal", 
                          "com.sun.mdm.index.ejb.master.MasterControllerEJB"}},

        {JNDINames.EJB_REF_PAGEDATA, 
            new String[] {"com.sun.mdm.index.ejb.page.PageDataRemote", 
                          "com.sun.mdm.index.ejb.page.PageDataEJB"}},
        {JNDINames.EJB_REF_CODELOOKUP, 
            new String[] {"com.sun.mdm.index.ejb.codelookup.CodeLookupRemote", 
                          "com.sun.mdm.index.ejb.codelookup.CodeLookupEJB"}},
        {JNDINames.EJB_REF_REPORTGENERATOR, 
            new String[] {"com.sun.mdm.index.ejb.report.ReportGeneratorRemote", 
                          "com.sun.mdm.index.ejb.report.ReportGeneratorEJB"}},
        {JNDINames.EJB_REF_BATCHREPORTGENERATOR, 
            new String[] {"com.sun.mdm.index.ejb.report.BatchReportGeneratorRemote", 
                          "com.sun.mdm.index.ejb.report.BatchReportGeneratorEJB"}}
    };        
}
